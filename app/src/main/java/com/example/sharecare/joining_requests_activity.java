package com.example.sharecare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.sharecare.Logic.Request;
import java.util.ArrayList;
import java.util.List;

public class joining_requests_activity extends AppCompatActivity {
    private RecyclerView requestsRecyclerView;
    private Button addRequestBtn;

    List<Request> Requests = new ArrayList<Request>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joining_requests);

        requestsRecyclerView = (RecyclerView) findViewById(R.id.requestsRecyclerView);
        addRequestBtn = (Button) findViewById(R.id.addRequestBtn);

        requestsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        requestsRecyclerView.setAdapter(new JoiningRequestsAdapter(getApplicationContext(),Requests));


        addRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}