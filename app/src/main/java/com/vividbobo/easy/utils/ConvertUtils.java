package com.vividbobo.easy.utils;

import com.vividbobo.easy.adapter.Itemzable;
import com.vividbobo.easy.database.model.CheckableItem;

import java.util.ArrayList;
import java.util.List;

public class ConvertUtils {
    public static <T extends Itemzable> List<CheckableItem<T>> convertToCheckableItem(List<T> items,boolean isChecked) {
        List<CheckableItem<T>> checkableItems = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            checkableItems.add(new CheckableItem<>(items.get(i),isChecked));
        }
        return checkableItems;
    }
}
