package com.funcxy.oj.models;

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

    public Message(String title, String content, MessageType type) {
        this.title = title;
        this.content = content;
        this.type = type;
    }

    public void markAsRead() {
        this.hasRead = true;
    }

    public void markAsUnread() {
        this.hasRead = false;
    }
}

