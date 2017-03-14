package com.funcxy.oj.contents;

/**
 * Created by wtupc96 on 2017/3/14.
 *
 * @author Peter
 * @version 1.0
 */
public class BindingProblemLists {
    private String sourceProblemListId;
    private String targetProblemListId;

    public BindingProblemLists(String sourceProblemListId, String targetProblemListId) {
        this.sourceProblemListId = sourceProblemListId;
        this.targetProblemListId = targetProblemListId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() == BindingProblemLists.class) {
            BindingProblemLists temp = (BindingProblemLists) obj;
            return temp.getSourceProblemListId().equals(this.getSourceProblemListId())
                    && temp.getTargetProblemListId().equals(this.getTargetProblemListId());
        } else
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
