package com.example.sharecare.handlers;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordResetFirebaseHandler {

    private FirebaseAuth mAuth;

    public PasswordResetFirebaseHandler() {
        mAuth = FirebaseAuth.getInstance();
    }

    public void sendPasswordResetEmail(String email, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {
        mAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }
}
