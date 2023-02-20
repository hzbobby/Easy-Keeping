package com.vividbobo.easy.model.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.vividbobo.easy.model.AccountItem;
import com.vividbobo.easy.model.CategoryItem;
import com.vividbobo.easy.model.TagItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/***
 * 用于三个Fragment之间共享数据
 */
public class BillViewModel extends ViewModel {
    //用 viewmodel 共享数据

    //金额
    private final MutableLiveData<Integer> _amount = new MutableLiveData<>(0);
    //支出类别
    private final MutableLiveData<CategoryItem> _category_outcome = new MutableLiveData<>(new CategoryItem());
    private final MutableLiveData<Integer> _category_outcome_position = new MutableLiveData<>(0);
    //收入类别
    private final MutableLiveData<CategoryItem> _category_income = new MutableLiveData<>(new CategoryItem());
    private final MutableLiveData<Integer> _category_income_position = new MutableLiveData<>(0);

    //转账类别
    private final MutableLiveData<CategoryItem> _category_transfer = new MutableLiveData<>(new CategoryItem());
    private final MutableLiveData<Integer> _category_transfer_position = new MutableLiveData<>(0);

    //账户
    private final MutableLiveData<AccountItem> _account_payment = new MutableLiveData<>(new AccountItem());
    private final MutableLiveData<AccountItem> _account_receive = new MutableLiveData<>(new AccountItem());
    //备注
    private final MutableLiveData<String> _remark = new MutableLiveData<>("");
    //标签
    private final MutableLiveData<List<TagItem>> _tagItems = new MutableLiveData<>(new ArrayList<>());
    //日期
    private final MutableLiveData<Long> _date = new MutableLiveData<>(System.currentTimeMillis());  //或改用long
    //时间
    private final MutableLiveData<Integer[]> _time = new MutableLiveData<>(new Integer[2]);
//    private final MutableLiveData<LegerItem> leger=new MutableLiveData<LegerItem>();  //账本是否在此处共享？


    public LiveData<String> remark = _remark;
    public LiveData<List<TagItem>> tagItems = _tagItems;

    public LiveData<Long> date = _date;
    public LiveData<Integer[]> time = _time;

    public void setRemark(String remark) {
        _remark.setValue(remark);
    }

    public void setTagItems(List<TagItem> tagItems) {
        _tagItems.setValue(tagItems);
    }

    public void setDate(Long date) {
        this._date.setValue(date);
    }

    public void setTime(Integer[] time) {
        this._time.setValue(time);
    }
}