package com.ischoolbar.programmer.jxc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public Map<String, Object> addOrUpdate(Product product) {
		boolean hasId = product.getProductId() == null ? false : true;
		Map<String, Object> ret = new HashMap<>();
		if (StringUtils.isEmpty(product.getProductName())) {
			ret.put("type", "error");
			ret.put("msg", "品牌名不能为空");
			return ret;
		}
		if (!hasId && isExist(product.getProductName())) {
			ret.put("type", "error");
			ret.put("msg", "该品牌已经存在，请重新输入！");
			return ret;
		}
		if (!productService.saveOrUpdate(product)) {
			ret.put("type", "error");
			ret.put("msg", "新增品牌异常，请联系管理员！");
			return ret;
		}
		// 添加商品同时，检查库存当中有没有该商品，如果没有，则添加
		if (!hasId) {
			List<Inventory> list = inventoryService.list(new QueryWrapper<Inventory>().eq("product_id", product.getProductId()));
			if (list == null || list.size() < 1) {
				Inventory inv = new Inventory();
				inv.setInventoryNum(0);
				inv.setProductId(product.getProductId());
				inv.setStoreId(1);
				inventoryService.saveOrUpdate(inv);
			}
		}

		ret.put("type", "success");
		ret.put("msg", hasId ? "品牌修改成功！" : "品牌添加成功！");
		return ret;

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
	public Map<String, Object> delete(Integer productId) {
		Map<String, Object> ret = new HashMap<>();

		if (!productService.removeById(productId)) {
			ret.put("type", "error");
			ret.put("msg", "删除品牌异常，请联系管理员！");
			return ret;
		}
		// 删除商品的同时，删除库存列表中对应的商品
		inventoryService.remove(new QueryWrapper<Inventory>().eq("product_id", productId));
		ret.put("type", "success");
		ret.put("msg", "删除成功！");
		return ret;
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

	/**
	 * 判断该品牌名称是否在数据库中已存在
	 * 
	 * @param cName
	 * @return
	 */
	private boolean isExist(String productName) {
		return productService.getOne(new QueryWrapper<Product>().eq("product_name", productName)) == null ? false : true;
	}
	
	
}
