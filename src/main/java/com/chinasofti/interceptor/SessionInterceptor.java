package com.chinasofti.interceptor;

import org.apache.struts2.ServletActionContext;


import com.chinasofti.pageModel.base.SessionInfo;
import com.chinasofti.util.ResourceUtil;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

/**
 * session拦截器
 * 
 */
public class SessionInterceptor extends MethodFilterInterceptor {

	private static final long serialVersionUID = 1L;

	protected String doIntercept(ActionInvocation actionInvocation) throws Exception {
		SessionInfo sessionInfo = (SessionInfo) ServletActionContext.getRequest().getSession().getAttribute(ResourceUtil.getSessionInfoName());
		if (sessionInfo == null) {
			ServletActionContext.getRequest().setAttribute("msg", "您还没有登录或登录已超时，请重新登录!");
			return "noSession";
		}
		return actionInvocation.invoke();
	}

}
