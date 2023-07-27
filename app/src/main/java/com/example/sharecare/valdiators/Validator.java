package com.example.sharecare.valdiators;

import android.text.TextUtils;

public class Validator {
    public static boolean isValidUserId(int userId) {
        return userId >= 0;
    }

    public static boolean isValidGroupName(String groupName) {
        return !TextUtils.isEmpty(groupName) && groupName.length() <= 50;
    }

    public static boolean isValidDescription(String description) {
        return !TextUtils.isEmpty(description) && description.length() <= 200;
    }

    public static boolean isValidCity(String city) {
        return !TextUtils.isEmpty(city) && city.length() <= 100;
    }

    public static boolean isValidStreet(String street) {
        return !TextUtils.isEmpty(street) && street.length() <= 100;
    }
}