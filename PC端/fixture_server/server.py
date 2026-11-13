# -*- coding: utf-8 -*-
"""
XCS PC 图传客户端 —— 本地 fixture 服务器（单进程三端口，只绑定本机回环地址）

默认绑定 127.0.0.2（127.0.0.0/8 回环别名，无需配置）：
本机 Docker 已占用 127.0.0.1:8081（aiquant-live-bot 容器），而客户端把业务
端口 :8081 硬编码（HttpReq.cs:23），故 fixture 整体挪到回环别名上零冲突共存。
GUI 登录时"口令地址"填 127.0.0.2；get_server_addr 下发的业务 IP 也是它。
可用 FIXTURE_HOST=127.0.0.1 切回（8081 空出时）。

  :8081  HTTP JSON 业务 API（含 /api 前缀规则；例外：/account/edit/pwd 无前缀）
  :80    图片文本服务（单行文本：dataKey 替换后的 base64 JPEG）
  :18889 WebSocket（Protocol JSON 帧）

协议合约来源（逐字核对过字段名）:
  analysis/xcs-cs112/distinguish.Model/{LoginResp,LoginUser,Device,Protocol,
      ProtocolDataContent,ProtocolDataContentImg,SaveMj,ConfigNumber,
      WifiConfig,DataConfig}.cs
  analysis/xcs-cs112/distinguish.Common/{HttpReq,Global,CryptionUtils,
      NetManager,Constant}.cs
  analysis/xcs-cs112/distinguish.ViewModel/{LoginViewModel,HomeViewModel,
      OnlineConfigViewModel,PreviewSettingViewModel,EditPwdViewModel}.cs

红线: 只 bind 回环地址；绝不连接真实服务器 8.137.196.28；不运行仓库任何 exe。

运行:  python server.py            （前台）
       start /b python server.py   （后台，Windows）
可选环境变量:
  FIXTURE_HOST      回环别名（默认 127.0.0.2）
  FIXTURE_JWT       覆盖固定测试 JWT（默认 FIXTURE-JWT-XCS232-0001）
  FIXTURE_401=1     鉴权失败时返回 {success:false,code:401} 形态（默认关闭，
                    避免客户端弹"登录已失效"打断 GUI 测试）
"""

import base64
import io
import json
import os
import random
import sys
import threading
import time
import uuid
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer

import websockets
import asyncio

# cryptography 仅在 get_server_addr 用到（加载客户端公钥并回加密数据）
from cryptography.hazmat.primitives import serialization
from cryptography.hazmat.primitives.asymmetric import padding
from cryptography.hazmat.primitives.serialization import load_der_public_key

# ----------------------------------------------------------------------------
# 配置
# BIND_HOST 默认 127.0.0.2 —— 仍是本机回环（127.0.0.0/8 全部直达 loopback）。
# 为什么不用 127.0.0.1：本机 Docker 发布了 127.0.0.1:8081（aiquant-live-bot），
# 而客户端 HttpReq.cs:23 硬编码业务端口 :8081，无法改端口；fixture 全部绑到
# 回环别名 127.0.0.2 即可零冲突共存（GUI 登录时"口令地址"填 127.0.0.2）。
# 可用环境变量 FIXTURE_HOST=127.0.0.1 切回（前提是 8081 已空出）。
# ----------------------------------------------------------------------------
BIND_HOST = os.environ.get("FIXTURE_HOST", "127.0.0.2")
HTTP_PORT = 8081
IMG_PORT = 80
WS_PORT = 18889

BASE_DIR = os.path.dirname(os.path.abspath(__file__))
LOG_PATH = os.path.join(BASE_DIR, "requests.log")
ROUNDS_PATH = os.path.join(BASE_DIR, "rounds.jsonl")

JWT = os.environ.get("FIXTURE_JWT", "FIXTURE-JWT-XCS232-0001")
USE_401 = os.environ.get("FIXTURE_401", "") == "1"

ACCOUNT_USERNAME = "XCS232"
# 凭据不入库（AGENTS.md 政策）：环境变量 PF_PASS 注入实际值
ACCOUNT_PASSWORD = os.environ.get("PF_PASS", "CHANGE_ME")

_log_lock = threading.Lock()


def log_line(line):
    """requests.log：每个收到的请求一行（时间|方法|路径|Body摘要|响应摘要）"""
    with _log_lock:
        with io.open(LOG_PATH, "a", encoding="utf-8") as f:
            f.write(line + "\n")
    print(line, flush=True)


def summarize(text, limit=160):
    if text is None:
        return ""
    text = str(text).replace("\r", " ").replace("\n", " ")
    return text if len(text) <= limit else text[:limit] + "...(%d)" % len(text)


def now_str():
    return time.strftime("%Y-%m-%d %H:%M:%S", time.localtime()) + ".%03d" % (
        int(time.time() * 1000) % 1000)


# ----------------------------------------------------------------------------
# dataKey 编解码
#   客户端 Global.GetDataBash64: dataKey 按 '|' 分组、组内 ',' 拆对，
#   做 text.Replace(pair[1], pair[0]) 还原。
#   服务器编码（反向）: text.Replace(pair[0], pair[1])，dataKey="A,a" 即 'A'->'a'。
#   注意该替换要能无损往返：原 base64 中不能含 pair[1]='a' 之外会冲突的字符——
#   即原文不含 'a' 时，A->a 编码 / a->A 解码 完全可逆。ensure_roundtrip_base64()
#   负责产出不含 'a' 的合法 JPEG base64。
# ----------------------------------------------------------------------------
DATA_KEY = "A,a"

