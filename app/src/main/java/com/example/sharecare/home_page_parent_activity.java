package com.example.sharecare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class home_page_parent_activity extends AppCompatActivity {
    private androidx.appcompat.widget.Toolbar toolbar;
    private Button buttonMyGroups;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_parent);
//        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
//
//        setSupportActionBar(toolbar);

        buttonMyGroups = findViewById(R.id.buttonMyGroups);

        buttonMyGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  Intent intent = new Intent(home_page_parent_activity.this, MyGroupsActivity.class);
                  startActivity(intent);
                }
            }
        );

    }
}

