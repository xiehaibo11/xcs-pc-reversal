/*
 * Decompiled with CFR 0.152.
 */
package quesheng.customcontrole;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import quesheng.controller.MainController;
import quesheng.customcontrole.MjPanelInterface;
import quesheng.model.Mj;
import quesheng.util.Config;

public abstract class MjPanel
extends Parent
implements MjPanelInterface {
    Mj hu_mj;
    Mj diyizhangda_mj;
    ImageView hu_text = new ImageView(new Image(MainController.class.getResourceAsStream("/quesheng/view/image/hu.png"), Config.MJ_WIDTH, Config.MJ_WIDTH, true, true));
    ImageView mo_text = new ImageView(new Image(MainController.class.getResourceAsStream("/quesheng/view/image/mo.png"), Config.MJ_WIDTH, Config.MJ_WIDTH, true, true));
    ImageView pang_text = new ImageView(new Image(MainController.class.getResourceAsStream("/quesheng/view/image/peng.png"), Config.MJ_WIDTH, Config.MJ_WIDTH, true, true));
    ImageView gang_text = new ImageView(new Image(MainController.class.getResourceAsStream("/quesheng/view/image/gang.png"), Config.MJ_WIDTH, Config.MJ_WIDTH, true, true));
    Label chi_text = new Label("\u5403");
    int hu = 0;
    int qiangzhi_hu = 0;
    public List<Integer> xueliuchenghe_hus = new ArrayList<Integer>();
    int mo = 0;
    boolean kehu = false;
    int que = -1;
    int direction = -1;
    boolean queren = false;
    ImageView dapaifangxiang_imageview;
    public List<Mj> yina_hupai = new LinkedList<Mj>();
    public String pre_pai = "";
    public EventHandler<MouseEvent> mjClick = null;
    AnchorPane group;

    public boolean kePeng(Mj mj, int d) {
        if (Config.systemConfig.caishengcanyuchipanggang == 1) {
            return Config.systemConfig.pangpai == 1 && Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == d && mj1.getResult() == mj.getResult() && mj1.getPaizhuantai() == 0 && !mj1.caisheng).count() + Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == d && mj1.caisheng && mj1.getPaizhuantai() == 0).count() > 1L;
        }
        if (Config.systemConfig.huapaidaiticaishengpai_foushanmajiang == 1) {
            return Config.systemConfig.pangpai == 1 && Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == d && mj1.getResult() == mj.getResult() && mj1.getPaizhuantai() == 0 && !mj1.caisheng).count() == 2L;
        }
        if (Config.systemConfig.pangpai == 1 && Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == d && mj1.getResult() == mj.getResult() && mj1.getPaizhuantai() == 0 && (!mj1.caisheng || Config.systemConfig.caishenganzhengchangpaichanyuchangpanggang == 1)).count() >= 2L) {
            return true;
        }
        if (Config.systemConfig.pangpai == 1 && Config.systemConfig.huapaikepang_foushanmajiang == 1 && mj.getResult() > 40 && Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == d && mj1.getResult() > 40 && mj1.getPaizhuantai() == 0 && (!mj1.caisheng || Config.systemConfig.caishenganzhengchangpaichanyuchangpanggang == 1)).count() >= 2L) {
            System.out.println("\u82b1\u724c\u53ef\u78b0");
            return true;
        }
        if (Config.systemConfig.guangxi152 == 1) {
            if (mj.getResult() < 10) {
                return Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == d && mj1.getPaizhuantai() == 0 && (mj1.getResult() == mj.getResult() || mj1.getResult() == 56 || mj1.getResult() == 54 || mj1.getResult() == 55)).count() >= 2L;
            }
            if (mj.getResult() > 10 && mj.getResult() < 20) {
                return Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == d && mj1.getPaizhuantai() == 0 && (mj1.getResult() == mj.getResult() || mj1.getResult() == 58 || mj1.getResult() == 54 || mj1.getResult() == 55)).count() >= 2L;
            }
            if (mj.getResult() > 20 && mj.getResult() < 30) {
                return Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == d && mj1.getPaizhuantai() == 0 && (mj1.getResult() == mj.getResult() || mj1.getResult() == 57 || mj1.getResult() == 54 || mj1.getResult() == 55)).count() >= 2L;
            }
            if (mj.getResult() >= 31 && mj.getResult() <= 34) {
                return Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == d && mj1.getPaizhuantai() == 0 && (mj1.getResult() == mj.getResult() || mj1.getResult() == 53 || mj1.getResult() == 55)).count() >= 2L;
            }
            if (mj.getResult() >= 35 && mj.getResult() <= 37) {
                return Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == d && mj1.getPaizhuantai() == 0 && (mj1.getResult() == mj.getResult() || mj1.getResult() == 52 || mj1.getResult() == 55)).count() >= 2L;
            }
        }
        return false;
    }

    public boolean keGang(Mj mj, int d) {
        boolean kegang = false;
        if (Config.systemConfig.caishengcanyuchipanggang == 1) {
            return Config.systemConfig.minggang == 1 && Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == d && mj1.getResult() == mj.getResult() && mj1.getPaizhuantai() == 0 && !mj1.caisheng).count() + Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == d && mj1.caisheng && mj1.getPaizhuantai() == 0).count() > 2L;
        }
        if (Config.systemConfig.huapaidaiticaishengpai_foushanmajiang == 1) {
            return Config.systemConfig.minggang == 1 && Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == d && mj1.getResult() == mj.getResult() && mj1.getPaizhuantai() == 0 && !mj1.caisheng).count() == 3L;
        }
        if (Config.systemConfig.gangshouzhongpai == 1 && mj.getNapai_direction() == -1) {
            return Config.yina.stream().filter(mj1 -> mj.getYuce_direction() == d && mj1.getNapai_direction() == d && mj1.getResult() < 40 && mj1.getPaizhuantai() == 1 && mj1.getResult() == mj.getResult() && mj1.yuanpai == mj.yuanpai && (!mj.caisheng || Config.systemConfig.caishenganzhengchangpaichanyuchangpanggang == 1)).count() >= 3L || Config.systemConfig.minggang == 1 && Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == d && mj1.getResult() < 40 && mj1.getResult() == mj.getResult() && mj1.getPaizhuantai() == 0 && (!mj1.caisheng || Config.systemConfig.caishenganzhengchangpaichanyuchangpanggang == 1)).count() >= 3L;
        }
        boolean bl = kegang = Config.systemConfig.minggang == 1 && Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == d && mj1.getResult() < 40 && mj1.getResult() == mj.getResult() && mj1.getPaizhuantai() == 0 && (!mj1.caisheng || Config.systemConfig.caishenganzhengchangpaichanyuchangpanggang == 1)).count() >= 3L;
        if (Config.systemConfig.dongnanxibeikechikegang == 1 && !kegang && mj.getResult() >= 31 && mj.getResult() <= 34) {
            Map<Integer, Integer> zhongfabaigang_map = new HashMap<Integer, Integer>();
            zhongfabaigang_map.put(mj.getResult(), 1);
            Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == d && mj1.getPaizhuantai() == 0 && mj1.getResult() >= 31 && mj1.getResult() <= 34).collect(Collectors.toList()).forEach(mj1 -> {
                if (zhongfabaigang_map.containsKey(mj1.getResult())) {
                    zhongfabaigang_map.put(mj1.getResult(), (Integer)zhongfabaigang_map.get(mj1.getResult()) + 1);
                } else {
                    zhongfabaigang_map.put(mj1.getResult(), 1);
                }
            });
            kegang = zhongfabaigang_map.keySet().size() == 4;
        }
        return kegang;
    }

    public List<Mj> getPengs(Mj mj, int d) {
        List<Mj> mjs;
        if (Config.systemConfig.huapaidaiticaishengpai_foushanmajiang == 1) {
            return Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == d && mj1.getResult() == mj.getResult() && mj1.getPaizhuantai() == 0 && !mj1.caisheng).collect(Collectors.toList());
        }
        if (Config.systemConfig.pangpai == 1 && Config.systemConfig.huapaikepang_foushanmajiang == 1 && mj.getResult() > 40) {
            System.out.println("\u83b7\u53d6\u78b0\u7684\u724c");
            List<Mj> mjs2 = Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == d && mj1.getResult() > 40 && mj1.getPaizhuantai() == 0 && (!mj1.caisheng || Config.systemConfig.caishenganzhengchangpaichanyuchangpanggang == 1)).collect(Collectors.toList());
            while (mjs2.size() > 2) {
                mjs2.remove(0);
            }
            return mjs2;
        }
        if (Config.systemConfig.guangxi152 == 1) {
            mjs = Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == d && mj1.getResult() == mj.getResult() && mj1.getPaizhuantai() == 0 && (!mj1.caisheng || Config.systemConfig.caishenganzhengchangpaichanyuchangpanggang == 1)).collect(Collectors.toList());
            while (mjs.size() > 2) {
                mjs.remove(0);
            }
            if (mjs.size() == 2) {
                return mjs;
            }
            if (mj.getResult() < 10) {
                mjs = Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == d && mj1.getPaizhuantai() == 0 && (mj1.getResult() == mj.getResult() || mj1.getResult() == 56 || mj1.getResult() == 54 || mj1.getResult() == 55)).collect(Collectors.toList());
                while (mjs.size() > 2) {
                    mjs.removeAll(mjs.stream().filter(mj1 -> mj1.getResult() == 55).collect(Collectors.toList()));
                }
                while (mjs.size() > 2) {
                    mjs.removeAll(mjs.stream().filter(mj1 -> mj1.getResult() == 54).collect(Collectors.toList()));
                }
                while (mjs.size() > 2) {
                    mjs.removeAll(mjs.stream().filter(mj1 -> mj1.getResult() == 56).collect(Collectors.toList()));
                }
                while (mjs.size() > 2) {
                    mjs.remove(0);
                }
                if (mjs.size() == 2) {
                    return mjs;
                }
            }
            if (mj.getResult() > 10 && mj.getResult() < 20) {
                mjs = Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == d && mj1.getPaizhuantai() == 0 && (mj1.getResult() == mj.getResult() || mj1.getResult() == 58 || mj1.getResult() == 54 || mj1.getResult() == 55)).collect(Collectors.toList());
                while (mjs.size() > 2) {
                    mjs.removeAll(mjs.stream().filter(mj1 -> mj1.getResult() == 55).collect(Collectors.toList()));
                }
                while (mjs.size() > 2) {
                    mjs.removeAll(mjs.stream().filter(mj1 -> mj1.getResult() == 54).collect(Collectors.toList()));
                }
                while (mjs.size() > 2) {
                    mjs.removeAll(mjs.stream().filter(mj1 -> mj1.getResult() == 58).collect(Collectors.toList()));
                }
                while (mjs.size() > 2) {
                    mjs.remove(0);
                }
                if (mjs.size() == 2) {
                    return mjs;
                }
            }
            if (mj.getResult() > 20 && mj.getResult() < 30) {
                mjs = Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == d && mj1.getPaizhuantai() == 0 && (mj1.getResult() == mj.getResult() || mj1.getResult() == 57 || mj1.getResult() == 54 || mj1.getResult() == 55)).collect(Collectors.toList());
                while (mjs.size() > 2) {
                    mjs.removeAll(mjs.stream().filter(mj1 -> mj1.getResult() == 55).collect(Collectors.toList()));
                }
                while (mjs.size() > 2) {
                    mjs.removeAll(mjs.stream().filter(mj1 -> mj1.getResult() == 54).collect(Collectors.toList()));
                }
                while (mjs.size() > 2) {
                    mjs.removeAll(mjs.stream().filter(mj1 -> mj1.getResult() == 57).collect(Collectors.toList()));
                }
                while (mjs.size() > 2) {
                    mjs.remove(0);
                }
                if (mjs.size() == 2) {
                    return mjs;
                }
            }
            if (mj.getResult() >= 31 && mj.getResult() <= 34) {
                mjs = Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == d && mj1.getPaizhuantai() == 0 && (mj1.getResult() == mj.getResult() || mj1.getResult() == 53 || mj1.getResult() == 55)).collect(Collectors.toList());
                while (mjs.size() > 2) {
                    mjs.removeAll(mjs.stream().filter(mj1 -> mj1.getResult() == 55).collect(Collectors.toList()));
                }
                while (mjs.size() > 2) {
                    mjs.removeAll(mjs.stream().filter(mj1 -> mj1.getResult() == 53).collect(Collectors.toList()));
                }
                while (mjs.size() > 2) {
                    mjs.remove(0);
                }
                if (mjs.size() == 2) {
                    return mjs;
                }
            }
            if (mj.getResult() >= 35 && mj.getResult() <= 37) {
                mjs = Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == d && mj1.getPaizhuantai() == 0 && (mj1.getResult() == mj.getResult() || mj1.getResult() == 52 || mj1.getResult() == 55)).collect(Collectors.toList());
                while (mjs.size() > 2) {
                    mjs.removeAll(mjs.stream().filter(mj1 -> mj1.getResult() == 55).collect(Collectors.toList()));
                }
                while (mjs.size() > 2) {
                    mjs.removeAll(mjs.stream().filter(mj1 -> mj1.getResult() == 52).collect(Collectors.toList()));
                }
                while (mjs.size() > 2) {
                    mjs.remove(0);
                }
                if (mjs.size() == 2) {
                    return mjs;
                }
            }
        }
        mjs = Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == d && mj1.getResult() == mj.getResult() && mj1.getPaizhuantai() == 0 && (!mj1.caisheng || Config.systemConfig.caishenganzhengchangpaichanyuchangpanggang == 1)).collect(Collectors.toList());
        while (mjs.size() > 2) {
            mjs.remove(0);
        }
        return mjs;
    }

    public List<Mj> getGangs(Mj mj, int d) {
        if (Config.systemConfig.huapaidaiticaishengpai_foushanmajiang == 1) {
            return Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == d && mj1.getResult() == mj.getResult() && mj1.getPaizhuantai() == 0 && !mj1.caisheng).collect(Collectors.toList());
        }
        List<Mj> mjs = Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == d && mj1.getResult() == mj.getResult() && mj1.getPaizhuantai() == 0 && (!mj1.caisheng || Config.systemConfig.caishenganzhengchangpaichanyuchangpanggang == 1)).collect(Collectors.toList());
        while (mjs.size() > 3) {
            mjs.remove(0);
        }
        if (Config.systemConfig.dongnanxibeikechikegang == 1 && mjs.size() < 3 && mj.getResult() >= 31 && mj.getResult() <= 34) {
            List<Mj> gangpai = Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == d && mj1.getPaizhuantai() == 0 && (mj1.getResult() == 31 || mj1.getResult() == 32 || mj1.getResult() == 33 || mj1.getResult() == 34)).collect(Collectors.toList());
            gangpai.sort(new MainController.ComparatorMjResult());
            Iterator<Mj> iterator3 = gangpai.iterator();
            int before_result = 0;
            while (iterator3.hasNext()) {
                Mj item = (Mj)iterator3.next();
                if (item.getResult() == before_result) {
                    iterator3.remove();
                }
                before_result = item.getResult();
            }
            return gangpai;
        }
        return mjs;
    }

    @Override
    public boolean keChi(Mj mj, int finalD) {
        List<Mj> mjs3;
        List<Mj> mjs2;
        boolean kechi = false;
        List<Mj> caishengs = Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == finalD && mj1.caisheng && mj1.getPaizhuantai() == 0).collect(Collectors.toList());
        List<Mj> mjs = Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == finalD && mj1.getPaizhuantai() == 0 && mj1.getResult() < 30 && mj.getResult() + 1 == mj1.getResult() && mj.getResult() / 10 == mj1.getResult() / 10 && (!mj1.caisheng || Config.systemConfig.caishenganzhengchangpaichanyuchangpanggang == 1)).collect(Collectors.toList());
        if (mjs.size() > 0) {
            List mjs22;
            List<Mj> mjs1;
            if (Config.systemConfig.caishengcanyuchipanggang == 1 && caishengs.size() > 0 && !((Mj)mjs.get((int)0)).caisheng) {
                kechi = true;
            }
            if ((mjs1 = Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == finalD && mj1.getPaizhuantai() == 0 && mj1.getResult() < 30 && mj.getResult() + 2 == mj1.getResult() && mj.getResult() / 10 == mj1.getResult() / 10 && (!mj1.caisheng || Config.systemConfig.caishenganzhengchangpaichanyuchangpanggang == 1)).collect(Collectors.toList())).size() > 0) {
                kechi = true;
            }
            if ((mjs22 = Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == finalD && mj1.getPaizhuantai() == 0 && mj1.getResult() < 30 && mj.getResult() - 1 == mj1.getResult() && mj.getResult() / 10 == mj1.getResult() / 10 && (!mj1.caisheng || Config.systemConfig.caishenganzhengchangpaichanyuchangpanggang == 1)).collect(Collectors.toList())).size() > 0) {
                kechi = true;
            }
        }
        if ((mjs2 = Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == finalD && mj1.getPaizhuantai() == 0 && mj1.getResult() < 30 && mj.getResult() - 1 == mj1.getResult() && mj.getResult() / 10 == mj1.getResult() / 10 && mj.getResult() / 10 == mj1.getResult() / 10 && (!mj1.caisheng || Config.systemConfig.caishenganzhengchangpaichanyuchangpanggang == 1)).collect(Collectors.toList())).size() > 0) {
            if (Config.systemConfig.caishengcanyuchipanggang == 1 && caishengs.size() > 0 && !((Mj)mjs2.get((int)0)).caisheng) {
                kechi = true;
            }
            if ((mjs3 = Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == finalD && mj1.getPaizhuantai() == 0 && mj1.getResult() < 30 && mj.getResult() - 2 == mj1.getResult() && mj.getResult() / 10 == mj1.getResult() / 10 && (!mj1.caisheng || Config.systemConfig.caishenganzhengchangpaichanyuchangpanggang == 1)).collect(Collectors.toList())).size() > 0) {
                kechi = true;
            }
        }
        if (Config.systemConfig.caishengcanyuchipanggang == 1 && caishengs.size() > 0) {
            List<Mj> mjs4;
            mjs3 = Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == finalD && mj1.getPaizhuantai() == 0 && mj1.getResult() < 30 && mj.getResult() - 2 == mj1.getResult() && !mj1.caisheng && mj.getResult() / 10 == mj1.getResult() / 10 && (!mj1.caisheng || Config.systemConfig.caishenganzhengchangpaichanyuchangpanggang == 1)).collect(Collectors.toList());
            if (mjs3.size() > 0) {
                kechi = true;
            }
            if ((mjs4 = Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == finalD && mj1.getPaizhuantai() == 0 && mj1.getResult() < 30 && mj.getResult() + 2 == mj1.getResult() && !mj1.caisheng && mj.getResult() / 10 == mj1.getResult() / 10 && (!mj1.caisheng || Config.systemConfig.caishenganzhengchangpaichanyuchangpanggang == 1)).collect(Collectors.toList())).size() > 0) {
                kechi = true;
            }
        }
        if (Config.systemConfig.dongnanxibeikechikegang == 1) {
            if (mj.getResult() >= 31 && mj.getResult() <= 34) {
                java.util.Map<Integer, Integer> zhongfabaigang_map = new HashMap();
                Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == finalD && mj1.getPaizhuantai() == 0 && mj1.getResult() >= 31 && mj1.getResult() <= 34 && mj1.getResult() != mj.getResult()).collect(Collectors.toList()).forEach(mj1 -> {
                    if (zhongfabaigang_map.containsKey(mj1.getResult())) {
                        zhongfabaigang_map.put(mj1.getResult(), (Integer)zhongfabaigang_map.get(mj1.getResult()) + 1);
                    } else {
                        zhongfabaigang_map.put(mj1.getResult(), 1);
                    }
                });
                if (zhongfabaigang_map.keySet().size() >= 2) {
                    kechi = true;
                }
            }
            if (mj.getResult() >= 35 && mj.getResult() <= 37) {
                java.util.Map<Integer, Integer> zhongfabaigang_map = new HashMap();
                Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == finalD && mj1.getPaizhuantai() == 0 && mj1.getResult() >= 35 && mj1.getResult() <= 37 && mj1.getResult() != mj.getResult()).collect(Collectors.toList()).forEach(mj1 -> {
                    if (zhongfabaigang_map.containsKey(mj1.getResult())) {
                        zhongfabaigang_map.put(mj1.getResult(), (Integer)zhongfabaigang_map.get(mj1.getResult()) + 1);
                    } else {
                        zhongfabaigang_map.put(mj1.getResult(), 1);
                    }
                });
                if (zhongfabaigang_map.keySet().size() >= 2) {
                    kechi = true;
                }
            }
        }
        return kechi;
    }

    @Override
    public void clear() {
        this.qiangzhi_hu = 0;
    }

    @Override
    public void display() {
    }

    @Override
    public int getQiangzhiHu() {
        return this.qiangzhi_hu;
    }

    @Override
    public void setQiangzhiHu(int qiangzhi_hu) {
        this.qiangzhi_hu = qiangzhi_hu;
    }

    @Override
    public final String getPre_pai() {
        return this.pre_pai;
    }

    @Override
    public final void setPre_pai(String pre_pai) {
        this.pre_pai = pre_pai;
    }

    @Override
    public final Mj getDiyizhangda_mj() {
        return this.diyizhangda_mj;
    }

    @Override
    public final void setDiyizhangda_mj(Mj diyizhangda_mj) {
        this.diyizhangda_mj = diyizhangda_mj;
    }

    @Override
    public final Mj getHu_mj() {
        return this.hu_mj;
    }

    @Override
    public final void setHu_mj(Mj hu_mj) {
        this.hu_mj = hu_mj;
    }

    @Override
    public final ImageView getHu_text() {
        return this.hu_text;
    }

    @Override
    public final ImageView getMo_text() {
        this.mo_text.setFocusTraversable(true);
        return this.mo_text;
    }

    @Override
    public final int getHu() {
        return this.hu;
    }

    @Override
    public final void setHu(int hu) {
        if (hu == 1 && Config.systemConfig.xueliuchenghe == 0) {
            this.yina_hupai.clear();
            this.pre_pai = "";
        }
        this.hu = hu;
    }

    @Override
    public final int getMo() {
        return this.mo;
    }

    @Override
    public final boolean isKehu() {
        return this.kehu;
    }

    @Override
    public final int getQue() {
        return this.que;
    }

    @Override
    public final int getDirection() {
        return this.direction;
    }

    @Override
    public final void setDirection(int direction) {
        this.direction = direction;
    }

    @Override
    public final void setMo(int mo) {
        this.mo = mo;
    }

    @Override
    public final void setKehu(boolean kehu) {
        this.kehu = kehu;
    }

    @Override
    public final void setQue(int que) {
        this.que = que;
    }

    @Override
    public final List<Mj> getYina_hupai() {
        return this.yina_hupai;
    }

    @Override
    public final void setYina_hupai(List<Mj> yina_hupai) {
        this.yina_hupai = yina_hupai;
    }

    @Override
    public final Label getChi_text() {
        return this.chi_text;
    }

    @Override
    public final ImageView getPang_text() {
        return this.pang_text;
    }

    @Override
    public final ImageView getGang_text() {
        return this.gang_text;
    }

    @Override
    public final boolean isQueren() {
        return this.queren;
    }

    @Override
    public final void setQueren(boolean queren) {
        this.queren = queren;
    }

    @Override
    public List<Integer> getXueliuchenghe_hus() {
        return this.xueliuchenghe_hus;
    }

    @Override
    public void setXueliuchenghe_hus(List<Integer> xueliuchenghe_hus) {
        this.xueliuchenghe_hus = xueliuchenghe_hus;
    }
}

