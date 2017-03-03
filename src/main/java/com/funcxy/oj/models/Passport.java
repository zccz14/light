package com.funcxy.oj.models;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by zccz14 on 2017/3/2.
 */
public class Passport {
    @NotEmpty
    public String username;
    @NotEmpty
    public String password;
    @NotEmpty(message = "email地址为空")
    @Email(message = "email地址无效！")
    public String email;
}
