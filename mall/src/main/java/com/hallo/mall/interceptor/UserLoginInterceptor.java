package com.hallo.mall.interceptor;

import com.hallo.mall.consts.MallConst;
import com.hallo.mall.exception.UserLoginException;
import com.hallo.mall.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author hallo
 * @datetime 2022-10-11 20:05
 * @description
 */
@Slf4j
public class UserLoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info(" preHandle....." );
        User user = (User) request.getSession().getAttribute(MallConst.CURRENT_USER);
        if (user == null) {
//            return ResponseVO.ERROR(ResponseEnum.NEED_LOGIN);
            throw new UserLoginException();
//            return false;
        }
        return true;
    }
}
