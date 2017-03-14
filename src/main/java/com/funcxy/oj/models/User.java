package com.funcxy.oj.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.funcxy.oj.contents.Passport;
import com.funcxy.oj.utils.UserUtil;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * User 模型
 *
 * @author ddhee aak1247 zccz14
 */
@Document(collection = "users")
public class User extends Model {
    /**
     * 用户名
     */
    @Indexed(unique = true)
    @NotBlank(message = "用户名为空")
    private String username;
    /**
     * Email
     */
    @Indexed(unique = true)
    @NotEmpty(message = "Email地址为空")
    @Email(message = "Email地址无效")
    private String email;
    /**
     * 密码
     */
    @NotEmpty(message = "密码为空")
    private String password;
    /**
     * 个人资料
     */
    private Profile profile = new Profile();
    /**
     * 所属用户组列表
     */
    private List<String> groupIn = new ArrayList<>(0);
    /**
     * 创建的题目列表
     */
    private List<String> problemOwned = new ArrayList<>(0);
    /**
     * 创建的题单列表
     */
    private List<String> problemListOwned = new ArrayList<>(0);
    /**
     * 提交历史记录
     */
    private List<String> submissionHistory = new ArrayList<>(0);
    /**
     * 待评提交列表
     */
    private List<String> submissionUndecided = new ArrayList<>(0);
    /**
     * 收藏题目列表
     */
    private List<String> problemLiked = new ArrayList<>(0);
    /**
     * 收藏题单列表
     */
    private List<String> problemListLiked = new ArrayList<>(0);
    /**
     * 验证码
     */
    private String identifyString = "";
    /**
     * 是否已验证
     */
    private boolean hasVerified = false;
    /**
     * 消息列表
     */
    private List<Message> messages = new ArrayList<>(0);
    /**
     * 复制的题单列表
     * problemListId
     */
    private List<String> problemListForked = new ArrayList<>(0);
    /**
     * 收到的邀请列表
     * groupId
     */
    private List<String> invitation = new ArrayList<>(0);
    /**
     * 评测转发规则列表
     */
    private List<Proxy> proxies = new ArrayList<>(0);

    public User() {
        super();
    }

    public User(Passport passport) {
        super();
        setUsername(passport.username);
        setPassword(passport.password);
        setEmail(passport.email);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.trim();
    }

    public void setPassword(String password) {
        this.password = UserUtil.encrypt("SHA1", password);
    }

    public boolean passwordVerify(String password) {
        return this.password.equals(UserUtil.encrypt("SHA1", password));
    }

    public List<String> getProblemOwned() {
        return problemOwned;
    }

    public void setProblemOwned(List<String> problemOwned) {
        this.problemOwned = problemOwned;
    }

    public List<String> getProblemListOwned() {
        return problemListOwned;
    }

    public void setProblemListOwned(List<String> problemListOwned) {
        this.problemListOwned = problemListOwned;
    }

    public void addProblemOwned(String problemId) {
        this.problemOwned.add(problemId);
    }

    public void deleteProblemOwned(String problemId) {
        this.problemOwned.remove(this.problemOwned.indexOf(problemId));
    }

    public void addProblemListOwned(String problemListId) {
        this.problemListOwned.add(problemListId);
    }

    public void deleteProblemListOwned(String problemListId) {
        this.problemListOwned.remove(this.problemListOwned.indexOf(problemListId));
    }

    public List<String> getSubmissionHistory() {
        return submissionHistory;
    }

    public void setSubmissionHistory(List<String> submissionHistory) {
        this.submissionHistory = submissionHistory;
    }

    public List<String> getSubmissionUndecided() {
        return submissionUndecided;
    }

    public void setSubmissionUndecided(List<String> submissionUndecided) {
        this.submissionUndecided = submissionUndecided;
    }

    public List<String> getProblemLiked() {
        return problemLiked;
    }

    public void setProblemLiked(List<String> problemLiked) {
        this.problemLiked = problemLiked;
    }

    public void addProblemLiked(String problemId) {
        this.problemLiked.add(problemId);
    }

    public void deleteProblemLiked(String problemId) {
        this.problemLiked.remove(this.problemLiked.indexOf(problemId));
    }

    public List<String> getProblemListLiked() {
        return problemListLiked;
    }

    public void setProblemListLiked(List<String> problemListLiked) {
        this.problemListLiked = problemListLiked;
    }

    public void addProblemListLiked(String problemId) {
        this.problemListLiked.add(problemId);
    }

    public void deleteProblemListLiked(String problemId) {
        this.problemListLiked.remove(this.problemListLiked.indexOf(problemId));
    }

    public void addSubmissionHistory(String submissionId) {
        this.submissionHistory.add(submissionId);
    }

    public void addSubmissionUndicided(String submissionId) {
        this.submissionUndecided.add(submissionId);
    }

    public void deleteSubmissionUndicided(String submissionId) {
        this.submissionUndecided.remove(submissionUndecided.indexOf(submissionId));
    }

    public Profile getProfile() {
        return this.profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    @JsonIgnore
    public String getIdentifyString() {
        return this.identifyString;
    }

    public boolean hasVerifiedEmail() {//是否已验证邮件
        return this.hasVerified;
    }

    public void verifyingEmail() {//设为已验证状态
        this.identifyString = "verified";
        this.hasVerified = true;
    }

    public void notVerified() {//设定随机字符串为验证字符串
        this.identifyString = UserUtil.getRandomCharAndNumr(20);//随机字符串长度为20位
    }

    public boolean toVerifyEmail(String verify) {
        return this.identifyString.equals(verify);
    }

    public boolean getHasVerified() {
        return this.hasVerified;
    }

    public boolean equals(User user) {
        return this.getId().equals(user.getId());
    }

    public void findPassword() {
        String password = UserUtil.getRandomCharAndNumr(10);
        UserUtil.sendFindPasswordEmail(this.email, password);
        this.setPassword(password);
    }

    public List<String> getGroupIn() {
        return this.groupIn;
    }

    public void setGroupIn(List<String> groupIn) {
        this.groupIn = groupIn;
    }

    public void addGroupIn(String group) {
        this.groupIn.add(group);
    }

    public void deleteGroupIn(String group) {
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

    public List<String> getProblemListForked() {
        return this.problemListForked;
    }

    public void setProblemListForked(List<String> problemListForked) {
        this.problemListForked = problemListForked;
    }

    public void addProblemListForked(String objectId) {
        this.problemListForked.add(objectId);
    }

    public void deleteProblemListForked(String objectId) {
        this.problemListForked.remove(objectId);
    }

    public List<String> getInvitation() {
        return this.invitation;
    }

    public void setInvitation(List<String> invitation) {
        this.invitation = invitation;
    }

    public List<Proxy> getProxies() {
        return proxies;
    }

    public void setProxies(List<Proxy> proxies) {
        this.proxies = proxies;
    }

}
