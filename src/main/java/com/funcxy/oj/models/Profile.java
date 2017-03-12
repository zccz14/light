package com.funcxy.oj.models;

import org.hibernate.validator.constraints.URL;
import org.springframework.data.mongodb.core.index.Indexed;

import javax.validation.constraints.Past;
import java.util.Date;

/**
 * @author aak1247 on 2017/3/3.
 */
public class Profile {
    /**
     * 头像 URL
     */
    private String avatar;
    /**
     * 个人 URL
     */
    @URL
    private String personalUrl;
    /**
     * 昵称
     */
    @Indexed
    private String nickname;
    /**
     * 格言
     */
    private String bio;
    /**
     * 性别
     */
    private Gender gender = Gender.UNKNOWN;
    /**
     * 生日
     */
    @Past
    private Date birthday;
    /**
     * 地址
     */
    @Indexed
    private String location;

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPersonalUrl() {
        return this.personalUrl;
    }

    public void setPersonalUrl(String personalUrl) {
        this.personalUrl = personalUrl;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBio() {
        return this.bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Date getBirthday() {
        return this.birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
