package com.lscr.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.lscr.entity.App;
import com.lscr.entity.Entity;
import com.lscr.entity.Param;
import com.lscr.entity.Req;
import com.lscr.entity.Soft;
import com.lscr.entity.YQadv;

public interface RequestMapper extends SqlMapper {
	public App lscrswitch(Param param);// 判断广告开关

	// public Soft request(Param param);//请求

	public Soft getSoftAdv(Param param);// 单个产品请求

	public List<Soft> getSoftAdvMore(Param param);// 多个产品

	public Entity getEntityAdv(Param param);// 实体广告请求
	public List<Entity> getEntityAdvmore(Param param);// 实体广告请求

	public List<Req> listindex(Param param);// 查询用户展示，点击，下载，安装过的索引

	public App lscrcount(Param param);// 解锁弹窗广告次数

	public App update(Param param);// 版本更新功能（停止不用，都无需更新）

	public Soft getvalidCode(Param param);// 展示弹窗广告时的验证（停止不用，如果没有产品时，默认从头开始循环展示弹窗）

	/**
	 * 内测删除请求记录
	 */
	public int delete(Param param);

	/**
	 * 给yq展示的广告
	 * 
	 * @param param
	 * @param string
	 * @return
	 */
	public YQadv getYqSoftAdv(Param param);

	/**
	 * 黑名单应用专门推的产品
	 * 
	 * @param param
	 * @return
	 */
	public List<Soft> getblacklistMore(Param param);

	/**
	 * 获取游戏帐号的id
	 * @param param
	 * @return
	 */
	@Select("SELECT id FROM zy_app WHERE userid='e33e2e46ae504ffa89e226d8833eddb5'")
	public List<String> gameids();
	
	/**
	 * 专门查询符合条件补量的产品
	 * @param param
	 * @return
	 */
	public List<Soft> getSoftAreaAdvMore(Param param);
	
	@Update("UPDATE zy_appori_area SET times=times+1 WHERE appid=#{appid} and time=#{time} and area=#{limit} ")
	public int updateIpArea(Param param);
	@Insert("INSERT INTO zy_appori_area VALUES(#{appid},#{time},#{limit},1);")
	public int insertIpArea(Param param);//插入app地区分布方法
	
	
	
	
	@Update("UPDATE zy_ori_appnosim SET icount=#{actuser} WHERE appid=#{appid} and time=#{time} ")
	public int updateiNosim(Param param);
	@Insert("INSERT INTO zy_ori_appnosim(appid,TIME,icount)  VALUES (#{appid},#{time},#{actuser});")
	public int  insertiNosim(Param param);//插入内插屏无sim卡的用户
	
	
	
	@Update("UPDATE zy_ori_appnosim SET ocount=#{actuser} WHERE appid=#{appid} and time=#{time} ")
	public int updateoNosim(Param param);
	@Insert("INSERT INTO zy_ori_appnosim(appid,TIME,ocount) VALUES (#{appid},#{time},#{actuser});")
	public int  insertoNosim(Param param);//插入外插屏无sim卡的用户
}
