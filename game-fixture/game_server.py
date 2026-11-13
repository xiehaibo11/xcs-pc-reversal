#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
game_server.py — 游戏端 HTTP fixture（本地验收用，绝不连真实服务器）

端点全部逆向自 game-src/（QueshengDaPai.jar 的 CFR 还原源码）：
  1. POST /api/login/login          LoginController.userLogging()
       请求 Form: ajax=true, deviceid="", lastloginversion, devicetype=3,
                  password, username, loginfield=username, type=username, version
       响应 JSON: {"success":bool, "message":<User 对象 JSON>, "jwt":"..."}
       User 字段逆向自 quesheng/model/User.java；server_ip 即 MobileIMSDK 的 UDP 地址
       (IMClientManager: ConfigEntity.serverIP=user.server_ip, port 固定 7901)。
  2. POST /api/mj/getConfigs        Config.initialize()（processLogin 时异步触发）
       响应: {"success":true, "list":[{id,number,name,url}...]}
       默认回空 list（消费代码对空 list 安全：不加载牌图、init2() 空转）。
  3. POST /api/config/GetLastVersion MainController onShown 的 OTA 检查
       请求: version=dapai64|dapai32；响应: {"success":true,"versionCode":0,"name":"","url":""}
       versionCode=0 < 客户端 param.version_code=1 → 不触发下载。
  4. POST /api/yiZhuo/get           MainController.jiance()/huoqu()（登录后每 10s 轮询）
       响应: {"success":true,"list":[{serial_number,id,mahjong_number,direction,zhuo_id,result}...]}
       result 为字符串数字。默认空 list（客户端跳过渲染）。
  5. POST /api/user/updateSelf      MainController.jihuo() 激活
  6. POST /api/user/updatePassword  ChangePasswordDialog

请求为 okhttp3 FormBody（application/x-www-form-urlencoded），带
Authorization: Bearer <jwt> 头。客户端固定拼 ":8081"，端口不可改。

绑定策略：只绑回环 127.0.0.1/127.0.0.3/127.0.0.5（PC 端 fixture 占 127.0.0.2:8081）。
选中的 IP 写入 game-fixture/runtime/http_ip.txt 供生成 sconfig.config 使用；
同时读取 runtime/udp_ip.txt（udp_server.py 写入）作为登录响应里的 User.server_ip。

用法：
  python game_server.py [--host 127.0.0.1] [--mj-configs] [--yizhuo-sample] [--runtime-dir DIR]
