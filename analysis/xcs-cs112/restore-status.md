# PC 端 UI 还原状态（历史副本）

> **2026-11-11 复核**：当前本工程目标为 net8.0-windows/x64，Windows 本机构建 0 错误/0 警告（第二轮，含运行时冒烟通过）；下文 net47 和”jar 未解出”为历史记录。当前状态见 [根目录状态](../../restore-status.md) 和 [项目核查](../../docs/项目理解与文档核查.md)。

## 2026-11-12 下午当前状态（取代上方”0 警告”旧表述）

**构建**：net8.0-windows/x64，Release **0 Error / 4 Warning**（SYSLIB0014 WebClient 过时 API，原版真实告警，如实保留）。
产物 `bin/Release/net8.0-windows/xcs.exe`（还原版 PC 客户端 2.0.15）。

经用户批准/要求做了 3 类**有意偏差**（均在源码标注，原始样本 exe 与参考反编译未改）：
1. **当前服务器兼容性字段（仅出站 JSON 键，常量 0）**：真机实测确认线上服务器强制要求
   `set_data_config` 的 `upload_img_clarity` 与 `set_params_v2` 的 `bate/img_add_num/gk_min/gk_max/upload_img_clarity/count_type`
   （缺键返回 serde 非 JSON 文本并崩溃）。`PreviewSettingViewModel.cs:451` saveParams +6 键、
   `OnlineConfigViewModel.cs:281` saveDatgaConfig +upload_img_clarity；Device.cs/DataConfig.cs 模型保持原版。
2. **2 处崩溃护栏**：`HomeWindow.xaml.cs:32` 关机判空（NRE）；`HttpReq.cs` post/get 非 JSON 护栏（JsonReaderException）。
3. **静音（用户要求）**：ErrorWindow `SystemSounds.Beep` + HomeViewModel 3 处 `SoundPlayer.Play()` 注释禁用。

**真机全按键结果**：新 xcs 2.0.15 **PASS=39 / FAIL=0 / SKIP=0**（14:10:45，最终交付构建直测
XCS232@8.137.196.28）。零崩溃、事件日志无应用错误，E4 数据配置/F2 画框保存均”保存成功”。
本地 fixture selftest 82/82（fixture 容忍额外兼容键）。详见根 `restore-status.md`、
`docs/audit/审计修复台账.md` A 轮后续、`docs/功能验证报告.md`。

**BUILD PASS：0 Error / 18 Warning**（dotnet build -p:EnableWindowsTargeting=true, Release）
产物：bin/Release/net47/xcs.exe（还原版 PC 客户端 2.0.15）

## 还原内容
- 9 个窗口 XAML 完整解码（ilspycmd 11.2 + DOTNET_ROLL_FORWARD=LatestMajor）
- 45 个 .cs 源码 + 资源 + csproj 组装为可编译工程
- ILSpy 伪标签机械修复：Ctor→Binding Path、Style/ControlTemplate TargetType、
  NullExtension、x:Key、RelativeSource、重复字段/InitializeComponent/Connect 清理、
  ThemeInfo/Image 限定/OnStartup 虚调用等

## 已知残余（诚实边界）
- 游戏端 exe4j 载荷（8.97MB）为专有加密格式，暴力 inflate 扫描无果——jar 未解出
- 原版运行行为（WPF 渲染）需 Windows 环境验收；本工程为源码级还原+交叉编译通过
