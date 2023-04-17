package com.vividbobo.easy.ui.others;

import android.view.View;

public interface OnItemClickListener {
    /**
     * @param view     the clickable list item view
     * @param item     data item
     * @param position the holder position
     */
    void onItemClick(View view, Object item, int position);
}
