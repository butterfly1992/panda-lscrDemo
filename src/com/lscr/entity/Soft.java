package com.lscr.entity;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * 产品广告
 * 
 * @author Administrator
 * 
 */
@JsonIgnoreProperties(value={"lscode","softtype","province"})
public class Soft implements Serializable {

	/**
	 * 
	 */
	private String id;
	private String name;
	private String apkurl;
	private int lsindex;
	private String pck;
	private String imgurl;
	private String icon;
	private Integer category;
	private Integer whatadv = 0;
	private Integer lscode;
	private int softtype=0;// 产品类型：0全国投放，2多省投放
	private String province;//多省

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

	public String getApkurl() {
		return apkurl;
	}

	public void setApkurl(String apkurl) {
		this.apkurl = apkurl;
	}

	public String getPck() {
		return pck;
	}

	public void setPck(String pck) {
		this.pck = pck;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Integer getWhatadv() {
		return whatadv;
	}

	public void setWhatadv(Integer whatadv) {
		this.whatadv = whatadv;
	}

	public Integer getLscode() {
		return lscode;
	}

	public void setLscode(Integer lscode) {
		this.lscode = lscode;
	}

	public int getLsindex() {
		return lsindex;
	}

	public void setLsindex(int lsindex) {
		this.lsindex = lsindex;
	}

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public Integer getCategory() {
		return category;
	}

	public void setCategory(Integer category) {
		this.category = category;
	}

	public int getSofttype() {
		return softtype;
	}

	public void setSofttype(int softtype) {
		this.softtype = softtype;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

}
