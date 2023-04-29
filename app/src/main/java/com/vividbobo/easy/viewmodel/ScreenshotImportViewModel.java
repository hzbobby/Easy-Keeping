package com.vividbobo.easy.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.vividbobo.easy.database.EasyDatabase;
import com.vividbobo.easy.database.dao.AccountDao;
import com.vividbobo.easy.database.dao.BillDao;
import com.vividbobo.easy.database.dao.ConfigDao;
import com.vividbobo.easy.database.model.Account;
import com.vividbobo.easy.database.model.Bill;
import com.vividbobo.easy.database.model.Config;
import com.vividbobo.easy.utils.AsyncProcessor;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;

public class ScreenshotImportViewModel extends AndroidViewModel {

    private final LiveData<List<Account>> allAccounts;
    private final ConfigDao configDao;
    private final LiveData<Account> alipayAccount;
    private final LiveData<Account> wechatAccount;
    private final BillDao billDao;

    public ScreenshotImportViewModel(@NonNull Application application) {
        super(application);
        EasyDatabase db = EasyDatabase.getDatabase(application);
        AccountDao accountDao = db.accountDao();
        allAccounts = accountDao.getAllAccounts();
        configDao = db.configDao();
        alipayAccount = configDao.getAccountByType(Config.TYPE_IMPORT_ACCOUNT_ALIPAY);
        wechatAccount = configDao.getAccountByType(Config.TYPE_IMPORT_ACCOUNT_WECHAT);
        billDao = db.billDao();
    }

    public LiveData<Account> getAlipayAccount() {
        return alipayAccount;
    }

    public LiveData<Account> getWechatAccount() {
        return wechatAccount;
    }

    public LiveData<List<Account>> getAllAccounts() {
        return allAccounts;
    }

    public void updateSelectedWechatId(Integer id) {
        AsyncProcessor.getInstance().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                Integer existingId = configDao.getRawSelectedIdByType(Config.TYPE_IMPORT_ACCOUNT_WECHAT);
                if (Objects.isNull(existingId)) {
                    configDao.insert(new Config(Config.TYPE_IMPORT_ACCOUNT_WECHAT, id));
                } else {
                    configDao.updateSelectedId(Config.TYPE_IMPORT_ACCOUNT_WECHAT, id);
                }
                return null;
            }
        });
    }

    public void updateSelectedAlipayId(Integer id) {
        AsyncProcessor.getInstance().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                Integer existingId = configDao.getRawSelectedIdByType(Config.TYPE_IMPORT_ACCOUNT_ALIPAY);
                if (Objects.isNull(existingId)) {
                    configDao.insert(new Config(Config.TYPE_IMPORT_ACCOUNT_ALIPAY, id));
                } else {
                    configDao.updateSelectedId(Config.TYPE_IMPORT_ACCOUNT_ALIPAY, id);
                }
                return null;
            }
        });
    }

    public void insert(List<Bill> billList) {
        AsyncProcessor.getInstance().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                billDao.insert(billList);
                return null;
            }
        });
    }
}
