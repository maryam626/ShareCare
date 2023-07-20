package com.example.sharecare.models;

public class Activity {

    private int id;
    private String activityName;
    private String selectedActivity;
    private String selectedDate;
    private String selectedTime;
    private int capacity;
    private int ageFrom;
    private int ageTo;
    private int ownerUserId;
    private int groupId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public void setSelectedActivity(String selectedActivity) {
        this.selectedActivity = selectedActivity;
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }

    public void setSelectedTime(String selectedTime) {
        this.selectedTime = selectedTime;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setAgeFrom(int ageFrom) {
        this.ageFrom = ageFrom;
    }

    public void setAgeTo(int ageTo) {
        this.ageTo = ageTo;
    }

    public int getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(int ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }


    public Activity() {
        // Empty constructor needed for Firebase
    }

    public Activity(String activityName, String selectedActivity, String selectedDate, String selectedTime,
                    int capacity, int ageFrom, int ageTo,int groupId,int ownerUserId) {
        this.activityName = activityName;
        this.selectedActivity = selectedActivity;
        this.selectedDate = selectedDate;
        this.selectedTime = selectedTime;
        this.capacity = capacity;
        this.ageFrom = ageFrom;
        this.ageTo = ageTo;
        this.groupId = groupId;
        this.ownerUserId = ownerUserId;
    }

    // Getter methods

    public String getActivityName() {
        return activityName;
    }

    public String getSelectedActivity() {
        return selectedActivity;
    }

    public String getSelectedDate() {
        return selectedDate;
    }

    public String getSelectedTime() {
        return selectedTime;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getAgeFrom() {
        return ageFrom;
    }

    public int getAgeTo() {
        return ageTo;
    }
}
