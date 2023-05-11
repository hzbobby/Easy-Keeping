package com.vividbobo.easy.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.vividbobo.easy.database.EasyDatabase;
import com.vividbobo.easy.database.dao.AccountDao;
import com.vividbobo.easy.database.dao.AccountTypeDao;
import com.vividbobo.easy.database.dao.BillDao;
import com.vividbobo.easy.database.dao.CategoryDao;
import com.vividbobo.easy.database.dao.LegerDao;
import com.vividbobo.easy.database.dao.PayeeDao;
import com.vividbobo.easy.database.dao.RoleDao;
import com.vividbobo.easy.database.dao.TagDao;
import com.vividbobo.easy.database.model.Account;
import com.vividbobo.easy.database.model.AccountType;
import com.vividbobo.easy.database.model.Bill;
import com.vividbobo.easy.database.model.Category;
import com.vividbobo.easy.database.model.CheckableItem;
import com.vividbobo.easy.database.model.Leger;
import com.vividbobo.easy.database.model.Payee;
import com.vividbobo.easy.database.model.QueryCondition;
import com.vividbobo.easy.database.model.Role;
import com.vividbobo.easy.database.model.Tag;
import com.vividbobo.easy.utils.ConvertUtils;

import java.sql.Date;
import java.util.List;
import java.util.Objects;

import kotlin.jvm.functions.Function1;

public class FilterViewModel extends AndroidViewModel {
    private final LiveData<List<Account>> accounts;
    private final LiveData<List<Leger>> legers;
    private final LiveData<List<Payee>> payees;
    private final LiveData<List<Role>> roles;
    private final LiveData<List<Tag>> tags;
    private final LiveData<List<Category>> expenditureCategories;
    private final LiveData<List<Category>> incomeCategories;
    private final LiveData<List<Bill>> queryBills;
    private final BillDao billDao;
    private MutableLiveData<QueryCondition> queryCondition;

    public FilterViewModel(@NonNull Application application) {
        super(application);
        EasyDatabase db = EasyDatabase.getDatabase(application);
        AccountDao accountDao = db.accountDao();
        accounts = accountDao.getAllAccounts();


        LegerDao legerDao = db.legerDao();
        legers = legerDao.getAllLegersLD();

        PayeeDao payeeDao = db.payeeDao();
        payees = payeeDao.getAllStores();

        RoleDao roleDao = db.roleDao();
        roles = roleDao.getAllRoles();

        TagDao tagDao = db.tagDao();
        tags = tagDao.getALlTags();

        CategoryDao categoryDao = db.categoryDao();
        expenditureCategories = categoryDao.getCategories(Category.TYPE_EXPENDITURE);
        incomeCategories = categoryDao.getCategories(Category.TYPE_INCOME);

        queryCondition = new MutableLiveData<>();
        billDao = db.billDao();
        queryBills = Transformations.map(Transformations.switchMap(queryCondition, new Function1<QueryCondition, LiveData<List<Bill>>>() {
            @Override
            public LiveData<List<Bill>> invoke(QueryCondition queryCondition) {
                return convertToBills(queryCondition, billDao);
            }
        }), this::filterTags);

    }

    public List<Bill> filterTags(List<Bill> billList) {
        Log.d("TAG", "filterTags: start: " + billList.size());
        String[] tags = null;
        QueryCondition qc = queryCondition.getValue();
        if (qc.getStringArrayMap().containsKey(QueryCondition.BILL_TAG)) {
            tags = qc.getStringArrayMap().get(QueryCondition.BILL_TAG);
        }
        for (int i = 0; i < billList.size(); i++) {
            Bill bill = billList.get(i);
            if (Objects.isNull(bill.getTags()) || bill.getTags().isEmpty()) {
                continue;
            }
            StringBuilder sb = new StringBuilder();

            for (int j = 0; j < bill.getTags().size(); j++) {
                Tag tag = bill.getTags().get(j);
                sb.append(tag.getTitle() + ",");
            }
            Log.d("TAG", "filterTags: " + sb.toString());
            for (int j = 0; j < tags.length; j++) {
                String tagTitle = tags[j];
                if (sb.toString().contains(tagTitle)) {
                    billList.remove(i);
                    Log.d("TAG", "filterTags: size -1");
                    break;
                }
            }
        }
        Log.d("TAG", "filterTags: end: " + billList.size());

        return billList;
    }

