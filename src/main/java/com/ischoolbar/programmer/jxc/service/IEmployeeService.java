package com.ischoolbar.programmer.jxc.service;

import com.ischoolbar.programmer.jxc.entity.Employee;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jobob
 * @since 2020-03-30
 */
public interface IEmployeeService extends IService<Employee> {

	Page<Employee> selectByPage(Page<Employee> page, Employee emp);

	Map<String, Object> addOrUpdate(Employee emp, HttpServletRequest request);

	Map<String, Object> delete(Integer eid, HttpServletRequest request);

}
