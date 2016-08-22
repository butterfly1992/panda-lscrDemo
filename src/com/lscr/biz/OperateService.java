package com.lscr.biz;

import com.lscr.entity.Param;
/**
 * 操作业务类
 * @author Administrator
 *
 */
public interface OperateService {
	/**
	 * 操作类，返回操作结果
	 * @param param
	 * @return
	 */
	public Object getOperResult(Param param);

	/**
	 * 根据索引获取id 
	 * @param advindex
	 * @return
	 */
	public String getReqsoftId(String advindex);

}
