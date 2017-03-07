package com.funcxy.oj.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * Created by chenyu on 2017/3/1.
 *
 * Modified by wtupc96 on 2017/3/4
 */
@Document(collection = "problemLists")
public class ProblemList {
    @Id
    private ObjectId id;

    @Indexed
    @NotNull
    @DBRef(lazy = true)
    private User creator;

    private ObjectId[] problemIds;

    @NotNull
    private boolean isAccessible = false;

    private List<ObjectId> userList;

    @Indexed
    @NotNull
    private String title;

    @Indexed
    @NotNull
    private String type;

    private Date readBeginTime;

    private Date answerBeginTime;

    private Date answerEndTime;

    private Date readEndTime;

    private List<JudgeProblem> judgerList;

    private List<ObjectId> submissionList;

    @NotNull
    private boolean isAnonymous = false;

    @NotNull
    private boolean submitterVisibleToJudge = false;

    @NotNull
    private boolean resultVisibleToOthers = true;

    @NotNull
    private boolean resultVisibleToSubmitterSelf = true;

    @NotNull
    private boolean canBeCopied = true;



    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }


    public List<ObjectId> getUserList() {
        return userList;
    }

    public void setUserList(List<ObjectId> userList) {
        this.userList = userList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getReadBeginTime() {
        return readBeginTime;
    }

    public void setReadBeginTime(Date readBeginTime) {
        this.readBeginTime = readBeginTime;
    }

    public Date getAnswerBeginTime() {
        return answerBeginTime;
    }

    public void setAnswerBeginTime(Date answerBeginTime) {
        this.answerBeginTime = answerBeginTime;
    }

    public Date getAnswerEndTime() {
        return answerEndTime;
    }

    public void setAnswerEndTime(Date answerEndTime) {
        this.answerEndTime = answerEndTime;
    }

    public Date getReadEndTime() {
        return readEndTime;
    }

    public void setReadEndTime(Date readEndTime) {
        this.readEndTime = readEndTime;
    }

    public List<JudgeProblem> getJudgerList() {
        return judgerList;
    }

    public void setJudgerList(List<JudgeProblem> judgerList) {
        this.judgerList = judgerList;
    }

    public List<ObjectId> getSubmissionList() {
        return submissionList;
    }

    public void setSubmissionList(List<ObjectId> submissionList) {
        this.submissionList = submissionList;
    }

    public boolean isSubmitterVisibleToJudge() {
        return submitterVisibleToJudge;
    }

    public void setSubmitterVisibleToJudge(boolean submitterVisibleToJudge) {
        this.submitterVisibleToJudge = submitterVisibleToJudge;
    }

    public boolean isResultVisibleToOthers() {
        return resultVisibleToOthers;
    }

    public void setResultVisibleToOthers(boolean resultVisibleToOthers) {
        this.resultVisibleToOthers = resultVisibleToOthers;
    }

    public boolean isResultVisibleToSubmitterSelf() {
        return resultVisibleToSubmitterSelf;
    }

    public boolean isAccessible() {
        return isAccessible;
    }

    public void setAccessible(boolean accessible) {
        isAccessible = accessible;
    }

    public boolean isAnonymous() {
        return isAnonymous;
    }

    public void setAnonymous(boolean anonymous) {
        isAnonymous = anonymous;
    }

    public boolean isCanBeCopied() {
        return canBeCopied;
    }

    public void setCanBeCopied(boolean canBeCopied) {
        this.canBeCopied = canBeCopied;
    }

    public void setResultVisibleToSubmitterSelf(boolean resultVisibleToSubmitterSelf) {
        this.resultVisibleToSubmitterSelf = resultVisibleToSubmitterSelf;
    }

    public ObjectId[] getProblemIds() {
        return problemIds;
    }

    public void setProblemIds(ObjectId[] problemIds) {
        this.problemIds = problemIds;
    }
}
