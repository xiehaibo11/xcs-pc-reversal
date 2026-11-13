# C · 文档诚实性审计（对抗性）

审计对象：仓库全部 22 份文档中的**可核查完成度声明**（完成标记 ✅、百分比、BUILD PASS / 0 Error、"全按键验证"、"逐行/逐字节/逐方法/全闭合"、"N 项自测"、"推翻/勘误"等绝对化用语）。
审计时间：2026-11-12。审计方式：**只读仓库 + 重跑构建/自测/计数类命令**；未修改任何文档与源码；未运行任何 GUI 程序；未触碰 8.137.196.28（涉该服务器的声明一律标 REPRODUCIBLE-BY-COMMAND，引用 `docs/功能验证报告.md` 与 `docs/设备登录.md` 中的命令/凭据）。
审计人：对抗性审计子代理（与本仓库文档作者无利益关系）。

---

## 结论行

**声明核对：65 条证实 / 5 条夸大 / 9 条过时 / 4 条矛盾 / 6 条 UNVERIFIED**（共 89 条编号声明；另有 3 份短文档不含可核查完成度声明，见 §2.16）。

**一句话回答用户关切——"当前是否可以声称逆向工程已全部完成？"**
**不可以。** PC 图传两端（2.0.15/2.0.14）确实达到了"源码级还原 + 双版本可编译（0 Error）+ fixture 真机功能闭环 + 真实服务器自动化测试最终轮 PASS=39 FAIL=0"的高度，文档对这一块的核心声明绝大多数能复现；但**游戏端（JavaFX）只有"反编译级 + UI 接线级"还原**：MainController 7956 行没有任何逐段语义文档、UDP 7901 无 fixture、sconfig.config 从未实际构造、游戏端从未在任何服务器（含本地）上运行过一局；另有 Myclassification.dll 缺失、OTA 下载分支未实测、右键"删除选中"菜单无确定性点验三处**功能验证报告自己承认的缺口**，以及整个安卓仓库的遗留项。声称"全部完成"会在以上每一处被本报告 §4 的证据驳倒。

---

## 0. 审计执行的可复现命令（全部实测）

| 命令 | 结果 | 证实的声明 |
|---|---|---|
| `dotnet build analysis/xcs-cs112/xcs.csproj -c Release --no-incremental` | **0 警告 / 0 错误** | 根 restore-status 第二轮、analysis/xcs-cs112/restore-status.md |
| `dotnet build analysis/distinguish-cs112/distinguish.csproj -c Release --no-incremental` | **0 错误 / 4 警告**（SYSLIB0014 × 4：`HomeViewModel.cs(2499,27)` 与 `(3166,6)` × 双工程重复计数） | analysis/distinguish-cs112/restore-status.md 逐字吻合 |
| `cd game-project && ./build.sh` | `javac exit=0`，`compiled classes=84`，产出 `out/quesheng.jar`（1,457,341 字节） | game-project/README、FIXES.md "0 Error / 84/84" |
| `python fixture_server/selftest.py` | **PASS=82 FAIL=0** | 功能验证报告"82 项"；证伪 fixture README"81 项" |
| `find`/`wc -l`/`grep` 计数系列 | 45 .cs（两版，排除 obj）/ 9 XAML（两版）/ 14 jar / 62 java / 9 fxml / 2 css / 79 png + 50 bmp = 129 图片 / QueshengDaPai.jar **84** class / MainController **7956** 行 / SystemConfig **145** 字段 / SettingStageController **986** 行 / LoginController **237** 行 / FIXES 操作表恰好 **542** 行（编号 1–542 连续）/ javafx9-classes.jar **5550** class / 右栏牌钮 CommandParameter 恰 **42** 个唯一值 | 逆向①②④、路线图、UI设计①②、game-project README |
| PE 解析（python struct 重算） | 游戏端.exe 段尾 `0x83800`=538,624，总长 9,517,056 → 覆盖区 **8,978,432 字节** | 逆向③/逆向④ 覆盖区数字 |
| SHA-256 全量比对 | `游戏端/游戏端/jre-9/` 132 文件 vs `游戏端.zip` 内 jre-9 132 项，**132 全部一致** | 项目核查"132 个文件与原始 ZIP 的 SHA-256 一致" |
| 资源集比对（python zipfile） | QueshengDaPai.jar 141 资源（含 MANIFEST）vs `game-project/resources/` 140 文件，**双向差集为空** | game-project README"逐文件一致（仅少 MANIFEST）" |
| MD5 复算 | MD5("******") 按 C# `ToString("X")`（每字节无前导零）= **E1ADC3949BA59ABBE56E057F2F883E**，与两工程 `appsettings.ini` 的 PWD 一致 | 根 restore-status"X 格式非 X2"配方 |
| `grep -rn "Global.verify" analysis/xcs-cs{,112}` | 原始层唯一读点 `MainWindow.cs:31`、唯一写点 `ErrorWindow.cs:54`；还原层 `MainWindow.xaml.cs:18` / `ErrorWindow.xaml.cs:52`；无置回 false 路径 | A2 报告全部行号 |
| `diff -r` 系列 | 两版 `Constant.cs` **逐字节相同**；两版 `res/` **零差异**；原 jar 资源与 resources/ 双向差集空 | 逆向②、A4 R1/R2 |
| 样本字节数 `stat` | xcs.exe **10,269,696** / distinguish.exe **10,182,144** / 游戏端.exe **9,517,056** | 逆向①②③ 头部 |
| 14 jar 字节逐一对表 | 与逆向④ §2 表格**全部一致** | 逆向④ |

