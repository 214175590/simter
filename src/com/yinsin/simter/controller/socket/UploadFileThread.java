package com.yinsin.simter.controller.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.yisin.commun.modal.Client;
import com.yisin.commun.modal.DataPackage;

public class UploadFileThread implements Runnable {
	private final static Logger logger = LoggerFactory.getLogger(UploadFileThread.class);
	
	public double percent = 0.00;
	private long millis = 50;

	private Client client;
	private String fileCode;

	public UploadFileThread(Client client, String fileCode) {
		this.client = client;
		this.fileCode = fileCode;
	}

	@Override
	public void run() {
		if(null != client){
			try {
				long startTime = System.currentTimeMillis();
				long endTime = System.currentTimeMillis();
				JSONObject body = new JSONObject();
				body.put("fileCode", fileCode);
				DataPackage dataPackage = new DataPackage();
				dataPackage.setReq("linux.upload");
				
				int proce = 0;
				while (percent < 1.0 && (endTime - startTime < (1000 * 60 * 5))) {
					try {
						endTime = System.currentTimeMillis();
						proce = (int) (percent * 100);
						body.put("percent", proce);
						dataPackage.setData(body);
						client.send(dataPackage);
						Thread.sleep(millis);
					} catch (Exception e) {
					}
				}
				body.put("percent", 100);
				dataPackage.setData(body);
				boolean result;
				result = client.send(dataPackage);
				logger.debug("文件上传进度完成："+ percent + " - " + fileCode + "====>" + result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
