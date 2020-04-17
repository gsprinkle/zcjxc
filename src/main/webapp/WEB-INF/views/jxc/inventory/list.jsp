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
			<label>产品名称:</label><input id="search-name" onkeyup="changeName()" class="wu-text" style="width: 100px">
			<!-- <a href="#" id="search-btn"	class="easyui-linkbutton" iconCls="icon-search">搜索</a> -->
			<label>品牌:</label><input id="search-brandId" type="text" /> <input type="checkbox" id="ck" /> <label for="ck">仅显示剩余不足项</label>
		</div>

	</div>
	<!-- End of toolbar -->
	<!-- 库存 -->
	<table id="data-datagrid" name="inventory-datagrid" class="easyui-datagrid" toolbar="#wu-toolbar"></table>
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

			<!--  产品分类，Tree 树中选择的  -->
			<input type="hidden" name="cid" id="add-product-cid" />
			<!-- 品牌 -->
			<tr>
				<td width="60" align="right">品牌:</td>
				<td><input type="text" name="brandId" class="wu-text easyui-combobox"
					data-options="required:true, missingMessage:'请填写产品名称',
						url : '../brand/getBrandDropList',
						valueField : 'brandId',textField : 'brandName'
					" /></td>
			</tr>
			<!-- 产品名称 -->
			<tr>
				<td width="60" align="right">产品名称:</td>
				<td><input type="text" name="productName" class="wu-text easyui-validatebox"
					data-options="required:true, missingMessage:'请填写产品名称'" /></td>
			</tr>

			<!-- 备注 -->
			<tr>
				<td align="right">备注:</td>
				<td><textarea name="productRemark" rows="6" class="wu-textarea" style="width: 260px"></textarea></td>
			</tr>
		</table>
	</form>
</div>

<%@include file="../../common/footer.jsp"%>

<!-- End of easyui-dialog -->
<script type="text/javascript">
// 刷新datagrid方法，保存到top中
window.top["reload_inventory"] = function(){
	$("#data-datagrid").datagrid('load');
}
	/** 
	 * 载入产品数据
	 private Integer productId;

	    private String productName;

	    private Integer brandId;
	    
	    private Integer cid;

	    private Long productPrice;

	    private Integer orderId;

	    private Integer state;

	    private String productRemark;
	    
	    private Integer inventoryId;

	    private Integer productId;

	    private Integer inventoryNum;
	    
	    private Integer storeId;// 所在仓库

	    private String inventoryRemark;
	 */
	/**
	初始化库存列表
	 */
	function initialize() {
		$.ajax({
			url : 'initialize',
			type : 'GET',
			dataType : 'json',
			success : function(data) {
				if (data.type == 'success') {
					$.messager.alert('信息提示', '初始化成功！', 'info');
					$('#data-datagrid').datagrid('reload');
				} else {
					$.messager.alert('信息提示', data.msg, 'warning');
				}
			}
		});
	}

	/**
		数据列表
	 */
	$('#data-datagrid')
			.datagrid(
					{
						url : 'list',
						rownumbers : true,
						singleSelect : true,
						pageSize : 20,
						pagination : true,
						multiSort : true,
						fitColumns : false,
						idField : 'inventoryId',
						fit : true,
						striped : true,
						rowStyler : function(index, row) {
							if (row.inventoryNum <= row.remind) {
								return 'background-color:#f55;color:#fff;';
							}
						},
						columns : [ [
								{
									field : 'chk',
									checkbox : true
								},
								{
									field : 'inventoryId',
									title : '库存ID',
									hidden : true
								},
								{
									field : 'productName',
									title : '产品名称',
									width : 100,
									sortable : true
								},
								{
									field : 'inventoryNum',
									title : '库存量',
									width : 100,
									sortable : true,
									editor : "text",
								}, {
									field : 'storeId',
									title : '所在仓库',
									width : 100,
									formatter : function(value, row) {
										return row.storeName;
									},
									editor : {
										type : 'combobox',
										options : {
											valueField : 'storeId',
											textField : 'storeName',
											method : 'get',
											url : '../store/getStoreDropList'
										}
									}
								}, {
									field : 'cid',
									title : '分类id',
									width : 100,
									sortable : true,
									hidden : true
								}, {
									field : 'cname',
									title : '分类',
									width : 100,
									sortable : true,
								}, {
									field : 'brandId',
									title : '品牌',
									width : 100,
									formatter : function(value, row) {
										return row.brandName;
									}
								}, {
									field : 'productRemark',
									title : '备注',
									width : 100,
									sortable : true,
								} ] ],
						onLoadSuccess : function(data) {
							$('#data-datagrid').datagrid('unselectAll');
							$(".easyui-tooltip").tooltip({
								onShow : function() {
									$(this).tooltip('tip').css({
										borderColor : '#000'
									});
								}
							});
						},
						onDblClickRow : onDblClickRow,
						onClickRow : onClickRow
					});
	// 编辑行 索引
	var editIndex = undefined;
	// 结束行编辑
	function endEditing() {
		if (editIndex == undefined) {
			return true;
		}
		// 获取选择的值
		var inventoryId = $("#data-datagrid").datagrid('getData').rows[editIndex].inventoryId;
		var eNum = $("#data-datagrid").datagrid('getEditor', {
			index : editIndex,
			field : 'inventoryNum'
		});
		var inventoryNum = $(eNum.target).val();
		var ed = $("#data-datagrid").datagrid('getEditor', {
			index : editIndex,
			field : 'storeId'
		});
		var storeId = $(ed.target).combobox('getValue');
		$.ajax({
			url : 'saveOrUpdate',
			dataType : 'json',
			data : {
				'inventoryId' : inventoryId,
				'inventoryNum' : inventoryNum,
				'storeId' : storeId
			},
			success : function() {
				$("#data-datagrid").datagrid('endEdit', editIndex);
				$("#data-datagrid").datagrid('reload');
				editIndex = undefined;
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

	// 监听 品牌选择下拉框，当选择品牌时，根据品牌查询产品数据
	$(function() {
		$("#search-brandId").combobox({
			url : '../brand/getBrandDropList',
			valueField : 'brandId',
			textField : 'brandName',
			editable : false,
			onSelect : function() {
				var productName = $("#search-name").val();
				var brandId = $("#search-brandId").combobox('getValue');
				var unenough = $('#ck')[0].checked;
				frashData(productName, brandId, unenough);
			}
		});
	});

	/**
		仅显示库存不足
	 */
	$('#ck').click(function() {
		var productName = $("#search-name").val();
		var brandId = $("#search-brandId").combobox('getValue');
		var unenough = $('#ck')[0].checked;
		frashData(productName, brandId, unenough);
	});
	/**
		产品名称 搜索框，根据输入的关键词搜索
	 */
	function changeName() {
		var productName = $("#search-name").val();
		var brandId = $("#search-brandId").combobox('getValue');
		var unenough = $('#ck')[0].checked;
		frashData(productName, brandId, unenough);
	}

	// 刷新数据方法
	function frashData(productName, brandId, unenough) {
		$('#data-datagrid').datagrid('reload', {
			'productName' : productName,
			'brandId' : brandId,
			'unenough' : unenough
		});
	}
</script>