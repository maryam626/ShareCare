package com.example.sharecare.Logic;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.sharecare.models.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class UsersDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "database";

    private static final String DATABASE_NAME = "ShareCare.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PHONE_NUMBER = "phone_number";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_NUMBER_OF_KIDS = "number_of_kids";
    private static final String COLUMN_MARITAL_STATUS = "marital_status";
    private static final String COLUMN_GENDER = "gender";
    private static final String COLUMN_LANGUAGE = "language";
    private static final String COLUMN_RELIGION = "religion";

    private static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USERNAME + " TEXT, " +
                    COLUMN_PHONE_NUMBER + " TEXT, " +
                    COLUMN_EMAIL + " TEXT, " +
                    COLUMN_ADDRESS + " TEXT, " +
                    COLUMN_PASSWORD + " TEXT, " +
                    COLUMN_NUMBER_OF_KIDS + " INTEGER, " +
                    COLUMN_MARITAL_STATUS + " TEXT, " +
                    COLUMN_GENDER + " TEXT, " +
                    COLUMN_LANGUAGE + " TEXT, " +
                    COLUMN_RELIGION + " TEXT)";

    private Context mContext;

    public UsersDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;

        // Check if the database exists, if not, copy it from assets
        if (!checkDatabaseExists()) {
            try {
                copyDatabase();
            } catch (IOException e) {
                Log.e(TAG, "Error copying database", e);
            }
        }
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

    private boolean checkDatabaseExists() {
        File databasePath = mContext.getDatabasePath(DATABASE_NAME);
        return databasePath.exists();
    }

    private void copyDatabase() throws IOException {
        AssetManager assetManager = mContext.getAssets();
        InputStream myInput = assetManager.open(DATABASE_NAME);
        String outFileName = mContext.getDatabasePath(DATABASE_NAME).getPath();
        OutputStream myOutput = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public long insertUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, user.getUsername());
        values.put(COLUMN_PHONE_NUMBER, user.getPhoneNumber());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_ADDRESS, user.getAddress());
        values.put(COLUMN_PASSWORD, user.getPassword());
        values.put(COLUMN_NUMBER_OF_KIDS, user.getNumberOfKids());
        values.put(COLUMN_MARITAL_STATUS, user.getMaritalStatus());
        values.put(COLUMN_GENDER, user.getGender());
        values.put(COLUMN_LANGUAGE, user.getLanguage());
        values.put(COLUMN_RELIGION, user.getReligion());

        long rowId = -1;
        try {
            rowId = db.insertOrThrow(TABLE_NAME, null, values);
        } catch (Exception ex) {
            Log.e(TAG, "Error inserting user", ex);
        }

        db.close();
        return rowId;
    }

    public void updateUser(String rowId, String username, String phone, String email, String address, String password, int numKids, String marital, String gender, String language, String religion) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PHONE_NUMBER, phone);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_ADDRESS, address);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_NUMBER_OF_KIDS, numKids);
        values.put(COLUMN_MARITAL_STATUS, marital);
        values.put(COLUMN_GENDER, gender);
        values.put(COLUMN_LANGUAGE, language);
        values.put(COLUMN_RELIGION, religion);

        db.update(TABLE_NAME, values, "id=?", new String[]{rowId});
        db.close();
    }
}
