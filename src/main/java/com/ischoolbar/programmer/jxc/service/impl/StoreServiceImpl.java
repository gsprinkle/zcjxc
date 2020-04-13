package com.ischoolbar.programmer.jxc.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ischoolbar.programmer.jxc.entity.Store;
import com.ischoolbar.programmer.jxc.mapper.StoreMapper;
import com.ischoolbar.programmer.jxc.service.IStoreService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-03-30
 */
@Service
public class StoreServiceImpl extends ServiceImpl<StoreMapper, Store> implements IStoreService {

	@Override
	public Page<Store> selectByPage(Page<Store> page, Store store) {
		QueryWrapper<Store> queryWrapper = new QueryWrapper<>(store);
		return baseMapper.selectPage(page, queryWrapper);
	}

}
