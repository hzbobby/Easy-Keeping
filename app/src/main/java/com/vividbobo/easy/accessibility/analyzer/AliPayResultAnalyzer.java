package com.vividbobo.easy.accessibility.analyzer;

import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.vividbobo.easy.accessibility.ContentReaderService;

public class AliPayResultAnalyzer extends PayResultAnalyzer {
    private static final String TAG = "AliPayResultAnalyzer";
    private int textViewCount = 0;

    @Override
    public void analyze(AccessibilityNodeInfo nodeInfo) {
        textViewCount = 0;
        exploreNodeHierarchy(nodeInfo, 0);
    }

    public void exploreNodeHierarchy(final AccessibilityNodeInfo nodeInfo, final int depth) {
        if (nodeInfo == null) return;

        Log.d(ContentReaderService.class.getName(), new String(new char[depth]).replace("\0", "\t") + nodeInfo.toString());

        String className = nodeInfo.getClassName().toString();
        if (className.toLowerCase().contains("textview")) {
            Log.d(TAG, "exploreNodeHierarchy: textview count: " + textViewCount);
            switch (textViewCount) {
                case 0:
                    // 成功
                    setSuccess(true);
                    break;
                case 3:
                    //amount
                    Double amount = 0.0;
                    try {
                        amount = Double.parseDouble(nodeInfo.getText().toString());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        Log.d(TAG, "exploreNodeHierarchy: infoNode: " + nodeInfo.getText().toString());
                    }

                    setAmount((long) (amount * 100));
                    break;
                case 1:
                    //payee
                    setPayee(nodeInfo.getText().toString());
                    break;
                case 5:
                    //实体店通用红包
                    setRemark(nodeInfo.getText().toString());
                    break;
                case 6:
                    setRemark(getRemark() + nodeInfo.getText().toString());
                    break;
                default:
            }

            textViewCount++;
        }


        for (int i = 0; i < nodeInfo.getChildCount(); ++i) {
            exploreNodeHierarchy(nodeInfo.getChild(i), depth + 1);
        }
        nodeInfo.recycle();
    }
}
