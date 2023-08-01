package com.example.sharecare;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sharecare.handlers.ActivityHandler;
import com.example.sharecare.models.Activity;
import com.example.sharecare.valdiators.CreateActivityValidator;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ChildrenActivityCreateActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editActivityName, editSelectedDate, editSelectedTime, editDuration,editCapacity, editAgeFrom, editAgeTo;
    private Spinner spinnerChooseActivity;
    private Button btnSelectDate, btnSelectTime, btnSave;
    private Calendar calendar;
    private SimpleDateFormat dateFormatter, timeFormatter;
    private String selectedActivity;
    private String[] activityOptions;
    private ActivityHandler activityHandler;
    private int loggedInUserId;
    private String loggedInUsername;
    private int groupId;
    private int ishost;

    private boolean isEditMode = false;
    private Activity activityObjectToEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children_activity_create);

        // Initialize views
        editActivityName = findViewById(R.id.edit_activity_name);
        editSelectedDate = findViewById(R.id.edit_selected_date);
        editSelectedTime = findViewById(R.id.edit_selected_time);
        editCapacity = findViewById(R.id.edit_capacity);
        editDuration = findViewById(R.id.edit_duration);
        editAgeFrom = findViewById(R.id.edit_age_from);
        editAgeTo = findViewById(R.id.edit_age_to);
        spinnerChooseActivity = findViewById(R.id.spinner_choose_activity);
        btnSelectDate = findViewById(R.id.btn_select_date);
        btnSelectTime = findViewById(R.id.btn_select_time);
        btnSave = findViewById(R.id.btn_save);

        Intent intent = getIntent();
        groupId = intent.getIntExtra("groupid", -1);
        ishost = intent.getIntExtra("ishost", -1);
        loggedInUserId =  getIntent().getIntExtra("userid", -1);
        loggedInUsername = getIntent().getStringExtra("username");
        int isEditModeAsInt = getIntent().getIntExtra("isEdit", 0);
        if (isEditModeAsInt == 1) {
            // The activity was triggered by clicking the edit button
            isEditMode = true;
            activityObjectToEdit = (Activity) getIntent().getSerializableExtra("currentActivity");

            // Set up edit mode by filling the fields with existing data
            fillFieldsForEditMode(activityObjectToEdit);
        }


        // Set click listeners
        btnSelectDate.setOnClickListener(this);
        btnSelectTime.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        // Initialize date and time formatters
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        timeFormatter = new SimpleDateFormat("hh:mm a", Locale.US);

        // Initialize activity options
        activityOptions = getResources().getStringArray(R.array.activity_options);
        activityHandler = new ActivityHandler(this);

        // Set up spinner with activity options
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, activityOptions);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerChooseActivity.setAdapter(spinnerAdapter);
        spinnerChooseActivity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedActivity = activityOptions[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedActivity = null;
            }
        });
    }


    /** Set up the fields for edit mode by pre-filling them with existing data. */
    private void fillFieldsForEditMode(Activity activity) {
        editActivityName.setText(activity.getActivityName());
        editSelectedDate.setText(activity.getSelectedDate());
        editSelectedTime.setText(activity.getSelectedTime());
        editCapacity.setText(String.valueOf(activity.getCapacity()));
        editDuration.setText(String.valueOf(activity.getDuration()));
        editAgeFrom.setText(String.valueOf(activity.getAgeFrom()));
        editAgeTo.setText(String.valueOf(activity.getAgeTo()));

        // Find the position of the selected activity in the spinner and set it
        int position = 0;
        String[] activityOptions = getResources().getStringArray(R.array.activity_options);
        for (int i = 0; i < activityOptions.length; i++) {
            if (activity.getSelectedActivity().equals(activityOptions[i])) {
                position = i;
                break;
            }
        }
        spinnerChooseActivity.setSelection(position);
    }
    /** Show an AlertDialog to confirm before saving changes. */
    private void showConfirmationDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Confirm Changes");
        alertDialogBuilder.setMessage("Are you sure you want to save the changes?");
        alertDialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveActivity(true);
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


    /** Show a Snackbar with the given message. */
    private void showSnackbar(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }


    @Override
    public void onClick(View v) {
        if (v == btnSelectDate) {
            showDatePickerDialog();
        } else if (v == btnSelectTime) {
            showTimePickerDialog();
        } else if (v == btnSave) {
            if(isEditMode)
            {
                showConfirmationDialog();
            }
            else {
              if (!saveActivity(false))
                    return;

                Intent intent = new Intent(ChildrenActivityCreateActivity.this, MyGroupsActivity.class);
                Bundle extras = new Bundle();
                extras.putInt("userid", loggedInUserId);
                extras.putString("username", loggedInUsername);
                extras.putInt("groupid", groupId);
                extras.putInt("ishost", ishost);

                intent.putExtras(extras);
                startActivity(intent);
            }
        }
    }

    /** Show DatePickerDialog to select a date and update the editSelectedDate field. */
    private void showDatePickerDialog() {
        calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    calendar.set(year, monthOfYear, dayOfMonth);
                    editSelectedDate.setText(dateFormatter.format(calendar.getTime()));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    /** Show TimePickerDialog to select a time and update the editSelectedTime field. */
    private void showTimePickerDialog() {
        calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    editSelectedTime.setText(timeFormatter.format(calendar.getTime()));
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false);
        timePickerDialog.show();
    }

