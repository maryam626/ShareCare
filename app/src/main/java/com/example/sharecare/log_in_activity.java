package com.example.sharecare;

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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sharecare.Logic.UsersSQLLiteDatabaseHelper;

public class log_in_activity extends AppCompatActivity {

    private TextView messageTextView;
    private TextView forgetTv;
    private TextView SignUpBtn;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText EtUserName;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        databaseHelper = new UsersSQLLiteDatabaseHelper(this);
        forgetTv = (TextView) findViewById(R.id.forgetTv);
        EtPassword = (EditText) findViewById(R.id.EtPassword);
        EtUserName = (EditText) findViewById(R.id.EtUserName);
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
                String username = EtUserName.getText().toString();
                String password = EtPassword.getText().toString();

                if(username.equals("")){
                    EtPassword.setError("Enter Your Password");
                }
                if(password.equals("")){
                    EtUserName.setError("Enter Your Email");
                }

                if (validateCredentials(username, password)) {
                    Intent intent = new Intent(log_in_activity.this, home_page_parent_activity.class);
                    gettingUserData();

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
                    messageTextView.setText("Username or password is incorrect");
                }
            }
        });

    }

    private void gettingUserData() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] columns = {"id","username","phone_number","email","address","password","number_of_kids","marital_status","gender", "language","religion"};
        String selection = "username = ? AND password = ?";
        String[] selectionArgs = {EtUserName.getText().toString(), EtPassword.getText().toString()};


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

    private boolean validateCredentials(String username, String password) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String[] projection = {
                "username",
                "password"
        };

        String selection = "username = ? AND password = ?";
        String[] selectionArgs = {username, password};

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
}