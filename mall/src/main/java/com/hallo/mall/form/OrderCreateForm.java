package com.hallo.mall.form;

import lombok.Data;

import javax.validation.constraints.NotNull;


/**
 * @author hallo
 * @datetime 2022-10-15 22:56
 * @description
 */
@Data
public class OrderCreateForm {

    @NotNull
    private Integer shippingId;
}
