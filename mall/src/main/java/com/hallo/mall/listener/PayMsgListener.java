package com.hallo.mall.listener;

import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.hallo.mall.pojo.PayInfo;
import com.hallo.mall.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author hallo
 * @datetime 2022-10-15 23:22
 * @description
 */
@Component
@RabbitListener(queues = "payNotify")
@Slf4j
public class PayMsgListener {

    @Resource
    private OrderService orderService;

    @RabbitHandler
    public void process(String msg) {
        log.info("【接收熬的消息】 ===》{}" , msg);

        PayInfo payInfo = new Gson().fromJson(msg, PayInfo.class);
        if (payInfo.getPlatformStatus().equals("SUCCESS")) {
            // 修改订单里的状态
            orderService.paid(payInfo.getOrderNo());

        }
    }
}
