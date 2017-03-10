package com.funcxy.oj.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.funcxy.oj.utils.UserUtil;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
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
    private List<ObjectId> groupIn = new ArrayList<>(0);
    @JsonIgnore
    private List<ObjectId> problemOwned = new ArrayList<>(0);
    @JsonIgnore
    private List<ObjectId> problemListOwned = new ArrayList<>(0);
    @JsonIgnore
    private List<ObjectId> submissionHistory = new ArrayList<>(0);
    @JsonIgnore
    private List<ObjectId> submissionUndecided = new ArrayList<>(0);
    @JsonIgnore
    private List<ObjectId> problemLiked = new ArrayList<>(0);
    @JsonIgnore
    private List<ObjectId> problemListLiked = new ArrayList<>(0);
    @JsonIgnore
    private String identifyString = "";

    private boolean hasVerified = false;

    private List<Message> messages;
    @JsonIgnore
    private List<ObjectId> problemListForked = new ArrayList<>(0);
    @JsonIgnore
    private List<ObjectId> invitation = new ArrayList<>(0);

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

    public void setProblemListOwned(List<ObjectId> problemListOwned) {
        this.problemListOwned = problemListOwned;
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

    public void addProblemLiked(ObjectId problemId) {
        this.problemLiked.add(problemId);
    }

    public void deleteProblemLiked(ObjectId problemId) {
        this.problemLiked.remove(this.problemLiked.indexOf(problemId));
    }

    public List<ObjectId> getProblemListLiked() {
        return problemListLiked;
    }

    public void setProblemListLiked(List<ObjectId> problemListLiked) {
        this.problemListLiked = problemListLiked;
    }

    public void addProblemListLiked(ObjectId problemId) {
        this.problemListLiked.add(problemId);
    }

    public void deleteProblemListLiked(ObjectId problemId) {
        this.problemListLiked.remove(this.problemListLiked.indexOf(problemId));
    }

    public void addSubmissionHistory(ObjectId submissionId) {
        this.submissionHistory.add(submissionId);
    }

    public void addSubmissionUndicided(ObjectId submissionId) {
        this.submissionUndecided.add(submissionId);
    }

    public void deleteSubmissionUndicided(ObjectId submissionId) {
        this.submissionUndecided.remove(submissionUndecided.indexOf(submissionId));
    }

    public Profile getProfile(){
        return this.profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
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
    public String getIdentifyString() {
        return this.identifyString;
    }

    public boolean hasVerifiedEmail(){//是否已验证邮件
        return this.hasVerified;
    }

    public void  verifyingEmail(){//设为已验证状态
        this.identifyString = "verified";
        this.hasVerified = true;
    }

    public void notVerified(){//设定随机字符串为验证字符串
        this.identifyString = UserUtil.getRandomCharAndNumr(20);//随机字符串长度为20位
    }

    public boolean toVerifyEmail(String verify){
        return this.identifyString.equals(verify);
    }

    public boolean getHasVerified() {
        return this.hasVerified;
    }

    public boolean equals(User user) {
        return this.id.equals(user.getId());
    }

    public void findPassword(){
        String password = UserUtil.getRandomCharAndNumr(10);
        UserUtil.sendFindPasswordEmail(this.email,password);
        this.setPassword(password);
    }

    public List<ObjectId> getGroupIn(){
        return this.groupIn;
    }

    public void setGroupIn(List<ObjectId> groupIn) {
        this.groupIn = groupIn;
    }

    public void addGroupIn(ObjectId group){
        this.groupIn.add(group);
    }

    public void deleteGroupIn(ObjectId group){
        this.groupIn.remove(this.groupIn.indexOf(group));
    }

    public List<Message> getMessages() {
        return this.messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void addMessage(Message message) {
        this.messages.add(message);
    }

    public void deleteMessage(Message message) {
        this.messages.remove(message);
    }

    public List<ObjectId> getProblemListForked() {
        return this.problemListForked;
    }

    public void setProblemListForked(List<ObjectId> problemListForked) {
        this.problemListForked = problemListForked;
    }

    public void addProblemListForked(ObjectId objectId) {
        this.problemListForked.add(objectId);
    }

    public void deleteProblemListForked(ObjectId objectId) {
        this.problemListForked.remove(objectId);
    }

    public List<ObjectId> getInvitation() {
        return this.invitation;
    }

    public void setInvitation(List<ObjectId> invitation) {
        this.invitation = invitation;
    }
}
