package com.example.sharecare.handlers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.sharecare.log_in_activity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class LogInFirebaseHandler {
    private static final String TAG = "FirebaseLogHandler";

    /** Firebase authentication instance */
    private FirebaseAuth mAuth;

    /** Firestore database instance */
    private FirebaseFirestore db;


    /**
     * Constructor to initialize Firebase authentication and database instances.
     */
    public LogInFirebaseHandler() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public ArrayList<QueryDocumentSnapshot> getParentData(String email, log_in_activity activity) {
        ArrayList<QueryDocumentSnapshot> results = new ArrayList<>();
        Task<QuerySnapshot> task = db.collection("Parents").whereEqualTo("email", email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        results.add(document);
                        System.out.println(results);

                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

        while(!task.isComplete()){

        }
        return results;

    }
}
