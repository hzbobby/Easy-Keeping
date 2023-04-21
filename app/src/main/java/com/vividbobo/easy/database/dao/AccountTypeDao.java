package com.vividbobo.easy.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.vividbobo.easy.database.model.AccountType;

import java.util.List;

@Dao
public interface AccountTypeDao {
    @Query("select * from accountTypes")
    LiveData<List<AccountType>> getAllAccountTypes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AccountType accountType);
}
