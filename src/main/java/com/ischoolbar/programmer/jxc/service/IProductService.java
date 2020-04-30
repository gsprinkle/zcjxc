package com.ischoolbar.programmer.jxc.service;

import com.ischoolbar.programmer.jxc.entity.Product;
import com.ischoolbar.programmer.jxc.pojo.ProductBrandCategoryVo;

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
 * @since 2020-03-30
 */
public interface IProductService extends IService<Product> {

	Page<ProductBrandCategoryVo> selectPageVo(Page<ProductBrandCategoryVo> page, Product product);

	Map<String, Object> addOrUpdate(Product product, HttpServletRequest request);

	Map<String, Object> delete(Integer productId, HttpServletRequest request);


}
