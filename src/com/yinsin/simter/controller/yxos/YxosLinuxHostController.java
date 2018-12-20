package com.yinsin.simter.controller.yxos;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.yinsin.simter.controller.BaseController;
import com.yinsin.simter.frame.Argument;
import com.yinsin.simter.frame.Response;
import com.yinsin.simter.frame.security.DesJS;
import com.yinsin.simter.modal.yxos.YxosLinuxHost;
import com.yinsin.simter.modal.yxos.YxosUser;
import com.yinsin.simter.service.yxos.YxosLinuxHostServiceImpl;
import com.yinsin.simter.util.CodeUtil;
import com.yinsin.utils.CommonUtils;

/**
 * 此Controller类为[代码工厂]自动生成
 * @Desc linux主机信息表
 * @Time 2017-06-18 23:51
 * @GeneratedByCodeFactory
 */
@Controller
@RequestMapping("/linuxHost")
@ResponseBody
public class YxosLinuxHostController extends BaseController {
	private final static Logger log = LoggerFactory.getLogger(YxosLinuxHostController.class);
	
	@Resource(name="yxosLinuxHostServiceImpl")
    YxosLinuxHostServiceImpl yxosLinuxHostServiceImpl;
	
	@RequestMapping("/loadLinuxList")
    public Response loadYxosLinuxList(@RequestParam String jsonValue) {
		Response res = new Response();
		YxosUser user = getYxosUser();
		if (user != null) {
			String userId = user.getUserId();
			Argument arg = new Argument();
			JSONObject param = parseJsonValue(jsonValue);
			String groupId = param.getString("groupId");
			YxosLinuxHost linux = new YxosLinuxHost();
			linux.setUserId(userId);
			if(CommonUtils.isNotBlank(groupId)){
				linux.setGroupId(groupId);
			}
			arg.setObj(linux);
			arg = yxosLinuxHostServiceImpl.select(arg);
			if(arg.isSuccess()){
				res.success().setDataToRtn(arg.getDataForRtn());
			}
		}
		return res;
    }
    
    @RequestMapping("/getLinuxInfo")
    public Response getYxosLinux(@RequestParam String jsonValue) {
    	Response res = new Response();
		YxosUser user = getYxosUser();
		if (user != null) {
			Argument arg = new Argument();
			JSONObject param = parseJsonValue(jsonValue);
			String host = param.getString("serverIp");
			String userId = user.getUserId();
			YxosLinuxHost linux = new YxosLinuxHost();
			linux.setUserId(userId);
			linux.setHost(host);
			arg.setObj(linux);
			arg = yxosLinuxHostServiceImpl.select(arg);
			if(arg.isSuccess()){
				res.success().setDataToRtn(arg.getDataForRtn());
			}
		}
		return res;
    }
    
    @RequestMapping("/exist")
    public Response exist(@RequestParam String jsonValue) {
    	Response res = new Response();
		YxosUser user = getYxosUser();
		if (user != null) {
			Argument arg = new Argument();
			JSONObject param = parseJsonValue(jsonValue);
			String host = param.getString("serverIp");
			String userId = user.getUserId();
			YxosLinuxHost linux = new YxosLinuxHost();
			linux.setUserId(userId);
			linux.setHost(host);
			arg.setObj(linux);
			arg = yxosLinuxHostServiceImpl.select(arg);
			if(arg.isSuccess()){
				List<YxosLinuxHost> dataList = (List<YxosLinuxHost>) arg.getDataForRtn();
				if(null != dataList && dataList.size() > 0){
					res.success();
				}
			}
		}
		return res;
    }
    
    @RequestMapping("/addYxosLinux")
    public Response addYxosLinux(@RequestParam String jsonValue) {
    	Response res = new Response();
		YxosUser user = getYxosUser();
		if (user != null) {
			Argument arg = new Argument();
			JSONObject param = parseJsonValue(jsonValue);
			String host = param.getString("serverIp");
			String name = param.getString("name");
			String account = param.getString("account");
			String password = param.getString("password");
			int port = param.getIntValue("port");
			String groupId = param.getString("groupId");
			YxosLinuxHost linux = new YxosLinuxHost();
			linux.setUserId(user.getUserId());
			linux.setHost(host);
			linux.setAccount(account);
			try {
				linux.setPassword(DesJS.encrypt(password, user.getUserId()));
			} catch (Exception e) {
			}
			linux.setPort(port);
			linux.setGroupId(groupId);
			arg.setObj(linux);
			arg = yxosLinuxHostServiceImpl.select(arg);
			boolean exist = false;
			if(arg.isSuccess()){
				List<YxosLinuxHost> dataList = (List<YxosLinuxHost>) arg.getDataForRtn();
				if(null != dataList && dataList.size() > 0){
					exist = true;
				}
			}
			
			if(!exist){
				linux.setId(CodeUtil.getRowId("LX"));
				linux.setName(name);
				arg.setObj(linux);
				arg = yxosLinuxHostServiceImpl.insert(arg);
				if(arg.isSuccess()){
					res.success().setDataToRtn(linux);
				}
			}
		}
		return res;
    }
    
    @RequestMapping("/editYxosLinux")
    public Response editYxosLinux(@RequestParam String jsonValue) {
    	Response res = new Response();
		YxosUser user = getYxosUser();
		if (user != null) {
			Argument arg = new Argument();
			JSONObject param = parseJsonValue(jsonValue);
			String id = param.getString("id");
			String host = param.getString("host");
			String name = param.getString("name");
			String account = param.getString("account");
			String password = param.getString("password");
			int port = param.getIntValue("port");
			String groupId = param.getString("groupId");
			YxosLinuxHost linux = new YxosLinuxHost();
			linux.setId(id);
			linux.setUserId(user.getUserId());
			linux.setHost(host);
			if(CommonUtils.isNotBlank(name)){
				linux.setName(name);
			}
			linux.setAccount(account);
			linux.setPassword(password);
			if(port != 0){
				linux.setPort(port);
			}
			if(CommonUtils.isNotBlank(groupId)){
				linux.setGroupId(groupId);
			}
			arg.setObj(linux);
			arg = yxosLinuxHostServiceImpl.update(arg);
			if(arg.isSuccess()){
				res.success();
			}
	    }
		return res;
    }
    
    @RequestMapping("/delYxosLinux")
    public Response delYxosLinux(@RequestParam String jsonValue) {
    	Response res = new Response();
		YxosUser user = getYxosUser();
		if (user != null) {
			Argument arg = new Argument();
			JSONObject param = parseJsonValue(jsonValue);
			String host = param.getString("host");
			String id = param.getString("id");
			YxosLinuxHost linux = new YxosLinuxHost();
			linux.setUserId(user.getUserId());
			if(CommonUtils.isNotBlank(id)){
				linux.setId(id);
			}
			if(CommonUtils.isNotBlank(host)){
				linux.setHost(host);
			}
			arg.setObj(linux);
			arg = yxosLinuxHostServiceImpl.delete(arg);
			if(arg.isSuccess()){
				res.success();
			}
		}
		return res;
    }

}