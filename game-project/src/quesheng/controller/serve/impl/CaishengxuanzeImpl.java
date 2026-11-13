/*
 * Decompiled with CFR 0.152.
 */
package quesheng.controller.serve.impl;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Rotate;
import javafx.stage.Modality;
import javafx.stage.Stage;
import quesheng.controller.BaseController;
import quesheng.controller.MainController;
import quesheng.controller.serve.AbstractServe;
import quesheng.customcontrole.MjPanelInterface;
import quesheng.model.Mj;
import quesheng.util.Config;

public class CaishengxuanzeImpl
extends AbstractServe {
    public CaishengxuanzeImpl(MainController mainController) {
        super(mainController);
    }

    @Override
    public void caishengxuanze(ObservableEmitter emitter) {
        if (MainController.directions_temp.size() < 1) {
            MainController.directions.forEach(dangqianba -> MainController.directions_temp.add((MjPanelInterface)dangqianba));
        }
        if (Config.systemConfig.zixuancaisheng == 1) {
            Observable.create(emitter1 -> {
                block4: {
                    block3: {
                        if (MainController.meijuchongshecaisheng == 1) break block3;
                        if (MainController.caisheng.size() != 0) break block4;
                    }
                    MainController.caisheng_seting_popup = new Stage();
                    MainController.caisheng_seting_popup.getIcons().add(new Image(BaseController.class.getResourceAsStream(Config.logo_url)));
                    MainController.caisheng_seting_popup.setAlwaysOnTop(true);
                    MainController.caisheng_seting_popup.initModality(Modality.APPLICATION_MODAL);
                    MainController.caisheng_seting_popup.resizableProperty().setValue(Boolean.FALSE);
                    MainController.caisheng_seting_popup.setResizable(false);
                    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/quesheng/view/CaiShengXuanZe.fxml"));
                    Pane tempPane = null;
                    try {
                        tempPane = (Pane)loader.load();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    Scene tempScene = new Scene(tempPane);
                    MainController.caisheng_seting_popup.setScene(tempScene);
                    MainController.caisheng_seting_popup.setTitle("\u8d22\u795e\u8bbe\u7f6e");
                    MainController.caisheng_seting_popup.setOnHidden(event1 -> {
                        if (MainController.caisheng.size() > 0) {
                            Config.weina.forEach(mj -> {
                                mj.caisheng = false;
                                if (MainController.caisheng.get(0) > 40) {
                                    if (mj.getResult() > 40) {
                                        mj.caisheng = true;
                                    }
                                } else if (MainController.caisheng.contains(mj.getResult())) {
                                    mj.caisheng = true;
                                }
                                if (Config.systemConfig.daiticaishengpai == 1 && Config.systemConfig.daiticaishengpai_combox > 0) {
                                    if (!MainController.caisheng.contains(Config.systemConfig.daiticaishengpai_combox) && mj.getResult() == Config.systemConfig.daiticaishengpai_combox) {
                                        mj.kedi_caisheng = true;
                                        mj.yuanpai = mj.getResult();
                                        mj.setResult(MainController.caisheng.get(0));
                                    }
                                }
                            });
                            emitter1.onNext(1);
                        } else {
                            Config.weina.forEach(mj -> {
                                mj.caisheng = false;
                            });
                            emitter1.onNext(1);
                        }
                    });
                    MainController.caisheng_seting_popup.show();
                    return;
                }
                Config.weina.forEach(mj -> {
                    mj.caisheng = false;
                    if (MainController.caisheng.get(0) > 40) {
                        if (mj.getResult() > 40) {
                            mj.caisheng = true;
                        }
                    } else if (MainController.caisheng.contains(mj.getResult())) {
                        mj.caisheng = true;
                    }
                    if (Config.systemConfig.daiticaishengpai == 1 && Config.systemConfig.daiticaishengpai_combox > 0) {
                        if (!MainController.caisheng.contains(Config.systemConfig.daiticaishengpai_combox) && mj.getResult() == Config.systemConfig.daiticaishengpai_combox) {
                            mj.kedi_caisheng = true;
                            mj.yuanpai = mj.getResult();
                            mj.setResult(MainController.caisheng.get(0));
                        }
                    }
                });
                emitter1.onNext(1);
            }).subscribe(integer3 -> emitter.onNext(1));
            return;
        }
        if (Config.systemConfig.renyicaisheng == 1) {
            MainController.renyicaisheng_label.setVisible(true);
            Observable.create(emitter1 -> {
                if (MainController.renyicaisheng_label.isVisible()) {
                    Config.weina.forEach(diyizhang -> {
                        if (diyizhang.getWeina_imageView() != null) {
                            diyizhang.getWeina_imageView().setOnMouseClicked(event -> {
                                if (Config.systemConfig.shengjicaisheng == 1) {
                                    int i;
                                    if (diyizhang.getResult() < 30) {
                                        if (diyizhang.getResult() % 10 < 9) {
                                            MainController.caisheng.add(diyizhang.getResult() + 1);
                                        } else {
                                            MainController.caisheng.add(diyizhang.getResult() / 10 * 10 + 1);
                                        }
                                    } else if (Config.systemConfig.zipaifengpaifenkaishengji == 0) {
                                        if (diyizhang.getResult() % 10 < 7 && diyizhang.getResult() < 40) {
                                            MainController.caisheng.add(diyizhang.getResult() + 1);
                                        } else if (Config.systemConfig.fankaibaibanshenghuahuashengdong == 1 && diyizhang.getResult() == 37) {
                                            for (i = 41; i < 49; ++i) {
                                                MainController.caisheng.add(i);
                                            }
                                        } else if (Config.systemConfig.fankaibaibanshenghuahuashengdong == 1 && diyizhang.getResult() > 40 && diyizhang.getResult() < 49) {
                                            MainController.caisheng.add(31);
                                        } else {
                                            MainController.caisheng.add(diyizhang.getResult() / 10 * 10 + 1);
                                        }
                                    } else if (diyizhang.getResult() < 35) {
                                        if (diyizhang.getResult() % 10 < 4) {
                                            MainController.caisheng.add(diyizhang.getResult() + 1);
                                        } else {
                                            MainController.caisheng.add(diyizhang.getResult() / 10 * 10 + 1);
                                        }
                                    } else if (diyizhang.getResult() % 10 < 7 && diyizhang.getResult() < 40) {
                                        MainController.caisheng.add(diyizhang.getResult() + 1);
                                    } else if (Config.systemConfig.fankaibaibanshenghuahuashengdong == 1 && diyizhang.getResult() == 37) {
                                        for (i = 41; i < 49; ++i) {
                                            MainController.caisheng.add(i);
                                        }
                                    } else if (Config.systemConfig.fankaibaibanshenghuahuashengdong == 1 && diyizhang.getResult() > 40 && diyizhang.getResult() < 49) {
                                        MainController.caisheng.add(31);
                                    } else {
                                        MainController.caisheng.add(diyizhang.getResult() / 10 * 10 + 5);
                                    }
                                    diyizhang.getWeina_imageView().setVisible(false);
                                    if (diyizhang.getOther() != null) {
                                        diyizhang.getOther().setVisible(false);
                                    }
                                    diyizhang.setSimo(true);
                                    Mj finalNamj = diyizhang;
                                    Config.weina.forEach(mj -> {
                                        if (finalNamj.equals(mj)) {
                                            mj.setSimo(true);
                                        }
                                    });
                                    MainController.weina.remove(diyizhang);
                                    Config.yuce.clear();
                                    MainController.yucepaiAfterMopai(0, MainController.dapaifangx, 1);
                                    Config.weina.forEach(mj -> {
                                        mj.caisheng = false;
                                        if (MainController.caisheng.contains(mj.getResult())) {
                                            mj.caisheng = true;
                                        }
                                        if (Config.systemConfig.daiticaishengpai == 1 && Config.systemConfig.daiticaishengpai_combox > 0) {
                                            if (!MainController.caisheng.contains(Config.systemConfig.daiticaishengpai_combox) && mj.getResult() == Config.systemConfig.daiticaishengpai_combox) {
                                                mj.kedi_caisheng = true;
                                                mj.yuanpai = mj.getResult();
                                                mj.setResult(MainController.caisheng.get(0));
                                            }
                                        }
                                    });
                                    emitter1.onNext(1);
                                    return;
                                }
                                MainController.caisheng.clear();
                                MainController.caisheng.add(diyizhang.getResult());
                                if (Config.systemConfig.fankaidepaibuzuocaisheng == 0) {
                                    diyizhang.getWeina_imageView().setVisible(false);
                                    if (diyizhang.getOther() != null) {
                                        diyizhang.getOther().setVisible(false);
                                    }
                                    diyizhang.setSimo(true);
                                    Mj finalNamj = diyizhang;
                                    Config.weina.forEach(mj -> {
                                        if (finalNamj.equals(mj)) {
                                            mj.setSimo(true);
                                        }
                                    });
                                    MainController.weina.remove(diyizhang);
                                }
                                Config.yuce.clear();
                                MainController.yucepaiAfterMopai(0, MainController.dapaifangx, 1);
                                Config.weina.forEach(mj -> {
                                    mj.caisheng = false;
                                    if (MainController.caisheng.contains(mj.getResult())) {
                                        if (Config.systemConfig.fankaidepaibuzuocaisheng == 1) {
                                            if (!mj.equals(diyizhang)) {
                                                mj.caisheng = true;
                                            }
                                        } else {
                                            mj.caisheng = true;
                                        }
                                    }
                                    if (Config.systemConfig.daiticaishengpai == 1 && Config.systemConfig.daiticaishengpai_combox > 0) {
                                        if (!MainController.caisheng.contains(Config.systemConfig.daiticaishengpai_combox) && mj.getResult() == Config.systemConfig.daiticaishengpai_combox) {
                                            mj.kedi_caisheng = true;
                                            mj.yuanpai = mj.getResult();
                                            mj.setResult(MainController.caisheng.get(0));
                                        }
                                    }
                                });
                                emitter1.onNext(1);
                            });
                        }
                    });
                }
            }).subscribe(integer3 -> {
                Config.weina.forEach(diyizhang -> {
                    if (diyizhang.getWeina_imageView() != null) {
                        diyizhang.getWeina_imageView().setOnMouseClicked(event -> this.mainController.napaisuanfa((Mj)diyizhang.getWeina_imageView().getUserData()));
                    }
                });
                MainController.renyicaisheng_label.setVisible(false);
                emitter.onNext(1);
            });
            return;
        }
        if (Config.systemConfig.shunmocaisheng == 1) {
            if (Config.systemConfig.shengjicaisheng == 1) {
                int i;
                Mj diyizhang = MainController.weina.get(0);
                if (diyizhang.getResult() < 30) {
                    if (diyizhang.getResult() % 10 < 9) {
                        MainController.caisheng.add(diyizhang.getResult() + 1);
                    } else {
                        MainController.caisheng.add(diyizhang.getResult() / 10 * 10 + 1);
                    }
                } else if (Config.systemConfig.zipaifengpaifenkaishengji == 0) {
                    if (diyizhang.getResult() % 10 < 7 && diyizhang.getResult() < 40) {
                        MainController.caisheng.add(diyizhang.getResult() + 1);
                    } else if (Config.systemConfig.fankaibaibanshenghuahuashengdong == 1 && diyizhang.getResult() == 37) {
                        for (i = 41; i < 49; ++i) {
                            MainController.caisheng.add(i);
                        }
                    } else if (Config.systemConfig.fankaibaibanshenghuahuashengdong == 1 && diyizhang.getResult() > 40 && diyizhang.getResult() < 49) {
                        MainController.caisheng.add(31);
                    } else {
                        MainController.caisheng.add(diyizhang.getResult() / 10 * 10 + 1);
                    }
                } else if (diyizhang.getResult() < 35) {
                    if (diyizhang.getResult() % 10 < 4) {
                        MainController.caisheng.add(diyizhang.getResult() + 1);
                    } else {
                        MainController.caisheng.add(diyizhang.getResult() / 10 * 10 + 1);
                    }
                } else if (diyizhang.getResult() % 10 < 7 && diyizhang.getResult() < 40) {
                    MainController.caisheng.add(diyizhang.getResult() + 1);
                } else if (Config.systemConfig.fankaibaibanshenghuahuashengdong == 1 && diyizhang.getResult() == 37) {
                    for (i = 41; i < 49; ++i) {
                        MainController.caisheng.add(i);
                    }
                } else if (Config.systemConfig.fankaibaibanshenghuahuashengdong == 1 && diyizhang.getResult() > 40 && diyizhang.getResult() < 49) {
                    MainController.caisheng.add(31);
                } else {
                    MainController.caisheng.add(diyizhang.getResult() / 10 * 10 + 5);
                }
                diyizhang.getWeina_imageView().setVisible(false);
                if (diyizhang.getOther() != null) {
                    diyizhang.getOther().setVisible(false);
                }
                diyizhang.setSimo(true);
                Mj finalNamj = diyizhang;
                Config.weina.forEach(mj -> {
                    if (finalNamj.equals(mj)) {
                        mj.setSimo(true);
                    }
                });
                MainController.weina.remove(diyizhang);
                if (Config.systemConfig.jiaohuanpai == 1) {
                    Mj daoshudisanzhang = MainController.weina.get(MainController.weina.size() - 6);
                    double x = daoshudisanzhang.getWeina_imageView().getLayoutX();
                    double y = daoshudisanzhang.getWeina_imageView().getLayoutY();
                    daoshudisanzhang.getWeina_imageView().setLayoutX(diyizhang.getWeina_imageView().getLayoutX());
                    daoshudisanzhang.getWeina_imageView().setLayoutY(diyizhang.getWeina_imageView().getLayoutY());
                    diyizhang.getWeina_imageView().setLayoutX(x);
                    diyizhang.getWeina_imageView().setLayoutY(y);
                    if (daoshudisanzhang.getDirection() == MainController.directions_temp.get(2).getDirection() && diyizhang.getDirection() == MainController.directions_temp.get(1).getDirection()) {
                        daoshudisanzhang.getWeina_imageView().getTransforms().add(new Rotate(-90.0));
                        diyizhang.getWeina_imageView().getTransforms().add(new Rotate(90.0));
                    }
                    if (daoshudisanzhang.getDirection() == MainController.directions_temp.get(0).getDirection() && diyizhang.getDirection() == MainController.directions_temp.get(3).getDirection()) {
                        daoshudisanzhang.getWeina_imageView().getTransforms().add(new Rotate(90.0));
                        diyizhang.getWeina_imageView().getTransforms().add(new Rotate(-90.0));
                    }
                    if (daoshudisanzhang.getDirection() == MainController.directions_temp.get(3).getDirection() && diyizhang.getDirection() == MainController.directions_temp.get(2).getDirection()) {
                        daoshudisanzhang.getWeina_imageView().getTransforms().add(new Rotate(90.0));
                        diyizhang.getWeina_imageView().getTransforms().add(new Rotate(90.0));
                        daoshudisanzhang.getWeina_imageView().setLayoutX(daoshudisanzhang.getWeina_imageView().getLayoutX() + Config.MJ_WIDTH_1_2);
                        daoshudisanzhang.getWeina_imageView().setLayoutY(daoshudisanzhang.getWeina_imageView().getLayoutY() + Config.MJ_HEIGHT_1_2);
                    }
                    if (daoshudisanzhang.getDirection() == MainController.directions_temp.get(1).getDirection() && diyizhang.getDirection() == MainController.directions_temp.get(0).getDirection()) {
                        daoshudisanzhang.getWeina_imageView().getTransforms().add(new Rotate(90.0));
                        diyizhang.getWeina_imageView().getTransforms().add(new Rotate(-90.0));
                    }
                    if (daoshudisanzhang.getDirection() == MainController.directions_temp.get(1).getDirection() && diyizhang.getDirection() == MainController.directions_temp.get(3).getDirection()) {
                        daoshudisanzhang.getWeina_imageView().getTransforms().add(new Rotate(180.0));
                        diyizhang.getWeina_imageView().getTransforms().add(new Rotate(180.0));
                    }
                    if (daoshudisanzhang.getDirection() == MainController.directions_temp.get(0).getDirection() && diyizhang.getDirection() == MainController.directions_temp.get(2).getDirection()) {
                        daoshudisanzhang.getWeina_imageView().getTransforms().add(new Rotate(180.0));
                        diyizhang.getWeina_imageView().getTransforms().add(new Rotate(180.0));
                        daoshudisanzhang.getWeina_imageView().setLayoutX(daoshudisanzhang.getWeina_imageView().getLayoutX() + Config.MJ_WIDTH_1_2);
                        daoshudisanzhang.getWeina_imageView().setLayoutY(daoshudisanzhang.getWeina_imageView().getLayoutY() + Config.MJ_HEIGHT_1_2);
                        diyizhang.getWeina_imageView().setLayoutX(diyizhang.getWeina_imageView().getLayoutX() + Config.MJ_WIDTH_1_2);
                        diyizhang.getWeina_imageView().setLayoutY(diyizhang.getWeina_imageView().getLayoutY() + Config.MJ_HEIGHT_1_2);
                    }
                    if (daoshudisanzhang.getDirection() == MainController.directions_temp.get(3).getDirection() && diyizhang.getDirection() == MainController.directions_temp.get(1).getDirection()) {
                        daoshudisanzhang.getWeina_imageView().getTransforms().add(new Rotate(180.0));
                        diyizhang.getWeina_imageView().getTransforms().add(new Rotate(180.0));
                    }
                    MainController.weina.remove(daoshudisanzhang);
                    MainController.weina.add(0, daoshudisanzhang);
                    MainController.weina.add(MainController.weina.size() - 6, diyizhang);
                    diyizhang.getWeina_imageView().setVisible(true);
                    if (diyizhang.getOther() != null) {
                        diyizhang.getOther().setVisible(true);
                    }
                    diyizhang.setSimo(false);
                    Config.weina.forEach(mj -> {
                        if (finalNamj.equals(mj)) {
                            mj.setSimo(false);
                        }
                    });
                }
                Config.yuce.clear();
                MainController.yucepaiAfterMopai(0, MainController.dapaifangx, 1);
                Config.weina.forEach(mj -> {
                    mj.caisheng = false;
                    if (MainController.caisheng.get(0) > 40) {
                        if (mj.getResult() > 40) {
                            mj.caisheng = true;
                        }
                    } else if (MainController.caisheng.contains(mj.getResult())) {
                        mj.caisheng = true;
                    }
                    if (Config.systemConfig.daiticaishengpai == 1 && Config.systemConfig.daiticaishengpai_combox > 0) {
                        if (!MainController.caisheng.contains(Config.systemConfig.daiticaishengpai_combox) && mj.getResult() == Config.systemConfig.daiticaishengpai_combox) {
                            mj.kedi_caisheng = true;
                            mj.yuanpai = mj.getResult();
                            mj.setResult(MainController.caisheng.get(0));
                        }
                    }
                    if (Config.systemConfig.huapaidaiticaishengpai_foushanmajiang == 1) {
                        if (MainController.caisheng.get(0) < 40 && mj.getResult() > 40) {
                            mj.kedi_caisheng = true;
                            mj.yuanpai = mj.getResult();
                            mj.setResult(MainController.caisheng.get(0));
                        }
                    }
                });
                emitter.onNext(1);
                return;
            }
            Mj diyizhang = MainController.weina.get(0);
            MainController.caisheng.clear();
            MainController.caisheng.add(diyizhang.getResult());
            Mj finalNamj = diyizhang;
            diyizhang.setSimo(true);
            diyizhang.getWeina_imageView().setVisible(false);
            if (diyizhang.getOther() != null) {
                diyizhang.getOther().setVisible(false);
            }
            Config.weina.forEach(mj -> {
                if (finalNamj.equals(mj)) {
                    mj.setSimo(true);
                }
            });
            MainController.weina.remove(diyizhang);
            if (Config.systemConfig.jiaohuanpai == 1) {
                Mj daoshudisanzhang = MainController.weina.get(MainController.weina.size() - 6);
                double x = daoshudisanzhang.getWeina_imageView().getLayoutX();
                double y = daoshudisanzhang.getWeina_imageView().getLayoutY();
                daoshudisanzhang.getWeina_imageView().setLayoutX(diyizhang.getWeina_imageView().getLayoutX());
                daoshudisanzhang.getWeina_imageView().setLayoutY(diyizhang.getWeina_imageView().getLayoutY());
                diyizhang.getWeina_imageView().setLayoutX(x);
                diyizhang.getWeina_imageView().setLayoutY(y);
                if (daoshudisanzhang.getDirection() == MainController.directions_temp.get(2).getDirection() && diyizhang.getDirection() == MainController.directions_temp.get(1).getDirection()) {
                    daoshudisanzhang.getWeina_imageView().getTransforms().add(new Rotate(-90.0));
                    diyizhang.getWeina_imageView().getTransforms().add(new Rotate(90.0));
                }
                if (daoshudisanzhang.getDirection() == MainController.directions_temp.get(0).getDirection() && diyizhang.getDirection() == MainController.directions_temp.get(3).getDirection()) {
                    daoshudisanzhang.getWeina_imageView().getTransforms().add(new Rotate(90.0));
                    diyizhang.getWeina_imageView().getTransforms().add(new Rotate(-90.0));
                }
                if (daoshudisanzhang.getDirection() == MainController.directions_temp.get(3).getDirection() && diyizhang.getDirection() == MainController.directions_temp.get(2).getDirection()) {
                    daoshudisanzhang.getWeina_imageView().getTransforms().add(new Rotate(90.0));
                    diyizhang.getWeina_imageView().getTransforms().add(new Rotate(90.0));
                    daoshudisanzhang.getWeina_imageView().setLayoutX(daoshudisanzhang.getWeina_imageView().getLayoutX() + Config.MJ_WIDTH_1_2);
                    daoshudisanzhang.getWeina_imageView().setLayoutY(daoshudisanzhang.getWeina_imageView().getLayoutY() + Config.MJ_HEIGHT_1_2);
                }
                if (daoshudisanzhang.getDirection() == MainController.directions_temp.get(1).getDirection() && diyizhang.getDirection() == MainController.directions_temp.get(0).getDirection()) {
                    daoshudisanzhang.getWeina_imageView().getTransforms().add(new Rotate(90.0));
                    diyizhang.getWeina_imageView().getTransforms().add(new Rotate(-90.0));
                }
                if (daoshudisanzhang.getDirection() == MainController.directions_temp.get(1).getDirection() && diyizhang.getDirection() == MainController.directions_temp.get(3).getDirection()) {
                    daoshudisanzhang.getWeina_imageView().getTransforms().add(new Rotate(180.0));
                    diyizhang.getWeina_imageView().getTransforms().add(new Rotate(180.0));
                }
                if (daoshudisanzhang.getDirection() == MainController.directions_temp.get(0).getDirection() && diyizhang.getDirection() == MainController.directions_temp.get(2).getDirection()) {
                    daoshudisanzhang.getWeina_imageView().getTransforms().add(new Rotate(180.0));
                    diyizhang.getWeina_imageView().getTransforms().add(new Rotate(180.0));
                    daoshudisanzhang.getWeina_imageView().setLayoutX(daoshudisanzhang.getWeina_imageView().getLayoutX() + Config.MJ_WIDTH_1_2);
                    daoshudisanzhang.getWeina_imageView().setLayoutY(daoshudisanzhang.getWeina_imageView().getLayoutY() + Config.MJ_HEIGHT_1_2);
                    diyizhang.getWeina_imageView().setLayoutX(diyizhang.getWeina_imageView().getLayoutX() + Config.MJ_WIDTH_1_2);
                    diyizhang.getWeina_imageView().setLayoutY(diyizhang.getWeina_imageView().getLayoutY() + Config.MJ_HEIGHT_1_2);
                }
                if (daoshudisanzhang.getDirection() == MainController.directions_temp.get(3).getDirection() && diyizhang.getDirection() == MainController.directions_temp.get(1).getDirection()) {
                    daoshudisanzhang.getWeina_imageView().getTransforms().add(new Rotate(180.0));
                    diyizhang.getWeina_imageView().getTransforms().add(new Rotate(180.0));
                }
                MainController.weina.remove(daoshudisanzhang);
                MainController.weina.add(0, daoshudisanzhang);
                MainController.weina.add(MainController.weina.size() - 6, diyizhang);
                diyizhang.getWeina_imageView().setVisible(true);
                if (diyizhang.getOther() != null) {
                    diyizhang.getOther().setVisible(true);
                }
                diyizhang.setSimo(false);
                Config.weina.forEach(mj -> {
                    if (finalNamj.equals(mj)) {
                        mj.setSimo(false);
                    }
                });
            }
            Config.yuce.clear();
            MainController.yucepaiAfterMopai(0, MainController.dapaifangx, 1);
            Config.weina.forEach(mj -> {
                mj.caisheng = false;
                if (MainController.caisheng.get(0) > 40) {
                    if (mj.getResult() > 40) {
                        mj.caisheng = true;
                    }
                } else if (MainController.caisheng.contains(mj.getResult())) {
                    mj.caisheng = true;
                }
                if (Config.systemConfig.daiticaishengpai == 1 && Config.systemConfig.daiticaishengpai_combox > 0) {
                    if (!MainController.caisheng.contains(Config.systemConfig.daiticaishengpai_combox) && mj.getResult() == Config.systemConfig.daiticaishengpai_combox) {
                        mj.kedi_caisheng = true;
                        mj.yuanpai = mj.getResult();
                        mj.setResult(MainController.caisheng.get(0));
                    }
                }
                if (Config.systemConfig.huapaidaiticaishengpai_foushanmajiang == 1) {
                    if (MainController.caisheng.get(0) < 40 && mj.getResult() > 40) {
                        mj.kedi_caisheng = true;
                        mj.yuanpai = mj.getResult();
                        mj.setResult(MainController.caisheng.get(0));
                    }
                }
            });
            emitter.onNext(1);
            return;
        }
        if (Config.systemConfig.houcaisheng == 1) {
            if (Config.systemConfig.shengjicaisheng == 1) {
                int i;
                Mj diyizhang = MainController.weina.get(MainController.weina.size() - 2);
                if (diyizhang.getResult() < 30) {
                    if (diyizhang.getResult() % 10 < 9) {
                        MainController.caisheng.add(diyizhang.getResult() + 1);
                    } else {
                        MainController.caisheng.add(diyizhang.getResult() / 10 * 10 + 1);
                    }
                } else if (Config.systemConfig.zipaifengpaifenkaishengji == 0) {
                    if (diyizhang.getResult() % 10 < 7 && diyizhang.getResult() < 40) {
                        MainController.caisheng.add(diyizhang.getResult() + 1);
                    } else if (Config.systemConfig.fankaibaibanshenghuahuashengdong == 1 && diyizhang.getResult() == 37) {
                        for (i = 41; i < 49; ++i) {
                            MainController.caisheng.add(i);
                        }
                    } else if (Config.systemConfig.fankaibaibanshenghuahuashengdong == 1 && diyizhang.getResult() > 40 && diyizhang.getResult() < 49) {
                        MainController.caisheng.add(31);
                    } else {
                        MainController.caisheng.add(diyizhang.getResult() / 10 * 10 + 1);
                    }
                } else if (diyizhang.getResult() < 35) {
                    if (diyizhang.getResult() % 10 < 4) {
                        MainController.caisheng.add(diyizhang.getResult() + 1);
                    } else {
                        MainController.caisheng.add(diyizhang.getResult() / 10 * 10 + 1);
                    }
                } else if (diyizhang.getResult() % 10 < 7 && diyizhang.getResult() < 40) {
                    MainController.caisheng.add(diyizhang.getResult() + 1);
                } else if (Config.systemConfig.fankaibaibanshenghuahuashengdong == 1 && diyizhang.getResult() == 37) {
                    for (i = 41; i < 49; ++i) {
                        MainController.caisheng.add(i);
                    }
                } else if (Config.systemConfig.fankaibaibanshenghuahuashengdong == 1 && diyizhang.getResult() > 40 && diyizhang.getResult() < 49) {
                    MainController.caisheng.add(31);
                } else {
                    MainController.caisheng.add(diyizhang.getResult() / 10 * 10 + 5);
                }
                diyizhang.getWeina_imageView().setVisible(false);
                if (diyizhang.getOther() != null) {
                    diyizhang.getOther().setVisible(false);
                }
                diyizhang.setSimo(true);
                Mj finalNamj = diyizhang;
                Config.weina.forEach(mj -> {
                    if (finalNamj.equals(mj)) {
                        mj.setSimo(true);
                    }
                });
                MainController.weina.remove(diyizhang);
                Config.yuce.clear();
                MainController.yucepaiAfterMopai(0, MainController.dapaifangx, 1);
                Config.weina.forEach(mj -> {
                    mj.caisheng = false;
                    if (MainController.caisheng.contains(mj.getResult())) {
                        mj.caisheng = true;
                    }
                    if (Config.systemConfig.daiticaishengpai == 1 && Config.systemConfig.daiticaishengpai_combox > 0) {
                        if (!MainController.caisheng.contains(Config.systemConfig.daiticaishengpai_combox) && mj.getResult() == Config.systemConfig.daiticaishengpai_combox) {
                            mj.kedi_caisheng = true;
                            mj.yuanpai = mj.getResult();
                            mj.setResult(MainController.caisheng.get(0));
                        }
                    }
                });
                emitter.onNext(1);
                return;
            }
            Mj diyizhang = MainController.weina.get(MainController.weina.size() - 2);
            MainController.caisheng.add(diyizhang.getResult());
            diyizhang.getWeina_imageView().setVisible(false);
            if (diyizhang.getOther() != null) {
                diyizhang.getOther().setVisible(false);
            }
            diyizhang.setSimo(true);
            Mj finalNamj = diyizhang;
            Config.weina.forEach(mj -> {
                if (finalNamj.equals(mj)) {
                    mj.setSimo(true);
                }
            });
            MainController.weina.remove(diyizhang);
            Config.yuce.clear();
            MainController.yucepaiAfterMopai(0, MainController.dapaifangx, 1);
            Config.weina.forEach(mj -> {
                mj.caisheng = false;
                if (MainController.caisheng.contains(mj.getResult())) {
                    mj.caisheng = true;
                }
                if (Config.systemConfig.daiticaishengpai == 1 && Config.systemConfig.daiticaishengpai_combox > 0) {
                    if (!MainController.caisheng.contains(Config.systemConfig.daiticaishengpai_combox) && mj.getResult() == Config.systemConfig.daiticaishengpai_combox) {
                        mj.kedi_caisheng = true;
                        mj.yuanpai = mj.getResult();
                        mj.setResult(MainController.caisheng.get(0));
                    }
                }
            });
            emitter.onNext(1);
            return;
        }
        if (Config.systemConfig.shengjicaisheng == 1) {
            Mj diyizhang = MainController.weina.get(0);
            if (diyizhang.getResult() < 30) {
                if (diyizhang.getResult() % 10 < 9) {
                    MainController.caisheng.add(diyizhang.getResult() + 1);
                } else {
                    MainController.caisheng.add(diyizhang.getResult() / 10 * 10 + 1);
                }
            } else if (diyizhang.getResult() > 30) {
                int i;
                if (Config.systemConfig.zipaifengpaifenkaishengji == 0) {
                    if (diyizhang.getResult() % 10 < 7 && diyizhang.getResult() < 40) {
                        MainController.caisheng.add(diyizhang.getResult() + 1);
                    } else if (Config.systemConfig.fankaibaibanshenghuahuashengdong == 1 && diyizhang.getResult() == 37) {
                        for (i = 41; i < 49; ++i) {
                            MainController.caisheng.add(i);
                        }
                    } else if (Config.systemConfig.fankaibaibanshenghuahuashengdong == 1 && diyizhang.getResult() > 40 && diyizhang.getResult() < 49) {
                        MainController.caisheng.add(31);
                    } else if (diyizhang.getResult() < 40) {
                        MainController.caisheng.add(diyizhang.getResult() / 10 * 10 + 1);
                    }
                } else if (diyizhang.getResult() < 35) {
                    if (diyizhang.getResult() % 10 < 4) {
                        MainController.caisheng.add(diyizhang.getResult() + 1);
                    } else {
                        MainController.caisheng.add(diyizhang.getResult() / 10 * 10 + 1);
                    }
                } else if (diyizhang.getResult() % 10 < 7 && diyizhang.getResult() < 40) {
                    MainController.caisheng.add(diyizhang.getResult() + 1);
                } else if (Config.systemConfig.fankaibaibanshenghuahuashengdong == 1 && diyizhang.getResult() == 37) {
                    for (i = 41; i < 49; ++i) {
                        MainController.caisheng.add(i);
                    }
                } else if (Config.systemConfig.fankaibaibanshenghuahuashengdong == 1 && diyizhang.getResult() > 40 && diyizhang.getResult() < 49) {
                    MainController.caisheng.add(31);
                } else {
                    MainController.caisheng.add(diyizhang.getResult() / 10 * 10 + 5);
                }
                if (Config.systemConfig.huapaidaiticaishengpai_foushanmajiang == 1) {
                    if (diyizhang.getResult() % 10 < 7) {
                        MainController.caisheng.add(diyizhang.getResult() + 1);
                    } else {
                        MainController.caisheng.add(41);
                    }
                }
            } else if (Config.systemConfig.huapaidaiticaishengpai_foushanmajiang == 1) {
                MainController.caisheng.add(31);
            }
            diyizhang.getWeina_imageView().setVisible(false);
            if (diyizhang.getOther() != null) {
                diyizhang.getOther().setVisible(false);
            }
            diyizhang.setSimo(true);
            Mj finalNamj = diyizhang;
            Config.weina.forEach(mj -> {
                if (finalNamj.equals(mj)) {
                    mj.setSimo(true);
                }
            });
            MainController.weina.remove(diyizhang);
            if (Config.systemConfig.jiaohuanpai == 1) {
                Mj daoshudisanzhang = MainController.weina.get(MainController.weina.size() - 6);
                double x = daoshudisanzhang.getWeina_imageView().getLayoutX();
                double y = daoshudisanzhang.getWeina_imageView().getLayoutY();
                daoshudisanzhang.getWeina_imageView().setLayoutX(diyizhang.getWeina_imageView().getLayoutX());
                daoshudisanzhang.getWeina_imageView().setLayoutY(diyizhang.getWeina_imageView().getLayoutY());
                diyizhang.getWeina_imageView().setLayoutX(x);
                diyizhang.getWeina_imageView().setLayoutY(y);
                if (daoshudisanzhang.getDirection() == MainController.directions_temp.get(2).getDirection() && diyizhang.getDirection() == MainController.directions_temp.get(1).getDirection()) {
                    daoshudisanzhang.getWeina_imageView().getTransforms().add(new Rotate(-90.0));
                    diyizhang.getWeina_imageView().getTransforms().add(new Rotate(90.0));
                }
                if (daoshudisanzhang.getDirection() == MainController.directions_temp.get(0).getDirection() && diyizhang.getDirection() == MainController.directions_temp.get(3).getDirection()) {
                    daoshudisanzhang.getWeina_imageView().getTransforms().add(new Rotate(90.0));
                    diyizhang.getWeina_imageView().getTransforms().add(new Rotate(-90.0));
                }
                if (daoshudisanzhang.getDirection() == MainController.directions_temp.get(3).getDirection() && diyizhang.getDirection() == MainController.directions_temp.get(2).getDirection()) {
                    daoshudisanzhang.getWeina_imageView().getTransforms().add(new Rotate(90.0));
                    diyizhang.getWeina_imageView().getTransforms().add(new Rotate(90.0));
                    daoshudisanzhang.getWeina_imageView().setLayoutX(daoshudisanzhang.getWeina_imageView().getLayoutX() + Config.MJ_WIDTH_1_2);
                    daoshudisanzhang.getWeina_imageView().setLayoutY(daoshudisanzhang.getWeina_imageView().getLayoutY() + Config.MJ_HEIGHT_1_2);
                }
                if (daoshudisanzhang.getDirection() == MainController.directions_temp.get(1).getDirection() && diyizhang.getDirection() == MainController.directions_temp.get(0).getDirection()) {
                    daoshudisanzhang.getWeina_imageView().getTransforms().add(new Rotate(90.0));
                    diyizhang.getWeina_imageView().getTransforms().add(new Rotate(-90.0));
                }
                if (daoshudisanzhang.getDirection() == MainController.directions_temp.get(1).getDirection() && diyizhang.getDirection() == MainController.directions_temp.get(3).getDirection()) {
                    daoshudisanzhang.getWeina_imageView().getTransforms().add(new Rotate(180.0));
                    diyizhang.getWeina_imageView().getTransforms().add(new Rotate(180.0));
                }
                if (daoshudisanzhang.getDirection() == MainController.directions_temp.get(0).getDirection() && diyizhang.getDirection() == MainController.directions_temp.get(2).getDirection()) {
                    daoshudisanzhang.getWeina_imageView().getTransforms().add(new Rotate(180.0));
                    diyizhang.getWeina_imageView().getTransforms().add(new Rotate(180.0));
                    daoshudisanzhang.getWeina_imageView().setLayoutX(daoshudisanzhang.getWeina_imageView().getLayoutX() + Config.MJ_WIDTH_1_2);
                    daoshudisanzhang.getWeina_imageView().setLayoutY(daoshudisanzhang.getWeina_imageView().getLayoutY() + Config.MJ_HEIGHT_1_2);
                    diyizhang.getWeina_imageView().setLayoutX(diyizhang.getWeina_imageView().getLayoutX() + Config.MJ_WIDTH_1_2);
                    diyizhang.getWeina_imageView().setLayoutY(diyizhang.getWeina_imageView().getLayoutY() + Config.MJ_HEIGHT_1_2);
                }
                if (daoshudisanzhang.getDirection() == MainController.directions_temp.get(3).getDirection() && diyizhang.getDirection() == MainController.directions_temp.get(1).getDirection()) {
                    daoshudisanzhang.getWeina_imageView().getTransforms().add(new Rotate(180.0));
                    diyizhang.getWeina_imageView().getTransforms().add(new Rotate(180.0));
                }
                MainController.weina.remove(daoshudisanzhang);
                MainController.weina.add(0, daoshudisanzhang);
                MainController.weina.add(MainController.weina.size() - 6, diyizhang);
                diyizhang.getWeina_imageView().setVisible(true);
                if (diyizhang.getOther() != null) {
                    diyizhang.getOther().setVisible(true);
                }
                diyizhang.setSimo(false);
                Config.weina.forEach(mj -> {
                    if (finalNamj.equals(mj)) {
                        mj.setSimo(false);
                    }
                });
            }
            Config.yuce.clear();
            MainController.yucepaiAfterMopai(0, MainController.dapaifangx, 1);
            Config.weina.forEach(mj -> {
                mj.caisheng = false;
                if (Config.systemConfig.huapaidaiticaishengpai_foushanmajiang == 1) {
                    if (MainController.caisheng.get(0) > 40) {
                        if (mj.getResult() > 40) {
                            mj.caisheng = true;
                        }
                    } else if (MainController.caisheng.contains(mj.getResult())) {
                        mj.caisheng = true;
                    }
                } else if (MainController.caisheng.contains(mj.getResult())) {
                    mj.caisheng = true;
                }
                if (Config.systemConfig.daiticaishengpai == 1 && Config.systemConfig.daiticaishengpai_combox > 0) {
                    if (!MainController.caisheng.contains(Config.systemConfig.daiticaishengpai_combox) && mj.getResult() == Config.systemConfig.daiticaishengpai_combox) {
                        mj.kedi_caisheng = true;
                        mj.yuanpai = mj.getResult();
                        mj.setResult(MainController.caisheng.get(0));
                    }
                }
                if (Config.systemConfig.huapaidaiticaishengpai_foushanmajiang == 1) {
                    if (MainController.caisheng.get(0) < 40 && mj.getResult() > 40) {
                        mj.kedi_caisheng = true;
                        mj.yuanpai = mj.getResult();
                        mj.setResult(MainController.caisheng.get(0));
                    }
                }
            });
            emitter.onNext(1);
            return;
        }
        emitter.onNext(1);
    }
}

