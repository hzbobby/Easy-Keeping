package com.vividbobo.easy.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.vividbobo.easy.database.model.Role;
import com.vividbobo.easy.repository.PayeesRepo;

import java.util.List;

public class RoleViewModel extends AndroidViewModel {
    private final PayeesRepo payeesRepo;
    private final LiveData<List<Role>> roleList;


    public RoleViewModel(@NonNull Application application) {
        super(application);
        payeesRepo = new PayeesRepo(application);
        roleList = payeesRepo.getRoleList();
    }

    public void insert(Role role) {
        payeesRepo.insert(role);
    }

    public LiveData<List<Role>> getRoleList() {
        return roleList;
    }

    public void update(Role entity) {
        payeesRepo.update(entity);
    }

    public void delete(Role item) {
        payeesRepo.delete(item);
    }
}
