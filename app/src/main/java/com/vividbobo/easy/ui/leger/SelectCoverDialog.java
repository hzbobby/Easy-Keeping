package com.vividbobo.easy.ui.leger;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.vividbobo.easy.adapter.adapter.CoverAdapter;
import com.vividbobo.easy.database.model.Resource;
import com.vividbobo.easy.databinding.LayoutSelectCoverBinding;
import com.vividbobo.easy.ui.common.BaseFullScreenMaterialDialog;
import com.vividbobo.easy.ui.common.ContextOperationMenuDialog;
import com.vividbobo.easy.ui.others.OnDialogResult;
import com.vividbobo.easy.ui.others.OnItemClickListener;
import com.vividbobo.easy.utils.AsyncProcessor;
import com.vividbobo.easy.viewmodel.ResourceViewModel;

import java.util.List;

public class SelectCoverDialog extends BaseFullScreenMaterialDialog<LayoutSelectCoverBinding> {
    public static final String TAG = "SelectCoverDialog";
    ResourceViewModel resourceViewModel;
    private OnDialogResult onDialogResult;

    public void setOnDialogResult(OnDialogResult onDialogResult) {
        this.onDialogResult = onDialogResult;
    }

    public static SelectCoverDialog newInstance() {
        Bundle args = new Bundle();

        SelectCoverDialog fragment = new SelectCoverDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resourceViewModel = new ViewModelProvider(getActivity()).get(ResourceViewModel.class);
    }

    @Override
    protected LayoutSelectCoverBinding getViewBinding(@NonNull LayoutInflater inflater) {
        return LayoutSelectCoverBinding.inflate(inflater);
    }

    @Override
    protected void onViewBinding(LayoutSelectCoverBinding binding) {
        binding.appBarLayout.layoutToolBarTitleTv.setText("选择封面");
        binding.appBarLayout.layoutToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        CoverAdapter coverAdapter = new CoverAdapter(getContext());
        coverAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                Resource resource = (Resource) item;
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", resource);
                onDialogResult.onResult(bundle);
                dismiss();
            }
        });
        binding.coverRv.setAdapter(coverAdapter);

        Futures.addCallback(resourceViewModel.getSysCoverLF(), new FutureCallback<List<Resource>>() {
            @Override
            public void onSuccess(List<Resource> result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "run: covers size: " + result.size());
                        coverAdapter.updateItems(result);
                    }
                });
            }

            @Override
            public void onFailure(Throwable t) {

            }
        }, AsyncProcessor.getInstance().getExecutorService());
    }


}
