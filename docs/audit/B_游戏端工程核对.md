# B · 游戏端工程核对（对抗性审计）

- 审计对象：`game-project/`（62 个 .java 的 JavaFX 工程 + resources + build 脚本）对照基准 `game-jars/QueshengDaPai.jar`
- 审计方式：一切从零（清空 `out/`、`javafx9/` 后重跑 `./build.sh`；jar 重解压做 sha256；`javap -p / -v / -c` 对账；FIXES.md 抽查逐条回对 `game-src/` CFR 原始输出）
- 审计约束：只读不改（未修改 game-project/、game-src/、game-jars/、.gitignore）；未运行 quesheng.jar；未联网
- 审计日期：2026-11-12（审计机 JDK 21.0.12.1，Git Bash / Windows）

## 结论行

**核对结果：构建可复现=是；类一致=84/84；方法签名抽查=10/10 一致；资源一致=140/140；FIXES 抽查=16/16 机械性（含 MainController 6 条）；字节码抽查=12/12 对等价（84 类全量分层对账：51/84 类在某级归一化后完全一致，33 类 147 条残差全部落入已逐指令核实等价的模式族）**

**总判定：通过。** 未发现任何常量值改变、条件翻转、语句删除等业务语义改动；全部差异均可归因于（a）javac 8 → 21 的代码生成差异、（b）CFR 反编译的源码级等价改写/泛型渲染、（c）jar 工具打包元数据。发现 2 处**声明层偏差**（不影响等价结论，见下文"声明层偏差"）。

---

## 1. 构建可复现性 — PASS

操作：`rm -rf game-project/out game-project/javafx9 && cd game-project && ./build.sh`（日志 /tmp/audit-build.log）

| 断言 | 声明值 | 实测 | 判定 |
| --- | --- | --- | --- |
| build.sh 整体退出码 | 0 | 0 | PASS |
| javafx9 自动再生 | 脚本应能再生 | `jimage extract` 从 `.sandbox-game/游戏端/jre-9/lib/modules` 重抽 5550 class 并打 javafx9-classes.jar，脚本具备该能力，本次从零再生成功 | PASS |
| javac | exit=0、0 Error | `javac exit=0`（`--release 8 -encoding UTF-8`；仅 unchecked 警告） | PASS |
| 编译类数 | 84 | `find out/classes -name '*.class' \| wc -l` = 84 | PASS |
| 产物大小 | ~1,457,341 字节 | `out/quesheng.jar` = **1,457,341 字节**（与声明逐字节相同） | PASS |

## 2. 类集合一致 — PASS（84/84）

`jar tf` 两侧各 249 条目；`*.class` 条目排序后 diff 为空：**84/84 类文件名（含内部类编号 `$1`/`$ComparatorMjResult` 等）完全一致**。非 class 条目（目录 + 资源 + META-INF）路径集合也完全一致（165 条，仅 MANIFEST.MF 内容差异见 §7）。

## 3. 方法签名对账（javap -p）— 10/10 一致（附差异分类）

必抽 10 类（覆盖任务要求的全部指定范围）：

| 类 | javap -p 原文对比 | 描述符级（javap -v `descriptor:`）对比 |
| --- | --- | --- |
| quesheng.controller.MainController | 差异仅 lambda 合成名 `lambda$null$N` ↔ `lambda$方法名$N`（编号一致，描述符一致）| **一致**（8 个合成 lambda 参数 `Integer`→`Object` 见下） |
| quesheng.controller.LoginController | 完全一致（含 `$1` 匿名类） | **一致** |
| quesheng.controller.StageController | 完全一致 | **一致** |
| quesheng.util.ObjectKit | 完全一致 | **一致** |
| quesheng.model.SystemConfig | 完全一致 | **一致** |
| quesheng.mobilesdk.IMClientManager | 完全一致 | **一致** |
| quesheng.ai.AbstractAi | 完全一致 | **一致** |
| quesheng.customcontrole.TopPanel | 差异仅 lambda 合成名 | **一致** |
| quesheng.dialog.AiDialog | 完全一致 | **一致** |
| quesheng.controller.serve.impl.LiangCengJiImpl | 差异仅 lambda 合成名 | **一致**（2 个合成 lambda 参数 `Integer`→`Object` 见下） |

**方法数**：两侧带参成员 2424/2424、`static{}` 初始化块 20/20、方法体（`Code:` 块）2394/2394 —— 与 FIXES.md "2444 个方法体"（2424+20）对账吻合。

