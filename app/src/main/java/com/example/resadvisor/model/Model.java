package com.example.resadvisor.model;

import java.util.LinkedList;
import java.util.List;

public class Model {

    static final Model _instance = new Model();

    public static Model instance()
    {
        return _instance;
    }
    private Firestore firestore = new Firestore();
    private Model()
    {
    }

    List<Post> data = new LinkedList<>();

    public interface GetAllPostsListener{
        void onComplete(List<Post> data);
    }
    public void getAllPosts(GetAllPostsListener callback)
    {
        firestore.getAllPosts(callback);
        //callback.onComplete(data);
    }

    public void addPost(Post post)
    {
        data.add(post);
    }


}
