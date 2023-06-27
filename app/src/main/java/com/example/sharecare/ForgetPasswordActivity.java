package com.example.sharecare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {
    EditText emailEt2;
    Button resetBtn;
    Button backBtn;
    ProgressBar progressBar;
    String email;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        emailEt2 = (EditText) findViewById(R.id.emailEt2);
        resetBtn = (Button) findViewById(R.id.resetBtn);
        backBtn = (Button) findViewById(R.id.backBtn);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailEt2.getText().toString().trim();
                if(!TextUtils.isEmpty(email)){
                    ResetPassword();
                }
                else{
                    emailEt2.setError("Email field can't be empty");
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgetPasswordActivity.this, log_in_activity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void ResetPassword() {
        progressBar.setVisibility(View.VISIBLE);
        resetBtn.setVisibility(View.INVISIBLE);

        mAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(ForgetPasswordActivity.this, "Reset password link has been sent to your email", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ForgetPasswordActivity.this, log_in_activity.class);
                startActivity(intent);
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ForgetPasswordActivity.this, "Error :- " + e.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                resetBtn.setVisibility(View.VISIBLE);

            }
        });
    }
}