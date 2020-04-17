package com.ischoolbar.programmer.jxc.pojo;

import com.ischoolbar.programmer.jxc.entity.Stock;

import lombok.Data;
import lombok.EqualsAndHashCode;

/** 

* @author 作者 郭小雨

* @version 创建时间：2020年4月15日 上午11:16:25 

* 类说明 

*/
@Data
@EqualsAndHashCode(callSuper=false)
public class StockVo extends Stock{
	private String productName;// 产品
	
}
