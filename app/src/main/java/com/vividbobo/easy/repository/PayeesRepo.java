package com.vividbobo.easy.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.vividbobo.easy.database.EasyDatabase;
import com.vividbobo.easy.database.dao.RoleDao;
import com.vividbobo.easy.database.model.Role;
import com.vividbobo.easy.utils.AsyncProcessor;

import java.util.List;
import java.util.concurrent.Callable;

public class PayeesRepo {

    //the Dao
    private final RoleDao roleDao;

    //the liveData
    private final LiveData<List<Role>> roleList;

    public PayeesRepo(Application application) {
        roleDao = EasyDatabase.getDatabase(application).roleDao();
        roleList = roleDao.getAllRoles();
    }

    public void insert(Role role) {
        AsyncProcessor.getInstance().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                roleDao.insert(role);
                return null;
            }
        });
    }

    public LiveData<List<Role>> getRoleList() {
        return roleList;
    }

    public void update(Role entity) {
        AsyncProcessor.getInstance().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                roleDao.update(entity);
                return null;
            }
        });
    }

    public void delete(Role item) {
        AsyncProcessor.getInstance().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                roleDao.delete(item);
                return null;
            }
        });
    }
}
