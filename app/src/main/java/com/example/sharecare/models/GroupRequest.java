package com.example.sharecare.models;


public class GroupRequest {

    private int id;
    private int userId;
    private int groupId;
    private String requestDate;
    private boolean accepted;

    public GroupRequest() {
        // Empty constructor required for Firestore
    }

    public GroupRequest(int id, int userId, int groupId, String requestDate, boolean accepted) {
        this.id = id;
        this.userId = userId;
        this.groupId = groupId;
        this.requestDate = requestDate;
        this.accepted = accepted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}
