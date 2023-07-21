package com.example.sharecare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

    private String id;
    private String age;
    private String gender;
    private String name;
    private String parent;
    private String schoolName;

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

        id = getIntent().getStringExtra("id");
        age = getIntent().getStringExtra("age");
        gender = getIntent().getStringExtra("gender");
        name = getIntent().getStringExtra("name");
        parent = getIntent().getStringExtra("parent");
        schoolName = getIntent().getStringExtra("schoolName");

        kidNameTv.setText(name);
        nameEt.setText(name);
        ageEt1.setText(age);
        schoolNameEt.setText(schoolName);

        //TODO gender spinner


        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}