package com.ischoolbar.programmer.jxc.entity;

import java.io.Serializable;
import java.sql.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author jobob
 * @since 2020-03-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Sale extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value = "sale_id", type = IdType.AUTO)
	private Integer saleId;

	private Integer eid;

	private Integer custId;
	
	private Integer productId;

	private Integer saleNum;

	private Date saleDate;

	private String saleRemark;

	// 查询字段，不映射数据库
	@TableField(exist = false)
	private Integer dateModel;
	@TableField(exist = false)
	private String date;

}
