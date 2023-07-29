package com.example.sharecare.handlers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.sharecare.log_in_activity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;

public class LogInFirebaseHandler {
    private static final String TAG = "FirebaseLogHandler";

    /** Firebase authentication instance */
    private FirebaseAuth mAuth;

    /** Firestore database instance */
    private FirebaseFirestore db;
    DocumentSnapshot result;
    ArrayList<DocumentSnapshot> results = new ArrayList<>();



    /**
     * Constructor to initialize Firebase authentication and database instances.
     */
    public LogInFirebaseHandler() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public ArrayList<DocumentSnapshot> getParentData(String email, log_in_activity activity) {

        Task<QuerySnapshot> task = db.collection("Parents").whereEqualTo("email", email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    results.add(task.getResult().getDocuments().get(0));
                    result = task.getResult().getDocuments().get(0);
                    log_in_activity.puttingDataInVariables(result);
                    Log.d(TAG, results.get(0).getId() + " => " + results.get(0).getData());

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

        while(!task.isComplete()){

        }
        System.out.println(results);
        System.out.println(result);
        return results;

    }

}
