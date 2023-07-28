package com.example.sharecare.Logic;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.sharecare.models.User;

public class UsersSQLLiteDatabaseHelper extends SQLiteOpenHelper {
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

    /**
     * Constructor to initialize the SQLite database helper.
     *
     * @param context The context to use for locating paths to the database.
     */
    public UsersSQLLiteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time.
     *
     * @param db The database where the new table will be created.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    /**
     * Called when the database needs to be upgraded.
     *
     * @param db The database to upgrade.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     * Inserts a user into the database.
     *
     * @param user The user object to be inserted.
     * @return The row ID of the newly inserted row, or -1 if an error occurred.
     */
    public long  insertUser(User user) {
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

        long rowId = db.insertOrThrow(TABLE_NAME, null, values);
        db.close();
        return rowId;
    }


    /**
     * Updates a user's details in the database.
     *
     * @param rowId The row ID of the user to be updated.
     * @param username New username.
     * @param phone New phone number.
     * @param email New email address.
     * @param address New address.
     * @param password New password.
     * @param numKids New number of kids.
     * @param marital New marital status.
     * @param gender New gender.
     * @param language New language.
     * @param religion New religion.
     */
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
