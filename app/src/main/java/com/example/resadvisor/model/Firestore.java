package com.example.resadvisor.model;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;
import java.util.List;

import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.graphics.ColorSpace;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;


public class Firestore {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final Firestore _instance = new Firestore(db);

    static {
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder().setPersistenceEnabled(false).build();
        db.setFirestoreSettings(settings);
    }

    public static Firestore instance() {
        return _instance;
    }

    public Firestore(FirebaseFirestore db) {
        this.db = db;
    }

    public FirebaseFirestore getDb() {
        return this.db;
    }

    Firestore ()
    {
        db = FirebaseFirestore.getInstance();
    }

    public void getAllPosts(Model.GetAllPostsListener callback){
        db.collection("published_posts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<Post> list = new LinkedList<>();
                        if (task.isSuccessful()){
                            QuerySnapshot jsonsList = task.getResult();
                            for (DocumentSnapshot json: jsonsList){
                                Post st = Post.fromJson(json.getData());
                                list.add(st);
                            }
                        }
                        callback.onComplete(list);
                    }
                });
    }

    public void getAllResturants(Model.GetAllResturantsListener callback){
        db.collection("resturants")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<Resturant> list = new LinkedList<>();
                        if (task.isSuccessful()){
                            QuerySnapshot jsonsList = task.getResult();
                            for (DocumentSnapshot json: jsonsList){
                                Resturant st = Resturant.fromJson(json.getData());
                                list.add(st);
                            }
                        }
                        callback.onComplete(list);
                    }
                });
    }


    public void getUserPosts(Model.GetAllPostsListener callback, String userEmail){
        db.collection("published_posts")
                .whereEqualTo(Post.EMAIL, userEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<Post> list = new LinkedList<>();
                        if (task.isSuccessful()){
                            QuerySnapshot jsonsList = task.getResult();
                            for (DocumentSnapshot json: jsonsList){
                                Post st = Post.fromJson(json.getData());
                                list.add(st);
                            }
                        }
                        callback.onComplete(list);
                    }
                });
    }

    public void getPost(String postId, EditText et_title, EditText et_desc, EditText et_price,
                        EditText et_res_name, EditText et_res_address, ImageView imgView){
        DocumentReference docRef = db.collection("published_posts").document(postId);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Post post = Post.fromJson(document.getData());
                    et_title.setText(post.title);
                    et_desc.setText(post.description);
                    et_price.setText(post.price);
                    et_res_name.setText(post.res_name);
                    et_res_address.setText(post.res_address);
                    Model.instance().getBitMap( post.pic_path,imgView);

                    if (document.exists()) {
                        Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }

        });

    }

}
