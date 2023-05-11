package com.vividbobo.easy.accessibility.analyzer;

import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.vividbobo.easy.accessibility.ContentReaderService;

import java.util.Objects;

public class AliPayResultAnalyzer extends PayResultAnalyzer {
    private static final String TAG = "AliPayResultAnalyzer";
    private int textViewCount = 0;

    @Override
    public void analyze(AccessibilityNodeInfo nodeInfo) {
        textViewCount = 0;
        Log.d(TAG, "analyze: start_____________________________________");
        exploreNodeHierarchy(nodeInfo, 0);
        Log.d(TAG, "analyze: end__________________________________________");
    }

    public void exploreNodeHierarchy(final AccessibilityNodeInfo nodeInfo, final int depth) {
        if (nodeInfo == null) return;

        //Log.d(ContentReaderService.class.getName(), new String(new char[depth])
        // .replace("\0", "\t") + nodeInfo.toString());

        String className = nodeInfo.getClassName().toString();
        if (className.toLowerCase().contains("textview") && Objects.nonNull(nodeInfo.getText())) {
            Log.d(TAG, "exploreNodeHierarchy: textview count: " + textViewCount);
            Log.d(TAG, "exploreNodeHierarchy: nodeInfo:" + nodeInfo.toString());
            Log.d(TAG, "exploreNodeHierarchy: textView: " + nodeInfo.getText().toString());
            switch (textViewCount) {
                case 0:
                    // 成功
                    if (nodeInfo.getText().toString().contains("成功"))
                        setSuccess(true);
                    break;
                case 1:
                    //amount
                    Double amount = 0.0;
                    try {
                        amount = Double.parseDouble(nodeInfo.getText().toString().substring(1));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        setSuccess(false);
                        Log.d(TAG, "exploreNodeHierarchy: infoNode: " + nodeInfo.getText()
                                .toString());
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        setSuccess(false);
                    }
                    setAmount((long) (amount * 100));
                    break;
                case 3:
                    //payee
                    setPayee(nodeInfo.getText().toString());
                    break;
                case 5:
                    //
                    setRemark(nodeInfo.getText().toString());
                    break;
//                case 6:
////                    setRemark(getRemark() + nodeInfo.getText().toString());
//                    break;
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
