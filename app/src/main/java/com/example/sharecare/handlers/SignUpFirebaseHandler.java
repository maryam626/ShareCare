package com.example.sharecare.handlers;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.sharecare.sign_up_activity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class SignUpFirebaseHandler {
    private static final String TAG = "FirebaseHandler";

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    public SignUpFirebaseHandler() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public void registerUser(String email, String password, sign_up_activity activity) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(activity, "Successfully Registered", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                throw task.getException();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(activity, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void addParentDataToFirebase(String id, String username, String phoneNumber, String email, String address, String password, int numberOfKids, String maritalStatus, String gender, String language, String religion, sign_up_activity activity) {
        Map<String, Object> parent = new HashMap<>();
        parent.put("id", id);
        parent.put("username", username);
        parent.put("phoneNumber", phoneNumber);
        parent.put("email", email);
        parent.put("address", address);
        parent.put("password", password);
        parent.put("numberOfKids", String.valueOf(numberOfKids));
        parent.put("maritalStatus", maritalStatus);
        parent.put("gender", gender);
        parent.put("language", language);
        parent.put("religion", religion);

        db.collection("Parents").document(id).set(parent)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        updateIdForParent(id, email, activity);
                        Map<String, Object> data = new HashMap<>();
                        data.put("name", "first kid");
                        db.collection("Parents").document(id).collection("myKids").add(data);

                        Log.d(TAG, "DocumentSnapshot added with ID: " + id);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    private void updateIdForParent(String id, String email, sign_up_activity activity) {
        Map<String, Object> parentDetail = new HashMap<>();
        parentDetail.put("id", id);

        db.collection("Parents")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                            String documentId = documentSnapshot.getId();
                            db.collection("Parents").document(documentId).update(parentDetail)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(activity, "id updated Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(activity, "Some error happened", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Log.d(TAG, "Error changing data", task.getException());
                        }
                    }
                });
    }
}