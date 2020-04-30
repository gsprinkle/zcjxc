package com.ischoolbar.programmer.jxc.service.impl;

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
import com.ischoolbar.programmer.jxc.entity.Employee;
import com.ischoolbar.programmer.jxc.entity.Sale;
import com.ischoolbar.programmer.jxc.entity.Stock;
import com.ischoolbar.programmer.jxc.mapper.EmployeeMapper;
import com.ischoolbar.programmer.jxc.mapper.SaleMapper;
import com.ischoolbar.programmer.jxc.mapper.StockMapper;
import com.ischoolbar.programmer.jxc.service.IEmployeeService;
import com.ischoolbar.programmer.service.admin.LogService;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-03-30
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {
	@Autowired
	LogService logService;
	@Autowired
	StockMapper stockMapper;
	@Autowired
	SaleMapper saleMapper;
	
	

	@Override
	public Page<Employee> selectByPage(Page<Employee> page, Employee emp) {
		QueryWrapper<Employee> queryWrapper = new QueryWrapper<>(emp);
		queryWrapper.orderByAsc("eorder");
		return baseMapper.selectPage(page, queryWrapper);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Map<String, Object> addOrUpdate(Employee emp, HttpServletRequest request) {
		boolean hasId = emp.getEid() == null ? false : true;
		Map<String, Object> ret = new HashMap<>();
		if (StringUtils.isEmpty(emp.getEname())) {
			ret.put("type", "error");
			ret.put("msg", "员工名不能为空");
			return ret;
		}
		// 获取登录信息
		User user = (User) request.getSession().getAttribute("user");
		// 没有ID表示新增，添加新增日期,记录日志
		if (!hasId) {
			if (isExist(emp.getEname())) {
				ret.put("type", "error");
				ret.put("msg", "该员工已经存在，请重新输入！");
				return ret;
			}
			if (baseMapper.insert(emp) < 1) {
				ret.put("type", "error");
				ret.put("msg", "新增员工异常，请联系管理员！");
				return ret;
			} else {
				// 记录日志
				logService.add("用户【" + user.getUsername() + "】添加了员工 {" + emp + "}");
			}
		} else {// 如果有ID表示修改
				// 先记录旧数据
			Employee oldEmp = baseMapper.selectById(emp.getEid());
			if (baseMapper.updateById(emp) < 1) {
				ret.put("type", "error");
				ret.put("msg", "修改员工异常，请联系管理员！");
				return ret;
			} else {
				// 记录日志
				emp = baseMapper.selectById(emp.getEid());
				logService.add("用户【" + user.getUsername() + "】修改员工 {" + oldEmp + "}为：-->>" + emp);
			}
		}
		ret.put("type", "success");
		ret.put("msg", hasId ? "员工修改成功！" : "员工添加成功！");
		return ret;
	}

	/**
	 * 判断该员工名称是否在数据库中已存在
	 * 
	 * @param cName
	 * @return
	 */
	private boolean isExist(String ename) {
		return baseMapper.selectOne(new QueryWrapper<Employee>().eq("ename", ename)) == null ? false : true;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Map<String, Object> delete(Integer eid, HttpServletRequest request) {
		Map<String, Object> ret = new HashMap<>();
		// 首先检查，是否存在与该员工相关的进货和销售记录
		List<Sale> selectList = saleMapper.selectList(new QueryWrapper<Sale>().eq("eid", eid));
		List<Stock> stockList = stockMapper.selectList(new QueryWrapper<Stock>().eq("eid", eid));
		if((selectList != null && selectList.size() > 0) || (stockList != null && stockList.size() > 0)){
			ret.put("type", "error");
			ret.put("msg", "删除员工出错，该员工有进货或销售记录，无法删除！");
			return ret;
		}
		
		// 查询要删除的对象，方便日志记录
		Employee emp = baseMapper.selectById(eid);
		if (baseMapper.deleteById(eid) < 1) {
			ret.put("type", "error");
			ret.put("msg", "删除员工异常，请联系管理员！");
			return ret;
		}
		// 记录日志：先获取登录的用户
		User user = (User) request.getSession().getAttribute("user");
		// 写入日志
		logService.add("用户【" + user.getUsername() + "】删除了员工 {" + emp + "}");
		ret.put("type", "success");
		ret.put("msg", "删除成功！");
		return ret;
	}
}
