package com.vividbobo.easy.ui.tags;

import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.vividbobo.easy.R;
import com.vividbobo.easy.database.model.Tag;
import com.vividbobo.easy.databinding.DialogCommonPickerBinding;
import com.vividbobo.easy.database.model.TagPresent;
import com.vividbobo.easy.ui.common.BaseMaterialDialog;
import com.vividbobo.easy.ui.common.BaseDialogPickerAdapter;
import com.vividbobo.easy.ui.common.CommonFooterViewHolder;
import com.vividbobo.easy.ui.common.CommonItemViewHolder;
import com.vividbobo.easy.ui.others.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class TagPicker extends BaseMaterialDialog<DialogCommonPickerBinding> {

    private OnPickerConfirmClickListener<List<Tag>> onPickerConfirmClickListener;

    public static final String TAG = "TagPicker";
    private List<Tag> selectedTags;

    public TagPicker() {
        selectedTags = new ArrayList<>();
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
        builder.setTitle(R.string.pick_tag).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        }).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (onPickerConfirmClickListener != null) {
                    onPickerConfirmClickListener.onClick(selectedTags);
                }
            }
        });
    }

    @Override
    protected void onBindView(DialogCommonPickerBinding binding) {
        binding.descriptionContentTv.setText(R.string.pick_tag_desc);

        TagPickerAdapter adapter = new TagPickerAdapter();
        adapter.setOnFooterClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add tags
                TagAddFullDialog.newInstance(null).show(getParentFragmentManager(), TagAddFullDialog.TAG);
            }
        });
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                TagPresent tagPresent = (TagPresent) item;
                if (tagPresent.isChecked()) selectedTags.add(tagPresent);
                else selectedTags.remove(tagPresent);
            }
        });

        binding.contentRecyclerView.setAdapter(adapter);
    }

    private class TagPickerAdapter extends BaseDialogPickerAdapter<TagPresent, TagPicker.TagPickerAdapter.TagViewHolder, RecyclerView.ViewHolder, CommonFooterViewHolder> {

        public TagPickerAdapter() {
            super();
            setEnableFooter(true);
        }

        @Override
        protected Integer getNormalLayoutRes() {
            return R.layout.item_common_with_checkbox;
        }

        @Override
        protected TagPicker.TagPickerAdapter.TagViewHolder getNormalViewHolder(View view) {
            return new TagViewHolder(view);
        }

        @Override
        protected CommonFooterViewHolder getFooterViewHolder(View view) {
            return new CommonFooterViewHolder(view);
        }


        @Override
        protected void onBindNormalViewHolder(@NonNull TagPicker.TagPickerAdapter.TagViewHolder holder, int position) {
            TagPresent tagPresent = getItem(getItemPosition(position));
            holder.bind(tagPresent);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tagPresent.setChecked(!tagPresent.isChecked());
                    holder.checkBox.setChecked(tagPresent.isChecked());
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, tagPresent, holder.getAdapterPosition());
                    }
                }
            });
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    tagPresent.setChecked(isChecked);
                }
            });
        }

        @Override
        protected void onBindFooterViewHolder(@NonNull CommonFooterViewHolder holder, int position) {
            Log.d(TAG, "onBindFooterViewHolder: ");
            holder.setTitle(R.string.add_tag);
        }

        public class TagViewHolder extends CommonItemViewHolder<TagPresent> {
            private MaterialCheckBox checkBox;

            public TagViewHolder(@NonNull View itemView) {
                super(itemView);
                this.checkBox = itemView.findViewById(R.id.item_check_box);

            }

            @Override
            public void bind(TagPresent item) {
                title.setText(item.getTitle());
            }
        }
    }

}
