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

import com.example.sharecare.Logic.ActivityDatabaseHelper;
import com.example.sharecare.Logic.GroupsDatabaseHelper;

public class PendingGroupRequestsActivity extends AppCompatActivity {

    private GroupsDatabaseHelper groupsDatabaseHelper;

    private TableLayout tableLayout;
    private int loggedInUserId;
    private String loggedInUsername;
    private int groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_pending_requests);

        groupsDatabaseHelper= new GroupsDatabaseHelper(this);

        Intent intent = getIntent();
        groupId = intent.getIntExtra("groupid", -1);
        loggedInUserId = getIntent().getIntExtra("userid",-1);
        loggedInUsername = getIntent().getStringExtra("username");

        //create table if not exist

        tableLayout = findViewById(R.id.tableLayout);

        loadPendingRequests();
    }

    private void loadPendingRequests() {
        SQLiteDatabase groupsDatabase = groupsDatabaseHelper.getReadableDatabase();
        // Query the activitiesRequest table to retrieve pending requests (isaccept = 0)
        Cursor cursor = groupsDatabase.rawQuery("SELECT u.id as userid, u.username, g.groupName, gr.requestDate " +
                "FROM groupsRequest gr " +
                "JOIN users u ON gr.userid = u.id " +
                "JOIN groups g ON gr.groupid = g.id " +
                "WHERE gr.isaccept = 0 and g.id=?",  new String[]{String.valueOf(groupId)});

        while (cursor.moveToNext()) {
            int userid = cursor.getInt(cursor.getColumnIndex("userid"));
            String username = cursor.getString(cursor.getColumnIndex("username"));
            String requestDate = cursor.getString(cursor.getColumnIndex("requestDate"));

            // Create a new row in the table
            TableRow row = new TableRow(this);

            // Create TextViews for username, activity name, and request date
            TextView usernameTextView = new TextView(this);
            usernameTextView.setText(username);
            row.addView(usernameTextView);

            TextView dateTextView = new TextView(this);
            dateTextView.setText(requestDate);
            row.addView(dateTextView);

            // Create buttons for accept and reject
            Button acceptButton = new Button(this);
            acceptButton.setText("Accept");
            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateRequestStatus(userid,groupId,true);
                }
            });
            row.addView(acceptButton);

            Button rejectButton = new Button(this);
            rejectButton.setText("Reject");
            rejectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateRequestStatus(userid,groupId,false);
                }
            });
            row.addView(rejectButton);

            // Add the row to the table
            tableLayout.addView(row);
        }

        cursor.close();
        groupsDatabase.close();
    }





    private void updateRequestStatus(int userId, int groupid, boolean isAccept) {
        SQLiteDatabase groupsDatabase = groupsDatabaseHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("isaccept", isAccept ? 1 : 0);

        String whereClause = "userid = ? AND groupid = ?";
        String[] whereArgs = {String.valueOf(userId), String.valueOf(groupid)};

        int rowsAffected = groupsDatabase.update("groupsRequest", values, whereClause, whereArgs);

        if (rowsAffected > 0) {
            Toast.makeText(this, "Request status updated successfully.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to update request status.", Toast.LENGTH_SHORT).show();
        }
        groupsDatabase.close();
    }


    public void returnToPreviousActivity(View view) {
        Intent intent = new Intent(PendingGroupRequestsActivity.this, MyGroupsActivity.class);
        Bundle extras = new Bundle();
        extras.putInt("userid", loggedInUserId);
        extras.putString("username", loggedInUsername);
        intent.putExtras(extras);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        groupsDatabaseHelper.close();
    }
}