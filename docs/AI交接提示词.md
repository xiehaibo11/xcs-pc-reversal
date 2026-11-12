# 复制以下整段给 W11 电脑上的 AI 即可接续工作

---

我在逆向鑫财神（XCS）麻将牌局识别系统，安卓端已完整逆向重建，现在继续完成 PC 端与游戏端的剩余工作。

## 背景
- 系统架构：四方位摄像头主板 → 服务器(8.137.196.28, HTTP 8081 / WS 18889) → 客户端
- 三个客户端已逆向：安卓 App（Flutter/Rust，已重建可编译）、PC 图传 xcs.exe 2.0.15 与
  distinguish.exe 2.0.14（C#/.NET WPF，已反编译 45 类+9 窗口 XAML，可编译 WPF 工程已 BUILD PASS）、
  游戏端 游戏端.exe（exe4j 打包的 JavaFX + MobileIMSDK4j，jar 内嵌在加密载荷中未解出）
- 两个 Git 仓库（请先 clone）：
  1. https://github.com/xiehaibo11/xcs-pc-reversal （PC端反编译源码+XAML+逆向文档+路线图）
  2. https://github.com/xiehaibo11/xcs-android-reconstruction （安卓完整逆向重建+协议证据）
- 测试服务器：IP 8.137.196.28，账号 XCS232，密码 123456（devicetype=5 是 PC 端，
  devicetype=1 是安卓；登录接口 /api/login/login/distinguish?app_name=xcs；
  HTTP 8081 / WS 18889；同账号单点登录，登录前先退出其他设备）
- 协议：MobileIMSDK 同源（types 1心跳/2业务/3登出/4QoS/6登录/50登录响应/51心跳响应
  /52错误/53回显/54被踢）；内层 msg_type 101单牌/102预览帧/113回合开始/114设备收满
  /115离线/116上线/117升降牌洗牌；22 个 HTTP 端点合约在 xcs-android-reconstruction 仓库

## 尚未完成（按优先级）
1. 【最高】游戏端 jar 获取与反编译：在 W11 直接运行 游戏端.exe，exe4j 启动时自动解密解包，
   从 %TEMP%\install4j\ 或进程目录拷出 QueshengDaPai.jar/MobileIMSDK4j.jar 等全部 jar，
   用 CFR 反编译 quesheng.* 包（MainApp/LoginController/StageController/util.ObjectKit），
   还原 FXML 界面布局、sconfig.config（Java 序列化配置对象）的字段结构、服务器连接参数。
   游戏端 UI 完全依赖这些 jar 内的 FXML——这是目前 0% 的部分
2. PC 图传运行时验证：dnSpy 调试 xcs.exe，对照已反编译源码验证还原行为；
   VerifyPwdWindow 校验口令（MD5=E951081AC30FFA83F10B68CDE1CAC7）通过后的解锁范围逐行核对
3. 安卓遗留：612 方法中 387 个语义未闭合（coverage_ledger.json）；T系列设备条件分支
   （当前账号 S系列不显示高级控件，需 T系列实测）；OTA 升级页外观（需服务器推送）；
   弹层/放大对话框/删除确认/设备信息的原版实机截图；msg117 第二段；inlet_pwd.conf 用途
4. 生产端一局完整验收：开始→识别→校验→上传→结束（需实体牌桌配合，注意会真实上传数据）

## 已完成（勿重复）
协议常量/端点/RSA/dataKey/单点登录——三端交叉验证一致；
PC WPF 工程 BUILD PASS（net47 x86 原目标 + net8.0-windows 自包含发布）；
安卓重建 38 项 RuntimeChecks + 9 套 host 测试 + 生产登录只读验收全绿。

## 工具（W11 需安装）
dnSpy、ILSpy GUI、x64dbg、CFR/JD-GUI、JDK17+、.NET SDK、Process Monitor、Wireshark、
Fiddler、7-Zip、exe4j/install4j 相关解包工具

## 规矩
只静态分析+在 W11 本机运行这些已持有的程序；不要对生产服务器发送控制指令/上传/
写配置（只读验证除外）；所有结论标注证据来源；不确定就明说不确定。
