package com.yinsin.simter.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.alibaba.fastjson.JSONObject;
import com.yinsin.simter.frame.Argument;
import com.yinsin.simter.frame.Response;
import com.yinsin.simter.frame.consts.Constants;
import com.yinsin.simter.frame.security.DesJS;
import com.yinsin.simter.frame.security.SecurityCode;
import com.yinsin.simter.modal.frame.UserInfo;
import com.yinsin.simter.modal.yxos.YxosUser;
import com.yinsin.simter.service.base.IBaseService;
import com.yinsin.simter.util.RequestUtil;
import com.yinsin.utils.CommonUtils;

/**
 * 控制器父类，提供简单的父类辅助操作
 * 
 * @author Yisin
 * @date 2015-1-21
 * @since 1.0
 * @version 1.0
 */
public abstract class BaseController {
    protected static final Logger log = Logger.getLogger(BaseController.class);

    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected HttpSession session;

    @Resource(name = "baseServiceImpl")
    protected IBaseService baseService;

    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        this.session = request.getSession();
    }

    protected UserInfo getUserInfo() {
        UserInfo user = (UserInfo) session.getAttribute(Constants.WEB_USER_SESSION_KEY);
        if(null == user){
            Integer userId = CommonUtils.stringToInt(request.getParameter("userId"));
            if(userId > 0){
                user = new UserInfo();
                user.setId(userId);
                try {
                    Argument arg = baseService.selectOne(user);
                    if(arg.isSuccess()){
                        user = (UserInfo) arg.getObj();
                        putUserToSession(user);
                    }
                } catch (Exception e) {
                   log.error("getUserInfo error", e);
                }
            }
        }
        return user;
    }
    
    protected YxosUser getYxosUser() {
    	YxosUser user = (YxosUser) session.getAttribute(Constants.WEB_YXOS_USER_SESSION_KEY);
        if(null == user){
            String userId = request.getParameter("userId");
            if(null != userId){
                user = new YxosUser();
                user.setUserId(userId);
                try {
                    Argument arg = baseService.selectOne(user);
                    if(arg.isSuccess()){
                        user = (YxosUser) arg.getObj();
                        putUserToSession(user);
                    }
                } catch (Exception e) {
                   log.error("getYxosUser error", e);
                }
            }
        }
        return user;
    }

    /**
     * 获取用户ID
     * @author yisin
     * @return Integer
     */
    protected Integer getUserId() {
        UserInfo user = getUserInfo();
        return null != user ? user.getId() : 0;
    }
    
    /**
     * 获取用户ID
     * @author yisin
     * @return Integer
     */
    protected String getOsUserId() {
        YxosUser user = getYxosUser();
        return null != user ? user.getUserId() : null;
    }
    
    /**
     * 将UserInfo对象设置到session中去
     * @author Yisin
     */
    public void putUserToSession(UserInfo user) {
        if (session != null) {
            session.setAttribute(Constants.WEB_USER_SESSION_KEY, user);
        }
    }
    
    /**
     * 将YxosUser对象设置到session中去
     * @author Yisin
     */
    public void putUserToSession(YxosUser user) {
        if (session != null) {
            session.setAttribute(Constants.WEB_YXOS_USER_SESSION_KEY, user);
        }
    }

    /**
     * 设置参数到seesion
     * @author Yisin
     */
    public void setSessionAttribute(String name, Object obj) {
        if (session != null) {
            session.setAttribute(name, obj);
        }
    }

    /**
     * 从session中删除某个对象
     * 
     * @author Yisin
     */
    public void removeSessionAttribute(String name) {
        if (session != null) {
            session.removeAttribute(name);
        }
    }
    
    /**
	 * 获取jsonValue值
	 * @param request
	 * @return JSONObject
	 */
	public JSONObject parseJsonValue(String jsonValue){
		JSONObject jsonObj = null;
		try{
			// 过滤器中已增加编码转换
	        jsonValue = CommonUtils.stringUncode(jsonValue);
			jsonObj = JSONObject.parseObject(jsonValue);
		}catch(Exception e){
			log.error("数据格式错误，请检查：" + e.getMessage(), e);
		}
		return jsonObj;
	}
	

    /**
     * 获取请求的IP地址
     * 
     * @author Yisin
     * @return String
     */
    protected String getIpAddress() {
        return RequestUtil.getIpAddr(request);
    }

    /**
     * 判断用户是否处于已登录状态（会话未超时）
     * 
     * @author Yisin
     * @return true:已登录/未超时,false：未登录/已超时
     */
    protected YxosUser isLogined(Response res) {
    	YxosUser user = getYxosUser();
    	if(null == user){
    		res.loginTimeout();
    	}
        return user;
    }
    
    /**
     * 判断用户是否处于已登录状态（会话未超时）
     * 
     * @author Yisin
     * @return true:已登录/未超时,false：未登录/已超时
     */
    protected boolean isLogined() {
        return getUserInfo() != null;
    }
    
    protected void isAllowCrossDomainRequest(boolean allow) {
        if(allow){
            // 指定允许其他域名访问
            response.addHeader("Access-Control-Allow-Origin", "*");
            // 响应类型
            response.addHeader("Access-Control-Allow-Methods", "POST");
            // 响应头设置
            response.addHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");
        }        
    }

    /**
	 * 输出数据到前端
	 * @param str
	 */
    protected void outByte(byte[] byt) {
        try {
            response.setCharacterEncoding("text/html;UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            ServletOutputStream sos = response.getOutputStream();
            sos.write(byt);
            sos.flush();
            sos.close();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 获取请求参数并解密
     * 
     * @return
     * @throws IOException
     */
    protected String getRequestString() {
        String str = "";
        try {
            String method = request.getMethod().toLowerCase();
            if (method.equals("get")) {
                str = request.getQueryString();
            } else {
                InputStream in = request.getInputStream();
                byte[] byt = new byte[1024];
                int i = -1;
                StringBuffer sb = new StringBuffer("");
                while ((i = in.read(byt)) != -1) {
                    sb.append(new String(byt, 0, i));
                }
                str = sb.toString();
            }
            if (CommonUtils.isNotEmpty(str)) {
                try {
                    str = DesJS.decrypt(str, SecurityCode.getEncryptKey());
                } catch (Exception e) {
                    log.error("解密参数失败，key值：" + SecurityCode.getEncryptKey() + "，密文：" + str, e);
                    str = "{}";
                }
            } else {
                log.warn("请求非法：" + request.getRequestURI());
            }
        } catch (Exception e) {
            log.warn("请求非法：" + request.getRequestURI());
        }
        return str;
    }

    /**
     * 判断入参是否为空，包括空字符串
     * 
     * @author Yisin
     * @date 2016年3月23日 - 下午9:09:02
     * @param key
     * @return
     */
    public boolean isNullable(String... key) {
        boolean isnull = false;
        if (key != null) {
            Object value = null;
            for (int i = 0, k = key.length; i < k; i++) {
                value = request.getParameter(key[i]);
                if (CommonUtils.isEmpty(value) || CommonUtils.isBlank(value.toString())) {
                    isnull = true;
                    break;
                }
            }
        }
        return isnull;
    }

    /**
     * 记录操作日志
     * 
     * @author Yisin
     * @since 1.0
     * @param operLogs
     * 
     */
    public void operationLog(String operLogs) {
        if (StringUtils.isNotBlank(operLogs)) {
            request.setAttribute("opResult", operLogs);
        }
    }
}
