package com.funcxy.oj.contents;

import com.funcxy.oj.models.Message;

/**
 * Created by wtupc96 on 2017/3/14.
 *
 * @author Peter
 * @version 1.0
 */
public class BindingProblemLists {
    private Message message;
    private String sourceProblemListId;
    private String targetProblemListId;

    public BindingProblemLists(Message message, String sourceProblemListId, String targetProblemListId) {
        this.message = message;
        this.sourceProblemListId = sourceProblemListId;
        this.targetProblemListId = targetProblemListId;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() == BindingProblemLists.class) {
            BindingProblemLists temp = (BindingProblemLists) obj;
            return temp.getSourceProblemListId().equals(this.getSourceProblemListId())
                    && temp.getTargetProblemListId().equals(this.getTargetProblemListId());
        }

        if (obj.getClass() == Message.class) {
            Message temp = (Message) obj;
            return temp == this.getMessage();
        }
        return super.equals(obj);
    }

    public String getSourceProblemListId() {
        return sourceProblemListId;
    }

    public void setSourceProblemListId(String sourceProblemListId) {
        this.sourceProblemListId = sourceProblemListId;
    }

    public String getTargetProblemListId() {
        return targetProblemListId;
    }

    public void setTargetProblemListId(String targetProblemListId) {
        this.targetProblemListId = targetProblemListId;
    }
}
