package com.example.sharecare;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sharecare.handlers.FirebaseHandler;
import com.example.sharecare.handlers.GroupHandler;
import com.example.sharecare.handlers.UserHandler;
import com.example.sharecare.models.Group;
import com.example.sharecare.models.GroupDataDTO;
import com.example.sharecare.models.Host;
import com.example.sharecare.valdiators.CreateGroupValidator;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CreateGroupActivity extends AppCompatActivity {
    private static final String TAG = "Create Group";

    private EditText groupNameEditText, descriptionEditText, streetEditText;
    private TextView participantsText,cityText;
    private Spinner participantsSpinner, CitySpinner, languageSpinner, religionSpinner;
    private Button createButton;
    private GroupHandler groupHandler;
    private UserHandler userHandler;
    private FirebaseHandler firebaseHandler;
    private int loggedInUserId;
    private String loggedInUsername;
    private boolean isGroupInserted = false;
    int groupId;

    private boolean isEditMode = false;
    private GroupDataDTO groupObjectToEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        groupHandler = new GroupHandler(this, FirebaseFirestore.getInstance());
        userHandler = new UserHandler(this);
        firebaseHandler = new FirebaseHandler(FirebaseFirestore.getInstance());

        groupNameEditText = findViewById(R.id.groupNameEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        streetEditText = findViewById(R.id.streetEditText);
        participantsText= findViewById(R.id.participantsText);
        cityText= findViewById(R.id.cityText);

        participantsSpinner = findViewById(R.id.participantsSpinner);


        CitySpinner = findViewById(R.id.CitySpinner);
        languageSpinner = findViewById(R.id.LanguagesSpinner);
        religionSpinner = findViewById(R.id.ReligionsSpinner);
        createButton = findViewById(R.id.createButton);
        loggedInUserId = getIntent().getIntExtra("userid", -1);
        loggedInUsername = getIntent().getStringExtra("username");

        participantsSpinner.setVisibility(View.VISIBLE);
        participantsText.setVisibility(View.VISIBLE);
        CitySpinner.setVisibility(View.VISIBLE);
        cityText.setVisibility(View.VISIBLE);

        int isEditModeAsInt = getIntent().getIntExtra("isEdit", 0);
        if (isEditModeAsInt == 1) {
            // The activity was triggered by clicking the edit button
            isEditMode = true;
            groupObjectToEdit = (GroupDataDTO) getIntent().getSerializableExtra("currentgroupdata");

            // Set up edit mode by filling the fields with existing data
            fillFieldsForEditMode(groupObjectToEdit);
        }

        loadParticipants();
        loadCities();
        loadLanguages();
        loadReligions();
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditMode) {
                    showConfirmationDialog();
                } else {
                    if (!createGroup(false))
                        return;

                    if (isGroupInserted) {
                        groupHandler.open();
                        Host host = new Host(log_in_activity.username, log_in_activity.phoneNumber, log_in_activity.email, log_in_activity.address, log_in_activity.password, Integer.parseInt(log_in_activity.numberOfKids), log_in_activity.maritalStatus, log_in_activity.gender, log_in_activity.language, log_in_activity.religion);
                        Group group = new Group(groupNameEditText.getText().toString(), descriptionEditText.getText().toString(), host, CitySpinner.getSelectedItem().toString(), streetEditText.getText().toString(), languageSpinner.getSelectedItem().toString(), religionSpinner.getSelectedItem().toString());
                        groupHandler.addGroupToFirebase(groupId, group);

                        boolean isParticipantInserted = groupHandler.insertGroupParticipant(groupId, participantsSpinner.getSelectedItem().toString());
                        groupHandler.close();

                        if (isParticipantInserted) {
                            groupHandler.Sync();
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
            }
        });
    }


    /** Set up the fields for edit mode by pre-filling them with existing data. */
    private void fillFieldsForEditMode(GroupDataDTO groupDataDTO) {

        Group currentGroup =groupDataDTO.getGroup();
        groupNameEditText.setText(currentGroup.getGroupName());
        descriptionEditText.setText(currentGroup.getDescription());
        streetEditText.setText(currentGroup.getStreet());
        participantsText= findViewById(R.id.participantsText);
        cityText= findViewById(R.id.cityText);

        // Participants Spinner
        participantsSpinner.setVisibility(View.GONE);
        participantsText.setVisibility(View.GONE);
        CitySpinner.setVisibility(View.GONE);
        cityText.setVisibility(View.GONE);


        // City Spinner
        List<String> cities = new ArrayList<>();
        cities.add(currentGroup.getCity());
        Spinner citySpinner = findViewById(R.id.CitySpinner);
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cities);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(cityAdapter);


        // Language Spinner
        String[] languages = {currentGroup.getLanguage()}; // Assuming getLanguage() returns a single language as a string
        Spinner languageSpinner = findViewById(R.id.LanguagesSpinner);
        ArrayAdapter<String> languageAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, languages);
        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(languageAdapter);
        // Find the position of the selected language in the spinner and set it (if needed)

        // Religion Spinner
        String[] religions = {currentGroup.getReligion()}; // Assuming getReligion() returns a single religion as a string
        Spinner religionSpinner = findViewById(R.id.ReligionsSpinner);
        ArrayAdapter<String> religionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, religions);
        religionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        religionSpinner.setAdapter(religionAdapter);
        // Find the position of the selected religion in the spinner and set it (if needed)

     }
    /** Show an AlertDialog to confirm before saving changes. */
    private void showConfirmationDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Confirm Changes");
        alertDialogBuilder.setMessage("Are you sure you want to save the changes?");
        alertDialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                createGroup(true);
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialogBuilder.show();
    }



    /** Load the list of cities from the local database and populate the CitySpinner. */
    private void loadCities() {
        groupHandler.open();
        List<String>  cityList = groupHandler.getAllCities();
        groupHandler.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cityList);
        CitySpinner.setAdapter(adapter);
    }

    /** Load the list of participants from the local database (except the current user) and populate the ParticipantsSpinner. */
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

    /** Load the list of languages from the local database and populate the LanguageSpinner. */
    private void loadLanguages() {
        groupHandler.open();
        List<String>  languagesList = groupHandler.getAllLanguages();
        groupHandler.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, languagesList);
        languageSpinner.setAdapter(adapter);
    }

    /** Load the list of religions from the local database and populate the ReligionSpinner. */
    private void loadReligions() {
        groupHandler.open();
        List<String>  religionsList = groupHandler.getAllReligions();
        groupHandler.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, religionsList);
        religionSpinner.setAdapter(adapter);
    }

    /** Create a new group with the provided data, validate the input,
     * insert the group into the local database, and display appropriate messages. */
    private boolean createGroup(boolean isEditMode) {
        String participant="",city="";
        String groupName = groupNameEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        if(!isEditMode)
        {
            participant = participantsSpinner.getSelectedItem().toString();
            city = CitySpinner.getSelectedItem().toString();
        }

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


        if (isEditMode) {
            // Update the group data in SQLite database
            long groupId = groupObjectToEdit.getGroup().getId(); // Assuming you have a method to get the group ID
            boolean isUpdateSuccessful = groupHandler.updateGroup(groupId, groupName, description, groupObjectToEdit.getGroup().getCity(), street, language, religion, loggedInUserId);

            if (isUpdateSuccessful) {
                // Update the group data in Firebase Firestore
                Group currentGroupToEdit = groupObjectToEdit.getGroup();
                currentGroupToEdit.setGroupName(groupName);
                currentGroupToEdit.setDescription(description);
                currentGroupToEdit.setCity(city);
                currentGroupToEdit.setStreet(street);
                currentGroupToEdit.setLanguage(language);
                currentGroupToEdit.setReligion(religion);

                groupHandler.updateGroupInFirebase(groupId, currentGroupToEdit,
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                groupHandler.Sync();
                                // Show success message using Snackbar
                                showSnackbar("Group data updated successfully!");

                                // Go back to the previous activity
                                Intent intent = new Intent(CreateGroupActivity.this, MyGroupsActivity.class);
                                Bundle extras = new Bundle();
                                extras.putInt("userid", loggedInUserId);
                                extras.putString("username", loggedInUsername);
                                extras.putInt("groupid", (int)groupId);
                                intent.putExtras(extras);
                                startActivity(intent);
                            }
                        },
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Show error message using Snackbar
                                showSnackbar("Failed to update group data in Firebase. Please try again.");
                            }
                        });
                return false;
            } else {
                // Show error message using Snackbar for SQLite update failure
                showSnackbar("Failed to update group data in SQLite. Please try again.");
            }
        } else {
            // Insert group into the local database
            groupHandler.open();
            groupId = (int) groupHandler.insertGroup(groupName, description, city, street, language, religion, loggedInUserId);
            groupHandler.close();

            Host host = new Host(log_in_activity.username, log_in_activity.phoneNumber, log_in_activity.email, log_in_activity.address, log_in_activity.password, Integer.parseInt(log_in_activity.numberOfKids), log_in_activity.maritalStatus, log_in_activity.gender, log_in_activity.language, log_in_activity.religion);

            firebaseHandler.addingHostDataToFirebase(host, new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    groupHandler.Sync();
                    Log.d(TAG, "Host added to Firebase");

                }
            }, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "Adding Host Failed");

                }
            });

            if (groupId == -1) {
                showSnackbar("Failed to create group. Please try again.");
                return false;
            } else {
                isGroupInserted = true;
            }

            return true;
        }
        return true;
    }


    /** Show a Snackbar with the given message. */
    private void showSnackbar(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
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