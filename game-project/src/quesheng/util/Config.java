/*
 * Decompiled with CFR 0.152.
 */
package quesheng.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.scene.image.Image;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import quesheng.controller.MainController;
import quesheng.controller.SettingStageController;
import quesheng.model.Mj;
import quesheng.model.MjConfig;
import quesheng.model.SystemConfig;
import quesheng.util.DESKit;
import quesheng.util.HttpKit;
import quesheng.util.param;

public final class Config {
    public static String config_file_url = "./sconfig.config";
    public static String logo_url = "/quesheng/view/image/logo.png";
    public static SystemConfig systemConfig;
    public static Image LANSE;
    public static Image LVSE;
    public static Image HONGSE;
    public static Image GANGFANGXIANG;
    public static Image PENGFANGXIANG;
    public static Image CHIFANGXIANG;
    public static Image GANG;
    public static Image FANGPAO;
    public static Image GANGFANGXIANG_YUCE;
    public static Image PENGFANGXIANG_YUCE;
    public static Image FANGPAO_YUCE;
    public static double SCREEN_WIDTH;
    public static double SCREEN_HEIGHT;
    public static double MJ_WIDTH;
    public static double MJ_HEIGHT;
    public static double MJ_WIDTH_1_2;
    public static double MJ_HEIGHT_1_2;
    public static double MJ_WIDTH_3_4;
    public static double MJ_HEIGHT_3_4;
    public static int MJ_COUNT;
    public static final Set<Mj> weina;
    public static final List<Mj> yina;
    public static final List<Mj> yuce;
    public static final List<Mj> yichu;
    public static final List<Mj> huanhoupai_yixuan;
    public static final List<Mj> jiuzhengpai_yixuan;
    private static List<Image> mjImages;
    public static List<MjConfig> mjConfigs;

    public static Image getImageByNumber(int number) {
        for (int i = 0; i < mjConfigs.size(); ++i) {
            if (number != mjConfigs.get(i).getNumber()) continue;
            return mjImages.get(i);
        }
        return null;
    }

    public static Image getImageByMj(Mj mj) {
        int number = mj.getResult();
        number = !mj.kedi_caisheng || Config.systemConfig.andaitipaixianshi == 1 ? mj.getResult() : mj.yuanpai;
        number = mj.kedi_huapai && (Config.systemConfig.zixuanhuapai == 1 || Config.systemConfig.leqing == 1) ? mj.yuanpai : mj.getResult();
        for (int i = 0; i < mjConfigs.size(); ++i) {
            if (number != mjConfigs.get(i).getNumber()) continue;
            return mjImages.get(i);
        }
        return null;
    }

    public static void xipai(int zhangshu, int fangshu) {
        Mj mj;
        int j;
        int i;
        MJ_COUNT = zhangshu;
        List<Mj> zongpai = new ArrayList<Mj>();
        for (i = 0; i < 27 && zongpai.size() < MJ_COUNT; ++i) {
            for (j = 0; j < 4; ++j) {
                mj = new Mj();
                mj.setSerial_number(123456);
                mj.setResult(mjConfigs.get(i).getNumber());
                zongpai.add(mj);
            }
        }
        while (i < 34 && zongpai.size() < MJ_COUNT) {
            for (j = 0; j < 4; ++j) {
                mj = new Mj();
                mj.setSerial_number(123456);
                mj.setResult(mjConfigs.get(i).getNumber());
                zongpai.add(mj);
            }
            ++i;
        }
        while (i < 42 && zongpai.size() < MJ_COUNT) {
            Mj mj2 = new Mj();
            mj2.setSerial_number(123456);
            mj2.setResult(mjConfigs.get(i).getNumber());
            zongpai.add(mj2);
            ++i;
        }
        while (i < 50 && zongpai.size() < MJ_COUNT) {
            Mj mj3 = new Mj();
            mj3.setSerial_number(123456);
            mj3.setResult(mjConfigs.get(i).getNumber());
            zongpai.add(mj3);
            ++i;
        }
        Collections.shuffle(zongpai);
        if (MJ_COUNT == 72) {
            int i2;
            int j2 = 0;
            for (i2 = 0; i2 < 24; ++i2) {
                mj = (Mj)zongpai.get(j2);
                mj.setMahjong_number(i2);
                mj.setDirection(0);
                weina.add(mj);
                ++j2;
            }
            for (i2 = 0; i2 < 24; ++i2) {
                mj = (Mj)zongpai.get(j2);
                mj.setMahjong_number(i2);
                mj.setDirection(3);
                weina.add(mj);
                ++j2;
            }
            for (i2 = 0; i2 < 24; ++i2) {
                mj = (Mj)zongpai.get(j2);
                mj.setMahjong_number(i2);
                mj.setDirection(1);
                weina.add(mj);
                ++j2;
            }
        }
    }