/** Save the activity data to the local database and Firebase.
 * Validate the user input and set errors if necessary.
 * Display appropriate success/error messages. */
    private boolean saveActivity(boolean isEditMode) {
        String activityName = editActivityName.getText().toString().trim();
        String selectedDate = editSelectedDate.getText().toString().trim();
        String selectedTime = editSelectedTime.getText().toString().trim();
        String capacity = editCapacity.getText().toString().trim();
        String duration = editDuration.getText().toString().trim();
        String ageFrom = editAgeFrom.getText().toString().trim();
        String ageTo = editAgeTo.getText().toString().trim();

        // Validate the user input and set errors if necessary
        if (!CreateActivityValidator.isActivityNameValid(activityName)) {
            editActivityName.setError("Activity name must be between 2 and 20 characters");
            editActivityName.requestFocus();
            return false;
        }

        if (!CreateActivityValidator.isDateValid(selectedDate)) {
            editSelectedDate.setError("Please select a date");
            editSelectedDate.requestFocus();
            return false;
        }

        if (!CreateActivityValidator.isTimeValid(selectedTime)) {
            editSelectedTime.setError("Please select a time");
            editSelectedTime.requestFocus();
            return false;
        }

        if (!CreateActivityValidator.isCapacityValid(capacity)) {
            editCapacity.setError("Capacity must be between 1 and 100");
            editCapacity.requestFocus();
            return false;
        }

        if (!CreateActivityValidator.isDurationValid(duration)) {
            editDuration.setError("Duration must be between 1 and 23");
            editDuration.requestFocus();
            return false;
        }

        if (!CreateActivityValidator.isAgeRangeValid(ageFrom, ageTo)) {
            editAgeFrom.setError("Invalid age range");
            editAgeFrom.requestFocus();
            return false;
        }

        if (isEditMode) {
            // Update the selectedActivity object with the new values
            activityObjectToEdit.setActivityName(activityName);
            activityObjectToEdit.setSelectedDate(selectedDate);
            activityObjectToEdit.setSelectedTime(selectedTime);
            activityObjectToEdit.setCapacity(Integer.parseInt(capacity));
            activityObjectToEdit.setDuration(Integer.parseInt(duration));
            activityObjectToEdit.setAgeFrom(Integer.parseInt(ageFrom));
            activityObjectToEdit.setAgeTo(Integer.parseInt(ageTo));
            activityObjectToEdit.setSelectedActivity(selectedActivity);


            // Set other fields here with the new data

            // Update the activity in the local database and Firebase
            activityHandler.updateActivity(activityObjectToEdit,
                    new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            activityHandler.Sync();
                            // Show success message using Snackbar
                            showSnackbar("Activity data updated successfully!");

                            // Go back to the previous activity
                            Intent intent = new Intent(ChildrenActivityCreateActivity.this, MyGroupsActivity.class);
                            Bundle extras = new Bundle();
                            extras.putInt("userid", loggedInUserId);
                            extras.putString("username", loggedInUsername);
                            extras.putInt("groupid", groupId);
                            extras.putInt("ishost", ishost);
                            intent.putExtras(extras);
                            startActivity(intent);
                        }
                    },
                    new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Show error message using Snackbar
                            showSnackbar("Failed to update activity data. Please try again.");
                        }
                    });
        } else {


            // Create Activity object
            Activity activity = new Activity(activityName, selectedActivity, selectedDate, selectedTime,
                    Integer.parseInt(capacity), Integer.parseInt(duration), Integer.parseInt(ageFrom), Integer.parseInt(ageTo), groupId, loggedInUserId);

            // Store user data in SQLite database
            long rowId = activityHandler.insertActivity(activity);
            activity.setId((int)rowId);
            activityHandler.addingActivityDataToFirebase(activity,
                    new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            activityHandler.Sync();
                            showSnackbar("Creation Was Successful!");
                         }
                    },
                    new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Error message
                            showSnackbar("Creation failed. Please try again.");
                        }

                    });
        }
        // Reset fields
        editActivityName.setText("");
        spinnerChooseActivity.setSelection(0);
        editSelectedDate.setText("");
        editSelectedTime.setText("");
        editCapacity.setText("");
        editAgeFrom.setText("");
        editAgeTo.setText("");
        return true;
    }
}
