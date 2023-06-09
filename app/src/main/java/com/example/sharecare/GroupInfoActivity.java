package com.example.sharecare;
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

import com.example.sharecare.Logic.ActivityDatabaseHelper;
import com.example.sharecare.Logic.GroupsDatabaseHelper;

public class GroupInfoActivity extends AppCompatActivity {
    private ActivityDatabaseHelper activityDatabaseHelper;
    private SQLiteDatabase groupsDatabase;
    private SQLiteDatabase activityDatabase;
    private GroupsDatabaseHelper groupsDatabaseHelper;

    private int groupId;
    private boolean isHost;
    private TableLayout tableLayout;

    private Button createActivityButton;
    private String loggedInUserId;
    private String loggedInUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);
        activityDatabaseHelper= new ActivityDatabaseHelper(this);
        activityDatabase = activityDatabaseHelper.getReadableDatabase();
        //create table if not exist
        activityDatabaseHelper.onCreate(activityDatabase);
        createActivityButton = findViewById(R.id.createActivityButton);
        groupsDatabaseHelper = new GroupsDatabaseHelper(this);
        groupsDatabase = groupsDatabaseHelper.getReadableDatabase();
        //create table if not exist
        groupsDatabaseHelper.onCreate(groupsDatabase);

        tableLayout = findViewById(R.id.tableLayout);

        // Retrieve the group ID and isHost value from the intent
        Intent intent = getIntent();
        groupId = intent.getIntExtra("groupid", -1);
        loggedInUserId = getIntent().getStringExtra("userid");
        loggedInUsername = getIntent().getStringExtra("username");
       // isHost = intent.getBooleanExtra("isHost", false);

        createActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupInfoActivity.this, ChildrenActivityCreateActivity.class);
                Bundle extras = new Bundle();
                extras.putString("userid", loggedInUserId);
                extras.putInt("groupid", groupId);
                extras.putString("username", loggedInUsername);

                intent.putExtras(extras);
                startActivity(intent);
            }
        });
        loadGroupData();
        loadGroupActivityData();
    }

    private void loadGroupData() {
        // Query the groups table to retrieve group ID and group name
        Cursor cursor = groupsDatabase.rawQuery("SELECT id as groupdid, groupName  as groupname FROM groups WHERE id = ?", new String[]{String.valueOf(groupId)});
        if (cursor.moveToFirst()) {
            String groupName = cursor.getString(cursor.getColumnIndex("groupname"));

            // Display the group name in a label
            TextView groupNameTextView = findViewById(R.id.groupNameTextView);
            groupNameTextView.setText(groupName);
        }
        cursor.close();
    }

    private void loadGroupActivityData() {
        // Query the groupActivity table to retrieve rows with matching group ID
        Cursor cursor = activityDatabase.rawQuery("SELECT    activity_name,activity_type,date,time," +
                "capcaity,child_age_from,child_age_to FROM activities WHERE groupid = ?", new String[]{String.valueOf(groupId)});
        while (cursor.moveToNext()) {
            String activity_name = cursor.getString(cursor.getColumnIndex("activity_name"));
            String activity_type = cursor.getString(cursor.getColumnIndex("activity_type"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            String capcaity = cursor.getString(cursor.getColumnIndex("capcaity"));
            String child_age_from = cursor.getString(cursor.getColumnIndex("child_age_from"));
            String child_age_to = cursor.getString(cursor.getColumnIndex("child_age_to"));



            // Create a new row in the table
            TableRow row = new TableRow(this);

            // Create TextViews for name and activity
            TextView nameTextView = new TextView(this);
            nameTextView.setText(activity_name);
            nameTextView.setPadding(8, 8, 8, 8);
            row.addView(nameTextView);

            TextView activityTextView = new TextView(this);
            activityTextView.setText(activity_type);
            activityTextView.setPadding(8, 8, 8, 8);
            row.addView(activityTextView);

            // Create buttons for delete, edit, and manage requests
            Button deleteButton = new Button(this);
            deleteButton.setText("Delete");
            deleteButton.setPadding(8, 8, 8, 8);
            row.addView(deleteButton);

            Button editButton = new Button(this);
            editButton.setText("Edit");
            editButton.setPadding(8, 8, 8, 8);
            row.addView(editButton);

            Button manageRequestsButton = new Button(this);
            manageRequestsButton.setText("Manage Requests");
            manageRequestsButton.setPadding(8, 8, 8, 8);
            row.addView(manageRequestsButton);

            // Set click listeners for the buttons
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle delete button click
                    Toast.makeText(GroupInfoActivity.this, "Delete button clicked for " + activity_name, Toast.LENGTH_SHORT).show();
                }
            });

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle edit button click
                    Toast.makeText(GroupInfoActivity.this, "Edit button clicked for " + activity_name, Toast.LENGTH_SHORT).show();
                }
            });

            manageRequestsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle manage requests button click
                    Toast.makeText(GroupInfoActivity.this, "Manage Requests button clicked for " + activity_name, Toast.LENGTH_SHORT).show();
                }
            });

            // Add the row to the table
            tableLayout.addView(row);
        }
        cursor.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityDatabaseHelper.close();
        groupsDatabase.close();
        groupsDatabaseHelper.close();
        activityDatabase.close();
    }
}