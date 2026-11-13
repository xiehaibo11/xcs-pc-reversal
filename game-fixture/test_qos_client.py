#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
test_qos_client.py — MobileIMSDK fixture 协议级自测（仅回环，绝不连真实服务器）。

补齐 game-fixture/README 诚实边界 item 3 的运行时验收：用一个合成 MobileIMSDK 客户端
（Python，复用 udp_server.make_protocal 保证字节级同构）逐场景验证服务端忠实实现的：
  1. 登录 type=0→50(QoS=true+fp) + 心跳 1→51 + type=4 ACK 把登录应答移出 QoS 发送队列
  2. QoS 重传：抑制 ACK → 服务端按 QoS4SendDaemon 算法重传同一 fp 至 2 次上限 → messagesLost
  3. ACK 中止重传：首次重传后补 ACK → 服务端 remove(fp)，不再重传/不判丢
  4. C→S type=2 去重：同一 fp 发两遍 → 服务端回两次 ACK 但只处理一次业务（QoS4ReciveDaemon 镜像）
  5. type=52 错误流(301) + AutoReLoginDaemon 全周期：会话中注入 301 → 客户端停心跳重登录 → 新 fp 登录应答 → 心跳恢复
  6. enforce-login：未登录先发心跳 → 服务端回 301 → 登录后心跳正常

每个场景独立启动一个 udp_server.py 子进程（独立临时 runtime 目录、测试端口 7911 避开真机 7901），
跑完即 terminate 清理。重传场景用 --qos-check-interval 1.0 加速观察（SDK 默认 5.0；算法常量
just-now=3s / try=2 保持忠实）。结果矩阵打印并写 runtime/qos_selftest.log，任一失败退出码 1。

