package com.vividbobo.easy.utils;

import android.content.Context;
import android.util.Log;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * In Android, a LayoutManager can only  be attach to a RecyclerView. if your recyclerview child
 * item is also a recyclerview, you need to set a LayoutManager for the child recyclerview, if you
 * want the child RV layoutManager given by user, it may pass the layoutManager through parent
 * Adapter, so the tool class can help you create the same LayoutManager as your given.
 */
public class LayoutManagerCloner {

    public static LinearLayoutManager clone(Context context, LinearLayoutManager original) {
        LinearLayoutManager cloned = new LinearLayoutManager(context);
        cloned.setOrientation(original.getOrientation());
        cloned.setReverseLayout(original.getReverseLayout());
        cloned.setStackFromEnd(original.getStackFromEnd());
        return cloned;
    }

    public static GridLayoutManager clone(Context context, GridLayoutManager original) {
        int spanCount = original.getSpanCount();
        GridLayoutManager cloned = new GridLayoutManager(context, spanCount);
        //
        return cloned;
    }
}
