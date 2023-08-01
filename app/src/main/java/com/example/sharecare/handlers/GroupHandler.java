package com.example.sharecare.handlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.sharecare.Logic.GroupsSQLLiteDatabaseHelper;
import com.example.sharecare.models.Group;
import com.example.sharecare.models.GroupDataDTO;
import com.example.sharecare.models.GroupRequest;
import com.example.sharecare.valdiators.CreateGroupValidator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A handler class to manage group-related operations, including local database interactions and Firebase Firestore operations.
 */
public class GroupHandler {

    private static final String TAG = "GroupHandler";
    private GroupsSQLLiteDatabaseHelper groupsDatabaseHelper;
    private FirebaseFirestore firebaseDb;


    private class SyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try{
            // Run the synchronization task in the background (async)
            syncGroupsFromFirebaseToSQLite(groupsDatabaseHelper);
            syncGroupRequestsFromFirebaseToSQLite(groupsDatabaseHelper);
            groupsDatabaseHelper.syncGroupRequestsFromSQLiteToFirebase();
            groupsDatabaseHelper.syncGroupsFromSQLiteToFirebase();

            }
            catch(Exception ex)
            {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // This method is called on the UI thread after the background task is completed.
            // You can add any UI updates or notifications here if needed.
        }
    }

    //sync sqllite with firestore , this will run async (non blocking for ui)
    public void Sync()
    {
        new SyncTask().execute();
    }

    /**
     * Constructor to initialize database helpers and Firestore instance.
     *
     * @param context Android context.
     * @param firebaseDb Firebase Firestore instance.
     */
    public GroupHandler(Context context, FirebaseFirestore firebaseDb) {
        groupsDatabaseHelper = new GroupsSQLLiteDatabaseHelper(context);
        this.firebaseDb = firebaseDb;
    }

    /**
     * Constructor to initialize only the database helpers.
     *
     * @param context Android context.
     */
    public GroupHandler(Context context) {
        this.firebaseDb=FirebaseFirestore.getInstance();
        groupsDatabaseHelper = new GroupsSQLLiteDatabaseHelper(context);
    }

    public void open() {
        // Open the database for writing if needed
    }

    public void close() {
        // Close the database if it's open if needed
    }

    public List<String> getAllCities() {
        return groupsDatabaseHelper.getAllCities();
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


    public void deleteGroup(long groupId) {
        // Delete the group from the local SQLite database
        SQLiteDatabase db = groupsDatabaseHelper.getWritableDatabase();
        db.delete("groupsRequest", "groupid = ?", new String[]{String.valueOf(groupId)});
        db.delete("groupParticipants", "groupId = ?", new String[]{String.valueOf(groupId)});
        db.delete("groups", "id = ?", new String[]{String.valueOf(groupId)});
        db.close();
        deleteGroupFromFirebase(groupId);

    }
    private void deleteGroupFromFirebase(long groupId) {

        Task<Void> task = firebaseDb.collection("groups").document(String.valueOf(groupId)).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "DocumentSnapshot successfully deleted!");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error deleting document", e);

            }
        });
        while(!task.isComplete()){

        }

    }
    /**
     * Updates an existing group in the local database and then updates it in Firebase.
     *
     * @param groupId The ID of the group to be updated.
     * @param groupName The new group name.
     * @param description The new group description.
     * @param city The new city for the group.
     * @param street The new street for the group.
     * @param language The new language for the group.
     * @param religion The new religion for the group.
     * @param hostUserId The host user ID of the group.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateGroup(long groupId, String groupName, String description, String city, String street, String language, String religion, int hostUserId) {
        if (!CreateGroupValidator.isGroupNameValid(groupName)) {
            return false; // Invalid group name
        }

        if (!CreateGroupValidator.isDescriptionValid(description)) {
            return false; // Invalid description
        }

        if (!CreateGroupValidator.isValidCity(city)) {
            return false; // Invalid city
        }

        if (!CreateGroupValidator.isStreetValid(street)) {
            return false; // Invalid street
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

        int rowsAffected = db.update("groups", values, "id = ?", new String[]{String.valueOf(groupId)});
        db.close();

        if (rowsAffected > 0) {
            return true;
        }

        return false;
    }

    public List<GroupDataDTO> getGroupsForUser(int currentUserId) {
        List<GroupDataDTO> groupsList = new ArrayList<>();
        SQLiteDatabase db = groupsDatabaseHelper.getWritableDatabase();
        String query = "SELECT DISTINCT " +
                "    groups.id AS groupid, groups.groupName, groups.description, groups.hostUserId, " +
                "    groups.city, groups.street, groups.language, groups.religion," +
                "    CASE " +
                "        WHEN groups.hostUserId = ? THEN 1 " +
                "        ELSE 0 " +
                "    END AS iamhost " +
                "FROM " +
                "    groups " +
                "LEFT JOIN " +
                "    groupsRequest ON groups.id = groupsRequest.groupid AND groupsRequest.userid = ? AND groupsRequest.isaccept = 1 " +
                "WHERE " +
                "    groups.hostUserId = ? OR groupsRequest.id IS NOT NULL;";



        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(currentUserId), String.valueOf(currentUserId), String.valueOf(currentUserId)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int groupId = cursor.getInt(cursor.getColumnIndex("groupid"));
                String groupName = cursor.getString(cursor.getColumnIndex("groupName"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                int hostUserId = cursor.getInt(cursor.getColumnIndex("hostUserId"));
                String city = cursor.getString(cursor.getColumnIndex("city"));
                String street = cursor.getString(cursor.getColumnIndex("street"));
                String language = cursor.getString(cursor.getColumnIndex("language"));
                String religion = cursor.getString(cursor.getColumnIndex("religion"));
                boolean iamHost = cursor.getInt(cursor.getColumnIndex("iamhost")) == 1;
                Group group = new Group(groupId, groupName, description, city, street, language, religion);
                GroupDataDTO groupData = new GroupDataDTO(group, iamHost);
                groupsList.add(groupData);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        db.close();
        return groupsList;
    }

    /**
     * Updates the group data in Firebase Firestore.
     *
     * @param groupId The ID of the group to be updated in Firebase.
     * @param updatedGroup The updated group object.
     * @param successListener Listener to be called on success.
     * @param failureListener Listener to be called on failure.
     */
    public void updateGroupInFirebase(long groupId, Group updatedGroup,
                                       OnSuccessListener<Void> successListener,
                                       OnFailureListener failureListener) {

        firebaseDb.collection("groups")
                .document(String.valueOf(groupId))
                .set(updatedGroup)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }


    /**
     * Adds group information to Firebase Firestore.
     *
     * @param groupId The ID of the group.
     * @param group Content values representing the group information.
     */
    public void addGroupToFirebase(long groupId, Group group) {
        Task<Void> task = firebaseDb.collection("groups")
                .document(String.valueOf(groupId))
                .set(group)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        addingActivityCollectionIntoGroupDocument(groupId);
                        updateGroupId(groupId);
                        Log.d(TAG, "Group added to Firebase");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding group to Firebase", e);
                    }
                });

        while(!task.isComplete()){

        }
    }

    private void updateGroupId(long groupId) {
        Map<String,Object> groupIdData = new HashMap<>();
        groupIdData.put("id",String.valueOf(groupId));

        Task<Void> task = firebaseDb.collection("groups").document(String.valueOf(groupId)).update(groupIdData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        while(!task.isComplete()){

        }
    }

    private void addingActivityCollectionIntoGroupDocument(long groupId) {
        Map<String,Object> activityData = new HashMap<>();

        Task<DocumentReference> task = firebaseDb.collection("groups").document(String.valueOf(groupId)).collection("Activities").add(activityData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        while(!task.isComplete()){

        }
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
        Task<Void> task = firebaseDb.collection("groupParticipants")
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
        while(!task.isComplete()){

        }
    }

    public List<String> getParticipantsExceptCurrent(int loggedInUserId) {
        List<String> participantList = new ArrayList<>();
        Task<QuerySnapshot> task = firebaseDb.collection("Parents").whereNotEqualTo("id", loggedInUserId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                    participantList.add(queryDocumentSnapshots.getDocuments().get(i).get("username").toString());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        while(!task.isComplete()){

        }
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

    public String getGroupNameById(int groupId) {
        SQLiteDatabase db = groupsDatabaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT groupName FROM groups WHERE id = ?", new String[]{String.valueOf(groupId)});
        String groupName = null;
        if (cursor.moveToFirst()) {
            groupName = cursor.getString(cursor.getColumnIndex("groupName"));
        }
        cursor.close();
        db.close();
        return groupName;
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
    public void syncGroupRequestsFromFirebaseToSQLite(GroupsSQLLiteDatabaseHelper databaseHelper) {
        firebaseDb.collection("GroupsRequest")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<GroupRequest> groupRequests = queryDocumentSnapshots.toObjects(GroupRequest.class);
                        databaseHelper.syncGroupRequestsFromFirebase(groupRequests);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle the failure if needed
                    }
                });
    }

    public void clearFirebaseGroups() {
        FirebaseFirestore firebaseDb = FirebaseFirestore.getInstance();
        firebaseDb.collection("Groups")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                document.getReference().delete();
                            }
                            // The "Groups" collection is now cleared in Firebase Firestore
                        } else {
                            // Handle the failure if needed
                        }
                    }
                });
        clearSQLiteGroups();
    }


    public void clearSQLiteGroups() {

        SQLiteDatabase db = groupsDatabaseHelper.getWritableDatabase();
        db.delete("groups", null, null);
        db.close();
        // The "groups" table is now cleared in the SQLite database
    }

    // Add a method to synchronize groupsRequest from Firebase to SQLite
    public void syncGroupsFromFirebaseToSQLite(GroupsSQLLiteDatabaseHelper databaseHelper) {

        firebaseDb.collection("groups")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Group> groups = new ArrayList<>();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            try{
                                Group group = document.toObject(Group.class);
                                groups.add(group);
                            }catch (Exception ex)
                            {
                                System.out.println(ex.getCause());
                            }


                        }

                        try {
                            databaseHelper.syncGroupsFromFirebase(groups);
                        } catch (Exception ex) {
                            System.out.println(ex.getCause());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle the failure if needed
                    }
                });
    }
}