    public static void xipai(int zhangshu) {
        Mj mj;
        int j;
        int i;
        MJ_COUNT = zhangshu;
        List<Mj> zongpai = new ArrayList<Mj>();
        int l = 4;
        if (MJ_COUNT == 90) {
            l = 5;
        }
        for (i = 0; i < 27 && zongpai.size() < MJ_COUNT; ++i) {
            for (j = 0; j < l; ++j) {
                mj = new Mj();
                mj.setSerial_number(123456);
                mj.setResult(mjConfigs.get(i).getNumber());
                zongpai.add(mj);
            }
        }
        while (i < 34 && zongpai.size() < MJ_COUNT) {
            for (j = 0; j < l; ++j) {
                mj = new Mj();
                mj.setSerial_number(123456);
                mj.setResult(mjConfigs.get(i).getNumber());
                zongpai.add(mj);
            }
            ++i;
        }
        while (i < 42 && zongpai.size() < MJ_COUNT) {
            Mj mj2 = new Mj();
            mj2.setSerial_number(123456);
            mj2.setResult(mjConfigs.get(i).getNumber());
            zongpai.add(mj2);
            ++i;
        }
        while (i < 50 && zongpai.size() < MJ_COUNT) {
            Mj mj3 = new Mj();
            mj3.setSerial_number(123456);
            mj3.setResult(mjConfigs.get(i).getNumber());
            zongpai.add(mj3);
            ++i;
        }
        Collections.shuffle(zongpai);
        if (MJ_COUNT == 112) {
            for (Mj mj4 : zongpai) {
                if (mj4.getResult() != 31) continue;
                mj4.setResult(35);
            }
        }
        if (MJ_COUNT == 72) {
            int j2 = 0;
            for (i = 0; i < 18; ++i) {
                Mj mj5 = (Mj)zongpai.get(j2);
                mj5.setMahjong_number(i);
                mj5.setDirection(0);
                weina.add(mj5);
                ++j2;
            }
            for (i = 0; i < 18; ++i) {
                Mj mj6 = (Mj)zongpai.get(j2);
                mj6.setMahjong_number(i);
                mj6.setDirection(1);
                weina.add(mj6);
                ++j2;
            }
            for (i = 0; i < 18; ++i) {
                Mj mj7 = (Mj)zongpai.get(j2);
                mj7.setMahjong_number(i);
                mj7.setDirection(2);
                weina.add(mj7);
                ++j2;
            }
            for (i = 0; i < 18; ++i) {
                Mj mj8 = (Mj)zongpai.get(j2);
                mj8.setMahjong_number(i);
                mj8.setDirection(3);
                weina.add(mj8);
                ++j2;
            }
        }
        if (MJ_COUNT == 90) {
            int j3 = 0;
            for (i = 0; i < 24; ++i) {
                Mj mj9 = (Mj)zongpai.get(j3);
                mj9.setMahjong_number(i);
                mj9.setDirection(0);
                weina.add(mj9);
                ++j3;
            }
            for (i = 0; i < 20; ++i) {
                Mj mj10 = (Mj)zongpai.get(j3);
                mj10.setMahjong_number(i);
                mj10.setDirection(1);
                weina.add(mj10);
                ++j3;
            }
            for (i = 0; i < 24; ++i) {
                Mj mj11 = (Mj)zongpai.get(j3);
                mj11.setMahjong_number(i);
                mj11.setDirection(2);
                weina.add(mj11);
                ++j3;
            }
            for (i = 0; i < 22; ++i) {
                Mj mj12 = (Mj)zongpai.get(j3);
                mj12.setMahjong_number(i);
                mj12.setDirection(3);
                weina.add(mj12);
                ++j3;
            }
        }
        if (MJ_COUNT == 108) {
            int j4 = 0;
            for (i = 0; i < 28; ++i) {
                Mj mj13 = (Mj)zongpai.get(j4);
                mj13.setMahjong_number(i);
                mj13.setDirection(0);
                weina.add(mj13);
                ++j4;
            }
            for (i = 0; i < 26; ++i) {
                Mj mj14 = (Mj)zongpai.get(j4);
                mj14.setMahjong_number(i);
                mj14.setDirection(1);
                weina.add(mj14);
                ++j4;
            }
            for (i = 0; i < 28; ++i) {
                Mj mj15 = (Mj)zongpai.get(j4);
                mj15.setMahjong_number(i);
                mj15.setDirection(2);
                weina.add(mj15);
                ++j4;
            }
            for (i = 0; i < 26; ++i) {
                Mj mj16 = (Mj)zongpai.get(j4);
                mj16.setMahjong_number(i);
                mj16.setDirection(3);
                weina.add(mj16);
                ++j4;
            }
        }
        if (MJ_COUNT == 112) {
            int j5 = 0;
            for (i = 0; i < 28; ++i) {
                Mj mj17 = (Mj)zongpai.get(j5);
                mj17.setMahjong_number(i);
                mj17.setDirection(0);
                weina.add(mj17);
                ++j5;
            }
            for (i = 0; i < 28; ++i) {
                Mj mj18 = (Mj)zongpai.get(j5);
                mj18.setMahjong_number(i);
                mj18.setDirection(1);
                weina.add(mj18);
                ++j5;
            }
            for (i = 0; i < 28; ++i) {
                Mj mj19 = (Mj)zongpai.get(j5);
                mj19.setMahjong_number(i);
                mj19.setDirection(2);
                weina.add(mj19);
                ++j5;
            }
            for (i = 0; i < 28; ++i) {
                Mj mj20 = (Mj)zongpai.get(j5);
                mj20.setMahjong_number(i);
                mj20.setDirection(3);
                weina.add(mj20);
                ++j5;
            }
        }
        if (MJ_COUNT == 136) {
            if (Config.systemConfig.mapaicengshu == 2) {
                int j6 = 0;
                for (i = 0; i < 34; ++i) {
                    Mj mj21 = (Mj)zongpai.get(j6);
                    mj21.setMahjong_number(i);
                    mj21.setDirection(0);
                    weina.add(mj21);
                    ++j6;
                }
                for (i = 0; i < 34; ++i) {
                    Mj mj22 = (Mj)zongpai.get(j6);
                    mj22.setMahjong_number(i);
                    mj22.setDirection(1);
                    weina.add(mj22);
                    ++j6;
                }
                for (i = 0; i < 34; ++i) {
                    Mj mj23 = (Mj)zongpai.get(j6);
                    mj23.setMahjong_number(i);
                    mj23.setDirection(2);
                    weina.add(mj23);
                    ++j6;
                }
                for (i = 0; i < 34; ++i) {
                    Mj mj24 = (Mj)zongpai.get(j6);
                    mj24.setMahjong_number(i);
                    mj24.setDirection(3);
                    weina.add(mj24);
                    ++j6;
                }
            }
            if (Config.systemConfig.mapaicengshu == 3) {
                int j7 = 0;
                for (i = 0; i < 33; ++i) {
                    Mj mj25 = (Mj)zongpai.get(j7);
                    mj25.setMahjong_number(i);
                    mj25.setDirection(0);
                    weina.add(mj25);
                    ++j7;
                }
                for (i = 0; i < 36; ++i) {
                    Mj mj26 = (Mj)zongpai.get(j7);
                    mj26.setMahjong_number(i);
                    mj26.setDirection(1);
                    weina.add(mj26);
                    ++j7;
                }
                for (i = 0; i < 33; ++i) {
                    Mj mj27 = (Mj)zongpai.get(j7);
                    mj27.setMahjong_number(i);
                    mj27.setDirection(2);
                    weina.add(mj27);
                    ++j7;
                }
                for (i = 0; i < 34; ++i) {
                    Mj mj28 = (Mj)zongpai.get(j7);
                    mj28.setMahjong_number(i);
                    mj28.setDirection(3);
                    weina.add(mj28);
                    ++j7;
                }
            }
        }
        if (MJ_COUNT == 144) {
            int j8 = 0;
            for (i = 0; i < 36; ++i) {
                Mj mj29 = (Mj)zongpai.get(j8);
                mj29.setMahjong_number(i);
                mj29.setDirection(0);
                weina.add(mj29);
                ++j8;
            }
            for (i = 0; i < 36; ++i) {
                Mj mj30 = (Mj)zongpai.get(j8);
                mj30.setMahjong_number(i);
                mj30.setDirection(1);
                weina.add(mj30);
                ++j8;
            }
            for (i = 0; i < 36; ++i) {
                Mj mj31 = (Mj)zongpai.get(j8);
                mj31.setMahjong_number(i);
                mj31.setDirection(2);
                weina.add(mj31);
                ++j8;
            }
            for (i = 0; i < 36; ++i) {
                Mj mj32 = (Mj)zongpai.get(j8);
                mj32.setMahjong_number(i);
                mj32.setDirection(3);
                weina.add(mj32);
                ++j8;
            }
        }
        if (MJ_COUNT == 152) {
            int j9 = 0;
            for (i = 0; i < 38; ++i) {
                Mj mj33 = (Mj)zongpai.get(j9);
                mj33.setMahjong_number(i);
                mj33.setDirection(0);
                weina.add(mj33);
                ++j9;
            }
            for (i = 0; i < 38; ++i) {
                Mj mj34 = (Mj)zongpai.get(j9);
                mj34.setMahjong_number(i);
                mj34.setDirection(1);
                weina.add(mj34);
                ++j9;
            }
            for (i = 0; i < 38; ++i) {
                Mj mj35 = (Mj)zongpai.get(j9);
                mj35.setMahjong_number(i);
                mj35.setDirection(2);
                weina.add(mj35);
                ++j9;
            }
            for (i = 0; i < 38; ++i) {
                Mj mj36 = (Mj)zongpai.get(j9);
                mj36.setMahjong_number(i);
                mj36.setDirection(3);
                weina.add(mj36);
                ++j9;
            }
        }
    }

