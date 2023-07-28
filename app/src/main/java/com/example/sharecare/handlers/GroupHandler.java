package com.example.sharecare.handlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.sharecare.Logic.GroupsSQLLiteDatabaseHelper;
import com.example.sharecare.models.Group;
import com.example.sharecare.valdiators.Validator;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
public class GroupHandler {

    private static final String TAG = "GroupHandler";

    private GroupsSQLLiteDatabaseHelper groupsDatabaseHelper;
    private FirebaseFirestore firebaseDb;

    public GroupHandler(Context context, FirebaseFirestore firebaseDb) {
        groupsDatabaseHelper = new GroupsSQLLiteDatabaseHelper(context);
        this.firebaseDb = firebaseDb;
    }

    public void open() {
        // Open the database for writing
    }

    public void close() {
        // Close the database if it's open
    }

    public List<String> getAllCities() {
        return groupsDatabaseHelper.loadCities();
    }

    public List<String> getGroupsDistinctCities() {
        return groupsDatabaseHelper.getGroupsDistinctCities();
    }

    public List<String> getAllLanguages() {
        return groupsDatabaseHelper.loadLanguages();
    }

    public List<String> getAllReligions() {
        return groupsDatabaseHelper.loadReligions();
    }
    public long insertGroup(String groupName, String description, String city, String street, String language, String religion, int hostUserId) {
        if (!Validator.isValidGroupName(groupName)) {
            return -1; // Invalid group name
        }

        if (!Validator.isValidDescription(description)) {
            return -1; // Invalid description
        }

        if (!Validator.isValidCity(city)) {
            return -1; // Invalid city
        }

        if (!Validator.isValidStreet(street)) {
            return -1; // Invalid street
        }

        SQLiteDatabase db = groupsDatabaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("groupName", groupName);
        values.put("description", description);
        values.put("city", city);
        values.put("street", street);
        values.put("language", language);
        values.put("religion", religion);
        values.put("hostUserId", hostUserId);

        long groupId = db.insert("groups", null, values);

        db.close();

        if (groupId != -1) {
            addGroupToFirebase(groupId, values);
        }

        return groupId;
    }

    private void addGroupToFirebase(long groupId, ContentValues values) {
        firebaseDb.collection("Groups")
                .document(String.valueOf(groupId))
                .set(values)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "Group added to Firebase");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding group to Firebase", e);
                    }
                });
    }

    public boolean insertGroupParticipant(int groupId, String participant) {
        int userId = groupsDatabaseHelper.getUserIdByUsername(participant);

        if (userId == -1) {
            return false; // User not found in the database
        }

        SQLiteDatabase db = groupsDatabaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("groupId", groupId);
        values.put("userId", userId);

        long groupParticipantId = db.insert("groupParticipants", null, values);

        db.close();

        if (groupParticipantId != -1) {
            addGroupParticipantToFirebase(groupParticipantId, values);
        }

        return groupParticipantId != -1;
    }

    private void addGroupParticipantToFirebase(long groupParticipantId, ContentValues values) {
        firebaseDb.collection("groupParticipants")
                .document(String.valueOf(groupParticipantId))
                .set(values)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "Group participant added to Firebase");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding group participant to Firebase", e);
                    }
                });
    }

    public List<String> getParticipantsExceptCurrent(int loggedInUserId) {
        List<String> participantList = new ArrayList<>();
        SQLiteDatabase db = groupsDatabaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT username FROM users WHERE id <> ?", new String[]{String.valueOf(loggedInUserId)});

        if (cursor.moveToFirst()) {
            do {
                String username = cursor.getString(cursor.getColumnIndex("username"));
                participantList.add(username);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return participantList;
    }

    public List<Group> getGroups(List<String> selectedCities, int loggedInUserId) {
        List<Group> groupList = new ArrayList<>();
        SQLiteDatabase db = groupsDatabaseHelper.getReadableDatabase();

        String selectQuery = "SELECT id, groupName, description, city, street FROM groups WHERE hostUserId <> ? " +
                "UNION ALL SELECT groups.id, groups.groupName, groups.description, groups.city, groups.street " +
                "FROM groups " +
                "INNER JOIN groupParticipants ON groups.id = groupParticipants.groupId " +
                "WHERE groupParticipants.userId <> ? " +
                "UNION ALL SELECT groups.id, groups.groupName, groups.description, groups.city, groups.street " +
                "FROM groups " +
                "INNER JOIN groupsRequest ON groups.id = groupsRequest.groupId " +
                "WHERE groupsRequest.userId = ? AND isaccept = 0 AND groups.city IN (";

        // Append each city name to the query
        for (int i = 0; i < selectedCities.size(); i++) {
            selectQuery += "'" + selectedCities.get(i) + "'";
            if (i < selectedCities.size() - 1) {
                selectQuery += ",";
            }
        }

        selectQuery += ")";

        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(loggedInUserId),
                String.valueOf(loggedInUserId), String.valueOf(loggedInUserId)});

        if (cursor.moveToFirst()) {
            do {
                int groupId = cursor.getInt(cursor.getColumnIndex("id"));
                String groupName = cursor.getString(cursor.getColumnIndex("groupName"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                String city = cursor.getString(cursor.getColumnIndex("city"));
                String street = cursor.getString(cursor.getColumnIndex("street"));

                Group group = new Group(groupId, groupName, description, city, street);
                groupList.add(group);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return groupList;
    }

    public ArrayList<Group> getGroupsResult(List<String> selectedCities,String language, String religion, int loggedInUserId) {
        Log.d("getGroupsResult", "1st res");
        ArrayList<Group> groupList = new ArrayList<>();
        SQLiteDatabase db = groupsDatabaseHelper.getReadableDatabase();

        String selectQuery = "SELECT id, groupName, description, city, street, language, religion FROM groups WHERE hostUserId <> ? AND language = ? AND religion = ? AND city IN (";

        // Append each city name to the query
        for (int i = 0; i < selectedCities.size(); i++) {
            selectQuery += "'" + selectedCities.get(i) + "'";
            if (i < selectedCities.size() - 1) {
                selectQuery += ",";
            }
        }

        selectQuery += ")";

        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(loggedInUserId), language, religion});

        if (cursor.moveToFirst()) {
            do {
                int groupId = cursor.getInt(cursor.getColumnIndex("id"));
                String groupName = cursor.getString(cursor.getColumnIndex("groupName"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                String city = cursor.getString(cursor.getColumnIndex("city"));
                String street = cursor.getString(cursor.getColumnIndex("street"));
                String groupLanguage = cursor.getString(cursor.getColumnIndex("language"));
                String groupReligion = cursor.getString(cursor.getColumnIndex("religion"));

                Group group = new Group(groupId, groupName, description, city, street, groupLanguage, groupReligion);
                groupList.add(group);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return groupList;
    }
}
