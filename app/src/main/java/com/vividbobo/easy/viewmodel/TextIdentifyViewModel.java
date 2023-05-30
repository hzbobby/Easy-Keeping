package com.vividbobo.easy.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.vividbobo.easy.EasyApplication;
import com.vividbobo.easy.database.EasyDatabase;
import com.vividbobo.easy.database.dao.BillDao;
import com.vividbobo.easy.database.dao.CategoryDao;
import com.vividbobo.easy.database.dao.ConfigDao;
import com.vividbobo.easy.database.model.Account;
import com.vividbobo.easy.database.model.Bill;
import com.vividbobo.easy.database.model.Category;
import com.vividbobo.easy.database.model.Config;
import com.vividbobo.easy.database.model.Leger;
import com.vividbobo.easy.utils.AsyncProcessor;

import java.util.Objects;
import java.util.concurrent.Callable;

public class TextIdentifyViewModel extends AndroidViewModel {

    private final LiveData<Leger> lastLeger;
    private final LiveData<Account> lastAccount;
    private final BillDao billDao;
    private final LiveData<Category> expCategory;
    private final LiveData<Category> inCategory;
    private final MutableLiveData<String> filterCategoryName;
    private final CategoryDao categoryDao;

    public TextIdentifyViewModel(@NonNull Application application) {
        super(application);
        EasyDatabase db = EasyDatabase.getDatabase(application);
        ConfigDao configDao = db.configDao();
        lastLeger = configDao.getSelectedLeger();
        lastAccount = configDao.getSelectedAccount();
        billDao = db.billDao();

        filterCategoryName = new MutableLiveData<>("其他");
        categoryDao = db.categoryDao();
        expCategory = Transformations.switchMap(filterCategoryName, title -> categoryDao.getCategoryByNameAndType(title, Category.TYPE_EXPENDITURE));
        inCategory = Transformations.switchMap(filterCategoryName, title -> categoryDao.getCategoryByNameAndType(title, Category.TYPE_INCOME));
    }

    public void insert(Bill bill) {
        AsyncProcessor.getInstance().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                billDao.insert(bill);
                return null;
            }
        });
    }

    public LiveData<Category> getExpCategory() {
        return expCategory;
    }

    public LiveData<Category> getInCategory() {
        return inCategory;
    }

    public LiveData<Account> getLastAccount() {
        return lastAccount;
    }

    public LiveData<Leger> getLastLeger() {
        return lastLeger;
    }

    public void setFilterCategoryName(String categoryName) {
        if (Objects.isNull(categoryName)) {
            categoryName = "其他";
        }
        filterCategoryName.postValue(categoryName);
    }
}
