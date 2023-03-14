package com.example.resadvisor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.resadvisor.model.Model;
import com.example.resadvisor.model.Post;

import java.util.LinkedList;
import java.util.List;

public class PostsListFragment extends Fragment {

    List<Post> data = new LinkedList<>();
    PostsRecyclerAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity parentActivity = getActivity();

        parentActivity.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.removeItem(R.id.postsListFragment);

            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        },this, Lifecycle.State.RESUMED);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_posts_list, container, false);
        adapter = new PostsRecyclerAdapter(getLayoutInflater(), data, false);

        Model.instance().getAllPosts((postsList)->{
            data = postsList;
            adapter.setData(data);
        });

        RecyclerView list = view.findViewById(R.id.postslistfrag_list);
        list.setHasFixedSize(true);

        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.setAdapter(adapter);

        return view;
    }

}