package com.ischoolbar.programmer.jxc.mapper;

import com.ischoolbar.programmer.jxc.entity.Inventory;
import com.ischoolbar.programmer.jxc.pojo.InventoryVo;

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
public interface InventoryMapper extends BaseMapper<Inventory> {
	public Page<InventoryVo> selectByPage(Page<InventoryVo> page, @Param("inv")Inventory inventory);
}
