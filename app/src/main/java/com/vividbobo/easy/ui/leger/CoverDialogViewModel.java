package com.vividbobo.easy.ui.leger;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.vividbobo.easy.database.EasyDatabase;
import com.vividbobo.easy.database.dao.ResourceDao;
import com.vividbobo.easy.database.model.Resource;
import com.vividbobo.easy.utils.AsyncProcessor;

import java.util.List;
import java.util.concurrent.Callable;

public class CoverDialogViewModel extends AndroidViewModel {

    private final LiveData<List<Resource>> covers;
    private final ResourceDao resourceDao;

    public CoverDialogViewModel(@NonNull Application application) {
        super(application);

        EasyDatabase db = EasyDatabase.getDatabase(application);
        resourceDao = db.resourceDao();

        covers = resourceDao.getResoucesByResTypes(new Resource.ResourceType[]{Resource.ResourceType.SYSTEM_COVER
                , Resource.ResourceType.USER_COVER});
    }

    public LiveData<List<Resource>> getCovers() {
        return covers;
    }

    public void insert(Resource resource) {
        AsyncProcessor.getInstance().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                resourceDao.insert(resource);
                return null;
            }
        });
    }

    public void delete(Resource resource) {
        AsyncProcessor.getInstance().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                resourceDao.delete(resource );
                return null;
            }
        });
    }
}
