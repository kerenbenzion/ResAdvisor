package com.example.resadvisor.model;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Display;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.resadvisor.MyApplication;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

@Entity
public class Post {
    @PrimaryKey
    @NonNull
    public String id;
    public String title;
    public String description;
    public String price;
    public String price_usd;
    public String res_name;
    public String res_address;
    public String email;
    public String pic_path;

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Long lastUpdated;
    public Post(){

    }

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
        data.put("lastUpdated", FieldValue.serverTimestamp());
        Firestore.instance().getDb().collection("posts").document(id).set(data)
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
    static final String LAST_UPDATED = "lastUpdated";
    static final String LOCAL_LAST_UPDATED = "posts_local_last_update";

    public static Post fromJson(Map<String,Object> json){
        String id = (String)json.get(ID);
        String title = (String)json.get(TITLE);
        String description = (String)json.get(DESCRIPTION);
        String price = String.valueOf(json.get(PRICE));
        String res_name = (String) json.get(RESNAME);
        String res_address = (String) json.get(RESADDRESS);
        String email = (String)json.get(EMAIL);
        String pic_path = (String)json.get(PICTURE);
        Timestamp time = (Timestamp) json.get(LAST_UPDATED);
        String priceUsd = String.format("%.2f", json.get(PRICEUSD));
        Post post = new Post(id, title, description, price, priceUsd, res_name, res_address ,email,pic_path);
        post.setLastUpdated(time.getSeconds());

        return post;
    }

    public static Long getLocalLastUpdate() {
        SharedPreferences sharedPref = MyApplication.getMyContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
        return sharedPref.getLong(LOCAL_LAST_UPDATED, 0);
    }

    public static void setLocalLastUpdate(Long time) {
        MyApplication.getMyContext().getSharedPreferences("TAG", Context.MODE_PRIVATE).edit().putLong(LOCAL_LAST_UPDATED,time).commit();
    }

    @NonNull
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getPrice_usd() {
        return price_usd;
    }

    public String getRes_name() {
        return res_name;
    }

    public String getRes_address() {
        return res_address;
    }

    public String getEmail() {
        return email;
    }

    public String getPic_path() {
        return pic_path;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setPrice_usd(String price_usd) {
        this.price_usd = price_usd;
    }

    public void setRes_name(String res_name) {
        this.res_name = res_name;
    }

    public void setRes_address(String res_address) {
        this.res_address = res_address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPic_path(String pic_path) {
        this.pic_path = pic_path;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

}
