package com.ischoolbar.programmer.jxc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ischoolbar.programmer.jxc.entity.Brand;
import com.ischoolbar.programmer.jxc.service.IBrandService;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2020-04-02
 */
@RestController
@RequestMapping("/jxc/brand")
public class BrandController {
	@Autowired
	IBrandService brandService;
	
	
	
	private static final Integer ALL_BRAND_ID = 1000;

	/**
	 * 列表页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(ModelAndView model) {
		model.setViewName("/jxc/brand/list");
		return model;
	}

	/**
	 * 分页级联查询列表
	 * 
	 * @param current
	 * @param size
	 * @param brand
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> list(@RequestParam("page") int current, @RequestParam("rows") int size, Brand brand) {
		Map<String, Object> ret = new HashMap<>();
		Page<Brand> page = new Page<>();
		page.setCurrent(current);
		page.setSize(size);
		page = brandService.selectByPage(page, brand);
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
	public Map<String, Object> addOrUpdate(Brand brand,HttpServletRequest request) {
		return brandService.addOrUpdate(brand,request);

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
	public Map<String, Object> delete(Integer brandId,HttpServletRequest request) {
		return brandService.delete(brandId,request);
	}
	@RequestMapping("/getBrandDropList")
	@ResponseBody
	public List<Brand> getBrandDropList(){
		List<Brand> brandList = brandService.list();
		// 手动添加一个所有品牌选项
		Brand brand = new Brand();
		brand.setBrandId(ALL_BRAND_ID);
		brand.setBrandName("所有品牌");
		brandList.add(brand);
		return brandList;
	}

	
}
