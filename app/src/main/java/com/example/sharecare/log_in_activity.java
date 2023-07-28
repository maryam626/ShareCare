package com.example.sharecare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sharecare.Logic.UsersSQLLiteDatabaseHelper;
import com.example.sharecare.handlers.LogInFirebaseHandler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class log_in_activity extends AppCompatActivity {
    private static final String TAG = "log in";


    private TextView messageTextView;
    private TextView forgetTv;
    private TextView SignUpBtn;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText EtEmail;
    private EditText EtPassword;
    private Button logInBtn;
    private CheckBox rememberCheckBox;
    private String id;
    private String username;
    private String phoneNumber;
    private String email;
    private String address;
    private String password;
    private String numberOfKids;
    private String maritalStatus;
    private String gender;
    private String language;
    private String religion;

    private UsersSQLLiteDatabaseHelper databaseHelper;
    private LogInFirebaseHandler logInFirebaseHandler;
    private FirebaseFirestore db;

    ArrayList<QueryDocumentSnapshot> result = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        db = FirebaseFirestore.getInstance();


        databaseHelper = new UsersSQLLiteDatabaseHelper(this);
        forgetTv = (TextView) findViewById(R.id.forgetTv);
        EtPassword = (EditText) findViewById(R.id.EtPassword);
        EtEmail = (EditText) findViewById(R.id.EtEmail);
        messageTextView =  findViewById(R.id.messageTextView);
        logInBtn = (Button) findViewById(R.id.logInBtn);
        rememberCheckBox = (CheckBox) findViewById(R.id.rememberCheckBox);
        SignUpBtn = (TextView) findViewById(R.id.SignUpBtn);

        forgetTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(log_in_activity.this, ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });

        SignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(log_in_activity.this, sign_up_activity.class);
                startActivity(intent);
            }
        });
        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = EtEmail.getText().toString();
                String password = EtPassword.getText().toString();

                if(email.equals("")){
                    EtPassword.setError("Enter Your Email");
                }
                if(password.equals("")){
                    EtEmail.setError("Enter Your Password");
                }

                if (validateCredentials(email, password)) {
                    Intent intent = new Intent(log_in_activity.this, home_page_parent_activity.class);
                    gettingUserData();
                    getParentData(EtEmail.getText().toString(),log_in_activity.this);
                    //puttingDataInVariables(getParentData(EtUserName.getText().toString(),log_in_activity.this));

                    //Sending Data To Home Page Using Bundle
                    Bundle extras = new Bundle();
                    extras.putString("id", id);
                    extras.putString("username", username);
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
                    finish();
                } else {
                    messageTextView.setVisibility(View.VISIBLE);
                    messageTextView.setText("Email or password is incorrect");
                }
            }
        });

    }

    private void puttingDataInVariables(ArrayList<QueryDocumentSnapshot> result) {
        address = result.get(0).get("address").toString();
        email = result.get(0).get("email").toString();
        gender = result.get(0).get("gender").toString();
        id = result.get(0).get("id").toString();
        language = result.get(0).get("language").toString();
        maritalStatus = result.get(0).get("maritalStatus").toString();
        numberOfKids = result.get(0).get("numberOfKids").toString();
        password = result.get(0).get("password").toString();
        phoneNumber = result.get(0).get("phoneNumber").toString();
        religion = result.get(0).get("religion").toString();
        username = result.get(0).get("username").toString();

        System.out.println(address);
        System.out.println(email);
        System.out.println(gender);
        System.out.println(id);
        System.out.println(language);
        System.out.println(maritalStatus);
        System.out.println(numberOfKids);
        System.out.println(password);
        System.out.println(phoneNumber);
        System.out.println(religion);
        System.out.println(username);



    }

    private void gettingUserData() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] columns = {"id","username","phone_number","email","address","password","number_of_kids","marital_status","gender", "language","religion"};
        String selection = "email = ? AND password = ?";
        String[] selectionArgs = {EtEmail.getText().toString(), EtPassword.getText().toString()};


        Cursor cursor = db.query(
                "users",
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex("id");
            if(columnIndex != -1){
                id = cursor.getString(columnIndex);
            } else{
                Log.e("CursorError", "Column not found");
            }

            int columnIndex1 = cursor.getColumnIndex("username");
            if(columnIndex1 != -1){
                username = cursor.getString(columnIndex1);
            }
            else{
                Log.e("CursorError", "Column not found");

            }

            int columnIndex2 = cursor.getColumnIndex("phone_number");
            if(columnIndex2 != -1){
                phoneNumber = cursor.getString(columnIndex2);

            }
            else{
                Log.e("CursorError", "Column not found");

            }

            int columnIndex3 = cursor.getColumnIndex("email");
            if(columnIndex3 != -1){
                email = cursor.getString(columnIndex3);
            }
            else{
                Log.e("CursorError", "Column not found");

            }
            int columnIndex4 = cursor.getColumnIndex("address");
            if(columnIndex4 != -1){
                address = cursor.getString(columnIndex4);
            }
            else{
                Log.e("CursorError", "Column not found");

            }
            int columnIndex5 = cursor.getColumnIndex("password");
            if(columnIndex5 != -1){
                password = cursor.getString(columnIndex5);
            }
            else{
                Log.e("CursorError", "Column not found");

            }
            int columnIndex6 = cursor.getColumnIndex("number_of_kids");
            if(columnIndex6 != -1){
                numberOfKids = cursor.getString(columnIndex6);
            }
            else{
                Log.e("CursorError", "Column not found");

            }
            int columnIndex7 = cursor.getColumnIndex("marital_status");
            if(columnIndex7 != -1){
                maritalStatus = cursor.getString(columnIndex7);
            }
            else{
                Log.e("CursorError", "Column not found");

            }

            int columnIndex8 = cursor.getColumnIndex("gender");
            if(columnIndex8 != -1){
                gender = cursor.getString(columnIndex8);
            }
            else{
                Log.e("CursorError", "Column not found");

            }

            int columnIndex9 = cursor.getColumnIndex("language");
            if(columnIndex9 != -1){
                language = cursor.getString(columnIndex9);
            }
            else{
                Log.e("CursorError", "Column not found");

            }

            int columnIndex10 = cursor.getColumnIndex("religion");
            if(columnIndex10 != -1){
                religion = cursor.getString(columnIndex10);
            }
            else{
                Log.e("CursorError", "Column not found");

            }

        } else {

            // Cursor is empty or null, handle this case accordingly
        }






        cursor.close();
        db.close();
    }

    private boolean validateCredentials(String email, String password) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String[] projection = {
                "email",
                "password"
        };

        String selection = "email = ? AND password = ?";
        String[] selectionArgs = {email, password};

        Cursor cursor = db.query(
                "users",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        boolean isValid = cursor.getCount() > 0;

        cursor.close();
        db.close();

        return isValid;
    }

    public ArrayList<QueryDocumentSnapshot> getParentData(String email, log_in_activity activity) {
        ArrayList<QueryDocumentSnapshot> results = new ArrayList<>();
        Task<QuerySnapshot> task = db.collection("Parents").whereEqualTo("email", email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        results.add(document);
                        System.out.println(results.get(0));
                        System.out.println(results);

                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

        while(!task.isComplete()){

        }
        return results;

    }
}