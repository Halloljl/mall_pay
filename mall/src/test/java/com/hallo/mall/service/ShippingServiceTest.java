package com.hallo.mall.service;

import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hallo.mall.MallApplicationTests;
import com.hallo.mall.form.ShippingForm;
import com.hallo.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * @author hallo
 * @datetime 2022-10-13 23:39
 * @description
 */
@Slf4j
public class ShippingServiceTest extends MallApplicationTests {

    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Resource
    private ShippingService shippingService;

    Integer uid = 1;


    @Test
    public void add() {
        ShippingForm shippingForm = new ShippingForm();
        shippingForm.setReceiverName("刘金磊");
        shippingForm.setReceiverPhone("15941819999");
        shippingForm.setReceiverMobile("9999");
        shippingForm.setReceiverProvince("辽宁省");
        shippingForm.setReceiverCity("阜新市");
        shippingForm.setReceiverDistrict("阜新蒙古族自治县");
        shippingForm.setReceiverAddress("1");
        shippingForm.setReceiverZip("123100");

        shippingService.add(1, shippingForm);
    }

    @Test
    public void delete() {

        ResponseVo delete = shippingService.delete(1, 1);
        log.info("delete = {}", gson.toJson(delete));
    }

    @Test
    public void update() {
        ShippingForm shippingForm = new ShippingForm();
        shippingForm.setReceiverName("刘金磊");
        shippingForm.setReceiverPhone("15941819999");
        shippingForm.setReceiverMobile("9999");
        shippingForm.setReceiverProvince("辽宁省");
        shippingForm.setReceiverCity("阜新市");
        shippingForm.setReceiverDistrict("阜新蒙古族自治县");
        shippingForm.setReceiverAddress("1");
        shippingForm.setReceiverZip("123100");

        ResponseVo update = shippingService.update(2, 1, shippingForm);

        log.info("update = {}", gson.toJson(update));
    }

    @Test
    public void list() {

        ResponseVo<PageInfo> list = shippingService.list(1, 1, 1);

        log.info("list= {}", gson.toJson(list));
    }
}