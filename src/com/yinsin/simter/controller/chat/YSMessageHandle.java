package com.yinsin.simter.controller.chat;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.yinsin.simter.controller.shell.ShellHandler;
import com.yisin.commun.modal.ChatUser;
import com.yisin.commun.modal.Client;
import com.yisin.commun.modal.Data;
import com.yisin.commun.modal.DataConstant;
import com.yisin.commun.modal.DataPackage;
import com.yisin.commun.modal.MessageData;
import com.yisin.commun.modal.UserState;
import com.yisin.commun.server.BaseMessageHandle;
import com.yisin.commun.server.HandleHelper;
import com.yisin.commun.util.JSONUtils;

public class YSMessageHandle extends BaseMessageHandle {
	public static Map<String, String> secretKey = new HashMap<String, String>();
	public static Map<String, String> onlineUser = new HashMap<String, String>();

    @Override
	public void init(HandleHelper helper) {
    	try {
			helper.registerHandle(
					// 登录
					LoginHandler.class,
					// Linux
					ShellHandler.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    @Override
	public boolean filter(Client client, DataPackage dataPackage) {
    	boolean result = true;
    	if(!dataPackage.getReq().equals("main.login") && !dataPackage.getReq().equals("main.wxinit")){ // 如果不是登录，则需要验证请求是否携带密钥
    		String key = YSMessageHandle.secretKey.get(client.getSid());
    		Data data = dataPackage.getData();
    		JSONObject jsonObj = (JSONObject) data.getValue();
    		ChatUser msgUser = null;
    		if(jsonObj.containsKey("user")){
    			msgUser = JSONUtils.toJavaObject(jsonObj.get("user"), ChatUser.class);
    		}
    		if(null != msgUser && !key.equals(msgUser.getPassword())){
    			result = false;
    			DataPackage pack = new DataPackage();
    			try {
    				pack.setReq("IllegalRequest");
    				pack.setData(new MessageData().setRetcode(DataConstant.ILLEGAL).setRetinfo("非法请求，请尝试重启软件再试"));
					client.send(pack);
				} catch (Exception e) {
					e.printStackTrace();
				}
    		}
    	}
    	return result;
	}

	@Override
    public void onMessage(Client client, DataPackage dataPackage) {
        System.out.println(client.getSid() + "#" + client.getAttr1() + "==>" +  dataPackage.toString());
        
    }

    @Override
    public void onClose(Client client) {
        System.out.println("Client disconnection.." + client.getSid());
        ChatUser user = (ChatUser) client.getAttr();
        if(null != user){
        	YSMessageHandle.onlineUser.remove(user.getCode());
        	
        	LoginHandler.pushUserState(client, UserState.Offline);
        	
        }
        super.onClose(client);
    }

    @Override
    public void onError(Client client, Throwable e) {
        System.out.println("Client disconnection.." + client.getSid());
        ChatUser user = (ChatUser) client.getAttr();
        if(null != user){
        	YSMessageHandle.onlineUser.remove(user.getCode());
        	
        	LoginHandler.pushUserState(client, UserState.Offline);
        	
        }
        super.onError(client, e);
    }

    @Override
    public void onOpen(Client client) {
        System.out.println("Client connection.." + client.getSid());
        super.onOpen(client);
    }

}
