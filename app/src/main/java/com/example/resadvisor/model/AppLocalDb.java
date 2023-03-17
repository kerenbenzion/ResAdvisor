package com.example.resadvisor.model;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.resadvisor.MyApplication;

@Database(entities = {Post.class}, version = 1)
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract PostDao postDao();
}

public class AppLocalDb {
    static public AppLocalDbRepository getAppDb() {
        return Room.databaseBuilder(MyApplication.getMyContext(),
                        AppLocalDbRepository.class,
                        "dbFileName.db")
                .fallbackToDestructiveMigration()
                .build();
    }

    private AppLocalDb(){}
}
