package com.chinasofti.interceptor;

import org.apache.struts2.ServletActionContext;


import com.chinasofti.pageModel.base.SessionInfo;
import com.chinasofti.util.RequestUtil;
import com.chinasofti.util.ResourceUtil;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

/**
 * 权限拦截器
 * 
 */
public class AuthInterceptor extends MethodFilterInterceptor {

	private static final long serialVersionUID = 1L;

	protected String doIntercept(ActionInvocation actionInvocation) throws Exception {
		SessionInfo sessionInfo = (SessionInfo) ServletActionContext.getRequest().getSession().getAttribute(ResourceUtil.getSessionInfoName());
		if (sessionInfo.getUserId().equals("admin")) {// admin用户不需要验证权限
			return actionInvocation.invoke();
		}
		String requestPath = RequestUtil.getRequestPath(ServletActionContext.getRequest());
		String authUrls = sessionInfo.getAuthUrls();
		boolean b = false;
		for (String url : authUrls.split(",")) {
			if (requestPath.equals(url)) {
				b = true;
				break;
			}
		}
		if (b) {
			return actionInvocation.invoke();
		}
		ServletActionContext.getRequest().setAttribute("msg", "您没有访问此功能的权限！权限路径为[" + requestPath + "]请联系管理员给你赋予相应权限。");
		return "noAuth";
	}

}