# get_server_addr 私钥解密后得到的内容：[业务IP, dataKey]
# 业务 IP = BIND_HOST（客户端后续 HTTP/WS/图片全部回到本机 fixture）
SERVER_ADDR_PLAINTEXT = json.dumps([BIND_HOST, DATA_KEY], separators=(",", ":"))


def encode_datakey(text, data_key=DATA_KEY):
    """服务器方向：按 pair[0]->pair[1] 替换（客户端 decode 的逆操作）"""
    for group in data_key.split("|"):
        parts = group.split(",")
        if len(parts) >= 2 and parts[0]:
            text = text.replace(parts[0], parts[1])
    return text


def decode_datakey(text, data_key=DATA_KEY):
    """客户端方向（GetDataBash64）：pair[1]->pair[0]，自测用"""
    for group in data_key.split("|"):
        parts = group.split(",")
        if len(parts) >= 2 and len(parts[1]) > 0:
            text = text.replace(parts[1], parts[0])
    return text


# ----------------------------------------------------------------------------
# fixture JPEG：159 字节最小基线灰度 JPEG（8x8，单分量；开发期手工构造并用
# Pillow 验证可解码），其标准 base64 已被筛选拼不含字母 'a' —— 这是 dataKey
# 'A'<->'a' 替换可无损往返的不变式（若原文含 'a'，客户端 a->A 还原会把原生
# 'a' 一并翻成 'A'，图片损坏）。server 运行时不依赖 PIL，启动时只做断言。
# 结构: SOI + APP0(JFIF) + DQT(64B 熵源) + SOF0(8x8灰度) + DHT(DC/AC 各1码)
#       + SOS + 1字节熵数据 + EOI
# ----------------------------------------------------------------------------
JPEG_HEX = (
    "ffd8ffe000104a46494600010100000100010000ffdb00430095cd7512d818f2467ab3"
    "ab1110bcb450a694afd37349b863e4ac5906f1775b2c9d1e7f1038c54a22be406665eb"
    "e080152b73678d48e324d26fde8d48b56bfd5cffc0000b080008000801011100ffc400"
    "14000100000000000000000000000000000000ffc40014100100000000000000000000"
    "000000000000ffda0008010100003f003fffd9"
)
FIXTURE_JPEG = bytes.fromhex(JPEG_HEX)


def _build_jpeg_with_com(com_bytes):
    raw = FIXTURE_JPEG
    com_payload = b"fixture:" + com_bytes
    com = b"\xff\xfe" + (len(com_payload) + 2).to_bytes(2, "big") + com_payload
    return raw[:2] + com + raw[2:]        # 插在 SOI 之后（备用，当前未用）


_ensure_lock = threading.Lock()
_roundtrip_jpeg_b64 = None


def ensure_roundtrip_base64():
    """返回 base64(合法 JPEG)，且 base64 字符串中不含 'a'（保证 a<->A 替换可逆）。
    JPEG_HEX 在开发期已按该不变式筛选拼；这里只断言校验。"""
    global _roundtrip_jpeg_b64
    with _ensure_lock:
        if _roundtrip_jpeg_b64 is not None:
            return _roundtrip_jpeg_b64
        b64 = base64.b64encode(FIXTURE_JPEG).decode("ascii")
        if "a" in b64:
            raise RuntimeError("JPEG_HEX invariant broken: base64 contains 'a'")
        if base64.b64decode(decode_datakey(encode_datakey(b64))) != FIXTURE_JPEG:
            raise RuntimeError("JPEG_HEX invariant broken: round trip mismatch")
        _roundtrip_jpeg_b64 = b64
        return _roundtrip_jpeg_b64


def fixture_img_url(serial_no, mahjong_number):
    return "img/%s_%d.jpg" % (serial_no, mahjong_number)


# ----------------------------------------------------------------------------
# 业务状态
# ----------------------------------------------------------------------------
class FixtureState(object):
    def __init__(self):
        self.lock = threading.Lock()
        self.games_started = False
        self.config_number = {"East": 26, "South": 26, "West": 26, "North": 26}
        self.wifi_config = {"Ssid": "XCS-Fixture-AP", "Pwd": "fixture123"}
        self.data_config = {"Data_send": 1, "Timeout": 15, "Delay": 0,
                            "Count_type": 0, "Upload_img_clarity": 2}
        self.devices = self._default_devices()
        self.preview_active = {}      # device_id -> True
        self.preview_params = {}      # device_id -> last set_params_v2 body
        self.ws_clients = set()       # websocket 对象集合
        self.ws_logged_in = set()
        self.pushes = []              # {fp, msg_type, acked, ts}
        self.qos_acked = []           # 客户端 types=4 回执登记
        self.edit_pwd_calls = []
        self.admin_tokens = set()

    @staticmethod
    def _default_devices():
        # 字段名逐字对齐 distinguish.Model/Device.cs 的属性名（Newtonsoft 大小写不敏感，
        # 但按任务要求逐字输出属性名）。serial_no 必须可 int.Parse ——
        # HomeViewModel.SendData 构造 SaveMj(serial_number=int) 时执行
        # int.Parse(Serial_number)（HomeViewModel.cs:3189），非数字序列号会让
        # zhuo/add 静默失败，故用 202501xx 数字型序列号，末两位 01/02/03/04
        # 对应任务原案 SN2025E/S/WE/N 的东南西北。
        def dev(i, direction, serial):
            return {
                "Id": i, "UserId": 1, "Direction": direction, "Mj_count": 26,
                "Serial_no": serial,
                "Top": 0.0, "Left": 0.0, "Right": 540.0, "Bottom": 960.0,
                "Brightness": 50.0, "Motor": 0,
                "Cap_brightness": 50, "Cap_exposure": 50, "Cap_hue": 0,
                "Cap_saturation": 50, "Cap_contrast": 50, "Cap_gain": 0,
                "Img_rotation": 0, "Img_min_hreshold": 0, "Cap_fps": 0,
                "Beta": 0, "Img_add_num": 0, "Gk_min": 0, "Gk_max": 0,
                "Upload_img_clarity": 2, "Count_type": 0,
            }
        return [
            dev(1, 1, "20250101"),   # 东 E
            dev(2, 2, "20250102"),   # 南 S
            dev(3, 3, "20250103"),   # 西 W
            dev(4, 4, "20250104"),   # 北 N
        ]

    def serial_by_direction(self, direction):
        for d in self.devices:
            if d["Direction"] == direction:
                return d["Serial_no"]
        return None

    def direction_mj_count(self, direction):
        for d in self.devices:
            if d["Direction"] == direction:
                return d["Mj_count"]
        return 26


