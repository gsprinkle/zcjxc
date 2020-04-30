<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@include file="../../common/header.jsp"%>
<div class="easyui-layout" id="sale-tab" data-options="fit:true">
	<!-- Begin of toolbar -->
	<div id="wu-toolbar">
		<div class="wu-toolbar-button">
			<%@include file="../../common/menus.jsp"%>
		</div>
		<div class="wu-toolbar-search">
			<!-- <label>产品名称:</label><input id="search-name" onkeyup="changeName()" class="wu-text" style="width: 100px"> -->
			<!-- <a href="#" id="search-btn"	class="easyui-linkbutton" iconCls="icon-search">搜索</a> -->
			<!-- 日期模式 -->

			<label>日期模式:<input type="radio" name="dateRadio" value="1">日
			</label> <label><input type="radio" checked="checked" name="dateRadio" value="2">月</label> <label
				style="margin-right: 30px;"><input type="radio" name="dateRadio" value="3">年</label>
			<!-- 选择日期 -->
			<label>选择日期:</label><input id="search-date" type="text" />

			<!-- 日期模式 默认为月 -->
			<input type="hidden" id="dateModel" value="2" />
			<!-- 日期参数 -->
			<input type="hidden" id="date" />
		</div>

	</div>
	<!-- End of toolbar -->

	<!-- 销售列表 -->
	<table name="sale-datagrid" id="data-datagrid" class="easyui-datagrid" toolbar="#wu-toolbar"></table>
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
			<!--  日期  -->
			<tr>
				<td width="60" align="right">日期:</td>
				<td><input type="text" name="saleDate" class="easyui-datebox" data-options=" value : myformatter(new Date())" />
				</td>
			</tr>
			<!-- 产品 -->
			<tr>
				<td width="60" align="right">产品:</td>
				<td><input type="text" name="productId" class="easyui-combobox"
					data-options="required:true, missingMessage:'请选产品',
						url : '../product/getProductDropList',
						valueField : 'productId',textField : 'productName'
					" /></td>
			</tr>
			<!-- 销售数量 -->
			<tr>
				<td width="60" align="right">销售数量:</td>
				<td><input type="text" name="saleNum" class="easyui-numberbox"
					data-options="required:true, missingMessage:'请填写销售数量'" /></td>
			</tr>
			<!-- 客户 -->
			<tr>
				<td width="60" align="right">客户:</td>
				<td><input type="text" name="custId" class="easyui-combobox"
					data-options="required:true, missingMessage:'请选产品',
						url : '../customer/getCustomerDropList',
						valueField : 'custId',textField : 'custName'
					" /></td>
			</tr>
			<!-- 销售人 
			<tr>
				<td width="60" align="right">销售人:</td>
				<td><input type="text" name="eid" class="easyui-combobox"
					data-options="required:true, missingMessage:'请选产品',
						url : '../employee/getEmployeeDropList',
						valueField : 'eid',textField : 'ename'
					" /></td>
			</tr>-->
			<!-- 备注 -->
			<tr>
				<td align="right">备注:</td>
				<td><textarea name="saleRemark" rows="6" class="wu-textarea" style="width: 260px"></textarea></td>
			</tr>
		</table>
	</form>
</div>

