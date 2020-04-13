package com.ischoolbar.programmer.jxc.service;

import com.ischoolbar.programmer.jxc.entity.Brand;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jobob
 * @since 2020-04-02
 */
public interface IBrandService extends IService<Brand> {

	Page<Brand> selectByPage(Page<Brand> page, Brand brand);

}
