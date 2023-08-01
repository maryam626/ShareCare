package com.example.sharecare;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.example.sharecare.models.GroupDataDTO;

import java.util.List;

public class MyGroupsActivity extends AppCompatActivity {
    private Button createGroupButton;
    private TableLayout groupsTableLayout;
    private int loggedInUserId;
    private String loggedInUsername;
    private GroupHandler groupHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_groups);
        groupHandler = new GroupHandler(this);
        loggedInUserId = getIntent().getIntExtra("userid",-1);
        loggedInUsername = getIntent().getStringExtra("username");
        createGroupButton = findViewById(R.id.createGroupButton);
        groupsTableLayout = findViewById(R.id.groupsTableLayout);

        createGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyGroupsActivity.this, CreateGroupActivity.class);
                Bundle extras = new Bundle();
                extras.putInt("userid", loggedInUserId);
                extras.putString("username", loggedInUsername);

                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        loadGroups();
    }

    private void loadGroups() {
        List<GroupDataDTO> groupsList =groupHandler.getGroupsForUser(getLoggedInUserId());
        addGroupsToTable(groupsList);
    }


    private void addGroupsToTable(List<GroupDataDTO> groupsList) {

        for (GroupDataDTO groupdata: groupsList) {
                int groupId = groupdata.getGroup().getId();
                String groupName = groupdata.getGroup().getGroupName();
                boolean isIamHost = groupdata.isIamHost();

                // Create a new row in the table
                TableRow row = new TableRow(this);

                // Create TextViews for group name and group ID
                TextView groupNameTextView = new TextView(this);
                groupNameTextView.setText(groupName);
                groupNameTextView.setPadding(1, 1, 1, 1);
                row.addView(groupNameTextView);

                TextView groupIdTextView = new TextView(this);
                groupIdTextView.setText(String.valueOf(groupId));
                groupIdTextView.setPadding(1, 1, 1, 1);
                row.addView(groupIdTextView);

                // Create a Button for "Open Group"
                Button openGroupButton = new Button(this);
                openGroupButton.setText("Open");
                openGroupButton.setPadding(2, 2, 2, 2);
                openGroupButton.setLayoutParams(new TableRow.LayoutParams(250, TableRow.LayoutParams.WRAP_CONTENT, 1.0f)); // Set weight to make it occupy available space

            openGroupButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(MyGroupsActivity.this, GroupInfoActivity.class);
                        Bundle extras = new Bundle();
                        extras.putInt("groupid", groupId);
                        extras.putInt("ishost",isIamHost ? 1 : 0);
                        extras.putInt("userid", loggedInUserId);
                        extras.putString("username", loggedInUsername);
                        intent.putExtras(extras);
                        startActivity(intent);

                    }
                });
                row.addView(openGroupButton);

                if(isIamHost)
                {
                    // Create buttons for delete, and manage requests


                    Button deleteButton = new Button(this);
                    deleteButton.setText("Delete");

                    deleteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDeleteConfirmationDialog(groupId, groupName);
                        }
                    });
                    deleteButton.setLayoutParams(new TableRow.LayoutParams(300, TableRow.LayoutParams.WRAP_CONTENT, 1.0f)); // Set weight to make it occupy available space

                    row.addView(deleteButton);

                    Button editButton = new Button(this);
                    editButton.setText("Edit");
                   // editButton.setPadding(2, 2, 2, 2);

                    editButton.setLayoutParams(new TableRow.LayoutParams(250, TableRow.LayoutParams.WRAP_CONTENT, 1.0f)); // Set weight to make it occupy available space

                    editButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(MyGroupsActivity.this, CreateGroupActivity.class);
                            Bundle extras = new Bundle();
                            extras.putInt("userid", loggedInUserId);
                            extras.putInt("groupid", groupId);
                            extras.putString("username", loggedInUsername);
                            extras.putInt("ishost", isIamHost ? 1 : 0);
                            extras.putInt("isEdit", 1);
                            extras.putSerializable("currentgroupdata", groupdata);
                            intent.putExtras(extras);
                            startActivity(intent);
                        }
                    });
                    row.addView(editButton);

                    Button manageRequestsButton = new Button(this);
                    manageRequestsButton.setText("Manage");
                    //  manageRequestsButton.setPadding(8, 8, 8, 8);
                    row.addView(manageRequestsButton);

                    // Set click listeners for the buttons
                    deleteButton.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            showDeleteConfirmationDialog(groupId,groupName);
                        }
                    });

                    manageRequestsButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(MyGroupsActivity.this, PendingGroupRequestsActivity.class);
                            Bundle extras = new Bundle();
                            extras.putInt("userid", loggedInUserId);
                            extras.putInt("groupid", groupId);
                            extras.putString("username", loggedInUsername);
                            extras.putInt("ishost", isIamHost ? 1 : 0);

                            intent.putExtras(extras);
                            startActivity(intent);
                        }
                    });

                }
            groupsTableLayout.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));


            }
        }


    private void showDeleteConfirmationDialog(int groupId,String groupName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Confirmation")
                .setMessage("Are you sure you want to delete this row?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Perform the delete operation here
                         groupHandler.deleteGroup(groupId);
                        Toast.makeText(MyGroupsActivity.this, "successfully deleted " + groupName, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MyGroupsActivity.this, MyGroupsActivity.class);
                        Bundle extras = new Bundle();
                        extras.putInt("userid", loggedInUserId);
                        extras.putString("username", loggedInUsername);
                        intent.putExtras(extras);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }



    private int getLoggedInUserId() {
        return loggedInUserId;
    }
}
