package com.vividbobo.easy.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.vividbobo.easy.database.model.Account;
import com.vividbobo.easy.database.model.Leger;
import com.vividbobo.easy.database.model.Role;
import com.vividbobo.easy.repository.ConfigsRepo;

public class ConfigViewModel extends AndroidViewModel {

    private final ConfigsRepo configsRepo;

    public ConfigViewModel(@NonNull Application application) {
        super(application);
        configsRepo = new ConfigsRepo(application);
    }

    public void updateSelected(int type, int selectedId) {
        configsRepo.updateSelectedId(type, selectedId);
    }

    public LiveData<Leger> getSelectedLeger() {
        return configsRepo.getSelectedLegerLD();
    }

    public LiveData<Role> getSelectedRole() {
        return configsRepo.getSelectedRoleLD();
    }

    public LiveData<Account> getSelectedAccount() {
        return configsRepo.getSelectedAccountLD();
    }


}
