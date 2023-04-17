package com.vividbobo.easy.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.vividbobo.easy.database.model.Category;
import com.vividbobo.easy.database.model.ChildRvItem;
import com.vividbobo.easy.repository.ResourceRepo;

import java.util.List;

public class ResourceViewModel extends AndroidViewModel {
    private ResourceRepo resourceRepo;

    public ResourceViewModel(Application application) {
        super(application);
        this.resourceRepo = new ResourceRepo(application);
    }

    public LiveData<List<ChildRvItem>> getIconChildRvItems() {
        return resourceRepo.getIconChildRvItems();
    }

}
