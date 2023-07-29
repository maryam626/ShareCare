package com.example.sharecare;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.sharecare.handlers.LogInFirebaseHandler;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class log_in_activity extends AppCompatActivity {
    private static final String TAG = "log in";
    private SharedPreferences sharedPreferences;

    private TextView messageTextView;
    private TextView forgetTv;
    private TextView SignUpBtn;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText EtEmail;
    private EditText EtPassword;
    private Button logInBtn;
    private CheckBox rememberCheckBox;
    public static String id;
    public static String username;
    public static String phoneNumber;
    public static String email;
    public static String address;
    public static String password;
    public static String numberOfKids;
    public static String maritalStatus;
    public static String gender;
    public static String language;
    public static String religion;

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

        logInFirebaseHandler = new LogInFirebaseHandler();

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE);

        // Check if rememberCheckBox state is saved and set it accordingly
        boolean rememberMeChecked = sharedPreferences.getBoolean("remember_me", false);
        rememberCheckBox.setChecked(rememberMeChecked);

        // If rememberMeChecked is true and credentials are saved, attempt automatic login
        if (rememberMeChecked) {
            String savedEmail = sharedPreferences.getString("email", "");
            String savedPassword = sharedPreferences.getString("password", "");
            EtEmail.setText(savedEmail);
            EtPassword.setText(savedPassword);

            // Attempt automatic login by calling the login method
            performLogin(savedEmail, savedPassword);
        }

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

                if (email.equals("")) {
                    EtPassword.setError("Enter Your Email");
                }
                if (password.equals("")) {
                    EtEmail.setError("Enter Your Password");
                }

                boolean rememberMe = rememberCheckBox.isChecked();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("remember_me", rememberMe);
                if (rememberMe) {
                    editor.putString("email", email);
                    editor.putString("password", password);
                } else {
                    // Clear saved credentials if rememberMe is false
                    editor.remove("email");
                    editor.remove("password");
                }
                editor.apply();

                // Attempt login when the login button is clicked
                performLogin(email, password);
            }
        });

    }


    private void performLogin(String email, String password) {
        // Your login authentication logic here
        // If login is successful, redirect to the main page
        // If login fails, show an error message or handle accordingly

        if (validateCredentials(email, password)) {
            Intent intent = new Intent(log_in_activity.this, home_page_parent_activity.class);
            gettingUserData();
            logInFirebaseHandler.getParentData(EtEmail.getText().toString(), log_in_activity.this);
            //puttingDataInVariables(logInFirebaseHandler.getParentData(EtEmail.getText().toString(),log_in_activity.this));

            //Sending Data To Home Page Using Bundle
            Bundle extras = new Bundle();
            extras.putString("id", id);
            extras.putString("username", username);
            extras.putString("phone_number", phoneNumber);
            extras.putString("email", email);
            extras.putString("address", address);
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
    public static void puttingDataInVariables(DocumentSnapshot result) {
        address = result.get("address").toString();
        email = result.get("email").toString();
        gender = result.get("gender").toString();
        id = result.get("id").toString();
        language = result.get("language").toString();
        maritalStatus = result.get("maritalStatus").toString();
        numberOfKids = result.get("numberOfKids").toString();
        password = result.get("password").toString();
        phoneNumber = result.get("phoneNumber").toString();
        religion = result.get("religion").toString();
        username = result.get("username").toString();

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


}