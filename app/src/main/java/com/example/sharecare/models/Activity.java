package com.example.sharecare.models;

public class Activity {
    private String activityName;
    private String selectedActivity;
    private String selectedDate;
    private String selectedTime;
    private int capacity;
    private int ageFrom;
    private int ageTo;

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    private int groupId;

    public Activity() {
        // Empty constructor needed for Firebase
    }

    public Activity(String activityName, String selectedActivity, String selectedDate, String selectedTime,
                    int capacity, int ageFrom, int ageTo,int groupId) {
        this.activityName = activityName;
        this.selectedActivity = selectedActivity;
        this.selectedDate = selectedDate;
        this.selectedTime = selectedTime;
        this.capacity = capacity;
        this.ageFrom = ageFrom;
        this.ageTo = ageTo;
        this.groupId = groupId;
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
