package com.example.resadvisor;

import androidx.lifecycle.ViewModel;

import com.example.resadvisor.model.Post;

import java.util.LinkedList;
import java.util.List;

public class PostsListFragmentViewModel extends ViewModel {
    private List<Post> data = new LinkedList<>();

    public List<Post> getData() {
        return data;
    }

    public void setData(List<Post> data) {
        this.data = data;
    }
}
