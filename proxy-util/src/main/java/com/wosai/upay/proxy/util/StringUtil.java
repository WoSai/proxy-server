package com.wosai.upay.proxy.util;

public class StringUtil {
	
	/**
	 * 比较两个字符串是否相等
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean equals(String a,String b){
		return a!=null&&b!=null&&a.equals(b);
	}
	
	/**
	 * 验证字符串是否空字符串
	 * @param str
	 * @return
	 */
	public static boolean empty(String str){
		return str==null||str.trim().length()==0;
	}
}
