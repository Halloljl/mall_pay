package com.hallo.pay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.gson.Gson;
import com.hallo.pay.enums.PayPlatformEnum;
import com.hallo.pay.mapper.PayInfoMapper;
import com.hallo.pay.pojo.PayInfo;
import com.hallo.pay.service.PayService;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.enums.OrderStatusEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.BestPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @author hallo
 * @datetime 2022-10-09 13:16
 * @description
 */
@Slf4j
@Service
public class PayServiceImpl implements PayService {

    public static final String QUEUE_PAY_NOTIFY = "payNotify";

    @Autowired
    private BestPayService bestPayService;
    @Resource
    private PayInfoMapper payInfoMapper;
    @Autowired
    private AmqpTemplate amqpTemplate;


    @Override
    public PayResponse create(String orderId, BigDecimal amount, BestPayTypeEnum bestPayTypeEnum) {

        PayInfo payInfo = new PayInfo(Long.parseLong(orderId),
                PayPlatformEnum.getBestPayTypeEnum(bestPayTypeEnum).getCode(),
                OrderStatusEnum.NOTPAY.name(),
                amount);
        // 写入数据库
        payInfoMapper.insert(payInfo);




        PayRequest request = new PayRequest();
        request.setOrderName("hallo-2022/10/9");
        request.setOrderId(orderId);
        request.setOrderAmount(amount.doubleValue());
        request.setPayTypeEnum(BestPayTypeEnum.WXPAY_NATIVE);
        PayResponse response = bestPayService.pay(request);
        log.info("发起支付 response={}", response);

        return response;
    }

    @Override
    public String asyncNotify(String notifyData) {

        // 1.签名校验
        PayResponse payResponse = bestPayService.asyncNotify(notifyData);
        log.info("异步通知 payResponse={}",payResponse);

        // 2。金额校验（从数据库查订单）
        // 比较严重（正常情况下不会发生的） 发出告警：钉钉，短信
        QueryWrapper<PayInfo> payInfoQueryWrapper = new QueryWrapper<>();
        payInfoQueryWrapper.eq("order_no", Long.parseLong(payResponse.getOrderId()));
        PayInfo payInfo = payInfoMapper.selectOne(payInfoQueryWrapper);

        if (payInfo == null) {
            throw new RuntimeException("通过orderNo查询到的结果是null,orderId = "+payResponse.getOrderId());
        }
        // 如果订单支付状态不是“已支付”
        if(!payInfo.getPlatformStatus().equals(OrderStatusEnum.SUCCESS.name())) {
            if (payInfo.getPayAmount().compareTo(BigDecimal.valueOf(payResponse.getOrderAmount())) != 0) {
                // 告警
                throw new RuntimeException("异步通知中的金额和数据库里的不一致，orderNo="+ payResponse.getOrderId());
            }
            // 3.如果金额一致，修改支付状态
            payInfo.setPlatformStatus(OrderStatusEnum.SUCCESS.name());
            payInfo.setPlatformNumber(payResponse.getOutTradeNo());
            payInfo.setUpdateTime(null);

            UpdateWrapper<PayInfo> payInfoUpdateWrapper = new UpdateWrapper<>();
            payInfoUpdateWrapper.eq("id", payInfo.getId());

            payInfoMapper.update(payInfo, payInfoUpdateWrapper);
        }

        //TODO pay发送MQ消息，mall接受MQ消息
        amqpTemplate.convertAndSend(QUEUE_PAY_NOTIFY, new Gson().toJson(payInfo));



        return "<xml>\n"+
                " <return_code><![CDATA[SUCCESS]]></return_code>\n" +
                " <return_msg><![CDATA[OK]]></return_msg>\n" +
                "</xml>";
    }


}
