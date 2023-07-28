package com.example.sharecare.handlers;

import com.example.sharecare.models.Activity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FirebaseHandler {
    private FirebaseFirestore firebaseDb;

    public FirebaseHandler(FirebaseFirestore db) {
        firebaseDb = db;
    }

    public void addingActivityDataToFirebase(Activity activity, OnSuccessListener successListener, OnFailureListener failureListener) {
        Map<String, Object> activityMap = new HashMap<>();
        activityMap.put("activityName", activity.getActivityName());
        activityMap.put("selectedActivity", activity.getSelectedActivity());
        activityMap.put("selectedDate", activity.getSelectedDate());
        activityMap.put("selectedTime", activity.getSelectedTime());
        activityMap.put("capacity", activity.getCapacity());
        activityMap.put("ageFrom", activity.getAgeFrom());
        activityMap.put("ageTo", activity.getAgeTo());
        activityMap.put("ownerUserId", activity.getOwnerUserId());
        activityMap.put("groupId", activity.getGroupId());

        firebaseDb.collection("Activities")
                .add(activityMap)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }
}
