package com.example.sharecare.handlers;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.sharecare.Logic.ActivitySQLLiteDatabaseHelper;
import com.example.sharecare.models.Activity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ActivityHandler {
    private ActivitySQLLiteDatabaseHelper databaseHelper;
    private FirebaseFirestore firebaseDb;


    public ActivityHandler(Context context, FirebaseFirestore db) {
        databaseHelper = new ActivitySQLLiteDatabaseHelper(context);
        firebaseDb = db;
    }

    public long insertActivity(Activity activity) {
        return databaseHelper.insertActivity(activity);
    }

   public void addingActivityDataToFirebase(Activity activity) {
        Map<String, Object> activityMap = new HashMap<>();
        activityMap.put("activityName",activity.getActivityName());
        activityMap.put("selectedActivity",activity.getSelectedActivity());
        activityMap.put("selectedDate",activity.getSelectedDate());
        activityMap.put("selectedTime",activity.getSelectedTime());
        activityMap.put("capacity",activity.getCapacity());
        activityMap.put("ageFrom",activity.getAgeFrom());
        activityMap.put("ageTo",activity.getAgeTo());
        activityMap.put("ownerUserId",activity.getOwnerUserId());
        activityMap.put("groupId",activity.getGroupId());

        Task<DocumentReference> referenceTask =  firebaseDb.collection("Activities")
                .add(activityMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        /*updateIdForParent(documentReference.getId());
                        id = documentReference.getId();
                        Map<String, Object> data = new HashMap<>();
                        data.put("name", "first kid");
                        db.collection("Parents").document(id).collection("myKids").add(data);*/

                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

        while(!referenceTask.isComplete()){

        }
    }
}