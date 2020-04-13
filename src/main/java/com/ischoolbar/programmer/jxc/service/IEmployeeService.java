package com.ischoolbar.programmer.jxc.service;

import com.ischoolbar.programmer.jxc.entity.Employee;
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

}
