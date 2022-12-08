package com.hallo.pay.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hallo.pay.pojo.PayInfo;
import com.hallo.pay.service.PayInfoService;
import com.hallo.pay.service.PayService;
import com.lly835.bestpay.config.WxPayConfig;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;

/**
 * @author hallo
 * @datetime 2022-10-09 13:53
 * @description
 */
@Slf4j
@Controller
@RequestMapping("/pay")
public class PayController {

    @Autowired
    private PayService payService;
    @Resource
    private PayInfoService payInfoService;
    @Resource
    private WxPayConfig wxPayConfig;

    @RequestMapping("/create")
    public ModelAndView create(@RequestParam("orderId") String orderId,
                                @RequestParam("amount") BigDecimal amount,
                               @RequestParam("payType") BestPayTypeEnum bestPayTypeEnum) {

        PayResponse payResponse = payService.create(orderId, amount, bestPayTypeEnum);


        HashMap<String, String> map = new HashMap<>();
        map.put("codeUrl", payResponse.getCodeUrl());
        map.put("orderId", orderId);
        map.put("returnUrl", wxPayConfig.getReturnUrl());

        return new ModelAndView("create",map);
    }

    @PostMapping("/notify")
    @ResponseBody
    public String asyncNotify(@RequestBody String notifyData) {
        log.info("notifyData={}", notifyData);
        return payService.asyncNotify(notifyData);

    }


    @GetMapping("/queryByOrderId")
    @ResponseBody
    public PayInfo queryByOrderId(@RequestParam("orderId") String orderId) {
        log.info("查询支付记录.....");
        QueryWrapper<PayInfo> payInfoQueryWrapper = new QueryWrapper<>();
        payInfoQueryWrapper.eq("order_no", orderId);

        PayInfo one = payInfoService.getOne(payInfoQueryWrapper);
        System.out.println("one.getPlatformStatus() = " + one.getPlatformStatus());
        return one;
    }




}
