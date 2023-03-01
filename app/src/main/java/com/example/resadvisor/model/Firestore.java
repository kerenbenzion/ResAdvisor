package com.example.resadvisor.model;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;


public class Firestore {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final Firestore _instance = new Firestore(db);

    static {
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder().setPersistenceEnabled(false).build();
        db.setFirestoreSettings(settings);
    }

    public static Firestore instance() {
        return _instance;
    }

    public Firestore(FirebaseFirestore db) {
        this.db = db;
    }

    public FirebaseFirestore getDb() {
        return this.db;
    }

}
