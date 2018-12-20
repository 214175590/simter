package com.yinsin.simter.modal.yxos;



/**
 * 此实体类为[代码工厂]自动生成
 * @Desc 用户表
 * @Time 2017-06-18 23:51
 * @GeneratedByCodeFactory
 */
@SuppressWarnings("serial")
public class YxosUser implements java.io.Serializable {

    /** 主键ID */
    private String userId;

    /** 账号 */
    private String userAccount;

    /** 密码 */
    private String userPassword;

    /** 姓名 */
    private String userName;

    /** 身份证 */
    private String userIdcard;

    /** 性别 */
    private String userSex;

    /** 年龄 */
    private Integer userAge;

    /** 电话 */
    private String userPhone;

    /** 状态,1正常,2禁用，3失效 */
    private Integer userStatue;

    /** 邮箱 */
    private String userEmail;

    /** 权限等级 */
    private Integer userPurview;

    /** 在线状态 */
    private Integer userOnlines;

    /** 所在地区 */
    private String userArea;

    /** 账号注册时间 */
    private String userRegisterTime;

    /** 最后登录时间 */
    private String userLoginTime;

    /** 头像地址 */
    private String userPhoto;

    /**
     * 获取 主键ID 的值
     * @return String
     */
    public String getUserId() {
        return userId;
    }
    
    /**
     * 设置主键ID 的值
     * @param String userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 获取 账号 的值
     * @return String
     */
    public String getUserAccount() {
        return userAccount;
    }
    
    /**
     * 设置账号 的值
     * @param String userAccount
     */
    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    /**
     * 获取 密码 的值
     * @return String
     */
    public String getUserPassword() {
        return userPassword;
    }
    
    /**
     * 设置密码 的值
     * @param String userPassword
     */
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    /**
     * 获取 姓名 的值
     * @return String
     */
    public String getUserName() {
        return userName;
    }
    
    /**
     * 设置姓名 的值
     * @param String userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 获取 身份证 的值
     * @return String
     */
    public String getUserIdcard() {
        return userIdcard;
    }
    
    /**
     * 设置身份证 的值
     * @param String userIdcard
     */
    public void setUserIdcard(String userIdcard) {
        this.userIdcard = userIdcard;
    }

    /**
     * 获取 性别 的值
     * @return String
     */
    public String getUserSex() {
        return userSex;
    }
    
    /**
     * 设置性别 的值
     * @param String userSex
     */
    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    /**
     * 获取 年龄 的值
     * @return Integer
     */
    public Integer getUserAge() {
        return userAge;
    }
    
    /**
     * 设置年龄 的值
     * @param Integer userAge
     */
    public void setUserAge(Integer userAge) {
        this.userAge = userAge;
    }

    /**
     * 获取 电话 的值
     * @return String
     */
    public String getUserPhone() {
        return userPhone;
    }
    
    /**
     * 设置电话 的值
     * @param String userPhone
     */
    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    /**
     * 获取 状态 的值 1正常,2禁用，3失效
     * @return Integer
     */
    public Integer getUserStatue() {
        return userStatue;
    }
    
    /**
     * 设置状态 的值 1正常,2禁用，3失效
     * @param Integer userStatue
     */
    public void setUserStatue(Integer userStatue) {
        this.userStatue = userStatue;
    }

    /**
     * 获取 邮箱 的值
     * @return String
     */
    public String getUserEmail() {
        return userEmail;
    }
    
    /**
     * 设置邮箱 的值
     * @param String userEmail
     */
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    /**
     * 获取 权限等级 的值
     * @return Integer
     */
    public Integer getUserPurview() {
        return userPurview;
    }
    
    /**
     * 设置权限等级 的值
     * @param Integer userPurview
     */
    public void setUserPurview(Integer userPurview) {
        this.userPurview = userPurview;
    }

    /**
     * 获取 在线状态 的值
     * @return Integer
     */
    public Integer getUserOnlines() {
        return userOnlines;
    }
    
    /**
     * 设置在线状态 的值
     * @param Integer userOnlines
     */
    public void setUserOnlines(Integer userOnlines) {
        this.userOnlines = userOnlines;
    }

    /**
     * 获取 所在地区 的值
     * @return String
     */
    public String getUserArea() {
        return userArea;
    }
    
    /**
     * 设置所在地区 的值
     * @param String userArea
     */
    public void setUserArea(String userArea) {
        this.userArea = userArea;
    }

    /**
     * 获取 账号注册时间 的值
     * @return String
     */
    public String getUserRegisterTime() {
        return userRegisterTime;
    }
    
    /**
     * 设置账号注册时间 的值
     * @param String userRegisterTime
     */
    public void setUserRegisterTime(String userRegisterTime) {
        this.userRegisterTime = userRegisterTime;
    }

    /**
     * 获取 最后登录时间 的值
     * @return String
     */
    public String getUserLoginTime() {
        return userLoginTime;
    }
    
    /**
     * 设置最后登录时间 的值
     * @param String userLoginTime
     */
    public void setUserLoginTime(String userLoginTime) {
        this.userLoginTime = userLoginTime;
    }

    /**
     * 获取 头像地址 的值
     * @return String
     */
    public String getUserPhoto() {
        return userPhoto;
    }
    
    /**
     * 设置头像地址 的值
     * @param String userPhoto
     */
    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

	public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getClass().getName());
        sb.append("; userId=" + (userId == null ? "null" : userId.toString()));
        sb.append("; userAccount=" + (userAccount == null ? "null" : userAccount.toString()));
        sb.append("; userPassword=" + (userPassword == null ? "null" : userPassword.toString()));
        sb.append("; userName=" + (userName == null ? "null" : userName.toString()));
        sb.append("; userIdcard=" + (userIdcard == null ? "null" : userIdcard.toString()));
        sb.append("; userSex=" + (userSex == null ? "null" : userSex.toString()));
        sb.append("; userAge=" + (userAge == null ? "null" : userAge.toString()));
        sb.append("; userPhone=" + (userPhone == null ? "null" : userPhone.toString()));
        sb.append("; userStatue=" + (userStatue == null ? "null" : userStatue.toString()));
        sb.append("; userEmail=" + (userEmail == null ? "null" : userEmail.toString()));
        sb.append("; userPurview=" + (userPurview == null ? "null" : userPurview.toString()));
        sb.append("; userOnlines=" + (userOnlines == null ? "null" : userOnlines.toString()));
        sb.append("; userArea=" + (userArea == null ? "null" : userArea.toString()));
        sb.append("; userRegisterTime=" + (userRegisterTime == null ? "null" : userRegisterTime.toString()));
        sb.append("; userLoginTime=" + (userLoginTime == null ? "null" : userLoginTime.toString()));
        sb.append("; userPhoto=" + (userPhoto == null ? "null" : userPhoto.toString()));

        return sb.toString();
    }
}