# UI 设计② 游戏端 —— JavaFX FXML（9 个视图 + 2 个 CSS）

> 素材来源（全部为逆向还原产物，UI 定义逐行提取，无臆造）：
> - FXML：`game-src/quesheng/view/*.fxml`（9 个：LoginStageView / MainStageView / SettingStageView 三主窗 + Ai / CaiShengXuanZe / ChangePassword / DanZhangKeGang / HuaPaiXuanZe 弹窗 + guopai_tishi 过牌提示组件）
> - 样式：`game-src/quesheng/view/Login.css`、`game-src/quesheng/view/Main.css`
> - 控制器：`game-src/quesheng/controller/`（LoginController、MainController 7956 行、SettingStageController、StageController、BaseController、MainApp）+ `serve/`（规则插件）+ `customcontrole/`（16 个自绘控件）+ `dialog/`（5 个弹窗控制器）
> - 交叉参考：`docs/逆向③_游戏端.md`（架构）、`docs/逆向④_游戏端源码还原.md`（源码还原与端点全集）
> 行号引用格式 `文件路径:行号`。源码含 CFR 反编译噪声头注释（"Could not load the following classes…"），本文不引用其内容。

---

## 0. 信息架构总览

### 0.1 窗口关系图（ASCII）

```
quesheng.controller.MainApp.start()  (MainApp.java:28-40)
  └ StageController.loadStage ×3 预载:
      LoginView  ← LoginStageView.fxml  (LoginController)      ── 首个显示 (:39)
      IndexView  ← MainStageView.fxml   (MainController)       ── 主牌桌（登录成功后切换）
      SettingView← SettingStageView.fxml(SettingStageController)── 置顶模态，标题"规则设置" (:33-37)
                                │
 LoginView "登录" ──成功──▶ IndexView (goToMain, LoginController.java:160-166；unload LoginView)
                                │
 IndexView 顶栏/热键触发的弹层：                        Stage 属性（MainController.java:285-288）:
  ├ [规则]          → SettingView.show()               maximized(true) + TRANSPARENT + resizable
  ├ [指挥提示]      → 新 Stage ← Ai.fxml               (zhihuitishiConfig, MainController.java:4329)
  ├ [修改密码]      → 新 Stage ← ChangePassword.fxml   (:436)
  ├ [激活]          → jihuo()  (HTTP /api/user/updateSelf)
  ├ 财神选择        → caisheng_seting_popup ← CaiShengXuanZe.fxml (CaishengxuanzeImpl.java:60)
  ├ 单张可杠选择    → danzhangkegang_seting_popup ← DanZhangKeGang.fxml (DanzhangkegangImpl.java:46)
  ├ 花牌设置        → zixuanhuapai_seting_popup ← HuaPaiXuanZe.fxml (ZixuanhuapaiImpl.java:55)
  ├ 过牌提示浮层    → GuopaiTishi（fx:root 自绘组件加载 guopai_tishi.fxml，GuopaiTishi.java:26）
  ├ 通用文本提示    → BaseController.showTextDialog（代码动态 VBox+Label+按钮，BaseController.java:36-66）
  ├ 登录/改密进度   → BaseController.showMyProgressDialog（ProgressIndicator 模态，:68-86）
  └ 纠正换牌面板    → JiuZhengHuanShouPaiPanel（自绘，MainController.java:957 附近）
 窗口关闭：out_btn / MainApp.stop() → IM 登出 + System.exit(0)（MainApp.java:42-48、MainController.java:895-900）
```

### 0.2 分辨率 / 尺寸总表（FXML 根节点声明）

| 视图 | pref 尺寸 | 其它布局约束 | 出处 |
|---|---|---|---|
| LoginStageView.fxml | 557 × 488（minWidth=844、minHeight=488） | AnchorPane id=LoginAnchorPane，styleClass=background，背景 ImageView 844×488 铺满 | `LoginStageView.fxml:56-62` |
| MainStageView.fxml | 1548 × 631 | maxHeight/maxWidth/minHeight/minWidth=-Infinity；运行时 `setMaximized(true)` + `StageStyle.TRANSPARENT` + resizable | `MainStageView.fxml:17`；`MainController.java:285-288` |
| SettingStageView.fxml | 1300 × 700 | 三列 VBox（600 + 600 + 200），运行时模态、置顶、不可缩放、标题"规则设置" | `SettingStageView.fxml:12`；`MainApp.java:33-37` |
| ChangePassword.fxml | 600 × 400 | 绝对坐标布局 | `ChangePassword.fxml:8` |
| Ai.fxml | 500 × 300 | 绝对坐标 | `Ai.fxml:9` |
| CaiShengXuanZe.fxml / DanZhangKeGang.fxml / HuaPaiXuanZe.fxml | 未声明（由内容撑开） | VBox/HBox 流式 | 各 :11 |
| guopai_tishi.fxml | 300 × 200 | fx:root 自定义组件，白边框透明底 | `guopai_tishi.fxml:7` |
| 运行时牌张尺寸 | `MJ_WIDTH=(SCREEN_HEIGHT-50)/(牌数+4)`（≤13 张时 /17）、`MJ_HEIGHT=MJ_WIDTH*1.37`；静态缺省 62×100 | 屏幕尺寸取 `Screen.getPrimary()` | `MainController.java:400-409`；`Config.java:551-558` |

### 0.4 FXML 根属性总表（controller / stylesheets / styleClass 逐个提取）

| FXML | fx:controller | stylesheets | id / styleClass / style |
|---|---|---|---|
| LoginStageView.fxml | quesheng.controller.LoginController（:56） | @Login.css（:104-106） | id=LoginAnchorPane，styleClass=background |
| MainStageView.fxml | quesheng.controller.MainController（:17） | @Main.css（:115-117） | 无 id/styleClass |
| SettingStageView.fxml | quesheng.controller.SettingStageController（:12） | （无 stylesheets 节点） | id=CameraSettingAnchorPane |
| Ai.fxml | quesheng.dialog.AiDialog（:9） | @Main.css（:10-12） | — |
| CaiShengXuanZe.fxml | quesheng.dialog.CaiShengXuanZeDialog（:11） | @Main.css（:46-48） | — |
| DanZhangKeGang.fxml | quesheng.dialog.DanZhangKeGangDialog（:11） | @Main.css（:46-48） | — |
| HuaPaiXuanZe.fxml | quesheng.dialog.HuaPaiXuanZeDialog（:11） | @Main.css（:46-48） | — |
| ChangePassword.fxml | quesheng.dialog.ChangePasswordDialog（:8） | （无） | — |
| guopai_tishi.fxml | （无，fx:root 由宿主任 controller，`GuopaiTishi.java:27-28`） | （无） | style 内联（透明底白框） |

---

## 1. LoginStageView —— 登录窗

### 1.1 用途与打开时机

