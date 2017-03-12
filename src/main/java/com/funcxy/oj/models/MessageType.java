package com.funcxy.oj.models;

/**
 * 消息类型枚举
 */
public enum MessageType {
    /**
     * 系统消息
     */
    SYSTEM,
    /**
     * 私信
     */
    PERSONAL,
    /**
     * 邀请
     */
    INVITATION,
    /**
     * 判题提醒
     */
    TOJUDGE,
    /**
     * 判决结果通知
     */
    SENTENCE,
    /**
     * 其他
     */
    OTHERS
}
