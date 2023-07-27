package com.example.sharecare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sharecare.handlers.GroupHandler;
import com.example.sharecare.handlers.UserHandler;
import com.example.sharecare.valdiators.CreateGroupValidator;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class CreateGroupActivity extends AppCompatActivity {
    private EditText groupNameEditText, descriptionEditText, streetEditText;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Spinner participantsSpinner, CitySpinner, languageSpinner, religionSpinner;
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

        groupHandler = new GroupHandler(this, db);
        userHandler = new UserHandler(this);

        groupNameEditText = findViewById(R.id.groupNameEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        streetEditText = findViewById(R.id.streetEditText);

        participantsSpinner = findViewById(R.id.participantsSpinner);
        CitySpinner = findViewById(R.id.CitySpinner);
        languageSpinner = findViewById(R.id.LanguagesSpinner);
        religionSpinner = findViewById(R.id.ReligionsSpinner);
        createButton = findViewById(R.id.createButton);
        loggedInUserId = getIntent().getIntExtra("userid", -1);
        loggedInUsername = getIntent().getStringExtra("username");

        loadParticipants();
        loadCities();
        loadLanguages();
        loadReligions();
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!createGroup())
                    return;

                if (isGroupInserted) {
                    groupHandler.open();
                    boolean isParticipantInserted = groupHandler.insertGroupParticipant(groupId, participantsSpinner.getSelectedItem().toString());
                    groupHandler.close();

                    if (isParticipantInserted) {
                        // Successful message
                        Toast.makeText(CreateGroupActivity.this, "successfully created group", Toast.LENGTH_SHORT).show();
                    } else {
                        // Error message
                        Toast.makeText(CreateGroupActivity.this, "something went wrong , failed to store group", Toast.LENGTH_SHORT).show();
                    }
                }

                Intent intent = new Intent(CreateGroupActivity.this, MyGroupsActivity.class);
                Bundle extras = new Bundle();
                extras.putInt("userid", loggedInUserId);
                extras.putString("username", loggedInUsername);
                intent.putExtras(extras);
                startActivity(intent);
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

    private void loadLanguages() {
        groupHandler.open();
        List<String>  languagesList = groupHandler.getAllLanguages();
        groupHandler.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, languagesList);
        languageSpinner.setAdapter(adapter);
    }

    private void loadReligions() {
        groupHandler.open();
        List<String>  religionsList = groupHandler.getAllReligions();
        groupHandler.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, religionsList);
        religionSpinner.setAdapter(adapter);
    }
    private boolean createGroup() {
        String groupName = groupNameEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        String participant = participantsSpinner.getSelectedItem().toString();
        String city = CitySpinner.getSelectedItem().toString();
        String street = streetEditText.getText().toString();
        String language = languageSpinner.getSelectedItem().toString();
        String religion = religionSpinner.getSelectedItem().toString();

        // Validate the user input
        if (!CreateGroupValidator.isGroupNameValid(groupName)) {
            groupNameEditText.setError("Group name must be 2 to 20 characters");
            return false;
        }

        if (!CreateGroupValidator.isDescriptionValid(description)) {
            descriptionEditText.setError("Description must be 10 to 30 characters");
            return false;
        }

        if (!CreateGroupValidator.isStreetValid(street)) {
            streetEditText.setError("Street must be 5 to 20 characters");
            return false;
        }

        // Insert group into the local database
        groupHandler.open();
        groupId = (int) groupHandler.insertGroup(groupName, description, city, street, language, religion, loggedInUserId);
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