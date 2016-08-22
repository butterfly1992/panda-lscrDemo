package com.lscr.biz.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.danga.MemCached.MemCachedClient;
import com.lscr.biz.OperateService;
import com.lscr.dao.OperateDao;
import com.lscr.entity.Param;
import com.lscr.entity.Req;
import com.lscr.tool.MemcacheUtil;
import com.lscr.tool.Utils;
import com.lscr.tool.Variable;

@Service("operservice")
@Transactional
public class OperateServiceImpl implements OperateService {

	private OperateDao operatedao;
	private MemCachedClient mcc = MemcacheUtil.mcc;
	// 0---APK展示---1---ENTITY展示----2----点击下载----3----下载完成----4----安装完成----5----entity查看详情
	@Override
	public Object getOperResult(Param param) {
		// TODO Auto-generated method stub
		String time = Utils.DateTime();
		param.setTime(time);
		if (imsinosimFilter(param)) {
			return Variable.errorJson;
		}
		String oper = param.getOper();
		int flag = 0;
		if (oper.equals("0")) {// apk展示
			flag = operatedao.updateOper(param);
			if (flag == 0) {
				flag = operatedao.insertOper(param);
			}
			/*
			 * if (flag > 0) {// 记录用户展示量 flag =
			 * operatedao.updateuserlook(param); if (flag != 1) { flag =
			 * operatedao.insertuserlook(param); } }
			 */
			if (flag > 0)
				return Variable.correntJson;
			else
				return Variable.errorJson;
		} else if (oper.equals("1")) {// 广告展示
			/*
			 * List<Req> reqs = null; reqs = operatedao.getIndexs(param); Req
			 * req = new Req(); req.setImei(param.getImei());
			 * req.setImsi(param.getImsi());
			 * req.setEntityindex(param.getAdvindex()); req.setTime(time);//
			 * 当前时间 if (reqs.size() == 0) { flag = operatedao.insertIndex(req);
			 * } else {// 品牌广告判断是否展示过 String entindex =
			 * reqs.get(0).getEntityindex(); String date_time =
			 * reqs.get(0).getTime(); if (isContain(entindex,
			 * param.getAdvindex())) { // 没有展示过 此处不执行 return Variable.errorJson;
			 * } else { req.setTime_flag(date_time);// 之前时间 flag =
			 * operatedao.updateNoovertimeIndex(req); } } param.setTime(time);
			 */
			flag = operatedao.updateOper(param);
			if (flag == 0) {
				flag = operatedao.insertOper(param);
			} else {
				return Variable.errorJson;
			}
		} else if (oper.equals("2")) {// 点击
			flag = operatedao.updateOper(param);
			if (flag == 0) {
				flag = operatedao.insertOper(param);
			}
			if (flag > 0)
				return Variable.correntJson;
			else
				return Variable.errorJson;
		} else if (oper.equals("3")) {// 下载
			flag = operatedao.updateOper(param);
			if (flag == 0) {
				flag = operatedao.insertOper(param);
				Utils.log.debug("插入下载……" + flag);
			}
			if (flag > 0)
				return Variable.correntJson;
			else
				return Variable.errorJson;
		} else if (oper.equals("4")) {// 安装
			List<Req> reqs = null;
			reqs = operatedao.getIndexs(param);
			if (reqs.size() == 0) {
				// 没有展示过 此处不执行
				Req req = new Req();
				req.setImei(param.getImei());
				req.setImsi(param.getImsi());
				req.setSetupindex(param.getAdvindex());
				req.setTime(time);// 当前时间
				flag = operatedao.insertIndex(req);
				flag = 1;
			} else {
				String setupindex = reqs.get(0).getSetupindex();
				String date_time = reqs.get(0).getTime();
				if (isContain(setupindex, param.getAdvindex())) {
					// 安装过 此处不执行
					return Variable.errorJson;
				} else {
					param.setTime(date_time);// 当前时间
					flag = operatedao.updateNoovertimeIndex(param);
				}
			}
			param.setTime(time);
			if (flag == 1) {
				flag = operatedao.updateOper(param);
				if (flag != 1) {
					flag = operatedao.insertOper(param);
					Utils.log.debug("插入安装……" + flag);
				}
			}
			if (flag > 0)
				return Variable.correntJson;
			else
				return Variable.errorJson;
		} else if (oper.equals("5")) {
			List<Req> reqs = null;
			reqs = operatedao.getIndexs(param);
			if (reqs.size() == 0) {
				Req req = new Req();
				req.setImei(param.getImei());
				req.setImsi(param.getImsi());
				req.setClickindex(param.getAdvindex());
				req.setTime(time);// 当前时间
				flag = operatedao.insertIndex(req);
				flag = 1;
			} else {
				String entindex = reqs.get(0).getClickindex();
				String date_time = reqs.get(0).getTime();
				if (isContain(entindex, param.getAdvindex())) {
					// 没有展示过 此处不执行
					return Variable.errorJson;
				} else {
					param.setTime(date_time);// 之前时间
					flag = operatedao.updateNoovertimeIndex(param);
				}
			}
			param.setTime(time);
			if (flag == 1) {
				flag = operatedao.updateOper(param);
				if (flag != 1) {
					flag = operatedao.insertOper(param);
				}
			}
		}
		if (flag > 0) {
			return Variable.correntJson;
		} else
			return Variable.errorJson;
	}

	/**
	 * 用户手机卡串码无手机卡时拦截过滤
	 * @param param
	 * @return
	 */
	private boolean imsinosimFilter(Param param) {
		// TODO Auto-generated method stub
		if (param.getImsi().equals("12345678912345")) {// 无手机卡用户继续判断
			if (param.getOper().equals("4")) {//判断是否为安装操作，
//				if (operatedao.updatesNosim(param) == 0) {//更新无卡用户安装记录
//					operatedao.insertsNosim(param);//插入无卡用户安装记录
//					return true;
//				}
				int setcount=0;
				if (mcc.get("nlscr_nosimAn" + param.getAppid()) == null) {
					setcount=1;
				}else{
					setcount=Integer.parseInt(mcc.get("nlscr_nosimAn" + param.getAppid()).toString())+1;
				}
				Date expiryd = getDefinedDateTime(23, 59, 59, 0);// 设置失效时间
				mcc.set("nlscr_nosimAn" + param.getAppid(), setcount, expiryd);
				Utils.log.error(param.getAppid()+",Tappid,0,Fappid,0,"+setcount+","+ param.getImei()+","+ param.getImsi()+",0");
			} else {
				return true;// 无卡其他操作,不继续操作统计
			}
			return true;
		} else {// 有手机卡串码，可以继续操作统计
			return false;
		}
	}

	@Autowired
	public void setOperatedao(OperateDao operatedao) {
		this.operatedao = operatedao;
	}

	public boolean isContain(String s, String c) {
		String a[] = s.split(",");
		for (int i = 0; i < a.length; i++) {
			if (a[i].equals(c)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getReqsoftId(String advindex) {
		// TODO Auto-generated method stub
		return operatedao.getReqsoftId(advindex);
	}
	public Date getDefinedDateTime(int hour, int minute, int second, int milliSecond) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, second);
		cal.set(Calendar.MILLISECOND, milliSecond);
		Date date = new Date(cal.getTimeInMillis());
		return date;
	}
}
