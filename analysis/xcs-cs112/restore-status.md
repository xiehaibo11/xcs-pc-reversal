# PC 端 UI 还原状态 — 完成 ✅

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
