package com.funcxy.light.contents;

import com.funcxy.light.models.Message;

/**
 * Created by wtupc96 on 2017/3/14.
 *
 * @author Peter
 * @version 1.0
 */

public class BindingProblemLists {
    /**
     * 绑定的题单请求的Message
     *
     * @see Message
     */
    private Message message;

    /**
     * 想要进行PR的题单
     */
    private String sourceProblemListId;

    /**
     * 目标题单
     */
    private String targetProblemListId;

    /**
     * 构造函数
     *
     * @param message             绑定题单请求的Message
     * @param sourceProblemListId 想要PR的题单
     * @param targetProblemListId 目标题单
     */
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

    /**
     * 重载该类的Equals方法：
     * 若进行两个BindingProblemList的比较，则当源和目标都一致时，认为是同一个请求
     * 若进行BindingProblemList和一个Message的比较，则只比较Message是否相同
     *
     * @param obj
     * @return
     */
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
