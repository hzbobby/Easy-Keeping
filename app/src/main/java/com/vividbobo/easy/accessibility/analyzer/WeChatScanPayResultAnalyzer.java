package com.vividbobo.easy.accessibility.analyzer;

import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.vividbobo.easy.accessibility.ContentReaderService;

public class WeChatScanPayResultAnalyzer extends PayResultAnalyzer {
    private static final String TAG = "WeChatPayResultAnalyzer";
    private int textViewCount;

    @Override
    public void analyze(AccessibilityNodeInfo nodeInfo) {
        setSuccess(false);
        textViewCount = 0;
        exploreNodeHierarchy(nodeInfo, 0);
    }

    public void exploreNodeHierarchy(final AccessibilityNodeInfo nodeInfo, final int depth) {
        if (nodeInfo == null) return;

        Log.d(ContentReaderService.class.getName(), new String(new char[depth]).replace("\0", "\t") + nodeInfo.toString());

        String className = nodeInfo.getClassName().toString();
        if (className.toLowerCase().contains("textview")) {
            Log.d(TAG, "exploreNodeHierarchy: textview count: " + textViewCount);
            Log.d(TAG, "exploreNodeHierarchy: textView: " + nodeInfo.getText());
            switch (textViewCount) {
                case 0:
                    // 成功
                    if (nodeInfo.getText().toString().contains("成功"))
                        setSuccess(true);
                    break;
                case 1:
                    //payee
                    setPayee(nodeInfo.getText().toString());
                    break;
                case 2:
                    //amount
                    try {
                        setAmount((long) (Double.parseDouble(nodeInfo.getText().toString().substring(1)) * 100));
                    } catch (Exception e) {
                        e.printStackTrace();
                        setSuccess(false);
                    }
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
