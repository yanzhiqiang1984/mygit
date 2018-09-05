package com.chinasofti.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.chinasofti.dao.UserDaoI;
import com.chinasofti.model.Tuser;
import com.chinasofti.pageModel.User;
import com.chinasofti.pageModel.base.DataGrid;
import com.chinasofti.service.UserServiceI;
import com.chinasofti.util.IpUtil;
import com.chinasofti.util.Md5Util;
import com.chinasofti.util.ToolUtil;

@Service("userService")
public class UserServiceImpl implements UserServiceI {

	private UserDaoI userDao;

	public UserDaoI getUserDao() {
		return userDao;
	}

	@Autowired
	public void setUserDao(UserDaoI userDao) {
		this.userDao = userDao;
	}

	public User login(User user) {
		Tuser t = userDao.get(user);
		if (t != null) {
			BeanUtils.copyProperties(t, user, new String[] { "pwd" });
			return user;
		}
		return null;
	}

	public void save(User user) {
		Tuser u = new Tuser();
		BeanUtils.copyProperties(user, u, new String[] { "pwd" });
		if (user.getCreatedatetime() == null) {
			u.setCreatedatetime(ToolUtil.getDateTime());
		}
		u.setPwd(Md5Util.e(user.getPwd()));
		u.setIp(IpUtil.getIpAddr(ServletActionContext.getRequest ()));
		userDao.save(u);
	}

	public void update(User user) {
		Tuser u = userDao.get(Tuser.class, user.getId());
		BeanUtils.copyProperties(user, u, new String[] { "id", "pwd" });
		if (user.getCreatedatetime() == null) {
			u.setCreatedatetime(ToolUtil.getDateTime());
		}
		if (user.getPwd() != null && !user.getPwd().trim().equals("")) {
			u.setPwd(Md5Util.e(user.getPwd()));
		}
		u.setModifydatetime(ToolUtil.getDateTime());
		u.setModifyid(ToolUtil.getUserId(ServletActionContext.getRequest()));
	}

	public DataGrid datagrid(User user) {
		DataGrid j = new DataGrid();
		j.setRows(this.changeModel(this.userDao.find(user)));
		j.setTotal(this.userDao.total(user));
		return j;
	}

	private List<User> changeModel(List<Tuser> tusers) {
		List<User> users = new ArrayList<User>();
		if (tusers != null && tusers.size() > 0) {
			for (Tuser tu : tusers) {
				User u = new User();
				BeanUtils.copyProperties(tu, u);
				users.add(u);
			}
		}
		return users;
	}
	
	public void delete(String ids) {
		if (ids != null) {
			for (String id : ids.split(",")) {
				if (!id.trim().equals("0")) {
					Tuser tuser = userDao.get(Tuser.class, id.trim());
					if (tuser != null) {
						userDao.delete(tuser);
					}
				}
			}
		}
	}

	public void editUserInfo(User user) {
		if (user.getPwd() != null && !user.getPwd().trim().equals("")) {
			Tuser t = userDao.get(Tuser.class, user.getId());
			t.setPwd(Md5Util.e(user.getPwd()));
		}
	}
	
}
