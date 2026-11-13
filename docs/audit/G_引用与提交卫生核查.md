# G · 引用完整性与提交卫生核查（对抗性审计，目录大整理后）

- 审计对象：2026-11-12 目录大整理（`PC端/` 创建；fixture_server→`PC端/fixture_server/`；runtime-test→`PC端/测试副本/`；原始包/解压移入 `PC端/`）+ 新增 `game-fixture/`、`tools/` 之后的**引用完整性、.gitignore 正确性、暂存区卫生**。
- 审计时间：2026-11-13。方式：只读扫描（grep 全部被 git 跟踪的文本文件）+ `git check-ignore -v` 逐条验证 + `git diff --cached` 静态分类；AGENTS.md 构建命令按授权**真跑**（dotnet build ×2），fixture 启动/自测/环境检查命令按边界**不执行**（标 UNVERIFIED-runtime）。
- 审计人：对抗性审计子代理（G 轮）。未修改任何既有文件、未动暂存区；构建产物 bin/obj 验证后已删除（恢复审计前空态）。

## 结论行

**核查：断链 live-broken=6 historical=19；ignore 失效=0；暂存区违规划分=captures/logs=13、凭据=1、INI/class/runtime/.sandbox-game=0（其余 311 项合规）；README 偏差=6（结构缺 4 + 过时声明 2）。交叉引用抽验 15 处：10 命中 / 5 未命中（2 处系修复后合理演进、2 处行号漂移、1 处副本陈旧）。**

---

## 0. 快照与并发声明

- 首次快照 `git diff --cached --name-only` = **324 项**（A258/M56/R100 改名10/D0）；审计中途变为 **325 项**：`docs/audit/F_游戏端声明复核.md` 被并行会话加入暂存区。
- 审计收尾时工作区出现**未跟踪**新文件 `docs/audit/E_PC忠实性复核.md`（并行会话正在撰写）。本报告 snapshot 口径以 325 项为准，注明动态。
- 工作区与暂存区一致（`git diff`（未暂存）对 analysis/ 为空；全仓无未暂存修改、无其它未跟踪文件）。

---

## 1. 断链引用全量扫描

扫描域：`git ls-files` 中 .md/.java/.py/.cs/.ps1/.cmd/.gitignore/.csproj 等文本文件（318 个）+ `.tools/` 下 ps1（被使用但被 ignore，按任务要求一并核查）。目标旧路径：`fixture_server/`（无 PC端/ 前缀）、`runtime-test/`、根级 `xcs电脑图传/`、根级 `鑫财神图传PC/`、`xcs电脑图传(新).zip`（无前缀）。

### 1.1 live-broken（6 处）——引用者会被使用/执行，路径已失效

