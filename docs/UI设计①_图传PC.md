# UI 设计① 图传 PC —— xcs.exe / distinguish.exe（.NET WPF，两版本共 9 窗口）

> 素材来源（全部为逆向还原产物，UI 定义逐行提取，无臆造）：
> - XAML：`analysis/xcs-cs112/distinguish/*.xaml`（App + 8 窗口）与各 `.xaml.cs` 事件逻辑
> - ViewModel：`analysis/xcs-cs112/distinguish.ViewModel/*.cs`（5 个）
> - 公共层：`analysis/xcs-cs112/distinguish.Common/`（HttpReq / NetManager / Constant / Global）
> - 资源：`analysis/xcs-cs112/res/`（登录背景/按钮贴图、牌面 jpg、sucai 素材、音效）
> - 两版差异：`docs/A4_双版本2.0.14_vs_2.0.15差异报告.md`（B0~B7）；授权链：`docs/A2_校验口令解锁链核对.md`
> - 协议：`docs/逆向①_xcs电脑图传.md`
> 行号引用格式 `文件路径:行号`。XAML/.cs 中的 `//IL_*`、`<!--Unknown connection ID: n-->` 为 ILSpy/BAML 反编译噪声，本文一律忽略并在涉及处注明。

---

## 0. 信息架构总览

### 0.1 窗口关系图（ASCII）

```
 进程启动 (Global.verify=false，每次启动必为 false)
   │ App.OnStartup: 挂 AssemblyResolve
   │ StartupUri="LoginWindow.xaml"（资源名，解析到 MainWindow 类）
   ▼
 ┌─────────────────────────────────────────────┐
 │ MainWindow（登录窗，540x502 无边框透明）        │ MainWindow.xaml.cs:18-24
 │  构造: RemoveCacheImg(); if(!verify)          │
 │    → new ErrorWindow().Show(); this.Close()   │ ← 每次启动必走（授权门）
 └───────────┬─────────────────────────┬───────┘
             │ LoginCommand 成功        │
             ▼                         ▼
 ┌──────────────────────┐   ┌────────────────────────────────┐
 │ HomeWindow（主界面,    │   │ ErrorWindow（授权门 520x280）     │
 │ WindowState=Maximized)│   │  全窗口键盘累积输入 MD5==口令     │
 │  ├ OnlineWindows(模态) │   │  命中 → verify=true             │
 │  │   └ VerifyPwdWindow│   │   isSetPwd=true → 回 MainWindow  │
 │  │     (toLogin=false) │   │   isSetPwd=false → VerifyPwdWin │
 │  ├ PreviewWindow(模态) │   └────────────────────────────────┘
 │  │   └ PreviewSetting  │
 │  │     Window(模态)    │   ┌────────────────────────────────┐
 │  ├ EditPwdWindow(模态) │   │ VerifyPwdWindow（专属密码 400x190）│
 │  └ 关闭时 Logout()     │   │  写 INI VERIFY/PWD;             │
 └──────────────────────┘   │  toLogin=true → MainWindow      │
                            └────────────────────────────────┘
 模态弹层：MessageBox（登录/开局/收满/校验失败等提示）、全局 ContextMenu「删除选中」
```

- 解锁链闭环证据：`analysis/xcs-cs112/distinguish/MainWindow.xaml.cs:18-22`（`!Global.verify` → ErrorWindow 并自关）、`ErrorWindow.xaml.cs:47-63`（`Window_PreviewTextInput` 累积输入、命中置 `verify=true`）、`docs/A2_校验口令解锁链核对.md` §2.2 状态机图。
- `Global.verify` 为进程级单布尔：定义 `distinguish.Common/Global.cs:37`，唯一写 true 点 `ErrorWindow.xaml.cs:52`，全工程无置回 false 的路径。

### 0.2 分辨率 / 尺寸总表

| 窗口 | XAML 声明尺寸 | 窗口样式 | 出处 |
|---|---|---|---|
| MainWindow（登录） | 540 × 502，FontSize=14 | WindowStyle=None + AllowsTransparency，CenterScreen，NoResize | `distinguish/MainWindow.xaml:2` |
| HomeWindow（主界面） | 1240 × 680（声明值） | WindowState=Maximized（实际全屏） | `distinguish/HomeWindow.xaml:2` |
| PreviewWindow | 420 × 300 | 默认边框，CenterScreen，NoResize | `distinguish/PreviewWindow.xaml:2` |
| PreviewSettingWindow | 640 × 340 | CenterScreen，NoResize | `distinguish/PreviewSettingWindow.xaml:2` |
| OnlineWindows | 450 × 340 | CenterScreen，NoResize | `distinguish/OnlineWindows.xaml:2` |
| EditPwdWindow | 450 × 300，FontSize=13 | CenterScreen，NoResize | `distinguish/EditPwdWindow.xaml:2` |
| VerifyPwdWindow | 400 × 190 | ThreeDBorderWindow，CenterScreen，NoResize | `distinguish/VerifyPwdWindow.xaml:2` |
| ErrorWindow | 520 × 280 | ThreeDBorderWindow，CenterScreen，NoResize | `distinguish/ErrorWindow.xaml:2` |
| App（全局） | 无窗体 | StartupUri="LoginWindow.xaml" | `distinguish/App.xaml:2` |

### 0.3 两版本（2.0.14 distinguish / 2.0.15 xcs）品牌与文案差异

| 项 | 2.0.14（distinguish.exe） | 2.0.15（xcs.exe） | 证据 |
|---|---|---|---|
| app_version | `2.0.14` | `2.0.15` | `distinguish-cs112/distinguish.Common/Global.cs:33` vs `xcs-cs112/distinguish.Common/Global.cs:33`（A4 B0） |
| app_name | `xcs`（两版相同） | `xcs` | `Global.cs:35`（A4 B0 注） |
| 主界面 7 个按钮文案 | XAML 硬编码长文案：联机配置/画框预览/开始游戏/结束游戏/重新开始/校验发送/立即发送 | 绑定 7 个 VM 属性，`initText()` 在 `app_name=="xcs"` 时切短文案：**设置/画框/开始/结束/重置/改牌/发送** | `xcs-cs112/distinguish/HomeWindow.xaml:27,33,39,45,51,57,63`（绑定）；`HomeViewModel.cs:1887-1899`（initText）；`docs/A4_…差异报告.md` B2 |
| 图标资源 | 000/favicon/app/jyz_favicon/xincaisheng_favicon.ico | 新增 `cq_favicon.ico`、`cs.ico`（多品牌图标储备，仓库根可见：`analysis/xcs-cs112/` 下 000.ico/app.ico/favicon.ico/cq_favicon.ico/cs.ico/jyz_favicon.ico/xincaisheng_favicon.ico） | A4 第三节 R1 |
| res/ 素材 | 与 2.0.15 零差异（牌面/背景/音效完全一致） | 同左 | A4 第三节 R2 |
| 其余窗口 | 9 个窗口的 XAML 结构、绑定、事件完全一致（差异仅反编译噪声） | 同左 | `docs/A4_…差异报告.md` §〇 diff 总览 |

