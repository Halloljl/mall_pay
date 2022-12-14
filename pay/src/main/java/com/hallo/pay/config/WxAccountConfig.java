package com.hallo.pay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author hallo
 * @datetime 2022-10-10 11:55
 * @description
 */
@Component
@Data
@ConfigurationProperties(prefix = "wx")
public class WxAccountConfig {

    private String appId;
    private String mchId;
    private String mchKey;
    private String notifyUrl;
    private String returnUrl;
}
