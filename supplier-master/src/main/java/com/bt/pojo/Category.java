package com.bt.pojo;

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
@TableName("s_category")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;


    private String id;

    private String name;

    private Integer cateNum;

    private Date createTime;

    private Date updateTime;

    private Integer status;


}
