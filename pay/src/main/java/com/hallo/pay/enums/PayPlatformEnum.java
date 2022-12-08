package com.hallo.pay.enums;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import lombok.Getter;

/**
 * @author hallo
 * @datetime 2022-10-09 22:56
 * @description
 */
@Getter
public enum PayPlatformEnum {

    ALPAY(1),
    WX(2),
    ;
    Integer code;

    PayPlatformEnum(Integer code) {
        this.code = code;
    }

    public static PayPlatformEnum getBestPayTypeEnum(BestPayTypeEnum bestPayTypeEnum) {
        for (PayPlatformEnum payPlatformEnum : PayPlatformEnum.values()) {
            if (bestPayTypeEnum.getPlatform().name().equals(payPlatformEnum.name())) {
                return payPlatformEnum;
            }
        }
        throw new RuntimeException("错误的支付平台：" + bestPayTypeEnum.name());
    }
}
