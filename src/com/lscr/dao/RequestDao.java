package com.lscr.dao;

import java.util.List;

import com.lscr.entity.Entity;
import com.lscr.entity.Param;
import com.lscr.entity.Req;
import com.lscr.entity.Soft;
import com.lscr.entity.YQadv;

public interface RequestDao {
	public boolean lscrInterfaceswitch(Param param);//接口开关

	public boolean lscrnoInterfaceswitch(Param param);//解锁开关


	public int lscrcount(Param param);//解锁弹出次数

	/**
	 * 获取产品广告
	 * 
	 * @param param
	 * @return
	 */
	public Soft getSoftAdv(Param param);
	/**
	 * 获取多个产品广告
	 * 
	 * @param param
	 * @return
	 */
	public List<Soft> getSoftAdvMore(Param param);

	/**
	 * 获取实体广告
	 * 
	 * @param param
	 * @return
	 */
	public Entity getEntityAdv(Param param);
	/**
	 * 多个实体广告
	 * @param param
	 * @return
	 */
	public List<Entity> getEntityAdvmore(Param param);// 实体广告请求
	/**
	 * 内测删除请求记录
	 */
	public int delete(Param param);

	/**
	 * 移趣互推的产品
	 * @param param
	 * @param string
	 * @return
	 */
	public YQadv getSoftAdv(Param param, String string);

	/**
	 * 黑名单应用专门推的产品
	 * @param param
	 * @return
	 */
	public List<Soft> getblacklistMore(Param param);
	
	/**
	 * 查出游戏账户的id
	 * @param param
	 * @return
	 */
	public List<String> validGameId();

	/**
	 * 专门用于查询补量的符合条件的产品
	 * 
	 * @param param
	 * @return
	 */
	public List<Soft> getSoftAreaAdvMore(Param param);
	
	/**
	 * 更新用户ip地区解析记录
	 * @param param
	 * @return
	 */
	public int updateIpArea(Param param);
	/**
	 * 插入用户ip地区鸡西记录
	 * @param param
	 * @return
	 */
	public int insertIpArea(Param param);//插入app地区分布方法

	/**
	 * 更新无卡用户请求记录
	 * @param param 请求参数数据，appid,time,actuser
	 * @param mode_flag 插屏展示表示，0应用内，1或2表示应用外
	 * @return
	 */
	public int updateNosim(Param param,int mode_flag);
	/**
	 * 插入无卡用户请求记录 
	 * @param param 请求参数数据，appid,time,actuser
	 * @param mode_flag 插屏展示表示，0应用内，1或2表示应用外
	 * @return
	 */
	public int  insertNosim(Param param,int mode_flag);//插入无sim卡的用户
}