| # | 引用者:行 | 引用内容 | 失效原因 | 建议 |
|---|---|---|---|---|
| L1 | `AGENTS.md:30`（Testing Guidelines） | "Extend `fixture_server/selftest.py` using …" | 该文件现位于 `PC端/fixture_server/selftest.py`；AGENTS 是活跃指令文件，此命令路径失效 | 改为 `PC端/fixture_server/selftest.py`（与同文件 :19-20 命令块一致） |
| L2 | `docs/Windows逆向环境清单.md:64` | `ilspy .\xcs电脑图传\xcs.exe` | 根级解压目录已移至 `PC端/原始包解压/xcs电脑图传/`；该文档是"常用命令"清单，按图索骥会失败 | 路径前缀改为 `PC端\原始包解压\` |
| L3 | `docs/Windows逆向环境清单.md:65` | `dnspy .\xcs电脑图传\xcs.exe` | 同上 | 同上 |
| L4 | `scripts/Test-ReverseEnvironment.ps1:104` | dnSpyEx CLI 探针目标 `(Join-Path $projectRoot 'xcs电脑图传\xcs.exe')`，期望输出 `PROTOCOL_TYPE_C_FROM_CLIENT_TYPE_OF_LOGIN = 6` | `$projectRoot`=仓库根，根下已无该目录 → 探针必然失败（脚本其余 37 项检查不受影响） | 改为 `'PC端\原始包解压\xcs电脑图传\xcs.exe'`；或与 AGENTS/环境清单同步修订 |
| L5 | `.tools/smoke-ui/fulltest.ps1:8-11`（未跟踪工具，PC端/README.md:13 引用） | `$dir='F:\xcs-pc-reversal\.tools\smoke-ui'`，`$wd=Join-Path $dir $Ver`（`new`/`old`），从中启动 `xcs.exe`/`distinguish.exe` | `.tools/smoke-ui/` 下**已无 `new/`、`old/` 子目录**（被测副本现已归 `PC端/测试副本/{xcs-2015,dist-2014,orig-2015}`）→ 脚本当前不可运行 | 若仍需复跑，把 `$wd` 指向 `PC端/测试副本/...`；或在 PC端/README 注明该脚本为历史记录 |
| L6 | `restore-status.md:37` | "`.tools/smoke-ui/` 与 `runtime-test/` 的 INI 均为外部测试夹具" | `runtime-test/` 目录已不存在（现 `PC端/测试副本/`）；restore-status 是**现状文档**（同文件 :3-4 已声明目录整理，正文此句未同步） | `runtime-test/` 改 `PC端/测试副本/` |

### 1.2 historical（19 处）——证据报告/历史快照中的旧路径，保留原文合理

AGENTS.md:8 已载明总注记（"old `fixture_server/` paths in older reports refer here"），以下各处可按需加"已移至"行内注记，不强制：

| 引用者:行 | 旧路径引用 | 性质 |
|---|---|---|
| `restore-status.md:56` | `原版 xcs电脑图传/xcs.exe`（2026-11-12 同屏对照记录） | 带日期的对照记录 |
| `逆向分析报告.md:69` 与 `docs/逆向分析报告.md:69`（双副本各 1） | `fixture_server.py 可据 C# 源码进一步补齐…`（首轮展望，彼时 fixture 尚未成形） | 历史快照+勘误头 |
| `docs/项目理解与文档核查.md:11` | "根目录三个 ZIP：`xcs电脑图传/`、`鑫财神图传PC/`、`游戏端/`" | 当时布局快照 |
| `docs/项目理解与文档核查.md:97,98` | SHA-256 表引用 `xcs电脑图传/xcs.exe`、`鑫财神图传PC/鑫财神/distinguish.exe` | 证据表（哈希仍可按新路径复核） |
| `docs/audit/A_PC工程忠实性核对.md:4,29,49` | `xcs电脑图传/xcs.exe`、`鑫财神图传PC/鑫财神/…`、两原包目录 | 审计证据（2026-11-12） |
| `docs/audit/C_文档诚实性审计.md:25,49,186,191,230` | `python fixture_server/selftest.py`、`fixture_server/README.md`、`fixture_server/requests.log`、`fixture_server 无 UDP 代码` | 审计证据 |
| `docs/audit/D_验证记录核查.md:16,54,82,84,86,90,97,165,166` | `fixture_server/requests.log`、`cd fixture_server && python selftest.py`、runtime-test §5 整节 | 审计证据（行号抽验见 §5，仍命中） |
| `docs/audit/审计修复台账.md:90` | "构建哈希与 runtime-test 副本一致" | 审计记录（副本现系 `PC端/测试副本/`；哈希断言已因后续重建过期，见 §5 第 9 条与 §6） |

### 1.3 专项核查：`.tools/` ps1 与 `game-fixture/run_acceptance.ps1`

