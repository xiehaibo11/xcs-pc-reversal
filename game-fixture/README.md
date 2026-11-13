# game-fixture — 游戏端本地 fixture 运行时验收

补齐审计 C 裁定的"游戏端零运行时验收"缺口：在**本地 fixture 环境**（只绑回环，绝不连
真实服务器 `8.137.196.28`）里，让真游戏端 `游戏端.exe`（exe4j + 随包 32 位 jre-9，包名
quesheng）**跑起来并登录成功**，且 UDP IM 通道完成协议级互通。

## 文件清单

| 文件 | 作用 |
| --- | --- |
| `game_server.py` | 游戏端 HTTP fixture：6 个 `/api/*` 端点，okhttp FormBody 兼容，bind 127.0.0.1:8081（冲突自动回退 127.0.0.3/127.0.0.5） |
| `udp_server.py` | MobileIMSDK UDP 7901 服务端（协议逆向实现，见下）：登录/心跳/登出/QoS ACK + **服务端 QoS 重传队列、接收去重、type=2 C→S、type=52 错误流、重登录**；`--reject-login` 回登录失败验证兜底分支 |
| `test_qos_client.py` | 合成 MobileIMSDK 客户端协议级自测（仅回环，测试端口 7911）：逐场景验证 QoS 重传/去重/ACK/错误流/重登录，6 场景矩阵，任一失败退出码 1 |
| `run_acceptance.ps1` | 一键真机验收：起 fixture → `tools/MakeSconfig` 生成 sconfig → 启动 exe → UIA 点登录 → 截图/收日志 → taskkill 清理（客户端寿命上限 100s < 120s 红线）。开关：`-RejectIM`（IM 登录拒绝）、`-PushTiles`（推 type=2 牌局帧+返回真实一局+自动点"获取"渲染+三张截图）；另有外部 watchdog 在 120s 强杀兜底 |
| `../tools/MakeSconfig.java` | sconfig.config 生成器/读取器（用 `game-jars/QueshengDaPai.jar` 真类序列化） |
| `evidence/` | 验收证据：两张主界面截图、客户端 error.log、HTTP/UDP 请求日志快照 |
| `runtime/`（gitignore） | 运行期产物：acceptance.log、requests.log、udp.log、*_stdout/stderr.log、http_ip.txt、udp_ip.txt |

## 运行方法

```powershell
# 前置（仓库根，一次）：编译 sconfig 生成器
javac -encoding UTF-8 -cp game-jars/QueshengDaPai.jar tools/MakeSconfig.java

# 一键验收（默认：IM 登录成功路径）
powershell -NoProfile -ExecutionPolicy Bypass -File game-fixture\run_acceptance.ps1

# 变体 1：IM 登录拒绝路径（验证 yiZhuo/get HTTP 轮询兜底）
powershell -NoProfile -ExecutionPolicy Bypass -File game-fixture\run_acceptance.ps1 -RejectIM

# 变体 2：推牌局（type=2/typeu=1 帧 → jiance → 返回真实 136 张一局 → 自动点"获取"全渲染）
powershell -NoProfile -ExecutionPolicy Bypass -File game-fixture\run_acceptance.ps1 -PushTiles
# 对照分支 = 默认模式（不推帧不点获取，UI 保持空桌）

# 单独起 fixture 调试
python game-fixture/udp_server.py --runtime-dir game-fixture/runtime
python game-fixture/game_server.py --runtime-dir game-fixture/runtime
# udp 可选：--push-tile-round --push-delay 3 --trigger-file PATH --reject-login
#           --echo-common（C→S type=2 回声） --enforce-login（未登录数据回 301）
#           --error-code 301 --error-trigger-file PATH（注入 type=52 错误流）
#           --qos-check-interval 5.0（QoS 巡检间隔，测试可调小）
# http 可选：--mj-configs（返回 42 张牌配置） / --yizhuo-sample（返回 136 张真实一局）

# QoS/去重/错误流/重登录 协议级自测（合成客户端，仅回环 7911，自动起停 fixture 子进程）
python game-fixture/test_qos_client.py            # 全 6 场景
python game-fixture/test_qos_client.py --only 2   # 只跑第 2 场景（QoS 重传→messagesLost）
```

sconfig 手工生成与读回校验（仓库根）：

