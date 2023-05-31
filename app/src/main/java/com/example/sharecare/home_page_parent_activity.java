package com.example.sharecare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toolbar;

public class home_page_parent_activity extends AppCompatActivity {
    private androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_parent);
        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
    }
}