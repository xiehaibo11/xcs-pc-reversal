/*
 * Decompiled with CFR 0.152.
 */
package quesheng.ai;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import quesheng.ai.AiInterface;
import quesheng.controller.MainController;
import quesheng.model.Mj;

public class AbstractAi
implements AiInterface {
    public MainController mainController;
    public int sige = 4;
    public int sange = 3;
    public int liangge = 2;
    public int lianxupai = 2;
    public int hu = 6;
    public int zimo = 12;

    public AbstractAi(MainController mainController) {
        this.mainController = mainController;
        Properties pro = new Properties();
        FileInputStream in = null;
        try {
            in = new FileInputStream("./config.txt");
            pro.load(in);
            in.close();
            this.sige = Integer.parseInt(pro.getProperty("sige", "4"));
            this.sange = Integer.parseInt(pro.getProperty("sange", "3"));
            this.liangge = Integer.parseInt(pro.getProperty("liangge", "2"));
            this.lianxupai = Integer.parseInt(pro.getProperty("lianxupai", "1"));
            this.hu = Integer.parseInt(pro.getProperty("hu", "6"));
            this.zimo = Integer.parseInt(pro.getProperty("zimo", "12"));
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Mj tuijian() {
        return null;
    }
}

