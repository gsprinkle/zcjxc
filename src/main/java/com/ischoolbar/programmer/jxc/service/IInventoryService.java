package com.ischoolbar.programmer.jxc.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ischoolbar.programmer.jxc.entity.Brand;
import com.ischoolbar.programmer.jxc.entity.Inventory;
import com.ischoolbar.programmer.jxc.pojo.InventoryVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jobob
 * @since 2020-03-30
 */
public interface IInventoryService extends IService<Inventory> {

	Page<InventoryVo> selectByPage(Page<InventoryVo> page, Inventory inventory);

	Map<String, Object> initList(HttpServletRequest request);

}
