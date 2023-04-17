package com.vividbobo.easy.ui.common;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.vividbobo.easy.R;

public abstract class BaseFullScreenMaterialDialog<VB extends androidx.viewbinding.ViewBinding> extends DialogFragment {
    private VB binding;

    protected abstract VB getViewBinding(@NonNull LayoutInflater inflater);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = getViewBinding(inflater);
        onViewBinding(binding);

        return binding.getRoot();
    }

    protected abstract void onViewBinding(VB binding);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogTheme);
    }

}
