package com.hallo.mall.enums;

import lombok.Getter;

/**
 * @author hallo
 * @datetime 2022-10-15 13:56
 * @description
 */
@Getter
public enum PaymentEnum {

    PAY_ONLINE(1, "在线支付"),
    ;

    Integer code;
    String desc;

    PaymentEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
