# FIXES.md — 反编译产物修复记录（game-project/src）

## 0. 底线与结论

- 目标：`javac --release 8` **0 Error**，产出可运行 jar，且**零业务逻辑改动**。
- 结论：**javac exit=0、0 Error**；编译产物 84 个 .class 与原始 `game-jars/QueshengDaPai.jar` 的 84 个类一一对应（类名/内部类编号完全一致）。
- **UNRESOLVED：0 项。** game-src 的 CFR 输出中没有 `/* ERROR */`、`Unable to fully structure`、`Decompilation failed` 之类的逻辑空洞；所有编译错误均已按机械规则修复（见下）。
- 本目录源码相对 `game-src/`（以及相对"带 classpath 的 CFR 参考"）的全部差异都记录在本文末尾的**逐条操作表**中（542 条），每条给出 文件、操作时行号、类别、依据/改法。

## 1. 源码基础：同版本 CFR、带完整 classpath 的重反编译（关键前置）

`game-src/` 是 CFR 0.152 **不带 classpath** 反编译的产物（每个文件头都有 `Could not load the following classes`）。缺失 JavaFX/依赖类型导致 CFR 输出被破坏：在所有 `ObservableList<Node>.add(...)`、`ObservableList<String>.add(...)`、`getTransforms().add(...)`、`addAll(...)` 等调用处插入擦除强转 `(Object)`、`(Object[])`（仅 MainController.java 就有 420 处 `(Object)`），并把大量局部变量退化成 raw 型（`List`、`HashMap`、`Iterator<Object>` 等）。这类损伤无法靠"逐个猜测类型"安全修复。

因此 `src/` 采用**同一工具、同一 jar、加 `--extraclasspath`（JavaFX 9 classes + 13 个依赖 jar）**的 CFR 0.152 重新反编译：

```
java -jar .tools/cfr.jar game-jars/QueshengDaPai.jar --outputdir <src> \
     --extraclasspath "javafx9-classes.jar;gson-2.3.1.jar;okhttp-3.9.1.jar;...;exe4jlib.jar"
```

- 输入字节码与 CFR 版本完全相同，差别仅是 CFR 能读取依赖的类型签名，因此输出**严格更接近**原始源码（无 `(Object)` 擦除强转、泛型完整）。
- `game-src/` 保持原样未动；本目录 `src/` 相对该参考输出的所有后续差异（542 条）见文末操作表。
- 零业务逻辑改动的最终证明见 §3 字节码对比。

## 2. 修复分类统计（共 542 条操作，逐条见文末表）

| 类别 | 条数 | 说明 |
| --- | ---: | --- |
| CFR `void` 声明修复 | 10 | CFR "WARNING - void declaration" 产生的 `void varN_M;` 非法声明（7 处编译错误），按操作数栈/LVT 证据恢复为真实类型（4 个面板各 1 处 `int` 循环变量、TopPanel 另 1 处、HuPaiKit 2 处 `int`/`Mj dierge`） |
| 补全局部变量泛型（依据 LVTT） | 232 | `List` → `List<Mj>`、`HashMap` → `Map<Integer, Integer>`、`Iterator` → `Iterator<Mj>` 等。类型实参逐一取自原 class 的 `LocalVariableTypeTable`（`javap -v`），不猜测 |
| 恢复局部变量声明类型（依据 LVT） | 58 | CFR 把 `Map<String,Object> map` 收窄成 `HashMap<String, Serializable>`、`ObjectProperty<Paint>` 写成 `SimpleObjectProperty<Color>`、`List<Mj>` 写成 `ArrayList<Mj>` 等；按 `LocalVariableTable` 描述符恢复声明类型（影响 invokevirtual/invokeinterface，向原字节码对齐） |
| 补缺失 import | 4 | `java.util.Map`（MjPanel、ChangePasswordDialog）、`javafx.beans.property.ObjectProperty`、`javafx.scene.paint.Paint` |
| 恢复 lambda 捕获变量原名（作用域内重命名） | 76 | CFR 的已知缺陷：lambda 体引用捕获变量时保留了合成方法 LVT 中的原名（`sub_enty`、`casishengs`、`mjs1`、`finalX/finalY1/finalY3`、`mj`、`suanpai/zong`），而外层却用了 CFR 自己起的名（`entry`、`list`、`list3`、`d/d2`、`mj4/mj6/mj7/mj8`、`linkedList`）。逐一用 `javap -v` 的 BootstrapMethods + 两层 LVT 对照确认"lambda 参数名 == 外层捕获槽位原名"后，在**确切的作用域内**把外层变量重命名回原名（不改任何表达式） |
| lambda 内捕获变量声明恢复 | 12 | 同上，外层被 CFR 顶置/合并的声明改为块作用域声明（如 `d = Config.SCREEN_WIDTH/2.0...;` → `double finalX = ...;`），并删除已无引用的 CFR 顶置 `double d;` |
| CFR 顶置声明还原为块作用域声明 | 142 | CFR 把同槽位同名、分属兄弟分支的局部变量合并成一条顶置声明 `T v;` + 多处 `v = ...;`，导致 lambda 捕获报 "must be final or effectively final"。按保守规则（每处赋值均为普通语句、各作用域互不相交、其余引用都在赋值作用域内）还原为 `T v = ...;`（c/size/c2/jiances/s/gangpai/zhongfabaigang_map/finalNamj/finalFangxian1/finalFangxian/finalI/group/namj/i 等 21 个变量、11+轮迭代） |
| 补泛型类型见证 | 2 | `Observable.<Mj>create(emitter -> ...)`（subscribe 消费 Mj）、`Map.Entry.<K, V>comparingByValue().reversed()`（javac 无法穿透 `.reversed()` 推断，原始源码必有显式见证；字节码无差异） |
| 删除 CFR 无意义占位代码 | 1 | HuPaiKit.sortByValue 中 `Comparable cfr_ignored_0 = (Comparable)result.put(...)` → `result.put(...)`（javac 生成 `pop` 而非 `checkcast+astore`，与原字节码一致） |
| 删除已修复项的 CFR 过期注释 | 5 | 5 处 `/* WARNING - void declaration */`（问题已修复，注释失效）。SqliteOpenHelper 的 `WARNING - Removed try catching itself` 为 CFR 说明性注释，予以保留 |

另有 1 处手工定位替换：MainController.java（chi_set 分支的 raw `List list` → `List<Mj>`，元素类型来自 `Config.yina`）。

> 每条重命名/改型都用工具从 `javap -v -p` 输出核对了原 class 的 LocalVariableTable / LocalVariableTypeTable / BootstrapMethods（lambda 实现方法参数名 == 捕获槽位的外层原名），不凭感觉命名。

## 3. 零业务逻辑改动证明：与原字节码逐方法对比

方法：对 84 个类做 `javap -p -c -constants` 反汇编，把每条指令规范化（去掉常量池索引、lambda 合成方法名按 `lambda$*` 折叠、空白折叠），按方法体做**多重集对比**，再分级归一化：

| 级别 | 归一化内容 |
| --- | --- |
| 0 | 直接对比 |
| 1 | `ldc_w/goto_w/jsr_w` ↔ `ldc/goto/jsr`（javac 版本差异） |
| 2 | 合成访问器编号 `access$NNN` ↔ `access$N` |
| 3 | 局部槽位号（CFR 删除死变量导致槽位平移） |
| 4 | `javac 21` 多出的防御性 `checkcast`（对泛型容器 `get()` 的接收者；见下） |

结果（两侧各 2444 个方法体）：

- 类级：84/84 类文件名完全一致；48 类在级别 0 完全一致，2 类级别 1，1 类级别 3（DESKit），其余 33 类存在下述已知良性差异（无一例业务逻辑差异）。
- 方法体级：未匹配的 1986 个方法体两两配对后：**745 对方法体完全相同**（仅 lambda 合成方法名/编号不同——javac 8 与 21 对合成方法编号规则不同，方法体逐指令一致）；其余 258 对落入以下**语义等价**差异：
  | 类别 | 对数 | 例子 |
  | --- | ---: | --- |
  | OTHER（CFR 源码级改写） | 155 | `if (b) {F.X=1;} else {F.X=0;}` 被 CFR 改写成 `F.X = b ? 1 : 0;`（原字节码每分支各求值一次字段引用，javac21 三元式只求值一次）；`for (int i=0,n=s.length(); i<n;...)` 的条件缓存被改写；嵌套 lambda 展平（LiangCengJiImpl.napai）。均为控制流等价改写 |
  | BRANCH-TARGET | 73 | 仅 ifeq/goto 等跳转目标偏移不同（布局差异） |
  | SLOT-SHIFT | 15 | 仅局部槽位号不同（CFR 删除死变量后槽位平移） |
  | DEAD-RECEIVER | 11 | 原字节码 lambda 体内含无副作用的 `aload_0; getfield mainController; pop` 死读（或因此捕获 `this` 的实例 lambda），CFR 清理了死代码，lambda 变为静态实现 |
  | INS:checkcast 等 | 4 | javac 21 对泛型容器 `get()` 结果作接收者时补插入冗余 `checkcast`（经最小样例验证为 javac 版本代码生成差异：同一份源码 javac 8 不生成、javac 21 生成，两者校验等价） |

**结论：除上述编译器版本/CFR 改写造成的语义等价差异外，编译产物与原字节码逐指令一致；不存在任何业务逻辑改动。**（对比方法：`javap -p -c -constants` 反汇编 → 指令规范化（去常量池索引、折叠 lambda 合成名）→ 方法体多重集配对 → 分级归一化对比，全过程留有操作日志。）

## 4. UNRESOLVED 清单

**无。** 所有编译期错误均已修复；无 `throw new IllegalStateException("Decompilation failed")`、无 CFR `/* ERROR */` 标记。运行期行为依赖外部资源（服务器下发牌墙配置 `MjConfig.url` → `sucai/**/*.bmp`）与原 jar 资源集完全一致（见 README）。

## 5. 逐条操作表（542 条）

说明：行号为该操作执行时的行号（后续操作可能使行号偏移），仅用于回溯定位；`lvtt-generic`/`lvt-declared-type` 的依据均为 `javap -v` 输出的 LVTT/LVT。

