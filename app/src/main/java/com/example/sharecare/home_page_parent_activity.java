package com.example.sharecare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.sharecare.models.User;

import java.util.Locale;

public class home_page_parent_activity extends AppCompatActivity {
    private androidx.appcompat.widget.Toolbar toolbar;
    private Button buttonMyGroups;
    private Button myProfileBtn;
    private Button buttonSearchGroups;
    private Button myChildrenButton;
    private ImageView logOutIv;
    private Button homePageFragment;
    private Button infoFragment;
    private TextView textView10;
    private TextView nameTv2;



    public static String id;
    public static String userName;
    public static String phoneNumber;
    public static String email;
    public static String address;
    public static String password;
    public static String numberOfKids;
    public static String maritalStatus;
    public static String gender;
    public static String language;
    public static String religion;

    public static User loggedInUser;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_parent);
//        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
//
//        setSupportActionBar(toolbar);

        buttonSearchGroups = findViewById(R.id.buttonSearchGroups);
        myChildrenButton = findViewById(R.id.myChildrenButton);
        textView10 = findViewById(R.id.textView10);
        homePageFragment = findViewById(R.id.homePageFragment);
        infoFragment = findViewById(R.id.infoFragment);
        nameTv2 = findViewById(R.id.nameTv2);

        buttonMyGroups = findViewById(R.id.buttonMyGroups);
        myProfileBtn = findViewById(R.id.myProfileBtn);
        logOutIv = (ImageView) findViewById(R.id.logOutIv);
        id = getIntent().getStringExtra("id");
        userName = getIntent().getStringExtra("username");
        textView10.setText("hello, "+userName.substring(0,1).toUpperCase(Locale.ROOT) +  userName.substring(1));
        phoneNumber = getIntent().getStringExtra("phone_number");
        email = getIntent().getStringExtra("email");
        address = getIntent().getStringExtra("address");
        password = getIntent().getStringExtra("password");
        numberOfKids = getIntent().getStringExtra("number_of_kids");
        maritalStatus = getIntent().getStringExtra("marital_status");
        gender = getIntent().getStringExtra("gender");
        language = getIntent().getStringExtra("language");
        religion = getIntent().getStringExtra("religion");

        sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE);

        loggedInUser = new User(userName,phoneNumber,email,address,password,Integer.parseInt(numberOfKids),maritalStatus,gender,language,religion);

        buttonSearchGroups.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(home_page_parent_activity.this, SearchGroupsActivity.class);
              Bundle extras = new Bundle();
              extras.putInt("userid", Integer.parseInt(id));
              extras.putString("username", userName);

              intent.putExtras(extras);
              startActivity(intent);
          }
         }
        );

        myChildrenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home_page_parent_activity.this,my_children_activity.class);
                Bundle extras = new Bundle();

                extras.putString("id",id);
                extras.putString("userName",userName);
                extras.putString("phone_number",phoneNumber);
                extras.putString("email",email);
                extras.putString("address",address);
                extras.putString("password",password);
                extras.putString("number_of_kids",numberOfKids);
                extras.putString("marital_status",maritalStatus);
                extras.putString("gender",gender);
                extras.putString("language",language);
                extras.putString("religion",religion);


                intent.putExtras(extras);
                startActivity(intent);
            }
        }
        );
        buttonMyGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  Intent intent = new Intent(home_page_parent_activity.this, MyGroupsActivity.class);
                Bundle extras = new Bundle();
                extras.putInt("userid", Integer.parseInt(id));
                extras.putString("username", userName);

                intent.putExtras(extras);
                startActivity(intent);
                }
            }
        );
        logOutIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home_page_parent_activity.this, log_in_activity.class);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("remember_me", false);
                editor.remove("email");
                editor.remove("password");
                editor.apply();
                startActivity(intent);

                finish();
            }
        });
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

        homePageFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragmentContainerView,HomeFragment.class,null).setReorderingAllowed(true).addToBackStack("name").commit();
                buttonMyGroups.setVisibility(View.VISIBLE);
                myProfileBtn.setVisibility(View.VISIBLE);
                buttonSearchGroups.setVisibility(View.VISIBLE);
                myChildrenButton.setVisibility(View.VISIBLE);
                logOutIv.setVisibility(View.VISIBLE);
                textView10.setVisibility(View.VISIBLE);
                nameTv2.setVisibility(View.VISIBLE);
            }
        });

        infoFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragmentContainerView,InfoFragment.class,null).setReorderingAllowed(true).addToBackStack("name").commit();
                buttonMyGroups.setVisibility(View.INVISIBLE);
                myProfileBtn.setVisibility(View.INVISIBLE);
                buttonSearchGroups.setVisibility(View.INVISIBLE);
                myChildrenButton.setVisibility(View.INVISIBLE);
                logOutIv.setVisibility(View.INVISIBLE);
                textView10.setVisibility(View.INVISIBLE);
                nameTv2.setVisibility(View.INVISIBLE);

            }
        });
    }



}

