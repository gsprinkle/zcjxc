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
import com.ischoolbar.programmer.jxc.entity.Store;
import com.ischoolbar.programmer.jxc.service.IStoreService;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2020-03-30
 */
@RestController
@RequestMapping("/jxc/store")
public class StoreController {
	@Autowired
	IStoreService storeService;

	/**
	 * 列表页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(ModelAndView model) {
		model.setViewName("/jxc/store/list");
		return model;
	}

	/**
	 * 分页级联查询列表
	 * 
	 * @param current
	 * @param size
	 * @param store
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> list(@RequestParam("page") int current, @RequestParam("rows") int size, Store store) {
		Map<String, Object> ret = new HashMap<>();
		Page<Store> page = new Page<>();
		page.setCurrent(current);
		page.setSize(size);
		page = storeService.selectByPage(page, store);
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
	public Map<String, Object> addOrUpdate(Store store) {
		boolean hasId = store.getStoreId() == null ? false : true;
		Map<String, Object> ret = new HashMap<>();
		if (StringUtils.isEmpty(store.getStoreName())) {
			ret.put("type", "error");
			ret.put("msg", "仓库名不能为空");
			return ret;
		}
		if (!hasId && isExist(store.getStoreName())) {
			ret.put("type", "error");
			ret.put("msg", "该仓库已经存在，请重新输入！");
			return ret;
		}
		if (!storeService.saveOrUpdate(store)) {
			ret.put("type", "error");
			ret.put("msg", "新增仓库异常，请联系管理员！");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", hasId ? "仓库修改成功！" : "仓库添加成功！");
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
	public Map<String, Object> delete(Integer storeId) {
		Map<String, Object> ret = new HashMap<>();

		if (!storeService.removeById(storeId)) {
			ret.put("type", "error");
			ret.put("msg", "删除仓库异常，请联系管理员！");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "删除成功！");
		return ret;
	}
	@RequestMapping(value = "/getStoreDropList")
	@ResponseBody
	public List<Store> getStoreDropList(){
		return storeService.list();
	}

	/**
	 * 判断该仓库名称是否在数据库中已存在
	 * 
	 * @param cName
	 * @return
	 */
	private boolean isExist(String storeName) {
		return storeService.getOne(new QueryWrapper<Store>().eq("store_name", storeName)) == null ? false : true;
	}
}
