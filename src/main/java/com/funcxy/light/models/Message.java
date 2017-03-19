package com.funcxy.light.models;

/**
 * 消息模型
 *
 * @author aak1247
 */
public class Message {
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 类型
     */
    private MessageType type = MessageType.OTHERS;
    /**
     * 是否已阅
     */
    private boolean hasRead = false;

    /**
     * 用户不可见的附属信息
     */
    private String additionalInformation;

    public Message(String title, String content, MessageType type, String additionalInformation) {
        this.title = title;
        this.content = content;
        this.type = type;
        this.additionalInformation = additionalInformation;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public boolean isHasRead() {
        return hasRead;
    }

    public void setHasRead(boolean hasRead) {
        this.hasRead = hasRead;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public void markAsRead() {
        this.hasRead = true;
    }

    public void markAsUnread() {
        this.hasRead = false;
    }
}