---

## 1. App.xaml —— 全局资源与启动入口

### 1.1 用途与打开时机

- 应用入口：`StartupUri="LoginWindow.xaml"`（`distinguish/App.xaml:2`）。该资源名经 csproj `LogicalName` 复刻映射到 `MainWindow.xaml`（`x:Class="distinguish.MainWindow"`），即**启动首窗是登录窗**（`docs/A2_校验口令解锁链核对.md` §四）。
- `App.xaml.cs:11-15`：OnStartup 挂 `AssemblyResolve`，从 manifest 加载嵌入 DLL（单文件发布形态兜底）。

### 1.2 全局样式（App.xaml Resources，:3-60）

| x:Key | 目标类型 | 关键 Setter / Trigger | 界面作用 |
|---|---|---|---|
| `ValidPage_Mj_Item_Grid` | Grid | Width=45；DataTrigger `ValidPageSelect==True` → Background `#FFFF0000`（:5-16） | 校验视图牌格：选中张变红底 |
| `ValidPage_Mj_Item_Img` | Image | Width=45，Stretch=Fill；`ValidPageSelect==True` → Margin 1.2（:17-29） | 选中张四周留 1.2px 白缝 |
| `ValidPage_Mj_Item_But` | Button | 透明背景、无边框、Cursor=Hand；模板 Border hover 时 `ForgroundBrush100` + Opacity 0.7（:30-50） | 校验视图可点击牌格 |
| `ValidPage_ListBox` | ListBox | 横向滚动 Auto、纵向 Disabled、CanContentScroll=False、背景 `#e3e3e3`、BorderThickness 0（:51-58） | 方位视图/校验视图所有牌列表 |

---

## 2. MainWindow —— 登录窗

### 2.1 用途与打开时机

- 登录入口窗，由 StartupUri 实例化（`App.xaml:2`）；退出登录/改密成功后亦由代码 `new MainWindow().Show()` 重建（`HomeWindow.xaml.cs:138-143`、`EditPwdWindow.xaml.cs:23-29`）。
- 构造时 `RemoveCacheImg()` 清理缓存图；若 `!Global.verify` 立即转投 ErrorWindow（`MainWindow.xaml.cs:17-23`）。

### 2.2 ASCII 线框图（按 XAML Grid 行列，`MainWindow.xaml:3-217`）

```
┌────────────────────────────────────────────┐  窗体背景 ImageBrush /res/login_bg.png (:5)
│ Row0 (高100)                    [X 关闭]    │  ← 右上角 32x32 圆形按钮，hover 红色半透明 (:13-30)
│                                            │
│ Row1 (自动)      ┌─ 宽320 居中 ─┐           │
│   (IsVisServer)  │ 口令地址: [ipAddrInput ]│  ← 仅未保存 SERVER 时显示
│                  │ 用户名称: [userInput   ]│
│                  │ 用户密码: [pwdInput    ]│
│                  │ [√] 记住密码            │
│                  │ (LoginLoading) ████████ │  ← 不定进度条
│                  │ [ loginBut 图片按钮 60高 ]│  ← login_but.png / login_but_ing.png
│                  │ [ resetBut 重置口令 40高 ]│  ← login_reset.png (IsVisResetBut)
│ Row2 (高80)      客户端版本号：x.y.z        │  ← AppVersion (:210-214)
└────────────────────────────────────────────┘
```

### 2.3 控件清单表

| x:Name | 类型 | 显示文本 | Binding / 资源 | 事件 | 出处 |
|---|---|---|---|---|---|
| — | Button(32×32) | （无文字，资源样式） | Border CornerRadius=15，hover `#FFFF0000`+Opacity .5 | `Click="Button_Click"` | `MainWindow.xaml:14-29` |
| ipAddrInput | TextBox(高30) | 占位"请输入口令地址"（VisualBrush HelpBrush 斜体 30% 透明） | `Text→IpAddr`；Visibility→`IsVisServer` | — | `:41-71`；Visibility `:33-35` |
| userInput | TextBox(高30) | 占位"请输入用户名称" | `Text→UserName` | — | `:79-109` |
| pwdInput | TextBox(高30) | 占位"请输入用户密码" | `Text→Pwd` | — | `:117-147` |
| — | CheckBox | 记住密码 | `IsChecked→SavePwdCheckBox` | `Checked/Unchecked` | `:149-152`；`.cs:27-35` |
| — | ProgressBar(高15) | 不定进度 | Visibility→`LoginLoading` | — | `:154-159` |
| loginBut | Button(高60, FontSize16 Bold) | Content→`LoginButContent`（默认"立即登录"） | IsEnabled→`LoginButIsDisable`；Command→`LoginCommand`（CommandParameter=Window）；模板两图 `LoginButVis`/`LoginButIngVis` 切换 `/res/login_but.png`、`/res/login_but_ing.png`（300×55） | — | `:160-190` |
| resetBut | Button(高40, FontSize12) | "重置口令" | Command→`ResetCommand`；Visibility→`IsVisResetBut`；模板图 `/res/login_reset.png`（180×30） | — | `:192-207` |
| — | Label(FontSize12) | Content→`AppVersion`（"客户端版本号：2.0.15"） | `:210-214` | — | — |

### 2.4 数据绑定与 VM 属性对照（LoginViewModel.cs）

| 属性 | 类型 | INPC | 界面作用 | 初始值 | 出处 |
|---|---|---|---|---|---|
| IpAddr / UserName / Pwd | string | 是 | 三个输入框内容，来自/回写 INI `APP/SERVER|USERNAME|PASSWORD` | INI 读回（`:233-237`） | `LoginViewModel.cs:44-81` |
| SavePwdCheckBox | bool | 是 | 记住密码勾选；Pwd 非空则默认勾选 | `savePwdCheckBox=true`（`:238-241`） | `:142-153` |
| IsVisServer | Visibility | 是 | 口令地址行可见性（未存 SERVER 时 Visible） | INI 判断（`:225-235`） | `:155-169` |
| IsVisResetBut | Visibility | 是 | 重置口令按钮可见性（已存 SERVER 时 Visible） | 同上 | `:113-127` |
| LoginButVis / LoginButIngVis | Visibility | 是 | 登录按钮常态图/进行中图切换 | Ing 初始 Collapsed（`:32`） | `:171-201` |
| LoginLoading | Visibility | 是 | 登录进度条 | Collapsed（`:34`） | `:203-217` |
| LoginButIsDisable / LoginButContent | bool/string | 是 | 登录按钮可用/文字 | true / "立即登录"（`:244-245`） | `:83-107` |
| AppVersion | string | 是 | 版本号 Label | `"客户端版本号：" + Global.app_version`（`:247`） | `:129-140` |
| LoginCommand | RelayCommand\<Window\> | — | 登录 | `:242` | `:109` |
| ResetCommand | RelayCommand\<Window\> | — | 重置口令 | `:243` | `:111` |

