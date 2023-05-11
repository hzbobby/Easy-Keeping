package com.vividbobo.easy.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.vividbobo.easy.database.model.Payee;

import java.util.List;

@Dao
public interface PayeeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Payee payee);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Payee> payee);

    @Query("select * from payees")
    LiveData<List<Payee>> getAllStores();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Payee entity);

    @Delete
    void delete(Payee item);

    @Query("select * from payees where id=:payeeId")
    LiveData<Payee> getPayeeById(Integer payeeId);
}
