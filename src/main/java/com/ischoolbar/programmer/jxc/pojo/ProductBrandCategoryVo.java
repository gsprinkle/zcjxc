package com.ischoolbar.programmer.jxc.pojo;

import com.ischoolbar.programmer.jxc.entity.Product;

import lombok.Data;

/** 

* @author 作者 郭小雨

* @version 创建时间：2020年4月3日 下午3:59:10 

* 类说明 

*/
@Data
public class ProductBrandCategoryVo extends Product {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String brandName; // 品牌名称
	
	private String cname; // 分类名称
	
	private String storeName;// 所在仓库
	
}
