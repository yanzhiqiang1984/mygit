package com.chinasofti.action;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.chinasofti.action.base.BaseAction;
import com.chinasofti.pageModel.User;
import com.chinasofti.pageModel.base.Json;
import com.chinasofti.pageModel.base.SessionInfo;
import com.chinasofti.service.UserServiceI;
import com.chinasofti.util.ExceptionUtil;
import com.chinasofti.util.IpUtil;
import com.chinasofti.util.ResourceUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 用户的ACTION
 * 
 */
@Action(value = "userAction", results = { @Result(name = "user", location = "/admin/user.jsp"), @Result(name = "userAdd", location = "/admin/userAdd.jsp"), @Result(name = "userEdit", location = "/admin/userEdit.jsp"), @Result(name = "userRoleEdit", location = "/admin/userRoleEdit.jsp"), @Result(name = "doNotNeedAuth_userInfo", location = "/user/userInfo.jsp") })
public class UserAction extends BaseAction implements ModelDriven<User> {

	private static final Logger logger = Logger.getLogger(UserAction.class);

	private UserServiceI userService;

	public UserServiceI getUserService() {
		return userService;
	}

	@Autowired
	public void setUserService(UserServiceI UserService) {
		this.userService = UserService;
	}

	private User user = new User();

	public User getModel() {
		return user;
	}

	public String user() {
		return "user";
	}

	public String userAdd() {
		return "userAdd";
	}

	public String userEdit() {
		return "userEdit";
	}

	public String userRoleEdit() {
		return "userRoleEdit";
	}

	public String doNotNeedAuth_userInfo() {
		return "doNotNeedAuth_userInfo";
	}

	public void doNotNeedAuth_editUserInfo() {
		Json j = new Json();
		try {
			userService.editUserInfo(user);
			j.setSuccess(true);
			j.setMsg("修改成功！");
		} catch (Exception e) {
			logger.error(ExceptionUtil.getExceptionMessage(e));
			j.setMsg("修改失败！");
		}
		super.writeJson(j);
	}

	public void doNotNeedSession_login() {
		Json j = new Json();
		User u = userService.login(user);
		if (u != null) {
			SessionInfo sessionInfo = new SessionInfo();
			sessionInfo.setUserId(u.getId());
			sessionInfo.setLoginName(user.getName());
			sessionInfo.setLoginPassword(user.getPwd());
			sessionInfo.setIp(IpUtil.getIpAddr(ServletActionContext.getRequest()));
			ServletActionContext.getRequest().getSession().setAttribute(ResourceUtil.getSessionInfoName(), sessionInfo);
			sessionInfo.setAuthIds(u.getAuthIds());
			sessionInfo.setAuthNames(u.getAuthNames());
			sessionInfo.setRoleIds(u.getRoleIds());
			sessionInfo.setRoleNames(u.getRoleNames());
			sessionInfo.setAuthUrls(u.getAuthUrls());
			sessionInfo.setIsLock(false);

			j.setObj(sessionInfo);
			j.setMsg("登录成功！");
			j.setSuccess(true);
		} else {
			j.setMsg("登录失败！用户名或密码错误！");
		}
		super.writeJson(j);
	}

	public void doNotNeedSession_logout() {
		ServletActionContext.getRequest().getSession().invalidate();
		Json j = new Json();
		j.setSuccess(true);
		super.writeJson(j);
	}
	
	public void doNotNeedSession_lock() {
		SessionInfo sessionInfo = (SessionInfo) ServletActionContext.getRequest().getSession().getAttribute(ResourceUtil.getSessionInfoName());
		if(sessionInfo!=null){
			sessionInfo.setIsLock(true);
		}
		Json j = new Json();
		j.setObj(sessionInfo);
		j.setSuccess(true);
		super.writeJson(j);
	}

	public void add() {
		Json j = new Json();
		try {
			userService.save(user);
			j.setMsg("新增成功！");
			j.setSuccess(true);
		} catch (Exception e) {
			j.setMsg("新增失败，用户名已存在！");
			ExceptionUtil.getExceptionMessage(e);
		}
		super.writeJson(j);
	}

	public void edit() {
		Json j = new Json();
		try {
			userService.update(user);
			j.setSuccess(true);
			j.setMsg("修改成功！");
		} catch (Exception e) {
			logger.error(ExceptionUtil.getExceptionMessage(e));
			j.setMsg("修改失败，用户名已存在！");
		}
		super.writeJson(j);
	}

	public void delete() {
		Json j = new Json();
		userService.delete(user.getIds());
		j.setSuccess(true);
		j.setMsg("删除成功!");
		super.writeJson(j);
	}

	public void datagrid() {
		super.writeJson(userService.datagrid(user));
	}

	public void doNotNeedSession_datagrid() {
		if (user.getQ() != null && !user.getQ().trim().equals("")) {
			user.setName(user.getQ());
		}
		super.writeJson(userService.datagrid(user));
	}

}
