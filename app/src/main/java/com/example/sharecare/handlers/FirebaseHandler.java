package com.example.sharecare.handlers;

import androidx.annotation.NonNull;

import com.example.sharecare.models.Activity;
import com.example.sharecare.models.Host;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class FirebaseHandler {
    private FirebaseFirestore firebaseDb;
    public static boolean isInFirebase;


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
        activityMap.put("duration", activity.getDuration());
        activityMap.put("ageFrom", activity.getAgeFrom());
        activityMap.put("ageTo", activity.getAgeTo());
        activityMap.put("ownerUserId", activity.getOwnerUserId());
        activityMap.put("groupId", activity.getGroupId());

        Task task = firebaseDb.collection("Activities")
                .add(activityMap)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
        while(!task.isComplete()){

        }
    }

    public void addingHostDataToFirebase(Host host, OnSuccessListener successListener, OnFailureListener failureListener){
        boolean isInFirebase = checkIfTheHostIsAlreadyInFirebase(host);

        if(!isInFirebase){
        Map<String, Object> hostMap = new HashMap<>();
        hostMap.put("id", "0");
        hostMap.put("username", host.getUsername());
        hostMap.put("phoneNumber", host.getPhoneNumber());
        hostMap.put("email", host.getEmail());
        hostMap.put("address", host.getAddress());
        hostMap.put("password", host.getPassword());
        hostMap.put("numberOfKids", host.getNumberOfKids());
        hostMap.put("maritalStatus", host.getMaritalStatus());
        hostMap.put("gender", host.getGender());
        hostMap.put("language", host.getLanguage());
        hostMap.put("religion", host.getReligion());


        Task task = firebaseDb.collection("Hosts")
                .add(hostMap)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
        while(!task.isComplete()){

        }}
    }

    private boolean checkIfTheHostIsAlreadyInFirebase(Host host) {
        Task<QuerySnapshot> task = firebaseDb.collection("Hosts").whereEqualTo("email", host.getEmail()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.size() > 0){
                    FirebaseHandler.isInFirebase = true;
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                FirebaseHandler.isInFirebase = false;

            }
        });

        while(!task.isComplete()){
            System.out.print(1);
        }

        System.out.println(FirebaseHandler.isInFirebase);

        return FirebaseHandler.isInFirebase;
    }
}
