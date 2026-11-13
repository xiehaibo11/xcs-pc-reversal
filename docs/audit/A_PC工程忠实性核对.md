# A — PC 端两版 .NET 工程忠实性核对（对抗性审计）

- 审计对象：`analysis/xcs-cs112/`（声明为 xcs.exe 2.0.15 还原）、`analysis/distinguish-cs112/`（声明为 distinguish.exe 2.0.14 还原）
- 基线：`analysis/xcs-cs/`（2.0.15 反编译 + baml-strings）、`analysis/distinguish-cs/`（2.0.14 反编译）、原包 `xcs电脑图传/xcs.exe`、`鑫财神图传PC/鑫财神/distinguish.exe`
- 方法：方法集差集（grep 成员签名）、逐行 diff（剥离生成代码后语义比对）、Connect() 全表机器比对、BAML 字符串对账、原版 exe 二进制串扫描（UTF-8/UTF-16，只读不执行）、资源 md5 对账、干净重建复核
- 审计日期：2026-11-12；SDK：dotnet 10.0.404

## 结论行

**忠实性判定：10 处实质偏差 / 2 处疑似（待人工）/ 9 项声明被证实**

> 核心裁决：两工程的**骨架是忠实的**（45×2 个 .cs 无成员缺失、Connect 接线 16/16 全对、资源/图标/依赖映射与原包一致、双版本 UI 差异被正确区分），但**四个文件被注入了两个原版 exe 中都不存在的新逻辑**（Device/DataConfig 新属性 + 两处 HTTP POST 新增键），且 XAML 有两处绑定细节丢失、一处路径语义改动、一个非原包文件 appsettings.ini 被随包交付。

---

## 1. 实质偏差清单（每项：文件:行号 | 差异内容 | 判定 | 影响）

