package com.example.resadvisor;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;

import android.util.Log;
import android.view.Menu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.resadvisor.model.Model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.resadvisor.model.FirebaseAuthModel;


public class ProfileFragment extends Fragment {
    private EditText profile_first_name_et, email_et;
    private EditText passwordTextView, updatedPasswordTextView;
    FirebaseAuth mAuth;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity parentActivity = getActivity();
        parentActivity.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.removeItem(R.id.profileFragment);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        }, this, Lifecycle.State.RESUMED);

    }


    private void updatePassword1(String password, String newPass) {
        FirebaseUser user = Model.instance().getcurrent();
        final String email = user.getEmail();
        Log.d(TAG, "email");
        Log.d(TAG, email);
        Log.d(TAG, "Password");
        Log.d(TAG, password);
        AuthCredential credential = EmailAuthProvider.getCredential(email, newPass);
        user.updatePassword(newPass)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User password updated.");
                            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Log.d(TAG, "User re-authenticated.");
                                    }else {
                                        Log.d("TAG", "Did not e-authenticate");
                                    }
                                }
                            });

                        }else {
                            Log.d("TAG","User did not change password");
                        }
                    }
                });

    }



    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_profile, container, false);
        String displayName = Model.instance().getcurrent().getDisplayName();
        profile_first_name_et = rootView.findViewById(R.id.profile_first_name_et);
        profile_first_name_et.setText(displayName);

        String email = Model.instance().getcurrent().getEmail();
        email_et = rootView.findViewById(R.id.editTextTextEmailAddress);
        email_et.setText(email);


        passwordTextView = rootView.findViewById(R.id.profileCurrentPass);
        updatedPasswordTextView = rootView.findViewById(R.id.profileUpdatePass);



        Button updatePass = rootView.findViewById(R.id.profile_change_pass_btn);

        updatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String currentPass = passwordTextView.getText().toString();
                String newPass = updatedPasswordTextView.getText().toString();
                Log.d("TAG", "currentPass");
                Log.d("TAG", currentPass);
                updatePassword1(currentPass,newPass );
                Log.d("TAG", String.valueOf(Model.instance().getcurrent()));
            }
        });




        return rootView;
    }
}