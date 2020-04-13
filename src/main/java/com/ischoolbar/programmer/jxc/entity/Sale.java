package com.ischoolbar.programmer.jxc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
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
public class Sale implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "sale_id", type = IdType.AUTO)
    private Integer saleId;

    private Integer eid;

    private Integer productId;

    private Integer salNum;

    private LocalDate saleDate;

    private String saleRemark;


}
