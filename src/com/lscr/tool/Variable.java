package com.lscr.tool;

import java.text.SimpleDateFormat;

public class Variable {

	public static SimpleDateFormat formats = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
	public static String errorJson = "{\"flag\":0}";
	public static String dataJson = "{\"flag\":-1}";
	public static String correntJson = "{\"flag\":1}";
	public static String updateJson = "{\"flag\":2}";
	public static String testId = "zy2860634b9e5742b2b43acc2e0a22b5f8,test";
	//宝马qq在线
	public static String blacklistId = "";
	public static String yqId = "1ae2c3d45e6b2dc85993ab36a676bc2b,d6cab8caa8e4442ba1b97bebb4dfe651";
	// 863166011518029--王翠，356512057050126--霍金龙，355056050219792--松姐，864133029488958--丁尚亮,358071043359917-程革
	public static String invalidImei = "356512057050126,358071043359917";
	public static String validJson = "{\"flag\":-1}";// 没有机会时，返回标识
	public static String infiniteId = "37ca437ed39b45f1b4eef68457d3b7b3";//不限制次数出广告的id
	public static String errorImsi="012345678901234,008576785051456,000000000000000,123456789012345,079307407276373";
}
