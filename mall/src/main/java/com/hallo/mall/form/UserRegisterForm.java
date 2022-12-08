package com.hallo.mall.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author hallo
 * @datetime 2022-10-10 20:51
 * @description
 */
@Data
public class UserRegisterForm {

//    @NotBlank // 用户 String 判断空格
//    @NotEmpty // 用于集合
//    @NotNull
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String email;

    public UserRegisterForm() {
    }

    public UserRegisterForm(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
