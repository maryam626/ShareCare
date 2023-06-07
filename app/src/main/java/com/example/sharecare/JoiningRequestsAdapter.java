package com.example.sharecare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharecare.models.Request;

import java.util.List;

public class JoiningRequestsAdapter extends RecyclerView.Adapter<JoiningRequestsHolder> {

    Context context;
    List<Request> requests;

    public JoiningRequestsAdapter(Context context, List<Request> requests) {
        this.context = context;
        this.requests = requests;
    }

    @NonNull
    @Override
    public JoiningRequestsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new JoiningRequestsHolder(LayoutInflater.from(context).inflate(R.layout.joining_request_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull JoiningRequestsHolder holder, int position) {
        holder.group.setText(String.valueOf(requests.get(position).getGroup()));
        holder.approved.setText(requests.get(position).getApproved().toString());
        holder.date.setText(requests.get(position).getRequestDate().toString());

    }

    @Override
    public int getItemCount() {
        return requests.size();
    }
}