### 2.5 交互流程（按钮 → 命令 → HTTP）

1. **loginBut → LoginCommand → `LoginViewModel.Login(window)`**（`LoginViewModel.cs:250-381`）：
   - 空值校验弹窗："请输入口令地址/用户名称/用户密码"（`:273-281`）；网络检测失败弹"网络不可以，请您检查网络配置！"（`:378`）。
   - `POST http://{入口IP}:8081/api/login/login/distinguish?app_name=xcs`，body `{"username","password","devicetype":5}`（`:301-298`，devicetype=5 为 PC 端标识）。
   - 成功后生成 RSA-2048 公钥（`:318`），`POST server/get_server_addr`（`HttpReq.post`，`:324`），私钥解密得 `[业务IP, dataKey]`（`:333-337`）；写 INI（`:330-332`），弹"登录成功"，`new HomeWindow(loginResp, Global.ipAddr).Show()` 并关登录窗（`:341-343`）。
   - 全程切换按钮态：LoginButContent="校验中..."、LoginButIngVis/L才显示 ing 图（`:287-291`、失败回置 `:348-363`）。
2. **resetBut → ResetCommand → `ResetFunt`**（`:383-398`）：`Global.serverInputVis` 且确认框选"是"→ 清 INI SERVER、IpAddr=""、显示口令行、隐藏重置按钮（`Global.cs:31`）。
3. **右上关闭钮 → `Button_Click` → `Application.Current.Shutdown()`**（`MainWindow.xaml.cs:37-40`）。

### 2.6 样式与视觉

- 背景：整窗 ImageBrush `/res/login_bg.png`（`MainWindow.xaml:4-6`，资源文件 `analysis/xcs-cs112/res/login_bg.png`）。
- 文字：三组 Label 前景 `#FFFFFFFF`（`:40,78,116`）；占位符 VisualBrush Opacity 0.3 斜体 12px（`:47-51`）。
- 按钮贴图：`/res/login_but.png`（常态）、`/res/login_but_ing.png`（登录中）、`/res/login_reset.png`（重置）（`:176-202`）。

---

## 3. ErrorWindow —— 启动授权门

### 3.1 用途与打开时机

- **每次进程启动的必经授权门**：MainWindow 构造发现 `!Global.verify` 时 `new ErrorWindow().Show()` 并自关（`MainWindow.xaml.cs:18-22`）。构造播放 `SystemSounds.Beep`，读 INI `VERIFY/PWD` 决定目标口令（空则用内置 `verifyDefaultPwd`）（`ErrorWindow.xaml.cs:23-34`）。

### 3.2 ASCII 线框图（`ErrorWindow.xaml:3-24`）

```
┌──────────────────────────────────────────────┐ 标题 "The program has exited abnormally"
│ Row0 (自动)                                   │
│  红字20px: The program has exited abnormally  │ (:12, Foreground #FFFF0000)
│  启动器已检测到程序异常关闭。如果您遇到您无法…   │ (:13, Wrap)
│  退出代码： 0xC0000001-STATUS_ACCESS_ERROR     │ (:15-16, #666666)
│  Feedback Tracker   （#D5B474 下划线手型）      │ (:18)
│  twitter 互动支持   （#D5B474 下划线手型）      │ (:19)
│ Row1 (高40, 背景#f2f2f2 右对齐)                │
│                            [ 关闭 ]           │ (:22, Click=Button_Click)
└──────────────────────────────────────────────┘
（隐藏 0×0 TextBox，KeyUp=TextBox_KeyUp，仅 Console 打印按键 :8-10）
```

### 3.3 控件清单与交互

| 控件 | 文本/样式 | 事件 | 作用 |
|---|---|---|---|
| TextBlock(20px 红) | "The program has exited abnormally" | — | 伪装异常提示标题（`ErrorWindow.xaml:12`） |
| TextBlock | "启动器已检测到程序异常关闭。……" | — | 说明文案（`:13`） |
| TextBlock ×2 | "退出代码："、"0xC0000001-STATUS_ACCESS_ERROR" | — | 假退出码（`:15-16`） |
| TextBlock ×2 | "Feedback Tracker"、"twitter 互动支持"（`#D5B474`、下划线、Hand） | — | 装饰链接，无 Click（`:18-19`） |
| Button | "关闭"（底条 `#f2f2f2`，520 宽右对齐） | `Click="Button_Click"` → `Application.Current.Shutdown()`（`ErrorWindow.xaml.cs:36-39`） | 放弃解锁直接退出 |
| Window | PreviewTextInput | `Window_PreviewTextInput`（`.cs:47-63`） | **解锁机制**：`inputText += e.Text` 累积全窗口键入，`GetMd5(inputText)==pwd` 时 `Global.verify=true`，按 isSetPwd 跳 MainWindow / VerifyPwdWindow（`:50-62`） |

> 界面伪装成"程序异常退出"报错框，实际是口令解锁门——键入隐藏口令即进入登录界面。此链路 A2 报告逐行核对一致（`docs/A2_校验口令解锁链核对.md` §2.3）。

---

## 4. VerifyPwdWindow —— 专属密码（首设 / 重置）

### 4.1 用途与打开时机

- **首设链**：ErrorWindow 命中且 `isSetPwd==false`（INI 无密钥）→ `new VerifyPwdWindow().Show()`（`ErrorWindow.xaml.cs:57-60`）。
- **重置链**：HomeWindow → OnlineWindows →"重置专属密码"→ `new VerifyPwdWindow{toLogin=false}.Show()`（`OnlineWindows.xaml.cs:40-46`）。

### 4.2 ASCII 线框图与控件（`VerifyPwdWindow.xaml:3-15`）

```
┌────── 标题: 专属密码 (400×190) ──────┐
│ Row0: 请输入您的专用解锁密码           │ (:9, Margin 50,20,50,5)
│       [ pwdInput  FontSize=24 高34 ] │ (:10, KeyUp=pwdInput_KeyUp)
│ Row1: [     立即保存  (高36)       ] │ (:14, Click=Button_Click)
└─────────────────────────────────────┘
```

| x:Name | 类型 | 文本 | 事件 / 绑定 |
|---|---|---|---|
| pwdInput | TextBox(FontSize24, 高34) | — | `KeyUp="pwdInput_KeyUp"`（Enter 键 `(int)e.Key==6` 触发 `_save`，`.cs:34-42`）；构造后 Dispatcher 延迟聚焦（`.cs:23-26`） |
| — | Button | 立即保存 | `Click="Button_Click"` → `_save()`（`.cs:29-32`） |

