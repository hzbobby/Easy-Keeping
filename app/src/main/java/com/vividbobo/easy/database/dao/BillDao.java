package com.vividbobo.easy.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.vividbobo.easy.database.model.Bill;
import com.vividbobo.easy.database.model.DayBillPresent;

import java.sql.Date;
import java.util.List;

@Dao
public interface BillDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Bill... bills);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Bill> bills);

    @Query("select * from bills where date==:date order by date,time desc")
    LiveData<List<Bill>> getBillsByDate(Date date);

    @Query("select SUM(amount) from bills where date==:date & billType==:billType")
    LiveData<Long> getTotalAmountByDate(Date date, int billType);
}
