package com.example.sharecare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddingNewKid extends AppCompatActivity {
    private EditText nameEt;
    private EditText ageEt1;
    private EditText schoolNameEt;
    private Spinner genderSpinner1;
    private Button finishBtn;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_new_kid);

        nameEt = findViewById(R.id.nameEt);
        ageEt1 = findViewById(R.id.ageEt1);
        schoolNameEt = findViewById(R.id.schoolNameEt);
        genderSpinner1 = findViewById(R.id.genderSpinner1);
        finishBtn = findViewById(R.id.finishBtn);


        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> kidDetails = new HashMap<String, Object>();
                kidDetails.put("age", ageEt1.getText().toString());
                kidDetails.put("gender", genderSpinner1.getSelectedItem().toString());
                kidDetails.put("id", "0");
                kidDetails.put("name", nameEt.getText().toString());
                kidDetails.put("parent", log_in_activity.id);
                kidDetails.put("schoolName", schoolNameEt.getText().toString());


                Task<DocumentReference> task = db.collection("Parents").document(log_in_activity.id).collection("myKids").add(kidDetails).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        int numKids = Integer.parseInt(log_in_activity.numberOfKids);
                        numKids++;
                        log_in_activity.numberOfKids = String.valueOf(numKids);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

                while(!task.isComplete()){

                }

                Intent intent = new Intent(AddingNewKid.this,my_children_activity.class);
                startActivity(intent);
            }
        });
    }
}