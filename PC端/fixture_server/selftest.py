# -*- coding: utf-8 -*-
"""
fixture 服务器端到端自测（无 GUI）。

模拟 XCS PC 客户端全流程：
  登录 -> RSA 换业务地址/dataKey -> WebSocket 登录/心跳/QoS -> 图牌101图片管线
  -> zhuo/add 合法/非法牌局 -> 修改密码（无 /api 前缀）-> requests.log 对账

运行:  python selftest.py
（若 8081 未起会自动拉起 server.py 子进程并在结束时停止；外部已启动的服务器保持不动）
"""

import base64
import io
import json
import math
import os
import random
import subprocess
import sys
import time
import urllib.error
import urllib.request

HERE = os.path.dirname(os.path.abspath(__file__))
# 与 server.py 的 BIND_HOST 一致：127.0.0.2 回环别名（127.0.0.1:8081 被本机
# Docker 容器 aiquant-live-bot 占用，客户端硬编码 :8081，故用回环别名避让）
HOST = os.environ.get("FIXTURE_HOST", "127.0.0.2")
HTTP_BASE = "http://%s:8081" % HOST
IMG_BASE = "http://%s" % HOST
WS_URL = "ws://%s:18889/" % HOST
LOG_PATH = os.path.join(HERE, "requests.log")
ROUNDS_PATH = os.path.join(HERE, "rounds.jsonl")

PASS = 0
FAIL = 0
FAILURES = []


def check(name, cond, detail=""):
    global PASS, FAIL
    if cond:
        PASS += 1
        print("  [PASS] %s %s" % (name, detail))
    else:
        FAIL += 1
        FAILURES.append(name)
        print("  [FAIL] %s %s" % (name, detail))
    return cond


def section(title):
    print("\n=== %s ===" % title)


# ----------------------------------------------------------------------------
# HTTP 帮助
# ----------------------------------------------------------------------------
def http(method, url, body=None, jwt=None, timeout=10):
    req = urllib.request.Request(url, method=method)
    if jwt:
        req.add_header("Authorization", "Bearer " + jwt)
    data = None
    if body is not None:
        data = body if isinstance(body, bytes) else json.dumps(body).encode("utf-8")
        req.add_header("Content-Type", "application/json")
    try:
        with urllib.request.urlopen(req, data=data, timeout=timeout) as r:
            return r.status, json.loads(r.read().decode("utf-8"))
    except urllib.error.HTTPError as e:
        try:
            return e.code, json.loads(e.read().decode("utf-8"))
        except Exception:
            return e.code, {}


def http_text(url, timeout=10):
    with urllib.request.urlopen(url, timeout=timeout) as r:
        return r.read().decode("utf-8")


# ----------------------------------------------------------------------------
# 客户端侧镜像：Global.GetDataBash64（distinguish.Common/Global.cs:67）
#   dataKey 按 '|' 分组、组内 ',' 拆对，Replace(pair[1] -> pair[0])
# ----------------------------------------------------------------------------
def client_decode_datakey(bash64, data_key="A,a"):
    text = bash64
    for group in data_key.split("|"):
        pair = group.split(",")
        if len(pair) >= 2 and pair[1]:
            text = text.replace(pair[1], pair[0])
    return text


# ----------------------------------------------------------------------------
# RSA-2048 e=3（镜像 BouncyCastle CryptionUtils.GetKey，e=3, 2048bit, SPKI DER）
# ----------------------------------------------------------------------------
def is_probable_prime(n, rounds=40):
    small = [2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37]
    for p in small:
        if n % p == 0:
            return n == p
    d, r = n - 1, 0
    while d % 2 == 0:
        d //= 2
        r += 1
    for _ in range(rounds):
        a = random.randrange(2, n - 2)
        x = pow(a, d, n)
        if x in (1, n - 1):
            continue
        composite = True
        for _ in range(r - 1):
            x = pow(x, 2, n)
            if x == n - 1:
                composite = False
                break
        if composite:
            return False
    return True


