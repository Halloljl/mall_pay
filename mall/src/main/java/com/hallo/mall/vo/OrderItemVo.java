package com.hallo.mall.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author hallo
 * @datetime 2022-10-14 23:05
 * @description
 */
@Data
public class OrderItemVo {

	private Long orderNo;

	private Integer productId;

	private String productName;

	private String productImage;

	private BigDecimal currentUnitPrice;

	private Integer quantity;

	private BigDecimal totalPrice;

	private Date createTime;
}
