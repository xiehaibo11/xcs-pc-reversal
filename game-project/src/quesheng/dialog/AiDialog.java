/*
 * Decompiled with CFR 0.152.
 */
package quesheng.dialog;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import quesheng.controller.BaseController;
import quesheng.controller.MainController;

public class AiDialog
extends BaseController
implements Initializable {
    @FXML
    CheckBox kaiqiai;
    @FXML
    ComboBox zhihuifangxiang;
    @FXML
    ComboBox dafa;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (MainController.kaiqiai == 1) {
            this.kaiqiai.setSelected(true);
        }
        this.kaiqiai.selectedProperty().addListener((observable, oldValue, newValue) -> {
            MainController.kaiqiai = newValue != false ? 1 : 0;
        });
        this.dafa.getItems().addAll("\u8840\u6218");
        this.dafa.valueProperty().addListener((observable, oldValue, newValue) -> {
            MainController.dafa = newValue.toString();
        });
        this.zhihuifangxiang.getItems().addAll("\u4e1c", "\u5357", "\u897f", "\u5317");
        this.zhihuifangxiang.valueProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue.toString()) {
                case "\u4e1c": {
                    MainController.zhihuifangxiang = 0;
                    break;
                }
                case "\u5357": {
                    MainController.zhihuifangxiang = 1;
                    break;
                }
                case "\u897f": {
                    MainController.zhihuifangxiang = 2;
                    break;
                }
                case "\u5317": {
                    MainController.zhihuifangxiang = 3;
                }
            }
        });
    }
}