    public LiveData<List<Bill>> getQueryBills() {
        return queryBills;
    }

    public LiveData<List<Bill>> convertToBills(QueryCondition queryCondition, BillDao billDao) {
        Integer[] billTypes = null;
        Integer[] legers = null;
        Integer[] accounts = null;
        Integer[] roles = null;
        Integer[] payees = null;
        Integer[] categories = null;
        Boolean image = null;
        Boolean refund = null;
        Boolean reimburse = null;
        Boolean inExp = null;
        Boolean budget = null;
        Long minAmount = null;
        Long maxAmount = null;
        String remark = null;
        Date startDate = null;
        Date endDate = null;

        if (queryCondition.getIntSetMap().containsKey(QueryCondition.BILL_BILLTYPE)) {
            billTypes = queryCondition.getIntSetMap().get(QueryCondition.BILL_BILLTYPE).toArray(new Integer[0]);
        }
        if (queryCondition.getIntSetMap().containsKey(QueryCondition.BILL_LEGER)) {
            legers = (Integer[]) queryCondition.getIntSetMap().get(QueryCondition.BILL_LEGER).toArray(new Integer[0]);
        }
        if (queryCondition.getIntSetMap().containsKey(QueryCondition.BILL_ACCOUNT)) {
            accounts = (Integer[]) queryCondition.getIntSetMap().get(QueryCondition.BILL_ACCOUNT).toArray(new Integer[0]);
        }
        if (queryCondition.getIntSetMap().containsKey(QueryCondition.BILL_ROLE)) {
            roles = (Integer[]) queryCondition.getIntSetMap().get(QueryCondition.BILL_ROLE).toArray(new Integer[0]);
        }
        if (queryCondition.getIntSetMap().containsKey(QueryCondition.BILL_PAYEE)) {
            payees = (Integer[]) queryCondition.getIntSetMap().get(QueryCondition.BILL_PAYEE).toArray(new Integer[0]);
        }
        if (queryCondition.getIntSetMap().containsKey(QueryCondition.BILL_CATEGORY)) {
            categories = (Integer[]) queryCondition.getIntSetMap().get(QueryCondition.BILL_CATEGORY).toArray(new Integer[0]);
        }


        if (queryCondition.getBoolMap().containsKey(QueryCondition.BILL_IMAGE)) {
            image = queryCondition.getBoolMap().get(QueryCondition.BILL_IMAGE);
        }
        if (queryCondition.getBoolMap().containsKey(QueryCondition.BILL_REFUND)) {
            refund = queryCondition.getBoolMap().get(QueryCondition.BILL_REFUND);
        }
        if (queryCondition.getBoolMap().containsKey(QueryCondition.BILL_REIMBURSE)) {
            reimburse = queryCondition.getBoolMap().get(QueryCondition.BILL_REIMBURSE);
        }
        if (queryCondition.getBoolMap().containsKey(QueryCondition.BILL_INCOME_EXPENDITURE)) {
            inExp = queryCondition.getBoolMap().get(QueryCondition.BILL_INCOME_EXPENDITURE);
        }
        if (queryCondition.getBoolMap().containsKey(QueryCondition.BILL_BUDGET)) {
            budget = queryCondition.getBoolMap().get(QueryCondition.BILL_BUDGET);
        }

        if (queryCondition.getLongMap().containsKey(QueryCondition.BILL_MIN_AMOUNT)) {
            minAmount = queryCondition.getLongMap().get(QueryCondition.BILL_MIN_AMOUNT);
        }
        if (queryCondition.getLongMap().containsKey(QueryCondition.BILL_MAX_AMOUNT)) {
            maxAmount = queryCondition.getLongMap().get(QueryCondition.BILL_MAX_AMOUNT);
        }

        if (queryCondition.getStringMap().containsKey(QueryCondition.BILL_REMARK)) {
            remark = queryCondition.getStringMap().get(QueryCondition.BILL_REMARK);
        }

        if (queryCondition.getObjectMap().containsKey(QueryCondition.BILL_DATE_START)) {
            java.util.Date tDate = (java.util.Date) queryCondition.getObjectMap().get(QueryCondition.BILL_DATE_START);
            startDate = new Date(tDate.getTime());
        }
        if (queryCondition.getObjectMap().containsKey(QueryCondition.BILL_DATE_END)) {
            java.util.Date tDate = (java.util.Date) queryCondition.getObjectMap().get(QueryCondition.BILL_DATE_END);
            endDate = new Date(tDate.getTime());
        }


        LiveData<List<Bill>> bills = billDao.queryByConditions(billTypes, legers, accounts, roles, payees, categories, image, refund, reimburse, inExp, budget, minAmount, maxAmount, remark, startDate, endDate);

        return bills;

    }