- 进程首个显示的窗口（`MainApp.java:39` `setStage("LoginView")`）；控制器 `quesheng.controller.LoginController`（`LoginStageView.fxml:56`），stylesheets `@Login.css`（`:104-106`）。
- 登录成功 `goToMain()` 切 IndexView 并 unload LoginView（`LoginController.java:160-166`）。

### 1.2 ASCII 线框图（按 FXML 绝对坐标/锚点，`LoginStageView.fxml:56-107`）

```
┌────────────────── 557×488（背景 login_background.png 铺满）──────────────────┐
│ anchorPane2(top-segment, 上 0~300)                                           │
│   ┌ VBox 居中(左29 右40) ────────────────────────────────────────┐           │
│   │ Label: 魔术道具,请勿用作其它用途 (白字)                         │           │
│   │ ┌ AnchorPane(180高) ────────────────────────────┐            │           │
│   │ │                        [delete ✕] (742,89)     │            │           │
│   │ │              口令  label_ip (538,15)            │            │           │
│   │ │              [ipchose TextField 204宽] (536,30) │            │           │
│   │ │              用户名 (538,61)                    │            │           │
│   │ │              [userId 可编辑 ComboBox] (538,90)  │            │           │
│   │ │              密码 (544,136)                     │            │           │
│   │ │              [password PasswordField] (538,155) │            │           │
│   │ └────────────────────────────────────────────────┘            │           │
│   └───────────────────────────────────────────────────────────────┘           │
│  [errorMessage Label]（600,266 处 VBox 顶）                                   │
│  [remeber_password ☑记住密码]                                                 │
│  [ 登       录 ] (567,309 宽204, 默认按钮, onAction=#processLogin)             │
│  [ 重置口令   ] (567,339 宽204)                                                │
└──────────────────────────────────────────────────────────────────────────────┘
```

### 1.3 控件清单表（fx:id / 类型 / 文本 / 事件）

| fx:id | 类型 | 显示文本/提示 | 事件/绑定 | 出处 |
|---|---|---|---|---|
| delete | ImageView(24×25) | image\delete.png | `setOnMouseClicked` 删除记住的账号（`LoginController.java:151-157`） | `:70-74` |
| label_ip | Label | "口令"（textFill #e8e5e5） | isFirstLogin=false 时隐藏 | `:76`；`.java:120-123` |
| ipchose | TextField(204×22) | — | 存 DES 加密 choseIp，回显 Base64（`.java:124-135`） | `:77` |
| userId | ComboBox(editable) | 历史账号下拉 | 选中项回填密码（`.java:146-150`） | `:82` |
| password | PasswordField(204×23) | — | focusTraversable=false | `:81` |
| login | Button | "登       录"（内联样式 `-fx-background-radius:4;-fx-background-color:#097299;-fx-text-fill:white;-fx-font-size:20px`） | `onAction="#processLogin"`，defaultButton=true | `:89`；`.java:212-235` |
| chongzhi_ip | Button | "重置口令"（同款蓝底样式） | 确认框后清 choseIp、显示口令行（`.java:106-119`） | `:90` |
| errorMessage | Label | （空，#errorMessage CSS 白字） | processLogin 校验失败写入"口令不能为空"/"用户名或密码不能为空"（`.java:216,230`） | `:95`；`Login.css:11-12` |
| remeber_password | CheckBox | "记住密码"（textFill #fbfbfb） | 勾选时保存 DES(用户名/密码) 到 sconfig.users（`.java:195-201`） | `:98` |
| — | Label | "魔术道具,请勿用作其它用途"（白字） | — | `:67` |

### 1.4 控制器接线（LoginController.initialize，`.java:100-158`）

1. `ObjectKit.getObject(Config.config_file_url)` 反序列化 `./sconfig.config` → `SystemConfig`，null 则新建（`:101-104`）。
2. `login.requestFocus()`（`:105`）。
3. chongzhi_ip 确认框（标题"重置口令"）→ 清 choseIp/isFirstLogin、回写文件、恢复口令行可见（`:106-119`）。
4. 非 FirstLogin 隐藏口令输入（`:120-123`）；choseIp 解密回显（`:124-135`）。
5. users 非空 → 勾上记住密码、填充 userId 下拉与密码框（`:136-145`）。
6. `processLogin`：口令非空校验 → Base64 解码口令文本（兼容 Base64(host) 输入）→ `DESKit.Encrypt` 存 choseIp → `Config.initialize(null)`（拉 `/api/mj/getConfigs` 牌图）→ 账密非空校验 → `showMyProgressDialog("正在登录")` → `userLogging`（`:212-235`）。
7. `userLogging`：`POST http://{choseIp解密}:8081/api/login/login`（form：ajax/deviceid/lastloginversion/devicetype=3/password/username/loginfield/type/version；`docs/逆向④_游戏端源码还原.md` §6.1）→ 成功存 jwt/User → `goToMain()`。

---

## 2. MainStageView —— 主牌桌（核心界面）

### 2.1 用途与打开时机

登录成功后显示（`LoginController.java:163`）；控制器 `quesheng.controller.MainController`（`MainStageView.fxml:17`，7956 行控制器）；stylesheets `@Main.css`（`:115-117`）。`setStageController` 中设为最大化、透明无边框、可缩放，并在 OnShown 做激活检查/自定义背景/检测轮询/OTA/IM 登录（`MainController.java:282-335`）。

### 2.2 ASCII 线框图（`MainStageView.fxml:18-118` + 运行时自绘面板）

```
┌──────────────── 顶栏 ScrollPane(高30, 可横滚) ────────────────────┐ ┌────┐
│ [指挥提示][规则][旋转][过牌▼][对调▼][栋▼][张▼][栋半▼][买马▼(无东南西北)]│ │ _  │ ← min_btn
│ [激活][获取][分析][测试▼(108/136/144/72四方/72三方/112/广西152/90张)] │ │ □  │ ← max_btn(图)
│ [撤销][重选庄][纠正换牌(红底)][还原][结束][修改密码][重置软件（清空缓存）]│ │ X  │ ← out_btn(红字)
│        ……右侧计分: 牌总数:0 东:0 南:0 西:0 北:0                     │ └────┘
├──────────────────────────────────────────────────────────────────┤
│ main_panel (AnchorPane，上锚30，运行时填自定义背景色)                  │
│   ┌──────────────────────────────────────────────┐                │
│   │            TopPanel (direction=2, 对家)        │                │
│   │ LeftPanel                              RightPanel              │
│   │ (direction=3,左手)   ZhuangPanel(中央)  (direction=1,右手)      │
│   │            BottomPanel (direction=0, 自己)      │                │
│   │   + YuceYinaPanel(预测/已拿汇总浮层) + yichu_pane(已出牌 FlowPane)│
│   │   + kech_list_panel(可吃列表 VBox)              │                │
│   └──────────────────────────────────────────────┘                │
└──────────────────────────────────────────────────────────────────┘
```

