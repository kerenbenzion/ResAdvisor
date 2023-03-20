package com.example.resadvisor;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.resadvisor.databinding.FragmentPostsListBinding;
import com.example.resadvisor.model.Model;
import com.example.resadvisor.model.Post;

import java.util.LinkedList;
import java.util.List;

public class PostsListFragment extends Fragment {

//    List<Post> data = new LinkedList<>();
    PostsRecyclerAdapter adapter;
    PostsListFragmentViewModel viewModel;
    FragmentPostsListBinding binding;

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
        binding = FragmentPostsListBinding.inflate(inflater, container, false);

        View view  = binding.getRoot();
        adapter = new PostsRecyclerAdapter(getLayoutInflater(), viewModel.getData().getValue(), false);

        binding.postslistfragList.setHasFixedSize(true);

        binding.postslistfragList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.postslistfragList.setAdapter(adapter);

        viewModel.getData().observe(getViewLifecycleOwner(),(list1)->{
            adapter.setData(list1);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        reloadData();
    }

    private void reloadData() {
        binding.progressBar.setVisibility(View.VISIBLE);
        Model.instance().refreshAllPosts();
        binding.progressBar.setVisibility(View.GONE);

    }

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(PostsListFragmentViewModel.class);
    }

}