| 脚本 | 硬编码路径核查结果 |
|---|---|
| `.tools/invoke-btn.ps1` / `dump-buttons.ps1` / `read-dialog.ps1` / `invoke-idx.ps1` | **无仓库路径硬编码**（全部经 `-TargetPid`/`-WinTitle` 参数化）→ 移动后不受影响 ✅ |
| `.tools/smoke-ui/fulltest.ps1` | 见 L5：`new`/`old` 被测目录不存在，**live-broken** |
| `.tools/smoke-ui/inspect-relaycommand.ps1` | 引用 `analysis\xcs-cs112\GalaSoft.MvvmLight.dll` → 文件存在 ✅ |
| `.tools/smoke-ui/check.ps1`、`diag.ps1` 等 10 余个 | 仅引用 `.tools/smoke-ui` 自身目录 ✅ |
| `.tools/supplement-local.ps1` | 指向仓库外工具根（`F:\逆向工具`、`F:\Dev`）——一次性环境装配脚本，非仓库布局依赖 ✅ |
| `game-fixture/run_acceptance.ps1` | `$ToolsDir=仓库根\tools`、`$JarsDir=仓库根\game-jars`、`$SandboxDir=.sandbox-game`：**tools/ 与 game-jars/ 未被移动，仍成立** ✅。依赖物实存：`tools/MakeSconfig.java`+`tools/MakeSconfig.class`（class 被 ignore，game-fixture/README.md:21 已写明 fresh clone 需先 `javac` 编译）、`game-jars/QueshengDaPai.jar`、`.sandbox-game/游戏端/游戏端.exe`。`game-fixture/README.md:15` 的 `../tools/MakeSconfig.java` 相对引用 ✅ |

### 1.4 AGENTS.md 命令逐条可执行性验证

| # | AGENTS.md 命令 | 验证方式与结果 |
|---|---|---|
| 1 | `dotnet build analysis/xcs-cs112/xcs.csproj -c Release` | **真跑 ✅ 0 Error / 4 Warning**（SYSLIB0014×2 主工程+wpftmp 双计），5.83s；NuGet 全局缓存 `F:\Dev\Cache\nuget` 命中 `system.drawing.common/8.0.10`，全程离线完成。**注意**：README.md 声称 distinguish"本机构建未通过"——见第 2 行，已过时 |
| 2 | `dotnet build analysis/distinguish-cs112/distinguish.csproj -c Release` | **真跑 ✅ 0 Error / 4 Warning**（SYSLIB0014×2×双计），2.21s。与 restore-status.md/台账"两版均 0E/4W"一致；**README.md:11 "本机构建未通过"为过时声明**（C 报告 B3 已指出，仍未更新）。产物哈希：`xcs.dll=ec847b21…`、`distinguish.dll=1bd35690…` |
| 3 | `.\game-project\build.cmd` | **静态核查 ✅ / UNVERIFIED-runtime**（按边界未执行）：build.cmd 存在；依赖齐备——PATH 上 javac 21.0.12.1（JDK21）✅、`game-jars/` 14 jar 含 QueshengDaPai.jar ✅、`..\.sandbox-game\游戏端\jre-9\lib\modules` 存在 ✅、`javafx9\javafx9-classes.jar` 已存在（脚本跳过重抽取）✅ |
| 4 | `python -m pip install websockets cryptography` | **静态核查 ✅ / UNVERIFIED-runtime**（该命令本身会联网，按"不联网"约束未执行）：两包已安装且版本与 fixture README 声明一致（websockets 17.4 / cryptography 50.0.2），`python -I -c "import …"` 通过 |
| 5 | `python PC端/fixture_server/server.py` | **静态核查 ✅ / UNVERIFIED-runtime**（按边界不启动）：文件存在、`ast.parse` 语法通过；绑定 127.0.0.2:80/8081/18889，日志写同目录 requests.log/rounds.jsonl（均被忽略）。另实测当前无 fixture 进程占用 127.0.0.2 三端口（127.0.0.1:8081 为本机 Docker，与本 fixture 无冲突） |
| 6 | `python PC端/fixture_server/selftest.py` | **静态核查 ✅ / UNVERIFIED-runtime**：文件存在、语法通过；会自拉起 server 子进程并写 selftest_server.log（已忽略） |
| 7 | `pwsh -File scripts/Test-ReverseEnvironment.ps1` | **静态核查 ⚠️ / UNVERIFIED-runtime**：脚本存在（#requires 7.2）；但 L4——:104 dnSpyEx 探针目标已随目录移动失效，运行后该项必为 Failed/校验串不命中。**该命令"可执行但结果含已知失败项"** |

