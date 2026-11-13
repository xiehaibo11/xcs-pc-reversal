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

public class CaiShengXuanZeDialog
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
        int i;
        this.caisheng_h_1.setPrefHeight(Config.MJ_HEIGHT_1_2);
        this.caisheng_h_2.setPrefHeight(Config.MJ_HEIGHT_1_2);
        this.caisheng_h_3.setPrefHeight(Config.MJ_HEIGHT_1_2);
        this.caisheng_h_4.setPrefHeight(Config.MJ_HEIGHT_1_2);
        this.caisheng_h_5.setPrefHeight(Config.MJ_HEIGHT_1_2);
        for (i = 1; i <= 9; ++i) {
            VBox group = new VBox();
            imageView = new ImageView(Config.getImageByNumber(i));
            group.getChildren().add(imageView);
            imageView.setFitWidth(Config.MJ_WIDTH_1_2);
            imageView.setFitHeight(Config.MJ_HEIGHT_1_2);
            group.setCursor(Cursor.HAND);
            this.caisheng_h_1.getChildren().add(group);
            Integer finalI = i;
            imageView.setOnMouseClicked(event -> {
                if (MainController.caisheng.contains(finalI)) {
                    group.getStyleClass().clear();
                    MainController.caisheng.remove(finalI);
                } else {
                    group.getStyleClass().add("mj_clicked");
                    MainController.caisheng.add(finalI);
                }
            });
            this.caisheng_list.add(group);
        }
        for (i = 11; i <= 19; ++i) {
            VBox group = new VBox();
            imageView = new ImageView(Config.getImageByNumber(i));
            imageView.setFitWidth(Config.MJ_WIDTH_1_2);
            imageView.setFitHeight(Config.MJ_HEIGHT_1_2);
            group.getChildren().add(imageView);
            group.setCursor(Cursor.HAND);
            this.caisheng_h_2.getChildren().add(group);
            Integer finalI = i;
            imageView.setOnMouseClicked(event -> {
                if (MainController.caisheng.contains(finalI)) {
                    group.getStyleClass().clear();
                    MainController.caisheng.remove(finalI);
                } else {
                    group.getStyleClass().add("mj_clicked");
                    MainController.caisheng.add(finalI);
                }
            });
            this.caisheng_list.add(group);
        }
        for (i = 21; i <= 29; ++i) {
            VBox group = new VBox();
            imageView = new ImageView(Config.getImageByNumber(i));
            imageView.setFitWidth(Config.MJ_WIDTH_1_2);
            imageView.setFitHeight(Config.MJ_HEIGHT_1_2);
            group.getChildren().add(imageView);
            group.setCursor(Cursor.HAND);
            this.caisheng_h_3.getChildren().add(group);
            Integer finalI = i;
            imageView.setOnMouseClicked(event -> {
                if (MainController.caisheng.contains(finalI)) {
                    group.getStyleClass().clear();
                    MainController.caisheng.remove(finalI);
                } else {
                    group.getStyleClass().add("mj_clicked");
                    MainController.caisheng.add(finalI);
                }
            });
            this.caisheng_list.add(group);
        }
        for (i = 31; i <= 37; ++i) {
            VBox group = new VBox();
            imageView = new ImageView(Config.getImageByNumber(i));
            imageView.setFitWidth(Config.MJ_WIDTH_1_2);
            imageView.setFitHeight(Config.MJ_HEIGHT_1_2);
            group.getChildren().add(imageView);
            group.setCursor(Cursor.HAND);
            this.caisheng_h_4.getChildren().add(group);
            Integer finalI = i;
            imageView.setOnMouseClicked(event -> {
                if (MainController.caisheng.contains(finalI)) {
                    group.getStyleClass().clear();
                    MainController.caisheng.remove(finalI);
                } else {
                    group.getStyleClass().add("mj_clicked");
                    MainController.caisheng.add(finalI);
                }
            });
            this.caisheng_list.add(group);
        }
        for (i = 41; i <= 48; ++i) {
            VBox group = new VBox();
            imageView = new ImageView(Config.getImageByNumber(i));
            imageView.setFitWidth(Config.MJ_WIDTH_1_2);
            imageView.setFitHeight(Config.MJ_HEIGHT_1_2);
            group.getChildren().add(imageView);
            group.setCursor(Cursor.HAND);
            this.caisheng_h_4.getChildren().add(group);
            Integer finalI = i;
            imageView.setOnMouseClicked(event -> {
                if (MainController.caisheng.contains(finalI)) {
                    group.getStyleClass().clear();
                    MainController.caisheng.remove(finalI);
                } else {
                    group.getStyleClass().add("mj_clicked");
                    MainController.caisheng.add(finalI);
                }
            });
            this.caisheng_list.add(group);
        }
        if (MainController.meijuchongshecaisheng == 1) {
            this.caisheng_meijuchongshe.setSelected(true);
        }
        this.caisheng_meijuchongshe.selectedProperty().addListener((observable, oldValue, newValue) -> {
            MainController.meijuchongshecaisheng = newValue != false ? 1 : 0;
        });
        this.caisheng_ok_btn.setOnAction(event -> {
            if (MainController.caisheng.size() == 0) {
                CaiShengXuanZeDialog.showTextDialog("\u8bf7\u9009\u62e9\u8d22\u795e");
            } else {
                MainController.caisheng_seting_popup.close();
            }
        });
        this.caisheng_cancle_btn.setOnAction(event -> MainController.caisheng_seting_popup.close());
    }

    public void clear() {
        this.caisheng_list.forEach(vBox -> vBox.getStyleClass().clear());
    }
}

