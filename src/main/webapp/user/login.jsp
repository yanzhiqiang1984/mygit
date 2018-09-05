<%@ page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript" charset="utf-8">
	var imgCode;
	var loginDialog;
	var backgroudDialog;
	var loginInputForm;
	var loginDatagridForm;
	var loginComboboxForm;
	var loginDatagridName;
	var sessionInfo;
	$(function() {
		backgroudDialog = $('#backgroudDialog');
		var formParam = {
			url : '${pageContext.request.contextPath}/userAction!doNotNeedSession_login.action',
			success : function(data) {
				var d = $.parseJSON(data);
				if (d.success) {
					loginDialog.dialog('close');
					backgroudDialog.dialog('close');
					$('#sessionInfoDiv').html(util.fs('[<strong>{0}</strong>]，欢迎你！您使用[<strong>{1}</strong>]IP登录！', d.obj.loginName, d.obj.ip));
				}
				$.messager.show({
					msg : d.msg,
					title : '提示'
				});
			}
		};

		loginInputForm = $('#loginInputForm').form(formParam);
		loginDatagridForm = $('#loginDatagridForm').form(formParam);
		loginComboboxForm = $('#loginComboboxForm').form(formParam);

		loginDialog = $('#loginDialog').show().dialog({
			closable : false,
			title : '登录',
			modal : true,
			onOpen : function() {
				var t = $(this);
				window.setTimeout(function() {
					t.find('input[name=cid]').focus();
				}, 0);
			}
		});

		$('form input').bind('keyup', function(event) {/* 增加回车提交功能 */
			if (event.keyCode == '13') {
				$(this).submit();
			}
		});

		sessionInfo = {
			userId : '${sessionInfo.userId}',
			loginName : '${sessionInfo.loginName}',
			ip : '${sessionInfo.ip}',
			isLock : '${sessionInfo.isLock}'
		};
		if (sessionInfo.userId) {/*如果有userId说明已经登陆过了*/
			window.setTimeout(function() {
				$('div.validatebox-tip').remove();
			}, 500);
			if (sessionInfo.isLock == 'false') {
				loginDialog.dialog('close');
				backgroudDialog.dialog('close');
			}

		}
		;
		loadCodeImg();
		$('#codeImage').load(function() {
			setTimeout('loadImgCode()', 1000);
		});

	});
	function loadImgCode() {
		$.post("${pageContext.request.contextPath}/imageAction!doNotNeedSession_loadImgCode.action", function(code) {
			imgCode = code;
		});
	}
	function loadCodeImg() {
		$('#imgCode').textbox("setText", "");
		$('#codeImage').attr('src', '${pageContext.request.contextPath}/imageAction!doNotNeedSession_imgCode.action?random=' + Math.random());
	}
	function login() {
		$('#loginComboboxForm').submit();
	}
</script>
<div id="backgroudDialog" class="easyui-dialog" data-options="fit:true,border:true,noheader:true"></div>
<div id="loginDialog" style="width: 350px; display: none; overflow: hidden; padding: 20px 50px 20px 50px;">
	<div data-options="fit:true,border:false">
		<form id="loginComboboxForm" method="post">
			<div style="margin-bottom: 5px">
				<input id="id" type="text" name="id" data-options="iconCls:'icon-man',prompt:'账号',required:true" class="easyui-textbox" style="width: 100%; height: 30px; padding: 12px">
			</div>
			<div style="margin-bottom: 5px">
				<input id="pwd" type="password" name="pwd" data-options="iconCls:'icon-lock',prompt:'密码',required:true" class="easyui-textbox" style="width: 100%; height: 30px; padding: 12px">
			</div>
			<div style="margin-bottom: 5px">
				<input id="imgCode" class="easyui-textbox" type="text" style="width: 45%; height: 30px; padding: 12px" data-options="prompt:'验证码',required:true,validType:'equalsImgCode[imgCode]',missingMessage:''" /> <img id="codeImage" style="margin: 0 0 0 3px; vertical-align: middle; height: 26px;" src=""> <a onclick="loadCodeImg()" class="easyui-linkbutton" data-options="plain: true, iconCls:'icon-reload'">换一张</a>
			</div>
			<a class="easyui-linkbutton" style="padding: 5px 0px; width: 100%;" onclick="login()"> <span style="font-size: 14px;">登录</span>
			</a>
		</form>
	</div>
</div>