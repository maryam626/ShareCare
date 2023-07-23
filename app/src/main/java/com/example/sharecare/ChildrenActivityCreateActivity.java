package com.example.sharecare;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sharecare.Logic.ActivityDatabaseHelper;
import com.example.sharecare.models.Activity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ChildrenActivityCreateActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "create activity";

    private EditText editActivityName, editSelectedDate, editSelectedTime, editCapacity, editAgeFrom, editAgeTo;
    private Spinner spinnerChooseActivity;
    private Button btnSelectDate, btnSelectTime, btnSave;
    private Calendar calendar;
    private SimpleDateFormat dateFormatter, timeFormatter;

    private String selectedActivity;
    private String[] activityOptions;
    private ActivityDatabaseHelper databaseHelper;

    private int loggedInUserId;
    private String loggedInUsername;

    private int groupId;
    private int ishost;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children_activity_create);

        // Initialize views
        editActivityName = findViewById(R.id.edit_activity_name);
        editSelectedDate = findViewById(R.id.edit_selected_date);
        editSelectedTime = findViewById(R.id.edit_selected_time);
        editCapacity = findViewById(R.id.edit_capacity);
        editAgeFrom = findViewById(R.id.edit_age_from);
        editAgeTo = findViewById(R.id.edit_age_to);
        spinnerChooseActivity = findViewById(R.id.spinner_choose_activity);
        btnSelectDate = findViewById(R.id.btn_select_date);
        btnSelectTime = findViewById(R.id.btn_select_time);
        btnSave = findViewById(R.id.btn_save);

        Intent intent = getIntent();
        groupId = intent.getIntExtra("groupid", -1);
        ishost = intent.getIntExtra("ishost", -1);
        loggedInUserId =  getIntent().getIntExtra("userid",-1);
        loggedInUsername = getIntent().getStringExtra("username");

        // Set click listeners
        btnSelectDate.setOnClickListener(this);
        btnSelectTime.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        // Initialize date and time formatters
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        timeFormatter = new SimpleDateFormat("hh:mm a", Locale.US);

        // Initialize activity options
        activityOptions = getResources().getStringArray(R.array.activity_options);
        databaseHelper = new ActivityDatabaseHelper(this);
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

    @Override
    public void onClick(View v) {
        if (v == btnSelectDate) {
            showDatePickerDialog();
        } else if (v == btnSelectTime) {
            showTimePickerDialog();
        } else if (v == btnSave) {
            if(!saveActivity())
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

    private boolean saveActivity() {
        String activityName = editActivityName.getText().toString().trim();
        String selectedDate = editSelectedDate.getText().toString().trim();
        String selectedTime = editSelectedTime.getText().toString().trim();
        String capacity = editCapacity.getText().toString().trim();
        String ageFrom = editAgeFrom.getText().toString().trim();
        String ageTo = editAgeTo.getText().toString().trim();

        // Clear previous errors
        editActivityName.setError(null);
        editSelectedDate.setError(null);
        editSelectedTime.setError(null);
        editCapacity.setError(null);
        editAgeFrom.setError(null);
        editAgeTo.setError(null);

        // Validate the user input and set errors if necessary
        if (TextUtils.isEmpty(activityName)) {
            editActivityName.setError("Activity name is required");
            editActivityName.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(selectedDate)) {
            editSelectedDate.setError("Date is required");
            editSelectedDate.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(selectedTime)) {
            editSelectedTime.setError("Time is required");
            editSelectedTime.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(capacity)) {
            editCapacity.setError("Capacity is required");
            editCapacity.requestFocus();
            return false;
        }

        int capacityValue, ageFromValue, ageToValue;

        try {
            capacityValue = Integer.parseInt(capacity);
            ageFromValue = Integer.parseInt(ageFrom);
            ageToValue = Integer.parseInt(ageTo);
        } catch (NumberFormatException e) {
            editCapacity.setError("Invalid input for capacity or age");
            editCapacity.requestFocus();
            return false;
        }

        if (capacityValue < 1 || capacityValue > 100) {
            editCapacity.setError("Capacity must be between 1 and 100");
            editCapacity.requestFocus();
            return false;
        }

        if (ageFromValue < 1 || ageFromValue > 99 || ageToValue < 1 || ageToValue > 99) {
            editAgeFrom.setError("Age of child must be between 1 and 99");
            editAgeFrom.requestFocus();
            return false;
        }

        if (ageFromValue >= ageToValue) {
            editAgeTo.setError("Invalid age range");
            editAgeTo.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(activityName)) {
            editActivityName.setError("Activity name is required");
            editActivityName.requestFocus();
            return false;
        } else if (activityName.length() < 2 || activityName.length() > 20) {
            editActivityName.setError("Activity name must be between 2 and 20 characters");
            editActivityName.requestFocus();
            return false;
        }

        // Validate selected date
        if (TextUtils.isEmpty(selectedDate)) {
            editSelectedDate.setError("Please select a date");
            editSelectedDate.requestFocus();
            return false;
        }

        // Get the current date
        Calendar currentDate = Calendar.getInstance();

        // Parse the selected date to compare
        Calendar selectedDateCalendar = Calendar.getInstance();
        try {
            selectedDateCalendar.setTime(dateFormatter.parse(selectedDate));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Compare the selected date with the current date
        if (selectedDateCalendar.compareTo(currentDate) <= 0) {
            editSelectedDate.setError("Please select a date in the future");
            editSelectedDate.requestFocus();
            return false;
        }

        // Create Activity object
        Activity activity = new Activity(activityName, selectedActivity, selectedDate, selectedTime,
                capacityValue, ageFromValue, ageToValue,groupId,loggedInUserId);

        // Store user data in SQLite database
        long rowId = databaseHelper.insertActivity(activity);

        addingActivityDataToFirebase();

        if (rowId != -1) {
            // Successful message
            Toast.makeText(ChildrenActivityCreateActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
        } else {
            // Error message
            Toast.makeText(ChildrenActivityCreateActivity.this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
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

    private void addingActivityDataToFirebase() {
        Map<String, Object> activity = new HashMap<>();
        activity.put("activityName",editActivityName.getText().toString());
        activity.put("selectedActivity",selectedActivity);
        activity.put("selectedDate",editSelectedDate.getText().toString());
        activity.put("selectedTime",editSelectedTime.getText().toString());
        activity.put("capacity",editCapacity.getText().toString());
        activity.put("ageFrom",editAgeFrom.getText().toString());
        activity.put("ageTo",editAgeTo.getText().toString());
        activity.put("ownerUserId",loggedInUserId);
        activity.put("groupId",groupId);

        Task<DocumentReference> referenceTask =  db.collection("Activities")
                .add(activity)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        /*updateIdForParent(documentReference.getId());
                        id = documentReference.getId();
                        Map<String, Object> data = new HashMap<>();
                        data.put("name", "first kid");
                        db.collection("Parents").document(id).collection("myKids").add(data);*/

                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

        while(!referenceTask.isComplete()){

        }
    }
}
