package com.example.sharecare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class sign_up_activity extends AppCompatActivity {

    private EditText userNameEt;
    private EditText phoneEt;
    private EditText emailEt;
    private EditText addressEt;
    private Spinner kidsSpinner;
    private Spinner maritalSpinner;
    private Spinner genderSpinner;
    private Button signUpBtn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        userNameEt = (EditText) findViewById(R.id.userNameEt);
        phoneEt = (EditText) findViewById(R.id.phoneEt);
        emailEt = (EditText) findViewById(R.id.emailEt);
        addressEt = (EditText) findViewById(R.id.addressEt);
        kidsSpinner = (Spinner) findViewById(R.id.kidsSpinner);
        maritalSpinner = (Spinner) findViewById(R.id.maritalSpinner);
        genderSpinner = (Spinner) findViewById(R.id.genderSpinner);
        signUpBtn1 = (Button) findViewById(R.id.signUpBtn1);


        signUpBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(sign_up_activity.this, create_children_profiles_activity.class);
                startActivity(intent);
            }
        });

    }
}