package com.hallo.pay.config;

import com.lly835.bestpay.config.WxPayConfig;
import com.lly835.bestpay.service.BestPayService;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author hallo
 * @datetime 2022-10-09 15:40
 * @description
 */
@Component
public class BestPayConfig {

    @Resource
    private WxAccountConfig wxAccountConfig;

    @Bean
    public BestPayService getBestPayService(WxPayConfig wxPayConfig) {

        //支付类, 所有方法都在这个类里
        BestPayServiceImpl bestPayService = new BestPayServiceImpl();
        bestPayService.setWxPayConfig(wxPayConfig);
        return bestPayService;
    }


    @Bean
    public WxPayConfig wxPayConfig() {
        /**
         * appdi：wx3e6b9f1c5a7ff034
         * 商户id：1614433647
         * 商户Key：Aa111111111122222222223333333333
         */
        WxPayConfig wxPayConfig = new WxPayConfig();
        wxPayConfig.setAppId(wxAccountConfig.getAppId());
        wxPayConfig.setMchId(wxAccountConfig.getMchId());
        wxPayConfig.setMchKey(wxAccountConfig.getMchKey());
        wxPayConfig.setNotifyUrl(wxAccountConfig.getNotifyUrl());
        wxPayConfig.setReturnUrl(wxAccountConfig.getReturnUrl());
        return wxPayConfig;
    }
}
