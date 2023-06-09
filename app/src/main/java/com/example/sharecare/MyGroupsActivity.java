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

import com.example.sharecare.Logic.GroupsDatabaseHelper;

public class MyGroupsActivity extends AppCompatActivity {

    private Button createGroupButton;
    private TableLayout groupsTableLayout;
    private GroupsDatabaseHelper databaseHelper;
    private String loggedInUserId;
    private String loggedInUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_groups);
        loggedInUserId = getIntent().getStringExtra("userid");
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
                extras.putString("userid", loggedInUserId);
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
        Cursor hostCursor = db.rawQuery("select distinct groupid,groupname from (SELECT id as groupid,groupname as groupname  FROM groups" +
                "  WHERE hostUserId = ? union all SELECT groups.id as groupid, groups.groupname as groupname" +
                " FROM groups INNER JOIN groupParticipants ON groups.id = groupParticipants.groupId " +
                " WHERE groupParticipants.userId = ?)", new String[]{String.valueOf(getLoggedInUserId()),String.valueOf(getLoggedInUserId())});
        addGroupsToTable(hostCursor);

        hostCursor.close();

        db.close();
    }




    private void addGroupsToTable(Cursor cursor) {
        if (cursor.moveToFirst()) {
            do {
                int groupId = cursor.getInt(cursor.getColumnIndex("groupid"));
                String groupName = cursor.getString(cursor.getColumnIndex("groupname"));

                // Create a new row in the table
                TableRow row = new TableRow(this);

                // Create TextViews for group name and group ID
                TextView groupNameTextView = new TextView(this);
                groupNameTextView.setText(groupName);
                groupNameTextView.setPadding(8, 8, 8, 8);
                row.addView(groupNameTextView);

                TextView groupIdTextView = new TextView(this);
                groupIdTextView.setText(String.valueOf(groupId));
                groupIdTextView.setPadding(8, 8, 8, 8);
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
                        extras.putString("isHost","true");

                        intent.putExtras(extras);
                        startActivity(intent);

                    }
                });
                row.addView(openGroupButton);

                // Add the row to the table
                groupsTableLayout.addView(row);
            } while (cursor.moveToNext());
        }
    }

    private int getLoggedInUserId() {
        return Integer.parseInt(loggedInUserId);
    }
}
