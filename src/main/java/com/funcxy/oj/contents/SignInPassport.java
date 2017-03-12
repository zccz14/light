package com.funcxy.oj.contents;

/**
 * 登录时使用的凭证结构
 */
public class SignInPassport implements RequestContent {
    public final String username;
    public final String password;

    public SignInPassport(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
