package com.example.resadvisor.model;

import android.content.Intent;

import com.example.resadvisor.LoginActivity;
import com.example.resadvisor.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuthModel {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentuser;
    public FirebaseUser getUser (){
        currentuser = mAuth.getCurrentUser();
        return currentuser;
    }
    public void signout(){
        FirebaseAuth.getInstance().signOut();
    }
}
