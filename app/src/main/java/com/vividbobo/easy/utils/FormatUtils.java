package com.vividbobo.easy.utils;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;

import com.vividbobo.easy.database.model.Tag;

import java.util.List;

public class FormatUtils {
    public static String getAmount(Long amount) {
        return String.format("%.2f", (double) amount / 100);
    }

    public static SpannableString getDeleteString(String str) {
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new StrikethroughSpan(), 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    public static SpannableStringBuilder getTagSpannableStringBuilder(List<Tag> tagList) {
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        int start = 0;
        int end;
        for (int i = 0; i < tagList.size(); i++) {
            Tag tag = tagList.get(i);
            ssb.append(String.format("#%s ", tag.getTitle()));
            end = ssb.length();
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor(tag.getHexCode()));
            ssb.setSpan(colorSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            start = end;
        }
        return ssb;
    }
}