- `_save()` 逻辑（`VerifyPwdWindow.xaml.cs:44-66`）：空输入弹"请输入专属密钥"；`GetMd5(输入)==verifyDefaultPwd` 拒绝（"专属密钥不能与初始化密钥一致！"）；否则 MD5 写 INI `VERIFY/PWD`（`.cs:60`，**无条件写**）；`toLogin==true` 时回 MainWindow，最后自关。
- 无 ViewModel：纯 code-behind 窗口（不走 MVVM）。

---

## 5. HomeWindow —— 主界面（牌面监控 + 校验改牌）

### 5.1 用途与打开时机

登录成功后由 `LoginViewModel.Login` 创建（`LoginViewModel.cs:342`）；构造挂 `SizeChanged`（自适应牌格尺寸）、`Closing`（触发 `Logout()` 清定时器/登出 WS）（`HomeWindow.xaml.cs:19-33`）。

### 5.2 ASCII 线框图（按 XAML 实际结构 `HomeWindow.xaml:13-3851`）

```
┌────────────────────────────────────────────────────────────────────────────┐
│ Row0(高33) 工具条:                                                          │
│ [设置|联机配置][画框|画框预览][开始|开始游戏][结束|结束游戏][重置|重新开始]      │ (:25-54)
│ [改牌|校验发送][发送|立即发送]  (红条:网络不可以…)(网络延迟: 链接成功 / xx ms) │ (:55-185)
│                        右侧: TopStatusText 用户名 [修改密码][退出登录(红)]   │ (:187-199)
│ Row1(高1) ─────────────────────── 黑色分隔线 (:202) ─────────────────────── │
│ Row2: ┌──────────── TabControl(MainTabControl) ────────────┬── 右栏(宽340) ──┤
│       │ Tab「方位视图」:                                    │ #f3f3f3 牌选区  │
│       │  ┌ 东行(高=ChannelHeight) ────────────────────┐    │ 万1-9 │条11-19 │
│       │  │ 东名|在线●|运行●|序列号|[刷新] │ 上排牌列表  │    │       │        │
│       │  │                               │ 下排牌列表  │    │ 筒21-29│字31-37 │
│       │  ├─ 黑线 ──────────────────────────────────┤    │       │        │
│       │  ┌ 南行 ┐ 同上(NanReload)                    │    │ 花41-48│        │
│       │  ┌ 西行 ┐ 同上(XiReload)                    │    │                │
│       │  ┌ 北行 ┐ 同上(BeiReload)                   │    │ (发送中)██████  │
│       │ Tab「校验视图」: 5列×9行，左格牌名右格牌图按钮     │    │ [立即发送]     │
│       │  一筒│二筒…九筒 / 一条…九条 / 一万…九万          │    │ [√]严格校验数据 │
│       │  东 南 西 北 中 发 白 百搭 白皮 春夏秋冬 梅兰菊竹 X│    │ 上次发送时间…  │
│       │                                                    │    │ 发送总数…     │
│       └────────────────────────────────────────────────────┴────────────────┘
```

### 5.3 顶栏控件清单（`HomeWindow.xaml:24-199`）

| x:Name | 类型 | 2.0.15 文本（xcs）/ 2.0.14 文本 | Binding | 事件(handler) | 作用 |
|---|---|---|---|---|---|
| — | Button×7(宽80) | 设置/联机配置、画框/画框预览、开始/开始游戏、结束/结束游戏、重置/重新开始、改牌/校验发送、发送/立即发送 | `OffineSettingText/PreviewText/StartText/EndText/RestartText/ValidaSendText/SendText`（:27-63） | `Online_Click/Preview_Click/StartGames_Click/EndGames_Click/RestartGames_Click/ValidationSendData_Click/SendData_Click`（`HomeWindow.xaml.cs:49-102`） | 7 大功能入口；文案由 `initText()` 按 `app_name=="xcs"` 切短（`HomeViewModel.cs:1887-1899`） |
| — | Label | Content→`NotConnectInternet` | DataTrigger `''` → Padding 0；默认白字红底 `#FFD10B0B` Bold（:67-88） | 断网横幅，`CheckIsConnectInternetTimer` 每 3s 刷新文案（`HomeViewModel.cs:2610-2620`） |
| — | Label×2 | "  网络延迟：" + `WsContentStatus` | DataTrigger：重连中.../断开链接→`#FFFF0000` 红、链接成功→`#FF008000` 绿（:98-115） | WS 状态灯，`CheckWsContentTimer_Elapsed` 驱动（`HomeViewModel.cs:2579-2597`） |
| — | Label×3 | " / " + `DelayTime` + "ms" | DataTrigger `DelayTimeError` True→红/False→绿（:120-185） | 心跳往返毫秒，>300 置 error（`HomeViewModel.cs:2092-2100`） |
| — | Label | `TopStatusText` / `LoginResp.LoginUser.Username`（:188-197） | 状态文本（如"接受完成，等待下轮数据..."）+ 当前账号 |
| — | Button | 修改密码 | `Click="EditPwd_Click"`（`.cs:145-148`） | 弹 EditPwdWindow |
| — | Button | 退出登录（红底白字 `#FFFF0000`） | `Click="Logout_Click"`（`.cs:138-143`） | Logout() → 回登录窗 |

### 5.4 Tab「方位视图」结构与控件（`HomeWindow.xaml:210-1191`）

- 外层 Grid 9 行：4 个内容行高绑定 `ChannelHeight`（:213-235），行间 1px 黑 Border（:475、:1189 等）。
- 每个方位行 = 左 90px 信息栏 + 右 ScrollViewer：

| 元素 | Binding | 视觉逻辑 | 出处（东方行） |
|---|---|---|---|
| 方位名 Label | `Channels[0].DirectionName` | — | `:244-248` |
| 在线指示灯 Label(10×10, 字"1") | `Channels[0].Online` DataTrigger 1→`#FF008000` 绿 / 0→`#FFFF0000` 红 | 配 `OnlineStr`（"在线"/"未在线"）同色 | `:250-296` |
| 运行指示灯 Label(10×10) | `Channels[0].Run` 1→绿 / 0→`#666` | 配 `RunStr` 同色 | `:298-345` |
| 序列号 Label(FontSize10) | `Channels[0].Serial_number` | — | `:347-351` |
| 刷新 Button | — | `Click="DongReload_Click"`（南/西/北行分别为 `NanReload_Click:590`、`XiReload_Click:828`、`BeiReload_Click:1066`） | `:352` |
| 上排 ListBox | ItemsSource `Channels[0].TopMj`，SelectedItem→`SelectChannelMjResult` | 样式 ValidPage_ListBox，横向 VirtualizingStackPanel | `:360-415` |
| 下排 ListBox | ItemsSource `Channels[0].BottomMj` | 同上 | `:416-471` |

