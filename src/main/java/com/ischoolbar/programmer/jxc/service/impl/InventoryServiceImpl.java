package com.ischoolbar.programmer.jxc.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ischoolbar.programmer.jxc.entity.Inventory;
import com.ischoolbar.programmer.jxc.mapper.InventoryMapper;
import com.ischoolbar.programmer.jxc.pojo.InventoryVo;
import com.ischoolbar.programmer.jxc.service.IInventoryService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-03-30
 */
@Service
public class InventoryServiceImpl extends ServiceImpl<InventoryMapper, Inventory> implements IInventoryService {

	@Override
	public Page<InventoryVo> selectByPage(Page<InventoryVo> page, Inventory inventory) {
		return baseMapper.selectByPage(page,inventory);
	}

}
