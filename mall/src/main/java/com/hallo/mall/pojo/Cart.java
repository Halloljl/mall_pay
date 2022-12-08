package com.hallo.mall.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hallo
 * @datetime 2022-10-13 16:01
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    private Integer productId;

    private Integer quantity;

    private Boolean productSelected;



}
