/*
 * Decompiled with CFR 0.152.
 */
package quesheng.controller;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class BaseController {
    private static Stage ProgressBar_popup = null;

    public static void showTextDialog(String message) {
        Platform.runLater(() -> {
            Stage popup = new Stage();
            popup.setAlwaysOnTop(true);
            popup.initModality(Modality.APPLICATION_MODAL);
            Button closeBtn = new Button("\u77e5\u9053\u4e86");
            closeBtn.setOnAction(e -> popup.close());
            VBox root = new VBox();
            root.setPadding(new Insets(20.0));
            root.setAlignment(Pos.BASELINE_CENTER);
            root.setSpacing(20.0);
            root.getChildren().addAll((Node[])new Node[]{new Label(message), closeBtn});
            Scene scene = new Scene(root);
            popup.setScene(scene);
            popup.setTitle("\u63d0\u793a\u4fe1\u606f");
            popup.show();
            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(3000L);
                    if (popup.isShowing()) {
                        Platform.runLater(() -> popup.close());
                    }
                }
                catch (Exception exp) {
                    exp.printStackTrace();
                }
            });
            thread.setDaemon(true);
            thread.start();
        });
    }

    public static void showMyProgressDialog(String message) {
        Platform.runLater(() -> {
            if (ProgressBar_popup == null) {
                ProgressBar_popup = new Stage();
                ProgressIndicator progressBar = new ProgressIndicator();
                ProgressBar_popup.setAlwaysOnTop(true);
                ProgressBar_popup.initModality(Modality.APPLICATION_MODAL);
                VBox root = new VBox();
                root.setPadding(new Insets(20.0));
                root.setAlignment(Pos.BASELINE_CENTER);
                root.setSpacing(20.0);
                root.getChildren().addAll((Node[])new Node[]{progressBar});
                Scene scene = new Scene(root);
                ProgressBar_popup.setScene(scene);
            }
            ProgressBar_popup.setTitle(message);
            ProgressBar_popup.show();
        });
    }

    public static void cancelMyProgressDialog() {
        Platform.runLater(() -> ProgressBar_popup.close());
    }
}

