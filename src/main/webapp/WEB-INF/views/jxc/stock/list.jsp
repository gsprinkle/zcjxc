<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@include file="../../common/header.jsp"%>
<div class="easyui-layout" id="stock-tab" data-options="fit:true">
	<!-- Begin of toolbar -->
	<div id="wu-toolbar">
		<div class="wu-toolbar-button">
			<%@include file="../../common/menus.jsp"%>
		</div>
		<div class="wu-toolbar-search">
			<!-- <label>产品名称:</label><input id="search-name" onkeyup="changeName()" class="wu-text" style="width: 100px"> -->
			<!-- <a href="#" id="search-btn"	class="easyui-linkbutton" iconCls="icon-search">搜索</a> -->
			<!-- 日期模式 -->
			<label>日期模式:<input type="radio" name="dateRadio" value="1">日</label> 
			<label><input type="radio" checked="checked" name="dateRadio" value="2">月</label> 
			<label style="margin-right:30px;"><input type="radio" name="dateRadio" value="3">年</label>
			<!-- 选择日期 -->
			<label>选择日期:</label><input id="search-date" type="text" /> 
			
			<!-- 日期模式 默认为月 -->
			<input type="hidden" id="dateModel" value="2" />
			<!-- 日期参数 -->
			<input type="hidden" id="date" />
		</div>
	</div>
	<!-- End of toolbar -->

	<!-- 产品 -->
	<div data-options="region:'center',title:'产品',split:true">
		<table name="stock-datagrid" id="data-datagrid" class="easyui-datagrid" toolbar="#wu-toolbar"></table>
	</div>
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
    private Date stockDate;
    private Integer productId;
    private Integer stockNum;
    private String stockRemark; 
-->
			<!--  日期  -->
			<tr>
				<td width="60" align="right">日期:</td>
				<td>
					<input type="text" name="stockDate" class="easyui-datebox" 
					data-options=" value : myformatter(new Date())"/>
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
			<!-- 进货数量 -->
			<tr>
				<td width="60" align="right">进货数量:</td>
				<td><input type="text" name="stockNum" class="easyui-numberbox"
					data-options="required:true, missingMessage:'请填写进货数量'" /></td>
			</tr>
			<!-- 备注 -->
			<tr>
				<td align="right">备注:</td>
				<td><textarea name="stockRemark" rows="6" class="wu-textarea" style="width: 260px"></textarea></td>
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
					loadData(dateModel,date);
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
		loadData(dateModel,date); 
	});
	// 选择列表模式 加载数据方法
	function loadData(dateModel, date) {
		$('#data-datagrid').datagrid('load', {			
			'dateModel' : dateModel,
			'date' : date
		});
	}
	/**
		产品名称 搜索框，根据输入的关键词搜索
	function changeName() {
		$('#data-datagrid').datagrid('reload', {
			productName : $("#search-name").val()
		})
	}*/

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
			url : '../stock/saveOrUpdate',
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
					url : '../stock/delete',
					dataType : 'json',
					type : 'post',
					data : {
						stockId : item.stockId
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
					parent.$("#wu-tabs").tabs('close','库存盘点');
					$('#add-dialog').dialog('close');
				}
			} ]
		});
	}

	/** 
	 * 载入进货列表数据
	 */
	$('#data-datagrid').datagrid({
		url : 'list',
		rownumbers : true,
		singleSelect : true,
		pageList : [20,40,60,80,100],
		pageSize : 100,
		pagination : true,
		multiSort : true,
		fitColumns : false,
		idField : 'stockId',
		fit : true,
		queryParams : {
			dateModel : $("#dateModel").val(),
			date : $("#date").val()
		},
		columns : [ [ {
			field : 'chk',
			checkbox : true
		}, {
			field : 'stockDate',
			title : '进货日期',
			width : 100,
			sortable : true,
			formatter : function(value){
				return myformatter(new Date(value));
			}
		},  {
			field : 'productId',
			title : '产品',
			width : 100,
			sortable : true,
			formatter : function(value,row){
				return row.productName;
			}
		}, {
			field : 'stockNum',
			title : '数量',
			width : 100,
			sortable : true,
			editor : 'text'
		}, {
			field : 'stockRemark',
			title : '备注',
			width : 100,
			sortable : true
		}, ] ],
		onLoadSuccess : function(data) {
			$('#data-datagrid').datagrid('unselectAll');
		},
		onDblClickRow : onDblClickRow,
		onClickRow :onClickRow
	});
	// 编辑行 索引
	var editIndex = undefined;
	// 结束行编辑
	function endEditing() {
		if (editIndex == undefined) {
			return true;
		}
		// 获取选择的值
		var stockId = $("#data-datagrid").datagrid('getData').rows[editIndex].stockId;
		var productId = $("#data-datagrid").datagrid('getData').rows[editIndex].productId;
		
		var eNum = $("#data-datagrid").datagrid('getEditor', {
			index : editIndex,
			field : 'stockNum'
		});
		var stockNum = $(eNum.target).val();
		$.ajax({
			url : 'saveOrUpdate',
			dataType : 'json',
			data : {
				'stockId' : stockId,
				'stockNum' : stockNum,
				'productId' : productId
			},
			success : function() {
				$("#data-datagrid").datagrid('endEdit', editIndex);
				$("#data-datagrid").datagrid('reload');
				editIndex = undefined;
				// 刷新库存列表
				setTimeout("parent.reloadTabGrid('库存盘点')",50);
			}
		});

	}
	// 双击开启行编辑
	function onDblClickRow(index) {
		if (editIndex != index) {
			if (endEditing()) {
				$("#data-datagrid").datagrid('beginEdit', index);
				editIndex = index;
			} else {
				$('#data-datagrid').datagrid('selectRow', editIndex);
			}
		}
	}
	// 单击其它的行，结束编辑
	function onClickRow() {
		endEditing();
	}
	// 编辑好之后保存
	function save(){
		endEditing();
	}
	// 编辑完之后，取消保存
	function cancel(){
		if (editIndex == undefined) {
			return;
		}else{
			editIndex = undefined;
			$("#data-datagrid").datagrid('endEdit', editIndex);
			$("#data-datagrid").datagrid('reload');
		}
	}
	
</script>