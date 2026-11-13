#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
udp_server.py — MobileIMSDK UDP 7901 fixture 服务端（本地验收用，仅回环）

协议逆向结论（CFR 反编译 game-jars/MobileIMSDK4j.jar + MobileIMSDKServerX_META.jar）：
  - UDP 数据报载荷 = Protocal 对象的 Gson JSON 的 UTF-8 字节，无帧头/长度前缀
    （Protocal.toBytes() = CharsetHelper.getBytes(toGsonString())）。
  - Protocal 字段（Gson 按声明序输出，null 省略）: bridge:boolean, type:int,
    dataContent:String(可嵌套 JSON 字符串), from:String, to:String, fp:String(QoS 指纹/uuid),
    QoS:boolean, typeu:int。retryCount 是 transient —— 不上线，重传计数仅本地维护。
    fp 生成规则（7 参构造）: fp = QoS && fingerPrint==null ? UUID.randomUUID() : fingerPrint。
  - 方向常量（ProtocalType）:
      C→S 登录      type=0  dataContent={"loginUserId":"..","loginToken":"..","extra":".."}  from=<userId> to="0"
      C→S 心跳      type=1  dataContent={}   (PKeepAlive 空对象)  from=<userId> to="0"
      C→S 普通消息   type=2  dataContent=业务字符串  (QoS=true 时带 fp，服务端去重并回 type=4 ACK)
      C→S 登出      type=3  dataContent=null
      C→S QoS 应答   type=4  dataContent=<被确认包的 fp>  (createRecivedBack: from=原 to, to=原 from)
      S→C 登录应答   type=50 dataContent={"code":0}  from="0" to=<userId>  QoS=true fp=<uuid>
                    （createPLoginInfoResponse 恒 QoS=true+自动 fp；code=0 成功→客户端置 loginHasInit、
                     启动 KeepAliveDaemon(3s)+QoS4Send/ReciveDaemon，并因 QoS=true 回 type=4 ACK；
                     code!=0 时客户端不发 ACK 且停止接收线程）
      S→C 心跳应答   type=51 dataContent={} (PKeepAliveResponse 空对象) from="0" to=<userId> QoS=false
      S→C 业务推送   type=2 dataContent=<业务字符串> typeu=<业务码> from="0" to=<userId>
                    （客户端 LocalUDPDataReciever$MessageHandler case 2 →
                     ChatTransDataEvent.onTransBuffer(fp, from, dataContent, typeu)；
                     游戏端 ChatTransDataEventImpl.onTransBuffer 只看 typeu==1 → 调
                     MainGUI.jiance() 触发 HTTP /api/yiZhuo/get 拉局，dataContent 内容被忽略。
                     QoS=true 时客户端按指纹去重并回 type=4 ACK）
      S→C 错误      type=52 dataContent={"errorCode":..,"errorMsg":".."} from="0" to=<userId> QoS=false
                    （errorCode=301 RESPONSE_FOR_UNLOGIN → 客户端 setLoginHasInit(false)、
                     KeepAliveDaemon.stop()、AutoReLoginDaemon.start(false) 每 2s 重登录）
  - 客户端 KeepAliveDaemon: 发送间隔 3s（KEEP_ALIVE_INTERVAL=3000），10s 无应答判掉线
    （NETWORK_CONNECTION_TIME_OUT=10000）→ networkConnectionLostObserver → AutoReLoginDaemon.start(true)。
  - 客户端 AutoReLoginDaemon: AUTO_RE$LOGIN_INTERVAL=2000，关本地 socket 后重发 type=0 登录。

