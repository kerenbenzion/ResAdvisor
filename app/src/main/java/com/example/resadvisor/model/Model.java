package com.example.resadvisor.model;

import java.util.LinkedList;
import java.util.List;

public class Model {
    private static final Model _instance = new Model();

    public static Model instance(){
        return _instance;
    }

    List<Post> data = new LinkedList<>();
    public List<Post> getAllPosts(){
        return data;
    }

    public void addPost(Post st){
        data.add(st);
    }

    public int getNextId(){
        return this.getAllPosts().size();
    }

}