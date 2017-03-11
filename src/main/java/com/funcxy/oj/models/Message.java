package com.funcxy.oj.models;

/**
 * @author aak1247 on 2017/3/10.
 */
public class Message {
    private String title;
    private String content;
    private int type = 3;
    private boolean hasRead = false;

    /**
     * @param type 0 for system message
     *             1 for personal message
     *             2 for invitation
     *             3 for others
     */
    public Message(String title, String content, int type) {
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

