package com.example.sharecare;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sharecare.Logic.GroupsSQLLiteDatabaseHelper;
import com.example.sharecare.handlers.GroupHandler;
import com.example.sharecare.models.Group;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {

    private TableLayout resultTable;
    private int loggedInUserId;
    private String loggedInUsername;
    private GroupsSQLLiteDatabaseHelper databaseHelper;
    private GroupHandler groupHandler; // New instance of GroupHandler class

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        resultTable = findViewById(R.id.resultTable);

        databaseHelper = new GroupsSQLLiteDatabaseHelper(this);
        groupHandler = new GroupHandler(this, FirebaseFirestore.getInstance()); // Initialize GroupHandler

        Intent intent = getIntent();
        loggedInUserId = intent.getIntExtra("userid", -1);
        loggedInUsername = intent.getStringExtra("username");
        List<String> selectedCities = intent.getStringArrayListExtra("selectedCities");

        // Scroll view reference
        HorizontalScrollView horizontalScrollView = findViewById(R.id.horizontalScrollView);

        // Scroll the view to the leftmost position (start) to show the beginning of the table
        horizontalScrollView.post(() -> horizontalScrollView.fullScroll(HorizontalScrollView.FOCUS_LEFT));
        loadGroups(selectedCities);
    }

    private void loadGroups(List<String> selectedCities) {
        groupHandler.open(); // Open the database connection

        List<Group> groups = groupHandler.getGroups(selectedCities, loggedInUserId);

        groupHandler.close(); // Close the database connection

        if (groups.isEmpty()) {
            Toast.makeText(this, "There are no groups that you are not part of! View groups in My Groups section.", Toast.LENGTH_SHORT).show();
        } else {
            addGroupsToTable(groups);
        }
    }

    private void addGroupsToTable(List<Group> groups) {
        resultTable.removeAllViews(); // Clear the table before adding new data

        TableRow headerRow = new TableRow(this);

        // Create TextViews for column headers
        TextView groupNameLabelTextView = new TextView(this);
        groupNameLabelTextView.setText("Group Name");
        groupNameLabelTextView.setPadding(8, 8, 8, 8);
        groupNameLabelTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        groupNameLabelTextView.setTypeface(null, Typeface.BOLD);
        groupNameLabelTextView.setTextColor(getResources().getColor(android.R.color.black));

        headerRow.addView(groupNameLabelTextView);

        TextView descriptionLabelTextView = new TextView(this);
        descriptionLabelTextView.setText("Description");
        descriptionLabelTextView.setPadding(8, 8, 8, 8);
        descriptionLabelTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        descriptionLabelTextView.setTypeface(null, Typeface.BOLD);
        descriptionLabelTextView.setTextColor(getResources().getColor(android.R.color.black));
        headerRow.addView(descriptionLabelTextView);

        TextView cityLabelTextView = new TextView(this);
        cityLabelTextView.setText("City");
        cityLabelTextView.setPadding(8, 8, 8, 8);
        cityLabelTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        cityLabelTextView.setTypeface(null, Typeface.BOLD);
        cityLabelTextView.setTextColor(getResources().getColor(android.R.color.black));
        headerRow.addView(cityLabelTextView);

        TextView streetLabelTextView = new TextView(this);
        streetLabelTextView.setText("Street");
        streetLabelTextView.setPadding(8, 8, 8, 8);
        streetLabelTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        streetLabelTextView.setTypeface(null, Typeface.BOLD);
        streetLabelTextView.setTextColor(getResources().getColor(android.R.color.black));
        headerRow.addView(streetLabelTextView);

        resultTable.addView(headerRow);

        for (Group group : groups) {
            TableRow row = new TableRow(this);

            // Create TextViews for group information
            TextView groupNameTextView = new TextView(this);
            groupNameTextView.setText(group.getGroupName());
            groupNameTextView.setPadding(8, 8, 8, 8);
            row.addView(groupNameTextView);

            TextView descriptionTextView = new TextView(this);
            descriptionTextView.setText(group.getBriefInformation());
            descriptionTextView.setPadding(8, 8, 8, 8);
            row.addView(descriptionTextView);

            TextView cityTextView = new TextView(this);
            cityTextView.setText(group.getCity());
            cityTextView.setPadding(8, 8, 8, 8);
            row.addView(cityTextView);

            TextView streetTextView = new TextView(this);
            streetTextView.setText(group.getStreet());
            streetTextView.setPadding(8, 8, 8, 8);
            row.addView(streetTextView);

            // Create a Button for "Join Group"
            Button openGroupButton = new Button(this);
            openGroupButton.setText("Join Group");
            openGroupButton.setPadding(8, 8, 8, 8);
            openGroupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    joinGroup(group.getId());
                }
            });
            row.addView(openGroupButton);

            resultTable.addView(row);
        }
    }

    private void joinGroup(int groupId) {
        boolean success = groupHandler.insertGroupParticipant(groupId, loggedInUsername);
        if (success) {
            Toast.makeText(this, "Your request has been submitted to the group owner.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to join the group. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
}
