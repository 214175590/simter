package com.yinsin.simter.controller.chat;

import com.yinsin.simter.frame.SpringContextHolder;
import com.yinsin.simter.service.base.IBaseService;

public class BaseHandler {
	
	private static IBaseService service;
	
	public synchronized IBaseService getBaseService(){
		if(service == null){
			service = SpringContextHolder.getBean("baseServiceImpl");
		}
		return service;
	}
	
}