### 全量 84 类加严核对（超出抽查要求的对抗性检查）

用 `javap -v` 的 **name+descriptor+flags** 做 84 类全量集合对比（lambda 合成名按编号折叠）：

- **80/84 类成员集合完全一致**。非合成成员（业务方法/字段/构造器）**无一例**名称、描述符、flags 差异。
- 4 类存在差异，且**全部位于 ACC_SYNTHETIC 的 lambda 实现方法**，共两类模式：
  1. **参数 `Integer`→`Object`（13 个合成方法）**：MainController#lambda$75/102/428/431/444/445/446/479、CaishengxuanzeImpl#lambda$6/16、LiangCengJiImpl#lambda$12/14、SanCengJiImpl#lambda$6。
     判定：**等价**。成因是 CFR 对 RxJava 泛型链按 raw 型渲染（如 `ObservableEmitter emitter` 不带类型实参），lambda 参数由 SAM 擦除型推断为 Object；实测调用点 BootstrapMethods 的 instantiated type 由 `(Integer)V` 变 `(Object)V`，抽验的 lambda 体（如 lambda$caishengxuanze$16）**完全不读该参数**，行为无差异；运行期仅在传入非 Integer 值时 ClassCastException 的抛出点前移/后移（均为错误路径）。
  2. **实例→静态（26 个合成方法）**：XueZhanAiImpl(3)、CaishengxuanzeImpl(13)、LiangCengJiImpl(7)、ZixuanhuapaiImpl(3)。与 FIXES.md §3 DEAD-RECEIVER 类别对应（CFR 清理了 lambda 体内 `aload_0; getfield mainController; pop` 死读，javac 21 因此生成静态实现）。抽验 XueZhanAiImpl lambda$0：原字节码确含该 3 指令死读，删除后无任何可观察效果。判定：**等价**。
- 2 个匿名类类级 ACC_FINAL 丢失：`quesheng.util.Config$1`、`quesheng.util.HttpKit$1`（原 `ACC_FINAL, ACC_SUPER` → `ACC_SUPER`）。判定：**等价**（javac 21 起不再给匿名类加 final；匿名类在源码层不可被引用继承，无运行期行为差异）。
- javap 显示层伪差异（非真实差异，已用 `javap -v descriptor:` 排除）：匿名类构造器在 javap 21 下渲染为 `MainController$1()`、`LoginController$1()`、`Config$1()`——**实际描述符与原 jar 完全相同**（如 MainController$1 实测 `(Lquesheng/controller/MainController;Lquesheng/util/AutoUpdateThread;)V` 两侧一致），系 javap 21 对无名合成参数的渲染策略不同。

## 4. 资源逐文件字节一致 — PASS（140/140）

方法：`jar xf` 重解压原 jar 到独立目录，对 140 个资源文件逐个 `sha256sum` 三方比对（原 jar ↔ game-project/resources ↔ 新 jar 内资源）。

| 比对 | 结果 |
| --- | --- |
| 路径集合（原 jar vs 新 jar vs resources/ 目录） | 完全一致（141 = 140 资源 + META-INF/MANIFEST.MF） |
| **原 jar ↔ game-project/resources** | **140/140 sha256 一致，不一致清单为空** |
| 原 jar ↔ 新 jar 内资源 | 140/140 sha256 一致 |
| 清点 | 9 fxml + 2 css + 79 png + 50 bmp = 140（与 README 声明一致） |

唯一非资源差异：`META-INF/MANIFEST.MF`（原 jar 内的清单升级为打包用 `manifest.mf`，内容见 §7）。

## 5. FIXES.md 抽查 — 16/16 机械性（要求 ≥15 条，含 MainController ≥5）

方法：每条记录用 文件:内容 定位 `game-src/`（CFR 0.152 原始输出）与 `game-project/src/` 当前文本，回对差异；capture-rename 类另用原 jar `javap -v` 的 lambda 实现方法 LVT 证伪"原名"断言。

