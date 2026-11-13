# D · 验证记录核查（对抗性审计）

审计对象：`docs/功能验证报告.md` 所载"真机功能验证"记录的可复现性与证据链一致性。
审计时间：2026-11-12 10:05–10:15（本机）。审计方式：只读仓库 + 重跑自测/构建 + 真实服务器最小探针；未修改任何仓库文件。

## 结论行

**记录核查：8 条证实 / 0 条不符 / 0 条 UNVERIFIED**（其中 2 条带口径注记，见 §1.5 与 §7）。

---

## 1. fixture 日志存在且包含所引请求

**声明**：requests.log 含 start_games 09:40:56 success、zhuo/add 09:43:57 success:true（104 张）、end_games/zhuo_reset 09:42:34/09:42:45、2.0.14 的 09:54:10/13/15 三条、config 端点 24 次命中、登录+get_server_addr 若干。

**复核方法**：对 `fixture_server/requests.log`（1445 行，08:31:10 BOOT 起）逐条 grep 时间戳与端点，并交叉比对 rounds.jsonl。

**实测结果**：

| 引用 | 日志行号 | 实际行 |
|---|---|---|
| start_games 09:40:56 success | 399 | `09:40:56.094|HTTP|GET /api/zhuo/start_games||200 {"success": true, "message": "开始游戏成功"}` |
| zhuo/add 09:43:57 success:true | 1049 | `09:43:57.425|HTTP|POST /api/zhuo/add|...(7521)|200 {"success": true, "message": "ok"}`；rounds.jsonl 第 5 行 ts=`09:43:57.424`、104 张（见 §2） |
| end_games 09:42:34 | 460 | `09:42:34.312|HTTP|GET /api/zhuo/end_games||200 {"success": true...}` |
| zhuo_reset 09:42:45 | 467 | `09:42:45.968|HTTP|GET /api/zhuo/zhuo_reset_games||200 {"success": true...}` |
| 2.0.14 start 09:54:10 | 1426 | `09:54:10.285|HTTP|GET /api/zhuo/start_games||200 success:true` |
| 2.0.14 end 09:54:13 | 1430 | `09:54:13.088|HTTP|GET /api/zhuo/end_games||200 success:true` |
| 2.0.14 reset 09:54:15 | 1431 | `09:54:15.935|HTTP|GET /api/zhuo/zhuo_reset_games||200 success:true` |
| 登录/地址下发 | 218/219、221/222 | `09:27:42.245 login success:true` + `09:27:43.816 get_server_addr success:true`；`09:31:15.063` + `09:31:15.988`（与报告"fixture 日志 09:27:42/09:31:15"吻合）；HTTP login 共 14 行、get_server_addr 共 8 行 |

config 端点按分钟分布：`08:38×6 + 08:41×6 + 08:43×12 = 24 次`（对应 08:38–08:43 四轮自测/自动化窗口），另有 `09:48×2 + 09:49×3`。

**判定**：证实。全部引用时间戳、端点、success 值与日志行一一对应。
**口径注记**：config"24 次命中"对应 08:38–08:43 窗口快照；日志为跨会话追加（11 次 BOOT，08:31–09:35），当前累计 29 次。差异源于日志持续追加，非日志滚动或造假。

---

## 2. rounds.jsonl 每条 104 张、26 种×4、每方向 26

**声明**：rounds.jsonl 存在且每条满足上述约束。

**复核方法**：python 逐行 JSON 解析并重算（不信任 tile_count 字段，按 tiles 数组实测）。

**实测结果**：5 条记录（08:38:40.710 / 08:41:48.841 / 08:43:27.593 / 08:43:50.443 / 09:43:57.424），每条：实际 tiles=104、mahjong_number 26 种每种恰 4 张、direction 1–4 各恰 26 张、每方向 serial 唯一（20250101–20250104）。

**判定**：证实（5/5 全部满足，非抽查）。

---

## 3. selftest.py 重跑通过项数 ≥82、0 FAIL

**声明**：82 项含新加 code 键回归断言。

**复核方法**：`cd fixture_server && python selftest.py`（外部已在运行的服务器保持不动；自测流量照设计落入 requests.log）。

**实测结果**：末行 `PASS=82 FAIL=0`，`全部通过`；含断言 `[PASS] app/check_update 带回归 code 键（HttpReq.cs:38 实绩…）`（selftest.py:536）。

**判定**：证实（82 ≥ 82，0 FAIL，回归断言在列）。

---

## 4. 真实服务器 start/end/reset/restart 均 0.1s success:true

