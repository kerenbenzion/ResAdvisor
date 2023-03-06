package com.example.resadvisor;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;

import android.util.Log;
import android.view.Menu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.resadvisor.model.Model;
import com.google.firebase.firestore.DocumentReference;
import com.example.resadvisor.model.Firestore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
//import java.net.HttpsURLConnection;
import java.net.URL;
import java.net.URLConnection;

import com.example.resadvisor.model.Post;

import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;

public class AddPostFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity parentActivity = getActivity();
        parentActivity.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.removeItem(R.id.addPostFragment);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        },this, Lifecycle.State.RESUMED);

    }



    private void getUSD(String collection_id,String title,String desc,Integer price,String res_name,
                        String res_address) {
        Thread usdThread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    String queryString = "https://exchange-rates.abstractapi.com/v1/live/?api_key=5e145483e8a94350baefc81285bec5a2&base=ILS&target=USD";
                    HttpsURLConnection connection = null;
                    try {
                        connection = (HttpsURLConnection) new URL(queryString).openConnection();
                        connection.setRequestMethod("GET");

                        InputStream inputStream = connection.getInputStream();

                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();

                        JSONObject jsonObject = new JSONObject(response.toString());
                        String currency_response = jsonObject.getJSONObject("exchange_rates").get("USD").toString();

                        Double price_usd = Double.parseDouble(currency_response)*price;
                        String email = Model.instance().getcurrent().getEmail();
                        Post.addPost(collection_id, title, desc, price, res_name,res_address, price_usd,email);

                    } catch (IOException e) {
                        System.out.println(e);

                    } finally {
                        if (connection != null) {
                            connection.disconnect();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        usdThread.start();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_post, container, false);

        EditText titleEt = view.findViewById(R.id.addpost_title_et);
        EditText descEt = view.findViewById(R.id.addpost_desc_et);
        EditText priceEt = view.findViewById(R.id.addpost_price_et);
        EditText res_nameEt = view.findViewById(R.id.addpost_res_name_et);
        EditText res_addressEt = view.findViewById(R.id.addpost_res_address_et);

        Button saveBtn = view.findViewById(R.id.addpost_save_btn);

        //When clicked - move to list page
        Button cancelBtn = view.findViewById(R.id.addpost_cancell_btn);


        saveBtn.setOnClickListener(view1 -> {
            String title = titleEt.getText().toString();
            String desc = descEt.getText().toString();
            Integer price = Integer.parseInt(priceEt.getText().toString());
            String res_name = res_nameEt.getText().toString();
            String res_address = res_addressEt.getText().toString();
            DocumentReference ref = Firestore.instance().getDb().collection("published_posts").document();
            String collection_id = ref.getId();
            getUSD(collection_id, title, desc, price,res_name,res_address);
            Toast.makeText(getContext(),
                            "Upload post successfully",
                            Toast.LENGTH_LONG)
                    .show();

        });
        return view;
    }

}