package com.ischoolbar.programmer.jxc.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ischoolbar.programmer.jxc.entity.Inventory;
import com.ischoolbar.programmer.jxc.pojo.InventoryVo;

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
	
	public void truncate();
	public List<Inventory> initList();
	
	
}
