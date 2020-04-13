package com.ischoolbar.programmer.jxc.service.impl;

import com.ischoolbar.programmer.jxc.entity.Brand;
import com.ischoolbar.programmer.jxc.entity.Employee;
import com.ischoolbar.programmer.jxc.mapper.BrandMapper;
import com.ischoolbar.programmer.jxc.service.IBrandService;
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
 * @since 2020-04-02
 */
@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements IBrandService {

	@Override
	public Page<Brand> selectByPage(Page<Brand> page, Brand brand) {
		QueryWrapper<Brand> queryWrapper = new QueryWrapper<>(brand);
		return baseMapper.selectPage(page, queryWrapper);
	}

}
