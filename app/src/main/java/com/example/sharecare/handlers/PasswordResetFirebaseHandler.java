package com.example.sharecare.handlers;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A handler class to manage Firebase password reset operations.
 */
public class PasswordResetFirebaseHandler {

    /** Firebase authentication instance */
    private FirebaseAuth mAuth;

    /**
     * Constructor to initialize Firebase authentication instance.
     */
    public PasswordResetFirebaseHandler() {
        mAuth = FirebaseAuth.getInstance();
    }


    /**
     * Sends a password reset email to the provided email address.
     *
     * @param email The email address to which the password reset link should be sent.
     * @param successListener Callback that handles success events.
     * @param failureListener Callback that handles failure events.
     */
    public void sendPasswordResetEmail(String email, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {
        mAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }
}
