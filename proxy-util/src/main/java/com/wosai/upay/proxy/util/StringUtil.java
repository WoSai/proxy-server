package com.wosai.upay.proxy.util;

public class StringUtil {
	
	public static final String CODE_CHAR = "0";
	
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
	
	/**
	 * 获取数字对应的字符串编号，以0补全
	 * @param i
	 * @param length
	 * @return
	 */
	public static String getCodeByNum(Long value,int length){
		StringBuilder sb = new StringBuilder().append(value);
		while(sb.length()<length){
			sb.insert(0, CODE_CHAR);
		}
		return sb.toString();
	}
}
