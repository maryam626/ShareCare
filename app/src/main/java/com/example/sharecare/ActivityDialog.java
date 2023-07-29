package com.example.sharecare;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ActivityDialog extends Dialog {

    /* Activity data */
    private String activity_name;
    private String activity_type;
    private String date;
    private String time;
    private String capacity;

    private String duration;
    private String child_age_from;
    private String child_age_to;


    /**
     * Constructor to initialize the ActivityDialog with activity information.
     * @param context The context of the dialog.
     * @param activity_name The name of the activity.
     * @param activity_type The type of the activity.
     * @param date The date of the activity.
     * @param time The time of the activity.
     * @param capacity The capacity of the activity.
     * @param duration The duration of the activity.
     * @param child_age_from The minimum age of children for the activity.
     * @param child_age_to The maximum age of children for the activity.
     */
    public ActivityDialog(Context context, String activity_name, String activity_type , String date,
                          String time, int capacity ,int duration , int child_age_from , int child_age_to) {
        super(context);
        this.activity_name = activity_name;
        this.activity_type = activity_type;
        this.date = date;
        this.time = time;
        this.capacity = String.valueOf(capacity);
        this.duration= String.valueOf(duration);
        this.child_age_from = String.valueOf(child_age_from);
        this.child_age_to = String.valueOf(child_age_to);
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
        TextView capacityLabel = findViewById(R.id.capacity);
        capacityLabel.setText(capacity);

        TextView durationLabel = findViewById(R.id.duration);
        durationLabel.setText(duration);

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
