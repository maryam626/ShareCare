package com.example.sharecare.valdiators;

import android.text.TextUtils;

public class CreateGroupValidator {

    /**
     * Validate the group name.
     * @param groupName The group name to validate.
     * @return True if the group name is valid, false otherwise.
     */
    public static boolean isGroupNameValid(String groupName) {
        return groupName.length() >= 2 && groupName.length() <= 20;
    }

    /**
     * Validate the group description.
     * @param description The group description to validate.
     * @return True if the description is valid, false otherwise.
     */
    public static boolean isDescriptionValid(String description) {
        return description.length() >= 10 && description.length() <= 30;
    }

    /**
     * Validate the street address.
     * @param street The street address to validate.
     * @return True if the street address is valid, false otherwise.
     */
    public static boolean isStreetValid(String street) {
        return street.length() >= 5 && street.length() <= 20;
    }

    /**
     * Validate the city.
     * @param city The city to validate.
     * @return True if the street city is valid, false otherwise.
     */
    public static boolean isValidCity(String city) {
        return !TextUtils.isEmpty(city) && city.length() <= 15;
    }
}