<div id="edit-dialog" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'"
	style="width: 450px; padding: 10px;">
	<form id="edit-form" method="post">
		<input type="hidden" id="edit-id" name="saleId" />
		<table>
			<!--  日期  -->
			<tr>
				<td width="60" align="right">日期:</td>
				<td><input type="text" id="edit-date" name="saleDate" class="easyui-datebox" /></td>
			</tr>
			<!-- 产品 -->
			<tr>
				<td width="60" align="right">产品:</td>
				<td><input type="text" id="edit-productId" name="productId" class="easyui-combobox"
					data-options="required:true, missingMessage:'请选产品',
						url : '../product/getProductDropList',
						valueField : 'productId',textField : 'productName'
					" /></td>
			</tr>
			<!-- 销售数量 -->
			<tr>
				<td width="60" align="right">销售数量:</td>
				<td><input type="text" id="edit-saleNum" name="saleNum" class="easyui-numberbox"
					data-options="required:true, missingMessage:'请填写销售数量'" /></td>
			</tr>
			<!-- 客户 -->
			<tr>
				<td width="60" align="right">客户:</td>
				<td><input type="text" id="edit-custId" name="custId" class="easyui-combobox"
					data-options="required:true, missingMessage:'请选产品',
						url : '../customer/getCustomerDropList',
						valueField : 'custId',textField : 'custName'
					" /></td>
			</tr>
			<!-- 销售人 
			<tr>
				<td width="60" align="right">销售人:</td>
				<td><input type="text" id="edit-eid" name="eid" class="easyui-combobox"
					data-options="required:true, missingMessage:'请选员工',
						url : '../employee/getEmployeeDropList',
						valueField : 'eid',textField : 'ename'
					" /></td>
			</tr>-->
			<!-- 备注 -->
			<tr>
				<td align="right">备注:</td>
				<td><textarea name="saleRemark" rows="6" class="wu-textarea" style="width: 260px"></textarea></td>
			</tr>
		</table>
	</form>
</div>

<%@include file="../../common/footer.jsp"%>

