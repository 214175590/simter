package com.yinsin.simter.modal.yxos;




import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 此实体类为[代码工厂]自动生成
 * @Desc linux主机信息表
 * @Time 2018-02-11 10:12
 * @GeneratedByCodeFactory
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "yxos_linux_host")
public class YxosLinuxHost implements java.io.Serializable {

    /** 主键ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private String id;

    /** 所属用户 */
    @Column(name = "user_id")
    private String userId;

    /** 主机地址 */
    @Column(name = "host")
    private String host;

    /** 名称 */
    @Column(name = "name")
    private String name;

    /** 所属用户 */
    @Column(name = "account")
    private String account;

    /** 所属密码 */
    @Column(name = "password")
    private String password;

    /** 端口 */
    @Column(name = "port")
    private Integer port = 22;

    /** 所属分组 */
    @Column(name = "group_id")
    private String groupId;

    /**
     * 获取 主键ID 的值
     * @return String
     */
    public String getId() {
        return id;
    }
    
    /**
     * 设置主键ID 的值
     * @param String id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取 所属用户 的值
     * @return String
     */
    public String getUserId() {
        return userId;
    }
    
    /**
     * 设置所属用户 的值
     * @param String userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 获取 主机地址 的值
     * @return String
     */
    public String getHost() {
        return host;
    }
    
    /**
     * 设置主机地址 的值
     * @param String host
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * 获取 名称 的值
     * @return String
     */
    public String getName() {
        return name;
    }
    
    /**
     * 设置名称 的值
     * @param String name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取 所属用户 的值
     * @return String
     */
    public String getAccount() {
        return account;
    }
    
    /**
     * 设置所属用户 的值
     * @param String account
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * 获取 所属用户 的值
     * @return String
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * 设置所属用户 的值
     * @param String password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取 端口 的值
     * @return Integer
     */
    public Integer getPort() {
        return port;
    }
    
    /**
     * 设置端口 的值
     * @param Integer port
     */
    public void setPort(Integer port) {
        this.port = port;
    }

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getClass().getName());
        sb.append("; id=" + (id == null ? "null" : id.toString()));
        sb.append("; userId=" + (userId == null ? "null" : userId.toString()));
        sb.append("; host=" + (host == null ? "null" : host.toString()));
        sb.append("; name=" + (name == null ? "null" : name.toString()));
        sb.append("; account=" + (account == null ? "null" : account.toString()));
        sb.append("; password=" + (password == null ? "null" : password.toString()));
        sb.append("; port=" + (port == null ? "null" : port.toString()));
        sb.append("; groupId=" + (groupId == null ? "null" : groupId.toString()));

        return sb.toString();
    }
}