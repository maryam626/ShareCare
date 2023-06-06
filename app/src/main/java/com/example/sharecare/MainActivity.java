package com.example.sharecare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.getStartedBtn);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //this code only for debug
                /*Intent intent = new Intent(MainActivity.this, ChildrenActivityCreateActivity.class);
                startActivity(intent);*/

                Intent intent = new Intent(MainActivity.this, log_in_activity.class);
                startActivity(intent);
            }
        });
    }
}