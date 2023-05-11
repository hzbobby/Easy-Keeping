package com.vividbobo.easy.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.os.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 图片工具类
 */
public class ImageUtils {
    /**
     * 保存图片到应用私有文件夹
     * @param context
     * @param imageUri
     * @param fileName
     * @return
     */
    public static String saveImageToAppPrivateFolder(Context context, Uri imageUri, String fileName,String parentDir) {
        InputStream inputStream;
        try {
            inputStream = context.getContentResolver().openInputStream(imageUri);
            File directory = context.getExternalFilesDir(parentDir);
//            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date(System.currentTimeMillis()));
//            String imageFileName = "IMG_" + timeStamp + ".jpg";
            String imageFileName = fileName + ".jpg";
            File destinationFile = new File(directory, imageFileName);

            if (inputStream != null) {
                FileUtils.copy(inputStream, new FileOutputStream(destinationFile));
                return destinationFile.getAbsolutePath();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
