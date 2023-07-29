package com.example.sharecare.valdiators;
import android.text.TextUtils;

public class CreateActivityValidator {


    /**
     * Checks if the activity name is valid (length between 2 and 20).
     *
     * @param activityName Name of the activity.
     * @return true if valid, false otherwise.
     */
    public static boolean isActivityNameValid(String activityName) {
        return !TextUtils.isEmpty(activityName) && activityName.length() >= 2 && activityName.length() <= 20;
    }

    /**
     * Checks if the date is valid (non-empty).
     *
     * @param date Date value.
     * @return true if valid, false otherwise.
     */
    public static boolean isDateValid(String date) {
        return !TextUtils.isEmpty(date);
    }

    /**
     * Checks if the time is valid (non-empty).
     *
     * @param time Time value.
     * @return true if valid, false otherwise.
     */
    public static boolean isTimeValid(String time) {
        return !TextUtils.isEmpty(time);
    }

    /**
     * Checks if the capacity is valid (between 1 and 100).
     *
     * @param capacity Capacity value as a string.
     * @return true if valid, false otherwise.
     */
    public static boolean isCapacityValid(String capacity) {
        try {
            int capacityValue = Integer.parseInt(capacity);
            return capacityValue >= 1 && capacityValue <= 100;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    /**
     * Checks if the duration is valid (between 1 and 23).
     *
     * @param duration Duration value as a string.
     * @return true if valid, false otherwise.
     */
    public static boolean isDurationValid(String duration) {
        try {
            int capacityValue = Integer.parseInt(duration);
            return capacityValue >= 1 && capacityValue <= 23;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if the age range is valid (from 1-99 and 'from' age is less than 'to' age).
     *
     * @param ageFrom Starting age.
     * @param ageTo Ending age.
     * @return true if valid, false otherwise.
     */
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