### 2.3 顶栏控件清单表（fx:id / 类型 / 文本 / 事件）

| fx:id | 类型 | 文本 | 事件 / 行为 | 出处 |
|---|---|---|---|---|
| — | Button | 指挥提示 | `onAction="#zhihuitishiConfig"` → Ai 弹窗（`MainController.java:4329-4352`） | `MainStageView.fxml:24` |
| guize_btn | Button | 规则 | `setOnAction` → SettingView.show()，关闭时回写配置并重算牌宽（`.java:837-852`） | `:25` |
| xuanzhuan_btn | Button | 旋转 | 四方位 direction 轮转并重绘（`.java:879-893`） | `:26` |
| guo_combobox | ComboBox(80) | ------/过张/循环过张/过栋/跨方位过栋/单张对调/合并牌 | valueProperty 分发 `guozhang/xunhuanguozhang/guodong/kuafangweiguodong/danzhangduidiao/hebingpai`（`.java:475-506`） | `:27` |
| duidiao_combobox | ComboBox(70) | 对调/东/南/西/北 | 对调指定方位的暗牌奇偶位（`.java:507-570`） | `:28` |
| dong_combobox | ComboBox(60) | 栋,±1..±9 | 整体移栋换向（`.java:571-655`） | `:29` |
| zhang_combobox | ComboBox(60) | 张,±1..±9 | 单张位移（`.java:657` 起） | `:30` |
| dongban_combobox | ComboBox(70) | 栋半,±1..±3 | 半栋位移（`.java:719-721`） | `:31` |
| — | MenuButton | 买马 | MenuItem 无/东/南/西/北 `onAction=#maimawu/#maimadong/#maimanan/#maimaxi/#maimabei`（`.java:4447-4520` 附近，加 `.maima` 高亮样式） | `:32-40` |
| jihuo_btn | Button | 激活 | `jihuo()`（HTTP `/api/user/updateSelf`）；已激活时 OnShown 隐藏（`.java:290-292`） | `:41` |
| huoqu_btn | Button | 获取 | 未激活弹"请先激活"；否则 `huoqu()` → `POST /api/yiZhuo/get` 拉牌局重绘（`.java:855-862、3895-3990`） | `:42` |
| fenxi_btn | Button | 分析 | 未激活弹"请先激活"；否则结束当前局、取上一把为暗牌、`begingamge()`（`.java:864-877`） | `:43` |
| — | MenuButton | 测试 | MenuItem `#ceshi108/#ceshi136/#ceshi144/#ceshi72sifeng/#ceshi72sanfeng/#ceshi112/#ceshiguangxi152/#ceshi90`（各自 `jieshu()+Config.xipai(n)+begingamge()`，`.java:4354-4425`） | `:44-55` |
| chexiao_btn | Button | 撤销 | `chexiao()`（`.java:852`） | `:56` |
| chongxuanzhuang_btn | Button | 重选庄 | `huanyuan()`（`.java:474`） | `:57` |
| jiuzhenghuanpai_btn | Button（内联 `-fx-background-color: red;`） | 纠正换牌 | 换牌后才可用；进入纠正模式：拿牌图点击标红/还原，弹 JiuZhengHuanShouPaiPanel（`.java:938-1000` 附近） | `:58` |
| huanyuan_btn | Button | 还原 | `huanyuan()`（`.java:853`） | `:59` |
| jieshu_btn | Button | 结束 | `jieshu()` 清桌（`.java:854`） | `:60` |
| changepassword_btn | Button | 修改密码 | 新 Stage 加载 ChangePassword.fxml，标题"修改密码"+用户名（`.java:429-448`） | `:61` |
| clean_btn | Button | 重置软件（清空缓存） | 确认框"重置软件"→清 choseIp/users 回写，2 秒后 `System.exit(0)`（`.java:449-473`） | `:62` |
| pai_count / dong_count / nan_count / xi_count / bei_count | Label ×5 | "0"（前缀 Label 牌总数:/东:/南:/西:/北:） | 识别结果计数（MainController 更新） | `:68-78` |
| min_btn | Button | "_"（Tooltip 最小化，HAND 光标） | `setIconified(true)`（`.java:901`） | `:87-94` |
| max_btn | ImageView(25×25, max.png, accessibleRole=BUTTON) | — | 切换最大化（`.java:393-399`） | `:95-102` |
| out_btn | Button | "X"（textFill #e80808，Tooltip 退出游戏，CLOSED_HAND） | 发 IM 登出 + `System.exit(0)`（`.java:895-900`） | `:103-110` |
| main_panel | AnchorPane | — | 四方位/庄家/预测/出牌区容器；父节点右键按 `shubiaoyouji` 配置映射"撤销/摸牌"（`.java:902-925`） | `:113` |

### 2.4 主牌局界面布局（四方位 / 牌墙 / 计分 / 提示）

自绘面板体系（`quesheng.customcontrole`，16 类）在 `initialize()` 中创建并装入 main_panel：

| 面板 | direction | 内容/子区 | 出处 |
|---|---|---|---|
| BottomPanel（自己） | 0 | VBox(yinaPanel 已拿 + houpaiPanel) + caozuoPanel；houpaiPanel = yucePanel(预测) + hupaiPanel(胡牌) + quePanel(缺门) | `BottomPanel.java:60-90` |
| RightPanel | 1 | 同构 VBox 纵排（`RightPanel.java:60`） | — |
| TopPanel（对家） | 2 | 同构，牌张倒转（`TopPanel.java:60-62`） | — |
| LeftPanel | 3 | 同构（`LeftPanel.java:60-62`） | — |
| MjPanel（抽象基类） | — | 操作提示图标：hu_text=hu.png、mo_text=mo.png、pang_text=peng.png、gang_text=gang.png、chi_text=Label"吃"，统一加 `caozuo_tishi` 样式（红字手型） | `MjPanel.java:38-42`；`MainController.java:422-428` |
| ZhuangPanel（庄家/中央） | — | center.png 中心图 + 东/南/西/北 方位图（dong/nan/xi/bei.png）+ 四个"缺" Label（缺门标记） | `ZhuangPanel.java:43-61` |
| YuceYinaPanel | — | wan/tiao/tong 三个 HBox + 东/南/西/北 汇总 Label，预测/已拿汇总浮层 | `YuceYinaPanel.java:42-51` |
| yichu_pane（已出牌） | — | static FlowPane，出牌依次入池 | `MainController.java:249`（`children.add` `:4205、4937`） |
| kech_list_panel | — | static VBox，可吃/可碰牌列表浮层 | `MainController.java:251`（TopPanel 定位 `:1365-1366`） |
| 庄家标记 | — | bottom_zhuang_label：黄底圆角黑字华文行楷 40px"庄"，叠在 BottomPanel 上 | `MainController.java:831-836` |
| 过牌标记 | — | 出牌/循环过牌在牌上叠 guopai_guozhang.png / guopai_xunhuan.png（按方位旋转 0/±90/180°） | `MainController.java:1321-2244` |
| 听牌提示 | — | ting.png / all_hu.png 图标叠放 | `LeftPanel.java:2062、2084` |
| 打牌方向 | — | dapaifangxiang.png 指示图 | `LeftPanel.java:135` |

