package com.yinsin.simter.modal.frame;


public class UserInfo implements java.io.Serializable {

    /** ID */
    private Integer id;

    /** 帐号 */
    private String account;

    /** 密码 */
    private String password;

    /** 姓名 */
    private String name;

    /** 昵称 */
    private String nickname;

    /** 性别 */
    private String sex;

    /** 手机 */
    private String mobile;

    /** 邮箱 */
    private String email;

    /** QQ */
    private String qq;

    /** 省份 */
    private String province;

    /** 地市 */
    private String city;

    /** 注册时间 */
    private String registerdate;

    /** 最后登录时间 */
    private String lastlogindate;

    /** 状态(1:正常,2:禁用) */
    private Integer status;

    /** 状态(1:在线,2:离线) */
    private Integer onlines;

    /** 登录IP */
    private String userip;

    /** 注册时间 */
    private String headimage;



    /**
     * 获取 ID 的值
     * @return Integer
     */
    public Integer getId() {
        return id;
    }
    
    /**
     * 设置ID 的值
     * @param Integer id
     */
    public UserInfo setId(Integer id) {
        this.id = id;
        return this;
    }

    /**
     * 获取 帐号 的值
     * @return String
     */
    public String getAccount() {
        return account;
    }
    
    /**
     * 设置帐号 的值
     * @param String account
     */
    public UserInfo setAccount(String account) {
        this.account = account;
        return this;
    }

    /**
     * 获取 密码 的值
     * @return String
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * 设置密码 的值
     * @param String password
     */
    public UserInfo setPassword(String password) {
        this.password = password;
        return this;
    }

    /**
     * 获取 姓名 的值
     * @return String
     */
    public String getName() {
        return name;
    }
    
    /**
     * 设置姓名 的值
     * @param String name
     */
    public UserInfo setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * 获取 昵称 的值
     * @return String
     */
    public String getNickname() {
        return nickname;
    }
    
    /**
     * 设置昵称 的值
     * @param String nickname
     */
    public UserInfo setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    /**
     * 获取 性别 的值
     * @return String
     */
    public String getSex() {
        return sex;
    }
    
    /**
     * 设置性别 的值
     * @param String sex
     */
    public UserInfo setSex(String sex) {
        this.sex = sex;
        return this;
    }

    /**
     * 获取 手机 的值
     * @return String
     */
    public String getMobile() {
        return mobile;
    }
    
    /**
     * 设置手机 的值
     * @param String mobile
     */
    public UserInfo setMobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    /**
     * 获取 邮箱 的值
     * @return String
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * 设置邮箱 的值
     * @param String email
     */
    public UserInfo setEmail(String email) {
        this.email = email;
        return this;
    }

    /**
     * 获取 QQ 的值
     * @return String
     */
    public String getQq() {
        return qq;
    }
    
    /**
     * 设置QQ 的值
     * @param String qq
     */
    public UserInfo setQq(String qq) {
        this.qq = qq;
        return this;
    }

    /**
     * 获取 省份 的值
     * @return String
     */
    public String getProvince() {
        return province;
    }
    
    /**
     * 设置省份 的值
     * @param String province
     */
    public UserInfo setProvince(String province) {
        this.province = province;
        return this;
    }

    /**
     * 获取 地市 的值
     * @return String
     */
    public String getCity() {
        return city;
    }
    
    /**
     * 设置地市 的值
     * @param String city
     */
    public UserInfo setCity(String city) {
        this.city = city;
        return this;
    }

    /**
     * 获取 注册时间 的值
     * @return String
     */
    public String getRegisterdate() {
        return registerdate;
    }
    
    /**
     * 设置注册时间 的值
     * @param String registerdate
     */
    public UserInfo setRegisterdate(String registerdate) {
        this.registerdate = registerdate;
        return this;
    }

    /**
     * 获取 最后登录时间 的值
     * @return String
     */
    public String getLastlogindate() {
        return lastlogindate;
    }
    
    /**
     * 设置最后登录时间 的值
     * @param String lastlogindate
     */
    public UserInfo setLastlogindate(String lastlogindate) {
        this.lastlogindate = lastlogindate;
        return this;
    }

    /**
     * 获取 状态(1:正常,2:禁用) 的值
     * @return Integer
     */
    public Integer getStatus() {
        return status;
    }
    
    /**
     * 设置状态(1:正常,2:禁用) 的值
     * @param Integer status
     */
    public UserInfo setStatus(Integer status) {
        this.status = status;
        return this;
    }

    /**
     * 获取 状态(1:在线,2:离线) 的值
     * @return Integer
     */
    public Integer getOnlines() {
        return onlines;
    }
    
    /**
     * 设置状态(1:在线,2:离线) 的值
     * @param Integer onlines
     */
    public UserInfo setOnlines(Integer onlines) {
        this.onlines = onlines;
        return this;
    }

    /**
     * 获取 登录IP 的值
     * @return String
     */
    public String getUserip() {
        return userip;
    }
    
    /**
     * 设置登录IP 的值
     * @param String userip
     */
    public UserInfo setUserip(String userip) {
        this.userip = userip;
        return this;
    }

    /**
     * 获取 注册时间 的值
     * @return String
     */
    public String getHeadimage() {
        return headimage;
    }
    
    /**
     * 设置注册时间 的值
     * @param String headimage
     */
    public UserInfo setHeadimage(String headimage) {
        this.headimage = headimage;
        return this;
    }


	public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getClass().getName());
        sb.append("; id=" + (id == null ? "null" : id.toString()));
        sb.append("; account=" + (account == null ? "null" : account.toString()));
        sb.append("; password=" + (password == null ? "null" : password.toString()));
        sb.append("; name=" + (name == null ? "null" : name.toString()));
        sb.append("; nickname=" + (nickname == null ? "null" : nickname.toString()));
        sb.append("; sex=" + (sex == null ? "null" : sex.toString()));
        sb.append("; mobile=" + (mobile == null ? "null" : mobile.toString()));
        sb.append("; email=" + (email == null ? "null" : email.toString()));
        sb.append("; qq=" + (qq == null ? "null" : qq.toString()));
        sb.append("; province=" + (province == null ? "null" : province.toString()));
        sb.append("; city=" + (city == null ? "null" : city.toString()));
        sb.append("; registerdate=" + (registerdate == null ? "null" : registerdate.toString()));
        sb.append("; lastlogindate=" + (lastlogindate == null ? "null" : lastlogindate.toString()));
        sb.append("; status=" + (status == null ? "null" : status.toString()));
        sb.append("; onlines=" + (onlines == null ? "null" : onlines.toString()));
        sb.append("; userip=" + (userip == null ? "null" : userip.toString()));
        sb.append("; headimage=" + (headimage == null ? "null" : headimage.toString()));

        return sb.toString();
    }
}