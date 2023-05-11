package com.vividbobo.easy.ui.leger;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.IconCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.vividbobo.easy.R;
import com.vividbobo.easy.database.model.Leger;
import com.vividbobo.easy.database.model.Resource;
import com.vividbobo.easy.databinding.DialogAddLegerBinding;
import com.vividbobo.easy.ui.common.BaseFullScreenMaterialDialog;
import com.vividbobo.easy.ui.others.OnDialogResult;
import com.vividbobo.easy.utils.ConstantValue;
import com.vividbobo.easy.utils.GlideUtils;
import com.vividbobo.easy.utils.ResourceUtils;
import com.vividbobo.easy.utils.ToastUtil;
import com.vividbobo.easy.viewmodel.LegerViewModel;

import java.sql.Timestamp;
import java.util.Objects;

/***
 * add leger full screen dialog
 */
public class AddLegerDialog extends BaseFullScreenMaterialDialog<DialogAddLegerBinding> {
    public static final String TAG = "AddLegerDialog";
    private static final String KEY_LEGER = "data";
    private LegerViewModel legerViewModel;

    private String coverResName;
    private Resource.ResourceType coverResType;
    private Leger editLeger;

    public static AddLegerDialog newInstance(Leger leger) {
        Bundle args = new Bundle();
        args.putSerializable(KEY_LEGER, leger);
        AddLegerDialog fragment = new AddLegerDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public static AddLegerDialog newInstance() {
        Bundle args = new Bundle();
        AddLegerDialog fragment = new AddLegerDialog();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected DialogAddLegerBinding getViewBinding(@NonNull LayoutInflater inflater) {
        return DialogAddLegerBinding.inflate(inflater);
    }

    @Override
    protected void onViewBinding(DialogAddLegerBinding binding) {
        editLeger = (Leger) getArguments().getSerializable(KEY_LEGER);
        Log.d(TAG, "update: edit is null: " + Objects.isNull(editLeger));
        binding.appBarLayout.layoutToolBarTitleTv.setText(R.string.add_leger);

        if (editLeger != null) {
            binding.appBarLayout.layoutToolBarTitleTv.setText("编辑账本");
            binding.legerTitleTil.getEditText().setText(editLeger.getTitle());
            binding.legerDescTil.getEditText().setText(editLeger.getDesc());
            coverResName = editLeger.getItemIconResName();
            coverResType = editLeger.getCoverType();

            GlideUtils.bindLegerCover(getContext(), editLeger.getItemIconResName(), editLeger.getCoverType())
                    .centerCrop().into(binding.legerCoverIv);
        }

        binding.appBarLayout.layoutToolBar.inflateMenu(R.menu.confirm);
        binding.appBarLayout.layoutToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        binding.appBarLayout.layoutToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.confirm) {
                    boolean resultValue;
                    if (editLeger == null) {
                        resultValue = save(binding);
                    } else {
                        resultValue = update(binding);
                    }
                    if (resultValue) {
                        dismiss();
                    }
                    Log.d(TAG, "onMenuItemClick: confirm result: " + resultValue);
                    return resultValue;
                }
                return false;
            }
        });

        SelectCoverDialog selectCoverDialog = SelectCoverDialog.newInstance();
        selectCoverDialog.setOnDialogResult(new OnDialogResult() {
            @Override
            public void onResult(Bundle result) {
                Resource resource = (Resource) result.getSerializable("data");
                coverResName = resource.getResName();
                coverResType = resource.getResType();
                if (coverResType.equals(Resource.ResourceType.USER_COVER)) {
                    Glide.with(getContext()).load(coverResName).centerCrop().into(binding.legerCoverIv);
                } else {
                    ResourceUtils.bindImageDrawable(getContext(), ResourceUtils.getDrawable(coverResName))
                            .centerCrop().into(binding.legerCoverIv);
                }
            }
        });
        binding.legerCoverIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //select cover

                selectCoverDialog.show(getParentFragmentManager(), SelectCoverDialog.TAG);
            }
        });
    }

    private boolean update(DialogAddLegerBinding binding) {
        String title = binding.legerTitleTil.getEditText().getText().toString();
        if (title == null || title.isEmpty()) {
            ToastUtil.makeToast("请填写账本名称");
            return false;
        }
        editLeger.setTitle(title);
        editLeger.setDesc(binding.legerDescTil.getEditText().getText().toString());
        editLeger.setCoverPath(coverResName);
        editLeger.setCoverType(coverResType);
        legerViewModel.update(editLeger);
        return true;
    }

    private boolean save(DialogAddLegerBinding binding) {
        Leger leger = new Leger();
        String title = binding.legerTitleTil.getEditText().getText().toString();
        String desc = binding.legerDescTil.getEditText().getText().toString();
        if (title.isEmpty()) {
            ToastUtil.makeToast("请填写账本名称");
            return false;
        }
        leger.setTitle(title);
        leger.setDesc(desc);
        if (coverResName == null || coverResName.isEmpty()) {
            coverResName = ConstantValue.DEFAULT_LEGER_COVER;
        }
        leger.setCoverPath(coverResName);

        if (coverResType == null) {
            coverResType = Resource.ResourceType.SYSTEM_COVER;
        }
        leger.setCoverType(coverResType);
        leger.setCreateTime(new Timestamp(System.currentTimeMillis()));
        Log.d(TAG, "save: " + leger.toString());
        legerViewModel.insert(leger);
        return true;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        legerViewModel = new ViewModelProvider(getActivity()).get(LegerViewModel.class);
    }


}
