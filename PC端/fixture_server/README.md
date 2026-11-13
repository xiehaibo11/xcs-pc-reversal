# XCS PC 图传客户端 —— 本地 fixture 服务器

单进程三端口，完整模拟 XCS PC 客户端（`analysis/xcs-cs112` WPF 还原工程）依赖的业务后端，
供后续真机 GUI 验证使用。**只绑定本机回环地址，绝不连接真实服务器 8.137.196.28，
不运行仓库中任何 exe。**

## 运行方法

```bash
# 依赖（一次性）
python -m pip install --user websockets cryptography

# 前台运行
python server.py

# 后台运行（Windows）
start /b python server.py

# 端到端自测（8081 未起会自动拉起 server 子进程，跑完自动停止）
python selftest.py
```

### 登录地址必须填 127.0.0.2（重要）

本机 Docker 发布了 `127.0.0.1:8081`（容器 `aiquant-live-bot`），而客户端把业务端口
`:8081` **硬编码**在 `HttpReq.cs:23`，无法改端口。fixture 因此整体绑定在回环别名
**127.0.0.2**（127.0.0.0/8 全部直达 loopback，Windows 无需任何配置），与 Docker
零冲突共存：

- GUI 登录窗口"口令地址"填 **`127.0.0.2`**
- `get_server_addr` 下发的业务 IP 也是 `127.0.0.2`，客户端后续 HTTP/WS/图片全部回到 fixture

可用环境变量 `FIXTURE_HOST=127.0.0.1` 切回 127.0.0.1（前提是 8081 已空出）。

| 端口  | 用途 |
|-------|------|
| :8081 | HTTP JSON 业务 API（GET/POST 一律 `/api/` 前缀；**例外**：`/account/edit/pwd` 无前缀，见 `HttpReq.cs:24`） |
| :80   | 图片文本服务（客户端 `FullImgUrl` 拼 `http://{业务IP}/{url}`；返回**单行文本**） |
| :18889 | WebSocket（`ws://127.0.0.2:18889/`，Protocol JSON 帧） |

## 账号凭据

| 项 | 值 |
|----|-----|
| 账号 | `XCS232` |
| 密码 | `******` |
| JWT | `FIXTURE-JWT-XCS232-0001`（环境变量 `FIXTURE_JWT` 可覆盖） |
| dataKey | `A,a`（get_server_addr 下发，图片/预览帧编码规则） |
| devicetype | `5`（PC 端标识，校验值） |

鉴权：除 `login/login/distinguish`、`server/get_server_addr` 外全部校验
`Authorization: Bearer <jwt>`。错误返回 `{"success":false,"msg":...,"message":...,"code":500}`
—— **必须带 `code` 键**：`HttpReq.cs:38` 对每个失败响应用短路 `&&` 读
`dictionary["code"]`，缺失会 KeyNotFoundException 崩掉调用线程而不是弹错误框。
`FIXTURE_401=1` 环境变量可切换成 `code:401` 形态（客户端弹"登录已失效"分支，可选开关，默认关闭）。

## 与客户端行为的两处刻意偏差（源码为准）

1. **设备序列号是数字**（`20250101`~`20250104`，末两位对应东南西北），
   不是任务原案的 `SN2025E/S/WE/N`：`HomeViewModel.SendData` 构造
   `SaveMj(serial_number=int)` 时执行 `int.Parse(upChannel.Serial_number)`
   （`HomeViewModel.cs:3189`），非数字序列号会在自动发送线程里静默抛
   FormatException，GUI 全自动回合测试将无法发出 `zhuo/add`。
2. **图片 base64 特意不含字母 `a`**（开发期手工构造的 159 字节最小基线 JPEG，
   8x8 灰度、已验证可解码）：`dataKey="A,a"` 的解码端 `GetDataBash64` 做的是
   全文 `Replace('a','A')`，若原 base64 含原生 `a` 会被一并翻成 `A` 导致图片损坏；
   原文不含 `a` 时编码 `'A'->'a'` / 解码 `'a'->'A'` 严格互逆，JPEG 可无损还原。

## 协议合约来源（字段名逐字核对）

- `distinguish.Model/`：`LoginResp`、`LoginUser`、`Device`、`Protocol`、
  `ProtocolDataContent`、`ProtocolDataContentImg`、`SaveMj`、`ConfigNumber`、
  `WifiConfig`、`DataConfig`
