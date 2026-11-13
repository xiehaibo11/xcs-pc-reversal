/*
 * Decompiled with CFR 0.152.
 */
package quesheng.controller;

import javafx.application.Application;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.openmob.mobileimsdk.java.core.LocalUDPDataSender;
import quesheng.controller.StageController;
import quesheng.mobilesdk.IMClientManager;

public class MainApp
extends Application {
    private StageController stageController;

    @Override
    public void init() throws Exception {
        super.init();
    }

    @Override
    public void start(Stage primaryStage) {
        this.stageController = new StageController();
        this.stageController.loadStage("LoginView", "/quesheng/view/LoginStageView.fxml", new StageStyle[0]);
        this.stageController.loadStage("IndexView", "/quesheng/view/MainStageView.fxml", new StageStyle[0]);
        this.stageController.loadStage("SettingView", "/quesheng/view/SettingStageView.fxml", new StageStyle[0]);
        this.stageController.getStage("SettingView").setAlwaysOnTop(true);
        this.stageController.getStage("SettingView").initModality(Modality.APPLICATION_MODAL);
        this.stageController.getStage("SettingView").resizableProperty().setValue(Boolean.FALSE);
        this.stageController.getStage("SettingView").setResizable(false);
        this.stageController.getStage("SettingView").setTitle("\u89c4\u5219\u8bbe\u7f6e");
        this.stageController.setPrimaryStage("primaryStage", primaryStage);
        this.stageController.setStage("LoginView");
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        int code = LocalUDPDataSender.getInstance().sendLoginout();
        IMClientManager.getInstance().resetInitFlag();
        IMClientManager.getInstance().release();
        System.exit(0);
    }

    public static void main(String[] args) {
        MainApp.launch(args);
    }
}

