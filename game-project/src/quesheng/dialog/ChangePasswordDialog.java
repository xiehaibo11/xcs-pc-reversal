/*
 * Decompiled with CFR 0.152.
 */
package quesheng.dialog;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import quesheng.controller.BaseController;
import quesheng.util.Config;
import quesheng.util.DESKit;
import quesheng.util.HttpKit;
import quesheng.util.ValidateKit;

public class ChangePasswordDialog
extends BaseController
implements Initializable {
    @FXML
    PasswordField pwd_old;
    @FXML
    PasswordField pwd_new;
    @FXML
    PasswordField pwd_repeat;
    @FXML
    Button chg_btn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.chg_btn.setOnAction(event -> {
            String str1 = this.pwd_old.getText().trim();
            if (ValidateKit.isNull(str1)) {
                ChangePasswordDialog.showTextDialog("\u8bf7\u8f93\u5165\u65e7\u5bc6\u7801");
                return;
            }
            String str2 = this.pwd_new.getText().trim();
            if (ValidateKit.isNull(str2)) {
                ChangePasswordDialog.showTextDialog("\u8bf7\u8f93\u5165\u65b0\u5bc6\u7801");
                return;
            }
            String str3 = this.pwd_repeat.getText().trim();
            if (ValidateKit.isNull(str3)) {
                ChangePasswordDialog.showTextDialog("\u8bf7\u786e\u8ba4\u65b0\u5bc6\u7801");
                return;
            }
            if (!str2.equals(str3)) {
                ChangePasswordDialog.showTextDialog("2\u6b21\u5bc6\u7801\u4e0d\u4e00\u81f4");
                return;
            }
            this.UpdatePassword(str1, str2);
        });
    }

    public void UpdatePassword(String old_password, String new_password) {
        ChangePasswordDialog.showMyProgressDialog("\u6b63\u5728\u4fee\u6539\u5bc6\u7801");
        Map<String, String> map = new HashMap<String, String>();
        map.put("ajax", "true");
        map.put("deviceid", "");
        map.put("old_password", old_password);
        map.put("password", new_password);
        HttpKit.Post("http://" + DESKit.Decrypt(Config.systemConfig.getChoseIp()) + ":8081" + "/api/user/updatePassword", map, new Callback(){

            @Override
            public void onFailure(Call call, IOException ioe) {
                BaseController.cancelMyProgressDialog();
                BaseController.showTextDialog(ioe.getLocalizedMessage());
            }

            @Override
            public void onResponse(Call call, Response rspns) throws IOException {
                String xx = rspns.body().string();
                JsonObject result = new JsonParser().parse(xx).getAsJsonObject();
                if (result.get("success").getAsBoolean()) {
                    BaseController.showTextDialog("\u5bc6\u7801\u5df2\u4fee\u6539\uff0c\u8bf7\u91cd\u65b0\u767b\u5f55");
                    Thread thread = new Thread(() -> {
                        try {
                            Thread.sleep(1000L);
                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.exit(0);
                    });
                    thread.setDaemon(true);
                    thread.start();
                } else {
                    BaseController.cancelMyProgressDialog();
                    BaseController.showTextDialog(result.get("message").getAsString());
                }
            }
        });
    }
}

