package com.funcxy.oj.models;

/**
 * Created by chenyu on 2017/3/1.
 */
public class JudgeProblem {
    /**
     * 题目 ID
     */
    private String problemId;
    /**
     * 评测者 ID
     */
    private String judgeId;

    public String getProblemId() {
        return problemId;
    }

    public void setProblemId(String problemId) {
        this.problemId = problemId;
    }

    public String getJudgeId() {
        return judgeId;
    }

    public void setJudgeId(String judgeId) {
        this.judgeId = judgeId;
    }
}
