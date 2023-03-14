package com.example.resadvisor.model;

import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.resadvisor.MainActivity;
import com.example.resadvisor.MapsActivity;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Resturant {
    public String name;
    public GeoPoint geo;
    public Resturant( String name, GeoPoint geo) {
        this.name = name;
        this.geo = geo;
    }
    static final String Address = "point";
    static final String Name = "Name";
    public static Resturant fromJson(Map<String,Object> json){
        String name = (String)json.get(Name);
        GeoPoint geo = (GeoPoint) json.get(Address);

        Resturant res = new Resturant(name,geo);
        return res;
    }
    public static void addResturant(String res_name, String res_address,String id, GeoPoint geo) {
        Map<String, Object> data = new HashMap<>();

        data.put("Name", res_name);
        data.put("address", res_address);
        data.put("point", res_address);
        data.put("point", geo);
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

}
