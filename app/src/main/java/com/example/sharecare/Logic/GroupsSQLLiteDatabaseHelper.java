package com.example.sharecare.Logic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

import com.example.sharecare.models.Group;
import com.example.sharecare.models.GroupRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class GroupsSQLLiteDatabaseHelper extends SQLiteOpenHelper {
    FirebaseFirestore firebaseDb;
    private static final String DATABASE_NAME = "ShareCare.db";
    private static final int DATABASE_VERSION = 1;

    public GroupsSQLLiteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.firebaseDb=FirebaseFirestore.getInstance();
    }

    private void DropTablesForDebug(SQLiteDatabase db)
    {

        String removeGroupsRequest = "DROP TABLE groupsRequest ";
        db.execSQL(removeGroupsRequest);
//        String removeGroup = "DROP TABLE groups ";
//        db.execSQL(removeGroup);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        /** These drop table commands can be used when necessary to delete tables.
         *  Use them cautiously.
         */
        //drop grougs table activate once whenever is needed
        // DropTablesForDebug(db);

        /** Create various tables for managing groups and their related information */
        String createGroupsTableQuery = "CREATE TABLE IF NOT EXISTS  groups (id INTEGER PRIMARY KEY AUTOINCREMENT, groupName TEXT,description TEXT,city TEXT,street TEXT,language Text, religion TEXT, hostUserId INTEGER)";
        db.execSQL(createGroupsTableQuery);

        String createGroupRequestTableQuery = "CREATE TABLE IF NOT EXISTS  groupsRequest (id INTEGER PRIMARY KEY AUTOINCREMENT,userid INTEGER, groupid INTEGER, requestDate TEXT, isaccept INTEGER)";
        db.execSQL(createGroupRequestTableQuery);

        String createCitiesTableQuery = "CREATE TABLE IF NOT EXISTS  cities (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT)";
        db.execSQL(createCitiesTableQuery);

        String createLanguagesTableQuery = "CREATE TABLE IF NOT EXISTS  languages (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT)";
        db.execSQL(createLanguagesTableQuery);

        String createReligionTableQuery = "CREATE TABLE IF NOT EXISTS  religions (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT)";
        db.execSQL(createReligionTableQuery);



        addCities(db);
        addLanguages(db);
        addReligions(db);
    }

    /** Populate the cities table with an extensive list of city names */
    private void addCities(SQLiteDatabase db) {
        List<String> cities = Arrays.asList("Jerusalem", "Tel Aviv", "Haifa", "Rishon LeZion", "Petah Tikva", "Ashdod", "Netanya", "Beersheba", "Holon", "Bnei Brak", "Ramat Gan", "Rehovot", "Bat Yam", "Ashkelon", "Jaffa", "Modiin-Maccabim-Reut", "Herzliya", "Kfar Saba", "Raanana", "Bet Shemesh", "Lod", "Nahariya", "Hadera", "Beit Shemesh", "Kiryat Ata", "Kiryat Gat", "Givaatayim", "Ramat HaSharon", "Yavne", "Modiin Illit", "Tiberias", "Hod HaSharon", "Qiryat Motzkin", "Eilat", "Raas al-Ein", "Nablus", "Ariel", "Qiryat Yam", "Kiryat Bialik", "Qiryat Ono", "Or Yehuda", "Qiryat Shemona", "Kiryat Malakhi", "Dimona", "Yehud-Monosson", "Sderot", "Tirat Carmel", "Rosh HaAyin", "Arad", "Kiryat Shmona", "Hod HaSharon", "Nazareth", "Qalansawe", "Karmiel", "Netivot", "Nesher", "Nahariya", "Ofakim", "Qiryat Gat", "Qiryat Ata", "Umm al-Fahm", "Tamra", "Migdal HaEmek", "Acre", "Yokneam", "Tayibe", "Beit Jann", "Qalqilya", "Shaghur", "Kafr Qasim", "Daliyat al-Karmel", "Afula", "Rahat", "Sakhnin", "Sderot", "Kiryat Yam", "Tuba-Zangariyye", "Beit Sheaan", "Shoham", "Kafr Manda", "Ofaqim", "Metula", "Ramat Yishai", "Sakhnin", "Nof HaGalil");
        String insertQuery = "INSERT OR IGNORE INTO cities (name) VALUES (?)";

        for (String city : cities) {
            // Create an array of values to be inserted
            String[] values = {city};

            // Execute the insert query
            db.execSQL(insertQuery, values);
        }
    }

    /** Populate the languages table with a predefined list of languages */
    private void addLanguages(SQLiteDatabase db) {
        List<String> languages = Arrays.asList("Arabic", "English", "Hebrew", "Russian", "Spanish");
        String insertQuery = "INSERT OR IGNORE INTO languages (name) VALUES (?)";

        for (String language : languages) {
            // Create an array of values to be inserted
            String[] values = {language};

            // Execute the insert query
            db.execSQL(insertQuery, values);
        }
    }

    /** Populate the religions table with a predefined list of religions */
    private void addReligions(SQLiteDatabase db) {
        List<String> religions = Arrays.asList("Muslim", "Christian", "Jewish", "Druz");
        String insertQuery = "INSERT OR IGNORE INTO religions (name) VALUES (?)";

        for (String religion : religions) {
            // Create an array of values to be inserted
            String[] values = {religion};

            // Execute the insert query
            db.execSQL(insertQuery, values);
        }
    }

    /** Fetch a list of unique cities where groups are based */
    public List<String> getGroupsDistinctCities() {
        List<String> cityList = new ArrayList<>();
         SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select distinct city as cityName from groups  ORDER BY city ASC", null);

        if (cursor.moveToFirst()) {
            do {
                String cityName = cursor.getString(cursor.getColumnIndex("cityName"));
                cityList.add(cityName);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return cityList;
    }

    /** Fetch a list of all cities stored in the database */
    public List<String> getAllCities() {
        List<String> citiesList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        // Query to select all cities from the cities table
        String query = "SELECT name FROM cities";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Get the city name from the cursor and add it to the list
                String cityName = cursor.getString(cursor.getColumnIndex("name"));
                citiesList.add(cityName);
            } while (cursor.moveToNext());
        }

        // Close the cursor and database
        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return citiesList;
    }
    /** Fetch a list of all languages stored in the database */
    public List<String> loadLanguages() {

        List<String> cityList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT name FROM languages ORDER BY name ASC", null);

        if (cursor.moveToFirst()) {
            do {
                String city = cursor.getString(cursor.getColumnIndex("name"));
                cityList.add(city);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return cityList;
    }

    /** Fetch a list of all religions stored in the database */
    public List<String> loadReligions() {
        List<String> cityList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT name FROM religions ORDER BY name ASC", null);

        if (cursor.moveToFirst()) {
            do {
                String city = cursor.getString(cursor.getColumnIndex("name"));
                cityList.add(city);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return cityList;
    }

    /** Get the user ID based on the provided username */
    public int getUserIdByUsername(String username) {
        int userId = -1;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM users WHERE username = ?", new String[]{username});

        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndex("id"));
        }

        cursor.close();
        db.close();
        return userId;
    }

    public List<Group> getAllGroups() {
        List<Group> groupsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Query the "groups" table and get all rows
            cursor = db.query("groups", null, null, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int groupId = cursor.getInt(cursor.getColumnIndex("id"));
                    String groupName = cursor.getString(cursor.getColumnIndex("groupName"));
                    String description = cursor.getString(cursor.getColumnIndex("description"));
                    String city = cursor.getString(cursor.getColumnIndex("city"));
                    String street = cursor.getString(cursor.getColumnIndex("street"));
                    String language = cursor.getString(cursor.getColumnIndex("language"));
                    String religion = cursor.getString(cursor.getColumnIndex("religion"));
                    int hostUserId = cursor.getInt(cursor.getColumnIndex("hostUserId"));

                    Group group = new Group(groupId, groupName, description, city, street, language, religion, hostUserId);
                    groupsList.add(group);
                } while (cursor.moveToNext());
            }
        } finally {
            // Close the cursor and database
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return groupsList;
    }

    public void syncGroupsFromSQLiteToFirebase() {
        SQLiteDatabase db = getReadableDatabase();
        List<Group> groups = getAllGroups();
        for (Group group : groups) {
            firebaseDb.collection("groups")
                    .document(String.valueOf(group.getId()))
                    .set(group)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Update successful
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Update failed
                        }
                    });
        }
    }

    /**
     * Synchronize group requests from Firebase to SQLite.
     * @param groupRequests List of GroupRequest objects received from Firebase.
     */
    public void syncGroupRequestsFromFirebase(List<GroupRequest> groupRequests) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            // Start a transaction
            db.beginTransaction();

            // Clear the existing groupRequests table to replace it with the updated data
            db.delete("groupsRequest", null, null);

            // Insert or update group requests in the database
            for (GroupRequest groupRequest : groupRequests) {
                ContentValues values = new ContentValues();
                values.put("id", groupRequest.getId());
                values.put("userid", groupRequest.getUserId());
                values.put("groupid", groupRequest.getGroupId());
                values.put("requestDate", groupRequest.getRequestDate());
                values.put("isaccept", groupRequest.isAccepted() ? 1 : 0);

                db.insert("groupsRequest", null, values);
            }

            // Set the transaction as successful and end it
            db.setTransactionSuccessful();
        } catch (Exception e) {
         } finally {
            // End the transaction
            db.endTransaction();
        }

        // Close the database
        db.close();
    }
    public List<GroupRequest> getAllGroupRequests() {
        List<GroupRequest> groupRequests = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM groupsRequest", null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                int userId = cursor.getInt(cursor.getColumnIndex("userid"));
                int groupId = cursor.getInt(cursor.getColumnIndex("groupid"));
                String requestDate = cursor.getString(cursor.getColumnIndex("requestDate"));
                int isAcceptInt = cursor.getInt(cursor.getColumnIndex("isaccept"));
                boolean isAccepted = isAcceptInt == 1;

                GroupRequest groupRequest = new GroupRequest(id, userId, groupId, requestDate, isAccepted);
                groupRequests.add(groupRequest);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        db.close();
        return groupRequests;
    }
    public void syncGroupRequestsFromSQLiteToFirebase() {
        List<GroupRequest> groupRequests =  getAllGroupRequests();
        for (GroupRequest groupRequest : groupRequests) {
            firebaseDb.collection("GroupsRequest")
                    .document(String.valueOf(groupRequest.getId()))
                    .set(groupRequest)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Update successful
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Update failed
                        }
                    });
        }
    }



    /**
     * Synchronize groups from Firebase to SQLite.
     * @param groups List of Group objects received from Firebase.
     */
    public void syncGroupsFromFirebase(List<Group> groups) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            // Start a transaction
            db.beginTransaction();

            // Clear the existing groups table to replace it with the updated data
            db.delete("groups", null, null);

            // Insert or update groups in the database
            for (Group group : groups) {
                ContentValues values = new ContentValues();
                values.put("id", group.getId());
                values.put("groupName", group.getGroupName());
                values.put("description", group.getDescription());
                values.put("city", group.getCity());
                values.put("street", group.getStreet());
                values.put("language", group.getLanguage());
                values.put("religion", group.getReligion());
                values.put("hostUserId", group.getHost().getId());

                db.insert("groups", null, values);
            }

            // Set the transaction as successful and end it
            db.setTransactionSuccessful();
        } catch (Exception e) {
        } finally {
            // End the transaction
            db.endTransaction();
        }

        // Close the database
        db.close();
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS groups");
        db.execSQL("DROP TABLE IF EXISTS groupParticipants");
        onCreate(db);
    }
}