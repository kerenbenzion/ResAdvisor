package com.example.resadvisor.model;

import java.util.LinkedList;
import java.util.List;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Model {

    static final Model _instance = new Model();
    private Executor executor = Executors.newSingleThreadExecutor();
    private Handler mainHandler = HandlerCompat.createAsync(Looper.getMainLooper());
    private FirebaseStoreageModel firebaseModel = new FirebaseStoreageModel();
    private FirebaseAuthModel authModel = new FirebaseAuthModel();
    AppLocalDbRepository localDb = AppLocalDb.getAppDb();


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
    final public MutableLiveData<LoadingState> EventPostsListLoadingState = new MutableLiveData<LoadingState>(LoadingState.NOT_LOADING);

    private LiveData<List<Post>> postslist;

    public interface GetAllPostsListener{
        void onComplete(List<Post> data);
    }
    public interface GetAllResturantsListener{
        void onComplete(List<Resturant> data);
    }
    public List<Post> getallposts(){
        return AppLocalDb.getAppDb().postDao().getAllposts();
    }
    public void getAllPosts(GetAllPostsListener callback)
    {
        //
        EventPostsListLoadingState.setValue(LoadingState.LOADING);
        @NonNull
        Long locallastupdated = Post.getLocalLastUpdate();

        //get local last update
        firestore.getAllPostsSince(locallastupdated,list->{
            executor.execute(()->{
                Log.d("TAG", " firebase return : " + list.size());
                Long time = locallastupdated;
                for(Post ps: list){
                    //insert new record into ROOM
                    localDb.postDao().insertAll();
                    if(time<ps.getLastUpdated()){
                        time = ps.getLastUpdated();
                    }
                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //update local last update
                Post.setLocalLastUpdate(time);

                // return complete list from ROOM

                postslist = localDb.postDao().getAll();
//                mainHandler.post(()->{
//                    callback.onComplete(complete);
//                });

            });
        });
        //get all updated record from firebase since local last updated


    }

    public void getUserPosts(GetAllPostsListener callback, String userEmail)
    {
        firestore.getUserPosts(callback, userEmail);

    }

    public void getAllResturants(GetAllResturantsListener callback)
    {
        firestore.getAllResturants(callback);

    }

//    public void addPost(Post post)
//    {
//        data.add(post);
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

    public void getBitMap(String path, ImageView img) {
        firebaseModel.getImage(path,img);
    }

}
