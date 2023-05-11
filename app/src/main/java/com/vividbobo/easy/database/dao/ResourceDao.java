package com.vividbobo.easy.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.google.common.util.concurrent.ListenableFuture;
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

    /**
     * 根据 resourceType 获取一次资源
     *
     * @param resourceType
     * @return
     */
    @Query("select * from resources where resType==:resourceType")
    ListenableFuture<List<Resource>> getResourcesByResType(Resource.ResourceType resourceType);

    @Query("select * from resources where resType in (:types)")
    LiveData<List<Resource>> getResoucesByResTypes(Resource.ResourceType[] types);

    @Delete
    void delete(Resource resource);
}
