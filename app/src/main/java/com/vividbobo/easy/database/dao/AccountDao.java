package com.vividbobo.easy.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.google.common.util.concurrent.ListenableFuture;
import com.vividbobo.easy.database.model.Account;

import java.util.List;

@Dao
public interface AccountDao {

    //    @Query("select accounts.*,COUNT(bills.id) as billCount from accounts,bills where bills.accountId==accounts.id")
    @Query("select * from accounts")
    LiveData<List<Account>> getAllAccounts();

    @Query("update accounts set balance = balance+:amount where id=:legerId")
    void updateLegerBalance(Integer legerId, Long amount);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Account account);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Account> account);

    @Query("select * from accounts where id==:id")
    ListenableFuture<Account> getAccountByID(int id);

    @Query("select * from accounts where id==:id")
    LiveData<Account> getAccountByIdLD(int id);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Account account);

    @Query("select * from accounts where id==:accountId")
    Account getRawAccountById(Integer accountId);

    @Delete
    void delete(Account item);
}
