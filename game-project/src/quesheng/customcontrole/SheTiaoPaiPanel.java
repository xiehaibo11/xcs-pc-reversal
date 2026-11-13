/*
 * Decompiled with CFR 0.152.
 */
package quesheng.customcontrole;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public final class SheTiaoPaiPanel
extends AnchorPane {
    RadioButton getiao = new RadioButton("\u9694\u8df3");
    RadioButton pingtiao = new RadioButton("\u5e73\u8df3");
    RadioButton shangxiatiao = new RadioButton("\u4e0a\u4e0b\u8df3");
    ToggleGroup radiogroup = new ToggleGroup();
    Text top_text;

    public RadioButton getGetiao() {
        return this.getiao;
    }

    public RadioButton getPingtiao() {
        return this.pingtiao;
    }

    public RadioButton getShangxiatiao() {
        return this.shangxiatiao;
    }

    public ToggleGroup getRadiogroup() {
        return this.radiogroup;
    }

    public Text getTop_text() {
        return this.top_text;
    }

    public SheTiaoPaiPanel() {
        Group group = new Group();
        VBox main_panel = new VBox();
        main_panel.setAlignment(Pos.CENTER);
        this.top_text = new Text("\u9009\u62e9\u8df3\u724c");
        this.top_text.setFont(Font.font("\u5b8b\u4f53", FontWeight.BOLD, 20.0));
        this.top_text.setFill(Color.RED);
        this.top_text.setTextAlignment(TextAlignment.CENTER);
        main_panel.getChildren().add(this.top_text);
        HBox hBox = new HBox();
        hBox.getChildren().add(this.pingtiao);
        hBox.getChildren().add(this.getiao);
        hBox.getChildren().add(this.shangxiatiao);
        hBox.setSpacing(20.0);
        main_panel.getChildren().add(hBox);
        main_panel.setStyle("-fx-background-color:white;-fx-padding: 20px");
        main_panel.setSpacing(20.0);
        group.getChildren().add(main_panel);
        this.getChildren().add(group);
    }
}

