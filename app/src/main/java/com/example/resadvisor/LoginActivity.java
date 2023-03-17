package com.example.resadvisor;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;

import com.example.resadvisor.model.Model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText emailTextView, passwordTextView;
    private Button Btn;
    private TextView registerRedirect;

    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
        //Log out need to enable it on my profile page
//        Model.instance().signout();
        FirebaseUser fbuser = Model.instance().getcurrent();
        if (fbuser !=null){
            Intent intent
                    = new Intent(LoginActivity.this,
                    MainActivity.class);
            startActivity(intent);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        emailTextView = findViewById(R.id.email);
        passwordTextView = findViewById(R.id.password);
        Btn = findViewById(R.id.login);
        registerRedirect = findViewById(R.id.register_redirect);

        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                loginUserAccount();
                Log.d("TAG", "The current user is:");
                Log.d("TAG", String.valueOf(Model.instance().getcurrent()));
            }
        });
        registerRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent
                        = new Intent(LoginActivity.this,
                        Signup_activity.class);
                startActivity(intent);
            }
        });

    }
    public  void oncomplete(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void loginUserAccount()
    {
        String email, password;
        email = emailTextView.getText().toString();
        password = passwordTextView.getText().toString();

        // validations for email and password
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter email",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter password",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }
//        if (Model.instance().signin(email,password)){
//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            startActivity(intent);
//        } else {
//            Toast.makeText(getApplicationContext(), "Login failed!", Toast.LENGTH_LONG).show();
//        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(
                                    @NonNull Task<AuthResult> task)
                            {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(),
                                                    "Login successful!",
                                                    Toast.LENGTH_LONG)
                                            .show();

                                    // if successful - go to main activity
                                    Intent intent
                                            = new Intent(LoginActivity.this,
                                            MainActivity.class);
                                    startActivity(intent);
                                }
                                else {
                                    // sign-in failed
                                    Toast.makeText(getApplicationContext(),
                                                    "Login failed!",
                                                    Toast.LENGTH_LONG)
                                            .show();
                                }
                            }
                        });
    }
}