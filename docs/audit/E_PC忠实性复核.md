# E 轮复核：A 轮修复声明对抗性第二轮复核（PC 端忠实性）

- 复核日期：2026-11-13
- 复核性质：**对抗性独立复核**（不信任修复方自述，全部结论基于本次重新取证）
- 复核范围：A 轮 10 处偏差修复声明 + "已全部修复/副本已刷新/0E4W" 等配套声明
- 对象：`analysis/xcs-cs112`（2.0.15 还原）、`analysis/distinguish-cs112`（2.0.14 还原）
- 原版基准：`PC端/原始包解压/xcs电脑图传/xcs.exe`（2.0.15）、`PC端/原始包解压/鑫财神图传PC/鑫财神/distinguish.exe`（2.0.14）、
  反编译参考层 `analysis/xcs-cs`（含 `baml-strings/`）、`analysis/distinguish-cs`（含逐窗 baml）
- 方法：全文 diff（含行尾/编码检查）、exe/BAML 字节级解码（长度前缀字符串编码，交叉验证）、
  原始 zip 清单对照、csproj 条目与 g.resources 条目集比对、`dotnet build -c Release -p:EnableWindowsTargeting=true --no-incremental` 复测、
  SHA-256 对比、docs 全量 grep。全程未运行 GUI 程序、未联网；构建产物 bin/obj 复核后已删除（工作树无未暂存改动，`git diff` 0 行）。

---

## 结论行

**复核：1 项证实 / 1 项证伪 / 6 项部分成立 / 8 项新发现**

一句话总评：A 轮修复方向正确、大部分证据链成立；但 **D7 修复中 4 个 ConverterParameter 值全部写反**
（原版 BAML 实际可解码且值相反，推翻"推断值无原版依据"的口径），且 **测试副本未随兼容性适配重新构建**
（"修复后已刷新副本""与交付源码逐字一致"不成立）。

---

## 逐项复核表