涉 8.137.196.28 的声明（真实登录验收、真实服务器 fulltest、直连探测 0.1s success 等）按约束**一律不执行**，标 REPRODUCIBLE-BY-COMMAND；本审计仅核验了其留下的本机日志/截图产物（`.tools/smoke-ui/fulltest-new.log` 等）。

---

## 1. 矛盾矩阵（文档间与文档内相互矛盾）

| # | 甲方（说法） | 乙方（说法） | 现场核实 | 勘误标记现状 |
|---|---|---|---|---|
| M1 | 逆向①（首轮）曾称原始 xcs.exe 为 ".NET 8 WPF" | 项目核查 §3 / 逆向① 现文 / 逆向分析报告勘误头：原始样本是 **.NET Framework 4.7** | BAML strings（`analysis/xcs-cs/baml-strings/loginwindow.strings.txt:4-5`）与还原 csproj 均支持 4.7 判定 | **已勘误**：两份逆向① 现文直接写 4.7（root/docs 副本 diff 仅链接路径），勘误过程记录在项目核查 §3 表。处理得当 |
| M2 | 逆向③ §6："游戏端无 zhuo/* 调用证据" | 逆向④ §6.2："存在 `/api/yiZhuo/get` 消费端调用（只拉不上传）"，MainController.java:3898/4033 实证 | grep 证实 ④ 正确 | **已勘误**：逆向③（两副本）头部有 2026-11-11 勘误注记，明说旧推断作废。处理得当 |
| M3 | 根 restore-status.md:5 "两版（2.0.14/2.0.15）Release 构建**均 0 警告 / 0 错误**" | analysis/distinguish-cs112/restore-status.md:3 "**0 Error / 4 Warning**（SYSLIB0014 ×4）" | 实测 distinguish **0E/4W**：4 个 SYSLIB0014 警告真实存在且未压制 | **未勘误**：根状态文档的"均 0 警告"对 2.0.14 不成立，与其子状态文档直接冲突（见声明 S1，判夸大） |
| M4 | 根 restore-status.md:51 "game-src/ … + **136 图片**" | 同文件 :17 及项目核查 §3："实有 **129** 张（79 PNG + 50 BMP）"，另有"136 图片→修正数量"条目 | find 实测 79 png + 50 bmp = **129** | **未勘误**：同一文件第 51 行仍保留旧数字 136，与第 17 行同文件自相矛盾——勘误只做了一半 |
| M5 | docs/后续逆向路线图.md："A 节 97% / B 节 85% / C 节 80%" | docs/项目理解与文档核查.md:5："历史文档中的完成度百分比不作为本次验收指标" | 百分比无客观口径、无验收标准可复核 | **部分处理**：项目核查声明了不采信，但路线图与根 restore-status 的 85%/97% 未加"非验收口径"标注，两套口径并存 |
| M6 | fixture_server/README.md:"全部 **81 项** PASS、0 FAIL（2026-11-12 实测）" | docs/功能验证报告.md:"selftest（**82 项**，含新加 code 键断言）" | 重跑 selftest.py 实测 **PASS=82** | **未勘误**：README 落后于加回归断言之后的现实；功能验证报告是对的 |
| M7 | docs/功能验证报告.md:6："真实服务器全按键测试（**PASS=35**，日志 fulltest-new.log）" | `.tools/smoke-ui/fulltest-new.log:522`：最终轮（09:24:56）"**PASS=39 FAIL=0 SKIP=0**"；35 出现在 09:07/09:20 两个中间轮 | 日志实查 | **未勘误**：引用了中间轮数字；最终轮更好（39/0），方向保守但数字过时 |
| M8 | docs/restore-status.md（历史副本）勘误头："旧版对照工程仍有 6 个 XAML Ctor 错误" | 根 restore-status.md 第二轮："旧版 6 个 XAML Ctor 错误、7 个引用警告**已修复**" | 实测 distinguish 现为 BUILD PASS 0E/4W | **未更新**：历史副本的勘误头停在第二轮之前的时点，未指向第二轮结论 |
| M9 | game-project/FIXES.md §3："**1986 个**未匹配方法体两两配对"→"745 对完全相同 + 258 对语义等价" | 内部算术：745+258=**1003 对 = 2006 个方法体 ≠ 1986**，差 20 无解释 | 文档内部数字不一致（§3 其余分级 48+2+1+33=84 一致） | **未处理**：FIXES.md 自身算术缺口，影响"2444 方法逐一对账"叙事的精确性（不影响 84/84 类级结论与 javac 0 Error 的独立复现） |
| M10 | 逆向① §5："QoS 重发扫描清理（阈值 **~5 秒级**）" | UI设计① §10.3：qosTimer **10000ms** 清理 **>60s** 登记项 | 代码实查 `HomeViewModel.cs:2791-2811`：Interval=10000，阈值 60000ms | **未勘误**：逆向① 的"5 秒级"与代码不符（见声明 V6，判夸大） |

已正确处理（有勘误且内容对）的历史矛盾：M1（.NET 8→4.7）、M2（无 zhuo 调用→yiZhuo/get 拉取）。**未处理**的：M3/M4/M6/M7/M8/M9/M10。

---

## 2. 声明核对表（89 条，判定：✔证实 / ✗夸大 / ⟳过时 / ⚡矛盾 / ？UNVERIFIED）

