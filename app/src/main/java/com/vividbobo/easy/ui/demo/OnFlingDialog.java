package com.vividbobo.easy.ui.demo;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.NonNull;

import com.google.android.material.card.MaterialCardView;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome;
import com.vividbobo.easy.R;
import com.vividbobo.easy.databinding.LayoutFlingBinding;
import com.vividbobo.easy.ui.bill.BillActivity;
import com.vividbobo.easy.ui.common.BaseFullScreenMaterialDialog;

public class OnFlingDialog extends BaseFullScreenMaterialDialog<LayoutFlingBinding> {
    public static final String TAG = "OnFlingDialog";

    private GestureDetector gestureDetector;

    @Override
    protected LayoutFlingBinding getViewBinding(@NonNull LayoutInflater inflater) {
        return LayoutFlingBinding.inflate(inflater);
    }

    @Override
    protected void onViewBinding(LayoutFlingBinding binding) {
        gestureDetector = new GestureDetector(binding.getRoot().getContext(), new MGestureListener(binding.flingViewCv));

        binding.getRoot().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });


        IconicsDrawable iconicsDrawable = new IconicsDrawable();
        iconicsDrawable.setIcon(FontAwesome.INSTANCE.getIcon(""));
        binding.onflingIv.setImageDrawable(iconicsDrawable);

    }

    private boolean isKeyBoardExpand = true;

    private void expandedBottomKeyBoard(View calView) {
        float translationY = isKeyBoardExpand ? calView.getHeight() : -calView.getHeight();
        calView.animate()
                .translationYBy(translationY)
                .setDuration(300)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();
        isKeyBoardExpand = !isKeyBoardExpand;
    }

    private class MGestureListener extends GestureDetector.SimpleOnGestureListener {
        private View v;

        public MGestureListener(View v) {
            this.v = v;
        }

        @Override
        public boolean onFling(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
            if (e1 == null || e2 == null) {
                return false;
            }
            Log.d(TAG, "onFling1:" + Math.abs(e1.getX() - e2.getX()));
            Log.d(TAG, "onFling2:" + Math.abs(e1.getY() - e2.getY()));

            // 判断垂直滑动
            if (Math.abs(e1.getX() - e2.getX()) < Math.abs(e1.getY() - e2.getY())) {
                Log.d(TAG, "onFling: " + (e1.getY() - e2.getY()));
                if (e1.getY() - e2.getY() > 100 && Math.abs(velocityY) > 200) {
                    // 向上滑动
                    if (!isKeyBoardExpand) {
                        expandedBottomKeyBoard(v);
                    }
                    return true;
                } else if (e2.getY() - e1.getY() > 100 && Math.abs(velocityY) > 200) {
                    // 向下滑动
                    if (isKeyBoardExpand) {
                        expandedBottomKeyBoard(v);
                    }
                    return true;
                }
            }

            return false;
        }
    }
}
