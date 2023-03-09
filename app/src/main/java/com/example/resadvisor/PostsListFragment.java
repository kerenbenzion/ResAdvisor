package com.example.resadvisor;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.resadvisor.model.Model;
import com.example.resadvisor.model.Post;

import java.util.LinkedList;
import java.util.List;

public class PostsListFragment extends Fragment {

    List<Post> data = new LinkedList<>();
    PostsRecyclerAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_posts_list, container, false);
        adapter = new PostsRecyclerAdapter(getLayoutInflater(), data);

        Model.instance().getAllPosts((postsList)->{
            data = postsList;
            adapter.setData(data);
        });

        RecyclerView list = view.findViewById(R.id.postslistfrag_list);
        list.setHasFixedSize(true);

        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.setAdapter(adapter);

        View button = view.findViewById(R.id.postslistfrag_add_btn);
        button.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_postsListFragment_to_addPostFragment2));
        return view;
    }
}