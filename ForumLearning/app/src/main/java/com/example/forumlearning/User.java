package com.example.forumlearning;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    private String id;

    public String getId() {
        return id;
    }

    public String getFullname() {
        return fullname;
    }

    public String getEmail() {
        return email;
    }

    private String fullname;
    private String email;
//    private String dob;
//    private String location;
//    private String bio;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String id, String email, String fullname) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
    }



}
