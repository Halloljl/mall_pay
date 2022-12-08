package com.hallo.pay.service;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayResponse;

import java.math.BigDecimal;

/**
 * @author hallo
 * @datetime 2022-10-09 13:15
 * @description
 */
public interface PayService {

    /**
     * 创建/发起支付
     * @param orderId
     * @param amount
     */
    PayResponse create(String orderId, BigDecimal amount, BestPayTypeEnum bestPayTypeEnum);

    /**
     * 异步通知处理
     *
     * @param notifyData
     * @return
     */
    String asyncNotify(String notifyData);
}
