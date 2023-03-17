package com.example.resadvisor.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.resadvisor.MainActivity;
import com.example.resadvisor.MapsActivity;
import com.example.resadvisor.MyApplication;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Entity
public class Resturant {
    public String name;
    public String address;
    public Double lat;
    public Double lng;
    public Long lastUpdated;

    @PrimaryKey
    @NonNull
    public String id;
    public Resturant( String name, String address,String id,Double lat,Double lng) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.id = id;
        this.address = address;

    }
    public Resturant(){

    }
    static final String Address = "point";
    static final String Name = "Name";
//    public static Resturant fromJson(Map<String,Object> json){
//        String name = (String)json.get(Name);
//        GeoPoint geo = (GeoPoint) json.get(Address);
//
//        Resturant res = new Resturant(name,geo);
//        return res;
//    }
    static final String ID = "id";
    static final String NAME = "Name";
    static final String ADDRESS = "address";
    static final String LAT = "lat";
    static final String LNG = "lng";
    static final String LAST_UPDATED = "lastUpdated";
    static final String LOCAL_LAST_UPDATED = "res_local_last_update";

    public static void addResturant(String res_name, String res_address,String id, Double lng,Double lat) {
        Map<String, Object> data = new HashMap<>();
        data.put(ID,id);
        data.put(NAME, res_name);
        data.put(ADDRESS, res_address);
        data.put(LAT, lat);
        data.put(LNG, lng);
        data.put(LAST_UPDATED, FieldValue.serverTimestamp());
        Firestore.instance().getDb().collection("resturants").document(id).set(data)
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
    public static Resturant fromJson(Map<String,Object> json){
        String id = (String)json.get(ID);
        String name = (String)json.get(NAME);
        String address = (String)json.get(ADDRESS);
        Double lat = (Double) json.get(LAT);
        Double lng = (Double) json.get(LNG);
        Timestamp time = (Timestamp) json.get(LAST_UPDATED);
        Resturant resturant = new Resturant(name, address, id,lat,lng );
        resturant.setLastUpdated(time.getSeconds());
        return resturant;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }


    public static Long getLocalLastUpdate() {
        SharedPreferences sharedPref = MyApplication.getMyContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
        return sharedPref.getLong(LOCAL_LAST_UPDATED, 0);
    }

    public static void setLocalLastUpdate(Long time) {
        SharedPreferences sharedPref = MyApplication.getMyContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(LOCAL_LAST_UPDATED,time);
        editor.commit();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public Long getLastUpdated() {
        return lastUpdated;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}