| # | 位置 | 差异内容 | 判定 | 影响 |
|---|------|----------|------|------|
| D1 | `analysis/xcs-cs112/distinguish.Model/Device.cs:45-56, 294-365`（对照 `xcs-cs/distinguish.Model/Device.cs`，284 行 → 368 行） | 还原版比 2.0.15 反编译**多出 6 个属性**：`Beta / Count_type / Gk_max / Gk_min / Img_add_num / Upload_img_clarity` | **杜撰/外来串入**（两原版 exe 二进制扫描均 0 命中：`bate`=0, `upload_img_clarity`=0, `gk_min`=0, `img_add_num`=0；2.0.14 反编译同样没有） | 模型与原版不符；为 D2 提供字段 |
| D2 | `analysis/xcs-cs112/distinguish.ViewModel/PreviewSettingViewModel.cs:450-456`（对照 `xcs-cs/.../PreviewSettingViewModel.cs:434-450`） | 保存参数 POST `preview/set_params_v2/{id}` 的 JSON **新增 6 键**：`bate`(注意拼写)、`img_add_num`、`gk_min`、`gk_max`、`upload_img_clarity`、`count_type` | **杜撰/外来串入 → 行为偏差**（原版 exe UTF-16 常量堆 `bate`/`upload_img_clarity`/`gk_min` 均 0 命中；还原 dll 各 1 命中） | **最重**：向服务器多发送 6 个恒为 0 的新键。若服务端"缺键=不改、显式 0=改"，会把服务器侧参数清零；原版从不发送这些键 |
| D3 | `analysis/xcs-cs112/distinguish.ViewModel/OnlineConfigViewModel.cs:280-281`（对照 `xcs-cs/.../OnlineConfigViewModel.cs:280`） | `saveDatgaConfig()` POST `config/set_data_config` **新增键** `["upload_img_clarity"] = DataConfig.Upload_img_clarity` | **杜撰/外来串入 → 行为偏差**（同上，原版 exe 0 命中） | 同 D2，覆盖数据配置接口 |
| D4 | `analysis/distinguish-cs112/distinguish.Model/Device.cs` + `distinguish.Model/DataConfig.cs`（对照 `distinguish-cs/` 同名文件） | 2.0.14 还原工程**同样多出** D1 的 6+1 个属性 | **杜撰/外来串入**（distinguish.exe 扫描 0 命中；2.0.14 反编译无） | 同 D1 |
| D5 | `analysis/distinguish-cs112/distinguish.ViewModel/PreviewSettingViewModel.cs`（与 xcs-cs112 版本同构，+6 键含 `bate`） | 同 D2，注入到 2.0.14 工程 | **杜撰/外来串入 → 行为偏差**（distinguish.exe UTF-16 `bate`=0 命中，还原 dll=1 命中） | 同 D2（2.0.14 客户端） |
| D6 | `analysis/distinguish-cs112/distinguish.ViewModel/OnlineConfigViewModel.cs`（saveDatgaConfig +`upload_img_clarity`） | 同 D3，注入到 2.0.14 工程 | **杜撰/外来串入 → 行为偏差** | 同 D3（2.0.14 客户端） |
| D7 | `analysis/xcs-cs112/distinguish/OnlineWindows.xaml:86-93,103-110`（对照 `xcs-cs/baml-strings/onlinewindows.strings.txt`：`Converter#`+`ConverterParameter$`+`DataConfig.Data_send+#` 相邻出现） | `数据发送`/`计数方式` 两组 RadioButton 的 `<Binding Path="DataConfig.Data_send"/>`（及 Count_type）**丢失 Converter=distinguish.RadioToBoolConverter 与 ConverterParameter**；资源 `genderConverterKey` 声明了但全文 0 处使用 | **缺失**（2.0.15 有 BAML 字符串铁证；还原 dll `ConverterParameter` 10→9） | 功能缺陷：ConvertBack 本应按参数写回 "1"/"0"；无转换器后两个 RadioButton 绑同一 int 属性、写回值相同——选择任一项都写 1，设置项无法正确切换/回显可能双选 |
| D8 | `analysis/xcs-cs112/distinguish/HomeWindow.xaml:388`（16 处 `DataContext.ChannelMjHeight` 绑定；对照 baml-strings：`IsAsync` 1 次 + `DataContext.ChannelMjHeight`+`FindAncestor`+`Window` 相邻） | 原版 BAML 有 1 处 `IsAsync=True`（最可能挂在首个 ChannelMjHeight 绑定上）；还原 XAML 16 处绑定**全部无 IsAsync**（exe 扫描：原版 utf8=1，还原 dll utf8=0） | **缺失**（影响哪一处绑定是推断，见 S1） | 绑定改为 UI 线程取值；原版用 IsAsync 避免卡顿。功能不中断，性能/线程语义有差 |
| D9 | `analysis/xcs-cs112/distinguish.Common/Global.cs:13`、`analysis/distinguish-cs112/distinguish.Common/Global.cs:13`（对照两原版同为 `./appsettings.ini`） | `iniConfigPath = "./appsettings.ini"` → `AppDomain.CurrentDomain.BaseDirectory + "appsettings.ini"` | **行为偏差（有意规范化）** | 工作目录≠exe 目录时（快捷方式"起始位置"不同、被其它进程带 CWD 启动），读写 ini 位置与原版不同。常规双击启动下等效 |
| D10 | `analysis/xcs-cs112/appsettings.ini`、`analysis/distinguish-cs112/appsettings.ini`（两原包 `xcs电脑图传/`、`鑫财神图传PC/鑫财神/` 均**无**此文件；反编译目录亦无） | 还原工程随包附带 `[VERIFY] PWD=E1ADC3949BA59ABBE56E057F2F883E`（即 GetMd5 风格的 md5("******")，为运行期 AppConfig.setConfig 生成的痕迹） | **非原包文件混入 → 行为偏差** | 原版首跑无 ini → 校验口令回退内置默认 `Global.verifyDefaultPwd`（`Global.cs:41`，E951081AC30FFA83F10B68CDE1CAC7）；还原交付首跑即读到 "******" 的口令哈希，**开箱校验口令与原版不一致** |

