package com.hallo.mall.service;

import com.hallo.mall.MallApplicationTests;
import com.hallo.mall.enums.ResponseEnum;
import com.hallo.mall.vo.CategoryVo;
import com.hallo.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author hallo
 * @datetime 2022-10-12 15:38
 * @description
 */
@Slf4j
public class CategoryServiceTest extends MallApplicationTests {

    @Resource
    private CategoryService categoryService;

    @Test
    public void selectAll() {

        ResponseVo<List<CategoryVo>> listResponseVo = categoryService.selectAll();
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), listResponseVo.getStatus());
    }


    @Test
    public void findSubCategoryId() {

        Set<Integer> resultSet = new HashSet<>();

        categoryService.findSubCategoryId(100001, resultSet);

        log.info("set = {}", resultSet);
    }

}