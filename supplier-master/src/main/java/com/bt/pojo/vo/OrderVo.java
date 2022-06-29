package com.bt.pojo.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 *
 * @website https://blog.csdn.net/Gaowumao
 * @Date 2022-05-06 11:53
 * @Description
 */
@Data
public class OrderVo {

    private String orderId;

    private String username;

    private String goodsName;

    private Integer type;

    private Integer orderNum;

    private BigDecimal totalPrice;

}
