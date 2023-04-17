package com.vividbobo.easy.ui.tags;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;
import com.vividbobo.easy.R;
import com.vividbobo.easy.database.model.Tag;
import com.vividbobo.easy.databinding.DialogAddTagsBinding;
import com.vividbobo.easy.database.model.TagPresent;
import com.vividbobo.easy.ui.common.BaseFullScreenMaterialDialog;
import com.vividbobo.easy.utils.ToastUtil;
import com.vividbobo.easy.viewmodel.TagViewModel;

public class TagAddFullDialog extends BaseFullScreenMaterialDialog<DialogAddTagsBinding> {

    public static final String TAG = "AddTagFullDialog";
    public static final String ARGS_ITEM = "args_item";

    private TagViewModel tagViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tagViewModel = new ViewModelProvider(getActivity()).get(TagViewModel.class);
    }

    public static TagAddFullDialog newInstance(Tag tag) {
        Bundle args = new Bundle();
        args.putSerializable(ARGS_ITEM, tag);
        TagAddFullDialog fragment = new TagAddFullDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public static TagAddFullDialog newInstance() {
        Bundle args = new Bundle();
        TagAddFullDialog fragment = new TagAddFullDialog();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected DialogAddTagsBinding getViewBinding(@NonNull LayoutInflater inflater) {
        return DialogAddTagsBinding.inflate(inflater);
    }

    @Override
    protected void onViewBinding(DialogAddTagsBinding binding) {
        binding.toolBarLayout.layoutToolBarTitleTv.setText(R.string.add_tag);
        binding.toolBarLayout.layoutToolBar.inflateMenu(R.menu.confirm);

        Tag tag = (Tag) getArguments().getSerializable(ARGS_ITEM);
        if (tag != null) {
            //edit init

            binding.toolBarLayout.layoutToolBarTitleTv.setText("修改标签");
            binding.tagTitleTxl.getEditText().setText(tag.getTitle());
            binding.tagColorPicker.setInitialColor(Color.parseColor(tag.getHexCode()));
        }


        binding.toolBarLayout.layoutToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        binding.toolBarLayout.layoutToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.confirm) {
                    String title = binding.tagTitleTxl.getEditText().getText().toString();
                    String hexCode =
                            String.format("#%-6s", binding.tagHexCodeTxl.getEditText().getText().toString().replace(" ", "0"));

                    if (tag != null) {
                        //update
                        tag.setTitle(title);
                        tag.setHexCode(hexCode);
                        update(tag);
                    } else {
                        //save
                        save(new Tag(title, hexCode));
                    }
                    ToastUtil.makeToast(title + hexCode);
                    dismiss();
                    return true;
                }
                return false;
            }
        });


        binding.tagColorPicker.setColorListener(new ColorEnvelopeListener() {
            @Override
            public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                binding.tagAlphaTileView.setBackgroundColor(envelope.getColor());

                binding.tagHexCodeTxl.getEditText().setText(envelope.getHexCode().substring(2));
                binding.tagRgbCodeRTxl.getEditText().setText(String.valueOf(envelope.getArgb()[1]));
                binding.tagRgbCodeGTxl.getEditText().setText(String.valueOf(envelope.getArgb()[2]));
                binding.tagRgbCodeBTxl.getEditText().setText(String.valueOf(envelope.getArgb()[3]));

            }
        });
        binding.tagHexCodeTxl.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                    //避免hexCode输入不足，发生错误
                    String hexCode = String.format("#%-6s", textView.getText().toString()).replace(" ", "0");
                    Log.d(TAG, "onEditorAction: hexCode: " + hexCode);
                    try {
                        binding.tagColorPicker.selectByHsvColor(Color.parseColor(hexCode));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                    binding.tagHexCodeTxl.clearFocus();
                    return true;
                }
                return false;
            }
        });
        binding.tagRgbCodeBTxl.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                    int red = Integer.valueOf(binding.tagRgbCodeRTxl.getEditText().getText().toString());
                    int green = Integer.valueOf(binding.tagRgbCodeGTxl.getEditText().getText().toString());
                    int blue = Integer.valueOf(binding.tagRgbCodeBTxl.getEditText().getText().toString());
                    if (red < 0) red = 0;
                    if (red > 255) red = 255;
                    if (green < 0) green = 0;
                    if (green > 255) green = 255;
                    if (blue < 0) blue = 0;
                    if (blue > 255) blue = 255;
                    try {
                        binding.tagColorPicker.selectByHsvColor(Color.rgb(red, green, blue));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                    return true;
                }
                return false;
            }
        });

        //slide bar必须依附color picker
        binding.tagColorPicker.attachBrightnessSlider(binding.tagBrightnessSlideBar);


    }

    private void update(Tag tag) {
        tagViewModel.update(tag);
    }

    private void save(Tag tag) {
        tagViewModel.insert(tag);
    }

}
