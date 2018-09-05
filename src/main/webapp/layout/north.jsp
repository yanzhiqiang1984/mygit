<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript" charset="utf-8">
	function logout(b) {
		$('#sessionInfoDiv').html('');
		$.post('${pageContext.request.contextPath}/userAction!doNotNeedSession_logout.action', function() {
			if (b) {
				if (util.isLessThanIe8()) {
					loginDialog.dialog('open');
				} else {
					location.replace('${pageContext.request.contextPath}/');
				}
			} else {
				loginDialog.dialog('open');
			}
		});
	}
	function showUserInfo() {
		var p = parent.util.dialog({
			title : '用户信息',
			href : '${pageContext.request.contextPath}/userAction!doNotNeedAuth_userInfo.action',
			width : 260,
			height :220,
			buttons : [ {
				text : '修改密码',
				handler : function() {
					var f = p.find('form');
					f.form('submit', {
						url : '${pageContext.request.contextPath}/userAction!doNotNeedAuth_editUserInfo.action',
						success : function(d) {
							var json = $.parseJSON(d);
							if (json.success) {
								p.dialog('close');
							}
							parent.util.messagerShow({
								msg : json.msg,
								title : '提示'
							});
						}
					});
				}
			} ]
		});
	}
</script>
<div id="sessionInfoDiv" style="position: absolute; top: 10px;">
	<c:if test="${sessionInfo.userId != null}">[<strong>${sessionInfo.loginName}</strong>]，欢迎你！您使用[<strong>${sessionInfo.ip}</strong>]IP登录！</c:if>
</div>
<div style="position: absolute; right: 0px; bottom: 0px;">
	<a href="javascript:void(0);" class="easyui-menubutton" data-options="menu:'#layout_north_zxMenu',iconCls:'icon-setting'">设置</a>
</div>
<div id="layout_north_zxMenu" style="width: 100px; display: none;">
	<div data-options="iconCls:'icon-person'" onclick="showUserInfo();">用户</div>
	<div class="menu-sep"></div>
	<div data-options="iconCls:'icon-head_ico'" onclick="logout(true);">退出</div>
</div>
