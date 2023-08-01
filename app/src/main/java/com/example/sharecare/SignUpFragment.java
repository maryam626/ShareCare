package com.example.sharecare;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sharecare.handlers.SignUpFirebaseHandler;
import com.example.sharecare.handlers.UserHandler;
import com.example.sharecare.models.User;
import com.example.sharecare.valdiators.SignUpValidator;

public class SignUpFragment extends Fragment {
    private static final String TAG = "sign_up_fragment";

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

    View view1;

    private UserHandler userHandler;
    private SignUpFirebaseHandler firebaseHandler;
    public static String id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view1 = inflater.inflate(R.layout.fragment_sign_up, container, false);

        userNameEt = view1.findViewById(R.id.userNameEt);
        phoneEt = view1.findViewById(R.id.phoneEt);
        emailEt = view1.findViewById(R.id.emailEt);
        addressEt = view1.findViewById(R.id.addressEt);
        kidsSpinner = view1.findViewById(R.id.kidsSpinner);
        maritalSpinner = view1.findViewById(R.id.maritalSpinner);
        genderSpinner = view1.findViewById(R.id.genderSpinner);
        signUpBtn1 = view1.findViewById(R.id.signUpBtn);
        languagesSpinner = view1.findViewById(R.id.languageSpinner);
        religionSpinner = view1.findViewById(R.id.religionSpinner);
        passwordEt = view1.findViewById(R.id.passwordEt);

        userHandler = new UserHandler(view1.getContext());
        firebaseHandler = new SignUpFirebaseHandler();

        // Set up spinner adapters
        ArrayAdapter<CharSequence> numberOfKidsAdapter = ArrayAdapter.createFromResource(
                view1.getContext(), R.array.Number_Of_Kids, android.R.layout.simple_spinner_item);
        kidsSpinner.setAdapter(numberOfKidsAdapter);

        ArrayAdapter<CharSequence> maritalStatusAdapter = ArrayAdapter.createFromResource(
                view1.getContext(), R.array.Marital_Status, android.R.layout.simple_spinner_item);
        maritalSpinner.setAdapter(maritalStatusAdapter);

        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(
                view1.getContext(), R.array.Gender, android.R.layout.simple_spinner_item);
        genderSpinner.setAdapter(genderAdapter);

        ArrayAdapter<CharSequence> languageAdapter = ArrayAdapter.createFromResource(
                view1.getContext(), R.array.Languages, android.R.layout.simple_spinner_item);
        languagesSpinner.setAdapter(languageAdapter);

        ArrayAdapter<CharSequence> religionAdapter = ArrayAdapter.createFromResource(
                view1.getContext(), R.array.Religions, android.R.layout.simple_spinner_item);
        religionSpinner.setAdapter(religionAdapter);

        // Register button click listener
        signUpBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SignUpValidator.isUserNameValid(userNameEt) &&
                        SignUpValidator.isPhoneNumberValid(phoneEt) &&
                        SignUpValidator.isAddressValid(addressEt) &&
                        SignUpValidator.isPasswordValid(passwordEt) &&
                        SignUpValidator.isEmailValid(emailEt) &&
                        SignUpValidator.isSpinnerSelectionValid(kidsSpinner, "Number of Kids") &&
                        SignUpValidator.isSpinnerSelectionValid(maritalSpinner, "Marital Status") &&
                        SignUpValidator.isSpinnerSelectionValid(genderSpinner, "Gender") &&
                        SignUpValidator.isSpinnerSelectionValid(languagesSpinner, "Language") &&
                        SignUpValidator.isSpinnerSelectionValid(religionSpinner, "Religion")) {
                    // All fields are valid, proceed with sign-up process

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
                    long rowId = userHandler.insertUser(user);

                    if (rowId != -1) {
                        // Successful message
                        Toast.makeText(view1.getContext(), "Registration successful!", Toast.LENGTH_SHORT).show();

                        // Register the user in Firebase
                        firebaseHandler.registerUserForFragment(email, password, view1.getContext());

                        // Add user data to Firebase
                        firebaseHandler.addParentDataToFirebaseForFragment(String.valueOf(rowId), username, phoneNumber, email, address,
                                password, numberOfKids, maritalStatus, gender, language, religion, view1.getContext());

                        // Start the next activity
                        Intent intent = new Intent(view1.getContext(), FillKidsInformation.class);
                        // Sending Data To Home Page Using Bundle
                        Bundle extras = new Bundle();
                        extras.putString("username", username);
                        extras.putString("phone_number", phoneNumber);
                        extras.putString("email", email);
                        extras.putString("address", address);
                        extras.putString("password", password);
                        extras.putString("number_of_kids", String.valueOf(numberOfKids));
                        extras.putString("marital_status", maritalStatus);
                        extras.putString("gender", gender);
                        extras.putString("language", language);
                        extras.putString("religion", religion);

                        intent.putExtras(extras);
                        startActivity(intent);
                    } else {
                        // Error message
                        Toast.makeText(view1.getContext(), "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


         return view1;
    }
}