package com.ischoolbar.programmer.jxc.mapper;

import com.ischoolbar.programmer.jxc.entity.Product;
import com.ischoolbar.programmer.jxc.pojo.ProductBrandCategoryVo;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2020-03-30
 */
public interface ProductMapper extends BaseMapper<Product> {

	Page<ProductBrandCategoryVo> selectPageVo(Page<ProductBrandCategoryVo> page, @Param("product")Product product);

}
