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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PendingRequestsActivity extends AppCompatActivity {

    private ActivityDatabaseHelper activityDatabaseHelper;
    private SQLiteDatabase activityDatabase;

    private TableLayout tableLayout;

    private int loggedInUserId;
    private String loggedInUsername;
    private int groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_requests);

        activityDatabaseHelper= new ActivityDatabaseHelper(this);
        activityDatabase = activityDatabaseHelper.getReadableDatabase();
        Intent intent = getIntent();
        groupId = intent.getIntExtra("groupid", -1);
        loggedInUserId = getIntent().getIntExtra("userid",-1);
        loggedInUsername = getIntent().getStringExtra("username");

        //create table if not exist
        activityDatabaseHelper.onCreate(activityDatabase);
        tableLayout = findViewById(R.id.tableLayout);

        loadPendingRequests();
    }

    private void loadPendingRequests() {
        // Query the activitiesRequest table to retrieve pending requests (isaccept = 0)
        Cursor cursor = activityDatabase.rawQuery("SELECT u.id as userid,a.id as activityid, u.username, a.activity_name, r.requestDate " +
                "FROM activitiesRequest r JOIN users u ON r.userid = u.id " +
                "JOIN activities a ON r.activityid = a.id " +
                "WHERE r.isaccept = 0", null);

        while (cursor.moveToNext()) {
            String username = cursor.getString(cursor.getColumnIndex("username"));
            String activityName = cursor.getString(cursor.getColumnIndex("activity_name"));
            String requestDate = cursor.getString(cursor.getColumnIndex("requestDate"));
            int userid = cursor.getInt(cursor.getColumnIndex("userid"));
            int activityid = cursor.getInt(cursor.getColumnIndex("activityid"));

            // Create a new row in the table
            TableRow row = new TableRow(this);

            // Create TextViews for username, activity name, and request date
            TextView usernameTextView = new TextView(this);
            usernameTextView.setText(username);
            row.addView(usernameTextView);

            TextView activityTextView = new TextView(this);
            activityTextView.setText(activityName);
            row.addView(activityTextView);

            TextView dateTextView = new TextView(this);
            dateTextView.setText(requestDate);
            row.addView(dateTextView);

            // Create buttons for accept and reject
            Button acceptButton = new Button(this);
            acceptButton.setText("Accept");
            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateRequestStatus(userid,activityid,true);
                }
            });
            row.addView(acceptButton);

            Button rejectButton = new Button(this);
            rejectButton.setText("Reject");
            rejectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateRequestStatus(userid,activityid,false);
                }
            });
            row.addView(rejectButton);

            // Add the row to the table
            tableLayout.addView(row);
        }

        cursor.close();
    }





    private void updateRequestStatus(int userId, int activityId, boolean isAccept) {
        ContentValues values = new ContentValues();
        values.put("isaccept", isAccept ? 1 : 0);

        String whereClause = "userid = ? AND activityid = ?";
        String[] whereArgs = {String.valueOf(userId), String.valueOf(activityId)};

        int rowsAffected = activityDatabase.update("activitiesRequest", values, whereClause, whereArgs);

        if (rowsAffected > 0) {
            Toast.makeText(this, "Request status updated successfully.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to update request status.", Toast.LENGTH_SHORT).show();
        }
    }


    public void returnToPreviousActivity(View view) {
        Intent intent = new Intent(PendingRequestsActivity.this, GroupInfoActivity.class);
        Bundle extras = new Bundle();
        extras.putInt("userid", loggedInUserId);
        extras.putInt("groupid", groupId);
        extras.putString("username", loggedInUsername);
        intent.putExtras(extras);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityDatabaseHelper.close();
        activityDatabase.close();
    }
}