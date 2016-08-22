package com.lscr.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.lscr.dao.OperateDao;
import com.lscr.entity.Param;
import com.lscr.entity.Req;
import com.lscr.mapper.OperateMapper;

@Repository("operatedao")
public class OperateDaoImpl implements OperateDao {
	@Autowired
	private OperateMapper mapper;

	@Override
	public Object getOperResult(Param param) {
		// TODO Auto-generated method stub
		return mapper.getOperResult(param);
	}

	@Override
	public List<Req> getIndexs(Param param) {
		// TODO Auto-generated method stub
		return mapper.getIndexs(param);
	}

	@Override
	public int insertIndex(Req req) {
		// TODO Auto-generated method stub
		return mapper.insertIndex(req);
	}

	@Override
	public int updateTimeoutIndex(Req req) {
		// TODO Auto-generated method stub
		return mapper.updateTimeoutIndex(req);
	}

	@Override
	public int updateNoovertimeIndex(Param param) {
		// TODO Auto-generated method stub
		return mapper.updateNoovertimeIndex(param);
	}

	@Override
	public int insertOper(Param param) {
		// TODO Auto-generated method stub
		String oper = param.getOper();
		if (oper.equals("0")) {
			return mapper.insertlookOper(param);
		} else if (oper.equals("1")) {
			return mapper.insertentlookOper(param);
		} else if (oper.equals("2")) {
			return mapper.insertclickOper(param);
		} else if (oper.equals("3")) {
			return mapper.insertdownOper(param);
		} else if (oper.equals("4")) {
			return mapper.insertsetupOper(param);
		} else if (oper.equals("5")) {
			return mapper.insertentclickOper(param);
		} else
			return 0;
	}

	@Override
	public int updateOper(Param param) {
		// TODO Auto-generated method stub
		String oper = param.getOper();
		if (oper.equals("0")) {
			return mapper.updatelookOper(param);
		} else if (oper.equals("1")) {
			return mapper.updateentlookOper(param);
		} else if (oper.equals("2")) {
			return mapper.updateclickOper(param);
		} else if (oper.equals("3")) {
			return mapper.updatedownOper(param);
		} else if (oper.equals("4")) {
			return mapper.updatesetupOper(param);
		} else if (oper.equals("5")) {
			return mapper.updateentdetialOper(param);
		} else
			return 0;
	}

	@Override
	public int insertuserlook(Param param) {
		// TODO Auto-generated method stub
		return mapper.insertuserlook(param);
	}

	@Override
	public int updateuserlook(Param param) {
		// TODO Auto-generated method stub
		return mapper.updateuserlook(param);
	}

	@Override
	public String getReqsoftId(String advindex) {
		// TODO Auto-generated method stub
		return mapper.getReqsoftId(advindex);
	}

	@Override
	public int updatesNosim(Param param) {
		// TODO Auto-generated method stub
		return mapper.updatesNosim(param);
	}

	@Override
	public int insertsNosim(Param param) {
		// TODO Auto-generated method stub
		return mapper.insertsNosim(param);
	}

}