> D1–D6 合并看：同一套"新功能字段"被同时注入两个版本工程，且两原版 exe 均无——既不是 2.0.15 串入 2.0.14，也不是 2.0.14 串入 2.0.15，而是**共同的外来增量**（来源不明，疑似对齐了某个未提供的更新版服务端协议，`bate` 拼写错误为显著指纹）。

## 2. 疑似项（待人工，S=Suspect）

| # | 位置 | 内容 | 为什么疑似 |
|---|------|------|-----------|
| S1 | `xcs-cs112/distinguish/HomeWindow.xaml`（IsAsync 缺失，见 D8） | BAML 字符串表只能证明原版有 1 处 IsAsync，且与首个 `DataContext.ChannelMjHeight` 绑定相邻出现；无法从字符串表断言它挂在第 388 行而非其它 15 处之一 | 需反编译原版 BAML 记录流（RecordType 级）才能定位到具体绑定 |
| S2 | `distinguish-cs112/distinguish/OnlineWindows.xaml`（D7 的 2.0.14 侧） | 2.0.14 无 baml-strings 目录，只能以 exe 扫描旁证（`RadioToBoolConverter`=2、`genderConverterKey`=1、`ConverterParameter`=10，与 2.0.15 完全相同；还原 dll 均少 1） | 同上，BAML 归属未逐窗口解码 |

## 3. 被证实的声明（9 项）

1. **成员级忠实**：45×2 个 .cs 对（两工程各自 vs 各自反编译基线）方法/属性集合差集 `only_orig=0`、`only_rest=0`——**无缺失成员**。逐文件见 §4。
2. **窗口代码后台语义等价**：9 个窗口逐一剥离反编译生成代码（InitializeComponent/Connect/字段声明）后 diff，仅存差异为：`public class`→`public partial class`、`+= new EventHandler(X)`→`+= X`、`((Size)(ref newSize)).Height`→`newSize.Height`、`delegate{}`→lambda、`(Image)`→`(System.Drawing.Image)`——全部为反编译伪码规整，无逻辑增删。
3. **Connect() 接线 16/16 全对**：8 个带 IComponentConnector.Connect 的窗口 × 2 版本，原始反编译 `Connect()` case→(字段赋值+事件) 全表与**干净重建后**的 `obj/.../*.g.cs` 机器比对，**0 处不一致**（全表见 §5）。App 无 Connect，StartupUri=LoginWindow.xaml 与原版 `App.cs` InitializeComponent 内设置一致。
4. **XAML 主体忠实**：9 窗口 BAML 字符串逐条对账，元素类型/x:Name/绑定路径/事件名/颜色/边距数值全部能在还原 XAML 找到（110 条 CJK UI 文案逐条在原版 exe 中验证存在；反向扫描原 exe 的 114 条 UTF-8 CJK 序列，除 7 条二进制巧合碎片外 100% 覆盖）。
5. **双版本 UI 差异被正确区分**：HomeWindow.xaml 仅两处版本差——①工具栏按钮 2.0.15 用 `Binding OffineSettingText/PreviewText/StartText/EndText/RestartText/ValidaSendText/SendText`、2.0.14 用静态文案（联机配置/画框预览/开始游戏/结束游戏/重新开始/校验发送/立即发送）；②2.0.15 多 `严格校验数据`(SendVlidateIsCheck) 复选框。经原版 exe 验证：这些绑定名与复选框文案在 distinguish.exe(2.0.14) 中 **0 命中**、静态文案**存在**——即 `distinguish-cs112/HomeWindow.xaml` 是 2.0.14 的忠实还原，未把 2.0.15 的 XAML 带进去。
6. **无 2.0.15 专有逻辑串入 distinguish-cs112**：`DeleteOffset/ShiftForward/GetLogicalSlot/initText/cq_favicon/cs.ico` 在 distinguish-cs112 全部 **0 命中**（.cs/.xaml/.csproj/.ini），在 xcs-cs112 中按原版位置存在（Channel.cs/HomeViewModel.cs/csproj）；distinguish.exe 二进制同样 0 命中、xcs.exe 存在——还原与原版逐项吻合。
7. **RemoveItem 裁决**：A4 报告正确。`distinguish-cs/distinguish.ViewModel/HomeViewModel.cs:3124-3132`（2.0.14 原版）RemoveItem **仅清空选中槽位**（ResultImg/UploadImg/Result 置空），全文无 ShiftForward/DeleteOffset（distinguish.exe 二进制 0 命中佐证）；`xcs-cs/distinguish.ViewModel/HomeViewModel.cs:3328-3361`（2.0.15 原版）才引入 `FindChannelByIndexPrefix + ShiftForward + DeleteOffset++`（"删除并前移"）。另一份"2.0.14 本就存在"的说法只在"存在同名方法"这一字面意义上成立，语义层面 2.0.14 没有"删牌前移"。两还原工程各自忠实于各自版本（distinguish-cs112=清槽版、xcs-cs112=前移版）。
8. **资源与依赖映射一致**：`res/` 101 个文件、7 个 ico、22 个依赖 DLL（BouncyCastle/MvvmLight/Newtonsoft/websocket-sharp/MS.Extensions.* 等）md5 与反编译基线逐字节相同；还原 `res/` 多出的 3 个 wav（error/not_error/receive_sound）与原包 `xcs电脑图传/res/`、`鑫财神图传PC/鑫财神/res/` 中同名文件 md5 逐字节相同（原 exe 运行时按 `./res/*.wav` 读取，属正确补齐）；csproj `LogicalName` 映射正确保留原资源名（`loginwindow.xaml`↔MainWindow.xaml、`app.xaml`、`res/...`、依赖 DLL 裸名），与原版 `LoadComponent("/xcs;component/loginwindow.xaml")` URI 兼容；App.xaml.cs 的 `.g.resources` 资源解析器逐行一致。csproj 差异仅为 net47→net8.0-windows 目标框架、新增 `System.Drawing.Common 8.0.10` 包引用（net8 必需，原版 net47 内置）、SupportedOSPlatform 特性（仅 xcs 版）。
9. **版本常量忠实**：`Global.cs` — xcs-cs112 `app_version="2.0.15"`/`app_name="xcs"`、distinguish-cs112 `app_version="2.0.14"`/`app_name="xcs"`，与各自原版一致；AssemblyInfo 版本号 2.0.1.0/Title 保持原值。