| # | 复核项 | 裁定 | 关键证据（本次独立取证） |
|---|---|---|---|
| 1 | 4 个回退文件逐字一致性 | **部分成立** | `Device.cs`/`DataConfig.cs` 两对（xcs-cs112↔xcs-cs、distinguish-cs112↔distinguish-cs）除 //IL_ 注释外**逐字节一致** ✅。`PreviewSettingViewModel.cs` 两对在 451-457 行、`OnlineConfigViewModel.cs` 两对在 281-282 行仍含 6+1 个"兼容性适配"出站键（bate/img_add_num/gk_min/gk_max/upload_img_clarity/count_type）——与 restore-status.md:22-24/87-89 记录的"用户批准后续修订"一致、注释明确标注"非原始 exe 行为"，**非夹带**；但台账 A1 行（审计修复台账.md:10）"剔除后逐字一致 ✅"对当前树**不再成立**（历史裁定记录与现状脱节，需读者读到 A 轮后续节才不误解）。另：IL 注释内 `Expected Obj`（112 版）vs `Expected O`（参考层）3 处差异（PreviewSettingViewModel.cs:360/413/428），属 //IL_ 注释豁免范围但注明。 |
| 2 | OnlineWindows converter 绑定 | **部分成立**（a 证实、b 证实、**c 证伪**） | a) `genderConverterKey` 声明：两版 OnlineWindows.xaml:5 均有 ✅。b) **2.0.14 原版依据：证实**——直接解码 distinguish.exe 内嵌 `onlinewindows.baml`（该 BAML 字节流在 exe 中逐字节命中 1 次，即权威原件）：字符串顺序 `distinguish.RadioToBoolConverter`(0x3EA) → `genderConverterKey`(0x410) → `IsChecked`+`DataConfig.Data_send`+`Converter`+`ConverterParameter`(0x7D5-0x82E) → …→ `DataConfig.Count_type`×2；exe 二进制计数 RadioToBoolConverter UTF8=2 / genderConverterKey=1 / ConverterParameter=10+2(UTF16)，与 2.0.15 xcs.exe 完全相同 → **加 converter 绑定有 2.0.14 原版铁证** ✅。c) **ConverterParameter 值：证伪**——原版 BAML 用 1 字节长度前缀存字符串（旁证：`0x0C`+12字节=自动发送、`0x0F`+15字节=计数方式：），4 个绑定参数值均为 1 字符串 `0x01 '1'`/`0x01 '0'`：**自动发送="1"、手动发送="0"、光控计数="1"、机头计数="0"**（2.0.14 与 2.0.15 两 exe 字节相同）；重建版 4 处全部写反（见新发现 N1）。机械自洽性（ConvertBack 返回字符串→int 属性）本身成立：Convert 为 `int.ToString().Equals(parameter)`；ConvertBack 返回参数字符串，WPF BindingExpression 会用目标源属性类型转换器（Int32Converter）完成 string→int，运行时无异常——**问题在值的方向，不在机制**。 |
| 3 | HomeWindow IsAsync="True" | **证实** | a) 计数：两版各恰 1 处（distinguish-cs112/HomeWindow.xaml:353、xcs-cs112/HomeWindow.xaml:388）✅；原版 `homewindow.baml` 两版各 1 处 ✅。b) 落点：baml-strings/homewindow.strings.txt:148-152 顺序 `ResultImg+ → IsAsync → DataContext.ChannelMjHeight+ → FindAncestor+ → WindowL`；BAML 字符串按文档首次使用序定义，IsAsync 首次使用紧邻"ChannelMjHeight+FindAncestor(Window)"绑定块 → 归属**第一个 ChannelMjHeight 绑定**，与重建落点一致；本次用重建产物 dll 内 homewindow.baml 复核：`DataContext.ChannelMjHeight → IsAsync → True → … Window` 三元组相邻，与原版语义相同 ✅（细节：原版 XAML 属性序为 IsAsync 在 Path 前，重建为 Path 后——XAML 属性序无语义，仅记录）。原版 16 处 ChannelMjHeight 绑定（baml 计数 16），重建 16 处、IsAsync 挂第一处，与推断一致。c) 2.0.14 distinguish.exe `IsAsync` UTF8=1（本次扫描复核）✅。 |
| 4 | 夹带文件清查 | **部分成立** | 原始 zip 实际仅含 exe+3 wav（xcs电脑图传(新).zip 4 文件、鑫财神图传PC.zip 6 条目）。两工程根逐文件分类：① 预期重建件：csproj、AssemblyInfo.cs、restore-status.md、全部 .cs/.xaml ✅。② **dll 22 个：均有原版来源**——抽验 9 个（Newtonsoft/BouncyCastle/websocket-sharp/GalaSoft.MvvmLight.Platform/Interactivity/CommonServiceLocator/Ini/Memory/ValueTuple）的正文 2KB 块在原 exe 内嵌载荷中字节命中（如 Newtonsoft 块 @exe 0x85CE64）→ 原版为"单文件打包"分发，提取合法 ✅。③ 图标：favicon/jyz_favicon/xincaisheng_favicon/000.ico（两版）+ cq_favicon/cs.ico（仅 xcs 侧）均为原 exe .g.resources 资源名（UTF-16 命中；cq/cs 仅 2.0.15 命中，2.0.14 为 0 —— 版本差异忠实还原）✅；**app.ico 例外：exe 内无此资源名（utf16=0），疑为 PE 图标的提取自命名 → 需来源注明（N8）**。④ res/** 图片：login_bg/bottom_bg/sucai/* 等资源名 exe 内 UTF-16 命中 ✅。⑤ **appsettings.ini：两工程根与 csproj 均 0 命中**，A 轮清夹带证实 ✅（测试副本下的 appsettings.ini 为外部 INI 夹具，PC端/README 已注明，属测试设施）。⑥ **应删未删：`analysis/xcs-cs112/build-errors.txt`**（4 行 MC3061/MC4102/MC4108 旧编译错误日志，非原包非工程件，随首次提交带入）→ N3。 |
| 5 | csproj 差异清单 | **部分成立** | ① 两 csproj 归一化 diff 仅 3 处：**NoWarn CA1416 只在 distinguish.csproj:9，xcs.csproj 无**（两版不一致；且本次构建两版均无该类告警 → 属可移除的脚手架差异，N4）；xcs.csproj:85-86/192-193 的 cq_favicon/cs.ico 映射与 2.0.15 原版资源集对应（distinguish 侧无）→ 正确。② System.Drawing.Common 8.0.10：原版代码真实使用 System.Drawing（HomeViewModel.cs:5-6/2661-2668 Image.Save/RotateFlip，net8 需包）→ 有依据 ✅。③ OutputType=WinExe/UseWPF/net8.0-windows/x64：与 AGENTS.md 口径一致 ✅。④ **LogicalName 映射完整性：证实**——重建 dll 的 g.resources 名称集 vs 原 exe：xcs 侧 57/60、distinguish 侧 56/59，**0 多余、0 错误命名**；唯一缺口是 3 个 wav（原版为内嵌 Resource，重建改 `<Content CopyToOutputDirectory>` 外置，见 N7；因提示音已禁用，运行时无影响）。 |
| 6 | 构建与产物一致性 | **证伪**（构建计数部分证实） | ① 构建：`--no-incremental` 强制重建两版 → **0 Error / 4 Warning**（SYSLIB0014 WebClient ×2 源 ×2 编译遍：HomeViewModel.cs 2691/3400（xcs）、2499/3166（distinguish）），与声明"各 0E/4W"一致 ✅（restore-status.md:8-9 如实）。② **SHA-256：证伪**——本次构建 `xcs.dll`=`ec847b21…`、`distinguish.dll`=`1bd35690…`；副本 `PC端/测试副本/xcs-2015/xcs.dll`=`6cc0faa4…`、`dist-2014/distinguish.dll`=`4ee6eb4c…`，**两对均不一致**。语义定位：副本 dll 中 `bate`/`img_add_num`/`upload_img_clarity` 的 UTF-16 字面量 **0 命中**（本次构建均 1 命中），而副本与重建版的 IsAsync/converter/资源名计数完全相同、文件大小相同 → 副本=**兼容性适配重加之前的构建**，"修复后已刷新副本"（任务声明）与功能验证报告.md:17"与交付源码逐字一致仅两处测试偏差"、restore-status.md:68-69 同口径 **不成立**（N2）。③ bin/obj 复核后已删除，工作树 `git diff` 0 行 ✅。 |
| 7 | 文档残留 | **部分成立** | ① 根 restore-status.md 已重写 ✅：0E/4W 如实（:8-9/97）、兼容键叙事正确（:18/76-98）、未实测项如实（:72-74）、提示音禁用已声明（:92-93）、无"0 警告/全按键通过"绝对化残留。② bate/img_add_num：现存提及均为修正后口径（兼容适配），"bate 拼写是客户端原样"旧表述仅在审计档案（台账:83 为修正记录）→ 无害残留 0 ✅。③ 残留点：`docs/功能验证报告.md:14`"真机**全按键**最终结果"仍用全称（C 轮 S7 已裁定"全按键"不成立：右键删除菜单等从未确定性触发；本页 :18-19 虽声称副本"与交付源码逐字一致"——被本次 N2 直接推翻）；`docs/audit/D_验证记录核查.md:86`"两项目均 0 警告 0 错误"与可复现的 4 警告不符（疑为增量构建跳过编译的假 0），:90-97"副本与当前源码确定性构建逐字节一致=证实"在兼容键重加后**已失效未复检**（N6）。 |
| 8 | AGENTS.md 一致性 | **部分成立** | ① 已更新：AGENTS.md:8 记录 fixture_server 移入 `PC端/fixture_server`（moved 2026-11-12），:19-20 命令用新路径 ✅；`dotnet build analysis/*112/…` 可执行（本次实测）✅；pwsh 7.6.10 可用 ✅。② 残留：AGENTS.md:6 "original ZIPs, and extracted applications: reference evidence" 未注明现址（实已移至 `PC端/原始包(解压)/`，根目录旧位置已不存在，实测 `ls xcs电脑图传` 失败）；AGENTS.md:32 "Extend `fixture_server/selftest.py`" 仍为旧相对路径；`scripts/Test-ReverseEnvironment.ps1:104` 探针引用 `<root>\xcs电脑图传\xcs.exe` —— 该路径已迁移，探针将失效（N 系并入第 8 项结论）。 |

---

## 新发现清单（按严重度）

### N1【高·行为偏差】4 个 ConverterParameter 值全部与原版相反
- 位置：`analysis/xcs-cs112/distinguish/OnlineWindows.xaml:88,92,105,109`；`analysis/distinguish-cs112/distinguish/OnlineWindows.xaml:88,92,105,109`
- 原版（两 exe 内嵌 BAML 字节一致）：`自动发送=ConverterParameter"1"`、`手动发送="0"`、`光控计数="1"`、`机头计数="0"`；重建版恰相反（"0"/"1"/"0"/"1"）。
- 影响：单选框初始回显方向颠倒（如服务器下发 Data_send=1，原版亮"自动发送"、重建版亮"手动发送"）；用户点选后 ConvertBack 写回值方向颠倒，与原版写出的 config/set_data_config 值语义相反。
- 解码方法（可复核）：BAML 属性值字符串为 1 字节长度前缀+UTF-8（旁证 `0x0C`+`自动发送`、`0x0F`+`计数方式：`），四绑定处字节 `\x07\x08\x00\x01 '1'/'0'`（详见上文项2c）。2.0.14 `onlinewindows.baml` 偏移：自动发送@0x829(`\x01'1'`)、手动发送@0x8E0(`\x01'0'`)、光控@0x97E(`\x01'1'`)、机头@0xA32(`\x01'0'`)。
- 建议：4 处参数改为 1/0/1/0；同步修订 restore-status.md:28 "BAML 优化存储未留字符串"与 A 轮报告 D7/S2 的"推断值"表述——**值在 BAML 中可解码，且已解码出与推断相反的结果**；修复后重建+刷新测试副本。

### N2【中·交付一致性】测试副本未随兼容性适配重建，"已刷新/逐字一致"声明失实
- 位置：`PC端/测试副本/xcs-2015/xcs.dll`（sha256 6cc0faa4…）、`PC端/测试副本/dist-2014/distinguish.dll`（sha256 4ee6eb4c…）
- 证据：副本 IL 字符串堆无 bate/img_add_num/upload_img_clarity（UTF-16 计数 0；当前源码构建为 1）；其余指纹（IsAsync=1、ConverterParameter=10、g.resources 名集、文件大小 10184704/10272256）与本次重建一致 → 副本=兼容键重加之前的构建。
- 影响：docs/功能验证报告.md:14-18 "最终复验（14:10/14:14）…与交付源码逐字一致"对应的二进制≠当前交付源码（被测物缺 7 处兼容键改动）；restore-status.md:67-69 同口径失实。真机 39/38 全 PASS 的结论**不覆盖**当前含兼容键的构建。
- 建议：以当前源码重建并刷新两副本（dist-2014 需含 app_version 旁路则重做），修订两文档表述或补做一轮 fulltest。

### N3【低·工程卫生】`analysis/xcs-cs112/build-errors.txt` 应删未删
- 4 行旧 markup 编译错误日志（MC3061/MC4102/MC4108），非原包、非工程必需、distinguish 侧无对应文件。建议删除或在 restore-status 注明其历史用途。

### N4【低·一致性】NoWarn CA1416 两版不一致
- `analysis/distinguish-cs112/distinguish.csproj:9` 有 `<NoWarn>$(NoWarn);CA1416</NoWarn>`；`analysis/xcs-cs112/xcs.csproj` 无。本次两版构建均未产生 CA1416 告警 → 该抑制在 distinguish 侧属无效果脚手架，建议两版统一（移除或都保留并注明原因）。

### N5【低·格式】`analysis/xcs-cs112/distinguish.ViewModel/PreviewSettingViewModel.cs` 为 LF 行尾
- 其余被核对的 C# 源文件（含参考层与 distinguish-cs112 同名文件）均为 CRLF；该文件与参考层逐行内容一致但行尾不同，导致字节级比较失败。建议统一为 CRLF。

### N6【低·文档】D 轮核查文档两条结论已过时未加时点限定
- `docs/audit/D_验证记录核查.md:86` "两项目均 0 警告 0 错误"——与 `--no-incremental` 可复现的 4×SYSLIB0014 不符（疑为增量构建跳过编译所得假 0）；`:90-97` "runtime-test 副本与当前源码逐字节一致=证实"——在 A 轮后续兼容键重加后已失效（副本未刷新，见 N2），应加"截至 2026-11-12 上午"时点限定或勘误。

### N7【信息·忠实性完备性】3 个 wav 的 Resource 映射缺失 + 提示音禁用
- 原版 exe 的 .g.resources 内嵌 `res/error.wav`/`res/not_error.wav`/`res/receive_sound.wav`（资源名命中），重建改走 `<Content>` 外置复制（csproj:307-309）；叠加 restore-status.md:92-93 已声明的 3 处 SoundPlayer 注释禁用（HomeViewModel.cs:3104/3110/3116，原版为 active 调用）。两者叠加后 wav "双不使用"。禁用是已声明的有意偏差（合规），但 csproj 映射差异未被单独记录——若未来恢复提示音，需注意 pack URI 路径将不可用（当前代码用相对路径 File 语义，不受影响）。

### N8【信息·来源注明】`app.ico` 文件名非原 exe 资源名
- 两版 exe 内 `app.ico` 资源名 0 命中（favicon/jyz/xincaisheng/000 均命中）→ app.ico 疑为从 PE 头图标提取后的自命名（csproj `<ApplicationIcon>` 引用）。建议在 restore-status 注明提取方式，避免后续审计误判为夹带。

---

## 与 A 轮 10 项修复声明的对照小结

| A 轮项 | 修复声明 | 本次复核 |
|---|---|---|
| A1 杜撰 6 键 | 剔除→后经批准作为兼容适配重加（restore-status:22-24） | 状态与声明一致（4 文件现况=兼容键在、模型属性回退）；台账 A1 "逐字一致✅"需读者注意后续修订 |
| A2 (D7) converter 绑定缺失 | 已恢复，参数 "0"/"1" 推断 | 绑定恢复属实，但**推断值 4/4 全错**（N1） |
| A3 (D8) IsAsync 缺失 | 已恢复 | 证实（计数+落点均对） |
| A4 夹带 appsettings.ini | 已移除 | 证实（工程根+csproj 0 命中） |
| A5 iniConfigPath 回退 | 回退 "./appsettings.ini" | 本次抽查 Global.cs 两版 `iniConfigPath = "./appsettings.ini"` ✓（restore-status:33 声明一致） |
| A6 SYSLIB0014 pragma 移除 | 警告如实显示 | 证实（实测 4W/版） |
| 副本刷新声明 | "修复后已刷新" | **证伪**（N2） |
| 0E/4W 声明 | 两版各 4 警告 | 证实（N2 使副本侧失效） |

## 复核局限
- fixture selftest "82/82" 未复跑（超出本清单范围）；`.tools/smoke-ui` 真机日志未重新解析（沿用文档引用，未独立验证 39/38）。
- BAML 解码为逆向字节级分析（长度前缀编码经多旁证交叉验证），未使用 BAML 官方解析器；结论以两版原 exe 字节互相印证为准。
- scripts/Test-ReverseEnvironment.ps1 未实际执行（避免写入 `.tools/`），:104 失效路径为静态判定。
