package com.ischoolbar.programmer.jxc.pojo;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/** 

* @author 作者 郭小雨

* @version 创建时间：2020年3月31日 下午4:00:14 

* 类说明 

*/
@Data
public class TreeNode {
	/**
	 * 显示节点的id
	 */
	private Integer id;
	/**
	 * 显示节点的名称
	 */
	private String text;
	/**
	 * 显示节点的图标
	 */
	private String icon;
	/**
	 * 显示节点的父节点
	 */
	private Integer pid;
	/**
	 * 显示节点的子节点集合
	 */
	private List<TreeNode> children;
	
	/**
	 * 空的构造函数
	 */
	public TreeNode(){
		
	}
	
	/**
	 * 有参数的构造参数
	 * @param id 显示的节点ID
	 * @param text 显示的节点名称
	 * @param icon 显示的节点图标
	 * @param parentId 显示的节点的父节点
	 * @param children 显示节点的子节点
	 */
	public TreeNode(Integer id, String text,String icon,Integer parentId,List<TreeNode> children){
		super();
		this.id=id;
		this.text=text;
		this.icon=icon;
		this.pid=parentId;
		this.children=children;
	}
 
	/**
	 * 添加子节点的方法
	 * @param node 树节点实体
	 */
	public void addChild(TreeNode node){
		if(this.children==null){
			children=new ArrayList<TreeNode>();
			children.add(node);
		}else{
			children.add(node);
		}
	}
}
