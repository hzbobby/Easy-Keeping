package com.vividbobo.easy.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

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

import java.util.concurrent.Callable;

public class TextIdentifyViewModel extends AndroidViewModel {

    private final LiveData<Leger> lastLeger;
    private final LiveData<Account> lastAccount;
    private final BillDao billDao;
    private final LiveData<Category> expCategory;
    private final LiveData<Category> inCategory;

    public TextIdentifyViewModel(@NonNull Application application) {
        super(application);
        EasyDatabase db = EasyDatabase.getDatabase(application);
        ConfigDao configDao = db.configDao();
        lastLeger = configDao.getSelectedLeger();
        lastAccount = configDao.getSelectedAccount();
        billDao = db.billDao();

        CategoryDao categoryDao = db.categoryDao();
        expCategory = categoryDao.getCategoryByIdLd(0);
        inCategory = categoryDao.getCategoryByIdLd(24);
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
}
