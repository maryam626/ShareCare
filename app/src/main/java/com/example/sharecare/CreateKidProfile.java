package com.example.sharecare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class CreateKidProfile extends AppCompatActivity {

    private EditText nameEt;
    private EditText ageEt1;
    private EditText schoolNameEt;
    private Spinner genderSpinner1;
    private Button finishBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_kid_profile);

        nameEt = (EditText) findViewById(R.id.nameEt);
        ageEt1 = (EditText) findViewById(R.id.ageEt1);
        schoolNameEt = (EditText) findViewById(R.id.schoolNameEt);
        genderSpinner1 = (Spinner) findViewById(R.id.genderSpinner1);
        finishBtn = (Button) findViewById(R.id.finishBtn);

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}