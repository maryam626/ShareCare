package com.example.sharecare.valdiators;
import android.text.TextUtils;

public class CreateActivityValidator {

    public static boolean isActivityNameValid(String activityName) {
        return !TextUtils.isEmpty(activityName) && activityName.length() >= 2 && activityName.length() <= 20;
    }

    public static boolean isDateValid(String date) {
        return !TextUtils.isEmpty(date);
    }

    public static boolean isTimeValid(String time) {
        return !TextUtils.isEmpty(time);
    }

    public static boolean isCapacityValid(String capacity) {
        try {
            int capacityValue = Integer.parseInt(capacity);
            return capacityValue >= 1 && capacityValue <= 100;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isAgeRangeValid(String ageFrom, String ageTo) {
        try {
            int ageFromValue = Integer.parseInt(ageFrom);
            int ageToValue = Integer.parseInt(ageTo);
            return ageFromValue >= 1 && ageFromValue <= 99 && ageToValue >= 1 && ageToValue <= 99 && ageFromValue < ageToValue;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}