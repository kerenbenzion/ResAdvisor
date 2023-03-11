package com.example.resadvisor;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.example.resadvisor.model.Model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.ByteArrayOutputStream;


public class ProfileFragment extends Fragment {
    private EditText profile_first_name_et, email_et;
    private EditText passwordTextView, updatedPasswordTextView;
    FirebaseAuth mAuth;
    int SELECT_PICTURE = 200;
    ImageView IVPreviewImage;


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


    private void updateUserPassword(String password, String newPass) {
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
                            Toast.makeText(getContext(),
                                            "User password updated.",
                                            Toast.LENGTH_LONG)
                                    .show();
                            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "User re-authenticated.");
                                    } else {
                                        Log.d("TAG", "Did not e-authenticate");
                                    }
                                }
                            });

                        } else {
                            Log.d("TAG", "User did not change password");
                        }
                    }
                });

    }
    private void image_chooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    @SuppressLint({"MissingInflatedId", "WrongThread"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
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
            public void onClick(View v) {
                String currentPass = passwordTextView.getText().toString();
                String newPass = updatedPasswordTextView.getText().toString();
                updateUserPassword(currentPass, newPass);
            }
        });

        IVPreviewImage = rootView.findViewById(R.id.IVProfilePreviewImage);
        Model.instance().getBitMap(Model.instance().getcurrent().getUid()+"_",IVPreviewImage);


        Button add_image = rootView.findViewById(R.id.profile_add_image_btn);
        add_image.setOnClickListener(view1->{
            image_chooser();
        });
        Button save_profile = rootView.findViewById(R.id.profile_save);
        save_profile.setOnClickListener(view1->{
            Bitmap bmap = ((BitmapDrawable) IVPreviewImage.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            Model.instance().uploadImage(Model.instance().getcurrent().getUid()+"_",data,url->Log.d("TAG","Start to upload"));
            Model.instance().getBitMap(Model.instance().getcurrent().getUid()+"_",IVPreviewImage);
            Toast.makeText(getContext(),
                            "User profile image updated.",
                            Toast.LENGTH_LONG)
                    .show();
        });
        return rootView;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    IVPreviewImage.setImageURI(selectedImageUri);

                }
            }
        }
    }
}