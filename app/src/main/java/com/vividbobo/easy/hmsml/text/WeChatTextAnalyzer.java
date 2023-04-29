package com.vividbobo.easy.hmsml.text;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.mlsdk.common.MLFrame;
import com.huawei.hms.mlsdk.text.MLText;

public class WeChatTextAnalyzer extends TextAnalyzer {
    private static final String TAG = "WeChatTextAnalyzer";

    public WeChatTextAnalyzer(Context mContext) {
        super(mContext);
    }


    @Override
    public void analyze(Bitmap bitmap) {
        MLFrame frame = MLFrame.fromBitmap(bitmap);
        Task<MLText> task = analyzer.asyncAnalyseFrame(frame);
        task.addOnSuccessListener(new OnSuccessListener<MLText>() {
            @Override
            public void onSuccess(MLText text) {
                // 识别成功处理。
                Log.d(TAG, "onSuccess: " + text.getStringValue());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                // 识别失败处理。
                e.printStackTrace();
            }
        });
    }


}
