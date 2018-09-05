package com.chinasofti.dao;

import java.util.List;

import com.chinasofti.dao.base.BaseDaoI;
import com.chinasofti.model.Tuser;
import com.chinasofti.pageModel.User;

public interface UserDaoI extends BaseDaoI<Tuser>{
	
	public Tuser get(User user);
	
	public List<Tuser> find(User user);
	
	public Long total(User user);
	
}
