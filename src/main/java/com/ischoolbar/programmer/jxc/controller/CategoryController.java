package com.ischoolbar.programmer.jxc.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ischoolbar.programmer.jxc.entity.Category;
import com.ischoolbar.programmer.jxc.pojo.TreeNode;
import com.ischoolbar.programmer.jxc.service.ICategoryService;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2020-03-30
 */
@RestController
@RequestMapping("/jxc/category")
public class CategoryController {
	@Autowired
	ICategoryService categoryService;

	/**
	 * 列表页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView listView(ModelAndView model) {
		model.setViewName("/jxc/category/list");
		return model;
	}

	/**
	 * 列表，Tree json 数据
	 * 
	 * @param current
	 * @param size
	 * @param category
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public List<TreeNode> list( ) {
		List<Category> catList = categoryService.list();
		return toTreeNode(catList);
	}
	
	// 把分类列表转换成EasyUI所需要的 Tree Json
	public List<TreeNode> toTreeNode(List<Category> catList){
		// 先把所有的Category 转为TreeNode
		List<TreeNode> treeNodeList = new ArrayList<>();
		for(Category category : catList){
			TreeNode treeNode = new TreeNode();
			treeNode.setId(category.getCid());
			treeNode.setText(category.getCname());
			treeNode.setPid(category.getPid());
			treeNodeList.add(treeNode);
		}
		// 创建一个Map，key 为cid，value 为对应的TreeNode
		Map<Integer,TreeNode> map = new HashMap<>();
		for(TreeNode treeNode : treeNodeList){
			map.put(treeNode.getId(), treeNode);
		}
		// 遍历TreeNodeList，如果该TreeNode有父节点，则把该节点添加到它的父节点的child中
		for(TreeNode treeNode : treeNodeList){
			Integer pid = treeNode.getPid();
			if(pid != 0){
				map.get(pid).addChild(treeNode);
			}
		}
		treeNodeList.clear();
		treeNodeList.add(map.get(1));
		return treeNodeList;
	}

	/**
	 * 新增或修改
	 * 
	 * @param category
	 * @return
	 */
	@RequestMapping(value = "/saveOrUpdate")
	@ResponseBody
	public Map<String, Object> addOrUpdate(Category category,HttpServletRequest request) {
		return categoryService.addOrUpdate(category,request);
	}

	/**
	 * 删除
	 * 
	 * @param category
	 * @return
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public Map<String, Object> delete(Integer cid, HttpServletRequest request) {
		return categoryService.delete(cid, request);

	}
	

	/**
	 * 获取分类下拉列表数据
	 * 
	 * @return
	 */
	@RequestMapping("/getCategoryDropList")
	@ResponseBody
	public List<Category> getCategoryDropList() {
		return categoryService.list();
	}

	/**
	 * 判断该分类名称是否在数据库中已存在
	 * 
	 * @param cName
	 * @return
	 */
	private boolean isExist(String cname) {
		return categoryService.getOne(new QueryWrapper<Category>().eq("cname", cname)) == null ? false : true;
	}
}
