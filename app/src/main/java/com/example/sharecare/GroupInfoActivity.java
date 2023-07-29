package com.example.sharecare;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sharecare.handlers.GroupHandler;
import com.example.sharecare.models.Activity;
import com.example.sharecare.models.ActivityShareDTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * GroupInfoActivity displays information about a specific group and its activities.
 * It allows the user to view group details, create new activities for the group,
 * view and join existing activities, and manage group activities if the user is the owner of the group.
 */
public class GroupInfoActivity extends AppCompatActivity {
    private GroupHandler groupHandler;
    private int ishost;
    private TableLayout tableLayout;
    private Button createActivityButton;
    private int loggedInUserId;
    private String loggedInUsername;
    private int groupId;
    private LinearLayout filterLayout;
    private Spinner activityTypes;
    private Button filterToggleButton;
    private Button submitFilterButton;


    private Button selectStartDateButton, selectEndDateButton, selectStartTimeButton, selectEndTimeButton;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
    private EditText durationFromEditText, durationToEditText,capacityFromEditText, capacityToEditText, ageFromEditText, ageToEditText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);

        groupHandler = new GroupHandler(this);

        tableLayout = findViewById(R.id.tableLayout);

        // Retrieve the group ID and isHost value from the intent
        Intent intent = getIntent();
        groupId = intent.getIntExtra("groupid", -1);
        loggedInUserId = getIntent().getIntExtra("userid", -1);
        loggedInUsername = getIntent().getStringExtra("username");
        ishost = intent.getIntExtra("ishost", -1);

        createActivityButton = findViewById(R.id.createActivityButton);
        createActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupInfoActivity.this, ChildrenActivityCreateActivity.class);
                Bundle extras = new Bundle();
                extras.putInt("userid", loggedInUserId);
                extras.putInt("groupid", groupId);
                extras.putString("username", loggedInUsername);
                extras.putInt("ishost", ishost);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        selectStartDateButton = findViewById(R.id.selectStartDateButton);
        selectEndDateButton = findViewById(R.id.selectEndDateButton);
        selectStartTimeButton = findViewById(R.id.selectStartTimeButton);
        selectEndTimeButton = findViewById(R.id.selectEndTimeButton);
        capacityFromEditText = findViewById(R.id.capacityFrom);
        capacityToEditText = findViewById(R.id.capacityTo);
        durationFromEditText = findViewById(R.id.durationFrom);
        durationToEditText = findViewById(R.id.durationTo);
        ageFromEditText = findViewById(R.id.ageFrom);
        ageToEditText = findViewById(R.id.ageTo);

        selectStartDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(selectStartDateButton);
            }
        });

        selectEndDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(selectEndDateButton);
            }
        });

        selectStartTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(selectStartTimeButton);
            }
        });

        selectEndTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(selectEndTimeButton);
            }
        });
        // Initialize the new filter components
        filterLayout = findViewById(R.id.filterLayout);

        activityTypes = findViewById(R.id.activityTypes);

        filterToggleButton = findViewById(R.id.filterToggleButton);
        submitFilterButton = findViewById(R.id.submitFilterButton);

        // Toggle filter visibility
        filterToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filterLayout.getVisibility() == View.GONE) {
                    filterLayout.setVisibility(View.VISIBLE);
                } else {
                    filterLayout.setVisibility(View.GONE);
                }
            }
        });

        // Apply filter and load data
        submitFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // You may want to fetch filter values here and use them to filter activities
                // For the sake of this example, I'm just hiding the filter
                filterLayout.setVisibility(View.GONE);

                // Reload the group activity data
                reloadGroupActivityData();
            }
        });

        loadGroupName();
        loadGroupActivityData();
    }

    private void showDatePickerDialog(final Button buttonToUpdate) {
        Calendar currentDate = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);
                buttonToUpdate.setText(dateFormatter.format(selectedDate.getTime()));
                buttonToUpdate.setBackgroundColor(Color.GRAY);  // Change button color to gray

            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void showTimePickerDialog(final Button buttonToUpdate) {
        Calendar currentTime = Calendar.getInstance();

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar selectedTime = Calendar.getInstance();
                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                selectedTime.set(Calendar.MINUTE, minute);
                buttonToUpdate.setText(timeFormatter.format(selectedTime.getTime()));
                buttonToUpdate.setBackgroundColor(Color.GRAY);  // Change button color to gray

            }
        }, currentTime.get(Calendar.HOUR_OF_DAY), currentTime.get(Calendar.MINUTE), true);

        timePickerDialog.show();
    }
    /**
     * Load and display the group name.
     */
    private void loadGroupName() {
        String groupName = groupHandler.getGroupNameById(groupId);
        TextView groupNameTextView = findViewById(R.id.groupNameTextView);
        groupNameTextView.setText("Group Name : " + groupName);
    }

    public int getHoursAndMinutes(Date time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        return hours * 60 + minutes;
    }

    /**
     * Load and display group activity data.
     */
    private void loadGroupActivityData() {
        int capacityFrom = -1;
        int capacityTo = Integer.MAX_VALUE;
        int durationFrom = -1;
        int durationTo = Integer.MAX_VALUE;
        int ageFrom = -1;
        int ageTo = Integer.MAX_VALUE;

        try {
            if(!capacityFromEditText.getText().toString().isEmpty()) {
                capacityFrom = Integer.parseInt(capacityFromEditText.getText().toString());
            }
            if(!capacityToEditText.getText().toString().isEmpty()) {
                capacityTo = Integer.parseInt(capacityToEditText.getText().toString());
            }

            if(!durationFromEditText.getText().toString().isEmpty()) {
                durationFrom = Integer.parseInt(durationFromEditText.getText().toString());
            }
            if(!durationToEditText.getText().toString().isEmpty()) {
                durationTo = Integer.parseInt(durationToEditText.getText().toString());
            }
             if(!ageFromEditText.getText().toString().isEmpty()) {
                ageFrom = Integer.parseInt(ageFromEditText.getText().toString());
            }
            if(!ageToEditText.getText().toString().isEmpty()) {
                ageTo = Integer.parseInt(ageToEditText.getText().toString());
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        String startDateStr = selectStartDateButton.getText().toString();
        String endDateStr = selectEndDateButton.getText().toString();
        String startTimeStr = selectStartTimeButton.getText().toString();
        String endTimeStr = selectEndTimeButton.getText().toString();
        Date filterStartDate = null, filterEndDate = null, filterStartTime = null, filterEndTime = null;

        try {
            if (!startDateStr.isEmpty()) filterStartDate = dateFormatter.parse(startDateStr);
            if (!endDateStr.isEmpty()) filterEndDate = dateFormatter.parse(endDateStr);
            if (!startTimeStr.isEmpty()) filterStartTime = timeFormatter.parse(startTimeStr);
            if (!endTimeStr.isEmpty()) filterEndTime = timeFormatter.parse(endTimeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String selectedActivityType = activityTypes.getSelectedItem().toString();


        List<ActivityShareDTO> activityList = groupHandler.getActivitiesForGroup(groupId, loggedInUserId);
        for (ActivityShareDTO activityShare : activityList) {
            try {
                Activity activity=activityShare.getActivity();
                Date activityDate = dateFormatter.parse(activity.getSelectedDate());
                Date activityTime = timeFormatter.parse(activity.getSelectedTime());

                if (!"All".equals(selectedActivityType) && !selectedActivityType.equals(activity.getSelectedActivity())) {
                    continue; // skip to the next iteration of the loop
                }

                // Check if the activity's date and time fall within the selected range
                if ((filterStartDate == null || !activityDate.before(filterStartDate)) &&
                        (filterEndDate == null || !activityDate.after(filterEndDate)) &&
                        (filterStartTime == null || (getHoursAndMinutes(activityTime) > getHoursAndMinutes(filterStartTime)) &&
                        (filterEndTime == null || (getHoursAndMinutes(activityTime) < getHoursAndMinutes(filterEndTime))))) {


                    if (activity.getCapacity() >= capacityFrom && activity.getCapacity() <= capacityTo &&
                            activity.getAgeFrom() >= ageFrom && activity.getAgeTo() <= ageTo &&
                        activity.getDuration() >= durationFrom && activity.getDuration() <= durationTo ) {

                        int activity_id = activity.getId();
                        String activity_name = activity.getActivityName();
                        String activity_type = activity.getSelectedActivity();
                        String date = activity.getSelectedDate();
                        String time = activity.getSelectedTime();
                        int capacity = activity.getCapacity();
                        int duration = activity.getDuration();
                        int child_age_from = activity.getAgeFrom();
                        int child_age_to = activity.getAgeTo();

                        TableRow row = new TableRow(this);

                        TextView nameTextView = new TextView(this);
                        nameTextView.setText(activity_name);
                        row.addView(nameTextView);

                        Button moreInfoButton = new Button(this);
                        moreInfoButton.setText("More Info");
                        moreInfoButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openActivityDialog(activity_name, activity_type, date, time, capacity,duration, child_age_from, child_age_to);
                            }
                        });
                        row.addView(moreInfoButton);

                        if (activityShare.isiAmOwner()) {
                            Button deleteButton = new Button(this);
                            deleteButton.setText("Delete");
                            deleteButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    groupHandler.deleteActivity(activity_id);
                                    Toast.makeText(GroupInfoActivity.this, "successfully deleted " + activity_name, Toast.LENGTH_SHORT).show();
                                    reloadGroupActivityData();
                                }
                            });
                            row.addView(deleteButton);

                            Button manageRequestsButton = new Button(this);
                            manageRequestsButton.setText("Manage Requests");
                            manageRequestsButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(GroupInfoActivity.this, PendingGroupRequestsActivity.class);
                                    Bundle extras = new Bundle();
                                    extras.putInt("userid", loggedInUserId);
                                    extras.putInt("groupid", groupId);
                                    extras.putString("username", loggedInUsername);
                                    extras.putInt("ishost", ishost);
                                    intent.putExtras(extras);
                                    startActivity(intent);
                                }
                            });
                            row.addView(manageRequestsButton);
                        } else if (activityShare.isSharedWithMe()) {
                            TextView activityjoinedTextView = new TextView(this);
                            activityjoinedTextView.setText("joined");
                            row.addView(activityjoinedTextView);
                        } else if (!activityShare.isSharedWithMe()) {
                            Button joinButton = new Button(this);
                            joinButton.setText("Join");
                            joinButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    groupHandler.insertActivityRequest(loggedInUserId, activity_id);
                                    Toast.makeText(GroupInfoActivity.this, "Your request submitted to activity owner ", Toast.LENGTH_SHORT).show();
                                }
                            });
                            row.addView(joinButton);
                        }
                        tableLayout.addView(row);
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Display a dialog with detailed information about the selected activity.
     *
     * @param activity_name   The name of the activity.
     * @param activity_type   The type of the activity.
     * @param date            The date of the activity.
     * @param time            The time of the activity.
     * @param capacity        The capacity of the activity.
     * @param child_age_from  The minimum age requirement for the activity.
     * @param child_age_to    The maximum age requirement for the activity.
     */
    private void openActivityDialog(String activity_name, String activity_type,
                                    String date, String time, int capacity,int duration, int child_age_from, int child_age_to) {
        ActivityDialog activityDialog = new ActivityDialog(this, activity_name, activity_type,
                date, time, capacity,duration, child_age_from, child_age_to);
        activityDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /**
     * Reload the group activity data when needed (e.g., after an activity is deleted).
     */
    private void reloadGroupActivityData() {
        tableLayout.removeAllViews();
        loadGroupActivityData();
    }
}
