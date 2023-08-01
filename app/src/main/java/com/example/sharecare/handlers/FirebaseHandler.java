package com.example.sharecare.handlers;

import androidx.annotation.NonNull;

import com.example.sharecare.Logic.ActivitySQLLiteDatabaseHelper;
import com.example.sharecare.Logic.UsersSQLLiteDatabaseHelper;
import com.example.sharecare.models.Activity;
import com.example.sharecare.models.Host;
import com.example.sharecare.models.PendingActivityRequestDTO;
import com.example.sharecare.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseHandler {
    private FirebaseFirestore firebaseDb;
    public static boolean isInFirebase;


    public FirebaseHandler(FirebaseFirestore db) {
        firebaseDb = db;
    }

    public void addingActivityDataToFirebase(Activity activity, OnSuccessListener successListener, OnFailureListener failureListener) {
        Map<String, Object> activityMap = new HashMap<>();
        activityMap.put("activityName", activity.getActivityName());
        activityMap.put("activityId", activity.getId());
        activityMap.put("selectedActivity", activity.getSelectedActivity());
        activityMap.put("selectedDate", activity.getSelectedDate());
        activityMap.put("selectedTime", activity.getSelectedTime());
        activityMap.put("capacity", activity.getCapacity());
        activityMap.put("duration", activity.getDuration());
        activityMap.put("ageFrom", activity.getAgeFrom());
        activityMap.put("ageTo", activity.getAgeTo());
        activityMap.put("ownerUserId", activity.getOwnerUserId());
        activityMap.put("groupId", activity.getGroupId());

        Task task = firebaseDb.collection("Activities")
                .add(activityMap)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
        while(!task.isComplete()){

        }
    }

    public void syncActivitiesRequestFromFirebaseToSQLite(ActivitySQLLiteDatabaseHelper databaseHelper) {
        firebaseDb.collection("activitiesRequest")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<PendingActivityRequestDTO> requests = queryDocumentSnapshots.toObjects(PendingActivityRequestDTO.class);
                        databaseHelper.syncActivitiesRequestFromFirebase(requests);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle the failure if needed
                    }
                });
    }

    // Add a method to synchronize activitiesRequest from SQLite to Firebase
    public void syncActivitiesRequestFromSQLiteToFirebase(ActivitySQLLiteDatabaseHelper databaseHelper) {
        List<PendingActivityRequestDTO> requests = databaseHelper.getAllPendingActivityRequests();
        for (PendingActivityRequestDTO request : requests) {
            firebaseDb.collection("activitiesRequest")
                    .document(String.valueOf(request.getId()))
                    .set(request)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Update successful
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Update failed
                        }
                    });
        }
    }
    // Add a method to synchronize activities from Firebase to SQLite
    public void syncActivitiesFromFirebaseToSQLite(ActivitySQLLiteDatabaseHelper databaseHelper) {
        firebaseDb.collection("Activities")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Activity> activities = queryDocumentSnapshots.toObjects(Activity.class);
                        databaseHelper.syncActivitiesFromFirebase(activities);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle the failure if needed
                    }
                });
    }

    // Add a method to synchronize activities from SQLite to Firebase
    public void syncActivitiesFromSQLiteToFirebase(ActivitySQLLiteDatabaseHelper databaseHelper) {
        List<Activity> activities = databaseHelper.getAllActivities();
        for (Activity activity : activities) {
            firebaseDb.collection("Activities")
                    .document(String.valueOf(activity.getId()))
                    .set(activity)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Update successful
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Update failed
                        }
                    });
        }
    }

    public void updateActivityDataInFirebase(Activity activity, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {
        Map<String, Object> activityMap = new HashMap<>();
        activityMap.put("activityName", activity.getActivityName());
        activityMap.put("selectedActivity", activity.getSelectedActivity());
        activityMap.put("selectedDate", activity.getSelectedDate());
        activityMap.put("selectedTime", activity.getSelectedTime());
        activityMap.put("capacity", activity.getCapacity());
        activityMap.put("duration", activity.getDuration());
        activityMap.put("ageFrom", activity.getAgeFrom());
        activityMap.put("ageTo", activity.getAgeTo());
        activityMap.put("ownerUserId", activity.getOwnerUserId());
        activityMap.put("groupId", activity.getGroupId());
        activityMap.put("activityId", activity.getId());


        FirebaseFirestore firebaseDb = FirebaseFirestore.getInstance();
        CollectionReference activitiesCollectionRef = firebaseDb.collection("Activities");

        activitiesCollectionRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (!queryDocumentSnapshots.isEmpty()) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    // Get the document data as a Map
                    Map<String, Object> activityData = documentSnapshot.getData();
                    // Print the document data
                    for (String key : activityData.keySet()) {
                        Object value = activityData.get(key);
                        System.out.println(key + " : " + value);
                    }
                    System.out.println("-------------------------");
                }
            } else {
                System.out.println("No documents found in the 'Activities' collection.");
            }
        }).addOnFailureListener(e -> {
            System.out.println("Error fetching documents: " + e.getMessage());
        });


                firebaseDb.collection("Activities")
                .whereEqualTo("activityId", activity.getId())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Assuming there's only one document with the given activity id
                        String documentId = queryDocumentSnapshots.getDocuments().get(0).getId();
                        firebaseDb.collection("Activities")
                                .document(documentId)
                                .set(activityMap)
                                .addOnSuccessListener(successListener)
                                .addOnFailureListener(failureListener);
                    } else {
                        // Handle the case when the activity with the given id is not found
                        // (optional: you can add the new activity here instead of updating)
                        failureListener.onFailure(new Exception("Activity not found with the given id"));
                    }
                })
                .addOnFailureListener(failureListener);
    }

    public void addingHostDataToFirebase(Host host, OnSuccessListener successListener, OnFailureListener failureListener){
        boolean isInFirebase = checkIfTheHostIsAlreadyInFirebase(host);

        if(!isInFirebase){
        Map<String, Object> hostMap = new HashMap<>();
        hostMap.put("id", "0");
        hostMap.put("username", host.getUsername());
        hostMap.put("phoneNumber", host.getPhoneNumber());
        hostMap.put("email", host.getEmail());
        hostMap.put("address", host.getAddress());
        hostMap.put("password", host.getPassword());
        hostMap.put("numberOfKids", host.getNumberOfKids());
        hostMap.put("maritalStatus", host.getMaritalStatus());
        hostMap.put("gender", host.getGender());
        hostMap.put("language", host.getLanguage());
        hostMap.put("religion", host.getReligion());


        Task task = firebaseDb.collection("Hosts")
                .add(hostMap)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
        while(!task.isComplete()){

        }}
    }

    private boolean checkIfTheHostIsAlreadyInFirebase(Host host) {
        Task<QuerySnapshot> task = firebaseDb.collection("Hosts").whereEqualTo("email", host.getEmail()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.size() > 0){
                    FirebaseHandler.isInFirebase = true;
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                FirebaseHandler.isInFirebase = false;

            }
        });

        while(!task.isComplete()){
            System.out.print(1);
        }

        System.out.println(FirebaseHandler.isInFirebase);

        return FirebaseHandler.isInFirebase;
    }


    // Add a method to synchronize users from Firebase to SQLite
    public void syncUsersFromFirebaseToSQLite(UsersSQLLiteDatabaseHelper databaseHelper) {
        firebaseDb.collection("users")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<User> users = queryDocumentSnapshots.toObjects(User.class);
                        databaseHelper.syncUsersFromFirebase(users);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle the failure if needed
                    }
                });
    }

    // Add a method to synchronize users from SQLite to Firebase
    public void syncUsersFromSQLiteToFirebase(UsersSQLLiteDatabaseHelper databaseHelper) {
        List<User> users = databaseHelper.getAllUsers();
        for (User user : users) {
            firebaseDb.collection("users")
                    .document(String.valueOf(user.getId()))
                    .set(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Update successful
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Update failed
                        }
                    });
        }
    }


}
