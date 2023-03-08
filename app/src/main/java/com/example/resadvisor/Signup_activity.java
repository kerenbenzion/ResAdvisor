package com.example.resadvisor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.resadvisor.model.Model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.io.ByteArrayOutputStream;

public class Signup_activity extends AppCompatActivity {
    Button signin,signup,uploadpic;
    Intent signinInent;
    int REQUEST_CODE=1;
    ImageView IVPreviewImage;
    private FirebaseAuth mAuth;
    int SELECT_PICTURE = 200;
    private static final int pic_id = 123;
    private Uri mImageUri = null;

    @SuppressLint("MissingInflatedId")
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
        IVPreviewImage = findViewById(R.id.IVPreviewImage);
        IVPreviewImage.setDrawingCacheEnabled(true);
        IVPreviewImage.buildDrawingCache();
        uploadpic =findViewById(R.id.uploadpic);
        uploadpic.setOnClickListener(view -> {
            imageChooser();
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

            // validations for all inputs
            if (TextUtils.isEmpty(firstname)) {
                Toast.makeText(getApplicationContext(),
                                "Please enter first name",
                                Toast.LENGTH_LONG)
                        .show();
                return;
            }
            if (TextUtils.isEmpty(lastname)) {
                Toast.makeText(getApplicationContext(),
                                "Please enter last name",
                                Toast.LENGTH_LONG)
                        .show();
                return;
            }
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


            Log.d("TAG", firstname);
            Log.d("TAG", lastname);
            Log.d("TAG", email);
            Log.d("TAG", password);
            createAccount(email,password,firstname,lastname);
//            Bitmap bmap = IVPreviewImage.getDrawingCache();


        });



    }
    void imageChooser() {
        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Start the activity with camera_intent, and request pic id
        startActivityForResult(camera_intent, pic_id);

    }
    private void createAccount(String email, String password,String firstname,String lastname) {
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "createUserWithEmail:success");
                            Bitmap bmap = ((BitmapDrawable) IVPreviewImage.getDrawable()).getBitmap();
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] data = baos.toByteArray();
                            Model.instance().uploadImage(firstname+"_"+lastname,data,url->Log.d("TAG","Start to upload"));

                            FirebaseUser user =Model.instance().getcurrent();
                            UserProfileChangeRequest profileupdate = new UserProfileChangeRequest.Builder().setDisplayName(firstname).build();
                            user.updateProfile(profileupdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        String displayName =Model.instance().getcurrent().getDisplayName();
                                        Log.d("TAG","User profile updated");
                                        Log.d("TAG","Display name: "+ displayName);
                                        Intent intent
                                                = new Intent(Signup_activity.this,
                                                LoginActivity.class);
                                        startActivity(intent);

                                    } else {
                                        Log.d("TAG","User profile was not updated");
                                    }
                                }
                            });


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Signup_activity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // [END create_user_with_email]
    }
    private void reload() { }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        // Match the request 'pic id with requestCode
        if (requestCode == pic_id) {
            // BitMap is data structure of image file which store the image in memory
            Bitmap photo = (Bitmap) data.getExtras().get("data");
//            mImageUri = data.getData();
//            mSelectImage.setImageURI(mImageUri);
            // Set the image in imageview for display
            IVPreviewImage.setImageBitmap(photo);
        }
    }
}