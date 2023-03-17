package com.example.resadvisor.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;
@Dao
public interface PostDao {
    @Query("select * from Post")
    List<Post> getAll();

    @Query("select * from Post where email=:emails")
    List<Post> getUsersPosts(String emails);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Post ... posts);

    @Delete
    void delete(Post post);
}