---

## 2. .gitignore 正确性（逐条 check-ignore -v 实证）

`git ls-files -i -c --exclude-standard` = **0**（没有任何"被跟踪但应被忽略"的文件）。全部 14 条根规则 + 5 条子目录规则与当前布局吻合：

| 规则（.gitignore 行号） | check-ignore 实证样本 | 结论 |
|---|---|---|
| `bin/`、`obj/`（:1-2） | `analysis/xcs-cs112/bin/…`（本轮构建临时产生后已删） | ✅ 生效 |
| `*.user`（:3） | — | ✅ |
| `游戏端/游戏端/jre-9/`（:4，CRLF） | `游戏端/游戏端/jre-9/lib/modules` 命中 | ✅ 生效（jre-9 实际在两处：`游戏端/游戏端/jre-9/` 与 `.sandbox-game/游戏端/jre-9/`，**两处均被覆盖**——后者由 ：7 `.sandbox-game/` 覆盖） |
| `game-jars/`（:5）、`game-src/`（:6） | `game-jars/QueshengDaPai.jar`、`game-src/quesheng/…` | ✅ 生效（两目录仍在根，未移动） |
| `.sandbox-game/`（:7） | `.sandbox-game/游戏端/sconfig.config`（**含 DES 加密凭据的文件确认未入暂存区**） | ✅ 生效 |
| `.tools/`（:8） | `.tools/reverse-environment.json` | ✅ 生效 |
| `game-project/javafx9/`（:9）、`game-project/out/`（:10） | `game-project/out/quesheng.jar`、`javafx9/javafx9-classes.jar` | ✅ 生效（暂存区无 out//javafx9/ 文件） |
| `PC端/测试副本/`（:13，**本轮新增**） | `PC端/测试副本/xcs-2015/appsettings.ini`、`xcs.exe`、`dist-2014/distinguish.exe`、`orig-2015/xcs.exe` 全部命中 | ✅ 生效（含明文密码的 appsettings.ini 未入暂存区） |
| `game-fixture/runtime/`（:16）、`tools/*.class`（:17）、`game-fixture/__pycache__/`（:18，均为本轮新增） | `runtime/acceptance.log`、`tools/MakeSconfig.class`、`__pycache__/x.py` 命中 | ✅ 生效 |
| `PC端/fixture_server/.gitignore`（本轮新增，自身已暂存）：`requests.log`/`rounds.jsonl`/`selftest_server.log`/`__pycache__/` | `PC端/fixture_server/requests.log`、`rounds.jsonl` 命中（:2/:3） | ✅ **子目录 .gitignore 有效拦住了 `git add -A`**：暂存区中 fixture_server 仅 5 个源文件（.gitignore/README/selftest.py/server.py + 无），**无任何运行期产物混入** |

**ignore 失效=0**。三条卫生注记（不计失效）：
1. `.gitignore` 前 6 行 CRLF、后 12 行 LF，**混合换行**（`cat -A` 实证）；git 实测不受影响（:4 CRLF 规则照样命中），建议统一。
2. `游戏端/游戏端/error.log`（启动失败日志）与 `游戏端.zip`（61MB）、4 个原始 exe **在 HEAD 历史中已被跟踪**——error.log 按"logs 排除"政策属历史遗留违例；ignore 规则不能作用于已跟踪文件，需 `git rm --cached` 才能移出（见 §3.3 可选项）。
3. 无全局 `*.log` 规则是有意设计（game-fixture/evidence 日志问题属暂存选择，见 §3.1），维持现状即可。

---

## 3. 暂存区卫生（对照 AGENTS.md："Exclude credentials, local INI settings, captures, logs, and generated outputs"）

### 3.0 总分类（325 项）

