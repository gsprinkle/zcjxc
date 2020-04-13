package com.ischoolbar.programmer.jxc.service.impl;

import com.ischoolbar.programmer.jxc.entity.Category;
import com.ischoolbar.programmer.jxc.mapper.CategoryMapper;
import com.ischoolbar.programmer.jxc.service.ICategoryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-03-30
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

	int deleteNum;
	
	public int removes(Integer id){
		deleteNum = 0;
		return deleteIdWithChild(id);
	}

	// 递归方法
	public int deleteIdWithChild(Integer id){
			// 先查询出该分类是否有子节点
			QueryWrapper<Category> queryWrapper = new QueryWrapper<Category>();
			queryWrapper.eq("pid", id);
			List<Category> catList = baseMapper.selectList(queryWrapper);
			// 有子节点
			if(catList != null && catList.size() > 0){
				for(Category cat : catList){
					// 删除该孩子
					baseMapper.deleteById(cat.getCid());
					deleteIdWithChild(cat.getCid());
				}
			}
			deleteNum += baseMapper.deleteById(id);
			return deleteNum;
		}
}