def gen_prime_3compat(bits):
    """素数且 p % 3 != 1（保证 gcd(e=3, p-1)=1）"""
    while True:
        c = random.getrandbits(bits) | (1 << (bits - 1)) | (1 << (bits - 2)) | 1
        if c % 3 == 1:
            continue
        if is_probable_prime(c):
            return c


def generate_rsa2048_e3():
    from cryptography.hazmat.primitives.asymmetric import rsa

    p = gen_prime_3compat(1024)
    q = gen_prime_3compat(1024)
    n = p * q
    e = 3
    lam = (p - 1) * (q - 1) // math.gcd(p - 1, q - 1)
    d = pow(e, -1, lam)
    # cryptography 50: RSAPrivateNumbers 仅接受位置参数
    # (p, q, d, dmp1, dmq1, iqmp, public_numbers)
    numbers = rsa.RSAPrivateNumbers(p, q, d, d % (p - 1), d % (q - 1),
                                    pow(q, -1, p), rsa.RSAPublicNumbers(e, n))
    priv = numbers.private_key()
    return priv.public_key(), priv


# ----------------------------------------------------------------------------
# 服务器进程管理
# ----------------------------------------------------------------------------
def server_reachable():
    try:
        _, body = http("GET", HTTP_BASE + "/_fixture/ping", timeout=3)
        return body.get("success") is True
    except Exception:
        return False


SERVER_PROC = None
SERVER_LOG = os.path.join(HERE, "selftest_server.log")


def ensure_server():
    global SERVER_PROC
    if server_reachable():
        print("server 已在运行（外部实例，结束时不会停止它）")
        return
    print("server 未运行，自动拉起 server.py 子进程 ...")
    logf = io.open(SERVER_LOG, "w", encoding="utf-8")
    SERVER_PROC = subprocess.Popen(
        [sys.executable, os.path.join(HERE, "server.py")],
        cwd=HERE, stdout=logf, stderr=subprocess.STDOUT)
    deadline = time.time() + 30
    while time.time() < deadline:
        if server_reachable():
            print("server ready.")
            return
        if SERVER_PROC.poll() is not None:
            break
        time.sleep(0.5)
    try:
        logf.close()
    except Exception:
        pass
    sys.exit("server 启动失败，日志见 %s" % SERVER_LOG)


def stop_server_if_spawned():
    global SERVER_PROC
    if SERVER_PROC is not None and SERVER_PROC.poll() is None:
        SERVER_PROC.terminate()
        try:
            SERVER_PROC.wait(timeout=10)
        except Exception:
            SERVER_PROC.kill()
        print("已停止自启动的 server 子进程")


# ----------------------------------------------------------------------------
# 用例
# ----------------------------------------------------------------------------
def test_1_deps():
    section("[1] pip 依赖")
    import websockets
    import cryptography
    print("  websockets=%s cryptography=%s" % (websockets.__version__,
                                              cryptography.__version__))
    check("import websockets+cryptography", True,
          "websockets==%s cryptography==%s" % (websockets.__version__,
                                               cryptography.__version__))


