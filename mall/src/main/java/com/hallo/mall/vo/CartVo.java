package com.hallo.mall.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author hallo
 * @datetime 2022-10-13 15:06
 * @description
 */
@Data
public class CartVo {

    private List<CartProductVo> cartProductVoList;

    private Boolean selectedAll;

    private BigDecimal cartTotalPrice;

    private Integer cartTotalQuantity;
}
