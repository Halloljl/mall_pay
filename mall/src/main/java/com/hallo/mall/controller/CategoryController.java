package com.hallo.mall.controller;

import com.hallo.mall.service.CategoryService;
import com.hallo.mall.vo.CategoryVo;
import com.hallo.mall.vo.ResponseVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author hallo
 * @datetime 2022-10-11 21:11
 * @description
 */
@RestController
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @GetMapping("/categories")
    ResponseVo<List<CategoryVo>> selectAll() {
        return categoryService.selectAll();
    }
}
