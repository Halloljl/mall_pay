package com.hallo.mall.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hallo.mall.MallApplicationTests;
import com.hallo.mall.enums.ResponseEnum;
import com.hallo.mall.form.CartAddForm;
import com.hallo.mall.form.CartUpdateForm;
import com.hallo.mall.vo.CartVo;
import com.hallo.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * @author hallo
 * @datetime 2022-10-13 16:15
 * @description
 */
@Slf4j
public class CartServiceTest extends MallApplicationTests {

    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    @Resource
    private CartService cartService;

    private Integer productId = 27;

    private Integer uid = 1;

    @Before
    public void add() {
        log.info("add 执行中.....");
        CartAddForm form = new CartAddForm();
        form.setProductId(productId);
        form.setSelected(true);
        ResponseVo<CartVo> cartVoResponseVo = cartService.add(uid, form);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), cartVoResponseVo.getStatus());
        log.info("result = {}", gson.toJson(cartVoResponseVo));
    }

    @Test
    public void test() {
        ResponseVo<CartVo> list = cartService.list(uid);
        log.info("list = {}", gson.toJson(list));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), list.getStatus());
    }

    @Test
    public void update() {

        CartUpdateForm cartUpdateForm = new CartUpdateForm();
        cartUpdateForm.setQuantity(10);
        cartUpdateForm.setSelected(false);
        ResponseVo<CartVo> update = cartService.update(uid, productId, cartUpdateForm);
        log.info("result = {}", gson.toJson(update));

        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), update.getStatus());
    }


    @After
//    @Test
    public void delete() {
        log.info("delete 执行中.....");
        ResponseVo<CartVo> delete = cartService.delete(uid, productId);
        log.info("result = {}", gson.toJson(delete));

        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), delete.getStatus());
    }

    @Test
    public void selectAll() {
        log.info("selectAll 执行...");
        ResponseVo<CartVo> cartVoResponseVo = cartService.selectAll(uid);

        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), cartVoResponseVo.getStatus());
    }

    @Test
    public void unSelectAll() {
        log.info("unSelectAll 执行...");
        ResponseVo<CartVo> cartVoResponseVo = cartService.unSelectAll(uid);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), cartVoResponseVo.getStatus());
    }

    @Test
    public void sum() {
        log.info("sum 执行...");
        ResponseVo<Integer> sum = cartService.sum(uid);

        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), sum.getStatus());
    }





}