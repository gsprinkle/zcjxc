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
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ischoolbar.programmer.entity.admin.User;
import com.ischoolbar.programmer.jxc.entity.Category;
import com.ischoolbar.programmer.jxc.entity.Product;
import com.ischoolbar.programmer.jxc.mapper.CategoryMapper;
import com.ischoolbar.programmer.jxc.mapper.ProductMapper;
import com.ischoolbar.programmer.jxc.service.ICategoryService;
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
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

	@Autowired
	LogService logService;
	@Autowired
	ProductMapper productMapper;

	/**
	 * 添加或修改分类
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public Map<String, Object> addOrUpdate(Category category, HttpServletRequest request) {
		boolean hasId = category.getCid() == null ? false : true;
		Map<String, Object> ret = new HashMap<>();
		// 数据校验
		if (StringUtils.isEmpty(category.getCname())) {
			ret.put("type", "error");
			ret.put("msg", "分类名不能为空");
			return ret;
		}
		if (!hasId && isExist(category.getCname())) {
			ret.put("type", "error");
			ret.put("msg", "该分类已经存在，请重新输入！");
			return ret;
		}

		// 没有ID表示新增，添加新增日期,记录日志
		// 获取登录信息
		User user = (User) request.getSession().getAttribute("user");
		if (!hasId) {
			if (baseMapper.insert(category) > 0) {
				// 记录日志
				logService.add("用户【" + user.getUsername() + "】添加了分类 {" + category + "}");
			} else {
				ret.put("type", "error");
				ret.put("msg", "新增分类异常，请联系管理员！");
				return ret;
			}
		} else { // 有ID 表示修改
			// 获取旧的分类信息
			Category oldCat = baseMapper.selectById(category.getCid());
			if (baseMapper.updateById(category) > 0) {
				category = baseMapper.selectById(category.getCid());
				logService.add("用户【" + user.getUsername() + "】修改分类 {" + oldCat + "}为：-->>" + category);
			} else {
				ret.put("type", "error");
				ret.put("msg", "修改分类异常，请联系管理员！");
				return ret;
			}
		}
		ret.put("type", "success");
		ret.put("msg", hasId ? "分类修改成功！" : "分类添加成功！");
		return ret;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Map<String, Object> delete(Integer cid, HttpServletRequest request) {
		Map<String, Object> ret = new HashMap<>();
		// 要删除分类，首先要检查该分类下是否包含其它分类或产品
		// 检查是否包含子节点
		List<Category> childList = baseMapper.selectList(new QueryWrapper<Category>().eq("pid", cid));
		// 检查是否包含产品
		List<Product> prodList = productMapper.selectList(new QueryWrapper<Product>().eq("cid", cid));

		if ((childList != null && childList.size() > 0) || (prodList != null && prodList.size() > 0)) {
			ret.put("type", "error");
			ret.put("msg", "删除异常,该分类包含有子分类或产品，无法删除！");
			return ret;
		}
		// 如果没有子类且没有产品，可以删除，记录日志
		// 获取登录信息
		User user = (User) request.getSession().getAttribute("user");
		Category category = baseMapper.selectById(cid);
		if (baseMapper.deleteById(cid) > 0) {
			// 写入日志
			logService.add("用户【" + user.getUsername() + "】删除了分类 {" + category + "}");
			ret.put("type", "success");
			ret.put("msg", "删除成功！");
			return ret;
		} else {
			ret.put("type", "error");
			ret.put("msg", "删除异常，请联系管理员！");
			return ret;
		}
	}

	/**
	 * 判断该分类名称是否在数据库中已存在
	 * 
	 * @param cName
	 * @return
	 */
	private boolean isExist(String cname) {
		return baseMapper.selectOne(new QueryWrapper<Category>().eq("cname", cname)) == null ? false : true;
	}
}