服务端 QoS 机制（本 fixture 镜像客户端 QoS4SendDaemon / QoS4ReciveDaemon，算法逐常量对齐）：
  - QoS4SendDaemon（发送方重传队列）: CHECH_INTERVAL=5000(5s 巡检), MESSAGES_JUST$NOW_TIME=3000
    (距首次发送<=3s 视为"刚刚"不重传), QOS_TRY_COUNT=2(最多重传 2 次)。put(p) 要求 fp!=null && QoS。
    巡检: retryCount>=2 → messagesLost(克隆)+remove；否则 delta<=3s 跳过；否则重传同一 fp 包并
    retryCount++（注意：重传不刷新首发时间戳，故 5s/10s 各重传一次，15s 判丢）。收到 type=4 ACK
    (dataContent=fp) → remove(fp)（messagesBeReceived）。本 fixture 对所有 QoS=true 外发包
    （登录应答 type=50、业务推送 type=2）入队跟踪。
  - QoS4ReciveDaemon（接收方去重）: CHECH_INTERVAL=300000(5min 清理), MESSAGES_VALID_TIME=600000
    (10min 有效)。recievedMessages<fp,时间戳>。重复 fp → 更新时间戳+重发 ACK，但不再处理业务
    （客户端 MessageHandler 对重复 QoS 包 sendRecievedBack 后 return）。本 fixture 对 C→S QoS 包同构去重。

本服务端行为：bind 127.0.0.1:7901（冲突则 127.0.0.3/127.0.0.5，选中的 IP 写入
runtime/udp_ip.txt 供 game_server.py 的 User.server_ip 使用）；收到 type=0 回 50(code=0/1)，
收到 type=1 回 51，收到 type=2(C→S) 去重+回 ACK，收到 type=3 记录登出，收到 type=4 从 QoS 队列移除。
所有收发帧记录 hex+JSON 到 runtime/udp.log。绝不连接真实服务器 8.137.196.28。

用法：python udp_server.py [--host 127.0.0.1] [--port 7901] [--reject-login]
                           [--push-tile-round] [--push-delay 3] [--trigger-file PATH]
                           [--echo-common] [--enforce-login]
                           [--error-code 301] [--error-trigger-file PATH]
                           [--qos-check-interval 5.0] [--runtime-dir DIR]
