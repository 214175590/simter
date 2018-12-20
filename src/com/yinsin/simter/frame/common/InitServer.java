package com.yinsin.simter.frame.common;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class InitServer implements ServletContextListener {

	public void contextDestroyed(ServletContextEvent arg0) {
	}

	public void contextInitialized(ServletContextEvent arg0) {
		// 加载系统配置
		ConfigManager.getInstance().initServletContext(arg0.getServletContext());
		
		//UdpServer.StartUdpServer();
	}

}