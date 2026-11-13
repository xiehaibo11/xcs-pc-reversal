/*
 * Decompiled with CFR 0.152.
 */
package quesheng.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import quesheng.controller.BaseController;
import quesheng.controller.StageController;
import quesheng.controller.intfc.ControlledStage;
import quesheng.mobilesdk.IMClientManager;
import quesheng.model.SystemConfig;
import quesheng.model.User;
import quesheng.util.Config;
import quesheng.util.DESKit;
import quesheng.util.HttpKit;
import quesheng.util.ObjectKit;
import quesheng.util.ValidateKit;
import quesheng.util.param;

public class LoginController
extends BaseController
implements ControlledStage,
Initializable {
    @FXML
    Label label_ip;
    @FXML
    TextField ipchose;
    @FXML
    ComboBox userId;
    @FXML
    ImageView delete;
    @FXML
    PasswordField password;
    @FXML
    Button login;
    @FXML
    Button chongzhi_ip;
    @FXML
    Label errorMessage;
    @FXML
    CheckBox remeber_password;
    StageController myController;

    @Override
    public void setStageController(StageController stageController) {
        this.myController = stageController;
        stageController.getStage("LoginView").resizableProperty().setValue(Boolean.FALSE);
        stageController.getStage("LoginView").setResizable(false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Config.systemConfig = (SystemConfig)ObjectKit.getObject(Config.config_file_url);
        if (Config.systemConfig == null) {
            Config.systemConfig = new SystemConfig();
        }
        this.login.requestFocus();
        this.chongzhi_ip.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("\u91cd\u7f6e\u53e3\u4ee4");
            alert.setHeaderText("\u8bf7\u6ce8\u610f\uff0c\u91cd\u7f6e\u53e3\u4ee4\u9700\u8981\u91cd\u65b0\u767b\u5f55");
            alert.setContentText("\u4f60\u786e\u5b9a\u8fd9\u6837\u505a\u5417?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                Config.systemConfig.setChoseIp(null);
                Config.systemConfig.setIsFirstLogin(true);
                ObjectKit.WriteObject(Config.systemConfig, Config.config_file_url);
                this.label_ip.setVisible(true);
                this.ipchose.setVisible(true);
            }
        });
        if (Config.systemConfig.getIsFirstLogin() != null && !Config.systemConfig.getIsFirstLogin().booleanValue()) {
            this.label_ip.setVisible(false);
            this.ipchose.setVisible(false);
        }
        if (Config.systemConfig.getChoseIp() != null) {
            try {
                String serverUrl = DESKit.Decrypt(Config.systemConfig.getChoseIp());
                byte[] textByte = serverUrl.getBytes("UTF-8");
                Base64.Encoder encoder = Base64.getEncoder();
                String encodedText = encoder.encodeToString(textByte);
                this.ipchose.setText(encodedText);
            }
            catch (Exception ex) {
                this.ipchose.setText("");
            }
        }
        if (Config.systemConfig.getUsers().size() > 0) {
            this.remeber_password.setSelected(true);
            ObservableList<String> keys = FXCollections.observableArrayList();
            for (Map.Entry<String, String> entry : Config.systemConfig.getUsers().entrySet()) {
                keys.add(DESKit.Decrypt(entry.getKey()));
                this.userId.getEditor().setText(DESKit.Decrypt(entry.getKey()));
                this.password.setText(DESKit.Decrypt(entry.getValue()));
            }
            this.userId.setItems(keys);
        }
        this.userId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && Config.systemConfig.getUsers().get(DESKit.Encrypt((String)newValue)) != null) {
                this.password.setText(DESKit.Decrypt(Config.systemConfig.getUsers().get(DESKit.Encrypt((String)newValue))));
            }
        });
        this.delete.setOnMouseClicked(event -> {
            Config.systemConfig.getUsers().remove(DESKit.Encrypt(this.userId.getEditor().getText()));
            this.userId.getItems().remove(this.userId.getEditor().getText());
            this.password.setText("");
            this.userId.getEditor().setText("");
            ObjectKit.WriteObject(Config.systemConfig, Config.config_file_url);
        });
    }

    public void goToMain() {
        IMClientManager.getInstance().initMobileIMSDK();
        Platform.runLater(() -> {
            this.myController.setStage("IndexView", "LoginView");
            this.myController.unloadStage("LoginView");
        });
    }

    public void userLogging(final String userId, final String password) {
        Map<String, String> map = new HashMap<String, String>(8);
        map.put("ajax", "true");
        map.put("deviceid", "");
        map.put("lastloginversion", param.version_name);
        map.put("devicetype", "3");
        map.put("password", password);
        map.put("username", userId);
        map.put("loginfield", "username");
        map.put("type", "username");
        map.put("version", param.version);
        HttpKit.Post("http://" + DESKit.Decrypt(Config.systemConfig.getChoseIp()) + ":8081" + "/api/login/login", map, new Callback(){

            @Override
            public void onFailure(Call call, IOException ioe) {
                BaseController.cancelMyProgressDialog();
                BaseController.showTextDialog(ioe.getLocalizedMessage());
            }

            @Override
            public void onResponse(Call call, Response rspns) throws IOException {
                BaseController.cancelMyProgressDialog();
                String xx = rspns.body().string();
                JsonObject result = new JsonParser().parse(xx).getAsJsonObject();
                if (result.get("success").getAsBoolean()) {
                    Config.systemConfig.setIsFirstLogin(false);
                    param.user = param.gson.fromJson(result.get("message").toString(), User.class);
                    if (param.user != null) {
                        param.jwt = result.get("jwt").getAsString();
                        if (LoginController.this.remeber_password.isSelected()) {
                            Config.systemConfig.getUsers().put(DESKit.Encrypt(userId), DESKit.Encrypt(password));
                            ObjectKit.WriteObject(Config.systemConfig, Config.config_file_url);
                        } else {
                            Config.systemConfig.getUsers().remove(DESKit.Encrypt(userId));
                            ObjectKit.WriteObject(Config.systemConfig, Config.config_file_url);
                        }
                        LoginController.this.goToMain();
                    }
                } else {
                    BaseController.cancelMyProgressDialog();
                    BaseController.showTextDialog(result.get("message").getAsString());
                }
            }
        });
    }

    public void processLogin(ActionEvent event) {
        String port_regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d):([0-9]|[1-9]\\d{1,3}|[1-5]\\d{4}|6[0-5]{2}[0-3][0-5])$";
        String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)";
        if (ValidateKit.isNull(this.ipchose.getText())) {
            this.errorMessage.setText("\u53e3\u4ee4\u4e0d\u80fd\u4e3a\u7a7a");
            return;
        }
        String serverUrl = this.ipchose.getText();
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            serverUrl = new String(decoder.decode(serverUrl), "UTF-8");
        }
        catch (Exception exception) {
            // empty catch block
        }
        Config.systemConfig.setChoseIp(DESKit.Encrypt(serverUrl));
        Config.initialize(null);
        if (ValidateKit.isNull(this.userId.getEditor().getText()) || ValidateKit.isNull(this.password.getText())) {
            this.errorMessage.setText("\u7528\u6237\u540d\u6216\u5bc6\u7801\u4e0d\u80fd\u4e3a\u7a7a");
            return;
        }
        LoginController.showMyProgressDialog("\u6b63\u5728\u767b\u5f55");
        this.userLogging(this.userId.getEditor().getText(), this.password.getText());
    }
}

