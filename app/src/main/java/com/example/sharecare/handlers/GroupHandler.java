package com.example.sharecare.handlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.sharecare.Logic.GroupsSQLLiteDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class GroupHandler {

    private static final String TAG = "GroupHandler";
    private GroupsSQLLiteDatabaseHelper databaseHelper;
    private SQLiteDatabase db;

    public GroupHandler(Context context) {
        databaseHelper = new GroupsSQLLiteDatabaseHelper(context);
    }

    public void open() {
        db = databaseHelper.getWritableDatabase();
    }

    public void close() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
    public List<String> getAllCities()
    {
        return databaseHelper.loadCities();
    }

    public long insertGroup(String groupName, String description, String city, String street, int hostUserId) {
        ContentValues values = new ContentValues();
        values.put("groupName", groupName);
        values.put("description", description);
        values.put("city", city);
        values.put("street", street);
        values.put("hostUserId", hostUserId);

        return db.insert("groups", null, values);
    }

    public boolean insertGroupParticipant(int groupId, String participant) {
        ContentValues values = new ContentValues();
        values.put("groupId", groupId);
        values.put("userId", getUserIdByUsername(participant));

        long result = db.insert("groupParticipants", null, values);
        return result != -1;
    }

    public List<String> getParticipantsExceptCurrent(int loggedInUserId) {
        List<String> participantList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT username FROM users WHERE id <> ?", new String[]{String.valueOf(loggedInUserId)});

        if (cursor.moveToFirst()) {
            do {
                String username = cursor.getString(cursor.getColumnIndex("username"));
                participantList.add(username);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return participantList;
    }

    private int getUserIdByUsername(String username) {
        int userId = -1;
        Cursor cursor = db.rawQuery("SELECT id FROM users WHERE username = ?", new String[]{username});

        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndex("id"));
        }

        cursor.close();
        return userId;
    }
}
