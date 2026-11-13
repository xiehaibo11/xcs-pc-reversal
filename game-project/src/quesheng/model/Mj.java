/*
 * Decompiled with CFR 0.152.
 */
package quesheng.model;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import quesheng.customcontrole.MjControlInterface;

public final class Mj
implements Cloneable {
    private int id;
    private int serial_number;
    private int mahjong_number;
    private int zhuo_id = 0;
    private int direction;
    private int napai_direction = -1;
    private int yuce_direction = -1;
    private int chupai_direction = -1;
    private String add_time;
    private int result;
    private int paizhuantai = 0;
    private boolean isque = false;
    private boolean simo = false;
    private boolean maima = false;
    private boolean last = false;
    private boolean hu = false;
    public boolean caisheng = false;
    public int caisheng_dipai = 0;
    public boolean kedi_caisheng = false;
    public int yuanpai = 0;
    public boolean kedi_huapai = false;
    public Mj huapai_yuce = null;
    private ImageView weina_imageView;
    private MjControlInterface yina_imageView;
    private MjControlInterface yuce_imageView;
    private ImageView yichu_imageView;
    private ImageView hupai_imageView;
    private Node other;

    public boolean isHu() {
        return this.hu;
    }

    public void setHu(boolean hu) {
        this.hu = hu;
    }

    public boolean isLast() {
        return this.last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public ImageView getHupai_imageView() {
        return this.hupai_imageView;
    }

    public boolean getIsque() {
        return this.isque;
    }

    public void setIsque(boolean isque) {
        this.isque = isque;
    }

    public void setHupai_imageView(ImageView hupai_imageView) {
        this.hupai_imageView = hupai_imageView;
    }

    public int getYuce_direction() {
        return this.yuce_direction;
    }

    public int getPaizhuantai() {
        return this.paizhuantai;
    }

    public void setPaizhuantai(int paizhuantai) {
        this.paizhuantai = paizhuantai;
    }

    public void setYuce_direction(int yuce_direction) {
        this.yuce_direction = yuce_direction;
    }

    public int getNapai_direction() {
        return this.napai_direction;
    }

    public void setNapai_direction(int napai_direction) {
        this.napai_direction = napai_direction;
    }

    public Node getOther() {
        return this.other;
    }

    public int getChupai_direction() {
        return this.chupai_direction;
    }

    public boolean getSimo() {
        return this.simo;
    }

    public void setSimo(boolean simo) {
        this.simo = simo;
    }

    public void setChupai_direction(int chupai_direction) {
        this.chupai_direction = chupai_direction;
    }

    public void setOther(Node other) {
        this.other = other;
    }

    public ImageView getWeina_imageView() {
        return this.weina_imageView;
    }

    public void setWeina_imageView(ImageView weina_imageView) {
        this.weina_imageView = weina_imageView;
    }

    public MjControlInterface getYina_imageView() {
        return this.yina_imageView;
    }

    public void setYina_imageView(MjControlInterface yina_imageView) {
        this.yina_imageView = yina_imageView;
    }

    public MjControlInterface getYuce_imageView() {
        return this.yuce_imageView;
    }

    public void setYuce_imageView(MjControlInterface yuce_imageView) {
        this.yuce_imageView = yuce_imageView;
    }

    public ImageView getYichu_imageView() {
        return this.yichu_imageView;
    }

    public void setYichu_imageView(ImageView yichu_imageView) {
        this.yichu_imageView = yichu_imageView;
    }

    public int getResult() {
        return this.result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSerial_number() {
        return this.serial_number;
    }

    public void setSerial_number(int serial_number) {
        this.serial_number = serial_number;
    }

    public int getMahjong_number() {
        return this.mahjong_number;
    }

    public void setMahjong_number(int mahjong_number) {
        this.mahjong_number = mahjong_number;
    }

    public int getDirection() {
        return this.direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void setZhuo_id(int zhuo_id) {
        this.zhuo_id = zhuo_id;
    }

    public boolean isMaima() {
        return this.maima;
    }

    public void setMaima(boolean maima) {
        this.maima = maima;
    }

    public String getZhiBiaoZHi() {
        return this.result + "_" + this.yuanpai;
    }

    public int getZhuo_id() {
        return this.zhuo_id;
    }

    public int hashCode() {
        return 0;
    }

    public boolean equals(Object obj) {
        Mj a = (Mj)obj;
        return a.getSerial_number() == this.getSerial_number() && a.getDirection() == this.getDirection() && a.getMahjong_number() == this.getMahjong_number() && a.getResult() == this.getResult() && a.getZhuo_id() == this.getZhuo_id();
    }

    public Object clone() {
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException e) {
            return null;
        }
    }
}

