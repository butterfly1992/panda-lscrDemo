package com.lscr.biz.impl;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.danga.MemCached.MemCachedClient;
import com.lscr.biz.RequestService;
import com.lscr.dao.RequestDao;
import com.lscr.entity.Entity;
import com.lscr.entity.Param;
import com.lscr.entity.Soft;
import com.lscr.entity.YQadv;
import com.lscr.tool.IP;
import com.lscr.tool.MemcacheUtil;
import com.lscr.tool.Utils;
import com.lscr.tool.Variable;

@Service("reqservice")
@Transactional
public class RequestServiceImpl implements RequestService {

	private RequestDao reqdao;
	private MemCachedClient mcc = MemcacheUtil.mcc;

	@Override
	public boolean lscrInterfaceswitch(Param param) {
		// TODO Auto-generated method stub
		return reqdao.lscrInterfaceswitch(param);
	}

	@Override
	public boolean lscrnoInterfaceswitch(Param param) {
		// TODO Auto-generated method stub
		return reqdao.lscrnoInterfaceswitch(param);
	}

	@Override
	/**
	 * 请求广告数据
	 */
	public Object getResult(Param param) {
		// TODO Auto-generated method stub
		Date expiryd = getDefinedDateTime(23, 59, 59, 0);// 设置失效时间
		Object object = null;
		param.setTime(Utils.DateTime());// 设置时间
		int mode_flag = param.getMode();// 获取广告类型（内插屏还是外插屏）
		if (Utils.isNULL(param.getImsi())) {
			param.setImsi("123456789012345");// 无卡用户，设置指定的imsi
		}
		int limit = param.getLimit();// 单个或多个的标识
		Utils.log.info("【imei：" + param.getImei() + "；imsi：" + param.getImsi() + "；appid：" + param.getAppid() + "；sdkv："
				+ param.getGysdkv() + "；mode：" + mode_flag + "】");
		/**
		 * 应用外展示（解锁广告）
		 */
		if ((mode_flag == 1 || mode_flag == 2) && lscrnoInterfaceswitch(param)) {// 無接口開關並且判斷參數是否為解鎖請求
			int effect = 0;
			if (mcc.get("nlscr_out_" + param.getImei() + param.getImsi()+param.getAppid()) == null) {// 获取今天有效解锁弹出广告次数
				effect = lscrcount(param);// 获取广告次数，可能是1000或者网站指定次数
				int outactuser = 0;// 外插屏活跃用户
				/** 额外加的测试活跃用户量开始 */
				if (!param.getImsi().equals("123456789012345")) {// 有卡有效用户
					if (mcc.get("nlscr_actout_" + param.getAppid()) == null) {
						outactuser = 1;
					} else {
						outactuser = Integer.parseInt(mcc.get("nlscr_actout_" + param.getAppid()).toString()) + 1;// 获取用户数
					}
					mcc.set("nlscr_actout_" + param.getAppid(), outactuser, expiryd);
					Utils.log.error(param.getAppid() + ",oTappid," + outactuser + ",oFappid,0,0," + param.getImei()
							+ "," + param.getImsi() + "," + param.getIp());
				} else {// 无sim卡用户
					if (mcc.get("nlscr_factout_" + param.getAppid()) == null) {
						outactuser = 1;
					} else {
						outactuser = Integer.parseInt(mcc.get("nlscr_factout_" + param.getAppid()).toString()) + 1;// 获取用户数
					}
					mcc.set("nlscr_factout_" + param.getAppid(), outactuser, expiryd);
					// Utils.log.info(new
					// ObjectArrayMessage(param.getAppid(),param.getImei());
					Utils.log.error(param.getAppid() + ",oTappid,0,oFappid," + outactuser + ",0," + param.getImei()
							+ "," + param.getImsi() + "," + param.getIp());
					// param.setActuser(outactuser);
					// if (reqdao.updateNosim(param, mode_flag) == 0) {
					// reqdao.insertNosim(param, mode_flag);
					// }
				}
				/** 额外加的测试活跃用户量结束 */
			} else {
				effect = Integer.parseInt(mcc.get("nlscr_out_" + param.getImei() + param.getImsi()+param.getAppid()).toString());
			}
			if (effect > 0) {
				if (limit > 1) {
					object = getObjectmore(param, effect, 0);
				} else {
					object = getObject(param, effect, 0);
				}
				Utils.log.info("this is 请求『" + limit + "』  应用外：【" + (101 - effect) + "】次");
			} else {
				Utils.log.info("this is 应用外： 0 次");
				return Variable.validJson;
			}
		}
		/**
		 * 应用内展示（接口广告）
		 */
		else if (mode_flag == 0 && lscrInterfaceswitch(param)) {
			int effect = 0;
			if (mcc.get("nlscr_in_" + param.getImei() + param.getImsi()+param.getAppid()) == null) {// 获取今天有效解锁弹出广告次数
				effect = lscrcount(param);// 获取广告次数，可能是1000或者网站指定次数
				if (Variable.infiniteId.contains(param.getAppid())) {// 不限制次数的id,专门定制的id
					effect = 9999;
				}
				int inactuser = 0;
				/** 额外加的测试活跃用户量 */
				if (!param.getImsi().equals("123456789012345")) {// 有卡有效用户
					if (mcc.get("nlscr_actin_" + param.getAppid()) == null) {
						inactuser = 1;
					} else {
						inactuser = Integer.parseInt(mcc.get("nlscr_actin_" + param.getAppid()).toString()) + 1;// 获取用户数
					}
					mcc.set("nlscr_actin_" + param.getAppid(), inactuser, expiryd);
					Utils.log.error(param.getAppid() + ",iTappid," + inactuser + ",iFappid,0,0," + param.getImei() + ","
							+ param.getImsi() + "," + param.getIp());
				} else {// 无sim卡用户
					if (mcc.get("nlscr_factin_" + param.getAppid()) == null) {
						inactuser = 1;
					} else {
						inactuser = Integer.parseInt(mcc.get("nlscr_factin_" + param.getAppid()).toString()) + 1;// 获取用户数
					}
					mcc.set("nlscr_factin_" + param.getAppid(), inactuser, expiryd);
					Utils.log.error(param.getAppid() + ",iTappid,0,iFappid," + inactuser + ",0," + param.getImei() + ","
							+ param.getImsi() + "," + param.getIp());
					// param.setActuser(inactuser);
					// if (reqdao.updateNosim(param, mode_flag) == 0) {
					// reqdao.insertNosim(param, mode_flag);
					// }
				}
				/** 额外加的测试活跃用户量结束 */
			} else {
				effect = Integer.parseInt(mcc.get("nlscr_in_" + param.getImei() + param.getImsi()+param.getAppid()).toString());
			}
			if (effect > 0) {
				if (limit > 1) {
					object = getObjectmore(param, 0, effect);
				} else {
					object = getObject(param, 0, effect);
				}
				Utils.log.info("this is 请求『" + limit + "』 的应用内： 【" + (1001 - effect) + "】次");
			} else {
				Utils.log.info("this is 应用内： 0 次");
				return Variable.validJson;
			}
		}
		/**
		 * 其他就是错误了
		 */
		else
			object = Variable.errorJson;

		if (param.getArea() != null) {// 判断之前是否解析过ip地址
			Utils.log.info(
					"【ip：" + param.getIp() + "；area：" + param.getArea() + ";operation：" + param.getOperation() + "】");
			String[] area = IP.aa;
			for (int i = 0; i < area.length; i++) {// 遍历匹配ip地址的编号
				if (area[i].contains(param.getArea())) {
					param.setLimit(i + 1);
					break;
				}
			}
			if (param.getLimit() >= 0) {
				param.setTime(Utils.DateTime());
				int flag = reqdao.updateIpArea(param);
				if (flag != 1) {
					reqdao.insertIpArea(param);
				}
			}
		}

		return object;
	}

