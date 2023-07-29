package com.example.sharecare.models;

public class PendingActivityRequestDTO {
    private int userId;
    private String username;
    private int activityId;
    private int isAccept;
    private String requestDate;
    private String activityName;

    // Constructors
    public PendingActivityRequestDTO() {
    }

    public PendingActivityRequestDTO(int userId, String username, int activityId, int isAccept, String requestDate, String activityName) {
        this.userId = userId;
        this.username = username;
        this.activityId = activityId;
        this.isAccept = isAccept;
        this.requestDate = requestDate;
        this.activityName = activityName;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public int getIsAccept() {
        return isAccept;
    }

    public void setIsAccept(int isAccept) {
        this.isAccept = isAccept;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    // Override the toString() method for debugging purposes
    @Override
    public String toString() {
        return "PendingActivityRequestDTO{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", activityId=" + activityId +
                ", isAccept=" + isAccept +
                ", requestDate='" + requestDate + '\'' +
                ", activityName='" + activityName + '\'' +
                '}';
    }
}
