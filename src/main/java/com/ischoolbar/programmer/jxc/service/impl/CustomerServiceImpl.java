package com.ischoolbar.programmer.jxc.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ischoolbar.programmer.jxc.entity.Customer;
import com.ischoolbar.programmer.jxc.mapper.CustomerMapper;
import com.ischoolbar.programmer.jxc.service.ICustomerService;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-04-02
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements ICustomerService {

	@Override
	public Page<Customer> selectByPage(Page<Customer> page, Customer customer) {
		QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
		if (customer != null && StringUtils.isNotEmpty(customer.getCustName())) {
			queryWrapper.like("cust_name", customer.getCustName());
		}
		return baseMapper.selectPage(page, queryWrapper);
	}

}
