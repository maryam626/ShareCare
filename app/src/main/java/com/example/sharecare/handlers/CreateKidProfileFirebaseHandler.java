package com.example.sharecare.handlers;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class CreateKidProfileFirebaseHandler {

    private static final String TAG = "FirebaseHandler";

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
