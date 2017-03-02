package com.funcxy.oj.models;

import org.bson.types.ObjectId;

/**
 * Created by chenyu on 2017/3/1.
 */
public class JudgeProblem {
    private ObjectId problemId;
    private ObjectId judgeId;

    public ObjectId getProblemId() {
        return problemId;
    }

    public void setProblemId(ObjectId problemId) {
        this.problemId = problemId;
    }

    public ObjectId getJudgeId() {
        return judgeId;
    }

    public void setJudgeId(ObjectId judgeId) {
        this.judgeId = judgeId;
    }
}
