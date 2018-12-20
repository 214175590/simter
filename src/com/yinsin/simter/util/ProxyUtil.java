package com.yinsin.simter.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.yinsin.utils.CommonUtils;

public class ProxyUtil {
	
	public static Map<String, TkBean> tkMap = new HashMap<String, TkBean>();
	private static long longtime = 1000 * 60;
	private static int runcount = 2;
	private static Object lock = new Object();
	private static boolean isrun = false;
	
	public static String createTk(){
		String tk = "TK" + CommonUtils.getRandomNumber(10);
		tkMap.put(tk, new TkBean());
		return tk;
	}
	
	public static boolean validateTk(String tk){
		boolean r = false;
		try {
			if (CommonUtils.isNotBlank(tk)) {
				TkBean bean = tkMap.get(tk);
				long end = System.currentTimeMillis();
				if ((end - bean.getTime()) < longtime && bean.getCount() < runcount) {
					r = true;
					bean.setCount(bean.getCount() + 1);
				} else {
					tkMap.remove(tk);
				}
			}
			synchronized (lock) {
				if(!isrun){
					isrun = true;
					new Thread(new ValidateThread()).start();
				}
			}
		} catch (Exception e) {
		}
		return r;
	}
	
	public static void main(String[] args) {
		//&bd__=http%3A%2F%2Fwww.baidu.com%2Flink%3Furl%3DOpoZj026BQVgGcwyqGFxnXPeRir43R7SDuq4zaBmsSnZLCMKVJB4hYPbVWjU3OHzNBulwnbuc0R6N4zy3g7hHa
		String tk = "9B940E4DF91D90FAF25D48A17D5C6015";
		System.out.println(tk.length() + "-" + validateTk(tk));	
		tk = "D7BCA05BA13A7F098A2C40ED015F6CF5";
		System.out.println(tk.length() + "-" + validateTk(tk.toLowerCase()));	
	}
	
	static class ValidateThread implements Runnable {
		private static long millis = 1000 * 60 * 60;
		public void run() {
			long end = 0;
			while(true){
				end = System.currentTimeMillis();
				for(Entry<String, TkBean> entry : tkMap.entrySet()){
					if ((end - entry.getValue().getTime()) > longtime || entry.getValue().getCount() >= runcount) {
						tkMap.remove(entry.getKey());
					}
				}
				try {
					Thread.sleep(millis);
				} catch (InterruptedException e) {
				}
			}
		}
	}
	
	static class TkBean {
		private long time;
		private int count;
		public TkBean(){
			this.time = System.currentTimeMillis();
			this.count = 0;
		}
		public long getTime() {
			return time;
		}
		public void setTime(long time) {
			this.time = time;
		}
		public int getCount() {
			return count;
		}
		public void setCount(int count) {
			this.count = count;
		}
	}
	
}
