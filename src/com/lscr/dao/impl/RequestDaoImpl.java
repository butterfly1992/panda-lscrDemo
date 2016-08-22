package com.lscr.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.lscr.dao.RequestDao;
import com.lscr.entity.App;
import com.lscr.entity.Entity;
import com.lscr.entity.Param;
import com.lscr.entity.Req;
import com.lscr.entity.Soft;
import com.lscr.entity.YQadv;
import com.lscr.mapper.RequestMapper;
import com.lscr.tool.Utils;
import com.lscr.tool.Variable;

@Repository("reqdao")
public class RequestDaoImpl implements RequestDao {
	@Autowired
	private RequestMapper mapper;

	@Override
	public boolean lscrInterfaceswitch(Param param) {
		// TODO Auto-generated method stub
		App app = mapper.lscrswitch(param);
		if (app == null)
			return false;
		else if (app.getLscr_in_status() == 0)
			return false;
		else
			return true;
	}

	@Override
	public boolean lscrnoInterfaceswitch(Param param) {
		// TODO Auto-generated method stub
		App app = mapper.lscrswitch(param);
		if (app == null)
			return false;
		else if (app.getLscr_out_status() == 0)
			return false;
		else
			return true;
	}

	@Override
	public int lscrcount(Param param) {
		// TODO Auto-generated method stub
		if (Variable.infiniteId.contains(param.getAppid())) {// 不限制次数的id,专门定制的id
			return 1900;
		} else {// 从网站定义的中查询
			App app = mapper.lscrcount(param);
			if (app == null)
				return 0;
			else if (param.getMode() == 0) {// 应用内广告
				return app.getLscr_in_count();
			} else {
				return app.getLscr_out_count();// 应用外广告次数
			}
		}
	}

	/**
	 * 获取soft广告
	 */
	@Override
	public Soft getSoftAdv(Param param) {
		// TODO Auto-generated method stub
		Soft soft = mapper.getSoftAdv(param);
		if (soft != null) {// 处理要推送的产品
			Utils.log.info("result—Soft—:【name:" + soft.getName() + ";pck：" + soft.getPck() + ";wareindex："
					+ soft.getLsindex() + "；】");
		}
		return soft;
	}

	/**
	 * 获取soft广告
	 */
	@Override
	public List<Soft> getSoftAdvMore(Param param) {
		// TODO Auto-generated method stub
		List<Soft> softs = mapper.getSoftAdvMore(param);
		if (!softs.isEmpty()) {// 处理要推送的产品
			for (Soft soft : softs) {
				Utils.log.info("『Num:" + softs.size() + ";Name:" + soft.getName() + ";index:" + soft.getLsindex()
						+ ";Pck:" + soft.getPck() + ";』");
			}
		}
		return softs;
	}

	/**
	 * 获取实体广告
	 */
	@Override
	public Entity getEntityAdv(Param param) {
		// TODO Auto-generated method stub
		Entity entity = mapper.getEntityAdv(param);
		if (entity != null) {
			Utils.log.info("result—Entity—【name:" + entity.getName() + ";wareindex：" + entity.getLsindex() + "；】");
		}
		return entity;
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
	public int delete(Param param) {
		// TODO Auto-generated method stub
		return mapper.delete(param);
	}

	@Override
	public YQadv getSoftAdv(Param param, String string) {
		// TODO Auto-generated method stub
		YQadv yqadv = mapper.getYqSoftAdv(param);
		if (yqadv != null) {// 处理要推送的产品
			Utils.log.info("result—yqadv—:【name:" + yqadv.getName() + ";pck：" + yqadv.getPck() + ";】");
		}
		return yqadv;
	}

	@Override
	public List<Soft> getblacklistMore(Param param) {
		// TODO Auto-generated method stub
		List<Soft> softs = mapper.getblacklistMore(param);
		if (!softs.isEmpty()) {// 处理要推送的产品
			for (Soft soft : softs) {
				Utils.log.info("----------------------------『balckno:" + softs.size() + ";Name:" + soft.getName()
						+ ";index:" + soft.getLsindex() + ";Pck:" + soft.getPck() + ";』");
			}
		}
		return softs;
	}

	@Override
	public List<String> validGameId() {
		// TODO Auto-generated method stub
		List<String> games = mapper.gameids();
		return games;
	}

	@Override
	public List<Soft> getSoftAreaAdvMore(Param param) {
		// TODO Auto-generated method stub
		List<Soft> softs = mapper.getSoftAreaAdvMore(param);
		if (!softs.isEmpty()) {// 处理要推送的产品
			for (Soft soft : softs) {
				Utils.log.info("『added:" + softs.size() + ";Name:" + soft.getName() + ";index:" + soft.getLsindex()
						+ ";Pck:" + soft.getPck() + ";』");
			}
		}
		return softs;
	}

	@Override
	public int updateIpArea(Param param) {
		// TODO Auto-generated method stub
		return mapper.updateIpArea(param);
	}

	@Override
	public int insertIpArea(Param param) {
		// TODO Auto-generated method stub
		return mapper.insertIpArea(param);
	}

	@Override
	public List<Entity> getEntityAdvmore(Param param) {
		// TODO Auto-generated method stub
		List<Entity> ents = mapper.getEntityAdvmore(param);
		if (!ents.isEmpty()) {// 处理要推送的产品
			for (Entity ent : ents) {
				Utils.log.info("『entAd:" + ents.size() + ";Name:" + ent.getName() + ";index:" + ent.getLsindex());
			}
		}
		return ents;
	}

	@Override
	public int updateNosim(Param param, int mode_flag) {
		// TODO Auto-generated method stub
		if (mode_flag == 1 || mode_flag == 2) {
			return mapper.updateoNosim(param);
		} else
			return mapper.updateiNosim(param);
	}

	@Override
	public int insertNosim(Param param, int mode_flag) {
		// TODO Auto-generated method stub
		if (mode_flag == 1 || mode_flag == 2) {
			return mapper.insertoNosim(param);
		} else
			return mapper.insertiNosim(param);
	}

}