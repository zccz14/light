package com.funcxy.oj.models;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author aak1247 on 2017/3/4.
 */
@Document(collection = "groups")
public class Group extends Model {
    /**
     * 所有者 ID
     */
    @Indexed
    @NotNull
    private String ownerId;
    /**
     * 组名
     */
    @Indexed(unique = true)
    @NotBlank(message = "组名为空")
    private String groupName;
    /**
     * 成员列表
     */
    private List<String> memberId;
    /**
     * 想加入的成员列表
     */
    private List<String> joiningMemberId;
    /**
     * 邀请加入的成员列表
     */
    private List<String> invitedMemberId;
    /**
     * 组拥有的题单列表
     */
    private List<String> ownedProblemList;
    /**
     * 组类型
     */
    private GroupType type;

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String name) {
        this.groupName = name;
    }

    public List<String> getMemberId() {
        return memberId;
    }

    public void setMemberId(List<String> memberId) {
        this.memberId = memberId;
    }

    public List<String> getJoiningMemberId() {
        return joiningMemberId;
    }

    public void setJoiningMemberId(List<String> joiningMemberId) {
        this.joiningMemberId = joiningMemberId;
    }

    public List<String> getOwnedProblemList() {
        return ownedProblemList;
    }

    public void setOwnedProblemList(List<String> ownedProblemList) {
        this.ownedProblemList = ownedProblemList;
    }

    public GroupType getType() {
        return type;
    }

    public void setType(GroupType type) {
        this.type = type;
    }

    public void addMember(String memberId) {
        this.joiningMemberId.remove(joiningMemberId.indexOf(memberId));
        this.memberId.add(memberId);
    }

    public void askJoin(String joinId) {
        this.joiningMemberId.add(joinId);
    }

    public void addProblemListOwned(String problemListId) {
        this.ownedProblemList.add(problemListId);
    }

    public void deleteProblemListOwned(String problemListId) {
        this.ownedProblemList.remove(this.ownedProblemList.indexOf(problemListId));
    }

    public List<String> getInvitedMemberId() {
        return this.invitedMemberId;
    }

    public void setInvitedMemberId(List<String> list) {
        this.invitedMemberId = list;
    }

    public void inviteMember(String user) {
        this.invitedMemberId.add(user);
    }

    public void admit(String user) {
        joiningMemberId.remove(user);
        invitedMemberId.remove(user);
        if (!memberId.contains(user)) memberId.add(user);
    }

    public void refuse(String user) {
        joiningMemberId.remove(user);
    }
}
