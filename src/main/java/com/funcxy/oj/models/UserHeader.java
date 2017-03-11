package com.funcxy.oj.models;

/**
 * 用户头部结构
 * 当用户被检索时返回的超简略数据类型
 */
public class UserHeader implements ResponseContent {
    private final String id;
    private final String username;
    private final String email;

    private UserHeader(String id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    private UserHeader(User user) {
        this(user.getId(), user.getUsername(), user.getEmail());
    }

    /**
     * 通过 User 实例创建 UserHeader
     *
     * @param user 用户实例
     * @return 用户实例的头部
     */
    public static UserHeader of(User user) {
        return new UserHeader(user);
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getId() {
        return id;
    }
}
