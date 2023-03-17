package com.example.resadvisor.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ResturantDao {
    @Query("select * from Resturant")
    List<Resturant> getAll();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Resturant ... resturants);

    @Delete
    void delete(Resturant resturant);
}
