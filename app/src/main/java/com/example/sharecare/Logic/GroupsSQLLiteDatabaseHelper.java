package com.example.sharecare.Logic;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.sharecare.models.Group;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class GroupsSQLLiteDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ShareCare.db";
    private static final int DATABASE_VERSION = 1;

    public GroupsSQLLiteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final String CREATE_TABLE_GROUP_PENDING_REQUESTS =
            "CREATE TABLE IF NOT EXISTS groupsRequest (userid INTEGER, groupid INTEGER, requestDate TEXT, isaccept INTEGER)";

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createGroupsTableQuery = "CREATE TABLE IF NOT EXISTS  groups (id INTEGER PRIMARY KEY AUTOINCREMENT, groupName TEXT,description TEXT,city TEXT,street TEXT,language Text, religion TEXT, hostUserId INTEGER)";
        db.execSQL(createGroupsTableQuery);

        // drop grougs table activate once whenever is needed
//        String createGroupsTableQuery = "DROP TABLE groups ";
//        db.execSQL(createGroupsTableQuery);

        String createGroupParticipantsTableQuery = "CREATE TABLE IF NOT EXISTS  groupParticipants (groupId INTEGER, userId INTEGER)";
        db.execSQL(createGroupParticipantsTableQuery);

        String createCitiesTableQuery = "CREATE TABLE IF NOT EXISTS  cities (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT)";
        db.execSQL(createCitiesTableQuery);

        String createLanguagesTableQuery = "CREATE TABLE IF NOT EXISTS  languages (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT)";
        db.execSQL(createLanguagesTableQuery);

        String createReligionTableQuery = "CREATE TABLE IF NOT EXISTS  religions (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT)";
        db.execSQL(createReligionTableQuery);

        db.execSQL(CREATE_TABLE_GROUP_PENDING_REQUESTS);

        addCities(db);
        addLanguages(db);
        addReligions(db);
    }

    private void addCities(SQLiteDatabase db) {
        List<String> cities = Arrays.asList("Jerusalem", "Tel Aviv", "Haifa", "Rishon LeZion", "Petah Tikva", "Ashdod", "Netanya", "Beersheba", "Holon", "Bnei Brak", "Ramat Gan", "Rehovot", "Bat Yam", "Ashkelon", "Jaffa", "Modi'in-Maccabim-Re'ut", "Herzliya", "Kfar Saba", "Ra'anana", "Bet Shemesh", "Lod", "Nahariya", "Hadera", "Beit Shemesh", "Kiryat Ata", "Kiryat Gat", "Giv'atayim", "Ramat HaSharon", "Yavne", "Modi'in Illit", "Tiberias", "Hod HaSharon", "Qiryat Motzkin", "Eilat", "Ra's al-'Ein", "Nablus", "Ariel", "Qiryat Yam", "Kiryat Bialik", "Qiryat Ono", "Or Yehuda", "Qiryat Shemona", "Kiryat Malakhi", "Dimona", "Yehud-Monosson", "Sderot", "Tirat Carmel", "Rosh Ha'Ayin", "Arad", "Kiryat Shmona", "Hod HaSharon", "Nazareth", "Qalansawe", "Karmiel", "Netivot", "Nesher", "Nahariya", "Ofakim", "Qiryat Gat", "Qiryat Ata", "Umm al-Fahm", "Tamra", "Migdal HaEmek", "Acre", "Yokneam", "Tayibe", "Beit Jann", "Qalqilya", "Shaghur", "Kafr Qasim", "Daliyat al-Karmel", "Afula", "Rahat", "Sakhnin", "Sderot", "Kiryat Yam", "Tuba-Zangariyye", "Beit She'an", "Shoham", "Kafr Manda", "Ofaqim", "Metula", "Ramat Yishai", "Sakhnin", "Nof HaGalil");
        String insertQuery = "INSERT OR IGNORE INTO cities (name) VALUES (?)";

        for (String city : cities) {
            // Create an array of values to be inserted
            String[] values = {city};

            // Execute the insert query
            db.execSQL(insertQuery, values);
        }
    }
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


    public List<String> loadCities() {
        List<String> cityList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT name FROM cities ORDER BY name ASC", null);

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

    // Method to get rows based on religion, language, and an array of cities
    public ArrayList<Group> getRowsByReligionLanguageAndCities(String religion, String language, String[] cities) {

        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Group> groups = new ArrayList<Group>();

        // Create the selection string for the query
        StringBuilder selection = new StringBuilder();
        selection.append("SELECT * FROM ").append("groups").append(" WHERE ");
        selection.append("language").append(" = ? AND ");
        selection.append("religion").append(" = ? AND ");
        selection.append("city").append(" IN (");
        for (int i = 0; i < cities.length; i++) {
            selection.append("?");
            if (i < cities.length - 1) {
                selection.append(",");
            }
        }
        selection.append(")");

        // Create the selection arguments
        String[] selectionArgs = new String[cities.length + 2];
        selectionArgs[0] = religion;
        selectionArgs[1] = language;
        System.arraycopy(cities, 0, selectionArgs, 2, cities.length);

        // Execute the query
        Cursor cursor = db.rawQuery(selection.toString(), selectionArgs);

        // Loop through the cursor to get the results and populate the ArrayList
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int groupId = cursor.getInt(cursor.getColumnIndex("id"));
                String groupName = cursor.getString(cursor.getColumnIndex("groupName"));
                String groupDescription = cursor.getString(cursor.getColumnIndex("description"));
                String groupCity = cursor.getString(cursor.getColumnIndex("city"));
                String groupStreet = cursor.getString(cursor.getColumnIndex("street"));
                String groupLanguage = cursor.getString(cursor.getColumnIndex("language"));
                String groupReligion = cursor.getString(cursor.getColumnIndex("religion"));

                Group group = new Group(groupId, groupName,groupDescription,groupCity, groupStreet, groupLanguage, groupReligion);
                groups.add(group);

            } while (cursor.moveToNext());

            cursor.close();
        }
        db.close();

        return groups;
    }
}
