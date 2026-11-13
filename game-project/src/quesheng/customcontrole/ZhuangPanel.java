/*
 * Decompiled with CFR 0.152.
 */
package quesheng.customcontrole;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import quesheng.controller.MainController;
import quesheng.customcontrole.GuopaiTishi;

public final class ZhuangPanel
extends Parent {
    Image center_image = new Image(MainController.class.getResourceAsStream("/quesheng/view/image/center.png"));
    ImageView center_imageview = new ImageView(this.center_image);
    Image nan_image = new Image(MainController.class.getResourceAsStream("/quesheng/view/image/nan.png"));
    Label nan_que = new Label("\u7f3a");
    Label bei_que = new Label("\u7f3a");
    Label xi_que = new Label("\u7f3a");
    Label dong_que = new Label("\u7f3a");
    AnchorPane nan_imageview = new AnchorPane();
    Image bei_image = new Image(MainController.class.getResourceAsStream("/quesheng/view/image/bei.png"));
    AnchorPane bei_imageview = new AnchorPane();
    Image xi_image = new Image(MainController.class.getResourceAsStream("/quesheng/view/image/xi.png"));
    AnchorPane xi_imageview = new AnchorPane();
    Image dong_image = new Image(MainController.class.getResourceAsStream("/quesheng/view/image/dong.png"));
    AnchorPane dong_imageview = new AnchorPane();
    Text top_text = new Text("\u8bf7\u9009\u62e9\u5e84\u5bb6");
    List<AnchorPane> imageViews = new ArrayList<AnchorPane>();
    List<Label> leabels = new ArrayList<Label>();
    Group xuanzhuang = new Group();
    GuopaiTishi guopai = new GuopaiTishi();

    public double getHeight() {
        return this.center_image.getHeight();
    }

    public double getWidth() {
        return this.center_image.getWidth();
    }

    public AnchorPane getNan_imageview() {
        return this.nan_imageview;
    }

    public AnchorPane getBei_imageview() {
        return this.bei_imageview;
    }

    public AnchorPane getXi_imageview() {
        return this.xi_imageview;
    }

    public AnchorPane getDong_imageview() {
        return this.dong_imageview;
    }

    public Label getNan_que() {
        return this.nan_que;
    }

    public Label getBei_que() {
        return this.bei_que;
    }

    public Label getXi_que() {
        return this.xi_que;
    }

    public Label getDong_que() {
        return this.dong_que;
    }

    public Text getTop_text() {
        return this.top_text;
    }

    public void setGuopai() {
        this.xuanzhuang.setVisible(false);
        this.guopai.setVisible(true);
    }

    public void setXuanZhuang() {
        this.xuanzhuang.setVisible(true);
        this.guopai.setVisible(false);
    }

    public void escGuopai() {
        this.guopai.setVisible(false);
    }

    public ZhuangPanel() {
        this.dong_imageview.getChildren().add(new ImageView(this.dong_image));
        this.imageViews.add(this.dong_imageview);
        this.nan_imageview.getChildren().add(new ImageView(this.nan_image));
        this.imageViews.add(this.nan_imageview);
        this.xi_imageview.getChildren().add(new ImageView(this.xi_image));
        this.imageViews.add(this.xi_imageview);
        this.bei_imageview.getChildren().add(new ImageView(this.bei_image));
        this.imageViews.add(this.bei_imageview);
        this.dong_que.setPrefHeight(50.0);
        this.dong_que.setPrefWidth(50.0);
        this.dong_que.setAlignment(Pos.CENTER);
        this.nan_que.setPrefHeight(50.0);
        this.nan_que.setPrefWidth(50.0);
        this.nan_que.setAlignment(Pos.CENTER);
        this.xi_que.setPrefHeight(50.0);
        this.xi_que.setPrefWidth(50.0);
        this.xi_que.setAlignment(Pos.CENTER);
        this.bei_que.setPrefHeight(50.0);
        this.bei_que.setPrefWidth(50.0);
        this.bei_que.setAlignment(Pos.CENTER);
        this.leabels.add(this.dong_que);
        this.leabels.add(this.nan_que);
        this.leabels.add(this.xi_que);
        this.leabels.add(this.bei_que);
        this.leabels.forEach(label -> {
            label.setVisible(false);
            label.setStyle("-fx-font-size: 50px;-fx-font-weight: 900;");
        });
        AnchorPane main_panel = new AnchorPane();
        this.top_text.setFont(Font.font("\u5b8b\u4f53", FontWeight.BOLD, 25.0));
        this.top_text.setFill(Color.RED);
        this.top_text.setTextAlignment(TextAlignment.CENTER);
        this.top_text.setLayoutY(-25.0);
        this.top_text.setLayoutX(this.getWidth() / 2.0 - 60.0);
        main_panel.getChildren().add(this.center_imageview);
        main_panel.getChildren().add(this.top_text);
        this.imageViews.get(MainController.bottomPanel.direction).setLayoutY(this.getHeight() - 50.0);
        this.imageViews.get(MainController.bottomPanel.direction).setLayoutX(this.getWidth() / 2.0 - 25.0);
        this.imageViews.get(MainController.bottomPanel.direction).setCursor(Cursor.HAND);
        this.imageViews.get(MainController.bottomPanel.direction).setUserData(MainController.bottomPanel.direction);
        this.leabels.get(MainController.bottomPanel.direction).setLayoutY(this.getHeight() - 50.0);
        this.leabels.get(MainController.bottomPanel.direction).setLayoutX(this.getWidth() / 2.0 - 25.0);
        this.leabels.get(MainController.bottomPanel.direction).setCursor(Cursor.HAND);
        this.leabels.get(MainController.bottomPanel.direction).setUserData(MainController.bottomPanel.direction);
        main_panel.getChildren().add(this.imageViews.get(MainController.bottomPanel.direction));
        main_panel.getChildren().add(this.leabels.get(MainController.bottomPanel.direction));
        this.imageViews.get(MainController.topPanel.direction).setLayoutX(this.getWidth() / 2.0 - 25.0);
        this.imageViews.get(MainController.topPanel.direction).setCursor(Cursor.HAND);
        this.imageViews.get(MainController.topPanel.direction).setUserData(MainController.topPanel.direction);
        this.leabels.get(MainController.topPanel.direction).setLayoutX(this.getWidth() / 2.0 - 25.0);
        this.leabels.get(MainController.topPanel.direction).setCursor(Cursor.HAND);
        this.leabels.get(MainController.topPanel.direction).setUserData(MainController.topPanel.direction);
        main_panel.getChildren().add(this.imageViews.get(MainController.topPanel.direction));
        main_panel.getChildren().add(this.leabels.get(MainController.topPanel.direction));
        this.imageViews.get(MainController.leftPanel.direction).setLayoutY(this.getHeight() / 2.0 - 25.0);
        this.imageViews.get(MainController.leftPanel.direction).setCursor(Cursor.HAND);
        this.imageViews.get(MainController.leftPanel.direction).setUserData(MainController.leftPanel.direction);
        this.leabels.get(MainController.leftPanel.direction).setLayoutY(this.getHeight() / 2.0 - 25.0);
        this.leabels.get(MainController.leftPanel.direction).setCursor(Cursor.HAND);
        this.leabels.get(MainController.leftPanel.direction).setUserData(MainController.leftPanel.direction);
        main_panel.getChildren().add(this.imageViews.get(MainController.leftPanel.direction));
        main_panel.getChildren().add(this.leabels.get(MainController.leftPanel.direction));
        this.imageViews.get(MainController.rightPanel.direction).setLayoutY(this.getHeight() / 2.0 - 25.0);
        this.imageViews.get(MainController.rightPanel.direction).setLayoutX(this.getWidth() - 50.0);
        this.imageViews.get(MainController.rightPanel.direction).setCursor(Cursor.HAND);
        this.imageViews.get(MainController.rightPanel.direction).setUserData(MainController.rightPanel.direction);
        this.leabels.get(MainController.rightPanel.direction).setLayoutY(this.getHeight() / 2.0 - 25.0);
        this.leabels.get(MainController.rightPanel.direction).setLayoutX(this.getWidth() - 50.0);
        this.leabels.get(MainController.rightPanel.direction).setCursor(Cursor.HAND);
        this.leabels.get(MainController.rightPanel.direction).setUserData(MainController.rightPanel.direction);
        main_panel.getChildren().add(this.imageViews.get(MainController.rightPanel.direction));
        main_panel.getChildren().add(this.leabels.get(MainController.rightPanel.direction));
        this.xuanzhuang.getChildren().add(main_panel);
        this.guopai.setVisible(false);
        this.getChildren().addAll((Node[])new Node[]{this.xuanzhuang, this.guopai});
    }
}

