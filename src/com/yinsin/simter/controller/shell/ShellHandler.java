package com.yinsin.simter.controller.shell;

import com.alibaba.fastjson.JSONObject;
import com.yisin.commun.annotation.MessageHandler;
import com.yisin.commun.annotation.Req;
import com.yisin.commun.modal.Client;
import com.yisin.commun.modal.Data;
import com.yisin.commun.modal.DataPackage;

@MessageHandler("linux")
public class ShellHandler {
	
	@Req("login")
	public void login(Client client, DataPackage dataPackage) {
		try {
			Data data = dataPackage.getData();
			JSONObject value = (JSONObject) data.getValue();
			String userId = value.getString("userId");
			String serverIp = value.getString("serverIp");
			client.setAttr1(userId);
			ShellInstance shell = ShellManager.getByIp(userId, serverIp);
			if(null == shell){
				shell = new ShellInstance();
				shell.setUserId(userId);
				shell.setClient(client);
				ShellManager.add(userId, shell);
			} else {
				shell.setClient(client);
			}
			client.send(dataPackage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void onOpen(Client client) {
	}
	
	public void onClose(Client client) {
		ShellInstance shell = ShellManager.get(client.getAttr1(), client.getSid());
		if(null != shell){
			shell.setClient(null);
			System.err.println("client exit =1==>" + client.getAttr1() + "----" + client.getSid());
		}
	}
	
	public void onError(Client client, Throwable thr) {
		ShellInstance shell = ShellManager.get(client.getAttr1(), client.getSid());
		if(null != shell){
			shell.setClient(null);
			System.err.println("client exit =2==>" + client.getAttr1() + "----" + client.getSid());
		}
	}
	
}
