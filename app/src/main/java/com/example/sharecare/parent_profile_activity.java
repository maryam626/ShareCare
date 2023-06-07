package com.example.sharecare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sharecare.Logic.UsersDatabaseHelper;

import java.util.Locale;

public class parent_profile_activity extends AppCompatActivity {
    private TextView userNameTv;
    private EditText phoneEt1;
    private EditText emailEt1;
    private EditText languageEt;
    private EditText religionEt;
    private EditText passwordEt1;
    private EditText addressEt3;
    private EditText numKidsEt;
    private EditText maritalEt;
    private EditText genderEt;
    private Button updateBtn;
    private Button saveBtn;
    private String id;
    private String userName;
    private String phoneNumber;
    private String email;
    private String address;
    private String password;
    private String numberOfKids;
    private String maritalStatus;
    private String gender;
    private String language;
    private String religion;

    private UsersDatabaseHelper usersDatabaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_profile);

        userNameTv = (TextView) findViewById(R.id.userNameTv);
        phoneEt1 = (EditText) findViewById(R.id.phoneEt1);
        emailEt1 = (EditText) findViewById(R.id.emailEt1);
        languageEt = (EditText) findViewById(R.id.LanguageEt);
        religionEt =(EditText) findViewById(R.id.religionEt);
        passwordEt1 = (EditText) findViewById(R.id.passwordEt1);
        addressEt3 = (EditText) findViewById(R.id.addressEt3);
        numKidsEt = (EditText) findViewById(R.id.numKidsEt);
        maritalEt = (EditText) findViewById(R.id.maritalEt);
        genderEt = (EditText) findViewById(R.id.genderEt);
        updateBtn = (Button) findViewById(R.id.updateBtn);
        saveBtn = (Button) findViewById(R.id.saveBtn);
        id = getIntent().getStringExtra("id");
        userName = getIntent().getStringExtra("username");
        phoneNumber = getIntent().getStringExtra("phone_number");
        email = getIntent().getStringExtra("email");
        address = getIntent().getStringExtra("address");
        password = getIntent().getStringExtra("password");
        numberOfKids = getIntent().getStringExtra("number_of_kids");
        maritalStatus = getIntent().getStringExtra("marital_status");
        gender = getIntent().getStringExtra("gender");
        language = getIntent().getStringExtra("language");
        religion = getIntent().getStringExtra("religion");

        userNameTv.setText(userName);
        phoneEt1.setText(phoneNumber);
        phoneEt1.setEnabled(false);
        emailEt1.setText(email);
        emailEt1.setEnabled(false);
        addressEt3.setText(address);
        addressEt3.setEnabled(false);
        passwordEt1.setText(password);
        passwordEt1.setEnabled(false);
        numKidsEt.setText(numberOfKids);
        numKidsEt.setEnabled(false);
        maritalEt.setText(maritalStatus);
        maritalEt.setEnabled(false);
        genderEt.setText(gender);
        genderEt.setEnabled(false);
        languageEt.setText(language);
        languageEt.setEnabled(false);
        religionEt.setText(religion);
        religionEt.setEnabled(false);





        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailEt1.setEnabled(true);
                phoneEt1.setEnabled(true);
                passwordEt1.setEnabled(true);
                addressEt3.setEnabled(true);
                numKidsEt.setEnabled(true);
                maritalEt.setEnabled(true);
                genderEt.setEnabled(true);
                languageEt.setEnabled(true);
                religionEt.setEnabled(true);


            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailEt1.setEnabled(false);
                phoneEt1.setEnabled(false);
                passwordEt1.setEnabled(false);
                addressEt3.setEnabled(false);
                numKidsEt.setEnabled(false);
                maritalEt.setEnabled(false);
                genderEt.setEnabled(false);
                languageEt.setEnabled(false);
                religionEt.setEnabled(false);

                usersDatabaseHelper = new UsersDatabaseHelper(parent_profile_activity.this);
                usersDatabaseHelper.updateUser(id, userNameTv.getText().toString(), phoneEt1.getText().toString(),emailEt1.getText().toString(), addressEt3.getText().toString(), passwordEt1.getText().toString(), Integer.parseInt(numKidsEt.getText().toString()), maritalEt.getText().toString(), genderEt.getText().toString(), languageEt.getText().toString(), religionEt.getText().toString());

            }
        });


    }
}