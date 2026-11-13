# PC 端 UI 还原状态（2026-11-12 审计轮更新）

> 目录整理（2026-11-12）：PC 全部产物归入 `PC端/`（原始包/原始包解压/测试副本/fixture_server，
> 总索引 `PC端/README.md`）；`analysis/` 反编译与工程保持原路径（审计证据链引用不变）。

## 当前构建状态（审计 A 轮复核后）

- xcs 2.0.15：`analysis/xcs-cs112` → **0 Error / 4 Warning**（SYSLIB0014 WebClient 过时 API，原版代码真实告警，如实保留）
- distinguish 2.0.14：`analysis/distinguish-cs112` → **0 Error / 4 Warning**（同上）
- 均为 net8.0-windows / x64；产物 bin/Release/net8.0-windows/
- 跨平台：net8.0-windows + win-x64 自包含发布可用；Mac 需 WineHQ 直链（待办）

## 忠实性审计与修复（2026-11-12 审计 A 轮，详见 `docs/audit/A_PC工程忠实性核对.md`）

审计裁定 **10 处实质偏差**，已全部修复（方向：向原版对齐）：

1. **杜撰逻辑（最严重）**：两版 `PreviewSettingViewModel.saveParams` 向 set_params_v2 多发
   6 个 JSON 键（bate/img_add_num/gk_min/gk_max/upload_img_clarity/count_type）；
   `OnlineConfigViewModel` 的 set_data_config 多发 upload_img_clarity；
   配套 Device.cs +6 属性、DataConfig.cs +1 属性。
   原 exe 二进制扫描全部 0 命中 → 已剔除，与原始反编译逐字一致（除 IL 注释）。
   **后续修订（2026-11-12 下午）**：真机实测确认当前线上服务器已演进、强制要求这些键
   （缺键返回 serde 非 JSON 文本并崩溃），经用户批准已作为"当前服务器兼容性适配"重新加入
   **仅出站 JSON 键（常量 0）**；Device.cs/DataConfig.cs 模型属性仍保持原版（A3/A4 不回退）。
   详见下方"当前服务器兼容性适配 + 崩溃修复 + 静音"节与 `docs/audit/审计修复台账.md` A 轮后续。
2. **OnlineWindows.xaml**：两组 RadioButton（数据发送/计数方式）丢失
   RadioToBoolConverter+ConverterParameter 绑定（baml-strings 铁证），导致设置无法切换 → 已恢复
   （ConverterParameter "0"/"1" 为推断值——BAML 优化存储未留字符串，见审计报告）。
3. **HomeWindow.xaml**：丢失原版唯一一处 IsAsync="True" 绑定（原 exe 二进制 1 命中）→ 两版均已恢复。
4. **夹带 appsettings.ini**（非原包文件，含 VERIFY/PWD=md5("******")，改变开箱门令行为）
   → 已从两工程目录与 csproj 移除。
5. **Global.iniConfigPath**：`"./appsettings.ini"` 曾被改为 exe 绝对路径（上一会话有意的
   自包含适配，理由见下方历史记录）→ 本轮按"严格忠实原版"回退为 `"./appsettings.ini"`。
   副作用提示：双击启动时 CWD 可能不在程序目录，INI 会落错位置；验收/测试请用
   `Start-Process -WorkingDirectory <程序目录>` 方式（本仓库所有自动化即如此）。
6. **SYSLIB0014 pragma 抑制**（非原版源码内容，为凑"0 警告"而加）→ 已移除，警告如实显示。
7. ~~运行时冒烟目录 `appsettings.ini`~~ 与其口径说明：`.tools/smoke-ui/` 与 `PC端/测试副本/`
   的 INI 均为**外部测试夹具**，不再随工程分发。

### 历史记录（上一会话 2026-11-11 的有意适配，已被本轮回退，留档）

> 上一会话曾有意新增 `appsettings.ini`（PWD=E1ADC…=GetMd5("******")）并把 iniConfigPath
> 改为绝对路径，理由是原版相对路径在双击启动时会被 Win32 GetPrivateProfileString 解析到
> Windows 目录。本轮审计裁定该改动偏离原版行为（开箱门令与默认口令体系不一致），
> 已回退；其关切改由"外部 INI + 指定 WorkingDirectory 启动"的测试方式满足。

### 修复过程中核实的原版事实

- Connect() 事件接线：9 窗口 × 2 版本 = **16/16 组全对**（对照原始反编译 Connect() 表）
- RemoveItem 裁决：A4 报告正确——"删牌前移(ShiftForward/DeleteOffset)"是 2.0.15 新增；
  2.0.14 原版只清槽位（distinguish.exe 二进制 ShiftForward/DeleteOffset 0 命中）；两还原工程各自忠实
- 串版本检查：distinguish-cs112 对 6 个 2.0.15 专有标记 0 命中 ✓

## 原版 vs 还原 同屏对照（2026-11-12）

原版 `xcs电脑图传/xcs.exe`（.NET Framework 4.7 原始二进制）与还原版同 INI 对照：
登录窗 UIA 元素 **23/23 逐一相同**、皮肤像素级一致；
"SERVER 预填后口令地址栏仍显示"经对照确认是**原版固有行为**，非还原偏差。

## 真机功能验证（2026-11-12，详见 `docs/功能验证报告.md`）

