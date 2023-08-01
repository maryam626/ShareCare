package com.example.sharecare.Logic;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.sharecare.models.Group;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class GroupsSQLLiteDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ShareCare.db";
    private static final int DATABASE_VERSION = 1;
    private FirebaseFirestore firebaseDb = FirebaseFirestore.getInstance();


    public GroupsSQLLiteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
        /*List<String> cityList = new ArrayList<>();
        firebaseDb.collection("Cities").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(int i = 0; i<queryDocumentSnapshots.size();i++) {
                    cityList.add(queryDocumentSnapshots.getDocuments().get(i).get("name").toString());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        return cityList;*/

        List<String> cityList = new ArrayList<>();
        /*SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select distinct city as cityName from groups  ORDER BY city ASC", null);

        if (cursor.moveToFirst()) {
            do {
                String cityName = cursor.getString(cursor.getColumnIndex("cityName"));
                cityList.add(cityName);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();*/

        return cityList;
    }

    /** Fetch a list of all cities stored in the database */
    public List<String> loadCities() {
        List<String> cityList = new ArrayList<>();

        /*firebaseDb.collection("Cities").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(int i = 0; i<queryDocumentSnapshots.size();i++) {
                    cityList.add(queryDocumentSnapshots.getDocuments().get(i).get("name").toString());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        return cityList;*/

        /*List<String> cityList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT name FROM cities ORDER BY name ASC", null);

        if (cursor.moveToFirst()) {
            do {
                String city = cursor.getString(cursor.getColumnIndex("name"));
                cityList.add(city);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();*/

        return cityList;
    }

    /** Fetch a list of all languages stored in the database */
    public List<String> loadLanguages() {

        /*List<String> languagesList = new ArrayList<>();

        firebaseDb.collection("Languages").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(int i = 0; i<queryDocumentSnapshots.size();i++) {
                    languagesList.add(queryDocumentSnapshots.getDocuments().get(i).get("name").toString());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        return languagesList;*/

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

        /*List<String> religionsList = new ArrayList<>();

        firebaseDb.collection("Religions").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(int i = 0; i<queryDocumentSnapshots.size();i++) {
                    religionsList.add(queryDocumentSnapshots.getDocuments().get(i).get("name").toString());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        return religionsList;*/

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

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS groups");
        db.execSQL("DROP TABLE IF EXISTS groupParticipants");
        onCreate(db);
    }
}