package com.vividbobo.easy.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.vividbobo.easy.database.model.Store;

import java.util.List;

@Dao
public interface StoreDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Store store);

    @Query("select * from stores")
    LiveData<List<Store>> getAllStores();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Store entity);

    @Delete
    void delete(Store item);
}
