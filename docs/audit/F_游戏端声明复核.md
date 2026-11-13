# F 游戏端声明复核（对抗性第二轮审计）

> 审计人：对抗性审计员（第二轮复核）。日期：2026-11-13。
> 范围：`docs/逆向⑤_游戏端语义还原.md`（1382 行）行号抽验与诚实性、game-fixture 运行验收声明、源地址过滤声明。
> 方法：只读复核 + 独立取证（CFR 0.152 反编译 `game-jars/MobileIMSDK4j.jar` 至系统临时目录 `/tmp/imsdk-decomp`，未入仓；Java ImageIO 裁剪放大截图核对）。未改任何文件；未运行游戏端 exe；未联网。

**结论行：复核：6 证实 / 2 证伪（子声明级，无整项证伪）/ 4 部分 / 1 UNVERIFIED。**
（A 类 17 处行号抽验全部命中；C 判定：**证实**——过滤真实存在，但机制是 `LocalUDPDataSender.send()` 的 `DatagramSocket.connect(serverIP,7901)` 由 OS 过滤，不在接收循环。）

---

## 1. A 类：逆向⑤ 行号引用抽验（17 处，覆盖 §1/§2/§4/§5/§8/§9）

抽验方式：逐条打开 `game-src/` 对应文件核对所述内容是否在该行。结果 **17/17 命中，0 错引**（含 2 处表格级微偏差，见 §1.2）。

### 1.1 抽验明细表

| # | 文档引用 | 章节 | 核对点 | 实测 | 判定 |
|---|---|---|---|---|---|
| 1 | MainApp.java:50-52 | §1.1 | `main()` → `launch()` | 50 声明 / 51 `MainApp.launch` / 52 `}` | 命中 |
| 2 | MainApp.java:42-48 | §1.1 | `stop()` = sendLoginout + resetInitFlag + release + exit(0) | 42-48 逐行一致 | 命中 |
| 3 | StageController.java:28 | §1.2 | `HashMap<String,Stage> stages` | :28 原文一致 | 命中 |
| 4 | StageController.java:71-75 | §1.3 | `setStage(show,close)`=先 close 再 show | :71-75 一致 | 命中 |
| 5 | LoginController.java:101-104 | §2.1 | getObject 强转；null→`new SystemConfig()` | :101/:102-104 一致 | 命中 |
| 6 | LoginController.java:179 | §2.4 | 拼 `":8081"+"/api/login/login"` | :179 一致 | 命中 |
| 7 | LoginController.java:213-214 | §2.2 | `port_regex`/`regex` 定义后无引用（死代码） | grep 全文件仅定义处命中 | 命中 |
| 8 | DESKit.java:14, :31-32, :48 | §2.2 | key="nihao985"；Encrypt IV=`keySpec.getKey()`(:32)；Decrypt IV=keyBytes(:48) | 逐行一致（key=IV 结论成立） | 命中 |
| 9 | MainController.java:837-851（:843） | §2.1/§4 S3 | guize_btn onCloseRequest 中 `ObjectKit.WriteObject`(:843)+牌宽重算(:844-849) | :837-851 一致 | 命中 |
| 10 | MainController.java:295-304, :313-321 | §4 S2 | onShown 新线程 POST GetLastVersion，form ajax+version=dapai64/32；versionCode>1 → saveUrlAs | :295 `new Thread`、:299-304 arch 判断、:315-317 读三字段、:318-321 下载弹窗（文档写 :313-317，实读自 :315，容差内） | 命中 |
| 11 | MainController.java:926-937 | §4 S3 | IM 登录回调 code==0 → `jiance_service.shutdown()` | :926/:931-932 一致 | 命中 |
| 12 | MainController.java:3898, :3920-3925, :3930-3936 | §4 S10/§9 | huoqu POST yiZhuo/get；6 字段读取，`result` 为字符串再 parseInt；本地重编 mahjong_number | 逐行一致（:3920-:3925 六键、:3925 parseInt） | 命中 |
| 13 | MainController.java:4033, :4044-4046 | §4 S10 | jiance 同 URL；list==null 静默 return | :4033/:4044-4046 一致 | 命中 |
| 14 | LiangCengJiImpl.java:84 | §5.1 | `MainController.weina = new LinkedList()` 重建渲染序 | :84 原文一致 | 命中 |
| 15 | SystemConfig.java:14-15；字段区 :18-162 | §8.1 | serialVersionUID=-6284005833985478852；users=LinkedHashMap；niezhuapai 在 :98、pingna 在 :40 | :14/:15 一致；:98 niezhuapai、:40 pingna 一致 | 命中 |
| 16 | Mj.java:21-23, :26, :209-211, :217-224 | §5/§8.3 | napai/yuce/chupai_direction 缺省 -1；paizhuantai=0；getZhiBiaoZHi；hashCode()=0；equals 五元组 | 逐行一致 | 命中 |
| 17 | Config.java:43, :463, :465-466, :476-482, :539-565；param.java:15/:20；ChangePasswordDialog.java:80/:90-102；IMClientManager.java:32-47；ChatTransDataEventImpl.java:18-20 | §3/§9 | config_file_url；getConfigs URL/空 onFailure/装载 mjImages；version_name/version；updatePassword 表单+1s 后 exit；appKey/serverIP/7901/localUDPPort=0；typeu==1→jiance | 全部逐行一致 | 命中 |

