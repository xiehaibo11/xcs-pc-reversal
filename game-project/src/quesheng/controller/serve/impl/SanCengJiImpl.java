/*
 * Decompiled with CFR 0.152.
 */
package quesheng.controller.serve.impl;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import quesheng.controller.MainController;
import quesheng.controller.serve.AbstractServe;
import quesheng.customcontrole.SheTiaoPaiPanel;
import quesheng.model.Mj;
import quesheng.util.Config;

public class SanCengJiImpl
extends AbstractServe {
    int napaifangxiang;
    int napaiweizhi;

    public SanCengJiImpl(MainController mainController) {
        super(mainController);
    }

    @Override
    public void napai(ObservableEmitter emitter, Mj mj1) {
        Mj namj;
        int i;
        int shoushu = Config.systemConfig.wanjiapaishu / Config.systemConfig.meishouzhangshu;
        int fangxian = mj1.getDirection();
        MainController.weina = new LinkedList();
        List<Mj> temp_weina = new LinkedList();
        Mj duoyu_mj = null;
        do {
            int i2;
            int finalFangxian = fangxian;
            List<Mj> weina_1 = Config.weina.stream().filter(mj -> mj.getDirection() == finalFangxian).collect(Collectors.toList());
            Collections.sort(weina_1, new MainController.ComparatorMj());
            if (weina_1.size() % 3 == 0) {
                for (i2 = weina_1.size() - 1; i2 > -1; --i2) {
                    temp_weina.add(weina_1.get(i2));
                }
            } else {
                duoyu_mj = (Mj)weina_1.get(0);
                weina_1.remove(duoyu_mj);
                Collections.reverse(weina_1);
                for (i2 = 0; i2 < weina_1.size(); ++i2) {
                    temp_weina.add(weina_1.get(i2));
                }
            }
            if (--fangxian >= 0) continue;
            fangxian = 3;
        } while (fangxian != mj1.getDirection());
        this.napaifangxiang = MainController.zhuangjia;
        this.napaiweizhi = temp_weina.indexOf(mj1);
        if (Config.weina.stream().filter(mj -> mj.getDirection() == mj1.getDirection()).count() % 3L == 0L) {
            if (mj1.getMahjong_number() % 3 == 0) {
                this.napaiweizhi -= 2;
            }
            if (mj1.getMahjong_number() % 3 == 1) {
                --this.napaiweizhi;
            }
        } else {
            if ((mj1.getMahjong_number() + 2) % 3 == 0) {
                this.napaiweizhi -= 2;
            }
            if ((mj1.getMahjong_number() + 2) % 3 == 1) {
                --this.napaiweizhi;
            }
        }
        for (i = this.napaiweizhi; i < temp_weina.size(); ++i) {
            namj = (Mj)temp_weina.get(i);
            MainController.weina.add(namj);
        }
        for (i = 0; i < this.napaiweizhi; ++i) {
            namj = (Mj)temp_weina.get(i);
            MainController.weina.add(namj);
        }
        if (duoyu_mj != null) {
            MainController.weina.add(duoyu_mj);
        }
        for (i = 0; i < shoushu * Config.systemConfig.meishouzhangshu * Config.systemConfig.wanjiarenshu; i += Config.systemConfig.meishouzhangshu.intValue()) {
            for (int a = 0; a < Config.systemConfig.meishouzhangshu; ++a) {
                Mj namj2 = MainController.weina.getFirst();
                namj2.getWeina_imageView().setVisible(false);
                if (namj2.getOther() != null) {
                    namj2.getOther().setVisible(false);
                }
                namj2.setSimo(true);
                MainController.weina.remove(namj2);
                namj2.setNapai_direction(this.napaifangxiang);
                Config.yina.add(namj2);
            }
            do {
                ++this.napaifangxiang;
                if (this.napaifangxiang > 3) {
                    this.napaifangxiang = 0;
                }
            } while (MainController.directions.get(this.napaifangxiang).isQueren());
        }
        Observable.create(e -> {
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
        }).subscribe(integer3 -> {
            Mj namj_lam = MainController.weina.get(0);
            namj_lam.getWeina_imageView().setVisible(false);
            if (namj_lam.getOther() != null) {
                namj_lam.getOther().setVisible(false);
            }
            namj_lam.setSimo(true);
            MainController.weina.remove(namj_lam);
            namj_lam.setNapai_direction(MainController.zhuangjia);
            Config.yina.add(namj_lam);
            switch (Config.systemConfig.tiaopai) {
                case "\u5e73\u8df3": {
                    namj_lam = MainController.weina.get(2);
                    namj_lam.getWeina_imageView().setVisible(false);
                    if (namj_lam.getOther() != null) {
                        namj_lam.getOther().setVisible(false);
                    }
                    namj_lam.setSimo(true);
                    MainController.weina.remove(namj_lam);
                    namj_lam.setNapai_direction(MainController.zhuangjia);
                    Config.yina.add(namj_lam);
                    do {
                        ++this.napaifangxiang;
                        if (this.napaifangxiang > 3) {
                            this.napaifangxiang = 0;
                        }
                    } while (MainController.directions.get(this.napaifangxiang).isQueren());
                    switch (Config.systemConfig.wanjiarenshu) {
                        case 2: {
                            namj_lam = MainController.weina.get(0);
                            namj_lam.getWeina_imageView().setVisible(false);
                            if (namj_lam.getOther() != null) {
                                namj_lam.getOther().setVisible(false);
                            }
                            namj_lam.setSimo(true);
                            MainController.weina.remove(namj_lam);
                            namj_lam.setNapai_direction(this.napaifangxiang);
                            Config.yina.add(namj_lam);
                            break;
                        }
                        case 3: {
                            namj_lam = MainController.weina.get(0);
                            namj_lam.getWeina_imageView().setVisible(false);
                            if (namj_lam.getOther() != null) {
                                namj_lam.getOther().setVisible(false);
                            }
                            namj_lam.setSimo(true);
                            MainController.weina.remove(namj_lam);
                            namj_lam.setNapai_direction(this.napaifangxiang);
                            Config.yina.add(namj_lam);
                            do {
                                ++this.napaifangxiang;
                                if (this.napaifangxiang > 3) {
                                    this.napaifangxiang = 0;
                                }
                            } while (MainController.directions.get(this.napaifangxiang).isQueren());
                            namj_lam = MainController.weina.get(0);
                            namj_lam.getWeina_imageView().setVisible(false);
                            if (namj_lam.getOther() != null) {
                                namj_lam.getOther().setVisible(false);
                            }
                            namj_lam.setSimo(true);
                            MainController.weina.remove(namj_lam);
                            namj_lam.setNapai_direction(this.napaifangxiang);
                            Config.yina.add(namj_lam);
                            break;
                        }
                        case 4: {
                            namj_lam = MainController.weina.get(0);
                            namj_lam.getWeina_imageView().setVisible(false);
                            if (namj_lam.getOther() != null) {
                                namj_lam.getOther().setVisible(false);
                            }
                            namj_lam.setSimo(true);
                            MainController.weina.remove(namj_lam);
                            namj_lam.setNapai_direction(this.napaifangxiang);
                            Config.yina.add(namj_lam);
                            do {
                                ++this.napaifangxiang;
                                if (this.napaifangxiang > 3) {
                                    this.napaifangxiang = 0;
                                }
                            } while (MainController.directions.get(this.napaifangxiang).isQueren());
                            namj_lam = MainController.weina.get(0);
                            namj_lam.getWeina_imageView().setVisible(false);
                            if (namj_lam.getOther() != null) {
                                namj_lam.getOther().setVisible(false);
                            }
                            namj_lam.setSimo(true);
                            MainController.weina.remove(namj_lam);
                            namj_lam.setNapai_direction(this.napaifangxiang);
                            Config.yina.add(namj_lam);
                            do {
                                ++this.napaifangxiang;
                                if (this.napaifangxiang > 3) {
                                    this.napaifangxiang = 0;
                                }
                            } while (MainController.directions.get(this.napaifangxiang).isQueren());
                            namj_lam = MainController.weina.get(0);
                            namj_lam.getWeina_imageView().setVisible(false);
                            if (namj_lam.getOther() != null) {
                                namj_lam.getOther().setVisible(false);
                            }
                            namj_lam.setSimo(true);
                            MainController.weina.remove(namj_lam);
                            namj_lam.setNapai_direction(this.napaifangxiang);
                            Config.yina.add(namj_lam);
                        }
                    }
                    MainController.yucepaiAfterMopai(0, MainController.zhuangjia, 1);
                    break;
                }
                case "\u4e0a\u4e0b\u8df3": {
                    namj_lam = MainController.weina.get(0);
                    namj_lam.getWeina_imageView().setVisible(false);
                    if (namj_lam.getOther() != null) {
                        namj_lam.getOther().setVisible(false);
                    }
                    namj_lam.setSimo(true);
                    MainController.weina.remove(namj_lam);
                    namj_lam.setNapai_direction(MainController.zhuangjia);
                    Config.yina.add(namj_lam);
                    do {
                        ++this.napaifangxiang;
                        if (this.napaifangxiang > 3) {
                            this.napaifangxiang = 0;
                        }
                    } while (MainController.directions.get(this.napaifangxiang).isQueren());
                    switch (Config.systemConfig.wanjiarenshu) {
                        case 2: {
                            namj_lam = MainController.weina.get(0);
                            namj_lam.getWeina_imageView().setVisible(false);
                            if (namj_lam.getOther() != null) {
                                namj_lam.getOther().setVisible(false);
                            }
                            namj_lam.setSimo(true);
                            MainController.weina.remove(namj_lam);
                            namj_lam.setNapai_direction(this.napaifangxiang);
                            Config.yina.add(namj_lam);
                            break;
                        }
                        case 3: {
                            namj_lam = MainController.weina.get(0);
                            namj_lam.getWeina_imageView().setVisible(false);
                            if (namj_lam.getOther() != null) {
                                namj_lam.getOther().setVisible(false);
                            }
                            namj_lam.setSimo(true);
                            MainController.weina.remove(namj_lam);
                            namj_lam.setNapai_direction(this.napaifangxiang);
                            Config.yina.add(namj_lam);
                            do {
                                ++this.napaifangxiang;
                                if (this.napaifangxiang > 3) {
                                    this.napaifangxiang = 0;
                                }
                            } while (MainController.directions.get(this.napaifangxiang).isQueren());
                            namj_lam = MainController.weina.get(0);
                            namj_lam.getWeina_imageView().setVisible(false);
                            if (namj_lam.getOther() != null) {
                                namj_lam.getOther().setVisible(false);
                            }
                            namj_lam.setSimo(true);
                            MainController.weina.remove(namj_lam);
                            namj_lam.setNapai_direction(this.napaifangxiang);
                            Config.yina.add(namj_lam);
                            break;
                        }
                        case 4: {
                            namj_lam = MainController.weina.get(0);
                            namj_lam.getWeina_imageView().setVisible(false);
                            if (namj_lam.getOther() != null) {
                                namj_lam.getOther().setVisible(false);
                            }
                            namj_lam.setSimo(true);
                            MainController.weina.remove(namj_lam);
                            namj_lam.setNapai_direction(this.napaifangxiang);
                            Config.yina.add(namj_lam);
                            do {
                                ++this.napaifangxiang;
                                if (this.napaifangxiang > 3) {
                                    this.napaifangxiang = 0;
                                }
                            } while (MainController.directions.get(this.napaifangxiang).isQueren());
                            namj_lam = MainController.weina.get(0);
                            namj_lam.getWeina_imageView().setVisible(false);
                            if (namj_lam.getOther() != null) {
                                namj_lam.getOther().setVisible(false);
                            }
                            namj_lam.setSimo(true);
                            MainController.weina.remove(namj_lam);
                            namj_lam.setNapai_direction(this.napaifangxiang);
                            Config.yina.add(namj_lam);
                            do {
                                ++this.napaifangxiang;
                                if (this.napaifangxiang > 3) {
                                    this.napaifangxiang = 0;
                                }
                            } while (MainController.directions.get(this.napaifangxiang).isQueren());
                            namj_lam = MainController.weina.get(0);
                            namj_lam.getWeina_imageView().setVisible(false);
                            if (namj_lam.getOther() != null) {
                                namj_lam.getOther().setVisible(false);
                            }
                            namj_lam.setSimo(true);
                            MainController.weina.remove(namj_lam);
                            namj_lam.setNapai_direction(this.napaifangxiang);
                            Config.yina.add(namj_lam);
                        }
                    }
                    MainController.yucepaiAfterMopai(0, MainController.zhuangjia, 1);
                    --this.napaiweizhi;
                }
            }
            emitter.onNext(1);
        });
    }
}

