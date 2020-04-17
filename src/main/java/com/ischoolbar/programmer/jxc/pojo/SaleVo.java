package com.ischoolbar.programmer.jxc.pojo;

import com.ischoolbar.programmer.jxc.entity.Sale;

import lombok.Data;
import lombok.EqualsAndHashCode;

/** 

* @author 作者 郭小雨

* @version 创建时间：2020年4月16日 上午10:51:44 

* 类说明 

*/
@Data
@EqualsAndHashCode(callSuper=false)
public class SaleVo extends Sale{
	private String productName;		// 产品名称
	private String ename;			// 销售人
	private String custName;		// 客户 
}
