package com.bt.pojo;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 *
 * @since 2022-05-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("s_goods")
public class Goods implements Serializable {

    private static final long serialVersionUID = 1L;


    private String id;

    private String title;

    private String cateId;

    private BigDecimal price;

    private String prams;

    private Integer num;

    private String desc;

    private Date createTime;

    private Date updateTime;


}
