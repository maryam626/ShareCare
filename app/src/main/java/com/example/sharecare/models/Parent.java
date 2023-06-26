package com.example.sharecare.models;

import java.util.ArrayList;

public class Parent extends User {

    private ArrayList<Kid> kids;

    public Parent(String username, String phoneNumber, String email, String address, String password, int numberOfKids, String maritalStatus, String gender, String language, String religion) {
        super(username, phoneNumber, email, address, password, numberOfKids, maritalStatus, gender, language, religion);
    }

    public Parent(String username, String phoneNumber, String email, String address, String password, int numberOfKids, String maritalStatus, String gender, String language, String religion, ArrayList<Kid> kids) {
        super(username, phoneNumber, email, address, password, numberOfKids, maritalStatus, gender, language, religion);
        this.kids = kids;
    }

    public ArrayList<Kid> getKids() {
        return kids;
    }

    public void setKids(ArrayList<Kid> kids) {
        this.kids = kids;
    }

    @Override
    public String toString() {
        return "Parent{" +
                "kids=" + kids +
                '}';
    }
}
