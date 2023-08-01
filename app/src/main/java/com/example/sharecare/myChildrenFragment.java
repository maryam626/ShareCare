package com.example.sharecare;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class myChildrenFragment extends Fragment {
    private static final String TAG = "my children fragment";


    private Button kid1Btn;
    private Button kid2Btn;
    private Button kid3Btn;
    private Button kid4Btn;
    private Button kid5Btn;
    private Button kid6Btn;
    private Button kid7Btn;
    private Button kid8Btn;
    private Button kid9Btn;
    private Button kid10Btn;

    private ArrayList<Button> kidsButtons;

    public static String id;
    public static String userName;
    public static String phoneNumber;
    public static String email;
    public static String address;
    public static String password;
    public static String numberOfKids;
    public static String maritalStatus;
    public static String gender;
    public static String language;
    public static String religion;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    HashMap<String, Map<String,Object>> kidsMap = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_my_children, container, false);

        kid1Btn = (Button) view.findViewById(R.id.kid1Btn);
        kid2Btn = (Button) view.findViewById(R.id.kid2Btn);
        kid3Btn = (Button) view.findViewById(R.id.kid3Btn);
        kid4Btn = (Button) view.findViewById(R.id.kid4Btn);
        kid5Btn = (Button) view.findViewById(R.id.kid5Btn);
        kid6Btn = (Button) view.findViewById(R.id.kid6Btn);
        kid7Btn = (Button) view.findViewById(R.id.kid7Btn);
        kid8Btn = (Button) view.findViewById(R.id.kid8Btn);
        kid9Btn = (Button) view.findViewById(R.id.kid9Btn);
        kid10Btn = (Button) view.findViewById(R.id.kid10Btn);

        id = log_in_activity.id;
        userName = log_in_activity.username;
        phoneNumber = log_in_activity.phoneNumber;
        email = log_in_activity.email;
        address = log_in_activity.address;
        password = log_in_activity.password;
        numberOfKids = log_in_activity.numberOfKids;
        maritalStatus = log_in_activity.maritalStatus;
        gender = log_in_activity.gender;
        language = log_in_activity.language;
        religion = log_in_activity.religion;

        kidsButtons = new ArrayList<>();
        kidsButtons.add(kid1Btn);
        kidsButtons.add(kid2Btn);
        kidsButtons.add(kid3Btn);
        kidsButtons.add(kid4Btn);
        kidsButtons.add(kid5Btn);
        kidsButtons.add(kid6Btn);
        kidsButtons.add(kid7Btn);
        kidsButtons.add(kid8Btn);
        kidsButtons.add(kid9Btn);
        kidsButtons.add(kid10Btn);

        System.out.println(numberOfKids);
        for(int i = 0; i<Integer.parseInt(log_in_activity.numberOfKids); i++){
            kidsButtons.get(i).setClickable(true);
            kidsButtons.get(i).setVisibility(View.VISIBLE);
        }

        loadKidsData();

        kid1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(),KidsInfo.class);
                Bundle extras = new Bundle();
                Map<String,Object> kidData = kidsMap.get(kid1Btn.getText().toString());
                extras.putString("id",kidData.get("id").toString());
                extras.putString("age",kidData.get("age").toString());
                extras.putString("gender",kidData.get("gender").toString());
                extras.putString("name",kidData.get("name").toString());
                extras.putString("parent",kidData.get("parent").toString());
                extras.putString("schoolName",kidData.get("schoolName").toString());
                intent.putExtras(extras);
                startActivity(intent);

            }
        });

        kid2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(),KidsInfo.class);
                Bundle extras = new Bundle();
                Map<String,Object> kidData = kidsMap.get(kid2Btn.getText().toString());
                extras.putString("id",kidData.get("id").toString());
                extras.putString("age",kidData.get("age").toString());
                extras.putString("gender",kidData.get("gender").toString());
                extras.putString("name",kidData.get("name").toString());
                extras.putString("parent",kidData.get("parent").toString());
                extras.putString("schoolName",kidData.get("schoolName").toString());
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        kid3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(),KidsInfo.class);
                Bundle extras = new Bundle();
                Map<String,Object> kidData = kidsMap.get(kid3Btn.getText().toString());
                extras.putString("id",kidData.get("id").toString());
                extras.putString("age",kidData.get("age").toString());
                extras.putString("gender",kidData.get("gender").toString());
                extras.putString("name",kidData.get("name").toString());
                extras.putString("parent",kidData.get("parent").toString());
                extras.putString("schoolName",kidData.get("schoolName").toString());
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        kid4Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(),KidsInfo.class);
                Bundle extras = new Bundle();
                Map<String,Object> kidData = kidsMap.get(kid4Btn.getText().toString());
                extras.putString("id",kidData.get("id").toString());
                extras.putString("age",kidData.get("age").toString());
                extras.putString("gender",kidData.get("gender").toString());
                extras.putString("name",kidData.get("name").toString());
                extras.putString("parent",kidData.get("parent").toString());
                extras.putString("schoolName",kidData.get("schoolName").toString());
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        kid5Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(),KidsInfo.class);
                Bundle extras = new Bundle();
                Map<String,Object> kidData = kidsMap.get(kid5Btn.getText().toString());
                extras.putString("id",kidData.get("id").toString());
                extras.putString("age",kidData.get("age").toString());
                extras.putString("gender",kidData.get("gender").toString());
                extras.putString("name",kidData.get("name").toString());
                extras.putString("parent",kidData.get("parent").toString());
                extras.putString("schoolName",kidData.get("schoolName").toString());
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        kid6Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(),KidsInfo.class);
                Bundle extras = new Bundle();
                Map<String,Object> kidData = kidsMap.get(kid6Btn.getText().toString());
                extras.putString("id",kidData.get("id").toString());
                extras.putString("age",kidData.get("age").toString());
                extras.putString("gender",kidData.get("gender").toString());
                extras.putString("name",kidData.get("name").toString());
                extras.putString("parent",kidData.get("parent").toString());
                extras.putString("schoolName",kidData.get("schoolName").toString());
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        kid7Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(),KidsInfo.class);
                Bundle extras = new Bundle();
                Map<String,Object> kidData = kidsMap.get(kid7Btn.getText().toString());
                extras.putString("id",kidData.get("id").toString());
                extras.putString("age",kidData.get("age").toString());
                extras.putString("gender",kidData.get("gender").toString());
                extras.putString("name",kidData.get("name").toString());
                extras.putString("parent",kidData.get("parent").toString());
                extras.putString("schoolName",kidData.get("schoolName").toString());
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        kid8Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(),KidsInfo.class);
                Bundle extras = new Bundle();
                Map<String,Object> kidData = kidsMap.get(kid8Btn.getText().toString());
                extras.putString("id",kidData.get("id").toString());
                extras.putString("age",kidData.get("age").toString());
                extras.putString("gender",kidData.get("gender").toString());
                extras.putString("name",kidData.get("name").toString());
                extras.putString("parent",kidData.get("parent").toString());
                extras.putString("schoolName",kidData.get("schoolName").toString());
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        kid9Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(),KidsInfo.class);
                Bundle extras = new Bundle();
                Map<String,Object> kidData = kidsMap.get(kid9Btn.getText().toString());
                extras.putString("id",kidData.get("id").toString());
                extras.putString("age",kidData.get("age").toString());
                extras.putString("gender",kidData.get("gender").toString());
                extras.putString("name",kidData.get("name").toString());
                extras.putString("parent",kidData.get("parent").toString());
                extras.putString("schoolName",kidData.get("schoolName").toString());
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        kid10Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(),KidsInfo.class);
                Bundle extras = new Bundle();
                Map<String,Object> kidData = kidsMap.get(kid10Btn.getText().toString());
                extras.putString("id",kidData.get("id").toString());
                extras.putString("age",kidData.get("age").toString());
                extras.putString("gender",kidData.get("gender").toString());
                extras.putString("name",kidData.get("name").toString());
                extras.putString("parent",kidData.get("parent").toString());
                extras.putString("schoolName",kidData.get("schoolName").toString());
                intent.putExtras(extras);
                startActivity(intent);
            }
        });



        return view;
    }
    private void loadKidsData(){
        Task<QuerySnapshot> task = db.collection("Parents").document(log_in_activity.id).collection("myKids").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(int i = 0;i<queryDocumentSnapshots.getDocuments().size();i++) {
                    if(!queryDocumentSnapshots.getDocuments().get(i).get("name").equals("first kid")){
                        kidsMap.put(queryDocumentSnapshots.getDocuments().get(i).getData().get("name").toString(),queryDocumentSnapshots.getDocuments().get(i).getData());
                        kidsButtons.get(i).setText(queryDocumentSnapshots.getDocuments().get(i).getData().get("name").toString());
                    }}
                System.out.println(kidsMap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "get failed with ", e);

            }
        });
        while(!task.isComplete()){

        }
    }
}