STATE = FixtureState()

# ----------------------------------------------------------------------------
# WS 推送原语（types=2 业务帧 / types=301 踢下线）
# ----------------------------------------------------------------------------
_ws_loop = None       # asyncio loop（主线程）


def _ws_broadcast_sync(frame):
    """线程安全入口：把帧调度到 asyncio loop 广播"""
    if _ws_loop is None:
        return 0
    fut = asyncio.run_coroutine_threadsafe(_broadcast_async(frame), _ws_loop)
    return fut.result(timeout=5)


async def _broadcast_async(frame):
    sent = 0
    for ws in list(STATE.ws_logged_in):
        try:
            await ws.send(frame)
            sent += 1
        except Exception:
            pass
    return sent


def build_push_frame(msg_type, msg_content, qos=True):
    fp = str(uuid.uuid4())           # Guid 风格（小写、连字符），同 C# Guid.NewGuid()
    frame = {
        "types": 2,                  # PROTOCOL_TYPE_C_FROM_CLIENT_TYPE_OF_COMMON_DATA
        "data_content": json.dumps({"msg_type": msg_type,
                                    "msg_content": msg_content},
                                   separators=(",", ":")),
        "from": "server",
        "to": "",
        "fp": fp,
        "qos": qos,
        "retry_count": 0,
    }
    with STATE.lock:
        STATE.pushes.append({"fp": fp, "msg_type": msg_type,
                             "msg_content": summarize(msg_content, 80),
                             "qos": qos, "acked": False,
                             "ts": now_str()})
    return json.dumps(frame, separators=(",", ":"))


def push_raw(frame_str, log_note):
    n = _ws_broadcast_sync(frame_str)
    log_line("%s|WS|server-push|%s|frame=%s|sent=%d" %
             (now_str(), log_note, summarize(frame_str, 200), n))
    return n


def push_101(serial_number, mahjong_number, ai_val, ai_precision, img):
    """图牌：msg_content = ProtocolDataContentImg JSON 字符串"""
    content = json.dumps({
        "serial_number": str(serial_number),
        "mahjong_number": int(mahjong_number),
        "ai_val": int(ai_val),
        "ai_precision": float(ai_precision),
        "img": img,
    }, separators=(",", ":"))
    return push_raw(build_push_frame(101, content), "101-img")


def push_102(device_id, encoded_b64):
    """预览帧：msg_content = "<deviceId>|<dataKey 替换后 base64>"""
    return push_raw(build_push_frame(102, "%d|%s" % (int(device_id), encoded_b64)),
                    "102-preview")


def push_113(serial, run):
    return push_raw(build_push_frame(113, "%s|%d" % (serial, int(run))), "113-run")


def push_114(serial):
    return push_raw(build_push_frame(114, str(serial)), "114-stop")


def push_115(serial):
    return push_raw(build_push_frame(115, str(serial)), "115-offline")


def push_116(serial):
    return push_raw(build_push_frame(116, str(serial)), "116-online")


def push_117(serial):
    return push_raw(build_push_frame(117, str(serial)), "117-shuffle")


def push_301():
    """ERROR_CODE_RESPONSE_FOR_UN_LOGIN：客户端收到后自动断开重连"""
    frame = json.dumps({"types": 301, "data_content": "", "from": "server",
                        "to": "", "fp": str(uuid.uuid4()), "qos": False,
                        "retry_count": 0}, separators=(",", ":"))
    return push_raw(frame, "301-unlogin")


