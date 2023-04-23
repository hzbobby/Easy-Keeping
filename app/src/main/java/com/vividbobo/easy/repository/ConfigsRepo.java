package com.vividbobo.easy.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.vividbobo.easy.database.EasyDatabase;
import com.vividbobo.easy.database.dao.ConfigDao;
import com.vividbobo.easy.database.model.Account;
import com.vividbobo.easy.database.model.Leger;
import com.vividbobo.easy.database.model.Role;
import com.vividbobo.easy.utils.AsyncProcessor;

import java.util.concurrent.Callable;

public class ConfigsRepo {


    private final ConfigDao configDao;
    private final LiveData<Leger> selectedLegerLD;
    private final LiveData<Account> selectedAccountLD;
    private final LiveData<Role> selectedRoleLD;

    public ConfigsRepo(Application application) {
        EasyDatabase db = EasyDatabase.getDatabase(application);
        configDao = db.configDao();
        selectedLegerLD = configDao.getSelectedLeger();
        selectedAccountLD = configDao.getSelectedAccount();
        selectedRoleLD = configDao.getSelectedRole();
    }

    public LiveData<Account> getSelectedAccountLD() {
        return selectedAccountLD;
    }

    public LiveData<Role> getSelectedRoleLD() {
        return selectedRoleLD;
    }

    public ConfigDao getConfigDao() {
        return configDao;
    }

    public LiveData<Leger> getSelectedLegerLD() {
        return selectedLegerLD;
    }

    public void updateSelectedId(int type, int selectedId) {
        AsyncProcessor.getInstance().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                configDao.updateSelectedId(type, selectedId);
                return null;
            }
        });
    }

}

