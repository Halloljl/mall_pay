package com.hallo.mall.service;

import com.hallo.mall.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hallo.mall.vo.ResponseVo;

/**
* @author 72460
* @description 针对表【mall_user】的数据库操作Service
* @createDate 2022-10-10 16:25:00
*/
public interface UserService extends IService<User> {

    /**
     * 注册
     * @param user
     * @return
     */
    ResponseVo<User> register(User user);

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    ResponseVo<User> login(String username, String password);

}
