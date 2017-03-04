package com.funcxy.oj.models;

import org.hibernate.validator.constraints.URL;
import org.springframework.data.mongodb.core.index.Indexed;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Past;
import java.util.Date;

/**
 * Created by aak1247 on 2017/3/3.
 */
public class Profile {
    private String avatar;
    @URL
    private String personalUrl;
    @Indexed
    private String nickname;
    private String bio;
    @Max(3)
    @Min(0)
    private int gender;
    @Past
    private Date birthday;
    @Indexed
    private String location;
    public void setAvatar(String avatar){
        this.avatar = avatar;
    }
    public String getAvatar(){
        return this.avatar;
    }
    public void setPersonalUrl(String personalUrl){
        this.personalUrl = personalUrl;
    }
    public String getPersonalUrl(){
        return this.personalUrl;
    }
    public void setNickname(String nickname){
        this.nickname = nickname;
    }
    public String getNickname(){
        return this.nickname;
    }
    public void setBio(String bio){
        this.bio = bio;
    }
    public String getBio(){
        return this.bio;
    }
    public void setGender(int gender){
        this.gender = gender;
    }
    public int getGender(){
        return this.gender;
    }
    public void setBirthday(Date birthday){
        this.birthday = birthday;
    }
    public Date getBirthday(){
        return this.birthday;
    }
    public void setLocation(String location){
        this.location = location;
    }
    public String getLocation(){
        return this.location;
    }
}