# ----------------------------------------------------------------------------
# zhuo/add 校验 —— 镜像 HomeViewModel.OnValidData (HomeViewModel.cs:3247)
# ----------------------------------------------------------------------------
def validate_round(tiles):
    """返回 None(合法) 或失败原因字符串。tiles: list[dict]"""
    if not isinstance(tiles, list) or not tiles:
        return "牌局数据为空或格式错误"
    for t in tiles:
        for k in ("mahjong_number", "serial_number", "direction", "result"):
            if k not in t:
                return "缺少字段 %s" % k
            if not isinstance(t[k], int):
                return "字段 %s 必须为整数" % k
    with STATE.lock:
        total_expected = sum(d["Mj_count"] for d in STATE.devices)
    if len(tiles) != total_expected:
        return ("配置合计为%d,目前为：%d张" % (total_expected, len(tiles)))
    per_dir = {}
    code_count = {}
    for t in tiles:
        per_dir[t["direction"]] = per_dir.get(t["direction"], 0) + 1
        key = str(t["result"])
        code_count[key] = code_count.get(key, 0) + 1
    for direction, cnt in sorted(per_dir.items()):
        expect = STATE.direction_mj_count(direction)
        if cnt != expect:
            return "错误方向%d配置为%d张，现在%d张" % (direction, expect, cnt)
    if len(per_dir) != 4:
        return "方向数量不足4个，现有%d个" % len(per_dir)
    for code, cnt in sorted(code_count.items()):
        try:
            num = int(code)
        except ValueError:
            return "错误编码%s不是数字" % code
        if num < 40 and cnt != 4:
            return "错误%s,数量应该为4，现在数量为%d" % (code, cnt)
    return None


def append_round(tiles, serial_map_note=""):
    rec = {"ts": now_str(), "tile_count": len(tiles), "tiles": tiles}
    with _log_lock:
        with io.open(ROUNDS_PATH, "a", encoding="utf-8") as f:
            f.write(json.dumps(rec, ensure_ascii=False) + "\n")


# ----------------------------------------------------------------------------
# HTTP 响应辅助
# ----------------------------------------------------------------------------
def ok(extra=None, message="ok"):
    body = {"success": True, "message": message}
    if extra:
        body.update(extra)
    return body


def fail(msg, code=500):
    # 客户端 HttpReq.post/get 对每个 success=false 的响应都会读
    # dictionary["code"]（HttpReq.cs:38，短路&&），缺 code 会直接
    # KeyNotFoundException 崩掉调用线程而不是弹错误框 —— 所以失败体必须带 code。
    # 默认 code=500（非401，客户端正常弹 msg/message 文本）；
    # FIXTURE_401=1 时统一改成 401，触发"登录已失效"弹窗分支（可选开关）。
    # msg/message 同时给出：不同端点分别读 dictionary["msg"] 或 ["message"]。
    body = {"success": False, "msg": msg, "message": msg, "code": code}
    if USE_401:
        body["code"] = 401
    return body


# ----------------------------------------------------------------------------
# HTTP 业务处理
# 返回 (status_code, dict) 或 (status_code, raw_text, content_type)
# ----------------------------------------------------------------------------
EXEMPT_AUTH_PATHS = (
    "/api/login/login/distinguish",
    "/api/server/get_server_addr",
)


