package com.ischoolbar.programmer.jxc.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ischoolbar.programmer.jxc.entity.Inventory;
import com.ischoolbar.programmer.jxc.entity.Product;
import com.ischoolbar.programmer.jxc.mapper.InventoryMapper;
import com.ischoolbar.programmer.jxc.mapper.ProductMapper;
import com.ischoolbar.programmer.jxc.pojo.InventoryVo;
import com.ischoolbar.programmer.jxc.service.IInventoryService;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-03-30
 */
@Service
public class InventoryServiceImpl extends ServiceImpl<InventoryMapper, Inventory> implements IInventoryService {

	@Autowired
	ProductMapper productMapper;

	@Override
	public Page<InventoryVo> selectByPage(Page<InventoryVo> page, Inventory inventory) {
		return baseMapper.selectByPage(page, inventory);
	}

	@Override
	public Map<String, Object> initList(HttpServletRequest request) {
		Map<String, Object> ret = new HashMap<>();
		// 初始化库存信息 先清空
		baseMapper.truncate();
		// 初始化列表
		List<Inventory> initList = baseMapper.initList();
		// 库存列表中是否存在该物品，存在修改数量，不存在添加
		for (Inventory inv : initList) {
			saveOrUpdate(inv);
		}
		ret.put("type", "success");
		ret.put("info", "初始化成功");
		return ret;
	}

}
