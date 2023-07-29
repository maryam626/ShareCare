package com.example.sharecare;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sharecare.Logic.GroupsSQLLiteDatabaseHelper;
import com.example.sharecare.models.GroupDataDTO;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MyGroupsActivity extends AppCompatActivity {
    private static final String TAG = "my groups activity";
    private Button createGroupButton;
    private TableLayout groupsTableLayout;
    private GroupsSQLLiteDatabaseHelper databaseHelper;
    private int loggedInUserId;
    private String loggedInUsername;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_groups);
        loggedInUserId = getIntent().getIntExtra("userid",-1);
        loggedInUsername = getIntent().getStringExtra("username");

        databaseHelper = new GroupsSQLLiteDatabaseHelper(this);
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
        List<GroupDataDTO> groupsList =getGroupsForUser(  getLoggedInUserId());
        addGroupsToTable(groupsList);
    }

    public List<GroupDataDTO> getGroupsForUser(int currentUserId) {
        List<GroupDataDTO> groupsList = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String query = "SELECT distinct " +
                "groups.id AS groupid, " +
                "groups.groupName, " +
                "CASE " +
                "   WHEN groups.hostUserId = ? THEN 1 " +
                "   ELSE 0 " +
                "END AS iamhost " +
                "FROM " +
                "groups " +
                "LEFT JOIN " +
                "groupsRequest ON groups.id = groupsRequest.groupid AND groupsRequest.userid = ? AND groupsRequest.isaccept = 1 " +
                "WHERE " +
                "groups.hostUserId = ? OR groupsRequest.id IS NOT NULL";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(currentUserId), String.valueOf(currentUserId), String.valueOf(currentUserId)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int groupId = cursor.getInt(cursor.getColumnIndex("groupid"));
                String groupName = cursor.getString(cursor.getColumnIndex("groupName"));
                boolean iamHost = cursor.getInt(cursor.getColumnIndex("iamhost")) == 1;

                GroupDataDTO groupData = new GroupDataDTO(groupId, groupName, iamHost);
                groupsList.add(groupData);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        db.close();
        return groupsList;
    }

    private void addGroupsToTable(List<GroupDataDTO> groupsList) {

        for (GroupDataDTO groupdata: groupsList) {
                int groupId = groupdata.getGroupId();
                String groupName = groupdata.getGroupName();
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
                openGroupButton.setText("Open Group");
                openGroupButton.setPadding(8, 8, 8, 8);
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
                            extras.putInt("ishost", isIamHost ? 1 : 0);

                            intent.putExtras(extras);
                            startActivity(intent);
                        }
                    });

                }

                // Add the row to the table
                groupsTableLayout.addView(row);
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
                        deleteGroupFromFirebase(groupId);
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

    private void deleteGroupFromFirebase(int groupId) {

        Task<Void> task = db.collection("Groups").document(String.valueOf(groupId)).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "DocumentSnapshot successfully deleted!");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error deleting document", e);

            }
        });
        while(!task.isComplete()){

        }

    }

    private int getLoggedInUserId() {
        return loggedInUserId;
    }
}