"""
import argparse
import json
import os
import socket
import sys
import time
import uuid
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer
from urllib.parse import parse_qs, urlparse

RUNTIME_DIR = os.path.join(os.path.dirname(os.path.abspath(__file__)), "runtime")

# 登录测试账号（与 tools/MakeSconfig.java 写入 sconfig.config 的 users 保持一致）
FIXTURE_USER = "XCS232"
# 凭据按 AGENTS.md 政策不入库：默认占位符，实际值用环境变量 GF_PASS 注入（见本地 docs/设备登录.md）
FIXTURE_PASS = os.environ.get("GF_PASS", "CHANGE_ME")

# 42 张牌配置（仅 --mj-configs 时返回）。
# number=牌面编码（逆向⑤ §8：万 1-9 / 筒 11-19 / 条 21-29 / 字 31-37 / 花 41-48），
# url 对应 jar 内 /quesheng/view/image 前缀下的真实资源（Config.initialize 会按同下标
# 加载 mjImages，任何一条 url 无效都会导致 NPE→列表截断→编号错位，必须全部有效）。
_SUIT_RANGES = [("wan", 1, 9), ("tong", 11, 19), ("tiao", 21, 29), ("zi", 31, 37), ("hua", 41, 48)]
MJ_CONFIGS = []
for _p, _lo, _hi in _SUIT_RANGES:
    for _n in range(_lo, _hi + 1):
        _local = _n - (_lo - 1)  # 花色内 1..N，对应 sucai/<花色>/<本地号>.png
        MJ_CONFIGS.append({"id": len(MJ_CONFIGS) + 1, "number": _n,
                           "name": "%s%d" % (_p, _local),
                           "url": "/sucai/%s/%d.png" % (_p, _local)})
assert [c["number"] for c in MJ_CONFIGS] == list(range(1, 10)) + list(range(11, 20)) \
       + list(range(21, 30)) + list(range(31, 38)) + list(range(41, 49))


def build_yizhuo_round(seed=20261112, zhuo_id=1):
    """构造真实一局的桌面牌数据（136 张：4 方向 × 34 张，每方向 mahjong_number 0..33）。

    direction: 0=东(底) 1=南(右) 2=西(顶) 3=北(左)（逆向⑤ §5，与 PC 端 1-4 编码不同）。
    result: 万 1-9 / 筒 11-19 / 条 21-29 / 字 31-37（136 张局不含花 41-48）。
    张数依据：displayWeinaPai 在 mapaicengshu==2 时把每方向按 mahjong_number 奇偶
    叠成两层（n/2 列），与 Config.xipai(136) 自产的 34 张/方向 完全同构——这是客户端
    自己生成满墙时的标准分布，不是拍脑袋数字。
    serial_number/id 全局唯一（Config.weina 是 HashSet，Mj 身份=五元组，id 用于新旧局判定）。
    """
    import random
    rnd = random.Random(seed)
    codes = list(range(1, 10)) + list(range(11, 20)) + list(range(21, 30)) + list(range(31, 38))
    # GF_FLOWERS=1 时在东方混入 8 张花牌 41-48——用于实机验证 MjConfig 花牌映射
    # （41-48 → sucai/hua/*.png；标准 136 张局不含花牌，此变体专为图像映射核对）
    flowers = os.environ.get("GF_FLOWERS")
    tiles = []
    sn = 10000
    for direction in range(4):
        dir_codes = codes
        if flowers and direction == 0:
            # 确定化：恰好 8 张花牌 41-48 + 26 张万筒条字（审计 F 证伪"随机抽样恰 8 张"后改为确定性混入）
            dir_codes = list(range(41, 49)) + codes[:26]
        for mahjong_number in range(34):
            sn += 1
            tiles.append({
                "serial_number": sn,
                "id": 5000 + sn,
                "mahjong_number": mahjong_number,
                "direction": direction,
                "zhuo_id": zhuo_id,
                "result": str(rnd.choice(dir_codes)),
            })
    return tiles


# yiZhuo 桌面样例（仅 --yizhuo-sample 时返回；字段逆向自 MainController.jiance/huoqu 消费代码）
YIZHUO_SAMPLE = build_yizhuo_round()


def read_udp_ip(default="127.0.0.1"):
    try:
        with open(os.path.join(RUNTIME_DIR, "udp_ip.txt"), "r", encoding="utf-8") as f:
            v = f.read().strip()
            if v:
                return v
    except OSError:
        pass
    return default


class NoFqdnHTTPServer(ThreadingHTTPServer):
    """HTTPServer.server_bind 会调 socket.getfqdn(host) 做反向 DNS——对 127.0.0.3
    这类回环别名在 DNS 受限环境下会挂起数十秒。这里跳过 fqdn 解析。"""

    def server_bind(self):
        import socketserver
        self.socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        self.socket.bind(self.server_address)
        self.server_address = self.socket.getsockname()
        host, port = self.server_address[:2]
        self.server_name = host   # 不做 getfqdn 反解
        self.server_port = port


class Handler(BaseHTTPRequestHandler):
    server_version = "GameFixture/1.0"
    protocol_version = "HTTP/1.1"

    # ---- 日志 ----
    def log_message(self, fmt, *args):
        self.server.logfile.write("[%s] %s\n" % (time.strftime("%H:%M:%S"), fmt % args))
        self.server.logfile.flush()

    def _log(self, text):
        self.server.logfile.write("[%s] %s\n" % (time.strftime("%H:%M:%S"), text))
        self.server.logfile.flush()
        print(text, flush=True)

    # ---- 响应 ----
    def _json(self, obj, code=200):
        body = json.dumps(obj, ensure_ascii=False).encode("utf-8")
        self.send_response(code)
        self.send_header("Content-Type", "application/json;charset=UTF-8")
        self.send_header("Content-Length", str(len(body)))
        self.end_headers()
        self.wfile.write(body)

    def _form(self):
        length = int(self.headers.get("Content-Length") or 0)
        raw = self.rfile.read(length).decode("utf-8", "replace") if length else ""
        return parse_qs(raw, keep_blank_values=True), raw

    # ---- 路由 ----
    def do_POST(self):
        path = urlparse(self.path).path
        form, raw = self._form()
        auth = self.headers.get("Authorization") or "-"
        params = {k: v[0] for k, v in form.items()}
        self._log("REQ POST %s auth=%s params=%s" % (path, auth, json.dumps(params, ensure_ascii=False)))

        if path == "/api/login/login":
            return self._handle_login(params)
        if path == "/api/mj/getConfigs":
            lst = MJ_CONFIGS if self.server.opts["mj_configs"] else []
            return self._json({"success": True, "list": lst})
        if path == "/api/config/GetLastVersion":
            return self._json({"success": True, "versionCode": 0, "name": "", "url": ""})
        if path == "/api/yiZhuo/get":
            lst = YIZHUO_SAMPLE if self.server.opts["yizhuo_sample"] else []
            return self._json({"success": True, "list": lst})
        if path == "/api/user/updateSelf":
            return self._json({"success": True, "message": "ok"})
        if path == "/api/user/updatePassword":
            return self._json({"success": True, "message": "ok"})

        self._log("RSP 404 unknown path")
        return self._json({"success": False, "message": "fixture: unknown path %s" % path}, code=404)

    def do_GET(self):
        path = urlparse(self.path).path
        self._log("REQ GET %s (客户端全部用 POST Form；GET 一律 404)" % path)
        return self._json({"success": False, "message": "fixture: use POST"}, code=404)

    # ---- 业务 ----
    def _handle_login(self, params):
        username = params.get("username")
        password = params.get("password")
        if not username or not password:
            self._log("RSP login success=false (缺 username/password)")
            return self._json({"success": False, "message": "用户名或密码不能为空"})
        if username != FIXTURE_USER or password != FIXTURE_PASS:
            self._log("RSP login success=false (账号不匹配: %s/%s)" % (username, password))
            return self._json({"success": False, "message": "fixture: 账号或密码错误"})

        # User 字段逆向自 quesheng/model/User.java；server_ip 指向本机 UDP fixture
        im_ip = read_udp_ip()
        user = {
            "user_id": 232,
            "username": username,
            "password": "fixture-not-default",   # 避开 MainController 的"默认密码"弹窗
            "priority": 0,
            "node_id": 0,
            "mobile": "",
            "email": "",
            "type": 0,
            "nick_name": "FixtureUser",
            "avatar": "",
            "onlineflag": 1,
            "devicetype": 3,
            "deviceid": "",
            "bio": "",
            "qq": "", "msn": "", "weixin": "", "nation": "",
            "gender": 1,
            "birth_date": "",
            "real_name": "",
            "salt": "fixturesalt",               # sha256(salt+真实密码) != password → 不弹默认密码提示
            "server_ip": im_ip,                  # MobileIMSDK UDP 目标
            "jihuo_status": 1,                   # 1=已激活 → 主界面隐藏"激活"按钮
        }
        jwt = "fixture-jwt-" + uuid.uuid4().hex[:16]
        self._log("RSP login success=true user_id=%s server_ip=%s jwt=%s" % (user["user_id"], im_ip, jwt))
        self.server.logfile.write("[%s] LOGIN-OK username=%s devicetype=%s version=%s\n" % (
            time.strftime("%H:%M:%S"), username, params.get("devicetype"), params.get("version")))
        self.server.logfile.flush()
        return self._json({"success": True, "message": user, "jwt": jwt})


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--host", default=None, help="绑定 IP；默认依次尝试 127.0.0.1/127.0.0.3/127.0.0.5")
    ap.add_argument("--port", type=int, default=8081, help="默认 8081（客户端硬编码 :8081，勿改）")
    ap.add_argument("--mj-configs", action="store_true", help="/api/mj/getConfigs 返回 42 张牌配置（推断值）")
    ap.add_argument("--yizhuo-sample", action="store_true", help="/api/yiZhuo/get 返回桌面牌样例数据")
    ap.add_argument("--runtime-dir", default=RUNTIME_DIR)
    args = ap.parse_args()

    os.makedirs(args.runtime_dir, exist_ok=True)
    os.makedirs(RUNTIME_DIR, exist_ok=True)

    candidates = [args.host] if args.host else ["127.0.0.1", "127.0.0.3", "127.0.0.5"]
    httpd = None
    chosen = None
    for host in candidates:
        try:
            httpd = NoFqdnHTTPServer((host, args.port), Handler)
            chosen = host
            break
        except Exception as e:
            print("[game_server] bind %s:%d failed: %r" % (host, args.port, e), flush=True)
    if httpd is None:
        print("[game_server] FATAL: 所有候选地址绑定失败", flush=True)
        sys.exit(1)

    httpd.opts = {"mj_configs": args.mj_configs, "yizhuo_sample": args.yizhuo_sample}
    log_path = os.path.join(args.runtime_dir, "requests.log")
    httpd.logfile = open(log_path, "a", encoding="utf-8")

    with open(os.path.join(RUNTIME_DIR, "http_ip.txt"), "w", encoding="utf-8") as f:
        f.write(chosen)

    print("[game_server] HTTP fixture listening on http://%s:%d" % (chosen, args.port), flush=True)
    print("[game_server] endpoints: /api/login/login /api/mj/getConfigs /api/config/GetLastVersion"
          " /api/yiZhuo/get /api/user/updateSelf /api/user/updatePassword", flush=True)
    print("[game_server] accept login: %s/%s ; IM server_ip=%s" % (FIXTURE_USER, FIXTURE_PASS, read_udp_ip()), flush=True)
    print("[game_server] log file: %s" % log_path, flush=True)
    try:
        httpd.serve_forever()
    except KeyboardInterrupt:
        pass


if __name__ == "__main__":
    main()
