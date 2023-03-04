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



    private void getUSD(String collection_id,String title,String desc,String price,String res_name,
                        String res_address) {
        String msg = "";
        Thread gfgThread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    String baseUrl = "https://'exchange-rates'.abstractapi.com/v1/live/";
                    String base = "ILS";
                    String target = "USD";
                    String apiKey = "5e145483e8a94350baefc81285bec5a2";
                    URL url = null;

                    String queryString = "https://exchange-rates.abstractapi.com/v1/live/?api_key=5e145483e8a94350baefc81285bec5a2&base=ILS&target=USD";
                    HttpsURLConnection connection = null;
                    try {
                        connection = (HttpsURLConnection) new URL(queryString).openConnection();
                        connection.setRequestMethod("GET");
                        System.out.println("$$$$$$$");

                        String msg = connection.getResponseMessage();
                        System.out.println(msg);
                        InputStream inputStream = connection.getInputStream();

                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();

                        JSONObject jsonObject = new JSONObject(response.toString());
                        String currency_response = jsonObject.getJSONObject("exchange_rates").getJSONObject("USD").toString();

//                        Double price_usd = Double.parseDouble(currency_response)*Integer.getInteger(price);
//                        System.out.println(price_usd);

                        Post.addPost(collection_id, title, desc, price,res_name,res_address, currency_response);
                        Toast.makeText(getContext(),
                                        String.valueOf(price),
                                        Toast.LENGTH_LONG)
                                .show();

                    } catch (IOException e) {
                        System.out.println(e);

                        // Handle the error
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

        gfgThread.start();
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
            String price = priceEt.getText().toString();
            String res_name = res_nameEt.getText().toString();
            String res_address = res_addressEt.getText().toString();
            DocumentReference ref = Firestore.instance().getDb().collection("published_posts").document();
            String collection_id = ref.getId();
            getUSD(collection_id, title, desc, price,res_name,res_address);


        });
        return view;
    }

}