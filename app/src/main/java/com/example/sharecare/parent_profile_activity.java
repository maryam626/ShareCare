package com.example.sharecare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.sharecare.Logic.UsersSQLLiteDatabaseHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

 import com.google.android.material.snackbar.Snackbar;

public class parent_profile_activity extends AppCompatActivity implements addChildFragment.ChildFragmentListener {
    private static final String TAG = "profile activity";


    private TextView userNameTv;
    private EditText phoneEt1;
    private EditText emailEt1;
    private EditText languageEt;
    private EditText religionEt;
    private EditText passwordEt1;
    private EditText addressEt3;
    private EditText numKidsEt;
    private EditText maritalEt;
    private EditText genderEt;
    private Button updateBtn;
    private Button saveBtn;
    private String id;
    private String userName;
    private String phoneNumber;
    private String email;
    private String address;
    private String password;
    private String numberOfKids;
    private String maritalStatus;
    private String gender;
    private String language;
    private String religion;

    private Button addchild;

    private UsersSQLLiteDatabaseHelper usersDatabaseHelper;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_profile);

        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        // Add the ChildFragment to the activity
        /*if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragmentContainerView, new addChildFragment())
                    .commit();
        }*/

        //userNameTv = (TextView) findViewById(R.id.userNameTv);
        phoneEt1 = (EditText) findViewById(R.id.phoneEt1);
        emailEt1 = (EditText) findViewById(R.id.emailEt1);
        languageEt = (EditText) findViewById(R.id.LanguageEt);
        religionEt = (EditText) findViewById(R.id.religionEt);
        passwordEt1 = (EditText) findViewById(R.id.passwordEt1);
        addressEt3 = (EditText) findViewById(R.id.addressEt3);
        numKidsEt = (EditText) findViewById(R.id.numKidsEt);
        maritalEt = (EditText) findViewById(R.id.maritalEt);
        genderEt = (EditText) findViewById(R.id.genderEt);
        updateBtn = (Button) findViewById(R.id.updateBtn);
        saveBtn = (Button) findViewById(R.id.saveBtn);
        id = getIntent().getStringExtra("id");
        userName = getIntent().getStringExtra("username");
        phoneNumber = getIntent().getStringExtra("phone_number");
        email = getIntent().getStringExtra("email");
        address = getIntent().getStringExtra("address");
        password = getIntent().getStringExtra("password");
        numberOfKids = getIntent().getStringExtra("number_of_kids");
        maritalStatus = getIntent().getStringExtra("marital_status");
        gender = getIntent().getStringExtra("gender");
        language = getIntent().getStringExtra("language");
        religion = getIntent().getStringExtra("religion");
        addchild = (Button) findViewById(R.id.addchildBTN);

        //userNameTv.setText(userName);
        phoneEt1.setText(phoneNumber);
        phoneEt1.setEnabled(false);
        emailEt1.setText(email);
        emailEt1.setEnabled(false);
        addressEt3.setText(address);
        addressEt3.setEnabled(false);
        passwordEt1.setText(password);
        passwordEt1.setEnabled(false);
        numKidsEt.setText(numberOfKids);
        numKidsEt.setEnabled(false);
        maritalEt.setText(maritalStatus);
        maritalEt.setEnabled(false);
        genderEt.setText(gender);
        genderEt.setEnabled(false);
        languageEt.setText(language);
        languageEt.setEnabled(false);
        religionEt.setText(religion);
        religionEt.setEnabled(false);


        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneEt1.setEnabled(true);
                addressEt3.setEnabled(true);
                numKidsEt.setEnabled(true);
                maritalEt.setEnabled(true);
                genderEt.setEnabled(true);
                languageEt.setEnabled(true);
                religionEt.setEnabled(true);

                // Find the view to anchor the Snackbar (e.g., the root view of your activity)
                View rootView = findViewById(android.R.id.content);

                // Create and show the Snackbar
                Snackbar snackbar = Snackbar.make(rootView, "You are going to update your info", Snackbar.LENGTH_SHORT);
                snackbar.show();


            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailEt1.setEnabled(false);
                phoneEt1.setEnabled(false);
                passwordEt1.setEnabled(false);
                addressEt3.setEnabled(false);
                numKidsEt.setEnabled(false);
                maritalEt.setEnabled(false);
                genderEt.setEnabled(false);
                languageEt.setEnabled(false);
                religionEt.setEnabled(false);

                usersDatabaseHelper = new UsersSQLLiteDatabaseHelper(parent_profile_activity.this);
                usersDatabaseHelper.updateUser(id, phoneEt1.getText().toString(), emailEt1.getText().toString(), addressEt3.getText().toString(), passwordEt1.getText().toString(), Integer.parseInt(numKidsEt.getText().toString()), maritalEt.getText().toString(), genderEt.getText().toString(), languageEt.getText().toString(), religionEt.getText().toString());
                updateUserInFirebase(phoneEt1.getText().toString(), addressEt3.getText().toString(), Integer.parseInt(numKidsEt.getText().toString()), maritalEt.getText().toString(), genderEt.getText().toString(), languageEt.getText().toString(), religionEt.getText().toString());
                updateParentInFirebase(phoneEt1.getText().toString(), addressEt3.getText().toString(), Integer.parseInt(numKidsEt.getText().toString()), maritalEt.getText().toString(), genderEt.getText().toString(), languageEt.getText().toString(), religionEt.getText().toString());

            }
        });


    }

    private void updateParentInFirebase(String phone,String address, int numOfKids, String maritalStatus, String gender, String language, String religion) {
        Map<String, Object> parentDetail = new HashMap<String, Object>();
        parentDetail.put("phoneNumber", phone);
        parentDetail.put("address", address);
        parentDetail.put("numberOfKids", numOfKids);
        parentDetail.put("maritalStatus", maritalStatus);
        parentDetail.put("gender", gender);
        parentDetail.put("language", language);
        parentDetail.put("religion", religion);


        db.collection("Parents").document(log_in_activity.id).update(parentDetail).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }



    private void updateUserInFirebase(String phone,String address ,int numOfKids, String maritalStatus, String gender, String language, String religion) {
        Map<String,Object> userDetail = new HashMap<String, Object>();
        userDetail.put("phoneNumber", phone);
        userDetail.put("address", address);
        userDetail.put("numberOfKids", numOfKids);
        userDetail.put("maritalStatus", maritalStatus);
        userDetail.put("gender", gender);
        userDetail.put("language", language);
        userDetail.put("religion", religion);




        db.collection("Users")
                .whereEqualTo("email", emailEt1.getText().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {

                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                            String documentId = documentSnapshot.getId();
                            db.collection("Users").document(documentId).update(userDetail).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                    Toast.makeText(parent_profile_activity.this, "details updated Successfully", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(parent_profile_activity.this, "Some error happened", Toast.LENGTH_SHORT).show();

                                }
                            });

                        }
                        else {
                            Log.d(TAG, "Error changing data", task.getException());

                        }
                    }
                });
    }

    @Override
    public void onChildDataAdded(String name, int age, String gender, String schoolName) {
         // Handle the data sent from the child fragment here
        // You can do whatever you want with the data, e.g., save it to variables or display it
        Log.d("MainActivity", "Child's Name: " + name);
        Log.d("MainActivity", "Child's Age: " + age);
        Log.d("MainActivity", "Child's Gender: " + gender);
        Log.d("MainActivity", "Child's School Name: " + schoolName);
    }

    private void setSupportActionBar(Toolbar toolbar) {
    }
}