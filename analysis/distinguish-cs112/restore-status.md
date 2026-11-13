# distinguish-cs112 还原状态（鑫财神图传 PC distinguish.exe 2.0.14）

**BUILD PASS：0 Error / 4 Warning**（`dotnet build distinguish.csproj -c Release -p:EnableWindowsTargeting=true`，2026-11-11，net8.0-windows/x64，连续两次全量清空 bin/obj 后重建结果一致）

产物：`bin/Release/net8.0-windows/distinguish.exe`（+ `distinguish.dll`、`distinguish.runtimeconfig.json`、依赖 DLL、`res/*.wav`、`appsettings.ini`）
警告明细：4 条 = 2 处源码位置 × (主编译 + XAML 临时工程编译) 重复计数，均为
`HomeViewModel.cs(2499,27)` 与 `HomeViewModel.cs(3166,6)` 的 `SYSLIB0014`（原始 2.0.14 代码本就使用
`WebClient`，属真实告警，未压制）。

## 2026-11-12 下午更新：兼容性适配 + 崩溃修复 + 静音 + 真机全按键验证（取代下方"零修改/未运行"旧表述）

> 下文"本轮修复清单"第 3 条"源码本轮零修改"与"已知边界""未运行产物 exe"为**审计 A 轮当时**的状态，
> 已被本节取代：后续经用户批准/要求做了 3 类有意偏差，并完成真机全按键验证。原始样本 exe 与参考反编译未改。

真机实测（XCS232@8.137.196.28）确认当前线上服务器强制要求 `set_data_config` 的 `upload_img_clarity`
与 `set_params_v2` 的 `bate/img_add_num/gk_min/gk_max/upload_img_clarity/count_type`（缺键返回 serde
非 JSON 文本并崩溃）。经用户批准"适配当前服务器"，本工程做了：
1. **兼容性字段（仅出站 JSON 键，常量 0）**：`PreviewSettingViewModel.cs:451` saveParams +6 键、
   `OnlineConfigViewModel.cs:281` saveDatgaConfig +upload_img_clarity；Device.cs/DataConfig.cs 模型保持原版。
2. **2 处崩溃护栏**：`HomeWindow.xaml.cs:32` 关机判空（NRE）；`HttpReq.cs` post/get 非 JSON 护栏（JsonReaderException）。
3. **静音（用户要求）**：ErrorWindow `SystemSounds.Beep` + HomeViewModel 3 处 `SoundPlayer.Play()` 注释禁用。

**真机全按键结果**：旧 distinguish 2.0.14 **PASS=38 / FAIL=0 / SKIP=0**（14:14:09，OTA 旁路测试副本
`.tools/smoke-ui/old-test-src`，与本工程源码逐字一致，仅 `Global.app_version="2.0.15"` 旁路 +
HttpReq 调试日志两处测试偏差；本工程 `app_version` 仍忠实为 `"2.0.14"`）。零崩溃、事件日志无应用错误，
E4 数据配置/F2 画框保存均"保存成功"。本地 fixture selftest 82/82（fixture 容忍额外兼容键）。
详见根 `restore-status.md` 与 `docs/audit/审计修复台账.md` A 轮后续、`docs/功能验证报告.md`。

## csproj 修正点（对齐已验证的 `../xcs-cs112/xcs.csproj` 模板）
- `TargetFramework`：`net47` → `net8.0-windows`
- `Prefer32Bit=True` → `PlatformTarget=x64`
- 新增 `<PackageReference Include="System.Drawing.Common" Version="8.0.10" />`（HomeViewModel 使用 System.Drawing）
- 移除 net47 框架引用 `<Reference Include="System.Net.Http" />`、`<Reference Include="System.Core" />`
- 新增 `<NoWarn>$(NoWarn);CA1416</NoWarn>`（仅此一条平台兼容噪声，未压制真实警告）
- 保留原有：全部依赖 DLL 的 `EmbeddedResource LogicalName`（供 `AssemblyResolve` 从内嵌资源加载）、
  9 个窗口 `Page` 条目及 `MainWindow.xaml→loginwindow.xaml` 等逻辑名映射（匹配原 `StartupUri="LoginWindow.xaml"`）、
  res/ 图片 `Resource LogicalName`、wav/ini 的 Content 拷贝、`app.ico`、`RootNamespace` 为空

## 忠实性核对（对照原始反编译 `../distinguish-cs/`）
- `distinguish.Common/Global.cs`：`app_version="2.0.14"`、`app_name="xcs"`（以原始反编译为准，与 xcs 同名但版本号不同）；唯一偏差 `iniConfigPath` 由 `"./appsettings.ini"` 改为 `BaseDirectory+`，与已验证的 2.0.15 还原工程一致（相对工作目录 → 绝对目录的还原加固），已在两工程同步保留
- `distinguish/App.xaml.cs`：无 2.0.15 曾出现的 `DoStartup` 死代码缺口；含 `protected override void OnStartup`（base 调用 + `AssemblyResolve += OnResolveAssembly`），与原始 `App.cs` 逐行等价；原 `InitializeComponent`/`Main` 由 SDK 工程 ApplicationDefinition 生成替代
- `Properties/AssemblyInfo.cs`：与原始一致（版本 2.0.1.0、ThemeInfo 已解码、3 条 AssemblyAssociatedContentFile）
- 全部 45 个 .cs 与原始逐一 diff（忽略 CR/LF）：仅反编译噪声级差异（变量命名 `obj`→`httpClient`、auto-property 展开、`IL_*` 注释措辞、lambda/delegate 等价变换、`Image`→`System.Drawing.Image` 限定），无逻辑改动
- 2.0.15 专有成员 `DeleteOffset`/`ShiftForward`/`GetLogicalSlot`/`initText` 已确认**不存在**于本工程（对照 `xcs-cs/` 原始基线核实）；`RemoveItem` 经核对 2.0.14 原始基线本就存在（`distinguish.ViewModel/HomeViewModel.cs:3124`），非 2.0.15 串入
- 9 个窗口 XAML 未引用 2.0.15 独有图标（`cq_favicon.ico`/`cs.ico`），csproj 资源清单与 2.0.14 目录实际文件一致

## 本轮修复清单
1. csproj 按上述 5 点修正（此前残留 net47/Prefer32Bit 旧配置，从未在 net8 下构建过）
2. 清空历史 bin/obj（net47 时代残留，状态未知），两次全量重建均 0 Error
3. 源码本轮**零修改**——还原工程源码本身已忠实且可直接编译，本轮全部工作在 csproj 与验证

## 与 2.0.15 工程（../xcs-cs112）的差异点
- 业务代码按 2.0.14 原始基线还原：无 DeleteOffset/ShiftForward/GetLogicalSlot/initText，App/Global 细节与 2.0.14 原始反编译一致（如 HomeViewModel 3221 行 vs 2.0.15 约 3400+ 行）
- 图标资源集不同：本工程 000/favicon/jyz_favicon/xincaisheng_favicon.ico，无 cq_favicon.ico、cs.ico；csproj `Resource` 清单相应缩减
- 程序集名 `distinguish`（对方 `xcs`）；其余工程结构（文件夹布局、依赖 DLL 集、XAML 逻辑名映射）同源

## 已知边界
- 原版运行行为（WPF 渲染、硬件交互）需 Windows 环境人工验收；本工程为源码级还原 + 编译通过，未运行产物 exe
- `obj/` 内 `distinguish_pgkhjooz_wpftmp.csproj` 为 WPF 构建临时工程，属正常构建产物
