package com.yinsin.simter.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;

import com.yinsin.http.HttpRequest;
import com.yinsin.security.MD5;
import com.yinsin.utils.CommonUtils;

public class NetUtils {
	private final static Logger logger = Logger.getLogger(NetUtils.class);

	public static void download(String urlString, String filename, String savePath) throws Exception {
		// 构造URL
		URL url = new URL(urlString);
		// 打开连接
		URLConnection con = url.openConnection();
		// 设置请求超时为10s
		con.setConnectTimeout(10 * 1000);
		// 输入流
		InputStream is = con.getInputStream();

		// 1K的数据缓冲
		byte[] bs = new byte[1024];
		// 读取到的数据长度
		int len;
		// 输出的文件流
		File sf = new File(savePath);
		if (!sf.exists()) {
			sf.mkdirs();
		}
		OutputStream os = new FileOutputStream(sf.getPath() + "\\" + filename);
		// 开始读取
		while ((len = is.read(bs)) != -1) {
			os.write(bs, 0, len);
		}
		// 完毕，关闭所有链接
		os.close();
		is.close();
	}
	
	public static byte[] download(String urlString) throws Exception {
		// 构造URL
		/*URL url = new URL(urlString);
		// 打开连接
		URLConnection con = url.openConnection();
		// 设置请求超时为10s
		con.setConnectTimeout(10 * 1000);
		con.addRequestProperty("header", "Referer=http://www.qylsp4.com/");
		// 输入流
		InputStream is = con.getInputStream();
		
		// 1K的数据缓冲
		byte[] bs = new byte[1024];
		// 读取到的数据长度
		int len;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while ((len = is.read(bs)) != -1) {  
			baos.write(bs, 0, len);  
        }
		is.close();
		// 完毕，关闭所有链接
		return baos.toByteArray();*/
		return HttpRequest.get(urlString).bytes();
	}
	
	public static String write(byte[] buf, String filename, String savePath) throws Exception {
		String filepath = null;
		try {
			// 输出的文件流
			filepath = savePath + "/" + filename;
			String sourcePath = savePath + "/" + "images/movies/" + CommonUtils.getUUID() + ".jpg";
			
			File file = new File(sourcePath);
			file.setWritable(true, false);
			OutputStream os = new FileOutputStream(sourcePath);
			os.write(buf);
			// 完毕，关闭所有链接
			os.flush();
			os.close();
			
			ImageHelper.scaleImage(sourcePath, filepath, 180, 290, "jpg");
			file.delete();
			
		} catch (Exception e) {
			filepath = null;
			logger.error("图片保存失败：" + e.getMessage(), e);
		}
		return filepath;
	}

}
