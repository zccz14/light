package com.funcxy.oj.models;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * 传入的 Passport
 */
public class Passport implements RequestContent {
    @NotEmpty
    public String username;
    @NotEmpty
    public String password;
    @NotEmpty(message = "email地址为空")
    @Email(message = "email地址无效！")
    public String email;
}
