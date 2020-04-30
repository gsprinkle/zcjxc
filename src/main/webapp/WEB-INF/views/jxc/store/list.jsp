<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@include file="../../common/header.jsp"%>
<div class="easyui-layout" data-options="fit:true">
	<!-- Begin of toolbar -->
	<div id="wu-toolbar">
		<div class="wu-toolbar-button">
			<%@include file="../../common/menus.jsp"%>
		</div>
	</div>
	<!-- End of toolbar -->
	
	<table id="data-datagrid" class="easyui-datagrid" toolbar="#wu-toolbar"></table>
</div>
<style>
.selected {
	background: red;
}
</style>
<!-- Begin of easyui-dialog -->
<div id="add-dialog" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'"
	style="width: 450px; padding: 10px;">
	<form id="add-form" method="post">
		<table>
		<!-- 
		`store_id` int(2) NOT NULL AUTO_INCREMENT COMMENT '�ֿ���',
  `store_name` varchar(15) DEFAULT NULL COMMENT '�ֿ�����',
  `store_remark` varchar(255) DEFAULT NULL, 
  -->
			<tr>
				<td width="60" align="right">仓库名称:</td>
				<td><input type="text" name="storeName" class="wu-text easyui-validatebox"
					data-options="required:true, missingMessage:'请填写仓库名称'" /></td>
			</tr>
			<tr>
				<td align="right">备注:</td>
				<td><textarea name="storeRemark" rows="6" class="wu-textarea" style="width: 260px"></textarea></td>
			</tr>
		</table>
	</form>
</div>


<!-- 修改窗口 -->
<div id="edit-dialog" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'"
	style="width: 450px; padding: 10px;">
	<form id="edit-form" method="post">
		<input type="hidden" name="storeId" id="edit-id">
		<table>
			<tr>
				<td width="60" align="right">仓库名称:</td>
				<td><input type="text" id="edit-name" name="storeName" class="wu-text easyui-validatebox"
					data-options="required:true, missingMessage:'请填写仓库名称'" /></td>
			</tr>
			<tr>
				<td align="right">备注:</td>
				<td><textarea id="edit-remark" name="storeRemark" rows="6" class="wu-textarea" style="width: 260px"></textarea></td>
			</tr>
		</table>
	</form>
</div>


<%@include file="../../common/footer.jsp"%>

<!-- End of easyui-dialog -->
<script type="text/javascript">
	/**
	 *  添加记录
	 */
	function add() {
		var validate = $("#add-form").form("validate");
		if (!validate) {
			$.messager.alert("消息提醒", "请检查你输入的数据!", "warning");
			return;
		}
		var data = $("#add-form").serialize();
		$.ajax({
			url : '../store/saveOrUpdate',
			dataType : 'json',
			type : 'post',
			data : data,
			success : function(data) {
				if (data.type == 'success') {
					$.messager.alert('信息提示', data.msg, 'info');
					$('#add-dialog').dialog('close');
					$('#data-datagrid').datagrid('reload');
					closeStoreTab();
				} else {
					$.messager.alert('信息提示', data.msg, 'warning');
				}
			}
		});
	}

	/**
	 * Name 修改记录
	 */
	function edit() {
		var validate = $("#edit-form").form("validate");
		if (!validate) {
			$.messager.alert("消息提醒", "请检查你输入的数据!", "warning");
			return;
		}

		var data = $("#edit-form").serialize();
		$.ajax({
			url : '../store/saveOrUpdate',
			dataType : 'json',
			type : 'post',
			data : data,
			success : function(data) {
				if (data.type == 'success') {
					$.messager.alert('信息提示', data.msg, 'info');
					$('#edit-dialog').dialog('close');
					$('#data-datagrid').datagrid('reload');
					closeStoreTab();
				} else {
					$.messager.alert('信息提示', data.msg, 'warning');
				}
			}
		});
	}

	/**
	 * 删除记录
	 */
	function remove() {
		$.messager.confirm('信息提示', '确定要删除该记录？', function(result) {
			if (result) {
				var item = $('#data-datagrid').datagrid('getSelected');
				$.ajax({
					url : '../store/delete',
					dataType : 'json',
					type : 'post',
					data : {
						storeId : item.storeId
					},
					success : function(data) {
						if (data.type == 'success') {
							$.messager.alert('信息提示',data.msg, 'info');
							$('#data-datagrid').datagrid('reload');
							closeStoreTab();
						} else {
							$.messager.alert('信息提示', data.msg, 'warning');
						}
					}
				});
			}
		});
	}

	/**
	 * Name 打开添加窗口
	 */
	function openAdd() {
		//$('#add-form').form('clear');
		$('#add-dialog').dialog({
			closed : false,
			modal : true,
			title : "添加仓库信息",
			buttons : [ {
				text : '确定',
				iconCls : 'icon-ok',
				handler : add
			}, {
				text : '取消',
				iconCls : 'icon-cancel',
				handler : function() {
					$('#add-dialog').dialog('close');
				}
			} ]
		});
	}

	/**
	 * 打开修改窗口
	 */
	function openEdit() {
		//$('#edit-form').form('clear');
		var item = $('#data-datagrid').datagrid('getSelected');
		if (item == null || item.length == 0) {
			$.messager.alert('信息提示', '请选择要修改的数据！', 'info');
			return;
		}

		$('#edit-dialog').dialog(
				{
					closed : false,
					modal : true,
					title : "修改信息",
					buttons : [ {
						text : '确定',
						iconCls : 'icon-ok',
						handler : edit
					}, {
						text : '取消',
						iconCls : 'icon-cancel',
						handler : function() {
							$('#edit-dialog').dialog('close');
						}
					} ],
					onBeforeOpen : function() {
						$("#edit-id").val(item.storeId);
						$("#edit-name").val(item.storeName);
						$("#edit-remark").val(item.storeRemark);
					}
				});
	}

	/** 
	 * 载入数据
	 */
	$('#data-datagrid').datagrid({
		url : 'list',
		rownumbers : true,
		singleSelect : true,
		pageSize : 20,
		pagination : true,
		multiSort : true,
		fitColumns : false,
		idField : 'storeId',
		fit : true,
		columns : [ [ {
			field : 'chk',
			checkbox : true
		}, {
			field : 'storeName',
			title : '仓库名称',
			width : 200,
			sortable : true
		}, {
			field : 'storeRemark',
			title : '备注',
			width : 300,
			sortable : true
		}, ] ],
		onLoadSuccess : function(data) {
			$('#data-datagrid').datagrid('unselectAll');
		}
	});
	
	function closeStoreTab(){
		closeTab('产品管理');
	}
</script>