### 2.1 README.md（根）— 7 条
| # | 声明 | 判定 | 证据 |
|---|---|---|---|
| R1 | xcs-cs112 构建通过"0 错误 / 4 警告" | ⟳过时 | 实测 `--no-incremental` 重建为 **0/0**；README 未随第二轮更新 |
| R2 | distinguish-cs112 "仍有 ILSpy Ctor 残留，**本机构建未通过**" | ⟳过时 | 实测 BUILD PASS 0E/4W；第二轮已修复，README 未更新（与根 restore-status 第二轮矛盾） |
| R3 | "`analysis/baml-strings/`：**14 个** BAML 的有序字符串证据" | ⟳过时（路径+数字双错） | 该目录**不存在**；实际为 `analysis/xcs-cs/baml-strings/` 下 **9** 个 .strings.txt（另有 18 个 .baml 分属两版反编译目录，git 内可查） |
| R4 | PC=HTTP 8081+WS 18889；游戏端=HTTP 8081+MobileIMSDK UDP 7901 | ✔ | `IMClientManager.java:36` serverUDPPort=7901；`HomeViewModel.cs:1871` ws://:18889 |
| R5 | 登录 devicetype=5（PC）、check_update type=3（PC） | ✔ | `HttpReq.cs`/`LoginViewModel.cs` grep 命中 |
| R6 | 原始两个样本是 .NET Framework 4.7 | ✔ | 项目核查 §3 + BAML strings（PresentationFramework 4.0.0.0） |
| R7 | 根/docs 首轮报告是历史快照，当前结论以核查报告/逆向④/restore-status 最新复核为准 | ✔ | 各文档勘误头存在且指向正确 |

### 2.2 restore-status.md（根）— 9 条
| # | 声明 | 判定 | 证据 |
|---|---|---|---|
| S1 | 第二轮："两版 Release 构建**均 0 警告 / 0 错误**" | ✗夸大 | 2.0.15 实测 0/0 ✔；2.0.14 实测 **0E/4W**——"均 0 警告"一半不实，且与子状态文档（Y1）冲突（矛盾 M3） |
| S2 | 冒烟截图存 `.tools/smoke-ui/old-login.png`、`new-login.png` | ✔ | 两文件存在（截图像素内容未逐张人工核验，存在性证实） |
| S3 | 真实登录验收：`XCS232@8.137.196.28`、四方位 22/20/22/20、序列号 02917-02920、"链接成功/51ms" | ？UNVERIFIED（REPRODUCIBLE-BY-COMMAND） | 涉生产服务器，本审计禁碰；可按 `docs/设备登录.md` 凭据与功能验证报告 §六工具复现 |
| S4 | "游戏端实有 14 JAR / 62 Java / 9 FXML / 2 CSS" | ✔ | find 计数逐一吻合 |
| S5 | 同文件 :51 "…+ **136 图片**" | ⚡矛盾 | 实测 **129**（79 PNG+50 BMP）；与同文件 :17、项目核查矛盾（M4），无行内勘误 |
| S6 | "推翻旧判断：游戏端存在 HTTP `/api/yiZhuo/get`（只拉不上传）" | ✔ | `MainController.java:3898,4033` 存在调用；无 zhuo/add 上传端点 |
| S7 | 真机功能验证—完成 ✅："两版 PC 在 fixture 上**全按键**真机验证通过…弹窗/统计 UI 全部正确" | ✗夸大 | "全按键"不成立：(a) fixture 矩阵 19 项不含 修改密码/重置口令/方位刷新×4/校验视图点牌/右栏 42 改牌钮/右键删除选中；(b) 真机 fulltest 的右键菜单项 D3 的 PASS 判据是弱判据 "right-click slot -> 删除选中 **(or verify no menu on empty area)**"，09:14 轮更直接 SKIP（"empty physical table"）；(c) 功能验证报告 §五.2 自认右键菜单"需人工鼠标点验一次"——与"全按键…通过"同文矛盾；(d) 2.0.14 仅实测 4 项，其余"✅（借用）"（见 F-矩阵 §2.12）。**各按钮本身的已测部分（C2-C5、E3-E6、F1-F5、G1-G4 等）成立**，夸大在"全"字 |
| S8 | "推翻上一会话 fulltest 的 C2-C5 失败…（真实服务器直连探测 0.1s 全 success）" | ✔ | C2-C5 在最终轮（09:23:57–09:24:50）逐项 PASS；直连探测部分 REPRODUCIBLE-BY-COMMAND |
| S9 | "抓到并修复 fixture check_update 缺 code 键…selftest 加回归断言" | ✔ | `server.py:529-532` 现带 `code:0`；selftest PASS=82 |

### 2.3 逆向分析报告.md（root+docs 双副本）— 4 条
| # | 声明 | 判定 | 证据 |
|---|---|---|---|
| A1 | 勘误头：4.7/14 JAR/UDP 7901/yiZhuo/get 修正 | ✔ | 两副本均有勘误头（diff 仅链接差异），内容与本审计实测一致 |
| A2 | "45 个 .cs（analysis/xcs-cs/）" | ✔ | find 46 含 1 个 obj 产物，排除后 **45** |
| A3 | "接近原始源码质量" | ？UNVERIFIED | 主观判断，无验收口径 |
| A4 | ③ 覆盖区 8.97MB、"jar 已打包其内未提取"（历史） | ✔（历史快照+勘误头） | 覆盖区 8,978,432 实算吻合；"未提取"已被勘误头标明过期 |

