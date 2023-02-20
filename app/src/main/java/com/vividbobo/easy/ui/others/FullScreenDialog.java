package com.vividbobo.easy.ui.others;

import androidx.fragment.app.DialogFragment;

import com.vividbobo.easy.R;

public class FullScreenDialog extends DialogFragment {

    //全屏设置
    @Override
    public int getTheme() {
        return R.style.FullDialogTheme;
    }
}
