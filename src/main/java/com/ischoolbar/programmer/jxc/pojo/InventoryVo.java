package com.ischoolbar.programmer.jxc.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ischoolbar.programmer.jxc.entity.Inventory;

import lombok.Data;

/**
 * 
 * @author 作者 郭小雨
 * 
 * @version 创建时间：2020年4月13日 上午11:41:39
 * 
 *          类说明
 * 
 */
@Data
public class InventoryVo extends Inventory {
	// i.inventory_id,c.cname,b.brand_name,i.inventory_num,s.store_name
	private String productName;
	private Integer remind;
	private String cname;
	private String brandName;
	private String storeName;
	private Integer brandId;
	private boolean unenough;
}
