/*
 * MakeSconfig.java — 游戏端 sconfig.config 生成器（本地 fixture 环境）
 *
 * 背景（逆向结论，源见 docs/逆向④_游戏端源码还原.md 与 game-src/）：
 *   - sconfig.config 是 quesheng.model.SystemConfig 的 Java 序列化对象，
 *     LoginController.initialize() 经 ObjectKit.getObject("./sconfig.config") 用
 *     ObjectInputStream 读取（相对进程工作目录，即 exe4j 启动目录）。
 *   - choseIp = DESKit.Encrypt(纯 host 字符串)；调用方固定拼接 ":8081"。
 *     DESKit: DES/CBC/PKCS5Padding, key="nihao985"(前 8 字节), IV=keySpec.getKey()=同 8 字节，
 *     输出大写 HEX。
 *   - users: Map<String,String>，key=DES(user_id)，value=DES(password)；
 *     initialize() 会在有记录时预填登录表单（userId 编辑框 + password 框）并勾选记住密码。
 *     注意：initialize() 并不会自动登录——仍需点击"登 录"按钮（processLogin）。
 *   - isFirstLogin=false → 登录页隐藏 IP 输入框，直接用已存 choseIp。
 *
 * 编译（仓库根目录执行）：
 *   javac -cp game-jars/QueshengDaPai.jar tools/MakeSconfig.java
 * 生成：
 *   java -cp "tools;game-jars/QueshengDaPai.jar" MakeSconfig gen out/sconfig.config [host] [userid] [password]
 * 读回校验：
 *   java -cp "tools;game-jars/QueshengDaPai.jar" MakeSconfig read out/sconfig.config
 * DES 工具：
 *   java -cp "tools;game-jars/QueshengDaPai.jar" MakeSconfig enc "127.0.0.1"
 *
 * 说明：直接使用 QueshengDaPai.jar 里的真类（quesheng.model.SystemConfig /
 * quesheng.util.DESKit）序列化，保证 serialVersionUID(-6284005833985478852) 与
 * 字段布局同游戏端 exe 内打包的类一致。其余 ~150 个玩法字段全部沿用类内默认值
 * （int=0 / Integer 默认值 / String 默认文案 / 空集合），与"首次安装未调过设置"状态一致。
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

import quesheng.model.SystemConfig;
import quesheng.util.DESKit;

public class MakeSconfig {

    public static void main(String[] args) throws Exception {
        String cmd = args.length > 0 ? args[0] : "gen";
        if ("gen".equals(cmd)) {
            String out = args.length > 1 ? args[1] : "out/sconfig.config";
            String host = args.length > 2 ? args[2] : "127.0.0.1";
            String user = args.length > 3 ? args[3] : "XCS232";
            String pass = args.length > 4 ? args[4] : "CHANGE_ME";  // 凭据不入库，见 docs/设备登录.md
            gen(out, host, user, pass);
        } else if ("read".equals(cmd)) {
            read(args.length > 1 ? args[1] : "out/sconfig.config");
        } else if ("enc".equals(cmd)) {
            System.out.println(args.length > 1 ? args[1] : "" + " -> " + DESKit.Encrypt(args[1]));
            System.out.println("Encrypt(" + args[1] + ") = " + DESKit.Encrypt(args[1]));
            System.out.println("Decrypt roundtrip = " + DESKit.Decrypt(DESKit.Encrypt(args[1])));
        } else {
            System.err.println("usage: MakeSconfig gen|read|enc ...");
            System.exit(2);
        }
    }

    static void gen(String outPath, String host, String userId, String password) throws Exception {
        SystemConfig c = new SystemConfig();

        // —— fixture 需要覆盖的字段（其余字段沿用类内默认值）——
        c.setChoseIp(DESKit.Encrypt(host));          // 客户端拼 ":8081" 访问 http://<host>:8081
        c.setIsFirstLogin(false);                     // false=登录页隐藏 IP 输入框，用已存 choseIp
        c.getUsers().put(DESKit.Encrypt(userId), DESKit.Encrypt(password)); // 预填登录表单

        File out = new File(outPath);
        out.getParentFile().mkdirs();
        ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(out));
        os.writeObject(c);
        os.close();

        System.out.println("[MakeSconfig] written: " + out.getAbsolutePath());
        System.out.println("[MakeSconfig] choseIp      = " + c.getChoseIp() + "  (DES(" + host + "))");
        System.out.println("[MakeSconfig] isFirstLogin = " + c.getIsFirstLogin());
        System.out.println("[MakeSconfig] users        = {DES(" + userId + "): DES(" + password + ")}");
        System.out.println("[MakeSconfig] serialVersionUID = -6284005833985478852 (来自真类)");
        System.out.println("[MakeSconfig] 其余字段沿用类默认值: mapaicengshu=" + c.mapaicengshu
                + ", wanjiapaishu=" + c.wanjiapaishu
                + ", wanjiarenshu=" + c.wanjiarenshu
                + ", zidingyibeijingyanse=" + java.util.Arrays.toString(c.zidingyibeijingyanse));
    }

    @SuppressWarnings("unchecked")
    static void read(String path) throws Exception {
        ObjectInputStream is = new ObjectInputStream(new FileInputStream(path));
        Object o = is.readObject();
        is.close();
        if (!(o instanceof SystemConfig)) {
            System.out.println("[ReadSconfig] unexpected class: " + o.getClass().getName());
            return;
        }
        SystemConfig c = (SystemConfig) o;
        System.out.println("[ReadSconfig] class        = " + o.getClass().getName());
        System.out.println("[ReadSconfig] choseIp      = " + c.getChoseIp());
        System.out.println("[ReadSconfig] choseIp(DEC) = " + (c.getChoseIp() == null ? null : DESKit.Decrypt(c.getChoseIp())));
        System.out.println("[ReadSconfig] isFirstLogin = " + c.getIsFirstLogin());
        System.out.println("[ReadSconfig] users.size   = " + c.getUsers().size());
        for (Map.Entry<String, String> e : c.getUsers().entrySet()) {
            System.out.println("[ReadSconfig]   user=" + DESKit.Decrypt(e.getKey())
                    + " pass=" + DESKit.Decrypt(e.getValue())
                    + "  (key=" + e.getKey() + ")");
        }
        System.out.println("[ReadSconfig] mapaicengshu=" + c.mapaicengshu
                + ", wanjiapaishu=" + c.wanjiapaishu
                + ", meishouzhangshu=" + c.meishouzhangshu
                + ", tiaopai=" + c.tiaopai
                + ", zidingyibeijingyanse=" + java.util.Arrays.toString(c.zidingyibeijingyanse));
    }
}
