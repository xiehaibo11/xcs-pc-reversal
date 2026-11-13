/*
 * Decompiled with CFR 0.152.
 */
package quesheng.controller.serve.impl;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.layout.AnchorPane;
import quesheng.controller.MainController;
import quesheng.controller.serve.AbstractServe;
import quesheng.customcontrole.SheTiaoPaiPanel;
import quesheng.model.Mj;
import quesheng.util.Config;

public class LiangCengJiImpl
extends AbstractServe {
    static int napaifangxiang;
    int napaiweizhi;

    public LiangCengJiImpl(MainController mainController) {
        super(mainController);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void napai(ObservableEmitter emitter, Mj mj1) {
        block111: {
            Mj namj;
            int a;
            block135: {
                Mj namj2;
                block134: {
                    block133: {
                        block131: {
                            int shoushu;
                            block132: {
                                block130: {
                                    block128: {
                                        block127: {
                                            int i;
                                            List<Mj> temp_weina2;
                                            block124: {
                                                Mj namj3;
                                                int i2;
                                                block122: {
                                                    Mj namj4;
                                                    int i3;
                                                    block120: {
                                                        Mj namj5;
                                                        int i4;
                                                        block117: {
                                                            int i5;
                                                            block115: {
                                                                block129: {
                                                                    block125: {
                                                                        block126: {
                                                                            block123: {
                                                                                block121: {
                                                                                    block119: {
                                                                                        block118: {
                                                                                            block116: {
                                                                                                block114: {
                                                                                                    block113: {
                                                                                                        block112: {
                                                                                                            Mj namj6;
                                                                                                            int i6;
                                                                                                            int i7;
                                                                                                            List<Mj> weina_1;
                                                                                                            shoushu = Config.systemConfig.wanjiapaishu / Config.systemConfig.meishouzhangshu;
                                                                                                            int fangxian = mj1.getDirection();
                                                                                                            MainController.weina = new LinkedList();
                                                                                                            List<Mj> temp_weina = new LinkedList();
                                                                                                            temp_weina2 = new LinkedList();
                                                                                                            if (Config.systemConfig.sicengsandun == 1) {
                                                                                                                do {
                                                                                                                    int finalFangxian = fangxian;
                                                                                                                    weina_1 = Config.weina.stream().filter(mj -> mj.getDirection() == finalFangxian).collect(Collectors.toList());
                                                                                                                    Collections.sort(weina_1, new MainController.ComparatorMj());
                                                                                                                    for (i7 = weina_1.size() - 1; i7 > weina_1.size() - 1 - 6; --i7) {
                                                                                                                        temp_weina.add(weina_1.get(i7));
                                                                                                                    }
                                                                                                                    for (i7 = 5; i7 > -1; --i7) {
                                                                                                                        temp_weina.add(weina_1.get(i7));
                                                                                                                    }
                                                                                                                    for (i7 = weina_1.size() - 1 - 6; i7 > 5; --i7) {
                                                                                                                        temp_weina2.add(weina_1.get(i7));
                                                                                                                    }
                                                                                                                    if (--fangxian >= 0) continue;
                                                                                                                    fangxian = 3;
                                                                                                                } while (fangxian != mj1.getDirection());
                                                                                                            } else {
                                                                                                                do {
                                                                                                                    int finalFangxian = fangxian;
                                                                                                                    weina_1 = Config.weina.stream().filter(mj -> mj.getDirection() == finalFangxian).collect(Collectors.toList());
                                                                                                                    Collections.sort(weina_1, new MainController.ComparatorMj());
                                                                                                                    for (i7 = weina_1.size() - 1; i7 > -1; --i7) {
                                                                                                                        temp_weina.add(weina_1.get(i7));
                                                                                                                    }
                                                                                                                    if (--fangxian >= 0) continue;
                                                                                                                    fangxian = 3;
                                                                                                                } while (fangxian != mj1.getDirection());
                                                                                                            }
                                                                                                            if (Config.systemConfig.qishourenyiliangdong == 1 || Config.systemConfig.renyiandongnapai == 1) {
                                                                                                                if (Config.yina.size() == 0) {
                                                                                                                    napaifangxiang = MainController.zhuangjia;
                                                                                                                }
                                                                                                            } else {
                                                                                                                napaifangxiang = MainController.zhuangjia;
                                                                                                            }
                                                                                                            this.napaiweizhi = temp_weina.indexOf(mj1);
                                                                                                            if (Config.systemConfig.niezhuapai == 1 || Config.systemConfig.niezhuanimo == 1) {
                                                                                                                this.napaiweizhi = mj1.getMahjong_number() % 2 == 0 ? ++this.napaiweizhi : (this.napaiweizhi += 2);
                                                                                                            } else if (mj1.getMahjong_number() % 2 == 0) {
                                                                                                                --this.napaiweizhi;
                                                                                                            }
                                                                                                            for (i6 = this.napaiweizhi; i6 < temp_weina.size(); ++i6) {
                                                                                                                namj6 = (Mj)temp_weina.get(i6);
                                                                                                                MainController.weina.add(namj6);
                                                                                                            }
                                                                                                            for (i6 = 0; i6 < this.napaiweizhi; ++i6) {
                                                                                                                namj6 = (Mj)temp_weina.get(i6);
                                                                                                                MainController.weina.add(namj6);
                                                                                                            }
                                                                                                            if (Config.systemConfig.baoliupai == 1 && Config.yina.size() > 0) {
                                                                                                                napaifangxiang = MainController.dapaifangx;
                                                                                                                Mj namj7 = MainController.weina.getFirst();
                                                                                                                namj7.getWeina_imageView().setVisible(false);
                                                                                                                if (namj7.getOther() != null) {
                                                                                                                    namj7.getOther().setVisible(false);
                                                                                                                }
                                                                                                                namj7.setLast(true);
                                                                                                                namj7.setSimo(true);
                                                                                                                MainController.weina.remove(namj7);
                                                                                                                namj7.setNapai_direction(napaifangxiang);
                                                                                                                Config.yina.add(namj7);
                                                                                                                MainController.yucepaiAfterMopai(0, MainController.dapaifangx, 1);
                                                                                                                emitter.onNext(1);
                                                                                                                return;
                                                                                                            }
                                                                                                            if (Config.systemConfig.niezhuapai == 1 || Config.systemConfig.niezhuanimo == 1) {
                                                                                                                List<Mj> mjList = new LinkedList<Mj>();
                                                                                                                mjList.addAll(MainController.weina);
                                                                                                                MainController.weina.clear();
                                                                                                                for (int i8 = mjList.size() - 1; i8 >= 0; --i8) {
                                                                                                                    Mj mj2 = (Mj)mjList.get(i8);
                                                                                                                    if (mj2.getMahjong_number() % 2 != 0) {
                                                                                                                        MainController.weina.add(MainController.weina.size() - 1, mj2);
                                                                                                                        continue;
                                                                                                                    }
                                                                                                                    MainController.weina.add(mj2);
                                                                                                                }
                                                                                                            }
                                                                                                            Iterator<Mj> mjIterator = MainController.weina.iterator();
                                                                                                            while (mjIterator.hasNext()) {
                                                                                                                Mj mj3 = (Mj)mjIterator.next();
                                                                                                                if (!mj3.isMaima()) continue;
                                                                                                                mjIterator.remove();
                                                                                                            }
                                                                                                            if (Config.systemConfig.qishourenyiliangdong == 1) break block112;
                                                                                                            if (Config.systemConfig.renyiandongnapai != 1) break block113;
                                                                                                        }
                                                                                                        Iterator<Mj> mj1Iterator = MainController.weina.iterator();
                                                                                                        while (mj1Iterator.hasNext()) {
                                                                                                            Mj mj4 = (Mj)mj1Iterator.next();
                                                                                                            if (!mj4.getSimo()) continue;
                                                                                                            mj1Iterator.remove();
                                                                                                        }
                                                                                                    }
                                                                                                    if (Config.systemConfig.butiaopaizhangjialianxu14xian13 != 1) break block114;
                                                                                                    for (i5 = 0; i5 < 14; ++i5) {
                                                                                                        Mj namj8 = MainController.weina.getFirst();
                                                                                                        namj8.getWeina_imageView().setVisible(false);
                                                                                                        if (namj8.getOther() != null) {
                                                                                                            namj8.getOther().setVisible(false);
                                                                                                        }
                                                                                                        namj8.setSimo(true);
                                                                                                        MainController.weina.remove(namj8);
                                                                                                        namj8.setNapai_direction(napaifangxiang);
                                                                                                        Config.yina.add(namj8);
                                                                                                    }
                                                                                                    do {
                                                                                                        if (++napaifangxiang <= 3) continue;
                                                                                                        napaifangxiang = 0;
                                                                                                    } while (MainController.directions.get(napaifangxiang).isQueren());
                                                                                                    break block115;
                                                                                                }
                                                                                                if (Config.systemConfig.xianzhuan8houzhuan4 != 1) break block116;
                                                                                                break block117;
                                                                                            }
                                                                                            if (Config.systemConfig.zijiazuosandunyousandun != 1) break block118;
                                                                                            MainController.directions.forEach(d -> {
                                                                                                if (!d.isQueren()) {
                                                                                                    Mj namj_lam;
                                                                                                    int i_lam;
                                                                                                    List<Mj> mjList = MainController.weina.stream().filter(mj -> mj.getDirection() == d.getDirection()).collect(Collectors.toList());
                                                                                                    mjList.sort(new MainController.ComparatorMj());
                                                                                                    for (i_lam = 0; i_lam < 6; ++i_lam) {
                                                                                                        namj_lam = (Mj)mjList.get(i_lam);
                                                                                                        namj_lam.getWeina_imageView().setVisible(false);
                                                                                                        if (namj_lam.getOther() != null) {
                                                                                                            namj_lam.getOther().setVisible(false);
                                                                                                        }
                                                                                                        namj_lam.setSimo(true);
                                                                                                        MainController.weina.remove(namj_lam);
                                                                                                        namj_lam.setNapai_direction(d.getDirection());
                                                                                                        Config.yina.add(namj_lam);
                                                                                                    }
                                                                                                    for (i_lam = mjList.size() - 1; i_lam > mjList.size() - 1 - 6; --i_lam) {
                                                                                                        namj_lam = (Mj)mjList.get(i_lam);
                                                                                                        namj_lam.getWeina_imageView().setVisible(false);
                                                                                                        if (namj_lam.getOther() != null) {
                                                                                                            namj_lam.getOther().setVisible(false);
                                                                                                        }
                                                                                                        namj_lam.setSimo(true);
                                                                                                        MainController.weina.remove(namj_lam);
                                                                                                        namj_lam.setNapai_direction(d.getDirection());
                                                                                                        Config.yina.add(namj_lam);
                                                                                                    }
                                                                                                }
                                                                                            });
                                                                                            break block111;
                                                                                        }
                                                                                        if (Config.systemConfig.zhuapai123 != 1) break block119;
                                                                                        break block120;
                                                                                    }
                                                                                    if (Config.systemConfig.zhuapai321 != 1) break block121;
                                                                                    break block122;
                                                                                }
                                                                                if (Config.systemConfig.sicengsandun != 1) break block123;
                                                                                i = 0;
                                                                                break block124;
                                                                            }
                                                                            if (Config.systemConfig.qishourenyiliangdong != 1) break block125;
                                                                            if (Config.yina.size() / Config.systemConfig.wanjiarenshu != 4) break block126;
                                                                            break block127;
                                                                        }
                                                                        for (int a2 = 0; a2 < Config.systemConfig.meishouzhangshu; ++a2) {
                                                                            Mj namj9 = MainController.weina.getFirst();
                                                                            namj9.getWeina_imageView().setVisible(false);
                                                                            if (namj9.getOther() != null) {
                                                                                namj9.getOther().setVisible(false);
                                                                            }
                                                                            namj9.setSimo(true);
                                                                            MainController.weina.remove(namj9);
                                                                            namj9.setNapai_direction(napaifangxiang);
                                                                            Config.yina.add(namj9);
                                                                        }
                                                                        break block128;
                                                                    }
                                                                    if (Config.systemConfig.renyiandongnapai != 1) break block129;
                                                                    if (Config.yina.size() / (Config.systemConfig.wanjiarenshu * Config.systemConfig.meishouzhangshu) == shoushu) break block111;
                                                                    for (int a3 = 0; a3 < Config.systemConfig.meishouzhangshu; ++a3) {
                                                                        Mj namj10 = MainController.weina.getFirst();
                                                                        namj10.getWeina_imageView().setVisible(false);
                                                                        if (namj10.getOther() != null) {
                                                                            namj10.getOther().setVisible(false);
                                                                        }
                                                                        namj10.setSimo(true);
                                                                        MainController.weina.remove(namj10);
                                                                        namj10.setNapai_direction(napaifangxiang);
                                                                        Config.yina.add(namj10);
                                                                    }
                                                                    break block130;
                                                                }
                                                                if (Config.systemConfig.disancengzuowanjiapai == 1) break block131;
                                                                break block132;
                                                            }
                                                            block15: for (i5 = 0; i5 < 12 * (Config.systemConfig.wanjiarenshu - 1); i5 += 12) {
                                                                for (a = 0; a < 12; ++a) {
                                                                    Mj namj11 = MainController.weina.getFirst();
                                                                    namj11.getWeina_imageView().setVisible(false);
                                                                    if (namj11.getOther() != null) {
                                                                        namj11.getOther().setVisible(false);
                                                                    }
                                                                    namj11.setSimo(true);
                                                                    MainController.weina.remove(namj11);
                                                                    namj11.setNapai_direction(napaifangxiang);
                                                                    Config.yina.add(namj11);
                                                                }
                                                                while (true) {
                                                                    if (++napaifangxiang > 3) {
                                                                        napaifangxiang = 0;
                                                                    }
                                                                    if (MainController.directions.get(napaifangxiang).isQueren()) continue;
                                                                    if (napaifangxiang != MainController.zhuangjia) continue block15;
                                                                }
                                                            }
                                                            i5 = 0;
                                                            while (true) {
                                                                if (i5 >= Config.systemConfig.wanjiarenshu - 1) {
                                                                    emitter.onNext(1);
                                                                    return;
                                                                }
                                                                Mj namj12 = MainController.weina.getFirst();
                                                                namj12.getWeina_imageView().setVisible(false);
                                                                if (namj12.getOther() != null) {
                                                                    namj12.getOther().setVisible(false);
                                                                }
                                                                namj12.setSimo(true);
                                                                MainController.weina.remove(namj12);
                                                                namj12.setNapai_direction(napaifangxiang);
                                                                Config.yina.add(namj12);
                                                                while (true) {
                                                                    if (++napaifangxiang > 3) {
                                                                        napaifangxiang = 0;
                                                                    }
                                                                    if (MainController.directions.get(napaifangxiang).isQueren()) continue;
                                                                    if (napaifangxiang != MainController.zhuangjia) break;
                                                                }
                                                                ++i5;
                                                            }
                                                        }
                                                        for (i4 = 0; i4 < 8 * Config.systemConfig.wanjiarenshu; i4 += 8) {
                                                            for (a = 0; a < 8; ++a) {
                                                                namj5 = MainController.weina.getFirst();
                                                                namj5.getWeina_imageView().setVisible(false);
                                                                if (namj5.getOther() != null) {
                                                                    namj5.getOther().setVisible(false);
                                                                }
                                                                namj5.setSimo(true);
                                                                MainController.weina.remove(namj5);
                                                                namj5.setNapai_direction(napaifangxiang);
                                                                Config.yina.add(namj5);
                                                            }
                                                            do {
                                                                if (++napaifangxiang <= 3) continue;
                                                                napaifangxiang = 0;
                                                            } while (MainController.directions.get(napaifangxiang).isQueren());
                                                        }
                                                        for (i4 = 0; i4 < 4 * Config.systemConfig.wanjiarenshu; i4 += 4) {
                                                            for (a = 0; a < 4; ++a) {
                                                                namj5 = MainController.weina.getFirst();
                                                                namj5.getWeina_imageView().setVisible(false);
                                                                if (namj5.getOther() != null) {
                                                                    namj5.getOther().setVisible(false);
                                                                }
                                                                namj5.setSimo(true);
                                                                MainController.weina.remove(namj5);
                                                                namj5.setNapai_direction(napaifangxiang);
                                                                Config.yina.add(namj5);
                                                            }
                                                            do {
                                                                if (++napaifangxiang <= 3) continue;
                                                                napaifangxiang = 0;
                                                            } while (MainController.directions.get(napaifangxiang).isQueren());
                                                        }
                                                        break block111;
                                                    }
                                                    for (i3 = 0; i3 < 2 * Config.systemConfig.wanjiarenshu; i3 += 2) {
                                                        for (a = 0; a < 2; ++a) {
                                                            namj4 = MainController.weina.getFirst();
                                                            namj4.getWeina_imageView().setVisible(false);
                                                            if (namj4.getOther() != null) {
                                                                namj4.getOther().setVisible(false);
                                                            }
                                                            namj4.setSimo(true);
                                                            MainController.weina.remove(namj4);
                                                            namj4.setNapai_direction(napaifangxiang);
                                                            Config.yina.add(namj4);
                                                        }
                                                        do {
                                                            if (++napaifangxiang <= 3) continue;
                                                            napaifangxiang = 0;
                                                        } while (MainController.directions.get(napaifangxiang).isQueren());
                                                    }
                                                    for (i3 = 0; i3 < 4 * Config.systemConfig.wanjiarenshu; i3 += 4) {
                                                        for (a = 0; a < 4; ++a) {
                                                            namj4 = MainController.weina.getFirst();
                                                            namj4.getWeina_imageView().setVisible(false);
                                                            if (namj4.getOther() != null) {
                                                                namj4.getOther().setVisible(false);
                                                            }
                                                            namj4.setSimo(true);
                                                            MainController.weina.remove(namj4);
                                                            namj4.setNapai_direction(napaifangxiang);
                                                            Config.yina.add(namj4);
                                                        }
                                                        do {
                                                            if (++napaifangxiang <= 3) continue;
                                                            napaifangxiang = 0;
                                                        } while (MainController.directions.get(napaifangxiang).isQueren());
                                                    }
                                                    for (i3 = 0; i3 < 6 * Config.systemConfig.wanjiarenshu; i3 += 6) {
                                                        for (a = 0; a < 6; ++a) {
                                                            namj4 = MainController.weina.getFirst();
                                                            namj4.getWeina_imageView().setVisible(false);
                                                            if (namj4.getOther() != null) {
                                                                namj4.getOther().setVisible(false);
                                                            }
                                                            namj4.setSimo(true);
                                                            MainController.weina.remove(namj4);
                                                            namj4.setNapai_direction(napaifangxiang);
                                                            Config.yina.add(namj4);
                                                        }
                                                        do {
                                                            if (++napaifangxiang <= 3) continue;
                                                            napaifangxiang = 0;
                                                        } while (MainController.directions.get(napaifangxiang).isQueren());
                                                    }
                                                    break block111;
                                                }
                                                for (i2 = 0; i2 < 6 * Config.systemConfig.wanjiarenshu; i2 += 6) {
                                                    for (a = 0; a < 6; ++a) {
                                                        namj3 = MainController.weina.getFirst();
                                                        namj3.getWeina_imageView().setVisible(false);
                                                        if (namj3.getOther() != null) {
                                                            namj3.getOther().setVisible(false);
                                                        }
                                                        namj3.setSimo(true);
                                                        MainController.weina.remove(namj3);
                                                        namj3.setNapai_direction(napaifangxiang);
                                                        Config.yina.add(namj3);
                                                    }
                                                    do {
                                                        if (++napaifangxiang <= 3) continue;
                                                        napaifangxiang = 0;
                                                    } while (MainController.directions.get(napaifangxiang).isQueren());
                                                }
                                                for (i2 = 0; i2 < 4 * Config.systemConfig.wanjiarenshu; i2 += 4) {
                                                    for (a = 0; a < 4; ++a) {
                                                        namj3 = MainController.weina.getFirst();
                                                        namj3.getWeina_imageView().setVisible(false);
                                                        if (namj3.getOther() != null) {
                                                            namj3.getOther().setVisible(false);
                                                        }
                                                        namj3.setSimo(true);
                                                        MainController.weina.remove(namj3);
                                                        namj3.setNapai_direction(napaifangxiang);
                                                        Config.yina.add(namj3);
                                                    }
                                                    do {
                                                        if (++napaifangxiang <= 3) continue;
                                                        napaifangxiang = 0;
                                                    } while (MainController.directions.get(napaifangxiang).isQueren());
                                                }
                                                for (i2 = 0; i2 < 2 * Config.systemConfig.wanjiarenshu; i2 += 2) {
                                                    for (a = 0; a < 2; ++a) {
                                                        namj3 = MainController.weina.getFirst();
                                                        namj3.getWeina_imageView().setVisible(false);
                                                        if (namj3.getOther() != null) {
                                                            namj3.getOther().setVisible(false);
                                                        }
                                                        namj3.setSimo(true);
                                                        MainController.weina.remove(namj3);
                                                        namj3.setNapai_direction(napaifangxiang);
                                                        Config.yina.add(namj3);
                                                    }
                                                    do {
                                                        if (++napaifangxiang <= 3) continue;
                                                        napaifangxiang = 0;
                                                    } while (MainController.directions.get(napaifangxiang).isQueren());
                                                }
                                                break block111;
                                            }
                                            while (true) {
                                                if (i >= shoushu * Config.systemConfig.meishouzhangshu * Config.systemConfig.wanjiarenshu) {
                                                    for (i = this.napaiweizhi; i < temp_weina2.size(); ++i) {
                                                        Mj namj13 = (Mj)temp_weina2.get(i);
                                                        MainController.weina.add(namj13);
                                                    }
                                                    for (i = 0; i < this.napaiweizhi; ++i) {
                                                        Mj namj14 = (Mj)temp_weina2.get(i);
                                                        MainController.weina.add(namj14);
                                                    }
                                                    break block111;
                                                }
                                                for (a = 0; a < Config.systemConfig.meishouzhangshu; ++a) {
                                                    Mj namj15 = MainController.weina.getFirst();
                                                    namj15.getWeina_imageView().setVisible(false);
                                                    if (namj15.getOther() != null) {
                                                        namj15.getOther().setVisible(false);
                                                    }
                                                    namj15.setSimo(true);
                                                    MainController.weina.remove(namj15);
                                                    namj15.setNapai_direction(napaifangxiang);
                                                    Config.yina.add(namj15);
                                                }
                                                do {
                                                    if (Config.systemConfig.niezhuanimo == 1) {
                                                        if (--napaifangxiang >= 0) continue;
                                                        napaifangxiang = 3;
                                                        continue;
                                                    }
                                                    if (++napaifangxiang <= 3) continue;
                                                    napaifangxiang = 0;
                                                } while (MainController.directions.get(napaifangxiang).isQueren());
                                                i += Config.systemConfig.meishouzhangshu.intValue();
                                            }
                                        }
                                        for (int i = 0; i < (shoushu - 1) * Config.systemConfig.meishouzhangshu * Config.systemConfig.wanjiarenshu; i += Config.systemConfig.meishouzhangshu.intValue()) {
                                            for (a = 0; a < Config.systemConfig.meishouzhangshu; ++a) {
                                                Mj namj16 = MainController.weina.getFirst();
                                                namj16.getWeina_imageView().setVisible(false);
                                                if (namj16.getOther() != null) {
                                                    namj16.getOther().setVisible(false);
                                                }
                                                namj16.setSimo(true);
                                                MainController.weina.remove(namj16);
                                                namj16.setNapai_direction(napaifangxiang);
                                                Config.yina.add(namj16);
                                            }
                                            do {
                                                if (Config.systemConfig.niezhuanimo == 1) {
                                                    if (--napaifangxiang >= 0) continue;
                                                    napaifangxiang = 3;
                                                    continue;
                                                }
                                                if (++napaifangxiang <= 3) continue;
                                                napaifangxiang = 0;
                                            } while (MainController.directions.get(napaifangxiang).isQueren());
                                        }
                                        break block111;
                                    }
                                    do {
                                        if (Config.systemConfig.niezhuanimo == 1) {
                                            if (--napaifangxiang >= 0) continue;
                                            napaifangxiang = 3;
                                            continue;
                                        }
                                        if (++napaifangxiang <= 3) continue;
                                        napaifangxiang = 0;
                                    } while (MainController.directions.get(napaifangxiang).isQueren());
                                    emitter.onNext(1);
                                    return;
                                }
                                do {
                                    if (Config.systemConfig.niezhuanimo == 1) {
                                        if (--napaifangxiang >= 0) continue;
                                        napaifangxiang = 3;
                                        continue;
                                    }
                                    if (++napaifangxiang <= 3) continue;
                                    napaifangxiang = 0;
                                } while (MainController.directions.get(napaifangxiang).isQueren());
                                emitter.onNext(1);
                                return;
                            }
                            for (int i = 0; i < shoushu * Config.systemConfig.meishouzhangshu * Config.systemConfig.wanjiarenshu; i += Config.systemConfig.meishouzhangshu.intValue()) {
                                for (a = 0; a < Config.systemConfig.meishouzhangshu; ++a) {
                                    namj = MainController.weina.getFirst();
                                    namj.getWeina_imageView().setVisible(false);
                                    if (namj.getOther() != null) {
                                        namj.getOther().setVisible(false);
                                    }
                                    namj.setSimo(true);
                                    MainController.weina.remove(namj);
                                    namj.setNapai_direction(napaifangxiang);
                                    Config.yina.add(namj);
                                }
                                do {
                                    if (Config.systemConfig.niezhuanimo == 1) {
                                        if (--napaifangxiang >= 0) continue;
                                        napaifangxiang = 3;
                                        continue;
                                    }
                                    if (++napaifangxiang <= 3) continue;
                                    napaifangxiang = 0;
                                } while (MainController.directions.get(napaifangxiang).isQueren());
                            }
                        }
                        if (Config.systemConfig.wanjiapaishu % Config.systemConfig.meishouzhangshu == 0) break block111;
                        if (!Config.systemConfig.tiaopai.equals("\u5e84\u5bb6\u8fde\u7eed\u4e09\u5f20")) break block133;
                        namj2 = MainController.weina.get(0);
                        namj2.getWeina_imageView().setVisible(false);
                        if (namj2.getOther() != null) {
                            namj2.getOther().setVisible(false);
                        }
                        break block134;
                    }
                    if (!Config.systemConfig.tiaopai.equals("\u4e0d\u8df3\u724c")) break block111;
                    break block135;
                }
                namj2.setSimo(true);
                MainController.weina.remove(namj2);
                napaifangxiang = MainController.zhuangjia;
                namj2.setNapai_direction(MainController.zhuangjia);
                Config.yina.add(namj2);
                int i = 0;
                while (true) {
                    if (i >= Config.systemConfig.wanjiapaishu % Config.systemConfig.meishouzhangshu * Config.systemConfig.wanjiarenshu) {
                        emitter.onNext(1);
                        return;
                    }
                    for (int a4 = 0; a4 < Config.systemConfig.wanjiapaishu % Config.systemConfig.meishouzhangshu; ++a4) {
                        namj2 = MainController.weina.getFirst();
                        namj2.getWeina_imageView().setVisible(false);
                        if (namj2.getOther() != null) {
                            namj2.getOther().setVisible(false);
                        }
                        namj2.setSimo(true);
                        MainController.weina.remove(namj2);
                        namj2.setNapai_direction(napaifangxiang);
                        Config.yina.add(namj2);
                    }
                    do {
                        if (Config.systemConfig.niezhuanimo == 1) {
                            if (--napaifangxiang >= 0) continue;
                            napaifangxiang = 3;
                            continue;
                        }
                        if (++napaifangxiang <= 3) continue;
                        napaifangxiang = 0;
                    } while (MainController.directions.get(napaifangxiang).isQueren());
                    i += Config.systemConfig.wanjiapaishu % Config.systemConfig.meishouzhangshu;
                }
            }
            for (int i = 0; i < Config.systemConfig.wanjiapaishu % Config.systemConfig.meishouzhangshu * Config.systemConfig.wanjiarenshu; i += Config.systemConfig.wanjiapaishu % Config.systemConfig.meishouzhangshu) {
                for (a = 0; a < Config.systemConfig.wanjiapaishu % Config.systemConfig.meishouzhangshu; ++a) {
                    namj = MainController.weina.getFirst();
                    namj.getWeina_imageView().setVisible(false);
                    if (namj.getOther() != null) {
                        namj.getOther().setVisible(false);
                    }
                    namj.setSimo(true);
                    MainController.weina.remove(namj);
                    namj.setNapai_direction(napaifangxiang);
                    Config.yina.add(namj);
                }
                do {
                    if (Config.systemConfig.niezhuanimo == 1) {
                        if (--napaifangxiang >= 0) continue;
                        napaifangxiang = 3;
                        continue;
                    }
                    if (++napaifangxiang <= 3) continue;
                    napaifangxiang = 0;
                } while (MainController.directions.get(napaifangxiang).isQueren());
            }
        }
        Observable.create(e -> Observable.create(emitter1 -> {
            if (Config.systemConfig.zhuapaihouxuanzhuang == 1) {
                MainController.zhuangPanel.getTop_text().setText("\u8bf7\u9009\u62e9\u5e84\u5bb6");
                MainController.zhuangPanel.getDong_imageview().setOnMouseClicked(event -> {
                    AnchorPane imageView = (AnchorPane)event.getSource();
                    MainController.xuanzeFangwei((Integer)imageView.getUserData());
                    emitter1.onNext(1);
                });
                MainController.zhuangPanel.getNan_imageview().setOnMouseClicked(event -> {
                    AnchorPane imageView = (AnchorPane)event.getSource();
                    MainController.xuanzeFangwei((Integer)imageView.getUserData());
                    emitter1.onNext(1);
                });
                MainController.zhuangPanel.getXi_imageview().setOnMouseClicked(event -> {
                    AnchorPane imageView = (AnchorPane)event.getSource();
                    MainController.xuanzeFangwei((Integer)imageView.getUserData());
                    emitter1.onNext(1);
                });
                MainController.zhuangPanel.getBei_imageview().setOnMouseClicked(event -> {
                    AnchorPane imageView = (AnchorPane)event.getSource();
                    MainController.xuanzeFangwei((Integer)imageView.getUserData());
                    emitter1.onNext(1);
                });
                MainController.zhuangPanel.setVisible(true);
            } else {
                emitter1.onNext(1);
            }
        }).subscribe(integer3 -> {
            if (Config.systemConfig.meijushetiaopai == 1) {
                SheTiaoPaiPanel sheTiaoPaiPanel = new SheTiaoPaiPanel();
                MainController.static_main_panel.getChildren().add(sheTiaoPaiPanel);
                MainController.static_main_panel.layout();
                sheTiaoPaiPanel.layout();
                sheTiaoPaiPanel.setLayoutY(MainController.static_main_panel.getHeight() / 2.0 - sheTiaoPaiPanel.getHeight());
                sheTiaoPaiPanel.setLayoutX(MainController.static_main_panel.getWidth() / 2.0 - sheTiaoPaiPanel.getWidth() / 2.0);
                sheTiaoPaiPanel.getPingtiao().setOnMouseClicked(event -> {
                    MainController.static_main_panel.getChildren().remove(sheTiaoPaiPanel);
                    Config.systemConfig.tiaopai = "\u5e73\u8df3";
                    e.onNext(1);
                });
                sheTiaoPaiPanel.getGetiao().setOnMouseClicked(event -> {
                    MainController.static_main_panel.getChildren().remove(sheTiaoPaiPanel);
                    Config.systemConfig.tiaopai = "\u9694\u8df3";
                    e.onNext(1);
                });
                sheTiaoPaiPanel.getShangxiatiao().setOnMouseClicked(event -> {
                    MainController.static_main_panel.getChildren().remove(sheTiaoPaiPanel);
                    Config.systemConfig.tiaopai = "\u4e0a\u4e0b\u8df3";
                    e.onNext(1);
                });
            } else {
                e.onNext(1);
            }
        })).subscribe(integer3 -> {
            Mj namj;
            if (!"\u6bcf\u5bb6\u8fde\u7eed\u4e24\u5f20".equals(Config.systemConfig.tiaopai)) {
                namj = MainController.weina.get(0);
                namj.getWeina_imageView().setVisible(false);
                if (namj.getOther() != null) {
                    namj.getOther().setVisible(false);
                }
                namj.setSimo(true);
                MainController.weina.remove(namj);
                napaifangxiang = MainController.zhuangjia;
                namj.setNapai_direction(MainController.zhuangjia);
                if (Config.systemConfig.zuihouyizhang == 1) {
                    namj.setLast(true);
                }
                Config.yina.add(namj);
            }
            switch (Config.systemConfig.tiaopai) {
                case "\u6bcf\u5bb6\u8fde\u7eed\u4e24\u5f20": {
                    int count;
                    for (count = 0; count < 2; ++count) {
                        namj = MainController.weina.get(0);
                        namj.getWeina_imageView().setVisible(false);
                        if (namj.getOther() != null) {
                            namj.getOther().setVisible(false);
                        }
                        namj.setSimo(true);
                        MainController.weina.remove(namj);
                        namj.setNapai_direction(MainController.zhuangjia);
                        if (Config.systemConfig.zuihouyizhang == 1 && Config.systemConfig.queyimeng == 1) {
                            namj.setLast(true);
                            MainController.directions.get(namj.getNapai_direction()).setQue(namj.getResult() / 10);
                            MainController.directions.get(namj.getNapai_direction()).setDiyizhangda_mj((Mj)namj.clone());
                        }
                        Config.yina.add(namj);
                    }
                    do {
                        if (Config.systemConfig.niezhuanimo == 1) {
                            if (--napaifangxiang < 0) {
                                napaifangxiang = 3;
                            }
                        } else if (++napaifangxiang > 3) {
                            napaifangxiang = 0;
                        }
                    } while (MainController.directions.get(napaifangxiang).isQueren());
                    switch (Config.systemConfig.wanjiarenshu) {
                        case 2: {
                            for (count = 0; count < 2; ++count) {
                                namj = MainController.weina.get(0);
                                namj.getWeina_imageView().setVisible(false);
                                if (namj.getOther() != null) {
                                    namj.getOther().setVisible(false);
                                }
                                namj.setSimo(true);
                                MainController.weina.remove(namj);
                                namj.setNapai_direction(napaifangxiang);
                                if (Config.systemConfig.zuihouyizhang == 1) {
                                    namj.setLast(true);
                                    if (Config.systemConfig.queyimeng == 1) {
                                        MainController.directions.get(namj.getNapai_direction()).setQue(namj.getResult() / 10);
                                        MainController.directions.get(namj.getNapai_direction()).setDiyizhangda_mj((Mj)namj.clone());
                                    }
                                }
                                Config.yina.add(namj);
                            }
                            break;
                        }
                        case 3: {
                            for (count = 0; count < 2; ++count) {
                                namj = MainController.weina.get(0);
                                namj.getWeina_imageView().setVisible(false);
                                if (namj.getOther() != null) {
                                    namj.getOther().setVisible(false);
                                }
                                namj.setSimo(true);
                                MainController.weina.remove(namj);
                                namj.setNapai_direction(napaifangxiang);
                                if (Config.systemConfig.zuihouyizhang == 1) {
                                    namj.setLast(true);
                                    if (Config.systemConfig.queyimeng == 1) {
                                        MainController.directions.get(namj.getNapai_direction()).setQue(namj.getResult() / 10);
                                        MainController.directions.get(namj.getNapai_direction()).setDiyizhangda_mj((Mj)namj.clone());
                                    }
                                }
                                Config.yina.add(namj);
                            }
                            do {
                                if (Config.systemConfig.niezhuanimo == 1) {
                                    if (--napaifangxiang < 0) {
                                        napaifangxiang = 3;
                                    }
                                } else if (++napaifangxiang > 3) {
                                    napaifangxiang = 0;
                                }
                            } while (MainController.directions.get(napaifangxiang).isQueren());
                            for (count = 0; count < 2; ++count) {
                                namj = MainController.weina.get(0);
                                namj.getWeina_imageView().setVisible(false);
                                if (namj.getOther() != null) {
                                    namj.getOther().setVisible(false);
                                }
                                namj.setSimo(true);
                                MainController.weina.remove(namj);
                                namj.setNapai_direction(napaifangxiang);
                                if (Config.systemConfig.zuihouyizhang == 1) {
                                    namj.setLast(true);
                                    if (Config.systemConfig.queyimeng == 1) {
                                        MainController.directions.get(namj.getNapai_direction()).setQue(namj.getResult() / 10);
                                        MainController.directions.get(namj.getNapai_direction()).setDiyizhangda_mj((Mj)namj.clone());
                                    }
                                }
                                Config.yina.add(namj);
                            }
                            break;
                        }
                        case 4: {
                            for (count = 0; count < 2; ++count) {
                                namj = MainController.weina.get(0);
                                namj.getWeina_imageView().setVisible(false);
                                if (namj.getOther() != null) {
                                    namj.getOther().setVisible(false);
                                }
                                namj.setSimo(true);
                                MainController.weina.remove(namj);
                                namj.setNapai_direction(napaifangxiang);
                                if (Config.systemConfig.zuihouyizhang == 1) {
                                    namj.setLast(true);
                                    if (Config.systemConfig.queyimeng == 1) {
                                        MainController.directions.get(namj.getNapai_direction()).setQue(namj.getResult() / 10);
                                        MainController.directions.get(namj.getNapai_direction()).setDiyizhangda_mj((Mj)namj.clone());
                                    }
                                }
                                Config.yina.add(namj);
                            }
                            do {
                                if (Config.systemConfig.niezhuanimo == 1) {
                                    if (--napaifangxiang < 0) {
                                        napaifangxiang = 3;
                                    }
                                } else if (++napaifangxiang > 3) {
                                    napaifangxiang = 0;
                                }
                            } while (MainController.directions.get(napaifangxiang).isQueren());
                            for (count = 0; count < 2; ++count) {
                                namj = MainController.weina.get(0);
                                namj.getWeina_imageView().setVisible(false);
                                if (namj.getOther() != null) {
                                    namj.getOther().setVisible(false);
                                }
                                namj.setSimo(true);
                                MainController.weina.remove(namj);
                                namj.setNapai_direction(napaifangxiang);
                                if (Config.systemConfig.zuihouyizhang == 1) {
                                    namj.setLast(true);
                                    if (Config.systemConfig.queyimeng == 1) {
                                        MainController.directions.get(namj.getNapai_direction()).setQue(namj.getResult() / 10);
                                        MainController.directions.get(namj.getNapai_direction()).setDiyizhangda_mj((Mj)namj.clone());
                                    }
                                }
                                Config.yina.add(namj);
                            }
                            do {
                                if (Config.systemConfig.niezhuanimo == 1) {
                                    if (--napaifangxiang < 0) {
                                        napaifangxiang = 3;
                                    }
                                } else if (++napaifangxiang > 3) {
                                    napaifangxiang = 0;
                                }
                            } while (MainController.directions.get(napaifangxiang).isQueren());
                            for (count = 0; count < 2; ++count) {
                                namj = MainController.weina.get(0);
                                namj.getWeina_imageView().setVisible(false);
                                if (namj.getOther() != null) {
                                    namj.getOther().setVisible(false);
                                }
                                namj.setSimo(true);
                                MainController.weina.remove(namj);
                                namj.setNapai_direction(napaifangxiang);
                                if (Config.systemConfig.zuihouyizhang == 1) {
                                    namj.setLast(true);
                                    if (Config.systemConfig.queyimeng == 1) {
                                        MainController.directions.get(namj.getNapai_direction()).setQue(namj.getResult() / 10);
                                        MainController.directions.get(namj.getNapai_direction()).setDiyizhangda_mj((Mj)namj.clone());
                                    }
                                }
                                Config.yina.add(namj);
                            }
                            break;
                        }
                    }
                    MainController.yucepaiAfterMopai(0, MainController.zhuangjia, 1);
                    break;
                }
                case "\u5e73\u8df3": {
                    namj = MainController.weina.get(1);
                    namj.getWeina_imageView().setVisible(false);
                    if (namj.getOther() != null) {
                        namj.getOther().setVisible(false);
                    }
                    namj.setSimo(true);
                    MainController.weina.remove(namj);
                    namj.setNapai_direction(MainController.zhuangjia);
                    if (Config.systemConfig.zuihouyizhang == 1 && Config.systemConfig.queyimeng == 1) {
                        namj.setLast(true);
                        MainController.directions.get(namj.getNapai_direction()).setQue(namj.getResult() / 10);
                        MainController.directions.get(namj.getNapai_direction()).setDiyizhangda_mj((Mj)namj.clone());
                    }
                    Config.yina.add(namj);
                    do {
                        if (Config.systemConfig.niezhuanimo == 1) {
                            if (--napaifangxiang < 0) {
                                napaifangxiang = 3;
                            }
                        } else if (++napaifangxiang > 3) {
                            napaifangxiang = 0;
                        }
                    } while (MainController.directions.get(napaifangxiang).isQueren());
                    switch (Config.systemConfig.wanjiarenshu) {
                        case 2: {
                            namj = MainController.weina.get(0);
                            namj.getWeina_imageView().setVisible(false);
                            if (namj.getOther() != null) {
                                namj.getOther().setVisible(false);
                            }
                            namj.setSimo(true);
                            MainController.weina.remove(namj);
                            namj.setNapai_direction(napaifangxiang);
                            if (Config.systemConfig.zuihouyizhang == 1) {
                                namj.setLast(true);
                                if (Config.systemConfig.queyimeng == 1) {
                                    MainController.directions.get(namj.getNapai_direction()).setQue(namj.getResult() / 10);
                                    MainController.directions.get(namj.getNapai_direction()).setDiyizhangda_mj((Mj)namj.clone());
                                }
                            }
                            Config.yina.add(namj);
                            break;
                        }
                        case 3: {
                            namj = MainController.weina.get(0);
                            namj.getWeina_imageView().setVisible(false);
                            if (namj.getOther() != null) {
                                namj.getOther().setVisible(false);
                            }
                            namj.setSimo(true);
                            MainController.weina.remove(namj);
                            namj.setNapai_direction(napaifangxiang);
                            if (Config.systemConfig.zuihouyizhang == 1) {
                                namj.setLast(true);
                                if (Config.systemConfig.queyimeng == 1) {
                                    MainController.directions.get(namj.getNapai_direction()).setQue(namj.getResult() / 10);
                                    MainController.directions.get(namj.getNapai_direction()).setDiyizhangda_mj((Mj)namj.clone());
                                }
                            }
                            Config.yina.add(namj);
                            do {
                                if (Config.systemConfig.niezhuanimo == 1) {
                                    if (--napaifangxiang < 0) {
                                        napaifangxiang = 3;
                                    }
                                } else if (++napaifangxiang > 3) {
                                    napaifangxiang = 0;
                                }
                            } while (MainController.directions.get(napaifangxiang).isQueren());
                            namj = MainController.weina.get(0);
                            namj.getWeina_imageView().setVisible(false);
                            if (namj.getOther() != null) {
                                namj.getOther().setVisible(false);
                            }
                            namj.setSimo(true);
                            MainController.weina.remove(namj);
                            namj.setNapai_direction(napaifangxiang);
                            if (Config.systemConfig.zuihouyizhang == 1) {
                                namj.setLast(true);
                                if (Config.systemConfig.queyimeng == 1) {
                                    MainController.directions.get(namj.getNapai_direction()).setQue(namj.getResult() / 10);
                                    MainController.directions.get(namj.getNapai_direction()).setDiyizhangda_mj((Mj)namj.clone());
                                }
                            }
                            Config.yina.add(namj);
                            break;
                        }
                        case 4: {
                            namj = MainController.weina.get(0);
                            namj.getWeina_imageView().setVisible(false);
                            if (namj.getOther() != null) {
                                namj.getOther().setVisible(false);
                            }
                            namj.setSimo(true);
                            MainController.weina.remove(namj);
                            namj.setNapai_direction(napaifangxiang);
                            if (Config.systemConfig.zuihouyizhang == 1) {
                                namj.setLast(true);
                                if (Config.systemConfig.queyimeng == 1) {
                                    MainController.directions.get(namj.getNapai_direction()).setQue(namj.getResult() / 10);
                                    MainController.directions.get(namj.getNapai_direction()).setDiyizhangda_mj((Mj)namj.clone());
                                }
                            }
                            Config.yina.add(namj);
                            do {
                                if (Config.systemConfig.niezhuanimo == 1) {
                                    if (--napaifangxiang < 0) {
                                        napaifangxiang = 3;
                                    }
                                } else if (++napaifangxiang > 3) {
                                    napaifangxiang = 0;
                                }
                            } while (MainController.directions.get(napaifangxiang).isQueren());
                            namj = MainController.weina.get(0);
                            namj.getWeina_imageView().setVisible(false);
                            if (namj.getOther() != null) {
                                namj.getOther().setVisible(false);
                            }
                            namj.setSimo(true);
                            MainController.weina.remove(namj);
                            namj.setNapai_direction(napaifangxiang);
                            if (Config.systemConfig.zuihouyizhang == 1) {
                                namj.setLast(true);
                                if (Config.systemConfig.queyimeng == 1) {
                                    MainController.directions.get(namj.getNapai_direction()).setQue(namj.getResult() / 10);
                                    MainController.directions.get(namj.getNapai_direction()).setDiyizhangda_mj((Mj)namj.clone());
                                }
                            }
                            Config.yina.add(namj);
                            do {
                                if (Config.systemConfig.niezhuanimo == 1) {
                                    if (--napaifangxiang < 0) {
                                        napaifangxiang = 3;
                                    }
                                } else if (++napaifangxiang > 3) {
                                    napaifangxiang = 0;
                                }
                            } while (MainController.directions.get(napaifangxiang).isQueren());
                            namj = MainController.weina.get(0);
                            namj.getWeina_imageView().setVisible(false);
                            if (namj.getOther() != null) {
                                namj.getOther().setVisible(false);
                            }
                            namj.setSimo(true);
                            MainController.weina.remove(namj);
                            namj.setNapai_direction(napaifangxiang);
                            if (Config.systemConfig.zuihouyizhang == 1) {
                                namj.setLast(true);
                                if (Config.systemConfig.queyimeng == 1) {
                                    MainController.directions.get(namj.getNapai_direction()).setQue(namj.getResult() / 10);
                                    MainController.directions.get(namj.getNapai_direction()).setDiyizhangda_mj((Mj)namj.clone());
                                }
                            }
                            Config.yina.add(namj);
                        }
                    }
                    MainController.yucepaiAfterMopai(0, MainController.zhuangjia, 1);
                    break;
                }
                case "\u9694\u8df3": {
                    namj = MainController.weina.get(3);
                    namj.getWeina_imageView().setVisible(false);
                    if (namj.getOther() != null) {
                        namj.getOther().setVisible(false);
                    }
                    namj.setSimo(true);
                    MainController.weina.remove(namj);
                    namj.setNapai_direction(MainController.zhuangjia);
                    if (Config.systemConfig.zuihouyizhang == 1 && Config.systemConfig.queyimeng == 1) {
                        namj.setLast(true);
                        MainController.directions.get(namj.getNapai_direction()).setQue(namj.getResult() / 10);
                        MainController.directions.get(namj.getNapai_direction()).setDiyizhangda_mj((Mj)namj.clone());
                    }
                    Config.yina.add(namj);
                    do {
                        if (Config.systemConfig.niezhuanimo == 1) {
                            if (--napaifangxiang < 0) {
                                napaifangxiang = 3;
                            }
                        } else if (++napaifangxiang > 3) {
                            napaifangxiang = 0;
                        }
                    } while (MainController.directions.get(napaifangxiang).isQueren());
                    switch (Config.systemConfig.wanjiarenshu) {
                        case 2: {
                            namj = MainController.weina.get(0);
                            namj.getWeina_imageView().setVisible(false);
                            if (namj.getOther() != null) {
                                namj.getOther().setVisible(false);
                            }
                            namj.setSimo(true);
                            MainController.weina.remove(namj);
                            namj.setNapai_direction(napaifangxiang);
                            if (Config.systemConfig.zuihouyizhang == 1) {
                                namj.setLast(true);
                                if (Config.systemConfig.queyimeng == 1) {
                                    MainController.directions.get(namj.getNapai_direction()).setQue(namj.getResult() / 10);
                                    MainController.directions.get(namj.getNapai_direction()).setDiyizhangda_mj((Mj)namj.clone());
                                }
                            }
                            Config.yina.add(namj);
                            break;
                        }
                        case 3: {
                            namj = MainController.weina.get(0);
                            namj.getWeina_imageView().setVisible(false);
                            if (namj.getOther() != null) {
                                namj.getOther().setVisible(false);
                            }
                            namj.setSimo(true);
                            MainController.weina.remove(namj);
                            namj.setNapai_direction(napaifangxiang);
                            if (Config.systemConfig.zuihouyizhang == 1) {
                                namj.setLast(true);
                                if (Config.systemConfig.queyimeng == 1) {
                                    MainController.directions.get(namj.getNapai_direction()).setQue(namj.getResult() / 10);
                                    MainController.directions.get(namj.getNapai_direction()).setDiyizhangda_mj((Mj)namj.clone());
                                }
                            }
                            Config.yina.add(namj);
                            do {
                                if (Config.systemConfig.niezhuanimo == 1) {
                                    if (--napaifangxiang < 0) {
                                        napaifangxiang = 3;
                                    }
                                } else if (++napaifangxiang > 3) {
                                    napaifangxiang = 0;
                                }
                            } while (MainController.directions.get(napaifangxiang).isQueren());
                            namj = MainController.weina.get(0);
                            namj.getWeina_imageView().setVisible(false);
                            if (namj.getOther() != null) {
                                namj.getOther().setVisible(false);
                            }
                            namj.setSimo(true);
                            MainController.weina.remove(namj);
                            namj.setNapai_direction(napaifangxiang);
                            if (Config.systemConfig.zuihouyizhang == 1) {
                                namj.setLast(true);
                                if (Config.systemConfig.queyimeng == 1) {
                                    MainController.directions.get(namj.getNapai_direction()).setQue(namj.getResult() / 10);
                                    MainController.directions.get(namj.getNapai_direction()).setDiyizhangda_mj((Mj)namj.clone());
                                }
                            }
                            Config.yina.add(namj);
                            break;
                        }
                        case 4: {
                            namj = MainController.weina.get(0);
                            namj.getWeina_imageView().setVisible(false);
                            if (namj.getOther() != null) {
                                namj.getOther().setVisible(false);
                            }
                            namj.setSimo(true);
                            MainController.weina.remove(namj);
                            namj.setNapai_direction(napaifangxiang);
                            if (Config.systemConfig.zuihouyizhang == 1) {
                                namj.setLast(true);
                                if (Config.systemConfig.queyimeng == 1) {
                                    MainController.directions.get(namj.getNapai_direction()).setQue(namj.getResult() / 10);
                                    MainController.directions.get(namj.getNapai_direction()).setDiyizhangda_mj((Mj)namj.clone());
                                }
                            }
                            Config.yina.add(namj);
                            do {
                                if (Config.systemConfig.niezhuanimo == 1) {
                                    if (--napaifangxiang < 0) {
                                        napaifangxiang = 3;
                                    }
                                } else if (++napaifangxiang > 3) {
                                    napaifangxiang = 0;
                                }
                            } while (MainController.directions.get(napaifangxiang).isQueren());
                            namj = MainController.weina.get(0);
                            namj.getWeina_imageView().setVisible(false);
                            if (namj.getOther() != null) {
                                namj.getOther().setVisible(false);
                            }
                            namj.setSimo(true);
                            MainController.weina.remove(namj);
                            namj.setNapai_direction(napaifangxiang);
                            if (Config.systemConfig.zuihouyizhang == 1) {
                                namj.setLast(true);
                                if (Config.systemConfig.queyimeng == 1) {
                                    MainController.directions.get(namj.getNapai_direction()).setQue(namj.getResult() / 10);
                                    MainController.directions.get(namj.getNapai_direction()).setDiyizhangda_mj((Mj)namj.clone());
                                }
                            }
                            Config.yina.add(namj);
                            do {
                                if (Config.systemConfig.niezhuanimo == 1) {
                                    if (--napaifangxiang < 0) {
                                        napaifangxiang = 3;
                                    }
                                } else if (++napaifangxiang > 3) {
                                    napaifangxiang = 0;
                                }
                            } while (MainController.directions.get(napaifangxiang).isQueren());
                            namj = MainController.weina.get(0);
                            namj.getWeina_imageView().setVisible(false);
                            if (namj.getOther() != null) {
                                namj.getOther().setVisible(false);
                            }
                            namj.setSimo(true);
                            MainController.weina.remove(namj);
                            namj.setNapai_direction(napaifangxiang);
                            if (Config.systemConfig.zuihouyizhang == 1) {
                                namj.setLast(true);
                                if (Config.systemConfig.queyimeng == 1) {
                                    MainController.directions.get(namj.getNapai_direction()).setQue(namj.getResult() / 10);
                                    MainController.directions.get(namj.getNapai_direction()).setDiyizhangda_mj((Mj)namj.clone());
                                }
                            }
                            Config.yina.add(namj);
                        }
                    }
                    MainController.yucepaiAfterMopai(0, MainController.zhuangjia, 1);
                    break;
                }
                case "\u4e0a\u4e0b\u8df3": {
                    namj = MainController.weina.get(0);
                    namj.getWeina_imageView().setVisible(false);
                    if (namj.getOther() != null) {
                        namj.getOther().setVisible(false);
                    }
                    namj.setSimo(true);
                    MainController.weina.remove(namj);
                    namj.setNapai_direction(MainController.zhuangjia);
                    if (Config.systemConfig.zuihouyizhang == 1 && Config.systemConfig.queyimeng == 1) {
                        namj.setLast(true);
                        MainController.directions.get(namj.getNapai_direction()).setQue(namj.getResult() / 10);
                        MainController.directions.get(namj.getNapai_direction()).setDiyizhangda_mj((Mj)namj.clone());
                    }
                    Config.yina.add(namj);
                    do {
                        if (Config.systemConfig.niezhuanimo == 1) {
                            if (--napaifangxiang < 0) {
                                napaifangxiang = 3;
                            }
                        } else if (++napaifangxiang > 3) {
                            napaifangxiang = 0;
                        }
                    } while (MainController.directions.get(napaifangxiang).isQueren());
                    switch (Config.systemConfig.wanjiarenshu) {
                        case 2: {
                            namj = MainController.weina.get(0);
                            namj.getWeina_imageView().setVisible(false);
                            if (namj.getOther() != null) {
                                namj.getOther().setVisible(false);
                            }
                            namj.setSimo(true);
                            MainController.weina.remove(namj);
                            namj.setNapai_direction(napaifangxiang);
                            if (Config.systemConfig.zuihouyizhang == 1) {
                                namj.setLast(true);
                                if (Config.systemConfig.queyimeng == 1) {
                                    MainController.directions.get(namj.getNapai_direction()).setQue(namj.getResult() / 10);
                                    MainController.directions.get(namj.getNapai_direction()).setDiyizhangda_mj((Mj)namj.clone());
                                }
                            }
                            Config.yina.add(namj);
                            break;
                        }
                        case 3: {
                            namj = MainController.weina.get(0);
                            namj.getWeina_imageView().setVisible(false);
                            if (namj.getOther() != null) {
                                namj.getOther().setVisible(false);
                            }
                            namj.setSimo(true);
                            MainController.weina.remove(namj);
                            namj.setNapai_direction(napaifangxiang);
                            if (Config.systemConfig.zuihouyizhang == 1) {
                                namj.setLast(true);
                                if (Config.systemConfig.queyimeng == 1) {
                                    MainController.directions.get(namj.getNapai_direction()).setQue(namj.getResult() / 10);
                                    MainController.directions.get(namj.getNapai_direction()).setDiyizhangda_mj((Mj)namj.clone());
                                }
                            }
                            Config.yina.add(namj);
                            do {
                                if (Config.systemConfig.niezhuanimo == 1) {
                                    if (--napaifangxiang < 0) {
                                        napaifangxiang = 3;
                                    }
                                } else if (++napaifangxiang > 3) {
                                    napaifangxiang = 0;
                                }
                            } while (MainController.directions.get(napaifangxiang).isQueren());
                            namj = MainController.weina.get(0);
                            namj.getWeina_imageView().setVisible(false);
                            if (namj.getOther() != null) {
                                namj.getOther().setVisible(false);
                            }
                            namj.setSimo(true);
                            MainController.weina.remove(namj);
                            namj.setNapai_direction(napaifangxiang);
                            if (Config.systemConfig.zuihouyizhang == 1) {
                                namj.setLast(true);
                                if (Config.systemConfig.queyimeng == 1) {
                                    MainController.directions.get(namj.getNapai_direction()).setQue(namj.getResult() / 10);
                                    MainController.directions.get(namj.getNapai_direction()).setDiyizhangda_mj((Mj)namj.clone());
                                }
                            }
                            Config.yina.add(namj);
                            break;
                        }
                        case 4: {
                            namj = MainController.weina.get(0);
                            namj.getWeina_imageView().setVisible(false);
                            if (namj.getOther() != null) {
                                namj.getOther().setVisible(false);
                            }
                            namj.setSimo(true);
                            MainController.weina.remove(namj);
                            namj.setNapai_direction(napaifangxiang);
                            if (Config.systemConfig.zuihouyizhang == 1) {
                                namj.setLast(true);
                                if (Config.systemConfig.queyimeng == 1) {
                                    MainController.directions.get(namj.getNapai_direction()).setQue(namj.getResult() / 10);
                                    MainController.directions.get(namj.getNapai_direction()).setDiyizhangda_mj((Mj)namj.clone());
                                }
                            }
                            Config.yina.add(namj);
                            do {
                                if (Config.systemConfig.niezhuanimo == 1) {
                                    if (--napaifangxiang < 0) {
                                        napaifangxiang = 3;
                                    }
                                } else if (++napaifangxiang > 3) {
                                    napaifangxiang = 0;
                                }
                            } while (MainController.directions.get(napaifangxiang).isQueren());
                            namj = MainController.weina.get(0);
                            namj.getWeina_imageView().setVisible(false);
                            if (namj.getOther() != null) {
                                namj.getOther().setVisible(false);
                            }
                            namj.setSimo(true);
                            MainController.weina.remove(namj);
                            namj.setNapai_direction(napaifangxiang);
                            if (Config.systemConfig.zuihouyizhang == 1) {
                                namj.setLast(true);
                                if (Config.systemConfig.queyimeng == 1) {
                                    MainController.directions.get(namj.getNapai_direction()).setQue(namj.getResult() / 10);
                                    MainController.directions.get(namj.getNapai_direction()).setDiyizhangda_mj((Mj)namj.clone());
                                }
                            }
                            Config.yina.add(namj);
                            do {
                                if (Config.systemConfig.niezhuanimo == 1) {
                                    if (--napaifangxiang < 0) {
                                        napaifangxiang = 3;
                                    }
                                } else if (++napaifangxiang > 3) {
                                    napaifangxiang = 0;
                                }
                            } while (MainController.directions.get(napaifangxiang).isQueren());
                            namj = MainController.weina.get(0);
                            namj.getWeina_imageView().setVisible(false);
                            if (namj.getOther() != null) {
                                namj.getOther().setVisible(false);
                            }
                            namj.setSimo(true);
                            MainController.weina.remove(namj);
                            namj.setNapai_direction(napaifangxiang);
                            if (Config.systemConfig.zuihouyizhang == 1) {
                                namj.setLast(true);
                                if (Config.systemConfig.queyimeng == 1) {
                                    MainController.directions.get(namj.getNapai_direction()).setQue(namj.getResult() / 10);
                                    MainController.directions.get(namj.getNapai_direction()).setDiyizhangda_mj((Mj)namj.clone());
                                }
                            }
                            Config.yina.add(namj);
                        }
                    }
                    MainController.yucepaiAfterMopai(0, MainController.zhuangjia, 1);
                    --this.napaiweizhi;
                    break;
                }
                case "\u4e0d\u8df3\u724c": {
                    MainController.yucepaiAfterMopai(0, MainController.zhuangjia, 1);
                }
            }
            emitter.onNext(1);
        });
    }
}

