package com.example.sharecare.Logic;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GroupsDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ShareCare.db";
    private static final int DATABASE_VERSION = 1;

    public GroupsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createGroupsTableQuery = "CREATE TABLE IF NOT EXISTS  groups (id INTEGER PRIMARY KEY AUTOINCREMENT, groupName TEXT,description TEXT, hostUserId INTEGER)";
        db.execSQL(createGroupsTableQuery);

        String createGroupParticipantsTableQuery = "CREATE TABLE IF NOT EXISTS  groupParticipants (groupId INTEGER, userId INTEGER)";
        db.execSQL(createGroupParticipantsTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS groups");
        db.execSQL("DROP TABLE IF EXISTS groupParticipants");
        onCreate(db);
    }
}
