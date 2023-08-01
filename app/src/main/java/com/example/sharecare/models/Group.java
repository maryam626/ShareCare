package com.example.sharecare.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Group implements Serializable {
    private int id;
    private String groupName;
    private String briefInformation;
    private Host host;
    private ArrayList<Parent> participants;
    private ArrayList<Activity> activities;
    private int capacity;
    private int ageFrom;
    private int ageTo;
    private String city;
    private String street;



    private String language;
    private String religion;

    public Group(String groupName, String briefInformation, Host host, ArrayList<Parent> participants, ArrayList<Activity> activities, int capacity, int ageFrom, int ageTo, String city, String street) {
        this.groupName = groupName;
        this.briefInformation = briefInformation;
        this.host = host;
        this.participants = participants;
        this.activities = activities;
        this.capacity = capacity;
        this.ageFrom = ageFrom;
        this.ageTo = ageTo;
        this.city = city;
        this.street = street;
    }
    public Group(String groupName, String briefInformation, Host host,String city, String street,String language, String religion) {
        this.groupName = groupName;
        this.briefInformation = briefInformation;
        this.host = host;
        this.city = city;
        this.street = street;
        this.language = language;
        this.religion = religion;
    }

    public Group(int groupId, String groupName, String description, String city, String street) {
        this.groupName = groupName;
        this.briefInformation = description;
        this.city = city;
        this.street = street;
    }

    public Group(int groupId, String groupName, String description, String city, String street, String language, String religion) {
        this.id = groupId;
        this.groupName = groupName;
        this.briefInformation = description;
        this.city = city;
        this.street = street;
        this.language = language;
        this.religion= religion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getBriefInformation() {
        return briefInformation;
    }

    public void setBriefInformation(String briefInformation) {
        this.briefInformation = briefInformation;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public ArrayList<Parent> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<Parent> participants) {
        this.participants = participants;
    }

    public ArrayList<Activity> getActivities() {
        return activities;
    }

    public void setActivities(ArrayList<Activity> activities) {
        this.activities = activities;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getAgeFrom() {
        return ageFrom;
    }

    public void setAgeFrom(int ageFrom) {
        this.ageFrom = ageFrom;
    }

    public int getAgeTo() {
        return ageTo;
    }

    public void setAgeTo(int ageTo) {
        this.ageTo = ageTo;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public String getLanguage(){return language;}
    public String getReligion(){return religion;}
    public void setStreet(String street) {
        this.street = street;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    @Override
    public String toString() {
        return "Group{" +
                "groupName='" + groupName + '\'' +
                ", briefInformation='" + briefInformation + '\'' +
                ", host=" + host +
                ", participants=" + participants +
                ", activities=" + activities +
                ", capacity=" + capacity +
                ", ageFrom=" + ageFrom +
                ", ageTo=" + ageTo +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                '}';
    }
}
