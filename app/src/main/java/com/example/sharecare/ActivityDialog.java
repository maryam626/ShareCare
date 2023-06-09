package com.example.sharecare;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ActivityDialog extends Dialog {
    private String activity_name;
    private String activity_type;

    private String date;
    private String time;

    private String capcaity;
    private String child_age_from;

    private String child_age_to;



    public ActivityDialog(Context context, String activity_name, String activity_type , String date,
                          String time, String capcaity ,String child_age_from , String child_age_to) {
        super(context);
        this.activity_name = activity_name;
        this.activity_type = activity_type;
        this.date = date;
        this.time = time;
        this.capcaity = capcaity;
        this.child_age_from = child_age_from;
        this.child_age_to = child_age_to;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_activity);

        TextView nameLabel = findViewById(R.id.activity_name);
        nameLabel.setText(activity_name);

        TextView typeLabel = findViewById(R.id.activity_type);
        typeLabel.setText(activity_type);

        TextView dateLabel = findViewById(R.id.date);
        dateLabel.setText(date);
        TextView timeLabel = findViewById(R.id.time);
        timeLabel.setText(time);
        TextView capcaityLabel = findViewById(R.id.capcaity);
        capcaityLabel.setText(capcaity);

        TextView child_age_fromLabel = findViewById(R.id.child_age_from);
        child_age_fromLabel.setText(child_age_from);

        TextView child_age_toLabel = findViewById(R.id.child_age_to);
        child_age_toLabel.setText(child_age_to);

        Button closeButton = findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
