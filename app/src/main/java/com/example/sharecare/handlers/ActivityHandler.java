package com.example.sharecare.handlers;

import android.content.Context;

import com.example.sharecare.Logic.ActivitySQLLiteDatabaseHelper;
import com.example.sharecare.models.Activity;
import com.example.sharecare.models.PendingActivityRequestDTO;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ActivityHandler {
    private ActivitySQLLiteDatabaseHelper databaseHelper;
    private FirebaseHandler firebaseHandler;

    public ActivityHandler(Context context, FirebaseFirestore db) {
        databaseHelper = new ActivitySQLLiteDatabaseHelper(context);
        firebaseHandler = new FirebaseHandler(db);
    }

    public long insertActivity(Activity activity) {
        return databaseHelper.insertActivity(activity);
    }
    public void updateActivity(Activity activity, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {
        // Update the activity in the SQLite database
        databaseHelper.updateActivity(activity);

        // Update the activity in Firebase
        firebaseHandler.updateActivityDataInFirebase(activity, successListener, failureListener);
    }

    public void addingActivityDataToFirebase(Activity activity, OnSuccessListener<DocumentReference> successListener, OnFailureListener failureListener) {
        firebaseHandler.addingActivityDataToFirebase(activity, successListener, failureListener);
    }

    public void updateRequestStatus(int userId, int activityId, boolean isAccept) {
        databaseHelper.updateRequestStatus(userId, activityId, isAccept);
    }

    public List<PendingActivityRequestDTO> getPendingActivityRequestsByGroupId(int groupid) {
        return databaseHelper.getPendingActivityRequestsByGroupId(groupid);
    }


    // Add other methods for handling database operations, if needed.
}
