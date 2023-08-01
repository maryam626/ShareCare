package com.example.sharecare.models;

import java.io.Serializable;

public class GroupDataDTO implements Serializable {

    private Group group;
    private boolean iamHost;

    public GroupDataDTO(Group group, boolean iamHost) {
        this.group = group;
        this.iamHost = iamHost;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public boolean isIamHost() {
        return iamHost;
    }

    public void setIamHost(boolean iamHost) {
        this.iamHost = iamHost;
    }
}
