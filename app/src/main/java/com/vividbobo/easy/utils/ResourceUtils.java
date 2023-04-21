package com.vividbobo.easy.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

import androidx.annotation.ArrayRes;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;

/**
 * 用于全局获取resource
 */
public class ResourceUtils {
    private static Context mContext;

    public static void setContext(Context context) {
        mContext = context;
    }

    public static String[] getStringArray(@ArrayRes int stringId) {
        return mContext.getResources().getStringArray(stringId);
    }


    /**
     * @param context  about the lifecycle, context, activity, fragment, view...
     * @param drawable
     * @return RequestBuilder<Drawable>
     */
    public static RequestBuilder<Drawable> bindImageDrawable(Context context, Drawable drawable) {
        return Glide.with(context)
                .load(drawable)
                .error(new ColorDrawable(Color.RED));
    }

    public static int getResourceId(String resourceName, String defType) {
        try {
            return mContext.getResources().getIdentifier(resourceName, defType, mContext.getPackageName());
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static Drawable getDrawable(@DrawableRes int id) {
        return ContextCompat.getDrawable(mContext, id);
    }


    public static Drawable getDrawable(String resName) {
        int resId = getResourceId(resName, "drawable");
        if (resId == 0) {
            return null;
        }
        return getDrawable(resId);
    }

    //1

    /**
     * @param drawableResId
     * @param color         if you use the colorResId,then wrap with ContextCompat.getColor(mContext, colorResId);
     * @return
     */
    public static Drawable getTintedDrawable(@DrawableRes int drawableResId, int color) {
        // 加载 drawable
        Drawable drawable = getDrawable(drawableResId);
        if (drawable == null) {
            return null;
        }
        // 创建一个可变的副本
        Drawable wrappedDrawable = DrawableCompat.wrap(drawable).mutate();
        // 使用新的颜色值设置着色器
        DrawableCompat.setTint(wrappedDrawable, color);
        return wrappedDrawable;
    }

    //2

    /**
     * @param drawableResId
     * @param hexCodeColor  such as #ffffff or #aaffffff with alpha
     * @return
     */
    public static Drawable getTintedDrawable(@DrawableRes int drawableResId, String hexCodeColor) {
        // 加载 drawable
        Drawable drawable = getDrawable(drawableResId);
        if (drawable == null) {
            return null;
        }
        // 创建一个可变的副本
        Drawable wrappedDrawable = DrawableCompat.wrap(drawable).mutate();
        // 使用新的颜色值设置着色器
        int color = Color.parseColor(hexCodeColor);
        DrawableCompat.setTint(wrappedDrawable, color);
        return wrappedDrawable;
    }

    //3

    /**
     * @param resName
     * @param color   if you use the colorResId,then wrap with ContextCompat.getColor(mContext, colorResId);
     * @return
     */
    public static Drawable getTintedDrawable(String resName, int color) {
        return getTintedDrawable(getResourceId(resName, "drawable"), color);
    }

    /**
     * @param resName
     * @param hexCodeColor
     * @return
     */
    public static Drawable getTintedDrawable(String resName, String hexCodeColor) {
        return getTintedDrawable(getResourceId(resName, "drawable"), hexCodeColor);
    }

    public static String getString(@StringRes int id) {
        return mContext.getString(id);
    }

    public static int convertDpToPixel(int dp) {
        return dp * (mContext.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }


    public static int convertPixelsToDp(int px) {
        return px / (mContext.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static Drawable getTextImageIcon(String text, int textColor) {
        return getTextImageIcon(text, textColor, 80);
    }

    public static Drawable getTextImageIcon(String text, int textColor, int textSize) {
        if (text == null || text.isEmpty()) {
            return new TextDrawable("无", textColor, textSize);
        }
        String firstChar = String.valueOf(text.charAt(0));
        return new TextDrawable(firstChar, textColor, textSize);
    }

    public static class TextDrawable extends Drawable {
        private final Paint textPaint;
        private final String text;

        public TextDrawable(String text, int textColor) {
            this.text = text;

            textPaint = new Paint();
            textPaint.setColor(textColor);
            textPaint.setTextSize(80);
            textPaint.setTextAlign(Paint.Align.CENTER);
            textPaint.setAntiAlias(true);
        }

        public TextDrawable(String text, int textColor, int textSize) {
            this(text, textColor);
            textPaint.setTextSize(textSize);
        }


        @Override
        public void draw(@NonNull Canvas canvas) {
            Rect bounds = getBounds();
            int x = bounds.width() / 2;
            int y = (int) ((bounds.height() / 2) - ((textPaint.descent() + textPaint.ascent()) / 2));
            canvas.drawText(text, x, y, textPaint);
        }

        @Override
        public void setAlpha(int alpha) {
            textPaint.setAlpha(alpha);
        }

        @Override
        public void setColorFilter(@Nullable ColorFilter colorFilter) {
            textPaint.setColorFilter(colorFilter);
        }

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSLUCENT;
        }
    }

    public static int getColor(@ColorRes int colorResId) {
        return ContextCompat.getColor(mContext, colorResId);
    }

}
