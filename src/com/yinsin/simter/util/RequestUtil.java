package com.yinsin.simter.util;

import javax.servlet.http.HttpServletRequest;

import com.yinsin.simter.frame.consts.Constants;
import com.yinsin.utils.CommonUtils;

public class RequestUtil {
	/**
	 * 获取HTTP请求的ip地址
	 * @author yisin
	 * @param request
	 * @return String
	 */
	public static String getIpAddr(HttpServletRequest request) {
		if (CommonUtils.isEmpty(request)) {
			throw new IllegalArgumentException("HttpServletRequest is null.");
		}
		String ip = request.getHeader(Constants.X_FORWARD_FOR);
		if (CommonUtils.isBlank(ip) || Constants.STRING_UNKNOW.equalsIgnoreCase(ip)) {
			ip = request.getHeader(Constants.PROXY_CLIENT_IP);
			if (CommonUtils.isBlank(ip)) {
				ip = request.getHeader(Constants.WL_PROXY_CLIENT_IP);
			}
			if (CommonUtils.isBlank(ip)) {
				ip = request.getRemoteAddr();
			}
		}
		if (CommonUtils.isBlank(ip) && ip.replace(" ", "").indexOf(",") != -1) {
			return ip.substring(0, ip.indexOf(","));
		}
		return ip;
	}

	/**
	 * 是否是ajax请求，true:是,false:否
	 * 
	 * @author yisin
	 * @param request
	 * @return boolean
	 * 
	 */
	public static boolean isAjaxRequest(HttpServletRequest request) {
		return CommonUtils.isNotBlank(request.getHeader(Constants.X_REQUESTED_WITH))
				&& request.getHeader(Constants.X_REQUESTED_WITH).equalsIgnoreCase(Constants.XML_HTTP_REQUEST);
	}
}