"""
import argparse
import json
import os
import socket
import sys
import time
import uuid

RUNTIME_DIR = os.path.join(os.path.dirname(os.path.abspath(__file__)), "runtime")

# —— QoS4SendDaemon 常量（逐值对齐反编译）——
QOS_CHECK_INTERVAL = 5.0        # CHECH_INTERVAL = 5000ms
QOS_JUST_NOW_TIME = 3.0         # MESSAGES_JUST$NOW_TIME = 3000ms
QOS_TRY_COUNT = 2               # QOS_TRY_COUNT = 2
# —— QoS4ReciveDaemon 常量 ——
QOS_RECV_CHECK_INTERVAL = 300.0  # CHECH_INTERVAL = 300000ms
QOS_RECV_VALID_TIME = 600.0      # MESSAGES_VALID_TIME = 600000ms


def make_protocal(ptype, data_content, src, dst, qos=False, fp=None, typeu=-1, bridge=False):
    """按 Protocal 字段构造与 Gson 输出同构的 JSON。

    字段序=类声明序 bridge,type,dataContent,from,to,fp,QoS,typeu；null 省略（fp=None 不出现）。
    fp 生成规则与 7 参构造一致：QoS=true 且 fp=None → 自动 UUID；否则用传入 fp。
    客户端按名解析，顺序无关，但此处保持与真服务端字节级同构以便证据比对。
    """
    if qos and fp is None:
        fp = str(uuid.uuid4())
    p = {"bridge": bridge, "type": ptype, "dataContent": data_content,
         "from": src, "to": dst}
    if fp is not None:
        p["fp"] = fp
    p["QoS"] = qos
    p["typeu"] = typeu
    return json.dumps(p, ensure_ascii=False, separators=(",", ":"))


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--host", default=None)
    ap.add_argument("--port", type=int, default=7901)
    ap.add_argument("--reject-login", action="store_true",
                    help="登录应答回 code=1（模拟 IM 登录失败）——用于验证客户端的"
                         " yiZhuo/get HTTP 轮询兜底分支（MainController.onLoginSucessObserver）")
    ap.add_argument("--push-tile-round", action="store_true",
                    help="登录成功（收到客户端 type=4 登录ACK）后推送 type=2/typeu=1 业务帧，"
                         "触发客户端 ChatTransDataEventImpl.onTransBuffer → jiance() → "
                         "HTTP /api/yiZhuo/get 拉局")
    ap.add_argument("--push-delay", type=float, default=3.0,
                    help="收到登录 ACK 后延迟多少秒推 type=2 帧（默认 3）")
    ap.add_argument("--trigger-file", default=None,
                    help="备选手动触发：该文件一旦存在即推 type=2 帧（推后删除）")
    ap.add_argument("--echo-common", action="store_true",
                    help="收到 C→S type=2 普通消息后，把同一 dataContent 以 S→C type=2(QoS=true) 回推"
                         "（单客户端 fixture 无其它在线用户可路由，用回声验证双向 type=2 业务通道）")
    ap.add_argument("--enforce-login", action="store_true",
                    help="对未登录会话发来的 type=1/2 数据回 type=52 errorCode=301（RESPONSE_FOR_UNLOGIN），"
                         "镜像真服务端'尚未登录'拒绝；默认关闭以保持既有验收路径不变")
    ap.add_argument("--error-code", type=int, default=301,
                    help="错误触发文件命中时下发的 errorCode（默认 301 未登录）")
    ap.add_argument("--error-trigger-file", default=None,
                    help="该文件一旦存在即向当前客户端下发 type=52 错误帧（发后删除），用于验证错误流+重登录")
    ap.add_argument("--dup-push", type=int, default=0,
                    help="type=2 推帧后，用同一 fp 再补发 N 次（间隔 2s）——验证客户端 QoS 去重")
    ap.add_argument("--qos-check-interval", type=float, default=QOS_CHECK_INTERVAL,
                    help="QoS 发送队列巡检间隔秒（默认 5.0=SDK CHECH_INTERVAL；测试可调小以加速观察重传）")
    ap.add_argument("--runtime-dir", default=RUNTIME_DIR)
    args = ap.parse_args()

    os.makedirs(args.runtime_dir, exist_ok=True)
    log_path = os.path.join(args.runtime_dir, "udp.log")
    log = open(log_path, "a", encoding="utf-8")

    def w(text):
        stamp = time.strftime("%Y-%m-%d %H:%M:%S")
        log.write("[%s] %s\n" % (stamp, text))
        log.flush()
        print(text, flush=True)

    candidates = [args.host] if args.host else ["127.0.0.1", "127.0.0.3", "127.0.0.5"]
    sock = None
    chosen = None
    for host in candidates:
        try:
            sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
            sock.bind((host, args.port))
            sock.settimeout(1.0)
            chosen = host
            break
        except OSError as e:
            print("[udp_server] bind %s:%d failed: %s" % (host, args.port, e), flush=True)
            sock.close()
    if sock is None:
        print("[udp_server] FATAL: 所有候选地址绑定失败", flush=True)
        sys.exit(1)

    with open(os.path.join(args.runtime_dir, "udp_ip.txt"), "w", encoding="utf-8") as f:
        f.write(chosen)

    w("[udp_server] MobileIMSDK fixture listening on udp://%s:%d (log: %s)" % (chosen, args.port, log_path))
    w("[udp_server] QoS send: check=%.1fs just-now=%.1fs try=%d | recv dedup valid=%.0fs | enforce-login=%s echo-common=%s"
      % (args.qos_check_interval, QOS_JUST_NOW_TIME, QOS_TRY_COUNT, QOS_RECV_VALID_TIME,
         args.enforce_login, args.echo_common))

    stats = {"login": 0, "keepalive": 0, "logout": 0, "ack": 0, "other": 0, "push": 0,
             "retransmit": 0, "lost": 0, "common_cs": 0, "ack_sent": 0, "error_sent": 0, "relogin": 0}
    client_state = {"addr": None, "user_id": None, "push_due_at": None, "pushed": False,
                    "logged_in": False, "login_count": 0}

    # —— 服务端 QoS 发送队列（镜像 QoS4SendDaemon.sentMessages + sendMessagesTimestamp）——
    # fp -> {"bytes":原始字节, "addr":目标, "ts":首发时间戳, "retry":重传次数, "type":..., "desc":...}
    qos_sent = {}
    # —— 服务端 QoS 接收去重表（镜像 QoS4ReciveDaemon.recievedMessages）—— fp -> 时间戳
    qos_received = {}
    last_qos_send_check = time.time()
    last_qos_recv_check = time.time()

    def send_qos(payload_str, addr, ptype, desc):
        """外发一个 QoS=true 包并入重传队列（镜像 LocalUDPDataSender.sendCommonData → QoS4SendDaemon.put）。"""
        out = payload_str.encode("utf-8")
        sock.sendto(out, addr)
        p = json.loads(payload_str)
        fp = p.get("fp")
        if fp:
            qos_sent[fp] = {"bytes": out, "addr": addr, "ts": time.time(),
                            "retry": 0, "type": ptype, "desc": desc}
        return fp

    def send_recieved_back(orig, addr):
        """对收到的 QoS 包回 type=4 ACK（镜像 ProtocalFactory.createRecivedBack：from=原 to, to=原 from, dataContent=fp）。"""
        fp = orig.get("fp")
        if not fp:
            w("   收到 QoS 包但 fp=null，无法发 ACK（镜像客户端 sendRecievedBack 的告警分支）")
            return
        ack = make_protocal(4, fp, orig.get("to") or "0", orig.get("from") or "0",
                            qos=False, bridge=orig.get("bridge", False))
        sock.sendto(ack.encode("utf-8"), addr)
        stats["ack_sent"] += 1
        w("TX %s:%d type=4 ACK dataContent=%s (回显指纹→对端 QoS4SendDaemon.remove)" % (
            addr[0], addr[1], fp[:13]))

    def send_error(code, msg, addr, user_id):
        """下发 type=52 错误帧（镜像 ProtocalFactory.createPErrorResponse，QoS=false）。"""
        dc = json.dumps({"errorCode": code, "errorMsg": msg}, ensure_ascii=False)
        payload = make_protocal(52, dc, "0", user_id or "0", qos=False)
        sock.sendto(payload.encode("utf-8"), addr)
        stats["error_sent"] += 1
        w("TX %s:%d type=52 errorCode=%d errorMsg=%s (错误流%s)" % (
            addr[0], addr[1], code, msg,
            "→客户端停心跳+AutoReLogin" if code == 301 else ""))

    def qos_send_check():
        """镜像 QoS4SendDaemon.run()：retry>=2 判丢；delta<=3s 跳过；否则重传+retry++（不刷新首发时间戳）。"""
        now = time.time()
        for fp in list(qos_sent.keys()):
            e = qos_sent[fp]
            if e["retry"] >= QOS_TRY_COUNT:
                del qos_sent[fp]
                stats["lost"] += 1
                w("【QoS发送】fp=%s 重传次数已达%d(最多%d)上限，判定为丢包 messagesLost type=%d desc=%s" % (
                    fp[:13], e["retry"], QOS_TRY_COUNT, e["type"], e["desc"]))
                continue
            delta = now - e["ts"]
            if delta <= QOS_JUST_NOW_TIME:
                continue
            sock.sendto(e["bytes"], e["addr"])
            e["retry"] += 1
            stats["retransmit"] += 1
            w("【QoS发送】fp=%s 未收到ACK，重传#%d type=%d desc=%s (距首发%.1fs>%.1fs)" % (
                fp[:13], e["retry"], e["type"], e["desc"], delta, QOS_JUST_NOW_TIME))

    def qos_recv_check():
        """镜像 QoS4ReciveDaemon.run()：清理生存超过 MESSAGES_VALID_TIME 的去重指纹。"""
        now = time.time()
        for fp in list(qos_received.keys()):
            if now - qos_received[fp] > QOS_RECV_VALID_TIME:
                del qos_received[fp]

    def push_type2_typeu1():
        """S→C 推 type=2 业务帧（typeu=1 → 客户端 jiance()），经 QoS 发送队列跟踪重传。"""
        if client_state["pushed"] or client_state["addr"] is None:
            return False
        dc = json.dumps({"action": "jiance", "zhuo_id": 1}, ensure_ascii=False)
        payload = make_protocal(2, dc, "0", client_state["user_id"] or "0",
                                qos=True, typeu=1)
        fp = send_qos(payload, client_state["addr"], 2, "jiance-push")
        stats["push"] += 1
        client_state["pushed"] = True
        client_state["push_due_at"] = None
        out = payload.encode("utf-8")
        w("TX %s:%d type=2 typeu=1 to=%s fp=%s len=%d hex=%s (业务推送→jiance #%d, 已入QoS队列)" % (
            client_state["addr"][0], client_state["addr"][1], client_state["user_id"],
            fp[:13], len(out), out[:96].hex().upper(), stats["push"]))
        for k in range(args.dup_push):
            time.sleep(2)
            sock.sendto(out, client_state["addr"])
            w("TX [dup-push %d/%d] 同 fp=%s 原样重发至客户端（验证 QoS 去重）" % (k + 1, args.dup_push, fp[:13]))
        return True

    try:
        while True:
            now = time.time()
            # —— type=2 推送调度：到点即推；备选 trigger 文件触发 ——
            due = client_state["push_due_at"]
            if due is not None and now >= due:
                push_type2_typeu1()
            if args.trigger_file:
                try:
                    if os.path.exists(args.trigger_file):
                        os.remove(args.trigger_file)
                        w("[udp_server] trigger file seen -> push type=2")
                        client_state["push_due_at"] = time.time()
                except OSError:
                    pass
            # —— type=52 错误触发文件 ——
            if args.error_trigger_file:
                try:
                    if os.path.exists(args.error_trigger_file):
                        os.remove(args.error_trigger_file)
                        if client_state["addr"] is not None:
                            w("[udp_server] error trigger file seen -> send type=52 code=%d" % args.error_code)
                            send_error(args.error_code, "fixture injected error",
                                       client_state["addr"], client_state["user_id"])
                        else:
                            w("[udp_server] error trigger seen but no client addr yet")
                except OSError:
                    pass
            # —— QoS 周期巡检 ——
            if now - last_qos_send_check >= args.qos_check_interval:
                last_qos_send_check = now
                qos_send_check()
            if now - last_qos_recv_check >= QOS_RECV_CHECK_INTERVAL:
                last_qos_recv_check = now
                qos_recv_check()

            try:
                data, addr = sock.recvfrom(65535)
            except socket.timeout:
                continue
            hex_preview = data[:96].hex().upper()
            try:
                p = json.loads(data.decode("utf-8"))
            except Exception as e:
                w("RX %s:%d len=%d hex=%s PARSE-FAIL %s" % (addr[0], addr[1], len(data), hex_preview, e))
                continue
            ptype = p.get("type")
            dc = p.get("dataContent")
            w("RX %s:%d type=%s from=%s to=%s QoS=%s fp=%s dataContent=%s | hex=%s" % (
                addr[0], addr[1], ptype, p.get("from"), p.get("to"),
                p.get("QoS"), p.get("fp"), dc, hex_preview))

            if ptype == 0:  # 登录（含 AutoReLoginDaemon 重登录，幂等处理）
                stats["login"] += 1
                if client_state["logged_in"]:
                    stats["relogin"] += 1
                try:
                    info = json.loads(dc) if dc else {}
                except Exception:
                    info = {}
                user_id = info.get("loginUserId") or p.get("from") or "0"
                client_state["login_count"] += 1
                w("   LOGIN#%d loginUserId=%s loginToken=%s extra=%s -> REPLY type=50 code=%d%s" % (
                    client_state["login_count"], info.get("loginUserId"), info.get("loginToken"),
                    info.get("extra"), 1 if args.reject_login else 0,
                    " (重登录)" if stats["relogin"] else ""))
                # createPLoginInfoResponse 恒 QoS=true + 自动 fp → 入 QoS 发送队列
                reply = make_protocal(50, json.dumps({"code": 1 if args.reject_login else 0}),
                                      "0", user_id, qos=True)
                fp = send_qos(reply, addr, 50, "login-reply")
                w("TX %s:%d type=50 dataContent=%s fp=%s (%s, 已入QoS队列)" % (
                    addr[0], addr[1], json.loads(reply)["dataContent"], fp[:13],
                    "DENY-LOGIN" if args.reject_login else "LOGIN-OK"))
                if not args.reject_login:
                    client_state["addr"] = addr
                    client_state["user_id"] = user_id
                    client_state["logged_in"] = True
            elif ptype == 1:  # 心跳
                if args.enforce_login and not client_state["logged_in"]:
                    send_error(301, "尚未登录", addr, p.get("from"))
                    continue
                stats["keepalive"] += 1
                reply = make_protocal(51, "{}", "0", p.get("from") or "0")
                sock.sendto(reply.encode("utf-8"), addr)
                w("TX %s:%d type=51 dataContent={} (心跳应答 #%d)" % (addr[0], addr[1], stats["keepalive"]))
            elif ptype == 2:  # C→S 普通消息业务
                if args.enforce_login and not client_state["logged_in"]:
                    send_error(301, "尚未登录", addr, p.get("from"))
                    continue
                stats["common_cs"] += 1
                fp = p.get("fp")
                if p.get("QoS") and fp:
                    if fp in qos_received:
                        # 重复包：更新时间戳 + 重发 ACK，但不重复处理业务（镜像客户端去重 return）
                        qos_received[fp] = time.time()
                        w("   C→S type=2 fp=%s 重复包(去重命中)，重发ACK，不重复处理业务" % fp[:13])
                        send_recieved_back(p, addr)
                        continue
                    qos_received[fp] = time.time()
                    send_recieved_back(p, addr)
                w("   C→S 普通消息 type=2 typeu=%s from=%s dataContent=%s" % (
                    p.get("typeu"), p.get("from"), dc))
                if args.echo_common:
                    resp = make_protocal(2, dc, "0", p.get("from") or "0",
                                         qos=True, typeu=p.get("typeu", -1))
                    efp = send_qos(resp, addr, 2, "echo-common")
                    w("TX %s:%d type=2 echo fp=%s typeu=%s (回声业务推送, 已入QoS队列)" % (
                        addr[0], addr[1], efp[:13], p.get("typeu")))
            elif ptype == 3:  # 登出
                stats["logout"] += 1
                client_state["logged_in"] = False
                w("   LOGOUT from=%s (会话置未登录)" % p.get("from"))
            elif ptype == 4:  # QoS ACK（客户端确认我们下发的 QoS 包）
                stats["ack"] += 1
                acked_fp = dc
                if acked_fp in qos_sent:
                    e = qos_sent.pop(acked_fp)
                    w("   ACK fp=%s -> 从QoS发送队列移除 messagesBeReceived type=%d retry=%d desc=%s" % (
                        acked_fp[:13], e["type"], e["retry"], e["desc"]))
                else:
                    w("   ACK received for fp=%s (不在发送队列：已移除/重复ACK/非本服务端发)" % (
                        acked_fp[:13] if acked_fp else None))
                if args.push_tile_round and not client_state["pushed"] \
                        and client_state["push_due_at"] is None \
                        and client_state["addr"] is not None:
                    client_state["push_due_at"] = time.time() + args.push_delay
                    w("[udp_server] login ACK seen -> schedule type=2(typeu=1) push in %.1fs"
                      " (to=%s)" % (args.push_delay, client_state["user_id"]))
            else:
                stats["other"] += 1
                w("   OTHER type=%s (无应答)" % ptype)
    except KeyboardInterrupt:
        pass
    finally:
        w("[udp_server] stats=%s" % stats)
        w("[udp_server] QoS队列残留=%d 去重表=%d" % (len(qos_sent), len(qos_received)))
        log.close()


if __name__ == "__main__":
    main()