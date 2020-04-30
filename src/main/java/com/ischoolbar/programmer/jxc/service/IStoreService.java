package com.ischoolbar.programmer.jxc.service;

import com.ischoolbar.programmer.jxc.entity.Store;

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
public interface IStoreService extends IService<Store> {

	Page<Store> selectByPage(Page<Store> page, Store store);

	Map<String, Object> addOrUpdate(Store store, HttpServletRequest request);

	Map<String, Object> delete(Integer storeId, HttpServletRequest request);

}