def test_2_login():
    section("[2] 登录 正确/错误账号")
    st, bad = http("POST", HTTP_BASE + "/api/login/login/distinguish?app_name=xcs",
                   body={"username": "hacker", "password": "1", "devicetype": 5})
    check("错误账号 success=false", bad.get("success") is False)
    check("错误账号 带 msg 弹窗文本", bool(bad.get("msg")))

    st, ok = http("POST", HTTP_BASE + "/api/login/login/distinguish?app_name=xcs",
                  body={"username": "XCS232", "password": os.environ.get("PF_PASS", "CHANGE_ME"),
                        "devicetype": 5})
    check("正确账号 success=true", ok.get("success") is True)
    check("jwt 存在", bool(ok.get("jwt")), "jwt=%s" % ok.get("jwt"))
    lu = ok.get("LoginUser") or {}
    check("LoginUser.Timeout==15", lu.get("Timeout") == 15,
          "Timeout=%s" % lu.get("Timeout"))
    check("LoginUser 字段齐",
          all(k in lu for k in ("Id", "Username", "Token", "Login_type", "Exp",
                                "Nbf", "JihuoStatus", "DataSend", "Timeout",
                                "Delay")))
    devs = ok.get("Devices") or []
    check("Devices 共 4 台", len(devs) == 4, "n=%d" % len(devs))
    check("direction=1..4", sorted(d["Direction"] for d in devs) == [1, 2, 3, 4])
    check("id=1..4", sorted(d["Id"] for d in devs) == [1, 2, 3, 4])
    check("mj_count 全 26", all(d["Mj_count"] == 26 for d in devs))
    check("serial_no 可 int.Parse（SaveMj serial_number 是 int）",
          all(str(d["Serial_no"]).isdigit() for d in devs),
          "serials=%s" % [d["Serial_no"] for d in devs])
    check("Device 字段齐（照 Device.cs 属性名）",
          all(k in devs[0] for k in
              ("Id", "UserId", "Direction", "Mj_count", "Serial_no", "Top",
               "Left", "Right", "Bottom", "Brightness", "Motor",
               "Cap_brightness", "Cap_exposure", "Cap_hue", "Cap_saturation",
               "Cap_contrast", "Cap_gain", "Img_rotation", "Img_min_hreshold",
               "Cap_fps", "Beta", "Img_add_num", "Gk_min", "Gk_max",
               "Upload_img_clarity", "Count_type")))
    return ok.get("jwt")


def test_3_server_addr():
    section("[3] RSA-2048 e=3 -> get_server_addr -> 私钥解密")
    pub, priv = generate_rsa2048_e3()
    check("生成 RSA-2048 e=3 密钥对",
          priv.key_size == 2048 and priv.public_key().public_numbers().e == 3)
    spki = base64.b64encode(
        pub.public_bytes(encoding=__import__(
            "cryptography.hazmat.primitives.serialization", fromlist=["Encoding"]).Encoding.DER,
            format=__import__(
            "cryptography.hazmat.primitives.serialization", fromlist=["PublicFormat"]).PublicFormat.SubjectPublicKeyInfo)
    ).decode("ascii")
    st, resp = http("POST", HTTP_BASE + "/api/server/get_server_addr",
                    body={"key": spki, "client_type": 5})
    check("get_server_addr success=true", resp.get("success") is True)
    check("msg 为 base64 密文", bool(resp.get("msg")),
          "len=%d" % len(resp.get("msg") or ""))
    from cryptography.hazmat.primitives.asymmetric import padding
    plain = priv.decrypt(base64.b64decode(resp["msg"]), padding.PKCS1v15())
    arr = json.loads(plain.decode("utf-8"))
    check("解出 [业务IP, dataKey]", arr == [HOST, "A,a"], "arr=%s" % arr)
    return arr