| # | FIXES.md 记录 | game-src（CFR 原始） | game-project/src（现状） | 判定 |
| --- | --- | --- | --- | --- |
| 1 | #1 TopPanel void-decl(2085) | `void var9_30;` + `while (var9_30 < size)`（game-src:2104） | `int var9_30 = 0;`（src:2082），循环体逐句相同 | 机械性（CFR 非法 `void` 声明恢复） |
| 2 | #3 LeftPanel void-decl(2143) | `void var9_32;`（game-src:2162） | `int var9_32 = size = yina_bottom.size() - 1;`（src:2140） | 机械性 |
| 3 | #8 HuPaiKit void-decl(783) | `void var14_55;`（game-src:118） | `Mj dierge = null;`（src:460，LVT 名 dierge） | 机械性 |
| 4 | #15 MainController lvtt-generic(390) | `Optional result`（game-src:454） | `Optional<ButtonType> result`（src:392） | 机械性（LVTT 泛型恢复） |
| 5 | #16 MainController lvtt-generic(3479) | `List yixuan1 = ...((Object)"mj_guozhang")...`（game-src:3543） | `List<Mj> yixuan1 = ...contains("mj_guozhang")...`（src:3472） | 机械性（泛型 + classpath-CFR 去擦除强转） |
| 6 | #232 TopPanel capture-rename(1578) | `for (Map.Entry entry : ...)`（game-src:1597，lambda 体引用 `sub_enty` 编译不过/原名） | `for (Map.Entry sub_enty : ...)`（src:1574） | 机械性（lambda 实现方法 LVT 原名恢复，参见下方证伪样本） |
| 7 | #269 HuPaiKit type-witness(1397) | `Map.Entry.comparingByValue().reversed()`（game-src:1399） | `Map.Entry.<K, V>comparingByValue().reversed()`（src:1394） | 机械性（javac 推断所需见证，字节码无差异） |
| 8 | #317 MainController type-witness(4987) | `Observable.create(emitter -> {`（game-src:966 等） | `Observable.<Mj>create(emitter -> {`（src:4977） | 机械性（钉死元素类型；对应 §3 的 lambda 参数 Integer→Object 签名差异，已判等价） |
| 9 | #321 MainController capture-decl(3631) | `d = Config.SCREEN_WIDTH / 2.0 + ...`（CFR 顶置声明，game-src:1719） | `double finalX = Config.SCREEN_WIDTH / 2.0 + ...`（src:1654，块作用域+原名） | 机械性 |
| 10 | #332 MainController import(38) | 无 import；`SimpleObjectProperty warningColor = new SimpleObjectProperty((Object)Color.web((String)"#000000"))`（game-src:4109） | `import javafx.beans.property.ObjectProperty;`（src:38）；`ObjectProperty<Paint> warningColor = new SimpleObjectProperty<Paint>(Color.web("#000000"))`（src:4043） | 机械性（声明类型按 LVTT 恢复 + 去擦除强转） |
| 11 | #334 MainController 手工(6228) | raw `List list = yina.stream()...`（game-src:6133/6263 同构点） | `List<Mj> list;`（src:6020/6176/6201）与 `List<Mj> list = Config.yina.stream()...`（src:6219） | 机械性（元素类型来自 Config.yina 的泛型，业务表达式逐字相同） |
| 12 | #391 TopPanel dehoist(310) | 顶置 `List<Mj> gangpai;`（game-src:316）+ 多处 `gangpai = ...`（game-src:329/370/428/...） | 块作用域 `List<Mj> gangpai = yina.stream()...`（src:309/350） | 机械性（CFR 顶置声明还原，兄弟作用域还原后 lambda 可捕获） |
| 13 | #477 LoginController lvt-declared-type(147) | `HashMap<String, String> map`（game-src:169） | `Map<String, String> map`（src:147，LVT 擦除 Ljava/util/Map;） | 机械性 |
| 14 | #535 MjPanel import(9) | 无 `import java.util.Map` | `import java.util.Map;`（src:10） | 机械性 |
| 15 | #537 HuPaiKit cfr-artifact(1398) | `Comparable cfr_ignored_0 = (Comparable)result.put(e.getKey(), e.getValue());`（game-src:1400） | `result.put(e.getKey(), e.getValue());`（src:1395） | 机械性（去 CFR 占位持有者；字节码 pop vs checkcast+astore） |
| 16 | #538 LeftPanel stale-comment(1396) | `* WARNING - void declaration`（game-src:1416） | 注释已随修复消失（src 无该注释） | 机械性（过期注释删除） |

**capture-rename 的字节码证伪样本**（对应 FIXES.md 的 mj8→mj 类记录，TopPanel `Config.yuce.forEach` 作用域）：原 jar lambda 实现方法 `lambda$hupaiYuCe$198` 的 LocalVariableTable 为 `slot1=mj, slot2=mj1`（捕获变量原名为 **mj**），方法体对 slot1 读取两次（getResult + yuanpai）；CFR 输出却把外层循环变量命名为 `mj8` 且 lambda 体同时引用 `mj8` 与外层 `mj`（ CFR 拆名缺陷）。`src/` 将其统一为 `mj` 后与原字节码逐指令一致。**该改名以原 jar LVT 为证，非臆造。**

