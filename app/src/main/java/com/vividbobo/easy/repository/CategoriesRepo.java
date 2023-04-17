package com.vividbobo.easy.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.google.common.util.concurrent.ListenableFuture;
import com.vividbobo.easy.database.EasyDatabase;
import com.vividbobo.easy.database.dao.CategoryDao;
import com.vividbobo.easy.database.model.Category;
import com.vividbobo.easy.database.model.CategoryPresent;
import com.vividbobo.easy.utils.AsyncProcessor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class CategoriesRepo {
    //the Dao
    private final CategoryDao categoryDao;
    //the LiveData
    private final LiveData<List<Category>> expenditureCategories;
    private final LiveData<List<Category>> incomeCategories;

    public CategoriesRepo(Application application) {
        EasyDatabase db = EasyDatabase.getDatabase(application);
        categoryDao = db.categoryDao();
        expenditureCategories = categoryDao.getCategories(Category.TYPE_EXPENDITURE);
        incomeCategories = categoryDao.getCategories(Category.TYPE_INCOME);
    }

    /**
     * Transformations.map是Android Jetpack库中Lifecycle组件的一部分，用于对LiveData对象应用一次性变换。
     * 它接收两个参数：一个输入LiveData对象和一个用于转换数据的Function对象。Transformations.map会返回一个
     * 新的LiveData对象，该对象将包含应用转换后的数据。
     *
     * @return 返回封装后的LiveData
     */
    public LiveData<List<CategoryPresent>> getExpenditureCategories() {
        return Transformations.map(expenditureCategories, this::convertToCategoryParentList);
    }

    public LiveData<List<CategoryPresent>> getIncomeCategories() {
        return Transformations.map(incomeCategories, this::convertToCategoryParentList);
    }


    private List<CategoryPresent> convertToCategoryParentList(List<Category> categories) {
        Map<Integer, CategoryPresent> map = new LinkedHashMap<>();
        for (int i = 0; i < categories.size(); i++) {
            Category item = categories.get(i);
            if (item.getParentId() == null || item.getParentId() == Category.DEFAULT_PARENT_ID) {
                //新的parent
                CategoryPresent categoryPresent = new CategoryPresent(item);
                categoryPresent.setParent(item);
                map.put(categoryPresent.getId(), categoryPresent);
                continue;
            }
            CategoryPresent parent = map.get(item.getParentId());
            // if parent is not null
            Log.d("TAG", "convertToCategoryParentList: " + item.toString());
            parent.addChild(item);
        }

        List<CategoryPresent> categoryPresentList = new ArrayList<>();
        for (Integer key : map.keySet()) {
            categoryPresentList.add(map.get(key));
        }
        //sort list by order
        Collections.sort(categoryPresentList, new Comparator<CategoryPresent>() {
            @Override
            public int compare(CategoryPresent o1, CategoryPresent o2) {
                if (o1.getOrderNum() < o2.getOrderNum()) return -1;
                else if (o1.getOrderNum() > o2.getOrderNum()) return 1;
                else return 0;
            }
        });
        Log.d("TAG", "convertToCategoryParentList: size:" + categoryPresentList.size());
        return categoryPresentList;
    }

    public void insert(Category category) {
        ListenableFuture future = AsyncProcessor.getInstance().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                category.setOrderNum(categoryDao.getMaxOrderNumByParentId(category.getParentId()) + 1);
                categoryDao.insert(category);
                Log.d("ASYGN", "call: 插入成功");
                return null;
            }
        });

    }


}
