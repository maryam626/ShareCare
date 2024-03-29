package com.example.sharecare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

    private ImageView checkedKid1;
    private ImageView checkedKid2;
    private ImageView checkedKid3;
    private ImageView checkedKid4;
    private ImageView checkedKid5;
    private ImageView checkedKid6;
    private ImageView checkedKid7;
    private ImageView checkedKid8;
    private ImageView checkedKid9;
    private ImageView checkedKid10;

    public static boolean done1;
    public static boolean done2;
    public static boolean done3;
    public static boolean done4;
    public static boolean done5;
    public static boolean done6;
    public static boolean done7;
    public static boolean done8;
    public static boolean done9;
    public static boolean done10;




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

        checkedKid1 = (ImageView) findViewById(R.id.checkedKid1);
        checkedKid2 = (ImageView) findViewById(R.id.checkedKid2);
        checkedKid3 = (ImageView) findViewById(R.id.checkedKid3);
        checkedKid4 = (ImageView) findViewById(R.id.checkedKid4);
        checkedKid5 = (ImageView) findViewById(R.id.checkedKid5);
        checkedKid6 = (ImageView) findViewById(R.id.checkedKid6);
        checkedKid7 = (ImageView) findViewById(R.id.checkedKid7);
        checkedKid8 = (ImageView) findViewById(R.id.checkedKid8);
        checkedKid9 = (ImageView) findViewById(R.id.checkedKid9);
        checkedKid10 = (ImageView) findViewById(R.id.checkedKid10);


        numberOfKids = getIntent().getStringExtra("number_of_kids");
        id = getIntent().getStringExtra("id");
        userName = getIntent().getStringExtra("username");
        phoneNumber = getIntent().getStringExtra("phone_number");
        email = getIntent().getStringExtra("email");
        address = getIntent().getStringExtra("address");
        password = getIntent().getStringExtra("password");
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


        checkDoneVariables();

        kid1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FillKidsInformation.this, CreateKidProfile.class);
                Bundle extras = new Bundle();
                extras.putString("number_of_kids",numberOfKids);
                extras.putInt("kid number", 1);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
        kid2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FillKidsInformation.this, CreateKidProfile.class);
                Bundle extras = new Bundle();
                extras.putString("number_of_kids",numberOfKids);
                extras.putInt("kid number", 2);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
        kid3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FillKidsInformation.this, CreateKidProfile.class);
                Bundle extras = new Bundle();
                extras.putString("number_of_kids",numberOfKids);
                extras.putInt("kid number", 3);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
        kid4Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FillKidsInformation.this, CreateKidProfile.class);
                Bundle extras = new Bundle();
                extras.putString("number_of_kids",numberOfKids);
                extras.putInt("kid number", 4);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
        kid5Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FillKidsInformation.this, CreateKidProfile.class);
                Bundle extras = new Bundle();
                extras.putString("number_of_kids",numberOfKids);
                extras.putInt("kid number", 5);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
        kid6Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FillKidsInformation.this, CreateKidProfile.class);
                Bundle extras = new Bundle();
                extras.putString("number_of_kids",numberOfKids);
                extras.putInt("kid number", 6);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
        kid7Btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(FillKidsInformation.this, CreateKidProfile.class);
                        Bundle extras = new Bundle();
                        extras.putString("number_of_kids",numberOfKids);
                        extras.putInt("kid number", 7);
                        intent.putExtras(extras);
                        startActivity(intent);
                    }
                }
        );

        kid8Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FillKidsInformation.this, CreateKidProfile.class);
                Bundle extras = new Bundle();
                extras.putString("number_of_kids",numberOfKids);
                extras.putInt("kid number", 8);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
        kid9Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FillKidsInformation.this, CreateKidProfile.class);
                Bundle extras = new Bundle();
                extras.putString("number_of_kids",numberOfKids);
                extras.putInt("kid number", 9);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        kid10Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FillKidsInformation.this, CreateKidProfile.class);
                Bundle extras = new Bundle();
                extras.putString("number_of_kids",numberOfKids);
                extras.putInt("kid number", 10);
                intent.putExtras(extras);
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

    private void checkDoneVariables() {
        if(done1){
            kid1Btn.setVisibility(View.INVISIBLE);
            checkedKid1.setVisibility(View.VISIBLE);
        }
        if(done2){
            kid2Btn.setVisibility(View.INVISIBLE);
            checkedKid2.setVisibility(View.VISIBLE);
        }
        if(done3){
            kid3Btn.setVisibility(View.INVISIBLE);
            checkedKid3.setVisibility(View.VISIBLE);
        }
        if(done4){
            kid4Btn.setVisibility(View.INVISIBLE);
            checkedKid4.setVisibility(View.VISIBLE);
        }
        if(done5){
            kid5Btn.setVisibility(View.INVISIBLE);
            checkedKid5.setVisibility(View.VISIBLE);
        }
        if(done6){
            kid6Btn.setVisibility(View.INVISIBLE);
            checkedKid6.setVisibility(View.VISIBLE);
        }
        if(done7){
            kid7Btn.setVisibility(View.INVISIBLE);
            checkedKid7.setVisibility(View.VISIBLE);
        }
        if(done8){
            kid8Btn.setVisibility(View.INVISIBLE);
            checkedKid8.setVisibility(View.VISIBLE);
        }
        if(done9){
            kid9Btn.setVisibility(View.INVISIBLE);
            checkedKid9.setVisibility(View.VISIBLE);
        }
        if(done10){
            kid10Btn.setVisibility(View.INVISIBLE);
            checkedKid10.setVisibility(View.VISIBLE);
        }

    }
}