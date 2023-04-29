package com.vividbobo.easy.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.vividbobo.easy.database.model.Account;
import com.vividbobo.easy.database.model.Category;
import com.vividbobo.easy.database.model.Config;
import com.vividbobo.easy.database.model.Leger;
import com.vividbobo.easy.database.model.Role;

@Dao
public interface ConfigDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Config config);

    @Query("select legers.* from configs,legers where legers.id==configs.selected_id and type==1" + Config.TYPE_LEGER)
    LiveData<Leger> getSelectedLeger();

    @Query("select roles.* from configs,roles where roles.id==configs.selected_id and type==" + Config.TYPE_ROLE)
    LiveData<Role> getSelectedRole();

    @Query("select accounts.* from configs,accounts where accounts.id==configs.selected_id and type==" + Config.TYPE_ACCOUNT)
    LiveData<Account> getSelectedAccount();

    @Query("update configs set selected_id=:selectedId where type==:type")
    void updateSelectedId(int type, int selectedId);

    @Query("select selected_id from configs where type==:type")
    LiveData<Integer> getSelectedIdByType(int type);

    @Query("select legers.* from legers,configs where configs.type==101 and legers.id==configs.selected_id")
    Leger getRawAutoBillingLeger();

    @Query("select selected_id from configs where type==:type")
    Integer getRawSelectedIdByType(int type);

    @Query("select accounts.* from accounts,configs where configs.type==:type and configs.selected_id==accounts.id")
    LiveData<Account> getAccountByType(int type);

    @Query("select accounts.* from accounts,configs where configs.type==:type and configs.selected_id==accounts.id")
    Account getRawAccountByType(int type);

    @Query("select categories.* from categories, configs where configs.type==:type and configs.selected_id==categories.id")
    Category getRawCategoryByType(int type);

    @Query("select legers.* from configs,legers where legers.id==configs.selected_id and type==:type")
    LiveData<Leger> getLegerByType(int type);

    @Query("select legers.* from configs,legers where legers.id==configs.selected_id and type==:type")
    Leger getRawLegerByType(int type);

    @Query("select roles.* from configs,roles where roles.id==configs.selected_id and type==:type")
    LiveData<Role> getRoleByType(int type);

    @Query("select roles.* from configs,roles where roles.id==configs.selected_id and type==:type")
    Role getRawRoleByType(int type);
}
