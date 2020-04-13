<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@include file="../../common/header.jsp"%>
<div class="easyui-layout" data-options="fit:true">
	<!-- Begin of toolbar -->
	<div id="wu-toolbar">
		<div class="wu-toolbar-button">
			<%@include file="../../common/menus.jsp"%>
		</div>
		<div class="wu-toolbar-search">
			<label>物品名称:</label><input id="search-name" class="wu-text" style="width: 100px"> <a href="#" id="search-btn"
				class="easyui-linkbutton" iconCls="icon-search">搜索</a>
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
			<tr>
				<td width="60" align="right">物品名称:</td>
				<td><input type="text" name="goodsName" class="wu-text easyui-validatebox"
					data-options="required:true, missingMessage:'请填写物品名称'" /></td>
			</tr>
			<tr>
				<td width="60" align="right">单价:</td>
				<td><input type="text" id="add-price-view" class="easyui-numberbox easyui-validatebox"
					data-options="precision:2,required:true, missingMessage:'物品价格，只可填写数字'" />
					<input type="hidden" id="add-price" name="goodsPrice" />
				</td>
			</tr>
			<tr>
				<td align="right">物品备注:</td>
				<td><textarea name="remark" rows="6" class="wu-textarea" style="width: 260px"></textarea></td>
			</tr>
		</table>
	</form>
</div>


<!-- 修改窗口 -->
<div id="edit-dialog" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'"
	style="width: 450px; padding: 10px;">
	<form id="edit-form" method="post">
		<input type="hidden" name="goodsId" id="edit-id">
		<table>
			<tr>
				<td width="60" align="right">物品名称:</td>
				<td><input type="text" id="edit-name" name="goodsName" class="wu-text easyui-validatebox"
					data-options="required:true, missingMessage:'请填写物品名称'" /></td>
			</tr>
			<tr>
				<td width="60" align="right">单价:</td>
				<td><input type="text" id="edit-price-view" class="easyui-numberbox easyui-validatebox"
					data-options="precision:2,required:true, missingMessage:'物品价格，只可填写数字'" /> 
					<input type="hidden" id="edit-price" name="goodsPrice" />
				</td>
			</tr>
			<tr>
				<td align="right">备注:</td>
				<td><textarea id="edit-remark" name="remark" rows="6" class="wu-textarea" style="width: 260px"></textarea></td>
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
		// 在提交前，把元改为分	
		$("#add-price").val(eval($("#add-price-view").val() * 100));
		var data = $("#add-form").serialize();
		$.ajax({
			url : '../../admin/goods/add',
			dataType : 'json',
			type : 'post',
			data : data,
			success : function(data) {
				if (data.type == 'success') {
					$.messager.alert('信息提示', '添加成功！', 'info');
					$('#add-dialog').dialog('close');
					$('#data-datagrid').datagrid('reload');
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
		// 在提交前，把元改为分	
		$("#edit-price").val(eval($("#edit-price-view").val() * 100));

		var data = $("#edit-form").serialize();
		$.ajax({
			url : '../../admin/goods/edit',
			dataType : 'json',
			type : 'post',
			data : data,
			success : function(data) {
				if (data.type == 'success') {
					$.messager.alert('信息提示', '修改成功！', 'info');
					$('#edit-dialog').dialog('close');
					$('#data-datagrid').datagrid('reload');
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
					url : '../../admin/goods/delete',
					dataType : 'json',
					type : 'post',
					data : {
						id : item.goodsId
					},
					success : function(data) {
						if (data.type == 'success') {
							$.messager.alert('信息提示', '删除成功！', 'info');
							$('#data-datagrid').datagrid('reload');
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
			title : "添加物品信息",
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
						$("#edit-id").val(item.goodsId);
						$("#edit-name").val(item.goodsName);
						$("#edit-price-view").numberbox('setValue',
								(item.goodsPrice / 100).toFixed(2));
						$("#edit-remark").val(item.remark);
					}
				});
	}

	//搜索按钮监听
	$("#search-btn").click(function() {
		$('#data-datagrid').datagrid('reload', {
			name : $("#search-name").val()
		});
	});

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
		fitColumns : true,
		idField : 'id',
		fit : true,
		columns : [ [ {
			field : 'chk',
			checkbox : true
		}, {
			field : 'goodsName',
			title : '物品名称',
			width : 100,
			sortable : true
		}, {
			field : 'goodsPrice',
			title : '物品价格',
			width : 100,
			sortable : true,
			formatter : function(value, row, index) {
				return "¥" + (value / 100).toFixed(2);
			}
		}, {
			field : 'remark',
			title : '备注',
			width : 100,
			sortable : true
		}, ] ],
		onLoadSuccess : function(data) {
			$('#data-datagrid').datagrid('unselectAll');
		}
	});
</script>