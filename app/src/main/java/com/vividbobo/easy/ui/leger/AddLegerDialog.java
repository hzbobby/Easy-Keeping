package com.vividbobo.easy.ui.leger;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.vividbobo.easy.R;
import com.vividbobo.easy.database.model.LegerItem;
import com.vividbobo.easy.ui.others.FullScreenDialog;
import com.vividbobo.easy.utils.ToastUtil;

/***
 * add leger full screen dialog
 */
public class AddLegerDialog extends FullScreenDialog {
    public static final String TAG = "AddLegerDialog";


    public static AddLegerDialog newInstance(LegerItem legerItem) {
        AddLegerDialog addLegerDialog = new AddLegerDialog();

        if (legerItem != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("leger", legerItem);
            addLegerDialog.setArguments(bundle);
        }

        return addLegerDialog;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_leger, container, false);

        Toolbar toolbar = view.findViewById(R.id.add_leger_tool_bar);
        TextInputLayout inputLayout = view.findViewById(R.id.add_leger_input_layout);
        ConstraintLayout selectCoverLayout = view.findViewById(R.id.add_leger_select_cover_layout);
        ImageView cover = view.findViewById(R.id.add_leger_cover);
        MaterialButton saveBtn = view.findViewById(R.id.add_leger_save_btn);
        MaterialButton deleteBtn = view.findViewById(R.id.add_leger_delete_btn);

        if (getArguments() != null) {
            Bundle bundle = getArguments();
            LegerItem legerItem = (LegerItem) bundle.getSerializable("leger");
            inputLayout.getEditText().setText(legerItem.getTitle());
            deleteBtn.setVisibility(View.VISIBLE);
            //TODO set cover
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        selectCoverLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SelectCoverDialog().show(getChildFragmentManager(), SelectCoverDialog.TAG);
            }
        });


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = inputLayout.getEditText().getText().toString();
                if (title.isEmpty()) {
                    ToastUtil.makeToast("请输入账本名称");
                } else {
                    //TODO save leger


                    dismiss();
                }
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO delete leger
            }
        });


        return view;
    }


}
