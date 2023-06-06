package com.example.sharecare.Logic;

import com.example.sharecare.models.User;

import java.sql.Date;

public class Request {
    private Group group;
    private User user;
    private Date requestDate;
    private Boolean approved;

    public Request(Group group, User user, Date requestDate, Boolean approved) {
        this.group = group;
        this.user = user;
        this.requestDate = requestDate;
        this.approved = approved;
    }

    public Group getGroup() {
        return group;
    }

    public User getUser() {
        return user;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public Boolean getApproved() {
        return approved;
    }
}