    public static void initialize(final Runnable runnable) {
        System.out.println("\u5f53\u524d\u7c7b\u7684\u8def\u5f84\uff1a" + new Config().getClass().getResource("").getPath() + "config/sconfig.config");
        Map<String, String> map = new HashMap<String, String>();
        map.put("ajax", "true");
        HttpKit.Post("http://" + DESKit.Decrypt(systemConfig.getChoseIp()) + ":8081" + "/api/mj/getConfigs", map, new Callback(){

            @Override
            public void onFailure(Call call, IOException ioe) {
            }

            @Override
            public void onResponse(Call call, Response rspns) throws IOException {
                String xx = rspns.body().string();
                try {
                    JsonObject result = new JsonParser().parse(xx).getAsJsonObject();
                    if (result.get("success").getAsBoolean()) {
                        if (result.get("list") == null) {
                            return;
                        }
                        mjConfigs = (List)param.gson.fromJson(result.get("list").toString(), new TypeToken<List<MjConfig>>(){}.getType());
                        if (mjConfigs != null && mjConfigs.size() > 0) {
                            ComparatorMj comparator = new ComparatorMj();
                            Collections.sort(mjConfigs, comparator);
                            for (MjConfig mj : mjConfigs) {
                                mjImages.add(new Image(MainController.class.getResourceAsStream("/quesheng/view/image" + mj.getUrl()), MJ_WIDTH, MJ_HEIGHT, false, true));
                            }
                            if (runnable != null) {
                                runnable.run();
                            }
                        }
                        SettingStageController.init2();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static Map<String, Integer> getKey2KeycodeMapping() {
        LinkedHashMap<String, Integer> map = new LinkedHashMap<String, Integer>();
        map.put("\u7a7a\u683c\u952e", 32);
        map.put("a", 65);
        map.put("b", 66);
        map.put("c", 67);
        map.put("d", 68);
        map.put("e", 69);
        map.put("f", 70);
        map.put("g", 71);
        map.put("h", 72);
        map.put("i", 73);
        map.put("j", 74);
        map.put("k", 75);
        map.put("l", 76);
        map.put("m", 77);
        map.put("n", 78);
        map.put("o", 79);
        map.put("p", 80);
        map.put("q", 81);
        map.put("r", 82);
        map.put("s", 83);
        map.put("t", 84);
        map.put("u", 85);
        map.put("v", 86);
        map.put("w", 87);
        map.put("x", 88);
        map.put("y", 89);
        map.put("z", 90);
        map.put("0", 48);
        map.put("1", 49);
        map.put("2", 50);
        map.put("3", 51);
        map.put("4", 52);
        map.put("5", 53);
        map.put("6", 54);
        map.put("7", 55);
        map.put("8", 56);
        map.put("9", 57);
        return map;
    }

    static {
        LANSE = new Image(MainController.class.getResourceAsStream("/quesheng/view/image/lanse.png"));
        LVSE = new Image(MainController.class.getResourceAsStream("/quesheng/view/image/lvse.png"));
        HONGSE = new Image(MainController.class.getResourceAsStream("/quesheng/view/image/hongse.png"));
        GANGFANGXIANG = new Image(MainController.class.getResourceAsStream("/quesheng/view/image/gangfangxiang.png"));
        PENGFANGXIANG = new Image(MainController.class.getResourceAsStream("/quesheng/view/image/pengfangxiang.png"));
        CHIFANGXIANG = new Image(MainController.class.getResourceAsStream("/quesheng/view/image/chifangxiang.png"));
        GANG = new Image(MainController.class.getResourceAsStream("/quesheng/view/image/gang.png"));
        FANGPAO = new Image(MainController.class.getResourceAsStream("/quesheng/view/image/fangpao.png"));
        GANGFANGXIANG_YUCE = new Image(MainController.class.getResourceAsStream("/quesheng/view/image/gangfangxiang_yuce.png"));
        PENGFANGXIANG_YUCE = new Image(MainController.class.getResourceAsStream("/quesheng/view/image/pengfangxiang_yuce.png"));
        FANGPAO_YUCE = new Image(MainController.class.getResourceAsStream("/quesheng/view/image/fangpao_yuce.png"));
        SCREEN_WIDTH = 960.0;
        SCREEN_HEIGHT = 720.0;
        MJ_WIDTH = 62.0;
        MJ_HEIGHT = 100.0;
        MJ_WIDTH_1_2 = 62.0;
        MJ_HEIGHT_1_2 = 100.0;
        MJ_WIDTH_3_4 = 62.0;
        MJ_HEIGHT_3_4 = 100.0;
        MJ_COUNT = 108;
        weina = new HashSet<Mj>(200);
        yina = new LinkedList<Mj>();
        yuce = new LinkedList<Mj>();
        yichu = new LinkedList<Mj>();
        huanhoupai_yixuan = new LinkedList<Mj>();
        jiuzhengpai_yixuan = new LinkedList<Mj>();
        mjImages = new ArrayList<Image>();
    }

    public static final class ComparatorMj
    implements Comparator {
        public int compare(Object obj0, Object obj1) {
            MjConfig user0 = (MjConfig)obj0;
            MjConfig user1 = (MjConfig)obj1;
            return user0.getNumber() - user1.getNumber();
        }
    }
}

