package com.vividbobo.easy.ui.common;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.vividbobo.easy.R;

public abstract class BottomSheetDialog<VB extends ViewBinding> extends BottomSheetDialogFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    public abstract VB getViewBinding(@NonNull LayoutInflater inflater);

    public abstract void onViewBinding(VB binding);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        VB binding = getViewBinding(inflater);

        onViewBinding(binding);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View bottomSheet = (View) view.getParent();
        if (bottomSheet != null) {
            // 获取BottomSheetBehavior
            BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);

            // 设置PeekHeight（半屏时的高度）
            behavior.setPeekHeight(getInitialPeekHeight());

            // 设置最大高度（全屏时的高度）
            ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
            layoutParams.height = getFullPeekHeight();
            bottomSheet.setLayoutParams(layoutParams);
        }
    }

    /**
     * 初始时的高度
     * @return
     */
    protected int getInitialPeekHeight() {
        return getResources().getDisplayMetrics().heightPixels / 5 * 2;
    }

    /**
     * 向上拉动后的最大高度
     * @return
     */
    protected int getFullPeekHeight() {
        return ViewGroup.LayoutParams.MATCH_PARENT;
    }

}