def test_4_ws(jwt):
    section("[4] WS 连接/登录/心跳/QoS")
    from websockets.sync.client import connect

    results = []
    ws = connect(WS_URL, open_timeout=5)
    try:
        ws.send(json.dumps({"types": 6, "data_content": jwt, "from": "",
                            "to": "", "fp": "st-login-0001", "qos": False,
                            "retry_count": 0}))
        f = json.loads(ws.recv(timeout=5))
        check("types=6 -> types=50", f.get("types") == 50, "types=%s" % f.get("types"))
        results.append(f)

        ws.send(json.dumps({"types": 1, "data_content": "", "from": "",
                            "to": "", "fp": "st-keep-0001", "qos": False,
                            "retry_count": 0}))
        f = json.loads(ws.recv(timeout=5))
        check("types=1 -> types=51", f.get("types") == 51, "types=%s" % f.get("types"))
        results.append(f)

        # 服务端业务推送（qos:true + fp），113 灯光状态
        st, resp = http("POST", HTTP_BASE + "/_fixture/push/113",
                        body={"serial": "20250101", "run": 1})
        f = json.loads(ws.recv(timeout=5))
        check("收到 types=2 业务推送", f.get("types") == 2, "types=%s" % f.get("types"))
        check("qos=true", f.get("qos") is True)
        check("from=server to=''", f.get("from") == "server" and f.get("to") == "",
              "from=%s to=%r" % (f.get("from"), f.get("to")))
        fp = f.get("fp") or ""
        import re
        check("fp 为 Guid 风格",
              bool(re.match(r"^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", fp)),
              "fp=%s" % fp)
        dc = json.loads(f.get("data_content") or "{}")
        check("data_content={msg_type,msg_content}",
              dc.get("msg_type") == 113 and dc.get("msg_content") == "20250101|1",
              "dc=%s" % dc)
        results.append(f)

        # 客户端 QoS 回执 types=4（data_content=fp）
        ws.send(json.dumps({"types": 4, "data_content": fp, "from": "",
                            "to": "server", "fp": "st-ack-0001", "qos": False,
                            "retry_count": 0}))
        time.sleep(0.6)
        _, status = http("GET", HTTP_BASE + "/_fixture/status")
        acked_fps = [a["fp"] for a in status.get("qos_acked") or []]
        check("服务端登记 types=4 回执", fp in acked_fps, "acked=%s" % acked_fps[-3:])
        push_rec = [p for p in (status.get("pushes") or []) if p["fp"] == fp]
        check("推送记录标记已 ack", push_rec and push_rec[0].get("acked") is True)

        # 301 未登录码推送（客户端收到会自动重连）
        st, resp = http("POST", HTTP_BASE + "/_fixture/push/301", body={})
        f = json.loads(ws.recv(timeout=5))
        check("推送 types=301（触发客户端重连）", f.get("types") == 301,
              "types=%s" % f.get("types"))
        results.append(f)
    finally:
        ws.close()
    return results


def test_5_pipeline_101(jwt):
    section("[5] 图牌101 管线（图片编码/URL/反替换/JPEG 头）")
    _, info = http("GET", HTTP_BASE + "/_fixture/img")
    canonical = info.get("b64") or ""
    check("原始 b64 不含 'a'（往返不变式）", info.get("has_lower_a") is False)
    check("服务器 JPEG 头 FF D8", info.get("jpeg_head_hex") == "FFD8FFE0",
          "head=%s" % info.get("jpeg_head_hex"))

    # 真实推送一条 101：WS 客户端在线时经 /_fixture/push/101 触发服务端原语
    from websockets.sync.client import connect
    ws = connect(WS_URL, open_timeout=5)
    try:
        ws.send(json.dumps({"types": 6, "data_content": jwt, "from": "",
                            "to": "", "fp": "st-login-0002", "qos": False,
                            "retry_count": 0}))
        json.loads(ws.recv(timeout=5))          # types=50
        serial, mj, ai_val, precision = "20250101", 3, 7, 0.987
        img_path = "img/%s_%d.jpg" % (serial, mj)
        st, resp = http("POST", HTTP_BASE + "/_fixture/push/101",
                        body={"serial_number": serial, "mahjong_number": mj,
                              "ai_val": ai_val, "ai_precision": precision,
                              "img": img_path})
        check("101 推送原语送达 WS 客户端", resp.get("sent", 0) >= 1,
              "resp=%s" % resp)
        frame = json.loads(ws.recv(timeout=5))
        check("帧 types=2 + qos=true + fp", frame.get("types") == 2
              and frame.get("qos") is True and bool(frame.get("fp")))
        dc = json.loads(frame.get("data_content") or "{}")
        msg_content = dc.get("msg_content") or ""
        check("msg_type==101", dc.get("msg_type") == 101)
        obj = json.loads(msg_content)
        check("msg_content 含 ProtocolDataContentImg 全字段",
              sorted(obj.keys()) == ["ai_precision", "ai_val", "img",
                                     "mahjong_number", "serial_number"],
              "keys=%s" % sorted(obj.keys()))
    finally:
        ws.close()

    # 从 :80 拉取 img URL 文本验证下载管线
    text = http_text(IMG_BASE + "/" + obj["img"])
    first_line = text.split("\n")[0]
    check(":80 返回单行文本", len([l for l in text.split("\n") if l.strip()]) == 1,
          "len=%d" % len(first_line))
    check(":80 文本 == encode_datakey(原始b64)",
          first_line == "".join(c.replace("A", "a") for c in canonical))

    decoded = client_decode_datakey(first_line)
    raw = base64.b64decode(decoded)
    check("反替换后 base64 解码成功", len(raw) > 0, "bytes=%d" % len(raw))
    check("JPEG 头 FF D8 校验", raw[:2] == b"\xff\xd8",
          "head=%s" % raw[:4].hex().upper())
    check("JPEG 尾 FF D9", raw[-2:] == b"\xff\xd9")
    check("客户端还原 == 原始 base64（无损往返）", decoded == canonical,
          "len=%d" % len(decoded))
    return msg_content