def handle_api(method, path, query, body_bytes, headers):
    """返回 (status, payload) payload=dict 或 (text, content_type) 元组"""
    # ---- 鉴权 ----
    auth = headers.get("Authorization", "") or headers.get("authorization", "")
    if not any(path.startswith(p) for p in EXEMPT_AUTH_PATHS):
        if auth != "Bearer " + JWT:
            # 默认 code=500 非 401 形态，客户端走普通错误弹窗；
            # FIXTURE_401=1 时 fail() 内部会改写成 401 触发"登录已失效"分支
            return 200, fail("未授权访问（fixture 校验 Bearer token 失败）")

    # ---- 登录 ----
    if path == "/api/login/login/distinguish" and method == "POST":
        try:
            req = json.loads(body_bytes.decode("utf-8")) if body_bytes else {}
        except ValueError:
            req = {}
        username = req.get("username", "")
        password = req.get("password", "")
        if username == ACCOUNT_USERNAME and password == ACCOUNT_PASSWORD:
            # 字段名逐字对齐 LoginResp.cs / LoginUser.cs 属性名
            resp = {
                "success": True,
                "jwt": JWT,
                "LoginUser": {
                    "Id": 1, "Username": ACCOUNT_USERNAME,
                    "Token": "fixture-token-0001",
                    "Login_type": 1,
                    "Exp": 4102444800,      # 2100-01-01
                    "Nbf": 1600000000,
                    "JihuoStatus": 1,
                    "DataSend": 1,
                    "Timeout": 15,          # HomeViewModel 接受计时用
                    "Delay": 0,
                },
                "Devices": STATE.devices,
            }
            return 200, resp
        return 200, fail("用户名或密码错误")

    # ---- RSA 业务地址 ----
    if path == "/api/server/get_server_addr" and method == "POST":
        try:
            req = json.loads(body_bytes.decode("utf-8")) if body_bytes else {}
            key_b64 = req.get("key", "")
            client_type = req.get("client_type")
            if client_type != 5:
                return 200, fail("client_type 不支持")
            pub = load_der_public_key(base64.b64decode(key_b64))
            ct = pub.encrypt(SERVER_ADDR_PLAINTEXT.encode("utf-8"),
                             padding.PKCS1v15())
            return 200, {"success": True, "msg": base64.b64encode(ct).decode("ascii")}
        except Exception as e:
            return 200, fail("公钥解析失败: %s" % e.__class__.__name__)

    # ---- 游戏 ----
    if path == "/api/zhuo/start_games" and method == "GET":
        with STATE.lock:
            STATE.games_started = True
        return 200, ok(message="开始游戏成功")
    if path == "/api/zhuo/end_games" and method == "GET":
        with STATE.lock:
            if not STATE.games_started:
                return 200, fail("游戏未开始，无法结束")
            STATE.games_started = False
        return 200, ok(message="结束游戏成功")
    if path == "/api/zhuo/zhuo_reset_games" and method == "GET":
        with STATE.lock:
            STATE.games_started = False
        return 200, ok(message="重新开始游戏成功")
    if path == "/api/zhuo/restart_device" and method == "GET":
        return 200, ok(message="重启成功")

    # ---- 设备状态 ----
    if path == "/api/zhuo/device_status_all" and method == "GET":
        with STATE.lock:
            data = [{"serial_no": d["Serial_no"], "ip": BIND_HOST,
                     "online": 1} for d in STATE.devices]
        return 200, {"success": True, "data": data}
    if path.startswith("/api/zhuo/device_status/") and method == "GET":
        try:
            dev_id = int(path.rsplit("/", 1)[1])
        except ValueError:
            return 200, fail("设备ID无效")
        with STATE.lock:
            dev = next((d for d in STATE.devices if d["Id"] == dev_id), None)
        if dev is None:
            return 200, fail("设备不存在")
        return 200, {"success": True, "data": {"ip": BIND_HOST, "online": 1}}

    # ---- 牌局上传 ----
    if path == "/api/zhuo/add" and method == "POST":
        try:
            tiles = json.loads(body_bytes.decode("utf-8"))
        except ValueError as e:
            return 200, fail("JSON 解析失败: %s" % e)
        err = validate_round(tiles)
        if err is None:
            append_round(tiles)
            return 200, {"success": True, "message": "ok"}
        return 200, fail(err)

    # ---- OTA（恒无更新，避免触发下载/重启）----
    # 注意必须带 code：HttpReq.cs:38 对 success=false 一律读 dictionary["code"]，
    # 缺 key 会 KeyNotFoundException 崩掉 HomeViewModel 构造（真机验证抓到的实绩）。
    # 真实服务器形态：{"code":0,"msg":"not update","success":false,"data":null,"req_id":""}
    if path == "/api/app/check_update" and method == "GET":
        return 200, {"success": False, "msg": "已是最新版本",
                     "message": "已是最新版本", "code": 0,
                     "data": None, "req_id": ""}

    # ---- 配置 ----
    if path == "/api/config/get_config" and method == "GET":
        with STATE.lock:
            data = {
                "config_number": dict(STATE.config_number),
                "wifi_config": dict(STATE.wifi_config),
                "data_config": dict(STATE.data_config),
            }
        return 200, {"success": True, "data": data}
    if path == "/api/config/get_device_wifi_list" and method == "GET":
        with STATE.lock:
            data = [{"ssid": STATE.wifi_config["Ssid"], "strength": 4}]
        return 200, {"success": True, "data": data}
    if path == "/api/config/set_number" and method == "POST":
        try:
            req = json.loads(body_bytes.decode("utf-8"))
        except ValueError:
            return 200, fail("JSON 解析失败")
        for k in ("east", "south", "west", "north"):
            v = req.get(k)
            if not isinstance(v, int) or v < 2 or v > 52 or v % 2 != 0:
                return 200, fail("%s 必须为 2..52 的偶数" % k)
        with STATE.lock:
            STATE.config_number = {"East": req["east"], "South": req["south"],
                                   "West": req["west"], "North": req["north"]}
            for d in STATE.devices:
                d["Mj_count"] = {
                    1: req["east"], 2: req["south"],
                    3: req["west"], 4: req["north"]}[d["Direction"]]
        return 200, ok(message="保存成功")
    if path == "/api/config/set_data_config" and method == "POST":
        try:
            req = json.loads(body_bytes.decode("utf-8"))
        except ValueError:
            return 200, fail("JSON 解析失败")
        # 原版合约（对照 analysis/xcs-cs/distinguish.ViewModel/OnlineConfigViewModel.cs
        # 原始反编译）：data_send/timeout/delay/count_type 为原版 4 键。
        # 当前真实服务器额外强制要求 upload_img_clarity（serde 缺键即拒绝），还原工程
        # 经用户批准按"当前服务器兼容性适配"补发该键（值 0，见 restore-status.md）。
        # 本 fixture 建模原版协议，仅校验必需子集，容忍额外兼容键。
        for k in ("data_send", "timeout", "delay", "count_type"):
            if not isinstance(req.get(k), int):
                return 200, fail("%s 必须为整数" % k)
        with STATE.lock:
            STATE.data_config = {
                "Data_send": req["data_send"], "Timeout": req["timeout"],
                "Delay": req["delay"], "Count_type": req["count_type"],
            }
        return 200, ok(message="保存成功")
    if path == "/api/config/set_wifi" and method == "POST":
        try:
            req = json.loads(body_bytes.decode("utf-8"))
        except ValueError:
            return 200, fail("JSON 解析失败")
        if not isinstance(req.get("ssid"), str) or not req["ssid"]:
            return 200, fail("ssid 不能为空")
        if not isinstance(req.get("pwd"), str) or len(req["pwd"]) < 8:
            return 200, fail("pwd 至少 8 位")
        with STATE.lock:
            STATE.wifi_config = {"Ssid": req["ssid"], "Pwd": req["pwd"]}
        return 200, ok(message="保存成功")

    # ---- 画框预览 ----
    if path.startswith("/api/preview/start_preview/") and method == "GET":
        try:
            dev_id = int(path.rsplit("/", 1)[1])
        except ValueError:
            return 200, fail("设备ID无效")
        with STATE.lock:
            dev = next((d for d in STATE.devices if d["Id"] == dev_id), None)
            if dev is None:
                return 200, fail("设备不存在")
            STATE.preview_active[dev_id] = True
        return 200, ok(message="预览已开启")
    if path.startswith("/api/preview/end_preview/") and method == "POST":
        try:
            dev_id = int(path.rsplit("/", 1)[1])
        except ValueError:
            return 200, fail("设备ID无效")
        with STATE.lock:
            STATE.preview_active.pop(dev_id, None)
        return 200, ok(message="预览已结束")
    if path.startswith("/api/preview/set_params_v2/") and method == "POST":
        try:
            dev_id = int(path.rsplit("/", 1)[1])
        except ValueError:
            return 200, fail("设备ID无效")
        try:
            req = json.loads(body_bytes.decode("utf-8"))
        except ValueError:
            return 200, fail("JSON 解析失败")
        # 字段级校验：键集对齐原版 PreviewSettingViewModel.saveParams 构造的 15 字段
        # （analysis/xcs-cs 原始反编译）。当前真实服务器额外强制要求 bate/img_add_num/
        # gk_min/gk_max/upload_img_clarity/count_type（serde 缺键即拒绝，"bate" 拼写为
        # 显著指纹），还原工程经用户批准按"当前服务器兼容性适配"补发这 6 键（值 0，
        # 见 restore-status.md）。本 fixture 建模原版协议，仅校验必需 15 字段，容忍额外兼容键。
        int_fields = ["top", "left", "right", "bottom", "brightness", "motor",
                      "cap_brightness", "cap_exposure", "cap_hue",
                      "cap_saturation", "cap_contrast", "cap_gain",
                      "img_rotation", "img_min_hreshold", "cap_fps"]
        for k in int_fields:
            if k not in req:
                return 200, fail("缺少字段 %s" % k)
            if not isinstance(req[k], int):
                return 200, fail("字段 %s 必须为整数" % k)
        if req["right"] <= req["left"]:
            return 200, fail("right 必须大于 left")
        if req["bottom"] <= req["top"]:
            return 200, fail("bottom 必须大于 top")
        with STATE.lock:
            dev = next((d for d in STATE.devices if d["Id"] == dev_id), None)
            if dev is None:
                return 200, fail("设备不存在")
            STATE.preview_params[dev_id] = req
            dev["Top"] = float(req["top"]); dev["Left"] = float(req["left"])
            dev["Right"] = float(req["right"]); dev["Bottom"] = float(req["bottom"])
            dev["Brightness"] = float(req["brightness"])
            dev["Motor"] = req["motor"]
            dev["Cap_brightness"] = req["cap_brightness"]
            dev["Cap_exposure"] = req["cap_exposure"]
            dev["Cap_hue"] = req["cap_hue"]
            dev["Cap_saturation"] = req["cap_saturation"]
            dev["Cap_contrast"] = req["cap_contrast"]
            dev["Cap_gain"] = req["cap_gain"]
            dev["Img_rotation"] = req["img_rotation"]
            dev["Img_min_hreshold"] = req["img_min_hreshold"]
            dev["Cap_fps"] = req["cap_fps"]
        return 200, ok(message="保存成功")

    return 200, fail("未知接口: %s" % path)   # 保持 200：客户端只解析 JSON 体


