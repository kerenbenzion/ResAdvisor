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
                imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("TAG", "success");
                        listener.onComplete(uri.toString());
                    }
                });
            }
        });

    }
}