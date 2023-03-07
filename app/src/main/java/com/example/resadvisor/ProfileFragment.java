package com.example.resadvisor;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
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

import com.example.resadvisor.model.Model;
import com.google.android.gms.tasks.OnCompleteListener;
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
    private String passwordTextView, updatedPasswordTextView;


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
        },this, Lifecycle.State.RESUMED);

    }


    private void updatePassword(String password, String newPass) {
        FirebaseUser user = new FirebaseAuthModel().getUser();
        final String email = user.getEmail();
        Log.d(TAG, "email");
        Log.d(TAG, email);
        Log.d(TAG, "Password");
        Log.d(TAG, password);

        AuthCredential credential = EmailAuthProvider.getCredential(email,password);
        Log.d(TAG, credential.toString());

//        Task<AuthResult> data = user.reauthenticateAndRetrieveData(credential);
//        Log.d(TAG, "getResult");
//
//        Log.d(TAG, data.getResult().toString());

        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(!task.isSuccessful()){
                                Log.d(TAG, "Password updated");
                            }else {
                                Log.d(TAG, "Error password not updated");

                            }
                        }
                    });
                }else {
                    Log.d(TAG, "Error auth failed");
                }
            }
        });
    }


//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        AuthCredential credential = EmailAuthProvider
//                .getCredential(email, password);
//        Log.d(TAG, email);
//        Log.d(TAG, password);
//
//        user.reauthenticate(credential)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()) {
//                                        Log.d(TAG, "Password updated");
//                                    } else {
//                                        Log.d(TAG, "Error password not updated");
//                                    }
//                                }
//                            });
//                        } else {
//                            Log.d(TAG, "Error auth failed");
//                        }
//                    }
//                });
//    }

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

//        EditText edtOldPass = rootView.findViewById(R.id.profileCurrentPassword_Et);
//
//        String pass = edtOldPass.getText().toString();

        passwordTextView = rootView.findViewById(R.id.profileCurrentPassword_Et).toString();
        updatedPasswordTextView = rootView.findViewById(R.id.profileUpdatePass).toString();

        Button updatePass = rootView.findViewById(R.id.profile_change_pass_btn);

        updatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                updatePassword(passwordTextView,updatedPasswordTextView );
                Log.d("TAG", String.valueOf(Model.instance().getcurrent()));
            }
        });




        return rootView;
    }
}