- 牌格 DataTemplate（`:380-413`）：`StackPanel` 宽绑定窗口 `ChannelMjWidth`，上下两个 Grid 高绑定 `ChannelMjHeight`：
  - 上格背景 `#FFF0F8FF`，Image `Stretch=Fill`，Source→`ResultImg`（识别结果牌面），Visibility→`ImgVis`；
  - 下格背景 `#FFFFFFFF`，Image Source→`UploadImg`（原图/上传牌面）；
  - 两格都挂右键菜单 `ContextMenu`（资源 `:6-10`，MenuItem"删除选中"→ `RemoveItem_Click` → `HomeViewModel.RemoveItem()`，`HomeWindow.xaml.cs:150-153`）。
- 尺寸自适应：`HomeWindow_SizeChanged` 计算 `ChannelHeight=(ActualHeight-92)/4-1`、`ChannelMjHeight=(ChannelHeight-25)/2/2`、`ChannelMjWidth=ChannelMjHeight+15`（`HomeWindow.xaml.cs:35-47`）。

### 5.5 Tab「校验视图」结构与控件（`HomeWindow.xaml:1192-3540`）

- 5 列 Grid（末列宽 100，`:1194-1200`），每列 9 行；每行 = 左 20px 宽的牌名 TextBlock + 右 ListBox（横向）。
- 牌名 TextBlock：白字黑体重，背景绑定 `Tong1StatusNumber` 等**数值着色**（如 `<Binding Path="Tong1StatusNumber"/>` 作 Background，`:1220-1223`）；字/花牌列固定灰底 `#696969`（百搭/白皮/春夏秋冬/梅兰菊竹/X，`:3000,3048,3111-3495`）。
- 每行 ListBox：ItemsSource→`Tong1 / … / Wan1 / DongFang / … / X`（39 个集合属性，`HomeViewModel.cs:579-1163`），ItemTemplate 用全局样式 `ValidPage_Mj_Item_Grid/_But/_Img`（`App.xaml:5-50`），按钮 Command 绑窗口 `DataContext.ValidMjClick`，CommandParameter 绑条目自身（`:1246-1250`）。
- 点牌 → `ValidMjClick` → `ExecutValidMj`：`ValidPageSelect` 取反 → 红底选中（`HomeViewModel.cs:1632-1650、3325-3328`）。
- 校验数据来源：切到本 Tab（SelectedIndex==1）时 `clacValidPageData()` 按 Result 分拣 39 组（`HomeWindow.xaml.cs:70-77`；`HomeViewModel.cs:2943-3057`）。

### 5.6 右栏（宽 340）控件清单（`HomeWindow.xaml:3544-3848`）

| 控件 | 数量/文本 | Binding/Command | 作用 |
|---|---|---|---|
| Button×42（5 列 StackPanel） | 万 1-9（CommandParameter 1-9）、条 11-19、筒 21-29、字 31-37（`/res/sucai/zi/1-7.png`）、花 41-48（`/res/sucai/hua/1-8.png`），每钮 66×66 牌图 | Command→`RightChangeCommand`（`HomeViewModel.cs:564、2710-2771`） | 点牌图给当前选中格/校验选中张赋值（改牌核心操作） |
| ProgressBar(高15) | 不定进度 | Visibility→`SendDataLoading`（:3822-3827） | 发送动画 |
| Button | "立即发送" | `Click="SendData_Click"`（:3828-3830） | 手动上传 |
| CheckBox | "严格校验数据" | `IsChecked→SendVlidateIsCheck`（:3831-3835） | 关闭后免校验直接发送（A4 B6） |
| Label×2 | `LastSendTimeStr` / `SendDataTotalStr` | （:3836-3845） | "上次发送时间：yyyy-MM-dd HH:mm:ss（手动/自动）"、发送统计（`HomeViewModel.cs:3430-3437`） |

### 5.7 HomeViewModel 属性对照（节选，全部 INPC）

| 属性 | 界面作用 | 出处 |
|---|---|---|
| TopStatusText | 顶栏状态文案（101/114/117 消息驱动，如"接受完成，等待下轮数据..."、“升降牌打开,等待洗牌中..."） | `HomeViewModel.cs:276-288、2046、2084` |
| WsContentStatus | WS 状态灯文字 | `:289-301` |
| OffineSettingText~SendText | 7 按钮文案 | `:302-392`；默认长文案字段 `:46-58` |
| DelayTime / DelayTimeError | 心跳毫秒/超标标志 | `:393-431` |
| SendDataLoading | 发送进度条可见性 | `:432-447` |
| NotConnectInternet | 断网横幅 | `:448-460` |
| Channels / UpChannels | 方位视图 4 通道 / 校验视图使用的数据通道 | `:474-486、487-` |
| Wan1..X、*StatusNumber | 校验视图 39 组集合与计数（背景色数值） | `:579-1630` |
| ValidMjClick | 校验视图点牌命令 | `:1632-1650` |
| SendVlidateIsCheck | 严格校验开关（默认 true，`:272`） | `:1729-1741` |
| RightChangeCommand | 右栏点牌命令 | `:564、2710` |

---

## 6. PreviewWindow —— 预览方位选择

### 6.1 用途与打开时机

主界面"画框（预览）"按钮 → `Preview_Click` → `new PreviewWindow(homeViewModel.IpAddr, PreviewSettingViewModel).ShowDialog()`（`HomeWindow.xaml.cs:54-58`）。

### 6.2 线框图与控件（`PreviewWindow.xaml:3-16`）

```
┌──── 标题: 选择预览窗口 (420×300) ────┐
│  [   东方  ]      [   南方  ]      │ 2×2 Grid，FontSize24 Bold
│  [   西方  ]      [   北方  ]      │ :12-15
└───────────────────────────────────┘
```

- 四按钮 Click 分别 `Button_Click/_1/_2/_3` → `sendMsg(1..4)`：按 `Global.devices` 找 Direction 匹配设备，调 `previewSettingViewModel.getServerData(device)` 打开 PreviewSettingWindow，关闭后 `endPreview()`；找不到弹"预览错误"（`PreviewWindow.xaml.cs:27-61`）。

---

## 7. PreviewSettingWindow —— 预览调参

### 7.1 用途与打开时机

由 PreviewWindow 选择方位后 `ShowDialog()`（`PreviewWindow.xaml.cs:55`）；进入前 `getServerData(device)` 已 `GET preview/start_preview/{id}` 启动预览（`PreviewSettingViewModel.cs:394`），WS msg_type=102 实时推帧。

