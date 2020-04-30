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
	private Integer brandId;

    private String brandName;

    private String brandRemark;

    private String place;
			 -->
			<!-- 品牌名称 -->
			<tr>
				<td width="60" align="right">品牌名:</td>
				<td><input type="text" name="brandName" class="wu-text easyui-validatebox"
					data-options="required:true, missingMessage:'请填写品牌名'" /></td>
			</tr>
			<!-- 产地 -->
			<tr>
				<td width="60" align="right">产地:</td>
				<td><input type="text" name="place" class="wu-text" /></td>
			</tr>
			<!-- 备注 -->
			<tr>
				<td align="right">备注:</td>
				<td><textarea rows="6" style="width: 260px" class="wu-text" name="brandRemark"></textarea></td>
			</tr>
		</table>
	</form>
</div>


<!-- 修改窗口 -->
<div id="edit-dialog" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'"
	style="width: 450px; padding: 10px;">
	<form id="edit-form" method="post">
		<input type="hidden" name="brandId" id="edit-id">
		<table>
			<!-- 品牌名称 -->
			<tr>
				<td width="60" align="right">品牌名:</td>
				<td><input type="text" id="edit-name" name="brandName" class="wu-text easyui-validatebox"
					data-options="required:true, missingMessage:'请填写品牌名'" /></td>
			</tr>
			<!-- 产地 -->
			<tr>
				<td width="60" align="right">产地:</td>
				<td><input type="text" id="edit-place" name="place" class="wu-text" /></td>
			</tr>
			<!-- 备注 -->
			<tr>
				<td align="right">备注:</td>
				<td><textarea rows="6" id="edit-remark" style="width: 260px" class="wu-text" name="brandRemark"></textarea></td>
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
			url : '../brand/saveOrUpdate',
			dataType : 'json',
			type : 'post',
			data : data,
			success : function(data) {
				if (data.type == 'success') {
					$.messager.alert('信息提示', data.msg, 'info');
					$('#add-dialog').dialog('close');
					$('#data-datagrid').datagrid('reload');
					closeBrandTab();
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
			url : '../brand/saveOrUpdate',
			dataType : 'json',
			type : 'post',
			data : data,
			success : function(data) {
				if (data.type == 'success') {
					$.messager.alert('信息提示', data.msg, 'info');
					$('#edit-dialog').dialog('close');
					$('#data-datagrid').datagrid('reload');
					closeBrandTab();
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
					url : '../brand/delete',
					dataType : 'json',
					type : 'post',
					data : {
						brandId : item.brandId
					},
					success : function(data) {
						if (data.type == 'success') {
							$.messager.alert('信息提示', data.msg, 'info');
							$('#data-datagrid').datagrid('reload');
							closeBrandTab();
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
				$("#edit-id").val(item.brandId);
				$("#edit-name").val(item.brandName);
				$("#edit-place").val(item.place);
				$("#edit-remark").val(item.brandRemark);
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
		idField : 'brandId',
		fit : true,
		columns : [ [ {
			field : 'chk',
			checkbox : true
		}, {
			field : 'brandName',
			title : '品牌名称',
			width : 100,
			sortable : true
		}, {
			field : 'place',
			title : '产地',
			width : 100,
			sortable : true
		}, {
			field : 'brandRemark',
			title : '备注',
			width : 100,
			sortable : true,
		}, ] ],
		onLoadSuccess : function(data) {
			$('#data-datagrid').datagrid('unselectAll');
		}
	});
	
	function closeBrandTab(){
		closeTab('产品管理');
	}
</script>