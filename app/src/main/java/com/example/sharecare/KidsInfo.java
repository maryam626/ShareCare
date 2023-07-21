package com.example.sharecare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class KidsInfo extends AppCompatActivity {
    private TextView kidNameTv;
    private EditText nameEt;
    private EditText ageEt1;
    private EditText schoolNameEt;
    private Spinner genderSpinner1;
    private Button updateBtn;
    private Button saveBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kids_info);

        kidNameTv = (TextView) findViewById(R.id.kidNameTv);
        nameEt = (EditText) findViewById(R.id.nameEt);
        ageEt1 = (EditText) findViewById(R.id.ageEt1);
        schoolNameEt = (EditText) findViewById(R.id.schoolNameEt);
        genderSpinner1 = (Spinner) findViewById(R.id.genderSpinner1);
        updateBtn = (Button) findViewById(R.id.updateBtn);
        saveBtn = (Button) findViewById(R.id.saveBtn);

    }
}