- `distinguish.Common/`：`HttpReq`、`Global`（GetDataBash64/FullImgUrl）、
  `CryptionUtils`（RSA-2048 e=3 + PKCS1）、`NetManager`、`Constant`（帧类型 1/2/3/4/6→50/51/52/301）
- `distinguish.ViewModel/`：`LoginViewModel`、`HomeViewModel`（含 `OnValidData`
  校验规则与 101/102/113~117 处理）、`OnlineConfigViewModel`、
  `PreviewSettingViewModel`、`EditPwdViewModel`

## HTTP 端点清单（:8081）

| 端点 | 方法 | 请求体 / 说明 | 成功响应 | 失败读法（客户端） |
|------|------|----------------|----------|---------------------|
| `/api/login/login/distinguish?app_name=xcs` | POST | `{"username","password","devicetype":5}`，仅接受配置账号 | `success/jwt/LoginUser{...Timeout:15}/Devices[4]` | `msg` |
| `/api/server/get_server_addr` | POST | `{"key":<SPKI DER b64>,"client_type":5}`；服务器 PKCS1v15 加密 `["127.0.0.2","A,a"]` → b64 放 `msg` | `success/msg` | `msg` |
| `/api/zhuo/start_games` | GET | 首次成功，状态置"进行中" | `success/message` | `message` |
| `/api/zhuo/end_games` | GET | 未开始时 `success:false`（业务态模拟） | `success/message` | `message` |
| `/api/zhuo/zhuo_reset_games` | GET | 重置为未开始 | `success/message` | — |
| `/api/zhuo/restart_device` | GET | — | `success/message` | `message` |
| `/api/zhuo/device_status_all` | GET | — | `data:[{serial_no,ip,online}]` | `message` |
| `/api/zhuo/device_status/{id}` | GET | id=1..4 | `data:{ip,online}` | `message` |
| `/api/zhuo/add` | POST | `[{"mahjong_number","serial_number"(int),"direction","result"}...]` **104 张**；校验镜像 `OnValidData`：总数=4×mj_count、每方向=mj_count、每个 <40 编码恰好 4 次 | `{"success":true}`，牌局追加进 `rounds.jsonl` | `msg` |
| `/api/app/check_update?type=3&version=...&app_name=xcs` | GET | 恒 `success:false`，不触发 OTA 下载 | — | — |
| `/api/config/get_config` | GET | — | `data:{config_number{East..},wifi_config{Ssid,Pwd},data_config{Data_send,...}}` | `message` |
| `/api/config/get_device_wifi_list` | GET | — | `data:[{ssid,strength}]` | — |
| `/api/config/set_number` | POST | `{"east","south","west","north"}`（2..52 偶数） | `success` | `msg` |
| `/api/config/set_data_config` | POST | 原版 4 键 `{"data_send","timeout","delay","count_type"}`；当前真实服务器另需 `upload_img_clarity`，还原工程经批准补发（值 0），本 fixture 容忍该兼容键 | `success` | `msg` |
| `/api/config/set_wifi` | POST | `{"ssid","pwd"}`（≥8 位） | `success` | `msg` |
| `/api/preview/start_preview/{id}` | GET | 标记预览态 | `success/message` | `message` |
| `/api/preview/end_preview/{id}` | POST | body 为空 | `success/message` | — |
| `/api/preview/set_params_v2/{id}` | POST | 原版合约 15 字段：`top/left/right/bottom/brightness/motor/cap_brightness/cap_exposure/cap_hue/cap_saturation/cap_contrast/cap_gain/img_rotation/img_min_hreshold/cap_fps`，全 int 且 right>left、bottom>top。当前真实服务器另需 `bate/img_add_num/gk_min/gk_max/upload_img_clarity/count_type`（serde 缺键即拒绝），还原工程经批准按兼容性适配补发（值 0）；本 fixture 建模原版协议，仅校验必需 15 字段，容忍额外兼容键 | `success` | `msg` |
| `/account/edit/pwd`（**无 /api 前缀**） | POST | `{"old_password","password"}`，旧密码须 `******` | `success:true` | `msg` |

### 管理端点（供 selftest / GUI 驱动脚本触发推送，同样只在本机回环可达）

