package com.vividbobo.easy.utils;

import android.util.Log;

public class ColorUtils {
    public static String getRandomColor() {
        StringBuilder sb = new StringBuilder("#");
        for (int i = 0; i < 6; i++) {
            int flag = (int) (Math.random() * 2);
            if (flag == 1) {
                //A-E
                sb.append((char)('A' + (int) (Math.random() * 6)));
            } else {
                //0-9
                sb.append((char)('0' + (int) (Math.random() * 10)));
            }
        }
        return sb.toString();
    }
}
