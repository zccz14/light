package com.funcxy.oj.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.funcxy.oj.utils.UserUtil;
import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import static com.funcxy.oj.utils.UserUtil.encrypt;

/**
 * User DOM
 * @author ddhee
 */
@Document(collection = "users")
public class User {
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    @Indexed
    @NotBlank
    private String username;
    @Indexed
    @Email
    private String email;
    @NotBlank
    private String password;
    private int gender;
/*
    0:not setted
    1:male
    2:female
    3:other
 */
    private Date birthday;
    private String location;
    private String personalUrl;
    private String avatar;
    private String profile;
    @JsonIgnore
    private List<ObjectId> problemOwned;
    @JsonIgnore
    private List<ObjectId> problemListOwned;
    @JsonIgnore
    private List<ObjectId> submissionHistory;
    @JsonIgnore
    private List<ObjectId> submissionUndecided;
    @JsonIgnore
    private List<ObjectId> problemLiked;
    @JsonIgnore
    private List<ObjectId> problemListLiked;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = encrypt("SHA1", password);
    }

    public boolean passwordVerify(String password) {
        return this.password.equals(encrypt("SHA1", password));
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public List<ObjectId> getProblemOwned() {
        return problemOwned;
    }

    public void setProblemOwned(List<ObjectId> problemOwned) {
        this.problemOwned = problemOwned;
    }

    public List<ObjectId> getProblemListOwned() {
        return problemListOwned;
    }

    public void setProblemListOwned(List<ObjectId> problemListOwned) {
        this.problemListOwned = problemListOwned;
    }

    public List<ObjectId> getSubmissionHistory() {
        return submissionHistory;
    }

    public void setSubmissionHistory(List<ObjectId> submissionHistory) {
        this.submissionHistory = submissionHistory;
    }

    public List<ObjectId> getSubmissionUndecided() {
        return submissionUndecided;
    }

    public void setSubmissionUndecided(List<ObjectId> submissionUndecided) {
        this.submissionUndecided = submissionUndecided;
    }

    public List<ObjectId> getProblemLiked() {
        return problemLiked;
    }

    public void setProblemLiked(List<ObjectId> problemLiked) {
        this.problemLiked = problemLiked;
    }

    public List<ObjectId> getProblemListLiked() {
        return problemListLiked;
    }

    public void setProblemListLiked(List<ObjectId> problemListLiked) {
        this.problemListLiked = problemListLiked;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPersonalUrl() {
        return personalUrl;
    }

    public void setPersonalUrl(String personalUrl) {
        this.personalUrl = personalUrl;
    }
}
