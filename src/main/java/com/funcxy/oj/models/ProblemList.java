package com.funcxy.oj.models;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * Created by chenyu on 2017/3/1.
 */
@Document
public class ProblemList {
    private ObjectId id;
    private ObjectId creator;
    private boolean Accessibility;
    private List<ObjectId> userList;
    private String title;
    private String type;
    private Date readBeginTime;
    private Date answerBeginTime;
    private Date answerEndTime;
    private Date readEndTime;
    private List<JudgeProblem> judgeProblemList;
    private List<ObjectId> submissionList;
    private boolean anonymous;
    private boolean submitterVisibleToJudge;
    private boolean resultVisibleToOthers;
    private boolean resultVisibleToSubmitterSelf;
    private boolean makeCopy;

    public void addUser(ObjectId userId) {
        userList.add(userId);
    }
}
