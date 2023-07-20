package com.example.sharecare.models;

import com.example.sharecare.models.Group;
import com.example.sharecare.models.User;

import java.sql.Date;

public class Request {

    private int id;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setGroup(Group group) {
        this.group = group;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }
}
