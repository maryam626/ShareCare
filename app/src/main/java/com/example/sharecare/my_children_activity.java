package com.example.sharecare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.sharecare.models.Kid;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class my_children_activity extends AppCompatActivity {

    private static final String TAG = "my children activity";


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

    private ImageButton addImageButton;

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

    Map<String,Object> map;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    HashMap<String, Map<String,Object>> kidsMap = new HashMap<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_children);

        //here is the add child fragment code//
        Button btnaddchild = findViewById(R.id.addChildbtn);
        btnaddchild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ///here is the transaction from one fragment to other when the button is clicked//
                FragmentManager fragmentManager = getSupportFragmentManager();  //here we created java class for fragment manger and get a supported fragment manger//
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, addChildFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("name") // Name can be null
                        .commit();

            }
        });


        //here is the my children fragment code//
         Button btnmychildren = findViewById(R.id.mychildrenbtn);
        btnmychildren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ///here is the transaction from one fragment to other when the button is clicked//
                FragmentManager fragmentManager = getSupportFragmentManager();  //here we created java class for fragment manger and get a supported fragment manger//
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, myChildrenFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("name") // Name can be null
                        .commit();

            }
        });

        kid1Btn = (Button) findViewById(R.id.kid1Btn);
        kid2Btn = (Button) findViewById(R.id.kid2Btn);
        kid3Btn = (Button) findViewById(R.id.kid3Btn);
        kid4Btn = (Button) findViewById(R.id.kid4Btn);
        kid5Btn = (Button) findViewById(R.id.kid5Btn);
        kid6Btn = (Button) findViewById(R.id.kid6Btn);
        kid7Btn = (Button) findViewById(R.id.kid7Btn);
        kid8Btn = (Button) findViewById(R.id.kid8Btn);
        kid9Btn = (Button) findViewById(R.id.kid9Btn);
        kid10Btn = (Button) findViewById(R.id.kid10Btn);

        addImageButton = (ImageButton) findViewById(R.id.addImageButton);

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

        for(int i = 0; i<Integer.parseInt(numberOfKids); i++){
            kidsButtons.get(i).setClickable(true);
            kidsButtons.get(i).setVisibility(View.VISIBLE);
        }

        loadKidsData();

        kid1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(my_children_activity.this,KidsInfo.class);
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
                Intent intent = new Intent(my_children_activity.this,KidsInfo.class);
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
                Intent intent = new Intent(my_children_activity.this,KidsInfo.class);
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
                Intent intent = new Intent(my_children_activity.this,KidsInfo.class);
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
                Intent intent = new Intent(my_children_activity.this,KidsInfo.class);
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
                Intent intent = new Intent(my_children_activity.this,KidsInfo.class);
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
                Intent intent = new Intent(my_children_activity.this,KidsInfo.class);
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
                Intent intent = new Intent(my_children_activity.this,KidsInfo.class);
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
                Intent intent = new Intent(my_children_activity.this,KidsInfo.class);
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
                Intent intent = new Intent(my_children_activity.this,KidsInfo.class);
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

        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(my_children_activity.this,CreateKidProfile.class);
                startActivity(intent);*/

            }
        });

    }

    private void loadKidsData() {
            Task<QuerySnapshot> task = db.collection("Parents").document(id).collection("myKids").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(int i = 0;i<queryDocumentSnapshots.getDocuments().size();i++) {
                    kidsMap.put(queryDocumentSnapshots.getDocuments().get(i).getData().get("name").toString(),queryDocumentSnapshots.getDocuments().get(i).getData());
                    kidsButtons.get(i).setText(queryDocumentSnapshots.getDocuments().get(i).getData().get("name").toString());

                        kidsMap.remove("first kid");

                }
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