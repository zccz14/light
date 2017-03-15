package com.funcxy.oj.contents;

/**
 * Created by wtupc96 on 2017/3/13.
 * 简化后的Group模型
 *
 * @author Peter
 * @version 1.0
 */
public class GroupHeader {
    private String groupName;
    private String groupId;

    public GroupHeader(String groupName, String groupId) {
        this.groupName = groupName;
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
