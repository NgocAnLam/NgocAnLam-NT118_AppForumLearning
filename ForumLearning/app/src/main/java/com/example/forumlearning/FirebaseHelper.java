package com.example.forumlearning;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class FirebaseHelper {

    private static final String TAG = "FirebaseHelper";

    public DatabaseReference mDatabase;


    public FirebaseHelper(DatabaseReference database) {
        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

//    public void writeNewUser(String userId, String name, String email) {
//        User user = new User(name, email);
//
//        mDatabase.child("users").child(userId).setValue(user);
//    }
}
