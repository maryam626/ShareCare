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

import com.example.sharecare.handlers.GroupHandler;
import com.example.sharecare.handlers.UserHandler;
import com.example.sharecare.valdiators.Validator;
import com.example.sharecare.models.User;
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

    private EditText groupNameEditText, descriptionEditText, streetEditText;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Spinner participantsSpinner, CitySpinner;
    private Button createButton;
    private GroupHandler groupHandler;
    private UserHandler userHandler;

    private int loggedInUserId;
    private String loggedInUsername;
    private boolean isGroupInserted = false;
    int groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        groupHandler = new GroupHandler(this);
        userHandler = new UserHandler(this);

        groupNameEditText = findViewById(R.id.groupNameEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        streetEditText = findViewById(R.id.streetEditText);

        participantsSpinner = findViewById(R.id.participantsSpinner);
        CitySpinner = findViewById(R.id.CitySpinner);
        createButton = findViewById(R.id.createButton);
        loggedInUserId = getIntent().getIntExtra("userid", -1);
        loggedInUsername = getIntent().getStringExtra("username");

        loadParticipants();
        loadCities();


        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!createGroup())
                    return;

                // Store the group to the Firebase and SQLite databases
                if (isGroupInserted) {
                    groupHandler.open();
                    boolean isParticipantInserted = groupHandler.insertGroupParticipant(groupId, participantsSpinner.getSelectedItem().toString());
                    groupHandler.close();

                    if (!isParticipantInserted) {
                        Toast.makeText(CreateGroupActivity.this, "Failed to add participant. Please try again.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                // Store the group to the Firebase and SQLite databases
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
        group.put("id", String.valueOf(groupId));
        group.put("groupName", groupNameEditText.getText().toString());
        group.put("briefInformation", descriptionEditText.getText().toString());
        group.put("host", loggedInUsername);
        group.put("participants", participantsSpinner.getSelectedItem().toString());
        group.put("city", CitySpinner.getSelectedItem().toString());
        group.put("street", streetEditText.getText().toString());

        db.collection("Groups")
                .document(String.valueOf(groupId))
                .set(group)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(CreateGroupActivity.this, "Group added to Firebase", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreateGroupActivity.this, "Failed to add group to Firebase", Toast.LENGTH_SHORT).show();
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    private void loadCities() {
        groupHandler.open();
        List<String>  cityList = groupHandler.getAllCities();
        groupHandler.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cityList);
        CitySpinner.setAdapter(adapter);
    }

    private void loadParticipants() {
        groupHandler.open();
        List<String> participantList = groupHandler.getParticipantsExceptCurrent(loggedInUserId);
        groupHandler.close();
        if (participantList.isEmpty()) {
            participantList.add("No other participants available");
        }
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
        if (!Validator.isGroupNameValid(groupName)) {
            groupNameEditText.setError("Group name must be 2 to 20 characters");
            return false;
        }

        if (!Validator.isDescriptionValid(description)) {
            descriptionEditText.setError("Description must be 10 to 30 characters");
            return false;
        }

        if (!Validator.isStreetValid(street)) {
            streetEditText.setError("Street must be 5 to 20 characters");
            return false;
        }

        // Insert group into the local database
        groupHandler.open();
        groupId = (int) groupHandler.insertGroup(groupName, description, city, street, loggedInUserId);
        groupHandler.close();

        if (groupId == -1) {
            Toast.makeText(this, "Failed to create group. Please try again.", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            isGroupInserted = true;
        }

        return true;

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (groupHandler != null) {
            groupHandler.close();
        }
        if (userHandler != null) {
           // userHandler.close();
        }
    }

}
