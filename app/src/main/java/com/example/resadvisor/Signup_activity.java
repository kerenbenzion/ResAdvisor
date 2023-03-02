package com.example.resadvisor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Signup_activity extends AppCompatActivity {
    Button signin,signup;
    Intent signinInent;
    int REQUEST_CODE=1;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        signin =findViewById(R.id.SignIn);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent
                        = new Intent(Signup_activity.this,
                        LoginActivity.class);
                startActivity(intent);
            }
        });

        signup = findViewById(R.id.signup);
        signup.setOnClickListener(view -> {
            EditText editText = (EditText)findViewById(R.id.firstname);
            String firstname = editText.getText().toString();
            editText = (EditText)findViewById(R.id.lastname);
            String lastname = editText.getText().toString();
            editText = (EditText)findViewById(R.id.email);
            String email = editText.getText().toString();
            editText = (EditText)findViewById(R.id.password);
            String password = editText.getText().toString();
            Log.d("TAG", firstname);
            Log.d("TAG", lastname);
            Log.d("TAG", email);
            Log.d("TAG", password);
            createAccount(email,password);
        });


    }
    private void createAccount(String email, String password) {
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Signup_activity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        // [END create_user_with_email]
    }
    private void reload() { }

    private void updateUI(FirebaseUser user) {

    }
}