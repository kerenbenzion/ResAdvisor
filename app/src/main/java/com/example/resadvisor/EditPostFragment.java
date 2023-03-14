package com.example.resadvisor;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.resadvisor.model.Firestore;
import com.example.resadvisor.model.Model;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import com.example.resadvisor.model.Post;

import org.json.JSONObject;


import javax.net.ssl.HttpsURLConnection;

public class EditPostFragment extends Fragment {

    int SELECT_PICTURE = 200;
    ImageView IVPreviewImage;
    Post post;
    String post_id;
//    public void setPost(Post post) {
//        this.post = post;
//    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        post_id = getArguments().getString("post_id");

    }

    private void updatePost(String post_id,String title,String desc,Integer price,String res_name,
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
                        String picpath = post_id+"_"+title+"_"+email.split("@")[0];

                        Post.addPost(post_id, title, desc, price, res_name,res_address, price_usd,email,picpath);
                        Bitmap bmap = ((BitmapDrawable) IVPreviewImage.getDrawable()).getBitmap();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();
                        Model.instance().uploadImage(picpath,data,url->Log.d("TAG","Start to upload"));


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

        View view = inflater.inflate(R.layout.fragment_edit_post, container, false);

        EditText et_title = view.findViewById(R.id.editpost_title_et);
        EditText et_desc = view.findViewById(R.id.editpost_desc_et);
        EditText et_price = view.findViewById(R.id.editpost_price_et);
        EditText et_res_name = view.findViewById(R.id.editpost_res_name_et);
        EditText et_res_address = view.findViewById(R.id.editpost_res_address_et);
        IVPreviewImage = view.findViewById(R.id.editpost_uploaded_picture);
        Model.instance().getPost(post_id, et_title, et_desc, et_price, et_res_name, et_res_address, IVPreviewImage);

        Button saveBtn = view.findViewById(R.id.editpost_save_btn);
        Button add_image = view.findViewById(R.id.editpost_add_image);

        add_image.setOnClickListener(view1->{
            image_chooser();
        });

        saveBtn.setOnClickListener(view1 -> {
            String title = et_title.getText().toString();
            String desc = et_desc.getText().toString();
            Integer price = Integer.parseInt(et_price.getText().toString());
            String res_name = et_res_name.getText().toString();
            String res_address = et_res_address.getText().toString();

            updatePost(post_id, title, desc, price,res_name,res_address);
            Toast.makeText(getContext(),
                            "Update post successfully",
                            Toast.LENGTH_LONG)
                    .show();

        });
        return view;

    }

    private void image_chooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    IVPreviewImage.setImageURI(selectedImageUri);
                }
            }
        }
    }
}