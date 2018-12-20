/*
 * @author yisin
 * @date 2012 2012-11-29
 */
package com.yinsin.simter.frame.common;

/**
 * <pre>
 * 提供获取上下文名称，服务器根目录等系统资源，可继续扩展
 * </pre>
 * 
 * @author yisin
 * @date 2012 2012-11-29
 * 
 */
public class RootContext {
	/**
	 * 应用程序上下文名称
	 */
	private static String rootName;

	/**
	 * 程序目录名称
	 */
	private static String projectName;

	/**
	 * 应用程序根目录路径
	 */
	private static String rootPath;

	/**
	 * @return the rootName
	 */
	public static String getRootName() {
		if (rootName == null) {
			rootName = "";
		}
		return rootName;
	}

	/**
	 * @param rootName
	 *            the rootName to set
	 */
	public static void setRootName(String rootName) {
		RootContext.rootName = rootName;
	}

	/**
	 * @return the rootPath
	 */
	public static String getRootPath() {
		return rootPath;
	}

	/**
	 * @param rootPath
	 *            the rootPath to set
	 */
	public static void setRootPath(String rootPath) {
		RootContext.rootPath = rootPath;
	}

	/**
	 * @param projectName
	 *            the projectName to set
	 */
	public static void setProjectName(String projectName) {
		RootContext.projectName = projectName;
	}

	/**
	 * @return the projectName
	 */
	public static String getProjectName() {
		return projectName;
	}

}
