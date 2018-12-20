/*
 * 简述：用于管理配置信息的初始化
 * 详述：管理配置初始化并得到配置信息，提供获取配置的方法
 * 修改内容：[新增]
 * 修改时间：2012-12-11
 * 修改人：yisin
 * 
 */
package com.yinsin.simter.frame.common;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Element;

import com.yinsin.simter.util.XmlParser;
import com.yinsin.utils.CommonUtils;

/**
 * <pre>
 * 简述:用于管理配置信息的初始化
 * 详述:管理配置初始化并得到配置信息，提供获取配置的方法
 * </pre>
 * 
 * @author yisin
 * @date 2012-12-11下午 13:44:55
 * 
 */
public class ConfigManager {
	private static final Logger logger = Logger.getLogger(ConfigManager.class);
	private static String XML_PATH = "/WEB-INF/config/system-config.xml";

	// 构造PortalConfigManager对象
	private static ConfigManager cfgMgr = new ConfigManager();
	private static final Map<String, Map<String, String>> configMaps = new HashMap<String, Map<String, String>>();

	/**
	 * 私有构造器
	 */
	private ConfigManager() {
	}

	/**
	 * <pre>
	 * 获取PortalConfigManager对象
	 * </pre>
	 * 
	 * @since 1.0
	 * 
	 */
	public static ConfigManager getInstance() {
		return cfgMgr;
	}

	public void initServletContext(ServletContext servletContext) {
		// 得到根目录信息
		String contextPath = servletContext.getRealPath("/");
		logger.info("Context Path = " + contextPath);
		if (contextPath.endsWith(File.separator)) {
			contextPath = contextPath.substring(0, contextPath.length() - 1);
			contextPath = contextPath.replace(File.separatorChar, '/');
		}
		// 设置根目录信息
		String rootName = servletContext.getContextPath();
		String projectName = rootName;
		if (CommonUtils.isEmpty(projectName)) {
			projectName = contextPath;
			if (projectName.substring(projectName.length() - 1).equals("/")) {
				projectName = projectName.substring(0, projectName.length() - 1);
			}
			projectName = projectName.substring(projectName.lastIndexOf("/"));
		}
		if (projectName.startsWith("/")) {
			projectName = projectName.substring(1);
		}
		RootContext.setRootPath(contextPath);
		// 设置虚拟目录名称
		RootContext.setRootName(rootName);
		RootContext.setProjectName(projectName);
		logger.info("RootName = " + rootName);
		logger.info("RootPath = " + contextPath);
		logger.info("ProjectName = " + projectName);
		logger.info("Setting the Context information success.." + RootContext.getRootName());

		initSystemConfig();
	}

	/**
	 * 初始化配置
	 */
	private void initSystemConfig() {
		try {
			String xmlPath = RootContext.getRootPath() + XML_PATH;
			Element root = XmlParser.getRootNode(xmlPath);
			List<Element> nodeList = XmlParser.getChildList(root);
			if (nodeList != null) {
				Element node = null;
				List<Attribute> attrList = null;
				Map<String, String> attrMap = null;
				String key = "";
				for (int i = 0, k = nodeList.size(); i < k; i++) {
					node = nodeList.get(i);
					attrList = XmlParser.getAttributeList(node);
					if (attrList != null) {
						attrMap = new HashMap<String, String>();
						for (Attribute attr : attrList) {
							if (attr.getName().equals("name")) {
								key = attr.getValue();
							}
							attrMap.put(attr.getName(), attr.getValue());
						}
						configMaps.put(key, attrMap);
					}
				}
			}
			logger.info("init the system-config information Success.");
		} catch (Exception e) {
			logger.error("init the system-config failed", e);
		}
	}

	/**
	 * 获取配置对象
	 * 
	 * @param name
	 * @return
	 */
	public Map<String, String> getConfig(String name) {
		if (configMaps.containsKey(name)) {
			return configMaps.get(name);
		}
		return null;
	}

	/**
	 * 获取配置项value值
	 * 
	 * @param name
	 * @return value
	 */
	public String getConfigValue(String name) {
		return getConfigValue(name, "value");
	}

	/**
	 * 获取配置项的属性值
	 * 
	 * @param name
	 * @param attr
	 * @return 属性的值
	 */
	public String getConfigValue(String name, String attr) {
		Map<String, String> map = getConfig(name);
		if (map != null) {
			return map.get(attr);
		}
		return null;
	}

}
