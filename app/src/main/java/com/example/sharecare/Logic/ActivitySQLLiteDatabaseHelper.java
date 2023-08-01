package com.example.sharecare.Logic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.sharecare.models.Activity;
import com.example.sharecare.models.PendingActivityRequestDTO;

import java.util.ArrayList;
import java.util.List;

public class ActivitySQLLiteDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ShareCare.db";
    private static final int DATABASE_VERSION = 1;
    private static final String ACTIVITIES_TABLE_NAME = "activities";
    private static final String COLUMN_ID = "id";

    private static final String GROUP_ID = "groupid";
    private static final String COLUMN_ACTIVITY_NAME = "activity_name";
    private static final String COLUMN_ACTIVITY_TYPE = "activity_type";
    private static final String COLUMN_ACTIVITY_DATE = "date";
    private static final String COLUMN_ACTIVITY_TIME = "time";
    private static final String COLUMN_CAPACITY = "capacity";
    private static final String COLUMN_DURATION = "duration";
    private static final String COLUMN_AGE_FROM = "child_age_from";
    private static final String COLUMN_AGE_TO = "child_age_to";
    private static final String OWNER_ID = "owner_user_id";
    public static final int REQUEST_STATUS_PENDING = 0;
    public static final int REQUEST_STATUS_ACCEPTED= 2;
    public static final int REQUEST_STATUS_REJECTED= 2;

    /** SQL command to create the table for activities */
    private static final String CREATE_TABLE_ACTIVITIES =
            "CREATE TABLE IF NOT EXISTS  " + ACTIVITIES_TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    GROUP_ID + " INTEGER, " +
                    COLUMN_ACTIVITY_NAME + " TEXT, " +
                    COLUMN_ACTIVITY_TYPE + " TEXT, " +
                    COLUMN_ACTIVITY_DATE + " TEXT, " +
                    COLUMN_ACTIVITY_TIME + " TEXT, " +
                    COLUMN_CAPACITY + " INTEGER, " +
                    COLUMN_DURATION + " INTEGER, " +
                    OWNER_ID + " INTEGER, " +
                    COLUMN_AGE_FROM + " INTEGER, " +
                    COLUMN_AGE_TO + " INTEGER)";

    /** SQL command to create the table for pending activity requests */
    private static final String CREATE_TABLE_PENDING_REQUESTS =
            "CREATE TABLE IF NOT EXISTS   activitiesRequest (id INTEGER PRIMARY KEY AUTOINCREMENT,userid INTEGER," +
                    " groupid INTEGER, activityid INTEGER, requestDate TEXT, isaccept INTEGER)";

    public ActivitySQLLiteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = this.getWritableDatabase();
        onCreate(db);
    }

    /** Create database tables when the database is created for the first time */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // run this to update activity table first drop tables and then recreate
        //db.execSQL("DROP TABLE IF EXISTS " + ACTIVITIES_TABLE_NAME);
        db.execSQL(CREATE_TABLE_ACTIVITIES);
        db.execSQL(CREATE_TABLE_PENDING_REQUESTS);
    }

    /** Handle database upgrades by dropping existing tables and creating them afresh */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ACTIVITIES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + "activitiesRequest");
        onCreate(db);
    }

    /**
     * Insert a new activity into the activities table
     * @param activity The activity object with details to be saved
     * @return The row ID of the newly inserted activity, or -1 in case of an error
     */
    public long  insertActivity(Activity activity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ACTIVITY_NAME, activity.getActivityName());
        values.put(COLUMN_ACTIVITY_TYPE, activity.getSelectedActivity());
        values.put(COLUMN_ACTIVITY_DATE, activity.getSelectedDate());
        values.put(COLUMN_ACTIVITY_TIME, activity.getSelectedTime());
        values.put(COLUMN_CAPACITY, activity.getCapacity());
        values.put(COLUMN_DURATION, activity.getDuration());
        values.put(COLUMN_AGE_FROM, activity.getAgeFrom());
        values.put(COLUMN_AGE_TO, activity.getAgeTo());
        values.put(GROUP_ID, activity.getGroupId());
        values.put(OWNER_ID, activity.getOwnerUserId());

        long rowId=-1;
        try{
              rowId= db.insertOrThrow(ACTIVITIES_TABLE_NAME, null, values);
        }catch(Exception ex)
        {
        }

        db.close();
        return rowId;
    }

    public void updateActivity(Activity activity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ACTIVITY_NAME, activity.getActivityName());
        values.put(COLUMN_ACTIVITY_TYPE, activity.getSelectedActivity());
        values.put(COLUMN_ACTIVITY_DATE, activity.getSelectedDate());
        values.put(COLUMN_ACTIVITY_TIME, activity.getSelectedTime());
        values.put(COLUMN_CAPACITY, activity.getCapacity());
        values.put(COLUMN_DURATION, activity.getDuration());
        values.put(COLUMN_AGE_FROM, activity.getAgeFrom());
        values.put(COLUMN_AGE_TO, activity.getAgeTo());


        String whereClause = COLUMN_ID + " = ?";
        String[] whereArgs = {String.valueOf(activity.getId())};

        db.update(ACTIVITIES_TABLE_NAME, values, whereClause, whereArgs);
        db.close();
    }

    public List<PendingActivityRequestDTO> getPendingActivityRequestsByGroupId(int groupId) {
        List<PendingActivityRequestDTO> pendingRequests = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT activitiesRequest.userid, users.username, activitiesRequest.activityid, " +
                "activitiesRequest.isaccept, activitiesRequest.requestDate, activities.activity_name " +
                "FROM activitiesRequest " +
                "INNER JOIN users ON activitiesRequest.userid = users.id " +
                "INNER JOIN activities ON activitiesRequest.activityid = activities.id " +
                "WHERE activitiesRequest.groupid = ? AND activitiesRequest.isaccept = 0";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(groupId)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                PendingActivityRequestDTO requestDTO = new PendingActivityRequestDTO();

                requestDTO.setUserId(cursor.getInt(cursor.getColumnIndex("userid")));
                requestDTO.setUsername(cursor.getString(cursor.getColumnIndex("username")));
                requestDTO.setActivityId(cursor.getInt(cursor.getColumnIndex("activityid")));
                requestDTO.setIsAccept(cursor.getInt(cursor.getColumnIndex("isaccept")));
                requestDTO.setRequestDate(cursor.getString(cursor.getColumnIndex("requestDate")));
                requestDTO.setActivityName(cursor.getString(cursor.getColumnIndex("activity_name")));

                pendingRequests.add(requestDTO);
            } while (cursor.moveToNext());

            cursor.close();
        }

        db.close();

        return pendingRequests;
    }

    public void updateRequestStatus(int userId, int activityId, boolean isAccept) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("isaccept", isAccept ? REQUEST_STATUS_ACCEPTED :REQUEST_STATUS_REJECTED);

        String whereClause = "userid = ? AND activityid = ?";
        String[] whereArgs = {String.valueOf(userId), String.valueOf(activityId)};

        int rowsAffected = db.update("activitiesRequest", values, whereClause, whereArgs);

        if (rowsAffected > 0) {
            // Update successful
        } else {
            // Update failed
        }

        db.close();
    }
}
