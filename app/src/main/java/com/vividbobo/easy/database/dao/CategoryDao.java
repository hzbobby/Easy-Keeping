package com.vividbobo.easy.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.vividbobo.easy.database.model.Category;

import java.util.List;

@Dao
public interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Category... categories);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Category> categories);

    @Query("select * from categories where type == :categoryType order by parentId asc, orderNum asc")
    LiveData<List<Category>> getCategories(Integer categoryType);

    @Query("select max(orderNum) from categories where parentId==:parentId")
    int getMaxOrderNumByParentId(int parentId);

    @Query("select * from categories where id==:id")
    LiveData<Category> getCategoryByIdLd(Integer id);

    @Query("select * from categories where id in (1,24) and type==:billType")
    Category getRawDefaultCategoryByType(Integer billType);

    @Query("select * from categories where id==:categoryId")
    Category getRawCategoryById(Integer categoryId);
}
