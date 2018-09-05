package com.chinasofti.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import com.chinasofti.dao.UserDaoI;
import com.chinasofti.dao.base.impl.BaseDaoImpl;
import com.chinasofti.model.Tuser;
import com.chinasofti.pageModel.User;
import com.chinasofti.util.Md5Util;

@Repository("userDao")
public class UserDaoImpl extends BaseDaoImpl<Tuser> implements UserDaoI {

	@Override
	public Tuser get(User user) {
		String hql = "from Tuser t where t.id = ? and t.pwd = ?";
		Object[] param = new Object[] { user.getId(), Md5Util.e(user.getPwd()) };
		return this.get(hql, param);
	}

	@Override
	public List<Tuser> find(User user) {
		String hql = "from Tuser t where 1=1 ";
		List<Object> values = new ArrayList<Object>();
		hql = addWhere(user, hql, values);
		return this.find(hql, values, user.getPage(), user.getRows());
	}
	
	@Override
	public Long total(User user) {
		String hql = "select count(*) from Tuser t where 1=1 ";
		List<Object> values = new ArrayList<Object>();
		hql = addWhere(user, hql, values);
		return this.count(hql, values);
	}

	private String addWhere(User user, String hql, List<Object> values) {
		if (user.getId() != null && !user.getId().trim().equals("")) {
			hql += " and t.id like ? ";
			values.add("%%" + user.getId().trim() + "%%");
		}
		if (user.getName() != null && !user.getName().trim().equals("")) {
			hql += " and t.name like ? ";
			values.add("%%" + user.getName().trim() + "%%");
		}
		if (user.getCreatedatetimeStart() != null) {
			hql += " and t.createdatetime>=? ";
			values.add(user.getCreatedatetimeStart());
		}
		if (user.getCreatedatetimeEnd() != null) {
			hql += " and t.createdatetime<=? ";
			values.add(user.getCreatedatetimeEnd());
		}
		if (user.getModifydatetimeStart() != null) {
			hql += " and t.modifydatetime>=? ";
			values.add(user.getModifydatetimeStart());
		}
		if (user.getCmodifydatetimeEnd() != null) {
			hql += " and t.modifydatetime<=? ";
			values.add(user.getCmodifydatetimeEnd());
		}
		return hql;
	}
}
