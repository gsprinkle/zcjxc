package com.ischoolbar.programmer.jxc.service;

import com.ischoolbar.programmer.jxc.entity.Stock;
import com.ischoolbar.programmer.jxc.pojo.InventoryVo;
import com.ischoolbar.programmer.jxc.pojo.StockVo;

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
public interface IStockService extends IService<Stock> {

	Page<StockVo> selectByPage(Page<StockVo> page, Stock stock);

	Map<String, Object> addOrUpdate(Stock stock, HttpServletRequest request);

	Map<String, Object> delete(Integer stockId, HttpServletRequest request);



}
