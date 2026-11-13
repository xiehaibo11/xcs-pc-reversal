# Windows 逆向环境清单

核查日期：2026-11-11。系统：Windows 11 x64，build 26300。主工具目录 `F:\逆向工具`，开发依赖在 `F:\Dev`。

本次检查了 PATH、卸载注册表、共享工具目录、开发 SDK 目录、用户应用安装目录，并运行 CLI 探针。`Verified` 表示命令及预期输出通过，`Present` 只表示 GUI/启动文件存在，未承诺交互调试已经验收。

## 1. 本次补齐和修复

| 项目 | 核查前 | 处理后 |
|---|---|---|
| Npcap | 未安装；Wireshark `dumpcap -D` 报无法加载 `wpcap.dll` | 安装官方 1.90，驱动 Running；能枚举回环及网卡，并完成回环报文抓取验证 |
| .NET 8 Desktop Runtime x64 | 仅有 .NET 10；不能据此保证运行目标为 .NET 8 的框架依赖构建 | 安装 8.0.34；`dotnet --list-runtimes` 可见 `Microsoft.WindowsDesktop.App 8.0.34` |
| WinDbg / CDB | PATH、SDK 和工具目录未发现 | 安装 Windows SDK 的 Debugging Tools 组件 10.0.26100.7705，包含 x86/x64，以及随组件提供的 ARM 调试器 |
| 已有工具入口 | 多个 GUI/CLI 工具只能按完整路径启动 | 新增 17 个 `.cmd` 入口到已经在 PATH 中的 `F:\逆向工具\bin`，并增加 5 个快捷方式 |
| Ghidra 路径兼容性 | 中文安装路径造成 Log4j XML include 解析错误 | 新建英文 Junction `F:\Dev\Tools\Ghidra` 指向原目录，修复三个启动入口；headless 静态 PE 导入通过，无需复制/移动安装目录 |
| 游戏端运行文件 | 样本旁随包 JRE 缺失 | 从隔离副本恢复，132 个文件与原始 ZIP 一致；未启动游戏端 |
| 游戏端查看素材 | `game-src` 图片缺失 | 从原始 JAR 恢复 129 张 PNG/BMP，逐文件字节核对 |

WinDbg 新版 MSIX 的官方 CDN 本次返回 HTTP 403；已按微软文档提供的 Windows SDK 路线装好 **WinDbg classic + CDB**。新版 MSIX 没有被安装，也没有将 classic 冒称为新版。

Npcap 采用标准图形安装向导；没有使用仅 OEM 提供的 `/S`。安装设置为 WinPcap 兼容开启、raw 802.11 关闭，保留回环支持。未启用 Fiddler 系统代理、未安装 HTTPS 解密根证书。

## 2. 已有且保留的工具