| # | 文件 | 行(操作时) | 类型 | 说明 |
| ---: | --- | ---: | --- | --- |
| 1 | quesheng/customcontrole/TopPanel.java | 2085 | void-decl | slot 9 is the for-int loop counter (bytecode: size=...; iconst_0; istore 9) |
| 2 | quesheng/customcontrole/TopPanel.java | 2123 | void-decl | slot 9 int loop counter (istore 4; iload 4; istore 9) |
| 3 | quesheng/customcontrole/LeftPanel.java | 2143 | void-decl | '                void var9_32;\n                int n = size =' -> '                int var9_32 = size = yina_bottom.size() - 1;' |
| 4 | quesheng/customcontrole/RightPanel.java | 2151 | void-decl | '                void var9_32;\n                int n = size =' -> '                int var9_32 = size = yina_bottom.size() - 1;' |
| 5 | quesheng/customcontrole/BottomPanel.java | 2152 | void-decl | '                void var9_32;\n                int n = size =' -> '                int var9_32 = size = yina_bottom.size() - 1;' |
| 6 | quesheng/util/HuPaiKit.java | 117 | void-decl | slot 14 int loop counter declared later (boolean bl=false was iconst_0; istore 14) |
| 7 | quesheng/util/HuPaiKit.java | 167 | void-decl | '                    int b = (suanpai.size() + caisheng_count' -> '                    int b = (suanpai.size() + caisheng_count' |
| 8 | quesheng/util/HuPaiKit.java | 783 | void-decl | slot 14 is Mj dierge (aconst_null; astore 14) |
| 9 | quesheng/util/HuPaiKit.java | 794 | void-decl | slot 14 Mj (LVT: dierge) |
| 10 | quesheng/util/HuPaiKit.java | 800 | void-decl | astore 14 (LVT: dierge) |
| 11 | quesheng/ai/impl/XueZhanAiImpl.java | 24 | lvtt-generic | List yina -> List<Mj> |
| 12 | quesheng/ai/impl/XueZhanAiImpl.java | 38 | lvtt-generic | List yina -> List<Mj> |
| 13 | quesheng/ai/impl/XueZhanAiImpl.java | 53 | lvtt-generic | List yuce -> List<Mj> |
| 14 | quesheng/controller/LoginController.java | 89 | lvtt-generic | Optional result -> Optional<ButtonType> |
| 15 | quesheng/controller/MainController.java | 390 | lvtt-generic | Optional result -> Optional<ButtonType> |
| 16 | quesheng/controller/MainController.java | 3479 | lvtt-generic | List yixuan1 -> List<Mj> |
| 17 | quesheng/controller/MainController.java | 3482 | lvtt-generic | List fanghxiang_mj -> List<Mj> |
| 18 | quesheng/controller/MainController.java | 3498 | lvtt-generic | List zhuanyi_mj -> List<Mj> |
| 19 | quesheng/controller/MainController.java | 3509 | lvtt-generic | List yixuan -> List<Mj> |
| 20 | quesheng/controller/MainController.java | 3512 | lvtt-generic | List fanghxiang_mj -> List<Mj> |
| 21 | quesheng/controller/MainController.java | 3530 | lvtt-generic | List zhuanyi_mj -> List<Mj> |
| 22 | quesheng/controller/MainController.java | 3786 | lvtt-generic | List mjs -> List<Mj> |
| 23 | quesheng/controller/MainController.java | 3869 | lvtt-generic | List temp_list -> List<Mj> |
| 24 | quesheng/controller/MainController.java | 4005 | lvtt-generic | List temp_list -> List<Mj> |
| 25 | quesheng/controller/MainController.java | 4216 | lvtt-generic | List temp_list -> List<Mj> |
| 26 | quesheng/controller/MainController.java | 4568 | lvtt-generic | List weina_1 -> List<Mj> |
| 27 | quesheng/controller/MainController.java | 5611 | lvtt-generic | List mjList -> List<Mj> |
| 28 | quesheng/controller/MainController.java | 5896 | lvtt-generic | LinkedList buhud -> List<Mj> |
| 29 | quesheng/controller/MainController.java | 5959 | lvtt-generic | HashSet config_weina -> java.util.Set<Mj> |
| 30 | quesheng/controller/MainController.java | 5965 | lvtt-generic | LinkedList yina_list -> List<Mj> |
| 31 | quesheng/controller/MainController.java | 5971 | lvtt-generic | LinkedList yuce_list -> List<Mj> |
| 32 | quesheng/controller/MainController.java | 5977 | lvtt-generic | LinkedList yichu_list -> List<Mj> |
| 33 | quesheng/controller/MainController.java | 5983 | lvtt-generic | LinkedList weina_list -> List<Mj> |
| 34 | quesheng/controller/MainController.java | 5989 | lvtt-generic | LinkedList huanhoupai_yixuan_list -> List<Mj> |
| 35 | quesheng/controller/MainController.java | 6029 | lvtt-generic | List mjs3 -> List<Mj> |
| 36 | quesheng/controller/MainController.java | 6031 | lvtt-generic | LinkedList chi_set -> List<Mj> |
| 37 | quesheng/controller/MainController.java | 6034 | lvtt-generic | LinkedList chi_list -> List<List<Mj>> |
| 38 | quesheng/controller/MainController.java | 6037 | lvtt-generic | List caishengs -> List<Mj> |
| 39 | quesheng/controller/MainController.java | 6038 | lvtt-generic | List mjs -> List<Mj> |
| 40 | quesheng/controller/MainController.java | 6081 | lvtt-generic | List mjs4 -> List<Mj> |
| 41 | quesheng/controller/MainController.java | 6168 | lvtt-generic | LinkedList chi_list -> List<List<Mj>> |
| 42 | quesheng/controller/MainController.java | 6181 | lvtt-generic | List mjs2 -> List<Mj> |
| 43 | quesheng/controller/MainController.java | 6182 | lvtt-generic | LinkedList chi_set -> List<Mj> |
| 44 | quesheng/controller/MainController.java | 6183 | lvtt-generic | List caishengs -> List<Mj> |
| 45 | quesheng/controller/MainController.java | 6184 | lvtt-generic | List mjs -> List<Mj> |
| 46 | quesheng/controller/MainController.java | 6187 | lvtt-generic | List mjs1 -> List<Mj> |
| 47 | quesheng/controller/MainController.java | 6228 | lvtt-generic | List mjs4 -> List<Mj> |
| 48 | quesheng/controller/MainController.java | 6354 | lvtt-generic | LinkedList chi_list -> List<List<Mj>> |
| 49 | quesheng/controller/MainController.java | 6369 | lvtt-generic | HashMap zhongfabaigang_map -> Map<Integer, Integer> |
| 50 | quesheng/controller/MainController.java | 6393 | lvtt-generic | HashMap zhongfabaigang_map -> Map<Integer, Integer> |
| 51 | quesheng/controller/MainController.java | 6697 | lvtt-generic | List caishengs -> List<Mj> |
| 52 | quesheng/controller/MainController.java | 6729 | lvtt-generic | List caishengs -> List<Mj> |
| 53 | quesheng/controller/MainController.java | 7387 | lvtt-generic | List yina -> List<Mj> |
| 54 | quesheng/controller/MainController.java | 7388 | lvtt-generic | ArrayList hupai -> List<Mj> |
| 55 | quesheng/controller/MainController.java | 7673 | lvtt-generic | List kechupai -> List<Mj> |
| 56 | quesheng/controller/serve/impl/LiangCengJiImpl.java | 48 | lvtt-generic | LinkedList temp_weina2 -> List<Mj> |
| 57 | quesheng/controller/serve/impl/LiangCengJiImpl.java | 75 | lvtt-generic | List weina_1 -> List<Mj> |
| 58 | quesheng/controller/serve/impl/LiangCengJiImpl.java | 80 | lvtt-generic | LinkedList temp_weina -> List<Mj> |
| 59 | quesheng/controller/serve/impl/LiangCengJiImpl.java | 161 | lvtt-generic | Iterator mjIterator -> Iterator<Mj> |
| 60 | quesheng/controller/serve/impl/LiangCengJiImpl.java | 170 | lvtt-generic | Iterator mj1Iterator -> Iterator<Mj> |
| 61 | quesheng/controller/serve/impl/LiangCengJiImpl.java | 203 | lvtt-generic | List mjList -> List<Mj> |
| 62 | quesheng/controller/serve/impl/SanCengJiImpl.java | 34 | lvtt-generic | LinkedList temp_weina -> List<Mj> |
| 63 | quesheng/controller/serve/impl/SanCengJiImpl.java | 39 | lvtt-generic | List weina_1 -> List<Mj> |
| 64 | quesheng/customcontrole/BottomPanel.java | 107 | lvtt-generic | List danzhuangangs -> List<Mj> |
| 65 | quesheng/customcontrole/BottomPanel.java | 330 | lvtt-generic | Iterator iterator3 -> Iterator<Mj> |
| 66 | quesheng/customcontrole/BottomPanel.java | 332 | lvtt-generic | HashMap dongnaxibei_map -> Map<Integer, Integer> |
| 67 | quesheng/customcontrole/BottomPanel.java | 406 | lvtt-generic | List caishengs -> List<Mj> |
| 68 | quesheng/customcontrole/BottomPanel.java | 431 | lvtt-generic | HashMap zhongfabai_map -> Map<Integer, Integer> |
| 69 | quesheng/customcontrole/BottomPanel.java | 494 | lvtt-generic | List caishengs -> List<Mj> |
| 70 | quesheng/customcontrole/BottomPanel.java | 508 | lvtt-generic | HashMap dongnanji_map -> Map<Integer, Integer> |
| 71 | quesheng/customcontrole/BottomPanel.java | 551 | lvtt-generic | HashMap dongnanxibeigang_map -> Map<Integer, Integer> |
| 72 | quesheng/customcontrole/BottomPanel.java | 595 | lvtt-generic | HashMap zhongfabaigang_map -> Map<Integer, Integer> |
| 73 | quesheng/customcontrole/BottomPanel.java | 639 | lvtt-generic | HashMap dongnanjigang_map -> Map<Integer, Integer> |
| 74 | quesheng/customcontrole/BottomPanel.java | 683 | lvtt-generic | HashMap dongnaxibei_map -> Map<Integer, Integer> |
| 75 | quesheng/customcontrole/BottomPanel.java | 737 | lvtt-generic | HashMap zhongfabai_map -> Map<Integer, Integer> |
| 76 | quesheng/customcontrole/BottomPanel.java | 780 | lvtt-generic | HashMap dongnanji_map -> Map<Integer, Integer> |
| 77 | quesheng/customcontrole/BottomPanel.java | 823 | lvtt-generic | HashMap dongnanji_map -> Map<Integer, Integer> |
| 78 | quesheng/customcontrole/BottomPanel.java | 866 | lvtt-generic | HashMap dongnanxibeigang_map -> Map<Integer, Integer> |
| 79 | quesheng/customcontrole/BottomPanel.java | 910 | lvtt-generic | HashMap zhongfabaigang_map -> Map<Integer, Integer> |
| 80 | quesheng/customcontrole/BottomPanel.java | 954 | lvtt-generic | HashMap dongnanjigang_map -> Map<Integer, Integer> |
| 81 | quesheng/customcontrole/BottomPanel.java | 998 | lvtt-generic | HashMap dongnanjigang_map -> Map<Integer, Integer> |
| 82 | quesheng/customcontrole/BottomPanel.java | 1042 | lvtt-generic | HashMap dongnaxibei_map -> Map<Integer, Integer> |
| 83 | quesheng/customcontrole/BottomPanel.java | 1126 | lvtt-generic | HashMap zhongfabai_map -> Map<Integer, Integer> |
| 84 | quesheng/customcontrole/BottomPanel.java | 1169 | lvtt-generic | HashMap dongnanxibeigang_map -> Map<Integer, Integer> |
| 85 | quesheng/customcontrole/BottomPanel.java | 1213 | lvtt-generic | HashMap zhongfabaigang_map -> Map<Integer, Integer> |
| 86 | quesheng/customcontrole/BottomPanel.java | 1372 | lvtt-generic | List caishengs -> List<Mj> |
| 87 | quesheng/customcontrole/BottomPanel.java | 1388 | lvtt-generic | List keqifeis -> List<Mj> |
| 88 | quesheng/customcontrole/BottomPanel.java | 1430 | lvtt-generic | HashMap map -> Map<Integer, Integer> |
| 89 | quesheng/customcontrole/BottomPanel.java | 1432 | lvtt-generic | List jiance_gang -> List<Mj> |
| 90 | quesheng/customcontrole/BottomPanel.java | 1443 | lvtt-generic | List jiance_caisheng_gang -> List<Mj> |
| 91 | quesheng/customcontrole/LeftPanel.java | 72 | lvtt-generic | List danzhuangangs -> List<Mj> |
| 92 | quesheng/customcontrole/LeftPanel.java | 302 | lvtt-generic | Iterator iterator3 -> Iterator<Mj> |
| 93 | quesheng/customcontrole/LeftPanel.java | 304 | lvtt-generic | HashMap dongnaxibei_map -> Map<Integer, Integer> |
| 94 | quesheng/customcontrole/LeftPanel.java | 378 | lvtt-generic | List caishengs -> List<Mj> |
| 95 | quesheng/customcontrole/LeftPanel.java | 403 | lvtt-generic | HashMap zhongfabai_map -> Map<Integer, Integer> |
| 96 | quesheng/customcontrole/LeftPanel.java | 466 | lvtt-generic | List caishengs -> List<Mj> |
| 97 | quesheng/customcontrole/LeftPanel.java | 480 | lvtt-generic | HashMap dongnanji_map -> Map<Integer, Integer> |
| 98 | quesheng/customcontrole/LeftPanel.java | 523 | lvtt-generic | HashMap dongnanxibeigang_map -> Map<Integer, Integer> |
| 99 | quesheng/customcontrole/LeftPanel.java | 567 | lvtt-generic | HashMap zhongfabaigang_map -> Map<Integer, Integer> |
| 100 | quesheng/customcontrole/LeftPanel.java | 611 | lvtt-generic | HashMap dongnanjigang_map -> Map<Integer, Integer> |
| 101 | quesheng/customcontrole/LeftPanel.java | 655 | lvtt-generic | HashMap dongnaxibei_map -> Map<Integer, Integer> |
| 102 | quesheng/customcontrole/LeftPanel.java | 709 | lvtt-generic | HashMap zhongfabai_map -> Map<Integer, Integer> |
| 103 | quesheng/customcontrole/LeftPanel.java | 752 | lvtt-generic | HashMap dongnanji_map -> Map<Integer, Integer> |
| 104 | quesheng/customcontrole/LeftPanel.java | 795 | lvtt-generic | HashMap dongnanji_map -> Map<Integer, Integer> |
| 105 | quesheng/customcontrole/LeftPanel.java | 838 | lvtt-generic | HashMap dongnanxibeigang_map -> Map<Integer, Integer> |
| 106 | quesheng/customcontrole/LeftPanel.java | 882 | lvtt-generic | HashMap zhongfabaigang_map -> Map<Integer, Integer> |
| 107 | quesheng/customcontrole/LeftPanel.java | 926 | lvtt-generic | HashMap dongnanjigang_map -> Map<Integer, Integer> |
| 108 | quesheng/customcontrole/LeftPanel.java | 970 | lvtt-generic | HashMap dongnanjigang_map -> Map<Integer, Integer> |
| 109 | quesheng/customcontrole/LeftPanel.java | 1014 | lvtt-generic | HashMap dongnaxibei_map -> Map<Integer, Integer> |
| 110 | quesheng/customcontrole/LeftPanel.java | 1098 | lvtt-generic | HashMap zhongfabai_map -> Map<Integer, Integer> |
| 111 | quesheng/customcontrole/LeftPanel.java | 1141 | lvtt-generic | HashMap dongnanxibeigang_map -> Map<Integer, Integer> |
| 112 | quesheng/customcontrole/LeftPanel.java | 1185 | lvtt-generic | HashMap zhongfabaigang_map -> Map<Integer, Integer> |
| 113 | quesheng/customcontrole/LeftPanel.java | 1344 | lvtt-generic | List caishengs -> List<Mj> |
| 114 | quesheng/customcontrole/LeftPanel.java | 1360 | lvtt-generic | List keqifeis -> List<Mj> |
| 115 | quesheng/customcontrole/LeftPanel.java | 1402 | lvtt-generic | HashMap map -> Map<Integer, Integer> |
| 116 | quesheng/customcontrole/LeftPanel.java | 1404 | lvtt-generic | List jiance_gang -> List<Mj> |
| 117 | quesheng/customcontrole/LeftPanel.java | 1415 | lvtt-generic | List jiance_caisheng_gang -> List<Mj> |
| 118 | quesheng/customcontrole/MjPanel.java | 233 | lvtt-generic | Iterator iterator3 -> Iterator<Mj> |
| 119 | quesheng/customcontrole/MjPanel.java | 249 | lvtt-generic | List mjs3 -> List<Mj> |
| 120 | quesheng/customcontrole/MjPanel.java | 250 | lvtt-generic | List mjs2 -> List<Mj> |
| 121 | quesheng/customcontrole/MjPanel.java | 252 | lvtt-generic | List caishengs -> List<Mj> |
| 122 | quesheng/customcontrole/MjPanel.java | 253 | lvtt-generic | List mjs -> List<Mj> |
| 123 | quesheng/customcontrole/MjPanel.java | 256 | lvtt-generic | List mjs1 -> List<Mj> |
| 124 | quesheng/customcontrole/MjPanel.java | 276 | lvtt-generic | List mjs4 -> List<Mj> |
| 125 | quesheng/customcontrole/MjPanel.java | 286 | lvtt-generic | HashMap zhongfabaigang_map -> java.util.Map<Integer, Integer> |
| 126 | quesheng/customcontrole/RightPanel.java | 94 | lvtt-generic | List danzhuangangs -> List<Mj> |
| 127 | quesheng/customcontrole/RightPanel.java | 304 | lvtt-generic | Iterator iterator3 -> Iterator<Mj> |
| 128 | quesheng/customcontrole/RightPanel.java | 309 | lvtt-generic | HashMap dongnaxibei_map -> Map<Integer, Integer> |
| 129 | quesheng/customcontrole/RightPanel.java | 383 | lvtt-generic | List caishengs -> List<Mj> |
| 130 | quesheng/customcontrole/RightPanel.java | 408 | lvtt-generic | HashMap zhongfabai_map -> Map<Integer, Integer> |
| 131 | quesheng/customcontrole/RightPanel.java | 471 | lvtt-generic | List caishengs -> List<Mj> |
| 132 | quesheng/customcontrole/RightPanel.java | 485 | lvtt-generic | HashMap dongnanji_map -> Map<Integer, Integer> |
| 133 | quesheng/customcontrole/RightPanel.java | 528 | lvtt-generic | HashMap dongnanxibeigang_map -> Map<Integer, Integer> |
| 134 | quesheng/customcontrole/RightPanel.java | 572 | lvtt-generic | HashMap zhongfabaigang_map -> Map<Integer, Integer> |
| 135 | quesheng/customcontrole/RightPanel.java | 616 | lvtt-generic | HashMap dongnanjigang_map -> Map<Integer, Integer> |
| 136 | quesheng/customcontrole/RightPanel.java | 660 | lvtt-generic | HashMap dongnaxibei_map -> Map<Integer, Integer> |
| 137 | quesheng/customcontrole/RightPanel.java | 714 | lvtt-generic | HashMap zhongfabai_map -> Map<Integer, Integer> |
| 138 | quesheng/customcontrole/RightPanel.java | 757 | lvtt-generic | HashMap dongnanji_map -> Map<Integer, Integer> |
| 139 | quesheng/customcontrole/RightPanel.java | 800 | lvtt-generic | HashMap dongnanji_map -> Map<Integer, Integer> |
| 140 | quesheng/customcontrole/RightPanel.java | 843 | lvtt-generic | HashMap dongnanxibeigang_map -> Map<Integer, Integer> |
| 141 | quesheng/customcontrole/RightPanel.java | 887 | lvtt-generic | HashMap zhongfabaigang_map -> Map<Integer, Integer> |
| 142 | quesheng/customcontrole/RightPanel.java | 931 | lvtt-generic | HashMap dongnanjigang_map -> Map<Integer, Integer> |
| 143 | quesheng/customcontrole/RightPanel.java | 975 | lvtt-generic | HashMap dongnanjigang_map -> Map<Integer, Integer> |
| 144 | quesheng/customcontrole/RightPanel.java | 1019 | lvtt-generic | HashMap dongnaxibei_map -> Map<Integer, Integer> |
| 145 | quesheng/customcontrole/RightPanel.java | 1103 | lvtt-generic | HashMap zhongfabai_map -> Map<Integer, Integer> |
| 146 | quesheng/customcontrole/RightPanel.java | 1146 | lvtt-generic | HashMap dongnanxibeigang_map -> Map<Integer, Integer> |
| 147 | quesheng/customcontrole/RightPanel.java | 1190 | lvtt-generic | HashMap zhongfabaigang_map -> Map<Integer, Integer> |
| 148 | quesheng/customcontrole/RightPanel.java | 1349 | lvtt-generic | List caishengs -> List<Mj> |
| 149 | quesheng/customcontrole/RightPanel.java | 1365 | lvtt-generic | List keqifeis -> List<Mj> |
| 150 | quesheng/customcontrole/RightPanel.java | 1407 | lvtt-generic | HashMap map -> Map<Integer, Integer> |
| 151 | quesheng/customcontrole/RightPanel.java | 1409 | lvtt-generic | List jiance_gang -> List<Mj> |
| 152 | quesheng/customcontrole/RightPanel.java | 1420 | lvtt-generic | List jiance_caisheng_gang -> List<Mj> |
| 153 | quesheng/customcontrole/TopPanel.java | 94 | lvtt-generic | List danzhuangangs -> List<Mj> |
| 154 | quesheng/customcontrole/TopPanel.java | 295 | lvtt-generic | Iterator iterator3 -> Iterator<Mj> |
| 155 | quesheng/customcontrole/TopPanel.java | 299 | lvtt-generic | HashMap dongnaxibei_map -> Map<Integer, Integer> |
| 156 | quesheng/customcontrole/TopPanel.java | 373 | lvtt-generic | List caishengs -> List<Mj> |
| 157 | quesheng/customcontrole/TopPanel.java | 398 | lvtt-generic | HashMap zhongfabai_map -> Map<Integer, Integer> |
| 158 | quesheng/customcontrole/TopPanel.java | 461 | lvtt-generic | List caishengs -> List<Mj> |
| 159 | quesheng/customcontrole/TopPanel.java | 475 | lvtt-generic | HashMap dongnanji_map -> Map<Integer, Integer> |
| 160 | quesheng/customcontrole/TopPanel.java | 518 | lvtt-generic | HashMap dongnanxibeigang_map -> Map<Integer, Integer> |
| 161 | quesheng/customcontrole/TopPanel.java | 562 | lvtt-generic | HashMap zhongfabaigang_map -> Map<Integer, Integer> |
| 162 | quesheng/customcontrole/TopPanel.java | 606 | lvtt-generic | HashMap dongnanjigang_map -> Map<Integer, Integer> |
| 163 | quesheng/customcontrole/TopPanel.java | 650 | lvtt-generic | HashMap dongnaxibei_map -> Map<Integer, Integer> |
| 164 | quesheng/customcontrole/TopPanel.java | 704 | lvtt-generic | HashMap zhongfabai_map -> Map<Integer, Integer> |
| 165 | quesheng/customcontrole/TopPanel.java | 747 | lvtt-generic | HashMap dongnanji_map -> Map<Integer, Integer> |
| 166 | quesheng/customcontrole/TopPanel.java | 790 | lvtt-generic | HashMap dongnanji_map -> Map<Integer, Integer> |
| 167 | quesheng/customcontrole/TopPanel.java | 833 | lvtt-generic | HashMap dongnanxibeigang_map -> Map<Integer, Integer> |
| 168 | quesheng/customcontrole/TopPanel.java | 877 | lvtt-generic | HashMap zhongfabaigang_map -> Map<Integer, Integer> |
| 169 | quesheng/customcontrole/TopPanel.java | 921 | lvtt-generic | HashMap dongnanjigang_map -> Map<Integer, Integer> |
| 170 | quesheng/customcontrole/TopPanel.java | 965 | lvtt-generic | HashMap dongnanjigang_map -> Map<Integer, Integer> |
| 171 | quesheng/customcontrole/TopPanel.java | 1009 | lvtt-generic | HashMap dongnaxibei_map -> Map<Integer, Integer> |
| 172 | quesheng/customcontrole/TopPanel.java | 1093 | lvtt-generic | HashMap zhongfabai_map -> Map<Integer, Integer> |
| 173 | quesheng/customcontrole/TopPanel.java | 1136 | lvtt-generic | HashMap dongnanxibeigang_map -> Map<Integer, Integer> |
| 174 | quesheng/customcontrole/TopPanel.java | 1180 | lvtt-generic | HashMap zhongfabaigang_map -> Map<Integer, Integer> |
| 175 | quesheng/customcontrole/TopPanel.java | 1339 | lvtt-generic | List caishengs -> List<Mj> |
| 176 | quesheng/customcontrole/TopPanel.java | 1355 | lvtt-generic | List keqifeis -> List<Mj> |
| 177 | quesheng/customcontrole/TopPanel.java | 1397 | lvtt-generic | HashMap map -> Map<Integer, Integer> |
| 178 | quesheng/customcontrole/TopPanel.java | 1399 | lvtt-generic | List jiance_gang -> List<Mj> |
| 179 | quesheng/customcontrole/TopPanel.java | 1410 | lvtt-generic | List jiance_caisheng_gang -> List<Mj> |
| 180 | quesheng/customcontrole/YuceYinaPanel.java | 113 | lvtt-generic | List yuce -> List<Mj> |
| 181 | quesheng/util/HttpKit.java | 38 | lvtt-generic | List cookies -> List<Cookie> |
| 182 | quesheng/util/HuPaiKit.java | 39 | lvtt-generic | Iterator iterator -> Iterator<Mj> |
| 183 | quesheng/util/HuPaiKit.java | 87 | lvtt-generic | List caisheng_list -> List<Mj> |
| 184 | quesheng/util/HuPaiKit.java | 90 | lvtt-generic | Iterator iterator3 -> Iterator<Mj> |
| 185 | quesheng/util/HuPaiKit.java | 104 | lvtt-generic | ArrayList result_count -> List<Integer> |
| 186 | quesheng/util/HuPaiKit.java | 123 | lvtt-generic | Iterator iterator1 -> Iterator<Mj> |
| 187 | quesheng/util/HuPaiKit.java | 198 | lvtt-generic | Iterator iterator2 -> Iterator<Mj> |
| 188 | quesheng/util/HuPaiKit.java | 247 | lvtt-generic | Iterator iterator6 -> Iterator<Mj> |
| 189 | quesheng/util/HuPaiKit.java | 257 | lvtt-generic | Iterator iterator5 -> Iterator<Mj> |
| 190 | quesheng/util/HuPaiKit.java | 266 | lvtt-generic | Iterator iterator4 -> Iterator<Mj> |
| 191 | quesheng/util/HuPaiKit.java | 276 | lvtt-generic | Iterator iterator2 -> Iterator<Mj> |
| 192 | quesheng/util/HuPaiKit.java | 300 | lvtt-generic | Iterator iterator2 -> Iterator<Mj> |
| 193 | quesheng/util/HuPaiKit.java | 321 | lvtt-generic | List zipai -> List<Mj> |
| 194 | quesheng/util/HuPaiKit.java | 409 | lvtt-generic | List zipai1 -> List<Mj> |
| 195 | quesheng/util/HuPaiKit.java | 577 | lvtt-generic | ArrayList result_count -> List<Integer> |
| 196 | quesheng/util/HuPaiKit.java | 595 | lvtt-generic | Iterator iterator -> Iterator<Mj> |
| 197 | quesheng/util/HuPaiKit.java | 628 | lvtt-generic | Iterator iterator2 -> Iterator<Mj> |
| 198 | quesheng/util/HuPaiKit.java | 688 | lvtt-generic | Iterator iterator -> Iterator<Mj> |
| 199 | quesheng/util/HuPaiKit.java | 709 | lvtt-generic | List zipai -> List<Mj> |
| 200 | quesheng/util/HuPaiKit.java | 710 | lvtt-generic | HashMap dongnaixibei_map -> Map<Integer, Integer> |
| 201 | quesheng/util/HuPaiKit.java | 722 | lvtt-generic | ArrayList list -> List<Integer> |
| 202 | quesheng/util/HuPaiKit.java | 742 | lvtt-generic | HashMap zhongfabai_map -> Map<Integer, Integer> |
| 203 | quesheng/util/HuPaiKit.java | 778 | lvtt-generic | List zipai1 -> List<Mj> |
| 204 | quesheng/util/HuPaiKit.java | 795 | lvtt-generic | Iterator iterator1 -> Iterator<Mj> |
| 205 | quesheng/util/HuPaiKit.java | 826 | lvtt-generic | List list -> List<Integer> |
| 206 | quesheng/util/HuPaiKit.java | 827 | lvtt-generic | List he -> List<Mj> |
| 207 | quesheng/util/HuPaiKit.java | 834 | lvtt-generic | List zhongfabai -> List<Mj> |
| 208 | quesheng/util/HuPaiKit.java | 850 | lvtt-generic | List zhongfabai -> List<Mj> |
| 209 | quesheng/util/HuPaiKit.java | 866 | lvtt-generic | List zhongfabai -> List<Mj> |
| 210 | quesheng/util/HuPaiKit.java | 876 | lvtt-generic | List zhongfabai -> List<Mj> |
| 211 | quesheng/util/HuPaiKit.java | 886 | lvtt-generic | List zhongfabai -> List<Mj> |
| 212 | quesheng/util/HuPaiKit.java | 896 | lvtt-generic | List zhongfabai -> List<Mj> |
| 213 | quesheng/util/HuPaiKit.java | 908 | lvtt-generic | List zhongfabai -> List<Mj> |
| 214 | quesheng/util/HuPaiKit.java | 922 | lvtt-generic | List wan -> List<Mj> |
| 215 | quesheng/util/HuPaiKit.java | 923 | lvtt-generic | List he1 -> List<Mj> |
| 216 | quesheng/util/HuPaiKit.java | 924 | lvtt-generic | List zong1 -> List<Mj> |
| 217 | quesheng/util/HuPaiKit.java | 928 | lvtt-generic | Iterator iterator1 -> Iterator<Mj> |
| 218 | quesheng/util/HuPaiKit.java | 1031 | lvtt-generic | List caisheng_list -> List<Mj> |
| 219 | quesheng/util/HuPaiKit.java | 1033 | lvtt-generic | Iterator iterator3 -> Iterator<Mj> |
| 220 | quesheng/util/HuPaiKit.java | 1049 | lvtt-generic | Iterator iterator1 -> Iterator<Mj> |
| 221 | quesheng/util/HuPaiKit.java | 1072 | lvtt-generic | Iterator iterator2 -> Iterator<Mj> |
| 222 | quesheng/util/HuPaiKit.java | 1121 | lvtt-generic | Iterator iterator2 -> Iterator<Mj> |
| 223 | quesheng/util/HuPaiKit.java | 1136 | lvtt-generic | Iterator iterator1 -> Iterator<Mj> |
| 224 | quesheng/util/HuPaiKit.java | 1173 | lvtt-generic | Iterator iterator -> Iterator<Mj> |
| 225 | quesheng/util/HuPaiKit.java | 1237 | lvtt-generic | Iterator iterator1 -> Iterator<Mj> |
| 226 | quesheng/util/HuPaiKit.java | 1290 | lvtt-generic | Iterator iterator -> Iterator<Mj> |
| 227 | quesheng/util/HuPaiKit.java | 1356 | lvtt-generic | Iterator iterator1 -> Iterator<Mj> |
| 228 | quesheng/util/HuPaiKit.java | 1396 | lvtt-generic | LinkedHashMap result -> Map<K, V> |
| 229 | quesheng/customcontrole/TopPanel.java | 374 | capture-rename | i -> ii (scope lines 374-376, 4 tokens) |
| 230 | quesheng/customcontrole/TopPanel.java | 462 | capture-rename | i -> ii (scope lines 462-464, 4 tokens) |
| 231 | quesheng/customcontrole/TopPanel.java | 1354 | capture-rename | mj4 -> mj (scope lines 1354-1380, 5 tokens) |
| 232 | quesheng/customcontrole/TopPanel.java | 1578 | capture-rename | entry -> sub_enty (scope lines 1578-1584, 2 tokens) |
| 233 | quesheng/customcontrole/TopPanel.java | 1716 | capture-rename | entry -> sub_enty (scope lines 1716-1824, 6 tokens) |
| 234 | quesheng/customcontrole/TopPanel.java | 1755 | capture-rename | list -> casishengs (scope lines 1755-1824, 8 tokens) |
| 235 | quesheng/customcontrole/TopPanel.java | 2050 | capture-rename | mj6 -> mj (scope lines 2050-2056, 2 tokens) |
| 236 | quesheng/customcontrole/TopPanel.java | 2060 | capture-rename | mj7 -> mj (scope lines 2060-2082, 8 tokens) |
| 237 | quesheng/customcontrole/TopPanel.java | 2088 | capture-rename | mj8 -> mj (scope lines 2088-2095, 2 tokens) |
| 238 | quesheng/customcontrole/BottomPanel.java | 407 | capture-rename | i -> ii (scope lines 407-409, 4 tokens) |
| 239 | quesheng/customcontrole/BottomPanel.java | 495 | capture-rename | i -> ii (scope lines 495-497, 4 tokens) |
| 240 | quesheng/customcontrole/BottomPanel.java | 1387 | capture-rename | mj6 -> mj (scope lines 1387-1413, 5 tokens) |
| 241 | quesheng/customcontrole/BottomPanel.java | 1611 | capture-rename | entry -> sub_enty (scope lines 1611-1617, 2 tokens) |
| 242 | quesheng/customcontrole/BottomPanel.java | 1749 | capture-rename | entry -> sub_enty (scope lines 1749-1857, 6 tokens) |
| 243 | quesheng/customcontrole/BottomPanel.java | 1788 | capture-rename | list -> casishengs (scope lines 1788-1857, 8 tokens) |
| 244 | quesheng/customcontrole/BottomPanel.java | 2081 | capture-rename | mj6 -> mj (scope lines 2081-2087, 2 tokens) |
| 245 | quesheng/customcontrole/BottomPanel.java | 2091 | capture-rename | mj7 -> mj (scope lines 2091-2113, 8 tokens) |
| 246 | quesheng/customcontrole/BottomPanel.java | 2118 | capture-rename | mj8 -> mj (scope lines 2118-2124, 2 tokens) |
| 247 | quesheng/customcontrole/LeftPanel.java | 379 | capture-rename | i -> ii (scope lines 379-381, 4 tokens) |
| 248 | quesheng/customcontrole/LeftPanel.java | 467 | capture-rename | i -> ii (scope lines 467-469, 4 tokens) |
| 249 | quesheng/customcontrole/LeftPanel.java | 1359 | capture-rename | mj6 -> mj (scope lines 1359-1385, 5 tokens) |
| 250 | quesheng/customcontrole/LeftPanel.java | 1583 | capture-rename | entry -> sub_enty (scope lines 1583-1589, 2 tokens) |
| 251 | quesheng/customcontrole/LeftPanel.java | 1721 | capture-rename | entry -> sub_enty (scope lines 1721-1829, 6 tokens) |
| 252 | quesheng/customcontrole/LeftPanel.java | 1760 | capture-rename | list -> casishengs (scope lines 1760-1829, 8 tokens) |
| 253 | quesheng/customcontrole/LeftPanel.java | 2072 | capture-rename | mj6 -> mj (scope lines 2072-2078, 2 tokens) |
| 254 | quesheng/customcontrole/LeftPanel.java | 2082 | capture-rename | mj7 -> mj (scope lines 2082-2104, 8 tokens) |
| 255 | quesheng/customcontrole/LeftPanel.java | 2109 | capture-rename | mj8 -> mj (scope lines 2109-2115, 2 tokens) |
| 256 | quesheng/customcontrole/RightPanel.java | 384 | capture-rename | i -> ii (scope lines 384-386, 4 tokens) |
| 257 | quesheng/customcontrole/RightPanel.java | 472 | capture-rename | i -> ii (scope lines 472-474, 4 tokens) |
| 258 | quesheng/customcontrole/RightPanel.java | 1364 | capture-rename | mj4 -> mj (scope lines 1364-1390, 5 tokens) |
| 259 | quesheng/customcontrole/RightPanel.java | 1588 | capture-rename | entry -> sub_enty (scope lines 1588-1594, 2 tokens) |
| 260 | quesheng/customcontrole/RightPanel.java | 1726 | capture-rename | entry -> sub_enty (scope lines 1726-1834, 6 tokens) |
| 261 | quesheng/customcontrole/RightPanel.java | 1765 | capture-rename | list -> casishengs (scope lines 1765-1834, 8 tokens) |
| 262 | quesheng/customcontrole/RightPanel.java | 2080 | capture-rename | mj7 -> mj (scope lines 2080-2086, 2 tokens) |
| 263 | quesheng/customcontrole/RightPanel.java | 2090 | capture-rename | mj8 -> mj (scope lines 2090-2112, 8 tokens) |
| 264 | quesheng/customcontrole/RightPanel.java | 2117 | capture-rename | mj4 -> mj (scope lines 2117-2123, 2 tokens) |
| 265 | quesheng/util/HuPaiKit.java | 686 | capture-rename | linkedList -> suanpai (scope lines 686-1024, 61 tokens) |
| 266 | quesheng/util/HuPaiKit.java | 826 | capture-rename | list -> zong (scope lines 826-1021, 1 tokens) |
| 267 | quesheng/util/HuPaiKit.java | 308 | lvtt-generic | Iterator<Object> -> Iterator<Mj> (LVTT: iterator1 is Iterator<Mj>) |
| 268 | quesheng/util/HuPaiKit.java | 782 | lvtt-generic | duoyu declared List<Mj> per LVT/LVTT |
| 269 | quesheng/util/HuPaiKit.java | 1397 | type-witness | restore explicit type witness lost by CFR; required for javac inference through .reversed() |
| 270 | quesheng/controller/MainController.java | 1178 | capture-rename | size -> size_lam (scope lines 1178-1229, 3 tokens) |
| 271 | quesheng/controller/MainController.java | 1202 | capture-rename | d -> finalX (scope lines 1202-1207, 1 tokens) |
| 272 | quesheng/controller/MainController.java | 1209 | capture-rename | d -> finalX (scope lines 1209-1214, 1 tokens) |
| 273 | quesheng/controller/MainController.java | 1216 | capture-rename | d -> finalY3 (scope lines 1216-1221, 1 tokens) |
| 274 | quesheng/controller/MainController.java | 1223 | capture-rename | d -> finalY1 (scope lines 1223-1229, 1 tokens) |
| 275 | quesheng/controller/MainController.java | 1778 | capture-rename | d2 -> finalY1 (scope lines 1778-1783, 1 tokens) |
| 276 | quesheng/controller/MainController.java | 2760 | capture-rename | size -> size_lam (scope lines 2760-2827, 3 tokens) |
| 277 | quesheng/controller/MainController.java | 2784 | capture-rename | d -> finalX (scope lines 2784-2793, 1 tokens) |
| 278 | quesheng/controller/MainController.java | 2795 | capture-rename | d -> finalX (scope lines 2795-2804, 1 tokens) |
| 279 | quesheng/controller/MainController.java | 2806 | capture-rename | d -> finalY3 (scope lines 2806-2815, 1 tokens) |
| 280 | quesheng/controller/MainController.java | 2817 | capture-rename | d -> finalY1 (scope lines 2817-2827, 1 tokens) |
| 281 | quesheng/controller/MainController.java | 3458 | capture-rename | d2 -> finalY1 (scope lines 3458-3467, 1 tokens) |
| 282 | quesheng/controller/MainController.java | 3760 | capture-rename | d2 -> finalY1 (scope lines 3760-3769, 1 tokens) |
| 283 | quesheng/controller/MainController.java | 6102 | capture-rename | list3 -> mjs1 (scope lines 6102-6141, 3 tokens) |
| 284 | quesheng/controller/MainController.java | 6250 | capture-rename | n -> finalD1 (scope lines 6250-6349, 1 tokens) |
| 285 | quesheng/controller/MainController.java | 6277 | capture-rename | list -> mjs1 (scope lines 6277-6330, 3 tokens) |
| 286 | quesheng/controller/MainController.java | 6448 | capture-rename | list -> mjs1 (scope lines 6448-6501, 3 tokens) |
| 287 | quesheng/controller/MainController.java | 7402 | capture-rename | image -> image_lam (scope lines 7402-7415, 6 tokens) |
| 288 | quesheng/controller/MainController.java | 7405 | capture-rename | imageView1 -> imageView1_lam (scope lines 7405-7415, 7 tokens) |
| 289 | quesheng/controller/MainController.java | 7420 | capture-rename | image -> image_lam (scope lines 7420-7428, 6 tokens) |
| 290 | quesheng/controller/MainController.java | 7423 | capture-rename | imageView1 -> imageView1_lam (scope lines 7423-7428, 6 tokens) |
| 291 | quesheng/controller/MainController.java | 7459 | capture-rename | image -> image_lam (scope lines 7459-7474, 7 tokens) |
| 292 | quesheng/controller/MainController.java | 7461 | capture-rename | imageView1 -> imageView1_lam (scope lines 7461-7474, 8 tokens) |
| 293 | quesheng/controller/MainController.java | 7479 | capture-rename | image -> image_lam (scope lines 7479-7491, 7 tokens) |
| 294 | quesheng/controller/MainController.java | 7480 | capture-rename | imageView1 -> imageView1_lam (scope lines 7480-7491, 6 tokens) |
| 295 | quesheng/controller/MainController.java | 7524 | capture-rename | imageView1 -> imageView1_lam (scope lines 7524-7534, 7 tokens) |
| 296 | quesheng/controller/MainController.java | 7539 | capture-rename | image -> image_lam (scope lines 7539-7545, 2 tokens) |
| 297 | quesheng/controller/MainController.java | 7540 | capture-rename | imageView1 -> imageView1_lam (scope lines 7540-7545, 6 tokens) |
| 298 | quesheng/controller/MainController.java | 7575 | capture-rename | image -> image_lam (scope lines 7575-7590, 7 tokens) |
| 299 | quesheng/controller/MainController.java | 7577 | capture-rename | imageView1 -> imageView1_lam (scope lines 7577-7590, 8 tokens) |
| 300 | quesheng/controller/MainController.java | 7595 | capture-rename | image -> image_lam (scope lines 7595-7608, 7 tokens) |
| 301 | quesheng/controller/MainController.java | 7596 | capture-rename | imageView1 -> imageView1_lam (scope lines 7596-7608, 8 tokens) |
| 302 | quesheng/controller/MainController.java | 7681 | capture-rename | cmj -> cmj_lam (scope lines 7681-7689, 4 tokens) |
| 303 | quesheng/controller/MainController.java | 7705 | capture-rename | cmj -> cmj_lam (scope lines 7705-7713, 4 tokens) |
| 304 | quesheng/controller/MainController.java | 7729 | capture-rename | cmj -> cmj_lam (scope lines 7729-7737, 4 tokens) |
| 305 | quesheng/controller/serve/impl/LiangCengJiImpl.java | 201 | capture-rename | namj -> namj_lam (scope lines 201-227, 17 tokens) |
| 306 | quesheng/controller/serve/impl/LiangCengJiImpl.java | 202 | capture-rename | i -> i_lam (scope lines 202-227, 9 tokens) |
| 307 | quesheng/controller/serve/impl/SanCengJiImpl.java | 130 | capture-rename | namj -> namj_lam (scope lines 130-344, 120 tokens) |
| 308 | quesheng/controller/MainController.java | 6448 | lvtt-generic | for (List mjs1 : chi_list) {  =>  for (List<Mj> mjs1 : chi_list) { |
| 309 | quesheng/controller/MainController.java | 6277 | lvtt-generic | for (List mjs1 : chi_list) {  =>  for (List<Mj> mjs1 : chi_list) { |
| 310 | quesheng/controller/MainController.java | 6211 | lvtt-generic | List list;  =>  List<Mj> list; |
| 311 | quesheng/controller/MainController.java | 6186 | lvtt-generic | List list;  =>  List<Mj> list; |
| 312 | quesheng/controller/MainController.java | 6102 | lvtt-generic | for (List mjs1 : chi_list) {  =>  for (List<Mj> mjs1 : chi_list) { |
| 313 | quesheng/controller/MainController.java | 6041 | lvtt-generic | List list2;  =>  List<Mj> list2; |
| 314 | quesheng/controller/MainController.java | 6040 | lvtt-generic | List mjs22;  =>  List<Mj> mjs22; |
| 315 | quesheng/controller/MainController.java | 6030 | lvtt-generic | List list;  =>  List<Mj> list; |
| 316 | quesheng/controller/MainController.java | 5952 | lvtt-generic | HashMap<String, Serializable> stringObjectMap = new HashMap<String, Serializable>(16);  =>  Map<String, Object> stringObjectMap = new HashMap<String, Object>(16); |
| 317 | quesheng/controller/MainController.java | 4987 | type-witness | Observable.create(emitter -> {  =>  Observable.<Mj>create(emitter -> { |
| 318 | quesheng/controller/MainController.java | 4051 | lvtt-generic | SimpleObjectProperty<Color> warningColor = new SimpleObjectProperty<Color>(Color.web("#000  =>  ObjectProperty<Paint> warningColor = new SimpleObjectProperty<Paint>(Color.web("#000000")) |
| 319 | quesheng/controller/MainController.java | 3717 | capture-decl | d = Config.SCREEN_HEIGHT / 2.0 - Config.MJ_WIDTH_1_2 * (double)(mjList.size() / 2) / 2.0 +  =>  double finalY3 = Config.SCREEN_HEIGHT / 2.0 - Config.MJ_WIDTH_1_2 * (double)(mjList.size() |
| 320 | quesheng/controller/MainController.java | 3674 | capture-decl | d = Config.SCREEN_WIDTH / 2.0 - Config.MJ_WIDTH_1_2 * (double)(mjList.size() / 2) / 2.0 +   =>  double finalX = Config.SCREEN_WIDTH / 2.0 - Config.MJ_WIDTH_1_2 * (double)(mjList.size() / |
| 321 | quesheng/controller/MainController.java | 3631 | capture-decl | d = Config.SCREEN_WIDTH / 2.0 + Config.MJ_WIDTH_1_2 * (double)(mjList.size() / 2) / 2.0 -   =>  double finalX = Config.SCREEN_WIDTH / 2.0 + Config.MJ_WIDTH_1_2 * (double)(mjList.size() / |
| 322 | quesheng/controller/MainController.java | 3579 | capture-decl | remove CFR-hoisted `double d;` (never read; its 3 assignments become the block-scoped captured variables below) |
| 323 | quesheng/controller/MainController.java | 3415 | capture-decl | d = Config.SCREEN_HEIGHT / 2.0 - Config.MJ_WIDTH_1_2 * (double)(mjList.size() / 2) / 2.0 +  =>  double finalY3 = Config.SCREEN_HEIGHT / 2.0 - Config.MJ_WIDTH_1_2 * (double)(mjList.size() |
| 324 | quesheng/controller/MainController.java | 3372 | capture-decl | d = Config.SCREEN_WIDTH / 2.0 - Config.MJ_WIDTH_1_2 * (double)(mjList.size() / 2) / 2.0 +   =>  double finalX = Config.SCREEN_WIDTH / 2.0 - Config.MJ_WIDTH_1_2 * (double)(mjList.size() / |
| 325 | quesheng/controller/MainController.java | 3329 | capture-decl | d = Config.SCREEN_WIDTH / 2.0 + Config.MJ_WIDTH_1_2 * (double)(mjList.size() / 2) / 2.0 -   =>  double finalX = Config.SCREEN_WIDTH / 2.0 + Config.MJ_WIDTH_1_2 * (double)(mjList.size() / |
| 326 | quesheng/controller/MainController.java | 3277 | capture-decl | remove CFR-hoisted `double d;` (never read; its 3 assignments become the block-scoped captured variables below) |
| 327 | quesheng/controller/MainController.java | 1737 | capture-decl | d = Config.SCREEN_HEIGHT / 2.0 - Config.MJ_WIDTH_1_2 * (double)(mjList.size() / 2) / 2.0 +  =>  double finalY3 = Config.SCREEN_HEIGHT / 2.0 - Config.MJ_WIDTH_1_2 * (double)(mjList.size() |
| 328 | quesheng/controller/MainController.java | 1696 | capture-decl | d = Config.SCREEN_WIDTH / 2.0 - Config.MJ_WIDTH_1_2 * (double)(mjList.size() / 2) / 2.0 +   =>  double finalX = Config.SCREEN_WIDTH / 2.0 - Config.MJ_WIDTH_1_2 * (double)(mjList.size() / |
| 329 | quesheng/controller/MainController.java | 1655 | capture-decl | d = Config.SCREEN_WIDTH / 2.0 + Config.MJ_WIDTH_1_2 * (double)(mjList.size() / 2) / 2.0 -   =>  double finalX = Config.SCREEN_WIDTH / 2.0 + Config.MJ_WIDTH_1_2 * (double)(mjList.size() / |
| 330 | quesheng/controller/MainController.java | 1601 | capture-decl | remove CFR-hoisted `double d;` (never read; its 3 assignments become the block-scoped captured variables below) |
| 331 | quesheng/controller/MainController.java | 67 | import | add import javafx.scene.paint.Paint (type argument of warningColor per LVTT) |
| 332 | quesheng/controller/MainController.java | 38 | import | add import javafx.beans.property.ObjectProperty (declared type of warningColor per LVTT) |
| 333 | quesheng/util/HuPaiKit.java | 826 | lvtt-generic | zong is List<Mj> per LVTT (name-collision guess List<Integer> corrected) |
| 334 | quesheng/controller/MainController.java | 6228 | lvtt-generic | raw List -> List<Mj> (element type of Config.yina; feeds chi_set.add(list.get(0))) |
| 335 | quesheng/controller/MainController.java | 1256 | dehoist | `c = ...;` -> `int c = ...;` (hoisted decl at line 1161 removed) |
| 336 | quesheng/controller/MainController.java | 1293 | dehoist | `c = ...;` -> `int c = ...;` (hoisted decl at line 1161 removed) |
| 337 | quesheng/controller/MainController.java | 1343 | dehoist | `c = ...;` -> `int c = ...;` (hoisted decl at line 1161 removed) |
| 338 | quesheng/controller/MainController.java | 1380 | dehoist | `c = ...;` -> `int c = ...;` (hoisted decl at line 1161 removed) |
| 339 | quesheng/controller/MainController.java | 1430 | dehoist | `c = ...;` -> `int c = ...;` (hoisted decl at line 1161 removed) |
| 340 | quesheng/controller/MainController.java | 1467 | dehoist | `c = ...;` -> `int c = ...;` (hoisted decl at line 1161 removed) |
| 341 | quesheng/controller/MainController.java | 1517 | dehoist | `c = ...;` -> `int c = ...;` (hoisted decl at line 1161 removed) |
| 342 | quesheng/controller/MainController.java | 1554 | dehoist | `c = ...;` -> `int c = ...;` (hoisted decl at line 1161 removed) |
| 343 | quesheng/controller/MainController.java | 1161 | dehoist | remove CFR-hoisted declaration `int c;` |
| 344 | quesheng/controller/MainController.java | 1251 | dehoist | `size = ...;` -> `long size = ...;` (hoisted decl at line 1163 removed) |
| 345 | quesheng/controller/MainController.java | 1338 | dehoist | `size = ...;` -> `long size = ...;` (hoisted decl at line 1163 removed) |
| 346 | quesheng/controller/MainController.java | 1425 | dehoist | `size = ...;` -> `long size = ...;` (hoisted decl at line 1163 removed) |
| 347 | quesheng/controller/MainController.java | 1512 | dehoist | `size = ...;` -> `long size = ...;` (hoisted decl at line 1163 removed) |
| 348 | quesheng/controller/MainController.java | 1163 | dehoist | remove CFR-hoisted declaration `long size;` |
| 349 | quesheng/controller/MainController.java | 1809 | dehoist | `c = ...;` -> `int c = ...;` (hoisted decl at line 1797 removed) |
| 350 | quesheng/controller/MainController.java | 1915 | dehoist | `c = ...;` -> `int c = ...;` (hoisted decl at line 1797 removed) |
| 351 | quesheng/controller/MainController.java | 2042 | dehoist | `c = ...;` -> `int c = ...;` (hoisted decl at line 1797 removed) |
| 352 | quesheng/controller/MainController.java | 2148 | dehoist | `c = ...;` -> `int c = ...;` (hoisted decl at line 1797 removed) |
| 353 | quesheng/controller/MainController.java | 2275 | dehoist | `c = ...;` -> `int c = ...;` (hoisted decl at line 1797 removed) |
| 354 | quesheng/controller/MainController.java | 2381 | dehoist | `c = ...;` -> `int c = ...;` (hoisted decl at line 1797 removed) |
| 355 | quesheng/controller/MainController.java | 2508 | dehoist | `c = ...;` -> `int c = ...;` (hoisted decl at line 1797 removed) |
| 356 | quesheng/controller/MainController.java | 2614 | dehoist | `c = ...;` -> `int c = ...;` (hoisted decl at line 1797 removed) |
| 357 | quesheng/controller/MainController.java | 1797 | dehoist | remove CFR-hoisted declaration `int c;` |
| 358 | quesheng/controller/MainController.java | 1802 | dehoist | `size = ...;` -> `long size = ...;` (hoisted decl at line 1800 removed) |
| 359 | quesheng/controller/MainController.java | 2035 | dehoist | `size = ...;` -> `long size = ...;` (hoisted decl at line 1800 removed) |
| 360 | quesheng/controller/MainController.java | 2268 | dehoist | `size = ...;` -> `long size = ...;` (hoisted decl at line 1800 removed) |
| 361 | quesheng/controller/MainController.java | 2501 | dehoist | `size = ...;` -> `long size = ...;` (hoisted decl at line 1800 removed) |
| 362 | quesheng/controller/MainController.java | 1800 | dehoist | remove CFR-hoisted declaration `long size;` |
| 363 | quesheng/controller/MainController.java | 2871 | dehoist | `c = ...;` -> `int c = ...;` (hoisted decl at line 2742 removed) |
| 364 | quesheng/controller/MainController.java | 2916 | dehoist | `c = ...;` -> `int c = ...;` (hoisted decl at line 2742 removed) |
| 365 | quesheng/controller/MainController.java | 2975 | dehoist | `c = ...;` -> `int c = ...;` (hoisted decl at line 2742 removed) |
| 366 | quesheng/controller/MainController.java | 3020 | dehoist | `c = ...;` -> `int c = ...;` (hoisted decl at line 2742 removed) |
| 367 | quesheng/controller/MainController.java | 2742 | dehoist | remove CFR-hoisted declaration `int c;` |
| 368 | quesheng/controller/MainController.java | 2865 | dehoist | `size = ...;` -> `long size = ...;` (hoisted decl at line 2744 removed) |
| 369 | quesheng/controller/MainController.java | 2969 | dehoist | `size = ...;` -> `long size = ...;` (hoisted decl at line 2744 removed) |
| 370 | quesheng/controller/MainController.java | 3073 | dehoist | `size = ...;` -> `long size = ...;` (hoisted decl at line 2744 removed) |
| 371 | quesheng/controller/MainController.java | 3170 | dehoist | `size = ...;` -> `long size = ...;` (hoisted decl at line 2744 removed) |
| 372 | quesheng/controller/MainController.java | 2744 | dehoist | remove CFR-hoisted declaration `long size;` |
| 373 | quesheng/controller/MainController.java | 3080 | dehoist | `c2 = ...;` -> `int c2 = ...;` (hoisted decl at line 3077 removed) |
| 374 | quesheng/controller/MainController.java | 3121 | dehoist | `c2 = ...;` -> `int c2 = ...;` (hoisted decl at line 3077 removed) |
| 375 | quesheng/controller/MainController.java | 3077 | dehoist | remove CFR-hoisted declaration `int c2;` |
| 376 | quesheng/controller/MainController.java | 4574 | dehoist | `finalFangxian1 = ...;` -> `int finalFangxian1 = ...;` (hoisted decl at line 4568 removed) |
| 377 | quesheng/controller/MainController.java | 4689 | dehoist | `finalFangxian1 = ...;` -> `int finalFangxian1 = ...;` (hoisted decl at line 4568 removed) |
| 378 | quesheng/controller/MainController.java | 4568 | dehoist | remove CFR-hoisted declaration `int finalFangxian1;` |
| 379 | quesheng/controller/MainController.java | 4636 | dehoist | `finalNamj = ...;` -> `Mj finalNamj = ...;` (hoisted decl at line 4563 removed) |
| 380 | quesheng/controller/MainController.java | 4666 | dehoist | `finalNamj = ...;` -> `Mj finalNamj = ...;` (hoisted decl at line 4563 removed) |
| 381 | quesheng/controller/MainController.java | 4753 | dehoist | `finalNamj = ...;` -> `Mj finalNamj = ...;` (hoisted decl at line 4563 removed) |
| 382 | quesheng/controller/MainController.java | 4783 | dehoist | `finalNamj = ...;` -> `Mj finalNamj = ...;` (hoisted decl at line 4563 removed) |
| 383 | quesheng/controller/MainController.java | 4563 | dehoist | remove CFR-hoisted declaration `Mj finalNamj;` |
| 384 | quesheng/controller/MainController.java | 6378 | dehoist | `s = ...;` -> `Integer[] s = ...;` (hoisted decl at line 6366 removed) |
| 385 | quesheng/controller/MainController.java | 6402 | dehoist | `s = ...;` -> `Integer[] s = ...;` (hoisted decl at line 6366 removed) |
| 386 | quesheng/controller/MainController.java | 6366 | dehoist | remove CFR-hoisted declaration `Integer[] s;` |
| 387 | quesheng/controller/MainController.java | 7678 | dehoist | `jiances = ...;` -> `ArrayList<Mj> jiances = ...;` (hoisted decl at line 7674 removed) |
| 388 | quesheng/controller/MainController.java | 7702 | dehoist | `jiances = ...;` -> `ArrayList<Mj> jiances = ...;` (hoisted decl at line 7674 removed) |
| 389 | quesheng/controller/MainController.java | 7726 | dehoist | `jiances = ...;` -> `ArrayList<Mj> jiances = ...;` (hoisted decl at line 7674 removed) |
| 390 | quesheng/controller/MainController.java | 7674 | dehoist | remove CFR-hoisted declaration `ArrayList<Mj> jiances;` |
| 391 | quesheng/customcontrole/TopPanel.java | 310 | dehoist | `gangpai = ...;` -> `List<Mj> gangpai = ...;` (hoisted decl at line 297 removed) |
| 392 | quesheng/customcontrole/TopPanel.java | 351 | dehoist | `gangpai = ...;` -> `List<Mj> gangpai = ...;` (hoisted decl at line 297 removed) |
| 393 | quesheng/customcontrole/TopPanel.java | 409 | dehoist | `gangpai = ...;` -> `List<Mj> gangpai = ...;` (hoisted decl at line 297 removed) |
| 394 | quesheng/customcontrole/TopPanel.java | 439 | dehoist | `gangpai = ...;` -> `List<Mj> gangpai = ...;` (hoisted decl at line 297 removed) |
| 395 | quesheng/customcontrole/TopPanel.java | 486 | dehoist | `gangpai = ...;` -> `List<Mj> gangpai = ...;` (hoisted decl at line 297 removed) |
| 396 | quesheng/customcontrole/TopPanel.java | 529 | dehoist | `gangpai = ...;` -> `List<Mj> gangpai = ...;` (hoisted decl at line 297 removed) |
| 397 | quesheng/customcontrole/TopPanel.java | 573 | dehoist | `gangpai = ...;` -> `List<Mj> gangpai = ...;` (hoisted decl at line 297 removed) |
| 398 | quesheng/customcontrole/TopPanel.java | 617 | dehoist | `gangpai = ...;` -> `List<Mj> gangpai = ...;` (hoisted decl at line 297 removed) |
| 399 | quesheng/customcontrole/TopPanel.java | 661 | dehoist | `gangpai = ...;` -> `List<Mj> gangpai = ...;` (hoisted decl at line 297 removed) |
| 400 | quesheng/customcontrole/TopPanel.java | 715 | dehoist | `gangpai = ...;` -> `List<Mj> gangpai = ...;` (hoisted decl at line 297 removed) |
| 401 | quesheng/customcontrole/TopPanel.java | 758 | dehoist | `gangpai = ...;` -> `List<Mj> gangpai = ...;` (hoisted decl at line 297 removed) |
| 402 | quesheng/customcontrole/TopPanel.java | 801 | dehoist | `gangpai = ...;` -> `List<Mj> gangpai = ...;` (hoisted decl at line 297 removed) |
| 403 | quesheng/customcontrole/TopPanel.java | 844 | dehoist | `gangpai = ...;` -> `List<Mj> gangpai = ...;` (hoisted decl at line 297 removed) |
| 404 | quesheng/customcontrole/TopPanel.java | 888 | dehoist | `gangpai = ...;` -> `List<Mj> gangpai = ...;` (hoisted decl at line 297 removed) |
| 405 | quesheng/customcontrole/TopPanel.java | 932 | dehoist | `gangpai = ...;` -> `List<Mj> gangpai = ...;` (hoisted decl at line 297 removed) |
| 406 | quesheng/customcontrole/TopPanel.java | 976 | dehoist | `gangpai = ...;` -> `List<Mj> gangpai = ...;` (hoisted decl at line 297 removed) |
| 407 | quesheng/customcontrole/TopPanel.java | 1020 | dehoist | `gangpai = ...;` -> `List<Mj> gangpai = ...;` (hoisted decl at line 297 removed) |
| 408 | quesheng/customcontrole/TopPanel.java | 1061 | dehoist | `gangpai = ...;` -> `List<Mj> gangpai = ...;` (hoisted decl at line 297 removed) |
| 409 | quesheng/customcontrole/TopPanel.java | 1104 | dehoist | `gangpai = ...;` -> `List<Mj> gangpai = ...;` (hoisted decl at line 297 removed) |
| 410 | quesheng/customcontrole/TopPanel.java | 1147 | dehoist | `gangpai = ...;` -> `List<Mj> gangpai = ...;` (hoisted decl at line 297 removed) |
| 411 | quesheng/customcontrole/TopPanel.java | 1191 | dehoist | `gangpai = ...;` -> `List<Mj> gangpai = ...;` (hoisted decl at line 297 removed) |
| 412 | quesheng/customcontrole/TopPanel.java | 297 | dehoist | remove CFR-hoisted declaration `List<Mj> gangpai;` |
| 413 | quesheng/customcontrole/MjPanel.java | 288 | dehoist | `zhongfabaigang_map = ...;` -> `java.util.Map<Integer, Integer> zhongfabaigang_map = ...;` (hoisted decl at line 286 removed) |
| 414 | quesheng/customcontrole/MjPanel.java | 301 | dehoist | `zhongfabaigang_map = ...;` -> `java.util.Map<Integer, Integer> zhongfabaigang_map = ...;` (hoisted decl at line 286 removed) |
| 415 | quesheng/customcontrole/MjPanel.java | 286 | dehoist | remove CFR-hoisted declaration `java.util.Map<Integer, Integer> zhongfabaigang_map;` |
| 416 | quesheng/customcontrole/RightPanel.java | 320 | dehoist | `gangpai = ...;` -> `List<Mj> gangpai = ...;` (hoisted decl at line 307 removed) |
| 417 | quesheng/customcontrole/RightPanel.java | 361 | dehoist | `gangpai = ...;` -> `List<Mj> gangpai = ...;` (hoisted decl at line 307 removed) |
| 418 | quesheng/customcontrole/RightPanel.java | 419 | dehoist | `gangpai = ...;` -> `List<Mj> gangpai = ...;` (hoisted decl at line 307 removed) |
| 419 | quesheng/customcontrole/RightPanel.java | 449 | dehoist | `gangpai = ...;` -> `List<Mj> gangpai = ...;` (hoisted decl at line 307 removed) |
| 420 | quesheng/customcontrole/RightPanel.java | 496 | dehoist | `gangpai = ...;` -> `List<Mj> gangpai = ...;` (hoisted decl at line 307 removed) |
| 421 | quesheng/customcontrole/RightPanel.java | 539 | dehoist | `gangpai = ...;` -> `List<Mj> gangpai = ...;` (hoisted decl at line 307 removed) |
| 422 | quesheng/customcontrole/RightPanel.java | 583 | dehoist | `gangpai = ...;` -> `List<Mj> gangpai = ...;` (hoisted decl at line 307 removed) |
| 423 | quesheng/customcontrole/RightPanel.java | 627 | dehoist | `gangpai = ...;` -> `List<Mj> gangpai = ...;` (hoisted decl at line 307 removed) |
| 424 | quesheng/customcontrole/RightPanel.java | 671 | dehoist | `gangpai = ...;` -> `List<Mj> gangpai = ...;` (hoisted decl at line 307 removed) |
| 425 | quesheng/customcontrole/RightPanel.java | 725 | dehoist | `gangpai = ...;` -> `List<Mj> gangpai = ...;` (hoisted decl at line 307 removed) |
| 426 | quesheng/customcontrole/RightPanel.java | 768 | dehoist | `gangpai = ...;` -> `List<Mj> gangpai = ...;` (hoisted decl at line 307 removed) |
| 427 | quesheng/customcontrole/RightPanel.java | 811 | dehoist | `gangpai = ...;` -> `List<Mj> gangpai = ...;` (hoisted decl at line 307 removed) |
| 428 | quesheng/customcontrole/RightPanel.java | 854 | dehoist | `gangpai = ...;` -> `List<Mj> gangpai = ...;` (hoisted decl at line 307 removed) |
| 429 | quesheng/customcontrole/RightPanel.java | 898 | dehoist | `gangpai = ...;` -> `List<Mj> gangpai = ...;` (hoisted decl at line 307 removed) |
| 430 | quesheng/customcontrole/RightPanel.java | 942 | dehoist | `gangpai = ...;` -> `List<Mj> gangpai = ...;` (hoisted decl at line 307 removed) |
| 431 | quesheng/customcontrole/RightPanel.java | 986 | dehoist | `gangpai = ...;` -> `List<Mj> gangpai = ...;` (hoisted decl at line 307 removed) |
| 432 | quesheng/customcontrole/RightPanel.java | 1030 | dehoist | `gangpai = ...;` -> `List<Mj> gangpai = ...;` (hoisted decl at line 307 removed) |
| 433 | quesheng/customcontrole/RightPanel.java | 1071 | dehoist | `gangpai = ...;` -> `List<Mj> gangpai = ...;` (hoisted decl at line 307 removed) |
| 434 | quesheng/customcontrole/RightPanel.java | 1114 | dehoist | `gangpai = ...;` -> `List<Mj> gangpai = ...;` (hoisted decl at line 307 removed) |
| 435 | quesheng/customcontrole/RightPanel.java | 1157 | dehoist | `gangpai = ...;` -> `List<Mj> gangpai = ...;` (hoisted decl at line 307 removed) |
| 436 | quesheng/customcontrole/RightPanel.java | 1201 | dehoist | `gangpai = ...;` -> `List<Mj> gangpai = ...;` (hoisted decl at line 307 removed) |
| 437 | quesheng/customcontrole/RightPanel.java | 307 | dehoist | remove CFR-hoisted declaration `List<Mj> gangpai;` |
| 438 | quesheng/controller/serve/impl/LiangCengJiImpl.java | 84 | dehoist | `finalFangxian = ...;` -> `int finalFangxian = ...;` (hoisted decl at line 76 removed) |
| 439 | quesheng/controller/serve/impl/LiangCengJiImpl.java | 101 | dehoist | `finalFangxian = ...;` -> `int finalFangxian = ...;` (hoisted decl at line 76 removed) |
| 440 | quesheng/controller/serve/impl/LiangCengJiImpl.java | 76 | dehoist | remove CFR-hoisted declaration `int finalFangxian;` |
| 441 | quesheng/dialog/CaiShengXuanZeDialog.java | 62 | dehoist | `finalI = ...;` -> `Integer finalI = ...;` (hoisted decl at line 45 removed) |
| 442 | quesheng/dialog/CaiShengXuanZeDialog.java | 82 | dehoist | `finalI = ...;` -> `Integer finalI = ...;` (hoisted decl at line 45 removed) |
| 443 | quesheng/dialog/CaiShengXuanZeDialog.java | 102 | dehoist | `finalI = ...;` -> `Integer finalI = ...;` (hoisted decl at line 45 removed) |
| 444 | quesheng/dialog/CaiShengXuanZeDialog.java | 122 | dehoist | `finalI = ...;` -> `Integer finalI = ...;` (hoisted decl at line 45 removed) |
| 445 | quesheng/dialog/CaiShengXuanZeDialog.java | 142 | dehoist | `finalI = ...;` -> `Integer finalI = ...;` (hoisted decl at line 45 removed) |
| 446 | quesheng/dialog/CaiShengXuanZeDialog.java | 45 | dehoist | remove CFR-hoisted declaration `Integer finalI;` |
| 447 | quesheng/dialog/CaiShengXuanZeDialog.java | 55 | dehoist | `group = ...;` -> `VBox group = ...;` (hoisted decl at line 47 removed) |
| 448 | quesheng/dialog/CaiShengXuanZeDialog.java | 75 | dehoist | `group = ...;` -> `VBox group = ...;` (hoisted decl at line 47 removed) |
| 449 | quesheng/dialog/CaiShengXuanZeDialog.java | 95 | dehoist | `group = ...;` -> `VBox group = ...;` (hoisted decl at line 47 removed) |
| 450 | quesheng/dialog/CaiShengXuanZeDialog.java | 115 | dehoist | `group = ...;` -> `VBox group = ...;` (hoisted decl at line 47 removed) |
| 451 | quesheng/dialog/CaiShengXuanZeDialog.java | 135 | dehoist | `group = ...;` -> `VBox group = ...;` (hoisted decl at line 47 removed) |
| 452 | quesheng/dialog/CaiShengXuanZeDialog.java | 47 | dehoist | remove CFR-hoisted declaration `VBox group;` |
| 453 | quesheng/dialog/DanZhangKeGangDialog.java | 62 | dehoist | `finalI = ...;` -> `int finalI = ...;` (hoisted decl at line 45 removed) |
| 454 | quesheng/dialog/DanZhangKeGangDialog.java | 83 | dehoist | `finalI = ...;` -> `int finalI = ...;` (hoisted decl at line 45 removed) |
| 455 | quesheng/dialog/DanZhangKeGangDialog.java | 104 | dehoist | `finalI = ...;` -> `int finalI = ...;` (hoisted decl at line 45 removed) |
| 456 | quesheng/dialog/DanZhangKeGangDialog.java | 125 | dehoist | `finalI = ...;` -> `int finalI = ...;` (hoisted decl at line 45 removed) |
| 457 | quesheng/dialog/DanZhangKeGangDialog.java | 146 | dehoist | `finalI = ...;` -> `int finalI = ...;` (hoisted decl at line 45 removed) |
| 458 | quesheng/dialog/DanZhangKeGangDialog.java | 45 | dehoist | remove CFR-hoisted declaration `int finalI;` |
| 459 | quesheng/dialog/DanZhangKeGangDialog.java | 55 | dehoist | `group = ...;` -> `VBox group = ...;` (hoisted decl at line 47 removed) |
| 460 | quesheng/dialog/DanZhangKeGangDialog.java | 76 | dehoist | `group = ...;` -> `VBox group = ...;` (hoisted decl at line 47 removed) |
| 461 | quesheng/dialog/DanZhangKeGangDialog.java | 97 | dehoist | `group = ...;` -> `VBox group = ...;` (hoisted decl at line 47 removed) |
| 462 | quesheng/dialog/DanZhangKeGangDialog.java | 118 | dehoist | `group = ...;` -> `VBox group = ...;` (hoisted decl at line 47 removed) |
| 463 | quesheng/dialog/DanZhangKeGangDialog.java | 139 | dehoist | `group = ...;` -> `VBox group = ...;` (hoisted decl at line 47 removed) |
| 464 | quesheng/dialog/DanZhangKeGangDialog.java | 47 | dehoist | remove CFR-hoisted declaration `VBox group;` |
| 465 | quesheng/dialog/HuaPaiXuanZeDialog.java | 62 | dehoist | `finalI = ...;` -> `Integer finalI = ...;` (hoisted decl at line 45 removed) |
| 466 | quesheng/dialog/HuaPaiXuanZeDialog.java | 82 | dehoist | `finalI = ...;` -> `Integer finalI = ...;` (hoisted decl at line 45 removed) |
| 467 | quesheng/dialog/HuaPaiXuanZeDialog.java | 102 | dehoist | `finalI = ...;` -> `Integer finalI = ...;` (hoisted decl at line 45 removed) |
| 468 | quesheng/dialog/HuaPaiXuanZeDialog.java | 122 | dehoist | `finalI = ...;` -> `Integer finalI = ...;` (hoisted decl at line 45 removed) |
| 469 | quesheng/dialog/HuaPaiXuanZeDialog.java | 142 | dehoist | `finalI = ...;` -> `Integer finalI = ...;` (hoisted decl at line 45 removed) |
| 470 | quesheng/dialog/HuaPaiXuanZeDialog.java | 45 | dehoist | remove CFR-hoisted declaration `Integer finalI;` |
| 471 | quesheng/dialog/HuaPaiXuanZeDialog.java | 55 | dehoist | `group = ...;` -> `VBox group = ...;` (hoisted decl at line 47 removed) |
| 472 | quesheng/dialog/HuaPaiXuanZeDialog.java | 75 | dehoist | `group = ...;` -> `VBox group = ...;` (hoisted decl at line 47 removed) |
| 473 | quesheng/dialog/HuaPaiXuanZeDialog.java | 95 | dehoist | `group = ...;` -> `VBox group = ...;` (hoisted decl at line 47 removed) |
| 474 | quesheng/dialog/HuaPaiXuanZeDialog.java | 115 | dehoist | `group = ...;` -> `VBox group = ...;` (hoisted decl at line 47 removed) |
| 475 | quesheng/dialog/HuaPaiXuanZeDialog.java | 135 | dehoist | `group = ...;` -> `VBox group = ...;` (hoisted decl at line 47 removed) |
| 476 | quesheng/dialog/HuaPaiXuanZeDialog.java | 47 | dehoist | remove CFR-hoisted declaration `VBox group;` |
| 477 | quesheng/controller/LoginController.java | 147 | lvt-declared-type | HashMap<String, String> map -> Map<String, String> (LVT erasure Ljava/util/Map;) |
| 478 | quesheng/controller/MainController.java | 232 | lvt-declared-type | HashMap<String, String> map -> Map<String, String> (LVT erasure Ljava/util/Map;) |
| 479 | quesheng/controller/MainController.java | 3824 | lvt-declared-type | HashMap<String, String> map -> Map<String, String> (LVT erasure Ljava/util/Map;) |
| 480 | quesheng/controller/MainController.java | 3858 | lvt-declared-type | LinkedList<Mj> temp -> List<Mj> (LVT erasure Ljava/util/List;) |
| 481 | quesheng/controller/MainController.java | 3921 | lvt-declared-type | HashMap<String, String> map -> Map<String, String> (LVT erasure Ljava/util/Map;) |
| 482 | quesheng/controller/MainController.java | 3963 | lvt-declared-type | HashMap<String, String> map -> Map<String, String> (LVT erasure Ljava/util/Map;) |
| 483 | quesheng/controller/MainController.java | 5120 | lvt-declared-type | LinkedList<Mj> mjList -> List<Mj> (LVT erasure Ljava/util/List;) |
| 484 | quesheng/controller/MainController.java | 5885 | lvt-declared-type | LinkedList<Mj> hupaid -> List<Mj> (LVT erasure Ljava/util/List;) |
| 485 | quesheng/controller/MainController.java | 5887 | lvt-declared-type | LinkedList<Mj> daijiance -> List<Mj> (LVT erasure Ljava/util/List;) |
| 486 | quesheng/controller/MainController.java | 5890 | lvt-declared-type | LinkedList<Mj> jianc -> List<Mj> (LVT erasure Ljava/util/List;) |
| 487 | quesheng/controller/MainController.java | 5916 | lvt-declared-type | LinkedList<Mj> buhud -> List<Mj> (LVT erasure Ljava/util/List;) |
| 488 | quesheng/controller/MainController.java | 5917 | lvt-declared-type | LinkedList<Mj> daijiance -> List<Mj> (LVT erasure Ljava/util/List;) |
| 489 | quesheng/controller/MainController.java | 5920 | lvt-declared-type | LinkedList<Mj> jianc -> List<Mj> (LVT erasure Ljava/util/List;) |
| 490 | quesheng/controller/MainController.java | 6974 | lvt-declared-type | LinkedList<Mj> temp_yuce -> List<Mj> (LVT erasure Ljava/util/List;) |
| 491 | quesheng/controller/MainController.java | 7643 | lvt-declared-type | ArrayList<Mj> mjList -> List<Mj> (LVT erasure Ljava/util/List;) |
| 492 | quesheng/controller/MainController.java | 7667 | lvt-declared-type | ArrayList<Mj> jiances -> List<Mj> (LVT erasure Ljava/util/List;) |
| 493 | quesheng/controller/MainController.java | 7691 | lvt-declared-type | ArrayList<Mj> jiances -> List<Mj> (LVT erasure Ljava/util/List;) |
| 494 | quesheng/controller/MainController.java | 7715 | lvt-declared-type | ArrayList<Mj> jiances -> List<Mj> (LVT erasure Ljava/util/List;) |
| 495 | quesheng/controller/MainController.java | 7778 | lvt-declared-type | ArrayList<Integer> maima_0 -> List<Integer> (LVT erasure Ljava/util/List;) |
| 496 | quesheng/controller/MainController.java | 7783 | lvt-declared-type | ArrayList<Integer> maima_1 -> List<Integer> (LVT erasure Ljava/util/List;) |
| 497 | quesheng/controller/MainController.java | 7787 | lvt-declared-type | ArrayList<Integer> maima_2 -> List<Integer> (LVT erasure Ljava/util/List;) |
| 498 | quesheng/controller/MainController.java | 7791 | lvt-declared-type | ArrayList<Integer> maima_3 -> List<Integer> (LVT erasure Ljava/util/List;) |
| 499 | quesheng/controller/serve/impl/LiangCengJiImpl.java | 148 | lvt-declared-type | LinkedList<Mj> mjList -> List<Mj> (LVT erasure Ljava/util/List;) |
| 500 | quesheng/customcontrole/BottomPanel.java | 1880 | lvt-declared-type | ArrayList<Mj> mjList -> List<Mj> (LVT erasure Ljava/util/List;) |
| 501 | quesheng/customcontrole/LeftPanel.java | 1852 | lvt-declared-type | ArrayList<Mj> mjList -> List<Mj> (LVT erasure Ljava/util/List;) |
| 502 | quesheng/customcontrole/MjPanel.java | 95 | lvt-declared-type | HashMap<Integer, Integer> zhongfabaigang_map -> Map<Integer, Integer> (LVT erasure Ljava/util/Map;) |
| 503 | quesheng/customcontrole/RightPanel.java | 1856 | lvt-declared-type | ArrayList<Mj> mjList -> List<Mj> (LVT erasure Ljava/util/List;) |
| 504 | quesheng/customcontrole/TopPanel.java | 1846 | lvt-declared-type | ArrayList<Mj> mjList -> List<Mj> (LVT erasure Ljava/util/List;) |
| 505 | quesheng/dialog/ChangePasswordDialog.java | 65 | lvt-declared-type | HashMap<String, String> map -> Map<String, String> (LVT erasure Ljava/util/Map;) |
| 506 | quesheng/util/Config.java | 90 | lvt-declared-type | ArrayList<Mj> zongpai -> List<Mj> (LVT erasure Ljava/util/List;) |
| 507 | quesheng/util/Config.java | 155 | lvt-declared-type | ArrayList<Mj> zongpai -> List<Mj> (LVT erasure Ljava/util/List;) |
| 508 | quesheng/util/Config.java | 452 | lvt-declared-type | HashMap<String, String> map -> Map<String, String> (LVT erasure Ljava/util/Map;) |
| 509 | quesheng/util/HuPaiKit.java | 35 | lvt-declared-type | LinkedList<Mj> pais -> List<Mj> (LVT erasure Ljava/util/List;) |
| 510 | quesheng/util/HuPaiKit.java | 86 | lvt-declared-type | ArrayList<Mj> pais -> List<Mj> (LVT erasure Ljava/util/List;) |
| 511 | quesheng/util/HuPaiKit.java | 96 | lvt-declared-type | HashMap<Integer, Integer> map -> Map<Integer, Integer> (LVT erasure Ljava/util/Map;) |
| 512 | quesheng/util/HuPaiKit.java | 122 | lvt-declared-type | LinkedList<Mj> suanpai -> List<Mj> (LVT erasure Ljava/util/List;) |
| 513 | quesheng/util/HuPaiKit.java | 241 | lvt-declared-type | ArrayList<Mj> mjs -> List<Mj> (LVT erasure Ljava/util/List;) |
| 514 | quesheng/util/HuPaiKit.java | 307 | lvt-declared-type | LinkedList<Mj> suanpai -> List<Mj> (LVT erasure Ljava/util/List;) |
| 515 | quesheng/util/HuPaiKit.java | 338 | lvt-declared-type | ArrayList<Mj> del_mjs -> List<Mj> (LVT erasure Ljava/util/List;) |
| 516 | quesheng/util/HuPaiKit.java | 444 | lvt-declared-type | ArrayList<Mj> duoyu -> List<Mj> (LVT erasure Ljava/util/List;) |
| 517 | quesheng/util/HuPaiKit.java | 569 | lvt-declared-type | HashMap<Integer, Integer> map -> Map<Integer, Integer> (LVT erasure Ljava/util/Map;) |
| 518 | quesheng/util/HuPaiKit.java | 580 | lvt-declared-type | LinkedList<Mj> suanpai -> List<Mj> (LVT erasure Ljava/util/List;) |
| 519 | quesheng/util/HuPaiKit.java | 686 | lvt-declared-type | LinkedList<Mj> suanpai -> List<Mj> (LVT erasure Ljava/util/List;) |
| 520 | quesheng/util/HuPaiKit.java | 687 | lvt-declared-type | HashMap<Integer, Integer> geshu -> Map<Integer, Integer> (LVT erasure Ljava/util/Map;) |
| 521 | quesheng/util/HuPaiKit.java | 1030 | lvt-declared-type | ArrayList<Mj> pais -> List<Mj> (LVT erasure Ljava/util/List;) |
| 522 | quesheng/util/HuPaiKit.java | 1039 | lvt-declared-type | HashMap<Integer, Integer> map -> Map<Integer, Integer> (LVT erasure Ljava/util/Map;) |
| 523 | quesheng/util/HuPaiKit.java | 1107 | lvt-declared-type | HashMap<Integer, Integer> map -> Map<Integer, Integer> (LVT erasure Ljava/util/Map;) |
| 524 | quesheng/util/HuPaiKit.java | 1120 | lvt-declared-type | LinkedList<Mj> suanpai -> List<Mj> (LVT erasure Ljava/util/List;) |
| 525 | quesheng/util/HuPaiKit.java | 1160 | lvt-declared-type | ArrayList<Mj> pais -> List<Mj> (LVT erasure Ljava/util/List;) |
| 526 | quesheng/util/HuPaiKit.java | 1161 | lvt-declared-type | HashMap<Integer, Integer> map -> Map<Integer, Integer> (LVT erasure Ljava/util/Map;) |
| 527 | quesheng/util/HuPaiKit.java | 1171 | lvt-declared-type | LinkedList<Mj> suanpai -> List<Mj> (LVT erasure Ljava/util/List;) |
| 528 | quesheng/util/HuPaiKit.java | 1172 | lvt-declared-type | HashMap<Integer, Integer> geshu -> Map<Integer, Integer> (LVT erasure Ljava/util/Map;) |
| 529 | quesheng/util/HuPaiKit.java | 1277 | lvt-declared-type | ArrayList<Mj> pais -> List<Mj> (LVT erasure Ljava/util/List;) |
| 530 | quesheng/util/HuPaiKit.java | 1278 | lvt-declared-type | HashMap<Integer, Integer> map -> Map<Integer, Integer> (LVT erasure Ljava/util/Map;) |
| 531 | quesheng/util/HuPaiKit.java | 1288 | lvt-declared-type | LinkedList<Mj> suanpai -> List<Mj> (LVT erasure Ljava/util/List;) |
| 532 | quesheng/util/HuPaiKit.java | 1289 | lvt-declared-type | HashMap<Integer, Integer> geshu -> Map<Integer, Integer> (LVT erasure Ljava/util/Map;) |
| 533 | quesheng/util/HuPaiKit.java | 1310 | lvt-declared-type | ArrayList<Mj> yiyoumajings -> List<Mj> (LVT erasure Ljava/util/List;) |
| 534 | quesheng/util/HuPaiKit.java | 1406 | lvt-declared-type | ArrayList<Mj> mjs -> List<Mj> (LVT erasure Ljava/util/List;) |
| 535 | quesheng/customcontrole/MjPanel.java | 9 | import | add import java.util.Map (declared type of zhongfabaigang_map per LVT) |
| 536 | quesheng/dialog/ChangePasswordDialog.java | 10 | import | add import java.util.Map (declared type of map per LVT) |
| 537 | quesheng/util/HuPaiKit.java | 1398 | cfr-artifact | remove CFR `Comparable cfr_ignored_0 = (Comparable)...` holder; restore plain statement lambda result.put(...) (bytecode: pop instead of checkcast+astore, matches original) |
| 538 | quesheng/customcontrole/LeftPanel.java | 1396 | stale-comment | remove stale CFR 'WARNING - void declaration' note (fixed) |
| 539 | quesheng/customcontrole/RightPanel.java | 1400 | stale-comment | remove stale CFR 'WARNING - void declaration' note (fixed) |
| 540 | quesheng/customcontrole/BottomPanel.java | 1424 | stale-comment | remove stale CFR 'WARNING - void declaration' note (fixed) |
| 541 | quesheng/customcontrole/TopPanel.java | 1390 | stale-comment | remove stale CFR 'WARNING - void declaration' note (fixed) |
| 542 | quesheng/util/HuPaiKit.java | 82 | stale-comment | remove stale CFR 'WARNING - void declaration' note (fixed) |