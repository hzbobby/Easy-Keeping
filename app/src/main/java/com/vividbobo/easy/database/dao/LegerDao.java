package com.vividbobo.easy.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.google.common.util.concurrent.ListenableFuture;
import com.vividbobo.easy.database.model.Leger;

import java.util.List;

@Dao
public interface LegerDao {
    @Query("select * from legers")
    ListenableFuture<List<Leger>> getAllLegersLF();

    @Query("select * from legers")
    LiveData<List<Leger>> getAllLegersLD();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Leger leger);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Leger> leger);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Leger editLeger);

    @Delete
    void delete(Leger item);

    @Query("select * from legers where id==:id")
    ListenableFuture<Leger> getLegerByIdLF(Integer id);

    @Query("select * from legers where id==:id")
    LiveData<Leger> getLegerByIdLD(Integer id);

    @Query("select * from legers where id==:legerId")
    Leger getRawLegerById(Integer legerId);
}
