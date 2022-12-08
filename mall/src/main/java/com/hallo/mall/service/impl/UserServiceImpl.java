package com.hallo.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hallo.mall.enums.ResponseEnum;
import com.hallo.mall.enums.RoleEnum;
import com.hallo.mall.pojo.User;
import com.hallo.mall.service.UserService;
import com.hallo.mall.vo.ResponseVo;
import org.springframework.util.DigestUtils;
import com.hallo.mall.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author 72460
* @description 针对表【mall_user】的数据库操作Service实现
* @createDate 2022-10-10 16:25:00
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "hallo";

    /**
     * 注册
     * @param user 用户
     */
    @Override
    public ResponseVo<User> register(User user) {

        // username 不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername());
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
//            throw new RuntimeException("该username已注册");
            return ResponseVo.error(ResponseEnum.USERNAME_EXIST);
        }

        // email 不重复
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", user.getEmail());
        count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
//            throw new RuntimeException("该email已注册");
            return ResponseVo.error(ResponseEnum.EMAIL_EXIST);
        }

        user.setRole(RoleEnum.CUSTOMER.getCode());

        // MD5 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + user.getPassword()).getBytes()); ;
        user.setPassword(encryptPassword);

        boolean save = this.save(user);
        if (!save) {
//            throw new RuntimeException("保存错误");
            return ResponseVo.error(ResponseEnum.ERROR);
        }
        return ResponseVo.success();
    }

    @Override
    public ResponseVo<User> login(String username, String password) {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userMapper.selectOne(queryWrapper);

        if (user == null) {
            // user 不存在
            return ResponseVo.error(ResponseEnum.USER_OR_PASSWORD_ERROR);
        }

        if (user.getPassword().equalsIgnoreCase(
                DigestUtils.md5DigestAsHex((SALT + user.getPassword()).getBytes()))) {
            return ResponseVo.error(ResponseEnum.USER_OR_PASSWORD_ERROR);
        }

        user.setPassword("");
        return ResponseVo.success(user);
    }
}




