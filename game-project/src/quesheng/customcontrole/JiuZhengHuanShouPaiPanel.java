/*
 * Decompiled with CFR 0.152.
 */
package quesheng.customcontrole;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public final class JiuZhengHuanShouPaiPanel
extends AnchorPane {
    Button quxiaojiaohuan = new Button("\u53d6\u6d88");
    Button querengjiaohuan = new Button("\u786e\u8ba4");
    Text top_text;

    public Button getQuxiaojiuzheng() {
        return this.quxiaojiaohuan;
    }

    public Button getQuerengjiuzheng() {
        return this.querengjiaohuan;
    }

    public Text getTop_text() {
        return this.top_text;
    }

    public JiuZhengHuanShouPaiPanel() {
        Group group = new Group();
        VBox main_panel = new VBox();
        main_panel.setAlignment(Pos.CENTER_LEFT);
        this.top_text = new Text("\u7ea0\u6b63-\u6362\u624b\u724c");
        this.top_text.setFont(Font.font("\u5b8b\u4f53", FontWeight.BOLD, 20.0));
        this.top_text.setFill(Color.RED);
        this.top_text.setTextAlignment(TextAlignment.CENTER);
        main_panel.getChildren().add(this.top_text);
        main_panel.getChildren().add(this.quxiaojiaohuan);
        main_panel.getChildren().add(this.querengjiaohuan);
        main_panel.setStyle("-fx-background-color:white;-fx-padding: 20px");
        main_panel.setSpacing(20.0);
        group.getChildren().add(main_panel);
        this.getChildren().add(group);
    }
}

