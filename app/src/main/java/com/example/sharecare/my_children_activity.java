package com.example.sharecare;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


//TODO : this development related to support children and parent editing
// it is in backlog for now
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

    private Button myKidsBtn;
    private Button addChildBtn;

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

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    HashMap<String, Map<String,Object>> kidsMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_children);

        myKidsBtn = findViewById(R.id.myKidsBtn);
        addChildBtn = findViewById(R.id.addChildBtn);

        myKidsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragmentContainerView2,myChildrenFragment.class,null).setReorderingAllowed(true).addToBackStack("name").commit();
            }
        });

        addChildBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragmentContainerView2,addChildFragment.class,null).setReorderingAllowed(true).addToBackStack("name").commit();
            }
        });


    }
}