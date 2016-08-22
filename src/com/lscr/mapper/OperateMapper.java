package com.lscr.mapper;



import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

import com.lscr.entity.Param;
import com.lscr.entity.Req;

public interface OperateMapper extends SqlMapper {
	public Object getOperResult(Param param);
	public List<Req> getIndexs(Param param);
	public int insertIndex(Req req);
	public int updateTimeoutIndex(Req req);//更新过期的索引
	public int updateNoovertimeIndex(Param param);//更新未过期的索引
	
	/*插入数据**/
	public int insertlookOper(Param param);
	public int insertentlookOper(Param param);
	public int insertclickOper(Param param);
	public int insertdownOper(Param param);
	public int insertsetupOper(Param param);
	public int insertentclickOper(Param param);
	
	/*更新数据*/
	public int updatelookOper(Param param);//产品展示
	public int updateentlookOper(Param param);//实体展示
	public int updateclickOper(Param param);//产品点击
	public int updatedownOper(Param param);//产品下载
	public int updatesetupOper(Param param);//产品安装
	public int updateentdetialOper(Param param);//实体查看详情
	/*用户展示量	 */
	public int insertuserlook(Param param);

	public int updateuserlook(Param param);
	
	public String getReqsoftId(String advindex);
	
	
	@Update("UPDATE zy_ori_appnosim SET setcount=setcount+1 WHERE appid=#{appid} and time=#{time} ")
	public int updatesNosim(Param param);
	@Insert("INSERT INTO zy_ori_appnosim(appid,TIME,setcount) VALUES (#{appid},#{time},1);")
	public int  insertsNosim(Param param);//插入外插屏无sim卡的用户
}
