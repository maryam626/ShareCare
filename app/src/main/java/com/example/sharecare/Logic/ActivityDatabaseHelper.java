package com.example.sharecare.Logic;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.sharecare.models.Activity;

public class ActivityDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ShareCare.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "activities";
    private static final String COLUMN_ID = "id";

    private static final String GROUP_ID = "groupid";
    private static final String COLUMN_ACTIVITY_NAME = "activity_name";
    private static final String COLUMN_ACTIVITY_TYPE = "activity_type";
    private static final String COLUMN_ACTIVITY_DATE = "date";
    private static final String COLUMN_ACTIVITY_TIME = "time";
    private static final String COLUMN_CAPACITY = "capcaity";
    private static final String COLUMN_AGE_FROM = "child_age_from";
    private static final String COLUMN_AGE_TO = "child_age_to";

    private static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS  " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    GROUP_ID + " INTEGER, " +
                    COLUMN_ACTIVITY_NAME + " TEXT, " +
                    COLUMN_ACTIVITY_TYPE + " TEXT, " +
                    COLUMN_ACTIVITY_DATE + " TEXT, " +
                    COLUMN_ACTIVITY_TIME + " TEXT, " +
                    COLUMN_CAPACITY + " INTEGER, " +
                    COLUMN_AGE_FROM + " INTEGER, " +
                    COLUMN_AGE_TO + " INTEGER)";

    public ActivityDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

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

        long rowId=-1;
        try{
              rowId= db.insertOrThrow(TABLE_NAME, null, values);
        }catch(Exception ex)
        {
        }

        db.close();
        return rowId;
    }
}
