package com.vividbobo.easy.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.vividbobo.easy.database.model.Currency;

import java.util.List;

@Dao
public interface CurrencyDao {
    @Query("select * from currencies")
    ListenableFuture<List<Currency>> getAllCurrencies();

    @Query("select * from currencies where enable == 1")
    LiveData<List<Currency>> getEnableCurrencies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Currency currency);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Currency> currencies);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Currency currency);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int update(List<Currency> currency);

    @Query("select * from currencies where code=:code")
    LiveData<Currency> getCurrency(String code);

    @Query("update currencies set rate=:rate where code==:code and autoUpdate = 1")
    void setAutoUpdateRate(String code, float rate);

    @Query("update currencies set rate=:rate where code==:code ")
    void setRate(String code, float rate);

    @Query("update currencies set enable=:isCheck where code==:code")
    void setEnable(String code, boolean isCheck);
}
