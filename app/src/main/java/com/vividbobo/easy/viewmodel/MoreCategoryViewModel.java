package com.vividbobo.easy.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.vividbobo.easy.database.EasyDatabase;
import com.vividbobo.easy.database.model.Bill;
import com.vividbobo.easy.database.model.Category;

import java.util.List;

public class MoreCategoryViewModel extends AndroidViewModel {
    private final MutableLiveData<Integer> billType;
    private final LiveData<List<Category>> categories;

    public MoreCategoryViewModel(@NonNull Application application) {
        super(application);
        EasyDatabase db = EasyDatabase.getDatabase(application);
        billType = new MutableLiveData<>(Bill.EXPENDITURE);
        categories = Transformations.switchMap(billType, type -> db.categoryDao().getCategoriesByType(type));
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }

    public void setBillType(Integer billType) {
        this.billType.postValue(billType);
    }
}
