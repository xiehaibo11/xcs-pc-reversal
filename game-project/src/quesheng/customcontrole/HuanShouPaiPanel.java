/*
 * Decompiled with CFR 0.152.
 */
package quesheng.customcontrole;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public final class HuanShouPaiPanel
extends AnchorPane {
    RadioButton naxianghjipai_xunhuan = new RadioButton("\u62ff\u4e0a\u5bb6\u724c\u5faa\u73af");
    RadioButton nashangjiapai_xunhuan = new RadioButton("\u62ff\u4e0b\u5bb6\u724c\u5faa\u73af");
    RadioButton naxiajiapan = new RadioButton("\u62ff\u4e0b\u5bb6\u724c");
    RadioButton nashangjiapai = new RadioButton("\u62ff\u4e0a\u5bb6\u724c");
    RadioButton naduijiapai = new RadioButton("\u62ff\u5bf9\u5bb6\u724c");
    RadioButton quxiaojiaohuan = new RadioButton("\u53d6\u6d88\u4ea4\u6362\u724c");
    ToggleGroup radiogroup = new ToggleGroup();
    Text top_text;

    public RadioButton getNaxianghjipai_xunhuan() {
        return this.naxianghjipai_xunhuan;
    }

    public RadioButton getNashangjiapai_xunhuan() {
        return this.nashangjiapai_xunhuan;
    }

    public RadioButton getNaxiajiapan() {
        return this.naxiajiapan;
    }

    public RadioButton getNaduijiapai() {
        return this.naduijiapai;
    }

    public RadioButton getQuxiaojiaohuan() {
        return this.quxiaojiaohuan;
    }

    public ToggleGroup getRadiogroup() {
        return this.radiogroup;
    }

    public Text getTop_text() {
        return this.top_text;
    }

    public RadioButton getNashangjiapai() {
        return this.nashangjiapai;
    }

    public HuanShouPaiPanel() {
        Group group = new Group();
        VBox main_panel = new VBox();
        main_panel.setAlignment(Pos.CENTER_LEFT);
        this.top_text = new Text("\u4ea4\u6362\u624b\u724c(0)\u5f20");
        this.top_text.setFont(Font.font("\u5b8b\u4f53", FontWeight.BOLD, 20.0));
        this.top_text.setFill(Color.RED);
        this.top_text.setTextAlignment(TextAlignment.CENTER);
        main_panel.getChildren().add(this.top_text);
        main_panel.getChildren().add(this.nashangjiapai_xunhuan);
        main_panel.getChildren().add(this.naxianghjipai_xunhuan);
        main_panel.getChildren().add(this.nashangjiapai);
        main_panel.getChildren().add(this.naxiajiapan);
        main_panel.getChildren().add(this.naduijiapai);
        main_panel.getChildren().add(this.quxiaojiaohuan);
        main_panel.setStyle("-fx-background-color:white;-fx-padding: 20px");
        main_panel.setSpacing(20.0);
        group.getChildren().add(main_panel);
        this.getChildren().add(group);
    }
}

