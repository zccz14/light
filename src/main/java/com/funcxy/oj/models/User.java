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
    private ObjectId userId;
    @Indexed(unique = true)
    @NotBlank
    private String username;
    @Indexed(unique = true)
    @Email
    private String email;
    @NotEmpty
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

    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
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
        return profile.getAvatar();
    }

    public void setAvatar(String avatar) {
        profile.setAvatar(avatar);
    }

    public int getGender() {
        return profile.getGender();
    }

    public void setGender(int gender) {
        profile.setGender(gender);
    }

    public Date getBirthday() {
        return profile.getBirthday();
    }

    public void setBirthday(Date birthday) {
        profile.setBirthday(birthday);
    }

    public String getLocation() {
        return profile.getLocation();
    }

    public void setLocation(String location) {
        profile.setLocation(location);
    }

    public String getPersonalUrl() {
        return profile.getPersonalUrl();
    }

    public void setPersonalUrl(String personalUrl) {
        profile.setPersonalUrl(personalUrl);
    }
    public void setNickname(String nickname){
        profile.setNickname(nickname);
    }
    public String getNickname(){
        return profile.getNickname();
    }
    public String getBio() {
        return profile.getBio();
    }

    public void setBio(String Bio) {
        profile.setBio(Bio);
    }
    public void setProfile(Profile profile){
        this.profile = profile;
    }
    public Profile getProfile(){
        return this.profile;
    }
}
