package com.example.sharecare;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sharecare.Logic.UsersDatabaseHelper;

public class log_in_activity extends AppCompatActivity {

    private TextView messageTextView;
    private TextView SignUpBtn;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText EtUserName;
    private EditText EtPassword;
    private Button logInBtn;

    private UsersDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        databaseHelper = new UsersDatabaseHelper(this);
        SignUpBtn = (TextView) findViewById(R.id.SignUpBtn);
        EtPassword = (EditText) findViewById(R.id.EtPassword);
        EtUserName = (EditText) findViewById(R.id.EtUserName);
        messageTextView =  findViewById(R.id.messageTextView);
        logInBtn = (Button) findViewById(R.id.logInBtn);

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
                    Intent intent = new Intent(log_in_activity.this, Search_activity.class);
                    startActivity(intent);
                    finish();
                } else {
                    messageTextView.setVisibility(View.VISIBLE);
                    messageTextView.setText("Username or password is incorrect");
                }
            }
        });

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