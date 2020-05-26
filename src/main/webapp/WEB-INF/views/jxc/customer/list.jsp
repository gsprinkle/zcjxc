<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@include file="../../common/header.jsp"%>
<div class="easyui-layout" data-options="fit:true">
	<!-- Begin of toolbar -->
	<div id="wu-toolbar">
		<div class="wu-toolbar-button">
			<%@include file="../../common/menus.jsp"%>
		</div>
		<label>关键词：</label> <input type="text" id="search-name" onkeyup="searchByName()" />
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
			<!-- 客户名称 -->
			<tr>
				<td width="60" align="right">姓名:</td>
				<td><input type="text" name="custName" class="wu-text easyui-validatebox"
					data-options="required:true, missingMessage:'请填写客户名'" /></td>
			</tr>
			<!-- 电话 -->
			<tr>
				<td width="60" align="right">电话:</td>
				<td><input type="text" name="custTel" class="wu-text" /></td>
			</tr>
			<!-- 地址 -->
			<tr>
				<td width="60" align="right">地址:</td>
				<td><input type="text" name="custAddress" class="wu-text" /></td>
			</tr>
			<!-- 公司 -->
			<tr>
				<td width="60" align="right">公司:</td>
				<td><input type="text" name="custCompany" class="wu-text" /></td>
			</tr>
			
			<!-- 备注 -->
			<tr>
				<td align="right">备注:</td>
				<td><textarea rows="6" style="width: 260px" class="wu-text" name="custRemark"></textarea></td>
			</tr>
		</table>
	</form>
</div>


<!-- 修改窗口 -->
<div id="edit-dialog" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'"
	style="width: 450px; padding: 10px;">
	<form id="edit-form" method="post">
		<input type="hidden" name="custId" id="edit-id">
		<table>
			<!-- 客户名称 -->
			<tr>
				<td width="60" align="right">姓名:</td>
				<td><input type="text" id="edit-name" name="custName" class="wu-text easyui-validatebox"
					data-options="required:true, missingMessage:'请填写客户名'" /></td>
			</tr>
			<!-- 电话 -->
			<tr>
				<td width="60" align="right">电话:</td>
				<td><input type="text" id="edit-tel" name="custTel" class="wu-text" /></td>
			</tr>
			<!-- 地址 -->
			<tr>
				<td width="60" align="right">地址:</td>
				<td><input type="text" id="edit-address" name="custAddress" class="wu-text" /></td>
			</tr>
			<!-- 公司 -->
			<tr>
				<td width="60" align="right">公司:</td>
				<td><input type="text" id="edit-company" name="custCompany" class="wu-text" /></td>
			</tr>
			<!-- 备注 -->
			<tr>
				<td align="right">备注:</td>
				<td><textarea rows="6" id="edit-remark" style="width: 260px" class="wu-text" name="custRemark"></textarea></td>
			</tr>
		</table>
	</form>
</div>


<%@include file="../../common/footer.jsp"%>

<!-- End of easyui-dialog -->
<script type="text/javascript">
	/*
		根据客户名称搜索
	 */
	function searchByName() {
		$("#data-datagrid").datagrid('reload', {
			custName : $("#search-name").val()
		})
	}
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
			url : '../customer/saveOrUpdate',
			dataType : 'json',
			type : 'post',
			data : data,
			success : function(data) {
				if (data.type == 'success') {
					$.messager.alert('信息提示', data.msg, 'info');
					$('#add-dialog').dialog('close');
					$('#data-datagrid').datagrid('reload');
					closeCustTab();
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
			url : '../customer/saveOrUpdate',
			dataType : 'json',
			type : 'post',
			data : data,
			success : function(data) {
				if (data.type == 'success') {
					$.messager.alert('信息提示', data.msg, 'info');
					$('#edit-dialog').dialog('close');
					$('#data-datagrid').datagrid('reload');
					closeCustTab();
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
					url : '../customer/delete',
					dataType : 'json',
					type : 'post',
					data : {
						custId : item.custId
					},
					success : function(data) {
						if (data.type == 'success') {
							$.messager.alert('信息提示', data.msg, 'info');
							$('#data-datagrid').datagrid('reload');
							closeCustTab();
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
			title : "添加部门信息",
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

		$('#edit-dialog').dialog({
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
				$("#edit-id").val(item.custId);
				$("#edit-name").val(item.custName);
				$("#edit-company").val(item.custCompany);
				$("#edit-address").val(item.custAddress);
				$("#edit-tel").val(item.custTel);
				$("#edit-remark").val(item.custRemark);
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
		idField : 'custId',
		fit : true,
		columns : [ [ {
			field : 'chk',
			checkbox : true
		}, {
			field : 'custName',
			title : '姓名',
			width : 100,
			sortable : true
		}, {
			field : 'custTel',
			title : '联系电话',
			width : 100,
			sortable : true
		},  {
			field : 'custAddress',
			title : '地址',
			width : 500,
			sortable : true
		}, {
			field : 'custCompany',
			title : '公司名称',
			width : 200,
			sortable : true
		},{
			field : 'createTime',
			title : '创建日期',
			width : 100,
			sortable : true,
			formatter : function(val) {
				return myformatter(new Date(val))
			}
		}, {
			field : 'custRemark',
			title : '备注',
			width : 100,
			sortable : true,
		},{
			field : 'username',
			title : '创建者',
			width : 100,
			sortable : true,
		}, ] ],
		onLoadSuccess : function(data) {
			$('#data-datagrid').datagrid('unselectAll');
		}
	});
	
	function closeCustTab(){
		closeTab('销售');
	}
</script>