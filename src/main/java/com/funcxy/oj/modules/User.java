package com.funcxy.oj.modules;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import javafx.util.StringConverter;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.parsing.Problem;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Created by DDHEE on 2017/2/28.
 */
@Document
public class User {
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    @Indexed
    private String username;
    @Indexed
    private String email;
    @JsonIgnore
    private String password;



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
        try{
            MessageDigest pwd = MessageDigest.getInstance("SHA1");
            pwd.update(password.getBytes());
            password = pwd.digest().toString();
        }catch (NoSuchAlgorithmException e){
            System.out.println("No such algorithm as SHA1");
        }
        this.password = password;
    }

    public boolean passwordVerify(String password) {
        try{
            MessageDigest pwd = MessageDigest.getInstance("SHA1");
            pwd.update(password.getBytes());
            password = pwd.digest().toString();
        }catch (NoSuchAlgorithmException e){
            System.out.println("No such algorithm as SHA1");
        }
        return this.password.equals(password);
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

    public void addProblemOwned(List<ObjectId> problemOwned) {


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
}
