package com.ischoolbar.programmer.jxc.service;

import com.ischoolbar.programmer.jxc.entity.Sale;
import com.ischoolbar.programmer.jxc.pojo.SaleVo;

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
public interface ISaleService extends IService<Sale> {

	Map<String, Object> addOrUpdate(Sale sale,HttpServletRequest request);

	Map<String, Object> delete(Integer saleId,HttpServletRequest request);

	Page<SaleVo> selectByPage(Page<SaleVo> page, Sale sale);

}