### 2.4 逆向①_xcs电脑图传.md（双副本）— 7 条
| # | 声明 | 判定 | 证据 |
|---|---|---|---|
| V1 | xcs.exe 10,269,696 字节、2026-04、2.0.15 | ✔ | stat 10,269,696；2.0.15 见 `Global.cs:33` |
| V2 | 原始为 .NET Framework 4.7 WPF（45 个 .cs） | ✔ | find 45（排除 obj） |
| V3 | "登录流程（LoginViewModel，399 行）" | ✔ | wc -l = **399** |
| V4 | "断线重连：OnClose 且非主动关闭 → **1 秒**间隔无限循环" | ✔ | `NetManager.cs:180` `Task.Delay(TimeSpan.FromSeconds(1.0))`，`while(true)` + IsClose 判断 |
| V5 | "心跳：首次 1ms 触发后改为 5000ms" | ✔ | `HomeViewModel.cs:2542,2551-2553` |
| V6 | "QoS 重发扫描清理（阈值 **~5 秒级**，见 2800 行清理段）" | ✗夸大 | 实际 qosTimer **10s** 间隔、清理 **>60s** 登记（`HomeViewModel.cs:2793,2809`）；"5 秒级"与代码不符 |
| V7 | §9 未决项："VerifyPwdWindow 校验通过后的解锁范围未逐行核对" | ⟳过时 | A2 报告已逐行闭环（本审计 grep 复核成立），逆向① 未决项未加"已闭环"标注；OTA 域名连通性一项目前仍开放（如实） |

### 2.5 逆向②_鑫财神图传PC.md — 4 条
| # | 声明 | 判定 | 证据 |
|---|---|---|---|
| W1 | distinguish.exe 10,182,144 字节、2.0.14 | ✔ | stat 10,182,144；`Global.cs:33`="2.0.14" |
| W2 | 反编译 45 个 .cs | ✔ | find 45（排除 obj） |
| W3 | "Channel.DeleteOffset 为 2.0.15 新增" | ✔ | xcs-cs `Channel.cs` 4 命中 / distinguish-cs **0** 命中 |
| W4 | "协议常量文件两版逐字相同" | ✔ | `diff Constant.cs` 两版零差异 |

### 2.6 逆向③_游戏端.md（双副本）— 4 条
| # | 声明 | 判定 | 证据 |
|---|---|---|---|
| G1 | 勘误头（JAR 已提取/UDP 7901/yiZhuo/get/随包 JRE 已恢复） | ✔ | 两副本头部均有；各项与本审计实测一致 |
| G2 | 样本 9,517,056 字节 + error.log 启动链（读 .\sconfig.config） | ✔ | stat 9,517,056；`游戏端/游戏端/error.log` 存在且首行即 `FileNotFoundException: .\sconfig.config` |
| G3 | 覆盖区 8,978,432 字节（94%），exe4j 专有加密 | ✔ | python struct 重算段尾 0x83800 → 8,978,432 精确吻合 |
| G4 | §6 "游戏端无 zhuo/* 调用证据"（正文原文保留） | ⚡矛盾（已勘误） | 被 逆向④ 推翻；勘误在头部而非 §6 行内——读者读正文仍会看到旧结论，但 G1 勘误头已明说"不再适用" |

### 2.7 逆向④_游戏端源码还原.md — 9 条
| # | 声明 | 判定 | 证据 |
|---|---|---|---|
| D1 | 14 jar 清单及字节数 | ✔ | 14 jar 在库，字节逐一对表全中 |
| D2 | §1.2 引用 i4j_nlog_1.log 关键内容（15 文件解包等） | ？UNVERIFIED | 该日志按其自述清理流程已从 %TEMP% 删除，仓库无副本；仅有 jar 字节/类数等次级佐证间接支持 |
| D3 | "62 类（quesheng.* 全闭合）"，无 CFR ERROR | ✔ | game-src 62 java；grep `Decompilation failed/ERROR` 全仓 **0** 命中；jar 内 84 class 全部属 quesheng.*（包分布实查） |
| D4 | "MainController 7956 行" | ✔ | wc -l = **7956** |
| D5 | SystemConfig "~140 个玩法/热键字段"、serialVersionUID -6284005833985478852、DES nihao985 | ✔ | 字段声明实测 **145**（~140 成立）；serialVersionUID `SystemConfig.java:14`；`DESKit.java:14` key="nihao985" |
| D6 | "**完整字段清单**见 game-src/quesheng/model/SystemConfig.java（**62 行声明段**）" | ✗夸大 | 该文件 188 行，字段声明段约 **145 行**（:18–162）；"62 行"对不上任何实测量（疑与"62 个 java"混淆）。字段清单本身存在且完整——夸大在行数描述 |
| D7 | appKey=5418023dfd98c579b6001741、serverUDPPort=7901 | ✔ | `IMClientManager.java:34,36` |
| D8 | §6.1 HTTP 端点全集 6 个；§6.2 推翻"无 zhuo 调用" | ✔ | 6 端点调用处行号抽核命中（login/getConfigs/GetLastVersion/yiZhuo/get/updateSelf/updatePassword）；推翻结论与源码一致 |
| D9 | "动态提取已 **100% 覆盖**其产物，故无信息缺口" | ✔（带注记） | 84/84 类 + 141 项资源清单双向核对为空集——以"类+资源清单"为界成立；"100%"是绝对化表述，其边界（不含 exe4j 加密算法本身）§9.2 已自认 |