## 4. 逐文件核对表（原始 ↔ 还原）

图例：成员数=方法/属性签名数（grep 统计）；差异行=剥除生成代码后的 diff 行数；判定=噪声(等价重构)/缺失/杜撰。

### 4.1 xcs-cs ↔ xcs-cs112（45 对 .cs）

| 文件 | 成员数(原=还) | 差异行 | 判定 |
|---|---|---|---|
| Properties/AssemblyInfo.cs | 0/0 | 3 | 噪声：ThemeInfo 参数解码 + SupportedOSPlatform（net8 要求） |
| distinguish.Common/AppConfig.cs | 2/2 | 0 | 一致 |
| distinguish.Common/Constant.cs | 0/0 | 0 | 一致 |
| distinguish.Common/CryptionUtils.cs | 7/7 | 34 | 噪声：局部变量 obj→val3 重命名、IL 注释 O→Obj |
| distinguish.Common/FilesINI.cs | 5/5 | 0 | 一致 |
| distinguish.Common/Global.cs | 4/4 | 2 | **偏差 D9**（iniConfigPath 语义变化） |
| distinguish.Common/HttpReq.cs | 2/2 | 8 | 噪声：obj→httpClient 重命名 |
| distinguish.Common/IniConfigReader.cs | 1/1 | 2 | 噪声 |
| distinguish.Common/IsConnectInternet.cs | 3/3 | 0 | 一致 |
| distinguish.Common/NetManager.cs | 10/10 | 22 | 噪声：自动属性 IsShowPingLog 展开为显式后备字段（语义等价） |
| distinguish.Model/AppVersion…WifiConfig (16 文件) | 全等 | 0 | 一致 |
| distinguish.Model/DataConfig.cs | 0/0 | 14 | **杜撰 D1**（+Upload_img_clarity） |
| distinguish.Model/Device.cs | 20/26 属性 | 84 | **杜撰 D1**（+6 属性） |
| distinguish.Properties/Resources.cs、Settings.cs | 1/1、0/0 | 0 | 一致 |
| distinguish.ViewModel/EditPwdViewModel.cs | 2/2 | 2 | 噪声 |
| distinguish.ViewModel/HomeViewModel.cs | 54/54 | 122 | 噪声：delegate→lambda、`X.Count()`→`Enumerable.Count(X)`、#pragma SYSLIB0014（仅压警告） |
| distinguish.ViewModel/LoginViewModel.cs | 3/3 | 8 | 噪声：ParameterizedThreadStart delegate→lambda |
| distinguish.ViewModel/OnlineConfigViewModel.cs | 7/7 | 15 | **杜撰 D3**（+1 POST 键） |
| distinguish.ViewModel/PreviewSettingViewModel.cs | 4/4 | 26 | **杜撰 D2**（+6 POST 键） |
| distinguish/App.cs ↔ App.xaml.cs | 4→2+生成 | 31 | 噪声：生成代码移入 App.g.cs；`((Application)this).OnStartup(e)`→`base.OnStartup(e)` 等价 |
| distinguish/{8 窗口}.cs ↔ *.xaml.cs | 各窗口 only_orig=InitializeComponent | — | 噪声：生成代码移入 .g.cs；VerifyPwdWindow delegate→lambda 等价 |
| distinguish/RadioToBoolConverter.cs、ValidTextBgBrushConverter.cs | 2/2 | 0 | 一致 |

