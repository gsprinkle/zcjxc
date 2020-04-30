<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@include file="../../common/header.jsp"%>
<div class="easyui-layout" data-options="fit:true">
	<!-- Begin of toolbar -->
	<%-- <div id="wu-toolbar">
		<div class="wu-toolbar-button">
			<%@include file="../../common/menus.jsp"%>
		</div>
		<div class="wu-toolbar-search">
			<label>分类名称:</label><input id="search-name" class="wu-text" style="width: 100px"> <a href="#" id="search-btn"
				class="easyui-linkbutton" iconCls="icon-search">搜索</a>
		</div>
	</div> --%>
	<!-- End of toolbar -->
	<!-- <table id="data-datagrid" class="easyui-datagrid" toolbar="#wu-toolbar"></table> -->
	<div class="easyui-panel" style="padding:5px;" data-options="title:'产品分类'">
		<ul id="tt"></ul>
	</div>
</div>
<div id="mm" class="easyui-menu" style="width:120px;">
		<div onclick="openAdd()" data-options="iconCls:'icon-add'">添加</div>
		<div onclick="openEdit()" data-options="iconCls:'icon-edit'">修改</div>
		<div onclick="remove()" data-options="iconCls:'icon-remove'">删除</div>
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
				<td width="60" align="right">分类名称:</td>
				<td><input type="text" name="cname" class="wu-text easyui-validatebox"
					data-options="required:true, missingMessage:'请填写分类名称'" /></td>
			</tr>
			<input type="hidden" name="pid" id="add-pid" />
		</table>
	</form>
</div>
<!-- 修改窗口 -->
<div id="edit-dialog" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'"
	style="width: 450px; padding: 10px;">
	<form id="edit-form" method="post">
		<input type="hidden" id="edit-id" name="cid" />
		<table>
			<tr>
				<td width="60" align="right">分类名称:</td>
				<td><input type="text" id="edit-name" name="cname" class="wu-text easyui-validatebox"
					data-options="required:true, missingMessage:'请填写分类名称'" /></td>
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
			url : '../category/saveOrUpdate',
			dataType : 'json',
			type : 'post',
			data : data,
			success : function(data) {
				if (data.type == 'success') {
					$.messager.alert('信息提示', data.msg, 'info');
					$('#add-dialog').dialog('close');
					$('#tt').tree('reload');
					closeCategoryTab();
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
			url : '../category/saveOrUpdate',
			dataType : 'json',
			type : 'post',
			data : data,
			success : function(data) {
				if (data.type == 'success') {
					$.messager.alert('信息提示', data.msg, 'info');
					$('#edit-dialog').dialog('close');
					$('#tt').tree('reload');
					closeCategoryTab();
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
		$.messager.confirm('信息提示', '确定要删除？该分类下面的所有都将被删除', function(result) {
			if (result) {
				var item = $('#tt').tree('getSelected');
				$.ajax({
					url : '../category/delete',
					dataType : 'json',
					type : 'post',
					data : {
						cid : item.id
					},
					success : function(data) {
						if (data.type == 'success') {
							$.messager.alert('信息提示', data.msg, 'info');
							$('#tt').tree('reload');
							closeCategoryTab();
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
		var pnode = $('#tt').tree('getSelected');
		$('#add-pid').val(pnode.id);
		$('#add-dialog').dialog({
			closed : false,
			modal : true,
			title : "添加",
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
		var pnode = $('#tt').tree('getSelected');
		console.log(pnode);
		if (pnode == null || pnode.length == 0) {
			$.messager.alert('信息提示', '请选择要修改的数据！', 'info');
			return;
		}
		$('#edit-dialog').dialog(
				{
					closed : false,
					modal : true,
					title : "修改",
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
						$("#edit-id").val(pnode.id);
						$("#edit-name").val(pnode.text);
					}
				});
	}

	/** 
	 * 载入数据
	 */
	 $('#tt').tree({
		    url:'list',
		    method: 'post',
			animate: true,
			onContextMenu: function(e,node){
				e.preventDefault();
				$(this).tree('select',node.target);
				$('#mm').menu('show',{
					left: e.pageX,
					top: e.pageY
				});
			}
		});
	
	 function closeCategoryTab(){
			closeTab('产品管理');
		}
</script>