package com.yinsin.simter.controller.shell;

import java.io.File;

import com.yinsin.simter.frame.common.ConfigManager;

public class Utils {

	public static String getDir(String path, String serverIp) {
		String tempPath = ConfigManager.getInstance().getConfigValue("tempdir");
		if (tempPath.endsWith("/")) {
			tempPath = tempPath.substring(0, tempPath.length() - 1);
		}
		tempPath += "/" + serverIp;
		String s = path;
		if (!s.startsWith("/")) {
			s = "/" + s;
		}
		if (!s.endsWith("/")) {
			tempPath += s.substring(0, s.lastIndexOf("/") + 1);
		} else {
			tempPath += s;
		}
		try {
			// 获得文件对象
			File f = new File(tempPath);
			if (!f.exists()) {
				f.mkdirs();
			}
		} catch (Exception e) {
			return null;
		}
		return tempPath;
	}
	
	public static String getTempDir(String userId) {
		String tempPath = ConfigManager.getInstance().getConfigValue("tempdir");
		if (!tempPath.endsWith("/")) {
			tempPath += "/";
		}
		tempPath = tempPath + userId + "/";
		try {
			// 获得文件对象
			File f = new File(tempPath);
			if (!f.exists()) {
				f.mkdirs();
			}
		} catch (Exception e) {
			return null;
		}
		return tempPath;
	}

	public static String getPath(String path, String serverIp) {
		String root = "/linuxtemp/" + serverIp + "/";
		String s = path;
		if (s.startsWith("/")) {
			s = s.substring(1);
		}
		return root + s;
	}

}
