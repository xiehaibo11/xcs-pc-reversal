/*
 * Decompiled with CFR 0.152.
 */
package quesheng.model;

import java.io.Serializable;

public class User
implements Serializable {
    private int user_id;
    private String username;
    private String password;
    private int priority;
    private int node_id;
    private String mobile;
    private String email;
    private int type;
    private String nick_name;
    private String avatar;
    private Integer onlineflag;
    private Integer devicetype;
    private String deviceid;
    private String bio;
    private String qq;
    private String msn;
    private String weixin;
    private String nation;
    private int gender;
    private String birth_date;
    private String real_name;
    private String salt;
    private String server_ip;
    private int jihuo_status = 0;

    public String getServer_ip() {
        return this.server_ip;
    }

    public void setServer_ip(String server_ip) {
        this.server_ip = server_ip;
    }

    public int getUser_id() {
        return this.user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPriority() {
        return this.priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getNode_id() {
        return this.node_id;
    }

    public void setNode_id(int node_id) {
        this.node_id = node_id;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getNick_name() {
        return this.nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getOnlineflag() {
        return this.onlineflag;
    }

    public void setOnlineflag(Integer onlineflag) {
        this.onlineflag = onlineflag;
    }

    public Integer getDevicetype() {
        return this.devicetype;
    }

    public void setDevicetype(Integer devicetype) {
        this.devicetype = devicetype;
    }

    public String getDeviceid() {
        return this.deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getBio() {
        return this.bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getQq() {
        return this.qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getMsn() {
        return this.msn;
    }

    public void setMsn(String msn) {
        this.msn = msn;
    }

    public String getWeixin() {
        return this.weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public String getNation() {
        return this.nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public int getGender() {
        return this.gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getBirth_date() {
        return this.birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public String getReal_name() {
        return this.real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public String getSalt() {
        return this.salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public int getJihuo_status() {
        return this.jihuo_status;
    }

    public void setJihuo_status(int jihuo_status) {
        this.jihuo_status = jihuo_status;
    }
}

