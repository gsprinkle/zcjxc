package com.ischoolbar.programmer.jxc.service.impl;

import com.ischoolbar.programmer.jxc.entity.Employee;
import com.ischoolbar.programmer.jxc.mapper.EmployeeMapper;
import com.ischoolbar.programmer.jxc.service.IEmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-03-30
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {

	@Override
	public Page<Employee> selectByPage(Page<Employee> page, Employee emp) {
		QueryWrapper<Employee> queryWrapper = new QueryWrapper<>(emp);
		queryWrapper.orderByAsc("eorder");
		return baseMapper.selectPage(page, queryWrapper);
	}
}