### 2.8 docs/后续逆向路线图.md — 7 条
| # | 声明 | 判定 | 证据 |
|---|---|---|---|
| L1 | "A. PC 图传 完成度约 **97%**"（及 B 85%/C 80%） | ⚡矛盾 | 无客观口径；与项目核查"百分比不作验收指标"并存（M5）。97% 中含"运行时等价性仍待验收"自我减项，数字本身不可复核 |
| L2 | A2/A4 已闭环 ✅（静态核对） | ✔ | 两文档存在且核心声明被本审计抽验证实（见 §2.10/§2.11） |
| L3 | B1-B4 全部闭环 ✅（jar 提取/CFR/sconfig/FXML 清单） | ✔ | 产物级全部核实：14 jar/62 java/9 fxml/2 css/129 图/84 class |
| L4 | W11 待办：dnSpyEx 运行时调试、OTA UI 需服务器推送 | ✔ | 与功能验证报告 §五.3 诚实边界一致，待办真实开放 |
| L5 | "C. 安卓端 80%，612 方法中 387 个语义未闭合" | ？UNVERIFIED | 另一仓库（xcs-android-reconstruction），本仓无法复核 |
| L6 | D 节"环境 38 项检查：0 缺失、0 失败" | ✔ | `.tools/reverse-environment.json` 实测 Tools=38，21 Verified+17 Present |
| L7 | E 节"安卓重建 38 项 RuntimeChecks + 9 套 host 测试 + 生产登录验收全绿" | ？UNVERIFIED | 另一仓库，本仓无法复核 |

### 2.9 三份历史状态副本（docs/restore-status.md、analysis/xcs-cs112/restore-status.md、analysis/distinguish-cs112/restore-status.md）— 4 条
| # | 声明 | 判定 | 证据 |
|---|---|---|---|
| H1 | docs/restore-status 勘误头："旧版对照工程仍有 6 个 XAML 错误" | ⟳过时 | 第二轮已修复；实测 BUILD PASS。勘误头未更新到第二轮之后（M8） |
| H2 | 同文正文 "net47 BUILD PASS 0E/18W" | ✔ | 明确标注历史记录，历史值无矛盾（根 restore-status 亦声明"历史 net47…不代表本次重新生成"——bin/Release 下现仅 net8.0-windows，实查一致） |
| X1 | analysis/xcs-cs112/restore-status："第二轮 0 错误/0 警告" | ✔ | 实测 0/0 |
| Y1 | analysis/distinguish-cs112/restore-status："0 Error / **4 Warning**（SYSLIB0014，2499,27 与 3166,6 ×重复计数）" | ✔ | 实测完全一致（连警告坐标都吻合）——**全仓对构建状态描述最准确的一份文档** |

### 2.10 docs/A2_校验口令解锁链核对.md — 4 条
| # | 声明 | 判定 | 证据 |
|---|---|---|---|
| B1 | "Global.verify 全工程唯一读点 MainWindow.cs:31，唯一写 true 点 ErrorWindow.cs:54（还原版 :52/:18），无置回 false 路径" | ✔ | grep 双层复核完全吻合（含还原层行号 :52/:18） |
| B2 | StartupUri "LoginWindow.xaml" 解析到 MainWindow 类（baml strings :5） | ✔ | `loginwindow.strings.txt` 第 4 行即 `distinguish.MainWindow`（文档写 :5，**行号漂移 1 行**，性质轻微） |
| B3 | "App.xaml.cs DoStartup 死代码→已改为 OnStartup override 并重建 BUILD PASS" | ✔ | 现源码 `App.xaml.cs:11-14` 存在 `protected override void OnStartup` + AssemblyResolve 挂钩 |
| B4 | "构建结果 BUILD PASS（0 错误，**56 个警告** CA1416）" | ⟳过时 | 当时快照；现 TFM=net8.0-windows 下 CA1416 不再产生，实测 0 警告。历史值未加时点限定，读者易误为现状 |

### 2.11 docs/A4_双版本2.0.14_vs_2.0.15差异报告.md — 3 条
| # | 声明 | 判定 | 证据 |
|---|---|---|---|
| C1 | "25 条差异（业务 8 + 噪声 9 + 资源品牌 3 + 排查否定 5 组）" | ✔ | 8+9+3+5=25 数学一致；抽验 B0（Global.cs:33 版本串）、B1（DeleteOffset 有/无）、R1（cq_favicon.ico/cs.ico 仅 2.0.15 树存在，实查 7 vs 5 个 ico）、R2（res/ 零差异）全中 |
| C2 | B7（新发现）："msg113/msg117 职责在两版间换位，2.0.15 清牌逻辑在 case 113（:1982-2024）、117 缩为停定时器（:2079-2086）" | ✔ | `case 113:` 恰在 :1982、`case 117:` 恰在 :2079 |
| C3 | "还原层与原始层行号基本一致（行对齐）" | ✔ | 抽验 4 处锚点（HomeViewModel 1871/1982/2133/3159、Channel.cs:36）全部命中 |

### 2.12 docs/UI设计①_图传PC.md — 4 条
| # | 声明 | 判定 | 证据 |
|---|---|---|---|
| U1 | 控件清单"逐行提取，无臆造"（抽验 MainWindow 登录窗 9 个功能控件、OnlineWindows 5 按钮/4 单选/8 输入框、HomeWindow 右栏"Button×42"） | ✔（抽验） | MainWindow.xaml 语义控件=9（原始元素 22 含模板噪声）；OnlineWindows 按钮 5（实查）；右栏 CommandParameter 唯一值恰 **42**（原始 `<Button` 计 85 系模板重复，语义计数正确） |
| U2 | §5.5 "39 个集合属性（HomeViewModel.cs:579-1163）"；§5.7 "39 组集合与计数" | ✗夸大 | 该区间 `ObservableCollection<ChannelMjResult>` 实为 **45** 个（Wan9+Tiao9+Tong9+字17+花8+X…）；**同文 §5.5 自己枚举的行名也是 45 项**——文档内部计数错误 |
| U3 | §10.1 "HTTP 端点全集（19 个，grep HttpReq. 全集）" | ✔ | HttpReq 调用去重 **18** 个 + 登录直连 HttpClient 1 个 = 19，与文档自注一致 |
| U4 | §12 忠实性说明："运行时渲染未验证…未运行 exe 截图比对" | ✔ | 诚实边界，与真机冒烟仅覆盖部分窗口的现实一致 |

