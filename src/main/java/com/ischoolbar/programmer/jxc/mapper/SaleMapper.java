package com.ischoolbar.programmer.jxc.mapper;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ischoolbar.programmer.jxc.entity.Sale;
import com.ischoolbar.programmer.jxc.pojo.SaleVo;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2020-03-30
 */
public interface SaleMapper extends BaseMapper<Sale> {

	Page<SaleVo> selectByPage(Page<SaleVo> page, @Param("sale")Sale sale);

}
