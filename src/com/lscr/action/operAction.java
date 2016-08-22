package com.lscr.action;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lscr.biz.OperateService;
import com.lscr.entity.Param;
import com.lscr.tool.Utils;
import com.lscr.tool.Variable;

/**
 * 操作action入口
 * 
 * @author Administrator
 * 
 */
@Controller
public class operAction {
	private OperateService operservice;

	// 0---APK展示---1---ENTITY展示----2----点击下载----3----下载完成----4----安装完成----5----entity查看详情
	@RequestMapping(value = "/oper", method = { RequestMethod.POST })
	public @ResponseBody Object operate(Param param, HttpServletRequest request) {// 处理产品操作方法，参数验证
		if (Utils.isNULL(param.getAppid()) || Utils.isNULL(param.getImei()) || Utils.isNULL(param.getAdvindex()) || Utils.isNULL(param.getGysdkv())
				|| Utils.isNULL(param.getOper())) {
			Utils.log.info("+Operate【" + param.getOper() + ";appid：" + param.getAppid() + ";softid："
					+ param.getSoftid() + ";imsi:" + param.getImsi() + ";Imei:" + param.getImei() + ";index："
					+ param.getAdvindex() + ";Gysdkv:" + param.getGysdkv() + "】");
			return Variable.errorJson;
		}
		if (Variable.testId.contains(param.getAppid()) || Variable.invalidImei.contains(param.getImei())) {
			Utils.log.info("测试id不统计数据");
			return Variable.errorJson;
		}
		if(Utils.isNULL(param.getImsi())){//设置手机卡串码，后面统计时拦截统计处理0114
			param.setImsi("12345678912345");
		}
		if (param.getImsi().indexOf("00000") >= 0 || param.getImei().indexOf("000000") >= 0 || Variable.errorImsi.contains(param.getImsi())) {//错误的串码
			Utils.log.info("Mobile moni： 『" + param.getOper() + "imei:" + param.getImei() + ";imsi：" + param.getImsi()
					+ ";appid:" + param.getAppid() + "』");
			return Variable.errorJson;
		}
		if (Utils.isNULL(param.getSoftid())) {
			String sid = operservice.getReqsoftId(param.getAdvindex());
			Utils.log.info("Softid无：" + sid + ";appid:" + param.getAppid() + ";");
			param.setSoftid(sid);
		}
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
			Utils.log.info("【appid：" + Variable.blacklistId + ",ip:" + ip + "】");
		}

		Utils.log.info("===============Operate=【" + param.getOper() + ";Gysdkv:" + param.getGysdkv() + ";appid："
				+ param.getAppid() + ";imsi:" + param.getImsi() + ";Imei:" + param.getImei() + ";softid："
				+ param.getSoftid() + ";index：" + param.getAdvindex() + "】");
		
		Object object = operservice.getOperResult(param);
		Utils.log.info("=result：【" + object.toString() + "】");
		return object;
	}

	@Autowired
	public void setOperservice(OperateService operservice) {
		this.operservice = operservice;
	}

	/**
	 * 加密路径，统计数据
	 * 
	 * @param param
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/calcrecod", method = { RequestMethod.POST })
	public @ResponseBody Object encryptoperate(Param param) {// 处理产品操作方法，参数验证
		if (Utils.isNULL(param.getAppid()) || Utils.isNULL(param.getImei()) || Utils.isNULL(param.getKey())) {//验证参数
			Utils.log.info("appid：" + param.getAppid() + ";softid："
					+ param.getSoftid() + ";imsi:" + param.getImsi() + ";Imei:" + param.getImei() + ";key："
					+ param.getKey()+"】");
			return Variable.errorJson;
		}
		if(Utils.isNULL(param.getImsi())){//设置手机卡串码，后面统计操作拦截统计处理0114
			param.setImsi("12345678912345");
		}
		if (param.getImsi().indexOf("00000") >= 0 || param.getImei().indexOf("000000") >= 0 || Variable.errorImsi.contains(param.getImsi())) {//错误的串码
			Utils.log.info("Mobile moni： 『" + param.getOper() + "imei:" + param.getImei() + ";imsi：" + param.getImsi()
					+ ";appid:" + param.getAppid() + "』");
			return Variable.errorJson;
		}
		if (Variable.testId.contains(param.getAppid()) || Variable.invalidImei.contains(param.getImei())) {
			Utils.log.info("测试id不统计数据");
			return Variable.errorJson;
		}
		String keycont = Utils.jiexiUrl(param.getKey(), param.getAppid(), param.getImei());// 获取解密内容
		if (keycont == null) {// 解密失败
			Utils.log.info("JieXiException : " + param.getKey() + ";appid:" + param.getAppid() + ";imei:" +  param.getImei() + "；");
			return Variable.errorJson;
		}
		String[] moreparam = keycont.split(";");
		param.setAdvindex(moreparam[0]);
		param.setOper(moreparam[1]);
		param.setGysdkv(moreparam[2]);
		if (Utils.isNULL(param.getSoftid())) {
			String sid = operservice.getReqsoftId(param.getAdvindex());
			Utils.log.info("Softid无：" + sid + ";appid:" + param.getAppid() + ";");
			param.setSoftid(sid);
		}
		
		Utils.log.info("===============Operate=【" + param.getOper() +  ";appid："
				+ param.getAppid() + ";imsi:" + param.getImsi() + ";Imei:" + param.getImei() + ";Gysdkv:" + param.getGysdkv() +";softid："
				+ param.getSoftid() + ";index：" + param.getAdvindex() + "】");
	
		Object object = operservice.getOperResult(param);
		Utils.log.info("=result：【" + object.toString() + "】");
		return object;
	}
}