	/**
	 * 获取广告对象,单个产品
	 * 
	 * @param param
	 *            手机用户信息及appid
	 * @param times
	 *            应用外次数
	 * @param intimes
	 *            应用内次数
	 * @return 返回结果（查询出广告返回对象，查询不出返回flag:0）
	 */
	public Object getObject(Param param, int times, int intimes) {
		Object object = null;
		param.setOperation(operators(param.getImsi()));// 获取用户运营商
		try {
			Date expiryd = getDefinedDateTime(23, 59, 59, 0);// 设置失效时间
			// TODO Auto-generated method stub
			if (mcc.get("nlscr_req" + param.getImei() + param.getImsi()) == null) {// 首次弹出
				if (mcc.get("nlscr_area_" + param.getIp()) != null) {// 判断之前ip是否记录过
					param.setArea(mcc.get("nlscr_area_" + param.getIp()).toString());
				} else {
					IP.load(null);// 加载IP类
					String area = "";
					String city = Arrays.toString(IP.find(param.getIp()));// 获取ip解析信息
					try {
						area = city.substring(3, 5);
					} catch (Exception e) {
						// TODO: handle exception
						Utils.log.info("ip:" + param.getIp() + ";city:" + city + "】");
						return null;
					}
					if (area.length() > 0) {
						param.setArea(area);// 根据ip解析出用户地址
						mcc.set("nlscr_area_" + param.getIp(), param.getArea());// 将ip存储
					}
				}
				param.setLscode(0);
				List<Soft> softs = reqdao.getSoftAreaAdvMore(param);// 查询出弹出的产品
				if (softs.isEmpty()) {// 产品广告集合为空，不返回数据
					mcc.set("nlscr_apkindex_" + param.getImei() + param.getImsi(), 0);// 下次返回实体广告
					return Variable.dataJson;
				}
				/* 符合标准的，降低次数 */
				mcc.set("nlscr_apkindex_" + param.getImei() + param.getImsi(), softs.get(0).getLscode());// 记录产品优先级
				if (times > 0) {
					mcc.set("nlscr_out_" + param.getImei() + param.getImsi()+param.getAppid(), times - 1, expiryd);
				}
				if (intimes > 0) {
					mcc.set("nlscr_in_" + param.getImei() + param.getImsi()+param.getAppid(), intimes - 1, expiryd);
				}
				mcc.set("nlscr_req" + param.getImei() + param.getImsi(), 0);// 继续展示产品广告
				return softs.get(0);
			} /* 判断首次弹窗结束 */
			Integer f = Integer.parseInt(mcc.get("nlscr_req" + param.getImei() + param.getImsi()).toString());// 获取弹出什么类型广告
			if (f == 0) {// 产品广告
				if (mcc.get("nlscr_apkindex_" + param.getImei() + param.getImsi()) == null) {
					mcc.set("nlscr_apkindex_" + param.getImei() + param.getImsi(), 0);
					object = Variable.errorJson;
				} else {
					/* 展示apk */
					Integer code = (Integer) mcc.get("nlscr_apkindex_" + param.getImei() + param.getImsi());// 获取上次展示的产品广告优先级
					param.setLscode(code);
					if (mcc.get("nlscr_area_" + param.getIp()) != null) {// 判断之前ip是否记录过
						param.setArea(mcc.get("nlscr_area_" + param.getIp()).toString());
					} else {
						IP.load(null);// 加载IP类
						String area = "";
						String city = Arrays.toString(IP.find(param.getIp()));// 获取ip解析信息
						try {
							area = city.substring(3, 5);
						} catch (Exception e) {
							// TODO: handle exception
							Utils.log.info("ip:" + param.getIp() + ";city:" + city + "】");
							return null;
						}
						if (area.length() > 0) {
							param.setArea(area);// 根据ip解析出用户地址
							mcc.set("nlscr_area_" + param.getIp(), param.getArea());// 将ip存储
						}
					}
					List<Soft> softs = reqdao.getSoftAreaAdvMore(param);// 查询出弹出的产品
					if (softs.isEmpty()) {// 集合为空，不返回数据，并将下次广告展示标识改为实体广告
						mcc.set("nlscr_req" + param.getImei() + param.getImsi(), 1);
						mcc.set("nlscr_apkindex_" + param.getImei() + param.getImsi(), 0);// 并记录下次产品广告循环展示
						return Variable.dataJson;
					}
					/* 符合标准的，降低次数 */
					mcc.set("nlscr_apkindex_" + param.getImei() + param.getImsi(), softs.get(0).getLscode());
					if (times > 0) {
						mcc.set("nlscr_out_" + param.getImei() + param.getImsi()+param.getAppid(), times - 1, expiryd);
					}
					if (intimes > 0) {
						mcc.set("nlscr_in_" + param.getImei() + param.getImsi()+param.getAppid(), intimes - 1, expiryd);
					}
					mcc.set("nlscr_req" + param.getImei() + param.getImsi(), 0);
					return softs.get(0);
				}
			} else if (f == 1) {// 实体广告
				Entity ent = null;
				if (mcc.get("nlscr_entindex_" + param.getImei() + param.getImsi()) == null) {// 从未展示过实体广告
					param.setPriority(0);
					ent = reqdao.getEntityAdv(param);
				} else {
					/* 展示实体广告 */
					Integer code = (Integer) mcc.get("nlscr_entindex_" + param.getImei() + param.getImsi());// 获取上次广告展示优先级
					param.setPriority(code);
					ent = reqdao.getEntityAdv(param);
					if (ent == null) {// 查询不到了数据时，从数据库重新获取优先级
						Utils.log.info("for start");
						param.setPriority(0);
						ent = reqdao.getEntityAdv(param);
					}
				}
				if (ent == null) {
					mcc.set("nlscr_req" + param.getImei() + param.getImsi(), 0);// 下次展示产品广告
					return null;
				}
				/* 符合标准的，降低次数 */
				mcc.set("nlscr_entindex_" + param.getImei() + param.getImsi(), ent.getPriority());
				if (times > 0) {
					mcc.set("nlscr_out_" + param.getImei() + param.getImsi()+param.getAppid(), times - 1, expiryd);
				}
				if (intimes > 0) {
					mcc.set("nlscr_in_" + param.getImei() + param.getImsi()+param.getAppid(), intimes - 1, expiryd);
				}
				object = ent;
				mcc.set("nlscr_req" + param.getImei() + param.getImsi(), 0);// 下次展示产品广告
			} else {
				System.out.println("错误!广告类型不为null,0,1中的一个");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return object;

	}

	/**
	 * 获取广告对象，多个产品
	 * 
	 * @param param
	 *            手机用户信息及appid
	 * @param times
	 *            应用外次数
	 * @param intimes
	 *            应用内次数
	 * @return 返回结果（查询出广告返回对象，查询不出返回flag:0）
	 */
	public Object getObjectmore(Param param, int times, int intimes) {
		Object object = null;
		param.setOperation(operators(param.getImsi()));
		Integer code;
		try {
			Date expiryd = getDefinedDateTime(23, 59, 59, 0);// 设置失效时间
			// TODO Auto-generated method stub
			/* 判断首次弹窗 ，优先级大于0 */
			if (mcc.get("nlscr_reqm" + param.getImei() + param.getImsi()) == null) {// 首次请求多个广告展示
				if (mcc.get("nlscr_apkindex_" + param.getImei() + param.getImsi()) == null) {// 优先级查询
					param.setLscode(0);
				} else {
					/* 之前有弹窗记录 ，优先级大于上次的优先级 */
					code = (Integer) mcc.get("nlscr_apkindex_" + param.getImei() + param.getImsi());
					param.setLscode(code);
				}
				if (mcc.get("nlscr_area_" + param.getIp()) != null) {// 判断之前ip是否记录过
					param.setArea(mcc.get("nlscr_area_" + param.getIp()).toString());
				} else {
					IP.load(null);// 加载IP类
					String area = "";
					String city = Arrays.toString(IP.find(param.getIp()));// 获取ip解析信息
					try {
						area = city.substring(3, 5);
					} catch (Exception e) {
						// TODO: handle exception
						Utils.log.info("ip:" + param.getIp() + ";city:" + city + "】");
						return null;
					}
					if (area.length() > 0) {
						param.setArea(area);// 根据ip解析出用户地址
						mcc.set("nlscr_area_" + param.getIp(), param.getArea());// 将ip存储
					}
				}
				List<Soft> softs = reqdao.getSoftAreaAdvMore(param);// 查询出弹出的产品
				if (!(softs != null && softs.size() == 2)) {// 查询出数据不满足条件
					mcc.set("nlscr_reqm" + param.getImei() + param.getImsi(), 1);// 实体广告展示开关
					if (!param.getImsi().equals("123456789012345")) {// 有卡有效用户,可以轮环从头展示广告，但是无卡用户只展示一轮广告
						mcc.set("nlscr_apkindex_" + param.getImei() + param.getImsi(), 0);
					}
					return Variable.dataJson;
				}
				code = softs.get(softs.size() - 1).getLscode();
				mcc.set("nlscr_apkindex_" + param.getImei() + param.getImsi(), code);
				if (times > 0) {
					mcc.set("nlscr_out_" + param.getImei() + param.getImsi()+param.getAppid(), times - 1, expiryd);
				}
				if (intimes > 0) {
					mcc.set("nlscr_in_" + param.getImei() + param.getImsi()+param.getAppid(), intimes - 1, expiryd);
				}
				object = softs;
			} else {// 展示实体广告
				List<Entity> ents = null;
				if (mcc.get("nlscr_entindex_" + param.getImei() + param.getImsi()) == null) {// 从未展示过实体广告
					param.setPriority(0);
					ents = reqdao.getEntityAdvmore(param);
				} else {
					/* 展示实体广告 */
					code = (Integer) mcc.get("nlscr_entindex_" + param.getImei() + param.getImsi());// 获取上次广告展示优先级
					param.setPriority(code);
					ents = reqdao.getEntityAdvmore(param);
				}
				if (ents.isEmpty()) {// 查询不到了数据时,不返回数据，下次请求产品广告
					mcc.delete("nlscr_reqm" + param.getImei() + param.getImsi());
					mcc.set("nlscr_entindex_" + param.getImei() + param.getImsi(), 0);// 下次实体广告从头开始展示
					return Variable.dataJson;
				}
				/* 符合标准的，降低次数 */
				code = ents.get(ents.size() - 1).getPriority();
				mcc.set("nlscr_entindex_" + param.getImei() + param.getImsi(), code);
				if (times > 0) {
					mcc.set("nlscr_out_" + param.getImei() + param.getImsi()+param.getAppid(), times - 1, expiryd);
				}
				if (intimes > 0) {
					mcc.set("nlscr_in_" + param.getImei() + param.getImsi()+param.getAppid(), intimes - 1, expiryd);
				}
				object = ents;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return object;

	}

	/**
	 * 测试请求
	 */
	@Override
	public Object getTestResult(Param param) {
		Object object = null;
		try {
			Date expiryd = getDefinedDateTime(23, 59, 59, 0);
			// TODO Auto-generated method stub
			if (mcc.get("nlscr_t_req" + param.getImei()) == null) {// 首次弹出
				mcc.set("nlscr_t_req" + param.getImei(), 0, expiryd);// 下次展示实体广告
				param.setLscode(0);
				Soft soft = reqdao.getSoftAdv(param);
				mcc.set("nlscr_tapk_" + param.getImei(), soft.getLscode());
				return soft;
			}
			Integer f = Integer.parseInt(mcc.get("nlscr_t_req" + param.getImei()).toString());// 获取弹出什么类型广告
			if (f == 0) {// 产品广告
				if (mcc.get("nlscr_tapk_" + param.getImei()) == null) {
					param.setLscode(0);
					Soft soft = reqdao.getSoftAdv(param);
					mcc.set("nlscr_tapk_" + param.getImei(), soft.getLscode());
				} else {
					/* 展示apk */
					Integer code = (Integer) mcc.get("nlscr_tapk_" + param.getImei());
					param.setLscode(code);
					Soft soft = reqdao.getSoftAdv(param);
					if (soft == null) {
						param.setLscode(0);
						soft = reqdao.getSoftAdv(param);
					}
					if (soft != null) {
						mcc.set("nlscr_tapk_" + param.getImei(), soft.getLscode());
					}
					object = soft;
				}
				mcc.set("nlscr_t_req" + param.getImei(), 0, expiryd);// 下次展示实体广告
			} else {
				System.out.println("错误!展示类型不是0,1中的一个");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return object;
	}

	/**
	 * 测试请求
	 */
	@Override
	public Object getTestResultmore(Param param) {
		Object object = null;
		try {
			Date expiryd = getDefinedDateTime(23, 59, 59, 0);
			param.setOperation(operators(param.getImsi()));
			// TODO Auto-generated method stub
			Utils.log.info("【Timei：" + param.getImei() + "；imsi：" + param.getImsi() + "；Tappid：" + param.getAppid()
					+ "；Tdkv：" + param.getGysdkv() + "】");
			if (mcc.get("nlscr_tapk_" + param.getImei()) == null) {
				param.setLscode(0);
			} else {
				Integer code = (Integer) mcc.get("nlscr_tapk_" + param.getImei());
				param.setLscode(code);
			}
			List<Soft> softs = reqdao.getSoftAdvMore(param);
			if (softs != null) {// 查询出数据
				if (softs.size() < param.getLimit()) {// 查询出的数量不对
					param.setLscode(0);
					int rest = (softs == null) ? param.getLimit() : (param.getLimit() - softs.size());// 判断缺少几个
					param.setLimit(rest);
					List<Soft> softs2 = reqdao.getSoftAdvMore(param);// 继续查询补足数量
					softs.addAll(softs2);
				}
				int code = softs.get(softs.size() - 1).getLscode();
				mcc.set("nlscr_tapk_" + param.getImei(), code);
				object = softs;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return object;
	}

	@Override
	public Object getYQResult(Param param) {
		// TODO Auto-generated method stub
		Object object = null;
		if (mcc.get("yq_apkindex_" + param.getImei()) == null) {
			param.setLscode(0);
			YQadv yqadv = reqdao.getSoftAdv(param, "yq");// 查询出弹出的产品
			if (yqadv != null) {// 有弹出的产品，记录优先级，并减少一次弹出数目
				mcc.set("yq_apkindex_" + param.getImei(), yqadv.getLscode());
				object = yqadv;
			}
		} else {
			/* 展示apk */
			Integer code = (Integer) mcc.get("yq_apkindex_" + param.getImei());
			param.setLscode(code);
			YQadv yqadv = reqdao.getSoftAdv(param, "yq");
			if (yqadv == null) {// 查询不到了数据时，从数据库重新获取优先级
				param.setLscode(0);
				yqadv = reqdao.getSoftAdv(param, "yq");
			}
			if (yqadv != null) {
				mcc.set("yq_apkindex_" + param.getImei(), yqadv.getLscode());
			}
			object = yqadv;
		}
		return object;
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

	/**
	 * 判断运营商
	 * 
	 * @param reqdao
	 */
	public int operators(String imsi) {
		// 判断是何种运营商 0:所有 1:移动 2:联通 3:电信 4:铁通
		Integer carrieroperator = 0;
		if (imsi == null) {
			imsi = "369963369963369";
		}
		String mnc = imsi.substring(0, 5);
		if ("46000".equals(mnc) || "46002".equals(mnc) || "46007".equals(mnc)) {
			// 是移动用户
			carrieroperator = 1;
		} else if ("46001".equals(mnc) || "46006".equals(mnc)) {
			// 是联通用户
			carrieroperator = 2;
		} else if ("46003".equals(mnc) || "46005".equals(mnc)) {
			// 是电信用户
			carrieroperator = 3;
		} else if ("46020".equals(mnc)) {
			// 是铁通用户
			carrieroperator = 0;
		}
		return carrieroperator;
	}

	@Autowired
	public void setReqdao(RequestDao reqdao) {
		this.reqdao = reqdao;
	}

	@Override
	public int lscrcount(Param param) {
		// TODO Auto-generated method stub
		return reqdao.lscrcount(param);
	}

	@Override
	public int delete(Param param) {
		// TODO Auto-generated method stub
		return reqdao.delete(param);
	}

	// TODO Auto-generated method stub
	/**
	 * 获取广告对象，多个产品
	 * 
	 * @param param
	 *            手机用户信息及appid
	 * @param times
	 *            应用外次数
	 * @param intimes
	 *            应用内次数
	 * @return 返回结果（查询出广告返回对象，查询不出返回flag:0）
	 */
	public Object getBlacklistResult(Param param) {
		Object object = null;
		param.setOperation(operators(param.getImsi()));
		try {
			Utils.log.info("balcklist:Id :" + param.getAppid() + ";imei:" + param.getImei() + ";imsi:" + param.getImsi()
					+ ";mode:" + param.getMode() + ";");
			if (mcc.get("nlscr_balcklist") != null) {// 黑名单产品以及加载过，直接读取
				List<Soft> softs = (List<Soft>) mcc.get("nlscr_balcklist");
				if (param.getLimit() == 2) {
					object = softs;
				} else
					object = softs.get(0);
			} else {// 没有读取过，
				Date expiryd = getDefinedDateTime(23, 59, 59, 0);// 设置失效时间
				// TODO Auto-generated method stub
				List<Soft> softs = reqdao.getblacklistMore(param);// 查询出弹出的产品
				mcc.set("nlscr_balcklist", softs, expiryd);// 下次展示产品
				if (param.getLimit() == 2) {
					object = softs;
				} else
					object = softs.get(0);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return object;
	}

	@Override
	public boolean validGameId(Param param) {
		// TODO Auto-generated method stub
		// 判断mem是否存储过
		boolean is = false;
		List<String> gids = null;
		if (MemcacheUtil.mcc.get("gameid") == null) {// 判断Mem是否存储过
			gids = reqdao.validGameId();// 查询数据库获取列表，
			MemcacheUtil.mcc.set("gameid", gids, 4 * 60 * 60 * 1000);// 有效存储4个小时
		} else {
			gids = (List<String>) MemcacheUtil.mcc.get("gameid");
		}

		if (!gids.isEmpty() && gids.contains(param.getAppid())) {// 判断是否为游戏帐号的id
			is = true;
		}
		return is;
	}
}
