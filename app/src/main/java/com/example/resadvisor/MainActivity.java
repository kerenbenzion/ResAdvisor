package com.example.resadvisor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    Button signup;
    Intent signupInent;
    int REQUEST_CODE=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signup =findViewById(R.id.SignUp);
        signup.setOnClickListener(view -> {
            signupInent = new Intent(this, Signup_activity.class);
            startActivityForResult(signupInent,REQUEST_CODE);

        });
    }
//    public void signup() {
//        Intent intent = new Intent(MainActivity.this, Signup_activity.class);
//        startActivity(intent);
//    }
}