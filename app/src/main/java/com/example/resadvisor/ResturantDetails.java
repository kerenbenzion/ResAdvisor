package com.example.resadvisor;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ResturantDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resturant_details);
        Bundle extras = getIntent().getExtras();
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        if(extras!=null){
            String name = extras.getString("name");
            TextView name_view = findViewById(R.id.name);
            name_view.setText(name);
            Double lat = (Double) extras.get("lat");
            Log.d("TAG",lat.toString());
            Double lng = (Double) extras.get("lng");
            Log.d("TAG",lng.toString());
            try {
                addresses = geocoder.getFromLocation(lat, lng, 1);
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
                TextView add = findViewById(R.id.address);
                TextView city1 = findViewById(R.id.city);
                TextView country1 = findViewById(R.id.country);
                TextView postalCode1 = findViewById(R.id.zipcode);
                add.setText(address);
                city1.setText(city);
                country1.setText(country);
                postalCode1.setText(postalCode);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}