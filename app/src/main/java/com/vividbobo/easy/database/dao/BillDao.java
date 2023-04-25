package com.vividbobo.easy.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.vividbobo.easy.database.model.Bill;
import com.vividbobo.easy.database.model.BillInfo;

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

    @Query("select SUM(case when billType = 1 and isBudgetIncluded!=1 and isIncomeExpenditureIncluded!=1 then amount else 0 end) as incomeAmount, SUM(case when billType = 0 and isRefund!=1 and isReimburse!=1 and isBudgetIncluded!=1 and isIncomeExpenditureIncluded!=1 then amount else 0 end) as expenditureAmount from bills where billType in (0,1) and date==:date")
    LiveData<BillInfo> getBillInfoByDate(Date date);

    /**
     * @param yearMonth YY-MM
     * @return
     */
    @Query("select SUM(case when billType = 1 and isBudgetIncluded!=1 and isIncomeExpenditureIncluded!=1 then amount else 0 end) as incomeAmount, SUM(case when billType = 0 and isRefund!=1 and isReimburse!=1 and isBudgetIncluded!=1 and isIncomeExpenditureIncluded!=1 then amount else 0 end) as expenditureAmount from bills where billType in (0,1) and strftime('%Y-%m', date) = :yearMonth")
    LiveData<BillInfo> getBillInfoByMonth(String yearMonth);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Bill bill);

    @Delete
    void delete(Bill bill);
}
