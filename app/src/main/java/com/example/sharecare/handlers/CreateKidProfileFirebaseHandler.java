package com.example.sharecare.handlers;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
        db.collection("Kids")
                .add(kid)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    public void addKidToParentCollection(String parentId, String kidId, Map<String, Object> kid, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {
        db.collection("Parents")
                .document(parentId)
                .collection("myKids")
                .document(kidId)
                .set(kid)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    public void addKidToUsersCollection(String parentId, String kidId, Map<String, Object> kid, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {
        db.collection("Users")
                .document(parentId)
                .collection("myKids")
                .document(kidId)
                .set(kid)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    public void updateIdInCollection(String collection, String parentId, String kidId, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {
        db.collection(collection)
                .document(parentId)
                .collection("myKids")
                .document(kidId)
                .update("id", kidId)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }
}
