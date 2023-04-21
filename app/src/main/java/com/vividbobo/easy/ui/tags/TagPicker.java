package com.vividbobo.easy.ui.tags;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.vividbobo.easy.R;
import com.vividbobo.easy.adapter.TagAdapter;
import com.vividbobo.easy.adapter.viewholder.BaseEntityFooterVH;
import com.vividbobo.easy.adapter.viewholder.TagVH;
import com.vividbobo.easy.database.model.Tag;
import com.vividbobo.easy.databinding.DialogCommonPickerBinding;
import com.vividbobo.easy.database.model.TagPresent;
import com.vividbobo.easy.ui.common.BaseMaterialDialog;
import com.vividbobo.easy.ui.common.BaseDialogPickerAdapter;
import com.vividbobo.easy.ui.common.CommonFooterViewHolder;
import com.vividbobo.easy.ui.common.CommonItemViewHolder;
import com.vividbobo.easy.ui.others.OnItemClickListener;
import com.vividbobo.easy.ui.others.commonAdapter.CommonAdapter;
import com.vividbobo.easy.viewmodel.BillViewModel;
import com.vividbobo.easy.viewmodel.CurrencyViewModel;
import com.vividbobo.easy.viewmodel.TagViewModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class TagPicker extends BaseMaterialDialog<DialogCommonPickerBinding> {

    private OnPickerConfirmClickListener<List<Tag>> onPickerConfirmClickListener;

    public static final String TAG = "TagPicker";
    private TagViewModel tagViewModel;

    //    private Set<Integer> selectedPos;
    private Set<Tag> selectedTagSet;
    TagPickerAdapter tagAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tagViewModel = new ViewModelProvider(getActivity()).get(TagViewModel.class);
    }

    public TagPicker() {
        selectedTagSet = new HashSet<>();
    }

    public void setOnPickerConfirmClickListener(OnPickerConfirmClickListener<List<Tag>> onPickerConfirmClickListener) {
        this.onPickerConfirmClickListener = onPickerConfirmClickListener;
    }

    @Override
    protected DialogCommonPickerBinding getViewBinding(@NonNull LayoutInflater inflater) {
        return DialogCommonPickerBinding.inflate(inflater);
    }


    @Override
    protected void onBuildDialog(MaterialAlertDialogBuilder builder) {
        builder.setTitle(R.string.pick_tag).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (onPickerConfirmClickListener != null) {
                    //get tag list
                    List<Tag> tags = new ArrayList<Tag>(selectedTagSet);
                    onPickerConfirmClickListener.onClick(tags);
                    Log.d(TAG, "onClick: tagsSize" + tags.size());
                }
                dismiss();
            }
        });
    }

    @Override
    protected void onBindView(DialogCommonPickerBinding binding) {
        binding.descriptionContentTv.setText(R.string.pick_tag_desc);

        tagAdapter = new TagPickerAdapter(getActivity());
        tagAdapter.setEnableFooter(true);
        tagAdapter.setSelectedTagSet(selectedTagSet);

        tagAdapter.setOnFooterClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                TagAddFullDialog.newInstance().show(getParentFragmentManager(), TagAddFullDialog.TAG);
            }
        });
        tagAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                MaterialCheckBox checkBox = view.findViewById(R.id.tag_checkbox);
                checkBox.setChecked(!checkBox.isChecked());
            }
        });
        tagAdapter.setOnItemCheckedListener(new TagPickerAdapter.OnItemCheckedListener() {
            @Override
            public void onItemChecked(CompoundButton compoundButton, Object item, int position) {
//                Tag tag = (Tag) item;
//                if (compoundButton.isChecked()) {
//                    selectedTags.add(tag);
//                } else {
//                    selectedTags.remove(tag);
//                }
            }
        });
        binding.contentRecyclerView.setAdapter(tagAdapter);

        tagViewModel.getAllTags().observe(getActivity(), new Observer<List<Tag>>() {
            @Override
            public void onChanged(List<Tag> tags) {
                tagAdapter.updateItems(tags);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private class TagPickerAdapter extends TagAdapter {
        private Set<Tag> selectedTagSet;

        private OnItemCheckedListener onItemCheckedListener;

        public interface OnItemCheckedListener {
            void onItemChecked(CompoundButton checkBox, Object item, int position);
        }

        public void setOnItemCheckedListener(OnItemCheckedListener onItemCheckedListener) {
            this.onItemCheckedListener = onItemCheckedListener;
        }

        public TagPickerAdapter(Context mContext) {
            super(mContext);
            this.selectedTagSet = new HashSet<>();
        }

        public Set<Tag> getSelectedTagSet() {
            return selectedTagSet;
        }

        public void setSelectedTagSet(Set<Tag> selectedTagSet) {
            this.selectedTagSet = selectedTagSet;
        }

        @Override
        protected BaseEntityFooterVH onCreateFooterViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item1, parent, false);
            return new BaseEntityFooterVH(view);
        }

        @Override
        protected CheckableTagVH onCreateNormalViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag, parent, false);
            return new CheckableTagVH(view);
        }

        @Override
        protected void onBindNormalViewHolder(@NonNull TagVH holder, int position) {
            Tag tag = getItemByHolderPosition(position);
            holder.bind(mContext, tag);

            if (holder instanceof CheckableTagVH) {
                ((CheckableTagVH) holder).checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        if (onItemCheckedListener != null) {
//                            onItemCheckedListener.onItemChecked(buttonView, tag, holder.getAdapterPosition());
//                        }
                        //check change
                        if (isChecked) selectedTagSet.add(tag);
                        else selectedTagSet.remove(tag);
                    }
                });
                if (selectedTagSet.contains(tag)) {
                    ((CheckableTagVH) holder).checkBox.setChecked(true);
                } else {
                    ((CheckableTagVH) holder).checkBox.setChecked(false);
                }
            }

        }


        private class CheckableTagVH extends TagVH {
            private MaterialCheckBox checkBox;

            public CheckableTagVH(@NonNull View itemView) {
                super(itemView);
                checkBox = itemView.findViewById(R.id.tag_checkbox);
            }

        }
    }
}
