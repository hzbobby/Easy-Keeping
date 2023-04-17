package com.vividbobo.easy.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.vividbobo.easy.database.model.BaseEntity;
import com.vividbobo.easy.database.model.Role;
import com.vividbobo.easy.repository.RolesRepo;

import java.util.List;

public class RoleViewModel extends AndroidViewModel {
    private final RolesRepo rolesRepo;
    private final LiveData<List<Role>> roleList;


    public RoleViewModel(@NonNull Application application) {
        super(application);
        rolesRepo = new RolesRepo(application);
        roleList = rolesRepo.getRoleList();
    }

    public void insert(Role role) {
        rolesRepo.insert(role);
    }

    public LiveData<List<Role>> getRoleList() {
        return roleList;
    }

    public void update(Role entity) {
        rolesRepo.update(entity);
    }

    public void delete(Role item) {
        rolesRepo.delete(item);
    }
}