def build_valid_tiles():
    codes = list(range(1, 10)) + list(range(11, 20)) + list(range(21, 26)) \
        + [31, 32, 33]          # 26 种编码，全部 <40
    assert len(codes) == 26
    serials = {1: 20250101, 2: 20250102, 3: 20250103, 4: 20250104}
    counters = {1: 0, 2: 0, 3: 0, 4: 0}
    tiles = []
    for _rep in range(4):                       # 每种编码恰好 4 张
        for code in codes:
            direction = (len(tiles) % 4) + 1    # 每方向轮流入 -> 各 26 张
            counters[direction] += 1
            tiles.append({
                "mahjong_number": counters[direction],   # 1..26
                "serial_number": serials[direction],
                "direction": direction,
                "result": code,
            })
    assert len(tiles) == 104
    return tiles


def test_6_zhuo_add(jwt):
    section("[6] zhuo/add 合法/非法牌局")
    tiles = build_valid_tiles()
    st, resp = http("POST", HTTP_BASE + "/api/zhuo/add", body=tiles, jwt=jwt)
    check("104 张合法牌局 success=true", resp.get("success") is True,
          "resp=%s" % resp)
    rounds_after = rounds_count()
    check("合法牌局写入 rounds.jsonl", rounds_after >= 1, "lines=%d" % rounds_after)

    bad_tiles = tiles[:-1]                      # 少一张
    st, resp = http("POST", HTTP_BASE + "/api/zhuo/add", body=bad_tiles, jwt=jwt)
    check("103 张 success=false", resp.get("success") is False)
    check("失败带 msg（GUI 错误弹窗读 msg 键）", bool(resp.get("msg")),
          "msg=%s" % (resp.get("msg") or "")[:60])
    check("失败带 code（HttpReq 短路读 code，缺失会崩）",
          "code" in resp and resp.get("code") != 401,
          "code=%s" % resp.get("code"))


def test_7_edit_pwd(jwt):
    section("[7] account/edit/pwd（无 /api 前缀）")
    st, resp = http("POST", HTTP_BASE + "/account/edit/pwd",
                    body={"old_password": os.environ.get("PF_PASS", "CHANGE_ME"), "password": "654321"},
                    jwt=jwt)
    check("POST /account/edit/pwd 命中（success=true）",
          resp.get("success") is True, "resp=%s" % resp)
    st, resp = http("POST", HTTP_BASE + "/account/edit/pwd",
                    body={"old_password": "wrong", "password": "654321"},
                    jwt=jwt)
    check("旧密码错 success=false+msg",
          resp.get("success") is False and bool(resp.get("msg")),
          "msg=%s" % (resp.get("msg") or "")[:40])
    st, resp = http("POST", HTTP_BASE + "/api/account/edit/pwd",
                    body={"old_password": os.environ.get("PF_PASS", "CHANGE_ME"), "password": "x"}, jwt=jwt)
    check("/api/account/edit/pwd 不是有效业务路径（带前缀反而 404 语义）",
          resp.get("success") is False)


