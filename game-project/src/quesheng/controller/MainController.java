/*
 * Decompiled with CFR 0.152.
 */
package quesheng.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.melloware.jintellitype.JIntellitype;
import io.reactivex.Observable;
import java.awt.AWTException;
import java.awt.Robot;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observer;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import net.openmob.mobileimsdk.java.core.LocalUDPDataSender;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import quesheng.ai.AiInterface;
import quesheng.ai.impl.XueZhanAiImpl;
import quesheng.controller.BaseController;
import quesheng.controller.StageController;
import quesheng.controller.intfc.ControlledStage;
import quesheng.controller.serve.impl.CaishengxuanzeImpl;
import quesheng.controller.serve.impl.DanzhangkegangImpl;
import quesheng.controller.serve.impl.LiangCengJiImpl;
import quesheng.controller.serve.impl.SanCengJiImpl;
import quesheng.controller.serve.impl.ZixuanhuapaiImpl;
import quesheng.customcontrole.BottomPanel;
import quesheng.customcontrole.HuanShouPaiPanel;
import quesheng.customcontrole.JiuZhengHuanShouPaiPanel;
import quesheng.customcontrole.LeftPanel;
import quesheng.customcontrole.MjPanelInterface;
import quesheng.customcontrole.RightPanel;
import quesheng.customcontrole.TopPanel;
import quesheng.customcontrole.YuceYinaPanel;
import quesheng.customcontrole.ZhuangPanel;
import quesheng.mobilesdk.IMClientManager;
import quesheng.model.Mj;
import quesheng.util.AutoUpdateThread;
import quesheng.util.Config;
import quesheng.util.DESKit;
import quesheng.util.HashKit;
import quesheng.util.HttpKit;
import quesheng.util.HuPaiKit;
import quesheng.util.ImageKit;
import quesheng.util.JFXKit;
import quesheng.util.ObjectKit;
import quesheng.util.param;

public class MainController
extends BaseController
implements ControlledStage,
Initializable {
    @FXML
    Label pai_count;
    @FXML
    Label dong_count;
    @FXML
    Label nan_count;
    @FXML
    Label xi_count;
    @FXML
    Label bei_count;
    @FXML
    Button chexiao_btn;
    @FXML
    Button huanyuan_btn;
    @FXML
    Button guize_btn;
    @FXML
    Button huoqu_btn;
    @FXML
    Button jihuo_btn;
    @FXML
    Button jieshu_btn;
    @FXML
    Button chongxuanzhuang_btn;
    @FXML
    Button fenxi_btn;
    @FXML
    Button out_btn;
    @FXML
    Button min_btn;
    @FXML
    ImageView max_btn;
    @FXML
    Button changepassword_btn;
    @FXML
    Button clean_btn;
    @FXML
    Button jiuzhenghuanpai_btn;
    @FXML
    Button xuanzhuan_btn;
    @FXML
    AnchorPane main_panel;
    @FXML
    ComboBox guo_combobox;
    @FXML
    ComboBox dong_combobox;
    @FXML
    ComboBox zhang_combobox;
    @FXML
    ComboBox dongban_combobox;
    @FXML
    ComboBox duidiao_combobox;
    static StageController myController;
    public static final String GANG_BORDER = "border_deepskyblue";
    public static final String PANG_BORDER = "border_green";
    public static final String CHI_BORDER = "border_yellow";
    private static Label bottom_zhuang_label;
    private static HBox bottom_panel;
    public static TopPanel topPanel;
    public static BottomPanel bottomPanel;
    public static LeftPanel leftPanel;
    public static RightPanel rightPanel;
    public static ZhuangPanel zhuangPanel;
    public static YuceYinaPanel yuceYinaPanel;
    public static Label renyicaisheng_label;
    public static FlowPane yichu_pane;
    public static AnchorPane static_main_panel;
    public static VBox kech_list_panel;
    public static Stage caisheng_seting_popup;
    public static Stage zixuanhuapai_seting_popup;
    public static Stage danzhangkegang_seting_popup;
    private boolean yinapai = false;
    private static int suiyigang_count;
    public static int zhuangjia;
    public static int dapaifangx;
    public static int meijuchongshehuapai;
    public static int meijuchongshecaisheng;
    public static int meijuchongshedanzhangkegang;
    public static LinkedList<Mj> weina_catch;
    public static int zhihuifangxiang;
    public static int kaiqiai;
    public static String dafa;
    public static AiInterface ai;
    private static List<Map<String, Object>> state;
    public static List<Integer> caisheng;
    public static List<Integer> zixuanhuapai;
    public static LinkedList<Mj> weina;
    public static List<MjPanelInterface> directions;
    public static List<MjPanelInterface> directions_temp;
    private List<Mj> huanhoupai_yixuan = new ArrayList<Mj>();
    private LinkedList<Mj> weina_backup = new LinkedList();
    List<Mj> shangyiba = new LinkedList<Mj>();
    List<Mj> dangqianba = new LinkedList<Mj>();
    List<Node> guozuang_top_label = new LinkedList<Node>();
    private ScheduledExecutorService jiance_service;
    private Observer onLoginSucessObserver = null;
    static Map<Integer, List<Integer>> maima_map;

    @Override
    public void setStageController(StageController stageController) {
        myController = stageController;
        stageController.getStage("IndexView").setMaximized(true);
        stageController.getStage("IndexView").setFullScreenExitHint("\u6309Ctrl+Q\u952e\u53ef\u9000\u51fa\u5168\u5c4f\u6a21\u5f0f");
        stageController.getStage("IndexView").setResizable(true);
        stageController.getStage("IndexView").initStyle(StageStyle.TRANSPARENT);
        stageController.getStage("IndexView").setOnShown(event -> {
            if (param.user.getJihuo_status() == 1) {
                this.jihuo_btn.setVisible(false);
            }
            this.main_panel.setBackground(new Background(new BackgroundFill(Color.color(Config.systemConfig.zidingyibeijingyanse[0], Config.systemConfig.zidingyibeijingyanse[1], Config.systemConfig.zidingyibeijingyanse[2]), CornerRadii.EMPTY, Insets.EMPTY)));
            this.openJiance();
            new Thread(() -> {
                final AutoUpdateThread autoUpdateThread = new AutoUpdateThread();
                Map<String, String> map = new HashMap<String, String>(3);
                map.put("ajax", "true");
                if (autoUpdateThread.getArchName().contains("64")) {
                    map.put("version", "dapai64");
                }
                if (autoUpdateThread.getArchName().contains("32")) {
                    map.put("version", "dapai32");
                }
                HttpKit.Post("http://" + DESKit.Decrypt(Config.systemConfig.getChoseIp()) + ":8081" + "/api/config/GetLastVersion", map, new Callback(){

                    @Override
                    public void onFailure(Call call, IOException ioe) {
                    }

                    @Override
                    public void onResponse(Call call, Response rspns) throws IOException {
                        String xx = rspns.body().string();
                        try {
                            JsonObject result = new JsonParser().parse(xx).getAsJsonObject();
                            if (result.get("success").getAsBoolean()) {
                                Integer versionCode = result.get("versionCode").getAsInt();
                                autoUpdateThread.filename = result.get("name").getAsString();
                                autoUpdateThread.xiheportalURL = result.get("url").getAsString();
                                if (versionCode > param.version_code) {
                                    String lujing = autoUpdateThread.saveUrlAs(autoUpdateThread.xiheportalURL, "./" + autoUpdateThread.filename, "GET");
                                    Platform.runLater(() -> BaseController.showTextDialog("\u5df2\u7ecf\u5347\u7ea7\u4e86\u65b0\u7684\u7a0b\u5e8f\uff0c\u8bf7\u91cd\u65b0\u542f\u52a8" + lujing));
                                }
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }).start();
            if (HashKit.sha256(param.user.getSalt() + "123456").equals(param.user.getPassword())) {
                MainController.showTextDialog("\u5bc6\u7801\u662f\u9ed8\u8ba4\u5bc6\u7801\uff0c\u8bf7\u5c3d\u5feb\u4fee\u6539");
            }
            IMClientManager.getInstance().getTransDataListener().setMainGUI(this);
            this.doZhuoLoginImpl();
        });
        stageController.getStage("IndexView").focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.booleanValue()) {
                for (Map.Entry<String, Integer> entry : Config.getKey2KeycodeMapping().entrySet()) {
                    JIntellitype.getInstance().registerHotKey(entry.getValue(), 0, entry.getValue());
                }
                JIntellitype.getInstance().registerHotKey(27, 0, 27);
            } else {
                for (Map.Entry<String, Integer> entry : Config.getKey2KeycodeMapping().entrySet()) {
                    JIntellitype.getInstance().unregisterHotKey(entry.getValue());
                }
                JIntellitype.getInstance().unregisterHotKey(27);
            }
        });
        JIntellitype.getInstance().addHotKeyListener(markCode -> {
            if (27 == markCode) {
                Platform.runLater(() -> this.hebingpai());
            }
            if (Config.getKey2KeycodeMapping().getOrDefault(Config.systemConfig.guopai, 0) == markCode) {
                Platform.runLater(() -> this.guozhang());
            }
            if (Config.getKey2KeycodeMapping().getOrDefault(Config.systemConfig.xunhuanguopai, 0) == markCode) {
                Platform.runLater(() -> this.xunhuanguozhang());
            }
            if (Config.getKey2KeycodeMapping().getOrDefault(Config.systemConfig.guodong, 0) == markCode) {
                Platform.runLater(() -> this.guodong());
            }
            if (Config.getKey2KeycodeMapping().getOrDefault(Config.systemConfig.mopai, 0) == markCode) {
                for (MjPanelInterface direction : directions) {
                    if (direction.getMo() != 1) continue;
                    for (MjPanelInterface direction1 : directions) {
                        direction1.setMo(0);
                    }
                    Platform.runLater(() -> direction.getMo_text().fireEvent(new MouseEvent(direction.getMo_text(), direction.getMo_text(), MouseEvent.MOUSE_CLICKED, 0.0, 0.0, 0.0, 0.0, MouseButton.PRIMARY, 1, false, false, false, false, true, false, false, false, true, false, null)));
                    break;
                }
            }
            if (Config.getKey2KeycodeMapping().getOrDefault(Config.systemConfig.chexiao, 0) == markCode) {
                Platform.runLater(() -> this.chexiao());
            }
            if (Config.getKey2KeycodeMapping().getOrDefault(Config.systemConfig.gangpai, 0) == markCode) {
                for (MjPanelInterface direction : directions) {
                    if (!((Boolean)direction.getGang_text().getProperties().getOrDefault("gang", false)).booleanValue()) continue;
                    Platform.runLater(() -> direction.getGang_text().fireEvent(new MouseEvent(direction.getGang_text(), direction.getMo_text(), MouseEvent.MOUSE_CLICKED, 0.0, 0.0, 0.0, 0.0, MouseButton.PRIMARY, 1, false, false, false, false, true, false, false, false, true, false, null)));
                    break;
                }
            }
            if (Config.getKey2KeycodeMapping().getOrDefault(Config.systemConfig.pangpai_kjj, 0) == markCode) {
                for (MjPanelInterface direction : directions) {
                    if (!((Boolean)direction.getPang_text().getProperties().getOrDefault("peng", false)).booleanValue()) continue;
                    Platform.runLater(() -> direction.getPang_text().fireEvent(new MouseEvent(direction.getPang_text(), direction.getMo_text(), MouseEvent.MOUSE_CLICKED, 0.0, 0.0, 0.0, 0.0, MouseButton.PRIMARY, 1, false, false, false, false, true, false, false, false, true, false, null)));
                    break;
                }
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.max_btn.setOnMouseClicked(event -> {
            if (myController.getStage("IndexView").isMaximized()) {
                myController.getStage("IndexView").setMaximized(false);
            } else {
                myController.getStage("IndexView").setMaximized(true);
            }
        });
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getBounds();
        Config.SCREEN_HEIGHT = bounds.getMaxY();
        Config.SCREEN_WIDTH = bounds.getMaxX();
        Config.MJ_WIDTH = Config.systemConfig.wanjiapaishu < 13 ? (Config.SCREEN_HEIGHT - 50.0) / 17.0 : (Config.SCREEN_HEIGHT - 50.0) / (double)(Config.systemConfig.wanjiapaishu + 2 + 2);
        Config.MJ_HEIGHT = Config.MJ_WIDTH * 1.37;
        Config.MJ_HEIGHT_1_2 = Config.MJ_HEIGHT / 2.0;
        Config.MJ_WIDTH_1_2 = Config.MJ_WIDTH / 2.0;
        Config.MJ_HEIGHT_3_4 = Config.MJ_HEIGHT * 3.0 / 4.0;
        Config.MJ_WIDTH_3_4 = Config.MJ_WIDTH * 3.0 / 4.0;
        bottom_panel.setLayoutX(Config.SCREEN_WIDTH - Config.MJ_WIDTH - 50.0);
        bottom_panel.setLayoutY(30.0);
        topPanel = new TopPanel();
        bottomPanel = new BottomPanel();
        leftPanel = new LeftPanel();
        rightPanel = new RightPanel();
        zhuangPanel = new ZhuangPanel();
        static_main_panel = this.main_panel;
        directions.add(bottomPanel);
        directions.add(rightPanel);
        directions.add(topPanel);
        directions.add(leftPanel);
        directions.forEach(direction -> {
            direction.getHu_text().getStyleClass().add("caozuo_tishi");
            direction.getMo_text().getStyleClass().add("caozuo_tishi");
            direction.getChi_text().getStyleClass().add("caozuo_tishi");
            direction.getPang_text().getStyleClass().add("caozuo_tishi");
            direction.getGang_text().getStyleClass().add("caozuo_tishi");
        });
        this.changepassword_btn.setOnAction(event -> {
            Stage popup = new Stage();
            popup.getIcons().add(new Image(BaseController.class.getResourceAsStream(Config.logo_url)));
            popup.setAlwaysOnTop(true);
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.resizableProperty().setValue(Boolean.FALSE);
            popup.setResizable(false);
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/quesheng/view/ChangePassword.fxml"));
            Pane tempPane = null;
            try {
                tempPane = (Pane)loader.load();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            Scene tempScene = new Scene(tempPane);
            popup.setScene(tempScene);
            popup.setTitle("\u4fee\u6539\u5bc6\u7801" + param.user.getUsername());
            popup.show();
        });
        this.clean_btn.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("\u91cd\u7f6e\u8f6f\u4ef6");
            alert.setHeaderText("\u8bf7\u6ce8\u610f\uff0c\u91cd\u7f6e\u4fe1\u606f\u9700\u8981\u91cd\u65b0\u914d\u7f6e\u670d\u52a1IP\uff0c\u91cd\u65b0\u767b\u5f55");
            alert.setContentText("\u4f60\u786e\u5b9a\u8fd9\u6837\u505a\u5417?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                Config.systemConfig.setChoseIp(null);
                Config.systemConfig.setIsFirstLogin(true);
                Config.systemConfig.setUsers(new LinkedHashMap<String, String>());
                ObjectKit.WriteObject(Config.systemConfig, Config.config_file_url);
                MainController.showTextDialog("\u8f6f\u4ef6\u5df2\u91cd\u7f6e\uff0c\u8bf7\u91cd\u65b0\u767b\u5f55");
                Thread thread = new Thread(() -> {
                    try {
                        Thread.sleep(2000L);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.exit(0);
                });
                thread.setDaemon(true);
                thread.start();
            }
        });
        this.chongxuanzhuang_btn.setOnAction(event -> this.huanyuan());
        this.guo_combobox.getItems().addAll("------", "\u8fc7\u5f20", "\u5faa\u73af\u8fc7\u5f20", "\u8fc7\u680b", "\u8de8\u65b9\u4f4d\u8fc7\u680b", "\u5355\u5f20\u5bf9\u8c03", "\u5408\u5e76\u724c");
        this.guo_combobox.setValue("------");
        this.guo_combobox.valueProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue.toString()) {
                case "------": {
                    break;
                }
                case "\u8fc7\u5f20": {
                    this.guozhang();
                    break;
                }
                case "\u5faa\u73af\u8fc7\u5f20": {
                    this.xunhuanguozhang();
                    break;
                }
                case "\u8fc7\u680b": {
                    this.guodong();
                    break;
                }
                case "\u8de8\u65b9\u4f4d\u8fc7\u680b": {
                    this.kuafangweiguodong();
                    break;
                }
                case "\u5355\u5f20\u5bf9\u8c03": {
                    this.danzhangduidiao();
                    break;
                }
                case "\u5408\u5e76\u724c": {
                    this.hebingpai();
                }
            }
        });
        this.duidiao_combobox.getItems().addAll("\u5bf9\u8c03", "\u4e1c", "\u5357", "\u897f", "\u5317");
        this.duidiao_combobox.setValue("\u5bf9\u8c03");
        this.duidiao_combobox.valueProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue.toString()) {
                case "\u5bf9\u8c03": {
                    break;
                }
                case "\u4e1c": {
                    Config.weina.forEach(mj -> {
                        if (mj.getDirection() == 0) {
                            if (mj.getMahjong_number() % 2 == 0) {
                                mj.setMahjong_number(mj.getMahjong_number() + 1);
                            } else {
                                mj.setMahjong_number(mj.getMahjong_number() - 1);
                            }
                        }
                    });
                    this.displayWeinaPai();
                    this.duidiao_combobox.setValue("\u5bf9\u8c03");
                    break;
                }
                case "\u5357": {
                    Config.weina.forEach(mj -> {
                        if (mj.getDirection() == 1) {
                            if (mj.getMahjong_number() % 2 == 0) {
                                mj.setMahjong_number(mj.getMahjong_number() + 1);
                            } else {
                                mj.setMahjong_number(mj.getMahjong_number() - 1);
                            }
                        }
                    });
                    this.displayWeinaPai();
                    this.duidiao_combobox.setValue("\u5bf9\u8c03");
                    break;
                }
                case "\u897f": {
                    Config.weina.forEach(mj -> {
                        if (mj.getDirection() == 2) {
                            if (mj.getMahjong_number() % 2 == 0) {
                                mj.setMahjong_number(mj.getMahjong_number() + 1);
                            } else {
                                mj.setMahjong_number(mj.getMahjong_number() - 1);
                            }
                        }
                    });
                    this.displayWeinaPai();
                    this.duidiao_combobox.setValue("\u5bf9\u8c03");
                    break;
                }
                case "\u5317": {
                    Config.weina.forEach(mj -> {
                        if (mj.getDirection() == 3) {
                            if (mj.getMahjong_number() % 2 == 0) {
                                mj.setMahjong_number(mj.getMahjong_number() + 1);
                            } else {
                                mj.setMahjong_number(mj.getMahjong_number() - 1);
                            }
                        }
                    });
                    this.displayWeinaPai();
                    this.duidiao_combobox.setValue("\u5bf9\u8c03");
                }
            }
        });
        this.dong_combobox.getItems().addAll("\u680b", 1, 2, 3, 4, 5, 6, 7, 8, 9, -1, -2, -3, -4, -5, -6, -7, -8, -9);
        this.dong_combobox.setValue("\u680b");
        this.dong_combobox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue instanceof Integer) {
                List<Mj> weina_right = Config.weina.stream().filter(mj -> mj.getDirection() == 1).collect(Collectors.toList());
                List<Mj> weina_bottom = Config.weina.stream().filter(mj -> mj.getDirection() == 0).collect(Collectors.toList());
                List<Mj> weina_top = Config.weina.stream().filter(mj -> mj.getDirection() == 2).collect(Collectors.toList());
                List<Mj> weina_left = Config.weina.stream().filter(mj -> mj.getDirection() == 3).collect(Collectors.toList());
                int dong = (Integer)newValue;
                int finalDong = dong = -1 * dong;
                weina_bottom.forEach(mj -> {
                    mj.setMahjong_number(mj.getMahjong_number() - finalDong * 2);
                    if (mj.getMahjong_number() < 0) {
                        mj.setDirection(3);
                        if (mj.getMahjong_number() % 2 != 0) {
                            mj.setMahjong_number(weina_left.size() - (mj.getMahjong_number() + finalDong * 2));
                        } else {
                            mj.setMahjong_number(weina_left.size() - (mj.getMahjong_number() + finalDong * 2) - 2);
                        }
                    } else if (mj.getMahjong_number() >= weina_bottom.size()) {
                        mj.setDirection(1);
                        if (mj.getMahjong_number() % 2 != 0) {
                            mj.setMahjong_number(-finalDong * 2 - (mj.getMahjong_number() - weina_bottom.size()));
                        } else {
                            mj.setMahjong_number(weina_bottom.size() - (mj.getMahjong_number() + finalDong * 2) - 2);
                        }
                    }
                });
                weina_right.forEach(mj -> {
                    mj.setMahjong_number(mj.getMahjong_number() - finalDong * 2);
                    if (mj.getMahjong_number() < 0) {
                        mj.setDirection(0);
                        if (mj.getMahjong_number() % 2 != 0) {
                            mj.setMahjong_number(weina_bottom.size() - (mj.getMahjong_number() + finalDong * 2));
                        } else {
                            mj.setMahjong_number(weina_bottom.size() - (mj.getMahjong_number() + finalDong * 2) - 2);
                        }
                    } else if (mj.getMahjong_number() >= weina_right.size()) {
                        mj.setDirection(2);
                        if (mj.getMahjong_number() % 2 != 0) {
                            mj.setMahjong_number(-finalDong * 2 - (mj.getMahjong_number() - weina_right.size()));
                        } else {
                            mj.setMahjong_number(weina_right.size() - (mj.getMahjong_number() + finalDong * 2) - 2);
                        }
                    }
                });
                weina_top.forEach(mj -> {
                    mj.setMahjong_number(mj.getMahjong_number() - finalDong * 2);
                    if (mj.getMahjong_number() < 0) {
                        mj.setDirection(1);
                        if (mj.getMahjong_number() % 2 != 0) {
                            mj.setMahjong_number(weina_right.size() - (mj.getMahjong_number() + finalDong * 2));
                        } else {
                            mj.setMahjong_number(weina_right.size() - (mj.getMahjong_number() + finalDong * 2) - 2);
                        }
                    } else if (mj.getMahjong_number() >= weina_top.size()) {
                        mj.setDirection(3);
                        if (mj.getMahjong_number() % 2 != 0) {
                            mj.setMahjong_number(-finalDong * 2 - (mj.getMahjong_number() - weina_top.size()));
                        } else {
                            mj.setMahjong_number(weina_top.size() - (mj.getMahjong_number() + finalDong * 2) - 2);
                        }
                    }
                });
                weina_left.forEach(mj -> {
                    mj.setMahjong_number(mj.getMahjong_number() - finalDong * 2);
                    if (mj.getMahjong_number() < 0) {
                        mj.setDirection(2);
                        if (mj.getMahjong_number() % 2 != 0) {
                            mj.setMahjong_number(weina_top.size() - (mj.getMahjong_number() + finalDong * 2));
                        } else {
                            mj.setMahjong_number(weina_top.size() - (mj.getMahjong_number() + finalDong * 2) - 2);
                        }
                    } else if (mj.getMahjong_number() >= weina_left.size()) {
                        mj.setDirection(0);
                        if (mj.getMahjong_number() % 2 != 0) {
                            mj.setMahjong_number(-finalDong * 2 - (mj.getMahjong_number() - weina_left.size()));
                        } else {
                            mj.setMahjong_number(weina_left.size() - (mj.getMahjong_number() + finalDong * 2) - 2);
                        }
                    }
                });
                this.displayWeinaPai();
            }
            this.dong_combobox.setValue("\u680b");
        });
        this.zhang_combobox.getItems().addAll("\u5f20", 1, 2, 3, 4, 5, 6, 7, 8, 9, -1, -2, -3, -4, -5, -6, -7, -8, -9);
        this.zhang_combobox.setValue("\u5f20");
        this.zhang_combobox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue instanceof Integer) {
                List<Mj> weina_right = Config.weina.stream().filter(mj -> mj.getDirection() == 1).collect(Collectors.toList());
                List<Mj> weina_bottom = Config.weina.stream().filter(mj -> mj.getDirection() == 0).collect(Collectors.toList());
                List<Mj> weina_top = Config.weina.stream().filter(mj -> mj.getDirection() == 2).collect(Collectors.toList());
                List<Mj> weina_left = Config.weina.stream().filter(mj -> mj.getDirection() == 3).collect(Collectors.toList());
                int dong = (Integer)newValue;
                int finalDong = dong = -1 * dong;
                weina_bottom.forEach(mj -> {
                    if (mj.getMahjong_number() % 2 != 0) {
                        mj.setMahjong_number(mj.getMahjong_number() - finalDong * 2);
                        if (mj.getMahjong_number() < 0) {
                            mj.setDirection(3);
                            mj.setMahjong_number(weina_left.size() - (mj.getMahjong_number() + finalDong * 2));
                        } else if (mj.getMahjong_number() >= weina_bottom.size()) {
                            mj.setDirection(1);
                            mj.setMahjong_number(-finalDong * 2 - (mj.getMahjong_number() - weina_bottom.size()));
                        }
                    }
                });
                weina_right.forEach(mj -> {
                    if (mj.getMahjong_number() % 2 != 0) {
                        mj.setMahjong_number(mj.getMahjong_number() - finalDong * 2);
                        if (mj.getMahjong_number() < 0) {
                            mj.setDirection(0);
                            mj.setMahjong_number(weina_bottom.size() - (mj.getMahjong_number() + finalDong * 2));
                        } else if (mj.getMahjong_number() >= weina_right.size()) {
                            mj.setDirection(2);
                            mj.setMahjong_number(-finalDong * 2 - (mj.getMahjong_number() - weina_right.size()));
                        }
                    }
                });
                weina_top.forEach(mj -> {
                    if (mj.getMahjong_number() % 2 != 0) {
                        mj.setMahjong_number(mj.getMahjong_number() - finalDong * 2);
                        if (mj.getMahjong_number() < 0) {
                            mj.setDirection(1);
                            mj.setMahjong_number(weina_right.size() - (mj.getMahjong_number() + finalDong * 2));
                        } else if (mj.getMahjong_number() >= weina_top.size()) {
                            mj.setDirection(3);
                            mj.setMahjong_number(-finalDong * 2 - (mj.getMahjong_number() - weina_top.size()));
                        }
                    }
                });
                weina_left.forEach(mj -> {
                    if (mj.getMahjong_number() % 2 != 0) {
                        mj.setMahjong_number(mj.getMahjong_number() - finalDong * 2);
                        if (mj.getMahjong_number() < 0) {
                            mj.setDirection(2);
                            mj.setMahjong_number(weina_top.size() - (mj.getMahjong_number() + finalDong * 2));
                        } else if (mj.getMahjong_number() >= weina_left.size()) {
                            mj.setDirection(0);
                            mj.setMahjong_number(-finalDong * 2 - (mj.getMahjong_number() - weina_left.size()));
                        }
                    }
                });
                this.displayWeinaPai();
            }
            this.zhang_combobox.setValue("\u5f20");
        });
        this.dongban_combobox.getItems().addAll("\u680b\u534a", 1, 2, 3, -1, -2, -3);
        this.dongban_combobox.setValue("\u680b\u534a");
        this.dongban_combobox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue instanceof Integer) {
                List<Mj> weina_right = Config.weina.stream().filter(mj -> mj.getDirection() == rightPanel.getDirection()).collect(Collectors.toList());
                List<Mj> weina_bottom = Config.weina.stream().filter(mj -> mj.getDirection() == bottomPanel.getDirection()).collect(Collectors.toList());
                List<Mj> weina_top = Config.weina.stream().filter(mj -> mj.getDirection() == topPanel.getDirection()).collect(Collectors.toList());
                List<Mj> weina_left = Config.weina.stream().filter(mj -> mj.getDirection() == leftPanel.getDirection()).collect(Collectors.toList());
                int dong = (Integer)newValue;
                int finalDong = dong = -1 * dong;
                weina_bottom.forEach(mj -> {
                    if (mj.getMahjong_number() % 2 != 0) {
                        if (finalDong < 0) {
                            mj.setMahjong_number(mj.getMahjong_number() - (finalDong * 2 - 2));
                        } else {
                            mj.setMahjong_number(mj.getMahjong_number() - (finalDong * 2 + 2));
                        }
                        if (mj.getMahjong_number() < 0) {
                            mj.setDirection(3);
                            mj.setMahjong_number(weina_left.size() - (mj.getMahjong_number() + (finalDong * 2 + 2)));
                        } else if (mj.getMahjong_number() >= weina_bottom.size()) {
                            mj.setDirection(1);
                            mj.setMahjong_number(-finalDong * 2 + 2 - (mj.getMahjong_number() - weina_bottom.size()));
                        }
                    } else {
                        mj.setMahjong_number(mj.getMahjong_number() - finalDong * 2);
                        if (mj.getMahjong_number() < 0) {
                            mj.setDirection(3);
                            mj.setMahjong_number(weina_left.size() - (mj.getMahjong_number() + finalDong * 2) - 2);
                        } else if (mj.getMahjong_number() >= weina_bottom.size()) {
                            mj.setDirection(1);
                            mj.setMahjong_number(weina_bottom.size() - (mj.getMahjong_number() + finalDong * 2) - 2);
                        }
                    }
                });
                weina_right.forEach(mj -> {
                    if (mj.getMahjong_number() % 2 != 0) {
                        if (finalDong < 0) {
                            mj.setMahjong_number(mj.getMahjong_number() - (finalDong * 2 - 2));
                        } else {
                            mj.setMahjong_number(mj.getMahjong_number() - (finalDong * 2 + 2));
                        }
                        if (mj.getMahjong_number() < 0) {
                            mj.setDirection(0);
                            mj.setMahjong_number(weina_bottom.size() - (mj.getMahjong_number() + (finalDong * 2 + 2)));
                        } else if (mj.getMahjong_number() >= weina_right.size()) {
                            mj.setDirection(2);
                            mj.setMahjong_number(-finalDong * 2 + 2 - (mj.getMahjong_number() - weina_right.size()));
                        }
                    } else {
                        mj.setMahjong_number(mj.getMahjong_number() - finalDong * 2);
                        if (mj.getMahjong_number() < 0) {
                            mj.setDirection(0);
                            mj.setMahjong_number(weina_bottom.size() - (mj.getMahjong_number() + finalDong * 2) - 2);
                        } else if (mj.getMahjong_number() >= weina_right.size()) {
                            mj.setDirection(2);
                            mj.setMahjong_number(weina_right.size() - (mj.getMahjong_number() + finalDong * 2) - 2);
                        }
                    }
                });
                weina_top.forEach(mj -> {
                    if (mj.getMahjong_number() % 2 != 0) {
                        if (finalDong < 0) {
                            mj.setMahjong_number(mj.getMahjong_number() - (finalDong * 2 - 2));
                        } else {
                            mj.setMahjong_number(mj.getMahjong_number() - (finalDong * 2 + 2));
                        }
                        if (mj.getMahjong_number() < 0) {
                            mj.setDirection(1);
                            mj.setMahjong_number(weina_right.size() - (mj.getMahjong_number() + (finalDong * 2 + 2)));
                        } else if (mj.getMahjong_number() > weina_top.size()) {
                            mj.setDirection(3);
                            mj.setMahjong_number(-finalDong * 2 + 2 - (mj.getMahjong_number() - weina_top.size()));
                        }
                    } else {
                        mj.setMahjong_number(mj.getMahjong_number() - finalDong * 2);
                        if (mj.getMahjong_number() < 0) {
                            mj.setDirection(1);
                            mj.setMahjong_number(weina_right.size() - (mj.getMahjong_number() + finalDong * 2) - 2);
                        } else if (mj.getMahjong_number() >= weina_top.size()) {
                            mj.setDirection(3);
                            mj.setMahjong_number(weina_top.size() - (mj.getMahjong_number() + finalDong * 2) - 2);
                        }
                    }
                });
                weina_left.forEach(mj -> {
                    if (mj.getMahjong_number() % 2 != 0) {
                        if (finalDong < 0) {
                            mj.setMahjong_number(mj.getMahjong_number() - (finalDong * 2 - 2));
                        } else {
                            mj.setMahjong_number(mj.getMahjong_number() - (finalDong * 2 + 2));
                        }
                        if (mj.getMahjong_number() < 0) {
                            mj.setDirection(2);
                            mj.setMahjong_number(weina_top.size() - (mj.getMahjong_number() + (finalDong * 2 + 2)));
                        } else if (mj.getMahjong_number() > weina_left.size()) {
                            mj.setDirection(0);
                            mj.setMahjong_number(-finalDong * 2 + 2 - (mj.getMahjong_number() - weina_left.size()));
                        }
                    } else {
                        mj.setMahjong_number(mj.getMahjong_number() - finalDong * 2);
                        if (mj.getMahjong_number() < 0) {
                            mj.setDirection(2);
                            mj.setMahjong_number(weina_top.size() - (mj.getMahjong_number() + finalDong * 2) - 2);
                        } else if (mj.getMahjong_number() >= weina_left.size()) {
                            mj.setDirection(0);
                            mj.setMahjong_number(weina_left.size() - (mj.getMahjong_number() + finalDong * 2) - 2);
                        }
                    }
                });
                this.displayWeinaPai();
            }
            this.dongban_combobox.setValue("\u680b\u534a");
        });
        bottom_zhuang_label = new Label();
        bottom_zhuang_label.setStyle("-fx-background-color:yellow;-fx-border-radius:5px;-fx-background-radius:5px;-fx-text-fill:black;-fx-font-size: 40px;-fx-padding:5px;-fx-font-weight: 900;-fx-font-family: '\u534e\u6587\u884c\u6977'");
        bottom_zhuang_label.setTextAlignment(TextAlignment.CENTER);
        bottom_panel.getChildren().add(bottom_zhuang_label);
        this.guize_btn.setOnAction(event -> {
            Stage popup = myController.getStage("SettingView");
            popup.show();
            popup.setOnCloseRequest(event1 -> {
                HuPaiKit.kehu_list.clear();
                this.main_panel.setBackground(new Background(new BackgroundFill(Color.color(Config.systemConfig.zidingyibeijingyanse[0], Config.systemConfig.zidingyibeijingyanse[1], Config.systemConfig.zidingyibeijingyanse[2]), CornerRadii.EMPTY, Insets.EMPTY)));
                ObjectKit.WriteObject(Config.systemConfig, Config.config_file_url);
                Config.MJ_WIDTH = (Config.SCREEN_HEIGHT - 50.0) / (double)(Config.systemConfig.wanjiapaishu + 2 + 2);
                Config.MJ_HEIGHT = Config.MJ_WIDTH * 1.37;
                Config.MJ_HEIGHT_1_2 = Config.MJ_HEIGHT / 2.0;
                Config.MJ_WIDTH_1_2 = Config.MJ_WIDTH / 2.0;
                Config.MJ_HEIGHT_3_4 = Config.MJ_HEIGHT * 3.0 / 4.0;
                Config.MJ_WIDTH_3_4 = Config.MJ_WIDTH * 3.0 / 4.0;
            });
        });
        this.chexiao_btn.setOnAction(event -> this.chexiao());
        this.huanyuan_btn.setOnAction(event -> this.huanyuan());
        this.jieshu_btn.setOnAction(event -> this.jieshu());
        this.huoqu_btn.setOnAction(event -> {
            if (param.user.getJihuo_status() == 1) {
                this.huoqu();
            } else {
                MainController.showTextDialog("\u8bf7\u5148\u6fc0\u6d3b");
                this.jieshu_btn.setVisible(true);
            }
        });
        this.jihuo_btn.setOnAction(event -> this.jihuo());
        this.fenxi_btn.setOnAction(event -> {
            if (param.user.getJihuo_status() == 1) {
                if (this.dangqianba.size() > 0) {
                    this.jieshu();
                    Config.weina.clear();
                    Config.weina.addAll(this.dangqianba);
                    this.shangyiba.clear();
                    this.shangyiba.addAll(Config.weina);
                    this.begingamge();
                }
            } else {
                MainController.showTextDialog("\u8bf7\u5148\u6fc0\u6d3b");
                this.jieshu_btn.setVisible(true);
            }
        });
        this.xuanzhuan_btn.setOnAction(event -> {
            directions.forEach(dangqianba -> {
                int d = dangqianba.getDirection();
                if (--d < 0) {
                    d = 3;
                }
                dangqianba.setDirection(d);
                if (directions_temp.size() < 4) {
                    directions_temp.add((MjPanelInterface)dangqianba);
                }
            });
            directions.sort(new ComparatorD());
            this.main_panel.getChildren().remove(zhuangPanel);
            zhuangPanel = new ZhuangPanel();
            this.begingamge();
        });
        this.out_btn.setOnAction(event -> {
            int code = LocalUDPDataSender.getInstance().sendLoginout();
            IMClientManager.getInstance().resetInitFlag();
            IMClientManager.getInstance().release();
            System.exit(0);
        });
        this.min_btn.setOnAction(event -> myController.getStage("IndexView").setIconified(true));
        this.main_panel.getParent().setOnMouseClicked(e -> {
            MouseButton button = e.getButton();
            switch (button) {
                case PRIMARY: {
                    break;
                }
                case SECONDARY: {
                    if ("\u64a4\u9500".equals(Config.systemConfig.shubiaoyouji)) {
                        this.chexiao();
                    }
                    if (!"\u6478\u724c".equals(Config.systemConfig.shubiaoyouji) || directions.stream().filter(direction -> direction.getMo() == 1).count() != 1L) break;
                    directions.forEach(direction -> {
                        if (direction.getMo() == 1) {
                            dapaifangx = direction.getDirection();
                        }
                    });
                    MainController.mopai();
                    break;
                }
                case MIDDLE: {
                    break;
                }
            }
        });
        this.onLoginSucessObserver = new Observer(){

            @Override
            public void update(java.util.Observable observable, Object data) {
                int code = (Integer)data;
                if (code == 0) {
                    MainController.this.jiance_service.shutdown();
                } else {
                    MainController.this.doZhuoLoginImpl();
                }
            }
        };
        this.jiuzhenghuanpai_btn.setOnAction(event -> {
            if (this.huanhoupai_yixuan.size() <= 0) {
                MainController.showTextDialog("\u6362\u724c\u540e\u624d\u53ef\u8fdb\u884c\u7ea0\u6b63");
                return;
            }
            System.out.println("\u6362\u724c\u7ea0\u6b63");
            MainController.saveState();
            Config.yina.forEach(mj -> mj.getYina_imageView().getImageView().setOnMouseClicked(event2 -> {
                if (mj.getYina_imageView().hasClass("jiuzheng_huanpai_biaoji")) {
                    mj.getYina_imageView().removeClass("jiuzheng_huanpai_biaoji");
                    if (mj.getNapai_direction() == leftPanel.getDirection() || mj.getNapai_direction() == rightPanel.getDirection()) {
                        if (!Config.huanhoupai_yixuan.contains(mj)) {
                            mj.getYina_imageView().setImage(ImageKit.rotate90SX(Config.getImageByMj(mj)));
                        }
                    } else {
                        mj.getYina_imageView().setImage(Config.getImageByMj(mj));
                    }
                } else {
                    mj.getYina_imageView().addClass("jiuzheng_huanpai_biaoji");
                    mj.getYina_imageView().setImage(ImageKit.coverImage(mj.getYina_imageView().getImage(), Config.HONGSE));
                }
            }));
            JiuZhengHuanShouPaiPanel jiuZhengHuanShouPaiPanel = new JiuZhengHuanShouPaiPanel();
            this.main_panel.getChildren().add(jiuZhengHuanShouPaiPanel);
            this.main_panel.layout();
            jiuZhengHuanShouPaiPanel.layout();
            jiuZhengHuanShouPaiPanel.setLayoutY(this.main_panel.getHeight() / 2.0 - jiuZhengHuanShouPaiPanel.getHeight());
            jiuZhengHuanShouPaiPanel.setLayoutX(this.main_panel.getWidth() / 2.0 - jiuZhengHuanShouPaiPanel.getWidth() / 2.0);
            Observable.create(emitter -> {
                jiuZhengHuanShouPaiPanel.getQuxiaojiuzheng().setOnMouseClicked(event3 -> emitter.onNext(1));
                jiuZhengHuanShouPaiPanel.getQuerengjiuzheng().setOnMouseClicked(event3 -> {
                    System.out.println("*******\u786e\u8ba4\u4ea4\u6362");
                    int[] jiuzheng_drections = new int[2];
                    int[] drection_cout = new int[]{0};
                    boolean[] \u662f\u5426\u80fd\u4ea4\u6362 = new boolean[]{true};
                    directions.forEach(mjPanelInterface -> {
                        if (Config.yina.stream().filter(mj -> mj.getNapai_direction() == mjPanelInterface.getDirection() && mj.getYina_imageView().hasClass("jiuzheng_huanpai_biaoji")).count() > (long)Config.systemConfig.huanshoupai.intValue()) {
                            \u662f\u5426\u80fd\u4ea4\u6362[0] = false;
                        }
                        if (Config.yina.stream().filter(mj -> mj.getNapai_direction() == mjPanelInterface.getDirection() && mj.getYina_imageView().hasClass("jiuzheng_huanpai_biaoji")).count() > 0L) {
                            System.out.println("\u6267\u884c\u6b21\u6570");
                            if (drection_cout[0] <= 1) {
                                jiuzheng_drections[drection_cout[0]] = mjPanelInterface.getDirection();
                            }
                            drection_cout[0] = drection_cout[0] + 1;
                        }
                    });
                    if (drection_cout[0] != 2) {
                        MainController.showTextDialog("\u9009\u62e9\u4e24\u65b9\u7684\u724c\u624d\u53ef\u4ea4\u6362");
                        return;
                    }
                    if (!\u662f\u5426\u80fd\u4ea4\u6362[0]) {
                        MainController.showTextDialog("\u9009\u62e9\u7684\u724c\u6709\u9519\u8bef");
                        return;
                    }
                    System.out.println("\u6240\u9009\u7684\u4e24\u4e2a\u65b9\u5411:" + Arrays.toString(jiuzheng_drections));
                    int a = jiuzheng_drections[0];
                    int b = jiuzheng_drections[1];
                    Config.yina.forEach(mj -> {
                        int d = mj.getNapai_direction();
                        if (d == a) {
                            d = b;
                        } else if (d == b) {
                            d = a;
                        }
                        if (mj.getYina_imageView().hasClass("jiuzheng_huanpai_biaoji")) {
                            Config.jiuzhengpai_yixuan.add((Mj)mj.clone());
                            mj.setNapai_direction(d);
                        }
                    });
                    emitter.onNext(1);
                });
            }).subscribe(integer3 -> {
                this.main_panel.getChildren().remove(jiuZhengHuanShouPaiPanel);
                MainController.clear();
                this.NaPaiHouXianShi();
            });
        });
    }

    private void huanShouPai() {
        if (Config.systemConfig.huanshoupai > 0) {
            MainController.clear();
            MainController.displayYinaPai();
            MainController.saveState();
            Config.yina.forEach(mj -> {
                if (this.huanhoupai_yixuan.contains(mj)) {
                    mj.getYina_imageView().addClass("huanpai_biaoji");
                    mj.getYina_imageView().setImage(ImageKit.coverImage(mj.getYina_imageView().getImage(), Config.LVSE));
                }
            });
            this.huanhoupai_yixuan.clear();
            Config.huanhoupai_yixuan.clear();
            Config.yina.forEach(mj -> mj.getYina_imageView().getImageView().setOnMouseClicked(event -> {
                if (mj.getYina_imageView().hasClass("huanpai_biaoji")) {
                    mj.getYina_imageView().removeClass("huanpai_biaoji");
                    if (mj.getNapai_direction() == leftPanel.getDirection() || mj.getNapai_direction() == rightPanel.getDirection()) {
                        mj.getYina_imageView().setImage(ImageKit.rotate90SX(Config.getImageByMj(mj)));
                    } else {
                        mj.getYina_imageView().setImage(Config.getImageByMj(mj));
                    }
                } else {
                    mj.getYina_imageView().addClass("huanpai_biaoji");
                    mj.getYina_imageView().setImage(ImageKit.coverImage(mj.getYina_imageView().getImage(), Config.LVSE));
                }
            }));
            HuanShouPaiPanel huanShouPaiPanel = new HuanShouPaiPanel();
            this.main_panel.getChildren().add(huanShouPaiPanel);
            this.main_panel.layout();
            huanShouPaiPanel.layout();
            huanShouPaiPanel.setLayoutY(this.main_panel.getHeight() / 2.0 - huanShouPaiPanel.getHeight());
            huanShouPaiPanel.setLayoutX(this.main_panel.getWidth() / 2.0 - huanShouPaiPanel.getWidth() / 2.0);
            huanShouPaiPanel.getTop_text().setText("\u4ea4\u6362\u624b\u724c(" + Config.systemConfig.huanshoupai + ")\u5f20");
            Observable.create(emitter -> {
                huanShouPaiPanel.getQuxiaojiaohuan().setOnMouseClicked(event -> emitter.onNext(1));
                huanShouPaiPanel.getNashangjiapai_xunhuan().setOnMouseClicked(event -> {
                    boolean[] \u662f\u5426\u80fd\u4ea4\u6362 = new boolean[]{true};
                    directions.forEach(mjPanelInterface -> {
                        if (!mjPanelInterface.isQueren() && Config.yina.stream().filter(mj -> mj.getNapai_direction() == mjPanelInterface.getDirection() && mj.getYina_imageView().hasClass("huanpai_biaoji")).count() != (long)Config.systemConfig.huanshoupai.intValue()) {
                            \u662f\u5426\u80fd\u4ea4\u6362[0] = false;
                        }
                    });
                    if (!\u662f\u5426\u80fd\u4ea4\u6362[0]) {
                        MainController.showTextDialog("\u9009\u62e9\u7684\u724c\u6709\u9519\u8bef");
                        return;
                    }
                    Config.yina.forEach(mj -> {
                        int d = mj.getNapai_direction();
                        do {
                            if (--d >= 0) continue;
                            d = 3;
                        } while (directions.get(d).isQueren());
                        if (mj.getYina_imageView().hasClass("huanpai_biaoji")) {
                            this.huanhoupai_yixuan.add((Mj)mj.clone());
                            Config.huanhoupai_yixuan.add((Mj)mj.clone());
                            mj.setNapai_direction(d);
                        }
                    });
                    emitter.onNext(1);
                });
                huanShouPaiPanel.getNaxianghjipai_xunhuan().setOnMouseClicked(event -> {
                    boolean[] \u662f\u5426\u80fd\u4ea4\u6362 = new boolean[]{true};
                    directions.forEach(mjPanelInterface -> {
                        if (!mjPanelInterface.isQueren() && Config.yina.stream().filter(mj -> mj.getNapai_direction() == mjPanelInterface.getDirection() && mj.getYina_imageView().hasClass("huanpai_biaoji")).count() != (long)Config.systemConfig.huanshoupai.intValue()) {
                            \u662f\u5426\u80fd\u4ea4\u6362[0] = false;
                        }
                    });
                    if (!\u662f\u5426\u80fd\u4ea4\u6362[0]) {
                        MainController.showTextDialog("\u9009\u62e9\u7684\u724c\u6709\u9519\u8bef");
                        return;
                    }
                    Config.yina.forEach(mj -> {
                        int d = mj.getNapai_direction();
                        do {
                            if (++d <= 3) continue;
                            d = 0;
                        } while (directions.get(d).isQueren());
                        if (mj.getYina_imageView().hasClass("huanpai_biaoji")) {
                            this.huanhoupai_yixuan.add((Mj)mj.clone());
                            Config.huanhoupai_yixuan.add((Mj)mj.clone());
                            mj.setNapai_direction(d);
                        }
                    });
                    emitter.onNext(1);
                });
                huanShouPaiPanel.getNaxiajiapan().setOnMouseClicked(event -> {
                    boolean[] \u662f\u5426\u80fd\u4ea4\u6362 = new boolean[]{true};
                    directions.forEach(mjPanelInterface -> {
                        if (Config.yina.stream().filter(mj -> mj.getNapai_direction() == mjPanelInterface.getDirection() && mj.getYina_imageView().hasClass("huanpai_biaoji")).count() != (long)Config.systemConfig.huanshoupai.intValue()) {
                            \u662f\u5426\u80fd\u4ea4\u6362[0] = false;
                        }
                    });
                    if (!\u662f\u5426\u80fd\u4ea4\u6362[0]) {
                        MainController.showTextDialog("\u9009\u62e9\u7684\u724c\u6709\u9519\u8bef");
                        return;
                    }
                    Config.yina.forEach(mj -> {
                        int d = mj.getNapai_direction();
                        switch (d) {
                            case 0: {
                                d = 1;
                                break;
                            }
                            case 1: {
                                d = 0;
                                break;
                            }
                            case 2: {
                                d = 3;
                                break;
                            }
                            case 3: {
                                d = 2;
                            }
                        }
                        if (mj.getYina_imageView().hasClass("huanpai_biaoji")) {
                            this.huanhoupai_yixuan.add((Mj)mj.clone());
                            Config.huanhoupai_yixuan.add((Mj)mj.clone());
                            mj.setNapai_direction(d);
                        }
                    });
                    emitter.onNext(1);
                });
                huanShouPaiPanel.getNashangjiapai().setOnMouseClicked(event -> {
                    boolean[] \u662f\u5426\u80fd\u4ea4\u6362 = new boolean[]{true};
                    directions.forEach(mjPanelInterface -> {
                        if (Config.yina.stream().filter(mj -> mj.getNapai_direction() == mjPanelInterface.getDirection() && mj.getYina_imageView().hasClass("huanpai_biaoji")).count() != (long)Config.systemConfig.huanshoupai.intValue()) {
                            \u662f\u5426\u80fd\u4ea4\u6362[0] = false;
                        }
                    });
                    if (!\u662f\u5426\u80fd\u4ea4\u6362[0]) {
                        MainController.showTextDialog("\u9009\u62e9\u7684\u724c\u6709\u9519\u8bef");
                        return;
                    }
                    Config.yina.forEach(mj -> {
                        int d = mj.getNapai_direction();
                        switch (d) {
                            case 0: {
                                d = 3;
                                break;
                            }
                            case 1: {
                                d = 2;
                                break;
                            }
                            case 2: {
                                d = 1;
                                break;
                            }
                            case 3: {
                                d = 0;
                            }
                        }
                        if (mj.getYina_imageView().hasClass("huanpai_biaoji")) {
                            this.huanhoupai_yixuan.add((Mj)mj.clone());
                            Config.huanhoupai_yixuan.add((Mj)mj.clone());
                            mj.setNapai_direction(d);
                        }
                    });
                    emitter.onNext(1);
                });
                huanShouPaiPanel.getNaduijiapai().setOnMouseClicked(event -> {
                    boolean[] \u662f\u5426\u80fd\u4ea4\u6362 = new boolean[]{true};
                    directions.forEach(mjPanelInterface -> {
                        if (Config.yina.stream().filter(mj -> mj.getNapai_direction() == mjPanelInterface.getDirection() && mj.getYina_imageView().hasClass("huanpai_biaoji")).count() != (long)Config.systemConfig.huanshoupai.intValue()) {
                            \u662f\u5426\u80fd\u4ea4\u6362[0] = false;
                        }
                    });
                    if (!\u662f\u5426\u80fd\u4ea4\u6362[0]) {
                        MainController.showTextDialog("\u9009\u62e9\u7684\u724c\u6709\u9519\u8bef");
                        return;
                    }
                    Config.yina.forEach(mj -> {
                        int d = mj.getNapai_direction();
                        if ((d += 2) > 4) {
                            d = 1;
                        }
                        if (d > 3) {
                            d = 0;
                        }
                        if (mj.getYina_imageView().hasClass("huanpai_biaoji")) {
                            this.huanhoupai_yixuan.add((Mj)mj.clone());
                            Config.huanhoupai_yixuan.add((Mj)mj.clone());
                            mj.setNapai_direction(d);
                        }
                    });
                    emitter.onNext(1);
                });
            }).subscribe(integer3 -> {
                this.main_panel.getChildren().remove(huanShouPaiPanel);
                MainController.clear();
                this.NaPaiHouXianShi();
            });
        } else {
            MainController.clear();
            this.NaPaiHouXianShi();
        }
    }

    private void guozhang() {
        zhuangPanel.setGuopai();
        this.displayWeinaPai();
        Config.weina.forEach(mj -> {
            ImageView imageView;
            AnchorPane anchorPane;
            Label label;
            mj.getWeina_imageView().setOnMouseClicked(event -> {
                MouseButton button = event.getButton();
                switch (button) {
                    case SECONDARY: {
                        List<Mj> mjList = Config.weina.stream().filter(mj1 -> mj1.getDirection() == mj.getDirection()).collect(Collectors.toList());
                        mjList.sort(new ComparatorMj());
                        if (mjList.stream().filter(mj1 -> mj1.getWeina_imageView().getStyleClass().contains("mj_guozhang")).count() <= 0L) break;
                        int begin = -1;
                        int end = 0;
                        for (Mj mj2 : mjList) {
                            if (!mj2.getWeina_imageView().getStyleClass().contains("mj_guozhang")) continue;
                            if (begin == -1) {
                                begin = mj2.getMahjong_number();
                            }
                            end = mj2.getMahjong_number();
                        }
                        int size_lam = end - begin;
                        if (mj.getMahjong_number() > end) {
                            for (Mj mj13 : mjList) {
                                if (mj13.getMahjong_number() >= mj.getMahjong_number() / 2 * 2 || mj13.getMahjong_number() % 2 == 0) continue;
                                if (mj13.getMahjong_number() >= begin && mj13.getMahjong_number() <= end) {
                                    mj13.setMahjong_number(mj13.getMahjong_number() + (mj.getMahjong_number() / 2 * 2 - 1 - end));
                                    continue;
                                }
                                if (mj13.getMahjong_number() <= end) continue;
                                mj13.setMahjong_number(mj13.getMahjong_number() - size_lam - 2);
                            }
                        }
                        if (mj.getMahjong_number() < begin) {
                            for (Mj mj12 : mjList) {
                                if (mj12.getMahjong_number() <= mj.getMahjong_number() / 2 * 2 + 1 || mj12.getMahjong_number() % 2 == 0) continue;
                                if (mj12.getMahjong_number() >= begin && mj12.getMahjong_number() <= end) {
                                    mj12.setMahjong_number(mj12.getMahjong_number() - (begin - (mj.getMahjong_number() / 2 * 2 + 3)));
                                    continue;
                                }
                                if (mj12.getMahjong_number() >= begin) continue;
                                mj12.setMahjong_number(mj12.getMahjong_number() + size_lam + 2);
                            }
                        }
                        if (topPanel.getDirection() == mj.getDirection()) {
                            double finalX = Config.SCREEN_WIDTH / 2.0 + Config.MJ_WIDTH_1_2 * (double)(mjList.size() / 2) / 2.0 - 5.0;
                            mjList.forEach(mj1 -> {
                                mj1.getWeina_imageView().getStyleClass().remove("mj_guozhang");
                                mj1.getWeina_imageView().setLayoutX(finalX - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                            });
                        }
                        if (bottomPanel.getDirection() == mj.getDirection()) {
                            double finalX = Config.SCREEN_WIDTH / 2.0 - Config.MJ_WIDTH_1_2 * (double)(mjList.size() / 2) / 2.0 + Config.MJ_WIDTH_1_2;
                            mjList.forEach(mj1 -> {
                                mj1.getWeina_imageView().getStyleClass().remove("mj_guozhang");
                                mj1.getWeina_imageView().setLayoutX(finalX + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                            });
                        }
                        if (leftPanel.getDirection() == mj.getDirection()) {
                            double finalY3 = Config.SCREEN_HEIGHT / 2.0 - Config.MJ_WIDTH_1_2 * (double)(mjList.size() / 2) / 2.0 + Config.MJ_WIDTH_1_2;
                            mjList.forEach(mj1 -> {
                                mj1.getWeina_imageView().getStyleClass().remove("mj_guozhang");
                                mj1.getWeina_imageView().setLayoutY(finalY3 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                            });
                        }
                        if (rightPanel.getDirection() != mj.getDirection()) break;
                        double finalY1 = Config.SCREEN_HEIGHT / 2.0 + Config.MJ_WIDTH_1_2 * (double)(mjList.size() / 2) / 2.0 + Config.MJ_WIDTH_1_2;
                        mjList.forEach(mj1 -> {
                            mj1.getWeina_imageView().getStyleClass().remove("mj_guozhang");
                            mj1.getWeina_imageView().setLayoutY(finalY1 - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                        });
                        break;
                    }
                    case PRIMARY: {
                        if (!mj.getWeina_imageView().getStyleClass().contains("mj_guozhang")) {
                            if (mj.getMahjong_number() % 2 == 0) {
                                Config.weina.forEach(mj1 -> {
                                    if (mj1.getMahjong_number() == mj.getMahjong_number() + 1 && mj1.getDirection() == mj.getDirection() && !mj1.getWeina_imageView().getStyleClass().contains("mj_guozhang")) {
                                        mj1.getWeina_imageView().getStyleClass().add("mj_guozhang");
                                    }
                                });
                                break;
                            }
                            if (mj.getWeina_imageView().getStyleClass().contains("mj_guozhang")) break;
                            mj.getWeina_imageView().getStyleClass().add("mj_guozhang");
                            break;
                        }
                        mj.getWeina_imageView().getStyleClass().remove("mj_guozhang");
                    }
                }
            });
            if (mj.getDirection() == bottomPanel.getDirection()) {
                long size = Config.weina.stream().filter(mj1 -> mj1.getDirection() == bottomPanel.getDirection()).count();
                double finalX2 = Config.SCREEN_WIDTH / 2.0 - Config.MJ_WIDTH_1_2 * (double)(size / 2L) / 2.0 + Config.MJ_WIDTH_1_2;
                if (mj.getMahjong_number() % 2 != 0) {
                    label = new Label();
                    if (mj.getMahjong_number() < 9) {
                        int c = (mj.getMahjong_number() + 1) / 2;
                        if (c == 1) {
                            anchorPane = new AnchorPane();
                            imageView = new ImageView(new Image(MainController.class.getResourceAsStream("/quesheng/view/image/guopai_guozhang.png")));
                            anchorPane.getChildren().add(imageView);
                            imageView.setFitHeight(mj.getWeina_imageView().getFitWidth());
                            imageView.setFitWidth(mj.getWeina_imageView().getFitWidth() - 2.0);
                            this.main_panel.getChildren().add(anchorPane);
                            this.guozuang_top_label.add(anchorPane);
                            anchorPane.getStyleClass().add("guopai_label_xuanze");
                            anchorPane.setLayoutX(mj.getWeina_imageView().getLayoutX());
                            anchorPane.setLayoutY(mj.getWeina_imageView().getLayoutY() + mj.getWeina_imageView().getFitHeight() * 2.0);
                            anchorPane.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == bottomPanel.getDirection()).forEach(mj1 -> {
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() - c * 2);
                                    if (mj1.getMahjong_number() < 0) {
                                        mj1.setMahjong_number((int)(size + (long)mj1.getMahjong_number()));
                                    }
                                    mj1.getWeina_imageView().setLayoutX(finalX2 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                }
                            }));
                        } else {
                            label.setText("" + c);
                            this.main_panel.getChildren().add(label);
                            this.guozuang_top_label.add(label);
                            label.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == bottomPanel.getDirection()).forEach(mj1 -> {
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() - c * 2);
                                    if (mj1.getMahjong_number() < 0) {
                                        mj1.setMahjong_number((int)(size + (long)mj1.getMahjong_number()));
                                    }
                                    mj1.getWeina_imageView().setLayoutX(finalX2 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                }
                            }));
                        }
                    }
                    if ((long)mj.getMahjong_number() > size - 9L) {
                        int c = (int)(size - (long)mj.getMahjong_number() + 1L) / 2;
                        if (c == 1) {
                            anchorPane = new AnchorPane();
                            imageView = new ImageView(ImageKit.rotate(new Image(MainController.class.getResourceAsStream("/quesheng/view/image/guopai_guozhang.png")), 180));
                            anchorPane.getChildren().add(imageView);
                            imageView.setFitHeight(mj.getWeina_imageView().getFitWidth());
                            imageView.setFitWidth(mj.getWeina_imageView().getFitWidth() - 2.0);
                            this.main_panel.getChildren().add(anchorPane);
                            this.guozuang_top_label.add(anchorPane);
                            anchorPane.getStyleClass().add("guopai_label_xuanze");
                            anchorPane.setLayoutX(mj.getWeina_imageView().getLayoutX());
                            anchorPane.setLayoutY(mj.getWeina_imageView().getLayoutY() + mj.getWeina_imageView().getFitHeight() * 2.0);
                            anchorPane.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == bottomPanel.getDirection()).forEach(mj1 -> {
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() + c * 2);
                                    if ((long)mj1.getMahjong_number() > size - 1L) {
                                        mj1.setMahjong_number((int)((long)mj1.getMahjong_number() - size));
                                    }
                                    mj1.getWeina_imageView().setLayoutX(finalX2 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                }
                            }));
                        } else {
                            label.setText("" + c);
                            this.main_panel.getChildren().add(label);
                            this.guozuang_top_label.add(label);
                            label.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == bottomPanel.getDirection()).forEach(mj1 -> {
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() + c * 2);
                                    if ((long)mj1.getMahjong_number() > size - 1L) {
                                        mj1.setMahjong_number((int)((long)mj1.getMahjong_number() - size));
                                    }
                                    mj1.getWeina_imageView().setLayoutX(finalX2 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                }
                            }));
                        }
                    }
                    label.setPrefWidth(mj.getWeina_imageView().getFitWidth() - 2.0);
                    label.setPrefHeight(mj.getWeina_imageView().getFitWidth());
                    label.setAlignment(Pos.CENTER);
                    label.getStyleClass().add("guopai_label_xuanze");
                    label.setLayoutX(mj.getWeina_imageView().getLayoutX());
                    label.setLayoutY(mj.getWeina_imageView().getLayoutY() + mj.getWeina_imageView().getFitHeight() * 2.0);
                }
            }
            if (mj.getDirection() == rightPanel.getDirection()) {
                long size = Config.weina.stream().filter(mj1 -> mj1.getDirection() == rightPanel.getDirection()).count();
                double finalY1 = Config.SCREEN_HEIGHT / 2.0 + Config.MJ_WIDTH_1_2 * (double)(size / 2L) / 2.0 + Config.MJ_WIDTH_1_2;
                if (mj.getMahjong_number() % 2 != 0) {
                    label = new Label();
                    if (mj.getMahjong_number() < 9) {
                        int c = (mj.getMahjong_number() + 1) / 2;
                        if (c == 1) {
                            anchorPane = new AnchorPane();
                            imageView = new ImageView(ImageKit.rotate(new Image(MainController.class.getResourceAsStream("/quesheng/view/image/guopai_guozhang.png")), -90));
                            anchorPane.getChildren().add(imageView);
                            imageView.setFitHeight(mj.getWeina_imageView().getFitWidth() - 2.0);
                            imageView.setFitWidth(mj.getWeina_imageView().getFitWidth());
                            this.main_panel.getChildren().add(anchorPane);
                            this.guozuang_top_label.add(anchorPane);
                            anchorPane.getStyleClass().add("guopai_label_xuanze");
                            anchorPane.setLayoutX(mj.getWeina_imageView().getLayoutX() + mj.getWeina_imageView().getFitHeight() * 2.0);
                            anchorPane.setLayoutY(mj.getWeina_imageView().getLayoutY() - imageView.getFitHeight());
                            anchorPane.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == rightPanel.getDirection()).forEach(mj1 -> {
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() - c * 2);
                                    if (mj1.getMahjong_number() < 0) {
                                        mj1.setMahjong_number((int)(size + (long)mj1.getMahjong_number()));
                                    }
                                    mj1.getWeina_imageView().setLayoutY(finalY1 - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                }
                            }));
                        } else {
                            label.setText("" + c);
                            this.main_panel.getChildren().add(label);
                            this.guozuang_top_label.add(label);
                            label.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == rightPanel.getDirection()).forEach(mj1 -> {
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() - c * 2);
                                    if (mj1.getMahjong_number() < 0) {
                                        mj1.setMahjong_number((int)(size + (long)mj1.getMahjong_number()));
                                    }
                                    mj1.getWeina_imageView().setLayoutY(finalY1 - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                }
                            }));
                        }
                    }
                    if ((long)mj.getMahjong_number() > size - 9L) {
                        int c = (int)(size - (long)mj.getMahjong_number() + 1L) / 2;
                        if (c == 1) {
                            anchorPane = new AnchorPane();
                            imageView = new ImageView(ImageKit.rotate(new Image(MainController.class.getResourceAsStream("/quesheng/view/image/guopai_guozhang.png")), 90));
                            anchorPane.getChildren().add(imageView);
                            imageView.setFitHeight(mj.getWeina_imageView().getFitWidth() - 2.0);
                            imageView.setFitWidth(mj.getWeina_imageView().getFitWidth());
                            this.main_panel.getChildren().add(anchorPane);
                            this.guozuang_top_label.add(anchorPane);
                            anchorPane.getStyleClass().add("guopai_label_xuanze");
                            anchorPane.setLayoutX(mj.getWeina_imageView().getLayoutX() + mj.getWeina_imageView().getFitHeight() * 2.0);
                            anchorPane.setLayoutY(mj.getWeina_imageView().getLayoutY() - imageView.getFitHeight());
                            anchorPane.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == rightPanel.getDirection()).forEach(mj1 -> {
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() + c * 2);
                                    if ((long)mj1.getMahjong_number() > size - 1L) {
                                        mj1.setMahjong_number((int)((long)mj1.getMahjong_number() - size));
                                    }
                                    mj1.getWeina_imageView().setLayoutY(finalY1 - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                }
                            }));
                        } else {
                            label.setText("" + c);
                            this.main_panel.getChildren().add(label);
                            this.guozuang_top_label.add(label);
                            label.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == rightPanel.getDirection()).forEach(mj1 -> {
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() + c * 2);
                                    if ((long)mj1.getMahjong_number() > size - 1L) {
                                        mj1.setMahjong_number((int)((long)mj1.getMahjong_number() - size));
                                    }
                                    mj1.getWeina_imageView().setLayoutY(finalY1 - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                }
                            }));
                        }
                    }
                    label.setPrefWidth(mj.getWeina_imageView().getFitWidth());
                    label.setPrefHeight(mj.getWeina_imageView().getFitWidth() - 2.0);
                    label.setAlignment(Pos.CENTER);
                    label.getStyleClass().add("guopai_label_xuanze");
                    label.setLayoutX(mj.getWeina_imageView().getLayoutX() + mj.getWeina_imageView().getFitHeight() * 2.0);
                    label.setLayoutY(mj.getWeina_imageView().getLayoutY() - label.getPrefHeight());
                }
            }
            if (mj.getDirection() == topPanel.getDirection()) {
                long size = Config.weina.stream().filter(mj1 -> mj1.getDirection() == topPanel.getDirection()).count();
                double finalX = Config.SCREEN_WIDTH / 2.0 + Config.MJ_WIDTH_1_2 * (double)(size / 2L) / 2.0 - 5.0;
                if (mj.getMahjong_number() % 2 != 0) {
                    label = new Label();
                    if (mj.getMahjong_number() < 9) {
                        int c = (mj.getMahjong_number() + 1) / 2;
                        if (c == 1) {
                            anchorPane = new AnchorPane();
                            imageView = new ImageView(ImageKit.rotate(new Image(MainController.class.getResourceAsStream("/quesheng/view/image/guopai_guozhang.png")), 180));
                            anchorPane.getChildren().add(imageView);
                            imageView.setFitHeight(mj.getWeina_imageView().getFitWidth());
                            imageView.setFitWidth(mj.getWeina_imageView().getFitWidth() - 2.0);
                            this.main_panel.getChildren().add(anchorPane);
                            this.guozuang_top_label.add(anchorPane);
                            anchorPane.getStyleClass().add("guopai_label_xuanze");
                            anchorPane.setLayoutX(mj.getWeina_imageView().getLayoutX());
                            anchorPane.setLayoutY(mj.getWeina_imageView().getLayoutY() - imageView.getFitHeight() - mj.getWeina_imageView().getFitHeight());
                            anchorPane.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == topPanel.getDirection()).forEach(mj1 -> {
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() - c * 2);
                                    if (mj1.getMahjong_number() < 0) {
                                        mj1.setMahjong_number((int)(size + (long)mj1.getMahjong_number()));
                                    }
                                    mj1.getWeina_imageView().setLayoutX(finalX - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                }
                            }));
                        } else {
                            label.setText("" + c);
                            this.main_panel.getChildren().add(label);
                            this.guozuang_top_label.add(label);
                            label.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == topPanel.getDirection()).forEach(mj1 -> {
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() - c * 2);
                                    if (mj1.getMahjong_number() < 0) {
                                        mj1.setMahjong_number((int)(size + (long)mj1.getMahjong_number()));
                                    }
                                    mj1.getWeina_imageView().setLayoutX(finalX - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                }
                            }));
                        }
                    }
                    if ((long)mj.getMahjong_number() > size - 9L) {
                        int c = (int)(size - (long)mj.getMahjong_number() + 1L) / 2;
                        if (c == 1) {
                            anchorPane = new AnchorPane();
                            imageView = new ImageView(ImageKit.rotate(new Image(MainController.class.getResourceAsStream("/quesheng/view/image/guopai_guozhang.png")), 0));
                            anchorPane.getChildren().add(imageView);
                            imageView.setFitHeight(mj.getWeina_imageView().getFitWidth());
                            imageView.setFitWidth(mj.getWeina_imageView().getFitWidth() - 2.0);
                            this.main_panel.getChildren().add(anchorPane);
                            this.guozuang_top_label.add(anchorPane);
                            anchorPane.getStyleClass().add("guopai_label_xuanze");
                            anchorPane.setLayoutX(mj.getWeina_imageView().getLayoutX());
                            anchorPane.setLayoutY(mj.getWeina_imageView().getLayoutY() - imageView.getFitHeight() - mj.getWeina_imageView().getFitHeight());
                            anchorPane.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == topPanel.getDirection()).forEach(mj1 -> {
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() + c * 2);
                                    if ((long)mj1.getMahjong_number() > size - 1L) {
                                        mj1.setMahjong_number((int)((long)mj1.getMahjong_number() - size));
                                    }
                                    mj1.getWeina_imageView().setLayoutX(finalX - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                }
                            }));
                        } else {
                            label.setText("" + c);
                            this.main_panel.getChildren().add(label);
                            this.guozuang_top_label.add(label);
                            label.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == topPanel.getDirection()).forEach(mj1 -> {
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() + c * 2);
                                    if ((long)mj1.getMahjong_number() > size - 1L) {
                                        mj1.setMahjong_number((int)((long)mj1.getMahjong_number() - size));
                                    }
                                    mj1.getWeina_imageView().setLayoutX(finalX - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                }
                            }));
                        }
                    }
                    label.setPrefWidth(mj.getWeina_imageView().getFitWidth() - 2.0);
                    label.setPrefHeight(mj.getWeina_imageView().getFitWidth());
                    label.setAlignment(Pos.CENTER);
                    label.getStyleClass().add("guopai_label_xuanze");
                    label.setLayoutX(mj.getWeina_imageView().getLayoutX());
                    label.setLayoutY(mj.getWeina_imageView().getLayoutY() - label.getPrefHeight() - mj.getWeina_imageView().getFitHeight());
                }
            }
            if (mj.getDirection() == leftPanel.getDirection()) {
                long size = Config.weina.stream().filter(mj1 -> mj1.getDirection() == leftPanel.getDirection()).count();
                double finalY3 = Config.SCREEN_HEIGHT / 2.0 - Config.MJ_WIDTH_1_2 * (double)(size / 2L) / 2.0 + Config.MJ_WIDTH_1_2;
                if (mj.getMahjong_number() % 2 != 0) {
                    label = new Label();
                    if (mj.getMahjong_number() < 9) {
                        int c = (mj.getMahjong_number() + 1) / 2;
                        if (c == 1) {
                            anchorPane = new AnchorPane();
                            imageView = new ImageView(ImageKit.rotate(new Image(MainController.class.getResourceAsStream("/quesheng/view/image/guopai_guozhang.png")), 90));
                            anchorPane.getChildren().add(imageView);
                            imageView.setFitHeight(mj.getWeina_imageView().getFitWidth() - 2.0);
                            imageView.setFitWidth(mj.getWeina_imageView().getFitWidth());
                            this.main_panel.getChildren().add(anchorPane);
                            this.guozuang_top_label.add(anchorPane);
                            anchorPane.getStyleClass().add("guopai_label_xuanze");
                            anchorPane.setLayoutX(mj.getWeina_imageView().getLayoutX() - mj.getWeina_imageView().getFitHeight() * 2.0 - imageView.getFitWidth());
                            anchorPane.setLayoutY(mj.getWeina_imageView().getLayoutY());
                            anchorPane.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == leftPanel.getDirection()).forEach(mj1 -> {
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() - c * 2);
                                    if (mj1.getMahjong_number() < 0) {
                                        mj1.setMahjong_number((int)(size + (long)mj1.getMahjong_number()));
                                    }
                                    mj1.getWeina_imageView().setLayoutY(finalY3 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                }
                            }));
                        } else {
                            label.setText("" + c);
                            this.main_panel.getChildren().add(label);
                            this.guozuang_top_label.add(label);
                            label.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == leftPanel.getDirection()).forEach(mj1 -> {
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() - c * 2);
                                    if (mj1.getMahjong_number() < 0) {
                                        mj1.setMahjong_number((int)(size + (long)mj1.getMahjong_number()));
                                    }
                                    mj1.getWeina_imageView().setLayoutY(finalY3 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                }
                            }));
                        }
                    }
                    if ((long)mj.getMahjong_number() > size - 9L) {
                        int c = (int)(size - (long)mj.getMahjong_number() + 1L) / 2;
                        if (c == 1) {
                            anchorPane = new AnchorPane();
                            imageView = new ImageView(ImageKit.rotate(new Image(MainController.class.getResourceAsStream("/quesheng/view/image/guopai_guozhang.png")), -90));
                            anchorPane.getChildren().add(imageView);
                            imageView.setFitHeight(mj.getWeina_imageView().getFitWidth() - 2.0);
                            imageView.setFitWidth(mj.getWeina_imageView().getFitWidth());
                            this.main_panel.getChildren().add(anchorPane);
                            this.guozuang_top_label.add(anchorPane);
                            anchorPane.getStyleClass().add("guopai_label_xuanze");
                            anchorPane.setLayoutX(mj.getWeina_imageView().getLayoutX() - mj.getWeina_imageView().getFitHeight() * 2.0 - imageView.getFitWidth());
                            anchorPane.setLayoutY(mj.getWeina_imageView().getLayoutY());
                            anchorPane.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == leftPanel.getDirection()).forEach(mj1 -> {
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() + c * 2);
                                    if ((long)mj1.getMahjong_number() > size - 1L) {
                                        mj1.setMahjong_number((int)((long)mj1.getMahjong_number() - size));
                                    }
                                    mj1.getWeina_imageView().setLayoutY(finalY3 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                }
                            }));
                        } else {
                            label.setText("" + c);
                            this.main_panel.getChildren().add(label);
                            this.guozuang_top_label.add(label);
                            label.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == leftPanel.getDirection()).forEach(mj1 -> {
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() + c * 2);
                                    if ((long)mj1.getMahjong_number() > size - 1L) {
                                        mj1.setMahjong_number((int)((long)mj1.getMahjong_number() - size));
                                    }
                                    mj1.getWeina_imageView().setLayoutY(finalY3 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                }
                            }));
                        }
                    }
                    label.setPrefWidth(mj.getWeina_imageView().getFitWidth());
                    label.setPrefHeight(mj.getWeina_imageView().getFitWidth() - 2.0);
                    label.setAlignment(Pos.CENTER);
                    label.getStyleClass().add("guopai_label_xuanze");
                    label.setLayoutX(mj.getWeina_imageView().getLayoutX() - mj.getWeina_imageView().getFitHeight() * 2.0 - label.getPrefWidth());
                    label.setLayoutY(mj.getWeina_imageView().getLayoutY());
                }
            }
        });
        this.main_panel.getParent().setOnMouseClicked(e -> {
            MouseButton button = e.getButton();
            switch (button) {
                case SECONDARY: {
                    int size;
                    int end;
                    int begin;
                    if (this.yinapai) {
                        if ("\u64a4\u9500".equals(Config.systemConfig.shubiaoyouji)) {
                            this.chexiao();
                        }
                        if (!"\u6478\u724c".equals(Config.systemConfig.shubiaoyouji) || directions.stream().filter(direction -> direction.getMo() == 1).count() != 1L) break;
                        directions.forEach(direction -> {
                            if (direction.getMo() == 1) {
                                dapaifangx = direction.getDirection();
                            }
                        });
                        MainController.mopai();
                        break;
                    }
                    double x = e.getX();
                    double y = e.getY();
                    List<Mj> mjList = Config.weina.stream().filter(mj1 -> mj1.getDirection() == topPanel.getDirection()).collect(Collectors.toList());
                    mjList.sort(new ComparatorMj());
                    if (mjList.stream().filter(mj1 -> mj1.getWeina_imageView().getStyleClass().contains("mj_guozhang")).count() > 0L) {
                        begin = -1;
                        end = 0;
                        for (Mj mj : mjList) {
                            if (!mj.getWeina_imageView().getStyleClass().contains("mj_guozhang")) continue;
                            if (begin == -1) {
                                begin = mj.getMahjong_number();
                            }
                            end = mj.getMahjong_number();
                        }
                        size = end - begin;
                        if (Config.SCREEN_WIDTH / 2.0 - Config.MJ_WIDTH_1_2 * (double)mjList.size() / 2.0 / 2.0 + 5.0 > x) {
                            for (Mj mj : mjList) {
                                if (mj.getMahjong_number() % 2 == 0) continue;
                                if (mj.getMahjong_number() >= begin && mj.getMahjong_number() <= end) {
                                    mj.setMahjong_number(mjList.size() - 1 - (end - mj.getMahjong_number()));
                                    continue;
                                }
                                if (mj.getMahjong_number() <= end) continue;
                                mj.setMahjong_number(mj.getMahjong_number() - size - 2);
                            }
                        }
                        if (Config.SCREEN_WIDTH / 2.0 + Config.MJ_WIDTH_1_2 * (double)mjList.size() / 2.0 / 2.0 < x) {
                            for (Mj mj : mjList) {
                                if (mj.getMahjong_number() % 2 == 0) continue;
                                if (mj.getMahjong_number() >= begin && mj.getMahjong_number() <= end) {
                                    mj.setMahjong_number(mj.getMahjong_number() - begin + 1);
                                    continue;
                                }
                                if (mj.getMahjong_number() >= begin) continue;
                                mj.setMahjong_number(mj.getMahjong_number() + size + 2);
                            }
                        }
                        double finalX = Config.SCREEN_WIDTH / 2.0 + Config.MJ_WIDTH_1_2 * (double)(mjList.size() / 2) / 2.0 - 5.0;
                        mjList.forEach(mj1 -> {
                            mj1.getWeina_imageView().getStyleClass().remove("mj_guozhang");
                            mj1.getWeina_imageView().setLayoutX(finalX - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                        });
                    }
                    mjList = Config.weina.stream().filter(mj1 -> mj1.getDirection() == bottomPanel.getDirection()).collect(Collectors.toList());
                    mjList.sort(new ComparatorMj());
                    if (mjList.stream().filter(mj1 -> mj1.getWeina_imageView().getStyleClass().contains("mj_guozhang")).count() > 0L) {
                        begin = -1;
                        end = 0;
                        for (Mj mj : mjList) {
                            if (!mj.getWeina_imageView().getStyleClass().contains("mj_guozhang")) continue;
                            if (begin == -1) {
                                begin = mj.getMahjong_number();
                            }
                            end = mj.getMahjong_number();
                        }
                        size = end - begin;
                        if (Config.SCREEN_WIDTH / 2.0 + Config.MJ_WIDTH_1_2 * (double)mjList.size() / 2.0 / 2.0 < x) {
                            for (Mj mj : mjList) {
                                if (mj.getMahjong_number() % 2 == 0) continue;
                                if (mj.getMahjong_number() >= begin && mj.getMahjong_number() <= end) {
                                    mj.setMahjong_number(mjList.size() - 1 - (end - mj.getMahjong_number()));
                                    continue;
                                }
                                if (mj.getMahjong_number() <= end) continue;
                                mj.setMahjong_number(mj.getMahjong_number() - size - 2);
                            }
                        }
                        if (Config.SCREEN_WIDTH / 2.0 - Config.MJ_WIDTH_1_2 * (double)mjList.size() / 2.0 / 2.0 + Config.MJ_WIDTH_1_2 > x) {
                            for (Mj mj : mjList) {
                                if (mj.getMahjong_number() % 2 == 0) continue;
                                if (mj.getMahjong_number() >= begin && mj.getMahjong_number() <= end) {
                                    mj.setMahjong_number(mj.getMahjong_number() - begin + 1);
                                    continue;
                                }
                                if (mj.getMahjong_number() >= begin) continue;
                                mj.setMahjong_number(mj.getMahjong_number() + size + 2);
                            }
                        }
                        double finalX = Config.SCREEN_WIDTH / 2.0 - Config.MJ_WIDTH_1_2 * (double)(mjList.size() / 2) / 2.0 + Config.MJ_WIDTH_1_2;
                        mjList.forEach(mj1 -> {
                            mj1.getWeina_imageView().getStyleClass().remove("mj_guozhang");
                            mj1.getWeina_imageView().setLayoutX(finalX + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                        });
                    }
                    mjList = Config.weina.stream().filter(mj1 -> mj1.getDirection() == leftPanel.getDirection()).collect(Collectors.toList());
                    mjList.sort(new ComparatorMj());
                    if (mjList.stream().filter(mj1 -> mj1.getWeina_imageView().getStyleClass().contains("mj_guozhang")).count() > 0L) {
                        begin = -1;
                        end = 0;
                        for (Mj mj : mjList) {
                            if (!mj.getWeina_imageView().getStyleClass().contains("mj_guozhang")) continue;
                            if (begin == -1) {
                                begin = mj.getMahjong_number();
                            }
                            end = mj.getMahjong_number();
                        }
                        size = end - begin;
                        if (Config.SCREEN_HEIGHT / 2.0 + Config.MJ_WIDTH_1_2 * (double)(size / 2) / 2.0 < y) {
                            for (Mj mj : mjList) {
                                if (mj.getMahjong_number() % 2 == 0) continue;
                                if (mj.getMahjong_number() >= begin && mj.getMahjong_number() <= end) {
                                    mj.setMahjong_number(mjList.size() - 1 - (end - mj.getMahjong_number()));
                                    continue;
                                }
                                if (mj.getMahjong_number() <= end) continue;
                                mj.setMahjong_number(mj.getMahjong_number() - size - 2);
                            }
                        }
                        if (Config.SCREEN_HEIGHT / 2.0 - Config.MJ_WIDTH_1_2 * (double)(size / 2) / 2.0 + Config.MJ_WIDTH_1_2 > y) {
                            for (Mj mj : mjList) {
                                if (mj.getMahjong_number() % 2 == 0) continue;
                                if (mj.getMahjong_number() >= begin && mj.getMahjong_number() <= end) {
                                    mj.setMahjong_number(mj.getMahjong_number() - begin + 1);
                                    continue;
                                }
                                if (mj.getMahjong_number() >= begin) continue;
                                mj.setMahjong_number(mj.getMahjong_number() + size + 2);
                            }
                        }
                        double finalY3 = Config.SCREEN_HEIGHT / 2.0 - Config.MJ_WIDTH_1_2 * (double)(mjList.size() / 2) / 2.0 + Config.MJ_WIDTH_1_2;
                        mjList.forEach(mj1 -> {
                            mj1.getWeina_imageView().getStyleClass().remove("mj_guozhang");
                            mj1.getWeina_imageView().setLayoutY(finalY3 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                        });
                    }
                    mjList = Config.weina.stream().filter(mj1 -> mj1.getDirection() == rightPanel.getDirection()).collect(Collectors.toList());
                    mjList.sort(new ComparatorMj());
                    if (mjList.stream().filter(mj1 -> mj1.getWeina_imageView().getStyleClass().contains("mj_guozhang")).count() <= 0L) break;
                    begin = -1;
                    end = 0;
                    for (Mj mj : mjList) {
                        if (!mj.getWeina_imageView().getStyleClass().contains("mj_guozhang")) continue;
                        if (begin == -1) {
                            begin = mj.getMahjong_number();
                        }
                        end = mj.getMahjong_number();
                    }
                    int size2 = end - begin;
                    if (Config.SCREEN_HEIGHT / 2.0 - Config.MJ_WIDTH_1_2 * (double)(size2 / 2) / 2.0 > y) {
                        for (Mj mj : mjList) {
                            if (mj.getMahjong_number() % 2 == 0) continue;
                            if (mj.getMahjong_number() >= begin && mj.getMahjong_number() <= end) {
                                mj.setMahjong_number(mjList.size() - 1 - (end - mj.getMahjong_number()));
                                continue;
                            }
                            if (mj.getMahjong_number() <= end) continue;
                            mj.setMahjong_number(mj.getMahjong_number() - size2 - 2);
                        }
                    }
                    if (Config.SCREEN_HEIGHT / 2.0 + Config.MJ_WIDTH_1_2 * (double)(size2 / 2) / 2.0 < y) {
                        for (Mj mj : mjList) {
                            if (mj.getMahjong_number() % 2 == 0) continue;
                            if (mj.getMahjong_number() >= begin && mj.getMahjong_number() <= end) {
                                mj.setMahjong_number(mj.getMahjong_number() - begin + 1);
                                continue;
                            }
                            if (mj.getMahjong_number() >= begin) continue;
                            mj.setMahjong_number(mj.getMahjong_number() + size2 + 2);
                        }
                    }
                    double finalY1 = Config.SCREEN_HEIGHT / 2.0 + Config.MJ_WIDTH_1_2 * (double)(mjList.size() / 2) / 2.0 + Config.MJ_WIDTH_1_2;
                    mjList.forEach(mj1 -> {
                        mj1.getWeina_imageView().getStyleClass().remove("mj_guozhang");
                        mj1.getWeina_imageView().setLayoutY(finalY1 - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                    });
                }
            }
        });
    }

    private void xunhuanguozhang() {
        zhuangPanel.setGuopai();
        this.displayWeinaPai();
        Config.weina.forEach(mj -> {
            ImageView imageView2;
            AnchorPane anchorPane2;
            ImageView imageView;
            AnchorPane anchorPane;
            Label label2;
            Label label;
            if (mj.getDirection() == bottomPanel.getDirection()) {
                long size = Config.weina.stream().filter(mj1 -> mj1.getDirection() == bottomPanel.getDirection()).count();
                double finalY2 = Config.SCREEN_HEIGHT - Config.MJ_HEIGHT * 4.0 - 50.0;
                double finalX2 = Config.SCREEN_WIDTH / 2.0 - Config.MJ_WIDTH_1_2 * (double)(size / 2L) / 2.0 + Config.MJ_WIDTH_1_2;
                if (mj.getMahjong_number() % 2 != 0) {
                    label = new Label();
                    label2 = new Label();
                    if (mj.getMahjong_number() < 9) {
                        int c = (mj.getMahjong_number() + 1) / 2;
                        if (c == 1) {
                            anchorPane = new AnchorPane();
                            imageView = new ImageView(ImageKit.rotate(new Image(MainController.class.getResourceAsStream("/quesheng/view/image/guopai_xunhuan.png")), 90));
                            anchorPane.getChildren().add(imageView);
                            imageView.setFitHeight(mj.getWeina_imageView().getFitWidth());
                            imageView.setFitWidth(mj.getWeina_imageView().getFitWidth() - 2.0);
                            this.main_panel.getChildren().add(anchorPane);
                            this.guozuang_top_label.add(anchorPane);
                            anchorPane.getStyleClass().add("guopai_label_xuanze");
                            anchorPane.setLayoutX(mj.getWeina_imageView().getLayoutX());
                            anchorPane.setLayoutY(mj.getWeina_imageView().getLayoutY() + mj.getWeina_imageView().getFitHeight() * 2.0);
                            anchorPane.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == bottomPanel.getDirection()).forEach(mj1 -> {
                                if (mj1.getMahjong_number() % 2 == 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() + c * 2);
                                } else {
                                    mj1.setMahjong_number(mj1.getMahjong_number() - c * 2);
                                }
                                if (mj1.getMahjong_number() < 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() + c * 2 - 1);
                                }
                                if ((long)mj1.getMahjong_number() > size - 1L) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() - c * 2 + 1);
                                }
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.getWeina_imageView().setLayoutY(finalY2);
                                    mj1.getWeina_imageView().setLayoutX(finalX2 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                } else {
                                    mj1.getWeina_imageView().setLayoutY(finalY2 + mj1.getWeina_imageView().getFitHeight());
                                    mj1.getWeina_imageView().setLayoutX(finalX2 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                }
                            }));
                            anchorPane2 = new AnchorPane();
                            imageView2 = new ImageView(new Image(MainController.class.getResourceAsStream("/quesheng/view/image/guopai_xunhuan.png")));
                            anchorPane2.getChildren().add(imageView2);
                            imageView2.setFitHeight(mj.getWeina_imageView().getFitWidth() - 1.0);
                            imageView2.setFitWidth(mj.getWeina_imageView().getFitWidth() - 2.0);
                            this.main_panel.getChildren().add(anchorPane2);
                            this.guozuang_top_label.add(anchorPane2);
                            anchorPane2.getStyleClass().add("guopai_label_xuanze_top");
                            anchorPane2.setLayoutX(mj.getWeina_imageView().getLayoutX());
                            anchorPane2.setLayoutY(mj.getWeina_imageView().getLayoutY() + mj.getWeina_imageView().getFitHeight() * 2.0 + imageView2.getFitHeight() + 2.0);
                            anchorPane2.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == bottomPanel.getDirection()).forEach(mj1 -> {
                                if (mj1.getMahjong_number() % 2 == 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() - c * 2);
                                    if (mj1.getMahjong_number() < 0) {
                                        mj1.setMahjong_number(c * 2 + mj1.getMahjong_number() + 1);
                                    }
                                } else if (mj1.getMahjong_number() / 2 < c) {
                                    mj1.setMahjong_number((int)(size - 1L - (long)(c * 2) + (long)mj1.getMahjong_number()));
                                }
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.getWeina_imageView().setLayoutY(finalY2);
                                    mj1.getWeina_imageView().setLayoutX(finalX2 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                } else {
                                    mj1.getWeina_imageView().setLayoutY(finalY2 + mj1.getWeina_imageView().getFitHeight());
                                    mj1.getWeina_imageView().setLayoutX(finalX2 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                }
                            }));
                        } else {
                            label.setText("" + c);
                            label2.setText("" + c);
                            this.main_panel.getChildren().add(label);
                            this.main_panel.getChildren().add(label2);
                            this.guozuang_top_label.add(label);
                            this.guozuang_top_label.add(label2);
                            label.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == bottomPanel.getDirection()).forEach(mj1 -> {
                                if (mj1.getMahjong_number() % 2 == 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() + c * 2);
                                } else {
                                    mj1.setMahjong_number(mj1.getMahjong_number() - c * 2);
                                }
                                if (mj1.getMahjong_number() < 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() + c * 2 - 1);
                                }
                                if ((long)mj1.getMahjong_number() > size - 1L) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() - c * 2 + 1);
                                }
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.getWeina_imageView().setLayoutY(finalY2);
                                    mj1.getWeina_imageView().setLayoutX(finalX2 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                } else {
                                    mj1.getWeina_imageView().setLayoutY(finalY2 + mj1.getWeina_imageView().getFitHeight());
                                    mj1.getWeina_imageView().setLayoutX(finalX2 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                }
                            }));
                            label2.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == bottomPanel.getDirection()).forEach(mj1 -> {
                                if (mj1.getMahjong_number() % 2 == 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() - c * 2);
                                    if (mj1.getMahjong_number() < 0) {
                                        mj1.setMahjong_number(c * 2 + mj1.getMahjong_number() + 1);
                                    }
                                } else if (mj1.getMahjong_number() / 2 < c) {
                                    mj1.setMahjong_number((int)(size - 1L - (long)(c * 2) + (long)mj1.getMahjong_number()));
                                }
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.getWeina_imageView().setLayoutY(finalY2);
                                    mj1.getWeina_imageView().setLayoutX(finalX2 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                } else {
                                    mj1.getWeina_imageView().setLayoutY(finalY2 + mj1.getWeina_imageView().getFitHeight());
                                    mj1.getWeina_imageView().setLayoutX(finalX2 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                }
                            }));
                        }
                    }
                    if ((long)mj.getMahjong_number() > size - 9L) {
                        int c = (int)(size - (long)mj.getMahjong_number() + 1L) / 2;
                        if (c == 1) {
                            anchorPane = new AnchorPane();
                            imageView = new ImageView(ImageKit.rotate(new Image(MainController.class.getResourceAsStream("/quesheng/view/image/guopai_xunhuan.png")), 0));
                            anchorPane.getChildren().add(imageView);
                            imageView.setFitHeight(mj.getWeina_imageView().getFitWidth());
                            imageView.setFitWidth(mj.getWeina_imageView().getFitWidth() - 2.0);
                            this.main_panel.getChildren().add(anchorPane);
                            this.guozuang_top_label.add(anchorPane);
                            anchorPane.getStyleClass().add("guopai_label_xuanze");
                            anchorPane.setLayoutX(mj.getWeina_imageView().getLayoutX());
                            anchorPane.setLayoutY(mj.getWeina_imageView().getLayoutY() + mj.getWeina_imageView().getFitHeight() * 2.0);
                            anchorPane.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == bottomPanel.getDirection()).forEach(mj1 -> {
                                if (mj1.getMahjong_number() % 2 == 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() - c * 2);
                                } else {
                                    mj1.setMahjong_number(mj1.getMahjong_number() + c * 2);
                                }
                                if (mj1.getMahjong_number() < 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() + c * 2 + 1);
                                }
                                if ((long)mj1.getMahjong_number() > size - 1L) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() - c * 2 - 1);
                                }
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.getWeina_imageView().setLayoutY(finalY2);
                                    mj1.getWeina_imageView().setLayoutX(finalX2 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                } else {
                                    mj1.getWeina_imageView().setLayoutY(finalY2 + mj1.getWeina_imageView().getFitHeight());
                                    mj1.getWeina_imageView().setLayoutX(finalX2 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                }
                            }));
                            anchorPane2 = new AnchorPane();
                            imageView2 = new ImageView(ImageKit.rotate(new Image(MainController.class.getResourceAsStream("/quesheng/view/image/guopai_xunhuan.png")), 90));
                            anchorPane2.getChildren().add(imageView2);
                            imageView2.setFitHeight(mj.getWeina_imageView().getFitWidth() - 1.0);
                            imageView2.setFitWidth(mj.getWeina_imageView().getFitWidth() - 2.0);
                            this.main_panel.getChildren().add(anchorPane2);
                            this.guozuang_top_label.add(anchorPane2);
                            anchorPane2.getStyleClass().add("guopai_label_xuanze_top");
                            anchorPane2.setLayoutX(mj.getWeina_imageView().getLayoutX());
                            anchorPane2.setLayoutY(mj.getWeina_imageView().getLayoutY() + mj.getWeina_imageView().getFitHeight() * 2.0 + imageView2.getFitHeight() + 2.0);
                            anchorPane2.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == bottomPanel.getDirection()).forEach(mj1 -> {
                                if (mj1.getMahjong_number() % 2 == 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() + c * 2);
                                    if ((long)mj1.getMahjong_number() > size - 1L) {
                                        mj1.setMahjong_number(mj1.getMahjong_number() - c * 2 + 1);
                                    }
                                } else if ((size - (long)mj1.getMahjong_number()) / 2L < (long)c) {
                                    mj1.setMahjong_number((int)((long)(c * 2 + mj1.getMahjong_number()) - size - 1L));
                                }
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.getWeina_imageView().setLayoutY(finalY2);
                                    mj1.getWeina_imageView().setLayoutX(finalX2 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                } else {
                                    mj1.getWeina_imageView().setLayoutY(finalY2 + mj1.getWeina_imageView().getFitHeight());
                                    mj1.getWeina_imageView().setLayoutX(finalX2 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                }
                            }));
                        } else {
                            label.setText("" + c);
                            label2.setText("" + c);
                            this.main_panel.getChildren().add(label);
                            this.main_panel.getChildren().add(label2);
                            this.guozuang_top_label.add(label);
                            this.guozuang_top_label.add(label2);
                            label.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == bottomPanel.getDirection()).forEach(mj1 -> {
                                if (mj1.getMahjong_number() % 2 == 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() - c * 2);
                                } else {
                                    mj1.setMahjong_number(mj1.getMahjong_number() + c * 2);
                                }
                                if (mj1.getMahjong_number() < 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() + c * 2 + 1);
                                }
                                if ((long)mj1.getMahjong_number() > size - 1L) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() - c * 2 - 1);
                                }
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.getWeina_imageView().setLayoutY(finalY2);
                                    mj1.getWeina_imageView().setLayoutX(finalX2 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                } else {
                                    mj1.getWeina_imageView().setLayoutY(finalY2 + mj1.getWeina_imageView().getFitHeight());
                                    mj1.getWeina_imageView().setLayoutX(finalX2 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                }
                            }));
                            label2.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == bottomPanel.getDirection()).forEach(mj1 -> {
                                if (mj1.getMahjong_number() % 2 == 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() + c * 2);
                                    if ((long)mj1.getMahjong_number() > size - 1L) {
                                        mj1.setMahjong_number(mj1.getMahjong_number() - c * 2 + 1);
                                    }
                                } else if ((size - (long)mj1.getMahjong_number()) / 2L < (long)c) {
                                    mj1.setMahjong_number((int)((long)(c * 2 + mj1.getMahjong_number()) - size - 1L));
                                }
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.getWeina_imageView().setLayoutY(finalY2);
                                    mj1.getWeina_imageView().setLayoutX(finalX2 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                } else {
                                    mj1.getWeina_imageView().setLayoutY(finalY2 + mj1.getWeina_imageView().getFitHeight());
                                    mj1.getWeina_imageView().setLayoutX(finalX2 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                }
                            }));
                        }
                    }
                    label.setPrefWidth(mj.getWeina_imageView().getFitWidth() - 2.0);
                    label.setPrefHeight(mj.getWeina_imageView().getFitWidth());
                    label.setAlignment(Pos.CENTER);
                    label.getStyleClass().add("guopai_label_xuanze");
                    label.setLayoutX(mj.getWeina_imageView().getLayoutX());
                    label.setLayoutY(mj.getWeina_imageView().getLayoutY() + mj.getWeina_imageView().getFitHeight() * 2.0);
                    label2.setPrefWidth(mj.getWeina_imageView().getFitWidth() - 2.0);
                    label2.setPrefHeight(mj.getWeina_imageView().getFitWidth());
                    label2.setAlignment(Pos.CENTER);
                    label2.getStyleClass().add("guopai_label_xuanze_top");
                    label2.setLayoutX(mj.getWeina_imageView().getLayoutX());
                    label2.setLayoutY(mj.getWeina_imageView().getLayoutY() + mj.getWeina_imageView().getFitHeight() * 2.0 + label.getPrefHeight() + 2.0);
                }
            }
            if (mj.getDirection() == rightPanel.getDirection()) {
                long size = Config.weina.stream().filter(mj1 -> mj1.getDirection() == rightPanel.getDirection()).count();
                double finalY1 = Config.SCREEN_HEIGHT / 2.0 + Config.MJ_WIDTH_1_2 * (double)(size / 2L) / 2.0 + Config.MJ_WIDTH_1_2;
                double finalX1 = Config.SCREEN_WIDTH / 2.0 + Config.MJ_WIDTH_1_2 * (double)(Config.weina.stream().filter(mj1 -> mj1.getDirection() == topPanel.getDirection()).count() / 2L) / 2.0 + Config.MJ_WIDTH * 3.0 / 2.0;
                if (mj.getMahjong_number() % 2 != 0) {
                    label = new Label();
                    label2 = new Label();
                    if (mj.getMahjong_number() < 9) {
                        int c = (mj.getMahjong_number() + 1) / 2;
                        if (c == 1) {
                            anchorPane = new AnchorPane();
                            imageView = new ImageView(ImageKit.rotate(new Image(MainController.class.getResourceAsStream("/quesheng/view/image/guopai_xunhuan.png")), 0));
                            anchorPane.getChildren().add(imageView);
                            imageView.setFitHeight(mj.getWeina_imageView().getFitWidth() - 2.0);
                            imageView.setFitWidth(mj.getWeina_imageView().getFitWidth());
                            this.main_panel.getChildren().add(anchorPane);
                            this.guozuang_top_label.add(anchorPane);
                            anchorPane.getStyleClass().add("guopai_label_xuanze");
                            anchorPane.setLayoutX(mj.getWeina_imageView().getLayoutX() + mj.getWeina_imageView().getFitHeight() * 2.0);
                            anchorPane.setLayoutY(mj.getWeina_imageView().getLayoutY() - imageView.getFitHeight());
                            anchorPane.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == rightPanel.getDirection()).forEach(mj1 -> {
                                if (mj1.getMahjong_number() % 2 == 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() + c * 2);
                                } else {
                                    mj1.setMahjong_number(mj1.getMahjong_number() - c * 2);
                                }
                                if (mj1.getMahjong_number() < 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() + c * 2 - 1);
                                }
                                if ((long)mj1.getMahjong_number() > size - 1L) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() - c * 2 + 1);
                                }
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.getWeina_imageView().setLayoutY(finalY1 - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                    mj1.getWeina_imageView().setLayoutX(finalX1);
                                } else {
                                    mj1.getWeina_imageView().setLayoutY(finalY1 - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                    mj1.getWeina_imageView().setLayoutX(finalX1 + mj1.getWeina_imageView().getFitHeight());
                                }
                            }));
                            anchorPane2 = new AnchorPane();
                            imageView2 = new ImageView(ImageKit.rotate(new Image(MainController.class.getResourceAsStream("/quesheng/view/image/guopai_xunhuan.png")), -90));
                            anchorPane2.getChildren().add(imageView2);
                            imageView2.setFitHeight(mj.getWeina_imageView().getFitWidth() - 4.0);
                            imageView2.setFitWidth(mj.getWeina_imageView().getFitWidth() - 4.0);
                            this.main_panel.getChildren().add(anchorPane2);
                            this.guozuang_top_label.add(anchorPane2);
                            anchorPane2.getStyleClass().add("guopai_label_xuanze_top");
                            anchorPane2.setLayoutX(anchorPane.getLayoutX() + imageView.getFitWidth());
                            anchorPane2.setLayoutY(anchorPane.getLayoutY());
                            anchorPane2.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == rightPanel.getDirection()).forEach(mj1 -> {
                                if (mj1.getMahjong_number() % 2 == 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() - c * 2);
                                    if (mj1.getMahjong_number() < 0) {
                                        mj1.setMahjong_number(c * 2 + mj1.getMahjong_number() + 1);
                                    }
                                } else if (mj1.getMahjong_number() / 2 < c) {
                                    mj1.setMahjong_number((int)(size - 1L - (long)(c * 2) + (long)mj1.getMahjong_number()));
                                }
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.getWeina_imageView().setLayoutY(finalY1 - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                    mj1.getWeina_imageView().setLayoutX(finalX1);
                                } else {
                                    mj1.getWeina_imageView().setLayoutY(finalY1 - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                    mj1.getWeina_imageView().setLayoutX(finalX1 + mj1.getWeina_imageView().getFitHeight());
                                }
                            }));
                        } else {
                            label.setText("" + c);
                            label2.setText("" + c);
                            this.main_panel.getChildren().add(label);
                            this.main_panel.getChildren().add(label2);
                            this.guozuang_top_label.add(label);
                            this.guozuang_top_label.add(label2);
                            label.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == rightPanel.getDirection()).forEach(mj1 -> {
                                if (mj1.getMahjong_number() % 2 == 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() + c * 2);
                                } else {
                                    mj1.setMahjong_number(mj1.getMahjong_number() - c * 2);
                                }
                                if (mj1.getMahjong_number() < 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() + c * 2 - 1);
                                }
                                if ((long)mj1.getMahjong_number() > size - 1L) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() - c * 2 + 1);
                                }
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.getWeina_imageView().setLayoutY(finalY1 - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                    mj1.getWeina_imageView().setLayoutX(finalX1);
                                } else {
                                    mj1.getWeina_imageView().setLayoutY(finalY1 - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                    mj1.getWeina_imageView().setLayoutX(finalX1 + mj1.getWeina_imageView().getFitHeight());
                                }
                            }));
                            label2.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == rightPanel.getDirection()).forEach(mj1 -> {
                                if (mj1.getMahjong_number() % 2 == 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() - c * 2);
                                    if (mj1.getMahjong_number() < 0) {
                                        mj1.setMahjong_number(c * 2 + mj1.getMahjong_number() + 1);
                                    }
                                } else if (mj1.getMahjong_number() / 2 < c) {
                                    mj1.setMahjong_number((int)(size - 1L - (long)(c * 2) + (long)mj1.getMahjong_number()));
                                }
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.getWeina_imageView().setLayoutY(finalY1 - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                    mj1.getWeina_imageView().setLayoutX(finalX1);
                                } else {
                                    mj1.getWeina_imageView().setLayoutY(finalY1 - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                    mj1.getWeina_imageView().setLayoutX(finalX1 + mj1.getWeina_imageView().getFitHeight());
                                }
                            }));
                        }
                    }
                    if ((long)mj.getMahjong_number() > size - 9L) {
                        int c = (int)(size - (long)mj.getMahjong_number() + 1L) / 2;
                        if (c == 1) {
                            anchorPane = new AnchorPane();
                            imageView = new ImageView(ImageKit.rotate(new Image(MainController.class.getResourceAsStream("/quesheng/view/image/guopai_xunhuan.png")), -90));
                            anchorPane.getChildren().add(imageView);
                            imageView.setFitHeight(mj.getWeina_imageView().getFitWidth() - 2.0);
                            imageView.setFitWidth(mj.getWeina_imageView().getFitWidth());
                            this.main_panel.getChildren().add(anchorPane);
                            this.guozuang_top_label.add(anchorPane);
                            anchorPane.getStyleClass().add("guopai_label_xuanze");
                            anchorPane.setLayoutX(mj.getWeina_imageView().getLayoutX() + mj.getWeina_imageView().getFitHeight() * 2.0);
                            anchorPane.setLayoutY(mj.getWeina_imageView().getLayoutY() - imageView.getFitHeight());
                            anchorPane.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == rightPanel.getDirection()).forEach(mj1 -> {
                                if (mj1.getMahjong_number() % 2 == 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() - c * 2);
                                } else {
                                    mj1.setMahjong_number(mj1.getMahjong_number() + c * 2);
                                }
                                if (mj1.getMahjong_number() < 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() + c * 2 + 1);
                                }
                                if ((long)mj1.getMahjong_number() > size - 1L) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() - c * 2 - 1);
                                }
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.getWeina_imageView().setLayoutY(finalY1 - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                    mj1.getWeina_imageView().setLayoutX(finalX1);
                                } else {
                                    mj1.getWeina_imageView().setLayoutY(finalY1 - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                    mj1.getWeina_imageView().setLayoutX(finalX1 + mj1.getWeina_imageView().getFitHeight());
                                }
                            }));
                            anchorPane2 = new AnchorPane();
                            imageView2 = new ImageView(ImageKit.rotate(new Image(MainController.class.getResourceAsStream("/quesheng/view/image/guopai_xunhuan.png")), 0));
                            anchorPane2.getChildren().add(imageView2);
                            imageView2.setFitHeight(mj.getWeina_imageView().getFitWidth() - 4.0);
                            imageView2.setFitWidth(mj.getWeina_imageView().getFitWidth() - 4.0);
                            this.main_panel.getChildren().add(anchorPane2);
                            this.guozuang_top_label.add(anchorPane2);
                            anchorPane2.getStyleClass().add("guopai_label_xuanze_top");
                            anchorPane2.setLayoutX(anchorPane.getLayoutX() + imageView.getFitWidth());
                            anchorPane2.setLayoutY(anchorPane.getLayoutY());
                            anchorPane2.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == rightPanel.getDirection()).forEach(mj1 -> {
                                if (mj1.getMahjong_number() % 2 == 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() + c * 2);
                                    if ((long)mj1.getMahjong_number() > size - 1L) {
                                        mj1.setMahjong_number(mj1.getMahjong_number() - c * 2 + 1);
                                    }
                                } else if ((size - (long)mj1.getMahjong_number()) / 2L < (long)c) {
                                    mj1.setMahjong_number((int)((long)(c * 2 + mj1.getMahjong_number()) - size - 1L));
                                }
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.getWeina_imageView().setLayoutY(finalY1 - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                    mj1.getWeina_imageView().setLayoutX(finalX1);
                                } else {
                                    mj1.getWeina_imageView().setLayoutY(finalY1 - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                    mj1.getWeina_imageView().setLayoutX(finalX1 + mj1.getWeina_imageView().getFitHeight());
                                }
                            }));
                        } else {
                            label.setText("" + c);
                            this.main_panel.getChildren().add(label);
                            label2.setText("" + c);
                            this.main_panel.getChildren().add(label2);
                            this.guozuang_top_label.add(label);
                            this.guozuang_top_label.add(label2);
                            label.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == rightPanel.getDirection()).forEach(mj1 -> {
                                if (mj1.getMahjong_number() % 2 == 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() - c * 2);
                                } else {
                                    mj1.setMahjong_number(mj1.getMahjong_number() + c * 2);
                                }
                                if (mj1.getMahjong_number() < 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() + c * 2 + 1);
                                }
                                if ((long)mj1.getMahjong_number() > size - 1L) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() - c * 2 - 1);
                                }
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.getWeina_imageView().setLayoutY(finalY1 - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                    mj1.getWeina_imageView().setLayoutX(finalX1);
                                } else {
                                    mj1.getWeina_imageView().setLayoutY(finalY1 - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                    mj1.getWeina_imageView().setLayoutX(finalX1 + mj1.getWeina_imageView().getFitHeight());
                                }
                            }));
                            label2.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == rightPanel.getDirection()).forEach(mj1 -> {
                                if (mj1.getMahjong_number() % 2 == 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() + c * 2);
                                    if ((long)mj1.getMahjong_number() > size - 1L) {
                                        mj1.setMahjong_number(mj1.getMahjong_number() - c * 2 + 1);
                                    }
                                } else if ((size - (long)mj1.getMahjong_number()) / 2L < (long)c) {
                                    mj1.setMahjong_number((int)((long)(c * 2 + mj1.getMahjong_number()) - size - 1L));
                                }
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.getWeina_imageView().setLayoutY(finalY1 - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                    mj1.getWeina_imageView().setLayoutX(finalX1);
                                } else {
                                    mj1.getWeina_imageView().setLayoutY(finalY1 - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                    mj1.getWeina_imageView().setLayoutX(finalX1 + mj1.getWeina_imageView().getFitHeight());
                                }
                            }));
                        }
                    }
                    label.setPrefWidth(mj.getWeina_imageView().getFitWidth());
                    label.setPrefHeight(mj.getWeina_imageView().getFitWidth() - 2.0);
                    label.setAlignment(Pos.CENTER);
                    label.getStyleClass().add("guopai_label_xuanze");
                    label.setLayoutX(mj.getWeina_imageView().getLayoutX() + mj.getWeina_imageView().getFitHeight() * 2.0);
                    label.setLayoutY(mj.getWeina_imageView().getLayoutY() - label.getPrefHeight());
                    label2.setPrefWidth(mj.getWeina_imageView().getFitWidth() - 2.0);
                    label2.setPrefHeight(mj.getWeina_imageView().getFitWidth());
                    label2.setAlignment(Pos.CENTER);
                    label2.getStyleClass().add("guopai_label_xuanze_top");
                    label2.setLayoutX(label.getLayoutX() + label.getPrefWidth());
                    label2.setLayoutY(label.getLayoutY());
                }
            }
            if (mj.getDirection() == topPanel.getDirection()) {
                long size = Config.weina.stream().filter(mj1 -> mj1.getDirection() == topPanel.getDirection()).count();
                double finalY = Config.MJ_HEIGHT + Config.MJ_HEIGHT_3_4 + Config.MJ_WIDTH * 2.0;
                double finalX = Config.SCREEN_WIDTH / 2.0 + Config.MJ_WIDTH_1_2 * (double)(size / 2L) / 2.0 - 5.0;
                if (mj.getMahjong_number() % 2 != 0) {
                    label = new Label();
                    label2 = new Label();
                    if (mj.getMahjong_number() < 9) {
                        int c = (mj.getMahjong_number() + 1) / 2;
                        if (c == 1) {
                            anchorPane = new AnchorPane();
                            imageView = new ImageView(ImageKit.rotate(new Image(MainController.class.getResourceAsStream("/quesheng/view/image/guopai_xunhuan.png")), -90));
                            anchorPane.getChildren().add(imageView);
                            imageView.setFitHeight(mj.getWeina_imageView().getFitWidth());
                            imageView.setFitWidth(mj.getWeina_imageView().getFitWidth() - 2.0);
                            this.main_panel.getChildren().add(anchorPane);
                            this.guozuang_top_label.add(anchorPane);
                            anchorPane.getStyleClass().add("guopai_label_xuanze");
                            anchorPane.setLayoutX(mj.getWeina_imageView().getLayoutX());
                            anchorPane.setLayoutY(mj.getWeina_imageView().getLayoutY() - imageView.getFitHeight() - mj.getWeina_imageView().getFitHeight());
                            anchorPane.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == topPanel.getDirection()).forEach(mj1 -> {
                                if (mj1.getMahjong_number() % 2 == 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() + c * 2);
                                } else {
                                    mj1.setMahjong_number(mj1.getMahjong_number() - c * 2);
                                }
                                if (mj1.getMahjong_number() < 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() + c * 2 - 1);
                                }
                                if ((long)mj1.getMahjong_number() > size - 1L) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() - c * 2 + 1);
                                }
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.getWeina_imageView().setLayoutY(finalY + mj1.getWeina_imageView().getFitHeight());
                                    mj1.getWeina_imageView().setLayoutX(finalX - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                } else {
                                    mj1.getWeina_imageView().setLayoutY(finalY);
                                    mj1.getWeina_imageView().setLayoutX(finalX - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                }
                            }));
                            anchorPane2 = new AnchorPane();
                            imageView2 = new ImageView(ImageKit.rotate(new Image(MainController.class.getResourceAsStream("/quesheng/view/image/guopai_xunhuan.png")), 180));
                            anchorPane2.getChildren().add(imageView2);
                            imageView2.setFitHeight(mj.getWeina_imageView().getFitWidth() - 2.0);
                            imageView2.setFitWidth(mj.getWeina_imageView().getFitWidth() - 2.0);
                            this.main_panel.getChildren().add(anchorPane2);
                            this.guozuang_top_label.add(anchorPane2);
                            anchorPane2.getStyleClass().add("guopai_label_xuanze_top");
                            anchorPane2.setLayoutX(anchorPane.getLayoutX());
                            anchorPane2.setLayoutY(anchorPane.getLayoutY() - imageView.getFitHeight());
                            anchorPane2.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == topPanel.getDirection()).forEach(mj1 -> {
                                if (mj1.getMahjong_number() % 2 == 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() - c * 2);
                                    if (mj1.getMahjong_number() < 0) {
                                        mj1.setMahjong_number(c * 2 + mj1.getMahjong_number() + 1);
                                    }
                                } else if (mj1.getMahjong_number() / 2 < c) {
                                    mj1.setMahjong_number((int)(size - 1L - (long)(c * 2) + (long)mj1.getMahjong_number()));
                                }
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.getWeina_imageView().setLayoutY(finalY + mj1.getWeina_imageView().getFitHeight());
                                    mj1.getWeina_imageView().setLayoutX(finalX - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                } else {
                                    mj1.getWeina_imageView().setLayoutY(finalY);
                                    mj1.getWeina_imageView().setLayoutX(finalX - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                }
                            }));
                        } else {
                            label.setText("" + c);
                            this.main_panel.getChildren().add(label);
                            label2.setText("" + c);
                            this.main_panel.getChildren().add(label2);
                            this.guozuang_top_label.add(label);
                            this.guozuang_top_label.add(label2);
                            label.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == topPanel.getDirection()).forEach(mj1 -> {
                                if (mj1.getMahjong_number() % 2 == 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() + c * 2);
                                } else {
                                    mj1.setMahjong_number(mj1.getMahjong_number() - c * 2);
                                }
                                if (mj1.getMahjong_number() < 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() + c * 2 - 1);
                                }
                                if ((long)mj1.getMahjong_number() > size - 1L) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() - c * 2 + 1);
                                }
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.getWeina_imageView().setLayoutY(finalY + mj1.getWeina_imageView().getFitHeight());
                                    mj1.getWeina_imageView().setLayoutX(finalX - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                } else {
                                    mj1.getWeina_imageView().setLayoutY(finalY);
                                    mj1.getWeina_imageView().setLayoutX(finalX - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                }
                            }));
                            label2.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == topPanel.getDirection()).forEach(mj1 -> {
                                if (mj1.getMahjong_number() % 2 == 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() - c * 2);
                                    if (mj1.getMahjong_number() < 0) {
                                        mj1.setMahjong_number(c * 2 + mj1.getMahjong_number() + 1);
                                    }
                                } else if (mj1.getMahjong_number() / 2 < c) {
                                    mj1.setMahjong_number((int)(size - 1L - (long)(c * 2) + (long)mj1.getMahjong_number()));
                                }
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.getWeina_imageView().setLayoutY(finalY + mj1.getWeina_imageView().getFitHeight());
                                    mj1.getWeina_imageView().setLayoutX(finalX - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                } else {
                                    mj1.getWeina_imageView().setLayoutY(finalY);
                                    mj1.getWeina_imageView().setLayoutX(finalX - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                }
                            }));
                        }
                    }
                    if ((long)mj.getMahjong_number() > size - 9L) {
                        int c = (int)(size - (long)mj.getMahjong_number() + 1L) / 2;
                        if (c == 1) {
                            anchorPane = new AnchorPane();
                            imageView = new ImageView(ImageKit.rotate(new Image(MainController.class.getResourceAsStream("/quesheng/view/image/guopai_xunhuan.png")), 180));
                            anchorPane.getChildren().add(imageView);
                            imageView.setFitHeight(mj.getWeina_imageView().getFitWidth());
                            imageView.setFitWidth(mj.getWeina_imageView().getFitWidth() - 2.0);
                            this.main_panel.getChildren().add(anchorPane);
                            this.guozuang_top_label.add(anchorPane);
                            anchorPane.getStyleClass().add("guopai_label_xuanze");
                            anchorPane.setLayoutX(mj.getWeina_imageView().getLayoutX());
                            anchorPane.setLayoutY(mj.getWeina_imageView().getLayoutY() - imageView.getFitHeight() - mj.getWeina_imageView().getFitHeight());
                            anchorPane.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == topPanel.getDirection()).forEach(mj1 -> {
                                if (mj1.getMahjong_number() % 2 == 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() - c * 2);
                                } else {
                                    mj1.setMahjong_number(mj1.getMahjong_number() + c * 2);
                                }
                                if (mj1.getMahjong_number() < 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() + c * 2 + 1);
                                }
                                if ((long)mj1.getMahjong_number() > size - 1L) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() - c * 2 - 1);
                                }
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.getWeina_imageView().setLayoutY(finalY + mj1.getWeina_imageView().getFitHeight());
                                    mj1.getWeina_imageView().setLayoutX(finalX - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                } else {
                                    mj1.getWeina_imageView().setLayoutY(finalY);
                                    mj1.getWeina_imageView().setLayoutX(finalX - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                }
                            }));
                            anchorPane2 = new AnchorPane();
                            imageView2 = new ImageView(ImageKit.rotate(new Image(MainController.class.getResourceAsStream("/quesheng/view/image/guopai_xunhuan.png")), -90));
                            anchorPane2.getChildren().add(imageView2);
                            imageView2.setFitHeight(mj.getWeina_imageView().getFitWidth() - 2.0);
                            imageView2.setFitWidth(mj.getWeina_imageView().getFitWidth() - 2.0);
                            this.main_panel.getChildren().add(anchorPane2);
                            this.guozuang_top_label.add(anchorPane2);
                            anchorPane2.getStyleClass().add("guopai_label_xuanze_top");
                            anchorPane2.setLayoutX(anchorPane.getLayoutX());
                            anchorPane2.setLayoutY(anchorPane.getLayoutY() - imageView.getFitHeight());
                            anchorPane2.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == topPanel.getDirection()).forEach(mj1 -> {
                                if (mj1.getMahjong_number() % 2 == 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() + c * 2);
                                    if ((long)mj1.getMahjong_number() > size - 1L) {
                                        mj1.setMahjong_number(mj1.getMahjong_number() - c * 2 + 1);
                                    }
                                } else if ((size - (long)mj1.getMahjong_number()) / 2L < (long)c) {
                                    mj1.setMahjong_number((int)((long)(c * 2 + mj1.getMahjong_number()) - size - 1L));
                                }
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.getWeina_imageView().setLayoutY(finalY + mj1.getWeina_imageView().getFitHeight());
                                    mj1.getWeina_imageView().setLayoutX(finalX - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                } else {
                                    mj1.getWeina_imageView().setLayoutY(finalY);
                                    mj1.getWeina_imageView().setLayoutX(finalX - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                }
                            }));
                        } else {
                            label.setText("" + c);
                            this.main_panel.getChildren().add(label);
                            label2.setText("" + c);
                            this.main_panel.getChildren().add(label2);
                            this.guozuang_top_label.add(label);
                            this.guozuang_top_label.add(label2);
                            label.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == topPanel.getDirection()).forEach(mj1 -> {
                                if (mj1.getMahjong_number() % 2 == 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() - c * 2);
                                } else {
                                    mj1.setMahjong_number(mj1.getMahjong_number() + c * 2);
                                }
                                if (mj1.getMahjong_number() < 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() + c * 2 + 1);
                                }
                                if ((long)mj1.getMahjong_number() > size - 1L) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() - c * 2 - 1);
                                }
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.getWeina_imageView().setLayoutY(finalY + mj1.getWeina_imageView().getFitHeight());
                                    mj1.getWeina_imageView().setLayoutX(finalX - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                } else {
                                    mj1.getWeina_imageView().setLayoutY(finalY);
                                    mj1.getWeina_imageView().setLayoutX(finalX - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                }
                            }));
                            label2.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == topPanel.getDirection()).forEach(mj1 -> {
                                if (mj1.getMahjong_number() % 2 == 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() + c * 2);
                                    if ((long)mj1.getMahjong_number() > size - 1L) {
                                        mj1.setMahjong_number(mj1.getMahjong_number() - c * 2 + 1);
                                    }
                                } else if ((size - (long)mj1.getMahjong_number()) / 2L < (long)c) {
                                    mj1.setMahjong_number((int)((long)(c * 2 + mj1.getMahjong_number()) - size - 1L));
                                }
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.getWeina_imageView().setLayoutY(finalY + mj1.getWeina_imageView().getFitHeight());
                                    mj1.getWeina_imageView().setLayoutX(finalX - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                } else {
                                    mj1.getWeina_imageView().setLayoutY(finalY);
                                    mj1.getWeina_imageView().setLayoutX(finalX - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                }
                            }));
                        }
                    }
                    label.setPrefWidth(mj.getWeina_imageView().getFitWidth() - 2.0);
                    label.setPrefHeight(mj.getWeina_imageView().getFitWidth());
                    label.setAlignment(Pos.CENTER);
                    label.getStyleClass().add("guopai_label_xuanze");
                    label.setLayoutX(mj.getWeina_imageView().getLayoutX());
                    label.setLayoutY(mj.getWeina_imageView().getLayoutY() - label.getPrefHeight() - mj.getWeina_imageView().getFitHeight());
                    label2.setPrefWidth(label.getPrefWidth());
                    label2.setPrefHeight(label.getPrefHeight());
                    label2.setAlignment(Pos.CENTER);
                    label2.getStyleClass().add("guopai_label_xuanze_top");
                    label2.setLayoutX(label.getLayoutX());
                    label2.setLayoutY(label.getLayoutY() - label.getPrefHeight());
                }
            }
            if (mj.getDirection() == leftPanel.getDirection()) {
                long size = Config.weina.stream().filter(mj1 -> mj1.getDirection() == leftPanel.getDirection()).count();
                double finalY3 = Config.SCREEN_HEIGHT / 2.0 - Config.MJ_WIDTH_1_2 * (double)(size / 2L) / 2.0 + Config.MJ_WIDTH_1_2;
                double finalX3 = Config.SCREEN_WIDTH / 2.0 - Config.MJ_WIDTH_1_2 * (double)(Config.weina.stream().filter(mj1 -> mj1.getDirection() == topPanel.getDirection()).count() / 2L) / 2.0 - Config.MJ_HEIGHT;
                if (mj.getMahjong_number() % 2 != 0) {
                    label = new Label();
                    label2 = new Label();
                    if (mj.getMahjong_number() < 9) {
                        int c = (mj.getMahjong_number() + 1) / 2;
                        if (c == 1) {
                            anchorPane = new AnchorPane();
                            imageView = new ImageView(ImageKit.rotate(new Image(MainController.class.getResourceAsStream("/quesheng/view/image/guopai_xunhuan.png")), 180));
                            anchorPane.getChildren().add(imageView);
                            imageView.setFitHeight(mj.getWeina_imageView().getFitWidth() - 2.0);
                            imageView.setFitWidth(mj.getWeina_imageView().getFitWidth());
                            this.main_panel.getChildren().add(anchorPane);
                            this.guozuang_top_label.add(anchorPane);
                            anchorPane.getStyleClass().add("guopai_label_xuanze");
                            anchorPane.setLayoutX(mj.getWeina_imageView().getLayoutX() - mj.getWeina_imageView().getFitHeight() * 2.0 - imageView.getFitWidth());
                            anchorPane.setLayoutY(mj.getWeina_imageView().getLayoutY());
                            anchorPane.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == leftPanel.getDirection()).forEach(mj1 -> {
                                if (mj1.getMahjong_number() % 2 == 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() + c * 2);
                                } else {
                                    mj1.setMahjong_number(mj1.getMahjong_number() - c * 2);
                                }
                                if (mj1.getMahjong_number() < 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() + c * 2 - 1);
                                }
                                if ((long)mj1.getMahjong_number() > size - 1L) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() - c * 2 + 1);
                                }
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.getWeina_imageView().setLayoutY(finalY3 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                    mj1.getWeina_imageView().setLayoutX(finalX3 + mj1.getWeina_imageView().getFitHeight());
                                } else {
                                    mj1.getWeina_imageView().setLayoutY(finalY3 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                    mj1.getWeina_imageView().setLayoutX(finalX3);
                                }
                            }));
                            anchorPane2 = new AnchorPane();
                            imageView2 = new ImageView(ImageKit.rotate(new Image(MainController.class.getResourceAsStream("/quesheng/view/image/guopai_xunhuan.png")), 90));
                            anchorPane2.getChildren().add(imageView2);
                            imageView2.setFitHeight(mj.getWeina_imageView().getFitWidth() - 4.0);
                            imageView2.setFitWidth(mj.getWeina_imageView().getFitWidth() - 4.0);
                            this.main_panel.getChildren().add(anchorPane2);
                            this.guozuang_top_label.add(anchorPane2);
                            anchorPane2.getStyleClass().add("guopai_label_xuanze_top");
                            anchorPane2.setLayoutX(anchorPane.getLayoutX() - imageView.getFitWidth());
                            anchorPane2.setLayoutY(anchorPane.getLayoutY());
                            anchorPane2.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == leftPanel.getDirection()).forEach(mj1 -> {
                                if (mj1.getMahjong_number() % 2 == 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() - c * 2);
                                    if (mj1.getMahjong_number() < 0) {
                                        mj1.setMahjong_number(c * 2 + mj1.getMahjong_number() + 1);
                                    }
                                } else if (mj1.getMahjong_number() / 2 < c) {
                                    mj1.setMahjong_number((int)(size - 1L - (long)(c * 2) + (long)mj1.getMahjong_number()));
                                }
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.getWeina_imageView().setLayoutY(finalY3 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                    mj1.getWeina_imageView().setLayoutX(finalX3 + mj1.getWeina_imageView().getFitHeight());
                                } else {
                                    mj1.getWeina_imageView().setLayoutY(finalY3 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                    mj1.getWeina_imageView().setLayoutX(finalX3);
                                }
                            }));
                        } else {
                            label.setText("" + c);
                            this.main_panel.getChildren().add(label);
                            label2.setText("" + c);
                            this.main_panel.getChildren().add(label2);
                            this.guozuang_top_label.add(label);
                            this.guozuang_top_label.add(label2);
                            label.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == leftPanel.getDirection()).forEach(mj1 -> {
                                if (mj1.getMahjong_number() % 2 == 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() + c * 2);
                                } else {
                                    mj1.setMahjong_number(mj1.getMahjong_number() - c * 2);
                                }
                                if (mj1.getMahjong_number() < 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() + c * 2 - 1);
                                }
                                if ((long)mj1.getMahjong_number() > size - 1L) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() - c * 2 + 1);
                                }
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.getWeina_imageView().setLayoutY(finalY3 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                    mj1.getWeina_imageView().setLayoutX(finalX3 + mj1.getWeina_imageView().getFitHeight());
                                } else {
                                    mj1.getWeina_imageView().setLayoutY(finalY3 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                    mj1.getWeina_imageView().setLayoutX(finalX3);
                                }
                            }));
                            label2.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == leftPanel.getDirection()).forEach(mj1 -> {
                                if (mj1.getMahjong_number() % 2 == 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() - c * 2);
                                    if (mj1.getMahjong_number() < 0) {
                                        mj1.setMahjong_number(c * 2 + mj1.getMahjong_number() + 1);
                                    }
                                } else if (mj1.getMahjong_number() / 2 < c) {
                                    mj1.setMahjong_number((int)(size - 1L - (long)(c * 2) + (long)mj1.getMahjong_number()));
                                }
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.getWeina_imageView().setLayoutY(finalY3 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                    mj1.getWeina_imageView().setLayoutX(finalX3 + mj1.getWeina_imageView().getFitHeight());
                                } else {
                                    mj1.getWeina_imageView().setLayoutY(finalY3 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                    mj1.getWeina_imageView().setLayoutX(finalX3);
                                }
                            }));
                        }
                    }
                    if ((long)mj.getMahjong_number() > size - 9L) {
                        int c = (int)(size - (long)mj.getMahjong_number() + 1L) / 2;
                        if (c == 1) {
                            anchorPane = new AnchorPane();
                            imageView = new ImageView(ImageKit.rotate(new Image(MainController.class.getResourceAsStream("/quesheng/view/image/guopai_xunhuan.png")), 0));
                            anchorPane.getChildren().add(imageView);
                            imageView.setFitHeight(mj.getWeina_imageView().getFitWidth() - 2.0);
                            imageView.setFitWidth(mj.getWeina_imageView().getFitWidth());
                            this.main_panel.getChildren().add(anchorPane);
                            this.guozuang_top_label.add(anchorPane);
                            anchorPane.getStyleClass().add("guopai_label_xuanze");
                            anchorPane.setLayoutX(mj.getWeina_imageView().getLayoutX() - mj.getWeina_imageView().getFitHeight() * 2.0 - imageView.getFitWidth());
                            anchorPane.setLayoutY(mj.getWeina_imageView().getLayoutY());
                            anchorPane.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == leftPanel.getDirection()).forEach(mj1 -> {
                                if (mj1.getMahjong_number() % 2 == 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() - c * 2);
                                } else {
                                    mj1.setMahjong_number(mj1.getMahjong_number() + c * 2);
                                }
                                if (mj1.getMahjong_number() < 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() + c * 2 + 1);
                                }
                                if ((long)mj1.getMahjong_number() > size - 1L) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() - c * 2 - 1);
                                }
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.getWeina_imageView().setLayoutY(finalY3 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                    mj1.getWeina_imageView().setLayoutX(finalX3 + mj1.getWeina_imageView().getFitHeight());
                                } else {
                                    mj1.getWeina_imageView().setLayoutY(finalY3 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                    mj1.getWeina_imageView().setLayoutX(finalX3);
                                }
                            }));
                            anchorPane2 = new AnchorPane();
                            imageView2 = new ImageView(ImageKit.rotate(new Image(MainController.class.getResourceAsStream("/quesheng/view/image/guopai_xunhuan.png")), -90));
                            anchorPane2.getChildren().add(imageView2);
                            imageView2.setFitHeight(mj.getWeina_imageView().getFitWidth() - 4.0);
                            imageView2.setFitWidth(mj.getWeina_imageView().getFitWidth() - 4.0);
                            this.main_panel.getChildren().add(anchorPane2);
                            this.guozuang_top_label.add(anchorPane2);
                            anchorPane2.getStyleClass().add("guopai_label_xuanze_top");
                            anchorPane2.setLayoutX(anchorPane.getLayoutX() - imageView.getFitWidth());
                            anchorPane2.setLayoutY(anchorPane.getLayoutY());
                            anchorPane2.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == leftPanel.getDirection()).forEach(mj1 -> {
                                if (mj1.getMahjong_number() % 2 == 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() + c * 2);
                                    if ((long)mj1.getMahjong_number() > size - 1L) {
                                        mj1.setMahjong_number(mj1.getMahjong_number() - c * 2 + 1);
                                    }
                                } else if ((size - (long)mj1.getMahjong_number()) / 2L < (long)c) {
                                    mj1.setMahjong_number((int)((long)(c * 2 + mj1.getMahjong_number()) - size - 1L));
                                }
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.getWeina_imageView().setLayoutY(finalY3 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                    mj1.getWeina_imageView().setLayoutX(finalX3 + mj1.getWeina_imageView().getFitHeight());
                                } else {
                                    mj1.getWeina_imageView().setLayoutY(finalY3 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                    mj1.getWeina_imageView().setLayoutX(finalX3);
                                }
                            }));
                        } else {
                            label.setText("" + c);
                            this.main_panel.getChildren().add(label);
                            label2.setText("" + c);
                            this.main_panel.getChildren().add(label2);
                            this.guozuang_top_label.add(label);
                            this.guozuang_top_label.add(label2);
                            label.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == leftPanel.getDirection()).forEach(mj1 -> {
                                if (mj1.getMahjong_number() % 2 == 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() - c * 2);
                                } else {
                                    mj1.setMahjong_number(mj1.getMahjong_number() + c * 2);
                                }
                                if (mj1.getMahjong_number() < 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() + c * 2 + 1);
                                }
                                if ((long)mj1.getMahjong_number() > size - 1L) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() - c * 2 - 1);
                                }
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.getWeina_imageView().setLayoutY(finalY3 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                    mj1.getWeina_imageView().setLayoutX(finalX3 + mj1.getWeina_imageView().getFitHeight());
                                } else {
                                    mj1.getWeina_imageView().setLayoutY(finalY3 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                    mj1.getWeina_imageView().setLayoutX(finalX3);
                                }
                            }));
                            label2.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == leftPanel.getDirection()).forEach(mj1 -> {
                                if (mj1.getMahjong_number() % 2 == 0) {
                                    mj1.setMahjong_number(mj1.getMahjong_number() + c * 2);
                                    if ((long)mj1.getMahjong_number() > size - 1L) {
                                        mj1.setMahjong_number(mj1.getMahjong_number() - c * 2 + 1);
                                    }
                                } else if ((size - (long)mj1.getMahjong_number()) / 2L < (long)c) {
                                    mj1.setMahjong_number((int)((long)(c * 2 + mj1.getMahjong_number()) - size - 1L));
                                }
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.getWeina_imageView().setLayoutY(finalY3 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                    mj1.getWeina_imageView().setLayoutX(finalX3 + mj1.getWeina_imageView().getFitHeight());
                                } else {
                                    mj1.getWeina_imageView().setLayoutY(finalY3 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                    mj1.getWeina_imageView().setLayoutX(finalX3);
                                }
                            }));
                        }
                    }
                    label.setPrefWidth(mj.getWeina_imageView().getFitWidth());
                    label.setPrefHeight(mj.getWeina_imageView().getFitWidth() - 2.0);
                    label.setAlignment(Pos.CENTER);
                    label.getStyleClass().add("guopai_label_xuanze");
                    label.setLayoutX(mj.getWeina_imageView().getLayoutX() - mj.getWeina_imageView().getFitHeight() * 2.0 - label.getPrefWidth());
                    label.setLayoutY(mj.getWeina_imageView().getLayoutY());
                    label2.setPrefWidth(mj.getWeina_imageView().getFitWidth() - 2.0);
                    label2.setPrefHeight(mj.getWeina_imageView().getFitWidth());
                    label2.setAlignment(Pos.CENTER);
                    label2.getStyleClass().add("guopai_label_xuanze_top");
                    label2.setLayoutX(label.getLayoutX() - label.getPrefWidth());
                    label2.setLayoutY(label.getLayoutY());
                }
            }
        });
    }

    private void guodong() {
        zhuangPanel.setGuopai();
        this.displayWeinaPai();
        Config.weina.forEach(mj -> {
            ImageView imageView;
            AnchorPane anchorPane;
            Label label;
            mj.getWeina_imageView().setOnMouseClicked(event -> {
                MouseButton button = event.getButton();
                switch (button) {
                    case SECONDARY: {
                        List<Mj> mjList = Config.weina.stream().filter(mj1 -> mj1.getDirection() == mj.getDirection()).collect(Collectors.toList());
                        mjList.sort(new ComparatorMj());
                        if (mjList.stream().filter(mj1 -> mj1.getWeina_imageView().getStyleClass().contains("mj_guozhang")).count() <= 1L) break;
                        int begin = -1;
                        int end = 0;
                        for (Mj mj2 : mjList) {
                            if (!mj2.getWeina_imageView().getStyleClass().contains("mj_guozhang")) continue;
                            if (begin == -1) {
                                begin = mj2.getMahjong_number();
                            }
                            end = mj2.getMahjong_number();
                        }
                        int size_lam = end - begin;
                        if (mj.getMahjong_number() > end) {
                            for (Mj mj13 : mjList) {
                                if (mj13.getMahjong_number() >= mj.getMahjong_number() / 2 * 2) continue;
                                if (mj13.getMahjong_number() >= begin && mj13.getMahjong_number() <= end) {
                                    mj13.setMahjong_number(mj13.getMahjong_number() + (mj.getMahjong_number() / 2 * 2 - 2 - end) + 1);
                                    continue;
                                }
                                if (mj13.getMahjong_number() <= end) continue;
                                mj13.setMahjong_number(mj13.getMahjong_number() - size_lam - 1);
                            }
                        }
                        if (mj.getMahjong_number() < begin) {
                            for (Mj mj12 : mjList) {
                                if (mj12.getMahjong_number() <= mj.getMahjong_number() / 2 * 2 + 1) continue;
                                if (mj12.getMahjong_number() >= begin && mj12.getMahjong_number() <= end) {
                                    mj12.setMahjong_number(mj12.getMahjong_number() - (begin - mj.getMahjong_number() / 2 * 2 - 2));
                                    continue;
                                }
                                if (mj12.getMahjong_number() >= begin) continue;
                                mj12.setMahjong_number(mj12.getMahjong_number() + size_lam + 1);
                            }
                        }
                        if (topPanel.getDirection() == mj.getDirection()) {
                            double finalX = Config.SCREEN_WIDTH / 2.0 + Config.MJ_WIDTH_1_2 * (double)(mjList.size() / 2) / 2.0 - 5.0;
                            mjList.forEach(mj1 -> {
                                mj1.getWeina_imageView().getStyleClass().remove("mj_guozhang");
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.getWeina_imageView().setLayoutX(finalX - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                } else {
                                    mj1.getWeina_imageView().setLayoutX(finalX - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                }
                            });
                        }
                        if (bottomPanel.getDirection() == mj.getDirection()) {
                            double finalX = Config.SCREEN_WIDTH / 2.0 - Config.MJ_WIDTH_1_2 * (double)(mjList.size() / 2) / 2.0 + Config.MJ_WIDTH_1_2;
                            mjList.forEach(mj1 -> {
                                mj1.getWeina_imageView().getStyleClass().remove("mj_guozhang");
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.getWeina_imageView().setLayoutX(finalX + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                } else {
                                    mj1.getWeina_imageView().setLayoutX(finalX + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                }
                            });
                        }
                        if (leftPanel.getDirection() == mj.getDirection()) {
                            double finalY3 = Config.SCREEN_HEIGHT / 2.0 - Config.MJ_WIDTH_1_2 * (double)(mjList.size() / 2) / 2.0 + Config.MJ_WIDTH_1_2;
                            mjList.forEach(mj1 -> {
                                mj1.getWeina_imageView().getStyleClass().remove("mj_guozhang");
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.getWeina_imageView().setLayoutY(finalY3 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                } else {
                                    mj1.getWeina_imageView().setLayoutY(finalY3 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                }
                            });
                        }
                        if (rightPanel.getDirection() != mj.getDirection()) break;
                        double finalY1 = Config.SCREEN_HEIGHT / 2.0 + Config.MJ_WIDTH_1_2 * (double)(mjList.size() / 2) / 2.0 + Config.MJ_WIDTH_1_2;
                        mjList.forEach(mj1 -> {
                            mj1.getWeina_imageView().getStyleClass().remove("mj_guozhang");
                            if (mj1.getMahjong_number() % 2 != 0) {
                                mj1.getWeina_imageView().setLayoutY(finalY1 - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                            } else {
                                mj1.getWeina_imageView().setLayoutY(finalY1 - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                            }
                        });
                        break;
                    }
                    case PRIMARY: {
                        if (!mj.getWeina_imageView().getStyleClass().contains("mj_guozhang")) {
                            mj.getWeina_imageView().getStyleClass().add("mj_guozhang");
                            if (mj.getMahjong_number() % 2 == 0) {
                                Config.weina.forEach(mj1 -> {
                                    if (mj1.getMahjong_number() == mj.getMahjong_number() + 1 && mj1.getDirection() == mj.getDirection()) {
                                        mj1.getWeina_imageView().getStyleClass().add("mj_guozhang");
                                    }
                                });
                                break;
                            }
                            Config.weina.forEach(mj1 -> {
                                if (mj1.getMahjong_number() == mj.getMahjong_number() - 1 && mj1.getDirection() == mj.getDirection()) {
                                    mj1.getWeina_imageView().getStyleClass().add("mj_guozhang");
                                }
                            });
                            break;
                        }
                        mj.getWeina_imageView().getStyleClass().remove("mj_guozhang");
                        if (mj.getMahjong_number() % 2 == 0) {
                            Config.weina.forEach(mj1 -> {
                                if (mj1.getMahjong_number() == mj.getMahjong_number() + 1 && mj1.getDirection() == mj.getDirection()) {
                                    mj1.getWeina_imageView().getStyleClass().remove("mj_guozhang");
                                }
                            });
                            break;
                        }
                        Config.weina.forEach(mj1 -> {
                            if (mj1.getMahjong_number() == mj.getMahjong_number() - 1 && mj1.getDirection() == mj.getDirection()) {
                                mj1.getWeina_imageView().getStyleClass().remove("mj_guozhang");
                            }
                        });
                    }
                }
            });
            if (mj.getDirection() == bottomPanel.getDirection()) {
                long size = Config.weina.stream().filter(mj1 -> mj1.getDirection() == bottomPanel.getDirection()).count();
                double finalY2 = Config.SCREEN_HEIGHT - Config.MJ_HEIGHT * 4.0 - 50.0;
                double finalX2 = Config.SCREEN_WIDTH / 2.0 - Config.MJ_WIDTH_1_2 * (double)(size / 2L) / 2.0 + Config.MJ_WIDTH_1_2;
                if (mj.getMahjong_number() % 2 != 0) {
                    label = new Label();
                    if (mj.getMahjong_number() < 9) {
                        int c = (mj.getMahjong_number() + 1) / 2;
                        if (c == 1) {
                            anchorPane = new AnchorPane();
                            imageView = new ImageView(new Image(MainController.class.getResourceAsStream("/quesheng/view/image/guopai_guozhang.png")));
                            anchorPane.getChildren().add(imageView);
                            imageView.setFitHeight(mj.getWeina_imageView().getFitWidth());
                            imageView.setFitWidth(mj.getWeina_imageView().getFitWidth() - 2.0);
                            this.main_panel.getChildren().add(anchorPane);
                            this.guozuang_top_label.add(anchorPane);
                            anchorPane.getStyleClass().add("guopai_label_xuanze");
                            anchorPane.setLayoutX(mj.getWeina_imageView().getLayoutX());
                            anchorPane.setLayoutY(mj.getWeina_imageView().getLayoutY() + mj.getWeina_imageView().getFitHeight() * 2.0);
                            anchorPane.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == bottomPanel.getDirection()).forEach(mj1 -> {
                                mj1.setMahjong_number(mj1.getMahjong_number() - c * 2);
                                if (mj1.getMahjong_number() < 0) {
                                    mj1.setMahjong_number((int)(size + (long)mj1.getMahjong_number()));
                                }
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.getWeina_imageView().setLayoutY(finalY2);
                                    mj1.getWeina_imageView().setLayoutX(finalX2 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                } else {
                                    mj1.getWeina_imageView().setLayoutY(finalY2 + mj1.getWeina_imageView().getFitHeight());
                                    mj1.getWeina_imageView().setLayoutX(finalX2 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                }
                            }));
                        } else {
                            label.setText("" + c);
                            this.main_panel.getChildren().add(label);
                            this.guozuang_top_label.add(label);
                            label.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == bottomPanel.getDirection()).forEach(mj1 -> {
                                mj1.setMahjong_number(mj1.getMahjong_number() - c * 2);
                                if (mj1.getMahjong_number() < 0) {
                                    mj1.setMahjong_number((int)(size + (long)mj1.getMahjong_number()));
                                }
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.getWeina_imageView().setLayoutY(finalY2);
                                    mj1.getWeina_imageView().setLayoutX(finalX2 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                } else {
                                    mj1.getWeina_imageView().setLayoutY(finalY2 + mj1.getWeina_imageView().getFitHeight());
                                    mj1.getWeina_imageView().setLayoutX(finalX2 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                }
                            }));
                        }
                    }
                    if ((long)mj.getMahjong_number() > size - 9L) {
                        int c = (int)(size - (long)mj.getMahjong_number() + 1L) / 2;
                        if (c == 1) {
                            anchorPane = new AnchorPane();
                            imageView = new ImageView(ImageKit.rotate(new Image(MainController.class.getResourceAsStream("/quesheng/view/image/guopai_guozhang.png")), 180));
                            anchorPane.getChildren().add(imageView);
                            imageView.setFitHeight(mj.getWeina_imageView().getFitWidth());
                            imageView.setFitWidth(mj.getWeina_imageView().getFitWidth() - 2.0);
                            this.main_panel.getChildren().add(anchorPane);
                            this.guozuang_top_label.add(anchorPane);
                            anchorPane.getStyleClass().add("guopai_label_xuanze");
                            anchorPane.setLayoutX(mj.getWeina_imageView().getLayoutX());
                            anchorPane.setLayoutY(mj.getWeina_imageView().getLayoutY() + mj.getWeina_imageView().getFitHeight() * 2.0);
                            anchorPane.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == bottomPanel.getDirection()).forEach(mj1 -> {
                                mj1.setMahjong_number(mj1.getMahjong_number() + c * 2);
                                if ((long)mj1.getMahjong_number() > size - 1L) {
                                    mj1.setMahjong_number((int)((long)mj1.getMahjong_number() - size));
                                }
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.getWeina_imageView().setLayoutY(finalY2);
                                    mj1.getWeina_imageView().setLayoutX(finalX2 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                } else {
                                    mj1.getWeina_imageView().setLayoutY(finalY2 + mj1.getWeina_imageView().getFitHeight());
                                    mj1.getWeina_imageView().setLayoutX(finalX2 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                }
                            }));
                        } else {
                            label.setText("" + c);
                            this.main_panel.getChildren().add(label);
                            this.guozuang_top_label.add(label);
                            label.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == bottomPanel.getDirection()).forEach(mj1 -> {
                                mj1.setMahjong_number(mj1.getMahjong_number() + c * 2);
                                if ((long)mj1.getMahjong_number() > size - 1L) {
                                    mj1.setMahjong_number((int)((long)mj1.getMahjong_number() - size));
                                }
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.getWeina_imageView().setLayoutY(finalY2);
                                    mj1.getWeina_imageView().setLayoutX(finalX2 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                } else {
                                    mj1.getWeina_imageView().setLayoutY(finalY2 + mj1.getWeina_imageView().getFitHeight());
                                    mj1.getWeina_imageView().setLayoutX(finalX2 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                }
                            }));
                        }
                    }
                    label.setPrefWidth(mj.getWeina_imageView().getFitWidth() - 2.0);
                    label.setPrefHeight(mj.getWeina_imageView().getFitWidth());
                    label.setAlignment(Pos.CENTER);
                    label.getStyleClass().add("guopai_label_xuanze");
                    label.setLayoutX(mj.getWeina_imageView().getLayoutX());
                    label.setLayoutY(mj.getWeina_imageView().getLayoutY() + mj.getWeina_imageView().getFitHeight() * 2.0);
                }
            }
            if (mj.getDirection() == rightPanel.getDirection()) {
                long size = Config.weina.stream().filter(mj1 -> mj1.getDirection() == rightPanel.getDirection()).count();
                double finalY1 = Config.SCREEN_HEIGHT / 2.0 + Config.MJ_WIDTH_1_2 * (double)(size / 2L) / 2.0 + Config.MJ_WIDTH_1_2;
                double finalX1 = Config.SCREEN_WIDTH / 2.0 + Config.MJ_WIDTH_1_2 * (double)(Config.weina.stream().filter(mj1 -> mj1.getDirection() == topPanel.getDirection()).count() / 2L) / 2.0 + Config.MJ_WIDTH * 3.0 / 2.0;
                if (mj.getMahjong_number() % 2 != 0) {
                    label = new Label();
                    if (mj.getMahjong_number() < 9) {
                        int c = (mj.getMahjong_number() + 1) / 2;
                        if (c == 1) {
                            anchorPane = new AnchorPane();
                            imageView = new ImageView(ImageKit.rotate(new Image(MainController.class.getResourceAsStream("/quesheng/view/image/guopai_guozhang.png")), -90));
                            anchorPane.getChildren().add(imageView);
                            imageView.setFitHeight(mj.getWeina_imageView().getFitWidth() - 2.0);
                            imageView.setFitWidth(mj.getWeina_imageView().getFitWidth());
                            this.main_panel.getChildren().add(anchorPane);
                            this.guozuang_top_label.add(anchorPane);
                            anchorPane.getStyleClass().add("guopai_label_xuanze");
                            anchorPane.setLayoutX(mj.getWeina_imageView().getLayoutX() + mj.getWeina_imageView().getFitHeight() * 2.0);
                            anchorPane.setLayoutY(mj.getWeina_imageView().getLayoutY() - imageView.getFitHeight());
                            anchorPane.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == rightPanel.getDirection()).forEach(mj1 -> {
                                mj1.setMahjong_number(mj1.getMahjong_number() - c * 2);
                                if (mj1.getMahjong_number() < 0) {
                                    mj1.setMahjong_number((int)(size + (long)mj1.getMahjong_number()));
                                }
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.getWeina_imageView().setLayoutY(finalY1 - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                    mj1.getWeina_imageView().setLayoutX(finalX1);
                                } else {
                                    mj1.getWeina_imageView().setLayoutY(finalY1 - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                    mj1.getWeina_imageView().setLayoutX(finalX1 + mj1.getWeina_imageView().getFitHeight());
                                }
                            }));
                        } else {
                            label.setText("" + c);
                            this.main_panel.getChildren().add(label);
                            this.guozuang_top_label.add(label);
                            label.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == rightPanel.getDirection()).forEach(mj1 -> {
                                mj1.setMahjong_number(mj1.getMahjong_number() - c * 2);
                                if (mj1.getMahjong_number() < 0) {
                                    mj1.setMahjong_number((int)(size + (long)mj1.getMahjong_number()));
                                }
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.getWeina_imageView().setLayoutY(finalY1 - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                    mj1.getWeina_imageView().setLayoutX(finalX1);
                                } else {
                                    mj1.getWeina_imageView().setLayoutY(finalY1 - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                    mj1.getWeina_imageView().setLayoutX(finalX1 + mj1.getWeina_imageView().getFitHeight());
                                }
                            }));
                        }
                    }
                    if ((long)mj.getMahjong_number() > size - 9L) {
                        int c = (int)(size - (long)mj.getMahjong_number() + 1L) / 2;
                        if (c == 1) {
                            anchorPane = new AnchorPane();
                            imageView = new ImageView(ImageKit.rotate(new Image(MainController.class.getResourceAsStream("/quesheng/view/image/guopai_guozhang.png")), 90));
                            anchorPane.getChildren().add(imageView);
                            imageView.setFitHeight(mj.getWeina_imageView().getFitWidth() - 2.0);
                            imageView.setFitWidth(mj.getWeina_imageView().getFitWidth());
                            this.main_panel.getChildren().add(anchorPane);
                            this.guozuang_top_label.add(anchorPane);
                            anchorPane.getStyleClass().add("guopai_label_xuanze");
                            anchorPane.setLayoutX(mj.getWeina_imageView().getLayoutX() + mj.getWeina_imageView().getFitHeight() * 2.0);
                            anchorPane.setLayoutY(mj.getWeina_imageView().getLayoutY() - imageView.getFitHeight());
                            anchorPane.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == rightPanel.getDirection()).forEach(mj1 -> {
                                mj1.setMahjong_number(mj1.getMahjong_number() + c * 2);
                                if ((long)mj1.getMahjong_number() > size - 1L) {
                                    mj1.setMahjong_number((int)((long)mj1.getMahjong_number() - size));
                                }
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.getWeina_imageView().setLayoutY(finalY1 - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                    mj1.getWeina_imageView().setLayoutX(finalX1);
                                } else {
                                    mj1.getWeina_imageView().setLayoutY(finalY1 - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                    mj1.getWeina_imageView().setLayoutX(finalX1 + mj1.getWeina_imageView().getFitHeight());
                                }
                            }));
                        } else {
                            label.setText("" + c);
                            this.main_panel.getChildren().add(label);
                            this.guozuang_top_label.add(label);
                            label.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == rightPanel.getDirection()).forEach(mj1 -> {
                                mj1.setMahjong_number(mj1.getMahjong_number() + c * 2);
                                if ((long)mj1.getMahjong_number() > size - 1L) {
                                    mj1.setMahjong_number((int)((long)mj1.getMahjong_number() - size));
                                }
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.getWeina_imageView().setLayoutY(finalY1 - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                    mj1.getWeina_imageView().setLayoutX(finalX1);
                                } else {
                                    mj1.getWeina_imageView().setLayoutY(finalY1 - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                    mj1.getWeina_imageView().setLayoutX(finalX1 + mj1.getWeina_imageView().getFitHeight());
                                }
                            }));
                        }
                    }
                    label.setPrefWidth(mj.getWeina_imageView().getFitWidth());
                    label.setPrefHeight(mj.getWeina_imageView().getFitWidth() - 2.0);
                    label.setAlignment(Pos.CENTER);
                    label.getStyleClass().add("guopai_label_xuanze");
                    label.setLayoutX(mj.getWeina_imageView().getLayoutX() + mj.getWeina_imageView().getFitHeight() * 2.0);
                    label.setLayoutY(mj.getWeina_imageView().getLayoutY() - label.getPrefHeight());
                }
            }
            if (mj.getDirection() == topPanel.getDirection()) {
                long size = Config.weina.stream().filter(mj1 -> mj1.getDirection() == topPanel.getDirection()).count();
                double finalX = Config.SCREEN_WIDTH / 2.0 + Config.MJ_WIDTH_1_2 * (double)(size / 2L) / 2.0 - 5.0;
                if (mj.getMahjong_number() % 2 != 0) {
                    AnchorPane anchorPane2;
                    Label label2 = new Label();
                    if (mj.getMahjong_number() < 9) {
                        int c2 = (mj.getMahjong_number() + 1) / 2;
                        if (c2 == 1) {
                            anchorPane2 = new AnchorPane();
                            ImageView imageView2 = new ImageView(ImageKit.rotate(new Image(MainController.class.getResourceAsStream("/quesheng/view/image/guopai_guozhang.png")), 180));
                            anchorPane2.getChildren().add(imageView2);
                            imageView2.setFitHeight(mj.getWeina_imageView().getFitWidth());
                            imageView2.setFitWidth(mj.getWeina_imageView().getFitWidth() - 2.0);
                            this.main_panel.getChildren().add(anchorPane2);
                            this.guozuang_top_label.add(anchorPane2);
                            anchorPane2.getStyleClass().add("guopai_label_xuanze");
                            anchorPane2.setLayoutX(mj.getWeina_imageView().getLayoutX());
                            anchorPane2.setLayoutY(mj.getWeina_imageView().getLayoutY() - imageView2.getFitHeight() - mj.getWeina_imageView().getFitHeight());
                            anchorPane2.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == topPanel.getDirection()).forEach(mj1 -> {
                                mj1.setMahjong_number(mj1.getMahjong_number() - c2 * 2);
                                if (mj1.getMahjong_number() < 0) {
                                    mj1.setMahjong_number((int)(size + (long)mj1.getMahjong_number()));
                                }
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.getWeina_imageView().setLayoutX(finalX - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                } else {
                                    mj1.getWeina_imageView().setLayoutX(finalX - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                }
                            }));
                        } else {
                            label2.setText("" + c2);
                            this.main_panel.getChildren().add(label2);
                            this.guozuang_top_label.add(label2);
                            label2.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == topPanel.getDirection()).forEach(mj1 -> {
                                mj1.setMahjong_number(mj1.getMahjong_number() - c2 * 2);
                                if (mj1.getMahjong_number() < 0) {
                                    mj1.setMahjong_number((int)(size + (long)mj1.getMahjong_number()));
                                }
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.getWeina_imageView().setLayoutX(finalX - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                } else {
                                    mj1.getWeina_imageView().setLayoutX(finalX - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                }
                            }));
                        }
                    }
                    if ((long)mj.getMahjong_number() > size - 9L) {
                        int c2 = (int)(size - (long)mj.getMahjong_number() + 1L) / 2;
                        if (c2 == 1) {
                            anchorPane2 = new AnchorPane();
                            ImageView imageView3 = new ImageView(ImageKit.rotate(new Image(MainController.class.getResourceAsStream("/quesheng/view/image/guopai_guozhang.png")), 0));
                            anchorPane2.getChildren().add(imageView3);
                            imageView3.setFitHeight(mj.getWeina_imageView().getFitWidth());
                            imageView3.setFitWidth(mj.getWeina_imageView().getFitWidth() - 2.0);
                            this.main_panel.getChildren().add(anchorPane2);
                            this.guozuang_top_label.add(anchorPane2);
                            anchorPane2.getStyleClass().add("guopai_label_xuanze");
                            anchorPane2.setLayoutX(mj.getWeina_imageView().getLayoutX());
                            anchorPane2.setLayoutY(mj.getWeina_imageView().getLayoutY() - imageView3.getFitHeight() - mj.getWeina_imageView().getFitHeight());
                            anchorPane2.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == topPanel.getDirection()).forEach(mj1 -> {
                                mj1.setMahjong_number(mj1.getMahjong_number() + c2 * 2);
                                if ((long)mj1.getMahjong_number() > size - 1L) {
                                    mj1.setMahjong_number((int)((long)mj1.getMahjong_number() - size));
                                }
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.getWeina_imageView().setLayoutX(finalX - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                } else {
                                    mj1.getWeina_imageView().setLayoutX(finalX - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                }
                            }));
                        } else {
                            label2.setText("" + c2);
                            this.main_panel.getChildren().add(label2);
                            this.guozuang_top_label.add(label2);
                            label2.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == topPanel.getDirection()).forEach(mj1 -> {
                                mj1.setMahjong_number(mj1.getMahjong_number() + c2 * 2);
                                if ((long)mj1.getMahjong_number() > size - 1L) {
                                    mj1.setMahjong_number((int)((long)mj1.getMahjong_number() - size));
                                }
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.getWeina_imageView().setLayoutX(finalX - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                } else {
                                    mj1.getWeina_imageView().setLayoutX(finalX - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                }
                            }));
                        }
                    }
                    label2.setPrefWidth(mj.getWeina_imageView().getFitWidth() - 2.0);
                    label2.setPrefHeight(mj.getWeina_imageView().getFitWidth());
                    label2.setAlignment(Pos.CENTER);
                    label2.getStyleClass().add("guopai_label_xuanze");
                    label2.setLayoutX(mj.getWeina_imageView().getLayoutX());
                    label2.setLayoutY(mj.getWeina_imageView().getLayoutY() - label2.getPrefHeight() - mj.getWeina_imageView().getFitHeight());
                }
            }
            if (mj.getDirection() == leftPanel.getDirection()) {
                long size = Config.weina.stream().filter(mj1 -> mj1.getDirection() == leftPanel.getDirection()).count();
                double finalY3 = Config.SCREEN_HEIGHT / 2.0 - Config.MJ_WIDTH_1_2 * (double)(size / 2L) / 2.0 + Config.MJ_WIDTH_1_2;
                double finalX3 = Config.SCREEN_WIDTH / 2.0 - Config.MJ_WIDTH_1_2 * (double)(Config.weina.stream().filter(mj1 -> mj1.getDirection() == topPanel.getDirection()).count() / 2L) / 2.0 - Config.MJ_HEIGHT;
                if (mj.getMahjong_number() % 2 != 0) {
                    label = new Label();
                    if (mj.getMahjong_number() < 9) {
                        int c3 = (mj.getMahjong_number() + 1) / 2;
                        if (c3 == 1) {
                            anchorPane = new AnchorPane();
                            imageView = new ImageView(ImageKit.rotate(new Image(MainController.class.getResourceAsStream("/quesheng/view/image/guopai_guozhang.png")), 90));
                            anchorPane.getChildren().add(imageView);
                            imageView.setFitHeight(mj.getWeina_imageView().getFitWidth() - 2.0);
                            imageView.setFitWidth(mj.getWeina_imageView().getFitWidth());
                            this.main_panel.getChildren().add(anchorPane);
                            this.guozuang_top_label.add(anchorPane);
                            anchorPane.getStyleClass().add("guopai_label_xuanze");
                            anchorPane.setLayoutX(mj.getWeina_imageView().getLayoutX() - mj.getWeina_imageView().getFitHeight() * 2.0 - imageView.getFitWidth());
                            anchorPane.setLayoutY(mj.getWeina_imageView().getLayoutY());
                            anchorPane.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == leftPanel.getDirection()).forEach(mj1 -> {
                                mj1.setMahjong_number(mj1.getMahjong_number() - c3 * 2);
                                if (mj1.getMahjong_number() < 0) {
                                    mj1.setMahjong_number((int)(size + (long)mj1.getMahjong_number()));
                                }
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.getWeina_imageView().setLayoutY(finalY3 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                    mj1.getWeina_imageView().setLayoutX(finalX3 + mj1.getWeina_imageView().getFitHeight());
                                } else {
                                    mj1.getWeina_imageView().setLayoutY(finalY3 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                    mj1.getWeina_imageView().setLayoutX(finalX3);
                                }
                            }));
                        } else {
                            label.setText("" + c3);
                            this.main_panel.getChildren().add(label);
                            this.guozuang_top_label.add(label);
                            label.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == leftPanel.getDirection()).forEach(mj1 -> {
                                mj1.setMahjong_number(mj1.getMahjong_number() - c3 * 2);
                                if (mj1.getMahjong_number() < 0) {
                                    mj1.setMahjong_number((int)(size + (long)mj1.getMahjong_number()));
                                }
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.getWeina_imageView().setLayoutY(finalY3 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                    mj1.getWeina_imageView().setLayoutX(finalX3 + mj1.getWeina_imageView().getFitHeight());
                                } else {
                                    mj1.getWeina_imageView().setLayoutY(finalY3 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                    mj1.getWeina_imageView().setLayoutX(finalX3);
                                }
                            }));
                        }
                    }
                    if ((long)mj.getMahjong_number() > size - 9L) {
                        int c4 = (int)(size - (long)mj.getMahjong_number() + 1L) / 2;
                        if (c4 == 1) {
                            anchorPane = new AnchorPane();
                            imageView = new ImageView(ImageKit.rotate(new Image(MainController.class.getResourceAsStream("/quesheng/view/image/guopai_guozhang.png")), -90));
                            anchorPane.getChildren().add(imageView);
                            imageView.setFitHeight(mj.getWeina_imageView().getFitWidth() - 2.0);
                            imageView.setFitWidth(mj.getWeina_imageView().getFitWidth());
                            this.main_panel.getChildren().add(anchorPane);
                            this.guozuang_top_label.add(anchorPane);
                            anchorPane.getStyleClass().add("guopai_label_xuanze");
                            anchorPane.setLayoutX(mj.getWeina_imageView().getLayoutX() - mj.getWeina_imageView().getFitHeight() * 2.0 - imageView.getFitWidth());
                            anchorPane.setLayoutY(mj.getWeina_imageView().getLayoutY());
                            anchorPane.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == leftPanel.getDirection()).forEach(mj1 -> {
                                mj1.setMahjong_number(mj1.getMahjong_number() + c4 * 2);
                                if ((long)mj1.getMahjong_number() > size - 1L) {
                                    mj1.setMahjong_number((int)((long)mj1.getMahjong_number() - size));
                                }
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.getWeina_imageView().setLayoutY(finalY3 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                    mj1.getWeina_imageView().setLayoutX(finalX3 + mj1.getWeina_imageView().getFitHeight());
                                } else {
                                    mj1.getWeina_imageView().setLayoutY(finalY3 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                    mj1.getWeina_imageView().setLayoutX(finalX3);
                                }
                            }));
                        } else {
                            label.setText("" + c4);
                            this.main_panel.getChildren().add(label);
                            this.guozuang_top_label.add(label);
                            label.setOnMouseClicked(event -> Config.weina.stream().filter(mj1 -> mj1.getDirection() == leftPanel.getDirection()).forEach(mj1 -> {
                                mj1.setMahjong_number(mj1.getMahjong_number() + c4 * 2);
                                if ((long)mj1.getMahjong_number() > size - 1L) {
                                    mj1.setMahjong_number((int)((long)mj1.getMahjong_number() - size));
                                }
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mj1.getWeina_imageView().setLayoutY(finalY3 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                    mj1.getWeina_imageView().setLayoutX(finalX3 + mj1.getWeina_imageView().getFitHeight());
                                } else {
                                    mj1.getWeina_imageView().setLayoutY(finalY3 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                                    mj1.getWeina_imageView().setLayoutX(finalX3);
                                }
                            }));
                        }
                    }
                    label.setPrefWidth(mj.getWeina_imageView().getFitWidth());
                    label.setPrefHeight(mj.getWeina_imageView().getFitWidth() - 2.0);
                    label.setAlignment(Pos.CENTER);
                    label.getStyleClass().add("guopai_label_xuanze");
                    label.setLayoutX(mj.getWeina_imageView().getLayoutX() - mj.getWeina_imageView().getFitHeight() * 2.0 - label.getPrefWidth());
                    label.setLayoutY(mj.getWeina_imageView().getLayoutY());
                }
            }
        });
        this.main_panel.getParent().setOnMouseClicked(e -> {
            MouseButton button = e.getButton();
            switch (button) {
                case SECONDARY: {
                    int size;
                    int end;
                    int begin;
                    if (this.yinapai) {
                        if ("\u64a4\u9500".equals(Config.systemConfig.shubiaoyouji)) {
                            this.chexiao();
                        }
                        if (!"\u6478\u724c".equals(Config.systemConfig.shubiaoyouji) || directions.stream().filter(direction -> direction.getMo() == 1).count() != 1L) break;
                        directions.forEach(direction -> {
                            if (direction.getMo() == 1) {
                                dapaifangx = direction.getDirection();
                            }
                        });
                        MainController.mopai();
                        break;
                    }
                    double x = e.getX();
                    double y = e.getY();
                    List<Mj> mjList = Config.weina.stream().filter(mj1 -> mj1.getDirection() == topPanel.getDirection()).collect(Collectors.toList());
                    mjList.sort(new ComparatorMj());
                    if (mjList.stream().filter(mj1 -> mj1.getWeina_imageView().getStyleClass().contains("mj_guozhang")).count() > 1L) {
                        begin = -1;
                        end = 0;
                        for (Mj mj : mjList) {
                            if (!mj.getWeina_imageView().getStyleClass().contains("mj_guozhang")) continue;
                            if (begin == -1) {
                                begin = mj.getMahjong_number();
                            }
                            end = mj.getMahjong_number();
                        }
                        size = end - begin;
                        if (Config.SCREEN_WIDTH / 2.0 - Config.MJ_WIDTH_1_2 * (double)mjList.size() / 2.0 / 2.0 + 5.0 > x) {
                            for (Mj mj : mjList) {
                                if (mj.getMahjong_number() >= begin && mj.getMahjong_number() <= end) {
                                    mj.setMahjong_number(mjList.size() - 1 - (end - mj.getMahjong_number()));
                                    continue;
                                }
                                if (mj.getMahjong_number() <= end) continue;
                                mj.setMahjong_number(mj.getMahjong_number() - size - 1);
                            }
                        }
                        if (Config.SCREEN_WIDTH / 2.0 + Config.MJ_WIDTH_1_2 * (double)mjList.size() / 2.0 / 2.0 < x) {
                            for (Mj mj : mjList) {
                                if (mj.getMahjong_number() >= begin && mj.getMahjong_number() <= end) {
                                    mj.setMahjong_number(mj.getMahjong_number() - begin);
                                    continue;
                                }
                                if (mj.getMahjong_number() >= begin) continue;
                                mj.setMahjong_number(mj.getMahjong_number() + size + 1);
                            }
                        }
                        double finalX = Config.SCREEN_WIDTH / 2.0 + Config.MJ_WIDTH_1_2 * (double)(mjList.size() / 2) / 2.0 - 5.0;
                        mjList.forEach(mj1 -> {
                            mj1.getWeina_imageView().getStyleClass().remove("mj_guozhang");
                            if (mj1.getMahjong_number() % 2 != 0) {
                                mj1.getWeina_imageView().setLayoutX(finalX - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                            } else {
                                mj1.getWeina_imageView().setLayoutX(finalX - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                            }
                        });
                    }
                    mjList = Config.weina.stream().filter(mj1 -> mj1.getDirection() == bottomPanel.getDirection()).collect(Collectors.toList());
                    mjList.sort(new ComparatorMj());
                    if (mjList.stream().filter(mj1 -> mj1.getWeina_imageView().getStyleClass().contains("mj_guozhang")).count() > 1L) {
                        begin = -1;
                        end = 0;
                        for (Mj mj : mjList) {
                            if (!mj.getWeina_imageView().getStyleClass().contains("mj_guozhang")) continue;
                            if (begin == -1) {
                                begin = mj.getMahjong_number();
                            }
                            end = mj.getMahjong_number();
                        }
                        size = end - begin;
                        if (Config.SCREEN_WIDTH / 2.0 + Config.MJ_WIDTH_1_2 * (double)mjList.size() / 2.0 / 2.0 < x) {
                            for (Mj mj : mjList) {
                                if (mj.getMahjong_number() >= begin && mj.getMahjong_number() <= end) {
                                    mj.setMahjong_number(mjList.size() - 1 - (end - mj.getMahjong_number()));
                                    continue;
                                }
                                if (mj.getMahjong_number() <= end) continue;
                                mj.setMahjong_number(mj.getMahjong_number() - size - 1);
                            }
                        }
                        if (Config.SCREEN_WIDTH / 2.0 - Config.MJ_WIDTH_1_2 * (double)mjList.size() / 2.0 / 2.0 + Config.MJ_WIDTH_1_2 > x) {
                            for (Mj mj : mjList) {
                                if (mj.getMahjong_number() >= begin && mj.getMahjong_number() <= end) {
                                    mj.setMahjong_number(mj.getMahjong_number() - begin);
                                    continue;
                                }
                                if (mj.getMahjong_number() >= begin) continue;
                                mj.setMahjong_number(mj.getMahjong_number() + size + 1);
                            }
                        }
                        double finalX = Config.SCREEN_WIDTH / 2.0 - Config.MJ_WIDTH_1_2 * (double)(mjList.size() / 2) / 2.0 + Config.MJ_WIDTH_1_2;
                        mjList.forEach(mj1 -> {
                            mj1.getWeina_imageView().getStyleClass().remove("mj_guozhang");
                            if (mj1.getMahjong_number() % 2 != 0) {
                                mj1.getWeina_imageView().setLayoutX(finalX + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                            } else {
                                mj1.getWeina_imageView().setLayoutX(finalX + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                            }
                        });
                    }
                    mjList = Config.weina.stream().filter(mj1 -> mj1.getDirection() == leftPanel.getDirection()).collect(Collectors.toList());
                    mjList.sort(new ComparatorMj());
                    if (mjList.stream().filter(mj1 -> mj1.getWeina_imageView().getStyleClass().contains("mj_guozhang")).count() > 1L) {
                        begin = -1;
                        end = 0;
                        for (Mj mj : mjList) {
                            if (!mj.getWeina_imageView().getStyleClass().contains("mj_guozhang")) continue;
                            if (begin == -1) {
                                begin = mj.getMahjong_number();
                            }
                            end = mj.getMahjong_number();
                        }
                        size = end - begin;
                        if (Config.SCREEN_HEIGHT / 2.0 + Config.MJ_WIDTH_1_2 * (double)(size / 2) / 2.0 < y) {
                            for (Mj mj : mjList) {
                                if (mj.getMahjong_number() >= begin && mj.getMahjong_number() <= end) {
                                    mj.setMahjong_number(mjList.size() - 1 - (end - mj.getMahjong_number()));
                                    continue;
                                }
                                if (mj.getMahjong_number() <= end) continue;
                                mj.setMahjong_number(mj.getMahjong_number() - size - 1);
                            }
                        }
                        if (Config.SCREEN_HEIGHT / 2.0 - Config.MJ_WIDTH_1_2 * (double)(size / 2) / 2.0 + Config.MJ_WIDTH_1_2 > y) {
                            for (Mj mj : mjList) {
                                if (mj.getMahjong_number() >= begin && mj.getMahjong_number() <= end) {
                                    mj.setMahjong_number(mj.getMahjong_number() - begin);
                                    continue;
                                }
                                if (mj.getMahjong_number() >= begin) continue;
                                mj.setMahjong_number(mj.getMahjong_number() + size + 1);
                            }
                        }
                        double finalY3 = Config.SCREEN_HEIGHT / 2.0 - Config.MJ_WIDTH_1_2 * (double)(mjList.size() / 2) / 2.0 + Config.MJ_WIDTH_1_2;
                        mjList.forEach(mj1 -> {
                            mj1.getWeina_imageView().getStyleClass().remove("mj_guozhang");
                            if (mj1.getMahjong_number() % 2 != 0) {
                                mj1.getWeina_imageView().setLayoutY(finalY3 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                            } else {
                                mj1.getWeina_imageView().setLayoutY(finalY3 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                            }
                        });
                    }
                    mjList = Config.weina.stream().filter(mj1 -> mj1.getDirection() == rightPanel.getDirection()).collect(Collectors.toList());
                    mjList.sort(new ComparatorMj());
                    if (mjList.stream().filter(mj1 -> mj1.getWeina_imageView().getStyleClass().contains("mj_guozhang")).count() <= 1L) break;
                    begin = -1;
                    end = 0;
                    for (Mj mj : mjList) {
                        if (!mj.getWeina_imageView().getStyleClass().contains("mj_guozhang")) continue;
                        if (begin == -1) {
                            begin = mj.getMahjong_number();
                        }
                        end = mj.getMahjong_number();
                    }
                    int size2 = end - begin;
                    if (Config.SCREEN_HEIGHT / 2.0 - Config.MJ_WIDTH_1_2 * (double)(size2 / 2) / 2.0 > y) {
                        for (Mj mj : mjList) {
                            if (mj.getMahjong_number() >= begin && mj.getMahjong_number() <= end) {
                                mj.setMahjong_number(mjList.size() - 1 - (end - mj.getMahjong_number()));
                                continue;
                            }
                            if (mj.getMahjong_number() <= end) continue;
                            mj.setMahjong_number(mj.getMahjong_number() - size2 - 1);
                        }
                    }
                    if (Config.SCREEN_HEIGHT / 2.0 + Config.MJ_WIDTH_1_2 * (double)(size2 / 2) / 2.0 < y) {
                        for (Mj mj : mjList) {
                            if (mj.getMahjong_number() >= begin && mj.getMahjong_number() <= end) {
                                mj.setMahjong_number(mj.getMahjong_number() - begin);
                                continue;
                            }
                            if (mj.getMahjong_number() >= begin) continue;
                            mj.setMahjong_number(mj.getMahjong_number() + size2 + 1);
                        }
                    }
                    double finalY1 = Config.SCREEN_HEIGHT / 2.0 + Config.MJ_WIDTH_1_2 * (double)(mjList.size() / 2) / 2.0 + Config.MJ_WIDTH_1_2;
                    mjList.forEach(mj1 -> {
                        mj1.getWeina_imageView().getStyleClass().remove("mj_guozhang");
                        if (mj1.getMahjong_number() % 2 != 0) {
                            mj1.getWeina_imageView().setLayoutY(finalY1 - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                        } else {
                            mj1.getWeina_imageView().setLayoutY(finalY1 - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                        }
                    });
                }
            }
        });
    }

    private void kuafangweiguodong() {
        zhuangPanel.setGuopai();
        this.displayWeinaPai();
        Config.weina.forEach(mj -> mj.getWeina_imageView().setOnMouseClicked(event -> {
            MouseButton button = event.getButton();
            switch (button) {
                case SECONDARY: {
                    List<Mj> yixuan1 = Config.weina.stream().filter(mj1 -> mj1.getWeina_imageView().getStyleClass().contains("mj_guozhang")).collect(Collectors.toList());
                    if (yixuan1.size() <= 0 || ((Mj)yixuan1.get(0)).getDirection() == mj.getDirection()) break;
                    int guodng_d = ((Mj)yixuan1.get(0)).getDirection();
                    List<Mj> fanghxiang_mj = Config.weina.stream().filter(mj1 -> mj1.getDirection() == mj.getDirection()).collect(Collectors.toList());
                    Collections.sort(fanghxiang_mj, new ComparatorMj());
                    Collections.sort(yixuan1, new ComparatorMj());
                    int index = fanghxiang_mj.indexOf(mj);
                    index = mj.getMahjong_number() % 2 == 0 ? (index += 2) : ++index;
                    if (event.isShiftDown()) {
                        Collections.reverse(yixuan1);
                        fanghxiang_mj.addAll(index, yixuan1);
                    } else {
                        fanghxiang_mj.addAll(index, yixuan1);
                    }
                    for (int i = 0; i < fanghxiang_mj.size(); ++i) {
                        ((Mj)fanghxiang_mj.get(i)).setDirection(mj.getDirection());
                        ((Mj)fanghxiang_mj.get(i)).setMahjong_number(i);
                        ((Mj)fanghxiang_mj.get(i)).getWeina_imageView().getStyleClass().remove("mj_guozhang");
                    }
                    List<Mj> zhuanyi_mj = Config.weina.stream().filter(mj1 -> mj1.getDirection() == guodng_d).collect(Collectors.toList());
                    Collections.sort(zhuanyi_mj, new ComparatorMj());
                    for (int i = 0; i < zhuanyi_mj.size(); ++i) {
                        ((Mj)zhuanyi_mj.get(i)).setMahjong_number(i);
                        ((Mj)zhuanyi_mj.get(i)).getWeina_imageView().getStyleClass().remove("mj_guozhang");
                    }
                    this.displayWeinaPai();
                    this.kuafangweiguodong();
                    return;
                }
                case PRIMARY: {
                    List<Mj> yixuan = Config.weina.stream().filter(mj1 -> mj1.getWeina_imageView().getStyleClass().contains("mj_guozhang")).collect(Collectors.toList());
                    if (yixuan.size() > 0 && ((Mj)yixuan.get(0)).getDirection() != mj.getDirection()) {
                        int guodng_d = ((Mj)yixuan.get(0)).getDirection();
                        List<Mj> fanghxiang_mj = Config.weina.stream().filter(mj1 -> mj1.getDirection() == mj.getDirection()).collect(Collectors.toList());
                        Collections.sort(fanghxiang_mj, new ComparatorMj());
                        Collections.sort(yixuan, new ComparatorMj());
                        int index = fanghxiang_mj.indexOf(mj);
                        if (mj.getMahjong_number() % 2 != 0) {
                            --index;
                        }
                        if (event.isShiftDown()) {
                            Collections.reverse(yixuan);
                            fanghxiang_mj.addAll(index, yixuan);
                        } else {
                            fanghxiang_mj.addAll(index, yixuan);
                        }
                        for (int i = 0; i < fanghxiang_mj.size(); ++i) {
                            ((Mj)fanghxiang_mj.get(i)).setDirection(mj.getDirection());
                            ((Mj)fanghxiang_mj.get(i)).setMahjong_number(i);
                            ((Mj)fanghxiang_mj.get(i)).getWeina_imageView().getStyleClass().remove("mj_guozhang");
                        }
                        List<Mj> zhuanyi_mj = Config.weina.stream().filter(mj1 -> mj1.getDirection() == guodng_d).collect(Collectors.toList());
                        Collections.sort(zhuanyi_mj, new ComparatorMj());
                        for (int i = 0; i < zhuanyi_mj.size(); ++i) {
                            ((Mj)zhuanyi_mj.get(i)).setMahjong_number(i);
                            ((Mj)zhuanyi_mj.get(i)).getWeina_imageView().getStyleClass().remove("mj_guozhang");
                        }
                        this.displayWeinaPai();
                        this.kuafangweiguodong();
                        return;
                    }
                    if (event.getClickCount() != 1) break;
                    if (!mj.getWeina_imageView().getStyleClass().contains("mj_guozhang")) {
                        mj.getWeina_imageView().getStyleClass().add("mj_guozhang");
                        if (mj.getMahjong_number() % 2 == 0) {
                            Config.weina.forEach(mj1 -> {
                                if (mj1.getMahjong_number() == mj.getMahjong_number() + 1 && mj1.getDirection() == mj.getDirection()) {
                                    mj1.getWeina_imageView().getStyleClass().add("mj_guozhang");
                                }
                            });
                            break;
                        }
                        Config.weina.forEach(mj1 -> {
                            if (mj1.getMahjong_number() == mj.getMahjong_number() - 1 && mj1.getDirection() == mj.getDirection()) {
                                mj1.getWeina_imageView().getStyleClass().add("mj_guozhang");
                            }
                        });
                        break;
                    }
                    mj.getWeina_imageView().getStyleClass().remove("mj_guozhang");
                    if (mj.getMahjong_number() % 2 == 0) {
                        Config.weina.forEach(mj1 -> {
                            if (mj1.getMahjong_number() == mj.getMahjong_number() + 1 && mj1.getDirection() == mj.getDirection()) {
                                mj1.getWeina_imageView().getStyleClass().remove("mj_guozhang");
                            }
                        });
                        break;
                    }
                    Config.weina.forEach(mj1 -> {
                        if (mj1.getMahjong_number() == mj.getMahjong_number() - 1 && mj1.getDirection() == mj.getDirection()) {
                            mj1.getWeina_imageView().getStyleClass().remove("mj_guozhang");
                        }
                    });
                }
            }
        }));
        this.main_panel.getParent().setOnMouseClicked(e -> {
            MouseButton button = e.getButton();
            switch (button) {
                case SECONDARY: {
                    int size;
                    int end;
                    int begin;
                    if (this.yinapai) {
                        if ("\u64a4\u9500".equals(Config.systemConfig.shubiaoyouji)) {
                            this.chexiao();
                        }
                        if (!"\u6478\u724c".equals(Config.systemConfig.shubiaoyouji) || directions.stream().filter(direction -> direction.getMo() == 1).count() != 1L) break;
                        directions.forEach(direction -> {
                            if (direction.getMo() == 1) {
                                dapaifangx = direction.getDirection();
                            }
                        });
                        MainController.mopai();
                        break;
                    }
                    double x = e.getX();
                    double y = e.getY();
                    List<Mj> mjList = Config.weina.stream().filter(mj1 -> mj1.getDirection() == topPanel.getDirection()).collect(Collectors.toList());
                    mjList.sort(new ComparatorMj());
                    if (mjList.stream().filter(mj1 -> mj1.getWeina_imageView().getStyleClass().contains("mj_guozhang")).count() > 1L) {
                        begin = -1;
                        end = 0;
                        for (Mj mj : mjList) {
                            if (!mj.getWeina_imageView().getStyleClass().contains("mj_guozhang")) continue;
                            if (begin == -1) {
                                begin = mj.getMahjong_number();
                            }
                            end = mj.getMahjong_number();
                        }
                        size = end - begin;
                        if (Config.SCREEN_WIDTH / 2.0 - Config.MJ_WIDTH_1_2 * (double)mjList.size() / 2.0 / 2.0 + 5.0 > x) {
                            for (Mj mj : mjList) {
                                if (mj.getMahjong_number() >= begin && mj.getMahjong_number() <= end) {
                                    mj.setMahjong_number(mjList.size() - 1 - (end - mj.getMahjong_number()));
                                    continue;
                                }
                                if (mj.getMahjong_number() <= end) continue;
                                mj.setMahjong_number(mj.getMahjong_number() - size - 1);
                            }
                        }
                        if (Config.SCREEN_WIDTH / 2.0 + Config.MJ_WIDTH_1_2 * (double)mjList.size() / 2.0 / 2.0 < x) {
                            for (Mj mj : mjList) {
                                if (mj.getMahjong_number() >= begin && mj.getMahjong_number() <= end) {
                                    mj.setMahjong_number(mj.getMahjong_number() - begin);
                                    continue;
                                }
                                if (mj.getMahjong_number() >= begin) continue;
                                mj.setMahjong_number(mj.getMahjong_number() + size + 1);
                            }
                        }
                        double finalX = Config.SCREEN_WIDTH / 2.0 + Config.MJ_WIDTH_1_2 * (double)(mjList.size() / 2) / 2.0 - 5.0;
                        mjList.forEach(mj1 -> {
                            mj1.getWeina_imageView().getStyleClass().remove("mj_guozhang");
                            if (mj1.getMahjong_number() % 2 != 0) {
                                mj1.getWeina_imageView().setLayoutX(finalX - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                            } else {
                                mj1.getWeina_imageView().setLayoutX(finalX - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                            }
                        });
                    }
                    mjList = Config.weina.stream().filter(mj1 -> mj1.getDirection() == bottomPanel.getDirection()).collect(Collectors.toList());
                    mjList.sort(new ComparatorMj());
                    if (mjList.stream().filter(mj1 -> mj1.getWeina_imageView().getStyleClass().contains("mj_guozhang")).count() > 1L) {
                        begin = -1;
                        end = 0;
                        for (Mj mj : mjList) {
                            if (!mj.getWeina_imageView().getStyleClass().contains("mj_guozhang")) continue;
                            if (begin == -1) {
                                begin = mj.getMahjong_number();
                            }
                            end = mj.getMahjong_number();
                        }
                        size = end - begin;
                        if (Config.SCREEN_WIDTH / 2.0 + Config.MJ_WIDTH_1_2 * (double)mjList.size() / 2.0 / 2.0 < x) {
                            for (Mj mj : mjList) {
                                if (mj.getMahjong_number() >= begin && mj.getMahjong_number() <= end) {
                                    mj.setMahjong_number(mjList.size() - 1 - (end - mj.getMahjong_number()));
                                    continue;
                                }
                                if (mj.getMahjong_number() <= end) continue;
                                mj.setMahjong_number(mj.getMahjong_number() - size - 1);
                            }
                        }
                        if (Config.SCREEN_WIDTH / 2.0 - Config.MJ_WIDTH_1_2 * (double)mjList.size() / 2.0 / 2.0 + Config.MJ_WIDTH_1_2 > x) {
                            for (Mj mj : mjList) {
                                if (mj.getMahjong_number() >= begin && mj.getMahjong_number() <= end) {
                                    mj.setMahjong_number(mj.getMahjong_number() - begin);
                                    continue;
                                }
                                if (mj.getMahjong_number() >= begin) continue;
                                mj.setMahjong_number(mj.getMahjong_number() + size + 1);
                            }
                        }
                        double finalX = Config.SCREEN_WIDTH / 2.0 - Config.MJ_WIDTH_1_2 * (double)(mjList.size() / 2) / 2.0 + Config.MJ_WIDTH_1_2;
                        mjList.forEach(mj1 -> {
                            mj1.getWeina_imageView().getStyleClass().remove("mj_guozhang");
                            if (mj1.getMahjong_number() % 2 != 0) {
                                mj1.getWeina_imageView().setLayoutX(finalX + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                            } else {
                                mj1.getWeina_imageView().setLayoutX(finalX + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                            }
                        });
                    }
                    mjList = Config.weina.stream().filter(mj1 -> mj1.getDirection() == leftPanel.getDirection()).collect(Collectors.toList());
                    mjList.sort(new ComparatorMj());
                    if (mjList.stream().filter(mj1 -> mj1.getWeina_imageView().getStyleClass().contains("mj_guozhang")).count() > 1L) {
                        begin = -1;
                        end = 0;
                        for (Mj mj : mjList) {
                            if (!mj.getWeina_imageView().getStyleClass().contains("mj_guozhang")) continue;
                            if (begin == -1) {
                                begin = mj.getMahjong_number();
                            }
                            end = mj.getMahjong_number();
                        }
                        size = end - begin;
                        if (Config.SCREEN_HEIGHT / 2.0 + Config.MJ_WIDTH_1_2 * (double)(size / 2) / 2.0 < y) {
                            for (Mj mj : mjList) {
                                if (mj.getMahjong_number() >= begin && mj.getMahjong_number() <= end) {
                                    mj.setMahjong_number(mjList.size() - 1 - (end - mj.getMahjong_number()));
                                    continue;
                                }
                                if (mj.getMahjong_number() <= end) continue;
                                mj.setMahjong_number(mj.getMahjong_number() - size - 1);
                            }
                        }
                        if (Config.SCREEN_HEIGHT / 2.0 - Config.MJ_WIDTH_1_2 * (double)(size / 2) / 2.0 + Config.MJ_WIDTH_1_2 > y) {
                            for (Mj mj : mjList) {
                                if (mj.getMahjong_number() >= begin && mj.getMahjong_number() <= end) {
                                    mj.setMahjong_number(mj.getMahjong_number() - begin);
                                    continue;
                                }
                                if (mj.getMahjong_number() >= begin) continue;
                                mj.setMahjong_number(mj.getMahjong_number() + size + 1);
                            }
                        }
                        double finalY3 = Config.SCREEN_HEIGHT / 2.0 - Config.MJ_WIDTH_1_2 * (double)(mjList.size() / 2) / 2.0 + Config.MJ_WIDTH_1_2;
                        mjList.forEach(mj1 -> {
                            mj1.getWeina_imageView().getStyleClass().remove("mj_guozhang");
                            if (mj1.getMahjong_number() % 2 != 0) {
                                mj1.getWeina_imageView().setLayoutY(finalY3 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                            } else {
                                mj1.getWeina_imageView().setLayoutY(finalY3 + (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                            }
                        });
                    }
                    mjList = Config.weina.stream().filter(mj1 -> mj1.getDirection() == rightPanel.getDirection()).collect(Collectors.toList());
                    mjList.sort(new ComparatorMj());
                    if (mjList.stream().filter(mj1 -> mj1.getWeina_imageView().getStyleClass().contains("mj_guozhang")).count() <= 1L) break;
                    begin = -1;
                    end = 0;
                    for (Mj mj : mjList) {
                        if (!mj.getWeina_imageView().getStyleClass().contains("mj_guozhang")) continue;
                        if (begin == -1) {
                            begin = mj.getMahjong_number();
                        }
                        end = mj.getMahjong_number();
                    }
                    int size2 = end - begin;
                    if (Config.SCREEN_HEIGHT / 2.0 - Config.MJ_WIDTH_1_2 * (double)(size2 / 2) / 2.0 > y) {
                        for (Mj mj : mjList) {
                            if (mj.getMahjong_number() >= begin && mj.getMahjong_number() <= end) {
                                mj.setMahjong_number(mjList.size() - 1 - (end - mj.getMahjong_number()));
                                continue;
                            }
                            if (mj.getMahjong_number() <= end) continue;
                            mj.setMahjong_number(mj.getMahjong_number() - size2 - 1);
                        }
                    }
                    if (Config.SCREEN_HEIGHT / 2.0 + Config.MJ_WIDTH_1_2 * (double)(size2 / 2) / 2.0 < y) {
                        for (Mj mj : mjList) {
                            if (mj.getMahjong_number() >= begin && mj.getMahjong_number() <= end) {
                                mj.setMahjong_number(mj.getMahjong_number() - begin);
                                continue;
                            }
                            if (mj.getMahjong_number() >= begin) continue;
                            mj.setMahjong_number(mj.getMahjong_number() + size2 + 1);
                        }
                    }
                    double finalY1 = Config.SCREEN_HEIGHT / 2.0 + Config.MJ_WIDTH_1_2 * (double)(mjList.size() / 2) / 2.0 + Config.MJ_WIDTH_1_2;
                    mjList.forEach(mj1 -> {
                        mj1.getWeina_imageView().getStyleClass().remove("mj_guozhang");
                        if (mj1.getMahjong_number() % 2 != 0) {
                            mj1.getWeina_imageView().setLayoutY(finalY1 - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                        } else {
                            mj1.getWeina_imageView().setLayoutY(finalY1 - (double)(mj1.getMahjong_number() / 2) * mj1.getWeina_imageView().getFitWidth());
                        }
                    });
                }
            }
        });
    }

    private void hebingpai() {
        if (!this.yinapai) {
            zhuangPanel.setXuanZhuang();
        } else {
            zhuangPanel.escGuopai();
            if (Config.systemConfig.mapaicengshu == 2) {
                int d = -1;
                for (int i = 0; i < weina.size(); ++i) {
                    Mj namj = weina.get(i);
                    if (namj.getSimo() || d != -1) continue;
                    int finalD = d = namj.getDirection();
                    if (weina.stream().filter(mj -> mj.getDirection() == finalD).count() == Config.weina.stream().filter(mj -> mj.getDirection() == finalD).count()) {
                        List<Mj> mjs = Config.weina.stream().filter(mj -> mj.getDirection() == finalD).collect(Collectors.toList());
                        Collections.sort(mjs, new ComparatorMj());
                        for (int j = mjs.size() - 1; j > -1; --j) {
                            weina.add(i, (Mj)mjs.get(j));
                            weina.remove(i + 1);
                            ++i;
                        }
                        d = -1;
                        continue;
                    }
                    d = -1;
                }
            }
        }
        this.displayWeinaPai();
        this.guo_combobox.setValue("------");
    }

    private void danzhangduidiao() {
        zhuangPanel.setGuopai();
        this.displayWeinaPai();
        Config.weina.forEach(mj -> mj.getWeina_imageView().setOnMouseClicked(event -> {
            if (mj.getWeina_imageView().getStyleClass().contains("mj_guozhang")) {
                mj.getWeina_imageView().getStyleClass().remove("mj_guozhang");
                return;
            }
            mj.getWeina_imageView().getStyleClass().add("mj_guozhang");
            for (Mj mj1 : Config.weina) {
                if (!mj1.getWeina_imageView().getStyleClass().contains("mj_guozhang") || mj1.equals(mj)) continue;
                Mj mj2 = (Mj)mj1.clone();
                mj1.setDirection(mj.getDirection());
                mj1.setMahjong_number(mj.getMahjong_number());
                mj1.setSerial_number(mj.getSerial_number());
                mj1.getWeina_imageView().setImage(Config.getImageByNumber(mj.getResult()));
                mj.setDirection(mj2.getDirection());
                mj.setMahjong_number(mj2.getMahjong_number());
                mj.setSerial_number(mj2.getSerial_number());
                mj.getWeina_imageView().setImage(Config.getImageByNumber(mj2.getResult()));
                mj1.getWeina_imageView().getStyleClass().remove("mj_guozhang");
                mj.getWeina_imageView().getStyleClass().remove("mj_guozhang");
                break;
            }
        }));
    }

    private void huoqu() {
        Map<String, String> map = new HashMap<String, String>(1);
        map.put("ajax", "true");
        HttpKit.Post("http://" + DESKit.Decrypt(Config.systemConfig.getChoseIp()) + ":8081" + "/api/yiZhuo/get", map, new Callback(){

            @Override
            public void onFailure(Call call, IOException ioe) {
                BaseController.showTextDialog("\u83b7\u53d6\u5931\u8d25");
            }

            @Override
            public void onResponse(Call call, Response rspns) throws IOException {
                String xx = rspns.body().string();
                try {
                    JsonObject result = new JsonParser().parse(xx).getAsJsonObject();
                    if (result.get("success").getAsBoolean()) {
                        if (result.get("list") == null) {
                            BaseController.showTextDialog("\u83b7\u53d6\u5931\u8d25");
                            return;
                        }
                        JsonArray jsonArray = result.getAsJsonArray("list");
                        if (jsonArray != null && jsonArray.size() > 0) {
                            Platform.runLater(() -> {
                                MainController.this.jieshu();
                                jsonArray.forEach(jsonElement -> {
                                    Mj mj = new Mj();
                                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                                    mj.setSerial_number(jsonObject.get("serial_number").getAsInt());
                                    mj.setId(jsonObject.get("id").getAsInt());
                                    mj.setMahjong_number(jsonObject.get("mahjong_number").getAsInt());
                                    mj.setDirection(jsonObject.get("direction").getAsInt());
                                    mj.setZhuo_id(jsonObject.get("zhuo_id").getAsInt());
                                    mj.setResult(Integer.parseInt(jsonObject.get("result").getAsString()));
                                    Config.weina.add(mj);
                                });
                                List<Mj> temp = new LinkedList<Mj>();
                                temp.addAll(Config.weina);
                                directions.forEach(d -> {
                                    List<Mj> temp_list = temp.stream().filter(mj -> mj.getDirection() == d.getDirection()).collect(Collectors.toList());
                                    Collections.sort(temp_list, new ComparatorMj());
                                    for (int i = 0; i < temp_list.size(); ++i) {
                                        ((Mj)temp_list.get(i)).setMahjong_number(i);
                                    }
                                });
                                if (Config.systemConfig.guoshanchediaohuan == 1) {
                                    directions.forEach(d -> {
                                        List<Mj> temp_list = temp.stream().filter(mj -> mj.getDirection() == d.getDirection()).collect(Collectors.toList());
                                        Collections.sort(temp_list, new ComparatorMj());
                                        temp_list.forEach(mj -> {
                                            if (mj.getMahjong_number() % 2 == 0) {
                                                mj.setMahjong_number(mj.getMahjong_number() + 1);
                                            } else {
                                                mj.setMahjong_number(mj.getMahjong_number() - 1);
                                            }
                                        });
                                        Collections.sort(temp_list, new ComparatorMj());
                                        for (int i = 0; i < temp_list.size(); ++i) {
                                            ((Mj)temp_list.get(i)).setMahjong_number(temp_list.size() - 1 - i);
                                        }
                                    });
                                }
                                int size = MainController.this.shangyiba.size();
                                boolean is_new = true;
                                for (int i = 0; i < size; ++i) {
                                    Mj mj = MainController.this.shangyiba.get(i);
                                    int size2 = temp.size();
                                    boolean h = false;
                                    for (int j = 0; j < size2; ++j) {
                                        Mj mj1 = (Mj)temp.get(j);
                                        if (mj1.getId() != mj.getId()) continue;
                                        h = true;
                                    }
                                    if (!h) {
                                        is_new = true;
                                        break;
                                    }
                                    is_new = false;
                                }
                                if (!is_new) {
                                    BaseController.showTextDialog("\u6ca1\u6709\u83b7\u53d6\u5230\u6570\u636e");
                                }
                                MainController.this.shangyiba.clear();
                                MainController.this.shangyiba.addAll(Config.weina);
                                MainController.this.begingamge();
                            });
                        }
                    } else {
                        BaseController.showTextDialog(result.get("message").getAsString());
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void jihuo() {
        Map<String, String> map = new HashMap<String, String>(1);
        map.put("ajax", "true");
        map.put("jihuo_status", "1");
        HttpKit.Post("http://" + DESKit.Decrypt(Config.systemConfig.getChoseIp()) + ":8081" + "/api/user/updateSelf", map, new Callback(){

            @Override
            public void onFailure(Call call, IOException ioe) {
                BaseController.showTextDialog("\u6fc0\u6d3b\u5931\u8d25");
            }

            @Override
            public void onResponse(Call call, Response rspns) throws IOException {
                String xx = rspns.body().string();
                try {
                    JsonObject result = new JsonParser().parse(xx).getAsJsonObject();
                    if (result.get("success").getAsBoolean()) {
                        BaseController.showTextDialog("\u6fc0\u6d3b\u6210\u529f");
                        param.user.setJihuo_status(1);
                        Platform.runLater(new Runnable(){

                            @Override
                            public void run() {
                                MainController.this.jihuo_btn.setVisible(false);
                            }
                        });
                    } else {
                        BaseController.showTextDialog(result.get("message").getAsString());
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void openJiance() {
        this.jiance_service = Executors.newSingleThreadScheduledExecutor();
        this.jiance_service.scheduleWithFixedDelay(() -> this.jiance(), 5L, 10L, TimeUnit.SECONDS);
    }

    public void jiance() {
        Map<String, String> map = new HashMap<String, String>(1);
        map.put("ajax", "true");
        HttpKit.Post("http://" + DESKit.Decrypt(Config.systemConfig.getChoseIp()) + ":8081" + "/api/yiZhuo/get", map, new Callback(){

            @Override
            public void onFailure(Call call, IOException ioe) {
                ioe.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response rspns) throws IOException {
                String xx = rspns.body().string();
                try {
                    JsonObject result = new JsonParser().parse(xx).getAsJsonObject();
                    if (result.get("success").getAsBoolean()) {
                        if (result.get("list") == null) {
                            return;
                        }
                        JsonArray jsonArray = result.getAsJsonArray("list");
                        if (jsonArray != null && jsonArray.size() > 0) {
                            Platform.runLater(() -> {
                                MainController.this.dangqianba.clear();
                                jsonArray.forEach(jsonElement -> {
                                    Mj mj = new Mj();
                                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                                    mj.setSerial_number(jsonObject.get("serial_number").getAsInt());
                                    mj.setId(jsonObject.get("id").getAsInt());
                                    mj.setMahjong_number(jsonObject.get("mahjong_number").getAsInt());
                                    mj.setDirection(jsonObject.get("direction").getAsInt());
                                    mj.setZhuo_id(jsonObject.get("zhuo_id").getAsInt());
                                    mj.setResult(Integer.parseInt(jsonObject.get("result").getAsString()));
                                    MainController.this.dangqianba.add(mj);
                                });
                                directions.forEach(d -> {
                                    List<Mj> temp_list = MainController.this.dangqianba.stream().filter(mj -> mj.getDirection() == d.getDirection()).collect(Collectors.toList());
                                    Collections.sort(temp_list, new ComparatorMj());
                                    for (int i = 0; i < temp_list.size(); ++i) {
                                        ((Mj)temp_list.get(i)).setMahjong_number(i);
                                    }
                                });
                                if (Config.systemConfig.guoshanchediaohuan == 1) {
                                    directions.forEach(d -> {
                                        List<Mj> temp_list = MainController.this.dangqianba.stream().filter(mj -> mj.getDirection() == d.getDirection()).collect(Collectors.toList());
                                        Collections.sort(temp_list, new ComparatorMj());
                                        temp_list.forEach(mj -> {
                                            if (mj.getMahjong_number() % 2 == 0) {
                                                mj.setMahjong_number(mj.getMahjong_number() + 1);
                                            } else {
                                                mj.setMahjong_number(mj.getMahjong_number() - 1);
                                            }
                                        });
                                        Collections.sort(temp_list, new ComparatorMj());
                                        for (int i = 0; i < temp_list.size(); ++i) {
                                            ((Mj)temp_list.get(i)).setMahjong_number(temp_list.size() - 1 - i);
                                        }
                                    });
                                }
                                boolean is_new = true;
                                int size = MainController.this.shangyiba.size();
                                int size2 = MainController.this.dangqianba.size();
                                for (int i = 0; i < size; ++i) {
                                    Mj mj2 = MainController.this.shangyiba.get(i);
                                    boolean h = false;
                                    for (int j = 0; j < size2; ++j) {
                                        Mj mj1 = MainController.this.dangqianba.get(j);
                                        if (mj1.getId() != mj2.getId()) continue;
                                        h = true;
                                    }
                                    if (!h) {
                                        is_new = true;
                                        break;
                                    }
                                    is_new = false;
                                }
                                if (is_new) {
                                    MainController.this.pai_count.setText(size2 + "");
                                    MainController.this.dong_count.setText(MainController.this.dangqianba.stream().filter(mj -> mj.getDirection() == 0).count() + "");
                                    MainController.this.nan_count.setText(MainController.this.dangqianba.stream().filter(mj -> mj.getDirection() == 1).count() + "");
                                    MainController.this.xi_count.setText(MainController.this.dangqianba.stream().filter(mj -> mj.getDirection() == 2).count() + "");
                                    MainController.this.bei_count.setText(MainController.this.dangqianba.stream().filter(mj -> mj.getDirection() == 3).count() + "");
                                    ObjectProperty<Paint> warningColor = new SimpleObjectProperty<Paint>(Color.web("#000000"));
                                    MainController.this.pai_count.textFillProperty().bindBidirectional(warningColor);
                                    Timeline flash = new Timeline(new KeyFrame(Duration.seconds(0.1), new KeyValue(warningColor, Color.web("#FF8247"), Interpolator.LINEAR)), new KeyFrame(Duration.seconds(0.1), new KeyValue(warningColor, Color.web("#FF7256"), Interpolator.LINEAR)), new KeyFrame(Duration.seconds(0.1), new KeyValue(warningColor, Color.web("#FF6347"), Interpolator.LINEAR)), new KeyFrame(Duration.seconds(0.1), new KeyValue(warningColor, Color.web("#FF34B3"), Interpolator.LINEAR)), new KeyFrame(Duration.seconds(0.1), new KeyValue(warningColor, Color.web("#FF0000"), Interpolator.LINEAR)), new KeyFrame(Duration.seconds(0.1), new KeyValue(warningColor, Color.web("#000000"), Interpolator.LINEAR)));
                                    flash.setCycleCount(300);
                                    flash.setAutoReverse(true);
                                    flash.play();
                                }
                            });
                        }
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void chexiao() {
        if (state.size() > 0) {
            MainController.clear();
            yichu_pane.getChildren().clear();
            Config.yuce.clear();
            Map<String, Object> stringObjectMap = state.get(state.size() - 1);
            state.remove(state.size() - 1);
            dapaifangx = (Integer)stringObjectMap.get("\u6253\u724c\u65b9\u5411");
            directions.forEach(direction -> {
                direction.setPre_pai("");
                direction.getYina_hupai().clear();
                direction.getHupaiPanel().getChildren().clear();
                direction.setQue((Integer)stringObjectMap.get("que_" + direction.getDirection()));
                direction.setHu((Integer)stringObjectMap.get("hu_" + direction.getDirection()));
                direction.setKehu((Boolean)stringObjectMap.get("kehu_" + direction.getDirection()));
            });
            Config.weina.clear();
            Config.weina.addAll((HashSet)stringObjectMap.get("config_weina"));
            Config.yina.clear();
            Config.yina.addAll((LinkedList)stringObjectMap.get("yina"));
            Config.yuce.clear();
            Config.yuce.addAll((LinkedList)stringObjectMap.get("yuce"));
            Config.yichu.clear();
            Config.yichu.addAll((LinkedList)stringObjectMap.get("yichu"));
            weina.clear();
            weina.addAll((LinkedList)stringObjectMap.get("weina"));
            for (Mj mj : Config.weina) {
                mj.getWeina_imageView().setUserData(mj);
                if (mj.isMaima()) {
                    mj.getWeina_imageView().setStyle("-fx-opacity: 0.5");
                    if (mj.getOther() != null) {
                        mj.getOther().setVisible(false);
                    }
                    mj.getWeina_imageView().setOnMouseClicked(event -> {
                        mj.getWeina_imageView().setStyle("-fx-opacity: 1");
                        if (mj.getOther() != null) {
                            mj.getOther().setVisible(true);
                        }
                        mj.setMaima(false);
                        Config.weina.forEach(mj1 -> {
                            if (mj1.equals(mj)) {
                                mj1.setMaima(false);
                            }
                        });
                        weina.forEach(mj1 -> {
                            if (mj1.equals(mj)) {
                                mj1.setMaima(false);
                            }
                        });
                        MainController.clear();
                        directions.forEach(direction -> {
                            direction.setPre_pai("");
                            direction.getYina_hupai().clear();
                            direction.getHupaiPanel().getChildren().clear();
                        });
                        Config.yuce.clear();
                        MainController.yucepaiAfterMopai(0, dapaifangx, 1);
                        MainController.displayYinaPai();
                        mj.getWeina_imageView().setOnMouseClicked(event1 -> this.napaisuanfa(mj));
                    });
                    continue;
                }
                if (!mj.getSimo()) {
                    mj.getWeina_imageView().setVisible(true);
                    if (mj.getOther() == null) continue;
                    mj.getOther().setVisible(true);
                    continue;
                }
                mj.getWeina_imageView().setVisible(false);
                if (mj.getOther() == null) continue;
                mj.getOther().setVisible(false);
            }
            if (Config.systemConfig.chupaixianshi == 1) {
                Config.yichu.sort(new ComparatorMjResult());
            }
            Config.yichu.forEach(mj1 -> {
                mj1.getYichu_imageView().getStyleClass().clear();
                mj1.getYichu_imageView().setImage(Config.getImageByNumber(mj1.getResult()));
                yichu_pane.getChildren().add(mj1.getYichu_imageView());
            });
            MainController.displayYinaPai();
        } else if (this.huanhoupai_yixuan.size() > 0) {
            this.huanShouPai();
        }
    }

    private void begingamge() {
        zhuangPanel.setXuanZhuang();
        this.weina_backup.clear();
        Config.weina.forEach(mj -> {
            Mj mj1 = (Mj)mj.clone();
            this.weina_backup.add(mj1);
        });
        this.guo_combobox.setValue("------");
        state.clear();
        this.yinapai = false;
        Config.weina.forEach(mj -> this.main_panel.getChildren().removeAll((Node[])new Node[]{mj.getWeina_imageView(), mj.getOther()}));
        if (Config.systemConfig.baoliupai == 0) {
            directions.forEach(direction -> {
                direction.setPre_pai("");
                direction.getYina_hupai().clear();
                direction.getHupaiPanel().getChildren().clear();
            });
            directions.forEach(direction -> {
                direction.setQue(-1);
                direction.setHu(0);
                direction.setKehu(false);
                direction.setMo(0);
                direction.setHu_mj(null);
                direction.setDiyizhangda_mj(null);
            });
            Config.yina.clear();
            dapaifangx = -1;
            zhuangjia = -1;
        } else if (Config.yina.size() > 0) {
            MainController.displayYinaPai();
        }
        Config.yuce.clear();
        yichu_pane.getChildren().clear();
        this.main_panel.getChildren().removeAll((Node[])new Node[]{yichu_pane, zhuangPanel, yuceYinaPanel});
        Config.yichu.clear();
        weina.clear();
        this.pai_count.setText("");
        this.dong_count.setText("");
        this.nan_count.setText("");
        this.xi_count.setText("");
        this.bei_count.setText("");
        if (Config.systemConfig.disancengzuowanjiapai == 1) {
            directions.forEach(d -> {
                List<Mj> weina_1 = Config.weina.stream().filter(mj -> mj.getDirection() == d.getDirection()).collect(Collectors.toList());
                Collections.sort(weina_1, new ComparatorMj());
                int dongshu = (weina_1.size() - (Config.systemConfig.wanjiapaishu - 1)) / 2;
                weina_1.forEach(mj -> {
                    if (mj.getMahjong_number() <= Config.systemConfig.wanjiapaishu - 1 - dongshu) {
                        mj.setSimo(true);
                        mj.setNapai_direction(d.getDirection());
                        Config.yina.add((Mj)mj);
                        Config.weina.remove(mj);
                    } else if (Config.yina.stream().filter(mj1 -> mj1.getDirection() == d.getDirection()).count() < (long)(Config.systemConfig.wanjiapaishu - 1) && (mj.getMahjong_number() - (Config.systemConfig.wanjiapaishu - 1) - dongshu - 1) % 3 == 0) {
                        mj.setSimo(true);
                        mj.setNapai_direction(d.getDirection());
                        Config.yina.add((Mj)mj);
                        Config.weina.remove(mj);
                    }
                });
            });
            directions.forEach(d -> {
                List<Mj> temp_list = Config.weina.stream().filter(mj -> mj.getDirection() == d.getDirection()).collect(Collectors.toList());
                Collections.sort(temp_list, new ComparatorMj());
                for (int i = 0; i < temp_list.size(); ++i) {
                    ((Mj)temp_list.get(i)).setMahjong_number(i);
                }
            });
        }
        this.displayWeinaPai();
    }

    private static void clear() {
        directions.forEach(direction -> {
            direction.clear();
            direction.setMo(0);
        });
    }

    private void jieshu() {
        this.guo_combobox.setValue("------");
        bottom_panel.getChildren().clear();
        bottom_panel.getChildren().add(bottom_zhuang_label);
        MainController.clear();
        state.clear();
        this.yinapai = false;
        this.main_panel.getChildren().clear();
        if (Config.systemConfig.baoliupai == 0) {
            directions.forEach(direction -> {
                direction.setPre_pai("");
                direction.getYina_hupai().clear();
                direction.getHupaiPanel().getChildren().clear();
            });
            directions.forEach(direction -> {
                direction.setQiangzhiHu(0);
                direction.setQue(-1);
                direction.setHu(0);
                direction.setKehu(false);
                direction.setMo(0);
                direction.setHu_mj(null);
                direction.setDiyizhangda_mj(null);
            });
            dapaifangx = -1;
            zhuangjia = -1;
            Config.yina.clear();
        }
        Config.weina.clear();
        Config.yuce.clear();
        yichu_pane.getChildren().clear();
        this.main_panel.getChildren().removeAll((Node[])new Node[]{yichu_pane, zhuangPanel, yuceYinaPanel});
        Config.yichu.clear();
        weina.clear();
    }

    public void zhihuitishiConfig(ActionEvent event) {
        Stage popup = new Stage();
        popup.getIcons().add(new Image(BaseController.class.getResourceAsStream(Config.logo_url)));
        popup.setAlwaysOnTop(true);
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.resizableProperty().setValue(Boolean.FALSE);
        popup.setResizable(false);
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/quesheng/view/Ai.fxml"));
        Pane tempPane = null;
        try {
            tempPane = (Pane)loader.load();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        Scene tempScene = new Scene(tempPane);
        popup.setScene(tempScene);
        popup.setTitle("\u6307\u6325\u63d0\u793a" + param.user.getUsername());
        popup.show();
        popup.setOnCloseRequest(event1 -> {
            if (kaiqiai == 1 && "\u8840\u6218".equals(dafa)) {
                ai = new XueZhanAiImpl(this);
            }
        });
    }

    public void ceshiguangxi152(ActionEvent event) {
        this.jieshu();
        Config.xipai(152);
        this.begingamge();
    }

    public void ceshi108(ActionEvent event) {
        this.jieshu();
        Config.xipai(108);
        this.begingamge();
    }

    public void ceshi136(ActionEvent event) {
        this.jieshu();
        Config.xipai(136);
        this.begingamge();
    }

    public void ceshi144(ActionEvent event) {
        this.jieshu();
        Config.xipai(144);
        this.begingamge();
    }

    public void ceshi72sanfeng(ActionEvent event) {
        this.jieshu();
        Config.xipai(72, 3);
        this.begingamge();
    }

    public void ceshi72sifeng(ActionEvent event) {
        this.jieshu();
        Config.xipai(72);
        this.begingamge();
    }

    public void ceshi112(ActionEvent event) {
        this.jieshu();
        Config.xipai(112);
        this.begingamge();
    }

    public void ceshi90(ActionEvent event) {
        this.jieshu();
        Config.xipai(90);
        this.begingamge();
    }

    public void maimawu(ActionEvent event) {
        for (Mj mj : weina) {
            mj.getWeina_imageView().getStyleClass().remove("maima");
        }
    }

    public void maimadong(ActionEvent event) {
        int s = Math.abs(zhuangjia - directions.get(0).getDirection());
        List<Integer> maima = maima_map.get(s);
        for (Mj mj : weina) {
            mj.getWeina_imageView().getStyleClass().remove("maima");
            if (!maima.contains(mj.getResult() % 10)) continue;
            mj.getWeina_imageView().getStyleClass().add("maima");
        }
    }

    public void maimanan(ActionEvent event) {
        int s = Math.abs(zhuangjia - directions.get(1).getDirection());
        List<Integer> maima = maima_map.get(s);
        for (Mj mj : weina) {
            mj.getWeina_imageView().getStyleClass().remove("maima");
            if (!maima.contains(mj.getResult() % 10)) continue;
            mj.getWeina_imageView().getStyleClass().add("maima");
        }
    }

    public void maimaxi(ActionEvent event) {
        int s = Math.abs(zhuangjia - directions.get(2).getDirection());
        List<Integer> maima = maima_map.get(s);
        for (Mj mj : weina) {
            mj.getWeina_imageView().getStyleClass().remove("maima");
            if (!maima.contains(mj.getResult() % 10)) continue;
            mj.getWeina_imageView().getStyleClass().add("maima");
        }
    }

    public void maimabei(ActionEvent event) {
        int s = Math.abs(zhuangjia - directions.get(3).getDirection());
        List<Integer> maima = maima_map.get(s);
        for (Mj mj : weina) {
            mj.getWeina_imageView().getStyleClass().remove("maima");
            if (!maima.contains(mj.getResult() % 10)) continue;
            mj.getWeina_imageView().getStyleClass().add("maima");
        }
    }

    private void huanyuan() {
        this.guo_combobox.setValue("------");
        bottom_panel.getChildren().clear();
        bottom_panel.getChildren().add(bottom_zhuang_label);
        MainController.clear();
        directions.forEach(direction -> {
            direction.setPre_pai("");
            direction.getYina_hupai().clear();
            direction.getHupaiPanel().getChildren().clear();
        });
        this.yinapai = false;
        zhuangjia = -1;
        directions.forEach(direction -> {
            direction.setQue(-1);
            direction.setQiangzhiHu(0);
            direction.setHu(0);
            direction.setKehu(false);
            direction.setMo(0);
            direction.setHu_mj(null);
            direction.setDiyizhangda_mj(null);
        });
        Config.yina.clear();
        Config.yuce.clear();
        yichu_pane.getChildren().clear();
        this.main_panel.getChildren().removeAll((Node[])new Node[]{yichu_pane, zhuangPanel, yuceYinaPanel});
        Config.yichu.clear();
        weina.clear();
        Config.weina.forEach(mj -> {
            mj.caisheng = false;
            if (mj.kedi_caisheng) {
                mj.kedi_caisheng = false;
                if (mj.yuanpai > 0) {
                    mj.setResult(mj.yuanpai);
                    mj.yuanpai = 0;
                }
            }
            mj.setPaizhuantai(0);
            mj.setChupai_direction(-1);
            mj.setSimo(false);
            mj.setIsque(false);
            mj.setYuce_direction(-1);
            mj.setNapai_direction(-1);
            this.main_panel.getChildren().removeAll((Node[])new Node[]{mj.getWeina_imageView(), mj.getOther()});
        });
        Config.weina.clear();
        this.weina_backup.forEach(mj -> {
            Mj mj1 = (Mj)mj.clone();
            Config.weina.add(mj1);
        });
        dapaifangx = -1;
        if (Config.weina.size() > 0) {
            this.displayWeinaPai();
        }
    }

    public static void xuanzeFangwei(int fangwei) {
        bottom_panel.getChildren().clear();
        bottom_panel.getChildren().add(bottom_zhuang_label);
        switch (fangwei) {
            case 0: {
                bottom_zhuang_label.setText("\u4e1c");
                zhuangjia = fangwei;
                break;
            }
            case 2: {
                bottom_zhuang_label.setText("\u897f");
                zhuangjia = fangwei;
                break;
            }
            case 3: {
                bottom_zhuang_label.setText("\u5317");
                zhuangjia = fangwei;
                break;
            }
            case 1: {
                bottom_zhuang_label.setText("\u5357");
                zhuangjia = fangwei;
            }
        }
        dapaifangx = zhuangjia;
        zhuangPanel.setVisible(false);
    }

    public void BiaoJiCaiSheng() {
        if (caisheng.size() > 0 && (Config.systemConfig.zixuancaisheng == 1 || Config.systemConfig.shengjicaisheng == 1 || Config.systemConfig.houcaisheng == 1 || Config.systemConfig.shunmocaisheng == 1 || Config.systemConfig.renyicaisheng == 1)) {
            Config.weina.forEach(mj -> {
                if (mj.caisheng) {
                    mj.getWeina_imageView().setImage(ImageKit.coverImage(mj.getWeina_imageView().getImage(), Config.LANSE));
                }
            });
            if (Config.systemConfig.zhuapaihouxuancaisheng == 1) {
                Config.yina.forEach(mj -> {
                    if (mj.caisheng) {
                        mj.getYina_imageView().setImage(ImageKit.coverImage(mj.getYina_imageView().getImage(), Config.LANSE));
                    }
                });
                Config.yuce.forEach(mj -> {
                    if (mj.caisheng) {
                        mj.getYuce_imageView().setImage(ImageKit.coverImage(mj.getYuce_imageView().getImage(), Config.LANSE));
                    }
                });
            }
            bottom_panel.getChildren().clear();
            bottom_panel.getChildren().add(bottom_zhuang_label);
            caisheng.forEach(integer -> {
                ImageView imageView = new ImageView(Config.getImageByNumber(integer));
                imageView.setFitHeight(Config.MJ_HEIGHT_1_2);
                imageView.setFitWidth(Config.MJ_WIDTH_1_2);
                bottom_panel.getChildren().add(imageView);
            });
        }
    }

    public void napaisuanfa(Mj mj1) {
        if (this.yinapai) {
            if (mj1.isMaima()) {
                mj1.getWeina_imageView().setStyle("-fx-opacity: 1");
                if (mj1.getOther() != null) {
                    mj1.getOther().setVisible(true);
                }
                mj1.setMaima(false);
                MainController.clear();
                Config.yuce.clear();
                MainController.yucepaiAfterMopai(0, dapaifangx, 1);
                MainController.displayYinaPai();
            } else {
                mj1.getWeina_imageView().setStyle("-fx-opacity: 0.5");
                if (mj1.getOther() != null) {
                    mj1.getOther().setVisible(false);
                }
                mj1.setMaima(true);
                MainController.clear();
                Config.yuce.clear();
                MainController.yucepaiAfterMopai(0, dapaifangx, 1);
                MainController.displayYinaPai();
            }
        } else {
            if (Config.systemConfig.qishourenyiliangdong == 1) {
                if (Config.yina.size() / Config.systemConfig.wanjiarenshu == 4) {
                    this.yinapai = true;
                }
            } else if (Config.systemConfig.renyiandongnapai == 1) {
                if (Config.yina.size() / (Config.systemConfig.wanjiarenshu * Config.systemConfig.meishouzhangshu) == Config.systemConfig.wanjiapaishu / Config.systemConfig.meishouzhangshu) {
                    this.yinapai = true;
                }
            } else {
                this.yinapai = true;
            }
            if (Config.systemConfig.mapaicengshu == 2) {
                Observable.create(emitter -> {
                    LiangCengJiImpl liangCengJi = new LiangCengJiImpl(this);
                    liangCengJi.napai(emitter, mj1);
                }).subscribe(integer3 -> {
                    if (this.yinapai) {
                        Platform.runLater(() -> {
                            MainController.clear();
                            this.huanShouPai();
                        });
                    } else {
                        Platform.runLater(() -> {
                            MainController.clear();
                            MainController.displayYinaPai();
                        });
                    }
                });
            }
            if (Config.systemConfig.mapaicengshu == 3) {
                Observable.create(emitter -> {
                    SanCengJiImpl sanCengJi = new SanCengJiImpl(this);
                    sanCengJi.napai(emitter, mj1);
                }).subscribe(integer3 -> Platform.runLater(() -> this.huanShouPai()));
            }
        }
    }

    private void ganghua() {
        Mj mj12;
        LinkedList<Mj> mjList;
        Mj namj;
        List<Mj> weina_1;
        int finalFangxian;
        if ("\u5404\u65b9\u6760\u5b8c\u518d\u6253\u724c,\u8865\u8d77\u82b1\u724c\u4e0b\u8f6e\u6760".equals(Config.systemConfig.gangfa)) {
            while (Config.yina.stream().filter(mj -> mj.getResult() > 40 && mj.getResult() < 52 && mj.getPaizhuantai() == 0).count() > 0L) {
                finalFangxian = dapaifangx;
                do {
                    int finalFangxian1 = finalFangxian;
                    weina_1 = Config.yina.stream().filter(mj -> mj.getNapai_direction() == finalFangxian1 && mj.getPaizhuantai() == 0).collect(Collectors.toList());
                    for (Mj mj2 : weina_1) {
                        if (mj2.getResult() <= 40 || mj2.getResult() >= 52 || mj2.getPaizhuantai() != 0) continue;
                        mj2.setPaizhuantai(3);
                        switch (Config.systemConfig.ganghua) {
                            case "\u540e\u6760\u724c": {
                                int i;
                                namj = null;
                                if (Config.systemConfig.mapaicengshu == 2) {
                                    mjList = new LinkedList<Mj>();
                                    for (i = weina.size() - 1; i >= 0; --i) {
                                        mj12 = weina.get(i);
                                        if (mj12.getMahjong_number() % 2 != 0) {
                                            mjList.add(mjList.size() - 1, mj12);
                                            continue;
                                        }
                                        mjList.add(mj12);
                                    }
                                    for (i = 0; i < mjList.size(); ++i) {
                                        mj12 = (Mj)mjList.get(i);
                                        if (mj12.isMaima()) continue;
                                        namj = mj12;
                                        break;
                                    }
                                }
                                if (Config.systemConfig.mapaicengshu == 3) {
                                    mjList = new LinkedList();
                                    block33: for (i = weina.size() - 1; i >= 0; --i) {
                                        mj12 = weina.get(i);
                                        switch (mj12.getMahjong_number() % 3) {
                                            case 0: {
                                                mjList.add(mj12);
                                                continue block33;
                                            }
                                            case 1: {
                                                mjList.add(mjList.size() - 1, mj12);
                                                continue block33;
                                            }
                                            case 2: {
                                                mjList.add(mjList.size() - 2, mj12);
                                            }
                                        }
                                    }
                                    for (i = 0; i < mjList.size(); ++i) {
                                        mj12 = (Mj)mjList.get(i);
                                        if (mj12.isMaima()) continue;
                                        namj = mj12;
                                        break;
                                    }
                                }
                                if (namj == null) {
                                    MainController.showTextDialog("\u672c\u5c40\u7ed3\u675f");
                                    MainController.clear();
                                    MainController.displayYinaPai();
                                    return;
                                }
                                namj.getWeina_imageView().setVisible(false);
                                if (namj.getOther() != null) {
                                    namj.getOther().setVisible(false);
                                }
                                namj.setSimo(true);
                                Mj finalNamj = namj;
                                Config.weina.forEach(mj1 -> {
                                    if (finalNamj.equals(mj1)) {
                                        mj1.setSimo(true);
                                    }
                                });
                                weina.remove(namj);
                                namj.setNapai_direction(finalFangxian1);
                                Config.yina.add(namj);
                                break;
                            }
                            case "\u987a\u6760\u724c": {
                                namj = null;
                                for (int i = 0; i < weina.size(); ++i) {
                                    Mj mj13 = weina.get(i);
                                    if (mj13.isMaima()) continue;
                                    namj = mj13;
                                    break;
                                }
                                if (namj == null) {
                                    MainController.showTextDialog("\u672c\u5c40\u7ed3\u675f");
                                    MainController.clear();
                                    MainController.displayYinaPai();
                                    return;
                                }
                                namj.getWeina_imageView().setVisible(false);
                                if (namj.getOther() != null) {
                                    namj.getOther().setVisible(false);
                                }
                                namj.setSimo(true);
                                Mj finalNamj = namj;
                                Config.weina.forEach(mj1 -> {
                                    if (finalNamj.equals(mj1)) {
                                        mj1.setSimo(true);
                                    }
                                });
                                weina.remove(namj);
                                namj.setNapai_direction(finalFangxian1);
                                Config.yina.add(namj);
                                break;
                            }
                        }
                    }
                    if (++finalFangxian <= 3) continue;
                    finalFangxian = 0;
                } while (finalFangxian != dapaifangx);
            }
            Config.yuce.clear();
            MainController.yucepaiAfterMopai(0, dapaifangx, 1);
        }
        if ("\u5404\u65b9\u6760\u5b8c\u518d\u6253\u724c,\u8865\u8d77\u82b1\u724c\u7acb\u5373\u6760".equals(Config.systemConfig.gangfa)) {
            finalFangxian = dapaifangx;
            do {
                int finalFangxian1 = finalFangxian;
                while (Config.yina.stream().filter(mj -> mj.getNapai_direction() == finalFangxian1 && mj.getResult() > 40 && mj.getResult() < 52 && mj.getPaizhuantai() == 0).count() > 0L) {
                    weina_1 = Config.yina.stream().filter(mj -> mj.getNapai_direction() == finalFangxian1 && mj.getPaizhuantai() == 0).collect(Collectors.toList());
                    for (Mj mj2 : weina_1) {
                        if (mj2.getResult() <= 40 || mj2.getResult() >= 52 || mj2.getPaizhuantai() != 0) continue;
                        mj2.setPaizhuantai(3);
                        switch (Config.systemConfig.ganghua) {
                            case "\u540e\u6760\u724c": {
                                namj = null;
                                if (Config.systemConfig.mapaicengshu == 2) {
                                    int i;
                                    mjList = new LinkedList();
                                    for (i = weina.size() - 1; i >= 0; --i) {
                                        mj12 = weina.get(i);
                                        if (mj12.getMahjong_number() % 2 != 0) {
                                            mjList.add(mjList.size() - 1, mj12);
                                            continue;
                                        }
                                        mjList.add(mj12);
                                    }
                                    for (i = 0; i < mjList.size(); ++i) {
                                        mj12 = (Mj)mjList.get(i);
                                        if (mj12.isMaima()) continue;
                                        namj = mj12;
                                        break;
                                    }
                                }
                                if (Config.systemConfig.mapaicengshu == 3) {
                                    int i;
                                    mjList = new LinkedList();
                                    block41: for (i = weina.size() - 1; i >= 0; --i) {
                                        mj12 = weina.get(i);
                                        switch (mj12.getMahjong_number() % 3) {
                                            case 0: {
                                                mjList.add(mj12);
                                                continue block41;
                                            }
                                            case 1: {
                                                mjList.add(mjList.size() - 1, mj12);
                                                continue block41;
                                            }
                                            case 2: {
                                                mjList.add(mjList.size() - 2, mj12);
                                            }
                                        }
                                    }
                                    for (i = 0; i < mjList.size(); ++i) {
                                        mj12 = (Mj)mjList.get(i);
                                        if (mj12.isMaima()) continue;
                                        namj = mj12;
                                        break;
                                    }
                                }
                                if (namj == null) {
                                    MainController.showTextDialog("\u672c\u5c40\u7ed3\u675f");
                                    MainController.clear();
                                    MainController.displayYinaPai();
                                    return;
                                }
                                namj.getWeina_imageView().setVisible(false);
                                if (namj.getOther() != null) {
                                    namj.getOther().setVisible(false);
                                }
                                namj.setSimo(true);
                                Mj finalNamj = namj;
                                Config.weina.forEach(mj1 -> {
                                    if (finalNamj.equals(mj1)) {
                                        mj1.setSimo(true);
                                    }
                                });
                                weina.remove(namj);
                                namj.setNapai_direction(finalFangxian1);
                                Config.yina.add(namj);
                                break;
                            }
                            case "\u987a\u6760\u724c": {
                                namj = null;
                                for (int i = 0; i < weina.size(); ++i) {
                                    Mj mj14 = weina.get(i);
                                    if (mj14.isMaima()) continue;
                                    namj = mj14;
                                    break;
                                }
                                if (namj == null) {
                                    MainController.showTextDialog("\u672c\u5c40\u7ed3\u675f");
                                    MainController.clear();
                                    MainController.displayYinaPai();
                                    return;
                                }
                                namj.getWeina_imageView().setVisible(false);
                                if (namj.getOther() != null) {
                                    namj.getOther().setVisible(false);
                                }
                                namj.setSimo(true);
                                Mj finalNamj = namj;
                                Config.weina.forEach(mj1 -> {
                                    if (finalNamj.equals(mj1)) {
                                        mj1.setSimo(true);
                                    }
                                });
                                weina.remove(namj);
                                namj.setNapai_direction(finalFangxian1);
                                Config.yina.add(namj);
                                break;
                            }
                        }
                    }
                }
                if (++finalFangxian <= 3) continue;
                finalFangxian = 0;
            } while (finalFangxian != dapaifangx);
            Config.yuce.clear();
            MainController.yucepaiAfterMopai(0, dapaifangx, 1);
        }
    }

    private void NaPaiHouXianShi() {
        Observable.create(emitter -> {
            if (Config.systemConfig.zixuanhuapai == 1) {
                ZixuanhuapaiImpl zixuanhuapaiServe = new ZixuanhuapaiImpl(this);
                zixuanhuapaiServe.hupaixuanze(emitter);
            } else if (Config.systemConfig.leqing == 1) {
                Config.weina.forEach(mj -> {
                    mj.kedi_huapai = false;
                    if (mj.getResult() == 35) {
                        mj.kedi_huapai = true;
                        mj.yuanpai = mj.getResult();
                        mj.setResult(41);
                    }
                });
                emitter.onNext(1);
            } else {
                emitter.onNext(1);
            }
        }).subscribe(integer -> {
            if (Config.systemConfig.huapaidaiticaishengpai_foushanmajiang == 0 && (caisheng.size() == 0 || caisheng.get(0) < 40)) {
                this.ganghua();
            }
            Observable.create(emitter -> {
                if (Config.systemConfig.danzhangkegang == 1) {
                    DanzhangkegangImpl danzhangkegang = new DanzhangkegangImpl(this);
                    danzhangkegang.danzhangkegang(emitter);
                } else {
                    emitter.onNext(1);
                }
            }).subscribe(integer2 -> {
                if (Config.systemConfig.zixuancaisheng != 1 || meijuchongshecaisheng != 0) {
                    caisheng.clear();
                }
                Observable.create(emitter -> {
                    if (Config.systemConfig.zhuapaihouxuancaisheng == 1) {
                        MainController.displayYinaPai();
                    }
                    if (Config.systemConfig.shuangcaisheng == 1) {
                        caisheng.add(Config.systemConfig.shangcaisheng_combox);
                    }
                    CaishengxuanzeImpl caishengxuanze = new CaishengxuanzeImpl(this);
                    caishengxuanze.caishengxuanze(emitter);
                }).subscribe(integer3 -> {
                    if (Config.systemConfig.zhuapaihouxuancaisheng == 0 || caisheng.size() == 0) {
                        MainController.displayYinaPai();
                    }
                    this.BiaoJiCaiSheng();
                });
            });
        });
    }

    private static void yichu(Mj mj) {
        if (Config.yichu.contains(mj)) {
            return;
        }
        mj.setChupai_direction(dapaifangx);
        Config.yina.remove(mj);
        directions.get(mj.getNapai_direction()).getYinaPanel().getChildren().remove(mj.getYina_imageView());
        yichu_pane.getChildren().clear();
        if (Config.systemConfig.chupaixianshi == 1) {
            Config.yichu.sort(new ComparatorMjResult());
        }
        Config.yichu.forEach(mj1 -> {
            mj1.getYichu_imageView().getStyleClass().clear();
            mj1.getYichu_imageView().setImage(Config.getImageByNumber(mj1.getResult()));
            if (Config.huanhoupai_yixuan.contains(mj1)) {
                mj1.getYichu_imageView().setImage(ImageKit.coverImage(mj1.getYina_imageView().getImage(), Config.LVSE));
            }
            if (Config.jiuzhengpai_yixuan.contains(mj1)) {
                System.out.println("\u7ea0\u6b63\u7684\u6362\u724c");
                mj1.getYichu_imageView().setImage(ImageKit.coverImage(mj1.getYina_imageView().getImage(), Config.HONGSE));
            }
            yichu_pane.getChildren().add(mj1.getYichu_imageView());
        });
        ImageView imageView = new ImageView();
        imageView.setImage(Config.getImageByNumber(mj.getResult()));
        imageView.setFitHeight(Config.MJ_HEIGHT_1_2);
        imageView.setFitWidth(Config.MJ_WIDTH_1_2);
        imageView.getStyleClass().clear();
        imageView.getStyleClass().add("mj_chu");
        if (Config.huanhoupai_yixuan.contains(mj)) {
            imageView.setImage(ImageKit.coverImage(mj.getYina_imageView().getImage(), Config.LVSE));
        } else if (Config.jiuzhengpai_yixuan.contains(mj)) {
            System.out.println("\u7ea0\u6b63\u7684\u6362\u724c");
            imageView.setImage(ImageKit.coverImage(mj.getYina_imageView().getImage(), Config.HONGSE));
        }
        mj.setYichu_imageView(imageView);
        Config.yichu.add(mj);
        yichu_pane.getChildren().add(imageView);
    }

    public static void houGangMopai() {
        Mj mj3;
        int i;
        LinkedList<Mj> mjList;
        if (weina.size() == 0) {
            MainController.showTextDialog("\u672c\u5c40\u7ed3\u675f");
            MainController.clear();
            MainController.displayYinaPai();
            return;
        }
        Config.yina.forEach(mj2 -> mj2.setLast(false));
        MainController.clear();
        Mj namj = null;
        if (Config.systemConfig.mapaicengshu == 2) {
            mjList = new LinkedList<Mj>();
            for (i = weina.size() - 1; i >= 0; --i) {
                mj3 = weina.get(i);
                if (mj3.getMahjong_number() % 2 != 0) {
                    mjList.add(mjList.size() - 1, mj3);
                    continue;
                }
                mjList.add(mj3);
            }
            for (i = 0; i < mjList.size(); ++i) {
                mj3 = (Mj)mjList.get(i);
                if (mj3.isMaima()) continue;
                namj = mj3;
                break;
            }
        }
        if (Config.systemConfig.mapaicengshu == 3) {
            mjList = new LinkedList();
            block7: for (i = weina.size() - 1; i >= 0; --i) {
                mj3 = weina.get(i);
                switch (mj3.getMahjong_number() % 3) {
                    case 0: {
                        mjList.add(mj3);
                        continue block7;
                    }
                    case 1: {
                        mjList.add(mjList.size() - 1, mj3);
                        continue block7;
                    }
                    case 2: {
                        mjList.add(mjList.size() - 2, mj3);
                    }
                }
            }
            for (i = 0; i < mjList.size(); ++i) {
                mj3 = (Mj)mjList.get(i);
                if (mj3.isMaima()) continue;
                namj = mj3;
                break;
            }
        }
        if (namj == null) {
            MainController.showTextDialog("\u672c\u5c40\u7ed3\u675f");
            MainController.clear();
            MainController.displayYinaPai();
            return;
        }
        namj.getWeina_imageView().setVisible(false);
        if (namj.getOther() != null) {
            namj.getOther().setVisible(false);
        }
        namj.setSimo(true);
        Mj finalNamj = namj;
        Config.weina.forEach(mj -> {
            if (finalNamj.equals(mj)) {
                mj.setSimo(true);
            }
        });
        weina.remove(namj);
        namj.setNapai_direction(dapaifangx);
        namj.setLast(true);
        Config.yina.add(namj);
        MainController.yucepaiAfterMopai(0, dapaifangx, 1);
        MainController.displayYinaPai();
    }

    public static void SuiYiGang() {
        directions.get(dapaifangx).getCaozuoPanel().getChildren().clear();
        if (weina.size() == 0) {
            MainController.showTextDialog("\u672c\u5c40\u7ed3\u675f");
            MainController.clear();
            MainController.displayYinaPai();
            return;
        }
        Config.yina.forEach(mj2 -> mj2.setLast(false));
        Observable.<Mj>create(emitter -> {
            weina.forEach(mj -> mj.getWeina_imageView().setOnMouseClicked(event -> emitter.onNext(mj)));
            if (suiyigang_count == 0) {
                MainController.showTextDialog("\u968f\u610f\u6760,\u8bf7\u6478\u724c");
                ++suiyigang_count;
            }
        }).subscribe(mj -> {
            MainController.clear();
            mj.getWeina_imageView().setVisible(false);
            if (mj.getOther() != null) {
                mj.getOther().setVisible(false);
            }
            mj.setSimo(true);
            Config.weina.forEach(mj1 -> {
                if (mj.equals(mj1)) {
                    mj1.setSimo(true);
                }
            });
            weina.remove(mj);
            mj.setNapai_direction(dapaifangx);
            mj.setLast(true);
            Config.yina.add((Mj)mj);
            MainController.yucepaiAfterMopai(0, dapaifangx, 1);
            MainController.displayYinaPai();
        });
    }

    public static void mopai() {
        if (weina.size() == 0) {
            MainController.showTextDialog("\u672c\u5c40\u7ed3\u675f");
            MainController.clear();
            MainController.displayYinaPai();
            return;
        }
        dapaifangx = Config.systemConfig.niezhuanimo == 1 ? ++dapaifangx : --dapaifangx;
        do {
            if (Config.systemConfig.niezhuanimo == 1) {
                if (--dapaifangx >= 0) continue;
                dapaifangx = 3;
                continue;
            }
            if (++dapaifangx <= 3) continue;
            dapaifangx = 0;
        } while (directions.get(dapaifangx).getHu() == 1 && Config.systemConfig.hupaikemo == 0 || directions.get(dapaifangx).getQiangzhiHu() == 1 || directions.get(dapaifangx).isQueren());
        if (Config.yina.stream().filter(mj -> mj.getNapai_direction() == dapaifangx && mj.getPaizhuantai() == 0).count() % 3L == 2L) {
            MainController.clear();
            MainController.displayYinaPai();
            return;
        }
        Config.yina.forEach(mj2 -> mj2.setLast(false));
        MainController.clear();
        Mj namj = null;
        for (int i = 0; i < weina.size(); ++i) {
            Mj mj3 = weina.get(i);
            if (mj3.isMaima()) continue;
            namj = mj3;
            break;
        }
        if (namj == null) {
            MainController.showTextDialog("\u672c\u5c40\u7ed3\u675f");
            MainController.clear();
            MainController.displayYinaPai();
            return;
        }
        namj.getWeina_imageView().setVisible(false);
        if (namj.getOther() != null) {
            namj.getOther().setVisible(false);
        }
        namj.setSimo(true);
        Mj finalNamj = namj;
        Config.weina.forEach(mj -> {
            if (finalNamj.equals(mj)) {
                mj.setSimo(true);
            }
        });
        weina.remove(namj);
        namj.setNapai_direction(dapaifangx);
        Config.yuce.clear();
        namj.setLast(true);
        Config.yina.add(namj);
        MainController.yucepaiAfterMopai(0, dapaifangx, 1);
        MainController.displayYinaPai();
    }

    public static boolean yucepaiAfterMopai(int index, int napaifangxiang, int last_index) {
        Mj yuce;
        block37: {
            block36: {
                if (weina.size() == 0) {
                    return true;
                }
                if (index == weina.size()) {
                    return true;
                }
                long renshu = directions.stream().filter(direction -> (direction.getHu() == 0 || Config.systemConfig.hupaikemo == 1 && direction.getQiangzhiHu() == 0) && !direction.isQueren()).count();
                if ((long)Config.yuce.size() == renshu * (long)Config.systemConfig.yucepai.intValue()) {
                    return true;
                }
                do {
                    if (Config.systemConfig.niezhuanimo == 1) {
                        if (--napaifangxiang >= 0) continue;
                        napaifangxiang = 3;
                        continue;
                    }
                    if (++napaifangxiang <= 3) continue;
                    napaifangxiang = 0;
                } while (directions.get(napaifangxiang).getHu() == 1 && Config.systemConfig.hupaikemo == 0 || directions.get(napaifangxiang).getQiangzhiHu() == 1 || directions.get(napaifangxiang).isQueren());
                yuce = null;
                if (last_index == 1) {
                    weina_catch.clear();
                    weina_catch.addAll(weina);
                }
                if (Config.systemConfig.yucepaibuxianshihuapai != 1) break block36;
                block5 : switch (Config.systemConfig.ganghua) {
                    case "\u540e\u6760\u724c": {
                        if (Config.systemConfig.mapaicengshu == 2 || Config.systemConfig.mapaicengshu == 4) {
                            while (index < weina.size()) {
                                Mj mj = weina.get(index);
                                ++index;
                                if (!mj.isMaima() && mj.getResult() < 40) {
                                    yuce = mj;
                                    break;
                                }
                                if (mj.isMaima() || mj.getResult() <= 40) continue;
                                System.out.println("\u82b1\u724c\u89d2\u6807\uff1a" + last_index);
                                Mj mj2 = weina_catch.getLast();
                                if (mj2.getMahjong_number() % 2 == 0) {
                                    System.out.println("\u662f\u5076\u6570\u724c\u9000\u4f4d\u4e00\u5f20\u641c\u7d22\uff0c");
                                    mj2 = weina_catch.get(weina_catch.size() - 2);
                                    if (mj2.getMahjong_number() % 2 == 0) {
                                        mj2 = weina_catch.getLast();
                                    }
                                } else {
                                    System.out.println("\u5947\u6570\u724c\uff0c\u4e0a\u5c42\u724c\uff0c\u9ed8\u8ba4\u9009\u62e9\u5373\u53ef");
                                }
                                ++last_index;
                                yuce = mj2;
                                weina_catch.remove(mj2);
                                System.out.println("\u540e\u6760\u9884\u6d4b\u724c\uff1a" + yuce.getResult());
                                break;
                            }
                        }
                        if (Config.systemConfig.mapaicengshu != 3) break;
                        List<Mj> mjList = new LinkedList<Mj>();
                        block17: for (int i = weina.size() - 1; i >= 0; --i) {
                            Mj mj1 = weina.get(i);
                            switch (mj1.getMahjong_number() % 3) {
                                case 0: {
                                    mjList.add(mj1);
                                    continue block17;
                                }
                                case 1: {
                                    mjList.add(mjList.size() - 1, mj1);
                                    continue block17;
                                }
                                case 2: {
                                    mjList.add(mjList.size() - 2, mj1);
                                }
                            }
                        }
                        while (index < mjList.size()) {
                            Mj mj = (Mj)mjList.get(index);
                            ++index;
                            if (mj.isMaima()) continue;
                            if (mj.getResult() > 40 && Config.systemConfig.yucepaibuxianshihuapai == 1) {
                                System.out.println("3\u5c42\u540e\u6760\uff0c\u82b1\u724c\u8df3\u8fc7" + mj.getResult());
                                continue;
                            }
                            yuce = mj;
                            break block5;
                        }
                        break block37;
                    }
                    case "\u987a\u6760\u724c": {
                        while (index < weina.size()) {
                            Mj mj = weina.get(index);
                            ++index;
                            if (mj.isMaima()) continue;
                            if (mj.getResult() > 40 && Config.systemConfig.yucepaibuxianshihuapai == 1) {
                                System.out.println("\u987a\u6760\uff0c\u82b1\u724c\u8df3\u8fc7" + mj.getResult());
                                continue;
                            }
                            yuce = mj;
                            break block5;
                        }
                        break;
                    }
                    case "\u968f\u610f\u6760": {
                        while (index < weina.size()) {
                            Mj mj = weina.get(index);
                            ++index;
                            if (mj.isMaima()) continue;
                            if (mj.getResult() > 40 && Config.systemConfig.yucepaibuxianshihuapai == 1) {
                                System.out.println("\u968f\u610f\u6760\uff0c\u82b1\u724c\u8df3\u8fc7" + mj.getResult());
                                continue;
                            }
                            yuce = mj;
                            break block5;
                        }
                        break;
                    }
                }
                break block37;
            }
            while (index < weina.size()) {
                Mj mj = weina.get(index);
                ++index;
                if (mj.isMaima()) continue;
                yuce = mj;
                break;
            }
        }
        if (yuce == null) {
            return true;
        }
        yuce.setYuce_direction(napaifangxiang);
        Config.yuce.add(yuce);
        return MainController.yucepaiAfterMopai(index, napaifangxiang, last_index);
    }

    public static void initYuceYinaPanel() {
        yuceYinaPanel.setVisible(true);
        if (!static_main_panel.getChildren().contains(yuceYinaPanel)) {
            static_main_panel.getChildren().add(yuceYinaPanel);
        }
        yuceYinaPanel.display();
        yuceYinaPanel.applyCss();
        yuceYinaPanel.layout();
        yuceYinaPanel.setLayoutY(Config.SCREEN_HEIGHT / 2.0 - yuceYinaPanel.getBoundsInLocal().getHeight() / 2.0);
        yuceYinaPanel.setLayoutX(Config.SCREEN_WIDTH / 2.0 - yuceYinaPanel.getBoundsInLocal().getWidth() / 2.0);
    }

    private void initXuanzhuangPanel() {
        if (Config.systemConfig.zhuapaihouxuanzhuang == 1) {
            zhuangPanel.getTop_text().setText("\u8bf7\u9009\u62e9\u8d77\u724c\u65b9");
            zhuangPanel.setLayoutY(Config.SCREEN_HEIGHT / 2.0 - zhuangPanel.getHeight() / 2.0);
            zhuangPanel.setLayoutX(Config.SCREEN_WIDTH / 2.0 - zhuangPanel.getWidth() / 2.0);
            zhuangPanel.getDong_imageview().setOnMouseClicked(event -> {
                AnchorPane imageView = (AnchorPane)event.getSource();
                MainController.xuanzeFangwei((Integer)imageView.getUserData());
            });
            zhuangPanel.getNan_imageview().setOnMouseClicked(event -> {
                AnchorPane imageView = (AnchorPane)event.getSource();
                MainController.xuanzeFangwei((Integer)imageView.getUserData());
            });
            zhuangPanel.getXi_imageview().setOnMouseClicked(event -> {
                AnchorPane imageView = (AnchorPane)event.getSource();
                MainController.xuanzeFangwei((Integer)imageView.getUserData());
            });
            zhuangPanel.getBei_imageview().setOnMouseClicked(event -> {
                AnchorPane imageView = (AnchorPane)event.getSource();
                MainController.xuanzeFangwei((Integer)imageView.getUserData());
            });
        } else {
            zhuangPanel.getTop_text().setText("\u8bf7\u9009\u62e9\u5e84\u5bb6");
            zhuangPanel.setLayoutY(Config.SCREEN_HEIGHT / 2.0 - zhuangPanel.getHeight() / 2.0);
            zhuangPanel.setLayoutX(Config.SCREEN_WIDTH / 2.0 - zhuangPanel.getWidth() / 2.0);
            zhuangPanel.getDong_imageview().setOnMouseClicked(event -> {
                AnchorPane imageView = (AnchorPane)event.getSource();
                MainController.xuanzeFangwei((Integer)imageView.getUserData());
            });
            zhuangPanel.getNan_imageview().setOnMouseClicked(event -> {
                AnchorPane imageView = (AnchorPane)event.getSource();
                MainController.xuanzeFangwei((Integer)imageView.getUserData());
            });
            zhuangPanel.getXi_imageview().setOnMouseClicked(event -> {
                AnchorPane imageView = (AnchorPane)event.getSource();
                MainController.xuanzeFangwei((Integer)imageView.getUserData());
            });
            zhuangPanel.getBei_imageview().setOnMouseClicked(event -> {
                AnchorPane imageView = (AnchorPane)event.getSource();
                MainController.xuanzeFangwei((Integer)imageView.getUserData());
            });
        }
        zhuangPanel.setVisible(true);
        if (renyicaisheng_label == null) {
            renyicaisheng_label = new Label("\u8bf7\u9009\u62e9\u8d22\u795e");
            renyicaisheng_label.setLayoutY(Config.SCREEN_HEIGHT / 2.0 - renyicaisheng_label.getHeight() / 2.0);
            renyicaisheng_label.setLayoutX(Config.SCREEN_WIDTH / 2.0 - renyicaisheng_label.getWidth() / 2.0);
            renyicaisheng_label.getStyleClass().add("renyicaisheng");
        }
        if (!this.main_panel.getChildren().contains(renyicaisheng_label)) {
            renyicaisheng_label.setVisible(false);
            this.main_panel.getChildren().add(renyicaisheng_label);
        }
        if (!this.main_panel.getChildren().contains(zhuangPanel)) {
            this.main_panel.getChildren().add(zhuangPanel);
        }
    }

    private void initXuanQuePanel() {
        if (directions.stream().filter(d -> d.isQueren()).count() < (long)(4 - Config.systemConfig.wanjiarenshu)) {
            zhuangPanel.getTop_text().setText("\u8bf7\u9009\u62e9\u7f3a\u65b9");
            Observable.create(emitter -> {
                zhuangPanel.setLayoutY(Config.SCREEN_HEIGHT / 2.0 - zhuangPanel.getHeight() / 2.0);
                zhuangPanel.setLayoutX(Config.SCREEN_WIDTH / 2.0 - zhuangPanel.getWidth() / 2.0);
                zhuangPanel.getDong_que().setOnMouseClicked(event -> {
                    Label label = (Label)event.getSource();
                    label.setVisible(false);
                    zhuangPanel.getDong_imageview().setVisible(true);
                    directions.get((Integer)label.getUserData()).setQueren(false);
                    emitter.onNext(Integer.valueOf((Integer)label.getUserData()));
                });
                zhuangPanel.getDong_imageview().setOnMouseClicked(event -> {
                    AnchorPane imageView = (AnchorPane)event.getSource();
                    imageView.setVisible(false);
                    zhuangPanel.getDong_que().setVisible(true);
                    directions.get((Integer)imageView.getUserData()).setQueren(true);
                    emitter.onNext(Integer.valueOf((Integer)imageView.getUserData()));
                });
                zhuangPanel.getNan_que().setOnMouseClicked(event -> {
                    Label label = (Label)event.getSource();
                    label.setVisible(false);
                    zhuangPanel.getNan_imageview().setVisible(true);
                    directions.get((Integer)label.getUserData()).setQueren(false);
                    emitter.onNext(Integer.valueOf((Integer)label.getUserData()));
                });
                zhuangPanel.getNan_imageview().setOnMouseClicked(event -> {
                    AnchorPane imageView = (AnchorPane)event.getSource();
                    imageView.setVisible(false);
                    zhuangPanel.getNan_que().setVisible(true);
                    directions.get((Integer)imageView.getUserData()).setQueren(true);
                    emitter.onNext(Integer.valueOf((Integer)imageView.getUserData()));
                });
                zhuangPanel.getXi_que().setOnMouseClicked(event -> {
                    Label label = (Label)event.getSource();
                    label.setVisible(false);
                    zhuangPanel.getXi_imageview().setVisible(true);
                    directions.get((Integer)label.getUserData()).setQueren(false);
                    emitter.onNext(Integer.valueOf((Integer)label.getUserData()));
                });
                zhuangPanel.getXi_imageview().setOnMouseClicked(event -> {
                    AnchorPane imageView = (AnchorPane)event.getSource();
                    imageView.setVisible(false);
                    zhuangPanel.getXi_que().setVisible(true);
                    directions.get((Integer)imageView.getUserData()).setQueren(true);
                    emitter.onNext(Integer.valueOf((Integer)imageView.getUserData()));
                });
                zhuangPanel.getBei_que().setOnMouseClicked(event -> {
                    Label label = (Label)event.getSource();
                    label.setVisible(false);
                    zhuangPanel.getBei_imageview().setVisible(true);
                    directions.get((Integer)label.getUserData()).setQueren(false);
                    emitter.onNext(Integer.valueOf((Integer)label.getUserData()));
                });
                zhuangPanel.getBei_imageview().setOnMouseClicked(event -> {
                    AnchorPane imageView = (AnchorPane)event.getSource();
                    imageView.setVisible(false);
                    zhuangPanel.getBei_que().setVisible(true);
                    directions.get((Integer)imageView.getUserData()).setQueren(true);
                    emitter.onNext(Integer.valueOf((Integer)imageView.getUserData()));
                });
                zhuangPanel.setVisible(true);
                if (!this.main_panel.getChildren().contains(zhuangPanel)) {
                    this.main_panel.getChildren().add(zhuangPanel);
                }
            }).subscribe(que -> {
                if (directions.stream().filter(d -> d.isQueren()).count() == (long)(4 - Config.systemConfig.wanjiarenshu)) {
                    zhuangPanel.getDong_que().setOnMouseClicked(null);
                    zhuangPanel.getNan_que().setOnMouseClicked(null);
                    zhuangPanel.getXi_que().setOnMouseClicked(null);
                    zhuangPanel.getBei_que().setOnMouseClicked(null);
                    this.initXuanzhuangPanel();
                }
            });
        } else {
            this.initXuanzhuangPanel();
        }
    }

    private void maima(Mj mj1) {
        if (mj1.isMaima()) {
            mj1.getWeina_imageView().setStyle("-fx-opacity: 1");
            if (mj1.getOther() != null) {
                mj1.getOther().setVisible(true);
            }
            mj1.setMaima(false);
        } else {
            mj1.getWeina_imageView().setStyle("-fx-opacity: 0.5");
            if (mj1.getOther() != null) {
                mj1.getOther().setVisible(false);
            }
            mj1.setMaima(true);
        }
    }

    private void displayWeinaPai() {
        this.main_panel.getChildren().remove(yichu_pane);
        this.main_panel.getChildren().remove(zhuangPanel);
        if (Config.systemConfig.baoliupai != 1 || Config.systemConfig.baoliupai == 1 && dapaifangx == -1) {
            if (Config.systemConfig.wanjiarenshu < 4) {
                if (Config.systemConfig.meijuchongxuanquefang == 1) {
                    directions.forEach(d -> d.setQueren(false));
                    zhuangPanel.getBei_que().setVisible(false);
                    zhuangPanel.getBei_imageview().setVisible(true);
                    zhuangPanel.getDong_que().setVisible(false);
                    zhuangPanel.getDong_imageview().setVisible(true);
                    zhuangPanel.getXi_que().setVisible(false);
                    zhuangPanel.getXi_imageview().setVisible(true);
                    zhuangPanel.getNan_que().setVisible(false);
                    zhuangPanel.getNan_imageview().setVisible(true);
                }
                this.initXuanQuePanel();
            } else {
                directions.forEach(d -> d.setQueren(false));
                zhuangPanel.getDong_imageview().setVisible(true);
                zhuangPanel.getNan_imageview().setVisible(true);
                zhuangPanel.getXi_imageview().setVisible(true);
                zhuangPanel.getBei_imageview().setVisible(true);
                zhuangPanel.getDong_que().setVisible(false);
                zhuangPanel.getNan_que().setVisible(false);
                zhuangPanel.getXi_que().setVisible(false);
                zhuangPanel.getBei_que().setVisible(false);
                this.initXuanzhuangPanel();
            }
        }
        Config.weina.forEach(mj -> {
            if (mj.getWeina_imageView() != null) {
                this.main_panel.getChildren().remove(mj.getWeina_imageView());
                if (mj.getOther() != null) {
                    this.main_panel.getChildren().remove(mj.getOther());
                }
            }
        });
        this.guozuang_top_label.forEach(label -> this.main_panel.getChildren().remove(label));
        this.guozuang_top_label.clear();
        List<Mj> weina_right = Config.weina.stream().filter(mj -> mj.getDirection() == rightPanel.getDirection()).collect(Collectors.toList());
        List<Mj> weina_bottom = Config.weina.stream().filter(mj -> mj.getDirection() == bottomPanel.getDirection()).collect(Collectors.toList());
        List<Mj> weina_top = Config.weina.stream().filter(mj -> mj.getDirection() == topPanel.getDirection()).collect(Collectors.toList());
        List<Mj> weina_left = Config.weina.stream().filter(mj -> mj.getDirection() == leftPanel.getDirection()).collect(Collectors.toList());
        weina_right.sort(new ComparatorMj());
        weina_bottom.sort(new ComparatorMj());
        weina_top.sort(new ComparatorMj());
        weina_left.sort(new ComparatorMj());
        if (Config.systemConfig.mapaicengshu == 2) {
            double y = Config.MJ_HEIGHT + Config.MJ_HEIGHT_3_4 + Config.MJ_WIDTH * 2.0;
            int size = weina_bottom.size() == 0 ? weina_top.size() : weina_bottom.size();
            double x = Config.SCREEN_WIDTH / 2.0 + Config.MJ_WIDTH_1_2 * (double)size / 2.0 / 2.0 - 5.0;
            double finalY = y;
            double finalX = x;
            yichu_pane.setMinWidth(Config.MJ_WIDTH_1_2 * 13.0 - 40.0);
            yichu_pane.setMaxWidth(Config.MJ_WIDTH_1_2 * 13.0 - 40.0);
            yichu_pane.setLayoutX(x - Config.MJ_WIDTH_1_2 * 13.0 + 20.0);
            yichu_pane.setLayoutY(y + Config.MJ_HEIGHT + 10.0);
            this.main_panel.getChildren().add(yichu_pane);
            weina_top.forEach(mj -> {
                ImageView imageView = new ImageView();
                imageView.setUserData(mj);
                imageView.setImage(Config.getImageByNumber(mj.getResult()));
                imageView.setCursor(Cursor.HAND);
                imageView.setFitHeight(Config.MJ_HEIGHT_1_2);
                imageView.setFitWidth(Config.MJ_WIDTH_1_2);
                if (mj.getMahjong_number() % 2 != 0) {
                    imageView.setLayoutY(finalY + imageView.getFitHeight());
                    imageView.setLayoutX(finalX - (double)(mj.getMahjong_number() / 2) * imageView.getFitWidth());
                    Text text = new Text(weina_top.size() / 2 - mj.getMahjong_number() / 2 + "");
                    text.setLayoutY(finalY + imageView.getFitHeight() + imageView.getFitHeight() + 10.0);
                    text.setLayoutX(finalX - (double)(mj.getMahjong_number() / 2) * imageView.getFitWidth() + imageView.getFitWidth() / 2.0 - 5.0);
                    text.setFill(Color.WHITE);
                    this.main_panel.getChildren().add(text);
                    mj.setOther(text);
                } else {
                    imageView.setLayoutY(finalY);
                    imageView.setLayoutX(finalX - (double)(mj.getMahjong_number() / 2) * imageView.getFitWidth());
                }
                imageView.setOnMouseClicked(event -> {
                    if (zhuangjia < 0) {
                        if (Config.systemConfig.xianmaimahoudingzhuang == 1) {
                            this.maima((Mj)imageView.getUserData());
                        } else {
                            MainController.showTextDialog("\u8bf7\u5148\u9009\u5e84");
                        }
                        return;
                    }
                    this.napaisuanfa((Mj)imageView.getUserData());
                });
                this.main_panel.getChildren().add(imageView);
                mj.setWeina_imageView(imageView);
                if (mj.getSimo()) {
                    mj.getWeina_imageView().setVisible(false);
                    if (mj.getOther() != null) {
                        mj.getOther().setVisible(false);
                    }
                }
            });
            size = weina_bottom.size() == 0 ? weina_top.size() : weina_bottom.size();
            x = Config.SCREEN_WIDTH / 2.0 + Config.MJ_WIDTH_1_2 * (double)(size / 2) / 2.0 + Config.MJ_WIDTH * 3.0 / 2.0;
            double finalY1 = y = Config.SCREEN_HEIGHT / 2.0 + Config.MJ_WIDTH_1_2 * (double)(weina_right.size() / 2) / 2.0 + Config.MJ_WIDTH_1_2;
            double finalX1 = x;
            weina_right.forEach(mj -> {
                ImageView imageView = new ImageView();
                imageView.setUserData(mj);
                imageView.setCursor(Cursor.HAND);
                Image image = Config.getImageByNumber(mj.getResult());
                imageView.setImage(image);
                imageView.setFitHeight(Config.MJ_HEIGHT_1_2);
                imageView.setFitWidth(Config.MJ_WIDTH_1_2);
                imageView.getTransforms().add(new Rotate(-90.0));
                if (mj.getMahjong_number() % 2 != 0) {
                    imageView.setLayoutY(finalY1 - (double)(mj.getMahjong_number() / 2) * imageView.getFitWidth());
                    imageView.setLayoutX(finalX1);
                } else {
                    imageView.setLayoutY(finalY1 - (double)(mj.getMahjong_number() / 2) * imageView.getFitWidth());
                    imageView.setLayoutX(finalX1 + imageView.getFitHeight());
                    Text text = new Text(weina_right.size() / 2 - mj.getMahjong_number() / 2 + "");
                    text.setLayoutY(finalY1 - (double)(mj.getMahjong_number() / 2) * imageView.getFitWidth() - imageView.getFitWidth() / 2.0 + 5.0);
                    text.setLayoutX(finalX1 - 20.0);
                    text.setFill(Color.WHITE);
                    this.main_panel.getChildren().add(text);
                    mj.setOther(text);
                }
                imageView.setOnMouseClicked(event -> {
                    if (zhuangjia < 0) {
                        if (Config.systemConfig.xianmaimahoudingzhuang == 1) {
                            this.maima((Mj)imageView.getUserData());
                        } else {
                            MainController.showTextDialog("\u8bf7\u5148\u9009\u5e84");
                        }
                        return;
                    }
                    this.napaisuanfa((Mj)imageView.getUserData());
                });
                this.main_panel.getChildren().add(imageView);
                mj.setWeina_imageView(imageView);
                if (mj.getSimo()) {
                    mj.getWeina_imageView().setVisible(false);
                    if (mj.getOther() != null) {
                        mj.getOther().setVisible(false);
                    }
                }
            });
            y = Config.SCREEN_HEIGHT - Config.MJ_HEIGHT * 4.0 - 50.0;
            size = weina_bottom.size() == 0 ? weina_top.size() : weina_bottom.size();
            x = Config.SCREEN_WIDTH / 2.0 - Config.MJ_WIDTH_1_2 * (double)(size / 2) / 2.0 + Config.MJ_WIDTH_1_2;
            double finalY2 = y;
            double finalX2 = x;
            weina_bottom.forEach(mj -> {
                ImageView imageView = new ImageView();
                imageView.setUserData(mj);
                imageView.setCursor(Cursor.HAND);
                imageView.setImage(Config.getImageByNumber(mj.getResult()));
                imageView.setFitHeight(Config.MJ_HEIGHT_1_2);
                imageView.setFitWidth(Config.MJ_WIDTH_1_2);
                if (mj.getMahjong_number() % 2 != 0) {
                    imageView.setLayoutY(finalY2);
                    imageView.setLayoutX(finalX2 + (double)(mj.getMahjong_number() / 2) * imageView.getFitWidth());
                } else {
                    imageView.setLayoutY(finalY2 + imageView.getFitHeight());
                    imageView.setLayoutX(finalX2 + (double)(mj.getMahjong_number() / 2) * imageView.getFitWidth());
                    Text text = new Text(weina_bottom.size() / 2 - mj.getMahjong_number() / 2 + "");
                    text.setLayoutY(finalY2 - 5.0);
                    text.setLayoutX(finalX2 + (double)(mj.getMahjong_number() / 2) * imageView.getFitWidth() + imageView.getFitWidth() / 2.0 - 5.0);
                    text.setFill(Color.WHITE);
                    this.main_panel.getChildren().add(text);
                    mj.setOther(text);
                }
                imageView.setOnMouseClicked(event -> {
                    if (zhuangjia < 0) {
                        if (Config.systemConfig.xianmaimahoudingzhuang == 1) {
                            this.maima((Mj)imageView.getUserData());
                        } else {
                            MainController.showTextDialog("\u8bf7\u5148\u9009\u5e84");
                        }
                        return;
                    }
                    this.napaisuanfa((Mj)imageView.getUserData());
                });
                this.main_panel.getChildren().add(imageView);
                mj.setWeina_imageView(imageView);
                if (mj.getSimo()) {
                    mj.getWeina_imageView().setVisible(false);
                    if (mj.getOther() != null) {
                        mj.getOther().setVisible(false);
                    }
                }
            });
            size = weina_bottom.size() == 0 ? weina_top.size() : weina_bottom.size();
            x = Config.SCREEN_WIDTH / 2.0 - Config.MJ_WIDTH_1_2 * (double)(size / 2) / 2.0 - Config.MJ_HEIGHT;
            double finalY3 = y = Config.SCREEN_HEIGHT / 2.0 - Config.MJ_WIDTH_1_2 * (double)(weina_left.size() / 2) / 2.0 + Config.MJ_WIDTH_1_2;
            double finalX3 = x;
            weina_left.forEach(mj -> {
                ImageView imageView = new ImageView();
                imageView.setUserData(mj);
                imageView.setCursor(Cursor.HAND);
                Image image = Config.getImageByNumber(mj.getResult());
                imageView.setImage(image);
                imageView.setFitHeight(Config.MJ_HEIGHT_1_2);
                imageView.setFitWidth(Config.MJ_WIDTH_1_2);
                imageView.getTransforms().add(new Rotate(90.0));
                if (mj.getMahjong_number() % 2 != 0) {
                    imageView.setLayoutY(finalY3 + (double)(mj.getMahjong_number() / 2) * imageView.getFitWidth());
                    imageView.setLayoutX(finalX3 + imageView.getFitHeight());
                    Text text = new Text(weina_left.size() / 2 - mj.getMahjong_number() / 2 + "");
                    text.setLayoutY(finalY3 + (double)(mj.getMahjong_number() / 2) * imageView.getFitWidth() + imageView.getFitWidth() / 2.0 + 5.0);
                    text.setLayoutX(finalX3 + imageView.getFitHeight() + 5.0);
                    text.setFill(Color.WHITE);
                    this.main_panel.getChildren().add(text);
                    mj.setOther(text);
                } else {
                    imageView.setLayoutY(finalY3 + (double)(mj.getMahjong_number() / 2) * imageView.getFitWidth());
                    imageView.setLayoutX(finalX3);
                }
                imageView.setOnMouseClicked(event -> {
                    if (zhuangjia < 0) {
                        if (Config.systemConfig.xianmaimahoudingzhuang == 1) {
                            this.maima((Mj)imageView.getUserData());
                        } else {
                            MainController.showTextDialog("\u8bf7\u5148\u9009\u5e84");
                        }
                        return;
                    }
                    this.napaisuanfa((Mj)imageView.getUserData());
                });
                this.main_panel.getChildren().add(imageView);
                mj.setWeina_imageView(imageView);
                if (mj.getSimo()) {
                    mj.getWeina_imageView().setVisible(false);
                    if (mj.getOther() != null) {
                        mj.getOther().setVisible(false);
                    }
                }
            });
            if (Config.systemConfig.sicengsandun == 1) {
                directions.forEach(d -> {
                    List<Mj> mjList = Config.weina.stream().filter(mj -> mj.getDirection() == d.getDirection()).collect(Collectors.toList());
                    Collections.sort(mjList, new ComparatorMj());
                    for (int i = 0; i < mjList.size(); ++i) {
                        Mj mj2 = (Mj)mjList.get(i);
                        if (i < 6 || i > mjList.size() - 7) {
                            mj2.getWeina_imageView().setStyle("-fx-opacity: 0.8");
                            continue;
                        }
                        mj2.getWeina_imageView().setOnMouseClicked(event -> {
                            if (zhuangjia < 0) {
                                MainController.showTextDialog("\u8bf7\u5148\u9009\u5e84");
                            } else {
                                MainController.showTextDialog("\u8bf7\u9009\u62e9\u5de6\u53f3\u4e09\u58a9\u62ff\u724c");
                            }
                        });
                    }
                });
            }
            return;
        }
        if (Config.systemConfig.mapaicengshu == 3) {
            double y = Config.MJ_HEIGHT + Config.MJ_HEIGHT_3_4 + Config.MJ_WIDTH * 2.0;
            double x = Config.SCREEN_WIDTH / 2.0 + Config.MJ_WIDTH_1_2 * (double)weina_top.size() / 3.0 / 2.0 - 5.0;
            double finalY = y;
            double finalX = x;
            yichu_pane.setMinWidth(Config.MJ_WIDTH_1_2 * (double)weina_top.size() / 3.0 - 40.0);
            yichu_pane.setMaxWidth(Config.MJ_WIDTH_1_2 * (double)weina_top.size() / 3.0 - 40.0);
            yichu_pane.setLayoutX(x - Config.MJ_WIDTH_1_2 * (double)weina_top.size() / 3.0 + 20.0);
            yichu_pane.setLayoutY(y + Config.MJ_HEIGHT * 2.0 + 10.0);
            this.main_panel.getChildren().add(yichu_pane);
            weina_top.forEach(mj -> {
                ImageView imageView = new ImageView();
                imageView.setUserData(mj);
                imageView.setImage(Config.getImageByNumber(mj.getResult()));
                imageView.setCursor(Cursor.HAND);
                imageView.setFitHeight(Config.MJ_HEIGHT_1_2);
                imageView.setFitWidth(Config.MJ_WIDTH_1_2);
                int yushu = mj.getMahjong_number() % 3;
                switch (yushu) {
                    case 0: {
                        imageView.setLayoutY(finalY);
                        imageView.setLayoutX(finalX - (double)(mj.getMahjong_number() / 3) * imageView.getFitWidth());
                        break;
                    }
                    case 1: {
                        imageView.setLayoutY(finalY + imageView.getFitHeight());
                        imageView.setLayoutX(finalX - (double)(mj.getMahjong_number() / 3) * imageView.getFitWidth());
                        break;
                    }
                    case 2: {
                        imageView.setLayoutY(finalY + imageView.getFitHeight() * 2.0);
                        imageView.setLayoutX(finalX - (double)(mj.getMahjong_number() / 3) * imageView.getFitWidth());
                        int dun = weina_top.size() % 3 == 0 ? weina_top.size() / 3 : weina_top.size() / 3 + 1;
                        Text text = new Text(dun - mj.getMahjong_number() / 3 + "");
                        text.setLayoutY(finalY + imageView.getFitHeight() * 3.0 + 10.0);
                        text.setLayoutX(finalX - (double)(mj.getMahjong_number() / 3) * imageView.getFitWidth() + imageView.getFitWidth() / 2.0 - 5.0);
                        text.setFill(Color.WHITE);
                        this.main_panel.getChildren().add(text);
                        mj.setOther(text);
                    }
                }
                imageView.setOnMouseClicked(event -> {
                    if (zhuangjia < 0) {
                        if (Config.systemConfig.xianmaimahoudingzhuang == 1) {
                            this.maima((Mj)imageView.getUserData());
                        } else {
                            MainController.showTextDialog("\u8bf7\u5148\u9009\u5e84");
                        }
                        return;
                    }
                    this.napaisuanfa((Mj)imageView.getUserData());
                });
                this.main_panel.getChildren().add(imageView);
                mj.setWeina_imageView(imageView);
            });
            x = Config.SCREEN_WIDTH / 2.0 + Config.MJ_WIDTH_1_2 * (double)(weina_bottom.size() / 3) / 2.0 + Config.MJ_WIDTH * 4.0 / 2.0;
            double finalY1 = y = Config.SCREEN_HEIGHT / 2.0 + Config.MJ_WIDTH_1_2 * (double)(weina_right.size() / 3) / 2.0 + Config.MJ_WIDTH_1_2;
            double finalX1 = x;
            weina_right.forEach(mj -> {
                ImageView imageView = new ImageView();
                imageView.setUserData(mj);
                imageView.setCursor(Cursor.HAND);
                Image image = Config.getImageByNumber(mj.getResult());
                imageView.setImage(image);
                imageView.setFitHeight(Config.MJ_HEIGHT_1_2);
                imageView.setFitWidth(Config.MJ_WIDTH_1_2);
                imageView.getTransforms().add(new Rotate(-90.0));
                int yushu = mj.getMahjong_number() % 3;
                switch (yushu) {
                    case 0: {
                        imageView.setLayoutY(finalY1 - (double)(mj.getMahjong_number() / 3) * imageView.getFitWidth());
                        imageView.setLayoutX(finalX1 + imageView.getFitHeight() * 2.0);
                        int dun = weina_right.size() % 3 == 0 ? weina_right.size() / 3 : weina_right.size() / 3 + 1;
                        Text text = new Text(dun - mj.getMahjong_number() / 3 + "");
                        text.setLayoutY(finalY1 - (double)(mj.getMahjong_number() / 3) * imageView.getFitWidth() - imageView.getFitWidth() / 2.0 + 5.0);
                        text.setLayoutX(finalX1 - 20.0);
                        text.setFill(Color.WHITE);
                        this.main_panel.getChildren().add(text);
                        mj.setOther(text);
                        break;
                    }
                    case 1: {
                        imageView.setLayoutY(finalY1 - (double)(mj.getMahjong_number() / 3) * imageView.getFitWidth());
                        imageView.setLayoutX(finalX1 + imageView.getFitHeight());
                        break;
                    }
                    case 2: {
                        imageView.setLayoutY(finalY1 - (double)(mj.getMahjong_number() / 3) * imageView.getFitWidth());
                        imageView.setLayoutX(finalX1);
                    }
                }
                imageView.setOnMouseClicked(event -> {
                    if (zhuangjia < 0) {
                        if (Config.systemConfig.xianmaimahoudingzhuang == 1) {
                            this.maima((Mj)imageView.getUserData());
                        } else {
                            MainController.showTextDialog("\u8bf7\u5148\u9009\u5e84");
                        }
                        return;
                    }
                    this.napaisuanfa((Mj)imageView.getUserData());
                });
                this.main_panel.getChildren().add(imageView);
                mj.setWeina_imageView(imageView);
            });
            y = Config.SCREEN_HEIGHT - Config.MJ_HEIGHT * 4.0 - 50.0;
            x = Config.SCREEN_WIDTH / 2.0 - Config.MJ_WIDTH_1_2 * (double)(weina_bottom.size() / 3) / 2.0 + Config.MJ_WIDTH_1_2;
            double finalY2 = y;
            double finalX2 = x;
            weina_bottom.forEach(mj -> {
                ImageView imageView = new ImageView();
                imageView.setUserData(mj);
                imageView.setCursor(Cursor.HAND);
                imageView.setImage(Config.getImageByNumber(mj.getResult()));
                imageView.setFitHeight(Config.MJ_HEIGHT_1_2);
                imageView.setFitWidth(Config.MJ_WIDTH_1_2);
                int yushu = mj.getMahjong_number() % 3;
                switch (yushu) {
                    case 0: {
                        imageView.setLayoutY(finalY2 + imageView.getFitHeight() * 2.0);
                        imageView.setLayoutX(finalX2 + (double)(mj.getMahjong_number() / 3) * imageView.getFitWidth());
                        int dun = weina_bottom.size() % 3 == 0 ? weina_bottom.size() / 3 : weina_bottom.size() / 3 + 1;
                        Text text = new Text(dun - mj.getMahjong_number() / 3 + "");
                        text.setLayoutY(finalY2 - 5.0);
                        text.setLayoutX(finalX2 + (double)(mj.getMahjong_number() / 3) * imageView.getFitWidth() + imageView.getFitWidth() / 2.0 - 5.0);
                        text.setFill(Color.WHITE);
                        this.main_panel.getChildren().add(text);
                        mj.setOther(text);
                        break;
                    }
                    case 1: {
                        imageView.setLayoutY(finalY2 + imageView.getFitHeight());
                        imageView.setLayoutX(finalX2 + (double)(mj.getMahjong_number() / 3) * imageView.getFitWidth());
                        break;
                    }
                    case 2: {
                        imageView.setLayoutY(finalY2);
                        imageView.setLayoutX(finalX2 + (double)(mj.getMahjong_number() / 3) * imageView.getFitWidth());
                    }
                }
                imageView.setOnMouseClicked(event -> {
                    if (zhuangjia < 0) {
                        if (Config.systemConfig.xianmaimahoudingzhuang == 1) {
                            this.maima((Mj)imageView.getUserData());
                        } else {
                            MainController.showTextDialog("\u8bf7\u5148\u9009\u5e84");
                        }
                        return;
                    }
                    this.napaisuanfa((Mj)imageView.getUserData());
                });
                this.main_panel.getChildren().add(imageView);
                mj.setWeina_imageView(imageView);
            });
            x = Config.SCREEN_WIDTH / 2.0 - Config.MJ_WIDTH_1_2 * (double)(weina_bottom.size() / 3) / 2.0 - Config.MJ_HEIGHT * 2.0;
            double finalY3 = y = Config.SCREEN_HEIGHT / 2.0 - Config.MJ_WIDTH_1_2 * (double)(weina_left.size() / 3) / 2.0 + Config.MJ_WIDTH_1_2;
            double finalX3 = x;
            if (weina_left.size() % 3 != 0) {
                weina_left.forEach(mj -> {
                    ImageView imageView = new ImageView();
                    imageView.setUserData(mj);
                    imageView.setCursor(Cursor.HAND);
                    Image image = Config.getImageByNumber(mj.getResult());
                    imageView.setImage(image);
                    imageView.setFitHeight(Config.MJ_HEIGHT_1_2);
                    imageView.setFitWidth(Config.MJ_WIDTH_1_2);
                    imageView.getTransforms().add(new Rotate(90.0));
                    int majiang_number = mj.getMahjong_number();
                    int yushu = majiang_number % 3;
                    if (weina_left.size() % 3 != 0 && majiang_number != 0) {
                        majiang_number += 2;
                    }
                    switch (yushu) {
                        case 0: {
                            imageView.setLayoutY(finalY3 + (double)(majiang_number / 3) * imageView.getFitWidth());
                            imageView.setLayoutX(finalX3 + imageView.getFitHeight() * 2.0);
                            break;
                        }
                        case 1: {
                            imageView.setLayoutY(finalY3 + (double)(majiang_number / 3) * imageView.getFitWidth());
                            imageView.setLayoutX(finalX3);
                            break;
                        }
                        case 2: {
                            imageView.setLayoutY(finalY3 + (double)(majiang_number / 3) * imageView.getFitWidth());
                            imageView.setLayoutX(finalX3 + imageView.getFitHeight());
                            int dun = weina_left.size() % 3 == 0 ? weina_left.size() / 3 : weina_left.size() / 3 + 1;
                            Text text = new Text(dun - majiang_number / 3 + "");
                            text.setLayoutY(finalY3 + (double)(majiang_number / 3) * imageView.getFitWidth() + imageView.getFitWidth() / 2.0 + 5.0);
                            text.setLayoutX(finalX3 + imageView.getFitHeight() * 2.0 + 5.0);
                            text.setFill(Color.WHITE);
                            this.main_panel.getChildren().add(text);
                            mj.setOther(text);
                        }
                    }
                    imageView.setOnMouseClicked(event -> {
                        if (zhuangjia < 0) {
                            if (Config.systemConfig.xianmaimahoudingzhuang == 1) {
                                this.maima((Mj)imageView.getUserData());
                            } else {
                                MainController.showTextDialog("\u8bf7\u5148\u9009\u5e84");
                            }
                            return;
                        }
                        this.napaisuanfa((Mj)imageView.getUserData());
                    });
                    this.main_panel.getChildren().add(imageView);
                    mj.setWeina_imageView(imageView);
                });
            } else {
                weina_left.forEach(mj -> {
                    ImageView imageView = new ImageView();
                    imageView.setUserData(mj);
                    imageView.setCursor(Cursor.HAND);
                    Image image = Config.getImageByNumber(mj.getResult());
                    imageView.setImage(image);
                    imageView.setFitHeight(Config.MJ_HEIGHT_1_2);
                    imageView.setFitWidth(Config.MJ_WIDTH_1_2);
                    imageView.getTransforms().add(new Rotate(90.0));
                    int majiang_number = mj.getMahjong_number();
                    int yushu = majiang_number % 3;
                    switch (yushu) {
                        case 0: {
                            imageView.setLayoutY(finalY3 + (double)(majiang_number / 3) * imageView.getFitWidth());
                            imageView.setLayoutX(finalX3);
                            break;
                        }
                        case 1: {
                            imageView.setLayoutY(finalY3 + (double)(majiang_number / 3) * imageView.getFitWidth());
                            imageView.setLayoutX(finalX3 + imageView.getFitHeight());
                            break;
                        }
                        case 2: {
                            imageView.setLayoutY(finalY3 + (double)(majiang_number / 3) * imageView.getFitWidth());
                            imageView.setLayoutX(finalX3 + imageView.getFitHeight() * 2.0);
                            int dun = weina_left.size() % 3 == 0 ? weina_left.size() / 3 : weina_left.size() / 3 + 1;
                            Text text = new Text(dun - majiang_number / 3 + "");
                            text.setLayoutY(finalY3 + (double)(majiang_number / 3) * imageView.getFitWidth() + imageView.getFitWidth() / 2.0 + 5.0);
                            text.setLayoutX(finalX3 + imageView.getFitHeight() * 2.0 + 5.0);
                            text.setFill(Color.WHITE);
                            this.main_panel.getChildren().add(text);
                            mj.setOther(text);
                        }
                    }
                    imageView.setOnMouseClicked(event -> {
                        if (zhuangjia < 0) {
                            if (Config.systemConfig.xianmaimahoudingzhuang == 1) {
                                this.maima((Mj)imageView.getUserData());
                            } else {
                                MainController.showTextDialog("\u8bf7\u5148\u9009\u5e84");
                            }
                            return;
                        }
                        this.napaisuanfa((Mj)imageView.getUserData());
                    });
                    this.main_panel.getChildren().add(imageView);
                    mj.setWeina_imageView(imageView);
                });
            }
            return;
        }
    }

    public static List<Mj> hupaiyuce(List<Mj> yuan, int que) {
        List<Mj> hupaid = new LinkedList<Mj>();
        List<Mj> buhud = new LinkedList();
        List<Mj> daijiance = new LinkedList<Mj>();
        daijiance.addAll(Config.weina);
        daijiance.forEach(mj -> {
            List<Mj> jianc = new LinkedList<Mj>();
            jianc.addAll(yuan);
            Mj mj2 = (Mj)mj.clone();
            mj2.setNapai_direction(-1);
            mj2.setPaizhuantai(0);
            mj2.setChupai_direction(-1);
            mj2.setIsque(false);
            jianc.add(mj2);
            if (hupaid.stream().filter(mj1 -> mj1.getZhiBiaoZHi().equals(mj.getZhiBiaoZHi())).count() <= 0L) {
                if (buhud.stream().filter(mj1 -> mj1.getZhiBiaoZHi().equals(mj.getZhiBiaoZHi())).count() > 0L) {
                    buhud.add(mj);
                } else {
                    Collections.sort(jianc, new ComparatorMjResult());
                    if (HuPaiKit.HuPaiPanDin(jianc, que)) {
                        Mj mj12 = (Mj)mj.clone();
                        hupaid.add(mj12);
                    } else {
                        buhud.add(mj);
                    }
                }
            }
        });
        return hupaid;
    }

    public static boolean hupaiyuce(List<Mj> yuan) {
        List<Mj> buhud = new LinkedList<Mj>();
        List<Mj> daijiance = new LinkedList<Mj>();
        daijiance.addAll(Config.weina);
        for (Mj mj : daijiance) {
            List<Mj> jianc = new LinkedList<Mj>();
            jianc.addAll(yuan);
            Mj mj2 = (Mj)mj.clone();
            mj2.setNapai_direction(-1);
            mj2.setPaizhuantai(0);
            mj2.setChupai_direction(-1);
            mj2.setIsque(false);
            jianc.add(mj2);
            if (buhud.stream().filter(mj1 -> mj1.getZhiBiaoZHi().equals(mj.getZhiBiaoZHi())).count() > 0L) {
                buhud.add(mj);
                continue;
            }
            Collections.sort(jianc, new ComparatorMjResult());
            if (HuPaiKit.HuPaiPanDin(jianc, -1)) {
                return true;
            }
            buhud.add(mj);
        }
        return false;
    }

    public static void saveState() {
        Map<String, Object> stringObjectMap = new HashMap<String, Object>(16);
        stringObjectMap.put("\u6253\u724c\u65b9\u5411", Integer.valueOf(dapaifangx));
        directions.forEach(direction -> {
            stringObjectMap.put("que_" + direction.getDirection(), (Serializable)direction.getQue());
            stringObjectMap.put("hu_" + direction.getDirection(), (Serializable)direction.getHu());
            stringObjectMap.put("kehu_" + direction.getDirection(), (Serializable)direction.isKehu());
        });
        java.util.Set<Mj> config_weina = new HashSet();
        Config.weina.forEach(mj1 -> {
            Mj mj2 = (Mj)mj1.clone();
            config_weina.add(mj2);
        });
        stringObjectMap.put("config_weina", config_weina);
        List<Mj> yina_list = new LinkedList();
        Config.yina.forEach(mj1 -> {
            Mj mj2 = (Mj)mj1.clone();
            yina_list.add(mj2);
        });
        stringObjectMap.put("yina", yina_list);
        List<Mj> yuce_list = new LinkedList();
        Config.yuce.forEach(mj1 -> {
            Mj mj2 = (Mj)mj1.clone();
            yuce_list.add(mj2);
        });
        stringObjectMap.put("yuce", yuce_list);
        List<Mj> yichu_list = new LinkedList();
        Config.yichu.forEach(mj1 -> {
            Mj mj2 = (Mj)mj1.clone();
            yichu_list.add(mj2);
        });
        stringObjectMap.put("yichu", yichu_list);
        List<Mj> weina_list = new LinkedList();
        weina.forEach(mj1 -> {
            Mj mj2 = (Mj)mj1.clone();
            weina_list.add(mj2);
        });
        stringObjectMap.put("weina", weina_list);
        List<Mj> huanhoupai_yixuan_list = new LinkedList();
        Config.huanhoupai_yixuan.forEach(mj1 -> {
            Mj mj2 = (Mj)mj1.clone();
            huanhoupai_yixuan_list.add(mj2);
        });
        stringObjectMap.put("huanhoupai_yixuan", weina_list);
        state.add(stringObjectMap);
    }

    private static void chupai(Mj mj) {
        Robot robot;
        if (mj.getNapai_direction() != dapaifangx) {
            return;
        }
        MainController.saveState();
        Config.yina.forEach(mj2 -> mj2.setLast(false));
        if (Config.systemConfig.queyimeng == 1 && Config.systemConfig.zixuankoupai == 1) {
            if (directions.get(dapaifangx).getQue() < 0) {
                directions.get(dapaifangx).setQue(mj.getResult() / 10);
                directions.get(dapaifangx).setDiyizhangda_mj((Mj)mj.clone());
            } else if (directions.get(dapaifangx).getDiyizhangda_mj() == null && mj.getResult() / 10 == directions.get(dapaifangx).getQue()) {
                directions.get(dapaifangx).setDiyizhangda_mj((Mj)mj.clone());
            }
        }
        mj.setChupai_direction(dapaifangx);
        kech_list_panel.getChildren().clear();
        boolean kechi = false;
        if (Config.systemConfig.chipai == 1) {
            if (Config.systemConfig.chisanfangpai == 1) {
                int next_d = dapaifangx;
                do {
                    if (Config.systemConfig.niezhuanimo == 1) {
                        if (--next_d >= 0) continue;
                        next_d = 3;
                        continue;
                    }
                    if (++next_d <= 3) continue;
                    next_d = 0;
                } while (directions.get(next_d).isQueren());
                for (MjPanelInterface dc1 : directions) {
                    List<Mj> mjs3;
                    List<Mj> list;
                    List<Mj> chi_set;
                    int finalD;
                    boolean kechi_temp = false;
                    List<List<Mj>> chi_list = new LinkedList();
                    int n = finalD = dc1.getDirection();
                    if (finalD == dapaifangx || directions.get(finalD).isQueren() || directions.get(finalD).getHu() != 0 || directions.get(finalD).getQue() == mj.getResult() / 10 || next_d != finalD && !MainController.ChiPaiKeTing(finalD, mj)) continue;
                    List<Mj> caishengs = Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == finalD && mj1.caisheng && mj1.getPaizhuantai() == 0).collect(Collectors.toList());
                    List<Mj> mjs = Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == finalD && mj1.getPaizhuantai() == 0 && mj1.getResult() < 30 && mj.getResult() + 1 == mj1.getResult() && mj.getResult() / 10 == mj1.getResult() / 10 && (!mj1.caisheng || Config.systemConfig.caishenganzhengchangpaichanyuchangpanggang == 1)).collect(Collectors.toList());
                    if (mjs.size() > 0) {
                        List<Mj> mjs22;
                        List<Mj> list2;
                        if (Config.systemConfig.caishengcanyuchipanggang == 1 && caishengs.size() > 0 && !((Mj)mjs.get((int)0)).caisheng) {
                            LinkedList linkedList = new LinkedList();
                            linkedList.add(mjs.get(0));
                            linkedList.add(caishengs.get(0));
                            chi_list.add(linkedList);
                            kechi_temp = true;
                        }
                        if ((list2 = Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == finalD && mj1.getPaizhuantai() == 0 && mj1.getResult() < 30 && mj.getResult() + 2 == mj1.getResult() && mj.getResult() / 10 == mj1.getResult() / 10 && (!mj1.caisheng || Config.systemConfig.caishenganzhengchangpaichanyuchangpanggang == 1)).collect(Collectors.toList())).size() > 0) {
                            LinkedList chi_set2 = new LinkedList();
                            chi_set2.add(mjs.get(0));
                            chi_set2.add(list2.get(0));
                            chi_list.add(chi_set2);
                            kechi_temp = true;
                        }
                        if ((mjs22 = Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == finalD && mj1.getPaizhuantai() == 0 && mj1.getResult() < 30 && mj.getResult() - 1 == mj1.getResult() && mj.getResult() / 10 == mj1.getResult() / 10 && (!mj1.caisheng || Config.systemConfig.caishenganzhengchangpaichanyuchangpanggang == 1)).collect(Collectors.toList())).size() > 0) {
                            chi_set = new LinkedList();
                            chi_set.add(mjs.get(0));
                            chi_set.add(mjs22.get(0));
                            chi_list.add(chi_set);
                            kechi_temp = true;
                        }
                    }
                    if ((list = Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == finalD && mj1.getPaizhuantai() == 0 && mj1.getResult() < 30 && mj.getResult() - 1 == mj1.getResult() && mj.getResult() / 10 == mj1.getResult() / 10 && (!mj1.caisheng || Config.systemConfig.caishenganzhengchangpaichanyuchangpanggang == 1)).collect(Collectors.toList())).size() > 0) {
                        if (Config.systemConfig.caishengcanyuchipanggang == 1 && caishengs.size() > 0 && !((Mj)list.get((int)0)).caisheng) {
                            LinkedList chi_set2 = new LinkedList();
                            chi_set2.add(list.get(0));
                            chi_set2.add(caishengs.get(0));
                            chi_list.add(chi_set2);
                            kechi_temp = true;
                        }
                        if ((mjs3 = Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == finalD && mj1.getPaizhuantai() == 0 && mj1.getResult() < 30 && mj.getResult() - 2 == mj1.getResult() && mj.getResult() / 10 == mj1.getResult() / 10 && (!mj1.caisheng || Config.systemConfig.caishenganzhengchangpaichanyuchangpanggang == 1)).collect(Collectors.toList())).size() > 0) {
                            chi_set = new LinkedList();
                            chi_set.add(mjs3.get(0));
                            chi_set.add(list.get(0));
                            chi_list.add(chi_set);
                            kechi_temp = true;
                        }
                    }
                    if (Config.systemConfig.caishengcanyuchipanggang == 1 && caishengs.size() > 0) {
                        List<Mj> mjs4;
                        mjs3 = Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == finalD && mj1.getPaizhuantai() == 0 && mj1.getResult() < 30 && mj.getResult() - 2 == mj1.getResult() && !mj1.caisheng && mj.getResult() / 10 == mj1.getResult() / 10).collect(Collectors.toList());
                        if (mjs3.size() > 0) {
                            chi_set = new LinkedList();
                            chi_set.add(mjs3.get(0));
                            chi_set.add(caishengs.get(0));
                            chi_list.add(chi_set);
                            kechi_temp = true;
                        }
                        if ((mjs4 = Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == finalD && mj1.getPaizhuantai() == 0 && mj1.getResult() < 30 && mj.getResult() + 2 == mj1.getResult() && !mj1.caisheng && mj.getResult() / 10 == mj1.getResult() / 10).collect(Collectors.toList())).size() > 0) {
                            LinkedList linkedList = new LinkedList();
                            linkedList.add(mjs4.get(0));
                            linkedList.add(caishengs.get(0));
                            chi_list.add(linkedList);
                            kechi_temp = true;
                        }
                    }
                    if (!kechi_temp) continue;
                    kechi = kechi_temp;
                    MainController.yichu(mj);
                    int finalD1 = n;
                    for (List<Mj> mjs1 : chi_list) {
                        mjs1.sort(new ComparatorMjResult());
                        for (Mj mj12 : mjs1) {
                            mj12.getYina_imageView().addClass(CHI_BORDER);
                            mj12.getYina_imageView().getImageView().setOnMouseClicked(event -> {
                                dapaifangx = directions.get(finalD1).getDirection();
                                mj.setNapai_direction(dapaifangx);
                                mj.setPaizhuantai(4);
                                mjs1.sort(new ComparatorMjResult());
                                int i = 0;
                                for (Mj mj2 : mjs1) {
                                    mj2.setPaizhuantai(4);
                                    if (mj2.caisheng) {
                                        switch (i) {
                                            case 0: {
                                                if (mj.getResult() - ((Mj)mjs1.get(1)).getResult() == 1) {
                                                    mj2.caisheng_dipai = ((Mj)mjs1.get(1)).getResult() - 1;
                                                    break;
                                                }
                                                mj2.caisheng_dipai = ((Mj)mjs1.get(1)).getResult() + 1;
                                                break;
                                            }
                                            case 1: {
                                                mj2.caisheng_dipai = mj.getResult() - ((Mj)mjs1.get(0)).getResult() == 1 ? ((Mj)mjs1.get(0)).getResult() - 1 : ((Mj)mjs1.get(0)).getResult() + 1;
                                            }
                                        }
                                    }
                                    ++i;
                                }
                                Config.yichu.remove(mj);
                                yichu_pane.getChildren().remove(mj.getYichu_imageView());
                                Config.yina.add(mj);
                                Config.yuce.clear();
                                directions.forEach(direction -> direction.setKehu(false));
                                MainController.yucepaiAfterMopai(0, dapaifangx, 1);
                                MainController.clear();
                                MainController.displayYinaPai();
                            });
                        }
                    }
                }
                if (kechi) {
                    directions.get(next_d).setMo(1);
                    directions.get(next_d).getCaozuoPanel().getChildren().add(directions.get(next_d).getMo_text());
                    directions.get(next_d).getMo_text().setUserData(directions.get(next_d).getDirection());
                    if (directions.get(next_d).getMo() == 1) {
                        static_main_panel.layout();
                        try {
                            Robot robot2 = new Robot();
                            robot2.mouseMove((int)(JFXKit.getScreenX(directions.get(next_d).getMo_text()) + directions.get(next_d).getMo_text().getBoundsInParent().getWidth() / 2.0), (int)(JFXKit.getScreenY(directions.get(next_d).getMo_text()) + directions.get(next_d).getMo_text().getBoundsInParent().getHeight() / 2.0));
                        }
                        catch (AWTException e) {
                            e.printStackTrace();
                        }
                        int next_d_fibal = next_d;
                        directions.get(next_d).getMo_text().setOnMouseClicked(event -> {
                            dapaifangx = directions.get(next_d_fibal).getDirection();
                            directions.forEach(direction1 -> {
                                direction1.setKehu(false);
                                direction1.setMo(0);
                            });
                            MainController.mopai();
                        });
                    }
                }
            } else {
                List<List<Mj>> chi_list = new LinkedList();
                int d = dapaifangx;
                do {
                    if (Config.systemConfig.niezhuanimo == 1) {
                        if (--d >= 0) continue;
                        d = 3;
                        continue;
                    }
                    if (++d <= 3) continue;
                    d = 0;
                } while (directions.get(d).isQueren());
                int finalD = d;
                if (directions.get(d).getHu() == 0 && directions.get(d).getQue() != mj.getResult() / 10) {
                    List<Mj> mjs2;
                    List<Mj> chi_set;
                    List<Mj> caishengs = Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == finalD && mj1.caisheng && mj1.getPaizhuantai() == 0).collect(Collectors.toList());
                    List<Mj> mjs = Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == finalD && mj1.getPaizhuantai() == 0 && mj1.getResult() < 30 && mj.getResult() + 1 == mj1.getResult() && mj.getResult() / 10 == mj1.getResult() / 10 && (!mj1.caisheng || Config.systemConfig.caishenganzhengchangpaichanyuchangpanggang == 1)).collect(Collectors.toList());
                    if (mjs.size() > 0) {
                        List<Mj> list;
                        List<Mj> mjs1;
                        if (Config.systemConfig.caishengcanyuchipanggang == 1 && caishengs.size() > 0 && !((Mj)mjs.get((int)0)).caisheng) {
                            LinkedList chi_set6 = new LinkedList();
                            chi_set6.add(mjs.get(0));
                            chi_set6.add(caishengs.get(0));
                            chi_list.add(chi_set6);
                            kechi = true;
                        }
                        if ((mjs1 = Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == finalD && mj1.getPaizhuantai() == 0 && mj1.getResult() < 30 && mj.getResult() + 2 == mj1.getResult() && mj.getResult() / 10 == mj1.getResult() / 10 && (!mj1.caisheng || Config.systemConfig.caishenganzhengchangpaichanyuchangpanggang == 1)).collect(Collectors.toList())).size() > 0) {
                            LinkedList linkedList = new LinkedList();
                            linkedList.add(mjs.get(0));
                            linkedList.add(mjs1.get(0));
                            chi_list.add(linkedList);
                            kechi = true;
                        }
                        if ((list = Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == finalD && mj1.getPaizhuantai() == 0 && mj1.getResult() < 30 && mj.getResult() - 1 == mj1.getResult() && mj.getResult() / 10 == mj1.getResult() / 10 && (!mj1.caisheng || Config.systemConfig.caishenganzhengchangpaichanyuchangpanggang == 1)).collect(Collectors.toList())).size() > 0) {
                            chi_set = new LinkedList();
                            chi_set.add(mjs.get(0));
                            chi_set.add(list.get(0));
                            chi_list.add(chi_set);
                            kechi = true;
                        }
                    }
                    if ((mjs2 = Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == finalD && mj1.getPaizhuantai() == 0 && mj1.getResult() < 30 && mj.getResult() - 1 == mj1.getResult() && mj.getResult() / 10 == mj1.getResult() / 10 && (!mj1.caisheng || Config.systemConfig.caishenganzhengchangpaichanyuchangpanggang == 1)).collect(Collectors.toList())).size() > 0) {
                        List<Mj> list;
                        if (Config.systemConfig.caishengcanyuchipanggang == 1 && caishengs.size() > 0 && !((Mj)mjs2.get((int)0)).caisheng) {
                            LinkedList linkedList = new LinkedList();
                            linkedList.add(mjs2.get(0));
                            linkedList.add(caishengs.get(0));
                            chi_list.add(linkedList);
                            kechi = true;
                        }
                        if ((list = Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == finalD && mj1.getPaizhuantai() == 0 && mj1.getResult() < 30 && mj.getResult() - 2 == mj1.getResult() && mj.getResult() / 10 == mj1.getResult() / 10 && (!mj1.caisheng || Config.systemConfig.caishenganzhengchangpaichanyuchangpanggang == 1)).collect(Collectors.toList())).size() > 0) {
                            chi_set = new LinkedList();
                            chi_set.add(list.get(0));
                            chi_set.add(mjs2.get(0));
                            chi_list.add(chi_set);
                            kechi = true;
                        }
                    }
                    if (Config.systemConfig.caishengcanyuchipanggang == 1 && caishengs.size() > 0) {
                        List<Mj> mjs4;
                        List<Mj> list = Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == finalD && mj1.getPaizhuantai() == 0 && mj1.getResult() < 30 && mj.getResult() - 2 == mj1.getResult() && !mj1.caisheng && mj.getResult() / 10 == mj1.getResult() / 10).collect(Collectors.toList());
                        if (list.size() > 0) {
                            chi_set = new LinkedList();
                            chi_set.add(list.get(0));
                            chi_set.add(caishengs.get(0));
                            chi_list.add(chi_set);
                            kechi = true;
                        }
                        if ((mjs4 = Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == finalD && mj1.getPaizhuantai() == 0 && mj1.getResult() < 30 && mj.getResult() + 2 == mj1.getResult() && !mj1.caisheng && mj.getResult() / 10 == mj1.getResult() / 10).collect(Collectors.toList())).size() > 0) {
                            LinkedList chi_set7 = new LinkedList();
                            chi_set7.add(mjs4.get(0));
                            chi_set7.add(caishengs.get(0));
                            chi_list.add(chi_set7);
                            kechi = true;
                        }
                    }
                    if (kechi) {
                        MainController.yichu(mj);
                        directions.get(d).setMo(1);
                        directions.get(d).getCaozuoPanel().getChildren().add(directions.get(d).getMo_text());
                        directions.get(d).getMo_text().setUserData(directions.get(d).getDirection());
                        int finalD1 = d;
                        Label label = new Label("\u53ef\u5403");
                        label.setFont(Font.font("\u5b8b\u4f53", FontWeight.BOLD, 20.0));
                        label.setStyle("-fx-background-color: white;-fx-text-fill: red");
                        kech_list_panel.getChildren().add(label);
                        kech_list_panel.setStyle("-fx-background-color: white;-fx-padding: 10px;");
                        kech_list_panel.setAlignment(Pos.CENTER);
                        if (d == bottomPanel.getDirection()) {
                            kech_list_panel.setLayoutY(JFXKit.getScreenY(MainController.bottomPanel.caozuoPanel) - (double)chi_list.size() * Config.MJ_HEIGHT - 20.0 - MainController.bottomPanel.caozuoPanel.getBoundsInParent().getHeight());
                            kech_list_panel.setLayoutX(bottomPanel.getLayoutX() + bottomPanel.getYinaPanel().getWidth() / 2.0);
                        }
                        if (d == rightPanel.getDirection()) {
                            kech_list_panel.setLayoutX(rightPanel.getLayoutX() - 2.0 * Config.MJ_HEIGHT_3_4);
                            kech_list_panel.setLayoutY(JFXKit.getScreenY(MainController.rightPanel.caozuoPanel) + MainController.rightPanel.caozuoPanel.getBoundsInParent().getHeight() + 20.0);
                        }
                        if (d == topPanel.getDirection()) {
                            kech_list_panel.setLayoutY(JFXKit.getScreenY(MainController.topPanel.caozuoPanel) + MainController.topPanel.caozuoPanel.getBoundsInParent().getHeight() + 20.0);
                            kech_list_panel.setLayoutX(topPanel.getLayoutX() + topPanel.getYinaPanel().getWidth() / 2.0);
                        }
                        if (d == leftPanel.getDirection()) {
                            kech_list_panel.setLayoutX(leftPanel.getLayoutX() + Config.MJ_HEIGHT + Config.MJ_HEIGHT_3_4 + 20.0);
                            kech_list_panel.setLayoutY(JFXKit.getScreenY(MainController.leftPanel.caozuoPanel) + MainController.leftPanel.caozuoPanel.getBoundsInParent().getHeight() + 20.0);
                        }
                        if (!static_main_panel.getChildren().contains(kech_list_panel)) {
                            static_main_panel.getChildren().add(kech_list_panel);
                        }
                        kech_list_panel.setVisible(true);
                        for (List<Mj> mjs1 : chi_list) {
                            mjs1.sort(new ComparatorMjResult());
                            HBox hBox = new HBox();
                            hBox.setAlignment(Pos.CENTER);
                            hBox.setCursor(Cursor.HAND);
                            for (Mj mj3 : mjs1) {
                                mj3.getYina_imageView().addClass(CHI_BORDER);
                                mj3.getYina_imageView().getImageView().setOnMouseClicked(event -> hBox.fireEvent(event));
                                ImageView imageView = new ImageView();
                                if (mj3.caisheng) {
                                    imageView.setImage(ImageKit.coverImage(Config.getImageByMj(mj3), Config.LANSE));
                                } else {
                                    imageView.setImage(Config.getImageByMj(mj3));
                                }
                                imageView.setFitWidth(Config.MJ_WIDTH_3_4);
                                imageView.setFitHeight(Config.MJ_HEIGHT_3_4);
                                hBox.getChildren().add(imageView);
                                hBox.setOnMouseClicked(event -> {
                                    dapaifangx = directions.get(finalD1).getDirection();
                                    mj.setNapai_direction(dapaifangx);
                                    mj.setPaizhuantai(4);
                                    mjs1.sort(new ComparatorMjResult());
                                    int i = 0;
                                    for (Mj mj2 : mjs1) {
                                        mj2.setPaizhuantai(4);
                                        if (mj2.caisheng) {
                                            switch (i) {
                                                case 0: {
                                                    if (mj.getResult() - ((Mj)mjs1.get(1)).getResult() == 1) {
                                                        mj2.caisheng_dipai = ((Mj)mjs1.get(1)).getResult() - 1;
                                                        break;
                                                    }
                                                    mj2.caisheng_dipai = ((Mj)mjs1.get(1)).getResult() + 1;
                                                    break;
                                                }
                                                case 1: {
                                                    mj2.caisheng_dipai = mj.getResult() - ((Mj)mjs1.get(0)).getResult() == 1 ? ((Mj)mjs1.get(0)).getResult() - 1 : ((Mj)mjs1.get(0)).getResult() + 1;
                                                }
                                            }
                                        }
                                        ++i;
                                    }
                                    Config.yichu.remove(mj);
                                    yichu_pane.getChildren().remove(mj.getYichu_imageView());
                                    Config.yina.add(mj);
                                    Config.yuce.clear();
                                    directions.forEach(direction -> direction.setKehu(false));
                                    MainController.yucepaiAfterMopai(0, dapaifangx, 1);
                                    MainController.clear();
                                    MainController.displayYinaPai();
                                });
                            }
                            kech_list_panel.getChildren().add(hBox);
                        }
                        if (directions.get(d).getMo() == 1) {
                            static_main_panel.layout();
                            try {
                                robot = new Robot();
                                robot.mouseMove((int)(JFXKit.getScreenX(directions.get(d).getMo_text()) + directions.get(d).getMo_text().getBoundsInParent().getWidth() / 2.0), (int)(JFXKit.getScreenY(directions.get(d).getMo_text()) + directions.get(d).getMo_text().getBoundsInParent().getHeight() / 2.0));
                            }
                            catch (AWTException e) {
                                e.printStackTrace();
                            }
                            directions.get(d).getMo_text().setOnMouseClicked(event -> {
                                dapaifangx = directions.get(finalD1).getDirection();
                                directions.forEach(direction1 -> {
                                    direction1.setKehu(false);
                                    direction1.setMo(0);
                                });
                                MainController.mopai();
                            });
                        }
                    }
                }
            }
        }
        if (Config.systemConfig.dongnanxibeikechikegang == 1 && mj.getResult() >= 31 && mj.getResult() <= 37) {
            List<List<Mj>> chi_list = new LinkedList();
            int d = dapaifangx;
            do {
                if (Config.systemConfig.niezhuanimo == 1) {
                    if (--d >= 0) continue;
                    d = 3;
                    continue;
                }
                if (++d <= 3) continue;
                d = 0;
            } while (directions.get(d).isQueren());
            int finalD = d;
            if (directions.get(d).getHu() == 0 && directions.get(d).getQue() != mj.getResult() / 10) {
                if (mj.getResult() >= 31 && mj.getResult() <= 34) {
                    Map<Integer, Integer> zhongfabaigang_map = new HashMap();
                    Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == finalD && mj1.getPaizhuantai() == 0 && mj1.getResult() >= 31 && mj1.getResult() <= 34 && mj1.getResult() != mj.getResult()).collect(Collectors.toList()).forEach(mj1 -> {
                        if (zhongfabaigang_map.containsKey(mj1.getResult())) {
                            zhongfabaigang_map.put(mj1.getResult(), (Integer)zhongfabaigang_map.get(mj1.getResult()) + 1);
                        } else {
                            zhongfabaigang_map.put(mj1.getResult(), 1);
                        }
                    });
                    if (zhongfabaigang_map.keySet().size() >= 2) {
                        kechi = true;
                        Integer[] s = new Integer[zhongfabaigang_map.keySet().size()];
                        zhongfabaigang_map.keySet().toArray(s);
                        int i = 0;
                        while (i < s.length - 1) {
                            LinkedList linkedList = new LinkedList();
                            int finali = i++;
                            linkedList.add(Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == finalD && mj1.getPaizhuantai() == 0 && mj1.getResult() == s[finali].intValue()).collect(Collectors.toList()).get(0));
                            linkedList.add(Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == finalD && mj1.getPaizhuantai() == 0 && mj1.getResult() == s[finali + 1].intValue()).collect(Collectors.toList()).get(0));
                            chi_list.add(linkedList);
                            kechi = true;
                        }
                    }
                }
                if (mj.getResult() >= 35 && mj.getResult() <= 37) {
                    Map<Integer, Integer> zhongfabaigang_map = new HashMap();
                    Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == finalD && mj1.getPaizhuantai() == 0 && mj1.getResult() >= 35 && mj1.getResult() <= 37 && mj1.getResult() != mj.getResult()).collect(Collectors.toList()).forEach(mj1 -> {
                        if (zhongfabaigang_map.containsKey(mj1.getResult())) {
                            zhongfabaigang_map.put(mj1.getResult(), (Integer)zhongfabaigang_map.get(mj1.getResult()) + 1);
                        } else {
                            zhongfabaigang_map.put(mj1.getResult(), 1);
                        }
                    });
                    if (zhongfabaigang_map.keySet().size() >= 2) {
                        kechi = true;
                        Integer[] s = new Integer[zhongfabaigang_map.keySet().size()];
                        zhongfabaigang_map.keySet().toArray(s);
                        int i = 0;
                        while (i < s.length - 1) {
                            LinkedList linkedList = new LinkedList();
                            int finali = i++;
                            linkedList.add(Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == finalD && mj1.getPaizhuantai() == 0 && mj1.getResult() == s[finali].intValue()).collect(Collectors.toList()).get(0));
                            linkedList.add(Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == finalD && mj1.getPaizhuantai() == 0 && mj1.getResult() == s[finali + 1].intValue()).collect(Collectors.toList()).get(0));
                            chi_list.add(linkedList);
                            kechi = true;
                        }
                    }
                }
                if (kechi) {
                    MainController.yichu(mj);
                    directions.get(d).setMo(1);
                    directions.get(d).getCaozuoPanel().getChildren().add(directions.get(d).getMo_text());
                    directions.get(d).getMo_text().setUserData(directions.get(d).getDirection());
                    int finalD1 = d;
                    Label label = new Label("\u53ef\u5403");
                    label.setFont(Font.font("\u5b8b\u4f53", FontWeight.BOLD, 20.0));
                    label.setStyle("-fx-background-color: white;-fx-text-fill: red");
                    kech_list_panel.getChildren().add(label);
                    kech_list_panel.setStyle("-fx-background-color: white;-fx-padding: 10px;");
                    kech_list_panel.setAlignment(Pos.CENTER);
                    if (d == bottomPanel.getDirection()) {
                        kech_list_panel.setLayoutY(JFXKit.getScreenY(MainController.bottomPanel.caozuoPanel) - (double)chi_list.size() * Config.MJ_HEIGHT - 20.0 - MainController.bottomPanel.caozuoPanel.getBoundsInParent().getHeight());
                        kech_list_panel.setLayoutX(bottomPanel.getLayoutX() + bottomPanel.getYinaPanel().getWidth() / 2.0);
                    }
                    if (d == rightPanel.getDirection()) {
                        kech_list_panel.setLayoutX(rightPanel.getLayoutX() - 2.0 * Config.MJ_HEIGHT_3_4);
                        kech_list_panel.setLayoutY(JFXKit.getScreenY(MainController.rightPanel.caozuoPanel) + MainController.rightPanel.caozuoPanel.getBoundsInParent().getHeight() + 20.0);
                    }
                    if (d == topPanel.getDirection()) {
                        kech_list_panel.setLayoutY(JFXKit.getScreenY(MainController.topPanel.caozuoPanel) + MainController.topPanel.caozuoPanel.getBoundsInParent().getHeight() + 20.0);
                        kech_list_panel.setLayoutX(topPanel.getLayoutX() + topPanel.getYinaPanel().getWidth() / 2.0);
                    }
                    if (d == leftPanel.getDirection()) {
                        kech_list_panel.setLayoutX(leftPanel.getLayoutX() + Config.MJ_HEIGHT + Config.MJ_HEIGHT_3_4 + 20.0);
                        kech_list_panel.setLayoutY(JFXKit.getScreenY(MainController.leftPanel.caozuoPanel) + MainController.leftPanel.caozuoPanel.getBoundsInParent().getHeight() + 20.0);
                    }
                    if (!static_main_panel.getChildren().contains(kech_list_panel)) {
                        static_main_panel.getChildren().add(kech_list_panel);
                    }
                    kech_list_panel.setVisible(true);
                    for (List<Mj> mjs1 : chi_list) {
                        mjs1.sort(new ComparatorMjResult());
                        HBox hBox = new HBox();
                        hBox.setAlignment(Pos.CENTER);
                        hBox.setCursor(Cursor.HAND);
                        for (Mj mj4 : mjs1) {
                            mj4.getYina_imageView().addClass(CHI_BORDER);
                            mj4.getYina_imageView().getImageView().setOnMouseClicked(event -> hBox.fireEvent(event));
                            ImageView imageView = new ImageView();
                            if (mj4.caisheng) {
                                imageView.setImage(ImageKit.coverImage(Config.getImageByMj(mj4), Config.LANSE));
                            } else {
                                imageView.setImage(Config.getImageByMj(mj4));
                            }
                            imageView.setFitWidth(Config.MJ_WIDTH_3_4);
                            imageView.setFitHeight(Config.MJ_HEIGHT_3_4);
                            hBox.getChildren().add(imageView);
                            hBox.setOnMouseClicked(event -> {
                                dapaifangx = directions.get(finalD1).getDirection();
                                mj.setNapai_direction(dapaifangx);
                                mj.setPaizhuantai(4);
                                mjs1.sort(new ComparatorMjResult());
                                int i = 0;
                                for (Mj mj2 : mjs1) {
                                    mj2.setPaizhuantai(4);
                                    if (mj2.caisheng) {
                                        switch (i) {
                                            case 0: {
                                                if (mj.getResult() - ((Mj)mjs1.get(1)).getResult() == 1) {
                                                    mj2.caisheng_dipai = ((Mj)mjs1.get(1)).getResult() - 1;
                                                    break;
                                                }
                                                mj2.caisheng_dipai = ((Mj)mjs1.get(1)).getResult() + 1;
                                                break;
                                            }
                                            case 1: {
                                                mj2.caisheng_dipai = mj.getResult() - ((Mj)mjs1.get(0)).getResult() == 1 ? ((Mj)mjs1.get(0)).getResult() - 1 : ((Mj)mjs1.get(0)).getResult() + 1;
                                            }
                                        }
                                    }
                                    ++i;
                                }
                                Config.yichu.remove(mj);
                                yichu_pane.getChildren().remove(mj.getYichu_imageView());
                                Config.yina.add(mj);
                                Config.yuce.clear();
                                directions.forEach(direction -> direction.setKehu(false));
                                MainController.yucepaiAfterMopai(0, dapaifangx, 1);
                                MainController.clear();
                                MainController.displayYinaPai();
                            });
                        }
                        kech_list_panel.getChildren().add(hBox);
                    }
                    if (directions.get(d).getMo() == 1) {
                        static_main_panel.layout();
                        try {
                            Robot robot3 = new Robot();
                            robot3.mouseMove((int)(JFXKit.getScreenX(directions.get(d).getMo_text()) + directions.get(d).getMo_text().getBoundsInParent().getWidth() / 2.0), (int)(JFXKit.getScreenY(directions.get(d).getMo_text()) + directions.get(d).getMo_text().getBoundsInParent().getHeight() / 2.0));
                        }
                        catch (AWTException e) {
                            e.printStackTrace();
                        }
                        directions.get(d).getMo_text().setOnMouseClicked(event -> {
                            dapaifangx = directions.get(finalD1).getDirection();
                            directions.forEach(direction1 -> {
                                direction1.setKehu(false);
                                direction1.setMo(0);
                            });
                            MainController.mopai();
                        });
                    }
                }
            }
        }
        boolean has_peng = false;
        boolean has_gang = false;
        for (MjPanelInterface direction2 : directions) {
            Robot robot2;
            if (direction2.getDirection() == mj.getNapai_direction() || direction2.getHu() == 1 && Config.systemConfig.xueliuchenghe == 0 || direction2.isQueren() || direction2.getQue() == mj.getResult() / 10) continue;
            if (topPanel.keGang(mj, direction2.getDirection())) {
                direction2.getGang_text().setUserData(direction2.getDirection());
                direction2.getPang_text().setUserData(direction2.getDirection());
                direction2.getCaozuoPanel().getChildren().addAll((Node[])new Node[]{direction2.getGang_text()});
                if (Config.systemConfig.pangpai == 1) {
                    direction2.getCaozuoPanel().getChildren().addAll((Node[])new Node[]{direction2.getPang_text()});
                }
                for (Mj mj15 : topPanel.getGangs(mj, direction2.getDirection())) {
                    mj15.getYina_imageView().addClass(GANG_BORDER);
                    mj15.getYina_imageView().getImageView().setOnMouseClicked(event -> direction2.getGang_text().fireEvent(event));
                }
                static_main_panel.layout();
                try {
                    robot2 = new Robot();
                    robot2.mouseMove((int)(JFXKit.getScreenX(direction2.getGang_text()) + direction2.getGang_text().getBoundsInParent().getWidth() / 2.0), (int)(JFXKit.getScreenY(direction2.getGang_text()) + direction2.getGang_text().getBoundsInParent().getHeight() / 2.0));
                }
                catch (AWTException e) {
                    e.printStackTrace();
                }
                direction2.getPang_text().getProperties().put("peng", true);
                direction2.getGang_text().getProperties().put("gang", true);
                has_gang = true;
            } else if (topPanel.kePeng(mj, direction2.getDirection())) {
                direction2.getPang_text().setUserData(direction2.getDirection());
                direction2.getCaozuoPanel().getChildren().addAll((Node[])new Node[]{direction2.getPang_text()});
                for (Mj mj16 : topPanel.getPengs(mj, direction2.getDirection())) {
                    mj16.getYina_imageView().addClass(PANG_BORDER);
                    mj16.getYina_imageView().getImageView().setOnMouseClicked(event -> direction2.getPang_text().fireEvent(event));
                }
                static_main_panel.layout();
                try {
                    Robot robot4 = new Robot();
                    robot4.mouseMove((int)(JFXKit.getScreenX(direction2.getPang_text()) + direction2.getPang_text().getBoundsInParent().getWidth() / 2.0), (int)(JFXKit.getScreenY(direction2.getPang_text()) + direction2.getPang_text().getBoundsInParent().getHeight() / 2.0));
                }
                catch (AWTException e) {
                    e.printStackTrace();
                }
                direction2.getPang_text().getProperties().put("peng", true);
                has_peng = true;
            }
            if (Config.systemConfig.caishengcanyuchipanggang != 1) continue;
            if (topPanel.keGang(mj, direction2.getDirection())) {
                direction2.getGang_text().setUserData(direction2.getDirection());
                direction2.getPang_text().setUserData(direction2.getDirection());
                if (!direction2.getCaozuoPanel().getChildren().contains(direction2.getGang_text())) {
                    direction2.getCaozuoPanel().getChildren().addAll((Node[])new Node[]{direction2.getGang_text()});
                }
                if (Config.systemConfig.pangpai == 1 && !direction2.getCaozuoPanel().getChildren().contains(direction2.getPang_text())) {
                    direction2.getCaozuoPanel().getChildren().addAll((Node[])new Node[]{direction2.getPang_text()});
                }
                for (Mj mj17 : Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == direction2.getDirection() && mj1.getResult() == mj.getResult() && mj1.getPaizhuantai() == 0).collect(Collectors.toList())) {
                    mj17.getYina_imageView().addClass(GANG_BORDER);
                    mj17.getYina_imageView().getImageView().setOnMouseClicked(event -> direction2.getGang_text().fireEvent(event));
                }
                static_main_panel.layout();
                try {
                    robot2 = new Robot();
                    robot2.mouseMove((int)(JFXKit.getScreenX(direction2.getGang_text()) + direction2.getGang_text().getBoundsInParent().getWidth() / 2.0), (int)(JFXKit.getScreenY(direction2.getGang_text()) + direction2.getGang_text().getBoundsInParent().getHeight() / 2.0));
                }
                catch (AWTException e) {
                    e.printStackTrace();
                }
                direction2.getPang_text().getProperties().put("peng", true);
                direction2.getGang_text().getProperties().put("gang", true);
                has_gang = true;
                continue;
            }
            if (!topPanel.kePeng(mj, direction2.getDirection())) continue;
            direction2.getPang_text().setUserData(direction2.getDirection());
            if (!direction2.getCaozuoPanel().getChildren().contains(direction2.getPang_text())) {
                direction2.getCaozuoPanel().getChildren().addAll((Node[])new Node[]{direction2.getPang_text()});
            }
            for (Mj mj18 : Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == direction2.getDirection() && mj1.getResult() == mj.getResult() && mj1.getPaizhuantai() == 0).collect(Collectors.toList())) {
                mj18.getYina_imageView().addClass(PANG_BORDER);
                mj18.getYina_imageView().getImageView().setOnMouseClicked(event -> direction2.getPang_text().fireEvent(event));
            }
            static_main_panel.layout();
            try {
                robot2 = new Robot();
                robot2.mouseMove((int)(JFXKit.getScreenX(direction2.getPang_text()) + direction2.getPang_text().getBoundsInParent().getWidth() / 2.0), (int)(JFXKit.getScreenY(direction2.getPang_text()) + direction2.getPang_text().getBoundsInParent().getHeight() / 2.0));
            }
            catch (AWTException e) {
                e.printStackTrace();
            }
            direction2.getPang_text().getProperties().put("peng", true);
            has_peng = true;
        }
        boolean has_hu = false;
        if (Config.systemConfig.zhixianzimo == 0) {
            for (MjPanelInterface direction3 : directions) {
                direction3.getCaozuoPanel().getParent().applyCss();
                direction3.getCaozuoPanel().getParent().layout();
                direction3.getCaozuoPanel().applyCss();
                direction3.getCaozuoPanel().layout();
                if (direction3.getDirection() == dapaifangx || direction3.isQueren()) continue;
                boolean kehu = false;
                List<Mj> list = Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == direction3.getDirection()).collect(Collectors.toList());
                Mj mj19 = (Mj)mj.clone();
                mj19.caisheng = false;
                list.add(mj19);
                if (HuPaiKit.HuPaiPanDin(list, direction3.getQue())) {
                    kehu = true;
                }
                if (direction3.getHu() != 0 && Config.systemConfig.xueliuchenghe != 1 || direction3.getQue() == mj.getResult() / 10 || !kehu) continue;
                direction3.getHu_text().setUserData(direction3.getDirection());
                direction3.setKehu(true);
                has_hu = true;
                direction3.getCaozuoPanel().getChildren().add(direction3.getHu_text());
                static_main_panel.layout();
                try {
                    robot = new Robot();
                    robot.mouseMove((int)(JFXKit.getScreenX(direction3.getHu_text()) + direction3.getHu_text().getBoundsInParent().getWidth() / 2.0), (int)(JFXKit.getScreenY(direction3.getHu_text()) + direction3.getHu_text().getBoundsInParent().getHeight() / 2.0));
                }
                catch (AWTException e) {
                    e.printStackTrace();
                }
            }
        }
        if (Config.systemConfig.yipaoduoxiang == 1 && directions.stream().filter(direction -> direction.isKehu()).count() > 1L) {
            directions.forEach(direction -> {
                if (direction.isKehu()) {
                    int dapaifangx_temp = direction.getDirection();
                    do {
                        if (++dapaifangx_temp > 3) {
                            dapaifangx_temp = 0;
                        }
                        if (directions.get(dapaifangx_temp).getHu() != 0 && (Config.systemConfig.hupaikemo != 1 || directions.get(dapaifangx_temp).getQiangzhiHu() != 0) || directions.get(dapaifangx_temp).isQueren()) continue;
                        directions.get(dapaifangx_temp).setMo(1);
                        directions.get(dapaifangx_temp).getMo_text().setUserData(directions.get(dapaifangx_temp).getDirection());
                        if (directions.get(dapaifangx_temp).getCaozuoPanel().getChildren().contains(directions.get(dapaifangx_temp).getMo_text())) continue;
                        directions.get(dapaifangx_temp).getCaozuoPanel().getChildren().add(directions.get(dapaifangx_temp).getMo_text());
                    } while (directions.get(dapaifangx_temp).getHu() == 1 && Config.systemConfig.hupaikemo == 0 || directions.get(dapaifangx_temp).getQiangzhiHu() == 1 || directions.get(dapaifangx_temp).isQueren());
                }
            });
        }
        int dapaifangx_temp = dapaifangx;
        do {
            if (Config.systemConfig.niezhuanimo == 1) {
                if (--dapaifangx_temp < 0) {
                    dapaifangx_temp = 3;
                }
            } else if (++dapaifangx_temp > 3) {
                dapaifangx_temp = 0;
            }
            if (directions.get(dapaifangx_temp).getHu() != 0 && (Config.systemConfig.hupaikemo != 1 || directions.get(dapaifangx_temp).getQiangzhiHu() != 0) || directions.get(dapaifangx_temp).isQueren()) continue;
            directions.get(dapaifangx_temp).setMo(1);
            directions.get(dapaifangx_temp).getMo_text().setUserData(directions.get(dapaifangx_temp).getDirection());
            if (directions.get(dapaifangx_temp).getCaozuoPanel().getChildren().contains(directions.get(dapaifangx_temp).getMo_text())) continue;
            directions.get(dapaifangx_temp).getCaozuoPanel().getChildren().add(directions.get(dapaifangx_temp).getMo_text());
        } while (directions.get(dapaifangx_temp).getHu() == 1 && Config.systemConfig.hupaikemo == 0 || directions.get(dapaifangx_temp).getQiangzhiHu() == 1 || directions.get(dapaifangx_temp).isQueren());
        if (has_peng || has_gang) {
            MainController.yichu(mj);
            directions.forEach(direction -> {
                direction.getPang_text().setOnMouseClicked(event -> {
                    dapaifangx = (Integer)direction.getPang_text().getUserData();
                    mj.setNapai_direction(dapaifangx);
                    mj.setPaizhuantai(1);
                    List<Mj> pangpai = topPanel.getPengs(mj, mj.getNapai_direction());
                    for (int i = 0; i < pangpai.size() && i < 2; ++i) {
                        pangpai.get(i).setPaizhuantai(1);
                    }
                    if (Config.systemConfig.juedicaisheng == 1 && pangpai.size() == 3) {
                        pangpai.forEach(mj1 -> {
                            if (mj1.getPaizhuantai() == 0) {
                                mj1.caisheng = true;
                            }
                        });
                    }
                    if (Config.systemConfig.caishengcanyuchipanggang == 1 && pangpai.size() < 2) {
                        List<Mj> caishengs = Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == mj.getNapai_direction() && mj1.caisheng && mj1.getPaizhuantai() == 0).collect(Collectors.toList());
                        for (int i = 0; i < 2 - pangpai.size(); ++i) {
                            ((Mj)caishengs.get(i)).setPaizhuantai(1);
                            if (!((Mj)caishengs.get((int)i)).caisheng) continue;
                            ((Mj)caishengs.get((int)i)).caisheng_dipai = mj.getResult();
                        }
                    }
                    Config.yichu.remove(mj);
                    yichu_pane.getChildren().remove(mj.getYichu_imageView());
                    Config.yina.add(mj);
                    Config.yuce.clear();
                    directions.forEach(direction1 -> {
                        direction1.setKehu(false);
                        direction1.getPang_text().getProperties().put("peng", false);
                        direction1.getGang_text().getProperties().put("gang", false);
                    });
                    MainController.yucepaiAfterMopai(0, dapaifangx, 1);
                    MainController.clear();
                    MainController.displayYinaPai();
                });
                direction.getGang_text().setOnMouseClicked(event -> {
                    dapaifangx = (Integer)direction.getGang_text().getUserData();
                    mj.setNapai_direction(dapaifangx);
                    mj.setPaizhuantai(2);
                    directions.forEach(direction1 -> {
                        direction1.setKehu(false);
                        direction1.getPang_text().getProperties().put("peng", false);
                        direction1.getGang_text().getProperties().put("gang", false);
                    });
                    List<Mj> gangpai = topPanel.getGangs(mj, mj.getNapai_direction());
                    gangpai.forEach(mj1 -> mj1.setPaizhuantai(2));
                    if (Config.systemConfig.caishengcanyuchipanggang == 1 && gangpai.size() < 3) {
                        List<Mj> caishengs = Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == mj.getNapai_direction() && mj1.caisheng && mj1.getPaizhuantai() == 0).collect(Collectors.toList());
                        for (int i = 0; i < 3 - gangpai.size(); ++i) {
                            ((Mj)caishengs.get(i)).setPaizhuantai(2);
                            if (!((Mj)caishengs.get((int)i)).caisheng) continue;
                            ((Mj)caishengs.get((int)i)).caisheng_dipai = mj.getResult();
                        }
                    }
                    Config.yichu.remove(mj);
                    yichu_pane.getChildren().remove(mj.getYichu_imageView());
                    Config.yina.add(mj);
                    Config.yuce.clear();
                    switch (Config.systemConfig.gangpaishunxu) {
                        case "\u987a\u6760\u724c": {
                            MainController.mopai();
                            break;
                        }
                        case "\u540e\u6760\u724c": {
                            MainController.houGangMopai();
                            break;
                        }
                        case "\u968f\u610f\u6760": {
                            MainController.SuiYiGang();
                        }
                    }
                });
                if (direction.getMo() == 1) {
                    direction.getMo_text().setOnMouseClicked(event -> {
                        dapaifangx = direction.getDirection();
                        directions.forEach(direction1 -> {
                            direction1.setKehu(false);
                            direction1.setMo(0);
                        });
                        MainController.mopai();
                    });
                }
            });
        }
        if (has_hu) {
            if (Config.systemConfig.yipaoduoxiang == 1) {
                mj.setChupai_direction(dapaifangx);
                MainController.yichu(mj);
                boolean finalHas_peng2 = has_peng;
                boolean finalHas_gang2 = has_gang;
                directions.forEach(direction -> {
                    if (direction.getHu() != 1 && direction.isKehu()) {
                        boolean finalHas_peng = finalHas_peng2;
                        boolean finalHas_gang = finalHas_gang2;
                        if (Config.systemConfig.hupaikepang == 1 && finalHas_peng2 || Config.systemConfig.hupaikegang == 1 && finalHas_gang2) {
                            int dapaifangx_temp_1 = (Integer)direction.getHu_text().getUserData();
                            do {
                                if (Config.systemConfig.niezhuanimo == 1) {
                                    if (--dapaifangx_temp_1 < 0) {
                                        dapaifangx_temp_1 = 3;
                                    }
                                } else if (++dapaifangx_temp_1 > 3) {
                                    dapaifangx_temp_1 = 0;
                                }
                                if (directions.get(dapaifangx_temp_1).getHu() != 0 && (Config.systemConfig.hupaikemo != 1 || directions.get(dapaifangx_temp_1).getQiangzhiHu() != 0) || directions.get(dapaifangx_temp_1).isQueren()) continue;
                                directions.get(dapaifangx_temp_1).setMo(1);
                                directions.get(dapaifangx_temp_1).getMo_text().setUserData(directions.get(dapaifangx_temp_1).getDirection());
                                if (directions.get(dapaifangx_temp_1).getCaozuoPanel().getChildren().contains(directions.get(dapaifangx_temp_1).getMo_text())) continue;
                                directions.get(dapaifangx_temp_1).getCaozuoPanel().getChildren().add(directions.get(dapaifangx_temp_1).getMo_text());
                            } while (directions.get(dapaifangx_temp_1).getHu() == 1 && Config.systemConfig.hupaikemo == 0 || directions.get(dapaifangx_temp_1).getQiangzhiHu() == 1 || directions.get(dapaifangx_temp_1).isQueren());
                        }
                        direction.getHu_text().setOnMouseClicked(event -> {
                            dapaifangx = (Integer)direction.getHu_text().getUserData();
                            direction.setHu(1);
                            direction.setKehu(false);
                            Config.yichu.remove(mj);
                            yichu_pane.getChildren().remove(mj.getYichu_imageView());
                            direction.setHu_mj((Mj)mj.clone());
                            direction.getHu_mj().setNapai_direction(dapaifangx);
                            direction.getHu_mj().setHu(true);
                            Config.yuce.clear();
                            Text yihu = new Text("\u80e1");
                            yihu.setFont(Font.font("\u5b8b\u4f53", FontWeight.BOLD, 25.0));
                            yihu.setFill(Color.RED);
                            direction.getCaozuoPanel().getChildren().clear();
                            direction.getCaozuoPanel().getChildren().add(yihu);
                            if (directions.stream().filter(direction1 -> direction1.isKehu()).count() == 0L) {
                                directions.forEach(direction1 -> {
                                    direction1.setKehu(false);
                                    direction1.setMo(0);
                                });
                                if (Config.systemConfig.hupaikepang == 1 && finalHas_peng) {
                                    return;
                                }
                                if (Config.systemConfig.hupaikegang == 1 && finalHas_gang) {
                                    return;
                                }
                                if (Config.systemConfig.hupaikemo == 1) {
                                    directions.forEach(d -> {
                                        if (!d.isQueren() && d.getQiangzhiHu() == 0) {
                                            d.setMo(1);
                                            d.getMo_text().setUserData(d.getDirection());
                                            if (!d.getCaozuoPanel().getChildren().contains(d.getMo_text())) {
                                                d.getCaozuoPanel().getChildren().add(d.getMo_text());
                                            }
                                            d.getMo_text().setOnMouseClicked(e -> {
                                                dapaifangx = d.getDirection();
                                                directions.forEach(direction1 -> {
                                                    direction1.setKehu(false);
                                                    direction1.setMo(0);
                                                });
                                                MainController.mopai();
                                            });
                                        }
                                    });
                                    return;
                                }
                                MainController.mopai();
                            }
                        });
                    }
                });
                directions.forEach(direction -> {
                    if (direction.getMo() == 1) {
                        direction.getMo_text().setOnMouseClicked(event -> {
                            dapaifangx = direction.getDirection();
                            directions.forEach(direction1 -> {
                                direction1.setKehu(false);
                                direction1.setMo(0);
                            });
                            MainController.mopai();
                        });
                    }
                });
            } else {
                MainController.yichu(mj);
                boolean finalHas_peng3 = has_peng;
                boolean finalHas_gang3 = has_gang;
                directions.forEach(direction -> {
                    if (direction.getHu() != 1 && direction.isKehu()) {
                        boolean finalHas_peng1 = finalHas_peng3;
                        boolean finalHas_gang1 = finalHas_gang3;
                        if (Config.systemConfig.hupaikepang == 1 && finalHas_peng3 || Config.systemConfig.hupaikegang == 1 && finalHas_gang3) {
                            int dapaifangx_temp_1 = (Integer)direction.getHu_text().getUserData();
                            do {
                                if (Config.systemConfig.niezhuanimo == 1) {
                                    if (--dapaifangx_temp_1 < 0) {
                                        dapaifangx_temp_1 = 3;
                                    }
                                } else if (++dapaifangx_temp_1 > 3) {
                                    dapaifangx_temp_1 = 0;
                                }
                                if (directions.get(dapaifangx_temp_1).getHu() != 0 && (Config.systemConfig.hupaikemo != 1 || directions.get(dapaifangx_temp_1).getQiangzhiHu() != 0) || directions.get(dapaifangx_temp_1).isQueren()) continue;
                                directions.get(dapaifangx_temp_1).setMo(1);
                                directions.get(dapaifangx_temp_1).getMo_text().setUserData(directions.get(dapaifangx_temp_1).getDirection());
                                if (directions.get(dapaifangx_temp_1).getCaozuoPanel().getChildren().contains(directions.get(dapaifangx_temp_1).getMo_text())) continue;
                                directions.get(dapaifangx_temp_1).getCaozuoPanel().getChildren().add(directions.get(dapaifangx_temp_1).getMo_text());
                            } while (directions.get(dapaifangx_temp_1).getHu() == 1 && Config.systemConfig.hupaikemo == 0 || directions.get(dapaifangx_temp_1).getQiangzhiHu() == 1 || directions.get(dapaifangx_temp_1).isQueren());
                        }
                        direction.getHu_text().setOnMouseClicked(event -> {
                            dapaifangx = (Integer)direction.getHu_text().getUserData();
                            direction.setHu(1);
                            direction.setKehu(false);
                            Config.yichu.remove(mj);
                            yichu_pane.getChildren().remove(mj.getYichu_imageView());
                            direction.setHu_mj((Mj)mj.clone());
                            direction.getHu_mj().setNapai_direction(dapaifangx);
                            direction.getHu_mj().setHu(true);
                            Config.yuce.clear();
                            Text yihu = new Text("\u80e1");
                            yihu.setFont(Font.font("\u5b8b\u4f53", FontWeight.BOLD, 25.0));
                            yihu.setFill(Color.RED);
                            direction.getCaozuoPanel().getChildren().clear();
                            direction.getCaozuoPanel().getChildren().add(yihu);
                            directions.forEach(direction1 -> direction1.setKehu(true));
                            if (Config.systemConfig.hupaikepang == 1 && finalHas_peng1) {
                                return;
                            }
                            if (Config.systemConfig.hupaikegang == 1 && finalHas_gang1) {
                                return;
                            }
                            if (Config.systemConfig.xueliuchenghe == 1) {
                                direction.getXueliuchenghe_hus().clear();
                                direction.getYina_hupai().forEach(mj1 -> direction.getXueliuchenghe_hus().add(mj1.getResult()));
                            }
                            if (Config.systemConfig.hupaikemo == 1) {
                                directions.forEach(d -> {
                                    if (!d.isQueren() && d.getQiangzhiHu() == 0) {
                                        d.setMo(1);
                                        d.getMo_text().setUserData(d.getDirection());
                                        if (!d.getCaozuoPanel().getChildren().contains(d.getMo_text())) {
                                            d.getCaozuoPanel().getChildren().add(d.getMo_text());
                                        }
                                        d.getMo_text().setOnMouseClicked(e -> {
                                            dapaifangx = d.getDirection();
                                            directions.forEach(direction1 -> {
                                                direction1.setKehu(false);
                                                direction1.setMo(0);
                                            });
                                            MainController.mopai();
                                        });
                                    }
                                });
                                return;
                            }
                            MainController.mopai();
                        });
                        if (Config.systemConfig.hupaikemo == 1) {
                            direction.setMo(1);
                            direction.getMo_text().setUserData(direction.getDirection());
                            if (!direction.getCaozuoPanel().getChildren().contains(direction.getMo_text())) {
                                direction.getCaozuoPanel().getChildren().add(direction.getMo_text());
                            }
                        }
                    }
                });
                directions.forEach(direction -> {
                    if (direction.getMo() == 1) {
                        direction.getMo_text().setOnMouseClicked(event -> {
                            dapaifangx = direction.getDirection();
                            directions.forEach(direction1 -> {
                                direction1.setKehu(false);
                                direction1.setMo(0);
                            });
                            MainController.mopai();
                        });
                    }
                });
            }
        }
        if (!(has_hu || has_peng || has_gang || kechi)) {
            for (MjPanelInterface direction1 : directions) {
                direction1.setMo(0);
            }
            MainController.yichu(mj);
            dapaifangx = Config.systemConfig.niezhuanimo == 1 ? --dapaifangx : ++dapaifangx;
            MainController.mopai();
        }
    }

    public static void displayYinaPai() {
        if (!Config.systemConfig.gangfa.equals("\u8f6e\u5230\u81ea\u7531\u6760(\u624b\u52a8\u6760\u82b1)")) {
            for (Mj mj2 : Config.yina) {
                if (!mj2.isLast() || mj2.getResult() <= 40 || mj2.getResult() >= 52 || caisheng.size() != 0 && caisheng.get(0) >= 40) continue;
                Config.yuce.clear();
                mj2.setPaizhuantai(2);
                dapaifangx = mj2.getNapai_direction();
                switch (Config.systemConfig.ganghua) {
                    case "\u987a\u6760\u724c": {
                        MainController.mopai();
                        return;
                    }
                    case "\u540e\u6760\u724c": {
                        MainController.houGangMopai();
                        return;
                    }
                    case "\u968f\u610f\u6760": {
                        MainController.SuiYiGang();
                        return;
                    }
                }
            }
        }
        List<Mj> temp_yuce = new LinkedList<Mj>(Config.yuce);
        Mj namj = null;
        for (Mj mj3 : temp_yuce) {
            mj3.huapai_yuce = null;
            if (mj3.getResult() <= 40 || mj3.getResult() >= 52 || caisheng.size() != 0 && caisheng.get(0) >= 40) continue;
            switch (Config.systemConfig.ganghua) {
                case "\u540e\u6760\u724c": {
                    do {
                        Mj mj1;
                        int i;
                        LinkedList<Mj> mjList;
                        if (Config.systemConfig.mapaicengshu == 2) {
                            mjList = new LinkedList<Mj>();
                            for (i = weina.size() - 1; i >= 0; --i) {
                                mj1 = weina.get(i);
                                if (mj1.getMahjong_number() % 2 != 0) {
                                    mjList.add(mjList.size() - 1, mj1);
                                    continue;
                                }
                                mjList.add(mj1);
                            }
                            for (i = 0; i < mjList.size(); ++i) {
                                mj1 = (Mj)mjList.get(i);
                                if (mj1.isMaima() || mj1.getResult() >= 40) continue;
                                if (namj == null) {
                                    namj = mj1;
                                    break;
                                }
                                if (mjList.indexOf(mj1) <= mjList.indexOf(namj)) continue;
                                namj = mj1;
                                break;
                            }
                        }
                        if (Config.systemConfig.mapaicengshu == 3) {
                            mjList = new LinkedList();
                            block30: for (i = weina.size() - 1; i >= 0; --i) {
                                mj1 = weina.get(i);
                                switch (mj1.getMahjong_number() % 3) {
                                    case 0: {
                                        mjList.add(mj1);
                                        continue block30;
                                    }
                                    case 1: {
                                        mjList.add(mjList.size() - 1, mj1);
                                        continue block30;
                                    }
                                    case 2: {
                                        mjList.add(mjList.size() - 2, mj1);
                                    }
                                }
                            }
                            for (i = 0; i < mjList.size(); ++i) {
                                mj1 = (Mj)mjList.get(i);
                                if (mj1.isMaima() || mj1.getResult() >= 40) continue;
                                if (namj == null) {
                                    namj = mj1;
                                    break;
                                }
                                if (mjList.indexOf(mj1) <= mjList.indexOf(namj)) continue;
                                namj = mj1;
                                break;
                            }
                        }
                        namj.setYuce_direction(mj3.getYuce_direction());
                        mj3.huapai_yuce = namj;
                    } while (namj != null && namj.getResult() > 40);
                    break;
                }
                case "\u987a\u6760\u724c": {
                    do {
                        for (int i = 0; i < weina.size(); ++i) {
                            Mj mj1 = weina.get(i);
                            if (mj1.isMaima() || mj1.getResult() >= 40) continue;
                            if (namj == null) {
                                namj = mj1;
                                break;
                            }
                            if (weina.indexOf(mj1) <= weina.indexOf(namj)) continue;
                            namj = mj1;
                            break;
                        }
                        namj.setYuce_direction(mj3.getYuce_direction());
                        mj3.huapai_yuce = namj;
                    } while (namj != null && namj.getResult() > 40 && namj.getResult() < 52);
                }
            }
        }
        if (!static_main_panel.getChildren().contains(bottom_panel)) {
            static_main_panel.getChildren().add(topPanel);
            static_main_panel.getChildren().add(rightPanel);
            static_main_panel.getChildren().add(bottom_panel);
            static_main_panel.getChildren().add(leftPanel);
            static_main_panel.getChildren().add(bottomPanel);
        }
        static_main_panel.getChildren().remove(kech_list_panel);
        double y = 0.0;
        double x = 0.0;
        if (MainController.topPanel.mjClick == null) {
            MainController.topPanel.mjClick = event -> {
                MouseButton button = event.getButton();
                switch (button) {
                    case PRIMARY: {
                        ImageView imageView = (ImageView)event.getSource();
                        Mj mj = (Mj)imageView.getUserData();
                        if (mj.getNapai_direction() != dapaifangx) {
                            dapaifangx = mj.getNapai_direction();
                        }
                        int index = MainController.topPanel.yinaPanel.getChildren().indexOf(mj.getYina_imageView());
                        int size = MainController.topPanel.yinaPanel.getChildren().size();
                        for (int i = index + 1; i < size; ++i) {
                            ((Node)MainController.topPanel.yinaPanel.getChildren().get(i)).setLayoutX(((Node)MainController.topPanel.yinaPanel.getChildren().get(i)).getLayoutX() + mj.getYina_imageView().getImageView().getFitWidth());
                        }
                        MainController.chupai(mj);
                        break;
                    }
                    case SECONDARY: {
                        if (Config.systemConfig.rengyijiu == 1) {
                            ImageView imageView1 = (ImageView)event.getSource();
                            Mj mj1 = (Mj)imageView1.getUserData();
                            if ((mj1.getResult() >= 30 || mj1.getResult() % 10 != 1 && mj1.getResult() % 10 != 9) && mj1.getResult() <= 30) break;
                            MainController.topPanel.yinaPanel.getChildren().remove(mj1.getYina_imageView());
                            MainController.saveState();
                            Config.yina.remove(mj1);
                            event.consume();
                            break;
                        }
                        ImageView imageView1 = (ImageView)event.getSource();
                        Mj mj1 = (Mj)imageView1.getUserData();
                        switch (mj1.getResult()) {
                            case 1: {
                                mj1.setResult(2);
                                mj1.caisheng = caisheng.contains(mj1.getResult());
                                if (mj1.caisheng) {
                                    imageView1.setImage(ImageKit.coverImage(Config.getImageByMj(mj1), Config.LANSE));
                                } else {
                                    imageView1.setImage(Config.getImageByMj(mj1));
                                }
                                event.consume();
                                break;
                            }
                            case 2: {
                                mj1.setResult(3);
                                mj1.caisheng = caisheng.contains(mj1.getResult());
                                if (mj1.caisheng) {
                                    imageView1.setImage(ImageKit.coverImage(Config.getImageByMj(mj1), Config.LANSE));
                                } else {
                                    imageView1.setImage(Config.getImageByMj(mj1));
                                }
                                event.consume();
                                break;
                            }
                            case 3: {
                                mj1.setResult(1);
                                mj1.caisheng = caisheng.contains(mj1.getResult());
                                if (mj1.caisheng) {
                                    imageView1.setImage(ImageKit.coverImage(Config.getImageByMj(mj1), Config.LANSE));
                                } else {
                                    imageView1.setImage(Config.getImageByMj(mj1));
                                }
                                event.consume();
                            }
                        }
                        break;
                    }
                }
            };
        }
        topPanel.display();
        x = Config.SCREEN_WIDTH / 2.0 - topPanel.getYinaPanel().getPrefWidth() / 2.0;
        topPanel.setLayoutX(x);
        topPanel.setLayoutY(y);
        x = Config.SCREEN_WIDTH / 2.0 + Config.MJ_WIDTH * (double)Config.systemConfig.wanjiapaishu / 2.0 + Config.MJ_WIDTH * 4.0;
        rightPanel.setLayoutX(x);
        if (MainController.rightPanel.mjClick == null) {
            MainController.rightPanel.mjClick = event -> {
                MouseButton button = event.getButton();
                switch (button) {
                    case PRIMARY: {
                        ImageView imageView = (ImageView)event.getSource();
                        Mj mj = (Mj)imageView.getUserData();
                        if (mj.getNapai_direction() != dapaifangx) {
                            dapaifangx = mj.getNapai_direction();
                        }
                        int index = MainController.rightPanel.yinaPanel.getChildren().indexOf(mj.getYina_imageView());
                        int size = MainController.rightPanel.yinaPanel.getChildren().size();
                        for (int i = index + 1; i < size; ++i) {
                            ((Node)MainController.rightPanel.yinaPanel.getChildren().get(i)).setLayoutY(((Node)MainController.rightPanel.yinaPanel.getChildren().get(i)).getLayoutY() + mj.getYina_imageView().getImageView().getFitHeight());
                        }
                        MainController.chupai(mj);
                        break;
                    }
                    case SECONDARY: {
                        if (Config.systemConfig.rengyijiu == 1) {
                            ImageView imageView1 = (ImageView)event.getSource();
                            Mj mj1 = (Mj)imageView1.getUserData();
                            if ((mj1.getResult() >= 30 || mj1.getResult() % 10 != 1 && mj1.getResult() % 10 != 9) && mj1.getResult() <= 30) break;
                            MainController.rightPanel.yinaPanel.getChildren().remove(mj1.getYina_imageView());
                            MainController.saveState();
                            Config.yina.remove(mj1);
                            event.consume();
                            break;
                        }
                        ImageView imageView1 = (ImageView)event.getSource();
                        Mj mj1 = (Mj)imageView1.getUserData();
                        switch (mj1.getResult()) {
                            case 1: {
                                mj1.setResult(2);
                                mj1.caisheng = caisheng.contains(mj1.getResult());
                                if (mj1.caisheng) {
                                    imageView1.setImage(ImageKit.rotate90SX(ImageKit.coverImage(Config.getImageByMj(mj1), Config.LANSE)));
                                } else {
                                    imageView1.setImage(ImageKit.rotate90SX(Config.getImageByMj(mj1)));
                                }
                                event.consume();
                                break;
                            }
                            case 2: {
                                mj1.setResult(3);
                                mj1.caisheng = caisheng.contains(mj1.getResult());
                                if (mj1.caisheng) {
                                    imageView1.setImage(ImageKit.rotate90SX(ImageKit.coverImage(Config.getImageByMj(mj1), Config.LANSE)));
                                } else {
                                    imageView1.setImage(ImageKit.rotate90SX(Config.getImageByMj(mj1)));
                                }
                                event.consume();
                                break;
                            }
                            case 3: {
                                mj1.setResult(1);
                                mj1.caisheng = caisheng.contains(mj1.getResult());
                                if (mj1.caisheng) {
                                    imageView1.setImage(ImageKit.rotate90SX(ImageKit.coverImage(Config.getImageByMj(mj1), Config.LANSE)));
                                } else {
                                    imageView1.setImage(ImageKit.rotate90SX(Config.getImageByMj(mj1)));
                                }
                                event.consume();
                            }
                        }
                        break;
                    }
                }
            };
        }
        rightPanel.display();
        y = Config.SCREEN_HEIGHT - Config.MJ_HEIGHT_3_4 - MainController.rightPanel.yinaPanel.getPrefHeight() - 40.0;
        rightPanel.setLayoutY(y);
        if (MainController.bottomPanel.mjClick == null) {
            MainController.bottomPanel.mjClick = event -> {
                MouseButton button = event.getButton();
                switch (button) {
                    case PRIMARY: {
                        ImageView imageView = (ImageView)event.getSource();
                        Mj mj = (Mj)imageView.getUserData();
                        if (mj.getNapai_direction() != dapaifangx) {
                            dapaifangx = mj.getNapai_direction();
                        }
                        int index = MainController.bottomPanel.yinaPanel.getChildren().indexOf(mj.getYina_imageView());
                        int size = MainController.bottomPanel.yinaPanel.getChildren().size();
                        for (int i = index + 1; i < size; ++i) {
                            ((Node)MainController.bottomPanel.yinaPanel.getChildren().get(i)).setLayoutX(((Node)MainController.bottomPanel.yinaPanel.getChildren().get(i)).getLayoutX() - mj.getYina_imageView().getImageView().getFitWidth());
                        }
                        MainController.chupai(mj);
                        break;
                    }
                    case SECONDARY: {
                        if (Config.systemConfig.rengyijiu == 1) {
                            ImageView imageView1 = (ImageView)event.getSource();
                            Mj mj1 = (Mj)imageView1.getUserData();
                            if ((mj1.getResult() >= 30 || mj1.getResult() % 10 != 1 && mj1.getResult() % 10 != 9) && mj1.getResult() <= 30) break;
                            MainController.bottomPanel.yinaPanel.getChildren().remove(mj1.getYina_imageView());
                            MainController.saveState();
                            Config.yina.remove(mj1);
                            event.consume();
                            break;
                        }
                        ImageView imageView1 = (ImageView)event.getSource();
                        Mj mj1 = (Mj)imageView1.getUserData();
                        switch (mj1.getResult()) {
                            case 1: {
                                mj1.setResult(2);
                                mj1.caisheng = caisheng.contains(mj1.getResult());
                                if (mj1.caisheng) {
                                    imageView1.setImage(ImageKit.coverImage(Config.getImageByMj(mj1), Config.LANSE));
                                } else {
                                    imageView1.setImage(Config.getImageByMj(mj1));
                                }
                                event.consume();
                                break;
                            }
                            case 2: {
                                mj1.setResult(3);
                                mj1.caisheng = caisheng.contains(mj1.getResult());
                                if (mj1.caisheng) {
                                    imageView1.setImage(ImageKit.coverImage(Config.getImageByMj(mj1), Config.LANSE));
                                } else {
                                    imageView1.setImage(Config.getImageByMj(mj1));
                                }
                                event.consume();
                                break;
                            }
                            case 3: {
                                mj1.setResult(1);
                                mj1.caisheng = caisheng.contains(mj1.getResult());
                                if (mj1.caisheng) {
                                    imageView1.setImage(ImageKit.coverImage(Config.getImageByMj(mj1), Config.LANSE));
                                } else {
                                    imageView1.setImage(Config.getImageByMj(mj1));
                                }
                                event.consume();
                            }
                        }
                        break;
                    }
                }
            };
        }
        bottomPanel.display();
        y = Config.SCREEN_HEIGHT - 40.0 - Config.MJ_HEIGHT - Config.MJ_HEIGHT_3_4;
        x = Config.SCREEN_WIDTH / 2.0 - (Config.MJ_WIDTH * (double)(Config.systemConfig.wanjiapaishu + 1) + Config.MJ_HEIGHT_1_2) / 2.0 + Config.MJ_WIDTH;
        bottomPanel.setLayoutX(x);
        bottomPanel.setLayoutY(y);
        x = Config.SCREEN_WIDTH / 2.0 - Config.MJ_WIDTH * (double)Config.systemConfig.wanjiapaishu / 2.0 - Config.MJ_HEIGHT * 4.0;
        if (MainController.leftPanel.mjClick == null) {
            MainController.leftPanel.mjClick = event -> {
                MouseButton button = event.getButton();
                switch (button) {
                    case PRIMARY: {
                        ImageView imageView = (ImageView)event.getSource();
                        Mj mj = (Mj)imageView.getUserData();
                        if (mj.getNapai_direction() != dapaifangx) {
                            dapaifangx = mj.getNapai_direction();
                        }
                        int index = MainController.leftPanel.yinaPanel.getChildren().indexOf(mj.getYina_imageView());
                        int size = MainController.leftPanel.yinaPanel.getChildren().size();
                        for (int i = index + 1; i < size; ++i) {
                            ((Node)MainController.leftPanel.yinaPanel.getChildren().get(i)).setLayoutY(((Node)MainController.leftPanel.yinaPanel.getChildren().get(i)).getLayoutY() - mj.getYina_imageView().getImageView().getFitHeight());
                        }
                        MainController.chupai(mj);
                        break;
                    }
                    case SECONDARY: {
                        if (Config.systemConfig.rengyijiu == 1) {
                            ImageView imageView1 = (ImageView)event.getSource();
                            Mj mj1 = (Mj)imageView1.getUserData();
                            if ((mj1.getResult() >= 30 || mj1.getResult() % 10 != 1 && mj1.getResult() % 10 != 9) && mj1.getResult() <= 30) break;
                            MainController.leftPanel.yinaPanel.getChildren().remove(mj1.getYina_imageView());
                            MainController.saveState();
                            Config.yina.remove(mj1);
                            event.consume();
                            break;
                        }
                        ImageView imageView1 = (ImageView)event.getSource();
                        Mj mj1 = (Mj)imageView1.getUserData();
                        switch (mj1.getResult()) {
                            case 1: {
                                mj1.setResult(2);
                                mj1.caisheng = caisheng.contains(mj1.getResult());
                                if (mj1.caisheng) {
                                    imageView1.setImage(ImageKit.rotate90DX(ImageKit.coverImage(Config.getImageByMj(mj1), Config.LANSE)));
                                } else {
                                    imageView1.setImage(ImageKit.rotate90DX(Config.getImageByMj(mj1)));
                                }
                                event.consume();
                                break;
                            }
                            case 2: {
                                mj1.setResult(3);
                                mj1.caisheng = caisheng.contains(mj1.getResult());
                                if (mj1.caisheng) {
                                    imageView1.setImage(ImageKit.rotate90DX(ImageKit.coverImage(Config.getImageByMj(mj1), Config.LANSE)));
                                } else {
                                    imageView1.setImage(ImageKit.rotate90DX(Config.getImageByMj(mj1)));
                                }
                                event.consume();
                                break;
                            }
                            case 3: {
                                mj1.setResult(1);
                                mj1.caisheng = caisheng.contains(mj1.getResult());
                                if (mj1.caisheng) {
                                    imageView1.setImage(ImageKit.rotate90DX(ImageKit.coverImage(Config.getImageByMj(mj1), Config.LANSE)));
                                } else {
                                    imageView1.setImage(ImageKit.rotate90DX(Config.getImageByMj(mj1)));
                                }
                                event.consume();
                            }
                        }
                        break;
                    }
                }
            };
        }
        leftPanel.display();
        y = 20.0 + Config.MJ_HEIGHT_3_4;
        leftPanel.setLayoutX(x);
        leftPanel.setLayoutY(y);
        if (Config.systemConfig.zhixianzimo == 0) {
            directions.forEach(d -> {
                ImageView imageView1;
                Image image;
                int x1;
                int d1;
                List<Mj> yina;
                List<Mj> hupai = new ArrayList();
                directions.forEach(dd -> {
                    if (dd.getDirection() != d.getDirection()) {
                        dd.getYina_hupai().forEach(mj -> {
                            mj.setNapai_direction(dd.getDirection());
                            hupai.add(mj);
                        });
                    }
                });
                if (d.getDirection() == bottomPanel.getDirection()) {
                    hupai.forEach(mj -> {
                        Config.yina.forEach(mj1 -> {
                            if (mj1.getNapai_direction() == d.getDirection() && mj1.getPaizhuantai() == 0 && (d.getHu() == 0 || Config.systemConfig.xueliuchenghe == 1) && mj1.getResult() == mj.getResult() && mj1.yuanpai == mj.yuanpai) {
                                int z = mj.getNapai_direction() - d.getDirection();
                                Image image_lam = Config.FANGPAO;
                                image_lam = ImageKit.rotate(image_lam, 180);
                                image_lam = ImageKit.rotate(image_lam, -90 * z);
                                ImageView imageView1_lam = new ImageView(image_lam);
                                if (Math.abs(-90 * z) == 180) {
                                    imageView1_lam.setFitWidth(10.0);
                                    imageView1_lam.setFitHeight(mj1.getYina_imageView().getImageView().getFitWidth() / 2.0);
                                    imageView1_lam.setY(-mj1.getYina_imageView().getImageView().getFitWidth() / 2.0);
                                } else {
                                    imageView1_lam.setFitWidth(mj1.getYina_imageView().getImageView().getFitWidth());
                                    imageView1_lam.setFitHeight(10.0);
                                }
                                mj1.getYina_imageView().addBottom(imageView1_lam);
                            }
                        });
                        Config.yuce.forEach(mj1 -> {
                            if (mj1.getYuce_direction() == d.getDirection() && (d.getHu() == 0 || Config.systemConfig.xueliuchenghe == 1) && mj1.getResult() == mj.getResult() && mj1.yuanpai == mj.yuanpai) {
                                int z = mj.getNapai_direction() - d.getDirection();
                                Image image_lam = Config.FANGPAO_YUCE;
                                image_lam = ImageKit.rotate(image_lam, 180);
                                image_lam = ImageKit.rotate(image_lam, -90 * z);
                                ImageView imageView1_lam = new ImageView(image_lam);
                                imageView1_lam.setFitWidth(15.0);
                                imageView1_lam.setFitHeight(15.0);
                                imageView1_lam.setLayoutX(mj1.getYuce_imageView().getImageView().getFitWidth() - imageView1_lam.getFitWidth());
                                mj1.getYuce_imageView().addBottom(imageView1_lam);
                            }
                        });
                    });
                    if (Config.systemConfig.chisanfangpai == 1) {
                        yina = Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == d.getDirection() && mj1.getPaizhuantai() == 0 && mj1.getResult() < 30).collect(Collectors.toList());
                        for (Mj mj2 : yina) {
                            for (MjPanelInterface dc1 : directions) {
                                d1 = dc1.getDirection();
                                if (d1 == d.getDirection() || dc1.isQueren() || dc1.getHu() != 0 || dc1.getQue() == mj2.getResult() / 10 || !dc1.keChi(mj2, d1) || !MainController.ChiPaiKeTing(d1, mj2)) continue;
                                x1 = d1 - d.getDirection();
                                image = Config.CHIFANGXIANG;
                                image = ImageKit.rotate(image, 180);
                                image = ImageKit.rotate(image, -90 * x1);
                                imageView1 = new ImageView(image);
                                if (Math.abs(-90 * x1) == 180) {
                                    imageView1.setFitWidth(10.0);
                                    imageView1.setFitHeight(mj2.getYina_imageView().getImageView().getFitWidth() / 2.0);
                                    imageView1.setY(-mj2.getYina_imageView().getImageView().getFitWidth() / 2.0);
                                } else {
                                    imageView1.setFitWidth(mj2.getYina_imageView().getImageView().getFitWidth());
                                    imageView1.setFitHeight(10.0);
                                }
                                mj2.getYina_imageView().addBottom(imageView1);
                            }
                        }
                    }
                }
                if (d.getDirection() == rightPanel.getDirection()) {
                    hupai.forEach(mj -> {
                        Config.yina.forEach(mj1 -> {
                            if (mj1.getNapai_direction() == d.getDirection() && mj1.getPaizhuantai() == 0 && (d.getHu() == 0 || Config.systemConfig.xueliuchenghe == 1) && mj1.getResult() == mj.getResult() && mj1.yuanpai == mj.yuanpai) {
                                Image image_lam = Config.FANGPAO;
                                int z = mj.getNapai_direction() - d.getDirection();
                                ImageView imageView1_lam = new ImageView();
                                if (Math.abs(-90 * z) == 180) {
                                    image_lam = ImageKit.rotate(image_lam, -90);
                                    imageView1_lam.setImage(image_lam);
                                    imageView1_lam.setFitHeight(10.0);
                                    imageView1_lam.setFitWidth(mj1.getYina_imageView().getImageView().getFitHeight() / 2.0);
                                } else {
                                    image_lam = ImageKit.rotate(image_lam, 90 + -90 * z);
                                    imageView1_lam.setImage(image_lam);
                                    imageView1_lam.setFitWidth(10.0);
                                    imageView1_lam.setFitHeight(mj1.getYina_imageView().getImageView().getFitHeight());
                                }
                                mj1.getYina_imageView().addBottom(imageView1_lam);
                            }
                        });
                        Config.yuce.forEach(mj1 -> {
                            if (mj1.getYuce_direction() == d.getDirection() && (d.getHu() == 0 || Config.systemConfig.xueliuchenghe == 1) && mj1.getResult() == mj.getResult() && mj1.yuanpai == mj.yuanpai) {
                                int z = mj.getNapai_direction() - d.getDirection();
                                Image image_lam = Config.FANGPAO_YUCE;
                                ImageView imageView1_lam = new ImageView();
                                if (Math.abs(-90 * z) == 180) {
                                    image_lam = ImageKit.rotate(image_lam, -90);
                                    imageView1_lam.setImage(image_lam);
                                } else {
                                    image_lam = ImageKit.rotate(image_lam, 90 + -90 * z);
                                    imageView1_lam.setImage(image_lam);
                                }
                                imageView1_lam.setFitWidth(15.0);
                                imageView1_lam.setFitHeight(15.0);
                                mj1.getYuce_imageView().addBottom(imageView1_lam);
                            }
                        });
                    });
                    if (Config.systemConfig.chisanfangpai == 1) {
                        yina = Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == d.getDirection() && mj1.getPaizhuantai() == 0 && mj1.getResult() < 30).collect(Collectors.toList());
                        for (Mj mj2 : yina) {
                            for (MjPanelInterface dc1 : directions) {
                                d1 = dc1.getDirection();
                                if (d1 == d.getDirection() || dc1.isQueren() || dc1.getHu() != 0 || dc1.getQue() == mj2.getResult() / 10 || !dc1.keChi(mj2, d1) || !MainController.ChiPaiKeTing(d1, mj2)) continue;
                                x1 = d1 - d.getDirection();
                                image = Config.CHIFANGXIANG;
                                imageView1 = new ImageView();
                                if (Math.abs(-90 * x1) == 180) {
                                    image = ImageKit.rotate(image, -90);
                                    imageView1.setImage(image);
                                    imageView1.setFitHeight(10.0);
                                    imageView1.setFitWidth(mj2.getYina_imageView().getImageView().getFitHeight() / 2.0);
                                } else {
                                    image = ImageKit.rotate(image, 90 + -90 * x1);
                                    imageView1.setImage(image);
                                    imageView1.setFitWidth(10.0);
                                    imageView1.setFitHeight(mj2.getYina_imageView().getImageView().getFitHeight());
                                }
                                mj2.getYina_imageView().addBottom(imageView1);
                            }
                        }
                    }
                }
                if (d.getDirection() == topPanel.getDirection()) {
                    hupai.forEach(mj -> {
                        Config.yina.forEach(mj1 -> {
                            if (mj1.getNapai_direction() == d.getDirection() && mj1.getPaizhuantai() == 0 && (d.getHu() == 0 || Config.systemConfig.xueliuchenghe == 1) && mj1.getResult() == mj.getResult() && mj1.yuanpai == mj.yuanpai) {
                                int z = mj.getNapai_direction() - d.getDirection();
                                ImageView imageView1_lam = new ImageView(ImageKit.rotate(Config.FANGPAO, -90 * z));
                                if (Math.abs(-90 * z) == 180) {
                                    imageView1_lam.setFitWidth(10.0);
                                    imageView1_lam.setFitHeight(mj1.getYina_imageView().getImageView().getFitWidth() / 2.0);
                                    imageView1_lam.setY(mj1.getYina_imageView().getImageView().getFitWidth() / 2.0);
                                } else {
                                    imageView1_lam.setFitWidth(mj1.getYina_imageView().getImageView().getFitWidth());
                                    imageView1_lam.setFitHeight(10.0);
                                }
                                mj1.getYina_imageView().addBottom(imageView1_lam);
                            }
                        });
                        Config.yuce.forEach(mj1 -> {
                            if (mj1.getYuce_direction() == d.getDirection() && (d.getHu() == 0 || Config.systemConfig.xueliuchenghe == 1) && mj1.getResult() == mj.getResult() && mj1.yuanpai == mj.yuanpai) {
                                int z = mj.getNapai_direction() - d.getDirection();
                                Image image_lam = Config.FANGPAO_YUCE;
                                ImageView imageView1_lam = new ImageView(ImageKit.rotate(image_lam, -90 * z));
                                imageView1_lam.setFitWidth(15.0);
                                imageView1_lam.setFitHeight(15.0);
                                imageView1_lam.setLayoutY(mj1.getYuce_imageView().getImageView().getFitHeight() - imageView1_lam.getFitHeight());
                                mj1.getYuce_imageView().addBottom(imageView1_lam);
                            }
                        });
                    });
                    if (Config.systemConfig.chisanfangpai == 1) {
                        yina = Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == d.getDirection() && mj1.getPaizhuantai() == 0 && mj1.getResult() < 30).collect(Collectors.toList());
                        for (Mj mj2 : yina) {
                            for (MjPanelInterface dc1 : directions) {
                                d1 = dc1.getDirection();
                                if (d1 == d.getDirection() || dc1.isQueren() || dc1.getHu() != 0 || dc1.getQue() == mj2.getResult() / 10 || !dc1.keChi(mj2, d1) || !MainController.ChiPaiKeTing(d1, mj2)) continue;
                                int y1 = d1 - d.getDirection();
                                image = Config.CHIFANGXIANG;
                                image = ImageKit.rotate(image, -90 * y1);
                                imageView1 = new ImageView(image);
                                if (Math.abs(-90 * y1) == 180) {
                                    imageView1.setFitWidth(10.0);
                                    imageView1.setFitHeight(mj2.getYina_imageView().getImageView().getFitWidth() / 2.0);
                                    imageView1.setY(-mj2.getYina_imageView().getImageView().getFitWidth() / 2.0);
                                } else {
                                    imageView1.setFitWidth(mj2.getYina_imageView().getImageView().getFitWidth());
                                    imageView1.setFitHeight(10.0);
                                }
                                mj2.getYina_imageView().addBottom(imageView1);
                            }
                        }
                    }
                }
                if (d.getDirection() == leftPanel.getDirection()) {
                    hupai.forEach(mj -> {
                        Config.yina.forEach(mj1 -> {
                            if (mj1.getNapai_direction() == d.getDirection() && mj1.getPaizhuantai() == 0 && (d.getHu() == 0 || Config.systemConfig.xueliuchenghe == 1) && mj1.getResult() == mj.getResult() && mj1.yuanpai == mj.yuanpai) {
                                Image image_lam = Config.FANGPAO;
                                int z = mj.getNapai_direction() - d.getDirection();
                                ImageView imageView1_lam = new ImageView();
                                if (Math.abs(-90 * z) == 180) {
                                    image_lam = ImageKit.rotate(image_lam, 90);
                                    imageView1_lam.setImage(image_lam);
                                    imageView1_lam.setFitHeight(10.0);
                                    imageView1_lam.setFitWidth(mj1.getYina_imageView().getImageView().getFitHeight() / 2.0);
                                } else {
                                    image_lam = ImageKit.rotate(image_lam, -90 + -90 * z);
                                    imageView1_lam.setImage(image_lam);
                                    imageView1_lam.setFitWidth(10.0);
                                    imageView1_lam.setFitHeight(mj1.getYina_imageView().getImageView().getFitHeight());
                                }
                                mj1.getYina_imageView().addBottom(imageView1_lam);
                            }
                        });
                        Config.yuce.forEach(mj1 -> {
                            if (mj1.getYuce_direction() == d.getDirection() && (d.getHu() == 0 || Config.systemConfig.xueliuchenghe == 1) && mj1.getResult() == mj.getResult() && mj1.yuanpai == mj.yuanpai) {
                                int z = mj.getNapai_direction() - d.getDirection();
                                Image image_lam = Config.FANGPAO_YUCE;
                                ImageView imageView1_lam = new ImageView();
                                if (Math.abs(-90 * z) == 180) {
                                    image_lam = ImageKit.rotate(image_lam, 90);
                                    imageView1_lam.setImage(image_lam);
                                } else {
                                    image_lam = ImageKit.rotate(image_lam, -90 + -90 * z);
                                    imageView1_lam.setImage(image_lam);
                                }
                                imageView1_lam.setFitWidth(15.0);
                                imageView1_lam.setFitHeight(15.0);
                                mj1.getYuce_imageView().addBottom(imageView1_lam);
                                imageView1_lam.setLayoutX(mj1.getYuce_imageView().getImageView().getFitWidth() - imageView1_lam.getFitWidth());
                            }
                        });
                    });
                    if (Config.systemConfig.chisanfangpai == 1) {
                        yina = Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == d.getDirection() && mj1.getPaizhuantai() == 0 && mj1.getResult() < 30).collect(Collectors.toList());
                        for (Mj mj2 : yina) {
                            for (MjPanelInterface dc1 : directions) {
                                d1 = dc1.getDirection();
                                if (d1 == d.getDirection() || dc1.isQueren() || dc1.getHu() != 0 || dc1.getQue() == mj2.getResult() / 10 || !dc1.keChi(mj2, d1) || !MainController.ChiPaiKeTing(d1, mj2)) continue;
                                x1 = d1 - d.getDirection();
                                image = Config.CHIFANGXIANG;
                                imageView1 = new ImageView();
                                if (Math.abs(90 * x1) == 180) {
                                    image = ImageKit.rotate(image, 90);
                                    imageView1.setImage(image);
                                    imageView1.setFitHeight(10.0);
                                    imageView1.setFitWidth(mj2.getYina_imageView().getImageView().getFitHeight() / 2.0);
                                } else {
                                    image = ImageKit.rotate(image, -90 + -90 * x1);
                                    imageView1.setImage(image);
                                    imageView1.setFitWidth(10.0);
                                    imageView1.setFitHeight(mj2.getYina_imageView().getImageView().getFitHeight());
                                }
                                mj2.getYina_imageView().addBottom(imageView1);
                            }
                        }
                    }
                }
            });
        }
        MainController.leftPanel.yinaPanel.getParent().layout();
        MainController.rightPanel.yinaPanel.getParent().layout();
        MainController.bottomPanel.yinaPanel.getParent().layout();
        MainController.topPanel.yinaPanel.getParent().layout();
        MainController.leftPanel.yinaPanel.getParent().getParent().layout();
        if (kaiqiai == 1 && dapaifangx == zhihuifangxiang) {
            Mj mj4 = ai.tuijian();
            try {
                Robot robot = new Robot();
                robot.mouseMove((int)(JFXKit.getScreenX(mj4.getYina_imageView().getImageView()) + mj4.getYina_imageView().getImageView().getFitWidth() / 2.0), (int)(JFXKit.getScreenY(mj4.getYina_imageView().getImageView()) + mj4.getYina_imageView().getImageView().getFitHeight() / 2.0));
            }
            catch (AWTException e) {
                e.printStackTrace();
            }
        }
        if (static_main_panel.getChildren().contains(yuceYinaPanel)) {
            List<Mj> mjList = new ArrayList<Mj>();
            mjList.addAll(Config.yina);
            mjList.addAll(Config.yuce);
            yuceYinaPanel.setMjs(mjList);
            yuceYinaPanel.display();
        }
        Config.yina.forEach(mj -> {
            if (Config.huanhoupai_yixuan.contains(mj)) {
                mj.getYina_imageView().setImage(ImageKit.coverImage(mj.getYina_imageView().getImage(), Config.LVSE));
            }
            if (Config.jiuzhengpai_yixuan.contains(mj)) {
                System.out.println("\u5c55\u793a\u7ea0\u6b63\u7684\u6362\u724c");
                mj.getYina_imageView().setImage(ImageKit.coverImage(mj.getYina_imageView().getImage(), Config.HONGSE));
            }
        });
    }

    private static boolean ChiPaiKeTing(int d, Mj mj) {
        ArrayList<Mj> jiance_temp;
        List<Mj> kechupai;
        Mj cmj;
        List<Mj> yina_jiance = Config.yina.stream().filter(mj1 -> mj1.getNapai_direction() == d).collect(Collectors.toList());
        yina_jiance.sort(new ComparatorMjResult());
        if (yina_jiance.stream().filter(mj1 -> mj1.getResult() - 1 == mj.getResult() && mj.getResult() / 10 == mj1.getResult() / 10).count() > 0L && yina_jiance.stream().filter(mj1 -> mj1.getResult() - 2 == mj.getResult() && mj.getResult() / 10 == mj1.getResult() / 10).count() > 0L) {
            List<Mj> jiances = new ArrayList<Mj>();
            yina_jiance.forEach(mj1 -> {
                Mj cmj_lam = (Mj)mj1.clone();
                if (mj1.getResult() - 1 == mj.getResult() && mj.getResult() / 10 == mj1.getResult() / 10 && mj1.getPaizhuantai() == 0 && jiances.stream().filter(mj2 -> mj2.getResult() == mj1.getResult() && mj2.getPaizhuantai() == 4).count() == 0L) {
                    cmj_lam.setPaizhuantai(4);
                }
                if (mj1.getResult() - 2 == mj.getResult() && mj.getResult() / 10 == mj1.getResult() / 10 && mj1.getPaizhuantai() == 0 && jiances.stream().filter(mj2 -> mj2.getResult() == mj1.getResult() && mj2.getPaizhuantai() == 4).count() == 0L) {
                    cmj_lam.setPaizhuantai(4);
                }
                jiances.add(cmj_lam);
            });
            cmj = (Mj)mj.clone();
            cmj.setPaizhuantai(4);
            jiances.add(cmj);
            kechupai = jiances.stream().filter(mj1 -> mj1.getPaizhuantai() == 0).collect(Collectors.toList());
            for (Mj mj12 : kechupai) {
                jiance_temp = new ArrayList<Mj>();
                jiance_temp.addAll(jiances);
                jiance_temp.remove(mj12);
                if (!MainController.hupaiyuce(jiance_temp)) continue;
                return true;
            }
        }
        if (yina_jiance.stream().filter(mj1 -> mj1.getResult() - 1 == mj.getResult() && mj.getResult() / 10 == mj1.getResult() / 10).count() > 0L && yina_jiance.stream().filter(mj1 -> mj1.getResult() + 1 == mj.getResult() && mj.getResult() / 10 == mj1.getResult() / 10).count() > 0L) {
            List<Mj> jiances = new ArrayList<Mj>();
            yina_jiance.forEach(mj1 -> {
                Mj cmj_lam = (Mj)mj1.clone();
                if (mj1.getResult() - 1 == mj.getResult() && mj.getResult() / 10 == mj1.getResult() / 10 && mj1.getPaizhuantai() == 0 && jiances.stream().filter(mj2 -> mj2.getResult() == mj1.getResult() && mj2.getPaizhuantai() == 4).count() == 0L) {
                    cmj_lam.setPaizhuantai(4);
                }
                if (mj1.getResult() + 1 == mj.getResult() && mj.getResult() / 10 == mj1.getResult() / 10 && mj1.getPaizhuantai() == 0 && jiances.stream().filter(mj2 -> mj2.getResult() == mj1.getResult() && mj2.getPaizhuantai() == 4).count() == 0L) {
                    cmj_lam.setPaizhuantai(4);
                }
                jiances.add(cmj_lam);
            });
            cmj = (Mj)mj.clone();
            cmj.setPaizhuantai(4);
            jiances.add(cmj);
            kechupai = jiances.stream().filter(mj1 -> mj1.getPaizhuantai() == 0).collect(Collectors.toList());
            for (Mj mj12 : kechupai) {
                jiance_temp = new ArrayList();
                jiance_temp.addAll(jiances);
                jiance_temp.remove(mj12);
                if (!MainController.hupaiyuce(jiance_temp)) continue;
                return true;
            }
        }
        if (yina_jiance.stream().filter(mj1 -> mj1.getResult() + 1 == mj.getResult() && mj.getResult() / 10 == mj1.getResult() / 10).count() > 0L && yina_jiance.stream().filter(mj1 -> mj1.getResult() + 1 == mj.getResult() && mj.getResult() / 10 == mj1.getResult() / 10).count() > 0L) {
            List<Mj> jiances = new ArrayList<Mj>();
            yina_jiance.forEach(mj1 -> {
                Mj cmj_lam = (Mj)mj1.clone();
                if (mj1.getResult() + 2 == mj.getResult() && mj.getResult() / 10 == mj1.getResult() / 10 && mj1.getPaizhuantai() == 0 && jiances.stream().filter(mj2 -> mj2.getResult() == mj1.getResult() && mj2.getPaizhuantai() == 4).count() == 0L) {
                    cmj_lam.setPaizhuantai(4);
                }
                if (mj1.getResult() + 1 == mj.getResult() && mj.getResult() / 10 == mj1.getResult() / 10 && mj1.getPaizhuantai() == 0 && jiances.stream().filter(mj2 -> mj2.getResult() == mj1.getResult() && mj2.getPaizhuantai() == 4).count() == 0L) {
                    cmj_lam.setPaizhuantai(4);
                }
                jiances.add(cmj_lam);
            });
            cmj = (Mj)mj.clone();
            cmj.setPaizhuantai(4);
            jiances.add(cmj);
            kechupai = jiances.stream().filter(mj1 -> mj1.getPaizhuantai() == 0).collect(Collectors.toList());
            for (Mj mj12 : kechupai) {
                jiance_temp = new ArrayList();
                jiance_temp.addAll(jiances);
                jiance_temp.remove(mj12);
                if (!MainController.hupaiyuce(jiance_temp)) continue;
                return true;
            }
        }
        return false;
    }

    private void doZhuoLoginImpl() {
        IMClientManager.getInstance().getBaseEventListener().setLoginOkForLaunchObserver(this.onLoginSucessObserver);
        new LocalUDPDataSender.SendLoginDataAsync(param.user.getUser_id() + "_" + "2", param.jwt, param.user.getUser_id() + ""){

            @Override
            protected void fireAfterSendLogin(int code) {
                if (code == 0) {
                    System.out.println("\u6570\u636e\u53d1\u9001\u6210\u529f\uff01");
                } else {
                    System.out.println("\u6570\u636e\u53d1\u9001\u5931\u8d25\u3002\u9519\u8bef\u7801\u662f\uff1a" + code + "\uff01");
                }
            }
        }.execute();
    }

    static {
        bottom_panel = new HBox();
        yuceYinaPanel = new YuceYinaPanel();
        yichu_pane = new FlowPane();
        kech_list_panel = new VBox();
        suiyigang_count = 0;
        zhuangjia = -1;
        dapaifangx = -1;
        meijuchongshehuapai = 1;
        meijuchongshecaisheng = 1;
        meijuchongshedanzhangkegang = 1;
        weina_catch = new LinkedList();
        zhihuifangxiang = 0;
        kaiqiai = 0;
        dafa = "\u8840\u6218";
        state = new LinkedList<Map<String, Object>>();
        caisheng = new ArrayList<Integer>();
        zixuanhuapai = new ArrayList<Integer>();
        weina = new LinkedList();
        directions = new LinkedList<MjPanelInterface>();
        directions_temp = new LinkedList<MjPanelInterface>();
        maima_map = new HashMap<Integer, List<Integer>>();
        List<Integer> maima_0 = new ArrayList<Integer>();
        maima_0.add(1);
        maima_0.add(5);
        maima_0.add(9);
        maima_map.put(0, maima_0);
        List<Integer> maima_1 = new ArrayList<Integer>();
        maima_1.add(2);
        maima_1.add(6);
        maima_map.put(1, maima_1);
        List<Integer> maima_2 = new ArrayList<Integer>();
        maima_2.add(3);
        maima_2.add(7);
        maima_map.put(2, maima_2);
        List<Integer> maima_3 = new ArrayList<Integer>();
        maima_3.add(5);
        maima_3.add(8);
        maima_map.put(3, maima_3);
    }

    public static final class ComparatorMjQueyimengResult
    implements Comparator {
        public int compare(Object obj0, Object obj1) {
            Mj user0 = (Mj)obj0;
            Mj user1 = (Mj)obj1;
            int r1 = user0.getResult();
            int r2 = user1.getResult();
            if (user0.caisheng == user1.caisheng) {
                if (user0.getIsque() == user1.getIsque()) {
                    return r1 - r2;
                }
                if (user0.getIsque()) {
                    return 1;
                }
                return -1;
            }
            if (user0.caisheng) {
                return -1;
            }
            return 1;
        }
    }

    public static final class ComparatorMjResult
    implements Comparator {
        public int compare(Object obj0, Object obj1) {
            double r;
            Mj user0 = (Mj)obj0;
            Mj user1 = (Mj)obj1;
            double r0 = user0.getResult();
            double r1 = user1.getResult();
            if (r0 == 56.0) {
                r0 = 10.0;
            }
            if (r1 == 56.0) {
                r1 = 10.0;
            }
            if (r0 == 57.0) {
                r0 = 30.0;
            }
            if (r1 == 57.0) {
                r1 = 30.0;
            }
            if (r0 == 58.0) {
                r0 = 20.0;
            }
            if (r1 == 58.0) {
                r1 = 20.0;
            }
            if (r0 == 52.0) {
                r0 = 38.0;
            }
            if (r1 == 52.0) {
                r1 = 38.0;
            }
            if (r0 == 53.0) {
                r0 = 34.5;
            }
            if (r1 == 53.0) {
                r1 = 34.5;
            }
            if ((r = r0 - r1) > 0.0) {
                return 1;
            }
            if (r < 0.0) {
                return -1;
            }
            return 0;
        }
    }

    public static final class ComparatorMj
    implements Comparator {
        public int compare(Object obj0, Object obj1) {
            Mj user0 = (Mj)obj0;
            Mj user1 = (Mj)obj1;
            return user0.getMahjong_number() - user1.getMahjong_number();
        }
    }

    public static final class ComparatorD
    implements Comparator {
        public int compare(Object obj0, Object obj1) {
            MjPanelInterface user0 = (MjPanelInterface)obj0;
            MjPanelInterface user1 = (MjPanelInterface)obj1;
            return user0.getDirection() - user1.getDirection();
        }
    }
}

