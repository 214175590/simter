package com.yinsin.simter.controller.socket;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yinsin.simter.controller.BaseController;
import com.yinsin.simter.frame.Response;
import com.yinsin.utils.CommonUtils;

/**
 * 此Controller类为[代码工厂]自动生成
 * @Desc 
 * @Time 2016-10-15 13:23
 * @CodeFactoryGenerated
 */
@Controller
@RequestMapping("/suc")
@ResponseBody
public class SoftUserController extends BaseController {
	private final static Logger logger = LoggerFactory.getLogger(SoftUserController.class);
	
	@RequestMapping("/lsul")
    public Response loadSoftUserList(@RequestParam String jsonValue) {
		Response res = new Response();
		JSONObject param = parseJsonValue(jsonValue);
		String appName = param.getString("appName");
		List<Map<String, Object>> userList = null;
		Iterator<Map<String, Object>> it = null;
		Map<String, Object> userMap = null;
		JSONArray list = new JSONArray();
		Map<String, List<Map<String, Object>>> softMap = UdpServer.getSoftUser();
		String aname = null;
		for (Map.Entry<String, List<Map<String, Object>>> entry : softMap.entrySet()) {
			userList = (List<Map<String, Object>>) entry.getValue();
			it = userList.iterator();
			if (it.hasNext()) {
				userMap = mapCopy(it.next());
				userMap.remove("packet");
				aname = (String)userMap.get("appName");
				if(CommonUtils.isNotBlank(appName)){
					if(appName.equals(aname)){
						list.add(userMap);
					}
				} else {
					list.add(userMap);
				}
			}
		}
		res.success().setDataToRtn(list);
		return res;
    }

	@RequestMapping("/msg")
    public Response sendMsgToClient(@RequestParam String jsonValue) {
		Response res = new Response();
		JSONObject param = parseJsonValue(jsonValue);
		String userId = param.getString("userId");
		JSONObject json = param.getJSONObject("msg");
		if(CommonUtils.isNotBlank(userId)){
			UdpServer.PushMessage(userId, json);
		}
		res.success();
		return res;
    }
	
	private Map<String, Object> mapCopy(Map<String, Object> resMap){
		Map<String, Object> targetMap = new HashMap<String, Object>();
		for (Entry<String, Object> entry : resMap.entrySet()) {
			targetMap.put(entry.getKey(), entry.getValue());
		}
		return targetMap;
	}
	
}