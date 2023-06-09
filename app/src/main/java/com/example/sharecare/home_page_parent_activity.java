package com.example.sharecare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class home_page_parent_activity extends AppCompatActivity {
    private androidx.appcompat.widget.Toolbar toolbar;
    private Button buttonMyGroups;
    private Button myProfileBtn;
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
    private TextView nameTv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_parent);
//        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
//
//        setSupportActionBar(toolbar);

        buttonMyGroups = findViewById(R.id.buttonMyGroups);
        myProfileBtn = findViewById(R.id.myProfileBtn);
        nameTv = (TextView) findViewById(R.id.nameTv);
        id = getIntent().getStringExtra("id");
        userName = getIntent().getStringExtra("username");
        userName = userName.substring(0,1).toUpperCase(Locale.ROOT) + userName.substring(1);
        nameTv.setText(userName.substring(0,1).toUpperCase(Locale.ROOT) + userName.substring(1));
        phoneNumber = getIntent().getStringExtra("phone_number");
        email = getIntent().getStringExtra("email");
        address = getIntent().getStringExtra("address");
        password = getIntent().getStringExtra("password");
        numberOfKids = getIntent().getStringExtra("number_of_kids");
        maritalStatus = getIntent().getStringExtra("marital_status");
        gender = getIntent().getStringExtra("gender");
        language = getIntent().getStringExtra("language");
        religion = getIntent().getStringExtra("religion");



        buttonMyGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  Intent intent = new Intent(home_page_parent_activity.this, MyGroupsActivity.class);
                Bundle extras = new Bundle();
                extras.putString("userid", id);
                extras.putString("username", userName);

                intent.putExtras(extras);
                startActivity(intent);
                }
            }
        );
        myProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home_page_parent_activity.this, parent_profile_activity.class);
                //Sending Data To Profile Using Bundle
                Bundle extras = new Bundle();
                extras.putString("id", id);
                extras.putString("username", userName);
                extras.putString("phone_number", phoneNumber);
                extras.putString("email", email);
                extras.putString("address",address);
                extras.putString("password", password);
                extras.putString("number_of_kids", numberOfKids);
                extras.putString("marital_status", maritalStatus);
                extras.putString("gender", gender);
                extras.putString("language", language);
                extras.putString("religion", religion);

                intent.putExtras(extras);
                startActivity(intent);
            }
        });

    }

}

