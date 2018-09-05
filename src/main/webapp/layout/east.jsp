<%@ page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript" charset="utf-8">
	var onlineDatagrid;
	var onlinePanel;
	var userOnlineInfoDialog;
	var userOnlineInfoDataGrid;
	$(function() {

		onlinePanel = $('#onlinePanel').panel({
			tools : [ {
				iconCls : 'icon-reload',
				handler : function() {
					if (onlineDatagrid.datagrid('options').url) {
						onlineDatagrid.datagrid('load');
					}
				}
			} ]
		});

		onlineDatagrid = $('#onlineDatagrid').datagrid({
			title : '',
			iconCls : '',
			fit : true,
			fitColumns : true,
			pagination : false,
			pageSize : 10,
			pageList : [ 10 ],
			nowarp : false,
			border : false,
			idField : 'id',
			sortName : 'datetime',
			sortOrder : 'desc',
			frozenColumns : [ [ {
				title : '账号',
				field : 'id',
				width : 150,
				hidden:true
			} ] ],
			columns : [ [ {
				title : '用户名',
				field : 'name',
				width : 150,
				sortable : true,
				formatter : function(value, rowData, rowIndex) {
					return util.fs('<span title="{0}">{1}</span>', value, value);
				}
			}, {
				title : '登陆地址',
				field : 'ip',
				width : 150,
				sortable : true,
				formatter : function(value, rowData, rowIndex) {
					return util.fs('<span title="{0}">{1}</span>', value, value);
				}
			}, {
				title : '登录时间',
				field : 'datetime',
				width : 150,
				sortable : true,
				formatter : function(value, rowData, rowIndex) {
					return util.fs('<span title="{0}">{1}</span>', value, value);
				}
			} ] ],
			onClickRow : function(rowIndex, rowData) {
				userOnlineInfoDataGrid.datagrid('loadData', [
				{
					value : '账号',
					name : rowData.id
				},{
					value : '用户名',
					name : rowData.name
				}, {
					value : 'IP',
					name : rowData.ip
				}, {
					value : '登录时间',
					name : rowData.datetime
				} ]);
				userOnlineInfoDialog.dialog('open');
				$(this).datagrid('unselectAll');
			},
			onLoadSuccess : function(data) {
				onlinePanel.panel('setTitle', '( ' + data.total + ' )人在线');
			}
		});

		userOnlineInfoDialog = $('#userOnlineInfoDialog').show().dialog({
			modal : true,
			closed : true,
			title : '用户登陆信息'
		});

		userOnlineInfoDataGrid = $('#userOnlineInfoDataGrid').datagrid({
			showHeader : false,
			fit : true,
			fitColumns : true,
			scrollbarSize : 0,
			border : false,
			columns : [ [ {
				title : '',
				width : 100,
				field : 'value'
			}, {
				title : '',
				width : 150,
				field : 'name'
			} ] ]
		});

	});
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" style="overflow: hidden;">
		<div id="onlinePanel" data-options="fit:true,border:false" title="在线人数">
			<table id="onlineDatagrid"></table>
		</div>
	</div>

	<div id="userOnlineInfoDialog" style="display: none;width: 250px;height: 150px;">
		<table id="userOnlineInfoDataGrid"></table>
	</div>
</div>