package com.funcxy.oj.models;

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
public class ProblemList extends Model {
    /**
     * 创建者 ID
     */
    @Indexed
    private String creator;
    /**
     * 题目列表
     */
    private List<String> problemIds = new ArrayList<>();
    /**
     * 是否公开
     */
    @Indexed
    @NotNull
    private boolean isPublic = false;
    /**
     * 参与用户列表
     */
    private List<String> userList = new ArrayList<>();
    /**
     * 题单标题
     */
    @Indexed
    @NotNull
    private String title;
    /**
     * 题单类型
     */
    @Indexed
    @NotNull
    private ProblemListType type = ProblemListType.DEFAULT;
    /**
     * 题单封面 URL
     */
    private String coverUrl;
    /**
     * 开始阅卷时间
     */
    private Date readBeginTime = null;
    /**
     * 开始答题时间
     */
    private Date answerBeginTime = null;
    /**
     * 结束答题时间
     */
    private Date answerEndTime = null;
    /**
     * 结束阅卷时间
     */
    private Date readEndTime = null;
    /**
     * 判题者列表
     */
    private List<String> judgerList = new ArrayList<>();
    /**
     * 提交列表
     */
    private List<String> submissionList = new ArrayList<>();
    /**
     * 是否允许匿名提交
     */
    private boolean isAnonymous = false;
    /**
     * 提交者是否对评测者可见
     */
    private boolean submitterVisibleToJudge = false;
    /**
     * 评测结果是否公开
     */
    private boolean resultVisibleToOthers = true;
    /**
     * 评测结果是否对提交者可见
     */
    private boolean resultVisibleToSubmitterSelf = true;
    /**
     * 题单是否可以被拷贝
     */
    private boolean canBeCopied = true;

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
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

    public ProblemListType getType() {
        return type;
    }

    public void setType(ProblemListType type) {
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

    public List<String> getJudgerList() {
        return judgerList;
    }

    public void setJudgerList(List<String> judgerList) {
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

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
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
