package com.vividbobo.easy.ui.dataImportAndExport;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.vividbobo.easy.database.EasyDatabase;
import com.vividbobo.easy.database.dao.AccountDao;
import com.vividbobo.easy.database.dao.BillDao;
import com.vividbobo.easy.database.dao.CategoryDao;
import com.vividbobo.easy.database.dao.LegerDao;
import com.vividbobo.easy.database.dao.PayeeDao;
import com.vividbobo.easy.database.dao.RoleDao;
import com.vividbobo.easy.database.model.Account;
import com.vividbobo.easy.database.model.Bill;
import com.vividbobo.easy.database.model.Category;
import com.vividbobo.easy.database.model.Leger;
import com.vividbobo.easy.database.model.Payee;
import com.vividbobo.easy.database.model.Role;
import com.vividbobo.easy.utils.AsyncProcessor;

import java.util.List;
import java.util.concurrent.Callable;

public class ImportActivityViewModel extends AndroidViewModel {

    private final BillDao billDao;
    private final LegerDao legerDao;
    private final AccountDao accountDao;
    private final CategoryDao categoryDao;
    private final RoleDao roleDao;
    private final PayeeDao payeeDao;

    public ImportActivityViewModel(@NonNull Application application) {
        super(application);
        EasyDatabase db = EasyDatabase.getDatabase(application);
        billDao = db.billDao();
        legerDao = db.legerDao();
        accountDao = db.accountDao();
        categoryDao = db.categoryDao();
        roleDao = db.roleDao();
        payeeDao = db.payeeDao();
    }

    public void insertBills(List<Bill> dataList) {
        AsyncProcessor.getInstance().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                billDao.insert(dataList);
                return null;
            }
        });
    }

    public void insertLegers(List<Leger> dataList) {
        AsyncProcessor.getInstance().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                legerDao.insert(dataList);
                return null;
            }
        });
    }

    public void insertAccounts(List<Account> dataList) {
        AsyncProcessor.getInstance().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                accountDao.insert(dataList);
                return null;
            }
        });
    }

    public void insertCategories(List<Category> dataList) {
        AsyncProcessor.getInstance().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                categoryDao.insert(dataList);
                return null;
            }
        });
    }

    public void insertRoles(List<Role> dataList) {
        AsyncProcessor.getInstance().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                roleDao.insert(dataList);
                return null;
            }
        });
    }

    public void insertPayees(List<Payee> dataList) {
        AsyncProcessor.getInstance().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                payeeDao.insert(dataList);
                return null;
            }
        });
    }

}