牌张渲染：每张牌 = `MjHBoxLeftControl/MjHBoxRightControl/MjVBoxBottomControl/MjVBoxTopControl`（内含单 ImageView，按方位 `rotate90DX` 等旋转，财神蓝罩 `coverImage(…,LANSE)`，单张杠 1/3 缩小成 3×n 块，`LeftPanel.java:73-125`）。

### 2.5 MainController.initialize 接线摘要（`.java:392-1000`）

1. max_btn 最大化切换（`:393-399`）；读主屏尺寸并按牌数计算 MJ_WIDTH/HEIGHT 系列（`:400-409`）。
2. new TopPanel/BottomPanel/LeftPanel/RightPanel/ZhuangPanel；directions 顺序 bottom→right→top→left（`:410-421`）。
3. 五个操作图标统一加 `caozuo_tishi` 样式类（`:422-428`）。
4. 顶栏按钮接线（见 2.3 表）。
5. 右键 main_panel：`shubiaoyouji=="撤销"`→chexiao；`=="摸牌"` 且恰有一个 mo=1 方位→模拟点击摸牌（`:902-925`）。
6. OnShown：激活检查、自定义背景色填充、`openJiance()`（10 秒轮询）、OTA 线程（`POST /api/config/GetLastVersion`，version=dapai64|dapai32）、默认密码检测（`sha256(salt+"******")==password` → 弹"密码是默认密码，请尽快修改"）、`doZhuoLoginImpl()` IM 登录（`:289-335`）。
7. `doZhuoLoginImpl`：`SendLoginDataAsync("<user_id>_2", jwt, "<user_id>")` 登录 MobileIMSDK（`:7811-7819`）；IM 收 typeu==1 推送 → `jiance()` 拉牌（`docs/逆向④_游戏端源码还原.md` §3.4、§6.2）。

### 2.6 MainController 主窗 UI 字段与弹窗 Stage 清单

| 字段（static） | 类型 | UI 角色 | 出处 |
|---|---|---|---|
| yuceYinaPanel | YuceYinaPanel | 预测/已拿汇总浮层（display 时 add 到 main_panel） | `MainController.java:247、5266-5272` |
| yichu_pane | FlowPane | 已出牌池 | `:249` |
| kech_list_panel | VBox | 可吃/可碰候选列表浮层 | `:251`（定位 `:1365-1366`） |
| directions / directions_temp | List\<MjPanelInterface\> | 四方位面板（bottom/right/top/left 顺序）与旋转备份 | `:271-272、418-421` |
| caisheng_seting_popup | Stage | 财神选择弹窗 | `CaishengxuanzeImpl.java:54` |
| danzhangkegang_seting_popup | Stage | 单张可杠弹窗 | `DanzhangkegangImpl.java:40` |
| zixuanhuapai_seting_popup | Stage | 花牌设置弹窗 | `ZixuanhuapaiImpl.java:49` |
| static_main_panel | AnchorPane | main_panel 的静态引用（供自绘面板添加子节点） | `MainController.java:417` |
| bottom_zhuang_label | Label | 庄家标记（叠于 BottomPanel） | `:831-836` |
| dapaifangx / zhuangjia | int | 出牌方位 / 庄家索引（驱动 ZhuangPanel 与方向图） | 重置点 `:4239-4240、4314-4315` |

弹窗 Stage 公共样式（三个选择弹窗一致，均 `CaishengxuanzeImpl.java:49-55` 同款）：logo 图标 + `setAlwaysOnTop(true)` + `APPLICATION_MODAL` + `resizable=false`。

### 2.7 全局热键（JIntellitype 注册点）

- 注册/注销：IndexView 获得焦点时注册 `Config.getKey2KeycodeMapping()` 全部键 + ESC(27)；失焦注销（`MainController.java:336-348`）。
- 键名→虚拟键码映射：空格键=32、a~z=65~90、0~9=48~57（`Config.java:497-537`）。
- 监听分发（`MainController.java:349-389`）：

| 热键（配置项名） | 触发动作 |
|---|---|
| 27（ESC，固定） | `hebingpai()` 合并牌 |
| `guopai`（过牌） | `guozhang()` 过张 |
| `xunhuanguopai`（循环过牌） | `xunhuanguozhang()` |
| `guodong`（过栋） | `guodong()` |
| `mopai`（摸牌，默认"空格键"） | 找 mo==1 方位，模拟点击其 mo_text |
| `chexiao`（撤销，默认"z"） | `chexiao()` |
| `gangpai`（杠牌） | 找 gang 属性方位模拟点击 gang_text |
| `pangpai_kjj`（碰牌） | 找 peng 属性方位模拟点击 pang_text |

- 热键在 SettingView"快捷方式"区用 ComboBox 选择（键名列表来自同一映射，`SettingStageController.java:940-971`）；"强制退出：双击预测牌 / 调整大小：双击空白处"为 FXML 静态说明文字（`SettingStageView.fxml:237-240`）。

---

## 3. SettingStageView —— 规则设置窗

### 3.1 用途与打开时机

`MainApp` 预载（`MainApp.java:32-37`：置顶 + APPLICATION_MODAL + 不可缩放 + 标题"规则设置"）；由主界面 [规则] 按钮显示（`MainController.java:838-839`）；控制器 `quesheng.controller.SettingStageController`（`SettingStageView.fxml:12`）。关闭时（guize_btn 的 onCloseRequest）清胡牌缓存、回写 sconfig、按新牌数重算牌宽（`MainController.java:840-851`）。

### 3.2 ASCII 线框图（三列 VBox，`SettingStageView.fxml:14-255`）

```
┌─ 左列(600): ──────────────────┬─ 中列(600): ────────────────┬─ 右列(200): ──────────┐
│ 码牌: (•)两层 ☐摆三层(144)     │ 打牌: ☐碰牌 ☐吃牌 ☐单张可吃… │ 快捷方式:              │
│   ☐单张(栋)在后 ☐逐层码牌      │  ☐暗杠 ☐明杠 ☐单张可杠 …    │  获取数据/过牌/循环过牌/ │
│   (•)三层 ☐136三层 ☐摆四层     │  杠牌顺序[后杠牌▼]           │  过栋/看出牌/摸牌/吃牌/  │
│   ☐过山车正洗反推              │ 胡牌: ☐只限自摸 ☐抢杠胡 …    │  杠牌/碰牌/撤销/重选庄/  │
│ 启牌: 玩家牌数[13▼] 跳牌[隔跳▼] │ 财神: ☐自选财神 ☐后财神…    │  鼠标右击 各键名▼       │
│   每手张数[4▼] 预测牌[Spinner] │  双财神[▼] 代替财神牌[▼] …   │  强制退出: 双击预测牌    │
│   玩家人数[4▼] 换手牌[0▼] …    │ 买马: ☐先买马后定庄          │  调整大小: 双击空白处    │
│ 花牌: 杠法[285宽▼] ☐自选花牌…  │                             │ 地方玩法: ☐广西152 …    │
│ 扣牌: ☐第一手 ☐第二手 ☐第三手… │                             │ "严禁用于赌博等非法用途" │
│ 其它: 界面布局[▼] 背景[ColorPicker] 出牌显示[▼] …            │                       │
└───────────────────────────────┴─────────────────────────────┴───────────────────────┘
```

