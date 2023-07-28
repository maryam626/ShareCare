package com.example.sharecare.Logic;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.sharecare.models.Activity;

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
    private static final String COLUMN_AGE_FROM = "child_age_from";
    private static final String COLUMN_AGE_TO = "child_age_to";
    private static final String OWNER_ID = "owner_user_id";


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
                    OWNER_ID + " INTEGER, " +
                    COLUMN_AGE_FROM + " INTEGER, " +
                    COLUMN_AGE_TO + " INTEGER)";


    /** SQL command to create the table for pending activity requests */
    private static final String CREATE_TABLE_PENDING_REQUESTS =
            "CREATE TABLE IF NOT EXISTS   activitiesRequest (userid INTEGER, activityid INTEGER, requestDate TEXT, isaccept INTEGER)";


    public ActivitySQLLiteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /** Create database tables when the database is created for the first time */
    @Override
    public void onCreate(SQLiteDatabase db) {
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
}
