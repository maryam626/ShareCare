package com.example.sharecare.models;

import com.example.sharecare.models.User;

public class Parent extends User {

    public Parent(String username, String phoneNumber, String email, String address, String password, int numberOfKids, String maritalStatus, String gender, String language, String religion) {
        super(username, phoneNumber, email, address, password, numberOfKids, maritalStatus, gender, language, religion);
    }
}
