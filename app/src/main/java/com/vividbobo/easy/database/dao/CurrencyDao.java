package com.vividbobo.easy.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.vividbobo.easy.database.model.Currency;

import java.util.List;

@Dao
public interface CurrencyDao {
    @Query("select * from currencies")
    LiveData<List<Currency>> getAllCurrencies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Currency currency);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Currency currency);

    @Query("select * from currencies where selected==1")
    LiveData<List<Currency>> getSelectedCurrencies();
}