| 能力 | 工具与版本 | 位置或命令 | 本次验证 |
|---|---|---|---|
| .NET 反编译/调试 | dnSpyEx 6.6.0 | `F:\逆向工具\dnSpyEx`；`dnspy`、`dnspy-console` | CLI 对真实类型反编译通过；GUI 文件存在 |
| .NET 反编译 | ILSpy GUI / CLI 11.2.0.10050 | GUI 在 `%LOCALAPPDATA%\Programs\ILSpy`；`ilspy`、`ilspycmd` | CLI 对真实类型反编译通过；GUI 文件存在 |
| 原生静态分析 | Ghidra 12.1.4 | 实体仍在 `F:\逆向工具\Ghidra`；从 `F:\Dev\Tools\Ghidra` 英文 Junction 启动；`ghidra`、`ghidra-analyze` | headless 启动、日志加载与系统 PE 静态导入通过；未验收 GUI 交互或完整自动分析 |
| 原生调试 | x64dbg / x32dbg，现有 snapshot 2026-05-27 | `F:\逆向工具\x64dbg\release` | 两种架构文件存在；未附加进程 |
| PE/壳识别 | Detect It Easy | `die`、`diec` | CLI 版本探针通过；安装记录写 3.21，PE 文件版本为 3.20.0.0，以实际探针输出和文件元数据分别记录 |
| 十六进制查看 | ImHex，现有安装记录 1.38.1 | `F:\逆向工具\ImHex`；`imhex` | GUI 文件存在 |
| Java 反编译 | CFR 0.152 | `F:\逆向工具\CFR\cfr.jar`；`cfr`；仓库 `.tools/cfr.jar` 亦存在 | 版本与真实 class 反编译通过 |
| Java GUI 查看 | JD-GUI 1.6.6 | `F:\逆向工具\JD-GUI\jd-gui-windows-1.6.6`；`jd-gui` | 文件存在 |
| Java 工具链 | JDK 21.0.12.1 | `F:\Dev\SDKs\JDK21`；`java`、`javac`、`javap` | Java/javap 版本探针通过；另有 JDK17 |
| .NET 构建 | SDK 10.0.404；Framework 4.8 系统组件 | `dotnet`；Framework Release 534296 | 新版 WPF 工程构建通过 |
| 归档解包 | 7-Zip 26.06 | `F:\Dev\Tools\7zip`；`7z` | 版本/格式列表通过 |
| 抓包 | Wireshark / TShark 4.6.9 | `F:\逆向工具\Wireshark` | 版本、接口枚举、本地回环抓包与读回通过 |
| HTTP 对照 | Fiddler Classic 6.0.20262.10021 | `%LOCALAPPDATA%\Programs\Fiddler`；`fiddler` | 文件存在，保持原有代理设置 |
| 进程/文件/注册表 | Process Monitor 4.12、Process Explorer 17.2 | `F:\逆向工具\Sysinternals`；另有 winget Procmon 4.13 | 文件存在；PATH 中 `procmon` 优先命中 4.13，未重复安装 |
| 转储/网络/启动诊断 | ProcDump 12.02、TCPView 4.19、DebugView 5.02、VMMap 3.4、Autoruns 14.3 | `F:\逆向工具\Sysinternals` | 主要 GUI/EXE 存在；不表示已启动捕获 |
| 字符串/签名/句柄 | Strings、Sigcheck、Handle、ListDLLs | 同上；已有命令入口 | 文件与原有记录可见；不计入本次每项 CLI 探针全部验证的承诺 |
| 安卓反编译 | JADX 1.5.6、APKTool 3.0.3、Androguard 4.1.4 | `jadx`、`jadx-gui`、`apktool`、`androguard` | CLI 版本通过 |
| 安卓连接 | Android Platform Tools 37.0.1 | `F:\Dev\SDKs\Android\platform-tools`；工具根另有副本 | ADB version 通过；未连接设备 |
| 动态插桩 | Frida 17.23.3、Objection 1.12.5 | `F:\逆向工具\PythonEnv`；已有命令入口 | 版本探针通过；未向设备/进程注入 |
| 二进制脚本分析 | capstone/unicorn/keystone/lief/pefile/pyelftools | `python-re`，独立 PythonEnv | 六模块导入通过 |
| Flutter 静态分析 | Blutter 现有源码 | `F:\逆向工具\Blutter` | 目录/安装记录可见；不属于本仓库本轮主链路，未重建或功能验收 |

## 3. 新增的命令与快捷方式

新增入口：`7z`、`dnspy`、`ilspy`、`x64dbg`、`x32dbg`、`die`、`jd-gui`、`jadx-gui`、`imhex`、`dumpcap`、`fiddler`、`windbg`、`windbg-x86`、`cdb`、`cdb-x86`、`ghidra`、`ghidra-analyze`。

Ghidra 两个命令入口及原有 `启动Ghidra.cmd` 均从英文 Junction 启动，单独设置 `JAVA_HOME=F:\Dev\SDKs\JDK21`，不改变系统全局 Java 配置。启动文件修改前的备份在 `F:\逆向工具\pc-project-backups-20261111`。

Ghidra 测试工程位于 `F:\Dev\Cache\XcsGhidraSmoke`。工程路径不可包含 `.tools` 这类以点开头的目录段，本次已实测该限制。

