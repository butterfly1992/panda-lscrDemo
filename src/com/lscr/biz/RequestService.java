package com.lscr.biz;

import com.lscr.entity.Param;

/**
 * 请求业务类
 * 
 * @author Administrator
 * 
 */
public interface RequestService {
	/**
	 * 判断应用是否开启接口开关
	 * 
	 * @param param
	 * @return
	 */
	public boolean lscrInterfaceswitch(Param param);

	public int lscrcount(Param param);

	/**
	 * 判断应用是否开启解锁开关
	 * 
	 * @param param
	 * @return
	 */
	public boolean lscrnoInterfaceswitch(Param param);

	/**
	 * 返回请求结果
	 * 
	 * @param param
	 * @return
	 */
	public Object getResult(Param param);

	/**
	 * 测试返回数据
	 * 
	 * @param param
	 * @return
	 */
	public Object getTestResult(Param param);

	/**
	 * 测试升级版，返回多个产品
	 * 
	 * @param param
	 * @return
	 */
	public Object getTestResultmore(Param param);

	/**
	 * 内测使用删除请求记录
	 * 
	 * @param param
	 * @return
	 */
	public int delete(Param param);

	/**
	 * 移趣广告
	 * 
	 * @param param
	 * @return
	 */
	public Object getYQResult(Param param);

	/**
	 * 黑名单固定返回数据测试
	 * 
	 * @param param
	 * @return
	 */
	public Object getBlacklistResult(Param param);

	/**
	 * 验证是否为游戏账户的id
	 * @param appid
	 * @return
	 */
	public boolean validGameId(Param param);

}
