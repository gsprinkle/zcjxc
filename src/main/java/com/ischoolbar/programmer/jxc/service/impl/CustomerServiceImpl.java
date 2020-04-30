package com.ischoolbar.programmer.jxc.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ischoolbar.programmer.entity.admin.User;
import com.ischoolbar.programmer.jxc.entity.Customer;
import com.ischoolbar.programmer.jxc.entity.Sale;
import com.ischoolbar.programmer.jxc.mapper.CustomerMapper;
import com.ischoolbar.programmer.jxc.mapper.SaleMapper;
import com.ischoolbar.programmer.jxc.service.ICustomerService;
import com.ischoolbar.programmer.service.admin.LogService;

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

	@Autowired
	SaleMapper saleMapper;

	@Autowired
	LogService logService;

	@Override
	public Page<Customer> selectByPage(Page<Customer> page, Customer customer) {
		QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
		if (customer != null && StringUtils.isNotEmpty(customer.getCustName())) {
			queryWrapper.like("cust_name", customer.getCustName());
		}
		return baseMapper.selectPage(page, queryWrapper);
	}

	/**
	 * 删除客户
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public Map<String, Object> delete(Integer custId, HttpServletRequest request) {
		Map<String, Object> ret = new HashMap<>();
		// 根据custID查询销售列表
		List<Sale> salList = saleMapper.selectList(new QueryWrapper<Sale>().eq("cust_id", custId));
		// 如果该客户有销售记录，提示无法删除；
		if (salList != null && salList.size() > 0) {
			ret.put("type", "error");
			ret.put("msg", "该客户在销售列表中存在记录，无法删除！");
			return ret;
		}
		// 查询所要删除的客户，用于日志记录
		Customer cust = baseMapper.selectById(custId);
		// 如果没有，直接删除
		if (baseMapper.deleteById(custId) < 1) {
			ret.put("type", "error");
			ret.put("msg", "删除客户异常，请联系管理员！");
			return ret;
		}
		// 记录日志：先获取登录的用户
		User user = (User) request.getSession().getAttribute("user");
		// 写入日志
		logService.add("用户【" + user.getUsername() + "】删除了客户 {" + cust + "}");
		// 返回
		ret.put("type", "success");
		ret.put("msg", "删除成功！");
		return ret;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Map<String, Object> addOrUpdate(Customer cust, HttpServletRequest request) {
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
		// 没有ID表示新增，添加新增日期,记录日志
		// 获取登录信息
		User user = (User) request.getSession().getAttribute("user");
		if (!hasId) {
			// 设置创建时间
			cust.setCreateTime(new Date());
			// 设置创建者
			cust.setUsername(user.getUsername());
			if(baseMapper.insert(cust) > 0){
				// 记录日志
				logService.add("用户【" + user.getUsername() + "】添加了客户 {" + cust + "}");
			}else{
				ret.put("type", "error");
				ret.put("msg", "新增客户异常，请联系管理员！");
				return ret;
			}
		}else{// 有ID，表示修改,修改成功 ，记录日志
			// 检查权限，登录的用户是否是该进货的创建者
			Customer oldCust = baseMapper.selectById(cust.getCustId());
			if (!user.getUsername().equals(oldCust.getUsername())) {
				// 如果不是同一个操作者，提示无权限提示用户去列表中修改
				ret.put("type", "error");
				ret.put("msg", "对不起，您不是该数据的创建者，无权更改！");
				return ret;
			}
			if(baseMapper.updateById(cust) > 0){
				cust = baseMapper.selectById(cust.getCustId());
				logService.add("用户【" + user.getUsername() + "】修改客户 {" + oldCust + "}为：-->>" + cust);
			}else{
				ret.put("type", "error");
				ret.put("msg", "修改客户异常，请联系管理员！");
				return ret;
			}
		}
		// 返回结果
		ret.put("type", "success");
		ret.put("msg", hasId ? "客户修改成功！" : "客户添加成功！");
		return ret;
		
	}

	/**
	 * 判断该客户名称是否在数据库中已存在
	 * 
	 * @param cName
	 * @return
	 */
	private boolean isExist(String custName) {
		return baseMapper.selectOne(new QueryWrapper<Customer>().eq("cust_name", custName)) == null ? false : true;
	}

}