def handle_account_edit_pwd(body_bytes, headers):
    """POST /account/edit/pwd —— HttpReq.cs:24 例外分支，无 /api 前缀。
    字段对齐 EditPwdViewModel.SaveEditPwd: {"old_password","password"}"""
    try:
        req = json.loads(body_bytes.decode("utf-8"))
    except ValueError:
        return 200, fail("JSON 解析失败")
    old_pwd = req.get("old_password", "")
    new_pwd = req.get("password", "")
    if not old_pwd:
        return 200, fail("请输入旧密码")
    if not new_pwd:
        return 200, fail("请输入新密码")
    if old_pwd != ACCOUNT_PASSWORD:
        return 200, fail("旧密码错误")
    with STATE.lock:
        STATE.edit_pwd_calls.append({"ts": now_str(), "ok": True})
    return 200, {"success": True, "message": "密码修改成功"}


# ----------------------------------------------------------------------------
# 管理端点（仅 127.0.0.1 可达；供 selftest / 后续 GUI 驱动脚本触发推送）
# ----------------------------------------------------------------------------
def handle_admin(method, path, body_bytes):
    try:
        req = json.loads(body_bytes.decode("utf-8")) if body_bytes else {}
    except ValueError:
        req = {}

    if path == "/_fixture/status" and method == "GET":
        with STATE.lock:
            return 200, {
                "success": True,
                "ws_clients": len(STATE.ws_logged_in),
                "games_started": STATE.games_started,
                "devices": STATE.devices,
                "config_number": STATE.config_number,
                "pushes": STATE.pushes[-50:],
                "qos_acked": STATE.qos_acked,
                "edit_pwd_calls": STATE.edit_pwd_calls,
                "jwt": JWT,
                "use_401": USE_401,
            }

    if path == "/_fixture/img" and method == "GET":
        b64 = ensure_roundtrip_base64()
        return 200, {"success": True, "b64": b64, "b64_len": len(b64),
                     "has_lower_a": "a" in b64,
                     "jpeg_head_hex": base64.b64decode(b64)[:4].hex().upper()}

    if path == "/_fixture/reset" and method == "POST":
        with STATE.lock:
            STATE.games_started = False
            STATE.preview_active = {}
            STATE.preview_params = {}
            STATE.pushes = []
            STATE.qos_acked = []
            STATE.devices = STATE._default_devices()
            STATE.config_number = {"East": 26, "South": 26, "West": 26,
                                   "North": 26}
        return 200, ok(message="fixture 状态已重置")

    push_map = {
        "/_fixture/push/101": lambda r: push_101(
            r.get("serial_number", STATE.serial_by_direction(1)),
            r.get("mahjong_number", 1), r.get("ai_val", 1),
            r.get("ai_precision", 0.99),
            r.get("img", fixture_img_url(
                r.get("serial_number", STATE.serial_by_direction(1)),
                r.get("mahjong_number", 1)))),
        "/_fixture/push/102": lambda r: push_102(
            r.get("device_id", 1),
            r.get("b64") or encode_datakey(base64.b64encode(
                base64.b64decode(ensure_roundtrip_base64())).decode("ascii"))),
        "/_fixture/push/113": lambda r: push_113(
            r.get("serial", STATE.serial_by_direction(1)), r.get("run", 1)),
        "/_fixture/push/114": lambda r: push_114(
            r.get("serial", STATE.serial_by_direction(1))),
        "/_fixture/push/115": lambda r: push_115(
            r.get("serial", STATE.serial_by_direction(1))),
        "/_fixture/push/116": lambda r: push_116(
            r.get("serial", STATE.serial_by_direction(1))),
        "/_fixture/push/117": lambda r: push_117(
            r.get("serial", STATE.serial_by_direction(1))),
        "/_fixture/push/301": lambda r: push_301(),
    }
    if path in push_map and method == "POST":
        n = push_map[path](req)
        return 200, {"success": n > 0, "sent": n}

    if path == "/_fixture/ping":
        return 200, {"success": True, "msg": "pong"}

    return 404, fail("未知管理接口: %s" % path)


