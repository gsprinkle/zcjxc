package com.ischoolbar.programmer.jxc.service;

import com.ischoolbar.programmer.jxc.entity.Customer;

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
 * @since 2020-04-02
 */
public interface ICustomerService extends IService<Customer> {

	Page<Customer> selectByPage(Page<Customer> page, Customer customer);

	Map<String, Object> delete(Integer custId,HttpServletRequest request);

	Map<String, Object> addOrUpdate(Customer cust, HttpServletRequest request);

}
