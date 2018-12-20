-- ------------------------------------
-- 用户表 `yxos_user`
-- ------------------------------------
DROP TABLE IF EXISTS `yxos_user`;
CREATE TABLE yxos_user(
	user_id VARCHAR(20) PRIMARY KEY NOT NULL COMMENT '主键ID',
	user_account VARCHAR(20) UNIQUE NOT NULL COMMENT '账号',
	user_password VARCHAR(32) NOT NULL COMMENT '密码',
	user_name VARCHAR(20) COMMENT '姓名',
	user_idcard VARCHAR(18) NOT NULL COMMENT '身份证',
	user_sex VARCHAR(20) COMMENT '性别',
	user_age INT COMMENT '年龄',
	user_phone VARCHAR(20) COMMENT '电话',
	user_statue INT COMMENT '状态',
	user_email VARCHAR(40) NOT NULL COMMENT '邮箱',
	user_purview INT COMMENT '权限等级',
	user_onlines INT COMMENT '在线状态',
	user_area VARCHAR(20) NOT NULL COMMENT '所在地区',
	user_register_time VARCHAR(20) NOT NULL COMMENT '账号注册时间',
	user_login_time VARCHAR(20) COMMENT '最后登录时间',
	user_photo VARCHAR(100) DEFAULT 'images/headImage/default.png' COMMENT '头像地址'
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='用户表';
ALTER TABLE yxos_user ADD INDEX yxos_user_index_account(user_account);
ALTER TABLE yxos_user ADD INDEX yxos_user_index_name(user_name);

-- ------------------------------------
-- 操作日志表 `yxos_action_log`
-- ------------------------------------
DROP TABLE IF EXISTS `yxos_action_log`;
CREATE TABLE yxos_action_log(
	logid INT AUTO_INCREMENT  NOT NULL   PRIMARY KEY COMMENT '主键ID（SEQ_ACTION_ID）', 
	logaccount VARCHAR(40)        NOT NULL COMMENT '登录帐号',               
	logip      VARCHAR(32)        NOT NULL COMMENT '登录IP',               
	accounttype VARCHAR(20)                COMMENT '账号类型（系统创建用户/其他系统同步）保留字段',               
	operationtime  DATE            NOT NULL COMMENT '操作时间',               
	actions    VARCHAR(4000)      NOT NULL COMMENT '操作内容'               
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='操作日志表'; 
ALTER TABLE yxos_action_log ADD INDEX yxos_action_log_index_logaccount(logaccount);

-- ------------------------------------
-- linux信息表`yxos_linux_host`
-- ------------------------------------
DROP TABLE IF EXISTS `yxos_linux_host`;
CREATE TABLE yxos_linux_host(
	id VARCHAR(20) PRIMARY KEY NOT NULL COMMENT '主键ID',
	user_id VARCHAR(20) NULL COMMENT '所属用户',
	host VARCHAR(20) NULL COMMENT '主机地址',
	name VARCHAR(20) NULL COMMENT '名称',
	account VARCHAR(20) NULL COMMENT '账号',
	password VARCHAR(1024) NULL COMMENT '密码',
	port INT NOT NULL default 22 COMMENT '端口',
	group_id VARCHAR(20) NOT NULL default '14' COMMENT '分组ID'
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='linux主机信息表';
ALTER TABLE yxos_linux_host ADD INDEX yxos_linux_index_user_id(user_id);