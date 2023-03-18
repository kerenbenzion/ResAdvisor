package com.example.resadvisor.model;

import java.util.LinkedList;
import java.util.List;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;

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
    AppLocalDbRepository localDb=AppLocalDb.getAppDb();
    public interface GetAllPostsListener{
        void onComplete(List<Post> data);
    }
    public interface GetAllResturantsListener{
        void onComplete(List<Resturant> data);
    }

    private LiveData<List<Post>> postsList;
    public LiveData<List<Post>> getAllPosts(){
        if(postsList ==null){
            postsList = localDb.postDao().getAll();
        }
        return postsList;

    }

    public void refreshAllPosts()
    {
        //get local last update
        Long localLastUpdate = Post.getLocalLastUpdate();
        //get all  posts since
        firestore.getAllPostsSince(localLastUpdate,list->{
            executor.execute(()->{
                Long time = localLastUpdate;
                for(Post ps:list){
                    //insert new records into ROOM
                    AppLocalDb.getAppDb().postDao().insertAll(ps);
                    if(time<ps.getLastUpdated()){
                        time=ps.getLastUpdated();
                    }
                }
                // update local last update
                Post.setLocalLastUpdate(time);
            });

        });
    }

    public void getUserPosts(GetAllPostsListener callback, String userEmail)
    {
        Long localLastUpdate = Post.getLocalLastUpdate();
        firestore.getUserPostsSince(localLastUpdate, userEmail,list->{
            executor.execute(()->{
                Long time = localLastUpdate;
                for(Post ps:list){
                    //insert new records into ROOM
                    AppLocalDb.getAppDb().postDao().insertAll(ps);
                    if(time<ps.getLastUpdated()){
                        time=ps.getLastUpdated();
                    }
                }
                Post.setLocalLastUpdate(time);
                //return complete list from ROOM
                List<Post> complete = AppLocalDb.getAppDb().postDao().getUsersPosts(userEmail);
                mainHandler.post(()->{
                    callback.onComplete(complete);
                });

            });

        });
    }

    public void getPost (String postId, EditText et_title, EditText et_desc, EditText et_price,
                         EditText et_res_name, EditText et_res_address, ImageView imgView){
        firestore.getPost(postId, et_title, et_desc, et_price, et_res_name, et_res_address, imgView);
    }

    public void getAllResturants(GetAllResturantsListener callback)
    {
        Long localLastUpdate = Resturant.getLocalLastUpdate();
        firestore.getAllResturantsSince(localLastUpdate,list->{
            executor.execute(()->{
                Long time = localLastUpdate;
                for(Resturant resturant:list){
                    //insert new records into ROOM
                    AppLocalDb.getAppDb().resturantDao().insertAll(resturant);
                    if(time<resturant.getLastUpdated()){
                        time=resturant.getLastUpdated();
                    }
                }
                // update local last update
                Resturant.setLocalLastUpdate(time);
                //return complete list from ROOM
                List<Resturant> complete = AppLocalDb.getAppDb().resturantDao().getAll();
                mainHandler.post(()->{
                    callback.onComplete(complete);
                });
            });
        });
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
    public interface SignInListener{
        void onComplete(boolean data);
    }
    public void signIn(String email, String password,SignInListener callback){
        authModel.signIn(email,password,callback);
    }
    public void register(String email, String password,String firstname,String lastname,ImageView IVPreviewImage, SignInListener callback){
        authModel.register(email,password,firstname,lastname,IVPreviewImage,callback);
    }

}
