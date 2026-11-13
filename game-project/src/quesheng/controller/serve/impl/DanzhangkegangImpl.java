/*
 * Decompiled with CFR 0.152.
 */
package quesheng.controller.serve.impl;

import io.reactivex.ObservableEmitter;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import quesheng.controller.BaseController;
import quesheng.controller.MainController;
import quesheng.controller.serve.AbstractServe;
import quesheng.util.Config;

public class DanzhangkegangImpl
extends AbstractServe {
    public DanzhangkegangImpl(MainController mainController) {
        super(mainController);
    }

    @Override
    public void danzhangkegang(ObservableEmitter emitter) {
        if (MainController.meijuchongshedanzhangkegang == 1 || Config.systemConfig.danzhangkegang_pai.size() == 0) {
            if (MainController.danzhangkegang_seting_popup == null) {
                MainController.danzhangkegang_seting_popup = new Stage();
                MainController.danzhangkegang_seting_popup.getIcons().add(new Image(BaseController.class.getResourceAsStream(Config.logo_url)));
                MainController.danzhangkegang_seting_popup.setAlwaysOnTop(true);
                MainController.danzhangkegang_seting_popup.initModality(Modality.APPLICATION_MODAL);
                MainController.danzhangkegang_seting_popup.resizableProperty().setValue(Boolean.FALSE);
                MainController.danzhangkegang_seting_popup.setResizable(false);
                FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/quesheng/view/DanZhangKeGang.fxml"));
                Pane tempPane = null;
                try {
                    tempPane = (Pane)loader.load();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                Scene tempScene = new Scene(tempPane);
                MainController.danzhangkegang_seting_popup.setScene(tempScene);
                MainController.danzhangkegang_seting_popup.setTitle("\u5355\u5f20\u53ef\u6760\u9009\u62e9");
                MainController.danzhangkegang_seting_popup.setOnHidden(event1 -> emitter.onNext(1));
                MainController.danzhangkegang_seting_popup.show();
            } else {
                MainController.danzhangkegang_seting_popup.show();
            }
        } else {
            emitter.onNext(1);
        }
    }
}

