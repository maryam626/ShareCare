package com.example.sharecare;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class JoiningRequestsHolder extends RecyclerView.ViewHolder {
    TextView group, date, approved;

    public JoiningRequestsHolder(@NonNull View itemView) {
        super(itemView);

        group = itemView.findViewById(R.id.group);
        date = itemView.findViewById(R.id.date);
        approved = itemView.findViewById(R.id.approved);


    }
}
