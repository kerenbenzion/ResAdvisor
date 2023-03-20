package com.example.resadvisor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.resadvisor.model.Model;
import com.example.resadvisor.model.Post;

import java.util.LinkedList;
import java.util.List;

public class PostsListFragmentViewModel extends ViewModel {
    private LiveData<List<Post>> data = Model.instance().getAllPosts();

    public LiveData<List<Post>> getData() {
        return data;
    }

}