| 类别 | 项数 | 判定 |
|---|---|---|
| analysis/ 两还原工程源码+XAML+res 音效+restore-status | 71 | ✅ 源码，应提交 |
| game-project/ src+resources+build 脚本+FIXES/README | 210 | ✅ 源码与资源（牌图 129 张=还原产物本体，非 captures） |
| PC端/（README + fixture_server 4 文件） | 5 | ✅ |
| PC端/原始包 2 zip + 原始包解压 8 文件（exe/wav） | 10 | ✅ R100 改名（HEAD 已跟踪的原件，"preserve originals" 政策） |
| docs/（报告/路线图/登录文档等 22）+ 根文档 5 + AGENTS/.gitignore/README | 30 | ⚠️ 其中 1 项违规（设备登录.md，见 3.2） |
| docs/audit/ A/B/C/D/F/审计修复台账 | 6 | ✅ |
| **game-fixture/ 源码+README（4）+ evidence（13）** | 17 | ⚠️ 其中 13 项违规（见 3.1） |
| scripts/、tools/MakeSconfig.java、根 restore-status | 3 | ✅（MakeSconfig.class 未入暂存 ✅） |

### 3.1 违规一：`game-fixture/evidence/` captures+logs（13 项，与 F 报告 N8 一致并扩展到 PNG）

按政策"captures, logs, and generated outputs"**全部 13 项不应入提交**；其中截图 9 张系台账/README 验收声明点名引用的证据件（台账:114、131-132），提交人可明示保留——**默认按政策处置建议如下**：

**git rm --cached 建议清单（13 项）：**

```
git rm --cached game-fixture/evidence/error.log
git rm --cached game-fixture/evidence/requests.log
git rm --cached game-fixture/evidence/udp.log
git rm --cached game-fixture/evidence/udp_type2_push_frame.txt
git rm --cached game-fixture/evidence/game_036s.png
git rm --cached game-fixture/evidence/game_final.png
git rm --cached game-fixture/evidence/interactive_flower_round.png
git rm --cached game-fixture/evidence/runA_38s_main_ui.png
git rm --cached game-fixture/evidence/runB_36s_main_ui.png
git rm --cached game-fixture/evidence/runC_1_pre_push.png
git rm --cached game-fixture/evidence/runC_2_post_push_counts.png
git rm --cached game-fixture/evidence/runC_3_post_render_tiles.png
git rm --cached game-fixture/evidence/runD_control_empty_board.png
```

（若决定保留 PNG 作验收证据：至少保留 runC_3_post_render_tiles.png / runD_control_empty_board.png / interactive_flower_round.png 三张关键判定图并在提交说明声明"验收 captures 特批入库"，其余 6 张退暂存；日志 4 项无论如何建议退暂存——D 报告引用的行号在 `PC端/fixture_server/requests.log`，不在 evidence 副本。）

### 3.2 违规二（最重要）：`docs/设备登录.md` 含真实凭据（1 项）

- 暂存区状态：**A（新文件）**，内容：`端口：8.137.196.28 / 账号：XCS232 / 密码：******`（+互踢警示）。
- 政策冲突："Exclude credentials" —— 这是本轮暂存中**唯一直接违反凭据条款的新增文件**；且 `PC端/README.md:14` 明文引用并复述该账号（"测试账号：XCS232/***@8.137.196.28"）。
- **处置建议（只报告，由用户决定）**：该文档是用户提供的实机凭据，属"用户自行决定去留"范畴。若决定不入库：`git rm --cached docs/设备登录.md` + `.gitignore` 增加该条 + `PC端/README.md:14` 的明文账号行一并处理（README 本身也会把凭据带入历史）。若决定保留（私有仓库+实机测试必需），建议在 AGENTS.md"Configuration & Evidence Handling"节加一条例外声明，使政策与实际一致。

### 3.3 其余类别全部为 0（实证）

