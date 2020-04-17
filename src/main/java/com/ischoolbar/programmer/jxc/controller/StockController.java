package com.ischoolbar.programmer.jxc.controller;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ischoolbar.programmer.jxc.entity.Stock;
import com.ischoolbar.programmer.jxc.pojo.InventoryVo;
import com.ischoolbar.programmer.jxc.pojo.StockVo;
import com.ischoolbar.programmer.jxc.service.IStockService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jobob
 * @since 2020-03-30
 */
@RestController
@RequestMapping("/jxc/stock")
public class StockController {

	@Autowired
	IStockService stockService;


	/**
	 * 列表页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(ModelAndView model) {
		model.setViewName("/jxc/stock/list");
		return model;
	}

	/**
	 * 分页级联查询列表
	 * 
	 * @param current
	 * @param size
	 * @param stock
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> list(@RequestParam("page") int current, @RequestParam("rows") int size, Stock stock) {
		Map<String, Object> ret = new HashMap<>();
		Page<StockVo> page = new Page<>();
		page.setCurrent(current);
		page.setSize(size);
		page = stockService.selectByPage(page, stock);
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
	public Map<String, Object> addOrUpdate(Stock stock) {
		return stockService.addOrUpdate(stock);
	}
	/**
	 * 删除
	 * @param stockId
	 * @return
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public Map<String, Object> delete(Integer stockId) {
		
		return stockService.delete(stockId);
	}
}
