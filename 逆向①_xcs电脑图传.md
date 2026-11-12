# 逆向① xcs电脑图传(新) — xcs.exe 完整逆向文档

样本：xcs.exe（10,269,696 字节，2026-04-17 编译，app_version 2.0.15，app_name=xcs）
类型：PE32 GUI，C#/.NET 8 WPF（ilspycmd 9.1 反编译，45 个 .cs 接近源码质量）
反编译源码：`analysis/xcs-cs/` ｜ 本文档：逐功能还原

## 1. 程序角色

PC 版"图传"客户端：接电脑摄像头/采集卡，替代四方位主板摄像头，
把识别结果按同一服务器协议上传。与安卓 App（主板版）共享同一后端账号体系。

## 2. 配置与凭据

- 配置文件 `./appsettings.ini`（Win32 INI API）
  - 节 `APP`：SERVER（入口IP）/ USERNAME / PASSWORD（勾选记住密码才存）
  - 节 `VERIFY`：PWD（功能校验口令的 MD5）
- 硬编码校验口令哈希：`verifyDefaultPwd = E951081AC30FFA83F10B68CDE1CAC7`
- 内存态：jwt / ipAddr(业务IP) / dataKey(替换规则) / rsaKey / loginUser / devices

## 3. 登录流程（LoginViewModel，399 行）

1. 校验网络（IsConnectInternet.IsConnect，尝试连接外部网址判断）
2. `POST http://{入口IP}:8081/api/login/login/distinguish?app_name=xcs`
   body：`{"username","password","devicetype":5}`  ← **devicetype=5 是 PC 端标识**（安卓=1）
3. 成功 → `LoginResp{jwt, devices[], loginUser}`；失败弹窗 msg
4. 生成 RSA-2048 密钥对（公钥指数 3，BouncyCastle）
5. `POST server/get_server_addr` body：`{"key":公钥,"client_type":5}` ← PC=5（安卓=1）
6. 私钥解密响应 `msg` → JSON 数组 `[业务IP, dataKey替换规则]`
7. 写 appsettings.ini；弹"登录成功"；打开 HomeWindow

## 4. HTTP 接口清单（HttpReq + HomeViewModel 实际调用）

统一封装：POST/GET + `Authorization: Bearer {jwt}`，基础 `http://{业务IP}:8081/`；
POST 路径不带 `api/` 前缀的例外：`account/edit/pwd`；GET 一律 `api/` 前缀。

| 接口 | 方法 | 用途 |
|---|---|---|
| login/login/distinguish?app_name=xcs | POST | 登录 devicetype=5 |
| server/get_server_addr | POST | RSA 配置下发 client_type=5 |
| zhuo/start_games | GET | 开始牌局 |
| zhuo/end_games | GET | 结束牌局 |
| zhuo/zhuo_reset_games | GET | 重置本轮 |
| zhuo/device_status_all | GET | 全设备状态（5秒轮询） |
| **zhuo/device_status/{deviceId}** | GET | 单设备状态（**安卓无此调用**） |
| zhuo/add | POST | 上传识别结果 `[SaveMj]`（SaveMj=mahjong_number/serial_number/direction/result） |
| app/check_update?**type=3**&version&app_name=xcs | GET | 更新检查（**PC type=3**，安卓=1） |
| account/edit/pwd | POST | 修改密码 |

## 5. WebSocket（NetManager.cs，websocket-sharp）

- `ws://{业务IP}:18889/`；Connect 后立即 `Send(LoginData)`（登录帧）
- **断线重连**：OnClose 且非主动关闭 → 1 秒间隔无限循环重连，成功后重发 LoginData
- **心跳**：System.Timers.Timer 首次 1ms 触发后改为 **5000ms**，发送 keepalive 帧
- **QoS**：`qosList{fp→时间戳}` 发送登记；重发扫描清理（阈值 ~5 秒级，见 2800 行清理段）
- 协议帧：`Protocol{types,data_content,from,to,fp,qos,retry_count}` JSON 文本帧
  （**retry_count 为 PC 独有字段**，安卓 JSON 无）

## 6. 消息分发（OnMessage → msg_type 分支，与安卓一一对应）

| msg_type | PC 行为 |
|---|---|
| 101 | 单牌识别结果（含图片通道） |
| 102 | 实时预览帧 |
| 113 | 回合开始（清结果/倒计时） |
| 114 | 单设备收满 |
| 115 | 设备离线 |
| 116 | 设备上线 |
| 117 | 升降牌打开等待洗牌 |

## 7. UI 窗口（WPF）

MainWindow→HomeWindow（主界面：四设备卡+牌墙+操作按钮）、
PreviewWindow/PreviewSettingWindow（预览与画框）、OnlineWindows（在线设置）、
EditPwdWindow（改密）、VerifyPwdWindow（功能口令校验，默认口令
MD5=E951081AC30FFA83F10B68CDE1CAC7）、ErrorWindow。

## 8. 与安卓客户端差异汇总
| 差异点 | PC | 安卓 |
|---|---|---|
| devicetype/client_type | 5 | 1 |
| check_update type | 3 | 1 |
| device_status/{id} 单设备轮询 | 有 | 无（只 all） |
| Protocol.retry_count 字段 | 有 | 无 |
| 技术栈 | C# WPF + websocket-sharp | Flutter + Rust |

## 9. 未决项
- check_update 返回的 OTA 下载链接域名未验证连通性
- VerifyPwdWindow 校验通过后的解锁范围未逐行核对
