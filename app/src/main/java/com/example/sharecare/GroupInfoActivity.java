package com.example.sharecare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sharecare.handlers.GroupHandler;
import com.example.sharecare.models.Activity;

import java.util.List;


/**
 * GroupInfoActivity displays information about a specific group and its activities.
 * It allows the user to view group details, create new activities for the group,
 * view and join existing activities, and manage group activities if the user is the owner of the group.
 */
public class GroupInfoActivity extends AppCompatActivity {
    private GroupHandler groupHandler;
    private int ishost;
    private TableLayout tableLayout;
    private Button createActivityButton;
    private int loggedInUserId;
    private String loggedInUsername;
    private int groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);

        groupHandler = new GroupHandler(this);

        tableLayout = findViewById(R.id.tableLayout);

        // Retrieve the group ID and isHost value from the intent
        Intent intent = getIntent();
        groupId = intent.getIntExtra("groupid", -1);
        loggedInUserId = getIntent().getIntExtra("userid", -1);
        loggedInUsername = getIntent().getStringExtra("username");
        ishost = intent.getIntExtra("ishost", -1);

        createActivityButton = findViewById(R.id.createActivityButton);
        createActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupInfoActivity.this, ChildrenActivityCreateActivity.class);
                Bundle extras = new Bundle();
                extras.putInt("userid", loggedInUserId);
                extras.putInt("groupid", groupId);
                extras.putString("username", loggedInUsername);
                extras.putInt("ishost", ishost);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
        loadGroupData();
        loadGroupActivityData();
    }

    /**
     * Load and display the group name.
     */
    private void loadGroupData() {
        String groupName = groupHandler.getGroupNameById(groupId);
        TextView groupNameTextView = findViewById(R.id.groupNameTextView);
        groupNameTextView.setText("Group Name : " + groupName);
    }

    /**
     * Load and display group activity data.
     */
    private void loadGroupActivityData() {
        List<Activity> activityList = groupHandler.getActivitiesForGroup(groupId, loggedInUserId);

        for (Activity activity : activityList) {
            int activity_id = activity.getId();
            String activity_name = activity.getActivityName();
            String activity_type = activity.getSelectedActivity();
            String date = activity.getSelectedDate();
            String time = activity.getSelectedTime();
            int capacity = activity.getCapacity();
            int child_age_from = activity.getAgeFrom();
            int child_age_to = activity.getAgeTo();
            int isaccept = (activity.getOwnerUserId() == loggedInUserId) ? 1 : 0;
            int isowner = (activity.getGroupId() == loggedInUserId) ? 1 : 0;

            TableRow row = new TableRow(this);

            TextView nameTextView = new TextView(this);
            nameTextView.setText(activity_name);
            row.addView(nameTextView);

            Button moreInfoButton = new Button(this);
            moreInfoButton.setText("More Info");
            moreInfoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openActivityDialog(activity_name, activity_type, date, time, capacity, child_age_from, child_age_to);
                }
            });
            row.addView(moreInfoButton);

            if (isowner == 1) {
                Button deleteButton = new Button(this);
                deleteButton.setText("Delete");
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        groupHandler.deleteActivity(activity_id);
                        Toast.makeText(GroupInfoActivity.this, "successfully deleted " + activity_name, Toast.LENGTH_SHORT).show();
                        reloadGroupActivityData();
                    }
                });
                row.addView(deleteButton);

                Button manageRequestsButton = new Button(this);
                manageRequestsButton.setText("Manage Requests");
                manageRequestsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(GroupInfoActivity.this, PendingGroupRequestsActivity.class);
                        Bundle extras = new Bundle();
                        extras.putInt("userid", loggedInUserId);
                        extras.putInt("groupid", groupId);
                        extras.putString("username", loggedInUsername);
                        extras.putInt("ishost", ishost);
                        intent.putExtras(extras);
                        startActivity(intent);
                    }
                });
                row.addView(manageRequestsButton);
            } else if (isaccept == 1) {
                TextView activityjoinedTextView = new TextView(this);
                activityjoinedTextView.setText("joined");
                row.addView(activityjoinedTextView);
            } else if (isaccept == 0) {
                Button joinButton = new Button(this);
                joinButton.setText("Join");
                joinButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        groupHandler.insertActivityRequest(loggedInUserId, activity_id);
                        Toast.makeText(GroupInfoActivity.this, "Your request submitted to activity owner ", Toast.LENGTH_SHORT).show();
                    }
                });
                row.addView(joinButton);
            }
            tableLayout.addView(row);
        }
    }


    /**
     * Display a dialog with detailed information about the selected activity.
     *
     * @param activity_name   The name of the activity.
     * @param activity_type   The type of the activity.
     * @param date            The date of the activity.
     * @param time            The time of the activity.
     * @param capacity        The capacity of the activity.
     * @param child_age_from  The minimum age requirement for the activity.
     * @param child_age_to    The maximum age requirement for the activity.
     */
    private void openActivityDialog(String activity_name, String activity_type,
                                    String date, String time, int capacity, int child_age_from, int child_age_to) {
        ActivityDialog activityDialog = new ActivityDialog(this, activity_name, activity_type,
                date, time, capacity, child_age_from, child_age_to);
        activityDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /**
     * Reload the group activity data when needed (e.g., after an activity is deleted).
     */
    private void reloadGroupActivityData() {
        tableLayout.removeAllViews();
        loadGroupActivityData();
    }
}
