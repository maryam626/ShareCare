package com.example.sharecare;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sharecare.Logic.GroupsDatabaseHelper;
import com.example.sharecare.Logic.UsersDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class CreateGroupActivity extends AppCompatActivity {

    private EditText groupNameEditText, descriptionEditText;
    private Spinner participantsSpinner;
    private Button createButton;
    private GroupsDatabaseHelper databaseHelper;
    private UsersDatabaseHelper usersDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        databaseHelper = new GroupsDatabaseHelper(this);
        usersDatabaseHelper= new UsersDatabaseHelper(this);
        groupNameEditText = findViewById(R.id.groupNameEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        participantsSpinner = findViewById(R.id.participantsSpinner);
        createButton = findViewById(R.id.createButton);

        loadParticipants();

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createGroup();
            }
        });
    }

    private void loadParticipants() {
        SQLiteDatabase db = usersDatabaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT username FROM users", null);

        List<String> participantList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                String username = cursor.getString(cursor.getColumnIndex("username"));
                participantList.add(username);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, participantList);
        participantsSpinner.setAdapter(adapter);
    }

    private void createGroup() {
        String groupName = groupNameEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        String participant = participantsSpinner.getSelectedItem().toString();

        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        // Insert group into groups table
        String groupInsertQuery = "INSERT INTO groups (groupName, hostUserId) VALUES (?, ?)";
        db.execSQL(groupInsertQuery, new String[]{groupName, String.valueOf(getLoggedInUserId())});

        // Retrieve the ID of the newly inserted group
        Cursor cursor = db.rawQuery("SELECT last_insert_rowid()", null);
        int groupId = 0;
        if (cursor.moveToFirst()) {
            groupId = cursor.getInt(0);
        }
        cursor.close();

        // Insert group participant into groupParticipants table
        String participantInsertQuery = "INSERT INTO groupParticipants (groupId, userId) " +
                "SELECT ?, id FROM users WHERE username = ?";
        db.execSQL(participantInsertQuery, new String[]{String.valueOf(groupId), participant});

        db.close();

        finish();
    }

    private int getLoggedInUserId() {
        // Retrieve the logged-in user's ID from your authentication/session mechanism
        // Replace this with your actual implementation
        return 1;
    }
}