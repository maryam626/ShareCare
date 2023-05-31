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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        SignUpBtn = (TextView) findViewById(R.id.SignUpBtn);



        SignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildDialog();
            }
        });
    }

    private void buildDialog() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View chooseDialog = getLayoutInflater().inflate(R.layout.choose_type_pop_up, null);

         Button parent;
         Button hostParent;

         parent = (Button) chooseDialog.findViewById(R.id.parentt);
         hostParent = (Button) chooseDialog.findViewById(R.id.hostParent);




        dialogBuilder.setView(chooseDialog);
        dialog = dialogBuilder.create();

        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(log_in_activity.this, sign_up_activity.class);
                startActivity(intent);

            }
        });
        hostParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(log_in_activity.this, sign_up_activity.class);
                startActivity(intent);

            }
        });


        dialog.show();

    }
}