### 4.2 distinguish-cs ↔ distinguish-cs112（45 对 .cs）

与 4.1 **逐条同构**（两工程对各自基线的差异集合完全相同）：除 4.1 列出的 13 个差异文件外其余 32 文件 0 差异；HomeViewModel 差异行仅 118（同一套等价重构，且**无** SYSLIB0014 pragma——这解释了构建警告 4 vs 0 的差异）；Device/DataConfig/PreviewSettingViewModel/OnlineConfigViewModel/Global.cs 差异与 D1/D2/D3/D9 同型（→D4/D5/D6/D9）。

### 4.3 XAML 对账（9 窗口 × 2 工程）

| XAML | 对应原 BAML | 对账结果 |
|---|---|---|
| App.xaml | app.strings.txt | 4 个 Style（ValidPage_Mj_Item_Grid/Img/But、ValidPage_ListBox）、触发器、边距 1.2/0.7 全部在；StartupUri=LoginWindow.xaml 与原版 App.cs 一致 |
| MainWindow.xaml | loginwindow.strings.txt | 登录界面：背景 /res/login_bg.png、ipAddrInput/userInput/pwdInput/loginBut/resetBut、CheckBox Checked/Unchecked、477,42,30,0 等全部在 |
| HomeWindow.xaml | homewindow.strings.txt | 17 个接线元素、42 处绑定路径（含 16×DataContext.ChannelMjHeight + AncestorType=Window×69、34 个 *StatusNumber）、牌图资源路径全部在；**缺 IsAsync×1（D8）**；`{RelativeSource AncestorType=Window}` 简写不生成字面 "FindAncestor"（原 exe 70→0）但语义等价（WPF 隐式 Mode=FindAncestor） |
| EditPwdWindow.xaml | editpwdwindow.strings.txt | 全部在 |
| ErrorWindow.xaml | errorwindow.strings.txt | 全部在（含 The program has exited abnormally、0xC0000001-STATUS_ACCESS_ERROR、#D5B474/#f2f2f2、Feedback Tracker、twitter 互动支持——原 exe 二进制逐条验证） |
| OnlineWindows.xaml | onlinewindows.strings.txt | 结构在；**Data_send/Count_type 绑定缺 Converter+ConverterParameter（D7）**；`distinguish.RadioToBoolConverter` 有声明、无使用 |
| PreviewSettingWindow.xaml | previewsettingwindow.strings.txt | 36 个接线、Slider 范围 240/300/320/±64/±500/255/100/4900、___CapFpsComboBox 全部在 |
| PreviewWindow.xaml | previewwindow.strings.txt | 全部在（0 缺失） |
| VerifyPwdWindow.xaml | verifypwdwindow.strings.txt | 全部在 |

