package com.example.sharecare;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sharecare.Logic.GroupsDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class SearchGroupsActivity extends AppCompatActivity {

    private LinearLayout cityContainer;
    private Button searchButton;

    private List<String> selectedCities = new ArrayList<>();

    private GroupsDatabaseHelper groupsDatabaseHelper;
    private SQLiteDatabase groupsDatabase;
    private int loggedInUserId;
    private String loggedInUsername;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_groups);

        groupsDatabaseHelper = new GroupsDatabaseHelper(this);
        groupsDatabase = groupsDatabaseHelper.getReadableDatabase();
        loggedInUserId = getIntent().getIntExtra("userid",-1);
        loggedInUsername = getIntent().getStringExtra("username");

        //create table if not exist
        groupsDatabaseHelper.onCreate(groupsDatabase);
        cityContainer = findViewById(R.id.cityContainer);
        searchButton = findViewById(R.id.searchButton);


        SQLiteDatabase db = groupsDatabaseHelper.getReadableDatabase();

        // Retrieve groups where the logged-in user is the host
        Cursor cursor = db.rawQuery("select distinct city from groups ", new String[]{});
        addCitiesToTable(cursor);

        cursor.close();
        db.close();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();
            }
        });
    }

    private void addCitiesToTable(Cursor cursor) {
        if (cursor.moveToFirst()) {
            do {
                String city = cursor.getString(cursor.getColumnIndex("city"));
                addCityElement(city);
            } while (cursor.moveToNext());
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
        cityContainer.addView(cityView);
    }

    private void performSearch() {
        Intent intent = new Intent(SearchGroupsActivity.this, SearchResultsActivity.class);

        Bundle extras = new Bundle();
        extras.putInt("userid", loggedInUserId);
        extras.putString("username", loggedInUsername);
        extras.putStringArrayList("selectedCities", new ArrayList<>(selectedCities));
        intent.putExtras(extras);

        startActivity(intent);
    }
}
