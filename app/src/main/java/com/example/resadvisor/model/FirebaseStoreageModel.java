package com.example.resadvisor.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class FirebaseStoreageModel {
    FirebaseStorage storage;
    FirebaseStoreageModel() {
        storage = FirebaseStorage.getInstance();
    }

    void getImage(String path, ImageView img) {
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imgRef = mStorageRef.child("images/" + path + ".jpg");
        final long ONE_MEGABYTE = 1024 * 1024;
        imgRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                Bitmap emptyBitmap = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
                if (!bmp.sameAs(emptyBitmap)) {
                    img.setImageBitmap(bmp);
                }
            }
        });


        //ContextWrapper cw = new ContextWrapper(MyApplication.getMyContext());
        //File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        //File file = new File(directory, "mario" + ".png");
        //img.setImageDrawable(Drawable.createFromPath(file.toString()));



    }
    void uploadImage(String name, byte[] data, Model.Listener<String> listener) {
        StorageReference storageRef = storage.getReference();
        StorageReference imagesRef = storageRef.child("images/" + name + ".jpg");

        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("TAG", "Did not succeed to upload!");
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("TAG", "Succeed to upload!");
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });

    }
}