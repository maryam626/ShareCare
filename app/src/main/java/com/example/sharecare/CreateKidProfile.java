package com.example.sharecare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class CreateKidProfile extends AppCompatActivity {
    private static final String TAG = "create kid profile";


    private EditText nameEt;
    private EditText ageEt1;
    private EditText schoolNameEt;
    private Spinner genderSpinner1;
    private TextView kidNumberTv;
    private Button finishBtn;
    private String numberOfKids;
    private String kidId;
    FirebaseFirestore db = FirebaseFirestore.getInstance();



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

        numberOfKids = getIntent().getStringExtra("number_of_kids");

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

                    addingKidsDataToFirebase();

                    Intent intent = new Intent(CreateKidProfile.this, FillKidsInformation.class);
                    Bundle extras = new Bundle();
                    extras.putString("number_of_kids", numberOfKids);
                    intent.putExtras(extras);
                    startActivity(intent);
                }
            }
        });

    }

    private void addingKidsDataToFirebase() {

        Map<String, Object> kid = new HashMap<>();
        kid.put("id","0");
        kid.put("age",ageEt1.getText().toString());
        kid.put("name",nameEt.getText().toString());
        kid.put("gender",genderSpinner1.getSelectedItem().toString());
        kid.put("parent",sign_up_activity.id);
        kid.put("schoolName",schoolNameEt.getText().toString());


        Task<DocumentReference> referenceTask =  db.collection("Kids")
                .add(kid)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        updateId(documentReference.getId());
                        kidId = documentReference.getId();

                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

        while(!referenceTask.isComplete()){


        }

        Task<DocumentReference> referenceTask1 =  db.collection("Parents").document(sign_up_activity.id).collection("myKids").add(kid).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                updateIdInParentCollection(documentReference.getId());
                kidId = documentReference.getId();
                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding document", e);

            }
        });

        while(!referenceTask1.isComplete()){

        }

        Task<DocumentReference> referenceTask2 =  db.collection("Users").document(sign_up_activity.id).collection("myKids").add(kid).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                updateIdInUsersCollection(documentReference.getId());
                kidId = documentReference.getId();
                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding document", e);

            }
        });

        while(!referenceTask2.isComplete()){

        }


    }

    private void updateIdInUsersCollection(String id) {
        Task<Void> task = db.collection("Users").document(sign_up_activity.id).collection("myKids").document(id).update("id",id).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "document updated successfully ");


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding document", e);

            }
        });

        while(!task.isComplete()){

        }
    }

    private void updateIdInParentCollection(String id) {
        Task<Void> task = db.collection("Parents").document(sign_up_activity.id).collection("myKids").document(id).update("id",id).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "document updated successfully ");


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding document", e);

            }
        });

        while(!task.isComplete()){

        }
    }

    private void updateId(String id) {


        Task<Void> task = db.collection("Kids").document(id).update("id",id).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "document updated successfully ");


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding document", e);

            }
        });

        while(!task.isComplete()){

        }

    }
}