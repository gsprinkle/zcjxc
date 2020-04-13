package com.ischoolbar.programmer.jxc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
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
public class Store implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "store_id", type = IdType.AUTO)
    private Integer storeId;

    private String storeName;

    private String storeRemark;


}
