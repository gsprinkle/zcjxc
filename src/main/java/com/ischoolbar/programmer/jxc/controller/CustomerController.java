package com.ischoolbar.programmer.jxc.controller;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
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
import com.ischoolbar.programmer.jxc.entity.Customer;
import com.ischoolbar.programmer.jxc.service.ICustomerService;

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
	public Map<String, Object> addOrUpdate(Customer cust) {
		boolean hasId = cust.getCustId() == null ? false : true;
		Map<String, Object> ret = new HashMap<>();
		if (StringUtils.isEmpty(cust.getCustName())) {
			ret.put("type", "error");
			ret.put("msg", "客户名不能为空");
			return ret;
		}
		if (!hasId && isExist(cust.getCustName())) {
			ret.put("type", "error");
			ret.put("msg", "该客户已经存在，请重新输入！");
			return ret;
		}
		if (!hasId) {// 新增时，添加新增日期
			cust.setCreateTime(new Date());
		}
		if (!custService.saveOrUpdate(cust)) {
			ret.put("type", "error");
			ret.put("msg", "新增客户异常，请联系管理员！");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", hasId ? "客户修改成功！" : "客户添加成功！");
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
	public Map<String, Object> delete(Integer custId) {
		Map<String, Object> ret = new HashMap<>();

		if (!custService.removeById(custId)) {
			ret.put("type", "error");
			ret.put("msg", "删除客户异常，请联系管理员！");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "删除成功！");
		return ret;

	}

	/**
	 * 判断该客户名称是否在数据库中已存在
	 * 
	 * @param cName
	 * @return
	 */
	private boolean isExist(String custName) {
		return custService.getOne(new QueryWrapper<Customer>().eq("cust_name", custName)) == null ? false : true;
	}
}