| 检查项 | 暂存区实况 |
|---|---|
| b) `PC端/fixture_server/requests.log`、`rounds.jsonl` | **未入暂存**（子目录 .gitignore :2/:3 拦截，`git check-ignore -v` 实证）✅ |
| c) `tools/*.class` | 未入暂存（`tools/MakeSconfig.class` 被 :17 拦截；仅 .java 入库）✅ |
| d) `.sandbox-game/` 下任何文件（含 DES 凭据 sconfig.config） | **0 项** ✅ |
| e) `game-fixture/runtime/` | 0 项 ✅ |
| f) `PC端/测试副本/*/appsettings.ini`（PASSWORD=****** 明文） | **0 项**（整个测试副本目录被 ：13 拦截；`.tools/smoke-ui/fulltest.ps1:311` 写入的 `PWD=E1ADC…` INI 也只存在于被忽略目录）✅ |
| bin/obj/out/javafx9/__pycache__ 产物 | 0 项 ✅ |

历史遗留（HEAD 已提交、本轮未触碰，供后续清理决策，不属本轮暂存违规）：`游戏端/游戏端/error.log`（日志）、`游戏端.zip` 61MB。二者如需移出：`git rm --cached 游戏端/游戏端/error.log`（并考虑 `git gc`/filter 瘦身 61MB zip 的历史——影响克隆体积，由用户决策）。

### 3.4 .gitignore 新增建议（视 §3.1/3.2 处置而定）

```
# 若证据退暂存后仍留在工作区：
game-fixture/evidence/
# 若设备登录文档决定不入库：
docs/设备登录.md
# 可选：统一已跟踪日志的口径（配合 git rm --cached 游戏端/游戏端/error.log）
```

---

## 4. 根目录与整体布局、README 比对

### 4.1 根目录现状

```
.gitignore  AGENTS.md  README.md  restore-status.md
PC端/  游戏端/  游戏端.zip  .sandbox-game/  .tools/
analysis/  game-project/  game-fixture/  game-jars/  game-src/  tools/
docs/  docs/audit/  scripts/
逆向①_xcs电脑图传.md  逆向②_鑫财神图传PC.md  逆向③_游戏端.md  逆向分析报告.md
```

- **无游离 PC 端散件**：PC 相关产物已全部归入 `PC端/`（原始包/解压/测试副本/fixture_server/README）✅；`analysis/` 保持原路径系 PC端/README.md:9-10 明示的设计决定（审计证据链引用不变）✅。
- `游戏端.zip`（61MB）+ `游戏端/` 留根：与 README.md:7"游戏端.zip、游戏端/：游戏端原始包与解压"**一致** ✅（用户只要求整理 PC 端；.gitignore 只挡 `游戏端/游戏端/jre-9/`，zip 与 exe 保持入库）。
- 根级 `逆向①/②/③/分析报告` 与 docs/ 同名副本并存（root ①③+分析报告本轮有 2 行修改=勘误头/链接，② 未动）：C 报告 B2 已建议"指定唯一权威版本"，本轮未合并——维持其历史快照定位，不新判违规。
- 并发新增：`docs/audit/E_PC忠实性复核.md`（未跟踪）与 F（已暂存）使 audit 目录达 7 份。

### 4.2 README.md（根，暂存版）与实际目录树逐行比对

README"内容结构"区逐行核对后，**偏差 6 处**：

| # | 类型 | 内容 |
|---|---|---|
| W1 | 结构缺失 | **`game-project/`（62 Java JavaFX 工程+resources+build）整目录未列入内容结构**——仓库第二大目录 |
| W2 | 结构缺失 | **`game-fixture/`**（游戏端本地 fixture 验收，台账 A2 补齐项）未列入 |
| W3 | 结构缺失 | **`tools/`**（MakeSconfig.java）未列入 |
| W4 | 结构缺失 | **`docs/audit/`**（六份审计报告+台账）未列入 |
| W5 | 过时声明 | `analysis/distinguish-cs112/`："当前仍有 ILSpy `Ctor` 残留，**本机构建未通过**"——本轮实测 **BUILD PASS 0E/4W**（§1.4 第 2 行），该句为第二轮修复前旧状态（C 报告 B3/R2 指出后仍未更新） |
| W6 | 过时声明 | `analysis/xcs-cs112/`："2026-11-11 本机构建通过（0 错误 / 4 警告）"——日期/数字与现状凑巧一致（今日复测 0E/4W）但"4 警告"成因是 SYSLIB0014×2 双计，建议同步台账口径并注明复查日期，避免再漂移 |

