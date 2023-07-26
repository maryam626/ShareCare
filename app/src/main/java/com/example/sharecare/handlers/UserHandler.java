package com.example.sharecare.handlers;

import android.content.Context;

import com.example.sharecare.Logic.UsersSQLLiteDatabaseHelper;
import com.example.sharecare.models.User;

public class UserHandler {

    private UsersSQLLiteDatabaseHelper databaseHelper;

    public UserHandler(Context context) {
        databaseHelper = new UsersSQLLiteDatabaseHelper(context);
    }

    public long insertUser(User user) {
        return databaseHelper.insertUser(user);
    }
}
