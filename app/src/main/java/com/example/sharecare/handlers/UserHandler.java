package com.example.sharecare.handlers;

import android.content.Context;

import com.example.sharecare.Logic.UsersSQLLiteDatabaseHelper;
import com.example.sharecare.models.User;

/**
 * A handler class to manage operations related to the User.
 */
public class UserHandler {

    /** Database helper instance to interact with the SQLite database */
    private UsersSQLLiteDatabaseHelper databaseHelper;

    /**
     * Constructor to initialize the UserHandler with a context.
     * @param context The context used to initialize the UsersSQLLiteDatabaseHelper.
     */
    public UserHandler(Context context) {
        databaseHelper = new UsersSQLLiteDatabaseHelper(context);
    }

    /**
     * Inserts a new user into the database.
     * @param user The user object with details to be saved.
     * @return The row ID of the newly inserted user, or -1 in case of an error.
     */
    public long insertUser(User user) {
        return databaseHelper.insertUser(user);
    }
}