`F:\逆向工具\快捷方式` 新增 ILSpy、Fiddler、JD-GUI、WinDbg x64、WinDbg x86 五个入口，保留原有快捷方式。

```powershell
# 在项目根目录复查环境；默认只运行静态/版本探针，不启动原始应用或抓包
pwsh -File .\scripts\Test-ReverseEnvironment.ps1

# 常用入口
ilspy .\PC端\原始包解压\xcs电脑图传\xcs.exe
dnspy .\xcs电脑图传\xcs.exe
jd-gui .\game-jars\QueshengDaPai.jar
dumpcap -D
cdb -version

# Wireshark 显示过滤器（PC HTTP / PC WS / 游戏端 UDP）
# tcp.port == 8081 || tcp.port == 18889 || udp.port == 7901
```

## 4. 验证结果和文件

环境脚本本次共检查 38 项：**21 项 Verified、17 项 Present、0 项 Missing、0 项 Failed**。GUI 的断点调试、窗口渲染以及 Fiddler 代理抓包没有冒充已验收。

共享工具目录的 `F:\逆向工具\pc-project-supplement-20261111.json` 另存本轮安装、启动入口、Ghidra 修复及完整验证记录。

- `.tools/reverse-environment.json`：每项绝对路径、探针输出、退出码、文件版本。
- `.tools/added-launchers.json`：本次 17 个入口与目标路径。
- `.tools/smoke/results.json`：真实类型/class 反编译与本机回环抓包的四项结果，全部通过。
- `.tools/smoke/loopback.pcapng`：仅本机 UDP 49001 的测试流量，发送内容为 `XCS_REVERSE_ENV_CHECK`。
- `.tools/ghidra-ascii-import.log`、`ghidra-ascii-result.json`：英文路径修复后静态导入系统 `where.exe` 成功，未执行该程序，未启用自动分析。
- `.tools/install-desktopruntime8.log`、`install-sdk-debuggers.log`、`install-windbg.log`：安装成功与新版 CDN 失败记录。

`ExecutableFileVersion` 是被执行探针的 EXE 元数据；例如 CFR 的探针 EXE 是 java.exe，因此该字段不是 CFR JAR 的版本。JAR 实际版本见 `Detail` 的工具输出。

## 5. 未安装的可选替代项

本次检查范围内未发现 IDA Pro/Free、Binary Ninja、radare2/rizin/Cutter、dotPeek。现有 Ghidra、x64dbg、WinDbg、ILSpy、dnSpyEx 已覆盖本项目的原生与 .NET 主链路；这些替代项不构成当前环境缺口，因此未追加安装。没有下载破解包或创建商业软件授权。

JavaFX 源码的可编译重建、AI 权重样本、账号/设备与 fixture 是后续工程/证据工作，不属于继续安装逆向工具即可解决的缺项。

## 6. 下载来源与安装校验

Npcap 下载自 [官方 Npcap 下载页](https://npcap.com/)，版本 1.90；安装器 Authenticode 为 Valid，签发主体 Nmap Software LLC。其 SHA-256 为 `682b57e1c05ef375931975c9d6ded40888f4bfdf9f4b11571e654f962f1199b6`。图形/静默安装方式参见 [Npcap 官方指南](https://npcap.com/guide/npcap-users-guide.html)。

.NET Desktop Runtime 使用 `winget` 的 `Microsoft.DotNet.DesktopRuntime.8` 官方微软下载源，安装器哈希检查通过。

WinDbg classic/CDB 使用 [微软文档说明的 SDK 调试工具安装路线](https://learn.microsoft.com/en-us/windows-hardware/drivers/debugger/debugger-download-tools)，仅补装 `OptionId.WindowsDesktopDebuggers`。SDK 安装器 Authenticode 为 Valid、Microsoft Corporation；SHA-256 为 `6fa0fa27db77a909f5ecb35183cb26a969a6775936780936fe239e4f9c66b458`，与 winget 清单一致，安装退出码为 0，无重启。
