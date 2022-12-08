package com.hallo.pay.service.impl;

import com.hallo.pay.PayApplicationTests;
import com.hallo.pay.service.PayService;
import org.junit.Test;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * @author hallo
 * @datetime 2022-10-09 13:36
 * @description
 */
public class PayServiceTest extends PayApplicationTests {

    @Autowired
    private PayService payService;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Test
    public void create() {
//        payService.create("123131231231", BigDecimal.valueOf(0.01));
    }

    @Test
    public void sendMQMsg() {
        amqpTemplate.convertAndSend("payNotify", "hello");
    }
}