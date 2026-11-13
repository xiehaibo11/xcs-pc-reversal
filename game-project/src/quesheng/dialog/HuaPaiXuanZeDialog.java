/*
 * Decompiled with CFR 0.152.
 */
package quesheng.dialog;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import quesheng.controller.BaseController;
import quesheng.controller.MainController;
import quesheng.util.Config;

public class HuaPaiXuanZeDialog
extends BaseController
implements Initializable {
    @FXML
    HBox huapai_h_1;
    @FXML
    HBox huapai_h_2;
    @FXML
    HBox huapai_h_3;
    @FXML
    HBox huapai_h_4;
    @FXML
    HBox huapai_h_5;
    @FXML
    Button huapai_ok_btn;
    @FXML
    Button huapai_cancle_btn;
    @FXML
    CheckBox huapai_meijuchongshe;
    private List<VBox> huapai_list = new ArrayList<VBox>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ImageView imageView;
        int i;
        this.huapai_h_1.setPrefHeight(Config.MJ_HEIGHT_1_2);
        this.huapai_h_2.setPrefHeight(Config.MJ_HEIGHT_1_2);
        this.huapai_h_3.setPrefHeight(Config.MJ_HEIGHT_1_2);
        this.huapai_h_4.setPrefHeight(Config.MJ_HEIGHT_1_2);
        this.huapai_h_5.setPrefHeight(Config.MJ_HEIGHT_1_2);
        for (i = 1; i <= 9; ++i) {
            VBox group = new VBox();
            imageView = new ImageView(Config.getImageByNumber(i));
            group.getChildren().add(imageView);
            imageView.setFitWidth(Config.MJ_WIDTH_1_2);
            imageView.setFitHeight(Config.MJ_HEIGHT_1_2);
            group.setCursor(Cursor.HAND);
            this.huapai_h_1.getChildren().add(group);
            Integer finalI = i;
            imageView.setOnMouseClicked(event -> {
                if (MainController.zixuanhuapai.contains(finalI)) {
                    group.getStyleClass().clear();
                    MainController.zixuanhuapai.remove(finalI);
                } else {
                    group.getStyleClass().add("mj_clicked");
                    MainController.zixuanhuapai.add(finalI);
                }
            });
            this.huapai_list.add(group);
        }
        for (i = 11; i <= 19; ++i) {
            VBox group = new VBox();
            imageView = new ImageView(Config.getImageByNumber(i));
            imageView.setFitWidth(Config.MJ_WIDTH_1_2);
            imageView.setFitHeight(Config.MJ_HEIGHT_1_2);
            group.getChildren().add(imageView);
            group.setCursor(Cursor.HAND);
            this.huapai_h_2.getChildren().add(group);
            Integer finalI = i;
            imageView.setOnMouseClicked(event -> {
                if (MainController.zixuanhuapai.contains(finalI)) {
                    group.getStyleClass().clear();
                    MainController.zixuanhuapai.remove(finalI);
                } else {
                    group.getStyleClass().add("mj_clicked");
                    MainController.zixuanhuapai.add(finalI);
                }
            });
            this.huapai_list.add(group);
        }
        for (i = 21; i <= 29; ++i) {
            VBox group = new VBox();
            imageView = new ImageView(Config.getImageByNumber(i));
            imageView.setFitWidth(Config.MJ_WIDTH_1_2);
            imageView.setFitHeight(Config.MJ_HEIGHT_1_2);
            group.getChildren().add(imageView);
            group.setCursor(Cursor.HAND);
            this.huapai_h_3.getChildren().add(group);
            Integer finalI = i;
            imageView.setOnMouseClicked(event -> {
                if (MainController.zixuanhuapai.contains(finalI)) {
                    group.getStyleClass().clear();
                    MainController.zixuanhuapai.remove(finalI);
                } else {
                    group.getStyleClass().add("mj_clicked");
                    MainController.zixuanhuapai.add(finalI);
                }
            });
            this.huapai_list.add(group);
        }
        for (i = 31; i <= 37; ++i) {
            VBox group = new VBox();
            imageView = new ImageView(Config.getImageByNumber(i));
            imageView.setFitWidth(Config.MJ_WIDTH_1_2);
            imageView.setFitHeight(Config.MJ_HEIGHT_1_2);
            group.getChildren().add(imageView);
            group.setCursor(Cursor.HAND);
            this.huapai_h_4.getChildren().add(group);
            Integer finalI = i;
            imageView.setOnMouseClicked(event -> {
                if (MainController.zixuanhuapai.contains(finalI)) {
                    group.getStyleClass().clear();
                    MainController.zixuanhuapai.remove(finalI);
                } else {
                    group.getStyleClass().add("mj_clicked");
                    MainController.zixuanhuapai.add(finalI);
                }
            });
            this.huapai_list.add(group);
        }
        for (i = 41; i <= 48; ++i) {
            VBox group = new VBox();
            imageView = new ImageView(Config.getImageByNumber(i));
            imageView.setFitWidth(Config.MJ_WIDTH_1_2);
            imageView.setFitHeight(Config.MJ_HEIGHT_1_2);
            group.getChildren().add(imageView);
            group.setCursor(Cursor.HAND);
            this.huapai_h_4.getChildren().add(group);
            Integer finalI = i;
            imageView.setOnMouseClicked(event -> {
                if (MainController.zixuanhuapai.contains(finalI)) {
                    group.getStyleClass().clear();
                    MainController.zixuanhuapai.remove(finalI);
                } else {
                    group.getStyleClass().add("mj_clicked");
                    MainController.zixuanhuapai.add(finalI);
                }
            });
            this.huapai_list.add(group);
        }
        if (MainController.meijuchongshehuapai == 1) {
            this.huapai_meijuchongshe.setSelected(true);
        }
        this.huapai_meijuchongshe.selectedProperty().addListener((observable, oldValue, newValue) -> {
            MainController.meijuchongshehuapai = newValue != false ? 1 : 0;
        });
        this.huapai_ok_btn.setOnAction(event -> {
            if (MainController.zixuanhuapai.size() < 1) {
                HuaPaiXuanZeDialog.showTextDialog("\u8bf7\u9009\u62e9\u82b1\u724c");
            } else {
                this.huapai_list.forEach(m -> m.getStyleClass().clear());
                MainController.zixuanhuapai_seting_popup.close();
            }
        });
        this.huapai_cancle_btn.setOnAction(event -> {
            this.huapai_list.forEach(m -> m.getStyleClass().clear());
            MainController.zixuanhuapai_seting_popup.close();
        });
    }
}