    public LiveData<List<CheckableItem<Category>>> getExpenditureCategories() {
        return Transformations.switchMap(expenditureCategories, new Function1<List<Category>, LiveData<List<CheckableItem<Category>>>>() {
            @Override
            public LiveData<List<CheckableItem<Category>>> invoke(List<Category> accountList) {
                return new MutableLiveData<>(ConvertUtils.convertToCheckableItem(accountList, true));
            }
        });
    }

    public LiveData<List<CheckableItem<Category>>> getIncomeCategories() {
        return Transformations.switchMap(incomeCategories, new Function1<List<Category>, LiveData<List<CheckableItem<Category>>>>() {
            @Override
            public LiveData<List<CheckableItem<Category>>> invoke(List<Category> accountList) {
                return new MutableLiveData<>(ConvertUtils.convertToCheckableItem(accountList, true));
            }
        });
    }

    public LiveData<List<CheckableItem<Account>>> getAccounts() {
        return Transformations.switchMap(accounts, new Function1<List<Account>, LiveData<List<CheckableItem<Account>>>>() {
            @Override
            public LiveData<List<CheckableItem<Account>>> invoke(List<Account> accountList) {
                return new MutableLiveData<>(ConvertUtils.convertToCheckableItem(accountList, true));
            }
        });
    }


    public LiveData<List<CheckableItem<Leger>>> getLegers() {
        return Transformations.switchMap(legers, new Function1<List<Leger>, LiveData<List<CheckableItem<Leger>>>>() {
            @Override
            public LiveData<List<CheckableItem<Leger>>> invoke(List<Leger> accountList) {
                return new MutableLiveData<>(ConvertUtils.convertToCheckableItem(accountList, true));
            }
        });
    }

    public LiveData<List<CheckableItem<Payee>>> getPayees() {
        return Transformations.switchMap(payees, new Function1<List<Payee>, LiveData<List<CheckableItem<Payee>>>>() {
            @Override
            public LiveData<List<CheckableItem<Payee>>> invoke(List<Payee> accountList) {
                return new MutableLiveData<>(ConvertUtils.convertToCheckableItem(accountList, true));
            }
        });
    }

    public LiveData<List<CheckableItem<Role>>> getRoles() {
        return Transformations.switchMap(roles, new Function1<List<Role>, LiveData<List<CheckableItem<Role>>>>() {
            @Override
            public LiveData<List<CheckableItem<Role>>> invoke(List<Role> accountList) {
                return new MutableLiveData<>(ConvertUtils.convertToCheckableItem(accountList, true));
            }
        });
    }

    public LiveData<List<CheckableItem<Tag>>> getTags() {
        return Transformations.switchMap(tags, new Function1<List<Tag>, LiveData<List<CheckableItem<Tag>>>>() {
            @Override
            public LiveData<List<CheckableItem<Tag>>> invoke(List<Tag> accountList) {
                return new MutableLiveData<>(ConvertUtils.convertToCheckableItem(accountList, true));
            }
        });
    }

    public void query(QueryCondition queryCondition) {
        this.queryCondition.postValue(queryCondition);
    }

}