# ----------------------------------------------------------------------------
# HTTP Server（8081 业务 + 80 图片文本）
# ----------------------------------------------------------------------------
class ApiHandler(BaseHTTPRequestHandler):
    protocol_version = "HTTP/1.1"
    allow_reuse_address = False   # Windows 下禁用 SO_REUSEADDR 双绑，冲突要响亮报错

    def log_message(self, fmt, *args):     # 关闭默认 stderr 噪声
        pass

    def _read_body(self):
        try:
            length = int(self.headers.get("Content-Length", "0") or "0")
        except ValueError:
            length = 0
        if length <= 0:
            return b""
        return self.rfile.read(length)

    def _send_json(self, status, payload):
        if isinstance(payload, tuple):
            text, ctype = payload
        else:
            text = json.dumps(payload, ensure_ascii=False)
            ctype = "application/json"
        data = text.encode("utf-8")
        self.send_response(status)
        self.send_header("Content-Type", ctype + "; charset=utf-8")
        self.send_header("Content-Length", str(len(data)))
        self.end_headers()
        self.wfile.write(data)
        return text

    def _handle(self, method):
        path = self.path
        route, _, query = path.partition("?")
        body = self._read_body()
        status = 500
        try:
            if route.startswith("/_fixture/"):
                status, payload = handle_admin(method, route, body)
                resp_text = self._send_json(status, payload)
            elif route == "/account/edit/pwd" and method == "POST":
                status, payload = handle_account_edit_pwd(body, self.headers)
                resp_text = self._send_json(status, payload)
            elif route.startswith("/api/"):
                status, payload = handle_api(method, route, query, body,
                                             self.headers)
                resp_text = self._send_json(status, payload)
            else:
                status, payload = 200, fail("未知路径: %s" % route)
                resp_text = self._send_json(status, payload)
        except Exception as e:
            status, payload = 500, fail("fixture 内部错误: %r" % e)
            try:
                resp_text = self._send_json(status, payload)
            except Exception:
                resp_text = repr(e)
        log_line("%s|HTTP|%s %s|%s|%s" % (
            now_str(), method, path, summarize(body.decode("utf-8", "replace")),
            "%d %s" % (status, summarize(resp_text))))

    def do_GET(self):
        self._handle("GET")

    def do_POST(self):
        self._handle("POST")


class ImageHandler(BaseHTTPRequestHandler):
    """:80 —— 客户端 Global.FullImgUrl 拼出 http://{业务IP}/{url}；
    DownloadUrlImg 读响应首行 -> GetDataBash64 反替换 -> Base64ToImage。
    所以这里返回"单行文本"：encode_datakey(base64(jpeg))。"""
    protocol_version = "HTTP/1.1"
    allow_reuse_address = False

    def log_message(self, fmt, *args):
        pass

    def do_GET(self):
        route = self.path.lstrip("/")
        if route in ("", "fixture", "fixture/"):
            text = ("xcs fixture image server. use /<url> paths issued by "
                    "fixture pushes.\n")
            status = 200
        else:
            b64 = ensure_roundtrip_base64()
            text = encode_datakey(b64) + "\n"
            status = 200
        data = text.encode("ascii")
        self.send_response(status)
        self.send_header("Content-Type", "text/plain; charset=utf-8")
        self.send_header("Content-Length", str(len(data)))
        self.end_headers()
        self.wfile.write(data)
        log_line("%s|IMG|GET %s||%d text len=%d" % (
            now_str(), self.path, status, len(data)))


