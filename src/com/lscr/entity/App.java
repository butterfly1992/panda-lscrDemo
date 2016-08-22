package com.lscr.entity;

/**
 * 开发者应用
 * @author Administrator
 *
 */
public class App {
	private String id;
	private String userid;
	private String name;
	private Integer lscr_out_count;//解锁弹出次数
	private Integer lscr_in_count;//应用内广告次数
	private Integer lscr_in_status;//应用内开关
	private Integer lscr_out_status;//应用外开关
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
 
 
	public Integer getLscr_out_count() {
		return lscr_out_count;
	}
	public void setLscr_out_count(Integer lscr_out_count) {
		this.lscr_out_count = lscr_out_count;
	}
	public Integer getLscr_in_status() {
		return lscr_in_status;
	}
	public void setLscr_in_status(Integer lscr_in_status) {
		this.lscr_in_status = lscr_in_status;
	}
	public Integer getLscr_out_status() {
		return lscr_out_status;
	}
	public void setLscr_out_status(Integer lscr_out_status) {
		this.lscr_out_status = lscr_out_status;
	}
	public Integer getLscr_in_count() {
		return lscr_in_count;
	}
	public void setLscr_in_count(Integer lscr_in_count) {
		this.lscr_in_count = lscr_in_count;
	}
	

}
