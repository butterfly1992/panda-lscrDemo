package com.lscr.entity;

/**
 * 请求参数
 * 
 * @author Administrator
 * 
 */
public class Param {
	private Integer id;
	private String imei;// 手机串码
	private String imsi;// 手机卡串码
	private String appid;// 应用id
	private String gysdkv;// sdk版本
	private String pck;// 游戏包名
	private String softid;// 产品id
	private int priority;// 优先级
	private String advindex;// 产品索引
	private String oper;// 操作
	private Integer lscode;
	private Integer mode;// 应用内还是应用外弹出广告标识（0：应用内，1：应用外）
	private String time;
	private Integer operation;
	private Integer limit;//升级后，限制弹出产品数目

	
	//ip信息+区域
	private String ip;
	private String area;
	
	
	//路径加密key
	private String key;
	
	//统计无卡用户量
	private int actuser;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getGysdkv() {
		return gysdkv;
	}

	public void setGysdkv(String gysdkv) {
		this.gysdkv = gysdkv;
	}

	public String getPck() {
		return pck;
	}

	public void setPck(String pck) {
		this.pck = pck;
	}

	public String getSoftid() {
		return softid;
	}

	public void setSoftid(String softid) {
		this.softid = softid;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getAdvindex() {
		return advindex;
	}

	public void setAdvindex(String advindex) {
		this.advindex = advindex;
	}

	public String getOper() {
		return oper;
	}

	public void setOper(String oper) {
		this.oper = oper;
	}

	public Integer getLscode() {
		return lscode;
	}

	public void setLscode(Integer lscode) {
		this.lscode = lscode;
	}

	public Integer getMode() {
		return mode;
	}

	public void setMode(Integer mode) {
		this.mode = mode;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Integer getOperation() {
		return operation;
	}

	public void setOperation(Integer operation) {
		this.operation = operation;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getActuser() {
		return actuser;
	}

	public void setActuser(int actuser) {
		this.actuser = actuser;
	}
	

	
}
