package com.chinasofti.util;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;

import com.chinasofti.pageModel.base.SessionInfo;

public class ToolUtil {
	
	public static String getUserId(HttpServletRequest request){
		SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ResourceUtil.getSessionInfoName());
		return sessionInfo==null?null:sessionInfo.getUserId();
	}
	
	public static String getUserName(SessionInfo sessionInfo){
		return sessionInfo==null?null:sessionInfo.getLoginName();
	}
	
	public static Date getDateTime(){
		return new Date();
	}
	
	public static boolean isEmpty(String str){
		return (str == null || "".equals(str)) ? true : false;
	}
}
