package com.hallo.mall.enums;

import lombok.Getter;

/**
 * @author hallo
 * @datetime 2022-10-10 20:38
 * @description
 */
@Getter
public enum ResponseEnum {

    ERROR(-1, "服务端错误"),
    SUCCESS(0, "成功"),

    PASSWORD_ERROR(1, "密码错误"),
    USERNAME_EXIST(2, "用户已存在"),
    PARAM_ERROR(3, "参数错误"),
    EMAIL_EXIST(4, "邮箱已存在"),
    NEED_LOGIN(10, "用户未登录，请先登陆"),
    USER_OR_PASSWORD_ERROR(11, "用户名或密码错误"),
    PRODUCT_OFF_SALE_OR_DELETE(12, "商品下架或删除"),
    PRODUCT_NOT_EXIST(13, "商品不存在"),
    PRODUCT_STOCK_ERROE(14, "商品库存不足"),

    CART_PRODUCT_NOT_EXIST(15, "商品不存在"),

    DELETE_SHIPPING_FAIL(16, "删除购物车失败"),

    SHIPPING_NOT_EXITS(17, "购物车不存在"),
    CART_SELECT_IS_EMPTY(18, "请选购商品后下单"),

    ORDER_NOT_EXIT(19, "订单不存在"),
    ORDER_STATUS_ERROR(20, "订单状态异常"),
    ORDER_NOT_EXIST(21, "订单不存在")
    ;

    Integer code;
    String desc;

    ResponseEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
