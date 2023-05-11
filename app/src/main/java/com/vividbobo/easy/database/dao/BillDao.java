package com.vividbobo.easy.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.google.common.util.concurrent.ListenableFuture;
import com.vividbobo.easy.database.model.Bill;
import com.vividbobo.easy.database.model.BillInfo;

import java.sql.Date;
import java.util.List;

@Dao
public interface BillDao {


    @Query("SELECT * FROM bills " +
            "WHERE billType IN (:billTypes) " +
            "AND legerId NOT IN (:legerIds) " +
            "AND accountId NOT IN (:accountIds) " +
            "AND roleId NOT IN (:roleIds) " +
            "AND payeeId NOT IN (:payeeIds) " +
            "AND categoryId NOT IN (:categoryIds) " +
            "AND (:image IS NULL OR imagePaths IS NOT NULL) " +
            "AND (:refund IS NULL OR isRefund = :refund) " +
            "AND (:reimburse IS NULL OR isReimburse = :reimburse) " +
            "AND (:inExp IS NULL OR isIncomeExpenditureIncluded = :inExp) " +
            "AND (:budget IS NULL OR isBudgetIncluded = :budget) " +
            "AND ((:minAmount IS NULL AND :maxAmount IS NULL) OR (amount >= :minAmount AND amount <= :maxAmount)) " +
            "AND (:remark IS NULL OR remark LIKE '%' || :remark || '%') " +
            "AND ((:startDate IS NULL AND :endDate IS NULL) OR (date BETWEEN :startDate AND :endDate)) " +
            "ORDER BY date,time DESC"
    )
    LiveData<List<Bill>> queryByConditions(Integer[] billTypes, Integer[] legerIds, Integer[] accountIds,
                                           Integer[] roleIds, Integer[] payeeIds, Integer[] categoryIds,
                                           Boolean image, Boolean refund, Boolean reimburse,
                                           Boolean inExp, Boolean budget, Long minAmount, Long maxAmount,
                                           String remark, Date startDate, Date endDate
    );


    //tags?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Bill... bills);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Bill> bills);

    @Query("select * from bills where date==:date and bills.legerId=:legerId order by date,time desc")
    LiveData<List<Bill>> getBillsByDateInLeger(Date date, Integer legerId);


    @Query("select SUM(case when billType = 1 and isBudgetIncluded!=1 and isIncomeExpenditureIncluded!=1 then amount else 0 end) as incomeAmount, SUM(case when billType = 0 and isRefund!=1 and isReimburse!=1 and isBudgetIncluded!=1 and isIncomeExpenditureIncluded!=1 then amount else 0 end) as expenditureAmount from bills where billType in (0,1) and date==:date and legerId=:legerId")
    LiveData<BillInfo> getBillInfoByDateInLeger(Date date, Integer legerId);

    /**
     * @param yearMonth YY-MM
     * @return
     */
    @Query("select SUM(case when billType = 1 and isBudgetIncluded!=1 and isIncomeExpenditureIncluded!=1 then amount else 0 end) as incomeAmount, SUM(case when billType = 0 and isRefund!=1 and isReimburse!=1 and isBudgetIncluded!=1 and isIncomeExpenditureIncluded!=1 then amount else 0 end) as expenditureAmount from bills where billType in (0,1) and strftime('%Y-%m', date) = :yearMonth and legerId=:legerId")
    LiveData<BillInfo> getBillInfoByMonthInLeger(String yearMonth, Integer legerId);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Bill bill);

    @Delete
    void delete(Bill bill);

    @Query("SELECT * FROM bills WHERE date >= :start and date<=:end and legerId=:legerId")
    LiveData<List<Bill>> getBillsByDateRangeInLeger(Date start, Date end, Integer legerId);

}
