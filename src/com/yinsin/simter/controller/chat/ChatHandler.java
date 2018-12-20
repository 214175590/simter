package com.yinsin.simter.controller.chat;

import java.util.Collection;
import java.util.List;

import com.yisin.commun.annotation.MessageHandler;
import com.yisin.commun.annotation.Req;
import com.yisin.commun.modal.ChatUser;
import com.yisin.commun.modal.Client;
import com.yisin.commun.modal.Data;
import com.yisin.commun.modal.DataCache;
import com.yisin.commun.modal.DataConstant;
import com.yisin.commun.modal.DataPackage;
import com.yisin.commun.modal.MessageBody;
import com.yisin.commun.modal.MessageData;
import com.yisin.commun.util.JSONUtils;

@MessageHandler("chat")
public class ChatHandler extends BaseHandler {
	
	@Req("single")
	public void singleChat(Client client, DataPackage dataPackage){
		System.out.println(client.getSid() + "#" + "==>" +  dataPackage.toString());
		
		MessageData msgData = null;
        Data data = dataPackage.getData();
    	MessageBody msgBody = JSONUtils.toJavaObject(data.getValue(), MessageBody.class);

    	Collection<Client> clients = DataCache.getClients();
        DataPackage pack = null;
        ChatUser fromUsers = (ChatUser) client.getAttr();
        ChatUser toUsers = null;
        
        for (Client cli : clients) {
            try {
                if(!client.isSelf(cli)){
                	toUsers = (ChatUser) cli.getAttr();
                	if(toUsers.getCode().equals(msgBody.getUser().getCode())){
                		msgData = new MessageData();
                		msgBody.getUser().setPassword("");
                    	msgData.setRetcode(DataConstant.SUCCESS)
                        	.setResult(
                        		new MessageBody()
                        			.setUser(fromUsers.clone().setPassword(null))
                        			.setContent(msgBody.getContent())
                			);
                    	
                        pack = new DataPackage(dataPackage.getDataType(), 
                                dataPackage.getReq(), 
                                msgData);
                        cli.send(pack);
                        break;
                	}
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
		
	}
	
	@Req("group")
	public void groupChat(Client client, DataPackage dataPackage){
		System.out.println(client.getSid() + "#" + "==>" +  dataPackage.toString());
		
        MessageData msgData = null;
        Data data = dataPackage.getData();
    	MessageBody msgBody = JSONUtils.toJavaObject(data.getValue(), MessageBody.class);

        List<Client> clients = DataCache.getByGroupId(client.getGroupId());
        DataPackage pack = null;
        ChatUser users = (ChatUser) client.getAttr();        
        ChatUser formUuser = users.clone().setPassword(null);
        
        for (Client cli : clients) {
            try {
                if(!client.isSelf(cli)){
                	msgData = new MessageData();
                	msgData.setRetcode(DataConstant.SUCCESS)
                    	.setResult(
                    		new MessageBody()
                    			.setUser(formUuser)
                    			.setContent(msgBody.getContent())
            			);
                	
                    pack = new DataPackage(dataPackage.getDataType(), 
                            dataPackage.getReq(), 
                            msgData);
                    cli.send(pack);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	}
	
}
