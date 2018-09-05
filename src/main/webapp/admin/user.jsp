<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../inc.jsp"></jsp:include>
<script type="text/javascript" charset="utf-8">
	var datagrid;
	$(function() {

		datagrid = $('#datagrid').datagrid({
			url : 'userAction!datagrid.action',
			title : '用户列表',
			iconCls : 'icon-save',
			pagination : true,
			pagePosition : 'bottom',
			pageSize : 10,
			pageList : [ 10, 20, 30, 40 ],
			fit : true,
			fitColumns : false,
			nowrap : false,
			border : false,
			idField : 'id',
			sortName : 'name',
			sortOrder : 'desc',
			checkOnSelect : true,
			selectOnCheck : true,
			frozenColumns : [ [ {
				checkbox : true,
				title : '',
				field : ''
			},{
				title : '账号',
				field : 'id',
				width : 150,
				sortable : true,
			}, {
				title : '昵称',
				field : 'name',
				width : 150,
				sortable : true
			} ] ],
			columns : [ [ {
				title : '密码',
				field : 'pwd',
				width : 50,
				formatter : function(value, rowData, rowIndex) {
					return '******';
				}
			}, {
				title : '创建时间',
				field : 'createdatetime',
				sortable : true,
				width : 150
			},{
				title : '创建人员',
				field : 'createid',
				sortable : true,
				width : 150,
				hidden : true
			},{
				title : '修改时间',
				field : 'modifydatetime',
				sortable : true,
				width : 150
			},{
				title : '修改人员',
				field : 'modifyid',
				sortable : true,
				width : 150,
				hidden : true
			} ] ],
			toolbar : [ {
				text : '增加',
				iconCls : 'icon-add',
				handler : function() {
					append();
				}
			}, '-', {
				text : '修改',
				iconCls : 'icon-edit',
				handler : function() {
					edit();
				}
			}, '-', {
				text : '删除',
				iconCls : 'icon-remove',
				handler : function() {
					remove();
				}
			}, '-', {
				text : '取消选中',
				iconCls : 'icon-undo',
				handler : function() {
					datagrid.datagrid('clearSelections');
					datagrid.datagrid('uncheckAll');
				}
			}, '-' ],
			onRowContextMenu : function(e, rowIndex, rowData) {
				e.preventDefault();
				$(this).datagrid('unselectAll');
				$(this).datagrid('selectRow', rowIndex);
				$('#menu').menu('show', {
					left : e.pageX,
					top : e.pageY
				});
			}
		});

	});

	function edit() {
		var rows = datagrid.datagrid('getChecked');
		if (rows.length == 1) {
			var p = parent.util.dialog({
				title : '修改用户',
				href : '${pageContext.request.contextPath}/userAction!userEdit.action',
				width : 500,
				height : 200,
				buttons : [ {
					text : '修改',
					handler : function() {
						var f = p.find('form');
						f.form('submit', {
							url : '${pageContext.request.contextPath}/userAction!edit.action',
							success : function(d) {
								var json = $.parseJSON(d);
								if (json.success) {
									datagrid.datagrid('reload');
									p.dialog('close');
								}
								parent.util.messagerShow({
									msg : json.msg,
									title : '提示'
								});
							}
						});
					}
				} ],
				onLoad : function() {
					var f = p.find('form');
					f.form('load', {
						id : rows[0].id,
						name : rows[0].name,
						createdatetime : rows[0].createdatetime,
						createid: rows[0].createid,
						modifydatetime : rows[0].modifydatetime,
						modifyid : rows[0].modifyid
					});
				}
			});
		} else if (rows.length > 1) {
			parent.util.messagerAlert('提示', '只能修改一条记录！', 'error');
		} else {
			parent.util.messagerAlert('提示', '请选择要修改的记录！', 'error');
		}
	}
	function append() {
		var p = parent.util.dialog({
			title : '新增用户',
			href : '${pageContext.request.contextPath}/userAction!userAdd.action',
			width : 260,
			height : 200,
			buttons : [ {
				text : '新增',
				handler : function() {
					var f = p.find('form');
					f.form('submit', {
						url : '${pageContext.request.contextPath}/userAction!add.action',
						success : function(d) {
							var json = $.parseJSON(d);
							if (json.success) {
								datagrid.datagrid('reload');
								p.dialog('close');
							}
							parent.util.messagerShow({
								msg : json.msg,
								title : '提示'
							});
						}
					});
				}
			} ],
			onLoad : function() {
				var f = p.find('form');
				var roleIds = f.find('input[name=roleIds]');
				var roleIdsCombobox = roleIds.combobox({
					url : '${pageContext.request.contextPath}/roleAction!doNotNeedSession_combobox.action',
					valueField : 'id',
					textField : 'name',
					multiple : true,
					editable : false,
					panelHeight : 'auto'
				});
			}
		});
	}
	function remove() {
		var rows = datagrid.datagrid('getChecked');
		var ids = [];
		if (rows.length > 0) {
			parent.util.messagerConfirm('请确认', '您要删除当前所选项目？', function(r) {
				if (r) {
					for ( var i = 0; i < rows.length; i++) {
						ids.push(rows[i].id);
					}
					$.ajax({
						url : '${pageContext.request.contextPath}/userAction!delete.action',
						data : {
							ids : ids.join(',')
						},
						dataType : 'json',
						success : function(d) {
							datagrid.datagrid('load');
							datagrid.datagrid('unselectAll');
							parent.util.messagerShow({
								title : '提示',
								msg : d.msg
							});
						}
					});
				}
			});
		} else {
			parent.util.messagerAlert('提示', '请勾选要删除的记录！', 'error');
		}
	}
	function _search() {
		datagrid.datagrid('load', util.serializeObject($('#searchForm')));
	}
	function cleanSearch() {
		datagrid.datagrid('load', {});
		$('#searchForm input').val('');
	}
