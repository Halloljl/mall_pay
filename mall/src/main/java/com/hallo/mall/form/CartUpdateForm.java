package com.hallo.mall.form;

import lombok.Data;

/**
 * @author hallo
 * @datetime 2022-10-13 21:01
 * @description
 */
@Data
public class CartUpdateForm {

    private Integer quantity; //非必填

    private Boolean selected; //非必填
}
