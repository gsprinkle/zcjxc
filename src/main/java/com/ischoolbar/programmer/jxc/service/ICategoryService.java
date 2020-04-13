package com.ischoolbar.programmer.jxc.service;

import com.ischoolbar.programmer.jxc.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author jobob
 * @since 2020-03-30
 */
public interface ICategoryService extends IService<Category> {
	int removes(Integer id);
}
