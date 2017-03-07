package com.funcxy.oj.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.funcxy.oj.utils.UserUtil;
import com.sun.org.apache.xpath.internal.operations.Equals;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * User DOM
 * @author ddhee
 */
@Document(collection = "users")
public class User {
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    @Indexed(unique = true)
    @NotBlank(message = "用户名为空")
    private String username;
    @Indexed(unique = true)
    @NotEmpty(message = "Email地址为空")
    @Email(message = "Email地址无效")
    private String email;
    @NotEmpty(message = "密码为空")
    private String password;
    private Profile profile = new Profile();
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

    private String identify = "";
    public ObjectId getId() {
        return this.id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getUsername() {
        username.trim();
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        email.trim();
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = UserUtil.encrypt("SHA1", password);
    }

    public boolean passwordVerify(String password) {
        return this.password.equals(UserUtil.encrypt("SHA1", password));
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

    public void addProblemOwned(ObjectId problemId) {
        this.problemOwned.add(problemId);
    }

    public void deleteProblemOwned(ObjectId problemId) {
        this.problemOwned.remove(this.problemOwned.indexOf(problemId));
    }

    public void addProblemListOwned(ObjectId problemListId) {
        this.problemListOwned.add(problemListId);
    }

    public void deleteProblemListOwned(ObjectId problemListId) {
        this.problemListOwned.remove(this.problemListOwned.indexOf(problemListId));
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

    public void setProfile(Profile profile){
        this.profile = profile;
    }

    public Profile getProfile(){
        return this.profile;
    }

    @JsonIgnore
    public String getLocation(){
        return this.profile.getLocation();
    }
    @JsonIgnore
    public String getBio(){
        return this.profile.getBio();
    }
    @JsonIgnore
    public String getNickname(){
        return this.profile.getNickname();
    }

    @JsonIgnore
    public String getIdentify(){
        return this.identify;
    }
    public void setIdentify(String identify){
        this.identify = identify;
    }

    public boolean hasVerifiedEmail(){//是否已验证邮件
        return this.identify.equals(new String("verified"));
    }

    public void  verifyingEmail(){//设为已验证状态
        this.identify = new String("verified");
    }

    public void notVerified(){//设定随机字符串为验证字符串
        this.identify = UserUtil.getRandomCharAndNumr(20);//随机字符串长度为20位
    }
    public boolean toVerifyEmail(String verify){
        return this.identify.equals(verify);
    }
    public boolean equals(User user) {
        return this.id.equals(user.getId());
    }

}
