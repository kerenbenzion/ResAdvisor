package com.example.resadvisor.model;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.Timestamp;

public class Post {
    public String title;
    public String description;
    public String price;
    public String res_name;
    public String res_address;


    public Post( String title, String description, String price,
                String res_name, String res_address) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.res_name = res_name;
        this.res_address = res_address;
    }

    public static void addPost(String id, String title, String description, Integer price,
                               String res_name, String res_address, Double price_usd) {
        Map<String, Object> data = new HashMap<>();
        data.put("title", title);
        data.put("description", description);
        data.put("price", price);
        data.put("res_name", res_name);
        data.put("res_address",res_address);
        data.put("price_usd",price_usd);

        Firestore.instance().getDb().collection("published_posts").document(id).set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "Document has been added with custom ID");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error adding document", e);
                    }
                });
    }

    static final String TITLE = "title";
    static final String DESCRIPTION = "description";
    static final String PRICE = "price";
    static final String RESNAME = "res_name";
    static final String RESADDRESS = "res_address";

    public static Post fromJson(Map<String,Object> json){
        String title = (String)json.get(TITLE);
        String description = (String)json.get(DESCRIPTION);
        String price = String.valueOf(json.get(PRICE));
        String res_name = (String) json.get(RESNAME);
        String res_address = (String) json.get(RESADDRESS);

        Post post = new Post(title, description, price, res_name, res_address);

        return post;
    }


}
