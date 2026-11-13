# game-project — 游戏端（JavaFX 麻将牌局展示端，quesheng.*）可编译工程

由 `game-jars/QueshengDaPai.jar`（84 类）的 CFR 0.152 反编译产物 `../game-src/` 重建的**完整可编译 JavaFX 工程**。
现状：`javac --release 8` **0 Error**，编译出 **84/84 个 class**（与原 jar 类列表一一对应），产出可运行
`out/quesheng.jar`（Main-Class `quesheng.controller.MainApp`，保留原 jar 的 `Class-Path`）。
全部修复记录与"零业务逻辑改动"的字节码级证明见 [FIXES.md](FIXES.md)。

## 目录结构

```
game-project/
  README.md            本文件（工程说明/构建/运行/忠实性）
  FIXES.md             全部 542 条 CFR 修复的逐条记录 + 字节码等价性验证
  build.cmd            Windows 一键构建（javac 全量 + 资源 + jar，javac 必须 exit 0）
  build.sh             同上（Git Bash 版）
  manifest.mf          打包清单：原 jar 的 Main-Class + Class-Path（原样保留）
  src/                 修复后的 62 个 .java（包结构与 game-src 一致，UTF-8，纯 ASCII 源码）
  resources/           从 QueshengDaPai.jar 原样重提取的资源：9 fxml + 2 css + 79 png + 50 bmp = 140 文件
  libs/README.md       依赖说明（14 个 jar 清单，来自 ../game-jars/）
  javafx9/             JavaFX 9 classes jar（jimage 抽取生成；.gitignore 排除，可由构建脚本再生）
  out/                 构建产物（.gitignore 排除）：out/quesheng.jar、out/classes、out/lib、out/sources.txt
```

## 构建步骤

前置条件：
- PATH 中有 `javac` / `jar` / `jimage`（本机 JDK21：`F:\Dev\SDKs\JDK21\bin`）；
- 仓库根有 `game-jars/`（13 个依赖 jar + 原始基准 jar）；
- 仓库根有 `.sandbox-game/游戏端/jre-9/lib/modules`（Oracle JRE 9 JIMAGE，JavaFX 9 类的唯一来源，不联网下载 openjfx）。

```cmd
cd game-project
build.cmd        # 或 Git Bash: ./build.sh
```

脚本做四件事：
1. 把 `../game-jars/` 的依赖 jar 复制到 `out/lib/`（**排除 QueshengDaPai.jar 本身**——它是字节码对照基准，不进编译 classpath，避免源码缺失时静默链接旧 class）；
2. 若 `javafx9/javafx9-classes.jar` 不存在，用 `jimage extract --include "regex:/javafx\.(base|graphics|controls|fxml|swing|media|web)/.*"` 从 JRE 9 的 `modules` 抽取 javafx.base/graphics/controls/fxml/swing/media/web 共 5550 个 class，剔除 `module-info.class` 后打成 `javafx9/javafx9-classes.jar`（约 7.7 MB）；
3. `javac --release 8 -encoding UTF-8 -Xlint:-options -cp "javafx9/javafx9-classes.jar;out/lib/*" -d out/classes @out/sources.txt`，打印 `javac exit=0` 与编译类数；
4. `jar cfm out/quesheng.jar manifest.mf -C out/classes . -C resources .`（用 `manifest.mf` 而不是 `jar cfe`：除了 Main-Class，还必须保留原 jar 的 `Class-Path` 依赖清单，否则 `java -jar` 无法定位依赖）。

## 运行说明

```cmd
cd game-project\out
..\..\.sandbox-game\游戏端\jre-9\bin\java.exe -jar quesheng.jar
```

- 原程序按 Java 8 语法编译（class 版本 52），官方推荐运行时为 Oracle JRE 9（JavaFX 内置于该 JRE）。用 JRE 9 的 `java.exe` 可直接 `java -jar`；
- manifest 的 `Class-Path` 与构建时复制到 `out/` 的 12 个依赖 jar（gson/okhttp/okio/ormlite×2/sqlite-jdbc/rxjava/reactive-streams/jintellitype/swing-worker/MobileIMSDK×2）配合工作；
- 程序会连接远端服务器（登录、牌墙配置 `MjConfig.url` 下发）、弹出 GUI、注册全局热键——**构建验证时不要运行它**。

## resources 清点（以 QueshengDaPai.jar 内原始资源为准，用 jar xf 重提取）

| 类型 | 数量 | 说明 |
| --- | ---: | --- |
| .fxml | 9 | Ai、CaiShengXuanZe、ChangePassword、DanZhangKeGang、guopai_tishi、HuaPaiXuanZe、LoginStageView、MainStageView、SettingStageView（均在 `quesheng/view/`） |
| .css | 2 | Login.css、Main.css |
| .png | 79 | `quesheng/view/image/`（含 logo、背景、操作提示等） |
| .bmp | 50 | `quesheng/view/image/sucai/{wan,tong,tiao,zi,hua,50}/N.bmp`（牌面素材，由服务器下发的 `MjConfig.url` 动态拼路径加载） |
| 合计 | 140 | 与原 jar 内资源集**逐文件一致**（仅少一个 `META-INF/MANIFEST.MF`，它升级为打包清单 `manifest.mf`） |

引用完整性核对：
- FXML 内 `@Login.css`、`@Main.css`、`@image/max.png`、`@image\delete.png`（原 jar 即为反斜杠写法，行为与原版一致）、`@login_background.png` 全部存在；
- 源码中 35 处 `"/quesheng/view/..."` 引用（9 个 fxml + 25 个 png + 1 个路径前缀常量）核对存在；
- 9 个 `fx:controller` 指向的控制器/对话框类全部在编译产物中；
- `logo_url`、`sucai/**` 等动态路径的文件集与原 jar 一致，无缺失。

## 忠实性说明（摘自 FIXES.md §3）

- `src/` 基于同版本 CFR 0.152 带 `--extraclasspath` 的重反编译（`game-src/` 未改动）；`game-src` 不带 classpath 反编译造成的 `(Object)` 擦除强转（仅 MainController 就 420 处）与 raw 类型损伤无法可靠手修，带 classpath 的输出严格更接近原始源码。
- 相对参考输出的 542 条后续修复全部为机械性修复（CFR `void` 声明、LVTT/LVT 依据的泛型与声明类型恢复、lambda 捕获变量原名恢复、CFR 顶置声明还原为块作用域、缺失 import、类型见证、过期注释），逐条记录于 FIXES.md。
- 字节码验证：84/84 类一致；2444 个方法体逐一对账——745 对完全相同（仅 lambda 合成方法编号差异），其余 258 对差异均为语义等价类别（CFR 的 if/else↔三元等源码级改写、跳转目标偏移、槽位平移、CFR 清理的 lambda 死接收器读、javac 21 的防御性 checkcast），**无业务逻辑差异**。