def test_bonus(jwt):
    section("[+] 其余端点扫描")
    _, r = http("GET", HTTP_BASE + "/api/zhuo/start_games", jwt=jwt)
    check("zhuo/start_games success=true", r.get("success") is True)
    _, r = http("GET", HTTP_BASE + "/api/zhuo/end_games", jwt=jwt)
    check("zhuo/end_games（已开始）success=true", r.get("success") is True)
    _, r = http("GET", HTTP_BASE + "/api/zhuo/end_games", jwt=jwt)
    check("zhuo/end_games（未开始）success=false+message",
          r.get("success") is False and bool(r.get("message")),
          "message=%s" % (r.get("message") or "")[:30])
    _, r = http("GET", HTTP_BASE + "/api/zhuo/device_status_all", jwt=jwt)
    check("device_status_all 4 台 {serial_no,ip,online}",
          r.get("success") is True and len(r.get("data") or []) == 4
          and all(set(d) >= {"serial_no", "ip", "online"} for d in r["data"]),
          "data=%s" % (r.get("data") or [])[:2])
    _, r = http("GET", HTTP_BASE + "/api/zhuo/device_status/1", jwt=jwt)
    check("device_status/1 {ip,online}",
          r.get("success") is True and set(r.get("data") or {}) >= {"ip", "online"})
    _, r = http("GET", HTTP_BASE + "/api/zhuo/zhuo_reset_games", jwt=jwt)
    check("zhuo/zhuo_reset_games success=true", r.get("success") is True)
    _, r = http("GET", HTTP_BASE + "/api/zhuo/restart_device", jwt=jwt)
    check("zhuo/restart_device success=true", r.get("success") is True)

    _, r = http("GET", HTTP_BASE + "/api/config/get_config", jwt=jwt)
    data = r.get("data") or {}
    check("config/get_config data{config_number,wifi_config,data_config}",
          r.get("success") is True
          and set(data) >= {"config_number", "wifi_config", "data_config"})
    check("config_number 字段 East/South/West/North",
          set(data.get("config_number") or {}) == {"East", "South", "West",
                                                   "North"})
    check("data_config 字段 Data_send/Timeout/Delay/Count_type/"
          "Upload_img_clarity",
          set(data.get("data_config") or {}) == {
              "Data_send", "Timeout", "Delay", "Count_type",
              "Upload_img_clarity"})
    _, r = http("GET", HTTP_BASE + "/api/config/get_device_wifi_list", jwt=jwt)
    check("config/get_device_wifi_list success=true", r.get("success") is True)
    _, r = http("POST", HTTP_BASE + "/api/config/set_number",
                body={"east": 26, "south": 26, "west": 26, "north": 26}, jwt=jwt)
    check("config/set_number success=true", r.get("success") is True)
    _, r = http("POST", HTTP_BASE + "/api/config/set_number",
                body={"east": 25, "south": 26, "west": 26, "north": 26}, jwt=jwt)
    check("config/set_number 奇数拒绝 success=false+msg",
          r.get("success") is False and bool(r.get("msg")))
    _, r = http("POST", HTTP_BASE + "/api/config/set_data_config",
                body={"data_send": 1, "timeout": 15, "delay": 0,
                      "count_type": 0}, jwt=jwt)
    check("config/set_data_config success=true", r.get("success") is True)
    _, r = http("POST", HTTP_BASE + "/api/config/set_wifi",
                body={"ssid": "XCS-Fixture-AP", "pwd": "fixture123"}, jwt=jwt)
    check("config/set_wifi success=true", r.get("success") is True)

    _, r = http("GET", HTTP_BASE + "/api/preview/start_preview/1", jwt=jwt)
    check("preview/start_preview/1 success=true", r.get("success") is True)
    params = {"top": 0, "left": 0, "right": 540, "bottom": 960,
              "brightness": 50, "motor": 0, "cap_brightness": 50,
              "cap_exposure": 50, "cap_hue": 0, "cap_saturation": 50,
              "cap_contrast": 50, "cap_gain": 0, "img_rotation": 0,
              "img_min_hreshold": 0, "cap_fps": 0}
    _, r = http("POST", HTTP_BASE + "/api/preview/set_params_v2/1",
                body=params, jwt=jwt)
    check("preview/set_params_v2/1 success=true（15 字段=原版合约）",
          r.get("success") is True)
    bad_params = dict(params)
    del bad_params["cap_fps"]
    _, r = http("POST", HTTP_BASE + "/api/preview/set_params_v2/1",
                body=bad_params, jwt=jwt)
    check("set_params_v2 缺字段 -> success=false+msg",
          r.get("success") is False and bool(r.get("msg")),
          "msg=%s" % (r.get("msg") or "")[:30])
    _, r = http("POST", HTTP_BASE + "/api/preview/end_preview/1", body="",
                jwt=jwt)
    check("preview/end_preview/1 success=true", r.get("success") is True)

    _, r = http("GET", HTTP_BASE +
                "/api/app/check_update?type=3&version=2.0.15&app_name=xcs",
                jwt=jwt)
    check("app/check_update 恒 success=false（不触发 OTA）",
          r.get("success") is False)
    check("app/check_update 带回归 code 键（HttpReq.cs:38 实绩：缺 code 会崩 HomeViewModel 构造）",
          "code" in r)

    st, r = http("GET", HTTP_BASE + "/api/zhuo/device_status_all", jwt="wrong-jwt")
    check("错误 Bearer -> success=false+msg（不弹 401 形态）",
          r.get("success") is False and bool(r.get("msg"))
          and r.get("code") != 401)


