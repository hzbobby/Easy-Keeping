package com.vividbobo.easy.hmsml.text;


import android.content.Context;
import android.graphics.Bitmap;

import com.huawei.hms.mlsdk.MLAnalyzerFactory;
import com.huawei.hms.mlsdk.text.MLLocalTextSetting;
import com.huawei.hms.mlsdk.text.MLTextAnalyzer;
import com.vividbobo.easy.database.model.Bill;

import java.util.ArrayList;
import java.util.List;

public abstract class TextAnalyzer {
    public interface OnTextAnalyzerSuccess {
        void onSuccess(List<Bill> results);
    }
    public abstract void analyze(Bitmap bitmap);

    private MLLocalTextSetting setting = new MLLocalTextSetting.Factory().setOCRMode(MLLocalTextSetting.OCR_DETECT_MODE)
            // 设置识别语种。
            .setLanguage("zh").create();
    protected MLTextAnalyzer analyzer = MLAnalyzerFactory.getInstance().getLocalTextAnalyzer(setting);
    protected OnTextAnalyzerSuccess onTextAnalyzerSuccess;

    public void setOnTextAnalyzerSuccess(OnTextAnalyzerSuccess onTextAnalyzerSuccess) {
        this.onTextAnalyzerSuccess = onTextAnalyzerSuccess;
    }
    //使用自定义参数MLLocalTextSetting配置端侧文本分析器。


    protected Context mContext;
    private List<Bill> mBillList = new ArrayList<>();

    protected void addBill(Bill bill) {
        mBillList.add(bill);
    }

    protected List<Bill> getBillList() {
        return mBillList;
    }

    public TextAnalyzer(Context mContext) {
        this.mContext = mContext;
    }


}
