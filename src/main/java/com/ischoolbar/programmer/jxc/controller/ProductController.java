package com.ischoolbar.programmer.jxc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ischoolbar.programmer.jxc.entity.Category;
import com.ischoolbar.programmer.jxc.entity.Inventory;
import com.ischoolbar.programmer.jxc.entity.Product;
import com.ischoolbar.programmer.jxc.pojo.ProductBrandCategoryVo;
import com.ischoolbar.programmer.jxc.service.ICategoryService;
import com.ischoolbar.programmer.jxc.service.IInventoryService;
import com.ischoolbar.programmer.jxc.service.IProductService;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2020-03-30
 */
@RestController
@RequestMapping("/jxc/product")
public class ProductController {
	@Autowired
	IProductService productService;
	@Autowired
	ICategoryService categoryService;

	@Autowired
	IInventoryService inventoryService;

	/**
	 * 列表页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(ModelAndView model) {
		model.setViewName("/jxc/product/list");
		return model;
	}

	/**
	 * 分页级联查询列表
	 * 
	 * @param current
	 * @param size
	 * @param product
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> list(@RequestParam("page") int current, @RequestParam("rows") int size, Product product) {
		Map<String, Object> ret = new HashMap<>();
		// 处理分类查询条件
		Integer cid = product.getCid();
		if (cid != null) {
			String cids = getCidsByCid(cid);
			// 去除最后一个逗号
			cids = cids.substring(0, cids.length() - 1);
			product.setCids(cids);
		}
		Page<ProductBrandCategoryVo> page = new Page<>();
		page.setCurrent(current);
		page.setSize(size);
		page = productService.selectPageVo(page, product);
		ret.put("total", page.getTotal());
		ret.put("rows", page.getRecords());
		return ret;
	}

	// 构造 分类ID以及子分类字符串
	String getCidsByCid(Integer cid) {
		String result = cid + ",";
		QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("pid", cid);
		List<Category> list = categoryService.list(queryWrapper);
		for (Category c : list) {
			result += getCidsByCid(c.getCid());
		}
		return result;
	}

	/**
	 * 新增或修改
	 * 
	 * @param category
	 * @return
	 */
	@RequestMapping(value = "/saveOrUpdate")
	@ResponseBody
	public Map<String, Object> addOrUpdate(Product product, HttpServletRequest request) {
		return productService.addOrUpdate(product,request);

	}

	/**
	 * 
	 * 
	 * 
	 * /** 删除
	 * 
	 * @param category
	 * @return
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public Map<String, Object> delete(Integer productId, HttpServletRequest request) {
		return productService.delete(productId,request);
	}
	
	/**
	 * 获取产品下拉列表
	 * @return
	 */
	@RequestMapping(value = "/getProductDropList")
	@ResponseBody
	public List<Product> getProductDropList(){
		return productService.list();
	}

	
	
	
}
