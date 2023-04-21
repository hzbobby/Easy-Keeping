package com.vividbobo.easy.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.vividbobo.easy.database.model.Account;

import java.util.List;

@Dao
public interface AccountDao {

    @Query("select * from accounts order by accountTypeTitle")
    LiveData<List<Account>> getAllAccounts();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Account account);
}
