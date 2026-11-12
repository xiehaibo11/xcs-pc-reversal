# 逆向② 鑫财神图传PC — distinguish.exe 完整逆向文档

样本：鑫财神/distinguish.exe（10,182,144 字节，2025-07-15 编译，**app_version 2.0.14**）
类型：PE32 GUI，C#/.NET WPF——与① xcs.exe（2.0.15）同一工程的两代构建
反编译源码：`analysis/distinguish-cs/`（45 个 .cs）
工程名确认：AssemblyName=`distinguish`（AssemblyInfo/csproj）

## 与 ①（2.0.15）的差异（diff -r 全量）

1. 版本：2.0.14 → 2.0.15
2. **Channel.DeleteOffset 字段为 2.0.15 新增**（删除牌后序号重排的偏移，与安卓
   客户端 Channel.removedOffset 对应）；2.0.14 无此字段——旧版删除后重排行为有差异
3. Global.cs 字段顺序微调（HomeViewModel 静态引用位置）
4. 图标品牌更换：2.0.15 新增 cq_favicon.ico / cs.ico（钱圈/cs 品牌），旧版单一图标
5. 其余 12 个窗口/VM 文件差异均为反编译噪声级（baml 编译产物与 IL 细节），无业务分叉

## 功能面（与①一致的部分不再重复）

登录（devicetype=5）、get_server_addr RSA 下发、ws://18889、五种 zhuo 接口、
device_status_all、zhuo/add、app/check_update（type 与版本随包）、appsettings.ini、
校验口令 MD5 同值。协议常量文件两版逐字相同。

## 结论

② 是 ① 的上代构建。逆向以 ① 为准；② 的唯一行为学差异是删除牌时
**没有 DeleteOffset 重排**——即 2.0.14 删除一张牌后，后续牌序号不前移。
