package com.yinsin.simter.controller.chat;

import java.util.Collection;

import com.yisin.commun.annotation.MessageHandler;
import com.yisin.commun.annotation.Req;
import com.yisin.commun.modal.ChatUser;
import com.yisin.commun.modal.Client;
import com.yisin.commun.modal.DataCache;
import com.yisin.commun.modal.DataConstant;
import com.yisin.commun.modal.DataPackage;
import com.yisin.commun.modal.DataType;
import com.yisin.commun.modal.MessageBody;
import com.yisin.commun.modal.MessageData;
import com.yisin.commun.modal.UserState;
import com.yisin.commun.util.JSONUtils;

@MessageHandler("main")
public class LoginHandler extends BaseHandler {

	@Req("logout")
	public void logout(Client client, DataPackage dataPackage) {
		ChatUser user = (ChatUser) client.getAttr();
		if (null != user) {

			YSMessageHandle.onlineUser.put(user.getCode(), UserState.Offline);

			// 推送登出消息
			pushUserState(client, UserState.Offline);
		}
	}

	@Req("changeState")
	public void changeState(Client client, DataPackage dataPackage) {
		ChatUser user = (ChatUser) client.getAttr();
		if (null != user) {
			Object json = dataPackage.getData().getValue();
			ChatUser msgUser = JSONUtils.toJavaObject(json, ChatUser.class);

			YSMessageHandle.onlineUser.put(user.getCode(), msgUser.getState());

			pushUserState(client, msgUser.getState());
		}
	}

	public static void pushUserState(Client client, String state) {
		ChatUser formUser = (ChatUser) client.getAttr();
		formUser.setState(state);

		Collection<Client> clients = DataCache.getClients();
		MessageData msgData = null;
		DataPackage pack = null;
		MessageBody msgBody = new MessageBody();
		msgBody.setUser(formUser.clone().setPassword(null));

		for (Client cli : clients) {
			try {
				if (!client.isSelf(cli)) {
					msgData = new MessageData();
					msgData.setRetcode(DataConstant.SUCCESS).setResult(msgBody);

					pack = new DataPackage(DataType.TEXT, "main.changeState", msgData);
					cli.send(pack);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void Send(Client client, DataPackage data) {
		try {
			client.send(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
