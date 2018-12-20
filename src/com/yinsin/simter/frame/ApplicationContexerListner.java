package com.yinsin.simter.frame;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

@SuppressWarnings("rawtypes")
public class ApplicationContexerListner implements ApplicationListener {

	public static final Log log = LogFactory.getLog(ApplicationContexerListner.class);

	@Resource(name = "appContext")
	private ApplicationContextUtil appContext;

	public ApplicationContextUtil getAppContext() {
		return appContext;
	}

	public void onApplicationEvent(ApplicationEvent applicationevent) {
		if (applicationevent instanceof ContextRefreshedEvent) {
			try {

			} catch (Exception e) {
				log.info("ApplicationContexerListner Spring监听器启动失败...........");
			}
			// 初始化消息监听器
			// this.getAppContext().getContext().getBean("messageContainer");
			// this.getAppContext().getContext().getBean("realMessageContainer");
		}

	}
}