### 3.3 控件清单表（按区归组，fx:id 全部列出于 FXML）

**码牌区（`:16-28`）**

| fx:id | 类型 | 文本 | 备注 |
|---|---|---|---|
| liangceng_radioBtn / sanceng_radioBtn | RadioButton | 两层 / 三层 | ToggleGroup，userData 2/3 → `mapaicengshu`，联动 `mapaifangshi`（`SettingStageController.java:352-373`） |
| baisanceng_144 | CheckBox | 摆三层(144) | 选中 → mapaifangshi=5（`.java:388-401`） |
| _136_sanceng_danzhangzaiqian | CheckBox | 136三层 | 选中 → mapaifangshi=4（`.java:374-387`） |
| danzhangdongzaihou / zhucengmapai / baisiceng / guoshanchediaohuan | CheckBox | 单张(栋)在后 / 逐层码牌 / 摆四层(头+尾=六栋) / 过山车正洗反推 | 前三者 disable="true" |

**启牌区（`:29-68`）**：`wanjiapaishu` ComboBox（4/7/10/13/16/19/22，`.java:406-409`）、`tiaopai`（平跳/隔跳/上下跳/不跳牌/庄家连续三张/每家连续两张，`.java:416-423`）、`meishouzhangshu`、`yucepai` Spinner、`niezhuapai`、`wanjiarenshu`（2/3/4，`.java:430`）、`huanshoupai`（0-4，`.java:411-415`）、`zhuapaihouxuanzhuang`、`shangxiangzhupai`(disabled)、`sanceng_liangdongsizhang`/`errenzhuaerfangweipai`/`qutouzhang`(disabled)、`disancengzuowanjiapai`、`meijuchetiaopai`（FXML 文本"每局设跳牌"）、`bugoudongyizhiweibu`/`_20zhangzhuangjiaxianzhuangsanzhangzaizhuayizhang`(disabled)、`butiaopaizhangjialianxu14xian13`、`pingna`(disabled)、`nanbeihuhuandongdexiajiashibei`(disabled)、`zijiazuosandunyousandun`、`qishourenyiliangdong`、`renyiandongnapai`、`xianzhuan8houzhuan4`、`meijuchongxuanquefang`、`niezhuanimo`、`zhuapai_123`、`zhuapai321`、`sicengsandun`。

**花牌区（`:69-84`）**：`gangfa` ComboBox（宽 285，四项"各方杠完再打牌,补起花牌立即杠"等，`.java:763-768`）、`zixuanhuapai`（自选花牌(非花牌当花)）、`anhuiganghua`/`shoupaibuxianshihuapai`/`anqingmajiangdahua`(disabled)、`yucepaibuxianshihuapai`、`huapaikepang_foushanmajiang`、`ganghua` ComboBox（后杠牌/顺杠牌/随意杠，`.java:758-762`）、`huapaidaiticaishengpai_foushanmajiang`（花牌代替财神牌(佛山140)）。

**扣牌区（`:85-94`）**：`diyishou`/`diershou`/`disanshou`(disabled)、`zuihouyizhang`、`zixuankoupai`。

**其它区（`:95-108`）**：`jiemianbuju`(disabled)、`zidingyibeijingyanse` ColorPicker（promptText"自定义背景颜色"；初值取 `systemConfig.zidingyibeijingyanse[3]`，`.java:794-796`）、`shengyingtishi`(disabled)、`chupaixianshi`（出牌顺序/花色，`.java:768-788`）、`zidongjietuo`（FXML 实为 `zidongjietu`"自动截图"，disabled）、`jianpancaozuomoshi`/`zhuomianbuxuanzhuan`(disabled)。

**打牌区（`:112-146`）**：`pangpai`、`chipai`、`danzhuangkechi`(disabled)、`chisanfangpai`、`gangshouzhongpai`、`angang`、`minggang`、`danzhangkegang`、`koupai`(disabled)、`dongnanxibeikechikegang`、`zipaifengkai`/`renyisangedanzhangzipai`(disabled)、`leqing`、`dongnanxibeizhongfabaigang`、`dongnanxibeizhongfabaigangqingdao`、`dongnanxibeizhongfabaishunzi`、`pangjiang`/`yaojiyitongdangfengpai`/`wugezipaigang`/`zhucengnapai`/`yingchuanbufeng`(disabled)、`yaojiyitongdangzipai`、`yaojiyitongdangzipaifengpai`、`rengyijiu`、`yaojiugang`、`yaojiugangshengyangbusangehuosige`(disabled)、`gangpaishunxu` ComboBox（后杠牌/顺杠牌/随意杠，`.java:753-757`）、`baoliupai`、`fengpaizipaisanzhangkegang`。

**胡牌区（`:147-166`）**：`zhixianzimo`、`qiangganghu`、`erwubajiang`、`queyimeng`、`xueliuchenghe`、`hupaikepang`、`hupaikegang`、`fangpaoxiajia`(disabled)、`yipaoduoxiang`、`hupaikemo`、`buchibupangdapaitishi`(disabled)、`lanpai`、`xiaoqidui`、`bixuyou19zipaifengpairenyiyizhang`、`pengpaihuozhezhongfabaidui`。

**财神区（`:167-199`）**：`zixuancaisheng`、`houcaisheng`、`shunmocaisheng`、`renyicaisheng`、`shengjicaisheng`、`zipaifengpaifenkaishengji`、`shuangcaisheng`+`shangcaisheng_combox`、`zhongjianpaizuohua`/`shengjicaishengshihuapaijixu`/`budaiticaishengzipai`/`buhuahoumocaisheng`/`fankaidcaishengpaidanghua`/`fankaidcaishengpaifangzuihouweizhi`/`fankaishihuapaijixufan`/`daitipaibaoliuyuanpaizuoyong`(disabled)、`daiticaishengpai`（无文本 CheckBox）+`daiticaishengpai_combox`+"代替财神牌" Label、`andaitipaixianshi`、`caishengcanyuchipanggang`（财神参与吃碰杠(起飞)）、`caishengkedangang`、`jiaohuanpai`、`fankaibaibanshenghuahuashengdong`、`caishenganzhengchangpaichanyuchangpanggang`、`zhuapaihouxuancaisheng`、`juedicaisheng`（掘地财神）、`fankaidepaibuzuocaisheng`。

