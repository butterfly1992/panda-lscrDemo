package com.lscr.action;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lscr.biz.RequestService;
import com.lscr.entity.Param;
import com.lscr.tool.IP;
import com.lscr.tool.MemcacheUtil;
import com.lscr.tool.Utils;
import com.lscr.tool.Variable;

import user.agent.tool.Platform;

/**
 * 请求action入口
 * 
 * @author Administrator
 * 
 */
@Controller
public class reqAction {

	private RequestService reqservice;

	@RequestMapping(value = "/request", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Object validate(Param param, HttpServletRequest request) {// 请求返回数据方法
		Object obejct = null;
		// 一系列参数验证后
		if (Utils.isNULL(param.getAppid()) || Utils.isNULL(param.getImei()) || Utils.isNULL(param.getImsi())
				|| Utils.isNULL(param.getGysdkv()) || (param.getMode() == null)) {
			Utils.log.info("[appid为" + param.getAppid() + "；imei为" + param.getImei() + "；imsi为" + param.getImsi()
					+ "；版本为" + param.getGysdkv() + "；弹出范围为 " + param.getMode() + ";]");
			return Variable.errorJson;
		}
		param.setLimit(1);
		// 测试id
		if (Variable.testId.contains(param.getAppid())) {
			obejct = reqservice.getTestResult(param);
			Utils.log.info("【mode:" + param.getMode() + ",Name：" + obejct + ";】");
			if (obejct == null) {
				return Variable.errorJson;
			} else
				return obejct;
		}
		// 判断是否为黑名单上的appid
		if (Variable.blacklistId.contains(param.getAppid())) {
			obejct = reqservice.getBlacklistResult(param);
			Utils.log.info("【mode:" + param.getMode() + ",Name：" + obejct + ";】");
			if (obejct == null) {
				return Variable.errorJson;
			} else
				return obejct;
		}

		// 判斷兩個開關是否開啟
		if (reqservice.lscrInterfaceswitch(param) || reqservice.lscrnoInterfaceswitch(param)) {
			param.setIp(Platform.findIp(request));// 设置用户请求ipPlatform.findIp(request)
			obejct = reqservice.getResult(param);
		} else {
			Utils.log.info("【two switch close-----------------------------】");
			return Variable.errorJson;
		}
		Utils.log.info("【mode:" + param.getMode() + ",Name：" + obejct + ";】");
		if (obejct == null) {
			return Variable.errorJson;
		} else
			return obejct;
	}

	@Autowired
	public void setReqservice(RequestService reqservice) {
		this.reqservice = reqservice;
	}

	@RequestMapping(value = "/clearmem", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody Object deleteme(Param param) {// 删除memcached用户数据
		if (Utils.isNULL(param.getImei())) {
			Utils.log.info(" imei为null");
			return Variable.errorJson;
		}
		if (Utils.isNULL(param.getImsi())) {
			param.setImsi("123456789012345");
		}
		boolean res = false;
		if (MemcacheUtil.mcc.keyExists("nlscr_req" + param.getImei() + param.getImsi())) {// 清空使用过的机会
			res = MemcacheUtil.mcc.delete("nlscr_out_" + param.getImei() + param.getImsi());
			res = MemcacheUtil.mcc.delete("nlscr_in_" + param.getImei() + param.getImsi());
		}
		int flag = 0;
		if (!Utils.isNULL(param.getOper())) {
			res = MemcacheUtil.mcc.delete("nlscr_apkindex_" + param.getImei() + param.getImsi());
			res = MemcacheUtil.mcc.delete("nlscr_entindex_" + param.getImei() + param.getImsi());
			if (param.getOper().equals("clear")) {// 清空数据
				flag = reqservice.delete(param);
			}
		}
		if (res || flag > 0) {
			return Variable.correntJson;
		} else
			return Variable.errorJson;
	}

	@RequestMapping(value = "/testclear", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody Object testclear(Param param) {// 删除memcached用户数据
		boolean res = false;
		if (MemcacheUtil.mcc.keyExists("nlscr_balcklist")) {// 清空使用过的机会
			res = MemcacheUtil.mcc.delete("nlscr_balcklist");
		}
		if (res) {
			return Variable.correntJson;
		} else
			return Variable.errorJson;
	}

	@RequestMapping(value = "/reqmore", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Object req2(Param param, HttpServletRequest request) {// 请求返回多个广告数据
		Object obejct = null;
		// 一系列参数验证后
		if (Utils.isNULL(param.getAppid()) || Utils.isNULL(param.getImei()) || Utils.isNULL(param.getGysdkv())
				|| (param.getMode() == null)) {
			Utils.log.info("[appid为" + param.getAppid() + "；imei为" + param.getImei() + "；版本为" + param.getGysdkv()
					+ "；弹出范围为 " + param.getMode() + ";]");
			return Variable.errorJson;
		}
		param.setLimit(2);
		// 测试id
		if (Variable.testId.contains(param.getAppid())) {
			obejct = reqservice.getTestResultmore(param);
			Utils.log.info("【mode:" + param.getMode() + ",Name：" + obejct + ";】");
			if (obejct == null) {
				return Variable.errorJson;
			} else
				return obejct;
		}

		// 游戏那边设定的id
		/*if (reqservice.validGameId(param)) {
			if (reqservice.lscrInterfaceswitch(param) || reqservice.lscrnoInterfaceswitch(param)) {
				obejct = reqservice.getTestResultmore(param);
				Utils.log.info("【mode:" + param.getMode() + ",Name：" + obejct + ";】");
				if (obejct == null) {
					return Variable.errorJson;
				} else
					return obejct;
			}
			return Variable.errorJson;
		}*/
		// 判断是否为黑名单上的appid,拦截用户请求ip
		if (Variable.blacklistId.contains(param.getAppid())) {
			String ip = request.getHeader("x-forwarded-for");
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getRemoteAddr();
			}
			IP.load(null);
			Utils.log.info(
					"【appid：" + Variable.blacklistId + ",ip:" + ip + ";area:" + Arrays.toString(IP.find(ip)) + "】");
			obejct = reqservice.getBlacklistResult(param);
			Utils.log.info("【mode:" + param.getMode() + ",Name：" + obejct + ";】");
			if (obejct == null) {
				return Variable.errorJson;
			} else
				return obejct;
		}

		// 判斷兩個開關是否開啟
		if (reqservice.lscrInterfaceswitch(param) || reqservice.lscrnoInterfaceswitch(param)) {
			param.setIp(Platform.findIp(request));// 设置用户请求ipPlatform.findIp(request)Platform.findIp(request)
			obejct = reqservice.getResult(param);
		} else {
			Utils.log.info("【two switch close-----------------------------】");
			return Variable.errorJson;
		}
		Utils.log.info("【mode:" + param.getMode() + ",Name：" + obejct + ";】");
		if (obejct == null) {
			return Variable.errorJson;
		} else
			return obejct;
	}

	@RequestMapping(value = "/psorder", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody Object reqInfinite(Param param, HttpServletRequest request) {// 删除memcached用户数据
		Object obejct = null;
		// 一系列参数验证后
		if (Utils.isNULL(param.getAppid()) || Utils.isNULL(param.getImei()) || Utils.isNULL(param.getGysdkv())
				|| (param.getMode() == null)) {
			Utils.log.info("[appid为" + param.getAppid() + "；imei为" + param.getImei() + "；版本为" + param.getGysdkv()
					+ "；弹出范围为 " + param.getMode() + ";]");
			return Variable.errorJson;
		}
		param.setLimit(2);
		// 判斷兩個開關是否開啟
		if (reqservice.lscrInterfaceswitch(param) || reqservice.lscrnoInterfaceswitch(param)) {
			obejct = reqservice.getResult(param);
		} else {
			Utils.log.info("【two switch close-----------------------------】");
			return Variable.errorJson;
		}
		// Utils.log.info("【mode:" + param.getMode() + ",Name：" + obejct +
		// ";】");
		if (obejct == null) {
			return Variable.errorJson;
		} else
			return obejct;
	}
}
