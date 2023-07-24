package com.example.sharecare;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.sharecare.Logic.ActivitySQLLiteDatabaseHelper;
import com.example.sharecare.Logic.GroupsSQLLiteDatabaseHelper;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GroupInfoActivity extends AppCompatActivity {
    private ActivitySQLLiteDatabaseHelper activityDatabaseHelper;

    private GroupsSQLLiteDatabaseHelper groupsDatabaseHelper;

    private int ishost;
    private TableLayout tableLayout;

    private Button createActivityButton;
    private int loggedInUserId;
    private String loggedInUsername;
    private int groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);
        activityDatabaseHelper= new ActivitySQLLiteDatabaseHelper(this);
        SQLiteDatabase activityDatabase = activityDatabaseHelper.getReadableDatabase();
        //create table if not exist
        activityDatabaseHelper.onCreate(activityDatabase);
        createActivityButton = findViewById(R.id.createActivityButton);
        groupsDatabaseHelper = new GroupsSQLLiteDatabaseHelper(this);
        SQLiteDatabase groupsDatabase = groupsDatabaseHelper.getReadableDatabase();
        //create table if not exist
        groupsDatabaseHelper.onCreate(groupsDatabase);

        tableLayout = findViewById(R.id.tableLayout);

        // Retrieve the group ID and isHost value from the intent
        Intent intent = getIntent();
        groupId = intent.getIntExtra("groupid", -1);
        loggedInUserId = getIntent().getIntExtra("userid",-1);
        loggedInUsername = getIntent().getStringExtra("username");
        ishost = intent.getIntExtra("ishost", -1);


        createActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupInfoActivity.this, ChildrenActivityCreateActivity.class);
                Bundle extras = new Bundle();
                extras.putInt("userid", loggedInUserId);
                extras.putInt("groupid", groupId);
                extras.putString("username", loggedInUsername);
                extras.putInt("ishost", ishost);

                intent.putExtras(extras);
                startActivity(intent);
            }
        });
        loadGroupData();
        loadGroupActivityData();

        activityDatabase.close();
        groupsDatabase.close();
    }

    private void loadGroupData() {
        SQLiteDatabase groupsDatabase = groupsDatabaseHelper.getReadableDatabase();
        // Query the groups table to retrieve group ID and group name
        Cursor cursor = groupsDatabase.rawQuery("SELECT id as groupdid, groupName  as groupname FROM groups WHERE id = ?", new String[]{String.valueOf(groupId)});
        if (cursor.moveToFirst()) {
            String groupName = cursor.getString(cursor.getColumnIndex("groupname"));

            // Display the group name in a label
            TextView groupNameTextView = findViewById(R.id.groupNameTextView);
            groupNameTextView.setText("Group Name : "+ groupName);
        }
        cursor.close();
        groupsDatabase.close();
    }

    private void loadGroupActivityData() {
        // you will see activity if :
        // you created the group , you are in the group
        //you will see edit, delete , mange request if you are the owner of the activity
        // you will join button if you are not joined in the activity
        SQLiteDatabase activityDatabase = activityDatabaseHelper.getReadableDatabase();
        Cursor cursor = activityDatabase.rawQuery(
                "select distinct activity_id,activity_name,activity_type,date,time,capcaity,child_age_from,child_age_to,isaccept,isowner from ( " +
                        "SELECT  activities.id as  activity_id ,  activity_name,activity_type,date,time,capcaity,child_age_from,child_age_to,case when activities.owner_user_id=? then 1 else 0 end  as  isaccept," +
                        "case when activities.owner_user_id=? then 1 else 0 end  as isowner" +
                " from activities inner join groups on activities.groupid=groups.id where groups.id=?  " +
                " union all " +
                " SELECT  activities.id as activity_id , activity_name,activity_type,date,time, " +
                " capcaity,child_age_from,child_age_to ,ar.isaccept as isaccept ,0 as isowner FROM activities " +
                " inner join  activitiesRequest ar on ar.activityid =activities.id where groupid = ? and activities.owner_user_id<>? ) "
                , new String[]{String.valueOf(loggedInUserId),String.valueOf(loggedInUserId),String.valueOf(groupId),String.valueOf(groupId),String.valueOf(loggedInUserId)});

        while (cursor.moveToNext()) {
            int activity_id = cursor.getInt(cursor.getColumnIndex("activity_id"));
            String activity_name = cursor.getString(cursor.getColumnIndex("activity_name"));
            String activity_type = cursor.getString(cursor.getColumnIndex("activity_type"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            String capcaity = cursor.getString(cursor.getColumnIndex("capcaity"));
            String child_age_from = cursor.getString(cursor.getColumnIndex("child_age_from"));
            String child_age_to = cursor.getString(cursor.getColumnIndex("child_age_to"));
            int isaccept = cursor.getInt(cursor.getColumnIndex("isaccept"));
            int isowner = cursor.getInt(cursor.getColumnIndex("isowner"));

            // Create a new row in the table
            TableRow row = new TableRow(this);

            // Create TextViews for name and activity
            TextView nameTextView = new TextView(this);
            nameTextView.setText(activity_name);
           // nameTextView.setPadding(8, 8, 8, 8);
            row.addView(nameTextView);

            // Create a Button for "More Info"
            Button moreInfoButton = new Button(this);
            moreInfoButton.setText("More Info");
            moreInfoButton.setPadding(8, 8, 8, 8);
            moreInfoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle the button click event
                    openActivityDialog(activity_name,activity_type,date,time,capcaity,child_age_from,child_age_to);
                }
            });
            row.addView(moreInfoButton);

            if(isowner ==1)
            {
                // Create buttons for delete, edit, and manage requests
                Button deleteButton = new Button(this);
                deleteButton.setText("Delete");
                deleteButton.setPadding(8, 8, 8, 8);
                row.addView(deleteButton);


                //for now we will not support edit
//                Button editButton = new Button(this);
//                editButton.setText("Edit");
//                editButton.setPadding(8, 8, 8, 8);
//                row.addView(editButton);

                Button manageRequestsButton = new Button(this);
                manageRequestsButton.setText("Manage Requests");
              //  manageRequestsButton.setPadding(8, 8, 8, 8);
                row.addView(manageRequestsButton);

                // Set click listeners for the buttons
                deleteButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        activityDatabase.delete("activities","id = ?", new String[]{String.valueOf(activity_id)});
                        Toast.makeText(GroupInfoActivity.this, "successfully deleted " + activity_name, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(GroupInfoActivity.this, GroupInfoActivity.class);
                        Bundle extras = new Bundle();
                        extras.putInt("userid", loggedInUserId);
                        extras.putInt("groupid", groupId);
                        extras.putString("username", loggedInUsername);
                        extras.putInt("ishost", ishost);
                        intent.putExtras(extras);
                        startActivity(intent);
                    }
                });

