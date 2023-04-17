package com.vividbobo.easy.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.vividbobo.easy.database.model.BaseEntity;
import com.vividbobo.easy.database.model.Role;

import java.util.List;

@Dao
public interface RoleDao {
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Role entity);

    @Query("select * from roles")
    LiveData<List<Role>> getAllRoles();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Role... roles);

    @Query("select * from roles where id=:id")
    LiveData<Role> getRoleById(int id);

    @Delete
    void delete(Role item);
}
