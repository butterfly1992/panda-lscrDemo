package com.lscr.entity;


/**
 * 品牌广告
 * 
 * @author Administrator
 * 
 */
public class Entity {
	private String id; // ID
	private String name; // 广告名称
	private String imgurl; // 图片地址
	private String linkurl; // 广告链接
	private String icon;
	private Integer lsindex; // 索引
	private Integer priority; // 优先级
	private Integer whatadv = 1;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
 
	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public String getLinkurl() {
		return linkurl;
	}

	public void setLinkurl(String linkurl) {
		this.linkurl = linkurl;
	}

	public Integer getLsindex() {
		return lsindex;
	}

	public void setLsindex(Integer lsindex) {
		this.lsindex = lsindex;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Integer getWhatadv() {
		return whatadv;
	}

	public void setWhatadv(Integer whatadv) {
		this.whatadv = whatadv;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	
}
