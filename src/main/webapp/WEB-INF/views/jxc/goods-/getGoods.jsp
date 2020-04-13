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
			<label>选择部门:</label> <input id="dept_id"> 
			<label>选择日期:</label> <input id="dd" type="text">
			<a href="#" id="search-btn" class="easyui-linkbutton">部门+日期过滤</a>
			<a href="#" id="search-all" class="easyui-linkbutton">显示所有</a>
		</div>

	</div>
	<!-- End of toolbar -->

	<table id="data-datagrid" class="easyui-datagrid" toolbar="#wu-toolbar" data-options="showFooter:true"></table>
</div>
<style>
.selected {
	background: red;
}
</style>

<!-- Begin of easyui-dialog -->

<!-- 添加窗口 -->
<div id="add-dialog" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'"
	style="width: 450px; padding: 10px;">
	<form id="add-form" method="post">
		<table>
			<!-- 日期 -->
			<tr>
				<td width="60" align="right">领取日期：</td>
				<td><input type="text" id="add-getDate" name="getDate" /></td>
			</tr>
			<!-- 部门 -->
			<tr>
				<td width="60" align="right">领取部门：</td>
				<td>
					<input type="text" name="dept.deptId" class="easyui-combobox"
					data-options="required:true, missingMessage:'请从下拉框中选择部门',
					valueField:'deptId',textField:'deptName',
					url:'${ctx }/admin/dept/getComboxList',method:'post'" />
				</td>
			</tr>
			<!-- 物品 -->
			<tr>
				<td width="60" align="right">领取物品：</td>
				<td><input type="text" name="goods.goodsId" class="easyui-combobox"
					data-options="required:true, missingMessage:'请从下拉框中选择领取的物品',
					valueField:'goodsId',textField:'goodsName',
					url:'${ctx }/admin/goods/getComboxList',method:'post'" /></td>
			</tr>

			<!-- 数量 -->
			<tr>
				<td width="60" align="right">领取数量：</td>
				<td><input name="goodsNum" type="text" class="easyui-numberbox"
					data-options="min:0,precision:0,required:'true',missingMessage:'请填写正确的数字'" /></td>
			</tr>

			<!-- 备注 -->
			 <tr>
				<td align="right">备注：</td>
				<td><textarea name="remark" rows="6" class="wu-textarea" style="width: 260px"></textarea></td>
			</tr>
		</table>
	</form>
</div>


<!-- 修改窗口 -->
<div id="edit-dialog" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'"
	style="width: 450px; padding: 10px;">
	<form id="edit-form" method="post">
		<input type="hidden" name=getId id="edit-getId">
		<table>
			<!-- 日期 -->
			<tr>
				<td width="60" align="right">领取日期：</td>
				<td><input type="text" id="edit-getDate" name="getDate" /></td>
			</tr>
			<!-- 部门 -->
			<tr>
				<td width="60" align="right">领取部门：</td>
				<td>
					<input type="text" id="edit-deptId" name="dept.deptId" class="easyui-combobox"
					data-options="required:true, missingMessage:'请从下拉框中选择部门',
					valueField:'deptId',textField:'deptName',
					url:'${ctx }/admin/dept/getComboxList',method:'post'" />
				</td>
			</tr>
			<!-- 物品 -->
			<tr>
				<td width="60" align="right">领取物品：</td>
				<td><input type="text" id="edit-goodsId" name="goods.goodsId" class="easyui-combobox"
					data-options="required:true, missingMessage:'请从下拉框中选择领取的物品',
					valueField:'goodsId',textField:'goodsName',
					url:'${ctx }/admin/goods/getComboxList',method:'post'" /></td>
			</tr>

			<!-- 数量 -->
			<tr>
				<td width="60" align="right">领取数量：</td>
				<td><input type="text" id="edit-goodsNum" name="goodsNum" class="easyui-numberbox"
					data-options="min:0,precision:0,required:'true',missingMessage:'请填写正确的数字'" /></td>
			</tr>

			<!-- 备注 -->
			 <tr>
				<td align="right">备注：</td>
				<td><textarea id="edit-remark" name="remark" rows="6" class="wu-textarea" style="width: 260px"></textarea></td>
			</tr>
		</table>
	</form>
</div>


<%@include file="../../common/footer.jsp"%>

<!-- End of easyui-dialog -->
<script type="text/javascript">
	/* 时间选择框 */
	$(function() {
		$('#add-getDate').datebox({});
		$('#edit-getDate').datebox({});
		$('#add-getDate').datebox('setValue', getCurDate());
	})

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
			url : '${ctx}/getgoods/add',
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
			url : '../../admin/dept/edit',
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
					url : '../../admin/dept/delete',
					dataType : 'json',
					type : 'post',
					data : {
						id : item.deptId
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
			title : "添加请领信息",
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
		console.log(item);

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
				$("#edit-getId").val(item.getId);
				$("#edit-deptId").combobox('setValue',item.dept.deptId);
				$("#edit-goodsId").combobox('setValue',item.goods.goodsId);
				$("#edit-goodsNum").numberbox('setValue', item.goodsNum);
				$("#edit-remark").val(item.remark);	 		
				$("#edit-getDate").datebox('setValue',myformatter(new Date(item.getDate)));
			}
		});
	}

	//搜索按钮监听
	$("#search-all").click(function() {
		$('#data-datagrid').datagrid('reload',{});
	});
	$("#search-btn").click(function() {
		$('#data-datagrid').datagrid('reload',{
			'dept.deptId' : $("#dept_id").combobox('getValue'),
			'getDate' : $("#dd").datebox('getValue')
		});
	});
	

	// 部门combobox监听
	$("#dept_id").combobox({
		valueField : 'deptId',
		textField : 'deptName',
		url : '${ctx }/admin/dept/getComboxList',
		method : 'post',
		onSelect : function() {
			$("#data-datagrid").datagrid('reload', {
				'dept.deptId' : $("#dept_id").combobox('getValue')
			});
		}
	});
	// 日期datebox监听
	$("#dd").datebox({
		editable : false,
		value : getCurDate(),
		onSelect : function() {
			$("#data-datagrid").datagrid('reload', {
				'getDate' : $("#dd").datebox('getValue')
			});
		}
	});

	/* data-options="valueField:'deptId',textField:'deptName',
				url:'${ctx }/admin/dept/getComboxList',method:'post'"
					'onSelect' */

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
		idField : 'id',
		fit : true,
		columns : [ [ {
			field : 'chk',
			checkbox : true
		}, {
			field : 'getDate',
			title : '领取日期',
			width : 100,
			sortable : true,
			formatter : function(value) {
				return myformatter(new Date(value));
			}
		}, {
			field : 'dept.deptName',
			title : '部门',
			width : 100,
			sortable : true,
			formatter : function(value, row, index) {
				return row.dept.deptName;
			}
		}, {
			field : 'goods.goodsName',
			title : '物品',
			width : 100,
			sortable : true,
			formatter : function(value, row, index) {
				return row.goods.goodsName;
			}
		}, {
			field : 'goods.goodsPrice',
			title : '单价',
			width : 100,
			sortable : true,
			formatter : function(value, row, index) {
				return "¥" + (row.goods.goodsPrice / 100).toFixed(2);
			}
		}, {
			field : 'goodsNum',
			title : '数量',
			width : 100,
			sortable : true
		}, {
			field : 'getTotal',
			title : '总价',
			width : 100,
			sortable : true,
			formatter : function(value) {
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