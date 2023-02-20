package com.vividbobo.easy.ui.bill;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.vividbobo.easy.R;
import com.vividbobo.easy.model.TagItem;
import com.vividbobo.easy.model.viewmodel.BillViewModel;
import com.vividbobo.easy.ui.others.OnItemClickListener;
import com.vividbobo.easy.ui.tags.AddTagDialog;

import java.util.ArrayList;
import java.util.List;

public class RemarkTagBottomSheet extends BottomSheetDialogFragment {
    public static final String TAG = "RemarkBottomSheet";

    private EditText editText;
    //    private TextView targetText;
    private Toolbar toolbar;

    private RecyclerView chipRecyclerView;
    private RemarkTagAdapter remarkTagAdapter;

    private List<TagItem> tagItems = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.layout_remark_tags_sheet, container, false);
        //view model
        BillViewModel remarkTagViewModel = new ViewModelProvider(getActivity()).get(BillViewModel.class);
        editText = view.findViewById(R.id.remark_sheet_edit_text);
//        if (targetText!=null){
//            editText.setText(targetText.getText());
//        }
//
        //initial
        editText.setText(remarkTagViewModel.remark.getValue());


        toolbar = view.findViewById(R.id.remark_sheet_tool_bar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.remark_sheet_check) {
//                    if (targetText != null) {
//                        targetText.setText(editText.getText());
//                    } else {
//                        Log.d(TAG, "onMenuItemClick: 请设置TargetText");
//                    }
                    remarkTagViewModel.setRemark(editText.getText().toString());
                    remarkTagViewModel.setTagItems(tagItems);
                }
                dismiss();
                return false;
            }
        });


        //tag chip
        chipRecyclerView = view.findViewById(R.id.remark_sheet_chip_recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 5);
        chipRecyclerView.setLayoutManager(gridLayoutManager);
        remarkTagAdapter = new RemarkTagAdapter();
        remarkTagAdapter.setTags(remarkTagViewModel.tagItems.getValue());       //tags initial


        remarkTagAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void OnItemClick(Object item, int position) {
//                Log.d(TAG, "OnItemClick: item is check? "+((TagItem)item).isChecked());
                tagItems.add((TagItem) item);
            }
        });
        //add new tags dialog
        remarkTagAdapter.setOnAddChipClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddTagDialog(getContext()).show();
            }
        });


        chipRecyclerView.setAdapter(remarkTagAdapter);

        //set tag chip initial


        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetFragmentTheme);
    }
}
