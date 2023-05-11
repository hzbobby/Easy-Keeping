package com.vividbobo.easy.accessibility.analyzer;

import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.annotation.VisibleForTesting;

import com.vividbobo.easy.accessibility.ContentReaderService;

import java.text.ParseException;
import java.util.List;
import java.util.Objects;

public class WeChatTransitionResultAnalyzer extends PayResultAnalyzer {
    private static final String TAG = "WeChatTransitionResultA";
    private static final String RES_NAME_OF_AMOUNT = "com.tencent.mm:id/imi";
    private static final String RES_NAME_OF_PAYEE = "com.tencent.mm:id/imp";
    private static final String RES_NAME_OF_REMARK = "com.tencent.mm:id/imc";


    @Override
    public void analyze(AccessibilityNodeInfo nodeInfo) {
        exploreNodeHierarchy(nodeInfo, 0);
    }

    public void exploreNodeHierarchy(final AccessibilityNodeInfo nodeInfo, final int depth) {
        //Super important check! AccessibilityNodes can get invalidated at ANY time.
        if (nodeInfo == null) return;

        //Log the nodeINfo to string, with some tabs for visible parent/child relationships.
        Log.d(ContentReaderService.class.getName(), new String(new char[depth]).replace("\0", "\t") + nodeInfo.toString());
        //get the specify node info;
        if (Objects.equals(nodeInfo.getViewIdResourceName(), RES_NAME_OF_AMOUNT)) {

            String amountStr = nodeInfo.getText().toString();
            setAmount((long) (Double.parseDouble(amountStr.substring(1)) * 100));
        }
        if (Objects.equals(nodeInfo.getViewIdResourceName(), RES_NAME_OF_PAYEE)) {
            if (nodeInfo.getText().toString().contains("你已收款")) {
                setPayee("自己");
            } else {
                setPayee(nodeInfo.getText().toString());
            }
        }
        if (Objects.equals(nodeInfo.getViewIdResourceName(), RES_NAME_OF_REMARK)) {
            setRemark(nodeInfo.getText().toString());
        }

        for (int i = 0; i < nodeInfo.getChildCount(); ++i) {
            exploreNodeHierarchy(nodeInfo.getChild(i), depth + 1);
        }
        nodeInfo.recycle();
    }


}
