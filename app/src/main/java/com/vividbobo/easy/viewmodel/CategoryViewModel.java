package com.vividbobo.easy.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.vividbobo.easy.database.model.Category;
import com.vividbobo.easy.database.model.CategoryPresent;
import com.vividbobo.easy.repository.CategoriesRepo;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {
    private final CategoriesRepo categoriesRepo;
    private final LiveData<List<CategoryPresent>> expenditureCategories;
    private final LiveData<List<CategoryPresent>> incomeCategories;

    public CategoryViewModel(@NonNull Application application) {
        super(application);
        categoriesRepo = new CategoriesRepo(application);
        expenditureCategories = categoriesRepo.getExpenditureCategories();
        incomeCategories=categoriesRepo.getIncomeCategories();
    }


    public LiveData<List<CategoryPresent>> getExpenditureCategories() {
        return expenditureCategories;
    }

    public void insert(Category category) {
        categoriesRepo.insert(category);
    }

    public LiveData<List<CategoryPresent>> getIncomeCategories() {
        return incomeCategories;
    }
}
