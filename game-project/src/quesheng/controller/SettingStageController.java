/*
 * Decompiled with CFR 0.152.
 */
package quesheng.controller;

import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import quesheng.controller.BaseController;
import quesheng.controller.StageController;
import quesheng.controller.intfc.ControlledStage;
import quesheng.model.MjConfig;
import quesheng.util.Config;

public class SettingStageController
extends BaseController
implements ControlledStage,
Initializable {
    @FXML
    RadioButton liangceng_radioBtn;
    @FXML
    CheckBox baisanceng_144;
    @FXML
    CheckBox danzhangdongzaihou;
    @FXML
    CheckBox zhucengmapai;
    @FXML
    RadioButton sanceng_radioBtn;
    @FXML
    CheckBox _136_sanceng_danzhangzaiqian;
    @FXML
    CheckBox baisiceng;
    @FXML
    ComboBox wanjiapaishu;
    @FXML
    ComboBox tiaopai;
    @FXML
    CheckBox shangxiangzhupai;
    @FXML
    CheckBox niezhuapai;
    @FXML
    ComboBox huanshoupai;
    @FXML
    CheckBox guoshanchediaohuan;
    @FXML
    CheckBox zhuapaihouxuanzhuang;
    @FXML
    CheckBox sanceng_liangdongsizhang;
    @FXML
    CheckBox errenzhuaerfangweipai;
    @FXML
    CheckBox qutouzhang;
    @FXML
    CheckBox xianzhuan8houzhuan4;
    @FXML
    CheckBox meijuchongxuanquefang;
    @FXML
    CheckBox disancengzuowanjiapai;
    @FXML
    CheckBox meijushetiaopai;
    @FXML
    CheckBox bugoudongyizhiweibu;
    @FXML
    CheckBox _20zhangzhuangjiaxianzhuangsanzhangzaizhuayizhang;
    @FXML
    CheckBox butiaopaizhangjialianxu14xian13;
    @FXML
    CheckBox zhuapai_123;
    @FXML
    CheckBox zhuapai321;
    @FXML
    ComboBox pingna;
    @FXML
    CheckBox niezhuanimo;
    @FXML
    CheckBox nanbeihuhuandongdexiajiashibei;
    @FXML
    CheckBox zijiazuosandunyousandun;
    @FXML
    CheckBox sicengsandun;
    @FXML
    CheckBox qishourenyiliangdong;
    @FXML
    CheckBox renyiandongnapai;
    @FXML
    ComboBox meishouzhangshu;
    @FXML
    Spinner yucepai;
    @FXML
    ComboBox wanjiarenshu;
    @FXML
    ComboBox gangfa;
    @FXML
    CheckBox zixuanhuapai;
    @FXML
    CheckBox anhuiganghua;
    @FXML
    CheckBox shoupaibuxianshihuapai;
    @FXML
    CheckBox yucepaibuxianshihuapai;
    @FXML
    CheckBox anqingmajiangdahua;
    @FXML
    CheckBox huapaikepang_foushanmajiang;
    @FXML
    CheckBox huapaidaiticaishengpai_foushanmajiang;
    @FXML
    ComboBox ganghua;
    @FXML
    CheckBox diyishou;
    @FXML
    CheckBox diershou;
    @FXML
    CheckBox disanshou;
    @FXML
    CheckBox zixuankoupai;
    @FXML
    CheckBox zuihouyizhang;
    @FXML
    ComboBox jiemianbuju;
    @FXML
    ColorPicker zidingyibeijingyanse;
    @FXML
    CheckBox shengyingtishi;
    @FXML
    ComboBox chupaixianshi;
    @FXML
    CheckBox zidongjietu;
    @FXML
    CheckBox jianpancaozuomoshi;
    @FXML
    CheckBox zhuomianbuxuanzhuan;
    @FXML
    CheckBox pangpai;
    @FXML
    CheckBox chipai;
    @FXML
    CheckBox baoliupai;
    @FXML
    CheckBox danzhuangkechi;
    @FXML
    CheckBox chisanfangpai;
    @FXML
    CheckBox gangshouzhongpai;
    @FXML
    CheckBox angang;
    @FXML
    CheckBox minggang;
    @FXML
    CheckBox danzhangkegang;
    @FXML
    CheckBox fengpaizipaisanzhangkegang;
    @FXML
    CheckBox koupai;
    @FXML
    CheckBox dongnanxibeikechikegang;
    @FXML
    CheckBox zipaifengkai;
    @FXML
    CheckBox renyisangedanzhangzipai;
    @FXML
    CheckBox leqing;
    @FXML
    CheckBox dongnanxibeizhongfabaigang;
    @FXML
    CheckBox dongnanxibeizhongfabaigangqingdao;
    @FXML
    CheckBox dongnanxibeizhongfabaishunzi;
    @FXML
    CheckBox pangjiang;
    @FXML
    CheckBox yaojiyitongdangfengpai;
    @FXML
    CheckBox yaojiyitongdangzipai;
    @FXML
    CheckBox yaojiyitongdangzipaifengpai;
    @FXML
    CheckBox rengyijiu;
    @FXML
    CheckBox wugezipaigang;
    @FXML
    CheckBox yaojiugang;
    @FXML
    CheckBox yaojiugangshengyangbusangehuosige;
    @FXML
    CheckBox zhucengnapai;
    @FXML
    CheckBox yingchuanbufeng;
    @FXML
    ComboBox gangpaishunxu;
    @FXML
    CheckBox zhixianzimo;
    @FXML
    CheckBox qiangganghu;
    @FXML
    CheckBox erwubajiang;
    @FXML
    CheckBox queyimeng;
    @FXML
    CheckBox xueliuchenghe;
    @FXML
    CheckBox hupaikepang;
    @FXML
    CheckBox hupaikegang;
    @FXML
    CheckBox fangpaoxiajia;
    @FXML
    CheckBox yipaoduoxiang;
    @FXML
    CheckBox hupaikemo;
    @FXML
    CheckBox buchibupangdapaitishi;
    @FXML
    CheckBox lanpai;
    @FXML
    CheckBox xiaoqidui;
    @FXML
    CheckBox bixuyou19zipaifengpairenyiyizhang;
    @FXML
    CheckBox pengpaihuozhezhongfabaidui;
    @FXML
    CheckBox zixuancaisheng;
    @FXML
    CheckBox houcaisheng;
    @FXML
    CheckBox shunmocaisheng;
    @FXML
    CheckBox renyicaisheng;
    @FXML
    CheckBox shengjicaisheng;
    @FXML
    CheckBox shuangcaisheng;
    @FXML
    ComboBox shangcaisheng_combox;
    @FXML
    CheckBox zhongjianpaizuohua;
    @FXML
    CheckBox shengjicaishengshihuapaijixu;
    @FXML
    CheckBox daiticaishengpai;
    @FXML
    ComboBox daiticaishengpai_combox;
    @FXML
    CheckBox daitipaibaoliuyuanpaizuoyong;
    @FXML
    CheckBox andaitipaixianshi;
    @FXML
    CheckBox caishenganzhengchangpaichanyuchangpanggang;
    @FXML
    CheckBox budaiticaishengzipai;
    @FXML
    CheckBox buhuahoumocaisheng;
    @FXML
    CheckBox caishengcanyuchipanggang;
    @FXML
    CheckBox caishengkedangang;
    @FXML
    CheckBox jiaohuanpai;
    @FXML
    CheckBox fankaidcaishengpaidanghua;
    @FXML
    CheckBox fankaidcaishengpaifangzuihouweizhi;
    @FXML
    CheckBox fankaishihuapaijixufan;
    @FXML
    CheckBox zhuapaihouxuancaisheng;
    @FXML
    CheckBox zipaifengpaifenkaishengji;
    @FXML
    CheckBox fankaibaibanshenghuahuashengdong;
    @FXML
    CheckBox juedicaisheng;
    @FXML
    CheckBox fankaidepaibuzuocaisheng;
    @FXML
    ComboBox huoqushuju;
    @FXML
    ComboBox guopai;
    @FXML
    ComboBox xunhuanguopai;
    @FXML
    ComboBox guodong;
    @FXML
    ComboBox kanchupai;
    @FXML
    ComboBox mopai;
    @FXML
    ComboBox chipai_kjj;
    @FXML
    ComboBox gangpai;
    @FXML
    ComboBox pangpai_kjj;
    @FXML
    ComboBox chexiao;
    @FXML
    ComboBox chongxuanzhuang;
    @FXML
    ComboBox shubiaoyouji;
    @FXML
    CheckBox leqingmajiang;
    @FXML
    CheckBox guangxi152;
    @FXML
    CheckBox xianmajiang;
    @FXML
    CheckBox yuhuanwahua;
    @FXML
    CheckBox wenchengmajiang;
    @FXML
    CheckBox wenlingmajiang;
    @FXML
    CheckBox xianmaimahoudingzhuang;
    private static ObservableList<Label> keys = FXCollections.observableArrayList();
    StageController myController;

    @Override
    public void setStageController(StageController stageController) {
        this.myController = stageController;
        this.myController.getStage("SettingView").setOnShown(event -> {});
        this.myController.getStage("SettingView").setOnCloseRequest(event -> {});
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ToggleGroup group = new ToggleGroup();
        this.liangceng_radioBtn.setToggleGroup(group);
        this.liangceng_radioBtn.setUserData(2);
        this.sanceng_radioBtn.setToggleGroup(group);
        this.sanceng_radioBtn.setUserData(3);
        group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (group.getSelectedToggle() != null) {
                Config.systemConfig.mapaicengshu = (Integer)group.getSelectedToggle().getUserData();
                if (Config.systemConfig.mapaicengshu == 2) {
                    Config.systemConfig.mapaifangshi = 0;
                }
                if (Config.systemConfig.mapaicengshu == 3) {
                    Config.systemConfig.mapaifangshi = 1;
                }
            }
        });
        switch (Config.systemConfig.mapaifangshi) {
            case 0: {
                this.liangceng_radioBtn.fire();
                break;
            }
            case 4: {
                this._136_sanceng_danzhangzaiqian.setSelected(true);
                this._136_sanceng_danzhangzaiqian.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue.booleanValue()) {
                        Config.systemConfig.mapaifangshi = 4;
                        this.sanceng_radioBtn.fire();
                    } else {
                        Config.systemConfig.mapaifangshi = 0;
                        this.liangceng_radioBtn.fire();
                    }
                });
                this.sanceng_radioBtn.fire();
                break;
            }
            case 5: {
                this.baisanceng_144.setSelected(true);
                this.baisanceng_144.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue.booleanValue()) {
                        Config.systemConfig.mapaifangshi = 5;
                        this.sanceng_radioBtn.fire();
                    } else {
                        Config.systemConfig.mapaifangshi = 0;
                        this.liangceng_radioBtn.fire();
                    }
                });
                this.sanceng_radioBtn.fire();
                break;
            }
            case 1: {
                this.sanceng_radioBtn.fire();
            }
        }
        this.wanjiapaishu.getItems().addAll(4, 7, 10, 13, 16, 19, 22);
        this.wanjiapaishu.setValue(Config.systemConfig.wanjiapaishu);
        this.wanjiapaishu.valueProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.wanjiapaishu = (Integer)newValue;
        });
        this.huanshoupai.getItems().addAll(0, 1, 2, 3, 4);
        this.huanshoupai.setValue(Config.systemConfig.huanshoupai);
        this.huanshoupai.valueProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.huanshoupai = (int)((Integer)newValue);
        });
        this.tiaopai.getItems().addAll("\u5e73\u8df3", "\u9694\u8df3", "\u4e0a\u4e0b\u8df3", "\u4e0d\u8df3\u724c", "\u5e84\u5bb6\u8fde\u7eed\u4e09\u5f20", "\u6bcf\u5bb6\u8fde\u7eed\u4e24\u5f20");
        this.tiaopai.setValue(Config.systemConfig.tiaopai);
        this.tiaopai.valueProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.tiaopai = newValue.toString();
        });
        this.meishouzhangshu.getItems().addAll(2, 4, 6, 12);
        this.meishouzhangshu.setValue(Config.systemConfig.meishouzhangshu);
        this.meishouzhangshu.valueProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.meishouzhangshu = (int)((Integer)newValue);
        });
        this.yucepai.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50, 10));
        this.yucepai.valueProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.yucepai = (int)((Integer)newValue);
        });
        this.wanjiarenshu.getItems().addAll(2, 3, 4);
        this.wanjiarenshu.setValue(Config.systemConfig.wanjiarenshu);
        this.wanjiarenshu.valueProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.wanjiarenshu = (int)((Integer)newValue);
        });
        if (Config.systemConfig.zixuankoupai == 1) {
            this.zixuankoupai.setSelected(true);
        }
        this.zixuankoupai.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.zixuankoupai = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.zuihouyizhang == 1) {
            this.zuihouyizhang.setSelected(true);
        }
        this.zuihouyizhang.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.zuihouyizhang = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.disancengzuowanjiapai == 1) {
            this.disancengzuowanjiapai.setSelected(true);
        }
        this.disancengzuowanjiapai.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.disancengzuowanjiapai = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.baoliupai == 1) {
            this.baoliupai.setSelected(true);
        }
        this.baoliupai.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.baoliupai = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.chisanfangpai == 1) {
            this.chisanfangpai.setSelected(true);
        }
        this.chisanfangpai.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.chisanfangpai = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.zijiazuosandunyousandun == 1) {
            this.zijiazuosandunyousandun.setSelected(true);
        }
        this.zijiazuosandunyousandun.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.zijiazuosandunyousandun = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.sicengsandun == 1) {
            this.sicengsandun.setSelected(true);
        }
        this.sicengsandun.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.sicengsandun = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.meijushetiaopai == 1) {
            this.meijushetiaopai.setSelected(true);
        }
        this.meijushetiaopai.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.meijushetiaopai = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.niezhuanimo == 1) {
            this.niezhuanimo.setSelected(true);
        }
        this.niezhuanimo.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.niezhuanimo = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.qishourenyiliangdong == 1) {
            this.qishourenyiliangdong.setSelected(true);
        }
        this.qishourenyiliangdong.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.qishourenyiliangdong = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.hupaikemo == 1) {
            this.hupaikemo.setSelected(true);
        }
        this.hupaikemo.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.hupaikemo = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.dongnanxibeizhongfabaigang == 1) {
            this.dongnanxibeizhongfabaigang.setSelected(true);
        }
        this.dongnanxibeizhongfabaigang.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.dongnanxibeizhongfabaigang = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.fengpaizipaisanzhangkegang == 1) {
            this.fengpaizipaisanzhangkegang.setSelected(true);
        }
        this.fengpaizipaisanzhangkegang.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.fengpaizipaisanzhangkegang = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.guangxi152 == 1) {
            this.guangxi152.setSelected(true);
        }
        this.guangxi152.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.guangxi152 = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.leqing == 1) {
            this.leqing.setSelected(true);
        }
        this.leqing.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.leqing = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.dongnanxibeizhongfabaigangqingdao == 1) {
            this.dongnanxibeizhongfabaigangqingdao.setSelected(true);
        }
        this.dongnanxibeizhongfabaigangqingdao.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.dongnanxibeizhongfabaigangqingdao = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.dongnanxibeizhongfabaishunzi == 1) {
            this.dongnanxibeizhongfabaishunzi.setSelected(true);
        }
        this.dongnanxibeizhongfabaishunzi.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.dongnanxibeizhongfabaishunzi = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.niezhuapai == 1) {
            this.niezhuapai.setSelected(true);
        }
        this.niezhuapai.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.niezhuapai = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.caishengkedangang == 1) {
            this.caishengkedangang.setSelected(true);
        }
        this.caishengkedangang.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.caishengkedangang = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.zhuapaihouxuanzhuang == 1) {
            this.zhuapaihouxuanzhuang.setSelected(true);
        }
        this.zhuapaihouxuanzhuang.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.zhuapaihouxuanzhuang = newValue != false ? Integer.valueOf(1) : Integer.valueOf(0);
        });
        if (Config.systemConfig.guoshanchediaohuan == 1) {
            this.guoshanchediaohuan.setSelected(true);
        }
        this.guoshanchediaohuan.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.guoshanchediaohuan = newValue != false ? Integer.valueOf(1) : Integer.valueOf(0);
        });
        if (Config.systemConfig.butiaopaizhangjialianxu14xian13 == 1) {
            this.butiaopaizhangjialianxu14xian13.setSelected(true);
        }
        this.butiaopaizhangjialianxu14xian13.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.butiaopaizhangjialianxu14xian13 = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.zhuapai123 == 1) {
            this.zhuapai_123.setSelected(true);
        }
        this.zhuapai_123.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.zhuapai123 = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.hupaikepang == 1) {
            this.hupaikepang.setSelected(true);
        }
        this.hupaikepang.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.hupaikepang = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.hupaikegang == 1) {
            this.hupaikegang.setSelected(true);
        }
        this.hupaikegang.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.hupaikegang = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.zhuapai321 == 1) {
            this.zhuapai321.setSelected(true);
        }
        this.zhuapai321.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.zhuapai321 = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.yaojiyitongdangzipaifengpai == 1) {
            this.yaojiyitongdangzipaifengpai.setSelected(true);
        }
        this.yaojiyitongdangzipaifengpai.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.yaojiyitongdangzipaifengpai = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.huapaidaiticaishengpai_foushanmajiang == 1) {
            this.huapaidaiticaishengpai_foushanmajiang.setSelected(true);
        }
        this.huapaidaiticaishengpai_foushanmajiang.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.huapaidaiticaishengpai_foushanmajiang = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.yucepaibuxianshihuapai == 1) {
            this.yucepaibuxianshihuapai.setSelected(true);
        }
        this.yucepaibuxianshihuapai.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.yucepaibuxianshihuapai = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.huapaikepang_foushanmajiang == 1) {
            this.huapaikepang_foushanmajiang.setSelected(true);
        }
        this.huapaikepang_foushanmajiang.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.huapaikepang_foushanmajiang = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.zixuanhuapai == 1) {
            this.zixuanhuapai.setSelected(true);
        }
        this.zixuanhuapai.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.zixuanhuapai = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.xianzhuan8houzhuan4 == 1) {
            this.xianzhuan8houzhuan4.setSelected(true);
        }
        this.xianzhuan8houzhuan4.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.xianzhuan8houzhuan4 = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.meijuchongxuanquefang == 1) {
            this.meijuchongxuanquefang.setSelected(true);
        }
        this.meijuchongxuanquefang.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.meijuchongxuanquefang = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.rengyijiu == 1) {
            this.rengyijiu.setSelected(true);
        }
        this.rengyijiu.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.rengyijiu = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.xueliuchenghe == 1) {
            this.xueliuchenghe.setSelected(true);
        }
        this.xueliuchenghe.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.xueliuchenghe = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.pangpai == 1) {
            this.pangpai.setSelected(true);
        }
        this.pangpai.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.pangpai = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.yaojiyitongdangzipai == 1) {
            this.yaojiyitongdangzipai.setSelected(true);
        }
        this.yaojiyitongdangzipai.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.yaojiyitongdangzipai = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.gangshouzhongpai == 1) {
            this.gangshouzhongpai.setSelected(true);
        }
        this.gangshouzhongpai.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.gangshouzhongpai = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.angang == 1) {
            this.angang.setSelected(true);
        }
        this.angang.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.angang = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.minggang == 1) {
            this.minggang.setSelected(true);
        }
        this.minggang.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.minggang = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.danzhangkegang == 1) {
            this.danzhangkegang.setSelected(true);
        }
        this.danzhangkegang.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.danzhangkegang = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.renyiandongnapai == 1) {
            this.renyiandongnapai.setSelected(true);
        }
        this.renyiandongnapai.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.renyiandongnapai = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.zipaifengpaifenkaishengji == 1) {
            this.zipaifengpaifenkaishengji.setSelected(true);
        }
        this.zipaifengpaifenkaishengji.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.zipaifengpaifenkaishengji = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.fankaibaibanshenghuahuashengdong == 1) {
            this.fankaibaibanshenghuahuashengdong.setSelected(true);
        }
        this.fankaibaibanshenghuahuashengdong.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.fankaibaibanshenghuahuashengdong = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.juedicaisheng == 1) {
            this.juedicaisheng.setSelected(true);
        }
        this.juedicaisheng.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.juedicaisheng = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.fankaidepaibuzuocaisheng == 1) {
            this.fankaidepaibuzuocaisheng.setSelected(true);
        }
        this.fankaidepaibuzuocaisheng.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.fankaidepaibuzuocaisheng = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.xiaoqidui == 1) {
            this.xiaoqidui.setSelected(true);
        }
        this.xiaoqidui.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.xiaoqidui = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.bixuyou19zipaifengpairenyiyizhang == 1) {
            this.bixuyou19zipaifengpairenyiyizhang.setSelected(true);
        }
        this.bixuyou19zipaifengpairenyiyizhang.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.bixuyou19zipaifengpairenyiyizhang = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.pengpaihuozhezhongfabaidui == 1) {
            this.pengpaihuozhezhongfabaidui.setSelected(true);
        }
        this.pengpaihuozhezhongfabaidui.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.pengpaihuozhezhongfabaidui = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.qiangganghu == 1) {
            this.qiangganghu.setSelected(true);
        }
        this.qiangganghu.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.qiangganghu = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.queyimeng == 1) {
            this.queyimeng.setSelected(true);
        }
        this.queyimeng.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.queyimeng = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.yipaoduoxiang == 1) {
            this.yipaoduoxiang.setSelected(true);
        }
        this.yipaoduoxiang.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.yipaoduoxiang = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.yaojiugang == 1) {
            this.yaojiugang.setSelected(true);
        }
        this.yaojiugang.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.yaojiugang = newValue != false ? 1 : 0;
        });
        this.gangpaishunxu.getItems().addAll("\u540e\u6760\u724c", "\u987a\u6760\u724c", "\u968f\u610f\u6760");
        this.gangpaishunxu.setValue(Config.systemConfig.gangpaishunxu);
        this.gangpaishunxu.valueProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.gangpaishunxu = newValue.toString();
        });
        this.ganghua.getItems().addAll("\u540e\u6760\u724c", "\u987a\u6760\u724c", "\u968f\u610f\u6760");
        this.ganghua.setValue(Config.systemConfig.ganghua);
        this.ganghua.valueProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.ganghua = newValue.toString();
        });
        this.gangfa.getItems().addAll("\u5404\u65b9\u6760\u5b8c\u518d\u6253\u724c,\u8865\u8d77\u82b1\u724c\u7acb\u5373\u6760", "\u5404\u65b9\u6760\u5b8c\u518d\u6253\u724c,\u8865\u8d77\u82b1\u724c\u4e0b\u8f6e\u6760", "\u8f6e\u5230\u53ef\u5148\u8865\u82b1\u518d\u5403\u78b0,\u5403\u78b0\u540e\u7acb\u5373\u6760", "\u8f6e\u5230\u81ea\u7531\u6760(\u624b\u52a8\u6760\u82b1)");
        this.gangfa.setValue(Config.systemConfig.gangfa);
        this.gangfa.valueProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.gangfa = newValue.toString();
        });
        this.chupaixianshi.getItems().addAll("\u51fa\u724c\u987a\u5e8f", "\u82b1\u8272");
        switch (Config.systemConfig.chupaixianshi) {
            case 0: {
                this.chupaixianshi.setValue("\u51fa\u724c\u987a\u5e8f");
                break;
            }
            case 1: {
                this.chupaixianshi.setValue("\u82b1\u8272");
            }
        }
        this.chupaixianshi.valueProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue.toString()) {
                case "\u51fa\u724c\u987a\u5e8f": {
                    Config.systemConfig.chupaixianshi = 0;
                    break;
                }
                case "\u82b1\u8272": {
                    Config.systemConfig.chupaixianshi = 1;
                }
            }
        });
        this.shubiaoyouji.getItems().addAll("\u64a4\u9500", "\u6478\u724c");
        this.shubiaoyouji.setValue(Config.systemConfig.shubiaoyouji);
        this.shubiaoyouji.valueProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.shubiaoyouji = newValue.toString();
        });
        this.zidingyibeijingyanse.setValue(Color.color(Config.systemConfig.zidingyibeijingyanse[0], Config.systemConfig.zidingyibeijingyanse[1], Config.systemConfig.zidingyibeijingyanse[2]));
        this.zidingyibeijingyanse.setOnAction(t -> {
            Config.systemConfig.zidingyibeijingyanse[0] = ((Color)this.zidingyibeijingyanse.getValue()).getRed();
            Config.systemConfig.zidingyibeijingyanse[1] = ((Color)this.zidingyibeijingyanse.getValue()).getGreen();
            Config.systemConfig.zidingyibeijingyanse[2] = ((Color)this.zidingyibeijingyanse.getValue()).getBlue();
        });
        if (Config.systemConfig.erwubajiang == 1) {
            this.erwubajiang.setSelected(true);
        }
        this.erwubajiang.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.erwubajiang = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.zixuancaisheng == 1) {
            this.zixuancaisheng.setSelected(true);
        }
        this.zixuancaisheng.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.zixuancaisheng = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.houcaisheng == 1) {
            this.houcaisheng.setSelected(true);
        }
        this.houcaisheng.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.houcaisheng = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.shunmocaisheng == 1) {
            this.shunmocaisheng.setSelected(true);
        }
        this.shunmocaisheng.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.shunmocaisheng = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.renyicaisheng == 1) {
            this.renyicaisheng.setSelected(true);
        }
        this.renyicaisheng.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.renyicaisheng = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.jiaohuanpai == 1) {
            this.jiaohuanpai.setSelected(true);
        }
        this.jiaohuanpai.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.jiaohuanpai = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.dongnanxibeikechikegang == 1) {
            this.dongnanxibeikechikegang.setSelected(true);
        }
        this.dongnanxibeikechikegang.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.dongnanxibeikechikegang = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.chipai == 1) {
            this.chipai.setSelected(true);
        }
        this.chipai.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.chipai = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.caishengcanyuchipanggang == 1) {
            this.caishengcanyuchipanggang.setSelected(true);
        }
        this.caishengcanyuchipanggang.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.caishengcanyuchipanggang = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.zhixianzimo == 1) {
            this.zhixianzimo.setSelected(true);
        }
        this.zhixianzimo.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.zhixianzimo = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.daitipaibaoliuyuanpaizuoyong == 1) {
            this.daitipaibaoliuyuanpaizuoyong.setSelected(true);
        }
        this.daitipaibaoliuyuanpaizuoyong.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.daitipaibaoliuyuanpaizuoyong = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.zhuapaihouxuancaisheng == 1) {
            this.zhuapaihouxuancaisheng.setSelected(true);
        }
        this.zhuapaihouxuancaisheng.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.zhuapaihouxuancaisheng = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.caishenganzhengchangpaichanyuchangpanggang == 1) {
            this.caishenganzhengchangpaichanyuchangpanggang.setSelected(true);
        }
        this.caishenganzhengchangpaichanyuchangpanggang.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.caishenganzhengchangpaichanyuchangpanggang = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.andaitipaixianshi == 1) {
            this.andaitipaixianshi.setSelected(true);
        }
        this.andaitipaixianshi.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.andaitipaixianshi = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.lanpai == 1) {
            this.lanpai.setSelected(true);
        }
        this.lanpai.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.lanpai = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.daiticaishengpai == 1) {
            this.daiticaishengpai.setSelected(true);
        }
        this.daiticaishengpai.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.daiticaishengpai = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.shengjicaisheng == 1) {
            this.shengjicaisheng.setSelected(true);
        }
        this.shengjicaisheng.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.shengjicaisheng = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.xianmaimahoudingzhuang == 1) {
            this.xianmaimahoudingzhuang.setSelected(true);
        }
        this.xianmaimahoudingzhuang.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.xianmaimahoudingzhuang = newValue != false ? 1 : 0;
        });
        if (Config.systemConfig.shuangcaisheng == 1) {
            this.shuangcaisheng.setSelected(true);
        }
        this.shuangcaisheng.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.shuangcaisheng = newValue != false ? 1 : 0;
        });
        this.shangcaisheng_combox.setItems(keys);
        if (Config.systemConfig.shangcaisheng_combox > 0) {
            keys.forEach(label -> {
                if ((Integer)label.getUserData() == Config.systemConfig.shangcaisheng_combox) {
                    this.shangcaisheng_combox.setValue(label);
                }
            });
        }
        this.shangcaisheng_combox.valueProperty().addListener((observable, oldValue, newValue) -> {
            Label label = (Label)newValue;
            Config.systemConfig.shangcaisheng_combox = (Integer)label.getUserData();
        });
        this.daiticaishengpai_combox.setItems(keys);
        if (Config.systemConfig.daiticaishengpai_combox > 0) {
            keys.forEach(label -> {
                if ((Integer)label.getUserData() == Config.systemConfig.daiticaishengpai_combox) {
                    this.daiticaishengpai_combox.setValue(label);
                }
            });
        }
        this.daiticaishengpai_combox.valueProperty().addListener((observable, oldValue, newValue) -> {
            Label label = (Label)newValue;
            Config.systemConfig.daiticaishengpai_combox = (Integer)label.getUserData();
        });
        ObservableList<String> keycodes = FXCollections.observableArrayList();
        keycodes.addAll((Collection<String>)Config.getKey2KeycodeMapping().keySet());
        this.guopai.getItems().addAll(keycodes);
        this.guopai.setValue(Config.systemConfig.guopai);
        this.guopai.valueProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.guopai = newValue.toString();
        });
        this.xunhuanguopai.getItems().addAll(keycodes);
        this.xunhuanguopai.setValue(Config.systemConfig.xunhuanguopai);
        this.xunhuanguopai.valueProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.xunhuanguopai = newValue.toString();
        });
        this.guodong.getItems().addAll(keycodes);
        this.guodong.setValue(Config.systemConfig.guodong);
        this.guodong.valueProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.guodong = newValue.toString();
        });
        this.mopai.getItems().addAll(keycodes);
        this.mopai.setValue(Config.systemConfig.mopai);
        this.mopai.valueProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.mopai = newValue.toString();
        });
        this.chexiao.getItems().addAll(keycodes);
        this.chexiao.setValue(Config.systemConfig.chexiao);
        this.chexiao.valueProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.chexiao = newValue.toString();
        });
        this.gangpai.getItems().addAll(keycodes);
        this.gangpai.setValue(Config.systemConfig.gangpai);
        this.gangpai.valueProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.gangpai = newValue.toString();
        });
        this.pangpai_kjj.getItems().addAll(keycodes);
        this.pangpai_kjj.setValue(Config.systemConfig.pangpai_kjj);
        this.pangpai_kjj.valueProperty().addListener((observable, oldValue, newValue) -> {
            Config.systemConfig.pangpai_kjj = newValue.toString();
        });
    }

    public static void init2() {
        for (MjConfig mjConfig : Config.mjConfigs) {
            Label label = new Label(mjConfig.getName());
            label.setUserData(mjConfig.getNumber());
            label.setStyle("-fx-text-fill: black");
            keys.add(label);
        }
    }
}

