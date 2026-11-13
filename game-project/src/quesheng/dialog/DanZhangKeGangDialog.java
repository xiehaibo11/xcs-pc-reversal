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

public class DanZhangKeGangDialog
extends BaseController
implements Initializable {
    @FXML
    HBox caisheng_h_1;
    @FXML
    HBox caisheng_h_2;
    @FXML
    HBox caisheng_h_3;
    @FXML
    HBox caisheng_h_4;
    @FXML
    HBox caisheng_h_5;
    @FXML
    Button caisheng_ok_btn;
    @FXML
    Button caisheng_cancle_btn;
    @FXML
    CheckBox caisheng_meijuchongshe;
    private List<VBox> caisheng_list = new ArrayList<VBox>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ImageView imageView;
        this.caisheng_h_1.setPrefHeight(Config.MJ_HEIGHT_1_2);
        this.caisheng_h_2.setPrefHeight(Config.MJ_HEIGHT_1_2);
        this.caisheng_h_3.setPrefHeight(Config.MJ_HEIGHT_1_2);
        this.caisheng_h_4.setPrefHeight(Config.MJ_HEIGHT_1_2);
        this.caisheng_h_5.setPrefHeight(Config.MJ_HEIGHT_1_2);
        int i = 1;
        while (i <= 9) {
            VBox group = new VBox();
            imageView = new ImageView(Config.getImageByNumber(i));
            group.getChildren().add(imageView);
            imageView.setFitWidth(Config.MJ_WIDTH_1_2);
            imageView.setFitHeight(Config.MJ_HEIGHT_1_2);
            group.setCursor(Cursor.HAND);
            this.caisheng_h_1.getChildren().add(group);
            int finalI = i++;
            imageView.setOnMouseClicked(event -> {
                if (Config.systemConfig.danzhangkegang_pai.contains(finalI)) {
                    group.getStyleClass().clear();
                    Config.systemConfig.danzhangkegang_pai.remove(finalI);
                } else {
                    group.getStyleClass().add("mj_clicked");
                    Config.systemConfig.danzhangkegang_pai.add(finalI);
                }
            });
            this.caisheng_list.add(group);
        }
        i = 11;
        while (i <= 19) {
            VBox group = new VBox();
            imageView = new ImageView(Config.getImageByNumber(i));
            imageView.setFitWidth(Config.MJ_WIDTH_1_2);
            imageView.setFitHeight(Config.MJ_HEIGHT_1_2);
            group.getChildren().add(imageView);
            group.setCursor(Cursor.HAND);
            this.caisheng_h_2.getChildren().add(group);
            int finalI = i++;
            imageView.setOnMouseClicked(event -> {
                if (Config.systemConfig.danzhangkegang_pai.contains(finalI)) {
                    group.getStyleClass().clear();
                    Config.systemConfig.danzhangkegang_pai.remove(finalI);
                } else {
                    group.getStyleClass().add("mj_clicked");
                    Config.systemConfig.danzhangkegang_pai.add(finalI);
                }
            });
            this.caisheng_list.add(group);
        }
        i = 21;
        while (i <= 29) {
            VBox group = new VBox();
            imageView = new ImageView(Config.getImageByNumber(i));
            imageView.setFitWidth(Config.MJ_WIDTH_1_2);
            imageView.setFitHeight(Config.MJ_HEIGHT_1_2);
            group.getChildren().add(imageView);
            group.setCursor(Cursor.HAND);
            this.caisheng_h_3.getChildren().add(group);
            int finalI = i++;
            imageView.setOnMouseClicked(event -> {
                if (Config.systemConfig.danzhangkegang_pai.contains(finalI)) {
                    group.getStyleClass().clear();
                    Config.systemConfig.danzhangkegang_pai.remove(finalI);
                } else {
                    group.getStyleClass().add("mj_clicked");
                    Config.systemConfig.danzhangkegang_pai.add(finalI);
                }
            });
            this.caisheng_list.add(group);
        }
        i = 31;
        while (i <= 37) {
            VBox group = new VBox();
            imageView = new ImageView(Config.getImageByNumber(i));
            imageView.setFitWidth(Config.MJ_WIDTH_1_2);
            imageView.setFitHeight(Config.MJ_HEIGHT_1_2);
            group.getChildren().add(imageView);
            group.setCursor(Cursor.HAND);
            this.caisheng_h_4.getChildren().add(group);
            int finalI = i++;
            imageView.setOnMouseClicked(event -> {
                if (Config.systemConfig.danzhangkegang_pai.contains(finalI)) {
                    group.getStyleClass().clear();
                    Config.systemConfig.danzhangkegang_pai.remove(finalI);
                } else {
                    group.getStyleClass().add("mj_clicked");
                    Config.systemConfig.danzhangkegang_pai.add(finalI);
                }
            });
            this.caisheng_list.add(group);
        }
        i = 41;
        while (i <= 48) {
            VBox group = new VBox();
            imageView = new ImageView(Config.getImageByNumber(i));
            imageView.setFitWidth(Config.MJ_WIDTH_1_2);
            imageView.setFitHeight(Config.MJ_HEIGHT_1_2);
            group.getChildren().add(imageView);
            group.setCursor(Cursor.HAND);
            this.caisheng_h_4.getChildren().add(group);
            int finalI = i++;
            imageView.setOnMouseClicked(event -> {
                if (Config.systemConfig.danzhangkegang_pai.contains(finalI)) {
                    group.getStyleClass().clear();
                    Config.systemConfig.danzhangkegang_pai.remove(finalI);
                } else {
                    group.getStyleClass().add("mj_clicked");
                    Config.systemConfig.danzhangkegang_pai.add(finalI);
                }
            });
            this.caisheng_list.add(group);
        }
        if (MainController.meijuchongshedanzhangkegang == 1) {
            this.caisheng_meijuchongshe.setSelected(true);
        }
        this.caisheng_meijuchongshe.selectedProperty().addListener((observable, oldValue, newValue) -> {
            MainController.meijuchongshedanzhangkegang = newValue != false ? 1 : 0;
        });
        this.caisheng_ok_btn.setOnAction(event -> {
            if (Config.systemConfig.danzhangkegang_pai.size() == 0) {
                DanZhangKeGangDialog.showTextDialog("\u8bf7\u9009\u62e9\u5355\u5f20\u53ef\u6760\u7684\u724c");
            } else {
                MainController.danzhangkegang_seting_popup.close();
            }
        });
        this.caisheng_cancle_btn.setOnAction(event -> MainController.danzhangkegang_seting_popup.close());
    }
}