def rounds_count():
    if not os.path.exists(ROUNDS_PATH):
        return 0
    with io.open(ROUNDS_PATH, "r", encoding="utf-8") as f:
        return sum(1 for line in f if line.strip())


def test_8_log():
    section("[8] requests.log 对账")
    with io.open(LOG_PATH, "r", encoding="utf-8") as f:
        lines = f.read().split("\n")

    def find(substr):
        return [l for l in lines if substr in l]

    checks = [
        ("登录请求", "HTTP|POST /api/login/login/distinguish"),
        ("get_server_addr", "HTTP|POST /api/server/get_server_addr"),
        ("WS 登录帧", "WS|recv|types=6"),
        ("WS 50 应答", "WS|send|types=50"),
        ("WS 心跳帧", "WS|recv|types=1"),
        ("WS 51 应答", "WS|send|types=51"),
        ("业务推送", "server-push|101-img"),
        ("QoS 回执登记", "WS|qos-ack|types=4"),
        ("图片抓取", "IMG|GET /img/"),
        ("zhuo/add", "HTTP|POST /api/zhuo/add"),
        ("改密无前缀", "HTTP|POST /account/edit/pwd"),
    ]
    for name, substr in checks:
        hits = find(substr)
        check("log 行: %s" % name, len(hits) >= 1,
              "e.g. %s" % summarize(hits[0]) if hits else "MISSING")

    # 113 推送 + 301 也应有行
    check("log 行: 113 推送", len(find("server-push|113-run")) >= 1)
    check("log 行: 301 推送", len(find("server-push|301-unlogin")) >= 1)


def summarize(line, limit=110):
    line = line.strip()
    return line if len(line) <= limit else line[:limit] + "..."


def main():
    print("fixture selftest @ %s" % time.strftime("%Y-%m-%d %H:%M:%S"))
    test_1_deps()
    ensure_server()
    # 状态复位，保证可重复跑
    http("POST", HTTP_BASE + "/_fixture/reset", body={})
    try:
        jwt = test_2_login()
        test_3_server_addr()
        test_4_ws(jwt)
        test_5_pipeline_101(jwt)
        test_6_zhuo_add(jwt)
        test_7_edit_pwd(jwt)
        test_bonus(jwt)
        test_8_log()
    finally:
        stop_server_if_spawned()

    print("\n================ 结果 ================")
    print("PASS=%d FAIL=%d" % (PASS, FAIL))
    if FAILURES:
        print("失败项: %s" % ", ".join(FAILURES))
        sys.exit(1)
    print("全部通过")


if __name__ == "__main__":
    main()
