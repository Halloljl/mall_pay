package com.hallo.mall.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author hallo
 * @datetime 2022-10-13 15:19
 * @description 添加商品
 */
@Data
public class CartAddForm {

    @NotNull
    private Integer productId;

    private Boolean selected = true;
}