（README 其余行与实况一致：PC端/ 描述、游戏端.zip/jre-9 ignore 说明、baml-strings 路径与"9 个"计数、docs/ 四份核查文档、scripts、环境检查节均 ✅。）

---

## 5. docs/audit 报告交叉引用抽验（15 处，覆盖 A/B/C/D/E(台账)/F 六报告）

| # | 出处 | 引用（文件:行→内容） | 实测 | 判定 |
|---|---|---|---|---|
| 1 | A:28 (D9) | `analysis/xcs-cs112/distinguish.Common/Global.cs:13`→iniConfigPath | `public static string iniConfigPath = "./appsettings.ini";` | ✅ 命中（行号仍准，内容为 A8 修复后的回退态） |
| 2 | A:20 (D1) | `analysis/xcs-cs112/distinguish.Model/Device.cs:45-56`→6 个注入属性 | :45-56 现为普通 `Id` 属性——注入属性已按台账 A3 覆盖为原版 | ◐ 未命中-合理演进（审计时快照，修复后内容已变；证据链由台账 A3 承接） |
| 3 | B:82 | `game-project/src/quesheng/customcontrole/TopPanel.java:2082`→`int var9_30 = 0;` | 逐字命中 | ✅ |
| 4 | B:94 | `.../controller/LoginController.java:147`→`Map<String, String> map` | 逐字命中 | ✅ |
| 5 | C:128 (D6) | `game-src/quesheng/model/SystemConfig.java`→"该文件 188 行" | wc -l = 188 | ✅ |
| 6 | C:196 (T3) | `HttpReq.cs:38`→读 `dictionary["code"]` | **analysis/xcs-cs112/HttpReq.cs:38 现为后加的 try 非JSON护栏行**（台账"崩溃修复"所致）；原始基线 `analysis/xcs-cs/distinguish.Common/HttpReq.cs:38` 仍逐字命中 | ◐ 未命中-合理演进（还原层因有意修复行移位；引用口径应注明指原始层） |
| 7 | D:22 | `fixture_server/requests.log:399`→start_games 09:40:56 success | **路径已变更**→`PC端/fixture_server/requests.log:399` = `2026-11-12 09:40:56.094|HTTP|GET /api/zhuo/start_games||200 {"success": true,…}` | ✅ 命中（内容逐字一致，仅前缀需加"已移至"） |
| 8 | D:23 | 同日志 :1049→zhuo/add 09:43:57 | 命中（POST /api/zhuo/add 09:43:57.425） | ✅（路径同上） |
| 9 | D:26 | 同日志 :1426→2.0.14 start 09:54:10 | 命中 | ✅（路径同上；日志现 1653 行，D 时代 1445→1546，追加型增长不破坏旧行号） |
| 10 | D:92-95 (§5) | runtime-test 副本 SHA-256 `fac7e708…`/`e8cddf49…` 与重建一致 | 副本（现 `PC端/测试副本/`）当前哈希=`6cc0faa4…`/`4ee6eb4c…`，与 D 记录**及本轮新构建**（`ec847b21…`/`1bd35690…`）**均不一致** | ◐ 未命中-副本陈旧：D 审计(10:05-10:15)后副本于 10:55 重建，而源码 12:09-14:06 又经历静音/崩溃护栏/兼容键三类修改——**副本现落后于暂存源码**（详见 §6） |
| 11 | D:120 (§7) | `.tools/smoke-ui/fulltest-new.log`→522 行、末轮 PASS=39/0/0 | 现 652 行（后续追加），**:522 仍为 `[09:24:56] ===== fulltest new done: PASS=39 FAIL=0 SKIP=0 =====`** | ✅ 命中 |
| 12 | 台账(E):157 | 逆向⑤ `:1224` 笔误 31→35 修正处 | :1224 现为无关表格行；修正文本现落在 **:1230**（"result==31→35 改写（:201-206；原稿笔误 35→35，F 复核修正）"） | ◐ 行号漂移（+6，F 轮对逆向⑤的三处修订连锁位移） |
| 13 | 台账(E):114/131-132 | `game-fixture/evidence/` 截图与日志、`udp_type2_push_frame.txt` | 13 个证据文件全部实存（且已暂存——处置见 §3.1） | ✅ 命中 |
| 14 | F:117 | `udp_server.py:252-255`→dup-push 循环 | 4 行逐字命中 | ✅ |
| 15 | F:81/逆向⑤:211,621 | `Config.java:201-206`→`result==31→35` 改写 | 实际改写块在 **:192-197**（`if (mj4.getResult() != 31) continue; mj4.setResult(35);`），:201-206 是 MJ_COUNT==72 分支——**逆向⑤与 F 的引用同偏 ~9 行** | ◐ 行号漂移（-9；语义内容存在，行号需勘误） |

