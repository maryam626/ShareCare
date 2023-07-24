package com.example.sharecare;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sharecare.Logic.GroupsSQLLiteDatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SearchResultsActivity extends AppCompatActivity {

    private TableLayout resultTable;
    private int loggedInUserId;
    private String loggedInUsername;
    private GroupsSQLLiteDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        resultTable = findViewById(R.id.resultTable);

        databaseHelper = new GroupsSQLLiteDatabaseHelper(this);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        databaseHelper.onCreate(db);
        db.close();

        Intent intent = getIntent();
        loggedInUserId = intent.getIntExtra("userid",-1);
        loggedInUsername = intent.getStringExtra("username");
        List<String> selectedCities = intent.getStringArrayListExtra("selectedCities");

        loadGroups(selectedCities);
    }

    private void loadGroups(List<String> selectedCities) {

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        // Retrieve groups where user is not part of or didnt do a requst to join to yet
        String selectQuery= "select distinct groupid,groupName,description,city,street from (SELECT  id as groupid,groupName,description,city,street  FROM groups" +
                "  WHERE hostUserId <> ? union all SELECT groups.id as groupid,groups.groupName,groups.description,groups.city,groups.street " +
                " FROM groups INNER JOIN groupParticipants ON groups.id = groupParticipants.groupId " +
                " WHERE groupParticipants.userId <> ?" +
                " union all SELECT groups.id as groupid,groups.groupName,groups.description,groups.city,groups.street " +
                " FROM groups INNER JOIN groupsRequest ON groups.id = groupsRequest.groupId " +
                "  WHERE groupsRequest.userId = ? and isaccept=0) where city in ( ";

         // Append each city name to the query
        for (int i = 0; i < selectedCities.size(); i++) {
            selectQuery += "'" + selectedCities.get(i) + "'";
            if (i < selectedCities.size() - 1) {
                selectQuery += ",";
            }
        }
        selectQuery += ")";
        Cursor hostCursor = db.rawQuery(selectQuery,new String[]{String.valueOf(loggedInUserId),String.valueOf(loggedInUserId),String.valueOf(loggedInUserId)});

        addGroupsToTable(hostCursor);

        hostCursor.close();
        db.close();
    }


    private void addGroupsToTable(Cursor cursor) {
boolean first=true;
        if (cursor.moveToFirst()) {
            do {

                int groupId = cursor.getInt(cursor.getColumnIndex("groupid"));
                String groupName = cursor.getString(cursor.getColumnIndex("groupName"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                String city = cursor.getString(cursor.getColumnIndex("city"));
                String street = cursor.getString(cursor.getColumnIndex("street"));

                // Create a new row in the table
                TableRow row = new TableRow(this);


                if(first)
                {
                    // Create TextViews for group name and group ID
                    TextView groupNameLabelTextView = new TextView(this);
                    groupNameLabelTextView.setText("Group Name");
                    groupNameLabelTextView.setPadding(8, 8, 8, 8);
                    row.addView(groupNameLabelTextView);

                    TextView  descriptionLabelTextView = new TextView(this);
                    descriptionLabelTextView.setText(String.valueOf("Description"));
                    descriptionLabelTextView.setPadding(8, 8, 8, 8);
                    row.addView(descriptionLabelTextView);

                    TextView  cityLabelTextView = new TextView(this);
                    cityLabelTextView.setText(String.valueOf("City"));
                    cityLabelTextView.setPadding(8, 8, 8, 8);
                    row.addView(cityLabelTextView);

                    TextView  streetLabelTextView = new TextView(this);
                    streetLabelTextView.setText(String.valueOf("Street"));
                    streetLabelTextView.setPadding(8, 8, 8, 8);
                    row.addView(streetLabelTextView);
                    resultTable.addView(row);
                    row = new TableRow(this);
                    first=false;
                }
                // Create TextViews for group name and group ID
                TextView groupNameTextView = new TextView(this);
                groupNameTextView.setText(groupName);
                groupNameTextView.setPadding(8, 8, 8, 8);
                row.addView(groupNameTextView);

                TextView  descriptionTextView = new TextView(this);
                descriptionTextView.setText(String.valueOf(description));
                descriptionTextView.setPadding(8, 8, 8, 8);
                row.addView(descriptionTextView);

                TextView  cityTextView = new TextView(this);
                cityTextView.setText(String.valueOf(city));
                cityTextView.setPadding(8, 8, 8, 8);
                row.addView(cityTextView);

                TextView  streetTextView = new TextView(this);
                streetTextView.setText(String.valueOf(street));
                streetTextView.setPadding(8, 8, 8, 8);
                row.addView(streetTextView);

                // Create a Button for "Open Group"
                Button openGroupButton = new Button(this);
                openGroupButton.setText("Join Group");
                openGroupButton.setPadding(8, 8, 8, 8);
                openGroupButton.setOnClickListener(new View.OnClickListener() {

                    public void insertActivityRequest(int userId, int activityId) {

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        String date = dateFormat.format(new Date());

                        ContentValues values = new ContentValues();
                        values.put("userid", userId);
                        values.put("groupid", activityId);
                        values.put("requestDate", date);
                        values.put("isaccept", false);

                        SQLiteDatabase db = databaseHelper.getReadableDatabase();

                        db.insert("groupsRequest", null, values);
                        db.close();
                    }

                    @Override
                    public void onClick(View v) {
                        insertActivityRequest(loggedInUserId,groupId);
                        Toast.makeText(SearchResultsActivity.this, "Your request submitted to group owner ", Toast.LENGTH_SHORT).show();
                    }
                });
                row.addView(openGroupButton);

                // Add the row to the table
                resultTable.addView(row);
            } while (cursor.moveToNext());
        }else
        {
            Toast.makeText(SearchResultsActivity.this, "There is no groups that you are not part of ! , view groups in My Groups section", Toast.LENGTH_SHORT).show();

        }
    }
}
