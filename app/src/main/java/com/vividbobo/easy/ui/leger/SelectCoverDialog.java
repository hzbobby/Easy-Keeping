package com.vividbobo.easy.ui.leger;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.vividbobo.easy.R;
import com.vividbobo.easy.ui.others.FullScreenDialog;
import com.vividbobo.easy.utils.ToastUtil;

public class SelectCoverDialog extends FullScreenDialog {
    public static final String TAG = "SelectCoverDialog";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_select_cover, container, false);

        Toolbar toolbar = view.findViewById(R.id.select_cover_tool_bar);
        RecyclerView coverRecyclerView = view.findViewById(R.id.select_cover_recycler_view);
        ExtendedFloatingActionButton fromGalleryBtn = view.findViewById(R.id.from_gallery_btn);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        fromGalleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 从图库中选择图片
                ToastUtil.makeToast("从图库中选择");
            }
        });

        coverRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        CoverAdapter coverAdapter = new CoverAdapter();

        coverRecyclerView.setAdapter(coverAdapter);


        return view;
    }
}