</script>
</head>
<body class="easyui-layout" data-options="fit:true">
	<div data-options="region:'north',border:false,title:'过滤条件'" style="height: 110px;overflow: hidden;" align="left">
		<form id="searchForm">
			<table class="tableForm datagrid-toolbar" style="width: 100%;height: 100%;">
				<tr>
					<th>账号</th>
					<td><input name="id" class="easyui-textbox" style="width:140px;" />昵称<input name="name" class="easyui-textbox" style="width:140px;" /></td>
				</tr>
				<tr>
					<th>创建时间</th>
					<td><input name="createdatetimeStart" class="easyui-datetimebox" data-options="editable:false" style="width: 155px;" />至<input name="createdatetimeEnd" class="easyui-datetimebox" data-options="editable:false" style="width: 155px;" /></td>
				</tr>
				<tr>
					<th>最后修改时间</th>
					<td><input name="modifydatetimeStart" class="easyui-datetimebox" data-options="editable:false" style="width: 155px;" />至<input name="modifydatetimeEnd" class="easyui-datetimebox" data-options="editable:false" style="width: 155px;" /><a href="javascript:void(0);" class="easyui-linkbutton" onclick="_search();">查询</a><a href="javascript:void(0);" class="easyui-linkbutton" onclick="cleanSearch();">清空</a></td>
				</tr>
			</table>
		</form>
	</div>
	<div data-options="region:'center',border:false" style="overflow: hidden;">
		<table id="datagrid"></table>
	</div>

	<div id="menu" class="easyui-menu" style="width:120px;display: none;">
		<div onclick="append();" data-options="iconCls:'icon-add'">增加</div>
		<div onclick="edit();" data-options="iconCls:'icon-edit'">修改</div>
		<div onclick="remove();" data-options="iconCls:'icon-remove'">删除</div>
	</div>
</body>
</html>