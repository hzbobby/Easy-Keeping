package com.vividbobo.easy.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.vividbobo.easy.accessibility.analyzer.AliPayResultAnalyzer;
import com.vividbobo.easy.accessibility.analyzer.PayResultAnalyzer;
import com.vividbobo.easy.accessibility.analyzer.WeChatTransitionResultAnalyzer;
import com.vividbobo.easy.database.EasyDatabase;
import com.vividbobo.easy.database.model.Account;
import com.vividbobo.easy.database.model.Bill;
import com.vividbobo.easy.database.model.Category;
import com.vividbobo.easy.database.model.Config;
import com.vividbobo.easy.database.model.Leger;
import com.vividbobo.easy.utils.AsyncProcessor;
import com.vividbobo.easy.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * read the view on screen
 */
public class ContentReaderService extends AccessibilityService {
    private static final String TAG = "ViewReaderService";
    //微信扫码支付
    private static final String CLASS_NAME_WECHAT_SCAN_PAYMENT = "com.tencent.mm.framework.app.UIPageFragmentActivity";
    //微信转账支付
    private static final String CLASS_NAME_WECHAT_TRANSITION_PAYMENT = "com.tencent.mm.plugin.remittance.ui.RemittanceDetailUI";
    //支付宝支付
    private static final String CLASS_NAME_ALIPAY_PAYMENT = "com.alipay.android.msp.ui.views.MspContainerActivity";


    private static final Map<String, PayResultAnalyzer> ANALYZERS = new HashMap<>() {{
        put(CLASS_NAME_WECHAT_SCAN_PAYMENT, new WeChatTransitionResultAnalyzer());
        put(CLASS_NAME_WECHAT_TRANSITION_PAYMENT, new WeChatTransitionResultAnalyzer());
        put(CLASS_NAME_ALIPAY_PAYMENT, new AliPayResultAnalyzer());
    }};

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        Log.d(TAG, "onAccessibilityEvent: packageName: " + event.getPackageName());
        Log.d(TAG, "onAccessibilityEvent: className: " + event.getClassName());
        String className = event.getClassName().toString();
        if (ANALYZERS.containsKey(className)) {
            PayResultAnalyzer analyzer = ANALYZERS.get(className);
            if (analyzer == null) {
                Log.d(TAG, "onAccessibilityEvent: analyzer is null");
                return;
            }
            AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
            if (nodeInfo == null) {
                Log.d(TAG, "onAccessibilityEvent: node info is null");
                return;
            }
            analyzer.analyze(nodeInfo);

            if (analyzer.isSuccess()) {
                // new bill
                Log.d(TAG, "onAccessibilityEvent, analyze result: " + analyzer.toString());
                save(analyzer);
            }
        }
    }

    private void save(PayResultAnalyzer analyzer) {
        EasyDatabase db = EasyDatabase.getDatabase(getApplicationContext());

        AsyncProcessor.getInstance().submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                Bill bill = null;
                if (Objects.equals(analyzer.getPayee(), "自己")) {
                    bill = Bill.create(Bill.INCOME);
                } else {
                    bill = Bill.create(Bill.EXPENDITURE);
                }
                bill.setAmount(analyzer.getAmount());
                // need create new payee? or just set the title
                bill.setPayeeTitle(analyzer.getPayee());
                bill.setRemark(analyzer.getRemark());

                // save leger,category,account

                // getConfig Leger
                Integer legerId = db.configDao().getRawSelectedIdByType(Config.TYPE_AUTO_BILLING_LEGER);
                Leger leger = db.legerDao().getRawLegerById(legerId);  //need to set default auto billing leger
                bill.setLegerId(leger.getId());
                bill.setLegerTitle(leger.getTitle());

                Integer categoryId = null;
                if (bill.getBillType() == Bill.EXPENDITURE) {
                    categoryId = db.configDao().getRawSelectedIdByType(Config.TYPE_AUTO_BILLING_CATEGORY_EXPENDITURE);
                } else {
                    categoryId = db.configDao().getRawSelectedIdByType(Config.TYPE_AUTO_BILLING_CATEGORY_INCOME);
                }
                Category category = db.categoryDao().getRawCategoryById(categoryId);
                bill.setCategoryId(category.getId());
                bill.setCategoryTitle(category.getTitle());
                bill.setCategoryIconResName(category.getIconResName());

                Integer accountId = null;
                if (analyzer instanceof AliPayResultAnalyzer) {
                    accountId = db.configDao().getRawSelectedIdByType(Config.TYPE_AUTO_BILLING_ACCOUNT_ALIPAY);
                } else {
                    accountId = db.configDao().getRawSelectedIdByType(Config.TYPE_AUTO_BILLING_ACCOUNT_WECHAT);
                }
                Account account = db.accountDao().getRawAccountById(accountId);    //wechat account id
                bill.setAccountId(account.getId());
                bill.setAccountTitle(account.getTitle());
                bill.setAccountIconResName(account.getIconResName());

                db.billDao().insert(bill);
                Toast.makeText(getApplicationContext(), analyzer.successInfo(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "call: save bill: " + bill.toString());
                return true;
            }
        });
    }


    @Override
    public void onInterrupt() {
        Log.d(TAG, "onInterrupt: ");
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.d(TAG, "onServiceConnected: ");
    }

}
