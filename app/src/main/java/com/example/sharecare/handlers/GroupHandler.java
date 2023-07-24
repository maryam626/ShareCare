package com.example.sharecare.handlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.sharecare.Logic.GroupsSQLLiteDatabaseHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class GroupHandler {

    private static final String TAG = "GroupHandler";
    private GroupsSQLLiteDatabaseHelper databaseHelper;
    private SQLiteDatabase db;

    private FirebaseFirestore firebaseDb;

    public GroupHandler(Context context, FirebaseFirestore firebaseDb) {
        databaseHelper = new GroupsSQLLiteDatabaseHelper(context);
        this.firebaseDb = firebaseDb;
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

        long groupId = db.insert("groups", null, values);

        firebaseDb.collection("Groups")
                .document(String.valueOf(groupId))
                .set(values)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                     }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                         Log.w(TAG, "Error adding document", e);
                    }
                });
        return groupId;
    }

    public boolean insertGroupParticipant(int groupId, String participant) {
        ContentValues values = new ContentValues();
        values.put("groupId", groupId);
        values.put("userId", getUserIdByUsername(participant));

        long groupParticipantId = db.insert("groupParticipants", null, values);
        firebaseDb.collection("groupParticipants")
                .document(String.valueOf(groupParticipantId))
                .set(values)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                       // Toast.makeText(CreateGroupActivity.this, "groupParticipants added to Firebase", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                       // Toast.makeText(CreateGroupActivity.this, "Failed to add group to Firebase", Toast.LENGTH_SHORT).show();
                        Log.w(TAG, "Error adding document", e);
                    }
                });
        return groupParticipantId != -1;
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
