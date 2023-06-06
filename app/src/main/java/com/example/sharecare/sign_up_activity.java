package com.example.sharecare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class sign_up_activity extends AppCompatActivity {
    private static final String TAG = "sign up activity";


    private EditText userNameEt;
    private EditText phoneEt;
    private EditText emailEt;
    private EditText addressEt;
    private Spinner kidsSpinner;
    private Spinner maritalSpinner;
    private Spinner genderSpinner;
    private Button signUpBtn1;
    private EditText passwordEt;

    private FirebaseAuth mAuth;


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
        passwordEt = (EditText) findViewById(R.id.passwordEt);

        // ...
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        signUpBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userNameEt.getText().toString().equals("")) {
                    userNameEt.setError("Enter your User Name");
                }
                if (phoneEt.getText().toString().equals("")) {
                    phoneEt.setError("Enter your phone number");
                }
                if (emailEt.getText().toString().equals("")) {
                    emailEt.setError("Enter your Email");
                }
                if (addressEt.getText().toString().equals("")) {
                    addressEt.setError("Enter your Address");
                } else {
                    registerUser(emailEt.getText().toString(),passwordEt.getText().toString());
                  
                    /*Intent intent = new Intent(sign_up_activity.this, create_children_profiles_activity.class);
                    startActivity(intent);*/
                }

            }
        });

    }

    private void registerUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(sign_up_activity.this , new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(sign_up_activity.this,"successfull!!!!!",Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        throw task.getException();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(sign_up_activity.this,"Faild------",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }

    }




}
