package com.example.sharecare.handlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.example.sharecare.Logic.ActivitySQLLiteDatabaseHelper;
import com.example.sharecare.models.Activity;
import com.example.sharecare.models.ActivityShareDTO;
import com.example.sharecare.models.PendingActivityRequestDTO;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActivityHandler {
    private class SyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                // Run the synchronization task in the background (async)
                firebaseHandler.syncActivitiesFromFirebaseToSQLite(databaseHelper);
                firebaseHandler.syncActivitiesRequestFromFirebaseToSQLite(databaseHelper);
                firebaseHandler.syncActivitiesFromSQLiteToFirebase(databaseHelper);
                firebaseHandler.syncActivitiesRequestFromSQLiteToFirebase(databaseHelper);
            }
            catch(Exception ex)
            {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
        }
    }



    private ActivitySQLLiteDatabaseHelper databaseHelper;
    private FirebaseHandler firebaseHandler;

    public ActivityHandler(Context context) {
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        databaseHelper = new ActivitySQLLiteDatabaseHelper(context);
        firebaseHandler = new FirebaseHandler(db);
    }

    public long insertActivity(Activity activity) {
        long res= databaseHelper.insertActivity(activity);
        return res;

    }
    public void updateActivity(Activity activity, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {
        // Update the activity in the SQLite database
        databaseHelper.updateActivity(activity);

        // Update the activity in Firebase
        firebaseHandler.updateActivityDataInFirebase(activity, successListener, failureListener);
    }

    //sync sqllite with firestore , this will run async (non blocking for ui)
    public void Sync()
    {

        new SyncTask().execute();
    }

    public void addingActivityDataToFirebase(Activity activity, OnSuccessListener<DocumentReference> successListener, OnFailureListener failureListener) {
        firebaseHandler.addingActivityDataToFirebase(activity, successListener, failureListener);
    }

    public void updateRequestStatus(int userId, int activityId, boolean isAccept) {
        databaseHelper.updateRequestStatus(userId, activityId, isAccept);
    }

    public void insertActivityRequest(int userId, int activityId,int groupid) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("userid", userId);
        values.put("groupid", groupid);
        values.put("activityid", activityId);

        // Get the current date in the desired format (e.g., "yyyy-MM-dd HH:mm:ss")
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        values.put("requestDate", currentDate);
        values.put("isaccept", 0);

        db.insert("activitiesRequest", null, values);
        db.close();
    }

    public List<PendingActivityRequestDTO> getPendingActivityRequestsByGroupId(int groupid) {
        return databaseHelper.getPendingActivityRequestsByGroupId(groupid);
    }


    // Add the deleteActivity method
    public void deleteActivity(int activityId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        db.delete("activities", "id = ?", new String[]{String.valueOf(activityId)});
        db.close();
    }

    public List<ActivityShareDTO> getActivitiesForGroup(int groupId, int loggedInUserId) {
        List<ActivityShareDTO> activityShareList = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT a.id, a.groupid, a.activity_name, a.activity_type, a.date, a.time, a.capacity," +
                        " a.duration, a.owner_user_id, a.child_age_from, a.child_age_to," +
                        "ar.isaccept as requestStatusCode," +
                        " CASE WHEN a.owner_user_id = ? THEN 1 ELSE 0 END AS IAmOwner" +
                        " FROM activities AS a " +
                        "LEFT JOIN activitiesRequest AS ar " +
                        "ON a.id = ar.activityid WHERE a.groupid = ?;\n",
                new String[]{String.valueOf(loggedInUserId),String.valueOf(groupId)}
        );

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            int groupid = cursor.getInt(cursor.getColumnIndex("groupid"));
            String activityName = cursor.getString(cursor.getColumnIndex("activity_name"));
            String selectedActivity = cursor.getString(cursor.getColumnIndex("activity_type"));
            String selectedDate = cursor.getString(cursor.getColumnIndex("date"));
            String selectedTime = cursor.getString(cursor.getColumnIndex("time"));
            int capacity = cursor.getInt(cursor.getColumnIndex("capacity"));
            int duration = cursor.getInt(cursor.getColumnIndex("duration"));
            int ageFrom = cursor.getInt(cursor.getColumnIndex("child_age_from"));
            int ageTo = cursor.getInt(cursor.getColumnIndex("child_age_to"));
            int ownerUserId = cursor.getInt(cursor.getColumnIndex("owner_user_id"));
            Activity activity = new Activity(id, activityName, selectedActivity, selectedDate, selectedTime, capacity, duration,ageFrom, ageTo, groupId, ownerUserId);
            activity.setGroupId(groupid);
            ActivityShareDTO sharedto = new ActivityShareDTO(activity);
            sharedto.setRequestStatusCode(cursor.getInt(cursor.getColumnIndex("requestStatusCode")));
            sharedto.setiAmOwner(cursor.getInt(cursor.getColumnIndex("IAmOwner")) ==1);
            activityShareList.add(sharedto);
        }

        cursor.close();
        db.close();
        return activityShareList;
    }
}
