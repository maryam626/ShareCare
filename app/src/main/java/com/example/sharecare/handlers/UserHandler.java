package com.example.sharecare.handlers;

import android.content.Context;
import android.os.AsyncTask;

import com.example.sharecare.Logic.UsersSQLLiteDatabaseHelper;
import com.example.sharecare.models.User;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A handler class to manage operations related to the User.
 */
public class UserHandler {

    private class SyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try{
            // Run the synchronization task in the background (async)
            firebaseHandler.syncUsersFromFirebaseToSQLite(databaseHelper);
            firebaseHandler.syncUsersFromFirebaseToSQLite(databaseHelper);

            }
            catch(Exception ex)
            {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
        }
    }


    /** Database helper instance to interact with the SQLite database */
    private UsersSQLLiteDatabaseHelper databaseHelper;
    private FirebaseHandler firebaseHandler;

    /**
     * Constructor to initialize the UserHandler with a context.
     * @param context The context used to initialize the UsersSQLLiteDatabaseHelper.
     */
    public UserHandler(Context context) {
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        databaseHelper = new UsersSQLLiteDatabaseHelper(context);
        firebaseHandler = new FirebaseHandler(db);
    }

    /**
     * Inserts a new user into the database.
     * @param user The user object with details to be saved.
     * @return The row ID of the newly inserted user, or -1 in case of an error.
     */
    public long insertUser(User user) {
        return databaseHelper.insertUser(user);
    }

    public void Sync()
    {
        new SyncTask().execute();
    }
}
