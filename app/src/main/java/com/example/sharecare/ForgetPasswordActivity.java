package com.example.sharecare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sharecare.handlers.PasswordResetFirebaseHandler;
import com.example.sharecare.valdiators.Validator;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class ForgetPasswordActivity extends AppCompatActivity {
    EditText emailEt2;
    Button resetBtn;
    Button backBtn;
    ProgressBar progressBar;
    String email;
    PasswordResetFirebaseHandler firebaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        firebaseHandler = new PasswordResetFirebaseHandler();

        emailEt2 = findViewById(R.id.emailEt2);
        resetBtn = findViewById(R.id.resetBtn);
        backBtn = findViewById(R.id.backBtn);
        progressBar = findViewById(R.id.progressBar);

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailEt2.getText().toString().trim();
                if (!Validator.isFieldEmpty(email)) {
                    resetPassword();
                } else {
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

    private void resetPassword() {
        progressBar.setVisibility(View.VISIBLE);
        resetBtn.setVisibility(View.INVISIBLE);

        firebaseHandler.sendPasswordResetEmail(email, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(ForgetPasswordActivity.this, "Reset password link has been sent to your email", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ForgetPasswordActivity.this, log_in_activity.class);
                startActivity(intent);
                finish();
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ForgetPasswordActivity.this, "Error :- " + e.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                resetBtn.setVisibility(View.VISIBLE);
            }
        });
    }
}