结论：抽到的 16 条全部为机械性修复（权限/import/泛型/声明类型/CFR 结构/注释），**未发现常量变值、条件翻转、语句删除等业务语义改动**。

## 6. 字节码对账抽验 — 12/12 对等价（要求 ≥8 对）

方法：自建分层比较器对 `javap -p -c -constants` 输出做规范化（层级：0 原始 token → 1 ldc_w/goto_w 折叠 → 2 access$/lambda 名折叠 → 3 槽位号折叠 → 4 合成 lambda 签名 Integer/Object 与 static 修饰折叠 → 5 死接收器三元组折叠 → 6 分支目标数折叠），84 类全量跑对账后，对每个残差模式族的代表样本做**逐指令人工判读**。

### 深检通过的方法对（12 对）

| # | 类#方法 | 差异摘要 | 判定 |
| --- | --- | --- | --- |
| 1 | SanCengJiImpl#napai | `istore_3`↔`istore 6` 槽位平移；CFR 死变量删除 | 等价 |
| 2 | SanCengJiImpl#lambda$6 | 签名 `(ObservableEmitter,Integer)`→`(ObservableEmitter,Object)`，参数体未读；调用点 instantiated `(Integer)V`→`(Object)V` | 等价（合成成员） |
| 3 | SettingStageController#lambda$50 | CFR 三元改写：`if(b){F.X=1}else{F.X=0}`（两次 getstatic+putstatic）→ `F.X=b?1:0`（单次 getstatic/putstatic，+goto） | 等价（getstatic 无副作用，赋值次数不变） |
| 4 | XueZhanAiImpl#lambda$0 | 原字节码含 `aload; getfield; pop` 死读（实例 lambda），新为静态 lambda 无死读 | 等价（死读无可观察效果） |
| 5 | XueZhanAiImpl#ComputerWeight | indy 捕获 `(XueZhanAiImpl,Mj)`→`(Mj)`（this 捕获随死读消失）；`if_icmpne`→`if_icmpeq+goto`、`ifle`→`ifgt+goto` 分支反转身交换 | 等价 |
| 6 | HashKit#fnv1a64 | `int i=0` 与 `int n=s.length()` 两条独立语句顺序互换 | 等价 |
| 7 | Config#getImageByMj | 三元赋值 javac8 逐支 `istore` ↔ javac21 合并后单次 `istore` | 等价 |
| 8 | MjPanel#keChi | `checkcast; dup; astore`（javac21 栈复用）↔ `checkcast; astore; aload`（javac8 存后重读） | 等价 |
| 9 | MainController#huoqu | CFR 常量折叠：`append(":8081")`+`append("/api/yiZhuo/get")`（两条 ldc）→ 单条 `ldc ":8081/api/yiZhuo/get"`；运行期字符串逐字符相同 | 等价 |
| 10 | MainController$ComparatorMjResult#compare | `dsub; dstore; dload; dcmpl` ↔ `dsub; dup2; dstore; dcmpl`（存栈两份 vs 存后重读） | 等价 |
| 11 | MainController#ChiPaiKeTing | `invokeinterface List.*`↔`invokevirtual`（声明类型差异致调用指令不同，解析目标同名同描述符）；分支反转 | 等价 |
| 12 | DanZhangKeGangDialog#initialize | 循环 `x=i; ...; i++`（尾部自增）↔ `x=i++;`（头部自增存旧值）；循环变量在体内未再读，迭代值序列相同 | 等价 |
| 附 | LiangCengJiImpl#napai | 最重构方法（1811↔1859 token）：**操作码多重集精确对账**——`pop 130→52`、`getfield 158→80`（恰为 78 组死读三元组）；分支反转 40 处（`goto +41`）；防御性 checkcast +4；`ldc→ldc_w` ×2；其余为槽位改名 | 等价（重排级） |
| 附 | MainController$1 | 匿名类构造器：`javap -p` 显示 `()` 伪差异，`javap -v` 描述符 `(MainController, AutoUpdateThread)V` 两侧一致，方法体逐指令一致 | 等价 |

### 全量分层对账结果

