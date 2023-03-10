package com.example.resadvisor.model;
import android.util.Log;
import android.view.Display;

import java.util.HashMap;
import java.util.Map;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.Timestamp;

public class Post {
    public String id;
    public String title;
    public String description;
    public String price;
    public String price_usd;
    public String res_name;
    public String res_address;
    public String email;
    public String pic_path;
    public Post( String id, String title, String description, String price, String priceUsd,
                String res_name, String res_address,String email,String pic_path) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.price_usd = priceUsd;
        this.res_name = res_name;
        this.res_address = res_address;
        this.email=email;
        this.pic_path=pic_path;
    }

    public static void addPost(String id, String title, String description, Integer price,
                               String res_name, String res_address, Double price_usd,String email,String picpath) {
        Map<String, Object> data = new HashMap<>();
        data.put("id", id);
        data.put("email", email);
        data.put("title", title);
        data.put("description", description);
        data.put("price", price);
        data.put("res_name", res_name);
        data.put("res_address",res_address);
        data.put("price_usd",price_usd);
        data.put("pic_path",picpath);
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

    static final String ID = "id";
    static final String TITLE = "title";
    static final String DESCRIPTION = "description";
    static final String PRICE = "price";
    static final String PRICEUSD = "price_usd";
    static final String RESNAME = "res_name";
    static final String RESADDRESS = "res_address";
    static final String EMAIL = "email";
    static final String PICTURE = "pic_path";
    public static Post fromJson(Map<String,Object> json){
        String id = (String)json.get(ID);
        String title = (String)json.get(TITLE);
        String description = (String)json.get(DESCRIPTION);
        String price = String.valueOf(json.get(PRICE));
        String priceUsd = String.format("%.2f", json.get(PRICEUSD));
        String res_name = (String) json.get(RESNAME);
        String res_address = (String) json.get(RESADDRESS);
        String email = (String)json.get(EMAIL);
        String pic_path = (String)json.get(PICTURE);
        Post post = new Post(id, title, description, price, priceUsd, res_name, res_address ,email,pic_path);

        return post;
    }


}
