package com.vividbobo.easy.ui.tags;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;
import com.vividbobo.easy.R;
import com.vividbobo.easy.model.TagItem;
import com.vividbobo.easy.utils.ColorUtils;

/***
 * 添加标签的页面
 */
public class AddTagDialog extends AlertDialog {
    private static final String TAG = "AddTagDialog";

    public static AddTagDialog newInstance(Context context, TagItem tagItem) {
        AddTagDialog addTagDialog = new AddTagDialog(context);
        if (tagItem != null) {
            addTagDialog.setTagItem(tagItem);
        }
        return addTagDialog;
    }

    public AddTagDialog(@NonNull Context context) {
        super(context);
    }

    private TagItem tagItem;

    public void setTagItem(TagItem tagItem) {
        this.tagItem = tagItem;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //dialog default setting

        //setContent
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_add_tags_dialog, null, false);
        setContentView(view);

        TextInputLayout tagInputText = view.findViewById(R.id.add_tags_dialog_text_input_layout);
        ColorPickerView colorPickerView = view.findViewById(R.id.add_tags_dialog_color_picker); //轮盘颜色选择器
        TextView colorHexCodeText = view.findViewById(R.id.add_tags_dialog_color_hex_code); //hex数值
        TextView colorRGBCodeText = view.findViewById(R.id.add_tags_dialog_color_rgb);  //rgb数值
        MaterialCardView colorBlock = view.findViewById(R.id.add_tags_dialog_color_block);     //颜色块

        MaterialButton cancelBtn = view.findViewById(R.id.add_tags_dialog_cancel_btn);
        MaterialButton submitBtn = view.findViewById(R.id.add_tags_dialog_submit);

        //initial
        if (tagItem != null) {
            tagInputText.getEditText().setText(tagItem.getText());
            colorPickerView.setInitialColor(Color.parseColor(tagItem.getColor()));
        }


        //解决软键盘不弹出的问题
        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        colorPickerView.setColorListener(new ColorEnvelopeListener() {
            @Override
            public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                colorBlock.setCardForegroundColor(ColorStateList.valueOf(envelope.getColor()));
                colorHexCodeText.setText("#" + envelope.getHexCode().substring(2));
                colorRGBCodeText.setText(String.format("(%d,%d,%d)", envelope.getArgb()[0], envelope.getArgb()[1], envelope.getArgb()[2]));
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tag = tagInputText.getEditText().getText().toString();
                String color = colorHexCodeText.getText().toString();
                if (tag.isEmpty()) {
                    Toast.makeText(getContext(), "请输入标签名", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), tag + " " + color, Toast.LENGTH_SHORT).show();
                    //TODO save to database
                    dismiss();
                }

            }
        });

    }

}