<!-- End of easyui-dialog -->
<script type="text/javascript">
	$("#date").val(getYearMonth());
	// 日期选择，默认为月
	$("#search-date").datebox(
			{
				value : getYearMonth(),
				onSelect : function(date) {
					// 设置日期
					$("#date").val($(this).datebox('getValue'));
					// 重新加载数据
					var dateModel = $("#dateModel").val();
					var date = $("#date").val();
					loadData(dateModel, date);
				},
				formatter : function(date) {
					var y = date.getFullYear();
					var m = date.getMonth() + 1;
					var d = date.getDate();
					var dateModel = $('#dateModel').val();
					if (dateModel == 1) {
						return y + '-' + (m < 10 ? ('0' + m) : m) + '-'
								+ (d < 10 ? ('0' + d) : d);
					}
					if (dateModel == 2) {
						return y.toString() + '-' + (m < 10 ? '0' + m : m);
					}
					if (dateModel == 3) {
						return y.toString();
					}
				},
				parser : function(date) {
					//console.log(date);
					if (date) {
						var dateModel = $('#dateModel').val();
						if (dateModel == 2) {
							return new Date(String(date).substring(0, 4) + '-'
									+ String(date).substring(5, 7));
						}
						if (dateModel == 3) {
							return new Date(String(date).substring(0, 4));
						} else {
							return new Date(String(date));
						}
					} else {
						return new Date();
					}
				}
			});

	// 日期类型查询 1：日 	2：月	3：年
	$(":radio[name='dateRadio']").click(function() {
		// 获取dateModel值，并设置到隐藏域中
		var dateModel = $(this).val();
		$("#dateModel").val(dateModel);
		// 清空当前日期框
		$("#search-date").datebox('clear');
		// 默认设置为当前时间，格式根据dateModel决定,date也设置
		var curDate = new Date();
		if (dateModel == 1) {
			$("#search-date").datebox('setValue', myformatter(curDate));
			$("#date").val(myformatter(curDate));
		}
		if (dateModel == 2) {
			$("#search-date").datebox('setValue', getYearMonth());
			$("#date").val(getYearMonth());
		}
		if (dateModel == 3) {
			$("#search-date").datebox('setValue', curDate.getFullYear());
			$("#date").val(curDate.getFullYear());
		}
		var date = $("#date").val();
		loadData(dateModel, date);
	});
	// 选择列表模式 加载数据方法
	function loadData(dateModel, date) {
		$('#data-datagrid').datagrid('load', {
			'dateModel' : dateModel,
			'date' : date
		});
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
			url : '../sale/saveOrUpdate',
			dataType : 'json',
			type : 'post',
			data : data,
			success : function(data) {
				if (data.type == 'success') {
					$.messager.alert('信息提示', data.msg, 'info');
					$('#add-dialog').dialog('close');
					$('#data-datagrid').datagrid('reload');
					closeSaleTab();
				} else {
					$.messager.alert('信息提示', data.msg, 'warning');
				}
			}
		});
	}
	/**
	 *  修改记录
	 */
	function edit() {
		var validate = $("#edit-form").form("validate");
		if (!validate) {
			$.messager.alert("消息提醒", "请检查你输入的数据!", "warning");
			return;
		}
		var data = $("#edit-form").serialize();
		$.ajax({
			url : '../sale/saveOrUpdate',
			dataType : 'json',
			type : 'post',
			data : data,
			success : function(data) {
				if (data.type == 'success') {
					$.messager.alert('信息提示', data.msg, 'info');
					$('#edit-dialog').dialog('close');
					$('#data-datagrid').datagrid('reload');
					closeSaleTab();
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
		var item = $('#data-datagrid').datagrid('getSelected');
		if (!item) {
			$.messager.alert('信息提示', '请选择要删除的数据！', 'info');
			return;
		}
		$.messager.confirm('信息提示', '确定要删除该记录？', function(result) {
			if (result) {
				var item = $('#data-datagrid').datagrid('getSelected');
				$.ajax({
					url : '../sale/delete',
					dataType : 'json',
					type : 'post',
					data : {
						saleId : item.saleId
					},
					success : function(data) {
						if (data.type == 'success') {
							$.messager.alert('信息提示', data.msg, 'info');
							$('#data-datagrid').datagrid('reload');
							closeSaleTab();
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
		$('#add-dialog').dialog({
			closed : false,
			modal : true,
			title : "添加信息",
			buttons : [ {
				text : '确定',
				iconCls : 'icon-ok',
				handler : add
			}, {
				text : '取消',
				iconCls : 'icon-cancel',
				handler : function() {
					parent.$("#wu-tabs").tabs('close', '库存盘点');
					$('#add-dialog').dialog('close');
				}
			} ]
		});
	}
	/**
	 * Name 打开修改窗口
	 */
	function openEdit() {
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
							parent.$("#wu-tabs").tabs('close', '库存');
							$('#edit-dialog').dialog('close');
						}
					} ],
					onBeforeOpen : function() {
						$("#edit-id").val(item.saleId);
						$("#edit-date").datebox('setValue',
								myformatter(new Date(item.saleDate)));
						$("#edit-productId").combobox('setValue',
								item.productId);
						$("#edit-saleNum").numberbox('setValue', item.saleNum);
						$("#edit-custId").combobox('setValue', item.custId);
						//$("#edit-eid").combobox('setValue',item.eid);
						$("#edit-remark").val(item.saleRemark);
					}
				});
	}

	/** 
	 * 载入销售列表数据
	 */
	$('#data-datagrid').datagrid({
		url : 'list',
		rownumbers : true,
		singleSelect : true,
		pageList : [ 20, 40, 60, 80, 100 ],
		pageSize : 100,
		pagination : true,
		multiSort : true,
		fitColumns : false,
		idField : 'saleId',
		fit : true,
		queryParams : {
			dateModel : $("#dateModel").val(),
			date : $("#date").val()
		},
		columns : [ [ {
			field : 'chk',
			checkbox : true
		}, {
			field : 'saleDate',
			title : '销售日期',
			width : 100,
			sortable : true,
			formatter : function(value) {
				return myformatter(new Date(value));
			}
		}, {
			field : 'productId',
			title : '产品',
			width : 100,
			sortable : true,
			formatter : function(value, row) {
				return row.productName;
			}
		}, {
			field : 'saleNum',
			title : '数量',
			width : 100,
			sortable : true,
		}, {
			field : 'custId',
			title : '客户',
			width : 100,
			sortable : true,
			formatter : function(val, row) {
				if (val) {
					return row.custName;
				}
			}
		}, /* {
				field : 'eid',
				title : '销售人',
				width : 100,
				sortable : true,
				formatter : function(val,row){
					if(val){
						return row.ename;
					}
				}
			}, */{
			field : 'saleRemark',
			title : '备注',
			width : 100,
			sortable : true
		}, {
			field : 'username',
			title : '创建者',
			width : 100,
			sortable : true
		}, ] ],
		onLoadSuccess : function(data) {
			$('#data-datagrid').datagrid('unselectAll');
		},
	});

	function closeSaleTab() {
		closeTab('库存');
	}
</script>