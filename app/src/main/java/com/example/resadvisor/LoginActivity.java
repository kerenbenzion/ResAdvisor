package com.example.resadvisor;
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
import com.example.resadvisor.model.Post;
import com.example.resadvisor.model.Resturant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.atomic.AtomicBoolean;

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
        Post.setLocalLastUpdate(new Long(0));
        Resturant.setLocalLastUpdate(new Long(0));

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

    private void loginUserAccount() {
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

        Model.instance().signIn(email,password,(ok)->{
            if(ok){
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(getApplicationContext(), "Login failed!", Toast.LENGTH_LONG).show();
            }
        });


//        Toast.makeText(getApplicationContext(), "IsLoggedIn: "+managed_to_login, Toast.LENGTH_LONG).show();

//        mAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(
//                        new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(
//                                    @NonNull Task<AuthResult> task)
//                            {
//                                if (task.isSuccessful()) {
//                                    Toast.makeText(getApplicationContext(),
//                                                    "Login successful!",
//                                                    Toast.LENGTH_LONG)
//                                            .show();
//
//                                    // if successful - go to main activity
//                                    Intent intent
//                                            = new Intent(LoginActivity.this,
//                                            MainActivity.class);
//                                    startActivity(intent);
//                                }
//                                else {
//                                    // sign-in failed

//                                }
//                            }
//                        });
    }
}