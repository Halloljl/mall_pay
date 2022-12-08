package com.hallo.mall.service;

import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hallo.mall.MallApplicationTests;
import com.hallo.mall.form.CartAddForm;
import com.hallo.mall.vo.OrderVo;
import com.hallo.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author hallo
 * @datetime 2022-10-15 20:47
 * @description
 */
@Slf4j
@Transactional
public class OrderServiceTest extends MallApplicationTests {

    @Resource
    private OrderService orderService;
    @Resource
    private CartService cartService;
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    Integer uid = 1;
    Integer shippingId = 1;

    private Integer productId = 26;

    @Before
    public void cartAdd() {
        CartAddForm cartAddForm = new CartAddForm();
        cartAddForm.setProductId(productId);
        cartService.add(uid, cartAddForm);
    }

    @Test
    public void create() {
        ResponseVo<OrderVo> orderVoResponseVo = orderService.create(uid, shippingId);
        log.info("orderVoResponseVo = {}", gson.toJson(orderVoResponseVo));
        Assert.assertEquals(ResponseVo.success().getStatus(), orderVoResponseVo.getStatus());

    }

    private ResponseVo<OrderVo> createTest() {
        ResponseVo<OrderVo> orderVoResponseVo = orderService.create(uid, shippingId);
        log.info("orderVoResponseVo = {}", gson.toJson(orderVoResponseVo));
        return orderVoResponseVo;

    }

    @Test
    public void list() {
        ResponseVo<PageInfo> pageInfoResponseVo = orderService.list(uid, 1, 2);
        log.info("pageInfoResponseVo = {}", gson.toJson(pageInfoResponseVo));
        Assert.assertEquals(ResponseVo.success().getStatus(), pageInfoResponseVo.getStatus());

    }

    @Test
    public void detail() {
        ResponseVo<OrderVo> orderVoResponseVo = createTest();
        ResponseVo<OrderVo> details = orderService.details(uid, orderVoResponseVo.getData().getOrderNo());

        log.info("details = {}", gson.toJson(details));
        Assert.assertEquals(ResponseVo.success().getStatus(), details.getStatus());
    }

    @Test
    public void close() {
        ResponseVo<OrderVo> orderVoResponseVo = createTest();
        ResponseVo<OrderVo> close = orderService.close(uid, orderVoResponseVo.getData().getOrderNo());

        log.info("close = {}", gson.toJson(close));
        Assert.assertEquals(ResponseVo.success().getStatus(), close.getStatus());
    }
}