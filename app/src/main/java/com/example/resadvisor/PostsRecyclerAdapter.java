package com.example.resadvisor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.resadvisor.model.Post;

import java.util.List;

class PostViewHolder extends RecyclerView.ViewHolder{

    TextView title;
    TextView description;
    TextView price;
    TextView email;
    List<Post> data;
    Boolean priceInNis;

    public PostViewHolder(@NonNull View itemView, List<Post> data) {
        super(itemView);
        title = itemView.findViewById(R.id.postslistrow_title);
        description = itemView.findViewById(R.id.postslistrow_description);
        price = itemView.findViewById(R.id.postslistrow_price);
        email = itemView.findViewById(R.id.postslistrow_email);
        Button convert = itemView.findViewById(R.id.postslistrow_convert_btn);
        this.data = data;
        priceInNis = true;
        convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int)price.getTag();
                Post post = data.get(pos);
                price.setText(priceInNis ? post.price_usd + " $" : post.price + " NIS");
                priceInNis = !priceInNis;
            }
        });
    }

    public void bind(Post post, int pos) {
        email.setText(post.email);
        title.setText(post.title);
        description.setText(post.description);
        price.setText(post.price + " NIS");
        price.setTag(pos);
    }
}

public class PostsRecyclerAdapter extends  RecyclerView.Adapter<PostViewHolder>{

    LayoutInflater inflater;
    List<Post> data;

    public void setData(List<Post> data)
    {
        this.data = data;
        notifyDataSetChanged();
    }

    public PostsRecyclerAdapter(LayoutInflater inflater, List<Post> data )
    {
        this.data = data;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.posts_list_row, parent, false);
        return new PostViewHolder(view, data);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = data.get(position);
        holder.bind(post, position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
