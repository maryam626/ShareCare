package com.example.sharecare.valdiators;

import java.util.Calendar;

public class Validator {

    public static boolean isGroupNameValid(String groupName) {
        return groupName.length() >= 2 && groupName.length() <= 20;
    }

    public static boolean isDescriptionValid(String description) {
        return description.length() >= 10 && description.length() <= 30;
    }

    public static boolean isStreetValid(String street) {
        return street.length() >= 5 && street.length() <= 20;
    }
    public static boolean isDateInFuture(Calendar selectedDateCalendar) {
        Calendar currentDate = Calendar.getInstance();
        return selectedDateCalendar.compareTo(currentDate) > 0;
    }
}
