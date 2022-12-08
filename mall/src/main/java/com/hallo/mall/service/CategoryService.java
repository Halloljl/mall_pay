package com.hallo.mall.service;

import com.hallo.mall.pojo.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hallo.mall.vo.CategoryVo;
import com.hallo.mall.vo.ResponseVo;

import java.util.List;
import java.util.Set;

/**
* @author 72460
* @description 针对表【mall_category】的数据库操作Service
* @createDate 2022-10-11 20:49:23
*/
public interface CategoryService extends IService<Category> {


    /**
     * 查询类别
     * @return
     */
    ResponseVo<List<CategoryVo>> selectAll();

    /**
     * 根据ID查询子类别的编号
     * @param id
     * @param resultSet
     */
    void findSubCategoryId(Integer id, Set<Integer> resultSet);
}
