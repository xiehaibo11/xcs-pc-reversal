/*
 * Decompiled with CFR 0.152.
 */
package quesheng.customcontrole;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

public class GuopaiTishi
extends AnchorPane
implements Initializable {
    public GuopaiTishi() {
        this.loadFxml();
    }

    private void loadFxml() {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/quesheng/view/guopai_tishi.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}

