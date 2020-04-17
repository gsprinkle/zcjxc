package com.ischoolbar.programmer.jxc.mapper;

import com.ischoolbar.programmer.jxc.entity.Stock;
import com.ischoolbar.programmer.jxc.pojo.StockVo;

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
public interface StockMapper extends BaseMapper<Stock> {

	Page<StockVo> selectByPage(Page<StockVo> page, @Param("stock")Stock stock);

}
