package com.hallo.mall.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @author hallo
 * @datetime 2022-10-13 23:21
 * @description
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShippingForm {

    /**
     * 收货姓名
     */
    @NotBlank
    private String receiverName;

    /**
     * 收货固定电话
     */
    @NotBlank
    private String receiverPhone;

    /**
     * 收货移动电话
     */
    @NotBlank
    private String receiverMobile;

    /**
     * 省份
     */
    @NotBlank
    private String receiverProvince;

    /**
     * 城市
     */
    @NotBlank
    private String receiverCity;

    /**
     * 区/县
     */
    @NotBlank
    private String receiverDistrict;

    /**
     * 详细地址
     */
    @NotBlank
    private String receiverAddress;

    /**
     * 邮编
     */
    @NotBlank
    private String receiverZip;

}
