package com.hallo.mall.controller;

import com.hallo.mall.consts.MallConst;
import com.hallo.mall.enums.ResponseEnum;
import com.hallo.mall.form.UserRegisterForm;
import com.hallo.mall.form.UserLoginForm;
import com.hallo.mall.pojo.User;
import com.hallo.mall.service.UserService;
import com.hallo.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @author hallo
 * @datetime 2022-10-10 20:19
 * @description
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public ResponseVo register(@Valid @RequestBody UserRegisterForm userRegisterForm) {

        log.info("user={}", userRegisterForm);
        User user = new User();
        BeanUtils.copyProperties(userRegisterForm, user);

        return userService.register(user);
    }

    @PostMapping("/login")
    public ResponseVo register(@Valid @RequestBody UserLoginForm userLoginForm,
                               HttpSession httpSession) {

        ResponseVo<User> userResponseVo = userService.login(userLoginForm.getUsername(), userLoginForm.getPassword());

        // 设置Session
        httpSession.setAttribute(MallConst.CURRENT_USER, userResponseVo.getData());

        return userResponseVo;
    }

    @GetMapping("")
    public ResponseVo<User> userInfo(HttpSession session) {

        User user = (User) session.getAttribute(MallConst.CURRENT_USER);

        if (user == null) {
            return ResponseVo.error(ResponseEnum.NEED_LOGIN);
        }

        return ResponseVo.success(user);
    }

    @GetMapping("/logout")
    public ResponseVo<User> logout(HttpSession session) {

        session.removeAttribute(MallConst.CURRENT_USER);

        return ResponseVo.success();
    }


}
