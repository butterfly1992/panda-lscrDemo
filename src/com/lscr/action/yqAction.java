package com.lscr.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lscr.biz.RequestService;
import com.lscr.entity.Param;
import com.lscr.tool.Utils;
import com.lscr.tool.Variable;

/**
 * 移趣互推请求action入口
 * 
 * @author Administrator
 * 
 */
@Controller
public class yqAction {

	private RequestService reqservice;

	@RequestMapping(value = "/advreq", method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody
	Object validate(Param param) {// 请求返回数据方法
		Object obejct = null;
		// 一系列参数验证后
		if (Utils.isNULL(param.getAppid()) || Utils.isNULL(param.getImei())) {
			Utils.log.info("[appid为" + param.getAppid() + "；imei为"
					+ param.getImei() + ";]");
			return Variable.errorJson;
		}
		Utils.log.info("========yqreq_start=============");
		// 测试id
		if (Variable.yqId.contains(param.getAppid())) {
			obejct = reqservice.getYQResult(param);
			Utils.log.info("【Name：" + obejct + ";】");
			if (obejct == null) {
				return Variable.errorJson;
			} else
				return obejct;
		} else {
			return Variable.errorJson;
		}
	}

	@Autowired
	public void setReqservice(RequestService reqservice) {
		this.reqservice = reqservice;
	}

}
