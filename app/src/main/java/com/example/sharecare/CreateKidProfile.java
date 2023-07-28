package com.example.sharecare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sharecare.handlers.CreateKidProfileFirebaseHandler;
import com.example.sharecare.valdiators.Validator;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Map;

public class CreateKidProfile extends AppCompatActivity {
    private EditText nameEt;
    private EditText ageEt1;
    private EditText schoolNameEt;
    private Spinner genderSpinner1;
    private TextView kidNumberTv;
    private Button finishBtn;
    private String numberOfKids;
    private String kidId;
    private CreateKidProfileFirebaseHandler firebaseHandler;
    private String parentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_kid_profile);

        firebaseHandler = new CreateKidProfileFirebaseHandler();
        parentId = sign_up_activity.id;

        nameEt = findViewById(R.id.nameEt);
        ageEt1 = findViewById(R.id.ageEt1);
        schoolNameEt = findViewById(R.id.schoolNameEt);
        genderSpinner1 = findViewById(R.id.genderSpinner1);
        kidNumberTv = findViewById(R.id.kidNameTv);
        finishBtn = findViewById(R.id.finishBtn);

        kidNumberTv.setText(String.valueOf(getIntent().getIntExtra("kid number", 1)));
        numberOfKids = getIntent().getStringExtra("number_of_kids");

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForm()) {
                    Map<String, Object> kid = new HashMap<>();
                    kid.put("id", "0");
                    kid.put("age", ageEt1.getText().toString());
                    kid.put("name", nameEt.getText().toString());
                    kid.put("gender", genderSpinner1.getSelectedItem().toString());
                    kid.put("parent", parentId);
                    kid.put("schoolName", schoolNameEt.getText().toString());

                    firebaseHandler.addKidData(kid, new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            kidId = documentReference.getId();
                            firebaseHandler.addKidToParentCollection(parentId, kidId, kid, new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    firebaseHandler.addKidToUsersCollection(parentId, kidId, kid, new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            firebaseHandler.updateIdInCollection("Kids", parentId, kidId, new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    changingDoneVariable();
                                                    Intent intent = new Intent(CreateKidProfile.this, FillKidsInformation.class);
                                                    Bundle extras = new Bundle();
                                                    extras.putString("number_of_kids", numberOfKids);
                                                    intent.putExtras(extras);
                                                    startActivity(intent);
                                                }
                                            }, new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    showToast("Failed to update kid ID in Kids collection.");
                                                }
                                            });
                                        }
                                    }, new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            showToast("Failed to add kid to Users collection.");
                                        }
                                    });
                                }
                            }, new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    showToast("Failed to add kid to Parent collection.");
                                }
                            });
                        }
                    }, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showToast("Failed to add kid data to Firebase.");
                        }
                    });
                }
            }
        });
    }

    /**
     * Validates the form data and returns true if all fields are filled; otherwise, false.
     */
    private boolean validateForm() {
        boolean isValid = true;

        if (Validator.isFieldEmpty(nameEt.getText().toString())) {
            nameEt.setError("Fill the field");
            isValid = false;
        }
        if (Validator.isFieldEmpty(ageEt1.getText().toString())) {
            ageEt1.setError("Fill the field");
            isValid = false;
        }
        if (Validator.isFieldEmpty(schoolNameEt.getText().toString())) {
            schoolNameEt.setError("Fill the field");
            isValid = false;
        }

        if (!isValid) {
            showToast("Fill all the details");
        }

        return isValid;
    }

    /**
     *   Displays a short toast message with the provided text.
     */
    private void showToast(String message) {
        Toast.makeText(CreateKidProfile.this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     *  Updates the static boolean variable "doneX" in the FillKidsInformation class,
     *  where X is the number of kids created, to track the completion status of each kid's information.
     */
    private void changingDoneVariable() {
        int kidNumber = Integer.parseInt(kidNumberTv.getText().toString());
        if (kidNumber >= 1 && kidNumber <= 10) {
            String doneVariable = "done" + kidNumber;
            try {
                FillKidsInformation.class.getDeclaredField(doneVariable).setBoolean(null, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}