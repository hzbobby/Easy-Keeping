package com.vividbobo.easy.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.vividbobo.easy.database.EasyDatabase;
import com.vividbobo.easy.database.dao.AccountDao;
import com.vividbobo.easy.database.dao.ConfigDao;
import com.vividbobo.easy.database.dao.LegerDao;
import com.vividbobo.easy.database.model.Account;
import com.vividbobo.easy.database.model.Config;
import com.vividbobo.easy.database.model.Leger;
import com.vividbobo.easy.utils.AsyncProcessor;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;

public class AccountSettingsViewModel extends AndroidViewModel {
    private final ConfigDao configDao;
    private final LiveData<Account> wechatAccount;
    private final LiveData<Account> alipayAccount;
    private final AccountDao accountDao;
    private final LiveData<List<Account>> allAccounts;
    private final LiveData<Leger> leger;
    private final LegerDao legerDao;
    private final LiveData<List<Leger>> allLegers;

    public AccountSettingsViewModel(@NonNull Application application) {
        super(application);
        EasyDatabase db = EasyDatabase.getDatabase(application);
        configDao = db.configDao();
        wechatAccount = configDao.getAccountByType(Config.TYPE_AUTO_BILLING_ACCOUNT_WECHAT);
        alipayAccount = configDao.getAccountByType(Config.TYPE_AUTO_BILLING_ACCOUNT_ALIPAY);
        leger = configDao.getLegerByType(Config.TYPE_AUTO_BILLING_LEGER);

        accountDao = db.accountDao();
        allAccounts = accountDao.getAllAccounts();

        legerDao = db.legerDao();
        allLegers = legerDao.getAllLegersLD();
    }

    public void updateSelectedWechatId(Integer id) {
        updateSelectedId(Config.TYPE_AUTO_BILLING_ACCOUNT_WECHAT, id);
    }

    public void updateSelectedAlipayId(Integer id) {
        updateSelectedId(Config.TYPE_AUTO_BILLING_ACCOUNT_ALIPAY, id);
    }

    public void updateSelectedId(int type, int id) {
        AsyncProcessor.getInstance().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                Integer existingId = configDao.getRawSelectedIdByType(type);
                if (Objects.isNull(existingId)) {
                    configDao.insert(new Config(type, id));
                } else {
                    configDao.updateSelectedId(type, id);
                }
                return null;
            }
        });
    }

    public LiveData<Account> getWechatAccount() {
        return wechatAccount;
    }

    public LiveData<Account> getAlipayAccount() {
        return alipayAccount;
    }

    public LiveData<List<Account>> getAllAccounts() {
        return allAccounts;
    }

    public LiveData<Leger> getLeger() {
        return leger;
    }

    public LiveData<List<Leger>> getAllLegers() {
        return allLegers;
    }
}
