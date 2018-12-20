package com.yinsin.simter.controller.frame;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yinsin.simter.controller.BaseController;
import com.yinsin.simter.frame.Argument;
import com.yinsin.simter.frame.Response;
import com.yinsin.simter.modal.frame.UserInfo;
import com.yinsin.simter.modal.yxos.YxosUser;
import com.yinsin.simter.service.frame.IUserInfoService;
import com.yinsin.simter.util.VerifyCodeUtils;
import com.yinsin.utils.CommonUtils;
import com.yinsin.utils.Constants;

@Controller
@RequestMapping("/lg")
@ResponseBody
public class LoginController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@Resource(name = "userInfoServiceImpl")
    protected IUserInfoService userService;
	
    public boolean isValid(int num, int val){
        String str = String.valueOf(num);
        String chats[] = str.split("");
        int value = 0;
        for (int i = 0; i < chats.length; i++) {
            value += CommonUtils.stringToInt(chats[i]);
        }
        return value % val == 0;
    }
    
    @RequestMapping("/{num}/**/v")
    public void getVcode(@PathVariable int num) throws IOException{
        if(isValid(num, 7)){
            String code = "";
            int index = 0;
            for (int i = 0; i < 4; i++) {
                index = (int) (Math.random() * Constants.CHAR_ARRAY.length);
                code += Constants.CHAR_ARRAY[index];
            }
            Map<String, Object> codeMap = new HashMap<String, Object>();
            codeMap.put("captcha_code", code);
            codeMap.put("captcha_time", new Date().getTime());
            UserDataHandle.getInstance().setUserData(request.getSession().getId(), codeMap);
            
            VerifyCodeUtils.outputImage(80, 30, response.getOutputStream(), code);
        } else {
            response.setStatus(403);
        }
    }
    
    @SuppressWarnings("unchecked")
    @RequestMapping("/{num}/**/ln")
    public Response login(@PathVariable int num) throws Exception{
    	Response result = new Response();
        if(isValid(num, 7)){
            String username = request.getParameter("acc");
            String vcode = request.getParameter("code");
            if(vcode != null){
                Map<String, Object> codeMap = (Map<String, Object>) UserDataHandle.getInstance().getUserData(request.getSession().getId());
                if(codeMap != null){
                    long time = CommonUtils.objectToLong(codeMap.get("captcha_time"));
                    long end = new Date().getTime();
                    if(end - time < (1000 * 60)){
                        String code = CommonUtils.objectToString(codeMap.get("captcha_code"));
                        if(vcode.equalsIgnoreCase(code)){
                            String password = request.getParameter("pwd");
                            Argument arg = new Argument();
                            UserInfo user = new UserInfo();
                            user.setAccount(username);
                            arg = baseService.selectOne(user);
                            if(arg.isSuccess()){
                                user = (UserInfo) arg.getObj();
                                if(user != null){
                                    if(user.getPassword().equals(password)){
                                        String key = UUID.randomUUID().toString().replaceAll("-", "");
                                        codeMap.put("user_sessionkey", key);
                                        codeMap.put("user_sessiontime", new Date().getTime());
                                        UserDataHandle.getInstance().setUserData(username, codeMap);
                                        putUserToSession(user);
                                        
                                        result.success().setToRtn("sessionkey", key);
                                    } else {
                                        result.fail("密码不正确");
                                    }
                                } else {
                                    result.fail("帐号不存在");
                                }
                            } else {
                                result.fail("帐号不存在");
                            }
                        } else {
                            result.fail("验证码不正确");
                        }
                    } else {
                        result.fail("验证码已过期");
                    }
                } else {
                    result.fail("未获取过验证码");
                }
            } else {
                result.fail("验证码为空");
            }
            return result;
        } else {
            response.setStatus(403);
        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
    @RequestMapping("/{num}/**/rg")
    public Response register(@PathVariable int num) throws Exception{
    	Response result = new Response();
        if(isValid(num, 5)){
            String username = request.getParameter("acc");
            String vcode = request.getParameter("code");
            if(vcode != null){
                Map<String, Object> codeMap = (Map<String, Object>) UserDataHandle.getInstance().getUserData(request.getSession().getId());
                if(codeMap != null){
                    long time = CommonUtils.objectToLong(codeMap.get("captcha_time"));
                    long end = new Date().getTime();
                    if(end - time < (1000 * 60)){
                        String code = CommonUtils.objectToString(codeMap.get("captcha_code"));
                        if(vcode.equalsIgnoreCase(code)){
                            String password = request.getParameter("pwd");
                            String uname = request.getParameter("uname");
                            Argument arg = new Argument();
                            UserInfo user = new UserInfo();
                            user.setAccount(username);
                            arg = baseService.selectOne(user);
                            if(arg.isSuccess()){
                                result.fail("帐号已存在");
                            } else {
                                user.setPassword(password);
                                user.setNickname(uname);
                                user.setName(uname);
                                user.setSex("");
                                user.setStatus(1);
                                user.setOnlines(1);
                                
                                arg = baseService.insert(user);
                                if(arg.isSuccess()){
                                    result.success("注册成功");
                                } else {
                                    result.fail("注册失败");
                                }
                            }
                        } else {
                            result.fail("验证码不正确");
                        }
                    } else {
                        result.fail("验证码已过期");
                    }
                } else {
                    result.fail("未获取过验证码");
                }
            } else {
                result.fail("验证码为空");
            }
           return result;
        } else {
            response.setStatus(403);
        }
        return null;
    }
    
    @RequestMapping("/lgos")
    public Response loginYisinOs(){
    	Response result = new Response();
    	try {
			String username = request.getParameter("acc");
			if (username != null) {
				String password = request.getParameter("pwd");
				Argument arg = new Argument();
				YxosUser user = new YxosUser();
				user.setUserAccount(username);
				arg = baseService.selectOne(user);
				if (arg.isSuccess()) {
					user = (YxosUser) arg.getObj();
					if (user != null) {
						if (user.getUserPassword().equals(password)) {
							putUserToSession(user);
							result.success().setToRtn("name", user.getUserName());
						} else {
							result.fail("密码不正确");
						}
					} else {
						result.fail("帐号不存在");
					}
				} else {
					result.fail("帐号不存在");
				}
			}
		} catch (Exception e) {
			logger.error("登录失败：" + e.getMessage(), e);
			result.fail("登录失败：" + e.getMessage());
		}
    	return result;
    }
    
    @RequestMapping("/lotos")
    public Response logoutYisinOs(){
    	Response result = new Response();
    	removeSessionAttribute(com.yinsin.simter.frame.consts.Constants.WEB_YXOS_USER_SESSION_KEY);
    	session.invalidate();
    	result.success();
    	return result;
    }
    
    @RequestMapping("/session")
    public Response session(){
    	Response result = new Response();
    	YxosUser user = getYxosUser();
    	if(user != null){
    		result.success().setDataToRtn(user);
    	}
    	return result;
    }
    
}
