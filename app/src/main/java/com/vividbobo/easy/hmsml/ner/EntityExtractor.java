package com.vividbobo.easy.hmsml.ner;

import android.util.Log;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hms.mlsdk.common.MLException;
import com.huawei.hms.mlsdk.entity.MLNerFactory;
import com.huawei.hms.mlsdk.entity.cloud.MLRemoteNer;
import com.huawei.hms.mlsdk.entity.cloud.MLRemoteNerSetting;
import com.huawei.hms.mlsdk.entity.cloud.bo.RemoteNerResultItem;

public class EntityExtractor {
    private static final String TAG = "EntityExtractor";
    // 使用自定义的参数配置创建语种检测器。
    private MLRemoteNer ner;

    public EntityExtractor() {
        MLRemoteNerSetting setting = new MLRemoteNerSetting.Factory()
                .setSourceLangCode("zh")// 只支持"zh"。
                .create();
        ner = MLNerFactory.getInstance().getRemoteNer(setting);

    }

    public void doNer(String inputText) {
        ner.asyncEntityExtract(inputText.trim()).addOnSuccessListener(new OnSuccessListener<RemoteNerResultItem[]>() {
            @Override
            public void onSuccess(RemoteNerResultItem[] remoteNerResults) {
                Log.d(TAG, "onSuccess: ");
                if (remoteNerResults != null) {
                    StringBuffer buffer = new StringBuffer();
                    for (RemoteNerResultItem item : remoteNerResults) {
                        buffer.append("[").append("entityWord：" + item.getEntity() + " entityType：" + item.getType() + " startSpan：" + item.getMention()).append("]").append(";").append("\n");
                    }
                    Log.d(TAG, "onSuccess: " + buffer.toString());
                } else {
                    Log.d(TAG, "onSuccess: The recognition result is empty.");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "onFailure: ");
                try {
                    MLException mlException = (MLException) e;
                    String errorMessage = mlException.getMessage();
                    Log.d(TAG, "onFailure: " + errorMessage.toString());
                } catch (Exception error) {
                    Log.d(TAG, "onFailure: " + error.getMessage());
                }
            }
        });
    }

    //进行文本实体抽取（错误码信息可参见：机器学习服务错误码）。
    public void extract(String input) {
        Log.d(TAG, "extract: inputText: " + input.toString());
        ner.asyncEntityExtract(input).addOnSuccessListener(new OnSuccessListener<RemoteNerResultItem[]>() {
            @Override
            public void onSuccess(RemoteNerResultItem[] remoteNerResults) {
                // 成功的处理逻辑。
                if (remoteNerResults != null) {
                    // 有识别结果
                    Log.d(TAG, "onSuccess: " + remoteNerResults.toString());
                } else {
                    //  识别结果为空
                    Log.d(TAG, "onSuccess: result is empty");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                // 识别失败,获取相关异常信息。
                e.printStackTrace();
                try {
                    MLException mlException = (MLException) e;
                    // 获取错误码，开发者可以对错误码进行处理，根据错误码进行差异化的页面提示。
                    int errorCode = mlException.getErrCode();
                    // 获取报错信息，开发者可以结合错误码，快速定位问题。
                    String errorMessage = mlException.getMessage();
                    Log.d(TAG, "onFailure: errorCode: " + errorCode);
                    Log.d(TAG, "onFailure: errorMessage: " + errorMessage);
                } catch (Exception error) {
                    // 转换错误处理。
                    error.printStackTrace();
                }
            }
        });
    }
}
