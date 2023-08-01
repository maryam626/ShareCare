package com.example.sharecare.handlers;

import androidx.annotation.NonNull;

import com.example.sharecare.log_in_activity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateKidProfileFirebaseHandler {

    private FirebaseFirestore db;

    public CreateKidProfileFirebaseHandler() {
        db = FirebaseFirestore.getInstance();
    }

    public void addKidData(Map<String, Object> kid, OnSuccessListener<DocumentReference> successListener, OnFailureListener failureListener) {
        Task<DocumentReference> task = db.collection("Kids")
                .add(kid)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
        while(!task.isComplete()){

        }
    }


    public void updateKidId(String id) {
        Map<String,Object> kidIdData = new HashMap<>();
        kidIdData.put("id",id);
        Task<Void> task = db.collection("Parents").document(log_in_activity.id).collection("myKids").document(id).update(kidIdData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                //TODO
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //TODO
            }
        });

        while(!task.isComplete()){

        }
    }


    public void updatingNumberOfKidsFieldInFirebase() {
        Map<String,Object> numberOfKidsData = new HashMap<>();
        numberOfKidsData.put("numberOfKids", log_in_activity.numberOfKids);
        Task<Void> task = db.collection("Parents").document(log_in_activity.id).update(numberOfKidsData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                //TODO
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //TODO
            }
        });

        while(!task.isComplete()){

        }
    }

    public void addKidToParentCollection(String parentId, String kidId, Map<String, Object> kid, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {
        Task<Void> task = db.collection("Parents")
                .document(parentId)
                .collection("myKids")
                .document(kidId)
                .set(kid)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
        while(!task.isComplete()){

        }
    }

    public void addKidToUsersCollection(String parentId, String kidId, Map<String, Object> kid, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {
        Task<Void> task = db.collection("Users")
                .document(parentId)
                .collection("myKids")
                .document(kidId)
                .set(kid)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
        while(!task.isComplete()){

        }
    }

    public void updateIdInCollection(String collection, String parentId, String kidId, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {
        Task<Void> task = db.collection(collection)
                .document(parentId)
                .collection("myKids")
                .document(kidId)
                .update("id", kidId)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
        while(!task.isComplete()){

        }
    }
}
