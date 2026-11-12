# XCS 服务端与 PC 端逆向还原仓库

鑫财神（com.re.xcs）体系的 PC 图传端、游戏端的逆向还原工程。
配套：安卓端逆向重建见 xcs-android-reconstruction 仓库；实机协议验证已完成。

## 内容结构
- `originals 说明`：三个原始包已在本目录根（游戏端.zip / 鑫财神图传PC.zip / xcs电脑图传(新).zip）
- `xcs电脑图传/`、`鑫财神图传PC/`、`游戏端/`：解压内容（游戏端自带 jre-9 未入库，见 .gitignore）
- `analysis/xcs-cs112/`：**xcs.exe 2.0.15 完整反编译源码 + 9 窗口 XAML + 可编译 WPF 工程（BUILD PASS）**
- `analysis/distinguish-cs112/`：2.0.14 旧版同构（9 XAML）
- `analysis/baml-strings/`：14 个 BAML 的有序字符串证据
- `逆向分析报告.md` + `逆向①/②/③*.md`：逐包逆向文档
- `docs/后续逆向路线图.md`：**未完成清单 + Windows 11 上的下一步起点**

## 构建还原版 PC 客户端
```sh
cd analysis/xcs-cs112
dotnet build xcs.csproj -c Release        # 需 Windows 或 -p:EnableWindowsTargeting=true
# 产物 bin/Release/net47/xcs.exe（原版 net47 WPF x86）
```

## 关键结论
- PC 图传（C# WPF）与安卓（Flutter/Rust）与游戏端（JavaFX/MobileIMSDK）同一服务器协议
- 端点/协议常量/RSA 流程/dataKey 与安卓逆向证据逐项吻合
- 登录 devicetype=5（PC）；check_update type=3（PC）
