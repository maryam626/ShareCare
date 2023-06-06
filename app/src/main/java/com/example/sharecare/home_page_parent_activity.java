package com.example.sharecare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toolbar;

public class home_page_parent_activity extends AppCompatActivity {
    private androidx.appcompat.widget.Toolbar toolbar;
    private Button joiningRequestsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_parent);
        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        joiningRequestsBtn = findViewById(R.id.joiningRequestsBtn);

        setSupportActionBar(toolbar);

        joiningRequestsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home_page_parent_activity.this, joining_requests_activity.class);
                startActivity(intent);
            }
        });
    }
}