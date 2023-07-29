package com.example.sharecare.handlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.sharecare.Logic.ActivitySQLLiteDatabaseHelper;
import com.example.sharecare.Logic.GroupsSQLLiteDatabaseHelper;
import com.example.sharecare.models.Activity;
import com.example.sharecare.models.ActivityShareDTO;
import com.example.sharecare.models.Group;
import com.example.sharecare.valdiators.CreateGroupValidator;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A handler class to manage group-related operations, including local database interactions and Firebase Firestore operations.
 */
public class GroupHandler {

    private static final String TAG = "GroupHandler";
    private GroupsSQLLiteDatabaseHelper groupsDatabaseHelper;
    private FirebaseFirestore firebaseDb;
    private ActivitySQLLiteDatabaseHelper activityDatabaseHelper;


    /**
     * Constructor to initialize database helpers and Firestore instance.
     *
     * @param context Android context.
     * @param firebaseDb Firebase Firestore instance.
     */
    public GroupHandler(Context context, FirebaseFirestore firebaseDb) {
        groupsDatabaseHelper = new GroupsSQLLiteDatabaseHelper(context);
        this.firebaseDb = firebaseDb;
        activityDatabaseHelper = new ActivitySQLLiteDatabaseHelper(context);
    }

    /**
     * Constructor to initialize only the database helpers.
     *
     * @param context Android context.
     */
    public GroupHandler(Context context) {
        groupsDatabaseHelper = new GroupsSQLLiteDatabaseHelper(context);
        activityDatabaseHelper = new ActivitySQLLiteDatabaseHelper(context);
    }

    public void open() {
        // Open the database for writing if needed
    }

    public void close() {
        // Close the database if it's open if needed
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

    /**
     * Inserts a new group into the local database and then to Firebase.
     *
     * @param ... Various parameters about the group.
     * @return The ID of the inserted group or -1 if insertion failed.
     */

    public long insertGroup(String groupName, String description, String city, String street, String language, String religion, int hostUserId) {
        if (!CreateGroupValidator.isGroupNameValid(groupName)) {
            return -1; // Invalid group name
        }

        if (!CreateGroupValidator.isDescriptionValid(description)) {
            return -1; // Invalid description
        }

        if (!CreateGroupValidator.isValidCity(city)) {
            return -1; // Invalid city
        }

        if (!CreateGroupValidator.isStreetValid(street)) {
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
            //addGroupToFirebase(groupId, values);
        }

        return groupId;
    }

    /**
     * Adds group information to Firebase Firestore.
     *
     * @param groupId The ID of the group.
     * @param group Content values representing the group information.
     */
    public void addGroupToFirebase(long groupId, Group group) {
        firebaseDb.collection("Groups")
                .document(String.valueOf(groupId))
                .set(group)
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

    /**
     * Inserts a participant for a group into the local database and then to Firebase.
     *
     * @param groupId The ID of the group.
     * @param participant Participant's name.
     * @return true if insertion was successful, false otherwise.
     */
    public boolean insertGroupParticipant(int groupId, String participant) {
        int userId = groupsDatabaseHelper.getUserIdByUsername(participant);

        if (userId == -1) {
            return false; // User not found in the database
        }

        SQLiteDatabase db = groupsDatabaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("groupId", groupId);
        values.put("userId", userId);

        // Get the current date and time in the required format
        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(new Date());
        values.put("requestDate", currentDate);

        values.put("isaccept", -1);

        long groupParticipantId = db.insert("groupsRequest", null, values);

        db.close();

        if (groupParticipantId != -1) {
            addGroupParticipantToFirebase(groupParticipantId, values);
        }

        return groupParticipantId != -1;
    }

    /**
     * Adds group participant information to Firebase Firestore.
     *
     * @param groupParticipantId The ID of the group participant relation.
     * @param values Content values representing the group participant information.
     */
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

    public Group getGroupById(int groupId) {
        Group group = null;
        SQLiteDatabase db = groupsDatabaseHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT id, groupName, description, city, street FROM groups WHERE id = ?", new String[]{String.valueOf(groupId)});
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String groupName = cursor.getString(cursor.getColumnIndex("groupName"));
            String description = cursor.getString(cursor.getColumnIndex("description"));
            String city = cursor.getString(cursor.getColumnIndex("city"));
            String street = cursor.getString(cursor.getColumnIndex("street"));

            group = new Group(id, groupName, description, city, street);
        }
        cursor.close();
        db.close();

        return group;
    }

    public List<ActivityShareDTO> getActivitiesForGroup(int groupId, int loggedInUserId) {
        List<ActivityShareDTO> activityShareList = new ArrayList<>();
        SQLiteDatabase db = activityDatabaseHelper.getReadableDatabase();

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

    public void insertActivityRequest(int userId, int activityId,int groupid) {
        SQLiteDatabase db = activityDatabaseHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("userid", userId);
        values.put("groupid", groupid);
        values.put("activityid", activityId);

        // Get the current date in the desired format (e.g., "yyyy-MM-dd HH:mm:ss")
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        values.put("requestDate", currentDate);
        values.put("isaccept", -1);

        db.insert("activitiesRequest", null, values);
        db.close();
    }

    public String getGroupNameById(int groupId) {
        SQLiteDatabase db = activityDatabaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT groupName FROM groups WHERE id = ?", new String[]{String.valueOf(groupId)});
        String groupName = null;
        if (cursor.moveToFirst()) {
            groupName = cursor.getString(cursor.getColumnIndex("groupName"));
        }
        cursor.close();
        db.close();
        return groupName;
    }

    // Add the deleteActivity method
    public void deleteActivity(int activityId) {
        SQLiteDatabase db = activityDatabaseHelper.getReadableDatabase();
        db.delete("activities", "id = ?", new String[]{String.valueOf(activityId)});
        db.close();
    }

    //query to all according to all filters
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
    //query according to city filter
    public ArrayList<Group> getCityGroupsResults(List<String> selectedCities, int loggedInUserId) {

        ArrayList<Group> groupList = new ArrayList<>();
        SQLiteDatabase db = groupsDatabaseHelper.getReadableDatabase();

        String selectQuery = "SELECT id, groupName, description, city, street, language, religion FROM groups WHERE hostUserId <> ? AND city IN (";

        // Append each city name to the query
        for (int i = 0; i < selectedCities.size(); i++) {
            selectQuery += "'" + selectedCities.get(i) + "'";
            if (i < selectedCities.size() - 1) {
                selectQuery += ",";
            }
        }

        selectQuery += ")";

        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(loggedInUserId)});
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

    //query according to language filter
    public ArrayList<Group> getLanguageGroupsResults(String language, int loggedInUserId) {

        ArrayList<Group> groupList = new ArrayList<>();
        SQLiteDatabase db = groupsDatabaseHelper.getReadableDatabase();

        String selectQuery = "SELECT id, groupName, description, city, street, language, religion FROM groups WHERE hostUserId <> ? AND language = ? ";

        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(loggedInUserId), language});
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

