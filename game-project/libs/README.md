# libs/ — 编译期依赖

编译所需依赖 jar 全部来自仓库根目录的 `../game-jars/`（不做任何修改、不重复入库），构建脚本
会把它们复制到 `out/lib/` 供 javac 与运行期 `Class-Path` 使用。`javafx9/`（.gitignore 排除）里的
JavaFX 9 classes 由构建脚本从 JRE 9 的 JIMAGE 抽取生成，见根 README。

## 依赖清单（14 个）

| jar | 来源 | 用途 |
| --- | --- | --- |
| gson-2.3.1.jar | ../game-jars/ | JSON 解析（服务器配置/存档） |
| okhttp-3.9.1.jar | ../game-jars/ | HTTP 客户端（HttpKit/登录/更新） |
| okio-1.13.0.jar | ../game-jars/ | okhttp 依赖 |
| ormlite-core-5.0.jar | ../game-jars/ | ORM（SqliteOpenHelper） |
| ormlite-jdbc-5.0.jar | ../game-jars/ | ORM JDBC 适配 |
| sqlite-jdbc-3.7.15-m1.jar | ../game-jars/ | SQLite JDBC 驱动（含 native） |
| rxjava-2.2.3.jar | ../game-jars/ | ReactiveX（napai 等异步流程） |
| reactive-streams-1.0.2.jar | ../game-jars/ | rxjava 依赖 |
| jintellitype-1.3.9.jar | ../game-jars/ | Windows 全局热键 |
| swing-worker-1.2(1.6-).jar | ../game-jars/ | Swing worker 兼容层 |
| MobileIMSDK4j.jar | ../game-jars/ | IM 客户端 SDK（IMClientManager） |
| MobileIMSDKServerX_META.jar | ../game-jars/ | MobileIMSDK 元数据/依赖 |
| exe4jlib.jar | ../game-jars/ | exe4j 启动器库（原程序打包用；代码未引用，仅随 classpath 提供） |
| QueshengDaPai.jar | ../game-jars/ | 原始字节码基准，**只作对照，不进编译 classpath**（避免源码缺失时静默回退到旧 class） |

`QueshengDaPai.jar` 内原始 `META-INF/MANIFEST.MF` 的 `Class-Path` 依赖 12 个 jar（不含
exe4jlib.jar）；本工程 `manifest.mf` 原样保留该清单，因此构建脚本会把这 12 个 jar 复制到
`out/` 旁路目录，使 `java -jar out/quesheng.jar` 可直接运行。