`GET /_fixture/status`（连接数/推送记录/QoS 登记/配置态）、`POST /_fixture/reset`、
`GET /_fixture/img`（当前 fixture JPEG 的 base64 元数据）、`GET /_fixture/ping`，以及推送触发器：

```
POST /_fixture/push/101  {"serial_number","mahjong_number","ai_val","ai_precision","img"}
POST /_fixture/push/102  {"device_id","b64"?}     # b64 缺省用 fixture JPEG 自动编码
POST /_fixture/push/113  {"serial","run"}
POST /_fixture/push/114  {"serial"}   # 115 离线 / 116 在线 / 117 洗牌 同形
POST /_fixture/push/301  {}           # 触发客户端断线重连逻辑
```

## WebSocket 协议（:18889）

帧 = Protocol JSON：`{types, data_content, from, to, fp, qos, retry_count}`。

| 方向 | types | 含义 | fixture 行为 |
|------|-------|------|--------------|
| 客户端→服务器 | 6 | 登录（data_content=jwt） | 校验后回 **50**（jwt 不符回 52） |
| 客户端→服务器 | 1 | 心跳（5 秒一次） | 回 **51**（客户端据此算 DelayTime） |
| 客户端→服务器 | 3 | 登出 | 记录并摘除登录态 |
| 客户端→服务器 | 4 | QoS 回执（data_content=推送 fp） | 登记进 `/_fixture/status` 的 `qos_acked`，对应推送标 acked |
| 服务器→客户端 | 2 | 业务推送（`data_content` = JSON 串 `{"msg_type":N,"msg_content":"..."}`），带 `qos:true` + Guid 风格 `fp`，`from="server"` | N=101 图牌（msg_content 为 ProtocolDataContentImg JSON）/102 预览帧 `"<deviceId>|<编码后base64>"`/113 `"<serial>|<run>"`/114 停止/115 离线/116 在线/117 洗牌 |
| 服务器→客户端 | 301 | 未登录码 | 客户端收到后自动断开重连 |

## 图片编码管线（:80）

`GET /{url}` 返回**单行文本** = `encode_datakey(base64(JPEG))`（即 `'A'→'a'` 替换后的
base64）。客户端 `DownloadUrlImg` 读首行 → `Global.GetDataBash64` 反替换
（`'a'→'A'`）→ `Convert.FromBase64String` → `Bitmap` 解码。fixture 的 JPEG base64
不含 `a`，保证严格无损往返（selftest 已验证 FF D8 头/FF D9 尾）。

## 运行期生成物（不跟踪）

- `requests.log` —— **每个收到的请求一行**：
  `时间|HTTP|方法 路径|Body摘要|状态码 响应摘要`，WS 帧同格式（`WS|recv|types=N` /
  `WS|send|types=N` / `server-push`）。GUI 测试按"按键→请求"对账全靠它。
- `rounds.jsonl` —— 每一局通过校验的 104 张牌局明细。
- `selftest_server.log` —— selftest 自动拉起子进程时的 stdout。

## 自测验收（selftest.py 输出）

`python selftest.py` 全部 81 项 PASS、0 FAIL（2026-11-12 实测）：

1. pip 依赖（websockets 17.4 / cryptography 50.0.2）
2. 登录正确/错误账号两分支（错误 `success=false`+`msg`；正确 `jwt`、`LoginUser.Timeout=15`、4 台 Devices）
3. 生成 RSA-2048 e=3 密钥对 → `get_server_addr` → 私钥解出 `["127.0.0.2","A,a"]`
4. WS 连接 → types=6 收 50 → types=1 收 51 → 收到 `qos:true` 推送 → 回 types=4 → 服务端登记确认；另验 301 推送
5. 101 管线：真实经 WS 推送图牌帧 → fetch 图片 URL → 反替换 → base64 → FF D8/FF D9 校验、无损往返
6. `zhuo/add` 104 张合法牌局 `success:true` + 写入 rounds.jsonl；103 张 `success:false` + `msg`
7. `/account/edit/pwd` 无 `/api` 前缀路由正确命中（带前缀反而失败）
8. requests.log 每个用例都有对账行

## 红线遵守

- 只 bind 回环地址（127.0.0.2，属 127.0.0.0/8 loopback）
- 全程未连接真实服务器 8.137.196.28（客户端源码中也没有硬编码该地址，业务 IP 全部来自登录输入与 get_server_addr 下发）
- 未运行仓库中任何 exe