**买马区（`:200-205`）**：`xianmaimahoudingzhuang`（先买马后定庄）。

**快捷方式区（`:210-241`）**：`huoqushuju`(disabled)、`guopai`、`xunhuanguopai`、`guodong`、`kanchupai`(disabled)、`mopai`、`chipai_kjj`(disabled)、`gangpai`、`pangpai_kjj`、`chexiao`、`chongxuanzhuang`(disabled)、`shubiaoyouji` 各 ComboBox（102 宽）+ 静态说明"强制退出: 双击预测牌"、"调整大小: 双击空白处"。

**地方玩法区（`:243-252`）**：`leqingmajiang`/`xianmajiang`/`yuhuanwahua`/`wenchengmajiang`/`wenlingmajiang`(disabled)、`guangxi152`（可勾）；底部 Label"严禁用于赌博等非法用途"（`:251`）。

### 3.4 控制器接线（SettingStageController.initialize，`.java:334-986`）

- 所有控件改值即写 `Config.systemConfig.*`（内存对象），窗口关闭由 guize_btn 的 onCloseRequest 统一 `ObjectKit.WriteObject` 落盘 `./sconfig.config`（`MainController.java:840-851`）。
- 快捷键 12 个 ComboBox 全部填充 `Config.getKey2KeycodeMapping()` 的键名集合并回显当前值（`:940-975`）。
- ColorPicker 初值/写回 `zidingyibeijingyanse[3]`（`:794-796`）；主界面背景用该 RGB 填充（`MainController.java:293`）。
- `shubiaoyouji` 两项"撤销/摸牌"（`:789-793`），决定主界面右键行为（`MainController.java:902-925`）。

---

## 4. 弹窗视图（5 个 FXML + guopai_tishi 组件）

### 4.1 Ai.fxml —— 指挥提示（AI 配置）

- 500×300，controller `quesheng.dialog.AiDialog`，stylesheets Main.css（`Ai.fxml:9-12`）。

| fx:id | 类型 | 文本 | 出处 |
|---|---|---|---|
| kaiqiai | CheckBox | 是否开启指挥提示 | `:14` |
| zhihuifangxiang | ComboBox(150) | 指挥方向 | `:15-16` |
| dafa | ComboBox(150) | 玩法 | `:17-18` |

- 打开时机：主界面 [指挥提示] → `zhihuitishiConfig`：新 Stage（logo 图标、置顶、模态、不可缩放）加载 Ai.fxml，标题"指挥提示"+用户名；关闭时 `kaiqiai==1 && dafa=="血战"` 则构造 `XueZhanAiImpl`（`MainController.java:4329-4352`）。

### 4.2 CaiShengXuanZe.fxml —— 财神选择

- controller `quesheng.dialog.CaiShengXuanZeDialog`，stylesheets Main.css（`CaiShengXuanZe.fxml:11,46-48`）。

| fx:id | 类型 | 文本/内容 | 行为（CaiShengXuanZeDialog.java） |
|---|---|---|---|
| caisheng_h_1~h_3 | HBox×3 | 运行时填万(1-9)/条(11-19)/筒(21-29) 半高牌图 | 点击牌 toggle `mj_clicked` 样式并增删 `MainController.caisheng`（`:63-122`） |
| caisheng_h_4 / caisheng_h_5 | HBox×2 | 字(31-37) / 花(41-48) | 注意：花牌图实际也加到 h_4（`:123-162`） |
| caisheng_ok_btn | Button | 确定 | 未选弹"请选择财神"；否则关闭 popup（`:169-175`） |
| caisheng_cancle_btn | Button | 取消 | 直接关闭（`:176`） |
| caisheng_meijuchongshe | CheckBox | 每局重设 | selectedProperty → `meijuchongshecaisheng`（`:163-168`） |
| — | Label | 注:选中每局重设，每局开始都会提示设置 | `:37-40` |

- 打开时机：抓牌后 `CaishengxuanzeImpl.caishengxuanze`（RxJava 钩子，`CaishengxuanzeImpl.java:43`）：`zixuancaisheng==1` 且（每局重设或未选过）→ 建 `caisheng_seting_popup` Stage 加载本 FXML，标题"财神设置"（FXML 加载在 `:60`），隐藏时把选中结果写回牌面。

### 4.3 DanZhangKeGang.fxml —— 单张可杠选择

- 与 4.2 结构完全相同（fx:id 也沿用 caisheng_* 前缀），controller `quesheng.dialog.DanZhangKeGangDialog`（`DanZhangKeGang.fxml:11,19-39`）。
- 打开时机：`DanzhangkegangImpl` 建 `danzhangkegang_seting_popup`（`:40`），标题"单张可杠选择"，`setOnHidden` 回发 RxJava onNext(1)（`:59`）。

### 4.4 HuaPaiXuanZe.fxml —— 花牌设置

- 同构弹窗，fx:id 为 huapai_h_1~h_5 / huapai_ok_btn / huapai_cancle_btn / huapai_meijuchongshe，controller `quesheng.dialog.HuaPaiXuanZeDialog`（`HuaPaiXuanZe.fxml:11,19-39`）。
- 打开时机：`ZixuanhuapaiImpl` 建 `zixuanhuapai_seting_popup`（`:49`），标题"花牌设置"；隐藏时把选中花牌映射 `result=41`（`:64-75`）。

### 4.5 ChangePassword.fxml —— 修改密码

- 600×400，controller `quesheng.dialog.ChangePasswordDialog`（`ChangePassword.fxml:8`）。

| fx:id | 类型 | 提示(promptText) | 行为（ChangePasswordDialog.java） |
|---|---|---|---|
| pwd_old | PasswordField(215×34) | 输入旧密码 | 校验非空（`ChangePasswordDialog.java:49-52`） |
| pwd_new | PasswordField | 输入新密码 | 非空（`:53-62`） |
| pwd_repeat | PasswordField | 确认新密码 | 与新密码一致（`:63-72`） |
| chg_btn | Button(118×34) | 文本"确定修改" | → `UpdatePassword`（`:73`）：`showMyProgressDialog("正在修改密码")` → `POST /api/user/updatePassword`（`:80`）→ 成功弹"密码已修改，请重新登录"并 1 秒后退出进程 |
| — | Label | "修改密码"（标题） | `:10` |

- 打开时机：主界面 [修改密码]（`MainController.java:429-448`）。

### 4.6 guopai_tishi.fxml —— 过牌提示组件（fx:root）