### 2.13 docs/UI设计②_游戏端.md — 3 条
| # | 声明 | 判定 | 证据 |
|---|---|---|---|
| J1 | LoginStageView 9 个 fx:id 清单、ChangePassword 4 个 fx:id 清单 | ✔ | grep fx:id 实测 9/4，名称逐一吻合 |
| J2 | MainController 7956 行 / SettingStageController 986 行 / LoginController 237 行 / 快捷方式区 12 个 ComboBox | ✔ | wc -l 7956/986/237；§3.3 快捷区所列 12 个 ComboBox fx:id 与 FXML 名称一致 |
| J3 | §9 忠实性说明："运行时渲染未验证；可见性/禁用态未穷举（disable 控件未逐一追踪 setter）" | ✔ | 诚实边界（正向样本：明确声明了未穷举） |

### 2.14 docs/功能验证报告.md — 5 条
| # | 声明 | 判定 | 证据 |
|---|---|---|---|
| F1 | "自测回归：selftest.py（**82 项**，含新加 code 键断言）" | ✔ | 重跑 **PASS=82 FAIL=0** |
| F2 | fixture 验证矩阵 19 项 ✅（含 09:43:57 zhuo/add success:true、104 张=26 种×4） | ✔ | `fixture_server/requests.log`/`rounds.jsonl`（7 局成功局）与截图佐证；另见 docs/audit/D_验证记录核查.md（结论 8 证实/0 不符） |
| F3 | §首段 "另一会话同期…完成了真实服务器**全按键**测试（**PASS=35**，日志 fulltest-new.log）" | ⟳过时 | 日志最终轮 **PASS=39 FAIL=0**（09:24:56）；"PASS=35"是 09:07/09:20 中间轮；"全按键"同 S7 弱判据问题（D3 "or verify no menu on empty area"）。注：该测试是**真实服务器**（8.137.196.28）会话，REPRODUCIBLE-BY-COMMAND |
| F4 | §七 "原版 vs 还原版同屏对照：UIA 元素树 **23/23 逐一相同**" | ？UNVERIFIED | 对照产物仅有 `screen-both.png` 与 old/new 隔离目录；23 个元素的机器可读清单/脚本未入库，无法独立复核数字；方法（同 INI、双启动、UIA 树比对）可按报告描述复现 |
| F5 | §五 诚实边界 4 项（Myclassification.dll 缺失/右键菜单需人工/OTA 未实测/C2 留观） | ✔ | 与本审计独立核实的现实完全一致（fixture 无 UDP、server.py 恒"无更新"、D3 弱判据）——本报告 A 级清单大量以此为锚 |

### 2.15 fixture_server/README.md — 4 条
| # | 声明 | 判定 | 证据 |
|---|---|---|---|
| T1 | "selftest 全部 **81 项** PASS、0 FAIL（2026-11-12 实测）" | ⟳过时 | 现为 **82** 项（加 code 键断言后）；与功能验证报告 F1 矛盾（M6） |
| T2 | 绑定 127.0.0.2 回环、三端口 80/8081/18889、绝不连真实服务器 | ✔ | `server.py` BIND_HOST 默认 127.0.0.2；全文件无任何出站连接与 UDP 代码 |
| T3 | "HttpReq.cs:38 对失败响应读 `dictionary[\"code\"]`，缺失即崩" | ✔ | `HttpReq.cs:38` 逐字命中 |
| T4 | selftest 依赖 websockets 17.4 / cryptography 50.0.2 | ✔ | 本机实测版本一致 |

### 2.16 其余短文档 — 无可核查完成度声明
`docs/设备登录.md`（凭据+互踢警示；注："端口：8.137.196.28"实为 IP，标目笔误）、`docs/启动程序随机设置.md`（一句设定说明）、`docs/PC端：三端.md`（任务考题文本："把PC端剩余的软件进行逆向，还有完整的工程，UI设计…无任何报错"——**这是任务要求而非完成声明**；对照现实："无任何报错"在编译层已达成（三套工程 0 Error），"完整"二字受 §4 A 级清单约束）。

---

## 3. 夸大检测专节（绝对化用语逐个过堂）

