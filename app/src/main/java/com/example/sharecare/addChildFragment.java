package com.example.sharecare;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.sharecare.handlers.CreateKidProfileFirebaseHandler;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Map;


public class addChildFragment extends Fragment {

    private EditText nameEt;
    private EditText ageEt1;
    private EditText schoolNameEt;
    private Spinner genderSpinner1;
    private Button addchildBTN;
    CreateKidProfileFirebaseHandler createKidProfileFirebaseHandler;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_child, container, false);

        nameEt = view.findViewById(R.id.nameEt);
        ageEt1 = view.findViewById(R.id.ageEt1);
        schoolNameEt = view.findViewById(R.id.schoolNameEt);
        genderSpinner1 = view.findViewById(R.id.genderSpinner1);
        addchildBTN = view.findViewById(R.id.addchildBTN);

        createKidProfileFirebaseHandler = new CreateKidProfileFirebaseHandler();

        addchildBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> kidDetails = new HashMap<String, Object>();
                kidDetails.put("age", ageEt1.getText().toString());
                kidDetails.put("gender", genderSpinner1.getSelectedItem().toString());
                kidDetails.put("id", "0");
                kidDetails.put("name", nameEt.getText().toString());
                kidDetails.put("parent", log_in_activity.id);
                kidDetails.put("schoolName", schoolNameEt.getText().toString());

                createKidProfileFirebaseHandler.addKidData(kidDetails, new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        int numKids = Integer.parseInt(log_in_activity.numberOfKids);
                        numKids++;
                        log_in_activity.numberOfKids = String.valueOf(numKids);
                        createKidProfileFirebaseHandler.updatingNumberOfKidsFieldInFirebase();
                        createKidProfileFirebaseHandler.updateKidId(documentReference.getId());
                        createKidProfileFirebaseHandler.addKidToParentCollection(log_in_activity.id, documentReference.getId(), kidDetails, new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                createKidProfileFirebaseHandler.addKidToUsersCollection(log_in_activity.id, documentReference.getId(), kidDetails, new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                    }
                                }, new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                            }
                        }, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                    }
                }, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

                Intent intent = new Intent(view.getContext(),my_children_activity.class);
                startActivity(intent);
            }

        });

        return view;
    }



}