package com.yinsin.simter.controller.shell;

import com.yinsin.simter.modal.shell.LinuxOs;
import com.yinsin.simter.modal.yxos.YxosUser;
import com.yisin.commun.modal.Client;
import com.yisin.commun.modal.DataPackage;
import com.yisin.ssh2.ShellMessageEvent;

public class ShellInstance {
	private YxosUser user;
	private String userId;
	private String serverIp;
	private String account;
	private Client client;
	private LinuxOs os;
	private boolean sended;
	private boolean enable = true;
	
	private boolean run = true;

	public void closeLinuxOs() {
		os.close();
		os = null;
	}
	
	public void start(){
		os.getChannel().regEvent(new ShellMessageEvent(){
			private DataPackage dataPackage = null;
			public void handle(Object... params) {
				try {
					setSended(false);
					dataPackage = new DataPackage();
					dataPackage.setReq("linux.data");
					dataPackage.setFlag(serverIp);
					dataPackage.setData(params[0]);
					if(null != client){
						String value = (String)dataPackage.getData().getValue();
						byte[] bytes = value.getBytes();
						int v = bytes[0] + bytes[1] + bytes[2];
						if(enable && v > 10){
							client.send(dataPackage);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		// Wait till channel is closed
		new Thread(){
			public void run(){
				try {
					while (run && os != null && !os.getChannel().isClosed()) {
						try {
							Thread.sleep(1000);
						} catch (Exception e) {
						}
					}
					// Disconnect from remote server
					os.getChannel().disconnect();
					os.getSession().disconnect();
				} catch (Exception e) {
				}
			}
		}.start();
	}

	public YxosUser getUser() {
		return user;
	}

	public void setUser(YxosUser user) {
		this.user = user;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public boolean isSended() {
		return sended;
	}

	public void setSended(boolean sended) {
		this.sended = sended;
	}

	public LinuxOs getLinuxOs() {
		return os;
	}
	
	public void setLinuxOs(LinuxOs os) {
		this.os = os;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	
}
