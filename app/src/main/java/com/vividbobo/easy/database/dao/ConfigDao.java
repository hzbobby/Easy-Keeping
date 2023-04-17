package com.vividbobo.easy.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.vividbobo.easy.database.model.Account;
import com.vividbobo.easy.database.model.Config;
import com.vividbobo.easy.database.model.Leger;
import com.vividbobo.easy.database.model.Role;

@Dao
public interface ConfigDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Config config);

    @Query("select legers.* from configs,legers where legers.id==configs.selected_id and type==" + Config.CONFIG_LEGER_SELECTED)
    LiveData<Leger> getSelectedLeger();

    @Query("select roles.* from configs,roles where roles.id==configs.selected_id and type==" + Config.CONFIG_ROLE_SELECTED)
    LiveData<Role> getSelectedRole();

    @Query("select accounts.* from configs,accounts where accounts.id==configs.selected_id and type==" + Config.CONFIG_ACCOUNT_SELECTED)
    LiveData<Account> getSelectedAccount();
}
