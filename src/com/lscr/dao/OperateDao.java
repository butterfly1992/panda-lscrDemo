package com.lscr.dao;

import java.util.List;

import com.lscr.entity.Param;
import com.lscr.entity.Req;

public interface OperateDao {
	/**
	 * 请求结果对象
	 * @param param
	 * @return
	 */
	public Object getOperResult(Param param);//

	/**
	 * 获取请求过的索引
	 * @param param
	 * @return
	 */
	public List<Req> getIndexs(Param param);//

	/**
	 * 记录用户索引
	 * @param 请求参数
	 * @return
	 */
	public int insertIndex(Req req);//
	/**
	 * 更新用户超时索引
	 * @param 请求参数
	 * @return
	 */
	public int updateTimeoutIndex(Req req);//
	/**
	 * 更新有效期内的用户索引
	 * @param 请求参数
	 * @return
	 */
	public int updateNoovertimeIndex(Param param);
	public int insertOper(Param param);//插入操作方法
	public int updateOper(Param param);//更新操作方法

	public int insertuserlook(Param param);//插入用户展示量

	public int updateuserlook(Param param);//更新用户展示量
	
	public String getReqsoftId(String advindex);

	/**
	 * 更新无卡用户安装操作记录
	 * @param param 请求参数
	 * @return
	 */
	public int updatesNosim(Param param);
	/**
	 * 插入无卡用户安装操作记录
	 * @param param 请求参数
	 * @return
	 */
	public int  insertsNosim(Param param);//插入无sim卡的用户
}
