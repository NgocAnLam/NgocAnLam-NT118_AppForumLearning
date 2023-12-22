package com.example.forumlearning;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nextActivity();
            }
        }, 2000);
    }

    private void nextActivity() {
       FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
       if (user == null) { // user not login
           Intent intent = new Intent(this, Login.class);
           startActivity(intent);
       } else { // user logined
           Intent intent = new Intent(this, MainActivity.class);
           startActivity(intent);
       }
       finish();
    }
}