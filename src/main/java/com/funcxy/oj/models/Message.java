package com.funcxy.oj.models;

/**
 * 消息模型
 *
 * @author aak1247
 */
public class Message {
    private String title;
    private String content;
    private MessageType type = MessageType.OTHERS;
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

