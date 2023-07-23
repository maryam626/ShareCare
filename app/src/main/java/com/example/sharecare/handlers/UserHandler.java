package com.example.sharecare.handlers;
import android.content.Context;

import com.example.sharecare.Logic.UsersDatabaseHelper;
import com.example.sharecare.models.User;

public class UserHandler {

    private UsersDatabaseHelper databaseHelper;

    public UserHandler(Context context) {
        databaseHelper = new UsersDatabaseHelper(context);
    }

    public long insertUser(User user) {
        return databaseHelper.insertUser(user);
    }
}