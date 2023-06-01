package com.example.sharecare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class create_children_profiles_activity extends AppCompatActivity {
    private Button continueBtn;
    private int numberOfKids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_children_profiles);

        continueBtn = (Button) findViewById(R.id.continueBtn);


        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}