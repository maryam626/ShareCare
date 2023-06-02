package com.example.sharecare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class parent_profile_activity extends AppCompatActivity {
    private ImageView imageView;
    private TextView userNameTv;
    private EditText phoneEt1;
    private EditText emailEt1;
    private EditText passwordEt1;
    private EditText addressEt3;
    private EditText numKidsEt;
    private EditText maritalEt;
    private EditText genderEt;
    private Button updateBtn;
    private Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_profile);

        imageView = (ImageView) findViewById(R.id.imageView);
        userNameTv = (TextView) findViewById(R.id.userNameTv);
        phoneEt1 = (EditText) findViewById(R.id.phoneEt1);
        emailEt1 = (EditText) findViewById(R.id.emailEt1);
        passwordEt1 = (EditText) findViewById(R.id.passwordEt1);
        addressEt3 = (EditText) findViewById(R.id.addressEt3);
        numKidsEt = (EditText) findViewById(R.id.numKidsEt);
        maritalEt = (EditText) findViewById(R.id.maritalEt);
        genderEt = (EditText) findViewById(R.id.genderEt);
        updateBtn = (Button) findViewById(R.id.updateBtn);
        saveBtn = (Button) findViewById(R.id.saveBtn);


    }
}