（2.0.14 无 baml-strings，以 exe 二进制扫描 + 与 2.0.15 同构 + §3.5 的版本差异验证覆盖；两工程 9 个 XAML 中 8 个逐字节相同，仅 HomeWindow.xaml 有 §3.5 所述两处正确的版本差。）

## 5. Connect() 接线对照表（原始 case 表 ↔ 重建后 g.cs，16 组全对）

App：无 Connect（原版亦无；InitializeComponent 设 StartupUri=LoginWindow.xaml）。

| 窗口 | case 数 | 接线内容（两版本完全一致） | 比对结果 |
|---|---|---|---|
| MainWindow(=loginwindow) | 7 | 1:Button_Click · 2:ipAddrInput · 3:userInput · 4:pwdInput · 5:CheckBox_Checked+CheckBox_Unchecked · 6:loginBut · 7:resetBut | 一致 |
| HomeWindow | 17 | 1:MenuItem Click→RemoveItem_Click · 2:MainGrid · 3:Online_Click · 4:Preview_Click · 5:StartGames_Click · 6:EndGames_Click · 7:RestartGames_Click · 8:ValidationSendData_Click · 9:SendData_Click · 10:EditPwd_Click · 11:Logout_Click · 12:MainTabControl+TabControl_SelectionChanged · 13:DongReload_Click · 14:NanReload_Click · 15:XiReload_Click · 16:BeiReload_Click · 17:SendData_Click | 一致 |
| EditPwdWindow | 4 | 1:ipAddrInput · 2:userInput · 3:pwdInput · 4:loginBut+SaveEditPwdBut_Click | 一致 |
| ErrorWindow | 3 | 1:Window_PreviewTextInput · 2:TextBox_KeyUp · 3:Button_Click | 一致 |
| OnlineWindows | 5 | 1:SaveConfigNumber_Click · 2:SaveDataConfig_Click · 3:SaveWifi_Click · 4:RestartDevice_Click · 5:ResetVerifyPwd_Click | 一致 |
| PreviewSettingWindow | 36 | 1:YSub 2:YAdd 3:XSub 4:XAdd 5:WidthSub 6:WidthAdd 7:HeightSub 8:HeightAdd 9:ImgRotationSub 10:Slider_ValueChanged 11:ImgRotationAdd 12:BrightnessSub 13:Slider_ValueChanged 14:BrightnessAdd 15:CapGainSub 16:Slider_ValueChanged 17:CapGainAdd 18:CapExposureSub 19:Slider_ValueChanged 20:CapExposureAdd 21:CapHueSub 22:Slider_ValueChanged 23:CapHueAdd 24:CapSaturationSub 25:Slider_ValueChanged 26:CapSaturationAdd 27:CapContrastSub 28:Slider_ValueChanged 29:CapContrastAdd 30:ImgMinHresholdSub 31:Slider_ValueChanged 32:ImgMinHresholdAdd 33:___CapFpsComboBox+ComboBox_SelectionChanged 34:MotorSub 35:MotorAdd 36:SaveClick | 一致 |
| PreviewWindow | 4 | 1:Button_Click 2:Button_Click_1 3:Button_Click_2 4:Button_Click_3 | 一致 |
| VerifyPwdWindow | 2 | 1:pwdInput+pwdInput_KeyUp 2:Button_Click | 一致 |

机器比对输出：`xcs-cs …mismatches=0`（8 组）、`distinguish-cs …mismatches=0`（8 组）；干净重建后复跑仍 0。事件处理器方法（RemoveItem_Click…SaveClick）在还原 code-behind 中全部存在（成员差集 only_orig=0 覆盖）。

## 6. 构建复核（干净重建，实测）

清理 bin/obj 后执行 `dotnet build <csproj> -c Release -p:EnableWindowsTargeting=true`（SDK 10.0.404）：

