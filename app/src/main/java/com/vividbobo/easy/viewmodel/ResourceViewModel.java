package com.vividbobo.easy.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.common.util.concurrent.ListenableFuture;
import com.vividbobo.easy.database.model.Category;
import com.vividbobo.easy.database.model.ChildRvItem;
import com.vividbobo.easy.database.model.Resource;
import com.vividbobo.easy.repository.ResourceRepo;

import java.util.List;

public class ResourceViewModel extends AndroidViewModel {
    private final ListenableFuture<List<Resource>> sysCoverLF;
    private ResourceRepo resourceRepo;

    private final ListenableFuture<List<Resource>> accountResLF;


    public ResourceViewModel(Application application) {
        super(application);
        this.resourceRepo = new ResourceRepo(application);
        accountResLF = resourceRepo.getAccountResLF();
        sysCoverLF = resourceRepo.getSysCoverLF();
    }

    public ListenableFuture<List<Resource>> getSysCoverLF() {
        return sysCoverLF;
    }

    public ListenableFuture<List<Resource>> getAccountResLF() {
        return accountResLF;
    }

    public LiveData<List<ChildRvItem>> getIconChildRvItems() {
        return resourceRepo.getIconChildRvItems();
    }

}
