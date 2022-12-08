package com.hallo.mall.service;

import com.hallo.mall.MallApplicationTests;
import com.hallo.mall.enums.ResponseEnum;
import com.hallo.mall.enums.RoleEnum;
import com.hallo.mall.pojo.User;
import com.hallo.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author hallo
 * @datetime 2022-10-10 17:30
 * @description
 * 加事务，使得测试数据不对数据造成污染
 */
@Transactional
@Slf4j
public class UserServiceTest extends MallApplicationTests {

    @Resource
    private UserService userService;

    @Test
    @Before
    public void register() {

        User user = new User("jack","123141","jack@qq.com", RoleEnum.CUSTOMER.getCode());
        userService.register(user);
    }


    @Test
    public void login() {

        ResponseVo<User> responseVO = userService.login("jack", "123141");

        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVO.getStatus());

    }
}

