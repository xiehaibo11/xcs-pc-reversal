# XCS 服务端与 PC 端逆向还原仓库

鑫财神（com.re.xcs）体系的 PC 图传端、游戏端的逆向还原工程。
配套：安卓端逆向重建见 xcs-android-reconstruction 仓库；实机协议验证已完成。

## 内容结构
- `PC端/`：PC 图传全部产物（原始包/解压/反编译/工程/测试副本/fixture，见 `PC端/README.md`）
- `游戏端.zip`、`游戏端/`：游戏端原始包与解压（自带 jre-9 未入库，见 .gitignore）
- `game-project/`：游戏端可编译工程；`game-fixture/`：游戏端 HTTP+UDP fixture 与真机验收证据（evidence 不入库）
- `tools/`：MakeSconfig.java（sconfig 生成器）；`scripts/`：环境检查脚本；`docs/audit/`：四轮对抗审计报告与修复台账
- `analysis/xcs-cs112/`：xcs.exe 2.0.15 还原工程；当前目标 **net8.0-windows / x64**，9 个 XAML（App + 8 个窗口）。2026-11-11 本机构建通过（0 错误 / 4 警告）
- `analysis/distinguish-cs112/`：2.0.14 旧版反编译与 XAML 对照工程；当前仍有 ILSpy `Ctor` 残留，**本机构建未通过**
- `analysis/xcs-cs/baml-strings/`：9 个窗口 BAML 的有序字符串证据（忠实性对照基准）
- `逆向分析报告.md` + `逆向①/②/③*.md`：逐包逆向文档
- `docs/后续逆向路线图.md`：**未完成清单 + Windows 11 上的下一步起点**
- `docs/项目理解与文档核查.md`：本机源码、构建、资源与历史文档的核对结果
- `docs/Windows逆向环境清单.md`：工具位置、已补齐组件、验证范围与常用命令
- `scripts/Test-ReverseEnvironment.ps1`：可重复运行的环境检查（不执行原始应用）

## 构建还原版 PC 客户端
```sh
cd analysis/xcs-cs112
dotnet build xcs.csproj -c Release
# 当前产物 bin/Release/net8.0-windows/xcs.exe + xcs.dll
# 运行此框架依赖构建需要 .NET 8 Windows Desktop Runtime x64。
# 原始两个样本是 .NET Framework 4.7；不要与还原工程的 .NET 8 目标混淆。
```

## 关键结论
- PC 图传（C# WPF）、安卓（Flutter/Rust）与游戏端（JavaFX/MobileIMSDK）属于同一业务体系；传输和登录合约存在端别差异
- PC 使用 HTTP 8081 + WebSocket 18889；游戏端使用 HTTP 8081 + MobileIMSDK UDP 7901，不能直接共用 WebSocket fixture
- 端点/协议常量/RSA 流程/dataKey 与安卓逆向证据逐项吻合
- 登录 devicetype=5（PC）；check_update type=3（PC）

## 本机环境检查

```powershell
pwsh -File .\scripts\Test-ReverseEnvironment.ps1
# 详细结果：.tools/reverse-environment.json（已忽略，不入库）
```

根目录与 `docs/` 中的首轮逆向报告保留了历史快照；当前结论优先看上述核查报告、
`docs/逆向④_游戏端源码还原.md` 与 `restore-status.md` 的最新复核说明。
