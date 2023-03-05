package com.example.resadvisor.model;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;

import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Model {
    private static final Model _instance = new Model();

    private Executor executor = Executors.newSingleThreadExecutor();
    private Handler mainHandler = HandlerCompat.createAsync(Looper.getMainLooper());
    private FirebaseStoreageModel firebaseModel = new FirebaseStoreageModel();
    private FirebaseAuthModel authModel = new FirebaseAuthModel();
//    AppLocalDbRepository localDb = AppLocalDb.getAppDb();

    public static Model instance(){
        return _instance;
    }
    private Model(){
    }

    public interface Listener<T>{
        void onComplete(T data);
    }


    public enum LoadingState{
        LOADING,
        NOT_LOADING
    }




//    public void addStudent(Student st, Listener<Void> listener){
//        firebaseModel.addStudent(st,(Void)->{
//            refreshAllStudents();
//            listener.onComplete(null);
//        });
//    }
    public FirebaseUser getcurrent(){
        return authModel.getUser();
    }
    public void signout(){
        authModel.signout();
    }
    public void uploadImage(String name, byte[] data, Listener<String> listener) {
        firebaseModel.uploadImage(name,data,listener);
    }
}
