package com.ischoolbar.programmer.jxc.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ischoolbar.programmer.jxc.entity.Product;
import com.ischoolbar.programmer.jxc.mapper.ProductMapper;
import com.ischoolbar.programmer.jxc.pojo.ProductBrandCategoryVo;
import com.ischoolbar.programmer.jxc.service.ICategoryService;
import com.ischoolbar.programmer.jxc.service.IProductService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-03-30
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {

	@Override
	public Page<ProductBrandCategoryVo> selectPageVo(Page<ProductBrandCategoryVo> page, Product product) {
		return baseMapper.selectPageVo(page,product);
	}
	

}
