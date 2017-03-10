package com.funcxy.oj.models;

/**
 * @author aak1247 on 2017/3/10.
 */
public class Message {
    public String title;
    public String content;
    public int type = 3;
    public boolean hasRead = false;

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

