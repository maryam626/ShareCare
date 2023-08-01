package com.example.sharecare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sharecare.handlers.ActivityHandler;
import com.example.sharecare.models.PendingActivityRequestDTO;

import java.util.List;

public class PendingRequestsActivity extends AppCompatActivity {

    private TableLayout tableLayout;
    private int loggedInUserId;
    private String loggedInUsername;
    private int groupId;
    private ActivityHandler activityHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_requests);

        Intent intent = getIntent();
        groupId = intent.getIntExtra("groupid", -1);
        loggedInUserId = getIntent().getIntExtra("userid", -1);
        loggedInUsername = getIntent().getStringExtra("username");
         activityHandler = new ActivityHandler(this);

        tableLayout = findViewById(R.id.tableLayout);
        loadPendingRequests();
    }

    private void loadPendingRequests() {
        List<PendingActivityRequestDTO> pendingActivityRequestList = activityHandler.getPendingActivityRequestsByGroupId(groupId);

        for (PendingActivityRequestDTO request: pendingActivityRequestList) {
            String username = request.getUsername();
            String activityName = request.getActivityName();
            String requestDate = request.getRequestDate();
            int userid = request.getUserId();
            int activityid = request.getActivityId();

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
                    activityHandler.updateRequestStatus(userid, activityid, true);
                    returnToPreviousActivity(v);
                }
            });
            row.addView(acceptButton);

            Button rejectButton = new Button(this);
            rejectButton.setText("Reject");
            rejectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activityHandler.updateRequestStatus(userid, activityid, false);
                    returnToPreviousActivity(v);
                }
            });
            row.addView(rejectButton);

            // Add the row to the table
            tableLayout.addView(row);
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
     }
}