### 7.2 ASCII 线框图（`PreviewSettingWindow.xaml:3-377`）

```
┌──── 标题: 窗口预览 (640×340) ────────────────────────────────────────┐
│ Col0(宽340):                │ Col1:                                 │
│  [320×240 预览图 ImgPath]   │ 行0: PreviewName  (红字)预览画面未上传  │
│  [Canvas 半透明覆盖:        │ 行1: TabControl                       │
│   红色矩形 Width/Height/X/Y] │  ├ 图形画框: 上边[- slider 0-240 +]   │
│  (DataStatus 时) 预览画面未上传│  │          左边[- slider 0-300 +]   │
│                             │  │          宽度[- slider 1-320 +]   │
│                             │  │          高度[- slider 1-50 +]    │
│                             │  ├ 图像质量: 图像旋转/灯光亮度/增益/曝光值│
│                             │  │   /色调/饱和度/对比度/二值数 各[-滑块+]│
│                             │  │   镜头帧率[60/70/80/90/180FPS]    │
│                             │  └ 电机配置: 电机转速[- slider 100-4900+]│
│                             │       电机转速设定值：N               │
│                             │ 行2: [保存参数] (SaveButContent)      │
└─────────────────────────────────────────────────────────────────────┘
```

### 7.3 控件清单表（关键项）

| 区 | 控件 | Binding/事件 | 范围 | 出处 |
|---|---|---|---|---|
| 左侧 | Image(320×240) | Source→`ImgPath` | — | `:9-13` |
| 左侧 | Canvas 覆盖 Grid 红块 `#FFFF0000`，Opacity .5 | Width/Height/Canvas.Top/Canvas.Left→`Width/Height/Y/X` | — | `:16-31` |
| 左侧 | Label "预览画面未上传"（红 Bold） | Visibility→`DataStatus` | — | `:33-38、51-54` |
| 顶 | Label | Content→`PreviewName`（"东方预览"等，`PreviewSettingViewModel.cs:362-376`） | — | `:46-50` |
| 图形画框 | 4 组 Label+Button(20)+Slider+Button | Value→`Y/X/Width/Height`；`Click="YSub/YAdd/XSub/XAdd/WidthSub/WidthAdd/HeightSub/HeightAdd"` | Y 0-240、X 0-300、Width 1-320、Height 1-50（+边界检查 `.cs:142-149`） | `:57-126` |
| 图像质量 | 7 组 Label(数值)+Button+Slider(ValueChanged=`Slider_ValueChanged`)+Button | `ImgRotation/CapBrightness/CapGain/CapExposure/CapHue/CapSaturation/CapContrast` 均 ±64；`CapExposure/CapHue/CapSaturation/CapContrast` ±500；`ImgMinHreshold` 0-255 | — | `:127-313` |
| 图像质量 | ComboBox `___CapFpsComboBox`(宽100) | 项 60/70/80/90/180FPS；`SelectionChanged="ComboBox_SelectionChanged"` → `CapFps` 值映射（60→idx0 … 180→idx4，`.cs:20-43`） | — | `:320-333` |
| 电机配置 | Slider+Button×2+Label | Value→`Motor`，`MotorSub/MotorAdd`；100-4900 | — | `:338-365` |
| 底 | Button | Content→`SaveButContent`（"保存参数"/"保存中..."），IsEnabled→`SaveButIsDisable`；`Click="SaveClick"` | — | `:367-375` |

### 7.4 交互流程

- 每个图像质量 +/− 按钮即改 VM 数值并立即 `onSaveHttp()`（`PreviewSettingWindow.xaml.cs:46-140`；`onSaveHttp` 为空方法，A2 报告 §三第 7 条注明）。
- 保存按钮 → `SaveClick` → `saveParams(alert)`：组 JSON `{top,left,right,bottom,brightness,motor,cap_*,img_rotation,img_min_hreshold,cap_fps}` → `POST preview/set_params_v2/{previewId}`（`PreviewSettingViewModel.cs:423-479`，端点在 `:468`）。
- 窗口关闭后由 PreviewWindow 调 `endPreview()` → `POST preview/end_preview/{previewId}`（`PreviewSettingViewModel.cs:408-421`）。
- msg_type=102 帧：`HomeViewModel.cs:1957-1980` 收帧 → Base64 落盘 cacheImg → `PreviewSettingViewModel.ImgPath` 刷新预览图（支持 `serial|base64` 帧格式，`:1964-1974`）。

---

## 8. OnlineWindows —— 联机配置

### 8.1 用途与打开时机

主界面"设置（联机配置）"按钮 → `Online_Click` → `new OnlineWindows().ShowDialog()`（`HomeWindow.xaml.cs:49-52`）；构造挂 `OnlineConfigViewModel`（`OnlineWindows.xaml.cs:14-18`）。

### 8.2 ASCII 线框图（`OnlineWindows.xaml:9-221`）

```
┌──── 标题: 联机配置 (450×340) ────────────────────────────────┐
│ 东方:[ ] 南方:[ ] 西方:[ ] 北方:[ ]  [保存(序号)]              │ :18-77
│ ───────────────── 0.5px 分隔线 :78 ──────────────────────── │
│ 数据发送: (•)自动发送 ( )手动发送        [保存(数据配置)]       │ :84-95、:96-154
│ 计数方式: (•)光控计数 ( )机头计数                             │ :101-112
│ 超时发送时间:[ ] 延时发送时间:[ ]                             │ :124-152
│ ───────────────── 分隔线 :155 ───────────────────────────── │
│ 当前WIFI配置:《ssid》密码:《pwd》                            │ :156-173
│ WIFI:[ ] 密码:[ ]                       [保存(WIFI)]         │ :174-215
│ ───────────────── 分隔线 :216 ───────────────────────────── │
│ [重启所有设备]  [重置专属密码]  (WrapPanel)                   │ :217-220
└─────────────────────────────────────────────────────────────┘
```

### 8.3 控件与绑定

| 区 | 控件 | Binding | 事件 | 端点（OnlineConfigViewModel.cs） |
|---|---|---|---|---|
| 设备序号 | TextBox×4 + Button | `ConfigNumber.East/South/West/North`；按钮 Content→`SaveConfigNumberButContent`、IsEnabled→`SaveConfigNumberButIsDisable` | `SaveConfigNumber_Click`（`.cs:20-23`） | `POST config/set_number`（`:211`）；数据来自 `GET config/get_config`（`:162`） |
| 数据发送 | RadioButton×2（同一组） | `DataConfig.Data_send`（自动/手动共用路径，Converter `RadioToBoolConverter` 资源 `:5`） | — | `POST config/set_data_config`（`:282`） |
| 计数方式 | RadioButton×2 | `DataConfig.Count_type`（光控/机头） | — | 同上 |
| 超时/延时 | TextBox×2 | `DataConfig.Timeout / DataConfig.Delay` | `SaveDataConfig_Click`（`.cs:25-28`） | 同上 |
| WIFI | Label 显示 + TextBox×2 | `WifiConfig.Ssid / WifiConfig.Pwd` | `SaveWifi_Click`（`.cs:30-33`） | `POST config/set_wifi`（`:318`）；`GET config/get_device_wifi_list`（`:186`） |
| 设备操作 | Button"重启所有设备" | — | `RestartDevice_Click`（`.cs:35-38`） | `GET zhuo/restart_device`（`:342`） |
| 密钥 | Button"重置专属密码" | — | `ResetVerifyPwd_Click` → VerifyPwdWindow(toLogin=false)（`.cs:40-46`） | 写本地 INI |