```bash
java -cp "tools;game-jars/QueshengDaPai.jar" MakeSconfig gen out/sconfig.config 127.0.0.3 XCS232 ******
java -cp "tools;game-jars/QueshengDaPai.jar" MakeSconfig read out/sconfig.config
```

### 端口/IP 约束与冲突记录

- 客户端**硬编码 `:8081`**（`"http://" + DES(choseIp) + ":8081"`），端口不可改，只能换回环 IP。
- `127.0.0.1:8081` 被机器上一个第三方 **uvicorn** 服务占用（WinError 10013，`Server: uvicorn`，
  `allow: GET`），`127.0.0.2:8081` 是 PC 端 fixture——因此本 fixture 实际绑 **127.0.0.3:8081**，
  并把选中 IP 写入 `runtime/http_ip.txt`，`run_acceptance.ps1` 据此生成 sconfig 的 choseIp。
- UDP 7901 无冲突，绑 **127.0.0.1:7901**（`runtime/udp_ip.txt`），登录响应 `User.server_ip` 动态指向它。

## 验收结果矩阵（2026-11-12，Run A=默认路径 / Run B=-RejectIM）

| 步骤 | 结果 | 证据 |
| --- | --- | --- |
| sconfig 生成（真类序列化） | **PASS** | `MakeSconfig gen` 输出 choseIp=`40684AB7772B3560F147E9EF3F3AEABE`(DES(127.0.0.3))；`read` 读回：choseIp 解密=127.0.0.3、isFirstLogin=false、users={XCS232:******}，serialVersionUID=-6284005833985478852 一致 |
| exe 启动 | **PASS** | Run A 8s 内出现 UIA 窗口 `HDv2.1.0`(GlassWndClass)，按钮=[登　录/重置口令]；Run B 6s |
| UI 自动点登录 | **PASS** | UIA InvokePattern 命中"登　录"按钮（sconfig users 预填了 XCS232/*** 与记住密码勾选；代码确认无自动登录，必须点击） |
| HTTP 登录 | **PASS** | 客户端 POST `/api/login/login`，Form 与逆向合约逐字段一致：`ajax/deviceid=""/lastloginversion=v2.1.0/devicetype=3/password/username/loginfield=username/type=username/version=1.0.0.1`；fixture 回 `{success,message:User,jwt}`；后续请求自动带 `Authorization: Bearer fixture-jwt-…`（evidence/requests.log 11:16:43 / 11:21:02） |
| 主界面渲染 | **PASS** | 登录后切 IndexView（error.log 可见 goToMain→setStage 栈），截图 evidence/runA_38s_main_ui.png、runB_36s_main_ui.png：满屏透明风格窗口 + 麻将工具栏（提牌提示/吃碰/标/张/拿平/买马/分析/测试/取消/置顶庄/还原/结束/设置密码/重置软件/清空数据/服务载0·华0·闲0·西0）；jihuo_status=1 → 激活按钮隐藏 |
| OTA 检查 | **PASS** | POST `/api/config/GetLastVersion`，body `version=dapai32`（32 位 JRE 路径），带 Bearer jwt |
| UDP IM 登录 | **PASS** | Run A 11:16:46 客户端发 type=0 登录帧（loginUserId=`232_2`=user_id+"_2"，loginToken=HTTP jwt，extra=`232`），fixture 回 type=50 code=0；客户端 3s 后回 **type=4 QoS ACK**（回显我们的 fp）——协议实现正确（evidence/udp.log） |
| UDP 心跳 | **PASS** | 登录成功后客户端按 3s 周期发 type=1（dataContent={}），fixture 回 type=51，Run A 收到 8 次心跳全部应答 |
| IM 失败→HTTP 兜底 | **PASS**（Run B） | fixture 回 code=1 → 客户端 onLoginMessage(1)→observer 立即重发登录帧；**yiZhuo/get 轮询保持活跃**：+5s 首轮、每 10s 一轮（11:21:10/20/30），与代码 `scheduleWithFixedDelay(5,10,SECONDS)` 一致 |
| IM 成功→轮询关闭 | **PASS**（Run A） | 登录成功（code=0）后无任何 yiZhuo/get——对应 MainController.onLoginSucessObserver：`code==0 → jiance_service.shutdown()`。轮询只是 IM 断联时的兜底数据源 |
| 进程清理 | **PASS** | Run A 客户端寿命 38s / Run B 37s（<120s 红线）；taskkill /T /F 杀 exe 树与 fixture；结束后无 7901 监听、无残留 java/python |
| 客户端未崩溃 | **PASS** | 两次运行至 taskkill 时 `clientAlive=True` |

### 第二轮验收（2026-11-12，Run C=`-PushTiles` 推牌局渲染 / Run D=同流程不推帧对照）

| 步骤 | 结果 | 证据 |
| --- | --- | --- |
| type=2 帧下发 | **PASS** | 登录 ACK 后 3s，udp_server TX `type=2, typeu=1, to=232_2, fp=32561711…, QoS=true`（171 字节，hex 见 evidence/udp_type2_push_frame.txt）；客户端 1s 内回 type=4 ACK（fp 回显）——已接收并处理 |
| typeu=1 → jiance 触发 | **PASS** | 推帧同一秒（11:43:17）客户端发起 `POST /api/yiZhuo/get`（Bearer jwt），requests.log RunC 段 |
| jiance 计数渲染 | **PASS** | 截图 runC_1_pre_push.png（推帧前 **牌点数:0 东:0 南:0 西:0 北:0**）→ runC_2_post_push_counts.png（推帧后 **牌点数:136 东:34 南:34 西:34 北:34**，正是 fixture 下发的 4 方向×34 张） |
| yiZhuo/get 真实一局数据 | **PASS** | `--yizhuo-sample`：136 张（4 方向×34，mahjong_number 0..33，zhuo_id=1，serial_number/id 全局唯一），result 取 1-9/11-19/21-29/31-37 合法牌编码（字符串数字）；42 条 MjConfig number=url 映射（wan/tong/tiao/zi/hua 真实 jar 资源）全部可加载 |
| 全渲染链（huoqu→begingamge→displayWeinaPai） | **PASS** | UIA 点击"获取"（huoqu_btn，jihuo_status=1 闸通过）→ 第二次 yiZhuo/get（11:43:27）→ 牌面渲染：runC_3_post_render_tiles.png 顶/左两侧出现真实牌面图（万/筒/条/字 与下发编码一致）+ 摞层序号文本，与 displayWeinaPai 布局算法吻合 |
| 对照分支（不推帧=排除自渲染假阳性） | **PASS** | Run D 同流程无推送：udp.log 无 type=2、requests.log 无 yiZhuo/get，UI 保持 **牌点数:0** 空桌（runD_control_empty_board.png）；渲染差异只能来自推帧链 |
| 渲染期无异常 | **PASS** | error.log 在 Run C 渲染时段无新增异常（仅既有 JIntellitype/UIA-WinAccessible 噪声） |
| 进程清理（第二轮） | **PASS** | Run C 寿命 39s/Run D 38s；新增外部 watchdog（120s 强杀）后未再触发 |

### 第三轮：QoS 全机制协议级自测 + 真机回归（2026-11-12 下午）

补齐诚实边界 item 3 的四个缺口（type=2 C→S、type=52 错误流、QoS 重传队列、AutoReLogin 全周期）。
服务端算法逐常量镜像反编译的 `QoS4SendDaemon`/`QoS4ReciveDaemon`（见下节）。合成客户端
`test_qos_client.py` 逐场景起停 fixture 子进程（独立临时 runtime、回环测试端口 7911），6/6 PASS：

| # | 场景 | 结果 | 关键证据 |
| --- | --- | --- | --- |
| 1 | 登录 50(QoS+fp)+心跳 51+ACK 移队列 | **PASS** | type=50 QoS=true 带 fp；type=4 ACK 后日志 `messagesBeReceived type=50`（队列移除） |
| 2 | 抑制 ACK→QoS 重传至上限→messagesLost | **PASS** | 收到 **3 帧同 fp** type=2（首发+2 重传），日志 `messagesLost`；巡检 1s 加速（SDK 默认 5s） |
| 3 | 首次重传后补 ACK→中止重传不判丢 | **PASS** | 仅 **2 帧**（首发+1 重传）后停止；日志 `messagesBeReceived type=2` 且**无** messagesLost |
| 4 | C→S type=2 同 fp 去重 | **PASS** | 同 fp 发两遍→**2 次 ACK 但只 1 次业务回声**；日志 `重复包(去重命中)` |
| 5 | type=52(301)+AutoReLogin 重登录全周期 | **PASS** | 会话中注入 301→客户端停心跳重登录→**新 fp** 登录应答→心跳恢复；日志 `重登录` |
| 6 | enforce-login 未登录心跳→301 | **PASS** | 未登录先发 type=1→回 301；登录后 type=50+心跳 51 正常 |

真机回归（增强 fixture 后重跑，确认不破坏既有链路）：

| 运行 | 结果 | 证据 |
| --- | --- | --- |
| Run A 默认路径 | **PASS** | 登录应答入 QoS 队列(fp=96257cf0)→客户端 ACK `messagesBeReceived type=50 retry=0`（**无误重传**）；9 次心跳全应答；寿命 40s |
| Run C `-PushTiles` | **PASS** | type=2/typeu=1 推帧入 QoS 队列(fp=7ff30f2c)→ACK `retry=0` 移除；jiance 计数 **136/34×4**；huoqu 全渲染牌墙（runC_3）；寿命 34s |

两次回归均 <120s 红线、clientAlive=True、结束后无 7901/7911 监听与残留 fixture 进程。
自测日志：`runtime/qos_selftest.log`。

## MobileIMSDK UDP 协议逆向发现（已实现）

载荷 = `net.openmob.mobileimsdk.server.protocal.Protocal` 的 **Gson JSON 的 UTF-8 字节**，
UDP 数据报无帧头/长度前缀（`Protocal.toBytes()=CharsetHelper.getBytes(toGsonString())`）。

字段（Gson 按名解析；null 省略；字段序=类声明序 bridge,type,dataContent,from,to,fp,QoS,typeu）：

```
客户端登录帧（真实抓包 hex 前缀见 evidence/udp.log）：
{"bridge":false,"type":0,"dataContent":"{\"loginUserId\":\"232_2\",\"loginToken\":\"<jwt>\",\"extra\":\"232\"}","from":"232_2","to":"0","QoS":false,"typeu":-1}
服务端登录应答（QoS=true 必带 fp，客户端会回 type=4 ACK）：
{"bridge":false,"type":50,"dataContent":"{\"code\":0}","from":"0","to":"232_2","fp":"<uuid>","QoS":true,"typeu":-1}
客户端 QoS ACK：type=4，dataContent=<被确认包的 fp>
客户端心跳：  {"type":1,"dataContent":"{}","from":"232_2","to":"0",...}（KeepAliveDaemon 周期 3000ms，10s 无应答判掉线）
服务端心跳应答：type=51，dataContent="{}"
登出：type=3；普通消息：type=2；错误：type=52 dataContent={"errorCode":..,"errorMsg":".."}（301=未登录→客户端停心跳重连）
```

登录名规则：`<user_id> + "_2"`；token=HTTP 登录返回的 jwt；extra=`<user_id>`；appKey
`5418023dfd98c579b6001741`（ConfigEntity，服务端 fixture 未校验）。服务端地址来自登录响应
`User.server_ip`，端口固定 7901。

### S→C type=2 业务推送帧（typeu=1 → jiance，第二轮实机验证）

```
{"bridge":false,"type":2,"dataContent":"{\"action\": \"jiance\", \"zhuo_id\": 1}","from":"0","to":"232_2","fp":"<uuid>","QoS":true,"typeu":1}
```

- 帧序（真机时序，Run C 11:43:13→11:43:17）：登录 ACK（type=4）到达 → 服务端等 `--push-delay`（默认 3s）→ 下发 type=2 → 客户端 **1s 内回 type=4 ACK**（QoS 回执，dataContent=帧指纹）→ 同一秒发起 yiZhuo/get。
- 客户端接收路径（反编译 LocalUDPDataReciever$MessageHandler case 2）：`onTransBuffer(fp, from, dataContent, typeu)` → 游戏 `ChatTransDataEventImpl` 只判 **typeu==1** → `mainGUI.jiance()`。**dataContent 内容被忽略**（fixture 发任意 JSON 串即可）；`from` 客户端侧不校验（发 "0" 即可——逆向⑤ §11 U2 的 UNVERIFIED 项在客户端侧就此关闭；服务器白名单行为仍未知，本地 fixture 无此层）。
- 164/171 字节 hex 样例与完整时序证据：`evidence/udp_type2_push_frame.txt`。

### 服务端 QoS 机制（镜像 QoS4SendDaemon / QoS4ReciveDaemon，第三轮实现）

反编译 `MobileIMSDK4j.jar` 逐常量对齐（`.tools/game-im-decompile/.../core/QoS4*.java`）：

- **发送重传队列 QoS4SendDaemon**：`CHECH_INTERVAL=5000`(5s 巡检)、`MESSAGES_JUST$NOW_TIME=3000`
  (距首发<=3s 视为"刚刚"不重传)、`QOS_TRY_COUNT=2`(最多重传 2 次)。`put(p)` 要求 fp!=null && QoS。
  巡检：`retryCount>=2`→`messagesLost`+remove；否则 delta<=3s 跳过；否则重传**同一 fp** 包并
  `retryCount++`。**重传不刷新首发时间戳**，故 5s/10s 各重传一次、15s 判丢（首发+2 重传=3 帧）。
  收到 type=4 ACK(dataContent=fp)→`remove(fp)`(messagesBeReceived)。fixture 对所有 QoS=true 外发包
  （登录应答 50、业务推送 2、回声 2）入队跟踪。`retryCount` 是 Protocal 的 **transient** 字段，不上线。
- **接收去重 QoS4ReciveDaemon**：`CHECH_INTERVAL=300000`(5min 清理)、`MESSAGES_VALID_TIME=600000`
  (10min 有效)。重复 fp→更新时间戳+重发 ACK，但**不再处理业务**（客户端 MessageHandler 对重复
  QoS 包 sendRecievedBack 后 return）。fixture 对 C→S QoS 包同构去重。
- **type=52 错误流**：`createPErrorResponse` QoS=false；`errorCode=301`(RESPONSE_FOR_UNLOGIN)→
  客户端 setLoginHasInit(false)+KeepAliveDaemon.stop()+AutoReLoginDaemon.start(2s 重登录)。
  fixture 用 `--error-trigger-file`/`--enforce-login` 注入；重登录(type=0)幂等受理、发新 fp 应答。
- **心跳/重登录常量**：KeepAliveDaemon `KEEP_ALIVE_INTERVAL=3000`/`NETWORK_CONNECTION_TIME_OUT=10000`；
  AutoReLoginDaemon `AUTO_RE$LOGIN_INTERVAL=2000`。登录应答 `createPLoginInfoResponse` **恒 QoS=true+自动 fp**。

## sconfig.config 生成器要点

- 直接 `new quesheng.model.SystemConfig()`（真类，serialVersionUID=-6284005833985478852）+
  ObjectOutputStream；~150 个玩法字段全部沿用类内默认值（int=0、`tiaopai=隔跳`、
  `gangfa=轮到自由杠(手动杠花)`、`mopai=空格键`、`chexiao=z`、`zidingyibeijingyanse=[0,0,0]` 等），
  即"首次安装未调设置"状态。
- 覆盖字段：`choseIp=DES(host)`、`isFirstLogin=false`（隐藏 IP 输入框）、
  `users={DES(user):DES(pass)}`（登录页预填并勾选记住密码）。
- DES：DES/CBC/PKCS5Padding，key=IV=`"nihao985"` 前 8 字节，输出大写 HEX（quesheng.util.DESKit）。
- 验证：`MakeSconfig read` 读回解密打印（见上文矩阵第一行）。
- 客户端在登录成功后会把 users/isFirstLogin **重写**回 sconfig.config（它自己的类序列化，
  与生成器兼容，两种写法可互相读取）。

## game_server.py 端点清单（响应结构自客户端消费代码反推）

| 端点 | 消费方（逆向源） | fixture 响应 |
| --- | --- | --- |
| POST /api/login/login | LoginController.userLogging：读 `success`/`message`(User)/`jwt` | `{"success":true,"message":{User…},"jwt":"fixture-jwt-…"}`；User.server_ip=UDP fixture IP，jihuo_status=1，salt/password 设为非默认组合避开"默认密码"弹窗（sha256(salt+"******")≠password） |
| POST /api/mj/getConfigs | Config.initialize：读 `success`/`list`(MjConfig[id,number,name,url]) | 默认 `{"success":true,"list":[]}`（消费代码对空表安全：不加载牌图、SettingStageController.init2 空转）；`--mj-configs` 返回 42 条配置，number=真实牌编码 1-9/11-19/21-29/31-37/41-48，url=jar 内实存资源 /sucai/{wan,tong,tiao,zi,hua}/N.png（Config.initialize 按同下标加载 mjImages，任何一条 url 无效都会 NPE→列表截断→编号错位，已全量校验有效） |
| POST /api/config/GetLastVersion | MainController onShown：读 `success`,`versionCode`,`name`,`url` | `{"success":true,"versionCode":0,"name":"","url":""}`（0 < 客户端 version_code=1 → 不触发下载） |
| POST /api/yiZhuo/get | MainController.jiance/huoqu：读 `success`,`list[]` 每项 `serial_number,id,mahjong_number,direction,zhuo_id`(int) + `result`(字符串数字) | 默认 `{"success":true,"list":[]}`；`--yizhuo-sample` 返回**真实一局 136 张**（4 方向×34，mahjong_number 0..33 两层摞，direction 0=东/1=南/2=西/3=北，result 为合法牌编码字符串，serial_number/id 全局唯一） |
| POST /api/user/updateSelf | MainController.jihuo | `{"success":true,"message":"ok"}` |
| POST /api/user/updatePassword | ChangePasswordDialog | `{"success":true,"message":"ok"}` |

请求均为 okhttp FormBody（`application/x-www-form-urlencoded`）+ `Authorization: Bearer <jwt>`
（登录请求 jwt 为空串）。

### 与逆向④/逆向⑤表述的核对结论（第二轮）

- **与逆向⑤ §5/S10（:444）无出入、实机确认**：`jiance` 只做"计数标签+闪烁告警"（牌点数/东/南/西/北），
  **不重绘牌墙、不调 begingamge**；typeu=1 推送触发的正是 jiance；全渲染必须人工（或自动化）点
  "获取"（huoqu→yiZhuo/get→jieshu+weina.add+begingamge→displayWeinaPai）。Run C 三张截图
  （0 计数 → 136/34×4 计数 → 牌面渲染）逐帧印证该表述。
- **需修正的范围在逆向④ §6.2 与 UI设计② §7.2**：其"IM 收到 typeu==1 推送立即 jiance() 拉取并重绘"
  中"重绘"两字不成立（docs/ 目录本轮任务禁改，仅在此记录；逆向⑤ :1130 已有同样的修正建议）。
  另逆向④ 若仍写"游戏端无 yiZhuo 调用/无 zhuo/* 调用"，其中 yiZhuo 部分已被逆向④ 自身的 :203 行
  自纠，本次实机再次确认该端点在推帧与获取两条链上都被消费。
- 逆向⑤ §11 U2（typeu=1 帧 from/dataContent 是否被校验）**客户端侧已实证关闭**：from="0"、
  dataContent 任意 JSON 均被接受（服务端白名单行为仍超出本地 fixture 能力）。

## 诚实边界

1. **UI 点击靠 UIA 自动化**：代码确认 `LoginController.initialize()` 无自动登录（只预填表单），
   必须点击"登　录"。run_acceptance.ps1 用 UI Automation InvokePattern 完成（JavaFX 9 暴露
   UIA 树），未验证手工鼠标路径。
2. **牌编码/牌图映射为高置信推断而非生产样本**：42 条 MjConfig 的 number 采用 1-9(万)/11-19(筒)/
   21-29(条)/31-37(字)/41-48(花)（逆向⑤ §8 语义 + ComparatorMjResult 的 52→38/53→34.5 插值锚点 +
   sucai 目录结构三方互证），**万/筒/条牌面已实机渲染核对无误**（runC_3 截图）；字牌 31-37 与
   sucai/zi 1-7 的具体对应顺序（31=东还是中）花牌 41-48 与 sucai/hua 的对应未逐一比对（136 局
   不含花牌，字牌仅占少数列，即使有错位也只影响个别字牌花色归属，不影响链路验证）。
3. **UDP 服务端已实现：登录(0/50)、业务推送(2/typeu=1)、心跳(1/51)、QoS ACK(4)、登出(3)，以及第三轮
   补齐的 type=2 C→S 普通消息(去重+ACK+可选回声)、type=52 错误流(301)、服务端 QoS 重传队列、
   AutoReLogin 重登录全周期**（`test_qos_client.py` 6/6 PASS + Run A/C 真机回归，见第三轮矩阵）。
   仍超出本地 fixture 能力：真服务端 MobileIMSDKServerX 的 TCP/多节点桥接、服务器侧 from 白名单。
4. **IM 登录成功后客户端主动停掉 yiZhuo/get 轮询**（onLoginSucessObserver → jiance_service
   .shutdown()）——Run A 中"无 yiZhuo/get 请求"是**正确客户端行为**而非 fixture 缺陷；兜底
   分支由 Run B（code=1 拒绝）单独验证。
5. **error.log 中的既有缺陷（与本次验收无关、不阻塞）**：
   - JIntellitype `Can't load AMD 64-bit .dll on a IA 32-bit platform`：包内 jintellitype 只带
     64 位 DLL，随包 JRE 是 32 位 → 全局热键注册每次焦点变化抛一次异常（游戏功能不受影响）。
   - 12 条 `WinAccessible.getUnignoredChildren` NPE：UIA 自动化遍历控件树时 JavaFX 可访问性
     层的副作用，非游戏逻辑。
6. **运行期痕迹**：游戏端会在沙箱目录写 `sconfig.config`（登录成功后重写 users）、可能在
   首次使用设置/数据库功能时建 `quesheng.db`（本次未触发，目录内未出现）。沙箱目录
   `.sandbox-game/` 整体已 gitignore。
7. **机器依赖**：127.0.0.1:8081 被第三方 uvicorn 占用、127.0.0.2:8081 归 PC fixture——本
   fixture 固定回退到 127.0.0.3。若该机端口占用格局变化，脚本自动按 127.0.0.1→3→5 选择并
   同步生成 choseIp，无需手工改配置。
8. 本次验收**全程未出现任何到 8.137.196.28 或其他非回环地址的连接**；所有绑定均在
   127.0.0.1–127.0.0.5 回环内。
9. **一次红线超时事故（已修复并加固）**：第二轮首次 Run D 时客户端启动异常缓慢（40s 内只有
   JavaSplash），脚本的无防护 `AppActivate` 在 observe 段挂死，导致该次客户端存活远超 120s
   后才被人工发现并 taskkill。修复：UIA 等待窗延至 70s、Shot 前先 UIA 确认存在有标题窗口再
   激活、SendKeys 兜底加前置条件，并新增**独立外部 watchdog**（exe 启动时随启，120s 仍存活即
   taskkill /T /F，不受脚本挂死影响）。加固后 Run C/Run D 客户端寿命分别 39s/38s。

## 交互实测补录（2026-11-13，主代理 computer-use + 扩展开关）

| 测试 | 方法 | 结果 |
|---|---|---|
| QoS 同 fp 去重 | udp_server 新增 `--dup-push N`（同 fp 原样补发）→ 1 原发+2 补发+1 次队列重传=**客户端收到 4 次** | **PASS**：客户端回 4 次 type=4 ACK，但 yiZhuo/get 仅 +1（基线2→3）——客户端按 fp 去重，业务只处理一次 |
| 服务端 QoS 重传队列 | 同上运行轨迹（"未收到ACK，重传#1 距首发4.0s"→ACK 到达→队列移除） | PASS（镜像 SDK QoS4SendDaemon 语义） |
| 花牌 41-48 映射 | game_server `GF_FLOWERS=1`（东方混 8 张花牌）→ 获取 → 渲染 | PASS：底墙出现 sucai/hua 花牌图（interactive_flower_round.png） |
| 旋转 | 主界面 旋转 按钮 | PASS：庄位菱形 90° 轮转+四面牌墙重排（像素对比） |
| 修改密码 | 修改密码 按钮 | PASS：弹出"修改密码XCS232"对话框 |
| 直发帧来源过滤 | python 裸 socket 直发同构帧到客户端端口 | 客户端静默丢弃、无 ACK——**客户端校验帧来源=服务器地址**（新发现的协议安全语义） |

运维注记：udp_server 重启后客户端不会自动重登录（新 fixture 会应答其心跳使其满足于连接）；
需发 301/52 或 `--error-trigger-file` 逼其 `onErrorResponse → 自动重登录`（本轮已用此法）。
