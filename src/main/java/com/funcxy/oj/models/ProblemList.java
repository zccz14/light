package com.funcxy.oj.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by chenyu on 2017/3/1.
 * <p>
 * Modified by wtupc96 on 2017/3/4.
 *
 * @author Peter
 * @version 1.0
 */
@Document(collection = "problemLists")
public class ProblemList {
    public static final String PATH = "F:/";

    @Id
    private String id;

    @Indexed
    private String creator;

    private List<String> problemIds = new ArrayList<>(0);

    @NotNull
    private boolean isAccessible = false;

    private List<String> userList = new ArrayList<>(0);

    @Indexed
    @NotNull
    private String title;

    @Indexed
    @NotNull
    private String type;
    private String coverUrl;
    private Date createdTime;
    private Date readBeginTime = null;
    private Date answerBeginTime = null;
    private Date answerEndTime = null;
    private Date readEndTime = null;
    private List<JudgeProblem> judgerList = new ArrayList<>(0);
    private List<String> submissionList = new ArrayList<>(0);
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

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public List<String> getUserList() {
        return userList;
    }

    public void setUserList(List<String> userList) {
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

    public List<String> getSubmissionList() {
        return submissionList;
    }

    public void setSubmissionList(List<String> submissionList) {
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

    public void setResultVisibleToSubmitterSelf(boolean resultVisibleToSubmitterSelf) {
        this.resultVisibleToSubmitterSelf = resultVisibleToSubmitterSelf;
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

    public List<String> getProblemIds() {
        return problemIds;
    }

    public void setProblemIds(List<String> problemIds) {
        this.problemIds = problemIds;
    }
}