---

## 9. EditPwdWindow —— 修改账号密码

### 9.1 用途与打开时机

主界面右上"修改密码"→ `EditPwd_Click` → `new EditPwdWindow().ShowDialog()`（`HomeWindow.xaml.cs:145-148`）。

### 9.2 线框图与控件（`EditPwdWindow.xaml:3-129`）

```
┌── 标题: 修改密码 (450×300, FontSize13) ──┐
│  旧密码:   [ ipAddrInput  Binding OldPwd ]   │ 占位"请输入旧密码"
│  新密码:   [ userInput    Binding NewPwd ]   │ 占位"请输入新密码"
│  重复新密码:[ pwdInput    Binding ReNewPwd]  │ 占位"请输入重复新密码"
│  [ loginBut  Content=LoginButContent 高44 ] │ Click=SaveEditPwdBut_Click
└─────────────────────────────────────────┘
```

- 三个 TextBox（x:Name 复用登录窗命名 ipAddrInput/userInput/pwdInput，`:11,49,87`）+ VisualBrush 占位；提交按钮绑定 `LoginButContent`（默认"立即修改密码"，`EditPwdViewModel.cs:95`）与 `LoginButIsDisable`（`:119-125`）。
- 提交 → `SaveEditPwd()`：非空/一致性校验（`EditPwdViewModel.cs:108-122`）→ `POST account/edit/pwd`（`{"old_password","password"}`，`:126-131`）→ 成功后关旧 MainWindow、开新 MainWindow、弹"修改密码成功"（`EditPwdWindow.xaml.cs:20-30`）。

---

## 10. 全局功能界面映射总表

### 10.1 HTTP 端点全集（19 个，`grep "HttpReq." --include=*.cs` 全集）

统一封装：`http://{业务IP}:8081/`，`Authorization: Bearer {jwt}`；GET 自动加 `api/` 前缀、POST 不加（`distinguish.Common/HttpReq.cs:23-29,61`；`docs/逆向①_xcs电脑图传.md` §4）。

| # | 端点 | 方法 | 界面入口（按钮/时机） | 调用处 |
|---|---|---|---|---|
| 1 | `api/login/login/distinguish?app_name=xcs` | POST(直连 HttpClient) | 登录窗 loginBut | `LoginViewModel.cs:301` |
| 2 | `server/get_server_addr` | POST | 登录成功后自动（RSA 地址下发） | `LoginViewModel.cs:324` |
| 3 | `zhuo/start_games` | GET | 主界面「开始」按钮 | `HomeViewModel.cs:2886` |
| 4 | `zhuo/end_games` | GET | 主界面「结束」按钮 | `HomeViewModel.cs:2909` |
| 5 | `zhuo/zhuo_reset_games` | GET | 主界面「重置」按钮 | `HomeViewModel.cs:3066` |
| 6 | `zhuo/device_status_all` | GET | 自动：5 秒轮询 + 启动首查 | `HomeViewModel.cs:3100`（定时器 `:2773-2789`） |
| 7 | `zhuo/device_status/{deviceId}` | GET | 方位行「刷新」按钮（东/南/西/北） | `HomeViewModel.cs:3135`（分发 `.xaml.cs:104-136`） |
| 8 | `zhuo/add` | POST | 「发送/立即发送」按钮 + 收满自动发送 | `HomeViewModel.cs:3201` |
| 9 | `app/check_update?type=3&version={v}&app_name=xcs` | GET | 登录后版本检查（OTA） | `HomeViewModel.cs:3379` |
| 10 | `account/edit/pwd` | POST | EditPwdWindow 提交钮 | `EditPwdViewModel.cs:131` |
| 11 | `config/get_config` | GET | OnlineWindows 打开时 | `OnlineConfigViewModel.cs:162` |
| 12 | `config/get_device_wifi_list` | GET | OnlineWindows（WIFI 列表） | `OnlineConfigViewModel.cs:186` |
| 13 | `config/set_number` | POST | OnlineWindows 序号保存钮 | `OnlineConfigViewModel.cs:211` |
| 14 | `config/set_data_config` | POST | OnlineWindows 数据配置保存钮 | `OnlineConfigViewModel.cs:282` |
| 15 | `config/set_wifi` | POST | OnlineWindows WIFI 保存钮 | `OnlineConfigViewModel.cs:318` |
| 16 | `preview/start_preview/{id}` | GET | PreviewWindow 选方位后 | `PreviewSettingViewModel.cs:394` |
| 17 | `preview/set_params_v2/{id}` | POST | PreviewSettingWindow 保存钮 | `PreviewSettingViewModel.cs:468` |
| 18 | `preview/end_preview/{id}` | POST | PreviewSettingWindow 关闭后 | `PreviewSettingViewModel.cs:414` |
| 19 | `zhuo/restart_device` | GET | OnlineWindows「重启所有设备」 | `OnlineConfigViewModel.cs:342` |

### 10.2 WebSocket 帧（ws://{业务IP}:18889/，`NetManager.cs:62`、`HomeViewModel.cs:1871`）

协议帧 `Protocol{types,data_content,from,to,fp,qos,retry_count}`；类型常量 `Constant.cs:5-27`。连接后立即发登录帧（types=6，`getWebStockLoginData` `HomeViewModel.cs:1901-1913`），心跳每 5000ms（types=1，`:2538-2566`），收到 51 回包刷新 `DelayTime`（`:2089-2101`），收到 301 未登录自动重连（`:2102-2108`），断线每 3s 重连并置"重连中..."（`:2579-2597`）。

**业务消息（msg_type）在界面上的呈现效果：**

