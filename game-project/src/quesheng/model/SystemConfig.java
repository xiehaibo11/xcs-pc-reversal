/*
 * Decompiled with CFR 0.152.
 */
package quesheng.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SystemConfig
implements Serializable {
    private static final long serialVersionUID = -6284005833985478852L;
    private Map<String, String> users = new LinkedHashMap<String, String>();
    private String choseIp;
    private Boolean isFirstLogin;
    public int mapaicengshu = 2;
    public int mapaifangshi = 0;
    public int wanjiapaishu = 13;
    public String tiaopai = "\u9694\u8df3";
    public Integer meishouzhangshu = 4;
    public Integer yucepai = 10;
    public Integer wanjiarenshu = 4;
    public Integer huanshoupai = 0;
    public Integer guoshanchediaohuan = 0;
    public Integer zhuapaihouxuanzhuang = 0;
    public int sanceng_liangdongsizhang = 0;
    public int errenzhuaerfangweipai = 0;
    public int qutouzhang = 0;
    public int xianzhuan8houzhuan4 = 0;
    public int meijuchongxuanquefang = 0;
    public int disancengzuowanjiapai = 0;
    public int meijushetiaopai = 0;
    public int bugoudongyizhiweibu = 0;
    public int _20zhangzhuangjiaxianzhuangsanzhangzaizhuayizhang = 0;
    public int butiaopaizhangjialianxu14xian13 = 0;
    public int zhuapai123 = 0;
    public int zhuapai321 = 0;
    public int pingna = 0;
    public int niezhuanimo = 0;
    public int nanbeihuhuandongdexiajiashibei = 0;
    public int zijiazuosandunyousandun = 0;
    public int sicengsandun = 0;
    public int qishourenyiliangdong = 0;
    public int renyiandongnapai = 0;
    public String gangfa = "\u8f6e\u5230\u81ea\u7531\u6760(\u624b\u52a8\u6760\u82b1)";
    public int zixuanhuapai = 0;
    public int anhuiganghua = 0;
    public int shoupaibuxianshihuapai = 0;
    public int yucepaibuxianshihuapai = 0;
    public int anqingmajiangdahua = 0;
    public int huapaikepang_foushanmajiang = 0;
    public int huapaidaiticaishengpai_foushanmajiang = 0;
    public String ganghua = "\u987a\u6760\u724c";
    public int diyishou = 0;
    public int diershou = 0;
    public int disanshou = 0;
    public int zuihouyizhang = 0;
    public int zixuankoupai = 1;
    public int jiemianbuju = 0;
    public double[] zidingyibeijingyanse = new double[]{0.0, 0.0, 0.0};
    public int shengyingtishi = 0;
    public int chupaixianshi = 0;
    public int zidongjietu = 0;
    public int mimabaohu = 0;
    public int jianpancaozuomoshi = 0;
    public int zhuomianbuxuanzhuan = 0;
    public int pangpai = 1;
    public int chipai = 0;
    public int baoliupai = 0;
    public int danzhuangkechi = 0;
    public int chisanfangpai = 0;
    public int gangshouzhongpai = 1;
    public int fengpaizipaisanzhangkegang = 0;
    public int angang = 1;
    public int minggang = 1;
    public int danzhangkegang = 0;
    public List<Integer> danzhangkegang_pai = new ArrayList<Integer>();
    public int koupai = 0;
    public int dongnanxibeikechikegang = 0;
    public int zipaifengkai = 0;
    public int renyisangedanzhangzipai = 0;
    public int leqing = 0;
    public int dongnanxibeizhongfabaigang = 0;
    public int dongnanxibeizhongfabaigangqingdao = 0;
    public int dongnanxibeizhongfabaishunzi = 0;
    public int pangjiang = 0;
    public int yaojiyitongdangfengpai = 0;
    public int rengyijiu = 0;
    public int yaojiyitongdangzipai = 0;
    public int yaojiyitongdangzipaifengpai = 0;
    public int wugezipaigang = 0;
    public int yaojiugangsangebubusigebuyige = 0;
    public int yaojiugang = 0;
    public int yaojiugangshengyangbusangehuosige = 0;
    public int zhucengnapai = 0;
    public int niezhuapai = 0;
    public int yingchuanbufeng = 0;
    public String gangpaishunxu = "\u987a\u6760\u724c";
    public int zhixianzimo = 0;
    public int qiangganghu = 1;
    public int erwubajiang = 0;
    public int queyimeng = 0;
    public int xueliuchenghe = 0;
    public int hupaikepang = 0;
    public int hupaikegang = 0;
    public int fangpaoxiajia = 0;
    public int yipaoduoxiang = 1;
    public int hupaikemo = 0;
    public int buchibupangdapaitishi = 0;
    public int lanpai = 0;
    public int xiaoqidui = 1;
    public int bixuyou19zipaifengpairenyiyizhang = 0;
    public int pengpaihuozhezhongfabaidui = 0;
    public int zixuancaisheng = 0;
    public int houcaisheng = 0;
    public int shunmocaisheng = 0;
    public int renyicaisheng = 0;
    public int shengjicaisheng = 0;
    public int shuangcaisheng = 0;
    public int shangcaisheng_combox = 0;
    public int zhongjianpaizuohua = 0;
    public int shengjicaishengshihuapaijixu = 0;
    public int daiticaishengpai = 0;
    public int daiticaishengpai_combox = 0;
    public int daitipaibaoliuyuanpaizuoyong = 0;
    public int caishenganzhengchangpaichanyuchangpanggang = 0;
    public int andaitipaixianshi = 0;
    public int budaiticaishengzipai = 0;
    public int buhuahoumocaisheng = 0;
    public int caishengcanyuchipanggang = 0;
    public int caishengkedangang = 0;
    public int jiaohuanpai = 0;
    public int fankaidcaishengpaidanghua = 0;
    public int fankaidcaishengpaifangzuihouweizhi = 0;
    public int fankaishihuapaijixufan = 0;
    public int caishenganzhengchangpaicanyuchengpanggang = 0;
    public int zhuapaihouxuancaisheng = 0;
    public int zipaifengpaifenkaishengji = 0;
    public int fankaibaibanshenghuahuashengdong = 0;
    public int juedicaisheng = 0;
    public int fankaidepaibuzuocaisheng = 0;
    public String huoqushuju;
    public String guopai;
    public String xunhuanguopai;
    public String guodong;
    public String kanchupai;
    public String mopai = "\u7a7a\u683c\u952e";
    public String chipai_kjj;
    public String gangpai;
    public String pangpai_kjj;
    public String chexiao = "z";
    public String chongxuanzhuang;
    public String shubiaoyouji = "\u64a4\u9500";
    public int leqingmajiang;
    public int guangxi152;
    public int xianmajiang;
    public int yuhuanwahua;
    public int wenchengmajiang;
    public int wenlingmajiang;
    public int xianmaimahoudingzhuang = 0;

    public Map<String, String> getUsers() {
        return this.users;
    }

    public void setUsers(Map<String, String> users) {
        this.users = users;
    }

    public void setChoseIp(String choseIp) {
        this.choseIp = choseIp;
    }

    public String getChoseIp() {
        return this.choseIp;
    }

    public void setIsFirstLogin(boolean isFirstLogin) {
        this.isFirstLogin = isFirstLogin;
    }

    public Boolean getIsFirstLogin() {
        return this.isFirstLogin;
    }
}