    //query according to religion filter
    public ArrayList<Group> getReligionGroupsResults(String religion, int loggedInUserId) {

        ArrayList<Group> groupList = new ArrayList<>();
        SQLiteDatabase db = groupsDatabaseHelper.getReadableDatabase();

        String selectQuery = "SELECT id, groupName, description, city, street, language, religion FROM groups WHERE hostUserId <> ? AND religion = ? ";

        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(loggedInUserId), religion});
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

    //query according to combination of city and language filters
    public ArrayList<Group> getCityLanguageGroupsResults(List<String> selectedCities,String language, int loggedInUserId) {

        ArrayList<Group> groupList = new ArrayList<>();
        SQLiteDatabase db = groupsDatabaseHelper.getReadableDatabase();

        String selectQuery = "SELECT id, groupName, description, city, street, language, religion FROM groups WHERE hostUserId <> ? AND language = ? AND city IN (";

        // Append each city name to the query
        for (int i = 0; i < selectedCities.size(); i++) {
            selectQuery += "'" + selectedCities.get(i) + "'";
            if (i < selectedCities.size() - 1) {
                selectQuery += ",";
            }
        }

        selectQuery += ")";

        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(loggedInUserId), language});
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

    //query according to combination of city and religion filters
    public ArrayList<Group> getCityReligionGroupsResults(List<String> selectedCities,String religion, int loggedInUserId) {

        ArrayList<Group> groupList = new ArrayList<>();
        SQLiteDatabase db = groupsDatabaseHelper.getReadableDatabase();

        String selectQuery = "SELECT id, groupName, description, city, street, language, religion FROM groups WHERE hostUserId <> ? AND religion = ? AND city IN (";

        // Append each city name to the query
        for (int i = 0; i < selectedCities.size(); i++) {
            selectQuery += "'" + selectedCities.get(i) + "'";
            if (i < selectedCities.size() - 1) {
                selectQuery += ",";
            }
        }

        selectQuery += ")";

        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(loggedInUserId), religion});
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

    public ArrayList<Group> getLanguageReligionGroupsResults(String religion,String language, int loggedInUserId) {

        ArrayList<Group> groupList = new ArrayList<>();
        SQLiteDatabase db = groupsDatabaseHelper.getReadableDatabase();

        String selectQuery = "SELECT id, groupName, description, city, street, language, religion FROM groups WHERE hostUserId <> ? AND language = ? AND religion = ?";

        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(loggedInUserId),language , religion});
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
