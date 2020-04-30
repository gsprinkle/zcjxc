package com.ischoolbar.programmer.jxc.service;

import com.ischoolbar.programmer.jxc.entity.Category;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

	Map<String, Object> addOrUpdate(Category category, HttpServletRequest request);

	Map<String, Object> delete(Integer cid, HttpServletRequest request);
}
