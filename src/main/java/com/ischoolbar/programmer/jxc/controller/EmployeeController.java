package com.ischoolbar.programmer.jxc.controller;

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
import com.ischoolbar.programmer.jxc.entity.Employee;
import com.ischoolbar.programmer.jxc.service.IEmployeeService;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2020-03-30
 */
@RestController
@RequestMapping("/jxc/employee")
public class EmployeeController {
	@Autowired
	IEmployeeService empService;

	/**
	 * 列表页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(ModelAndView model) {
		model.setViewName("/jxc/employee/list");
		return model;
	}

	/**
	 * 分页级联查询列表
	 * 
	 * @param current
	 * @param size
	 * @param emp
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> list(@RequestParam("page") int current, @RequestParam("rows") int size, Employee emp) {
		Map<String, Object> ret = new HashMap<>();
		Page<Employee> page = new Page<>();
		page.setCurrent(current);
		page.setSize(size);
		page = empService.selectByPage(page, emp);
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
	public Map<String, Object> addOrUpdate(Employee emp) {
		boolean hasId = emp.getEid() == null ? false : true;
		Map<String, Object> ret = new HashMap<>();
		if (StringUtils.isEmpty(emp.getEname())) {
			ret.put("type", "error");
			ret.put("msg", "员工名不能为空");
			return ret;
		}
		if (!hasId && isExist(emp.getEname())) {
			ret.put("type", "error");
			ret.put("msg", "该员工已经存在，请重新输入！");
			return ret;
		}
		if (!empService.saveOrUpdate(emp)) {
			ret.put("type", "error");
			ret.put("msg", "新增员工异常，请联系管理员！");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", hasId ? "员工修改成功！" : "员工添加成功！");
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
	public Map<String, Object> delete(Integer eid) {
		Map<String, Object> ret = new HashMap<>();

		if (!empService.removeById(eid)) {
			ret.put("type", "error");
			ret.put("msg", "删除员工异常，请联系管理员！");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "删除成功！");
		return ret;

	}

	/**
	 * 判断该员工名称是否在数据库中已存在
	 * 
	 * @param cName
	 * @return
	 */
	private boolean isExist(String ename) {
		return empService.getOne(new QueryWrapper<Employee>().eq("ename", ename)) == null ? false : true;
	}
}
