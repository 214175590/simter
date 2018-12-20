package com.yinsin.simter.controller.frame;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yinsin.simter.controller.BaseController;
import com.yinsin.simter.service.frame.IUserInfoService;

@Controller
@RequestMapping("/userInfo")
@ResponseBody
public class UserInfoController extends BaseController {
	private final static Logger logger = Logger.getLogger(UserInfoController.class);
	
	@Resource(name="userInfoServiceImpl")
    IUserInfoService userInfoServiceImpl;
	
	@RequestMapping("/loadUserInfoList")
    public String loadUserInfoList() {
    	return null;
    }
    
    @RequestMapping("/addUserInfo")
    public String addUserInfo() {
    	return null;
    }
    
    @RequestMapping("/editUserInfo")
    public String editUserInfo() {
    	return null;
    }
    
    @RequestMapping("/delUserInfo")
    public String delUserInfo() {
    	return null;
    }
}