小结：15 处中 10 处命中；5 处未命中均非"伪造引用"——2 处是审计后被有意修复的合理演进、2 处行号漂移、1 处被测副本重建陈旧（该处同时构成新的文档真实性风险，见 §6）。

---

## 6. 附带发现（审计中顺带证实，只报告）

1. **`PC端/测试副本/` 两副本已落后于暂存源码**：副本 dll/exe mtime=2026-11-12 10:55，而源码最后修改至 14:03-14:06（兼容键回加 `PreviewSettingViewModel.cs`/`OnlineConfigViewModel.cs`、HttpReq 非 JSON 护栏、HomeWindow_Closing 判空、静音）。因此 `docs/功能验证报告.md:16`"（14:14:09，OTA 旁路测试副本）**与交付源码逐字一致**"与台账:62-63 同款表述**在当前树上已不成立**（14:14 测试时副本就已是 10:55 构建）。建议：重跑构建刷新两副本，或在两文档加"副本构建于 10:55，不含 12:09 后三类变更"的时效注记。
2. `PC端/fixture_server/requests.log` 现 1653 行（D 报告时代 1445→1546，其后又 +107）——D 所引旧行号全部仍命中，追加式日志的"旧行号稳定性"再次得到实证。
3. `AGENTS.md:8` 对旧路径已有总括注记（"moved 2026-11-12; old paths refer here"）——这是 historical 类引用可不逐条加注的合理依据；但 live-broken L1（同文件 :30）与之自相矛盾，应优先修复。
4. 本审计副作用披露：真跑了 2 次 dotnet build（bin/obj 由空建有，验证后 `rm -rf` 清空复原）；未启动任何 fixture/客户端；未联网（NuGet 走本机 `F:\Dev\Cache\nuget` 缓存）；未触碰暂存区与任何既有文件。`docs/audit/` 下 E（未跟踪）/F（中途入暂存）两文件的并发变化已如实记录（§0）。

## 7. 处置清单汇总（供提交前执行；本审计不动手）

| 优先级 | 动作 |
|---|---|
| P0 | 决策 `docs/设备登录.md` 去留（凭据）；若退：`git rm --cached` + .gitignore + `PC端/README.md:14` 明文账号行同步处理 |
| P0 | `git rm --cached` game-fixture/evidence 13 项（或按 §3.1 最低保留 3 张关键 PNG 并声明） |
| P1 | 修 6 处 live-broken：AGENTS.md:30、Windows逆向环境清单:64-65、Test-ReverseEnvironment.ps1:104、restore-status.md:37、fulltest.ps1 目标目录（或标注历史） |
| P1 | README.md 补 game-project/、game-fixture/、tools/、docs/audit/ 四行，更新 distinguish"构建未通过"旧句 |
| P2 | .gitignore 统一换行；视决策追加 `game-fixture/evidence/`、`docs/设备登录.md` 条目 |
| P2 | 刷新/标注 `PC端/测试副本/` 两副本的时效状态（§6.1）；historical 引用按需加"已移至 PC端/…"注记（AGENTS.md:8 总注记已覆盖大半） |

（完）