| msg_type | 界面呈现 | 代码 |
|---|---|---|
| 101 单牌识别图 | 按 `serial_number` 定位通道，`mahjong_number - DeleteOffset` 重映射落位（越界丢弃并打印日志）；偶数→BottomMj、奇数→TopMj 更新 `ResultImg/UploadImg`（图 URL 下载/base64）；收满自动 `SendData(autoSend:true)`，未满 `PlayReceiveSound()` 收牌音 | `HomeViewModel.cs:1951-1956、2111-2322`（重映射 `:2133-2139`） |
| 102 实时预览帧 | 写 cacheImg → `PreviewSettingViewModel.ImgPath` 刷新 PreviewSettingWindow 预览图；带 `|` 时按设备号过滤；同时熄灭"预览画面未上传"（DataStatus） | `:1957-1980` |
| 113 回合开始/运行 | 选参考通道（upChannels 首个非空），若首格已有上传图则 `upSend=false`（冻结改牌下发）；按 serial 置 `channel.Run`（运行灯变绿），清空该通道全部牌面；启动 acceptDataTimer（超时保护） | `:1982-2024`（2.0.14 该业务在 117，见 A4 B7） |
| 114 单设备收满 | 匹配 serial 的通道 `Run=0`（运行灯灰）；全部归零时停 acceptDataTimer、`TopStatusText="接受完成，等待下轮数据..."`、各通道 `DeleteOffset=0` | `:2025-2054` |
| 115 设备离线 | 匹配通道 `Online=0`、`OnlineStr="未在线"` → 在线灯/文字变红 | `:2055-2066` |
| 116 设备上线 | `Online=1`、"在线" → 变绿 | `:2067-2078` |
| 117 升降牌打开 | 停 acceptDataTimer、`TopStatusText="升降牌打开,等待洗牌中..."` | `:2079-2086` |

### 10.3 定时器全集（System.Timers.Timer）

| 定时器 | 周期 | 界面作用 | 出处 |
|---|---|---|---|
| keepaliveTimer | 5000ms（首次 1ms 触发后改 5s） | 发心跳帧，驱动 DelayTime 灯 | `HomeViewModel.cs:2538-2566` |
| checkWsContentTimer | 连接时 1000ms / 断开 3000ms | WsContentStatus 红绿灯 + 自动重连 | `:2568-2597` |
| checkIsConnectInternetTimer | 首次 1ms 后默认 | 断网红条横幅文字 | `:2599-2620` |
| deviceTimer | 5000ms | `SelectAllDeviceStatus()` 刷新在线状态 | `:2773-2789` |
| qosTimer | 10000ms | 清理 >60s 的 QoS 登记项 | `:2791-2811` |
| acceptDataTimer | 动态 | 101 接收超时保护（113 启动/114 停止） | `:2510-2536` |

---

## 11. 样式与视觉总表（XAML 资源 + res/ 素材）

| 类别 | 值 | 出处 |
|---|---|---|
| 登录背景 | `/res/login_bg.png`（整窗 ImageBrush） | `MainWindow.xaml:5` |
| 登录按钮贴图 | `/res/login_but.png`、`/res/login_but_ing.png`、`/res/login_reset.png` | `MainWindow.xaml:176-202` |
| 全局列表底色 | `#e3e3e3`（ValidPage_ListBox） | `App.xaml:56` |
| 牌格底色 | 识别格 `#FFF0F8FF`（AliceBlue）、原图格 `#FFFFFFFF` | `HomeWindow.xaml:386,399` |
| 状态绿/红 | `#FF008000` / `#FFFF0000`（在线、运行、WS、延时、校验选中） | `HomeWindow.xaml:259-267、98-115` 等 |
| 断网横幅 | 白字红底 `#FFD10B0B` Bold | `HomeWindow.xaml:74-77` |
| 退出登录钮 | 红底白字 `#FFFF0000` | `HomeWindow.xaml:199` |
| 分隔线 | `#FF000000` 1px Border | `HomeWindow.xaml:202,475` |
| 牌面素材 | `/res/wan/1-9.jpg`、`/res/tiao/1-9.jpg`、`/res/tong/1-9.jpg`、`/res/sucai/zi/1-7.png`、`/res/sucai/hua/1-8.png`（右栏 42 钮 + MjConfig 表 `HomeViewModel.cs:2822-2863`） | `HomeWindow.xaml:3563-3817` |
| 空位图 | `/res/bottom_bg.jpg`（初始底）、`/res/other/empty.jpg`（识别空）、`/res/other/*.jpg`（字/花牌 jpg：baida/bei/chun/dong/fa/ju/lan/mei/qiu/xi/xia/zhong/zhu/x/kuang） | `HomeViewModel.cs:2002、2145-2146`；`analysis/xcs-cs112/res/other/` |
| 音效 | `not_error.wav`（发送成功）、`error.wav`（校验失败）、`receive_sound.wav`（收牌） | `PlayNotError/PlayError/PlayReceiveSound` `HomeViewModel.cs:3307-3324`；文件在 `res/` |
| 字体 | 未显式设 FontFamily（系统默认），登录窗 FontSize=14、ErrorWindow 标题 20、登录钮 16 Bold | 各 XAML 根/控件属性 |

---

## 12. 忠实性说明

1. **来源边界**：本文所有窗口结构、控件、绑定、颜色、尺寸均逐行提取自反编译还原产物 `analysis/xcs-cs112/`（2.0.15）与 `analysis/distinguish-cs112/`（2.0.14）的 XAML/.cs 文件；XAML 由 BAML 反编译而来，`<!--Unknown connection ID: n-->` 为 BAML 连接点噪声，`//IL_*` 为 ILSpy 偏移注释，均无 UI 语义，已忽略。
2. **还原层与原始层一致性**：`docs/A2_校验口令解锁链核对.md` §三、`docs/A4_双版本2.0.14_vs_2.0.15差异报告.md` §二逐文件 diff 确认：还原工程的 XAML 绑定/事件与原始反编译层语义一致，业务差异仅 A4 所列 B0~B7（本文已将 B2 文案差异并入各表）。
3. **运行时渲染未验证**：本文档为静态 UI 定义提取，未运行 exe 截图比对；以下项目与真实渲染可能有差异——ImageBrush/DPI 缩放下的登录背景铺排、Window.Maximized 时 HomeWindow 实际布局（XAML 声明 1240×680 仅为设计值）、VisualBrush 占位符在 TextBox 有焦点时的表现、`#FFD10B0B` 等颜色经系统主题合成后的观感。
4. **噪声级差异**：`ErrorWindow` 中的"Feedback Tracker / twitter 互动支持" TextBlock 在 XAML 中无任何 Click 处理（`ErrorWindow.xaml:18-19`），按静态定义其为纯装饰；`PreviewSettingWindow` 的 `onSaveHttp()` 为空方法（`docs/A2_…核对.md` §三第 7 条），即质量参数 +/− 只改本地显示不即时上传。
5. **品牌差异边界**：两版 UI 定义除 B2 文案与图标集外完全一致；`cq_favicon.ico/cs.ico` 等多品牌图标"随资源打包、未见运行时切换代码"（A4 §三 R1）。本文 2.0.15 为基准描述，涉及 2.0.14 处已显式标注。
