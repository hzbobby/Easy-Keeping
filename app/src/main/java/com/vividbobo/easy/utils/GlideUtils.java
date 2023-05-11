package com.vividbobo.easy.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.vividbobo.easy.database.model.Resource;

public class GlideUtils {
    public static RequestBuilder<Drawable> bindLegerCover(Context context, String resName, Resource.ResourceType resType) {
        if (resType.equals(Resource.ResourceType.USER_COVER)) {
            return Glide.with(context).load(resName);
        } else {
            return ResourceUtils.bindImageDrawable(context, ResourceUtils.getDrawable(resName));
        }
    }
}