# ----------------------------------------------------------------------------
# WebSocket Server（18889）
# ----------------------------------------------------------------------------
async def ws_handler(ws):
    peer = getattr(ws, "remote_address", ("?",))
    log_line("%s|WS|connect|%s|logged_in=0" % (now_str(), peer))
    STATE.ws_clients.add(ws)
    try:
        async for raw in ws:
            try:
                frame = json.loads(raw)
            except ValueError:
                log_line("%s|WS|recv-nonjson|%s|%s" % (now_str(), peer,
                                                       summarize(raw, 120)))
                continue
            types = frame.get("types")
            data_content = frame.get("data_content", "")
            qos = bool(frame.get("qos"))
            fp = frame.get("fp", "")
            log_line("%s|WS|recv|types=%s qos=%s fp=%s|data=%s" % (
                now_str(), types, qos, fp, summarize(data_content, 100)))

            if types == 6:
                # 客户端登录帧：data_content = jwt
                if data_content == JWT:
                    STATE.ws_logged_in.add(ws)
                    reply = {"types": 50, "data_content": "login ok",
                             "from": "server", "to": "",
                             "fp": str(uuid.uuid4()), "qos": False,
                             "retry_count": 0}
                    await ws.send(json.dumps(reply, separators=(",", ":")))
                    log_line("%s|WS|send|types=50|login ok (%s)" % (
                        now_str(), summarize(data_content, 40)))
                else:
                    reply = {"types": 52, "data_content": "jwt mismatch",
                             "from": "server", "to": "",
                             "fp": str(uuid.uuid4()), "qos": False,
                             "retry_count": 0}
                    await ws.send(json.dumps(reply, separators=(",", ":")))
                    log_line("%s|WS|send|types=52|jwt mismatch" % now_str())
            elif types == 1:
                # 心跳 -> PROTOCAL_TYPE_S_FROM_SERVER_TYPE_OF_RESPONSE_KEEP_ALIVE
                reply = {"types": 51, "data_content": "", "from": "server",
                         "to": "", "fp": str(uuid.uuid4()), "qos": False,
                         "retry_count": 0}
                await ws.send(json.dumps(reply, separators=(",", ":")))
                log_line("%s|WS|send|types=51|keepalive ack" % now_str())
            elif types == 3:
                # 登出
                STATE.ws_logged_in.discard(ws)
                log_line("%s|WS|logout|types=3|" % now_str())
            elif types == 4:
                # QoS 回执：data_content = 推送帧的 fp —— 登记
                with STATE.lock:
                    STATE.qos_acked.append({"fp": data_content,
                                            "ts": now_str()})
                    for p in STATE.pushes:
                        if p["fp"] == data_content:
                            p["acked"] = True
                log_line("%s|WS|qos-ack|types=4|fp=%s registered" % (
                    now_str(), data_content))
            else:
                log_line("%s|WS|recv-unhandled|types=%s|" % (now_str(), types))
    except Exception as e:
        log_line("%s|WS|error|%s|%r" % (now_str(), peer, e))
    finally:
        STATE.ws_clients.discard(ws)
        STATE.ws_logged_in.discard(ws)
        log_line("%s|WS|close|%s|" % (now_str(), peer))


def main():
    global _ws_loop

    # 请求日志新起一份（保留旧行为：追加更利于对账，这里选择追加）
    with _log_lock:
        with io.open(LOG_PATH, "a", encoding="utf-8") as f:
            f.write("%s|BOOT|fixture server start|jwt=%s|401_mode=%s\n" % (
                now_str(), JWT, USE_401))

    # 预热 round-trip JPEG
    b64 = ensure_roundtrip_base64()
    print("[fixture] round-trip jpeg ready: b64_len=%d jpeg_head=%s" % (
        len(b64), base64.b64decode(b64)[:4].hex().upper()), flush=True)

    api_srv = ThreadingHTTPServer((BIND_HOST, HTTP_PORT), ApiHandler)
    img_srv = ThreadingHTTPServer((BIND_HOST, IMG_PORT), ImageHandler)
    threading.Thread(target=api_srv.serve_forever, daemon=True).start()
    threading.Thread(target=img_srv.serve_forever, daemon=True).start()

    async def ws_main():
        global _ws_loop
        _ws_loop = asyncio.get_running_loop()
        async with websockets.serve(ws_handler, BIND_HOST, WS_PORT):
            print("[fixture] ws ready on ws://%s:%d/" % (BIND_HOST, WS_PORT),
                  flush=True)
            await asyncio.Future()

    print("[fixture] HTTP api on http://%s:%d/  images on http://%s:%d/" % (
        BIND_HOST, HTTP_PORT, BIND_HOST, IMG_PORT), flush=True)
    print("[fixture] account %s/%s  jwt=%s" % (ACCOUNT_USERNAME,
                                               ACCOUNT_PASSWORD, JWT), flush=True)
    print("[fixture] red line: bind %s only; never contact 8.137.196.28" %
          BIND_HOST, flush=True)
    try:
        asyncio.run(ws_main())
    except KeyboardInterrupt:
        pass
    finally:
        api_srv.shutdown()
        img_srv.shutdown()


if __name__ == "__main__":
    main()
