package com.example.sharecare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sharecare.handlers.GroupHandler;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SearchGroupsActivity extends AppCompatActivity {

    private LinearLayout cityContainer;
    private Button searchButton;

    private List<String> selectedCities = new ArrayList<>();

    private GroupHandler groupHandler; // New instance of GroupHandler class
    private int loggedInUserId;
    private String loggedInUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_groups);

        groupHandler = new GroupHandler(this, FirebaseFirestore.getInstance()); // Initialize GroupHandler
        loggedInUserId = getIntent().getIntExtra("userid", -1);
        loggedInUsername = getIntent().getStringExtra("username");

        cityContainer = findViewById(R.id.cityContainer);
        searchButton = findViewById(R.id.searchButton);

        loadCitiesFromDatabase();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();
            }
        });
    }

    private void loadCitiesFromDatabase() {
        List<String> cities = groupHandler.getGroupsDistinctCities();

        for (String city : cities) {
            addCityElement(city);
        }
    }

    private void addCityElement(String cityName) {
        View cityView = getLayoutInflater().inflate(R.layout.item_city, cityContainer, false);
        CheckBox cityCheckbox = cityView.findViewById(R.id.cityCheckbox);
        cityCheckbox.setText(cityName);
        cityCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedCities.add(cityName);
            } else {
                selectedCities.remove(cityName);
            }
        });

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 0, 0, 8); // Set the margin bottom to add spacing between rows
        cityView.setLayoutParams(layoutParams);
        cityContainer.addView(cityView);
    }

    private void performSearch() {
        if (selectedCities.isEmpty()) {
            // Show an error message if no cities are selected
            Toast.makeText(this, "Please select at least one city.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(SearchGroupsActivity.this, SearchResultsActivity.class);

        Bundle extras = new Bundle();
        extras.putInt("userid", loggedInUserId);
        extras.putString("username", loggedInUsername);
        extras.putStringArrayList("selectedCities", new ArrayList<>(selectedCities));
        intent.putExtras(extras);

        startActivity(intent);
    }
}
