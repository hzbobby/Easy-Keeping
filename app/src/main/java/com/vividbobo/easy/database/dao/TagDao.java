package com.vividbobo.easy.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.vividbobo.easy.database.model.Tag;

import java.util.List;

@Dao
public interface TagDao {
    @Query("select * from tags")
    LiveData<List<Tag>> getALlTags();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Tag tag);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Tag tag);

    @Delete
    void delete(Tag tag);
}