| 用语 | 出处 | 判定 | 依据 |
|---|---|---|---|
| **"全按键真机验证通过"** | 根 restore-status:66 | ✗ 不成立 | HomeWindow 实有交互点远超已测集：顶栏 7 钮（已测 6，"修改密码"未入 fixture 矩阵）、方位刷新钮×4（真机 C1 已测、fixture 未测）、右栏 42 牌钮（仅真机 D2/D7 弱条件覆盖"if live data present"）、校验视图 45 组点牌（D1/D2 弱覆盖）、右键"删除选中"（**从未确定性触发**：fixture 合成右键不弹菜单；真机 D3 PASS 判据为"或验证空区无菜单"；09:14 轮空桌 SKIP）、登录窗"重置口令"（fixture 未测，真机 H2/H3 已测）。**"全按键"应改写为"已覆盖清单见矩阵 19 项+fulltest 39 项，右键菜单待人工点验"** |
| **"逐行闭环/逐行核对"** | A2 标题、A2 §六 | ✔ 基本成立 | A2 的读写点、状态机、逐窗口 diff 表经 grep/行号抽验全部命中（唯一瑕疵：baml strings 行号漂移 1 行） |
| **"全闭合"（62 类 quesheng.*）** | 逆向④ §3、路线图 | ✔ 成立（限定意义上） | 62 java ↔ jar 84 class（含内部类，javac 复现 84/84）；全仓无 CFR ERROR 标记；闭合指"反编译产物完整"，不指"语义已还原"（后者未做，见 A 级清单） |
| **"完整字段清单（62 行声明段）"** | 逆向④ §5 | ✗ 行数错误 | 声明段约 145 行（:18–162）；"完整"成立、"62 行"不成立 |
| **"100% 覆盖其产物"** | 逆向④ §9.2 | ✔ 边界成立 | 以类清单（84/84）+资源清单（141 双向差集空）为界成立；该句同段即自认"离线纯静态还原不可行"，无隐藏夸大 |
| **"82 项自测"** | 功能验证报告 | ✔ 成立 | 重跑 PASS=82 |
| **"45 个 .cs"** | 逆向①②/README 等 | ✔ 成立 | find 排除 obj 后恰 45（两版） |
| **"2444 个方法体逐一对账"** | game-project README §忠实性 | ⚡ 部分存疑 | 类级 84/84 与 javac 0 Error 已独立复现；但 FIXES.md §3 的 1986 体 vs 745+258=1003 对（2006 体）差 20 未解释（M9），方法体级精确计数无法从仓库残留物独立复核（对比操作日志未入库） |
| **"推送 zhuo/add(104 张全校验通过)"** | 根 restore-status:67 | ✔ 成立 | rounds.jsonl 7 局、每局 104 张；fixture 校验镜像 OnValidData |
| **"还原版 UI 是对原版的忠实重建（23/23 逐一相同）"** | 功能验证报告 §七 | ？ 无法独立复核 | screen-both.png 存在，但 23 元素清单不可考；方向可信（XAML 直出原 BAML、资源直出原包有独立证据），数字标 UNVERIFIED |

---

## 4. 诚实未完成清单（重点交付）

> 分级：**A 级**＝影响"逆向工程完成"这一定性的实质缺口；**B 级**＝文档质量/一致性缺口。每项给出依据（文件:行号）、影响、建议动作。

### A 级（10 项）

