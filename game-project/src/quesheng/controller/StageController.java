/*
 * Decompiled with CFR 0.152.
 */
package quesheng.controller;

import java.util.HashMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import quesheng.controller.intfc.ControlledStage;
import quesheng.util.Config;
import quesheng.util.param;

public class StageController {
    private final HashMap<String, Stage> stages = new HashMap();

    public void addStage(String name, Stage stage) {
        this.stages.put(name, stage);
        stage.setTitle("HD" + param.version_name);
        stage.getIcons().add(new Image(StageController.class.getResourceAsStream(Config.logo_url)));
    }

    public Stage getStage(String name) {
        return this.stages.get(name);
    }

    public void setPrimaryStage(String primaryStageName, Stage primaryStage) {
        this.addStage(primaryStageName, primaryStage);
    }

    public boolean loadStage(String name, String resources, StageStyle ... styles) {
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource(resources));
            Pane tempPane = (Pane)loader.load();
            Scene tempScene = new Scene(tempPane);
            Stage tempStage = new Stage();
            tempStage.setScene(tempScene);
            for (StageStyle style : styles) {
                tempStage.initStyle(style);
            }
            this.addStage(name, tempStage);
            ControlledStage controlledStage = (ControlledStage)loader.getController();
            controlledStage.setStageController(this);
            return true;
        }
        catch (Exception e) {
            System.out.println("\u52a0\u8f7d\u573a\u666f\u51fa\u9519\uff01");
            e.printStackTrace();
            return false;
        }
    }

    public boolean setStage(String name) {
        this.getStage(name).show();
        return true;
    }

    public boolean setStage(String show, String close) {
        this.getStage(close).close();
        this.setStage(show);
        return true;
    }

    public boolean closeStage(String close) {
        this.getStage(close).close();
        return true;
    }

    public boolean unloadStage(String name) {
        if (this.stages.remove(name) == null) {
            System.out.println("\u7a97\u53e3\u4e0d\u5b58\u5728\uff0c\u8bf7\u68c0\u67e5\u540d\u79f0");
            return false;
        }
        System.out.println("\u7a97\u53e3\u79fb\u9664\u6210\u529f");
        return true;
    }
}

