package com.vividbobo.easy.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.google.common.util.concurrent.ListenableFuture;
import com.vividbobo.easy.database.model.Icon;
import com.vividbobo.easy.database.model.Resource;

import java.util.List;

@Dao
public interface ResourceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Resource... resources);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Resource> resources);

    @Query("DELETE FROM resources")
    void deleteAll();

    @Query("SELECT * FROM resources")
    LiveData<List<Resource>> getResources();

    @Query("SELECT DISTINCT `group` FROM resources")
    LiveData<List<String>> getDistinctGroups();

    @Query("SELECT resName FROM resources where `group`==:groupName")
    LiveData<List<Icon>> getIconsGroupBy(String groupName);

    /**
     * 根据 resourceType 获取一次资源
     *
     * @param resourceType
     * @return
     */
    @Query("select * from resources where resType==:resourceType")
    ListenableFuture<List<Resource>> getResourcesByResType(Resource.ResourceType resourceType);
}
