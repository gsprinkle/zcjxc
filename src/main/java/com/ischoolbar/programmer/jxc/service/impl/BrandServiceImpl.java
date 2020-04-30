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
import com.ischoolbar.programmer.jxc.entity.Brand;
import com.ischoolbar.programmer.jxc.entity.Customer;
import com.ischoolbar.programmer.jxc.entity.Product;
import com.ischoolbar.programmer.jxc.mapper.BrandMapper;
import com.ischoolbar.programmer.jxc.mapper.ProductMapper;
import com.ischoolbar.programmer.jxc.service.IBrandService;
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
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements IBrandService {
	@Autowired
	LogService logService;

	@Autowired
	ProductMapper productMapper;

	@Override
	public Page<Brand> selectByPage(Page<Brand> page, Brand brand) {
		QueryWrapper<Brand> queryWrapper = new QueryWrapper<>(brand);
		return baseMapper.selectPage(page, queryWrapper);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Map<String, Object> addOrUpdate(Brand brand, HttpServletRequest request) {
		boolean hasId = brand.getBrandId() == null ? false : true;
		Map<String, Object> ret = new HashMap<>();
		// 数据校验
		if (StringUtils.isEmpty(brand.getBrandName())) {
			ret.put("type", "error");
			ret.put("msg", "品牌名不能为空");
			return ret;
		}
		if (!hasId && isExist(brand.getBrandName())) {
			ret.put("type", "error");
			ret.put("msg", "该品牌已经存在，请重新输入！");
			return ret;
		}
		// 没有ID表示新增，添加新增日期,记录日志
		// 获取登录信息
		User user = (User) request.getSession().getAttribute("user");
		if (!hasId) {
			if (baseMapper.insert(brand) > 0) {
				// 记录日志
				logService.add("用户【" + user.getUsername() + "】添加了品牌 {" + brand + "}");
			} else {
				ret.put("type", "error");
				ret.put("msg", "新增品牌异常，请联系管理员！");
				return ret;
			}
		} else { // 有ID表示修改，先获取旧品牌，以记录日志
			Brand oldBrand = baseMapper.selectById(brand.getBrandId());
			if (baseMapper.updateById(brand) > 0) {
				brand = baseMapper.selectById(brand.getBrandId());
				logService.add("用户【" + user.getUsername() + "】修改品牌{" + oldBrand + "}为：-->>" + brand);
			} else {
				ret.put("type", "error");
				ret.put("msg", "修改品牌异常，请联系管理员！");
				return ret;
			}
		}
		ret.put("type", "success");
		ret.put("msg", hasId ? "品牌修改成功！" : "品牌添加成功！");
		return ret;
	}

	/**
	 * 判断该品牌名称是否在数据库中已存在
	 * 
	 * @param cName
	 * @return
	 */
	private boolean isExist(String brandName) {
		return baseMapper.selectOne(new QueryWrapper<Brand>().eq("brand_name", brandName)) == null ? false : true;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Map<String, Object> delete(Integer brandId, HttpServletRequest request) {
		Map<String, Object> ret = new HashMap<>();
		// 品牌删除，先检查是否有产品使用了品牌,如有，则无法删除
		List<Product> prodList = productMapper.selectList(new QueryWrapper<Product>().eq("brand_id", brandId));
		if (prodList != null && prodList.size() > 0) {
			ret.put("type", "error");
			ret.put("msg", "删除品牌错误，该品牌下有产品，无法删除！");
			return ret;
		}
		// 如果没有产品，则可以删除
		// 查询所要删除的品牌，用于日志记录
		Brand brand = baseMapper.selectById(brandId);
		// 如果没有，直接删除
		if (baseMapper.deleteById(brandId) < 1) {
			ret.put("type", "error");
			ret.put("msg", "删除品牌异常，请联系管理员！");
			return ret;
		}
		// 记录日志：先获取登录的用户
		User user = (User) request.getSession().getAttribute("user");
		// 写入日志
		logService.add("用户【" + user.getUsername() + "】删除了品牌 {" + brand + "}");
		// 返回
		ret.put("type", "success");
		ret.put("msg", "删除成功！");
		return ret;
	}
}