fixture 全链路验证通过，**验证范围如实声明**：
- 已实测（fixture）：门禁/登录(RSA)/WS 心跳/设备轮询/开始/结束/重置/
  WS 全回合(113→101×104→114)→自动发送 zhuo/add(104 张全校验)/设置窗 4 按钮
  （应用张数/立即应用×2/重启所有设备）/画框预览全链路/发送统计与状态 UI
- 真机 fulltest（XCS232@8.137.196.28）**最终交付构建复验（2026-11-12 下午）**：
  新 xcs 2.0.15 **PASS=39 / FAIL=0 / SKIP=0**（14:10:45，最终构建直测）；
  旧 distinguish 2.0.14 **PASS=38 / FAIL=0 / SKIP=0**（14:14:09，OTA 旁路测试副本，
  与交付源码逐字一致仅 app_version+调试日志两处测试偏差）；两版均零崩溃、事件日志无应用错误，
  E4 数据配置/F2 画框保存均"保存成功"。（09:24:56 的 39/0/0 为上一会话旧构建轮；
  "35/5/5"为更早中间轮快照，C3"失败"实为迟到弹窗。）
- **未实测项（不声称覆盖）**：修改密码窗与重置专属密码（真机 fulltest E6/G1-G4 覆盖过校验分支）、
  方位刷新×4 逐点、校验视图点牌/右栏 42 改牌钮、右键删除菜单（需人工点验）、
  OTA 下载流（需服务器推送）、本地 AI 识别路径（Myclassification.dll 不在包内）

## 当前服务器兼容性适配 + 崩溃修复 + 静音（2026-11-12 下午，用户批准/要求）

真机实测（XCS232@8.137.196.28）发现当前线上服务器已演进，与原始 2.0.14/2.0.15 合约不同：
`config/set_data_config` 强制要求 `upload_img_clarity`、`preview/set_params_v2` 强制要求
`bate/img_add_num/gk_min/gk_max/upload_img_clarity/count_type`，缺键即返回 serde 文本
`Json deserialize error: missing field ...`（非 JSON），触发原版脆弱错误处理崩溃。
逐包证据 `.tools/smoke-ui/http-debug-final.log`：补键（值 0）后两端点均 `success:true`。
这恰好确认了审计 A 轮自身留下的 UNVERIFIED 假设（"疑似对齐某个未提供的更新版服务端协议，
bate 拼写错误为显著指纹"）。经用户批准"适配当前服务器"，两版交付工程做了 3 类**有意偏差**
（均在源码标注，原始样本 exe 与参考反编译未改）：

1. **兼容性字段（仅出站 JSON 键，常量 0）**：`PreviewSettingViewModel.cs:451` saveParams +6 键、
   `OnlineConfigViewModel.cs:281` saveDatgaConfig +upload_img_clarity。注释"兼容性适配（非原始 exe 行为）"。
   Device.cs/DataConfig.cs 模型属性**保持原版**（A3/A4 不回退）——适配只改出站报文，不扩模型。
2. **2 处崩溃护栏（健壮性）**：`HomeWindow.xaml.cs:32` 关机判空（消除 NullReferenceException）；
   `HttpReq.cs` post/get 非 JSON 护栏（try/catch JsonException + 缺 success 键兜底，消除 JsonReaderException 进程退出）。
3. **静音（用户要求）**：ErrorWindow `SystemSounds.Beep` + HomeViewModel 3 处 `SoundPlayer.Play()`
   注释禁用，标注"提示音已按需求禁用"。

**fixture 一致性**：本地 fixture 建模原版协议（仅校验必需子集、容忍额外兼容键），故适配客户端对
本地 fixture 与当前线上服务器**均通过**；selftest 复跑 **82/82**。fixture server.py/README 注释已同步修订。
**构建**：两版 Release 均 0 Error / 4 Warning（SYSLIB0014 原版真实告警）。
**最终全按键真机**：新 39/0/0、旧 38/0/0，零崩溃（详见上节与 `docs/功能验证报告.md`）。

## 游戏端状态（2026-11-12 第二批更新）

jar 提取 + CFR 反编译 + 可编译工程（javac 0 Error、84 类、资源逐字节一致）✅
**语义级还原**：`docs/逆向⑤_游戏端语义还原.md`（1382 行：MainController 19 段功能地图、
登录逐行、SystemConfig 145 字段表、serve/ai 算法、协议合约）✅
**运行时验收**：`game-fixture/`（sconfig 真类序列化生成器 tools/MakeSconfig.java +
HTTP 6 端点 127.0.0.3:8081 + **UDP 7901 MobileIMSDK 服务端完整实现**）→
游戏端.exe 真机两轮验收**全 PASS**（启动/HTTP 登录/主界面渲染/IM 登录+ACK+心跳 3s/
拒绝分支兜底轮询）✅ 证据 `game-fixture/evidence/`。
**type=2 业务帧→全渲染链（2026-11-13）**：UDP type=2(typeu=1) 推帧→客户端 ACK→
jiance()→yiZhuo/get→**真实 136 张（4×34）牌面渲染 PASS**+不推帧空桌对照 PASS；
jiance 仅计数不重绘（重绘需人工"获取"）——逆向④/UI设计② 已加勘误；
MjConfig 42 条牌编码修正。证据 `game-fixture/evidence/runC_*/runD_*`。
剩余如实：真实牌桌一局全流程、Myclassification.dll、OTA 分支、右键菜单人工点验、
choseIp 真实样本比对；A 级清单见 `docs/audit/C_文档诚实性审计.md` + `docs/audit/审计修复台账.md`。