package com.hallo.mall.mapper;

import com.hallo.mall.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 72460
* @description 针对表【mall_user】的数据库操作Mapper
* @createDate 2022-10-10 16:25:00
* @Entity com.hallo.mall.pojo.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




