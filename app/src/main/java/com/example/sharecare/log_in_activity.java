package com.example.sharecare;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class log_in_activity extends AppCompatActivity {

    private TextView SignUpBtn;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText EtUserName;
    private EditText EtPassword;
    private Button logInBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        SignUpBtn = (TextView) findViewById(R.id.SignUpBtn);
        EtPassword = (EditText) findViewById(R.id.EtPassword);
        EtUserName = (EditText) findViewById(R.id.EtUserName);
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
                if(EtPassword.getText().toString().equals("")){
                    EtPassword.setError("Enter Your Password");
                }
                if(EtUserName.getText().toString().equals("")){
                    EtUserName.setError("Enter Your Email");
                }
                else{
                    Intent intent = new Intent(log_in_activity.this, home_page_parent_activity.class);
                    startActivity(intent);
                }
            }
        });
    }
}