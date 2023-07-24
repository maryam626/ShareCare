package com.example.sharecare;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sharecare.Logic.UsersSQLLiteDatabaseHelper;
import com.example.sharecare.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class sign_up_activity extends AppCompatActivity {
    private static final String TAG = "sign up activity";

    private EditText userNameEt;
    private EditText phoneEt;
    private EditText emailEt;
    private EditText addressEt;
    private Spinner kidsSpinner;
    private Spinner maritalSpinner;
    private Spinner genderSpinner;
    private Spinner languagesSpinner;
    private Spinner religionSpinner;
    private Button signUpBtn1;
    private EditText passwordEt;

    private FirebaseAuth mAuth;

    private UsersSQLLiteDatabaseHelper usersDatabaseHelper;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static String id;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        userNameEt = findViewById(R.id.userNameEt);
        phoneEt = findViewById(R.id.phoneEt);
        emailEt = findViewById(R.id.emailEt);
        addressEt = findViewById(R.id.addressEt);
        kidsSpinner = findViewById(R.id.kidsSpinner);
        maritalSpinner = findViewById(R.id.maritalSpinner);
        genderSpinner = findViewById(R.id.genderSpinner);
        signUpBtn1 = findViewById(R.id.signUpBtn);
        languagesSpinner = findViewById(R.id.languageSpinner);
        religionSpinner = findViewById(R.id.religionSpinner);
        passwordEt = findViewById(R.id.passwordEt);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        usersDatabaseHelper = new UsersSQLLiteDatabaseHelper(this);


        // Set up spinner adapters
        ArrayAdapter<CharSequence> numberOfKidsAdapter = ArrayAdapter.createFromResource(
                this, R.array.Number_Of_Kids, android.R.layout.simple_spinner_item);
        kidsSpinner.setAdapter(numberOfKidsAdapter);

        ArrayAdapter<CharSequence> maritalStatusAdapter = ArrayAdapter.createFromResource(
                this, R.array.Marital_Status, android.R.layout.simple_spinner_item);
        maritalSpinner.setAdapter(maritalStatusAdapter);

        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(
                this, R.array.Gender, android.R.layout.simple_spinner_item);
        genderSpinner.setAdapter(genderAdapter);

        ArrayAdapter<CharSequence> languageAdapter = ArrayAdapter.createFromResource(
                this, R.array.Languages, android.R.layout.simple_spinner_item);
        languagesSpinner.setAdapter(languageAdapter);

        ArrayAdapter<CharSequence> religionAdapter = ArrayAdapter.createFromResource(
                this, R.array.Religions, android.R.layout.simple_spinner_item);
        religionSpinner.setAdapter(religionAdapter);


        // Register button click listener
        signUpBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SignUpValidation.isUserNameValid(userNameEt) &&
                        SignUpValidation.isPhoneNumberValid(phoneEt) &&
                        SignUpValidation.isAddressValid(addressEt) &&
                        SignUpValidation.isPasswordValid(passwordEt) &&
                        SignUpValidation.isEmailValid(emailEt) &&
                        SignUpValidation.isSpinnerSelectionValid(kidsSpinner, "Number of Kids") &&
                        SignUpValidation.isSpinnerSelectionValid(maritalSpinner, "Marital Status") &&
                        SignUpValidation.isSpinnerSelectionValid(genderSpinner, "Gender") &&
                        SignUpValidation.isSpinnerSelectionValid(languagesSpinner, "Language") &&
                        SignUpValidation.isSpinnerSelectionValid(religionSpinner, "Religion")) {
                    // All fields are valid, proceed with sign-up process

                    // Retrieve user input
                    String username = userNameEt.getText().toString().trim();
                    String phoneNumber = phoneEt.getText().toString().trim();
                    String email = emailEt.getText().toString().trim();
                    String address = addressEt.getText().toString().trim();
                    String password = passwordEt.getText().toString().trim();
                    int numberOfKids = Integer.parseInt(kidsSpinner.getSelectedItem().toString());
                    String maritalStatus = maritalSpinner.getSelectedItem().toString();
                    String gender = genderSpinner.getSelectedItem().toString();
                    String language = languagesSpinner.getSelectedItem().toString();
                    String religion = religionSpinner.getSelectedItem().toString();


                    // Create a new User object
                    User user = new User(username, phoneNumber, email, address, password, numberOfKids,
                            maritalStatus, gender, language, religion);


                    // Store user data in SQLite database
                    long rowId = usersDatabaseHelper.insertUser(user);

                    id = String.valueOf(rowId);

                    if (rowId != -1) {
                        registerUser(emailEt.getText().toString(), passwordEt.getText().toString());

                        if (userNameEt.getText().toString().equals("")) {
                            userNameEt.setError("Enter your User Name");
                        }
                        if (phoneEt.getText().toString().equals("")) {
                            phoneEt.setError("Enter your phone number");
                        }
                        if (emailEt.getText().toString().equals("")) {
                            emailEt.setError("Enter your Email");
                        }
                        if (addressEt.getText().toString().equals("")) {
                            addressEt.setError("Enter your Address");
                        } else {

                            // Successful message
                            Toast.makeText(sign_up_activity.this, "Registration successful!", Toast.LENGTH_SHORT).show();

                            addingParentDataToFirebase(username, phoneNumber, email, address, password, numberOfKids,
                                    maritalStatus, gender, language, religion);


                            Intent intent = new Intent(sign_up_activity.this, FillKidsInformation.class);
                            //Sending Data To Home Page Using Bundle
                            Bundle extras = new Bundle();
                            extras.putString("username", username);
                            extras.putString("phone_number", phoneNumber);
                            extras.putString("email", email);
                            extras.putString("address", address);
                            extras.putString("password", password);
                            extras.putString("number_of_kids", String.valueOf(numberOfKids));
                            extras.putString("marital_status", maritalStatus);
                            extras.putString("gender", gender);
                            extras.putString("language", language);
                            extras.putString("religion", religion);

                            intent.putExtras(extras);
                            startActivity(intent);
                        }
                    } else {
                        // Error message
                        Toast.makeText(sign_up_activity.this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });


    }

    private void addingParentDataToFirebase(String username, String phoneNumber, String email, String address, String password, int numberOfKids, String maritalStatus, String gender, String language, String religion) {
        Map<String, Object> parent = new HashMap<>();
        parent.put("id","0");
        parent.put("username",username);
        parent.put("phoneNumber",phoneNumber);
        parent.put("email",email);
        parent.put("address",address);
        parent.put("password",password);
        parent.put("numberOfKids",String.valueOf(numberOfKids));
        parent.put("maritalStatus",maritalStatus);
        parent.put("gender",gender);
        parent.put("language",language);
        parent.put("religion",religion);

        Task<Void> referenceTask =  db.collection("Parents").document(id).set(parent).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        updateIdForParent(id);
                        Map<String, Object> data = new HashMap<>();
                        data.put("name", "first kid");
                        db.collection("Parents").document(id).collection("myKids").add(data);


                        addingUserDataToFirebase(username, phoneNumber, email, address, password, numberOfKids,
                                maritalStatus, gender, language, religion);

                        Log.d(TAG, "DocumentSnapshot added with ID: " + id);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);

                    }
                });


        while(!referenceTask.isComplete()){

        }

    }

    private void updateIdForParent(String id) {
        Map<String,Object> parentDetail = new HashMap<String, Object>();
        parentDetail.put("id", id);


        db.collection("Parents")
                .whereEqualTo("email", emailEt.getText().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {

                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                            String documentId = documentSnapshot.getId();
                            db.collection("Parents").document(documentId).update(parentDetail).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                    Toast.makeText(sign_up_activity.this, "id updated Successfully", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(sign_up_activity.this, "Some error happened", Toast.LENGTH_SHORT).show();

                                }
                            });

                        }
                        else {
                            Log.d(TAG, "Error changing data", task.getException());

                        }
                    }
                });
    }

    private void addingUserDataToFirebase(String username, String phoneNumber, String email, String address, String password, int numberOfKids, String maritalStatus, String gender, String language, String religion) {

        Map<String, Object> user = new HashMap<>();
        user.put("id","0");
        user.put("username",username);
        user.put("phoneNumber",phoneNumber);
        user.put("email",email);
        user.put("address",address);
        user.put("password",password);
        user.put("numberOfKids",String.valueOf(numberOfKids));
        user.put("maritalStatus",maritalStatus);
        user.put("gender",gender);
        user.put("language",language);
        user.put("religion",religion);

        Task<Void> referenceTask =  db.collection("Users").document(id)
                .set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        updateId(id);
                        Map<String, Object> data = new HashMap<>();
                        data.put("name", "first kid");
                        db.collection("Users").document(id).collection("myKids").add(data);

                        Log.d(TAG, "DocumentSnapshot added with ID: " + id);
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



    }

    private void updateId(String id) {
        Map<String,Object> userDetail = new HashMap<String, Object>();
        userDetail.put("id", id);


        db.collection("Users")
                .whereEqualTo("email", emailEt.getText().toString())
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

                                    Toast.makeText(sign_up_activity.this, "id updated Successfully", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(sign_up_activity.this, "Some error happened", Toast.LENGTH_SHORT).show();

                                }
                            });

                        }
                        else {
                            Log.d(TAG, "Error changing data", task.getException());

                        }
                    }
                });

    }

    private void registerUser (String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(sign_up_activity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(sign_up_activity.this, "successfull!!!!!", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        throw task.getException();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(sign_up_activity.this, "Faild------", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUser.reload();
        }

    }
}
