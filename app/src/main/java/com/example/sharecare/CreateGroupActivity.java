package com.example.sharecare;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sharecare.Logic.GroupsDatabaseHelper;
import com.example.sharecare.Logic.UsersDatabaseHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateGroupActivity extends AppCompatActivity {

    private static final String TAG = "create group activity";


    private EditText groupNameEditText, descriptionEditText,streetEditText;
    FirebaseFirestore db = FirebaseFirestore.getInstance();




    private Spinner participantsSpinner,CitySpinner;
    private Button createButton;
    private GroupsDatabaseHelper databaseHelper;
    private UsersDatabaseHelper usersDatabaseHelper;

    private int loggedInUserId;
    private String loggedInUsername;
    int groupId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        databaseHelper = new GroupsDatabaseHelper(this);
        usersDatabaseHelper= new UsersDatabaseHelper(this);
        groupNameEditText = findViewById(R.id.groupNameEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        streetEditText = findViewById(R.id.streetEditText);

        participantsSpinner = findViewById(R.id.participantsSpinner);
        CitySpinner = findViewById(R.id.CitySpinner);
        createButton = findViewById(R.id.createButton);
        loggedInUserId =  getIntent().getIntExtra("userid",-1);
        loggedInUsername = getIntent().getStringExtra("username");
        loadParticipants();
        loadCities();

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!createGroup())
                    return;
                addingGroupToFirebase();
                Intent intent = new Intent(CreateGroupActivity.this, MyGroupsActivity.class);
                Bundle extras = new Bundle();
                extras.putInt("userid", loggedInUserId);
                extras.putString("username", loggedInUsername);

                intent.putExtras(extras);
                startActivity(intent);

            }
        });
    }

    private void addingGroupToFirebase() {
        Map<String, Object> group = new HashMap<>();
        group.put("id",String.valueOf(groupId));
        group.put("groupName",groupNameEditText.getText().toString());
        group.put("briefInformation",descriptionEditText.getText().toString());
        group.put("host",loggedInUsername);
        group.put("participants",participantsSpinner.getSelectedItem().toString());
        group.put("city",CitySpinner.getSelectedItem().toString());
        group.put("street",streetEditText.getText().toString());


        Task<Void> Task =  db.collection("Groups")
                .document(String.valueOf(groupId)).set(group).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "DocumentSnapshot added");

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);

                    }
                });


        while(!Task.isComplete()){

        }


    }


    private void loadCities() {
        SQLiteDatabase db = usersDatabaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT distinct name from cities order by name asc ", new String[]{});

        List<String> cityList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                String city = cursor.getString(cursor.getColumnIndex("name"));
                cityList.add(city);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cityList);
        CitySpinner.setAdapter(adapter);
    }

    private void loadParticipants() {
        SQLiteDatabase db = usersDatabaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT username FROM users where id<>? ", new String[]{String.valueOf(loggedInUserId)});

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

    private boolean createGroup() {
        String groupName = groupNameEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        String participant = participantsSpinner.getSelectedItem().toString();

        String city = CitySpinner.getSelectedItem().toString();
        String street = streetEditText.getText().toString();


        // Validate the user input
        if (!isGroupNameValid(groupName)) {
            groupNameEditText.setError("Group name must be 2 to 20 characters");
            return false;
        }

        if (!isDescriptionValid(description)) {
            descriptionEditText.setError("Description must be 10 to 30 characters");
            return false;
        }

        if (!isStreetValid(street)) {
            streetEditText.setError("Street must be 5 to 20 characters");
            return false;
        }

        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        // Insert group into groups table
        String groupInsertQuery = "INSERT INTO groups (groupName,description,city,street, hostUserId) VALUES (?,?,?,?,?)";
        db.execSQL(groupInsertQuery, new String[]{groupName, description,city,street,String.valueOf(loggedInUserId)});

        // Retrieve the ID of the newly inserted group
        Cursor cursor = db.rawQuery("SELECT last_insert_rowid()", null);
        groupId = 0;
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
        return true;
    }
    // Helper methods to validate input

    private boolean isGroupNameValid(String groupName) {
        return groupName.length() >= 2 && groupName.length() <= 20;
    }

    private boolean isDescriptionValid(String description) {
        return description.length() >= 10 && description.length() <= 30;
    }

    private boolean isStreetValid(String street) {
        return street.length() >= 5 && street.length() <= 20;
    }

}
