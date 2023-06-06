package com.example.sharecare;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sharecare.Logic.UsersDatabaseHelper;
import com.example.sharecare.models.User;

public class sign_up_activity extends AppCompatActivity {
    private static final String TAG = "sign up activity";


    private EditText userNameEt;
    private EditText phoneEt;
    private EditText emailEt;
    private EditText addressEt;
    private Spinner kidsSpinner;
    private Spinner maritalSpinner;
    private Spinner genderSpinner;

    private Spinner languagesSpinner;

    private Spinner religionSpinner;
    private Button signUpBtn1;
    private EditText passwordEt;

    private UsersDatabaseHelper usersDatabaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        userNameEt = findViewById(R.id.userNameEt);
        phoneEt = findViewById(R.id.phoneEt);
        emailEt = findViewById(R.id.emailEt);
        addressEt = findViewById(R.id.addressEt);
        kidsSpinner = findViewById(R.id.kidsSpinner);
        maritalSpinner = findViewById(R.id.maritalSpinner);
        genderSpinner = findViewById(R.id.GenderSpinner);
        languagesSpinner = findViewById(R.id.LanguagesSpinner);
        religionSpinner = findViewById(R.id.ReligionsSpinner);
        signUpBtn1 = findViewById(R.id.signUpBtn1);
        passwordEt = findViewById(R.id.passwordEt);
        usersDatabaseHelper = new UsersDatabaseHelper(this);
        // Set up spinner adapters
        ArrayAdapter<CharSequence> numberOfKidsAdapter = ArrayAdapter.createFromResource(
                this, R.array.Number_Of_Kids, android.R.layout.simple_spinner_item);
        kidsSpinner.setAdapter(numberOfKidsAdapter);

        ArrayAdapter<CharSequence> maritalStatusAdapter = ArrayAdapter.createFromResource(
                this, R.array.Marital_Status, android.R.layout.simple_spinner_item);
        maritalSpinner.setAdapter(maritalStatusAdapter);

        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(
                this, R.array.Gender, android.R.layout.simple_spinner_item);
        genderSpinner.setAdapter(genderAdapter);

        ArrayAdapter<CharSequence> languageAdapter = ArrayAdapter.createFromResource(
                this, R.array.Languages, android.R.layout.simple_spinner_item);
        languagesSpinner.setAdapter(languageAdapter);

        ArrayAdapter<CharSequence> religionAdapter = ArrayAdapter.createFromResource(
                this, R.array.Religions, android.R.layout.simple_spinner_item);
        religionSpinner.setAdapter(religionAdapter);


        // Register button click listener
        signUpBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve user input
                String username = userNameEt.getText().toString().trim();
                String phoneNumber = phoneEt.getText().toString().trim();
                String email = emailEt.getText().toString().trim();
                String address = addressEt.getText().toString().trim();
                String password = passwordEt.getText().toString().trim();
                int numberOfKids = Integer.parseInt(kidsSpinner.getSelectedItem().toString());
                String maritalStatus = maritalSpinner.getSelectedItem().toString();
                String gender = genderSpinner.getSelectedItem().toString();
                String language = languagesSpinner.getSelectedItem().toString();
                String religion = religionSpinner.getSelectedItem().toString();


                // Create a new User object
                User user = new User(username, phoneNumber, email, address, password, numberOfKids,
                        maritalStatus, gender, language, religion);

                // Store user data in SQLite database
                long rowId = usersDatabaseHelper.insertUser(user);

                if (rowId != -1) {
                    // Successful message
                    Toast.makeText(sign_up_activity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                } else {
                    // Error message
                    Toast.makeText(sign_up_activity.this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }



}