- `fx:root type="AnchorPane"` 300×200，style `-fx-background: transparent;-fx-border-color: white;-fx-border-width: 1px;-fx-border-style: solid`，无 fx:controller（由宿主类自任控制器）（`guopai_tishi.fxml:7`）。
- 加载方式：`GuopaiTishi extends AnchorPane implements Initializable`，构造内 `loader.setRoot(this); loader.setController(this)` 后 load（`GuopaiTishi.java:21-35`）。
- 内容为 5 组操作说明（Label 红字 `#fc0808` + Text 白字，`guopai_tishi.fxml:9-17`）：

| Label | Text 说明 |
|---|---|
| 过张\|过栋 | 数字按钮快速向左右移动，或选择牌后右击屏幕右(左)半部分空白处向右(左)移动，右击目标牌在牌后插入。 |
| 循环过牌 | 黄色数字按钮全部循环，透明按钮部分循环。 |
| 单张对调 | 任意两张牌交换，不限方位。 |
| 跨方位过栋 | 鼠标左键单击在目标栋前插入，鼠标右键单击在目标栋后插入，按着Shift时转向 |
| 按ESC键退出 | （无正文） |

---

## 5. 控制器与自绘控件清单

### 5.1 controller/ 与 dialog/（Java）

| 类 | 行数 | UI 职责 | 出处 |
|---|---|---|---|
| MainApp | 54 | 预载 3 窗、SettingView 模态置顶、stop() 登出退出 | `MainApp.java:28-53` |
| StageController | 91 | 窗口注册表 HashMap；loadStage 统一标题"HD"+version_name + logo.png 图标 | `StageController.java:30-34,44-64` |
| BaseController | 92 | 通用模态弹窗：showTextDialog（Label+"知道了"按钮，3 秒自动关）、showMyProgressDialog（ProgressIndicator） | `BaseController.java:36-90` |
| LoginController | 237 | 登录窗逻辑（§1.4） | — |
| MainController | **7956** | 主牌桌全部交互、四方位面板编排、热键分发、jiance 轮询、OTA、测试发牌 | §2 |
| SettingStageController | 986 | 规则窗 ~140 个开关双向绑定 | §3 |
| dialog/*.java | 64+183+111+183+183 | 5 个弹窗控制器 | §4 |
| serve/impl/*.java | — | 规则钩子（RxJava ObservableEmitter）：CaishengxuanzeImpl / DanzhangkegangImpl / ZixuanhuapaiImpl / LiangCengJiImpl / SanCengJiImpl，前三个负责弹 §4.2-4.4 弹窗 | `docs/逆向④_游戏端源码还原.md` §3.2 |

### 5.2 customcontrole/（16 个自绘控件）

| 类 | 作用 |
|---|---|
| MjPanel（485 行，抽象） | 四方位面板基类：hu/mo/peng/gang 四个 ImageView 提示图 + chi "吃" Label + group AnchorPane（`MjPanel.java:38-55`） |
| MjPanelInterface（90 行）/ MjControlInterface / MjYuCeControl | 面板/牌格接口（getHu_text/getMo_text/getCaozuoPanel 等） |
| TopPanel / BottomPanel / LeftPanel / RightPanel（各 ~2200 行） | 四方位牌墙渲染（牌图旋转、碰杠牌 1/3 缩放块、听牌图标、打牌方向图） |
| MjHBoxLeftControl / MjHBoxRightControl / MjVBoxBottomControl / MjVBoxTopControl | 单张牌格（内含 1 个 ImageView） |
| ZhuangPanel | 中央庄家/方位图 + 四"缺"标记 |
| YuceYinaPanel | 预测/已拿按万条筒汇总浮层 |
| HuanShouPaiPanel / JiuZhengHuanShouPaiPanel / SheTiaoPaiPanel | 换手牌 / 纠正换牌 / 设跳牌浮动面板 |
| GuopaiTishi | 过牌提示组件（加载 guopai_tishi.fxml） |

---

## 6. CSS 主题表

### 6.1 Login.css（12 行，`view/Login.css`）

| 选择器 | 属性 | 效果 |
|---|---|---|
| `#errorMessage` | `-fx-text-fill: white` | 登录错误提示白字（其余视觉靠 FXML 内联样式与 login_background.png） |

### 6.2 Main.css（119 行，`view/Main.css`）——类名 / 颜色 / 伪类全表

| 类名 | 关键属性 | 界面作用 |
|---|---|---|
| `.mj_chu` | opacity 1.2、translate-x 10px | 已出牌偏移强调 |
| `.mj_clicked` | 红色 1px 实线边框 | 财神/花牌选择弹窗中选中牌 |
| `.border_red` / `.border_green` / `.border_yellow`(2px) / `.border_deepskyblue` | 1px 实线边框（红/草绿/#ff6808/deepskyblue） | 运行时给牌格加的彩色状态框 |
| `.caozuo_tishi` | padding 0、红字、hand 光标 | 吃/碰/杠/摸/胡操作提示图标 |
| `.ting_tishi` | 白底、padding 0.5em、红字、黑体 2em 字重 900、2em 圆角 | 听牌大字提示气泡 |
| `.huanpai_biaoji` | 红色 1px 边框 | 换牌选中标记 |
| `.jiuzheng_huanpai_biaoji` | greenyellow 1px 边框 | 纠正换牌标记 |
| `.mj_guozhang` | opacity 0.3 + 红底红框 | 已过张的牌置灰标红 |
| `.guopai_label_xuanze` (+`:hover`) | 黑字、hand、黄底 / hover 黄绿底 | 过牌选择数字按钮 |
| `.guopai_label_xuanze_top` (+`:hover`) | 白字红框、hand / hover 黄绿底 | 过牌栋顶标签 |
| `.mj_last` | 黄色 5px 边框 | 最后一张（摸牌张）高亮 |
| `.renyicaisheng` | 红字、hand | "任意财神"可点文字 |
| `.maima` | fill crimson、红底、`-fx-blend-mode: hard-light` | 买马高亮（MenuButton 买马菜单应用） |

### 6.3 代码内联主题（FXML style 属性 / setStyle）

| 位置 | 样式 | 作用 |
|---|---|---|
| 登录/重置按钮 | `-fx-background-radius:4;-fx-background-color:#097299;-fx-text-fill:white;-fx-font-size:20px` | 品牌蓝按钮 | `LoginStageView.fxml:89-90` |
| jiuzhenghuanpai_btn | `-fx-background-color: red;` | 纠正换牌红色警示钮 | `MainStageView.fxml:58` |
| bottom_zhuang_label | `-fx-background-color:yellow;-fx-border-radius:5px;-fx-background-radius:5px;-fx-text-fill:black;-fx-font-size:40px;-fx-padding:5px;-fx-font-weight:900;-fx-font-family:'华文行楷'` | 庄家标记 | `MainController.java:831` |
| 主界面背景 | `Color.color(systemConfig.zidingyibeijingyanse[0..2])` BackgroundFill | 用户自定义桌面底色 | `MainController.java:293` |
| guopai_tishi 根 | 透明底 + 白色 1px 边框 | 提示浮层 | `guopai_tishi.fxml:7` |

---

## 7. 全局功能映射总表（界面 → 控制器 → 后端）

### 7.1 HTTP 端点全集（6 个，全部 `http://{choseIp}:8081`，FormBody + Bearer jwt，`docs/逆向④_游戏端源码还原.md` §6.1）

| 端点 | 界面入口 | 调用处 |
|---|---|---|
| `/api/login/login` | 登录窗 [登录] | `LoginController.java:179` |
| `/api/mj/getConfigs` | 登录时自动（牌图配置） | `Config.java:459-495`（processLogin → `Config.initialize`，`.java:228`） |
| `/api/config/GetLastVersion` | 主窗 OnShown 自动（OTA，dapai64/dapai32） | `MainController.java:305` |
| `/api/yiZhuo/get` | [获取] 按钮 + 10s 兜底轮询 + IM typeu==1 推送触发 | `MainController.java:3895（huoqu）、4033（jiance 内 POST）` |
| `/api/user/updateSelf` | [激活] 按钮 | `MainController.java:3990`（`private void jihuo()`） |
| `/api/user/updatePassword` | 修改密码弹窗 [确定修改] | `ChangePasswordDialog.java:80` |

### 7.2 实时通道（MobileIMSDK UDP 7901，非 WS）

- 登录：`doZhuoLoginImpl` → `SendLoginDataAsync("<user_id>_2", jwt, "<user_id>")`（`MainController.java:7811-7819`）。
- 收牌：`ChatTransDataEventImpl.onTransBuffer typeu==1 → mainGUI.jiance()`（服务器"牌局有更新"信号 → 拉取并重绘四面牌墙）【勘误 2026-11-13 实机 Run C：jiance() 仅计数+闪烁，**重绘需人工点"获取"**】；errorCode==301 → 自动重连提示（`docs/逆向④_游戏端源码还原.md` §3.4、§7）。
- 登出：out_btn / stop() → `LocalUDPDataSender.sendLoginout()` + `IMClientManager.release()`（`MainController.java:895-900`、`MainApp.java:42-48`）。

### 7.3 功能按钮 → 动作速查

| 界面元素 | 动作函数 | 效果 |
|---|---|---|
| [开始（测试）菜单] | ceshi108/136/144/72sifeng/72sanfeng/112/guangxi152/90 | `jieshu()+Config.xipai(n)+begingamge()` 本地摆牌模拟 |
| [获取] | huoqu() | 拉服务器真实牌局 → Config.weina → begingamge() 渲染 |
| [分析] | fenxi_btn handler | 以上一把牌为暗牌重开（`MainController.java:864-877`） |
| [旋转] | xuanzhuan_btn handler | 四方位 direction-1 轮转、重建 ZhuangPanel、重绘 |
| [撤销]/热键 | chexiao() | 撤销最近操作 |
| [还原]/[重选庄] | huanyuan() | 回退到初始摆牌 |
| [结束] | jieshu() | 清四面牌墙/预测/出牌池 |
| guo_combobox 七选项 | guozhang/xunhuanguozhang/guodong/kuafangweiguodong/danzhangduidiao/hebingpai | 过张族操作（配 guopai_tishi 说明浮层） |
| duidiao/dong/zhang/dongban 四组合框 | 各 valueProperty 监听 | 精细位移（张/栋/栋半/方位对调） |
| 买马菜单 | maimawu/dong/nan/xi/bei（`MainController.java:4400-4447`） | 按 `maima_map` 给符合余数的暗牌加 `.maima` 类 |
| [激活]/[获取]/[分析] 未激活 | showTextDialog("请先激活") | 激活闸（`param.user.getJihuo_status()==1`） |
| 双击预测牌 / 双击空白 | FXML 说明：强制退出 / 调整大小 | `SettingStageView.fxml:237-240` |

---

## 8. 资源清单（UI 相关）

| 资源 | 位置 | 用途 |
|---|---|---|
| login_background.png | `quesheng/view/login_background.png` | 登录整窗背景 |
| image/（30+ 文件） | `quesheng/view/image/` | hu/mo/peng/gang 操作图、dong/nan/xi/bei/center 方位图、dapaifangxiang 打牌方向、gangfangxiang/pengfangxiang/chifangxiang(+_yuce) 杠碰吃方向、fangpao 放炮、ting/all_hu 听牌、guopai_guozhang/guopai_xunhuan 过牌标记、lanse/lvse/hongse 蒙色、max.png 窗口钮、delete.png、logo.png/logo3.png |
| image/sucai/{wan,tiao,tong,zi,hua} | `quesheng/view/image/sucai/` | 万条筒字花牌面（PNG/BMP 共 50 张目录 + `50` 号文件） |
| 牌图配置 | 服务器 `/api/mj/getConfigs` → MjConfig(id/number/name/url)，`Config.getImageByNumber` 取图 | 运行时牌面与本地 sucai 双源 |
| 2 CSS / 9 FXML | `quesheng/view/` | 本文全部素材 |

---

## 9. 忠实性说明

1. **来源边界**：本文 FXML 控件、fx:id、onAction、stylesheets、controller 属性均逐行提取自 `game-src/quesheng/view/*.fxml`（CFR 反编译 JAR 后解压产物，`docs/逆向④_游戏端源码还原.md` §1.2 动态提取）；控制器行为提取自 `game-src/quesheng/controller/`（62 个 Java）。
2. **反编译产物局限**：JavaFX 8 的 FXML 在 CFR 反编译的 Java 中仅以字符串常量保留（`FxmlViewContent.java:8-18` 注册表）；MainController 7956 行中少量 lambda 被展平，个别 setOnAction 处引用以最近可读行为准；`game-src` 为只读参考、不可直接编译（逆向④ §9.1）。
3. **运行时渲染未验证**：本文为静态 UI 定义，未运行游戏端截图比对。以下与真实渲染可能有差异——MainStageView 声明 1548×631 但运行时强制最大化且 StageStyle.TRANSPARENT（窗口装饰与阴影不可考）；四方位牌墙全为代码动态排版（绝对坐标由 SCREEN_HEIGHT/MJ_WIDTH 运行时计算，FXML 无对应节点）；登录窗 ImageView `disable="true"` 仅禁交互不隐视觉，实际观感受 login_background.png 原图决定。
4. **可见性/禁用态未穷举**：SettingStageView 大量 `disable="true"` 的 CheckBox 为预留规则位，其运行时是否被控制器按玩法动态启用仅核对了部分（如 `sanceng_liangdongsizhang` 等未逐一追踪 setter）；`jihuo_btn` 在已激活账号下被 OnShown 隐藏（`MainController.java:290-292`），其余按钮可见性随激活状态的变化未穷举。
5. **噪声说明**：FXML 头部 Oracle License 注释（LoginStageView.fxml:3-41）、Java 文件头 CFR "Could not load the following classes" 注释均为工具产物，无 UI 语义，本文未采信。
