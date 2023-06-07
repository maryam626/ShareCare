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

import androidx.appcompat.app.AppCompatActivity;

import com.example.sharecare.Logic.GroupsDatabaseHelper;

public class MyGroupsActivity extends AppCompatActivity {

    private Button createGroupButton;
    private TableLayout groupsTableLayout;
    private GroupsDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_groups);

        databaseHelper = new GroupsDatabaseHelper(this);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        databaseHelper.onCreate(db);

        createGroupButton = findViewById(R.id.createGroupButton);
        groupsTableLayout = findViewById(R.id.groupsTableLayout);

        createGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyGroupsActivity.this, CreateGroupActivity.class);
                startActivity(intent);
            }
        });

        loadGroups();
    }

    private void loadGroups() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        // Retrieve groups where the logged-in user is the host
        Cursor hostCursor = db.rawQuery("SELECT * FROM groups WHERE hostUserId = ?", new String[]{String.valueOf(getLoggedInUserId())});
        addGroupsToTable(hostCursor);

        // Retrieve groups where the logged-in user is a participant
        Cursor participantCursor = db.rawQuery("SELECT groups.* FROM groups INNER JOIN groupParticipants " +
                "ON groups.id = groupParticipants.groupId WHERE groupParticipants.userId = ?", new String[]{String.valueOf(getLoggedInUserId())});
        addGroupsToTable(participantCursor);

        hostCursor.close();
        participantCursor.close();
        db.close();
    }

    private void addGroupsToTable(Cursor cursor) {
        if (cursor.moveToFirst()) {
            do {
                int groupId = cursor.getInt(cursor.getColumnIndex("id"));
                String groupName = cursor.getString(cursor.getColumnIndex("groupName"));
                int hostUserId = cursor.getInt(cursor.getColumnIndex("hostUserId"));

                TableRow row = new TableRow(this);
                TextView groupNameTextView = new TextView(this);
                TextView hostUserIdTextView = new TextView(this);

                groupNameTextView.setText(groupName);
                hostUserIdTextView.setText(String.valueOf(hostUserId));

                row.addView(groupNameTextView);
                row.addView(hostUserIdTextView);

                groupsTableLayout.addView(row);
            } while (cursor.moveToNext());
        }
    }

    private int getLoggedInUserId() {
        // Retrieve the logged-in user's ID from your authentication/session mechanism
        // Replace this with your actual implementation
        return 1;
    }
}
