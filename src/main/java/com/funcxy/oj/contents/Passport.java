package com.funcxy.oj.contents;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * 传入的 Passport
 */
public class Passport implements RequestContent {
    /**
     * 用户名
     */
    @NotEmpty
    public String username;
    /**
     * 密码
     */
    @NotEmpty
    public String password;
    /**
     * 电子邮件
     */
    @NotEmpty(message = "email地址为空")
    @Email(message = "email地址无效！")
    public String email;
}
