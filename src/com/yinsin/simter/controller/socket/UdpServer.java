package com.yinsin.simter.controller.socket;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.yinsin.utils.CommonUtils;

public class UdpServer {
	private final static Logger logger = LoggerFactory.getLogger(UdpServer.class);
	private static final int ECHOMAX = 1024;
	private static final int PORT = 8657;
	private static DatagramSocket socket = null;
	private static Map<String, List<Map<String, Object>>> SoftUserMap = new HashMap<String, List<Map<String, Object>>>();

	public static void main(String[] args) {
		UdpServer.StartUdpServer();
	}

	public static Map<String, List<Map<String, Object>>> getSoftUser() {
		return SoftUserMap;
	}

	public static void StartUdpServer() {
		try {
			socket = new DatagramSocket(PORT);

			new UdpThread().start();
			new UdpCheckThread().start();
			new UDPClient().start();
		} catch (Exception e) {
			logger.error("启动Udp服务异常：" + e.getMessage());
		}
	}

	public static void PushMessage(String userId, JSONObject json) {
		try {
			List<Map<String, Object>> userList = SoftUserMap.get(userId);
			if (null != userList && !userList.isEmpty()) {
				Map<String, Object> userMap = userList.get(userList.size() - 1);
				byte[] msgByte = new byte[ECHOMAX];
				byte[] bytes = null;
				DatagramPacket packet = (DatagramPacket) userMap.get("packet");
				
				bytes = strToBytes(CommonUtils.stringEncode(json.toString()));
				msgByte = new byte[ECHOMAX]; 
				System.arraycopy(bytes, 0, msgByte, 0, bytes.length);

				packet.setData(msgByte);
				socket.send(packet);
			}
		} catch (Exception e) {
			logger.error("推送消息异常：" + e.getMessage());
		}
	}
	
	public static void pushUserStatus(String currId) {
		try {
			List<Map<String, Object>> userList = null;
			Iterator<Map<String, Object>> it = null;
			Map<String, Object> userMap = null;
			String userId = null;
			JSONObject data = new JSONObject();
			data.put("type", "userStatus");
			for (Map.Entry<String, List<Map<String, Object>>> entry : SoftUserMap.entrySet()) {
				userList = (List<Map<String, Object>>) entry.getValue();
				it = userList.iterator();
				while (it.hasNext()) {
					userMap = it.next();
					userId = (String) userMap.get("userId");
					if(currId == null || !currId.equals(userId)){
						PushMessage(userId, data);
					}
				}
			}
		} catch (Exception e) {
			logger.error("推送消息异常：" + e.getMessage());
		}
	}
	
	private static byte[] strToBytes(String msg){
		ByteArrayOutputStream ostream = new ByteArrayOutputStream();  
		try {
	        DataOutputStream dataStream = new DataOutputStream(ostream);  
	        dataStream.writeUTF(msg);  
	        dataStream.flush();
	        dataStream.close();  
		} catch (Exception e) {
			try {
				return msg.getBytes("UTF-8");
			} catch (UnsupportedEncodingException e1) {
			}
		}
        return ostream.toByteArray();
	}

	private static Map<String, Object> getUserMap(String userId, String addrees) {
		return getUserMap(SoftUserMap.get(userId), addrees);
	}

	private static Map<String, Object> getUserMap(List<Map<String, Object>> userList, String addrees) {
		String adr = "";
		if (null != userList && !userList.isEmpty()) {
			for (Map<String, Object> userMap : userList) {
				adr = (String) userMap.get("address");
				if (adr.equals(addrees)) {
					return userMap;
				}
			}
		}
		return null;
	}

	private static class UdpThread extends Thread {
		public void run() {
			String content = "", address = "", userId = "";
			JSONObject json = null;
			DatagramPacket packet = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);
			List<Map<String, Object>> userList = null;
			Map<String, Object> userMap = null;
			while (true) {
				try {
					packet = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);
					socket.receive(packet);
					content = new String(packet.getData());
					json = JSONObject.parseObject(content);
					if(json.containsKey("userId")){
						userId = json.getString("userId");
						address = packet.getSocketAddress().toString();
						if (SoftUserMap.containsKey(userId)) {
							userList = SoftUserMap.get(json.getString("userId"));
							userMap = getUserMap(userList, address);
							if (userMap == null) {
								userMap = new HashMap<String, Object>();
								userList.add(userMap);
							}
							userMap.put("userId", userId);
							for(Entry<String, Object> entry : json.entrySet()){
								userMap.put(entry.getKey(), entry.getValue());
							}
							userMap.put("address", address);
							userMap.put("packet", packet);
							userMap.put("time", System.currentTimeMillis());
						} else {
							userMap = new HashMap<String, Object>();
							userMap.put("userId", userId);
							for(Entry<String, Object> entry : json.entrySet()){
								userMap.put(entry.getKey(), entry.getValue());
							}
							userMap.put("address", address);
							userMap.put("time", System.currentTimeMillis());
							userMap.put("packet", packet);
							
							userList = new ArrayList<Map<String, Object>>();
							userList.add(userMap);
							SoftUserMap.put(userId, userList);
						}
						if(json.getString("flag") != null){
							pushUserStatus(userId);
						}
					}
				} catch (Exception e) {
					logger.error("接收客户端数据报时异常：" + e.getMessage());
				}
			}
		}
	}

	private static class UdpCheckThread extends Thread {

		public void run() {
			List<Map<String, Object>> userList = null;
			long start = 0, end = 0, count = 0;
			Iterator<Map<String, Object>> it = null;
			Map<String, Object> userMap = null;
			String userId = null, address = null;
			while (true) {
				try {
					Thread.sleep(1000 * 20);
					end = System.currentTimeMillis();
					count = 0;
					for (Map.Entry<String, List<Map<String, Object>>> entry : SoftUserMap.entrySet()) {
						userList = (List<Map<String, Object>>) entry.getValue();
						it = userList.iterator();
						while (it.hasNext()) {
							userMap = it.next();
							userId = (String) userMap.get("userId");
							address = (String) userMap.get("address");
							start = (Long) userMap.get("time");
							if ((end - start) > (1000 * 60)) {
								// 超时，删除
								logger.debug("Del ..... " + userId + " - " + address);
								it.remove();
								count++;
							}
						}
					}
					if(count > 0){
						pushUserStatus(null);
					}
				} catch (Exception e) {
					logger.error("检测客户端连接时异常：" + e.getMessage());
				}
			}

		}

	}

	public static class UDPClient extends Thread {
		public void run() {
			try {
				DatagramSocket datagramSocket = new DatagramSocket();
				InetAddress address = InetAddress.getByName("127.0.0.1");
				byte[] bytes = "{}".getBytes();
				byte[] buffer = new byte[ECHOMAX];	
				System.arraycopy(bytes, 0, buffer, 0, bytes.length);
				DatagramPacket packet = null;
				while (true) {
					Thread.sleep(3000);
					// 发送数据
					packet = new DatagramPacket(buffer, buffer.length, address, PORT);
					datagramSocket.send(packet);
				}
			} catch (Exception e) {
				logger.error("Custom Client Send Error：" + e.getMessage());
			}
		}

	}

}