- 84 类中 **51 类**在层级 0–6 之间达到"方法签名数 + 方法体 token 序列完全一致"；33 类共 **147 条 BODY-DIFF 残差**（另有 6 条匿名构造器显示伪差异）。
- 147 条残差按首差异指令归类，**全部落入上表已逐指令核实等价的模式族**：CFR 三元/分支重排（~96，代表样本 #3、#5）、javac21 dup/dup_x1/dup2 栈复用（~14，样本 #8/#10）、CFR 常量折叠（7，样本 #9）、声明类型 invoke 接口指令差异（7，样本 #11）、javac21 防御 checkcast（4）、语句重排/槽位（其余，样本 #6/#12）。
- **未发现任何**非常量等价的差异（无常量值不同、无分支条件单侧翻转不换臂、无指令增删超出上述族）。

### 声明层偏差（非 FAIL，如实记录）

1. **FIXES.md §3 的 DEAD-RECEIVER=11 对与实测 26 个 instance→static lambda 方法不符**（XueZhanAiImpl 3 + CaishengxuanzeImpl 13 + LiangCengJiImpl 7 + ZixuanhuapaiImpl 3）。按其自述归一化规则，这些方法体差异都会落入 DEAD-RECEIVER 桶，计数应≈26。分类**计数偏低**，但等价性论证本身对每个样本均成立（抽验 3 个均为纯死读）。
2. FIXES.md 称"745 对完全相同（仅 lambda 合成方法编号差异）"——实测**合成方法编号未漂移**（`lambda$null$N`↔`lambda$方法名$N` N 一一对应），仅合成名与 static 修饰、Integer/Object 参数渲染差异；其对"编号差异"的表述与实测相符，但 static/Integer-Object 两类签名层差异未在其 §3 分类表中显式列出（本报告 §3 已代为归类）。

## 7. MANIFEST 一致性 — PASS

| 属性 | 原 jar | 新 jar | 判定 |
| --- | --- | --- | --- |
| Main-Class | `quesheng.controller.MainApp` | 同 | 一致 |
| Class-Path | 12 个依赖 jar（MobileIMSDK4j/MobileIMSDKServerX_META/gson/jintellitype/okhttp/okio/ormlite-core/ormlite-jdbc/reactive-streams/rxjava/sqlite-jdbc/swing-worker），顺序一致 | 同（jar 工具重新折行） | 一致（续行折法不同，语义等价） |
| Permissions / JavaFX-Version / Implementation-Title / Implementation-Version | `sandbox` / `8.0` / `queshengdapai` / 空 | 同 | 一致 |
| Created-By | `JavaFX Packager` | `21.0.12.1 (Microsoft)` | **工具署名差异**（打包工具产物，无功能影响，无法由 `jar cfm` 复现原值） |

## UNVERIFIED 项

1. **运行期行为**：按审计约束未运行 quesheng.jar（会连真实服务器 8.137.196.28 / 弹 GUI / 注册全局热键），故"可运行"与 GUI/网络行为未验证（仅字节码/资源/清单层验证）。
2. **FIXES.md 542 条仅抽 16 条**（11.2%）：其余 526 条未逐条回对；但其中 lvtt-generic/dehoist/capture 类的系统性正确性已由 §6 的 84 类全量字节码对账间接覆盖（凡泛型/改名改错，字节码对不齐）。
3. **147 条字节码残差为"模式族"级判定**：每族代表样本已逐指令人工核实，MainController（74 条）与 HuPaiKit（64 条）的残差未做 hunk 级全量人工复核。若需绝对闭合，可对这两个类的残差逐条走 §6 同款流程。
4. javap 21 对部分方法名渲染使用 JDK 21 的反汇编器，与原 jar 编译期工具不同——本报告所有"一致"判定均以 descriptor/Code 字节为准，不受渲染影响；但无法排除 javap 本身对两侧的系统性解析偏差（概率极低，两侧同一 javap 读取）。

## 审计产物与留痕

- 构建日志：`/tmp/audit-build.log`；类清单 diff、资源 sha256 三方比对、84 类分层对账与残差清单、方法体计数等中间产物位于审计临时目录（`/tmp/audit/`：`allclass-cmp.txt`、`residual6.txt`、`all-desc-diffs.txt`、`all-flag-diffs.txt`、`o.txt/n.txt`）。
- 自建对账工具（未入仓库）：分层 javap 比较器与侧侧反汇编 diff（jcmp.py / mshow.py / mdiff.py，置于审计机临时目录）。
- 审计过程中对仓库**零写入、零修改**；`game-project/out` 与 `javafx9/` 由审计的从零构建再生（与声明值一致）。