**声明**：python 直连探测 4 端点均 0.1s success:true。

**复核方法**：按报告口径重建最小探针并复跑一次：POST `http://8.137.196.28:8081/api/login/login/distinguish?app_name=xcs`（XCS232/***，devicetype=5）取 jwt，随后带 `Authorization: Bearer {jwt}` 依次 GET start_games / end_games / zhuo_reset_games / restart_device，15s 超时，逐请求计时。未做任何其他写入类调用。

**实测结果**（2026-11-12 10:11）：

| 请求 | 耗时 | 响应 |
|---|---|---|
| login | 0.159s | success=true，jwt 正常下发 |
| zhuo/start_games | 0.104s | `{"code":1,"msg":"SUCCESS","success":true}` |
| zhuo/end_games | 0.115s | `{"code":1,"msg":"结束游戏成功","success":true}` |
| zhuo/zhuo_reset_games | 0.096s | `{"code":1,"msg":"重置游戏","success":true}` |
| zhuo/restart_device | 0.107s | `{"code":1,"msg":"重启设备成功","success":true}` |

**判定**：证实（4/4 均 ~0.1s success:true，与声明一致；网络可达，无 UNVERIFIED）。

---

## 5. 构建产物一致性（runtime-test 副本是否陈旧）

**声明**：runtime-test 下两份被测副本与源码当前状态一致。

**复核方法**：`dotnet build`（SDK 10.0.404）分别重建 `analysis/xcs-cs112/xcs.csproj` 与 `analysis/distinguish-cs112/distinguish.csproj`（`-c Release -p:EnableWindowsTargeting=true`，产物输出到仓库外临时目录，不触碰 runtime-test），对 dll/exe 做 SHA-256 比对。两项目均 0 警告 0 错误（2.46s / 1.88s）。

**实测结果**：

| 产物 | 重建 SHA-256 | runtime-test 副本 SHA-256 | 一致 |
|---|---|---|---|
| xcs.dll (10,272,768 B) | `fac7e708f910a0cd46ea3507b77bd5d5fe8a6c08cfb2d3238979479b60d256c5` | 同左（xcs-2015，11-12 08:45） | ✅ |
| distinguish.dll (10,185,728 B) | `e8cddf49264d97070546b578c1a308c327e78dd04ab5e765cd6c81fc4ea24205` | 同左（dist-2014，11-12 08:45） | ✅ |
| xcs.exe | `ebae05011f3761fde3fce876079e395092fb00923232fa967e0b98e666c15a95` | 同左 | ✅ |
| distinguish.exe | `dab99e3220bf74eac71c0dc1d5c2640ac22151ddf32713ef852dc29b597436d2` | 同左 | ✅ |

**判定**：证实。runtime-test 副本非陈旧，与当前源码确定性构建逐字节一致。
**旁注**：两 DLL 的 FileVersion/ProductVersion 元数据均为 2.0.1.0，"2.0.15/2.0.14"的版本身份靠源码树来源与运行时 UI 显示（日志/UIA 树"客户端版本号：2.0.15"），不靠二进制版本元数据——不影响本条结论。

---

## 6. game-project 构建可复现（轻量核对）

**声明**：`bash game-project/build.sh` 跑通且 javac exit=0（完整对账由另一审计负责）。

**复核方法**：JDK21（F:\Dev\SDKs\JDK21，javac 21.0.12.1）下执行 build.sh，观察 javac 退出码与产物。产物写 game-project/out/（gitignore 内）。

**实测结果**：`javac exit=0`、`compiled classes=84`、`jar=out/quesheng.jar size=1457341 bytes`，脚本整体 exit 0。

**判定**：证实（轻量口径：编译+打包可复现）。

---

## 7. 截图证据链：fulltest-new.log 的 PASS=35/FAIL=5/SKIP=1

**声明**：`.tools/smoke-ui/fulltest-new.log` 支撑 PASS=35/FAIL=5/SKIP=1（报告 §首引言"真实服务器全按键测试 PASS=35"）；报告引用的 fixture 日志时间戳逐条一致。

**复核方法**：按 `===== fulltest new start/done =====` 边界切分日志逐轮统计 `PASS:/FAIL:/SKIP:` 行数，与各轮 done 汇总行比对；时间戳逐条 grep（结果见 §1 表）。

**实测结果**：日志共 522 行、**6 轮运行**：

| 轮次 | 窗口 | 逐行标记 (P/F/S) | done 汇总行 |
|---|---|---|---|
| 1 | 07:35:02→07:35:12 | 0/5/0（另 1 条 ABORT） | PASS=0 FAIL=6 SKIP=0 |
| 2 | 07:50:32→07:54:29 | 9/28/0 | PASS=9 FAIL=28 SKIP=0 |
| 3 | 08:18:38→08:50:00 | 22/5/0 | （无 done 行，中断） |
| 4 | 08:50:00→08:53:52 | 32/7/0 | PASS=32 FAIL=10 SKIP=0 |
| 5a | 08:59:49→09:01:04 | 5/0/0 | （无 done 行，中断） |
| 5b | 09:01:04→09:07:36 | 35/4/0 | PASS=35 FAIL=6 SKIP=0 |
| **5c** | **09:09:59→09:20:25** | **35/4+1/1** | **PASS=35 FAIL=5 SKIP=1** |
| 6 | 09:22:15→09:24:56 | 39/0/0 | PASS=39 FAIL=0 SKIP=0 |

- 引用的 **PASS=35/FAIL=5/SKIP=1 确实存在**，对应 09:09:59→09:20:25 轮的 done 行。
- **FAIL=5 的构成**：4 条显式 `FAIL:`（C2/C3/C4/C5 "no dialog appeared"）+ 1 条 `UNEXPECTED-DIALOG after [C3 结束游戏]: '消息提示' text='结束游戏成功'`（09:16:04，成功弹窗迟到，越过脚本超时后才出现，被计为异常事件）。即第 5 个"失败"实质是**迟到的成功**——与报告 §三"按钮零缺陷、脚本元素命中问题"的叙述自洽。
- 时间戳逐条 grep：09:40:56 / 09:43:57 / 09:42:34 / 09:42:45 / 09:54:10 / 09:54:13 / 09:54:15 / 09:27:42 / 09:31:15 全部与日志实际行一致（见 §1）。

**判定**：证实。
**口径注记**：PASS=35 是第 5c 轮的快照；日志**末轮（09:22:15→09:24:56）为 PASS=39 FAIL=0 SKIP=0 全过**（C2–C5 全部转 PASS）。报告引用中间轮数字不构成不符，但引用时日志已有更优的末轮结果，读报告者应知最终状态是 39/0/0。

---

## 8. 事件日志干净声明（09:00–10:00）

**声明**：验证窗口内 Windows Application 日志无 xcs/distinguish/.NET 相关错误。

**复核方法**：`Get-WinEvent -FilterHashtable @{LogName='Application'; Level=1,2; StartTime=09:00; EndTime=10:00}`（对 System 日志与全天窗口亦做同口径复查，排除查询本身失效的假阴性）。

**实测结果**：

- Application Level 1/2，09:00–10:00：**0 条**（"找不到任何与指定的选择条件匹配的事件"）；System 同窗口亦 0 条。
- 全天复查：仅 **07:53:55 / 07:53:56** 各 1 条——`.NET Runtime` id=1026 与 `Application Error` id=1000，`xcs.exe 版本 2.0.1.0`，异常码 0xe0434352（.NET 未处理异常），对应 fulltest 第 2 轮（07:50–07:54，PASS=9/FAIL=28）期间的一次崩溃。
- fulltest 日志各轮自带 `eventlog: no app errors during run`（末轮 09:24:56 在列），与本查询互证。

**判定**：证实（限定于 09:00–10:00 审计窗口）。
**注记**：若把声明外推为"全天事件日志干净"则不成立——07:53 有 2 条 xcs.exe 崩溃记录。该时刻在审计窗口之外且早于本轮验证，属 fulltest 第 2 轮故障期的遗留证据，记录于此以保持完整性。

---

## 无法复核的项

无。8 条声明全部完成实测复核。

## 审计过程对运行期产物的扰动披露

- `python fixture_server/selftest.py`（§3）按其设计向 `fixture_server/requests.log` 追加了自测流量：行数 1445 → 1546（+101 行，时间戳 10:07–10:08，含 `/_fixture/ping`、`/_fixture/reset` 与自测业务请求）。这是自测的预期行为（只追加、不改写旧行），§1 所引旧行号与内容不受影响。
- §5 构建在 `analysis/*/obj/` 下产生了增量编译中间文件（bin/obj 均在 .gitignore），产物本体输出到仓库外临时目录并已随审计结束不再引用；runtime-test 两副本未被触碰（哈希前后一致）。
- §4 探针对真实服务器执行了报告记载的最小写入面（start/end/reset/restart 各一次 GET），未做其他调用；按 docs/设备登录.md 的单点登录约定，探针结束后未保持会话。