### 1.2 微偏差（不影响结论，记录备查）

- §2.1 表称"全代码只有 **5 处** `ObjectKit.WriteObject`"：grep 实测 **6 条语句 / 5 组触发点**（LoginController:115、:156、:197、:200，MainController:459、:843）——按"处=语句"计应写作 6，按"触发时机"计为 5（文档表即 5 行触发）。
- §5.3 表（文档 :621）行名"result==35 在 112 张模式被改为 35（即"发财"）"为**笔误**：Config.java:201-206 实为 `result==31 → 35`（§3.2 :211 处表述正确）。
- **字段计数错误**：§8.1 称 SystemConfig"145 个字段（3 private + 142 public）"。实测 **148 个字段（145 public 于 :18-162 每行一字段 + 3 private 于 :15-17）**。文档表格编号含 2 个占位行（#85"见 #27"、#131"已列 #127"），实际独立字段数被低估 3。

### 1.3 诚实边界抽验（5 段语义：代码直读 vs 推断）

| 段 | 判定 | 说明 |
|---|---|---|
| §3.5 kehu_list "近似但不严格 LRU（推断）" | 推断已标注，代码直读一致 | HuPaiKit.java:23/:58-59/:71-75 与文档一致（HashSet+remove(0)） |
| §7.6 AbstractAi config.txt 缺省（lianxupai 字段 2 vs 缺省 "1"） | 代码直读一致 | AbstractAi :20（=2）与 :35（"1"）属实；小瑕疵：FileNotFoundException 分支并非"静默"（有 printStackTrace），功能语义无差 |
| §2.4 "登录后不写 sconfig…（推断+代码路径已核）" | 推断已标注 | grep 全库 6 语句如上，无其它写点，结论成立 |
| §1.3 "重登录=重启进程（推断）" | 推断已标注 | ChangePasswordDialog :90-102 确为 1s 后 `System.exit(0)` |
| **§5.3 编号表首行"1-9/11-19/21-29 = 万/条/筒"** | **推断未标注（新发现）** | 所引"证据"（CaishengxuanzeImpl 升位算术、chupai:6242）只能证明**十分位 gap 结构**（x1..x9、31-37、41-48），**不能区分 11-19 是"条"还是"筒"**。见 §2.4 与 fixture 命名冲突 |

其余诚实性：§11.3 UNVERIFIED 表实际列 U1-U10（U10 已静态闭合），U1-U9 各附阻塞原因，与"UNVERIFIED 9 项"口径相符，未见夸大；§11.1 未逐行区间的自我披露与实际抽查一致。

### 1.4 §9 六端点逐字段比对（对照 game_server.py 与消费代码）

