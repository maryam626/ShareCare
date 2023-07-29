package com.example.sharecare.models;

public class GroupDataDTO {
    private int groupId;
    private String groupName;
    private boolean iamHost;

    public GroupDataDTO(int groupId, String groupName, boolean iamHost) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.iamHost = iamHost;
    }

    public int getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public boolean isIamHost() {
        return iamHost;
    }
}
