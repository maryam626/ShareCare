package com.example.sharecare.handlers;

import android.content.Context;
import android.database.Cursor;

import com.example.sharecare.Logic.ActivitySQLLiteDatabaseHelper;
import com.example.sharecare.models.Activity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

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

    public void addingActivityDataToFirebase(Activity activity, OnSuccessListener<DocumentReference> successListener, OnFailureListener failureListener) {
        firebaseHandler.addingActivityDataToFirebase(activity, successListener, failureListener);
    }

    public void updateRequestStatus(int userId, int activityId, boolean isAccept) {
        databaseHelper.updateRequestStatus(userId, activityId, isAccept);
    }

    public Cursor getPendingRequestsCursor() {
        return databaseHelper.getPendingRequestsCursor();
    }

    // Add other methods for handling database operations, if needed.
}
