/*
 * Decompiled with CFR 0.152.
 */
package quesheng.customcontrole;

import java.awt.AWTException;
import java.awt.Robot;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import quesheng.controller.MainController;
import quesheng.customcontrole.MjHBoxLeftControl;
import quesheng.customcontrole.MjPanel;
import quesheng.customcontrole.MjPanelInterface;
import quesheng.customcontrole.MjYuCeControl;
import quesheng.model.Mj;
import quesheng.util.Config;
import quesheng.util.HuPaiKit;
import quesheng.util.ImageKit;
import quesheng.util.JFXKit;

public final class RightPanel
extends MjPanel {
    public AnchorPane yinaPanel;
    public VBox houpaiPanel;
    public VBox yucePanel;
    public AnchorPane hupaiPanel;
    public VBox caozuoPanel;
    public VBox quePanel;
    private double offect_y = 0.0;
    public double height = 0.0;

    public RightPanel() {
        this.direction = 1;
        this.group = new AnchorPane();
        this.getChildren().add(this.group);
        this.initContent();
    }

    @Override
    public void display() {
        ImageView imageView1;
        int i;
        Mj mj2;
        this.offect_y = 0.0;
        List<Mj> yina_bottom = Config.yina.stream().filter(mj -> mj.getNapai_direction() == this.direction).collect(Collectors.toList());
        if (this.hu == 1 && this.hu_mj != null) {
            this.hu_mj.setLast(false);
            yina_bottom.add(this.hu_mj);
        }
        List<Mj> yina_bottom_pang = yina_bottom.stream().filter(mj -> mj.getPaizhuantai() == 1 || mj.getPaizhuantai() == 2 || mj.getPaizhuantai() == 3 || mj.getPaizhuantai() == 4).collect(Collectors.toList());
        Collections.sort(yina_bottom_pang, new MainController.ComparatorMjResult());
        yina_bottom.removeAll(yina_bottom_pang);
        if (Config.systemConfig.queyimeng == 1) {
            if (this.que > -1) {
                yina_bottom.forEach(mj -> {
                    if (mj.getResult() / 10 == this.que) {
                        mj.setIsque(true);
                    }
                });
            } else {
                for (int i2 = 0; i2 < 3; ++i2) {
                    int finalI = i2;
                    if (Config.yina.stream().filter(mj -> mj.getNapai_direction() == this.direction && mj.getResult() / 10 == finalI).count() != 0L) continue;
                    this.que = i2;
                }
            }
            Collections.sort(yina_bottom, new MainController.ComparatorMjQueyimengResult());
        } else {
            Collections.sort(yina_bottom, new MainController.ComparatorMjResult());
        }
        this.height = this.yinaPanel.getPrefHeight() + 10.0 + 5.0;
        MjHBoxLeftControl mjHBoxLeftControl = null;
        ImageView imageView = null;
        List<Mj> danzhuangangs = yina_bottom_pang.stream().filter(mj -> mj.getResult() > 40 || Config.systemConfig.danzhangkegang_pai.contains(mj.getResult())).collect(Collectors.toList());
        int danzhanggang = 0;
        for (int j = 0; j < danzhuangangs.size(); ++j) {
            Mj mj3 = (Mj)danzhuangangs.get(j);
            mjHBoxLeftControl = new MjHBoxLeftControl();
            imageView = mjHBoxLeftControl.getImageView();
            if (mj3.caisheng) {
                imageView.setImage(ImageKit.rotate90SX(ImageKit.coverImage(Config.getImageByMj(mj3), Config.LANSE)));
            } else {
                imageView.setImage(ImageKit.rotate90SX(Config.getImageByMj(mj3)));
            }
            imageView.setFitHeight(Config.MJ_WIDTH / 3.0);
            imageView.setFitWidth(Config.MJ_HEIGHT / 3.0);
            if (danzhanggang % 3 == 0) {
                this.offect_y += imageView.getFitHeight();
            }
            mjHBoxLeftControl.setLayoutX(Config.MJ_HEIGHT - (double)(danzhanggang % 3) * imageView.getFitWidth() - imageView.getFitWidth());
            mjHBoxLeftControl.setLayoutY(this.height - this.offect_y);
            mj3.setYina_imageView(mjHBoxLeftControl);
            this.yinaPanel.getChildren().add(mjHBoxLeftControl);
            ++danzhanggang;
        }
        yina_bottom.removeAll(yina_bottom_pang);
        yina_bottom_pang.removeAll(danzhuangangs);
        int size = yina_bottom_pang.size();
        for (int j = 0; j < size; ++j) {
            mj2 = (Mj)yina_bottom_pang.get(j);
            mjHBoxLeftControl = new MjHBoxLeftControl();
            imageView = mjHBoxLeftControl.getImageView();
            if (mj2.caisheng) {
                imageView.setImage(ImageKit.rotate90SX(ImageKit.coverImage(Config.getImageByMj(mj2), Config.LANSE)));
            } else {
                imageView.setImage(ImageKit.rotate90SX(Config.getImageByMj(mj2)));
            }
            imageView.setFitHeight(Config.MJ_WIDTH_1_2);
            imageView.setFitWidth(Config.MJ_HEIGHT_1_2);
            mj2.setYina_imageView(mjHBoxLeftControl);
            this.offect_y += imageView.getFitHeight();
            mjHBoxLeftControl.setLayoutX(imageView.getFitWidth());
            mjHBoxLeftControl.setLayoutY(this.height - this.offect_y);
            this.yinaPanel.getChildren().add(mjHBoxLeftControl);
        }
        if ((this.hu == 0 || Config.systemConfig.hupaikemo == 1) && MainController.dapaifangx == this.direction) {
            if (this.dapaifangxiang_imageview == null) {
                Image dapai_image = new Image(MainController.class.getResourceAsStream("/quesheng/view/image/dapaifangxiang.png"));
                this.dapaifangxiang_imageview = new ImageView(dapai_image);
                this.dapaifangxiang_imageview.setFitHeight(Config.MJ_HEIGHT_1_2);
                this.dapaifangxiang_imageview.setFitWidth(Config.MJ_HEIGHT_1_2);
            }
            this.offect_y += this.dapaifangxiang_imageview.getFitHeight();
            this.dapaifangxiang_imageview.setLayoutY(this.height - this.offect_y);
            this.yinaPanel.getChildren().add(this.dapaifangxiang_imageview);
        } else {
            this.offect_y += Config.MJ_HEIGHT_1_2;
        }
        size = yina_bottom.size();
        for (i = 0; i < size; ++i) {
            mj2 = (Mj)yina_bottom.get(i);
            if (!mj2.isLast()) continue;
            yina_bottom.remove(mj2);
            yina_bottom.add(mj2);
            break;
        }
        size = yina_bottom.size();
        for (i = 0; i < size; ++i) {
            mj2 = (Mj)yina_bottom.get(i);
            mjHBoxLeftControl = new MjHBoxLeftControl();
            imageView = mjHBoxLeftControl.getImageView();
            imageView.setUserData(mj2);
            if (mj2.isHu()) {
                imageView.setImage(ImageKit.rotate90SX(ImageKit.drawBorder(Config.getImageByMj(mj2), Color.RED)));
            } else if (mj2.caisheng) {
                imageView.setImage(ImageKit.rotate90SX(ImageKit.coverImage(Config.getImageByMj(mj2), Config.LANSE)));
            } else {
                imageView.setImage(ImageKit.rotate90SX(Config.getImageByMj(mj2)));
            }
            imageView.setCursor(Cursor.HAND);
            imageView.setFitHeight(Config.MJ_WIDTH);
            imageView.setFitWidth(Config.MJ_HEIGHT);
            if ((this.hu == 0 || Config.systemConfig.hupaikemo == 1) && MainController.dapaifangx == this.direction) {
                MjHBoxLeftControl finalMjHBoxLeftControl = mjHBoxLeftControl;
                imageView.setOnMouseMoved(event -> finalMjHBoxLeftControl.setTranslateX(-10.0));
                MjHBoxLeftControl finalMjHBoxLeftControl1 = mjHBoxLeftControl;
                imageView.setOnMouseExited(event -> finalMjHBoxLeftControl1.setTranslateX(0.0));
            }
            imageView.setOnMouseClicked(this.mjClick);
            if (mj2.isLast()) {
                mjHBoxLeftControl.setTranslateY(-15.0);
                mjHBoxLeftControl.setLayoutX(-10.0);
                mjHBoxLeftControl.addClass("mj_last");
            }
            if (this.hu == 0 || Config.systemConfig.hupaikemo == 1) {
                Image image;
                for (MjPanelInterface direction1 : MainController.directions) {
                    int x;
                    if (direction1.getHu() != 0 || direction1.isQueren() || direction1.getDirection() == this.direction || direction1.getQue() == mj2.getResult() / 10) continue;
                    if (this.keGang(mj2, direction1.getDirection())) {
                        image = Config.GANGFANGXIANG;
                        x = direction1.getDirection() - this.direction;
                        imageView1 = new ImageView();
                        if (Math.abs(-90 * x) == 180) {
                            image = ImageKit.rotate(image, -90);
                            imageView1.setImage(image);
                            imageView1.setFitHeight(10.0);
                            imageView1.setFitWidth(imageView.getFitHeight() / 2.0);
                        } else {
                            image = ImageKit.rotate(image, 90 + -90 * x);
                            imageView1.setImage(image);
                            imageView1.setFitWidth(10.0);
                            imageView1.setFitHeight(imageView.getFitHeight());
                        }
                        mjHBoxLeftControl.addBottom(imageView1);
                        continue;
                    }
                    if (!this.kePeng(mj2, direction1.getDirection())) continue;
                    image = Config.PENGFANGXIANG;
                    x = direction1.getDirection() - this.direction;
                    imageView1 = new ImageView();
                    if (Math.abs(-90 * x) == 180) {
                        image = ImageKit.rotate(image, -90);
                        imageView1.setImage(image);
                        imageView1.setFitHeight(10.0);
                        imageView1.setFitWidth(imageView.getFitHeight() / 2.0);
                    } else {
                        image = ImageKit.rotate(image, 90 + -90 * x);
                        imageView1.setImage(image);
                        imageView1.setFitWidth(10.0);
                        imageView1.setFitHeight(imageView.getFitHeight());
                    }
                    mjHBoxLeftControl.addBottom(imageView1);
                }
                if (Config.systemConfig.chipai == 1) {
                    int d = this.direction;
                    do {
                        if (Config.systemConfig.niezhuanimo == 1) {
                            if (--d >= 0) continue;
                            d = 3;
                            continue;
                        }
                        if (++d <= 3) continue;
                        d = 0;
                    } while (MainController.directions.get(d).isQueren());
                    if (MainController.directions.get(d).getHu() == 0 && MainController.directions.get(d).getQue() != mj2.getResult() / 10 && this.keChi(mj2, d)) {
                        int x = d - this.direction;
                        image = Config.CHIFANGXIANG;
                        ImageView imageView12 = new ImageView();
                        if (Math.abs(-90 * x) == 180) {
                            image = ImageKit.rotate(image, -90);
                            imageView12.setImage(image);
                            imageView12.setFitHeight(10.0);
                            imageView12.setFitWidth(imageView.getFitHeight() / 2.0);
                        } else {
                            image = ImageKit.rotate(image, 90 + -90 * x);
                            imageView12.setImage(image);
                            imageView12.setFitWidth(10.0);
                            imageView12.setFitHeight(imageView.getFitHeight());
                        }
                        mjHBoxLeftControl.addBottom(imageView12);
                    }
                }
                if (Config.systemConfig.dongnanxibeikechikegang == 1 && mj2.getResult() >= 31 && mj2.getResult() <= 37) {
                    int d = this.direction;
                    do {
                        if (Config.systemConfig.niezhuanimo == 1) {
                            if (--d >= 0) continue;
                            d = 3;
                            continue;
                        }
                        if (++d <= 3) continue;
                        d = 0;
                    } while (MainController.directions.get(d).isQueren());
                    if (MainController.directions.get(d).getHu() == 0 && MainController.directions.get(d).getQue() != mj2.getResult() / 10 && this.keChi(mj2, d)) {
                        int x = d - this.direction;
                        image = Config.CHIFANGXIANG;
                        ImageView imageView13 = new ImageView();
                        if (Math.abs(-90 * x) == 180) {
                            image = ImageKit.rotate(image, -90);
                            imageView13.setImage(image);
                            imageView13.setFitHeight(10.0);
                            imageView13.setFitWidth(imageView.getFitHeight() / 2.0);
                        } else {
                            image = ImageKit.rotate(image, 90 + -90 * x);
                            imageView13.setImage(image);
                            imageView13.setFitWidth(10.0);
                            imageView13.setFitHeight(imageView.getFitHeight());
                        }
                        mjHBoxLeftControl.addBottom(imageView13);
                    }
                }
            }
            mj2.setYina_imageView(mjHBoxLeftControl);
            this.offect_y += imageView.getFitHeight();
            mjHBoxLeftControl.setLayoutY(this.height - this.offect_y);
            this.yinaPanel.getChildren().add(mjHBoxLeftControl);
        }
        if (this.hu == 1) {
            if (Config.systemConfig.xueliuchenghe == 0) {
                this.yina_hupai.clear();
                this.hupaiPanel.getChildren().clear();
            }
            Text yihu = new Text("\u80e1");
            yihu.setFont(Font.font("\u5b8b\u4f53", FontWeight.BOLD, 25.0));
            yihu.setFill(Color.RED);
            this.caozuoPanel.getChildren().add(yihu);
            if (Config.systemConfig.hupaikemo == 1) {
                List<Mj> yuce = Config.yuce.stream().filter(mj -> mj.getYuce_direction() == this.direction).collect(Collectors.toList());
                this.hupaiYuCe(yina_bottom, yina_bottom_pang, imageView, size, yuce);
            }
        } else {
            if (MainController.dapaifangx == this.direction) {
                Iterator<Mj> iterator3;
                Mj item;
                Iterator iterator32;
                if (Config.systemConfig.dongnanxibeizhongfabaigangqingdao == 1 && Config.yichu.stream().filter(mj -> mj.getChupai_direction() == this.direction).count() == 0L) {
                    Map<Integer, Integer> dongnaxibei_map = new HashMap(yina_bottom.size());
                    yina_bottom.forEach(mj -> {
                        if (mj.getResult() >= 31 && mj.getResult() <= 34) {
                            if (dongnaxibei_map.containsKey(mj.getResult())) {
                                dongnaxibei_map.put(mj.getResult(), (Integer)dongnaxibei_map.get(mj.getResult()) + 1);
                            } else {
                                dongnaxibei_map.put(mj.getResult(), 1);
                            }
                        }
                    });
                    if (dongnaxibei_map.size() == 4) {
                        List<Mj> gangpai = yina_bottom.stream().filter(mj1 -> mj1.getPaizhuantai() == 0 && dongnaxibei_map.getOrDefault(mj1.getResult(), 0) > 0).collect(Collectors.toList());
                        gangpai.sort(new MainController.ComparatorMjResult());
                        iterator32 = gangpai.iterator();
                        int before_result = 0;
                        while (iterator32.hasNext()) {
                            item = (Mj)iterator32.next();
                            if (item.getResult() == before_result) {
                                iterator32.remove();
                            }
                            before_result = item.getResult();
                        }
                        gangpai.forEach(mj -> {
                            ImageView an_gang_text = new ImageView(Config.GANG);
                            an_gang_text.getStyleClass().add("caozuo_tishi");
                            an_gang_text.setFitHeight(Config.MJ_WIDTH);
                            an_gang_text.setFitWidth(Config.MJ_WIDTH);
                            an_gang_text.setUserData(this.direction);
                            an_gang_text.setVisible(true);
                            mj.getYina_imageView().addClass("border_deepskyblue");
                            mj.getYina_imageView().addBottom(an_gang_text);
                            an_gang_text.setOnMouseClicked(event -> {
                                MainController.saveState();
                                gangpai.forEach(mj1 -> mj1.setPaizhuantai(3));
                                Config.yuce.clear();
                                MainController.dapaifangx = this.direction;
                                switch (Config.systemConfig.gangpaishunxu) {
                                    case "\u987a\u6760\u724c": {
                                        MainController.mopai();
                                        break;
                                    }
                                    case "\u540e\u6760\u724c": {
                                        MainController.houGangMopai();
                                        break;
                                    }
                                    case "\u968f\u610f\u6760": {
                                        MainController.SuiYiGang();
                                    }
                                }
                            });
                        });
                    } else if ((long)dongnaxibei_map.size() + yina_bottom.stream().filter(mj -> mj.caisheng && dongnaxibei_map.getOrDefault(mj.getResult(), 0) == 0).count() >= 4L) {
                        List<Mj> gangpai = yina_bottom.stream().filter(mj1 -> mj1.getPaizhuantai() == 0 && dongnaxibei_map.getOrDefault(mj1.getResult(), 0) > 0).collect(Collectors.toList());
                        gangpai.sort(new MainController.ComparatorMjResult());
                        iterator32 = gangpai.iterator();
                        int before_result = 0;
                        while (iterator32.hasNext()) {
                            item = (Mj)iterator32.next();
                            if (item.getResult() == before_result) {
                                iterator32.remove();
                            }
                            before_result = item.getResult();
                        }
                        gangpai.forEach(mj -> {
                            ImageView an_gang_text = new ImageView(Config.GANG);
                            an_gang_text.getStyleClass().add("caozuo_tishi");
                            an_gang_text.setFitHeight(Config.MJ_WIDTH);
                            an_gang_text.setFitWidth(Config.MJ_WIDTH);
                            an_gang_text.setUserData(this.direction);
                            an_gang_text.setVisible(true);
                            mj.getYina_imageView().addClass("border_deepskyblue");
                            mj.getYina_imageView().addBottom(an_gang_text);
                            an_gang_text.setOnMouseClicked(event -> {
                                MainController.saveState();
                                List<Mj> caishengs = yina_bottom.stream().filter(mj1 -> mj1.getPaizhuantai() == 0 && mj1.caisheng).collect(Collectors.toList());
                                for (int ii = 0; ii < 4 - gangpai.size(); ++ii) {
                                    ((Mj)caishengs.get(ii)).setPaizhuantai(3);
                                }
                                gangpai.forEach(mj1 -> mj1.setPaizhuantai(3));
                                Config.yuce.clear();
                                MainController.dapaifangx = this.direction;
                                switch (Config.systemConfig.gangpaishunxu) {
                                    case "\u987a\u6760\u724c": {
                                        MainController.mopai();
                                        break;
                                    }
                                    case "\u540e\u6760\u724c": {
                                        MainController.houGangMopai();
                                        break;
                                    }
                                    case "\u968f\u610f\u6760": {
                                        MainController.SuiYiGang();
                                    }
                                }
                            });
                        });
                    }
                }
                if (Config.systemConfig.dongnanxibeizhongfabaigangqingdao == 1 && Config.yichu.stream().filter(mj -> mj.getChupai_direction() == this.direction).count() == 0L) {
                    Map<Integer, Integer> zhongfabai_map = new HashMap(yina_bottom.size());
                    yina_bottom.forEach(mj -> {
                        if (mj.getResult() >= 35 && mj.getResult() <= 37) {
                            if (zhongfabai_map.containsKey(mj.getResult())) {
                                zhongfabai_map.put(mj.getResult(), (Integer)zhongfabai_map.get(mj.getResult()) + 1);
                            } else {
                                zhongfabai_map.put(mj.getResult(), 1);
                            }
                        }
                    });
                    if (zhongfabai_map.size() == 3) {
                        List<Mj> gangpai = yina_bottom.stream().filter(mj1 -> mj1.getPaizhuantai() == 0 && zhongfabai_map.getOrDefault(mj1.getResult(), 0) > 0).collect(Collectors.toList());
                        gangpai.sort(new MainController.ComparatorMjResult());
                        iterator32 = gangpai.iterator();
                        int before_result = 0;
                        while (iterator32.hasNext()) {
                            item = (Mj)iterator32.next();
                            if (item.getResult() == before_result) {
                                iterator32.remove();
                            }
                            before_result = item.getResult();
                        }
                        gangpai.forEach(mj -> {
                            ImageView an_gang_text = new ImageView(Config.GANG);
                            an_gang_text.getStyleClass().add("caozuo_tishi");
                            an_gang_text.setFitHeight(Config.MJ_WIDTH);
                            an_gang_text.setFitWidth(Config.MJ_WIDTH);
                            an_gang_text.setUserData(this.direction);
                            an_gang_text.setVisible(true);
                            mj.getYina_imageView().addClass("border_deepskyblue");
                            mj.getYina_imageView().addBottom(an_gang_text);
                            an_gang_text.setOnMouseClicked(event -> {
                                MainController.saveState();
                                gangpai.forEach(mj1 -> mj1.setPaizhuantai(4));
                                Config.yuce.clear();
                                MainController.dapaifangx = this.direction;
                                this.clear();
                                MainController.displayYinaPai();
                            });
                        });
                    } else if ((long)zhongfabai_map.size() + yina_bottom.stream().filter(mj -> mj.caisheng && zhongfabai_map.getOrDefault(mj.getResult(), 0) == 0).count() >= 3L) {
                        List<Mj> gangpai = yina_bottom.stream().filter(mj1 -> mj1.getPaizhuantai() == 0 && zhongfabai_map.getOrDefault(mj1.getResult(), 0) > 0).collect(Collectors.toList());
                        gangpai.sort(new MainController.ComparatorMjResult());
                        iterator32 = gangpai.iterator();
                        int before_result = 0;
                        while (iterator32.hasNext()) {
                            item = (Mj)iterator32.next();
                            if (item.getResult() == before_result) {
                                iterator32.remove();
                            }
                            before_result = item.getResult();
                        }
                        gangpai.forEach(mj -> {
                            ImageView an_gang_text = new ImageView(Config.GANG);
                            an_gang_text.getStyleClass().add("caozuo_tishi");
                            an_gang_text.setFitHeight(Config.MJ_WIDTH);
                            an_gang_text.setFitWidth(Config.MJ_WIDTH);
                            an_gang_text.setUserData(this.direction);
                            an_gang_text.setVisible(true);
                            mj.getYina_imageView().addClass("border_deepskyblue");
                            mj.getYina_imageView().addBottom(an_gang_text);
                            an_gang_text.setOnMouseClicked(event -> {
                                MainController.saveState();
                                List<Mj> caishengs = yina_bottom.stream().filter(mj1 -> mj1.getPaizhuantai() == 0 && mj1.caisheng).collect(Collectors.toList());
                                for (int ii = 0; ii < 3 - gangpai.size(); ++ii) {
                                    ((Mj)caishengs.get(ii)).setPaizhuantai(4);
                                }
                                gangpai.forEach(mj1 -> mj1.setPaizhuantai(4));
                                Config.yuce.clear();
                                MainController.dapaifangx = this.direction;
                                this.clear();
                                MainController.displayYinaPai();
                            });
                        });
                    }
                }
                if (Config.systemConfig.dongnanxibeizhongfabaigangqingdao == 1 && Config.yichu.stream().filter(mj -> mj.getChupai_direction() == this.direction).count() == 0L) {
                    Map<Integer, Integer> dongnanji_map = new HashMap(yina_bottom.size());
                    yina_bottom.forEach(mj -> {
                        if (mj.getResult() >= 31 && mj.getResult() <= 32 || mj.getResult() == 11) {
                            if (dongnanji_map.containsKey(mj.getResult())) {
                                dongnanji_map.put(mj.getResult(), (Integer)dongnanji_map.get(mj.getResult()) + 1);
                            } else {
                                dongnanji_map.put(mj.getResult(), 1);
                            }
                        }
                    });
                    if (dongnanji_map.size() == 3) {
                        List<Mj> gangpai = yina_bottom.stream().filter(mj1 -> mj1.getPaizhuantai() == 0 && dongnanji_map.getOrDefault(mj1.getResult(), 0) > 0).collect(Collectors.toList());
                        gangpai.sort(new MainController.ComparatorMjResult());
                        iterator32 = gangpai.iterator();
                        int before_result = 0;
                        while (iterator32.hasNext()) {
                            item = (Mj)iterator32.next();
                            if (item.getResult() == before_result) {
                                iterator32.remove();
                            }
                            before_result = item.getResult();
                        }
                        gangpai.forEach(mj -> {
                            ImageView an_gang_text = new ImageView(Config.GANG);
                            an_gang_text.getStyleClass().add("caozuo_tishi");
                            an_gang_text.setFitHeight(Config.MJ_WIDTH);
                            an_gang_text.setFitWidth(Config.MJ_WIDTH);
                            an_gang_text.setUserData(this.direction);
                            an_gang_text.setVisible(true);
                            mj.getYina_imageView().addClass("border_deepskyblue");
                            mj.getYina_imageView().addBottom(an_gang_text);
                            an_gang_text.setOnMouseClicked(event -> {
                                MainController.saveState();
                                gangpai.forEach(mj1 -> mj1.setPaizhuantai(4));
                                Config.yuce.clear();
                                MainController.dapaifangx = this.direction;
                                this.clear();
                                MainController.displayYinaPai();
                            });
                        });
                    }
                }
                if (Config.systemConfig.dongnanxibeizhongfabaigangqingdao == 1) {
                    Map<Integer, Integer> dongnanxibeigang_map = new HashMap(yina_bottom_pang.size());
                    yina_bottom_pang.forEach(mj -> {
                        if (mj.getResult() >= 31 && mj.getResult() <= 34 && mj.getPaizhuantai() == 3) {
                            if (dongnanxibeigang_map.containsKey(mj.getResult())) {
                                dongnanxibeigang_map.put(mj.getResult(), (Integer)dongnanxibeigang_map.get(mj.getResult()) + 1);
                            } else {
                                dongnanxibeigang_map.put(mj.getResult(), 1);
                            }
                        }
                    });
                    if (dongnanxibeigang_map.size() > 0 && (dongnanxibeigang_map.getOrDefault(31, 4) < 4 || dongnanxibeigang_map.getOrDefault(32, 4) < 4 || dongnanxibeigang_map.getOrDefault(33, 4) < 4 || dongnanxibeigang_map.getOrDefault(34, 4) < 4)) {
                        List<Mj> gangpai = yina_bottom.stream().filter(mj1 -> mj1.getPaizhuantai() == 0 && (mj1.getResult() == 31 || mj1.getResult() == 32 || mj1.getResult() == 33 || mj1.getResult() == 34)).collect(Collectors.toList());
                        gangpai.forEach(mj -> {
                            ImageView an_gang_text = new ImageView(Config.GANG);
                            an_gang_text.getStyleClass().add("caozuo_tishi");
                            an_gang_text.setFitHeight(Config.MJ_WIDTH);
                            an_gang_text.setFitWidth(Config.MJ_WIDTH);
                            an_gang_text.setUserData(this.direction);
                            an_gang_text.setVisible(true);
                            mj.getYina_imageView().addClass("border_deepskyblue");
                            mj.getYina_imageView().addBottom(an_gang_text);
                            an_gang_text.setOnMouseClicked(event -> {
                                MainController.saveState();
                                mj.setPaizhuantai(3);
                                Config.yuce.clear();
                                MainController.dapaifangx = this.direction;
                                switch (Config.systemConfig.gangpaishunxu) {
                                    case "\u987a\u6760\u724c": {
                                        MainController.mopai();
                                        break;
                                    }
                                    case "\u540e\u6760\u724c": {
                                        MainController.houGangMopai();
                                        break;
                                    }
                                    case "\u968f\u610f\u6760": {
                                        MainController.SuiYiGang();
                                    }
                                }
                            });
                        });
                    }
                }
                if (Config.systemConfig.dongnanxibeizhongfabaigangqingdao == 1) {
                    Map<Integer, Integer> zhongfabaigang_map = new HashMap(yina_bottom_pang.size());
                    yina_bottom_pang.forEach(mj -> {
                        if (mj.getResult() >= 35 && mj.getResult() <= 37 && mj.getPaizhuantai() == 4) {
                            if (zhongfabaigang_map.containsKey(mj.getResult())) {
                                zhongfabaigang_map.put(mj.getResult(), (Integer)zhongfabaigang_map.get(mj.getResult()) + 1);
                            } else {
                                zhongfabaigang_map.put(mj.getResult(), 1);
                            }
                        }
                    });
                    if (zhongfabaigang_map.size() > 0) {
                        List<Mj> gangpai = yina_bottom.stream().filter(mj1 -> mj1.getPaizhuantai() == 0 && (mj1.getResult() == 35 || mj1.getResult() == 36 || mj1.getResult() == 37)).collect(Collectors.toList());
                        gangpai.forEach(mj -> {
                            ImageView an_gang_text = new ImageView(Config.GANG);
                            an_gang_text.getStyleClass().add("caozuo_tishi");
                            an_gang_text.setFitHeight(Config.MJ_WIDTH);
                            an_gang_text.setFitWidth(Config.MJ_WIDTH);
                            an_gang_text.setUserData(this.direction);
                            an_gang_text.setVisible(true);
                            mj.getYina_imageView().addClass("border_deepskyblue");
                            mj.getYina_imageView().addBottom(an_gang_text);
                            an_gang_text.setOnMouseClicked(event -> {
                                MainController.saveState();
                                mj.setPaizhuantai(4);
                                Config.yuce.clear();
                                MainController.dapaifangx = this.direction;
                                switch (Config.systemConfig.gangpaishunxu) {
                                    case "\u987a\u6760\u724c": {
                                        MainController.mopai();
                                        break;
                                    }
                                    case "\u540e\u6760\u724c": {
                                        MainController.houGangMopai();
                                        break;
                                    }
                                    case "\u968f\u610f\u6760": {
                                        MainController.SuiYiGang();
                                    }
                                }
                            });
                        });
                    }
                }
                if (Config.systemConfig.dongnanxibeizhongfabaigangqingdao == 1) {
                    Map<Integer, Integer> dongnanjigang_map = new HashMap(yina_bottom_pang.size());
                    yina_bottom_pang.forEach(mj -> {
                        if ((mj.getResult() >= 31 && mj.getResult() <= 32 || mj.getResult() == 11) && mj.getPaizhuantai() == 4) {
                            if (dongnanjigang_map.containsKey(mj.getResult())) {
                                dongnanjigang_map.put(mj.getResult(), (Integer)dongnanjigang_map.get(mj.getResult()) + 1);
                            } else {
                                dongnanjigang_map.put(mj.getResult(), 1);
                            }
                        }
                    });
                    if (dongnanjigang_map.size() == 3) {
                        List<Mj> gangpai = yina_bottom.stream().filter(mj1 -> mj1.getPaizhuantai() == 0 && (mj1.getResult() == 31 || mj1.getResult() == 32 || mj1.getResult() == 11)).collect(Collectors.toList());
                        gangpai.forEach(mj -> {
                            ImageView an_gang_text = new ImageView(Config.GANG);
                            an_gang_text.getStyleClass().add("caozuo_tishi");
                            an_gang_text.setFitHeight(Config.MJ_WIDTH);
                            an_gang_text.setFitWidth(Config.MJ_WIDTH);
                            an_gang_text.setUserData(this.direction);
                            an_gang_text.setVisible(true);
                            mj.getYina_imageView().addClass("border_deepskyblue");
                            mj.getYina_imageView().addBottom(an_gang_text);
                            an_gang_text.setOnMouseClicked(event -> {
                                MainController.saveState();
                                mj.setPaizhuantai(4);
                                Config.yuce.clear();
                                MainController.dapaifangx = this.direction;
                                switch (Config.systemConfig.gangpaishunxu) {
                                    case "\u987a\u6760\u724c": {
                                        MainController.mopai();
                                        break;
                                    }
                                    case "\u540e\u6760\u724c": {
                                        MainController.houGangMopai();
                                        break;
                                    }
                                    case "\u968f\u610f\u6760": {
                                        MainController.SuiYiGang();
                                    }
                                }
                            });
                        });
                    }
                }
                if (Config.systemConfig.yaojiugang == 1 && Config.yichu.stream().filter(mj -> mj.getChupai_direction() == this.direction).count() == 0L) {
                    Map<Integer, Integer> dongnaxibei_map = new HashMap(yina_bottom.size());
                    yina_bottom.forEach(mj -> {
                        if (mj.getResult() >= 31 && mj.getResult() <= 34) {
                            if (dongnaxibei_map.containsKey(mj.getResult())) {
                                dongnaxibei_map.put(mj.getResult(), (Integer)dongnaxibei_map.get(mj.getResult()) + 1);
                            } else {
                                dongnaxibei_map.put(mj.getResult(), 1);
                            }
                        }
                    });
                    if (dongnaxibei_map.size() == 4) {
                        List<Mj> gangpai = yina_bottom.stream().filter(mj1 -> mj1.getPaizhuantai() == 0 && dongnaxibei_map.getOrDefault(mj1.getResult(), 0) > 0).collect(Collectors.toList());
                        gangpai.sort(new MainController.ComparatorMjResult());
                        iterator32 = gangpai.iterator();
                        int before_result = 0;
                        while (iterator32.hasNext()) {
                            item = (Mj)iterator32.next();
                            if (item.getResult() == before_result) {
                                iterator32.remove();
                            }
                            before_result = item.getResult();
                        }
                        gangpai.forEach(mj -> {
                            ImageView an_gang_text = new ImageView(Config.GANG);
                            an_gang_text.getStyleClass().add("caozuo_tishi");
                            an_gang_text.setFitHeight(Config.MJ_WIDTH);
                            an_gang_text.setFitWidth(Config.MJ_WIDTH);
                            an_gang_text.setUserData(this.direction);
                            an_gang_text.setVisible(true);
                            mj.getYina_imageView().addClass("border_deepskyblue");
                            mj.getYina_imageView().addBottom(an_gang_text);
                            an_gang_text.setOnMouseClicked(event -> {
                                MainController.saveState();
                                gangpai.forEach(mj1 -> mj1.setPaizhuantai(3));
                                Config.yuce.clear();
                                MainController.dapaifangx = this.direction;
                                switch (Config.systemConfig.gangpaishunxu) {
                                    case "\u987a\u6760\u724c": {
                                        MainController.mopai();
                                        break;
                                    }
                                    case "\u540e\u6760\u724c": {
                                        MainController.houGangMopai();
                                        break;
                                    }
                                    case "\u968f\u610f\u6760": {
                                        MainController.SuiYiGang();
                                    }
                                }
                            });
                        });
                    }
                }
                if (Config.systemConfig.yaojiugang == 1 && Config.yichu.stream().filter(mj -> mj.getChupai_direction() == this.direction).count() == 0L) {
                    Map<Integer, Integer> zhongfabai_map = new HashMap(yina_bottom.size());
                    yina_bottom.forEach(mj -> {
                        if (mj.getResult() >= 35 && mj.getResult() <= 37) {
                            if (zhongfabai_map.containsKey(mj.getResult())) {
                                zhongfabai_map.put(mj.getResult(), (Integer)zhongfabai_map.get(mj.getResult()) + 1);
                            } else {
                                zhongfabai_map.put(mj.getResult(), 1);
                            }
                        }
                    });
                    if (zhongfabai_map.size() == 3) {
                        List<Mj> gangpai = yina_bottom.stream().filter(mj1 -> mj1.getPaizhuantai() == 0 && zhongfabai_map.getOrDefault(mj1.getResult(), 0) > 0).collect(Collectors.toList());
                        gangpai.sort(new MainController.ComparatorMjResult());
                        iterator32 = gangpai.iterator();
                        int before_result = 0;
                        while (iterator32.hasNext()) {
                            item = (Mj)iterator32.next();
                            if (item.getResult() == before_result) {
                                iterator32.remove();
                            }
                            before_result = item.getResult();
                        }
                        gangpai.forEach(mj -> {
                            ImageView an_gang_text = new ImageView(Config.GANG);
                            an_gang_text.getStyleClass().add("caozuo_tishi");
                            an_gang_text.setFitHeight(Config.MJ_WIDTH);
                            an_gang_text.setFitWidth(Config.MJ_WIDTH);
                            an_gang_text.setUserData(this.direction);
                            an_gang_text.setVisible(true);
                            mj.getYina_imageView().addClass("border_deepskyblue");
                            mj.getYina_imageView().addBottom(an_gang_text);
                            an_gang_text.setOnMouseClicked(event -> {
                                MainController.saveState();
                                gangpai.forEach(mj1 -> mj1.setPaizhuantai(4));
                                Config.yuce.clear();
                                MainController.dapaifangx = this.direction;
                                this.clear();
                                MainController.displayYinaPai();
                            });
                        });
                    }
                }
                if (Config.systemConfig.yaojiugang == 1 && Config.yichu.stream().filter(mj -> mj.getChupai_direction() == this.direction).count() == 0L) {
                    Map<Integer, Integer> dongnanji_map = new HashMap(yina_bottom.size());
                    yina_bottom.forEach(mj -> {
                        if (mj.getResult() == 1 || mj.getResult() == 11 || mj.getResult() == 21) {
                            if (dongnanji_map.containsKey(mj.getResult())) {
                                dongnanji_map.put(mj.getResult(), (Integer)dongnanji_map.get(mj.getResult()) + 1);
                            } else {
                                dongnanji_map.put(mj.getResult(), 1);
                            }
                        }
                    });
                    if (dongnanji_map.size() == 3) {
                        List<Mj> gangpai = yina_bottom.stream().filter(mj1 -> mj1.getPaizhuantai() == 0 && dongnanji_map.getOrDefault(mj1.getResult(), 0) > 0).collect(Collectors.toList());
                        gangpai.sort(new MainController.ComparatorMjResult());
                        iterator32 = gangpai.iterator();
                        int before_result = 0;
                        while (iterator32.hasNext()) {
                            item = (Mj)iterator32.next();
                            if (item.getResult() == before_result) {
                                iterator32.remove();
                            }
                            before_result = item.getResult();
                        }
                        gangpai.forEach(mj -> {
                            ImageView an_gang_text = new ImageView(Config.GANG);
                            an_gang_text.getStyleClass().add("caozuo_tishi");
                            an_gang_text.setFitHeight(Config.MJ_WIDTH);
                            an_gang_text.setFitWidth(Config.MJ_WIDTH);
                            an_gang_text.setUserData(this.direction);
                            an_gang_text.setVisible(true);
                            mj.getYina_imageView().addClass("border_deepskyblue");
                            mj.getYina_imageView().addBottom(an_gang_text);
                            an_gang_text.setOnMouseClicked(event -> {
                                MainController.saveState();
                                gangpai.forEach(mj1 -> mj1.setPaizhuantai(4));
                                Config.yuce.clear();
                                MainController.dapaifangx = this.direction;
                                this.clear();
                                MainController.displayYinaPai();
                            });
                        });
                    }
                }
                if (Config.systemConfig.yaojiugang == 1 && Config.yichu.stream().filter(mj -> mj.getChupai_direction() == this.direction).count() == 0L) {
                    Map<Integer, Integer> dongnanji_map = new HashMap(yina_bottom.size());
                    yina_bottom.forEach(mj -> {
                        if (mj.getResult() == 9 || mj.getResult() == 19 || mj.getResult() == 29) {
                            if (dongnanji_map.containsKey(mj.getResult())) {
                                dongnanji_map.put(mj.getResult(), (Integer)dongnanji_map.get(mj.getResult()) + 1);
                            } else {
                                dongnanji_map.put(mj.getResult(), 1);
                            }
                        }
                    });
                    if (dongnanji_map.size() == 3) {
                        List<Mj> gangpai = yina_bottom.stream().filter(mj1 -> mj1.getPaizhuantai() == 0 && dongnanji_map.getOrDefault(mj1.getResult(), 0) > 0).collect(Collectors.toList());
                        gangpai.sort(new MainController.ComparatorMjResult());
                        iterator32 = gangpai.iterator();
                        int before_result = 0;
                        while (iterator32.hasNext()) {
                            item = (Mj)iterator32.next();
                            if (item.getResult() == before_result) {
                                iterator32.remove();
                            }
                            before_result = item.getResult();
                        }
                        gangpai.forEach(mj -> {
                            ImageView an_gang_text = new ImageView(Config.GANG);
                            an_gang_text.getStyleClass().add("caozuo_tishi");
                            an_gang_text.setFitHeight(Config.MJ_WIDTH);
                            an_gang_text.setFitWidth(Config.MJ_WIDTH);
                            an_gang_text.setUserData(this.direction);
                            an_gang_text.setVisible(true);
                            mj.getYina_imageView().addClass("border_deepskyblue");
                            mj.getYina_imageView().addBottom(an_gang_text);
                            an_gang_text.setOnMouseClicked(event -> {
                                MainController.saveState();
                                gangpai.forEach(mj1 -> mj1.setPaizhuantai(4));
                                Config.yuce.clear();
                                MainController.dapaifangx = this.direction;
                                this.clear();
                                MainController.displayYinaPai();
                            });
                        });
                    }
                }
                if (Config.systemConfig.yaojiugang == 1) {
                    Map<Integer, Integer> dongnanxibeigang_map = new HashMap(yina_bottom_pang.size());
                    yina_bottom_pang.forEach(mj -> {
                        if (mj.getResult() >= 31 && mj.getResult() <= 34 && mj.getPaizhuantai() == 3) {
                            if (dongnanxibeigang_map.containsKey(mj.getResult())) {
                                dongnanxibeigang_map.put(mj.getResult(), (Integer)dongnanxibeigang_map.get(mj.getResult()) + 1);
                            } else {
                                dongnanxibeigang_map.put(mj.getResult(), 1);
                            }
                        }
                    });
                    if (dongnanxibeigang_map.size() > 0 && (dongnanxibeigang_map.getOrDefault(31, 4) < 4 || dongnanxibeigang_map.getOrDefault(32, 4) < 4 || dongnanxibeigang_map.getOrDefault(33, 4) < 4 || dongnanxibeigang_map.getOrDefault(34, 4) < 4)) {
                        List<Mj> gangpai = yina_bottom.stream().filter(mj1 -> mj1.getPaizhuantai() == 0 && (mj1.getResult() == 31 || mj1.getResult() == 32 || mj1.getResult() == 33 || mj1.getResult() == 34)).collect(Collectors.toList());
                        gangpai.forEach(mj -> {
                            ImageView an_gang_text = new ImageView(Config.GANG);
                            an_gang_text.getStyleClass().add("caozuo_tishi");
                            an_gang_text.setFitHeight(Config.MJ_WIDTH);
                            an_gang_text.setFitWidth(Config.MJ_WIDTH);
                            an_gang_text.setUserData(this.direction);
                            an_gang_text.setVisible(true);
                            mj.getYina_imageView().addClass("border_deepskyblue");
                            mj.getYina_imageView().addBottom(an_gang_text);
                            an_gang_text.setOnMouseClicked(event -> {
                                MainController.saveState();
                                mj.setPaizhuantai(3);
                                Config.yuce.clear();
                                MainController.dapaifangx = this.direction;
                                switch (Config.systemConfig.gangpaishunxu) {
                                    case "\u987a\u6760\u724c": {
                                        MainController.mopai();
                                        break;
                                    }
                                    case "\u540e\u6760\u724c": {
                                        MainController.houGangMopai();
                                        break;
                                    }
                                    case "\u968f\u610f\u6760": {
                                        MainController.SuiYiGang();
                                    }
                                }
                            });
                        });
                    }
                }
                if (Config.systemConfig.yaojiugang == 1) {
                    Map<Integer, Integer> zhongfabaigang_map = new HashMap(yina_bottom_pang.size());
                    yina_bottom_pang.forEach(mj -> {
                        if (mj.getResult() >= 35 && mj.getResult() <= 37 && mj.getPaizhuantai() == 4) {
                            if (zhongfabaigang_map.containsKey(mj.getResult())) {
                                zhongfabaigang_map.put(mj.getResult(), (Integer)zhongfabaigang_map.get(mj.getResult()) + 1);
                            } else {
                                zhongfabaigang_map.put(mj.getResult(), 1);
                            }
                        }
                    });
                    if (zhongfabaigang_map.size() > 0) {
                        List<Mj> gangpai = yina_bottom.stream().filter(mj1 -> mj1.getPaizhuantai() == 0 && (mj1.getResult() == 35 || mj1.getResult() == 36 || mj1.getResult() == 37)).collect(Collectors.toList());
                        gangpai.forEach(mj -> {
                            ImageView an_gang_text = new ImageView(Config.GANG);
                            an_gang_text.getStyleClass().add("caozuo_tishi");
                            an_gang_text.setFitHeight(Config.MJ_WIDTH);
                            an_gang_text.setFitWidth(Config.MJ_WIDTH);
                            an_gang_text.setUserData(this.direction);
                            an_gang_text.setVisible(true);
                            mj.getYina_imageView().addClass("border_deepskyblue");
                            mj.getYina_imageView().addBottom(an_gang_text);
                            an_gang_text.setOnMouseClicked(event -> {
                                MainController.saveState();
                                mj.setPaizhuantai(4);
                                Config.yuce.clear();
                                MainController.dapaifangx = this.direction;
                                switch (Config.systemConfig.gangpaishunxu) {
                                    case "\u987a\u6760\u724c": {
                                        MainController.mopai();
                                        break;
                                    }
                                    case "\u540e\u6760\u724c": {
                                        MainController.houGangMopai();
                                        break;
                                    }
                                    case "\u968f\u610f\u6760": {
                                        MainController.SuiYiGang();
                                    }
                                }
                            });
                        });
                    }
                }
                if (Config.systemConfig.yaojiugang == 1) {
                    Map<Integer, Integer> dongnanjigang_map = new HashMap(yina_bottom_pang.size());
                    yina_bottom_pang.forEach(mj -> {
                        if ((mj.getResult() == 1 || mj.getResult() == 11 || mj.getResult() == 21) && mj.getPaizhuantai() == 4) {
                            if (dongnanjigang_map.containsKey(mj.getResult())) {
                                dongnanjigang_map.put(mj.getResult(), (Integer)dongnanjigang_map.get(mj.getResult()) + 1);
                            } else {
                                dongnanjigang_map.put(mj.getResult(), 1);
                            }
                        }
                    });
                    if (dongnanjigang_map.size() == 3) {
                        List<Mj> gangpai = yina_bottom.stream().filter(mj1 -> mj1.getPaizhuantai() == 0 && (mj1.getResult() == 1 || mj1.getResult() == 11 || mj1.getResult() == 21)).collect(Collectors.toList());
                        gangpai.forEach(mj -> {
                            ImageView an_gang_text = new ImageView(Config.GANG);
                            an_gang_text.getStyleClass().add("caozuo_tishi");
                            an_gang_text.setFitHeight(Config.MJ_WIDTH);
                            an_gang_text.setFitWidth(Config.MJ_WIDTH);
                            an_gang_text.setUserData(this.direction);
                            an_gang_text.setVisible(true);
                            mj.getYina_imageView().addClass("border_deepskyblue");
                            mj.getYina_imageView().addBottom(an_gang_text);
                            an_gang_text.setOnMouseClicked(event -> {
                                MainController.saveState();
                                mj.setPaizhuantai(4);
                                Config.yuce.clear();
                                MainController.dapaifangx = this.direction;
                                switch (Config.systemConfig.gangpaishunxu) {
                                    case "\u987a\u6760\u724c": {
                                        MainController.mopai();
                                        break;
                                    }
                                    case "\u540e\u6760\u724c": {
                                        MainController.houGangMopai();
                                        break;
                                    }
                                    case "\u968f\u610f\u6760": {
                                        MainController.SuiYiGang();
                                    }
                                }
                            });
                        });
                    }
                }
                if (Config.systemConfig.yaojiugang == 1) {
                    Map<Integer, Integer> dongnanjigang_map = new HashMap(yina_bottom_pang.size());
                    yina_bottom_pang.forEach(mj -> {
                        if ((mj.getResult() == 9 || mj.getResult() == 19 || mj.getResult() == 29) && mj.getPaizhuantai() == 4) {
                            if (dongnanjigang_map.containsKey(mj.getResult())) {
                                dongnanjigang_map.put(mj.getResult(), (Integer)dongnanjigang_map.get(mj.getResult()) + 1);
                            } else {
                                dongnanjigang_map.put(mj.getResult(), 1);
                            }
                        }
                    });
                    if (dongnanjigang_map.size() == 3) {
                        List<Mj> gangpai = yina_bottom.stream().filter(mj1 -> mj1.getPaizhuantai() == 0 && (mj1.getResult() == 9 || mj1.getResult() == 19 || mj1.getResult() == 29)).collect(Collectors.toList());
                        gangpai.forEach(mj -> {
                            ImageView an_gang_text = new ImageView(Config.GANG);
                            an_gang_text.getStyleClass().add("caozuo_tishi");
                            an_gang_text.setFitHeight(Config.MJ_WIDTH);
                            an_gang_text.setFitWidth(Config.MJ_WIDTH);
                            an_gang_text.setUserData(this.direction);
                            an_gang_text.setVisible(true);
                            mj.getYina_imageView().addClass("border_deepskyblue");
                            mj.getYina_imageView().addBottom(an_gang_text);
                            an_gang_text.setOnMouseClicked(event -> {
                                MainController.saveState();
                                mj.setPaizhuantai(4);
                                Config.yuce.clear();
                                MainController.dapaifangx = this.direction;
                                switch (Config.systemConfig.gangpaishunxu) {
                                    case "\u987a\u6760\u724c": {
                                        MainController.mopai();
                                        break;
                                    }
                                    case "\u540e\u6760\u724c": {
                                        MainController.houGangMopai();
                                        break;
                                    }
                                    case "\u968f\u610f\u6760": {
                                        MainController.SuiYiGang();
                                    }
                                }
                            });
                        });
                    }
                }
                if (Config.systemConfig.fengpaizipaisanzhangkegang == 1) {
                    Map<Integer, Integer> dongnaxibei_map = new HashMap(yina_bottom.size());
                    yina_bottom.forEach(mj -> {
                        if (mj.getResult() >= 31 && mj.getResult() <= 34) {
                            if (dongnaxibei_map.containsKey(mj.getResult())) {
                                dongnaxibei_map.put(mj.getResult(), (Integer)dongnaxibei_map.get(mj.getResult()) + 1);
                            } else {
                                dongnaxibei_map.put(mj.getResult(), 1);
                            }
                        }
                    });
                    if (dongnaxibei_map.size() == 4) {
                        List<Mj> gangpai = yina_bottom.stream().filter(mj1 -> mj1.getPaizhuantai() == 0 && dongnaxibei_map.getOrDefault(mj1.getResult(), 0) > 0).collect(Collectors.toList());
                        gangpai.sort(new MainController.ComparatorMjResult());
                        iterator32 = gangpai.iterator();
                        int before_result = 0;
                        while (iterator32.hasNext()) {
                            item = (Mj)iterator32.next();
                            if (item.getResult() == before_result) {
                                iterator32.remove();
                            }
                            before_result = item.getResult();
                        }
                        gangpai.forEach(mj -> {
                            ImageView an_gang_text = new ImageView(Config.GANG);
                            an_gang_text.getStyleClass().add("caozuo_tishi");
                            an_gang_text.setFitHeight(Config.MJ_WIDTH);
                            an_gang_text.setFitWidth(Config.MJ_WIDTH);
                            an_gang_text.setUserData(this.direction);
                            an_gang_text.setVisible(true);
                            mj.getYina_imageView().addClass("border_deepskyblue");
                            mj.getYina_imageView().addBottom(an_gang_text);
                            an_gang_text.setOnMouseClicked(event -> {
                                MainController.saveState();
                                gangpai.forEach(mj1 -> mj1.setPaizhuantai(3));
                                Config.yuce.clear();
                                MainController.dapaifangx = this.direction;
                                switch (Config.systemConfig.gangpaishunxu) {
                                    case "\u987a\u6760\u724c": {
                                        MainController.mopai();
                                        break;
                                    }
                                    case "\u540e\u6760\u724c": {
                                        MainController.houGangMopai();
                                        break;
                                    }
                                    case "\u968f\u610f\u6760": {
                                        MainController.SuiYiGang();
                                    }
                                }
                            });
                        });
                    } else if (dongnaxibei_map.size() == 3) {
                        List<Mj> gangpai = yina_bottom.stream().filter(mj1 -> mj1.getPaizhuantai() == 0 && dongnaxibei_map.getOrDefault(mj1.getResult(), 0) > 0).collect(Collectors.toList());
                        gangpai.sort(new MainController.ComparatorMjResult());
                        iterator32 = gangpai.iterator();
                        int before_result = 0;
                        while (iterator32.hasNext()) {
                            item = (Mj)iterator32.next();
                            if (item.getResult() == before_result) {
                                iterator32.remove();
                            }
                            before_result = item.getResult();
                        }
                        gangpai.forEach(mj -> {
                            ImageView an_gang_text = new ImageView(Config.GANG);
                            an_gang_text.getStyleClass().add("caozuo_tishi");
                            an_gang_text.setFitHeight(Config.MJ_WIDTH);
                            an_gang_text.setFitWidth(Config.MJ_WIDTH);
                            an_gang_text.setUserData(this.direction);
                            an_gang_text.setVisible(true);
                            mj.getYina_imageView().addClass("border_deepskyblue");
                            mj.getYina_imageView().addBottom(an_gang_text);
                            an_gang_text.setOnMouseClicked(event -> {
                                MainController.saveState();
                                gangpai.forEach(mj1 -> mj1.setPaizhuantai(3));
                                Config.yuce.clear();
                                MainController.dapaifangx = this.direction;
                                this.clear();
                                MainController.displayYinaPai();
                            });
                        });
                    }
                }
                if (Config.systemConfig.fengpaizipaisanzhangkegang == 1) {
                    Map<Integer, Integer> zhongfabai_map = new HashMap(yina_bottom.size());
                    yina_bottom.forEach(mj -> {
                        if (mj.getResult() >= 35 && mj.getResult() <= 37) {
                            if (zhongfabai_map.containsKey(mj.getResult())) {
                                zhongfabai_map.put(mj.getResult(), (Integer)zhongfabai_map.get(mj.getResult()) + 1);
                            } else {
                                zhongfabai_map.put(mj.getResult(), 1);
                            }
                        }
                    });
                    if (zhongfabai_map.size() == 3) {
                        List<Mj> gangpai = yina_bottom.stream().filter(mj1 -> mj1.getPaizhuantai() == 0 && zhongfabai_map.getOrDefault(mj1.getResult(), 0) > 0).collect(Collectors.toList());
                        gangpai.sort(new MainController.ComparatorMjResult());
                        iterator32 = gangpai.iterator();
                        int before_result = 0;
                        while (iterator32.hasNext()) {
                            item = (Mj)iterator32.next();
                            if (item.getResult() == before_result) {
                                iterator32.remove();
                            }
                            before_result = item.getResult();
                        }
                        gangpai.forEach(mj -> {
                            ImageView an_gang_text = new ImageView(Config.GANG);
                            an_gang_text.getStyleClass().add("caozuo_tishi");
                            an_gang_text.setFitHeight(Config.MJ_WIDTH);
                            an_gang_text.setFitWidth(Config.MJ_WIDTH);
                            an_gang_text.setUserData(this.direction);
                            an_gang_text.setVisible(true);
                            mj.getYina_imageView().addClass("border_deepskyblue");
                            mj.getYina_imageView().addBottom(an_gang_text);
                            an_gang_text.setOnMouseClicked(event -> {
                                MainController.saveState();
                                gangpai.forEach(mj1 -> mj1.setPaizhuantai(3));
                                Config.yuce.clear();
                                MainController.dapaifangx = this.direction;
                                this.clear();
                                MainController.displayYinaPai();
                            });
                        });
                    }
                }
                if (Config.systemConfig.fengpaizipaisanzhangkegang == 1) {
                    Map<Integer, Integer> dongnanxibeigang_map = new HashMap(yina_bottom_pang.size());
                    yina_bottom_pang.forEach(mj -> {
                        if (mj.getResult() >= 31 && mj.getResult() <= 34 && mj.getPaizhuantai() == 3) {
                            if (dongnanxibeigang_map.containsKey(mj.getResult())) {
                                dongnanxibeigang_map.put(mj.getResult(), (Integer)dongnanxibeigang_map.get(mj.getResult()) + 1);
                            } else {
                                dongnanxibeigang_map.put(mj.getResult(), 1);
                            }
                        }
                    });
                    if (dongnanxibeigang_map.size() > 0 && (dongnanxibeigang_map.getOrDefault(31, 4) < 4 || dongnanxibeigang_map.getOrDefault(32, 4) < 4 || dongnanxibeigang_map.getOrDefault(33, 4) < 4 || dongnanxibeigang_map.getOrDefault(34, 4) < 4)) {
                        List<Mj> gangpai = yina_bottom.stream().filter(mj1 -> mj1.getPaizhuantai() == 0 && (mj1.getResult() == 31 || mj1.getResult() == 32 || mj1.getResult() == 33 || mj1.getResult() == 34)).collect(Collectors.toList());
                        gangpai.forEach(mj -> {
                            ImageView an_gang_text = new ImageView(Config.GANG);
                            an_gang_text.getStyleClass().add("caozuo_tishi");
                            an_gang_text.setFitHeight(Config.MJ_WIDTH);
                            an_gang_text.setFitWidth(Config.MJ_WIDTH);
                            an_gang_text.setUserData(this.direction);
                            an_gang_text.setVisible(true);
                            mj.getYina_imageView().addClass("border_deepskyblue");
                            mj.getYina_imageView().addBottom(an_gang_text);
                            an_gang_text.setOnMouseClicked(event -> {
                                MainController.saveState();
                                mj.setPaizhuantai(3);
                                Config.yuce.clear();
                                MainController.dapaifangx = this.direction;
                                switch (Config.systemConfig.gangpaishunxu) {
                                    case "\u987a\u6760\u724c": {
                                        MainController.mopai();
                                        break;
                                    }
                                    case "\u540e\u6760\u724c": {
                                        MainController.houGangMopai();
                                        break;
                                    }
                                    case "\u968f\u610f\u6760": {
                                        MainController.SuiYiGang();
                                    }
                                }
                            });
                        });
                    }
                }
                if (Config.systemConfig.fengpaizipaisanzhangkegang == 1) {
                    Map<Integer, Integer> zhongfabaigang_map = new HashMap(yina_bottom_pang.size());
                    yina_bottom_pang.forEach(mj -> {
                        if (mj.getResult() >= 35 && mj.getResult() <= 37 && mj.getPaizhuantai() == 3) {
                            if (zhongfabaigang_map.containsKey(mj.getResult())) {
                                zhongfabaigang_map.put(mj.getResult(), (Integer)zhongfabaigang_map.get(mj.getResult()) + 1);
                            } else {
                                zhongfabaigang_map.put(mj.getResult(), 1);
                            }
                        }
                    });
                    if (zhongfabaigang_map.size() > 0) {
                        List<Mj> gangpai = yina_bottom.stream().filter(mj1 -> mj1.getPaizhuantai() == 0 && (mj1.getResult() == 35 || mj1.getResult() == 36 || mj1.getResult() == 37)).collect(Collectors.toList());
                        gangpai.forEach(mj -> {
                            ImageView an_gang_text = new ImageView(Config.GANG);
                            an_gang_text.getStyleClass().add("caozuo_tishi");
                            an_gang_text.setFitHeight(Config.MJ_WIDTH);
                            an_gang_text.setFitWidth(Config.MJ_WIDTH);
                            an_gang_text.setUserData(this.direction);
                            an_gang_text.setVisible(true);
                            mj.getYina_imageView().addClass("border_deepskyblue");
                            mj.getYina_imageView().addBottom(an_gang_text);
                            an_gang_text.setOnMouseClicked(event -> {
                                MainController.saveState();
                                mj.setPaizhuantai(3);
                                Config.yuce.clear();
                                MainController.dapaifangx = this.direction;
                                switch (Config.systemConfig.gangpaishunxu) {
                                    case "\u987a\u6760\u724c": {
                                        MainController.mopai();
                                        break;
                                    }
                                    case "\u540e\u6760\u724c": {
                                        MainController.houGangMopai();
                                        break;
                                    }
                                    case "\u968f\u610f\u6760": {
                                        MainController.SuiYiGang();
                                    }
                                }
                            });
                        });
                    }
                }
                if (Config.systemConfig.dongnanxibeizhongfabaigang == 1 && Config.yichu.stream().filter(mj -> mj.getChupai_direction() == this.direction).count() == 0L && yina_bottom.stream().filter(mj -> mj.getResult() == 31).count() > 0L && yina_bottom.stream().filter(mj -> mj.getResult() == 32).count() > 0L && yina_bottom.stream().filter(mj -> mj.getResult() == 33).count() > 0L && yina_bottom.stream().filter(mj -> mj.getResult() == 34).count() > 0L) {
                    List<Mj> gangpai2 = yina_bottom.stream().filter(mj1 -> mj1.getPaizhuantai() == 0 && (mj1.getResult() == 31 || mj1.getResult() == 32 || mj1.getResult() == 33 || mj1.getResult() == 34)).collect(Collectors.toList());
                    gangpai2.sort(new MainController.ComparatorMjResult());
                    iterator3 = gangpai2.iterator();
                    int before_result = 0;
                    while (iterator3.hasNext()) {
                        Mj item2 = (Mj)iterator3.next();
                        if (item2.getResult() == before_result) {
                            iterator3.remove();
                        }
                        before_result = item2.getResult();
                    }
                    gangpai2.forEach(mj -> {
                        ImageView an_gang_text = new ImageView(Config.GANG);
                        an_gang_text.getStyleClass().add("caozuo_tishi");
                        an_gang_text.setFitHeight(Config.MJ_WIDTH);
                        an_gang_text.setFitWidth(Config.MJ_WIDTH);
                        an_gang_text.setUserData(this.direction);
                        an_gang_text.setVisible(true);
                        mj.getYina_imageView().addClass("border_deepskyblue");
                        mj.getYina_imageView().addBottom(an_gang_text);
                        an_gang_text.setOnMouseClicked(event -> {
                            MainController.saveState();
                            gangpai2.forEach(mj1 -> mj1.setPaizhuantai(3));
                            Config.yuce.clear();
                            MainController.dapaifangx = this.direction;
                            switch (Config.systemConfig.gangpaishunxu) {
                                case "\u987a\u6760\u724c": {
                                    MainController.mopai();
                                    break;
                                }
                                case "\u540e\u6760\u724c": {
                                    MainController.houGangMopai();
                                    break;
                                }
                                case "\u968f\u610f\u6760": {
                                    MainController.SuiYiGang();
                                }
                            }
                        });
                    });
                }
                if (Config.systemConfig.dongnanxibeizhongfabaigang == 1 && Config.yichu.stream().filter(mj -> mj.getChupai_direction() == this.direction).count() == 0L && yina_bottom.stream().filter(mj -> mj.getResult() == 35).count() > 0L && yina_bottom.stream().filter(mj -> mj.getResult() == 36).count() > 0L && yina_bottom.stream().filter(mj -> mj.getResult() == 37).count() > 0L) {
                    List<Mj> gangpai3 = yina_bottom.stream().filter(mj1 -> mj1.getPaizhuantai() == 0 && (mj1.getResult() == 35 || mj1.getResult() == 36 || mj1.getResult() == 37)).collect(Collectors.toList());
                    gangpai3.sort(new MainController.ComparatorMjResult());
                    iterator3 = gangpai3.iterator();
                    int before_result = 0;
                    while (iterator3.hasNext()) {
                        Mj item3 = (Mj)iterator3.next();
                        if (item3.getResult() == before_result) {
                            iterator3.remove();
                        }
                        before_result = item3.getResult();
                    }
                    gangpai3.forEach(mj -> {
                        ImageView an_gang_text = new ImageView(Config.GANG);
                        an_gang_text.getStyleClass().add("caozuo_tishi");
                        an_gang_text.setFitHeight(Config.MJ_WIDTH);
                        an_gang_text.setFitWidth(Config.MJ_WIDTH);
                        an_gang_text.setUserData(this.direction);
                        an_gang_text.setVisible(true);
                        mj.getYina_imageView().addClass("border_deepskyblue");
                        mj.getYina_imageView().addBottom(an_gang_text);
                        an_gang_text.setOnMouseClicked(event -> {
                            MainController.saveState();
                            gangpai3.forEach(mj1 -> mj1.setPaizhuantai(4));
                            Config.yuce.clear();
                            MainController.dapaifangx = this.direction;
                            this.clear();
                            MainController.displayYinaPai();
                        });
                    });
                }
                if (Config.systemConfig.dongnanxibeikechikegang == 1 && yina_bottom.stream().filter(mj -> mj.getResult() == 31).count() > 0L && yina_bottom.stream().filter(mj -> mj.getResult() == 32).count() > 0L && yina_bottom.stream().filter(mj -> mj.getResult() == 33).count() > 0L && yina_bottom.stream().filter(mj -> mj.getResult() == 34).count() > 0L) {
                    List<Mj> gangpai4 = yina_bottom.stream().filter(mj1 -> mj1.getPaizhuantai() == 0 && (mj1.getResult() == 31 || mj1.getResult() == 32 || mj1.getResult() == 33 || mj1.getResult() == 34)).collect(Collectors.toList());
                    gangpai4.sort(new MainController.ComparatorMjResult());
                    iterator3 = gangpai4.iterator();
                    int before_result = 0;
                    while (iterator3.hasNext()) {
                        Mj item4 = (Mj)iterator3.next();
                        if (item4.getResult() == before_result) {
                            iterator3.remove();
                        }
                        before_result = item4.getResult();
                    }
                    gangpai4.forEach(mj -> {
                        ImageView an_gang_text = new ImageView(Config.GANG);
                        an_gang_text.getStyleClass().add("caozuo_tishi");
                        an_gang_text.setFitHeight(Config.MJ_WIDTH);
                        an_gang_text.setFitWidth(Config.MJ_WIDTH);
                        an_gang_text.setUserData(this.direction);
                        an_gang_text.setVisible(true);
                        mj.getYina_imageView().addClass("border_deepskyblue");
                        mj.getYina_imageView().addBottom(an_gang_text);
                        an_gang_text.setOnMouseClicked(event -> {
                            MainController.saveState();
                            gangpai4.forEach(mj1 -> mj1.setPaizhuantai(3));
                            Config.yuce.clear();
                            MainController.dapaifangx = this.direction;
                            switch (Config.systemConfig.gangpaishunxu) {
                                case "\u987a\u6760\u724c": {
                                    MainController.mopai();
                                    break;
                                }
                                case "\u540e\u6760\u724c": {
                                    MainController.houGangMopai();
                                    break;
                                }
                                case "\u968f\u610f\u6760": {
                                    MainController.SuiYiGang();
                                }
                            }
                        });
                    });
                }
                if (Config.systemConfig.caishengcanyuchipanggang == 1) {
                    List<Mj> caishengs = yina_bottom_pang.stream().filter(mj1 -> mj1.caisheng).collect(Collectors.toList());
                    MainController.kech_list_panel.getChildren().clear();
                    if (caishengs.size() > 0) {
                        Label label = new Label("\u8d22\u795e\u8d77\u98de");
                        label.setFont(Font.font("\u5b8b\u4f53", FontWeight.BOLD, 20.0));
                        label.setStyle("-fx-background-color: white;-fx-text-fill: red");
                        MainController.kech_list_panel.getChildren().add(label);
                        MainController.kech_list_panel.setLayoutX(MainController.rightPanel.getLayoutX() - 2.0 * Config.MJ_HEIGHT_3_4);
                        MainController.kech_list_panel.setLayoutY(MainController.rightPanel.getLayoutY() + MainController.rightPanel.getYinaPanel().getHeight() / 2.0);
                        MainController.kech_list_panel.setStyle("-fx-background-color: white;-fx-padding: 10px;");
                        MainController.kech_list_panel.setAlignment(Pos.CENTER);
                        MainController.kech_list_panel.setVisible(false);
                        if (!MainController.static_main_panel.getChildren().contains(MainController.kech_list_panel)) {
                            MainController.static_main_panel.getChildren().add(MainController.kech_list_panel);
                        }
                        for (Mj mj : caishengs) {
                            List<Mj> keqifeis = yina_bottom.stream().filter(mj1 -> mj1.getResult() == mj.caisheng_dipai).collect(Collectors.toList());
                            if (keqifeis.size() <= 0) continue;
                            MainController.kech_list_panel.setVisible(true);
                            HBox hBox = new HBox();
                            hBox.setAlignment(Pos.CENTER);
                            hBox.setCursor(Cursor.HAND);
                            imageView1 = new ImageView();
                            imageView1.setImage(ImageKit.coverImage(Config.getImageByMj(mj), Config.LANSE));
                            imageView1.setFitWidth(Config.MJ_WIDTH_3_4);
                            imageView1.setFitHeight(Config.MJ_HEIGHT_3_4);
                            hBox.getChildren().add(imageView1);
                            ImageView imageView2 = new ImageView();
                            imageView2.setImage(Config.getImageByNumber(mj.caisheng_dipai));
                            imageView2.setFitWidth(Config.MJ_WIDTH_3_4);
                            imageView2.setFitHeight(Config.MJ_HEIGHT_3_4);
                            hBox.getChildren().add(imageView2);
                            hBox.setOnMouseClicked(event -> {
                                MainController.static_main_panel.getChildren().remove(MainController.kech_list_panel);
                                ((Mj)keqifeis.get(0)).setPaizhuantai(mj.getPaizhuantai());
                                mj.setPaizhuantai(0);
                                mj.caisheng_dipai = 0;
                                this.clear();
                                MainController.displayYinaPai();
                            });
                            MainController.kech_list_panel.getChildren().add(hBox);
                        }
                    }
                }
            }
            List<Mj> yuce = Config.yuce.stream().filter(mj -> mj.getYuce_direction() == this.direction).collect(Collectors.toList());
            this.hupaiYuCe(yina_bottom, yina_bottom_pang, imageView, size, yuce);
        }
        this.yinaPanel.layout();
        this.caozuoPanel.setLayoutY(this.yinaPanel.getBoundsInLocal().getHeight() / 2.0);
    }

    private void hupaiYuCe(List<Mj> yina_bottom, List<Mj> yina_bottom_pang, ImageView imageView, int size, List<Mj> yuce) {
        int i;
        if (MainController.dapaifangx == MainController.dapaifangx) {
            Map<Integer, Integer> map = new HashMap(yina_bottom.size());
            yina_bottom.forEach(mj -> {
                List<Mj> jiance_gang;
                if (map.containsKey(mj.getResult())) {
                    map.put(mj.getResult(), (Integer)map.get(mj.getResult()) + 1);
                } else {
                    map.put(mj.getResult(), 1);
                }
                if (Config.systemConfig.juedicaisheng == 1 && (jiance_gang = yina_bottom_pang.stream().filter(mj1 -> mj1.getPaizhuantai() == 1 && mj1.getResult() == mj.getResult()).collect(Collectors.toList())).size() == 3) {
                    mj.caisheng = true;
                    mj.getYina_imageView().setImage(ImageKit.coverImage(mj.getYina_imageView().getImage(), Config.LANSE));
                }
                if (Config.systemConfig.gangshouzhongpai == 1) {
                    List<Mj> jiance_caisheng_gang;
                    jiance_gang = yina_bottom_pang.stream().filter(mj1 -> !(mj1.getPaizhuantai() != 1 && mj1.getPaizhuantai() != 2 && mj1.getPaizhuantai() != 3 || mj1.getResult() != mj.getResult() || mj1.yuanpai != mj.yuanpai || mj.caisheng && Config.systemConfig.caishenganzhengchangpaichanyuchangpanggang != 1)).collect(Collectors.toList());
                    if (jiance_gang.size() > 0) {
                        ImageView zhui_gang_text = new ImageView(Config.GANG);
                        zhui_gang_text.getStyleClass().add("caozuo_tishi");
                        zhui_gang_text.setFitHeight(Config.MJ_WIDTH);
                        zhui_gang_text.setFitWidth(Config.MJ_WIDTH);
                        mj.getYina_imageView().addBottom(zhui_gang_text);
                        zhui_gang_text.setOnMouseClicked(event -> {
                            MainController.saveState();
                            mj.getYina_imageView().clear();
                            mj.setPaizhuantai(2);
                            List<Mj> gangpai = yina_bottom_pang.stream().filter(mj1 -> mj1.getResult() == mj.getResult()).collect(Collectors.toList());
                            gangpai.forEach(mj1 -> mj1.setPaizhuantai(2));
                            if (Config.systemConfig.qiangganghu == 1) {
                                boolean hu = false;
                                for (MjPanelInterface direction1 : MainController.directions) {
                                    if (direction1.getHu() == 1 || direction1.getDirection() == this.direction) continue;
                                    List<Mj> yina_jiance = Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == direction1.getDirection()).collect(Collectors.toList());
                                    Mj hu_mj = (Mj)mj.clone();
                                    hu_mj.setPaizhuantai(0);
                                    yina_jiance.add(hu_mj);
                                    hu = HuPaiKit.HuPaiPanDin(yina_jiance, this.que);
                                    if (!hu) continue;
                                    direction1.getHu_text().setUserData(direction1.getDirection());
                                    direction1.getCaozuoPanel().getChildren().add(direction1.getHu_text());
                                    yina_jiance.forEach(mj1 -> {
                                        if (mj1.getPaizhuantai() == 0) {
                                            mj1.getYina_imageView().setImage(ImageKit.drawBorder(mj1.getYina_imageView().getImage()));
                                        }
                                    });
                                    direction1.getHu_text().setOnMouseClicked(event1 -> {
                                        MainController.saveState();
                                        MainController.dapaifangx = (Integer)direction1.getHu_text().getUserData();
                                        direction1.setHu(1);
                                        Config.yichu.remove(mj);
                                        Config.yina.remove(mj);
                                        MainController.yichu_pane.getChildren().remove(mj.getYichu_imageView());
                                        direction1.setHu_mj(hu_mj);
                                        direction1.getHu_mj().setNapai_direction(MainController.dapaifangx);
                                        direction1.getHu_mj().setPaizhuantai(0);
                                        direction1.getHu_mj().setHu(true);
                                        Config.yuce.clear();
                                        MainController.mopai();
                                    });
                                }
                                if (!hu) {
                                    Config.yuce.clear();
                                    MainController.dapaifangx = this.direction;
                                    switch (Config.systemConfig.gangpaishunxu) {
                                        case "\u987a\u6760\u724c": {
                                            MainController.mopai();
                                            break;
                                        }
                                        case "\u540e\u6760\u724c": {
                                            MainController.houGangMopai();
                                            break;
                                        }
                                        case "\u968f\u610f\u6760": {
                                            MainController.SuiYiGang();
                                        }
                                    }
                                }
                            } else {
                                Config.yuce.clear();
                                MainController.dapaifangx = this.direction;
                                switch (Config.systemConfig.gangpaishunxu) {
                                    case "\u987a\u6760\u724c": {
                                        MainController.mopai();
                                        break;
                                    }
                                    case "\u540e\u6760\u724c": {
                                        MainController.houGangMopai();
                                        break;
                                    }
                                    case "\u968f\u610f\u6760": {
                                        MainController.SuiYiGang();
                                    }
                                }
                            }
                        });
                    }
                    if (Config.systemConfig.caishengcanyuchipanggang == 1 && (jiance_caisheng_gang = yina_bottom_pang.stream().filter(mj1 -> mj1.getPaizhuantai() == 1).collect(Collectors.toList())).size() > 0 && mj.caisheng && mj.getPaizhuantai() == 0) {
                        ImageView zhui_gang_text = new ImageView(Config.GANG);
                        zhui_gang_text.getStyleClass().add("caozuo_tishi");
                        zhui_gang_text.setFitHeight(Config.MJ_WIDTH);
                        zhui_gang_text.setFitWidth(Config.MJ_WIDTH);
                        mj.getYina_imageView().addBottom(zhui_gang_text);
                        zhui_gang_text.setOnMouseClicked(event -> {
                            MainController.saveState();
                            if (mj.caisheng) {
                                mj.caisheng_dipai = ((Mj)jiance_caisheng_gang.get(0)).getResult();
                            }
                            mj.getYina_imageView().clear();
                            mj.setPaizhuantai(2);
                            List<Mj> gangpai = jiance_caisheng_gang.stream().filter(mj1 -> mj1.getResult() == mj.caisheng_dipai).collect(Collectors.toList());
                            gangpai.forEach(mj1 -> mj1.setPaizhuantai(2));
                            if (Config.systemConfig.qiangganghu == 1) {
                                boolean hu = false;
                                for (MjPanelInterface direction1 : MainController.directions) {
                                    if (direction1.getHu() == 1 || direction1.getDirection() == this.direction) continue;
                                    List<Mj> yina_jiance = Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == direction1.getDirection()).collect(Collectors.toList());
                                    Mj hu_mj = (Mj)mj.clone();
                                    hu_mj.setPaizhuantai(0);
                                    yina_jiance.add(hu_mj);
                                    hu = HuPaiKit.HuPaiPanDin(yina_jiance, this.que);
                                    if (!hu) continue;
                                    direction1.getHu_text().setUserData(direction1.getDirection());
                                    direction1.getCaozuoPanel().getChildren().add(direction1.getHu_text());
                                    yina_jiance.forEach(mj1 -> {
                                        if (mj1.getPaizhuantai() == 0) {
                                            mj1.getYina_imageView().setImage(ImageKit.drawBorder(mj1.getYina_imageView().getImage()));
                                        }
                                    });
                                    direction1.getHu_text().setOnMouseClicked(event1 -> {
                                        MainController.saveState();
                                        MainController.dapaifangx = (Integer)direction1.getHu_text().getUserData();
                                        direction1.setHu(1);
                                        Config.yichu.remove(mj);
                                        Config.yina.remove(mj);
                                        MainController.yichu_pane.getChildren().remove(mj.getYichu_imageView());
                                        direction1.setHu_mj(hu_mj);
                                        direction1.getHu_mj().setNapai_direction(MainController.dapaifangx);
                                        direction1.getHu_mj().setPaizhuantai(0);
                                        direction1.getHu_mj().setHu(true);
                                        Config.yuce.clear();
                                        MainController.mopai();
                                    });
                                }
                                if (!hu) {
                                    Config.yuce.clear();
                                    MainController.dapaifangx = this.direction;
                                    switch (Config.systemConfig.gangpaishunxu) {
                                        case "\u987a\u6760\u724c": {
                                            MainController.mopai();
                                            break;
                                        }
                                        case "\u540e\u6760\u724c": {
                                            MainController.houGangMopai();
                                            break;
                                        }
                                        case "\u968f\u610f\u6760": {
                                            MainController.SuiYiGang();
                                        }
                                    }
                                }
                            } else {
                                Config.yuce.clear();
                                MainController.dapaifangx = this.direction;
                                switch (Config.systemConfig.gangpaishunxu) {
                                    case "\u987a\u6760\u724c": {
                                        MainController.mopai();
                                        break;
                                    }
                                    case "\u540e\u6760\u724c": {
                                        MainController.houGangMopai();
                                        break;
                                    }
                                    case "\u968f\u610f\u6760": {
                                        MainController.SuiYiGang();
                                    }
                                }
                            }
                        });
                    }
                }
            });
            if (Config.systemConfig.juedicaisheng == 1) {
                for (Map.Entry sub_enty : map.entrySet()) {
                    if ((Integer)sub_enty.getValue() <= 3) continue;
                    List list = yina_bottom.stream().filter(mj1 -> mj1.getPaizhuantai() == 0 && mj1.getResult() == ((Integer)sub_enty.getKey()).intValue()).collect(Collectors.toList());
                    Mj mj3 = (Mj)list.get(0);
                    mj3.caisheng = true;
                    mj3.getYina_imageView().setImage(ImageKit.coverImage(mj3.getYina_imageView().getImage(), Config.LANSE));
                }
            }
            if (Config.systemConfig.angang == 1) {
                List<Mj> gangpai;
                if ("\u8f6e\u5230\u81ea\u7531\u6760(\u624b\u52a8\u6760\u82b1)".equals(Config.systemConfig.gangfa) && (MainController.caisheng.size() == 0 || MainController.caisheng.get(0) < 40)) {
                    gangpai = yina_bottom.stream().filter(mj1 -> mj1.getPaizhuantai() == 0 && mj1.getResult() > 40 && mj1.getResult() <= 51).collect(Collectors.toList());
                    gangpai.forEach(mj -> {
                        ImageView an_gang_text = new ImageView(Config.GANG);
                        an_gang_text.getStyleClass().add("caozuo_tishi");
                        an_gang_text.setFitHeight(Config.MJ_WIDTH);
                        an_gang_text.setFitWidth(Config.MJ_WIDTH);
                        an_gang_text.setUserData(this.direction);
                        an_gang_text.setVisible(true);
                        mj.getYina_imageView().addClass("border_deepskyblue");
                        mj.getYina_imageView().addBottom(an_gang_text);
                        an_gang_text.setOnMouseClicked(event -> {
                            MainController.saveState();
                            mj.setPaizhuantai(3);
                            Config.yuce.clear();
                            MainController.dapaifangx = this.direction;
                            switch (Config.systemConfig.ganghua) {
                                case "\u987a\u6760\u724c": {
                                    MainController.mopai();
                                    break;
                                }
                                case "\u540e\u6760\u724c": {
                                    MainController.houGangMopai();
                                    break;
                                }
                                case "\u968f\u610f\u6760": {
                                    MainController.SuiYiGang();
                                }
                            }
                        });
                    });
                }
                if (Config.systemConfig.danzhangkegang == 1 && Config.systemConfig.danzhangkegang_pai.size() > 0) {
                    gangpai = yina_bottom.stream().filter(mj1 -> mj1.getPaizhuantai() == 0 && Config.systemConfig.danzhangkegang_pai.contains(mj1.getResult())).collect(Collectors.toList());
                    gangpai.forEach(mj -> {
                        ImageView an_gang_text = new ImageView(Config.GANG);
                        an_gang_text.getStyleClass().add("caozuo_tishi");
                        an_gang_text.setFitHeight(Config.MJ_WIDTH);
                        an_gang_text.setFitWidth(Config.MJ_WIDTH);
                        an_gang_text.setUserData(this.direction);
                        an_gang_text.setVisible(true);
                        mj.getYina_imageView().addClass("border_deepskyblue");
                        mj.getYina_imageView().addBottom(an_gang_text);
                        an_gang_text.setOnMouseClicked(event -> {
                            MainController.saveState();
                            mj.setPaizhuantai(3);
                            Config.yuce.clear();
                            MainController.dapaifangx = this.direction;
                            switch (Config.systemConfig.gangpaishunxu) {
                                case "\u987a\u6760\u724c": {
                                    MainController.mopai();
                                    break;
                                }
                                case "\u540e\u6760\u724c": {
                                    MainController.houGangMopai();
                                    break;
                                }
                                case "\u968f\u610f\u6760": {
                                    MainController.SuiYiGang();
                                }
                            }
                        });
                    });
                }
                if (Config.systemConfig.caishengkedangang == 1 && MainController.caisheng.size() > 0) {
                    gangpai = yina_bottom.stream().filter(mj1 -> mj1.getPaizhuantai() == 0 && mj1.caisheng).collect(Collectors.toList());
                    gangpai.forEach(mj -> {
                        ImageView an_gang_text = new ImageView(Config.GANG);
                        an_gang_text.getStyleClass().add("caozuo_tishi");
                        an_gang_text.setFitHeight(Config.MJ_WIDTH);
                        an_gang_text.setFitWidth(Config.MJ_WIDTH);
                        an_gang_text.setUserData(this.direction);
                        an_gang_text.setVisible(true);
                        mj.getYina_imageView().addClass("border_deepskyblue");
                        mj.getYina_imageView().addBottom(an_gang_text);
                        an_gang_text.setOnMouseClicked(event -> {
                            MainController.saveState();
                            mj.setPaizhuantai(3);
                            Config.yuce.clear();
                            MainController.dapaifangx = this.direction;
                            switch (Config.systemConfig.gangpaishunxu) {
                                case "\u987a\u6760\u724c": {
                                    MainController.mopai();
                                    break;
                                }
                                case "\u540e\u6760\u724c": {
                                    MainController.houGangMopai();
                                    break;
                                }
                                case "\u968f\u610f\u6760": {
                                    MainController.SuiYiGang();
                                }
                            }
                        });
                    });
                }
                if (Config.systemConfig.guangxi152 == 1) {
                    gangpai = yina_bottom.stream().filter(mj1 -> mj1.getPaizhuantai() == 0 && mj1.getResult() >= 52 && mj1.getResult() <= 58).collect(Collectors.toList());
                    gangpai.forEach(mj -> {
                        ImageView an_gang_text = new ImageView(Config.GANG);
                        an_gang_text.getStyleClass().add("caozuo_tishi");
                        an_gang_text.setFitHeight(Config.MJ_WIDTH);
                        an_gang_text.setFitWidth(Config.MJ_WIDTH);
                        an_gang_text.setUserData(this.direction);
                        an_gang_text.setVisible(true);
                        mj.getYina_imageView().addClass("border_deepskyblue");
                        mj.getYina_imageView().addBottom(an_gang_text);
                        an_gang_text.setOnMouseClicked(event -> {
                            MainController.saveState();
                            mj.setPaizhuantai(3);
                            Config.yuce.clear();
                            MainController.dapaifangx = this.direction;
                            switch (Config.systemConfig.gangpaishunxu) {
                                case "\u987a\u6760\u724c": {
                                    MainController.mopai();
                                    break;
                                }
                                case "\u540e\u6760\u724c": {
                                    MainController.houGangMopai();
                                    break;
                                }
                                case "\u968f\u610f\u6760": {
                                    MainController.SuiYiGang();
                                }
                            }
                        });
                    });
                }
                for (Map.Entry sub_enty : map.entrySet()) {
                    ImageView an_gang_text;
                    if ((Integer)sub_enty.getValue() > 3) {
                        ImageView imageView2 = new ImageView(Config.GANG);
                        imageView2.getStyleClass().add("caozuo_tishi");
                        imageView2.setFitHeight(Config.MJ_WIDTH);
                        imageView2.setFitWidth(Config.MJ_WIDTH);
                        imageView2.setUserData(this.direction);
                        imageView2.setVisible(true);
                        List<Mj> gangpai3 = yina_bottom.stream().filter(mj1 -> mj1.getPaizhuantai() == 0 && mj1.getResult() == ((Integer)sub_enty.getKey()).intValue() && (!mj1.caisheng || Config.systemConfig.caishenganzhengchangpaichanyuchangpanggang == 1)).collect(Collectors.toList());
                        if (gangpai3.size() > 3) {
                            while (gangpai3.size() > 4) {
                                gangpai3.remove(0);
                            }
                            gangpai3.forEach(mj -> mj.getYina_imageView().addClass("border_deepskyblue"));
                            ((Mj)gangpai3.get(0)).getYina_imageView().addClass("border_deepskyblue");
                            ((Mj)gangpai3.get(0)).getYina_imageView().addBottom(imageView2);
                            imageView2.setOnMouseClicked(event -> {
                                MainController.saveState();
                                gangpai3.forEach(mj1 -> mj1.setPaizhuantai(3));
                                Config.yuce.clear();
                                MainController.dapaifangx = this.direction;
                                switch (Config.systemConfig.gangpaishunxu) {
                                    case "\u987a\u6760\u724c": {
                                        MainController.mopai();
                                        break;
                                    }
                                    case "\u540e\u6760\u724c": {
                                        MainController.houGangMopai();
                                        break;
                                    }
                                    case "\u968f\u610f\u6760": {
                                        MainController.SuiYiGang();
                                    }
                                }
                            });
                        }
                    }
                    if (Config.systemConfig.caishengcanyuchipanggang != 1) continue;
                    List casishengs = yina_bottom.stream().filter(mj -> mj.caisheng).collect(Collectors.toList());
                    if ((Integer)sub_enty.getValue() == 3 && casishengs.size() > 0 && ((Mj)casishengs.get(0)).getResult() != ((Integer)sub_enty.getKey()).intValue()) {
                        an_gang_text = new ImageView(Config.GANG);
                        an_gang_text.getStyleClass().add("caozuo_tishi");
                        an_gang_text.setFitHeight(Config.MJ_WIDTH);
                        an_gang_text.setFitWidth(Config.MJ_WIDTH);
                        an_gang_text.setUserData(this.direction);
                        an_gang_text.setVisible(true);
                        List<Mj> gangpai2 = yina_bottom.stream().filter(mj1 -> mj1.getPaizhuantai() == 0 && mj1.getResult() == ((Integer)sub_enty.getKey()).intValue()).collect(Collectors.toList());
                        gangpai2.forEach(mj -> mj.getYina_imageView().addClass("border_deepskyblue"));
                        ((Mj)casishengs.get(0)).getYina_imageView().addClass("border_deepskyblue");
                        ((Mj)gangpai2.get(0)).getYina_imageView().addBottom(an_gang_text);
                        an_gang_text.setOnMouseClicked(event -> {
                            MainController.saveState();
                            gangpai2.forEach(mj1 -> mj1.setPaizhuantai(3));
                            ((Mj)casishengs.get(0)).setPaizhuantai(3);
                            ((Mj)casishengs.get((int)0)).caisheng_dipai = (Integer)sub_enty.getKey();
                            Config.yuce.clear();
                            MainController.dapaifangx = this.direction;
                            switch (Config.systemConfig.gangpaishunxu) {
                                case "\u987a\u6760\u724c": {
                                    MainController.mopai();
                                    break;
                                }
                                case "\u540e\u6760\u724c": {
                                    MainController.houGangMopai();
                                    break;
                                }
                                case "\u968f\u610f\u6760": {
                                    MainController.SuiYiGang();
                                }
                            }
                        });
                    }
                    if ((Integer)sub_enty.getValue() != 2 || casishengs.size() <= 1 || ((Mj)casishengs.get(0)).getResult() == ((Integer)sub_enty.getKey()).intValue()) continue;
                    an_gang_text = new ImageView(Config.GANG);
                    an_gang_text.getStyleClass().add("caozuo_tishi");
                    an_gang_text.setFitHeight(Config.MJ_WIDTH);
                    an_gang_text.setFitWidth(Config.MJ_WIDTH);
                    an_gang_text.setUserData(this.direction);
                    an_gang_text.setVisible(true);
                    List<Mj> gangpai2 = yina_bottom.stream().filter(mj1 -> mj1.getPaizhuantai() == 0 && mj1.getResult() == ((Integer)sub_enty.getKey()).intValue()).collect(Collectors.toList());
                    gangpai2.forEach(mj -> mj.getYina_imageView().addClass("border_deepskyblue"));
                    ((Mj)casishengs.get(0)).getYina_imageView().addClass("border_deepskyblue");
                    ((Mj)casishengs.get(1)).getYina_imageView().addClass("border_deepskyblue");
                    ((Mj)gangpai2.get(0)).getYina_imageView().addBottom(an_gang_text);
                    an_gang_text.setOnMouseClicked(event -> {
                        MainController.saveState();
                        gangpai2.forEach(mj1 -> mj1.setPaizhuantai(3));
                        ((Mj)casishengs.get(0)).setPaizhuantai(3);
                        ((Mj)casishengs.get((int)0)).caisheng_dipai = (Integer)sub_enty.getKey();
                        ((Mj)casishengs.get(1)).setPaizhuantai(3);
                        ((Mj)casishengs.get((int)1)).caisheng_dipai = (Integer)sub_enty.getKey();
                        Config.yuce.clear();
                        MainController.dapaifangx = this.direction;
                        switch (Config.systemConfig.gangpaishunxu) {
                            case "\u987a\u6760\u724c": {
                                MainController.mopai();
                                break;
                            }
                            case "\u540e\u6760\u724c": {
                                MainController.houGangMopai();
                                break;
                            }
                            case "\u968f\u610f\u6760": {
                                MainController.SuiYiGang();
                            }
                        }
                    });
                }
            }
        }
        size = yuce.size();
        for (i = 0; i < size; ++i) {
            Mj mj2 = yuce.get(i);
            MjYuCeControl mjYuCeControl = new MjYuCeControl();
            imageView = mjYuCeControl.getImageView();
            if (this.keGang(mj2, mj2.getYuce_direction())) {
                imageView.setImage(ImageKit.rotate(ImageKit.coverImage(Config.getImageByMj(mj2), Config.LVSE), -90));
            } else if (mj2.caisheng) {
                imageView.setImage(ImageKit.rotate(ImageKit.coverImage(Config.getImageByMj(mj2), Config.LANSE), -90));
            } else if (Config.systemConfig.danzhangkegang == 1 && Config.systemConfig.danzhangkegang_pai.contains(mj2.getResult())) {
                imageView.setImage(ImageKit.rotate(ImageKit.coverImage(Config.getImageByMj(mj2), Config.LVSE), -90));
            } else {
                imageView.setImage(ImageKit.rotate(Config.getImageByMj(mj2), -90));
            }
            imageView.setFitHeight(Config.MJ_WIDTH_3_4);
            imageView.setFitWidth(Config.MJ_HEIGHT_3_4);
            imageView.setOnMouseClicked(event -> {
                MouseButton button = event.getButton();
                switch (button) {
                    case SECONDARY: {
                        List<Mj> mjList = new ArrayList<Mj>();
                        mjList.addAll(Config.yina);
                        mjList.addAll(Config.yuce);
                        MainController.yuceYinaPanel.setMjs(mjList);
                        MainController.yuceYinaPanel.display();
                        MainController.initYuceYinaPanel();
                    }
                }
            });
            mj2.setYuce_imageView(mjYuCeControl);
            this.yucePanel.getChildren().add(mjYuCeControl);
            if (mj2.huapai_yuce == null) continue;
            MjYuCeControl mjYuCeControl2 = new MjYuCeControl();
            imageView = mjYuCeControl2.getImageView();
            if (this.keGang(mj2, mj2.huapai_yuce.getYuce_direction())) {
                imageView.setImage(ImageKit.rotate(ImageKit.coverImage(Config.getImageByMj(mj2.huapai_yuce), Config.LVSE), -90));
            } else if (mj2.huapai_yuce.caisheng) {
                imageView.setImage(ImageKit.rotate(ImageKit.coverImage(Config.getImageByMj(mj2.huapai_yuce), Config.LANSE), -90));
            } else if (Config.systemConfig.danzhangkegang == 1 && Config.systemConfig.danzhangkegang_pai.contains(mj2.huapai_yuce.getResult())) {
                imageView.setImage(ImageKit.rotate(ImageKit.coverImage(Config.getImageByMj(mj2.huapai_yuce), Config.LVSE), -90));
            } else {
                imageView.setImage(ImageKit.rotate(Config.getImageByMj(mj2.huapai_yuce), -90));
            }
            imageView.setFitHeight(Config.MJ_WIDTH_3_4);
            imageView.setFitWidth(Config.MJ_HEIGHT_3_4);
            mj2.huapai_yuce.setYuce_imageView(mjYuCeControl2);
            this.yucePanel.getChildren().add(mjYuCeControl2);
        }
        size = yuce.size();
        for (i = 0; i < size; ++i) {
            Mj mj2 = yuce.get(i);
            mj2.getYuce_imageView().getImageView().setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    MainController.saveState();
                    MainController.directions.get(this.direction).setHu(1);
                    MainController.directions.get(this.direction).setQiangzhiHu(1);
                    MainController.directions.get(this.direction).setKehu(false);
                    Config.yuce.clear();
                    Text yihu = new Text("\u80e1");
                    yihu.setFont(Font.font("\u5b8b\u4f53", FontWeight.BOLD, 25.0));
                    yihu.setFill(Color.RED);
                    MainController.directions.get(this.direction).getCaozuoPanel().getChildren().clear();
                    MainController.directions.get(this.direction).getCaozuoPanel().getChildren().add(yihu);
                    if (this.direction == MainController.dapaifangx) {
                        MainController.mopai();
                    }
                }
            });
            for (MjPanelInterface mjPanelInterface : MainController.directions) {
                ImageView imageView1;
                Image image;
                int x;
                if (mjPanelInterface.getHu() != 0 || mjPanelInterface.isQueren() || mjPanelInterface.getDirection() == this.direction || mjPanelInterface.getQue() == mj2.getResult() / 10) continue;
                if (this.keGang(mj2, mjPanelInterface.getDirection())) {
                    x = mjPanelInterface.getDirection() - this.direction;
                    image = Config.GANGFANGXIANG_YUCE;
                    imageView1 = new ImageView();
                    if (Math.abs(-90 * x) == 180) {
                        image = ImageKit.rotate(image, -90);
                        imageView1.setImage(image);
                    } else {
                        image = ImageKit.rotate(image, 90 + -90 * x);
                        imageView1.setImage(image);
                    }
                    imageView1.setFitWidth(15.0);
                    imageView1.setFitHeight(15.0);
                    mj2.getYuce_imageView().addBottom(imageView1);
                    continue;
                }
                if (!this.kePeng(mj2, mjPanelInterface.getDirection())) continue;
                x = mjPanelInterface.getDirection() - this.direction;
                image = Config.PENGFANGXIANG_YUCE;
                imageView1 = new ImageView();
                if (Math.abs(-90 * x) == 180) {
                    image = ImageKit.rotate(image, -90);
                    imageView1.setImage(image);
                } else {
                    image = ImageKit.rotate(image, 90 + -90 * x);
                    imageView1.setImage(image);
                }
                imageView1.setFitWidth(15.0);
                imageView1.setFitHeight(15.0);
                mj2.getYuce_imageView().addBottom(imageView1);
            }
        }
        List<Mj> yina_jiance = Config.yina.stream().filter(mj -> mj.getNapai_direction() == this.direction).collect(Collectors.toList());
        Collections.sort(yina_jiance, new MainController.ComparatorMjResult());
        if (HuPaiKit.HuPaiPanDin(yina_jiance, this.que)) {
            this.hu_text.setUserData(this.direction);
            this.caozuoPanel.getChildren().add(this.hu_text);
            this.hu_text.setOnMouseClicked(event -> {
                MainController.saveState();
                MainController.dapaifangx = (Integer)this.hu_text.getUserData();
                this.hu = 1;
                this.kehu = false;
                Mj hu_mj = null;
                for (Mj mj : yina_jiance) {
                    if (!mj.isLast()) continue;
                    hu_mj = mj;
                    break;
                }
                if (hu_mj != null) {
                    Config.yina.remove(hu_mj);
                    this.setHu_mj((Mj)hu_mj.clone());
                    this.getHu_mj().setHu(true);
                }
                Config.yuce.clear();
                if (Config.systemConfig.xueliuchenghe == 1) {
                    this.getXueliuchenghe_hus().clear();
                    this.getYina_hupai().forEach(mj1 -> this.getXueliuchenghe_hus().add(mj1.getResult()));
                }
                if (Config.systemConfig.hupaikemo == 1) {
                    MainController.directions.forEach(d -> {
                        if (!d.isQueren()) {
                            d.setMo(1);
                            d.getMo_text().setUserData(d.getDirection());
                            if (!d.getCaozuoPanel().getChildren().contains(d.getMo_text())) {
                                d.getCaozuoPanel().getChildren().add(d.getMo_text());
                            }
                            d.getMo_text().setOnMouseClicked(e -> {
                                MainController.dapaifangx = d.getDirection();
                                MainController.directions.forEach(direction1 -> {
                                    direction1.setKehu(false);
                                    direction1.setMo(0);
                                });
                                MainController.mopai();
                            });
                        }
                    });
                    return;
                }
                MainController.mopai();
            });
            yina_jiance.forEach(mj -> {
                if (mj.getPaizhuantai() == 0) {
                    if (mj.caisheng) {
                        mj.getYina_imageView().setImage(ImageKit.rotate(ImageKit.drawBorder(ImageKit.coverImage(Config.getImageByMj(mj), Config.LANSE)), -90));
                    } else {
                        mj.getYina_imageView().setImage(ImageKit.rotate(ImageKit.drawBorder(Config.getImageByMj(mj)), -90));
                    }
                }
            });
            try {
                this.yinaPanel.getParent().layout();
                MainController.static_main_panel.layout();
                Robot robot = new Robot();
                robot.mouseMove((int)(JFXKit.getScreenX(this.hu_text) + this.hu_text.getFitWidth() / 2.0), (int)(JFXKit.getScreenY(this.hu_text) + this.hu_text.getFitHeight() / 2.0));
            }
            catch (AWTException e) {
                e.printStackTrace();
            }
        } else {
            Robot robot;
            if (Config.systemConfig.queyimeng == 1) {
                yina_bottom.forEach(mj -> {
                    if (mj.getIsque() && !mj.caisheng) {
                        mj.getYina_imageView().getImageView().setImage(ImageKit.rotate(ImageKit.drawImage(Config.getImageByMj(mj)), -90));
                    }
                });
                if (this.diyizhangda_mj != null) {
                    imageView = new ImageView(ImageKit.rotate(ImageKit.drawImage(Config.getImageByNumber(this.diyizhangda_mj.getResult())), -90));
                    imageView.setFitWidth(Config.MJ_HEIGHT_3_4);
                    imageView.setFitHeight(Config.MJ_WIDTH_3_4);
                    this.quePanel.getChildren().addAll((Node[])new Node[]{imageView});
                }
            }
            List<Mj> yina_jiance1 = Config.yina.stream().filter(mj -> !mj.isLast() && mj.getNapai_direction() == this.direction).collect(Collectors.toList());
            Collections.sort(yina_jiance1, new MainController.ComparatorMjResult());
            StringBuilder stringBuilder = new StringBuilder();
            for (Mj mj5 : yina_jiance1) {
                stringBuilder.append(mj5.getZhiBiaoZHi());
                stringBuilder.append(",");
            }
            if (!this.pre_pai.equals(stringBuilder.toString())) {
                this.pre_pai = stringBuilder.toString();
                this.yina_hupai.clear();
                this.yina_hupai.addAll(MainController.hupaiyuce(yina_jiance1, this.que));
                this.hupaiPanel.getChildren().clear();
                if (this.yina_hupai.size() > 0) {
                    yina_jiance1.forEach(mj -> {
                        if (mj.getPaizhuantai() == 0) {
                            if (mj.caisheng) {
                                mj.getYina_imageView().setImage(ImageKit.rotate(ImageKit.drawBorder(ImageKit.coverImage(Config.getImageByMj(mj), Config.LANSE)), -90));
                            } else {
                                mj.getYina_imageView().setImage(ImageKit.rotate(ImageKit.drawBorder(Config.getImageByMj(mj)), -90));
                            }
                        }
                    });
                }
                double d = 0.0;
                Collections.sort(this.yina_hupai, new MainController.ComparatorMjResult());
                ImageView label = new ImageView(new Image(MainController.class.getResourceAsStream("/quesheng/view/image/ting.png")));
                label.getStyleClass().add("ting_tishi");
                label.setLayoutY(d);
                label.setFitWidth(Config.MJ_HEIGHT_3_4);
                label.setFitHeight(Config.MJ_HEIGHT_3_4);
                label.applyCss();
                d += label.getFitHeight();
                if (this.yina_hupai.size() > 0) {
                    this.hupaiPanel.getChildren().addAll((Node[])new Node[]{label});
                }
                boolean all = true;
                for (Mj mj6 : Config.weina) {
                    if (mj6.getResult() / 10 == this.que) continue;
                    boolean a = false;
                    for (Mj mj12 : this.yina_hupai) {
                        if (mj12.getResult() != mj6.getResult()) continue;
                        a = true;
                        break;
                    }
                    if (a) continue;
                    all = false;
                    break;
                }
                if (all) {
                    ImageView imageView2 = new ImageView();
                    imageView2.setImage(new Image(MainController.class.getResourceAsStream("/quesheng/view/image/all_hu.png")));
                    imageView2.setFitHeight(Config.MJ_HEIGHT_3_4);
                    imageView2.setFitWidth(Config.MJ_HEIGHT_3_4);
                    imageView2.setLayoutY(d);
                    this.hupaiPanel.getChildren().addAll((Node[])new Node[]{imageView2});
                    int size2 = this.yina_hupai.size();
                    for (int i2 = 0; i2 < size2; ++i2) {
                        Mj mj = (Mj)this.yina_hupai.get(i2);
                        Config.yuce.forEach(mj1 -> {
                            if (mj1.getYuce_direction() == this.direction && mj1.getResult() == mj.getResult() && mj1.yuanpai == mj.yuanpai) {
                                mj1.getYuce_imageView().setImage(ImageKit.coverImage(mj1.getYuce_imageView().getImage(), Config.HONGSE));
                            }
                        });
                    }
                } else {
                    int size2 = this.yina_hupai.size();
                    for (int i3 = 0; i3 < size2; ++i3) {
                        Mj mj = (Mj)this.yina_hupai.get(i3);
                        ImageView imageView2 = new ImageView();
                        if (mj.caisheng) {
                            imageView2.setImage(ImageKit.rotate90SX(ImageKit.coverImage(Config.getImageByMj(mj), Config.LANSE)));
                        } else {
                            imageView2.setImage(ImageKit.rotate90SX(Config.getImageByMj(mj)));
                        }
                        imageView2.setFitWidth(Config.MJ_HEIGHT_3_4);
                        imageView2.setFitHeight(Config.MJ_WIDTH_3_4);
                        Text text = new Text(MainController.weina.stream().filter(mj1 -> mj1.getResult() == mj.getResult() && mj1.yuanpai == mj.yuanpai).count() + Config.yina.stream().filter(mj1 -> mj1.getResult() == mj.getResult() && mj1.getNapai_direction() != this.direction && mj1.getPaizhuantai() == 0 && MainController.directions.get(mj1.getNapai_direction()).getHu() == 0 && mj1.yuanpai == mj.yuanpai).count() + "");
                        text.setFont(Font.font("\u5b8b\u4f53", FontWeight.BOLD, 15.0));
                        text.setFill(Color.RED);
                        imageView2.setLayoutY(d + (double)i3 * imageView2.getFitHeight());
                        text.setLayoutY(d + (double)i3 * imageView2.getFitHeight() + imageView2.getFitHeight() - 5.0);
                        text.setLayoutX(5.0);
                        this.hupaiPanel.getChildren().addAll((Node[])new Node[]{imageView2, text});
                        mj.setHupai_imageView(imageView2);
                        Config.yuce.forEach(mj1 -> {
                            if (mj1.getYuce_direction() == this.direction && mj1.getResult() == mj.getResult() && mj1.yuanpai == mj.yuanpai) {
                                mj1.getYuce_imageView().setImage(ImageKit.coverImage(mj1.getYuce_imageView().getImage(), Config.HONGSE));
                            }
                        });
                    }
                }
            } else {
                int n = this.yina_hupai.size();
                for (int i4 = 0; i4 < n; ++i4) {
                    Mj mj = (Mj)this.yina_hupai.get(i4);
                    Config.yuce.forEach(mj1 -> {
                        if (mj1.getYuce_direction() == this.direction && mj1.getResult() == mj.getResult() && mj1.yuanpai == mj.yuanpai) {
                            mj1.getYuce_imageView().setImage(ImageKit.coverImage(mj1.getYuce_imageView().getImage(), Config.HONGSE));
                        }
                    });
                }
            }
            if (Config.systemConfig.queyimeng == 1 && MainController.dapaifangx == this.direction) {
                List list = yina_bottom.stream().filter(mj -> mj.getIsque() && mj.getPaizhuantai() == 0 && !mj.caisheng).collect(Collectors.toList());
                if (list.size() > 0) {
                    Mj mj9 = (Mj)list.get(0);
                    try {
                        robot = new Robot();
                        robot.mouseMove((int)(JFXKit.getScreenX(mj9.getYina_imageView().getImageView()) + mj9.getYina_imageView().getImageView().getFitWidth() / 2.0), (int)(JFXKit.getScreenY(mj9.getYina_imageView().getImageView()) + mj9.getYina_imageView().getImageView().getFitHeight() / 2.0));
                    }
                    catch (AWTException e) {
                        e.printStackTrace();
                    }
                } else {
                    for (int i5 = size = yina_bottom.size() - 1; i5 > 0; --i5) {
                        Mj mj3 = yina_bottom.get(i5);
                        if (!mj3.isLast()) continue;
                        try {
                            Robot robot2 = new Robot();
                            robot2.mouseMove((int)(JFXKit.getScreenX(mj3.getYina_imageView().getImageView()) + mj3.getYina_imageView().getImageView().getFitWidth() / 2.0), (int)(JFXKit.getScreenY(mj3.getYina_imageView().getImageView()) + mj3.getYina_imageView().getImageView().getFitHeight() / 2.0));
                            continue;
                        }
                        catch (AWTException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                int var9_32 = size = yina_bottom.size() - 1;
                while (var9_32 > 0) {
                    Mj mj10 = yina_bottom.get((int)var9_32);
                    if (mj10.isLast()) {
                        try {
                            robot = new Robot();
                            robot.mouseMove((int)(JFXKit.getScreenX(mj10.getYina_imageView().getImageView()) + mj10.getYina_imageView().getImageView().getFitWidth() / 2.0), (int)(JFXKit.getScreenY(mj10.getYina_imageView().getImageView()) + mj10.getYina_imageView().getImageView().getFitHeight() / 2.0));
                        }
                        catch (AWTException e) {
                            e.printStackTrace();
                        }
                    }
                    --var9_32;
                }
            }
        }
    }

    @Override
    public void clear() {
        this.yinaPanel.getChildren().clear();
        this.quePanel.getChildren().clear();
        this.yucePanel.getChildren().clear();
        this.caozuoPanel.getChildren().clear();
    }

    private void initPanel() {
        this.yinaPanel = new AnchorPane();
        this.yucePanel = new VBox();
        this.hupaiPanel = new AnchorPane();
        this.caozuoPanel = new VBox();
        this.houpaiPanel = new VBox();
        this.quePanel = new VBox();
        this.yucePanel.setAlignment(Pos.BOTTOM_LEFT);
        this.houpaiPanel.setAlignment(Pos.BOTTOM_LEFT);
        this.caozuoPanel.setAlignment(Pos.TOP_CENTER);
        this.yinaPanel.setPrefHeight(Config.MJ_WIDTH * (double)(Config.systemConfig.wanjiapaishu + 1));
        this.caozuoPanel.setLayoutX(-Config.MJ_WIDTH - Config.MJ_WIDTH);
        this.houpaiPanel.setSpacing(Config.MJ_HEIGHT_3_4);
        VBox.setVgrow(this.hupaiPanel, Priority.ALWAYS);
        this.houpaiPanel.getChildren().addAll((Node[])new Node[]{this.quePanel, this.hupaiPanel, this.yucePanel});
    }

    private void initContent() {
        this.initPanel();
        HBox vBox = new HBox();
        vBox.setAlignment(Pos.BOTTOM_LEFT);
        vBox.setSpacing(5.0);
        vBox.getChildren().addAll((Node[])new Node[]{this.yinaPanel, this.houpaiPanel});
        this.group.getChildren().addAll((Node[])new Node[]{vBox, this.caozuoPanel});
    }

    @Override
    public AnchorPane getYinaPanel() {
        return this.yinaPanel;
    }

    @Override
    public VBox getYucePanel() {
        return this.yucePanel;
    }

    @Override
    public VBox getCaozuoPanel() {
        return this.caozuoPanel;
    }

    @Override
    public AnchorPane getHupaiPanel() {
        return this.hupaiPanel;
    }
}

