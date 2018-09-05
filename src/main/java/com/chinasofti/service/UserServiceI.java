package com.chinasofti.service;

import com.chinasofti.pageModel.User;
import com.chinasofti.pageModel.base.DataGrid;

public interface UserServiceI {

	public User login(User user);

	public void save(User user);

	public DataGrid datagrid(User user);

	public void delete(String ids);

	public void update(User user);

	public void editUserInfo(User user);
	
}