| 端点 | 客户端请求（消费代码实测） | fixture 实现 | 判定 |
|---|---|---|---|
| /api/login/login | 9 键 ajax/deviceid/lastloginversion/devicetype=3/password/username/loginfield/type/version（LoginController:169-178） | 逐键一致（requests.log 实录 9 键） | 一致 |
| login 响应 | 读顶层 `success`/`message`/`jwt`（:189-194）；message 经 `.toString()` 再 Gson→User | `{"success":true,"message":<User 对象>,"jwt":...}`——**message 为对象而非字符串**，但 `JsonObject.toString()` 对对象输出紧凑 JSON，客户端二次 `fromJson` 兼容两种形态，实测登录成功 | 一致（形态差异无害） |
| User 字段名 | User.java:10-33 共 23 字段（user_id/username/password/salt/server_ip/jihuo_status=0…） | game_server.py:203-225 字段名逐一对应；无"token"字段——客户端读的是顶层 `jwt`，IM 登录 `loginToken=param.jwt`（MainController:7813），声明中"token"只是口头别名 | 一致 |
| /api/mj/getConfigs | ajax；读 success/list（Config.java:461-482） | ajax 一致；list 元素 id/number/name/url | 一致（number/url 语义见 §2.4） |
| /api/config/GetLastVersion | ajax+version=dapai64/32（MainController:297-304）；读 success/versionCode/name/url（:313-321） | versionCode=0 < 1 不触发下载 | 一致 |
| /api/yiZhuo/get | ajax；list 每项 serial_number/id/mahjong_number/direction/zhuo_id(int) + result(字符串数字)（:3920-3925/:4054-4059） | build_yizhuo_round 六键同形，result=str() | 一致 |
| /api/user/updateSelf | ajax+jihuo_status="1"（:3991-3993） | success+message | 一致 |
| /api/user/updatePassword | ajax/deviceid/old_password/password（ChangePasswordDialog:76-80）；成功 1s 后 exit | success+message | 一致 |

---

## 2. B 类：game-fixture 运行验收声明复核

### 2.1 QoS 去重（声明"4 次到达 / 4 次 ACK / 仅 1 次 yiZhuo/get"）——**证实**

以**最后一次运行**（2026-11-13 06:56，`game-fixture/runtime/us3.log` + `udp.log` + `requests.log`，127.0.0.3:7901 会话）完整时序：

| 时刻 | 事件 | 证据 |
|---|---|---|
| 06:56:36 | TX type=2 typeu=1 fp=5893a122-8fb0（首发，入 QoS 队列） | us3.log |
| 06:56:38 | TX [dup-push 1/2] 同 fp 原样重发 | us3.log |
| 06:56:40 | TX [dup-push 2/2] 同 fp 原样重发 | us3.log |
| 06:56:40 | 【QoS发送】"未收到 ACK，重传#1（距首发 4.0s>3.0s）"——第 4 次发送 | us3.log |
| 06:56:40 | RX type=4 ×4（dataContent 均为 fp=5893a122…；第 1 条入队移除 retry=1，后 3 条"不在发送队列"重复 ACK） | us3.log |
| 06:56:36 | yiZhuo/get **仅 1 次**（推帧后窗口内）；下一次在 06:58:31（≈115s 后，人工"获取"截图用） | requests.log |

- 与台账（审计修复台账.md:142-144"1 原发+2 补发+1 队列重传 → 4 次 ACK 但 yiZhuo/get 仅 +1"）**逐点一致**；"第 4 次到达=队列重传"的细节台账自己也写对了。
- 微瑕疵：台账"yiZhuo/get 仅 +1（2→3）"的计数口径无法从 requests.log 精确对上（该日志按时间追加无会话内计数器），但"4 次到达仅 1 次拉取"的实质由时间线独立证实。

### 2.2 截图证据——**证实**