用法：python game-fixture/test_qos_client.py [--port 7911] [--only N]
"""
import argparse
import json
import os
import shutil
import socket
import subprocess
import sys
import tempfile
import time

HERE = os.path.dirname(os.path.abspath(__file__))
sys.path.insert(0, HERE)
from udp_server import make_protocal  # 与服务端字节级同构（Gson 字段序/null 省略/自动 fp）

SERVER_PY = os.path.join(HERE, "udp_server.py")
HOST = "127.0.0.1"
DEFAULT_PORT = 7911  # 测试端口，避开真机游戏端硬编码的 7901


class Client:
    """合成 MobileIMSDK 客户端：bind 回环临时端口，向 fixture 服务端收发 Protocal JSON。"""

    def __init__(self, port, user_id="232_2"):
        self.sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        self.sock.bind((HOST, 0))
        self.sock.settimeout(0.5)
        self.server = (HOST, port)
        self.user_id = user_id

    def send(self, ptype, dc, qos=False, fp=None, typeu=-1, to="0", bridge=False):
        payload = make_protocal(ptype, dc, self.user_id, to, qos=qos, fp=fp,
                                typeu=typeu, bridge=bridge)
        self.sock.sendto(payload.encode("utf-8"), self.server)
        return json.loads(payload)

    def login(self, token="test-jwt", extra="232"):
        dc = json.dumps({"loginUserId": self.user_id, "loginToken": token, "extra": extra})
        return self.send(0, dc, qos=False)

    def ack(self, fp, to="0"):
        return self.send(4, fp, qos=False, to=to)

    def heartbeat(self):
        return self.send(1, "{}", qos=False)

    def common(self, dc, fp=None, typeu=-1, qos=True):
        return self.send(2, dc, qos=qos, fp=fp, typeu=typeu)

    def recv(self, timeout=0.5):
        self.sock.settimeout(timeout)
        try:
            data, _ = self.sock.recvfrom(65535)
        except socket.timeout:
            return None
        try:
            return json.loads(data.decode("utf-8"))
        except Exception:
            return {"_raw": data.hex()}

    def wait_type(self, ptype, timeout=3.0):
        end = time.time() + timeout
        while time.time() < end:
            p = self.recv(timeout=min(0.5, max(0.05, end - time.time())))
            if p is not None and p.get("type") == ptype:
                return p
        return None

    def close(self):
        self.sock.close()


class Server:
    """按场景 flags 启动 udp_server.py 子进程，独立临时 runtime，等待 listening 后就绪。"""

    def __init__(self, flags, port, tmp=None):
        self.tmp = tmp or tempfile.mkdtemp(prefix="qostest_")
        self.port = port
        self.log_path = os.path.join(self.tmp, "udp.log")
        cmd = [sys.executable, SERVER_PY, "--host", HOST, "--port", str(port),
               "--runtime-dir", self.tmp] + flags
        self.proc = subprocess.Popen(cmd, stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)
        ready = False
        for _ in range(50):
            if os.path.exists(self.log_path):
                if "listening on" in self.log():
                    ready = True
                    break
            if self.proc.poll() is not None:
                break
            time.sleep(0.1)
        if not ready:
            raise RuntimeError("udp_server 未就绪 (port=%d, flags=%s)" % (port, flags))

    def log(self):
        try:
            with open(self.log_path, encoding="utf-8") as f:
                return f.read()
        except OSError:
            return ""

    def stop(self):
        try:
            self.proc.terminate()
            self.proc.wait(timeout=3)
        except Exception:
            try:
                self.proc.kill()
            except Exception:
                pass
        shutil.rmtree(self.tmp, ignore_errors=True)


def _login_and_ack(c):
    """登录并 ACK 登录应答，返回 (login_reply, fp)。"""
    c.login()
    reply = c.wait_type(50, timeout=3.0)
    fp = reply.get("fp") if reply else None
    if fp:
        c.ack(fp)
    return reply, fp


# ---------------- 场景 ----------------

def scenario_1_login_heartbeat(port):
    srv = Server([], port)
    c = Client(port)
    try:
        reply, fp = _login_and_ack(c)
        ok_login = bool(reply and reply.get("type") == 50
                        and json.loads(reply["dataContent"]).get("code") == 0
                        and reply.get("QoS") is True and fp)
        c.heartbeat()
        hb = c.wait_type(51, timeout=3.0)
        ok_hb = bool(hb and hb.get("type") == 51 and hb.get("dataContent") == "{}")
        time.sleep(0.4)
        ok_ack = "messagesBeReceived type=50" in srv.log()
        passed = ok_login and ok_hb and ok_ack
        return ("1 登录50(QoS+fp)+心跳51+ACK移队列", passed,
                "login50.QoS=%s fp=%s code0=%s | hb51=%s | ackRemoved=%s"
                % (reply.get("QoS") if reply else None, bool(fp), ok_login, ok_hb, ok_ack))
    finally:
        c.close(); srv.stop()


def scenario_2_retransmit_lost(port):
    # 加速巡检 1s（SDK 默认 5s）；just-now=3s/try=2 忠实 → 首发+2 重传后判丢
    srv = Server(["--push-tile-round", "--push-delay", "1", "--qos-check-interval", "1.0"], port)
    c = Client(port)
    try:
        _login_and_ack(c)  # 登录 ACK 触发 push 调度
        frames = []
        end = time.time() + 9.0
        while time.time() < end:
            p = c.recv(timeout=0.5)
            if p is not None and p.get("type") == 2:
                frames.append(p)  # 故意不 ACK push，观察重传
        fps = {f.get("fp") for f in frames}
        typeu_ok = all(f.get("typeu") == 1 for f in frames)
        log = srv.log()
        ok_lost = "messagesLost" in log and "type=2" in log
        # 首发 + 2 次重传 = 3 帧，同一 fp
        passed = len(frames) == 3 and len(fps) == 1 and typeu_ok and ok_lost
        return ("2 QoS重传至上限→messagesLost", passed,
                "type2帧数=%d(期望3) 唯一fp=%d typeu1=%s lost日志=%s"
                % (len(frames), len(fps), typeu_ok, ok_lost))
    finally:
        c.close(); srv.stop()


def scenario_3_ack_stops_retransmit(port):
    srv = Server(["--push-tile-round", "--push-delay", "1", "--qos-check-interval", "1.0"], port)
    c = Client(port)
    try:
        _login_and_ack(c)
        frames = []
        acked = False
        end = time.time() + 9.0
        while time.time() < end:
            p = c.recv(timeout=0.5)
            if p is not None and p.get("type") == 2:
                frames.append(p)
                if len(frames) == 2 and not acked:  # 首发+首次重传后补 ACK
                    c.ack(p.get("fp"))
                    acked = True
        log = srv.log()
        ok_recv = "messagesBeReceived type=2" in log
        ok_no_lost = "messagesLost" not in log
        # 补 ACK 后应停止：首发 + 1 重传 = 2 帧
        passed = len(frames) == 2 and acked and ok_recv and ok_no_lost
        return ("3 补ACK中止重传(不判丢)", passed,
                "type2帧数=%d(期望2) 已ACK=%s beReceived=%s 无lost=%s"
                % (len(frames), acked, ok_recv, ok_no_lost))
    finally:
        c.close(); srv.stop()


def scenario_4_cs_dedup_echo(port):
    srv = Server(["--echo-common", "--qos-check-interval", "1.0"], port)
    c = Client(port)
    try:
        _login_and_ack(c)
        fp = "dedup-fp-0001"
        c.common("hello-business", fp=fp, typeu=5, qos=True)
        time.sleep(0.2)
        c.common("hello-business", fp=fp, typeu=5, qos=True)  # 同 fp 重复
        acks, echoes = [], []
        end = time.time() + 3.0
        while time.time() < end:
            p = c.recv(timeout=0.5)
            if p is None:
                continue
            if p.get("type") == 4 and p.get("dataContent") == fp:
                acks.append(p)
            elif p.get("type") == 2:
                echoes.append(p)
                if p.get("QoS") and p.get("fp"):
                    c.ack(p.get("fp"))  # ACK 回声，避免其重传干扰计数
        log = srv.log()
        ok_dedup = "重复包(去重命中)" in log
        # 两次 ACK（首发+重复都回 ACK），但只处理一次业务 → 只 1 个回声
        passed = len(acks) == 2 and len(echoes) == 1 and ok_dedup
        return ("4 C→S type=2同fp去重(2ACK/1业务)", passed,
                "ACK数=%d(期望2) 回声数=%d(期望1) 去重日志=%s"
                % (len(acks), len(echoes), ok_dedup))
    finally:
        c.close(); srv.stop()


def scenario_5_error_relogin(port):
    tmp = tempfile.mkdtemp(prefix="qostest_err_")
    trigger = os.path.join(tmp, "err.trigger")
    srv = Server(["--error-trigger-file", trigger, "--error-code", "301"], port, tmp=tmp)
    c = Client(port)
    try:
        reply1, fp1 = _login_and_ack(c)
        c.heartbeat()
        hb1 = c.wait_type(51, timeout=3.0)
        # 注入 301：写错误触发文件（路径与服务端 --error-trigger-file 实参一致）
        with open(trigger, "w") as f:
            f.write("go")
        err = c.wait_type(52, timeout=4.0)
        ok_err = bool(err and json.loads(err["dataContent"]).get("errorCode") == 301)
        # 模拟 AutoReLoginDaemon：停心跳，重发登录
        reply2, fp2 = _login_and_ack(c)
        ok_relogin = bool(reply2 and json.loads(reply2["dataContent"]).get("code") == 0
                          and fp2 and fp2 != fp1)
        c.heartbeat()
        hb2 = c.wait_type(51, timeout=3.0)
        log = srv.log()
        ok_log = "重登录" in log
        passed = bool(hb1) and ok_err and ok_relogin and bool(hb2) and ok_log
        return ("5 type52(301)+重登录全周期", passed,
                "hb1=%s err301=%s 重登新fp=%s hb2=%s 重登日志=%s"
                % (bool(hb1), ok_err, ok_relogin, bool(hb2), ok_log))
    finally:
        c.close(); srv.stop()


def scenario_6_enforce_login(port):
    srv = Server(["--enforce-login"], port)
    c = Client(port)
    try:
        c.heartbeat()  # 未登录先发心跳
        err = c.wait_type(52, timeout=3.0)
        ok_301 = bool(err and json.loads(err["dataContent"]).get("errorCode") == 301)
        reply, fp = _login_and_ack(c)
        c.heartbeat()
        hb = c.wait_type(51, timeout=3.0)
        passed = ok_301 and bool(reply) and bool(hb)
        return ("6 enforce-login未登录心跳→301", passed,
                "未登录301=%s 登录后50=%s 心跳51=%s" % (ok_301, bool(reply), bool(hb)))
    finally:
        c.close(); srv.stop()


SCENARIOS = [
    scenario_1_login_heartbeat,
    scenario_2_retransmit_lost,
    scenario_3_ack_stops_retransmit,
    scenario_4_cs_dedup_echo,
    scenario_5_error_relogin,
    scenario_6_enforce_login,
]


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--port", type=int, default=DEFAULT_PORT)
    ap.add_argument("--only", type=int, default=None, help="只跑第 N 个场景(1-based)")
    args = ap.parse_args()

    runtime = os.path.join(HERE, "runtime")
    os.makedirs(runtime, exist_ok=True)
    log_path = os.path.join(runtime, "qos_selftest.log")
    logf = open(log_path, "w", encoding="utf-8")

    def out(m):
        print(m, flush=True)
        logf.write(m + "\n")
        logf.flush()

    out("=== MobileIMSDK fixture QoS 协议级自测 (port=%d, 仅回环) ===" % args.port)
    results = []
    for idx, fn in enumerate(SCENARIOS, 1):
        if args.only and idx != args.only:
            continue
        t0 = time.time()
        try:
            name, passed, detail = fn(args.port)
        except Exception as e:
            name, passed, detail = ("场景%d 异常" % idx, False, repr(e))
        dt = time.time() - t0
        results.append(passed)
        out("[%s] %s (%.1fs) | %s" % ("PASS" if passed else "FAIL", name, dt, detail))
    npass = sum(1 for r in results if r)
    out("=== 结果: %d/%d PASS ===" % (npass, len(results)))
    logf.close()
    sys.exit(0 if npass == len(results) and results else 1)


if __name__ == "__main__":
    main()