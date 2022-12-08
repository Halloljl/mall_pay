package com.hallo.mall.service;

import com.github.pagehelper.PageInfo;
import com.hallo.mall.MallApplicationTests;
import com.hallo.mall.enums.ResponseEnum;
import com.hallo.mall.vo.ProductDetailVo;
import com.hallo.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * @author hallo
 * @datetime 2022-10-12 19:50
 * @description
 */
@Slf4j
public class ProductServiceTest extends MallApplicationTests {

    @Resource
    private ProductService productService;

    @Test
    public void getList() {

        ResponseVo<PageInfo> productVOList = productService.getList(null, 1, 1);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), productVOList.getStatus());

    }

    @Test
    public void detail() {
        ResponseVo<ProductDetailVo> detail = productService.detail(26);
        log.info("detail={}", detail);
    }
}