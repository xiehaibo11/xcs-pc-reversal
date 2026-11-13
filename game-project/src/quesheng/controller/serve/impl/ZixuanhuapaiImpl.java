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

public class ZixuanhuapaiImpl
extends AbstractServe {
    public ZixuanhuapaiImpl(MainController mainController) {
        super(mainController);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public void hupaixuanze(ObservableEmitter emitter) {
        block6: {
            block5: {
                if (MainController.meijuchongshehuapai == 1) break block5;
                if (MainController.zixuanhuapai.size() >= 1) break block6;
            }
            MainController.zixuanhuapai.clear();
            if (MainController.zixuanhuapai_seting_popup == null) {
                MainController.zixuanhuapai_seting_popup = new Stage();
                MainController.zixuanhuapai_seting_popup.getIcons().add(new Image(BaseController.class.getResourceAsStream(Config.logo_url)));
                MainController.zixuanhuapai_seting_popup.setAlwaysOnTop(true);
                MainController.zixuanhuapai_seting_popup.initModality(Modality.APPLICATION_MODAL);
                MainController.zixuanhuapai_seting_popup.resizableProperty().setValue(Boolean.FALSE);
                MainController.zixuanhuapai_seting_popup.setResizable(false);
                FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/quesheng/view/HuaPaiXuanZe.fxml"));
                Pane tempPane = null;
                try {
                    tempPane = (Pane)loader.load();
                }
                catch (IOException e1) {
                    e1.printStackTrace();
                }
                Scene tempScene = new Scene(tempPane);
                MainController.zixuanhuapai_seting_popup.setScene(tempScene);
                MainController.zixuanhuapai_seting_popup.setTitle("\u82b1\u724c\u8bbe\u7f6e");
                MainController.zixuanhuapai_seting_popup.setOnHidden(event1 -> {
                    if (MainController.zixuanhuapai.size() > 0) {
                        Config.weina.forEach(mj -> {
                            mj.kedi_huapai = false;
                            if (MainController.zixuanhuapai.contains(mj.getResult())) {
                                mj.kedi_huapai = true;
                                mj.yuanpai = mj.getResult();
                                mj.setResult(41);
                            }
                        });
                        emitter.onNext(1);
                    } else {
                        Config.weina.forEach(mj -> {
                            mj.kedi_huapai = false;
                        });
                        emitter.onNext(1);
                    }
                });
                MainController.zixuanhuapai_seting_popup.show();
                return;
            }
            MainController.zixuanhuapai_seting_popup.show();
            return;
        }
        Config.weina.forEach(mj -> {
            mj.kedi_huapai = false;
            if (MainController.zixuanhuapai.contains(mj.getResult())) {
                mj.kedi_huapai = true;
                mj.yuanpai = mj.getResult();
                mj.setResult(41);
            }
        });
        emitter.onNext(1);
    }
}