- `evidence/runC_3_post_render_tiles.png`（2560×1440）：四方向牌墙**真实渲染**——两层码牌、白色序号 1-17、牌面可辨（發/東/中/北、筒点、条、萬）；放大裁剪核验非空桌非黑屏。
- `evidence/interactive_flower_round.png`：底墙（direction 0）两行 34 张中可见 **9-10 张花牌图**（菊×3、兰×2、竹×1、夏×2、冬×2 等植物花卉纹样，即 sucai/hua/*.png 内容），**花牌图确实可辨识**（中央被"修改密码"弹窗遮挡，不影响底墙判读）。
- `evidence/runD_control_empty_board.png`：顶栏"牌点数:0 东:0 南:0 西:0 北:0"、空桌——对照分支成立。备注：该截图含与验收无关的桌面直播挂件（弹幕/互动消息区/在线观众），属证据噪声，不影响判读。

### 2.3 GF_FLOWERS 变体——**部分**（子声明"混入 8 张"证伪）

`game_server.py:80-99`（build_yizhuo_round）：

- 数量仍 34 ✓（每方向 `range(34)`，GF_FLOWERS 不变）；字段完整 ✓（六键同形）。
- **"东方混入 8 张 41-48"与实现不符（证伪）**：`dir_codes = list(range(41,49)) + codes[:34-8]` 只是构造了"8 花 + 26 常"的 **34 元候选池**，随后每张 `rnd.choice(dir_codes)` **有放回抽样**——实际花牌张数是随机变量（期望 8，可能 0..34）。实测 interactive_flower_round 底墙约 9-10 张花牌，与抽样期望相符而非"恰 8 张"。
- 渲染映射链本身**成立**（见 §2.4/B4）：41-48 按 number 匹配到 url=/sucai/hua/1..8.png，资源实存（`game-project/resources/quesheng/view/image/sucai/hua/1-8.png`），实机截图已见 hua 图渲染，无 NPE/缺图。

### 2.4 MjConfig 修正的依据强度——**部分证实 + 1 UNVERIFIED + 命名冲突新发现**

**c) 渲染依赖 number 还是 url 下标？——结论：number 是查找键，修正功能性必需（证实）。**
`Config.getImageByNumber(number)`（Config.java:75-81）线性匹配 `mjConfigs.get(i).getNumber()==number` 后返回**同下标**的 `mjImages.get(i)`；`getImageByMj`（:83-92）同构。mjImages 按 number 升序装载（initialize :476-482，ComparatorMj :569-576），url 只是匹配下标处的载荷。因此：若 number 为旧"0-41 连续码"，`result`（来自 yiZhuo 数据的 gap 码 1-9/11-19/21-29/31-37/41-48）中 **41-48 将查无图（getImageByNumber 返回 null）**，且 Config.xipai 的 `i<27 / i<34 / i<42` 分段（Config.java:169-199）会把连续码表错切花色段。即"修正"不是语义洁癖——**jiance/显示的牌面查找与测试发牌器都依赖 number 值域正确**（行为判定如 kePeng/hupaiyuce 用的是 `Mj.result`，不经 MjConfig，那部分不受影响）。

**a) "原为 0-41"从何而来——UNVERIFIED。** `git log --all -- game-fixture/game_server.py` 为空（该文件首入库即修正版，暂存区内容与工作区一致），仓库内无旧副本、无备份、无补丁痕迹（全库 grep "0-41" 仅台账自述）。**"42 条 MjConfig number 原为 0-41（错误）已改"这一历史声明无法独立取证**，只能视为台账自报。

**b) "真实牌编码 1-9/11-19/21-29/31-37/41-48"的依据——客户端内部结构实证（高置信推断），非生产实证。**
支撑：① `xipai` 分段边界 27/34/42 恰对应 27 万条筒 + 7 字 + 8 花（Config.java:169-199）；② 财神升位算术 `r%10<9→r+1 else r/10*10+1`、37→41-48→31 循环（CaishengxuanzeImpl:131-165）；③ kePeng 广西百搭分组 56↔1-9、58↔11-19、57↔21-29、53↔31-34、52↔35-37（MjPanel.java:71-86）；④ ComparatorMjResult 锚点 56→10、58→20、57→30、53→34.5、52→38（MainController.java:7898-7927）。以上证明 **gap 结构与百搭分组**无懈可击；但**"万/条/筒"的花色命名归属不可由客户端代码判定**（代码无任何字符串证据）。README（:215-219）已如实写"高置信推断而非生产样本"；台账:135"已改**真实**牌编码"措辞强于 README 自评。

**新发现（证伪"互证"声明 + 文档冲突）**：game_server.py:52 注释与 game-fixture/README.md:215-217 称"万 1-9 / **筒 11-19** / **条 21-29**（逆向⑤ §8）+ ComparatorMjResult 锚点 + sucai 目录结构三方互证"，而逆向⑤ §3.2（:210）与 §5.3（:616）写"万 1-9 / **条 11-19** / **筒 21-29**"。两文档直接冲突，且 README 把 ComparatorMjResult 锚点当命名"互证"是**过度声明**——锚点只证明 56 归 1-9 组、58 归 11-19 组、57 归 21-29 组，不能证明哪组叫"条"哪组叫"筒"。影响：若 fixture 命名与生产服务器相反，则 number 11-29 的两组牌**图面花色互换**（条筒对调）——不影响计数/链路验收结论，影响"牌面忠实还原"的正确性，且现有截图无法发现（无生产基准）。两说必有一误，建议以生产样本或原图资源内容比对后裁决。

### 2.5 udp_server --dup-push 实现——**证实**

`udp_server.py:252-255`：初始推帧后 `for k in range(args.dup_push): time.sleep(2); sock.sendto(out, client_state["addr"])`——① **同一字节串原样补发 ⇒ 同 fp**；② 用同一 `sock`（bind 于选中的 127.0.0.3:7901）⇒ 同一源地址/端口；③ 每次间隔 `sleep(2)` ⇒ 2s，与声明一致。日志实测 06:56:36→38→40 间隔恰 2s。小瑕疵：sleep 阻塞主 recv 循环各 2s（期间心跳积压在 socket 缓冲，不丢）。

### 2.6 遗留进程 / 状态卫生 / git 暂存区——**部分**

- **进程**：无残留。4 个 python.exe 均为无关自启服务（G:\AIQuant hummingbot，命令行已核对）；无 java/javaw 游戏端进程；UDP 7901 无监听；TCP 127.0.0.1:8081 监听者为 com.docker.backend.exe（非 fixture）。
- **sconfig 残留**：`.sandbox-game/游戏端/sconfig.config` 存在于工作区，但 `.gitignore:7` 覆盖 `.sandbox-game/`，未入暂存区（本地残留，符合政策）。
- **runtime/ 产物**：`game-fixture/runtime/`（us*.log、udp.log、requests.log、watchdog.ps1 等）被 `.gitignore:16` 覆盖，未入暂存区 ✓。
- **暂存区判类（git diff --cached --name-only，共 324 项）**——发现 **AGENTS.md 政策违规项**：
  - `game-fixture/evidence/error.log`、`evidence/requests.log`、`evidence/udp.log`、`evidence/udp_type2_push_frame.txt` —— **日志/抓包，按"Exclude … captures, logs, and generated outputs"不应进提交**（建议移 docs/evidence 或退暂存）。
  - `evidence/*.png` 9 张验收截图 —— 属"captures"，但为 README/台账验收声明直接引用的证据件；建议保留与否由提交人明示（至少应在提交说明中声明其证据属性），默认按政策应退暂存。
  - 其余大类（PC端/原始包 zip+exe、analysis/ 源码、game-project/resources 牌图、tools/MakeSconfig.java、game_server.py/udp_server.py/selftest 等）均为证据/源码/工具类，符合"preserve originals"政策。

---

## 3. C 类：客户端源地址过滤声明——**最终判定：证实（机制定位修正）**

**声明**（审计修复台账.md:148-149）："客户端静默丢弃非服务器地址来源的 UDP 帧（无 ACK、无业务）——MobileIMSDK 客户端按对端地址过滤；fixture 之外的直发无效。"

独立取证：以 CFR 0.152 全量反编译 `game-jars/MobileIMSDK4j.jar` 至系统临时目录（`/tmp/imsdk-decomp`，未入仓），逐类排查 `getAddress()/getSocketAddress()/connect()`：

| 取证点 | 结论 |
|---|---|
| `LocalUDPDataReciever.p2pListeningImpl`（反编译 :78-87） | 接收循环 `localUDPSocket.receive(packet)` 后直接 `messageHandler.handleMessage(packet)`，**无任何 packet.getAddress() 检查** |
| `LocalUDPDataReciever$MessageHandler.handleMessage`（:90-189） | 只按 Protocal.type 分派（2/51/4/50/52）+ QoS fp 去重，**无源地址、无 from/to 白名单检查** |
| `LocalUDPSocketProvider.resetLocalUDPSocket`（:32） | `new DatagramSocket()` 创建**未连接** socket，全类无 `connect()` |
| **`LocalUDPDataSender.send(byte[],int)`（:92-112，connect 在 :109）** | **过滤点在此**：每次发送前若 socket 未连接则 `ds.connect(InetAddress.getByName(ConfigEntity.serverIP), ConfigEntity.serverUDPPort)`（serverIP=登录响应 User.server_ip，端口 7901）。此后该 UDP socket 为**已连接（connected）状态，OS 层面仅放行来自该对端地址:端口的报文，其余源静默丢弃**（Java connected DatagramSocket 语义）。登录/心跳/重登录/业务回执全部经此 send 路径，故首帧登录后 socket 恒保持 connected |
| `UDPUtils.send`（全类） | 仅发送，无过滤逻辑 |

**判定**：
1. 行为声明（fixture 之外的源直发客户端帧 → 静默丢弃、无 ACK、无业务）——**证实**，机制即 connected-socket 的 OS 过滤；
2. "客户端按对端地址过滤"——**证实但需精确化**：过滤不在 MobileIMSDK 应用层接收循环（那里完全不查地址），而在 `LocalUDPDataSender.send()` 的 `DatagramSocket.connect(serverIP,7901)`，由操作系统执行；过滤键 = **源 IP+源端口二元组**（须恰为 server_ip:7901，即同一 socket 才能命中），不是应用层用户白名单；
3. 推论修正：逆向⑤ 附录 B.4 推帧样例中"客户端只按本地端口收包，to 不做过滤"表述不完整——应更正为"**客户端只接收来自已 connect 的 <User.server_ip>:7901 的帧；to/from/dataContent 不做应用层过滤**"。对 fixture 的实操含义：type=2 推帧必须从 udp_server 绑定的同一 IP:7901 socket 发出（当前实现满足），若以其它本地端口直发客户端则必然无效——与台账的实验观察一致。

---

## 4. 新发现问题汇总（文件:行号 + 影响）

| # | 位置 | 问题 | 影响 |
|---|---|---|---|
| N1 | docs/逆向⑤_游戏端语义还原.md:616 与 game_server.py:55-62、game-fixture/README.md:215-217 | 花色命名冲突：文档"条=11-19/筒=21-29" vs fixture"筒=11-19/条=21-29"；README 将 ComparatorMjResult 锚点当命名互证 | 两说必有一误；number 11-29 两组牌图花色可能互换（不影响链路验收，影响牌面忠实性）。需生产样本裁决 |
| N2 | game_server.py:87-97 | GF_FLOWERS 实现为候选池有放回抽样，不保证"8 张花牌" | 变体确定性/断言缺失：花牌张数随机（0..34），作为"恰好 8 张"的验收口径不成立 |
| N3 | docs/逆向⑤ §8.1（:785） | SystemConfig 字段数宣称 145（3+142），实测 148（145 public+3 private） | 计数性错误；§8.1 表覆盖度因此不足（未逐字段核对全 145 public 项） |
| N4 | docs/逆向⑤ §2.1（:114） | "只有 5 处 ObjectKit.WriteObject"实为 6 语句/5 触发组 | 口径含混，建议写"5 个触发点、6 条调用" |
| N5 | docs/逆向⑤ §5.3（:621） | "result==35 在 112 张模式被改为 35"笔误（实为 31→35，§3.2 :211 正确） | 文字性错误 |
| N6 | docs/逆向⑤ §5.3（:616）/§3.2（:210） | 花色命名（万/条/筒顺序）无代码证据却未标"（推断）" | 诚实边界缺口：该行引用的"证据"只能证明结构不能证明命名 |
| N7 | docs/audit/审计修复台账.md:135 vs game-fixture/README.md:215 | 台账"已改真实牌编码"强于 README 自评"高置信推断而非生产样本"；"原为 0-41"无 repo 取证 | 措辞越界 + 历史不可考（UNVERIFIED） |
| N8 | git 暂存区：game-fixture/evidence/{error.log, requests.log, udp.log, udp_type2_push_frame.txt}（及 9 张 png） | 日志/抓包/截图入暂存，违反 AGENTS.md"排除 captures/logs/generated outputs" | 提交前应退暂存或移入明确的证据目录并在提交说明声明 |
| N9 | 逆向⑤ 附录 B.4（:1308） | "客户端只按本地端口收包"表述不完整（漏 connect 过滤） | 与 C 类结论合并更正（见 §3 判定 3） |

## 5. 复核口径与限制

- 行号抽验基于 `game-src/` 当前 CFR 输出，与文档 §B.6 声明的口径一致（CFR 头注释计入行号）。
- 截图判读经 Java ImageIO 3× 放大裁剪（临时产物在 C:\Windows\Temp\audit_*.png，未入仓），分辨率为 2560×1440 原图。
- 反编译取证产物在系统临时目录 `/tmp/imsdk-decomp`，未修改仓库任何文件。
- 未验证项保持原状：逆向⑤ U1-U9（需生产侧/真机）本轮无新证据，维持 UNVERIFIED；新增 U11"MjConfig 旧值 0-41 的来源"（repo 内不可考）。

（完）