//                editButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // Handle edit button click
//                        Toast.makeText(GroupInfoActivity.this, "Edit button clicked for " + activity_name, Toast.LENGTH_SHORT).show();
//                    }
//                });

                manageRequestsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(GroupInfoActivity.this, PendingGroupRequestsActivity.class);
                        Bundle extras = new Bundle();
                        extras.putInt("userid", loggedInUserId);
                        extras.putInt("groupid", groupId);
                        extras.putString("username", loggedInUsername);
                        extras.putInt("ishost", ishost);


                        intent.putExtras(extras);
                        startActivity(intent);

                        // Handle manage requests button click
                        Toast.makeText(GroupInfoActivity.this, "Manage Requests button clicked for " + activity_name, Toast.LENGTH_SHORT).show();
                    }
                });

            }else if(isaccept==1)
            {
                TextView activityjoinedTextView = new TextView(this);
                activityjoinedTextView.setText("joined");
               // activityTextView.setPadding(8, 8, 8, 8);
                row.addView(activityjoinedTextView);
            }
            else if(isaccept==0)
            {
                Button joinButton = new Button(this);
                joinButton.setText("Join");
               // joinButton.setPadding(8, 8, 8, 8);
                row.addView(joinButton);

                joinButton.setOnClickListener(new View.OnClickListener() {

                    public void insertActivityRequest(int userId, int activityId) {

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        String date = dateFormat.format(new Date());

                        ContentValues values = new ContentValues();
                        values.put("userid", userId);
                        values.put("activityid", activityId);
                        values.put("requestDate", date);
                        values.put("isaccept", false);

                        activityDatabase.insert("activitiesRequest", null, values);
                    }

                    @Override
                    public void onClick(View v) {
                        insertActivityRequest(loggedInUserId,activity_id);
                        Toast.makeText(GroupInfoActivity.this, "Your request submitted to activity owner ", Toast.LENGTH_SHORT).show();
                    }
                });
            }


            // Add the row to the table
            tableLayout.addView(row);
        }
        cursor.close();
        activityDatabase.close();
    }

    private void openActivityDialog(String activity_name,String activity_type,
           String date,String time,String capcaity,String child_age_from,String child_age_to) {

        // Create an instance of the ActivityDialog and display it
        ActivityDialog activityDialog = new ActivityDialog(this, activity_name, activity_type,
                date,time,capcaity,child_age_from,child_age_to);
        activityDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityDatabaseHelper.close();
        groupsDatabaseHelper.close();
    }

}