| # | 未完成项 | 依据 | 影响 | 建议动作 |
|---|---|---|---|---|
| A1 | **游戏端无语义级还原**：MainController 7956 行只有 CFR 代码与 UI 接线层文档（UI设计② 覆盖控件/事件/端点/热键），牌局规则语义（出牌/吃碰杠/胡牌判定 HuPaiKit/记牌 LiangCengJiImpl·SanCengJiImpl/买马/换手牌算法）无逐段文档 | 逆向④ §9.1 自认"只读参考不可直接编译…非本节目标"；路线图 B 待办 3"MainController 7956 行的逐段语义还原"仍开放；UI设计② §9.4 自认未穷举 | 游戏端只能算"反编译级+UI 级"还原，不能算语义级完成；修改/移植该端无从下手 | 按 UI设计② 的接线表为骨架，对 MainController/HuPaiKit/serve/* 逐方法写语义注释文档（可分模块：牌墙渲染、操作分发、胡牌引擎、规则插件） |
| A2 | **游戏端零运行时验收**：从未运行过还原 jar 或原游戏端连任何服务器（含本地 fixture）；sconfig.config 从未实际构造 | 路线图 B 待办 5"造出 SystemConfig 实例…连自建 fixture 跑通登录+IM"仍开放；逆向④ §9.3/9.4 自认；本审计核实 fixture_server 无任何 UDP/7901 代码 | UDP 7901 IM 协议、`<user_id>_2` 登录、typeu=1 推送全部只在源码层理解，无一次端到端验证 | 先用 jre-9 + 伪 sconfig（DES 配方已具备）对 `java -jar quesheng.jar` 做无服务器冒烟（登录窗渲染）；再写 UDP 7901 mini-fixture 闭合登录+IM |
| A3 | **Myclassification.dll 缺失**：`ai_val==-1` 本地识别分支 P/Invoke 的 DLL 是厂商单独下发件，三个原始包均无 | 功能验证报告 §五.1；`HomeViewModel.cs`（shibie 调用） | 本地 AI 识别路径无法实测，还原版与原版在该分支行为等价性未验证 | 保持 fixture 恒发 ai_val≠-1 的绕行策略并记录；取得 DLL 前将该项永久列为诚实边界 |
| A4 | **OTA 全链路未实测**：真实服务器与 fixture 均恒"无更新"，check_update→下载（AutoUpdateThread/UpdateAppPage）→替换→重启分支零次运行 | 功能验证报告 §五.3；路线图 A 待办 2；`server.py:529-532` 恒 success:false | OTA 是唯一会"替换程序自身"的代码路径，未验证即存在未知行为风险 | fixture 增加可开关的"有更新"模式（下发本地假 exe）打一次全链路 |
| A5 | **右键"删除选中"菜单无确定性点验**；DeleteOffset/ShiftForward 只有源码级闭环 | 功能验证报告 §五.2 自认；fulltest 日志 :81,:154,:262,:356,:448 D3 弱判据 PASS、:403 SKIP（空桌） | 删牌重排是 2.0.15 的核心业务演进（A4 B3/B4/B5/B6 的接收端入口），入口菜单本身从未被确定性点击 | 人工鼠标点验一次（点一张已渲染牌面→右键→删除→观察前移+DeleteOffset 日志"删除并前移"）；或用带真实牌面的 fixture 轮重跑 |
| A6 | **PC 运行时等价性（dnSpyEx 断点调试）未做**：构建通过≠行为等价 | 项目核查 §5.1 自认；路线图 W11 待办 1 | 冒烟+fixture 已覆盖大部分可见行为，但异常分支（断网重连竞态、INI 损坏、单点登录互踢后行为）未对照原版 | 按 dnSpyEx 对照原版 x86 断点验证 3-5 个关键路径（登录失败/WS 断线/收满自动发送） |
| A7 | **exe4j 静态解包器未实现**（载荷加密算法未逆向） | 逆向④ §1.1/§9.2 自认 | "离线纯静态还原"不可行；当前依赖动态提取（可复现，依赖 .sandbox-game 的 exe+jre-9） | 低优先级：仅在需要"不运行程序即可解包"的场景才值得实现 |
| A8 | **安卓仓库遗留**（另一仓库）：612 方法中 387 未语义闭合、T 系列分支、OTA 页、实机截图、msg117 第二段、生产端完整一局 | 路线图 C 节 1-6；根 README:4 "实机协议验证已完成"指向该仓库，本仓不可复核（？） | 三端体系中安卓端语义完成度自评仅 80%，"体系整体完成"不成立 | 在该仓库执行同口径审计；本仓文档引用安卓结论时应带 UNVERIFIED 标注 |
| A9 | **游戏端 ai/config.txt 权重实际取值未取得**（需实机样本） | 路线图 B 待办 4；逆向④ §3.3 | AI 推荐行为只能静态描述默认权重 | 收集一份真实部署的 config.txt 或标记永久未知 |
| A10 | **choseIp 真实配置样本未动态比对**（`:8081` 追加配方与真实部署可能不符） | 项目核查 §3 表第 7 行、逆向④ §5 | 若真实环境 choseIp 存的是"IP:8081"，登录会拼出 `IP:8081:8081`——文档已改用 host/IP 示例，但无真实样本证实 | 需真实环境样本；无样本前保持文档现口径并保留待验证标注 |

### B 级（12 项）

| # | 问题 | 依据 | 建议 |
|---|---|---|---|
| B1 | 百分比口径不一且无验收口径（97%/85%/80% vs "不作为验收指标"） | M5 | 统一为"分级清单完成度"（如 A 级清零率），废除百分比 |
| B2 | 重复副本未合并：逆向①②③/逆向分析报告/restore-status 均存 root+docs 双份（内容仅链接差异）；状态文档 4 份（root、docs、analysis×2） | 项目核查 §3 末段自认 | 指定唯一权威版本，其余加"已废弃，见 X" |
| B3 | 根 README 落后第二轮：R1（0/4→0/0）、R2（构建失败→PASS）、R3（baml-strings 路径/数字双错） | §2.1 | 按实测更新三行 |
| B4 | restore-status.md:51 "136 图片"残留（同文件 129 与 136 并存） | M4 | 删除 :51 的 136 或改为 129 |
| B5 | fixture README "81 项"过时（实际 82） | M6 | 更新数字 |
| B6 | 功能验证报告引用中间轮 "PASS=35"，最终轮 39/0 | M7 | 更新为最终轮数字并注明轮次 |
| B7 | FIXES.md §3 方法体对账算术缺口（1986≠2×1003） | M9 | 补一句口径说明（如 20 个未配对体的去向）或修正数字 |
| B8 | UI设计① "39 个集合属性"应为 45（同文枚举即 45） | U2 | 改数字 |
| B9 | 逆向④ "62 行声明段"应为约 145 行 | D6 | 改数字 |
| B10 | docs/restore-status.md 勘误头停留在第二轮之前（"旧版仍有 6 XAML 错误"已失效） | H1/M8 | 勘误头加一行指向第二轮 |
| B11 | 逆向① §5 "QoS 阈值 ~5 秒级"与代码（10s 定时器/60s 阈值）不符；§9 未决项未标 A2 已闭环 | V6/V7 | 更正并加"已由 A2 闭环"注记 |
| B12 | A2 引用 loginwindow.strings.txt:5 实为第 4 行；设备登录.md 把 IP 写在"端口："标目下 | B2/§2.16 | 顺手修正（本审计不改文件） |

---

## 5. 总评

1. **可复现率高的部分**：所有能跑的数字声明（构建 0 Error、84/84 类、82 项自测、14 jar/62 java/9 fxml/2 css/129 图、7956 行、8,978,432 覆盖区、SHA-256 132 文件、MD5 X 格式、542 条修复表、38 项环境检查）经本审计独立重跑**全部成立**。这个仓库的"硬数字"文化是可靠的。
2. **失分点集中在两处**：一是**状态类形容词**（"全按键"、"均 0 警告"、"100%"、"完成 ✅"）总是比其下的明细表更乐观——明细表几乎都对，总括句屡屡超前（S1/S7/U2/D6/V6）；二是**勘误只做一遍**：勘误头写了，正文旧数字（136、81、35、6 个错误）残留未清，同一个文件里新旧结论并存。
3. **"逆向工程完成"的定性口径**：若定义为"PC 图传两端：源码级还原+可编译+关键功能真机闭环"——已基本达成（余 A5/A6 两项收尾）；若定义为"三端体系全部完成"——**远未达成**（游戏端 A1/A2 为结构性缺口，安卓 A8 在另一仓库且自评 80%）。

（本审计未修改除本文件外的任何文件；所有构建产物为既有的 bin/obj/out 增量，属审计命令的预期副作用。）
