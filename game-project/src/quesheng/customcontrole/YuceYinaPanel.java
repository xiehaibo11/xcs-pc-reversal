/*
 * Decompiled with CFR 0.152.
 */
package quesheng.customcontrole;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import quesheng.controller.MainController;
import quesheng.model.Mj;
import quesheng.util.Config;

public final class YuceYinaPanel
extends Parent {
    HBox wan = new HBox();
    HBox tiao = new HBox();
    HBox tong = new HBox();
    Text top_text = new Text("\u9884\u6d4b\u724c\u548c\u624b\u4e0a\u724c\u663e\u793a");
    Label dong_text = new Label("\u4e1c");
    Label nan_text = new Label("\u5357");
    Label xi_text = new Label("\u897f");
    Label bei_text = new Label("\u5317");
    int direction = 0;
    List<Mj> mjs = new ArrayList<Mj>();

    public Text getTop_text() {
        return this.top_text;
    }

    public YuceYinaPanel() {
        Group group = new Group();
        VBox main_panel = new VBox();
        this.top_text.setFont(Font.font("\u5b8b\u4f53", FontWeight.BOLD, 18.0));
        this.top_text.setFill(Color.RED);
        this.top_text.setTextAlignment(TextAlignment.CENTER);
        this.dong_text.setFont(Font.font("\u5b8b\u4f53", FontWeight.BOLD, 30.0));
        this.nan_text.setFont(Font.font("\u5b8b\u4f53", FontWeight.BOLD, 30.0));
        this.xi_text.setFont(Font.font("\u5b8b\u4f53", FontWeight.BOLD, 30.0));
        this.bei_text.setFont(Font.font("\u5b8b\u4f53", FontWeight.BOLD, 30.0));
        main_panel.setAlignment(Pos.CENTER);
        main_panel.setStyle("-fx-background-color:#eee;");
        main_panel.getChildren().add(this.top_text);
        main_panel.getChildren().add(this.wan);
        main_panel.getChildren().add(this.tiao);
        main_panel.getChildren().add(this.tong);
        HBox bottom = new HBox();
        this.dong_text.setStyle("-fx-background-color:#222;-fx-text-fill: white;");
        bottom.getChildren().add(this.dong_text);
        this.dong_text.setOnMouseClicked(event -> {
            this.nan_text.setStyle("");
            this.xi_text.setStyle("");
            this.bei_text.setStyle("");
            this.dong_text.setStyle("-fx-background-color:#222;-fx-text-fill: white;");
            this.direction = 0;
            this.display();
        });
        bottom.getChildren().add(this.nan_text);
        this.nan_text.setOnMouseClicked(event -> {
            this.nan_text.setStyle("-fx-background-color:#222;-fx-text-fill: white;");
            this.xi_text.setStyle("");
            this.bei_text.setStyle("");
            this.dong_text.setStyle("");
            this.direction = 1;
            this.display();
        });
        bottom.getChildren().add(this.xi_text);
        this.xi_text.setOnMouseClicked(event -> {
            this.nan_text.setStyle("");
            this.xi_text.setStyle("-fx-background-color:#222;-fx-text-fill: white;");
            this.bei_text.setStyle("");
            this.dong_text.setStyle("");
            this.direction = 2;
            this.display();
        });
        bottom.getChildren().add(this.bei_text);
        this.bei_text.setOnMouseClicked(event -> {
            this.nan_text.setStyle("");
            this.xi_text.setStyle("");
            this.bei_text.setStyle("-fx-background-color:#222;-fx-text-fill: white;");
            this.dong_text.setStyle("");
            this.direction = 3;
            this.display();
        });
        bottom.setAlignment(Pos.BOTTOM_CENTER);
        bottom.setSpacing(40.0);
        main_panel.getChildren().add(bottom);
        group.getChildren().add(main_panel);
        this.getChildren().add(group);
    }

    public void setMjs(List<Mj> mjs) {
        this.mjs.clear();
        this.mjs.addAll(mjs);
    }

    public void display() {
        this.wan.getChildren().clear();
        this.tiao.getChildren().clear();
        this.tong.getChildren().clear();
        List<Mj> yuce = this.mjs.stream().filter(mj -> mj.getNapai_direction() == this.direction || mj.getYuce_direction() == this.direction).collect(Collectors.toList());
        Collections.sort(yuce, new MainController.ComparatorMjResult());
        yuce.stream().forEach(mj -> {
            if (mj.getResult() < 10) {
                ImageView imageView = new ImageView();
                imageView.setImage(Config.getImageByMj(mj));
                imageView.setFitHeight(Config.MJ_HEIGHT_1_2);
                imageView.setFitWidth(Config.MJ_WIDTH_1_2);
                this.wan.getChildren().add(imageView);
            } else if (mj.getResult() < 20) {
                ImageView imageView = new ImageView();
                imageView.setImage(Config.getImageByMj(mj));
                imageView.setFitHeight(Config.MJ_HEIGHT_1_2);
                imageView.setFitWidth(Config.MJ_WIDTH_1_2);
                this.tiao.getChildren().add(imageView);
            } else if (mj.getResult() < 30) {
                ImageView imageView = new ImageView();
                imageView.setImage(Config.getImageByMj(mj));
                imageView.setFitHeight(Config.MJ_HEIGHT_1_2);
                imageView.setFitWidth(Config.MJ_WIDTH_1_2);
                this.tong.getChildren().add(imageView);
            }
        });
    }
}

