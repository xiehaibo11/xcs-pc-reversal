/*
 * Decompiled with CFR 0.152.
 */
package quesheng.customcontrole;

import java.util.List;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import quesheng.model.Mj;

public interface MjPanelInterface {
    public void clear();

    public void display();

    public Mj getHu_mj();

    public void setHu_mj(Mj var1);

    public ImageView getHu_text();

    public ImageView getMo_text();

    public int getHu();

    public void setHu(int var1);

    public int getQiangzhiHu();

    public void setQiangzhiHu(int var1);

    public int getDirection();

    public void setDirection(int var1);

    public Pane getYinaPanel();

    public Pane getYucePanel();

    public Pane getCaozuoPanel();

    public Pane getHupaiPanel();

    public void setMo(int var1);

    public int getMo();

    public void setKehu(boolean var1);

    public boolean isKehu();

    public void setQue(int var1);

    public int getQue();

    public Mj getDiyizhangda_mj();

    public void setDiyizhangda_mj(Mj var1);

    public List<Mj> getYina_hupai();

    public void setYina_hupai(List<Mj> var1);

    public Label getChi_text();

    public ImageView getPang_text();

    public ImageView getGang_text();

    public boolean isQueren();

    public void setQueren(boolean var1);

    public String getPre_pai();

    public void setPre_pai(String var1);

    public List<Integer> getXueliuchenghe_hus();

    public void setXueliuchenghe_hus(List<Integer> var1);

    public boolean keChi(Mj var1, int var2);
}

