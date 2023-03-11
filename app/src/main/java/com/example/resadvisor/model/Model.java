package com.example.resadvisor.model;

import java.util.LinkedList;
import java.util.List;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import androidx.core.os.HandlerCompat;

import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Model {

    static final Model _instance = new Model();
    private Executor executor = Executors.newSingleThreadExecutor();
    private Handler mainHandler = HandlerCompat.createAsync(Looper.getMainLooper());
    private FirebaseStoreageModel firebaseModel = new FirebaseStoreageModel();
    private FirebaseAuthModel authModel = new FirebaseAuthModel();
//    AppLocalDbRepository localDb = AppLocalDb.getAppDb();

    public static Model instance()
    {
        return _instance;
    }
    private Firestore firestore = new Firestore();
    private Model()
    {
    }
    
    public interface Listener<T>{
        void onComplete(T data);
    }


    public enum LoadingState{
        LOADING,
        NOT_LOADING
    }

    List<Post> data = new LinkedList<>();

    public interface GetAllPostsListener{
        void onComplete(List<Post> data);
    }
    public interface GetAllResturantsListener{
        void onComplete(List<Resturant> data);
    }

    public void getAllPosts(GetAllPostsListener callback)
    {
        firestore.getAllPosts(callback);
    }

    public void getUserPosts(GetAllPostsListener callback, String userEmail)
    {
        firestore.getUserPosts(callback, userEmail);
    }

    public void getAllResturants(GetAllResturantsListener callback)
    {
        firestore.getAllResturants(callback);
    }

    public void addPost(Post post)
    {
        data.add(post);
    }

    public FirebaseUser getcurrent(){
        return authModel.getUser();
    }
    public void signout(){
        authModel.signout();
    }
    public void uploadImage(String name, byte[] data, Listener<String> listener) {
        firebaseModel.uploadImage(name,data,listener);
    }

    public void getBitMap(String path, ImageView img) {
        firebaseModel.getImage(path,img);
    }

}
