package com.ischoolbar.programmer.jxc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ischoolbar.programmer.jxc.entity.Inventory;
import com.ischoolbar.programmer.jxc.entity.Product;
import com.ischoolbar.programmer.jxc.pojo.InventoryVo;
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
@RequestMapping("/jxc/inventory")
public class InventoryController {
	@Autowired
	IInventoryService inventoryService;

	@Autowired
	IProductService productService;

	@RequestMapping("/initialize")
	@ResponseBody
	public Map<String, Object> initialize() {
		Map<String, Object> ret = new HashMap<>();
		Integer num = 0;
		// 查询所有商品
		List<Product> pList = productService.list();
		// 商品库存默认为0
		for (Product p : pList) {
			// 该产品是否已存在
			boolean exist = isExist(p.getProductId());
			if(exist){
				continue;
			}
			// 仓库默认为1号库,公司负一层仓库
			Inventory inv = new Inventory();
			inv.setProductId(p.getProductId());
			inv.setInventoryNum(0);
			inv.setStoreId(1);
			inventoryService.saveOrUpdate(inv);
		}
		ret.put("type", "success");
		ret.put("info", "初始化成功");
		return ret;
	}

	/**
	 * 列表页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(ModelAndView model) {
		model.setViewName("/jxc/inventory/list");
		return model;
	}

	/**
	 * 分页级联查询列表
	 * 
	 * @param current
	 * @param size
	 * @param inventory
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> list(@RequestParam("page") int current, @RequestParam("rows") int size, InventoryVo inventory) {
		Map<String, Object> ret = new HashMap<>();
		Page<InventoryVo> page = new Page<>();
		page.setCurrent(current);
		page.setSize(size);
		page = inventoryService.selectByPage(page, inventory);
		ret.put("total", page.getTotal());
		ret.put("rows", page.getRecords());
		return ret;
	}

	/**
	 * 新增或修改
	 * 
	 * @param category
	 * @return
	 */
	@RequestMapping(value = "/saveOrUpdate")
	@ResponseBody
	public Map<String, Object> addOrUpdate(Inventory inventory) {
		boolean hasId = inventory.getInventoryId() == null ? false : true;
		Map<String, Object> ret = new HashMap<>();
		if (!inventoryService.saveOrUpdate(inventory)) {
			ret.put("type", "error");
			ret.put("msg", "新增品牌异常，请联系管理员！");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", hasId ? "品牌修改成功！" : "品牌添加成功！");
		return ret;

	}
	/**
	 * 删除
	 * @param inventoryId
	 * @return
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public Map<String, Object> delete(Integer inventoryId) {
		Map<String, Object> ret = new HashMap<>();

		if (!inventoryService.removeById(inventoryId)) {
			ret.put("type", "error");
			ret.put("msg", "删除库存异常，请联系管理员！");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "删除成功！");
		return ret;
	}

	/**
	 * 判断该产品名称是否在库存表中存在
	 * 
	 * @param cName
	 * @return
	 */
	private boolean isExist(Integer productId) {
		List<Inventory> list = inventoryService.list(new QueryWrapper<Inventory>().eq("product_id", productId));
		if(list == null || list.size()<1){
			return false;
		}
		return true;
	}
}
