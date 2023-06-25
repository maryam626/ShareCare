package com.example.sharecare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CreateKidProfile extends AppCompatActivity {

    private EditText nameEt;
    private EditText ageEt1;
    private EditText schoolNameEt;
    private Spinner genderSpinner1;
    private TextView kidNumberTv;
    private Button finishBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_kid_profile);

        nameEt = (EditText) findViewById(R.id.nameEt);
        ageEt1 = (EditText) findViewById(R.id.ageEt1);
        schoolNameEt = (EditText) findViewById(R.id.schoolNameEt);
        genderSpinner1 = (Spinner) findViewById(R.id.genderSpinner1);
        kidNumberTv = (TextView) findViewById(R.id.kidNumberTv);
        finishBtn = (Button) findViewById(R.id.finishBtn);

        kidNumberTv.setText(String.valueOf(getIntent().getIntExtra("kid number",1)));

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int check = 0;
                if(nameEt.getText().toString().equals("")){
                    nameEt.setError("fill the field");
                    check = 1;
                }
                if(ageEt1.getText().toString().equals("")){
                    ageEt1.setError("fill the field");
                    check = 1;
                }
                if(schoolNameEt.getText().toString().equals("")){
                    schoolNameEt.setError("fill the field");
                    check = 1;
                }
                if(check == 1){
                    Toast.makeText(CreateKidProfile.this, "Fill all the details", Toast.LENGTH_SHORT).show();

                }
                else{
                    Intent intent = new Intent(CreateKidProfile.this, FillKidsInformation.class);
                    Bundle extras = new Bundle();
                    extras.putString("number_of_kids", kidNumberTv.getText().toString());
                    intent.putExtras(extras);
                    startActivity(intent);
                }
            }
        });

    }
}