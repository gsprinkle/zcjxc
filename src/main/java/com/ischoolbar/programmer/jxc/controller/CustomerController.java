package com.ischoolbar.programmer.jxc.controller;

import java.util.Date;
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
import com.ischoolbar.programmer.jxc.entity.Customer;
import com.ischoolbar.programmer.jxc.service.ICustomerService;
import com.ischoolbar.programmer.service.admin.LogService;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2020-04-02
 */
@RestController
@RequestMapping("/jxc/customer")
public class CustomerController {
	@Autowired
	ICustomerService custService;
	@Autowired
	LogService logService;

	/**
	 * 列表页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(ModelAndView model) {
		model.setViewName("/jxc/customer/list");
		return model;
	}

	/**
	 * 分页级联查询列表
	 * 
	 * @param current
	 * @param size
	 * @param customer
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> list(@RequestParam("page") int current, @RequestParam("rows") int size, Customer customer) {
		Map<String, Object> ret = new HashMap<>();
		Page<Customer> page = new Page<>();
		page.setCurrent(current);
		page.setSize(size);
		page = custService.selectByPage(page, customer);
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
	public Map<String, Object> addOrUpdate(Customer cust,HttpServletRequest request) {
		return custService.addOrUpdate(cust,request);

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
	public Map<String, Object> delete(Integer custId,HttpServletRequest request) {
		return custService.delete(custId,request);

	}
	
	@RequestMapping(value="/getCustomerDropList")
	@ResponseBody
	public List<Customer> getCustomerDropList(){
		return custService.list();
	}

	
}
