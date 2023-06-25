package com.example.sharecare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class FillKidsInformation extends AppCompatActivity {

    private Button kid1Btn;
    private Button kid2Btn;
    private Button kid3Btn;
    private Button kid4Btn;
    private Button kid5Btn;
    private Button kid6Btn;
    private Button kid7Btn;
    private Button kid8Btn;
    private Button kid9Btn;
    private Button kid10Btn;
    private Button FinishBtn;

    private ArrayList<Button> kidsButtons;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_kids_information);

        kid1Btn = (Button) findViewById(R.id.kid1Btn);
        kid2Btn = (Button) findViewById(R.id.kid2Btn);
        kid3Btn = (Button) findViewById(R.id.kid3Btn);
        kid4Btn = (Button) findViewById(R.id.kid4Btn);
        kid5Btn = (Button) findViewById(R.id.kid5Btn);
        kid6Btn = (Button) findViewById(R.id.kid6Btn);
        kid7Btn = (Button) findViewById(R.id.kid7Btn);
        kid8Btn = (Button) findViewById(R.id.kid8Btn);
        kid9Btn = (Button) findViewById(R.id.kid9Btn);
        kid10Btn = (Button) findViewById(R.id.kid10Btn);
        FinishBtn = (Button) findViewById(R.id.FinishBtn);

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

        kidsButtons = new ArrayList<>();
        kidsButtons.add(kid1Btn);
        kidsButtons.add(kid2Btn);
        kidsButtons.add(kid3Btn);
        kidsButtons.add(kid4Btn);
        kidsButtons.add(kid5Btn);
        kidsButtons.add(kid6Btn);
        kidsButtons.add(kid7Btn);
        kidsButtons.add(kid8Btn);
        kidsButtons.add(kid9Btn);
        kidsButtons.add(kid10Btn);


        for(int i = 0; i<Integer.parseInt(numberOfKids); i++){
            kidsButtons.get(i).setClickable(true);
            kidsButtons.get(i).setVisibility(View.VISIBLE);
        }




        kid1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FillKidsInformation.this, CreateKidProfile.class);
                startActivity(intent);
            }
        });
        kid2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FillKidsInformation.this, CreateKidProfile.class);
                startActivity(intent);
            }
        });
        kid3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FillKidsInformation.this, CreateKidProfile.class);
                startActivity(intent);
            }
        });
        kid4Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FillKidsInformation.this, CreateKidProfile.class);
                startActivity(intent);
            }
        });
        kid5Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FillKidsInformation.this, CreateKidProfile.class);
                startActivity(intent);
            }
        });
        kid6Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FillKidsInformation.this, CreateKidProfile.class);
                startActivity(intent);
            }
        });
        kid7Btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(FillKidsInformation.this, CreateKidProfile.class);
                        startActivity(intent);
                    }
                }
        );

        kid8Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FillKidsInformation.this, CreateKidProfile.class);
                startActivity(intent);
            }
        });
        kid9Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FillKidsInformation.this, CreateKidProfile.class);
                startActivity(intent);
            }
        });

        kid10Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FillKidsInformation.this, CreateKidProfile.class);
                startActivity(intent);
            }
        });

        FinishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FillKidsInformation.this, "Filling Kids Information finished successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(FillKidsInformation.this, log_in_activity.class);
                startActivity(intent);
            }
        });


    }
}