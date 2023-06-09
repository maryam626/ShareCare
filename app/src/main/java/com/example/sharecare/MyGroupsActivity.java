package com.example.sharecare;
import android.app.AlertDialog;
import android.content.DialogInterface;


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
import com.example.sharecare.Logic.GroupsDatabaseHelper;

import java.util.HashMap;
import java.util.Map;

public class MyGroupsActivity extends AppCompatActivity {

    private Button createGroupButton;
    private TableLayout groupsTableLayout;
    private GroupsDatabaseHelper databaseHelper;
    private int loggedInUserId;
    private String loggedInUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_groups);
        loggedInUserId = getIntent().getIntExtra("userid",-1);
        loggedInUsername = getIntent().getStringExtra("username");

        databaseHelper = new GroupsDatabaseHelper(this);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        databaseHelper.onCreate(db);

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
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        // Retrieve groups where the logged-in user is the host
        Cursor hostCursor = db.rawQuery("select distinct groupid,groupname,ishost from (SELECT id as groupid,groupname as groupname ,1 as ishost  FROM groups" +
                "  WHERE hostUserId = ? union all SELECT groups.id as groupid, groups.groupname as groupname ,0 as ishost" +
                " FROM groups INNER JOIN groupParticipants ON groups.id = groupParticipants.groupId " +
                " WHERE groupParticipants.userId = ?" +
                "union all SELECT groups.id as groupid,groupname as groupname ,0 as ishost  FROM groups " +
                "INNER JOIN  groupsRequest gr on gr.groupid=groups.id and isaccept=1)", new String[]{String.valueOf(getLoggedInUserId()),String.valueOf(getLoggedInUserId())});
        addGroupsToTable(hostCursor);

        hostCursor.close();

        db.close();
    }

    private void addGroupsToTable(Cursor cursor) {

        if (cursor.moveToFirst()) {
            do {
                int groupId = cursor.getInt(cursor.getColumnIndex("groupid"));
                String groupName = cursor.getString(cursor.getColumnIndex("groupname"));
                int ishost = cursor.getInt(cursor.getColumnIndex("ishost"));

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
                openGroupButton.setText("Open Group");
                openGroupButton.setPadding(8, 8, 8, 8);
                openGroupButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(MyGroupsActivity.this, GroupInfoActivity.class);
                        Bundle extras = new Bundle();
                        extras.putInt("groupid", groupId);
                        extras.putInt("ishost",ishost);
                        extras.putInt("userid", loggedInUserId);
                        extras.putString("username", loggedInUsername);
                        intent.putExtras(extras);
                        startActivity(intent);

                    }
                });
                row.addView(openGroupButton);

                if(ishost ==1)
                {
                    // Create buttons for delete, and manage requests
                    Button deleteButton = new Button(this);
                    deleteButton.setText("Delete");
                  //  deleteButton.setPadding(2, 2, 2, 2);
                    row.addView(deleteButton);

                    //for now we will not support edit button
//                    Button editButton = new Button(this);
//                    editButton.setText("Edit");
//                    editButton.setPadding(8, 8, 8, 8);
//                    row.addView(editButton);

                    Button manageRequestsButton = new Button(this);
                    manageRequestsButton.setText("Manage Requests");
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
                            extras.putInt("ishost", ishost);

                            intent.putExtras(extras);
                            startActivity(intent);
                        }
                    });

                }

                // Add the row to the table
                groupsTableLayout.addView(row);
            } while (cursor.moveToNext());
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
                        SQLiteDatabase db = databaseHelper.getReadableDatabase();

                        db.delete("groupsRequest","groupid = ?", new String[]{String.valueOf(groupId)});
                        db.delete("groupParticipants","groupId = ?", new String[]{String.valueOf(groupId)});
                        db.delete("groups","id = ?", new String[]{String.valueOf(groupId)});
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