| 工程 | Error | Warning | 明细 |
|---|---|---|---|
| xcs-cs112 | **0** | **0** | 声明称"4-56 条警告"**未复现**：0 条。原因：xcs-cs112 的 HomeViewModel 加了 `#pragma warning disable SYSLIB0014`（distinguish-cs112 没加），把仅有的 SYSLIB0014（WebClient 过时，2 处）压掉了 |
| distinguish-cs112 | **0** | **4** | SYSLIB0014 ×2（HomeViewModel.cs:2499,3166，wpftmp+主工程各报一遍）。落在声明"0-4 条"区间上沿 |

产物哈希（已记录，bin/obj 审计后已删除）：
- `xcs.dll` = md5 `7a942cee88c44f3aebda01541a65aa91`，`xcs.exe` = `01fdbeb52af112b7b55190c91fe58be9`
- `distinguish.dll` = md5 `7e3683d3b397fafc7603a7fa9b68cee2`，`distinguish.exe` = `abda74d0b70681b8dcb4d06671fd49d5`
- 与重建前 dll 对比：重建可复现（同哈希）；体积对照原版：xcs.dll 10,272,768 B vs xcs.exe 10,269,696 B（差 0.03%，主因 D1-D3 新增字符串与 net8 元数据），distinguish.dll 10,185,728 B vs distinguish.exe 10,182,144 B（差 0.04%）
- 二进制指纹复核（重建 dll vs 原版 exe）：`bate/upload_img_clarity/gk_min/img_add_num` 原版 0→还原有（D1-D6 铁证）；`IsAsync` 原版 1→还原 0（D8）；`ConverterParameter` 原版 10→还原 9（D7）；`initText/GetLogicalSlot/ShiftForward/DeleteOffset` xcs 有/dg 无，两侧与原版一致

## 7. 未尽事项（UNVERIFIED）

- **UNVERIFIED**：原版 BAML 记录流级反编译未做（无现成工具链），因此 D7/D8 的"具体挂在哪个元素"在 2.0.15 侧靠字符串表相邻性推断、2.0.14 侧仅 exe 计数旁证。
- **UNVERIFIED**：`bate` 等 6 键的来源（哪个更新版本/哪份服务端协议）无法在本仓库内追溯。
- **UNVERIFIED**：还原 exe 的运行时行为等价性（登录/发牌/WebSocket 流程）未做端到端实测——本审计不运行任何 exe。
- **UNVERIFIED**：依赖 DLL（22 个）与原 exe 内嵌副本的一致性只做了"还原工程目录 vs 反编译目录"比对（逐字节相同），未解包原 exe 的嵌入资源逐字节比对（体积与 ResourceResolver 机制一致，间接佐证）。
- 原 exe 内 `ConverterParameter` 计数 10 vs 还原 9 的其余 9 处分布未逐窗口定位（onlinewindows 之外的 9 处与还原一致，因还原 XAML 仅缺 D7 一处）。

## 8. 审计方法与证据留痕

- 方法集提取：`grep -ohE "(public|private|protected|internal)[^=;{}]*\b[A-Za-z_][A-Za-z0-9_]*\s*\("`，`comm` 差集；45×2 对全部 only_orig=0/only_rest=0。
- 剥离生成代码后的语义比对：Python difflib（去字段声明/_contentLoaded/InitializeComponent/Connect/[Debugger…]/[GeneratedCode…] 块）。
- Connect 机器比对：正则提取 case→(ASSIGN/EV) 序列，orig(`\t`缩进) vs g.cs(4 空格缩进) 双格式解析器，比较 `Dict[int, sorted list]`。
- BAML 对账：baml-strings 行清洗（去二进制长度前缀字符）后在 XAML 全文（lower）中查找；CJK 双向校验直接对原 exe 做字节扫描（UTF-8 BAML 串 + UTF-16 #US 堆）。
- 构建：`rm -rf bin obj` 后 `dotnet build -c Release -p:EnableWindowsTargeting=true`；产物 md5 记录于本文 §6，bin/obj 已删除。
