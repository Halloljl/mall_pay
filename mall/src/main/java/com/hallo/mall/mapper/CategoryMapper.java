package com.hallo.mall.mapper;

import com.hallo.mall.pojo.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 72460
* @description 针对表【mall_category】的数据库操作Mapper
* @createDate 2022-10-11 20:49:23
* @Entity com.hallo.mall.pojo.Category
*/
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

}




