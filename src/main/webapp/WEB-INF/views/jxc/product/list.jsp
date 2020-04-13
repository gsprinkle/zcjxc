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
			<label>品牌:</label><input id="search-brandId" type="text" />
		</div>

	</div>
	<!-- End of toolbar -->
	<!-- 分类 -->
	<div data-options="region:'west',title:'分类',split:true" style="width: 300px;">
		<ul id="tt"></ul>
	</div>
	<!-- 产品 -->
	<div data-options="region:'center',title:'产品',split:true">
		<table id="data-datagrid" class="easyui-datagrid" toolbar="#wu-toolbar"></table>

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




<!-- 修改窗口 -->
<div id="edit-dialog" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'"
	style="width: 450px; padding: 10px;">
	<form id="edit-form" method="post">
		<input type="hidden" id="edit-id" name="productId">
		<table>
			<!-- 品牌 -->
			<tr>
				<td width="60" align="right">品牌:</td>
				<td><input type="text" id="edit-brand" name="brandId" class="wu-text easyui-combobox"
					data-options="required:true, missingMessage:'请填写产品名称',
						url : '../brand/getBrandDropList',
						valueField : 'brandId',textField : 'brandName'
					" /></td>
			</tr>

			<!-- 分类 -->
			<tr>
				<td width="60" align="right">分类:</td>
				<td><input type="text" id="edit-cid" name="cid" class="wu-text easyui-combobox"
					data-options="required:true, missingMessage:'请选择分类',
						url : '../category/getCategoryDropList',
						valueField : 'cid',textField : 'cname'
					" /></td>
			</tr>
			<!-- 产品名称 -->
			<tr>
				<td width="60" align="right">产品名称:</td>
				<td><input type="text" id="edit-name" name="productName" class="wu-text easyui-validatebox"
					data-options="required:true, missingMessage:'请填写产品名称'" /></td>
			</tr>

			<!-- 备注 -->
			<tr>
				<td align="right">备注:</td>
				<td><textarea id="edit-remark" name="productRemark" rows="6" class="wu-textarea" style="width: 260px"></textarea></td>
			</tr>
		</table>
	</form>
</div>


<%@include file="../../common/footer.jsp"%>

<!-- End of easyui-dialog -->
<script type="text/javascript">
	/** 
	 * 载入产品数据
	 */
	$('#data-datagrid').datagrid({
		url : 'list',
		rownumbers : true,
		singleSelect : true,
		pageSize : 20,
		pagination : true,
		multiSort : true,
		fitColumns : false,
		idField : 'productId',
		fit : true,
		columns : [ [ {
			field : 'chk',
			checkbox : true
		}, {
			field : 'productName',
			title : '产品名称',
			width : 100,
			sortable : true
		},  {
			field : 'cname',
			title : '分类',
			width : 100,
			sortable : true,
		},{
			field : 'brandId',
			title : '品牌ID',
			width : 100,
			sortable : true,
			hidden : true
		}, {
			field : 'brandName',
			title : '品牌',
			width : 100,
			sortable : true,
		}, {
			field : 'cid',
			title : '分类id',
			width : 100,
			sortable : true,
			hidden : true
		}, {
			field : 'productRemark',
			title : '备注',
			width : 100,
			sortable : true
		}, ] ],
		onLoadSuccess : function(data) {
			$('#data-datagrid').datagrid('unselectAll');
		}
	});
	/** 
	 * 载入分类数据
	 */
	$('#tt').tree({
		url : '../category/list',
		method : 'post',
		animate : true,
		checkbox : true,
		onlyLeafCheck : true,
		onContextMenu : function(e, node) {
			e.preventDefault();
			$(this).tree('select', node.target);
			$('#mm').menu('show', {
				left : e.pageX,
				top : e.pageY
			});
		},
		onSelect : function(node){
			$('#data-datagrid').datagrid('reload', {
				cid : $(this).tree('getSelected').id
			});
		}
		
	});

	// 监听 品牌选择下拉框，当选择品牌时，根据品牌查询产品数据
	$(function() {
		$("#search-brandId").combobox({
			url : '../brand/getBrandDropList',
			valueField : 'brandId',
			textField : 'brandName',
			editable : false,
			onSelect : function() {
				var node = $('#tt').tree('getSelected');
				var cid;
				if(node){
					cid = node.id;
				}
				if(cid){
					$('#data-datagrid').datagrid('reload', {
						brandId : $("#search-brandId").combobox('getValue'),
						'cid' : cid
					});
				}else{
					$('#data-datagrid').datagrid('reload', {
						brandId : $("#search-brandId").combobox('getValue')
					});
				}
			}
		});

	});

	/**
		产品名称 搜索框，根据输入的关键词搜索
	 */
	function changeName() {
		$('#data-datagrid').datagrid('reload', {
			productName : $("#search-name").val()
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
			url : '../product/saveOrUpdate',
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

		var data = $("#edit-form").serialize();
		$.ajax({
			url : '../product/saveOrUpdate',
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
		var item = $('#data-datagrid').datagrid('getSelected');
		if(!item){
			$.messager.alert('信息提示', '请选择要删除的数据！', 'info');
			return ;
		}
		$.messager.confirm('信息提示', '确定要删除该记录？', function(result) {
			if (result) {
				var item = $('#data-datagrid').datagrid('getSelected');
				$.ajax({
					url : '../product/delete',
					dataType : 'json',
					type : 'post',
					data : {
						productId : item.productId
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
		var node = $("#tt").tree('getChecked', 'checked');
		if (node.length < 1) {
			$.messager.alert('Warning', "请在左边勾选一个分类");
			return;
		} else if (node.length > 1) {
			$.messager.alert('Warning', "只能勾选一个分类");
			return;
		}
		// console.log(node);
		// 填充 产品 分类 表单项
		$("#add-product-cid").val(node[0].id);

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
				$("#edit-id").val(item.productId);
				$("#edit-brand").combobox("setValue", item.brandId);
				$("#edit-cid").combobox("setValue", item.cid);
				$("#edit-name").val(item.productName);
				$("#edit-remark").val(item.productRemark);
			}
		});
	}
</script>