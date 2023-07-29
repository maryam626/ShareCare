package com.example.sharecare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sharecare.handlers.GroupHandler;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


/**
 * SearchGroupsActivity allows users to search for groups based on selected cities.
 * Users can select cities from a list of available cities and perform a search.
 * The selected cities are passed to the SearchResultsActivity to display search results.
 */
public class SearchGroupsActivity extends AppCompatActivity {

    private LinearLayout cityContainer;
    private Button searchButton;

    private List<String> selectedCities = new ArrayList<>();
    private Spinner languagesSpinner, religionsSpinner;
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
        languagesSpinner = findViewById(R.id.LanguagesSpinner);
        religionsSpinner = findViewById(R.id.ReligionsSpinner);

        loadCitiesFromDatabase();
        loadLanguages();
        loadReligions();
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();
            }
        });
    }

    /**
     * Load cities from the database and populate the cityContainer with city elements (checkboxes).
     */
    private void loadCitiesFromDatabase() {
        List<String> cities = groupHandler.getGroupsDistinctCities();

        for (String city : cities) {
            addCityElement(city);
        }
    }
    private void loadLanguages() {
        groupHandler.open();
        List<String>  languagesList = groupHandler.getAllLanguages();
        groupHandler.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, languagesList);
        setSpinnersDefaultValues(languagesSpinner, languagesList);
        languagesSpinner.setAdapter(adapter);
    }


    private void loadReligions() {
        groupHandler.open();
        List<String>  religionsList = groupHandler.getAllReligions();
        groupHandler.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, religionsList);
        setSpinnersDefaultValues(religionsSpinner, religionsList);
        religionsSpinner.setAdapter(adapter);
    }

    public void setSpinnersDefaultValues(Spinner spinner,List<String> spinnerValues){

        String defaultValue = "--";
        spinnerValues.add(0,defaultValue);
        spinner.setSelection(0);
    }
    /**
     * Add a city element (checkbox) to the cityContainer with the given city name.
     *
     * @param cityName The name of the city to be added as a city element.
     */

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


    /**
     * Perform the search for groups based on the selected cities.
     * If no cities are selected, show an error message.
     * If cities are selected, pass the selected cities to the SearchResultsActivity
     * to display search results.
     */
    private void performSearch() {
        if (selectedCities.isEmpty()) {
            // Show an error message if no cities are selected
            Toast.makeText(this, "Please select at least one city.", Toast.LENGTH_SHORT).show();
            return;
        }
        String language = languagesSpinner.getSelectedItem().toString();
        String religion = religionsSpinner.getSelectedItem().toString();

        Intent intent = new Intent(SearchGroupsActivity.this, SearchResultsActivity.class);

        Bundle extras = new Bundle();
        extras.putInt("userid", loggedInUserId);
        extras.putString("username", loggedInUsername);
        extras.putStringArrayList("selectedCities", new ArrayList<>(selectedCities));
        extras.putString("language", language);
        extras.putString("religion", religion);
        intent.putExtras(extras);
        startActivity(intent);
    }
}
