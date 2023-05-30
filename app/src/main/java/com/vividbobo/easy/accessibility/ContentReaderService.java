package com.vividbobo.easy.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.vividbobo.easy.accessibility.analyzer.AliPayResultAnalyzer;
import com.vividbobo.easy.accessibility.analyzer.PayResultAnalyzer;
import com.vividbobo.easy.accessibility.analyzer.WeChatScanPayResultAnalyzer;
import com.vividbobo.easy.accessibility.analyzer.WeChatTransitionResultAnalyzer;
import com.vividbobo.easy.database.EasyDatabase;
import com.vividbobo.easy.database.model.Account;
import com.vividbobo.easy.database.model.Bill;
import com.vividbobo.easy.database.model.Category;
import com.vividbobo.easy.database.model.Config;
import com.vividbobo.easy.database.model.Leger;
import com.vividbobo.easy.database.model.Role;
import com.vividbobo.easy.ui.EmptyActivity;
import com.vividbobo.easy.utils.AsyncProcessor;
import com.vividbobo.easy.utils.LogWatcher;
import com.vividbobo.easy.utils.ToastUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        put(CLASS_NAME_WECHAT_SCAN_PAYMENT, new WeChatScanPayResultAnalyzer());
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
                Log.d(TAG, "onAccessibilityEvent: analyze is success: " + analyzer.isSuccess());
                Log.d(TAG, "onAccessibilityEvent, analyze result: " + analyzer.toString());
                saveBill(analyzer);
            }
        }
    }

    @Override
    public void onInterrupt() {

    }

    private void saveBill(PayResultAnalyzer analyzer) {
        EasyDatabase db = EasyDatabase.getDatabase(getApplicationContext());
        ListenableFuture<Bill> createBillFuture = AsyncProcessor.getInstance().submit(new Callable<Bill>() {
            @Override
            public Bill call() throws Exception {
//                Bill bill=null;
                Integer billType = null;
                if (Objects.equals(analyzer.getPayee(), "自己")) {
//                    bill = Bill.create(Bill.INCOME);
                    billType = Bill.INCOME;
                } else {
//                    bill = Bill.create(Bill.EXPENDITURE);
                    billType = Bill.EXPENDITURE;
                }
//                bill.setAmount(analyzer.getAmount());
//                // need create new payee? or just set the title
//                bill.setPayeeTitle(analyzer.getPayee());
//                bill.setRemark(analyzer.getRemark());

                // save leger,category,account

                // getConfig Leger
                Integer legerId = db.configDao().getRawSelectedIdByType(Config.TYPE_AUTO_BILLING_LEGER);
                Leger leger = db.legerDao().getRawLegerById(legerId);  //need to set default auto billing leger
//                bill.setLegerId(leger.getId());
//                bill.setLegerTitle(leger.getTitle());
                Bill bill = Bill.createBill(billType, analyzer.getAmount(), leger);
                bill.setPayeeTitle(analyzer.getPayee());
                bill.setRemark(analyzer.getRemark());

//                Integer categoryId = null;
//                if (bill.getBillType() == Bill.EXPENDITURE) {
//                    categoryId = db.configDao().getRawSelectedIdByType(Config.TYPE_CATEGORY_EXPENDITURE);
//                } else {
//                    categoryId = db.configDao().getRawSelectedIdByType(Config.TYPE_CATEGORY_INCOME);
//                }
//                Category category = db.categoryDao().getRawCategoryById(categoryId);
//                bill.setCategoryId(category.getId());
//                bill.setCategoryTitle(category.getTitle());
//                bill.setCategoryIconResName(category.getIconResName());

                Integer accountId = null;
                if (analyzer instanceof AliPayResultAnalyzer) {
                    accountId = db.configDao().getRawSelectedIdByType(Config.TYPE_AUTO_BILLING_ACCOUNT_ALIPAY);
                } else {
                    accountId = db.configDao().getRawSelectedIdByType(Config.TYPE_AUTO_BILLING_ACCOUNT_WECHAT);
                }
                Account account = db.accountDao().getRawAccountById(accountId);    //wechat account id
                bill.setAccount(account);
//                bill.setAccountId(account.getId());
//                bill.setAccountTitle(account.getTitle());
//                bill.setAccountIconResName(account.getIconResName());
//                bill.setCurrencyCode(account.getCurrencyCode());

                Role role = db.configDao().getRawRoleByType(Config.TYPE_ROLE);
                bill.setRole(role);
//                db.billDao().insert(bill);
                bill.initDateTime();

                return bill;
            }
        });

        Futures.addCallback(createBillFuture, new FutureCallback<Bill>() {
            @Override
            public void onSuccess(Bill result) {
                Intent intent = new Intent(getApplicationContext(), EmptyActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                intent.putExtra(EmptyActivity.KEY_BILL, result);
                getApplicationContext().startActivity(intent);
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        }, AsyncProcessor.getInstance().getExecutorService());
    }


    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.d(TAG, "onServiceConnected: ");
        Toast.makeText(getApplicationContext(), "自动记账已开启", Toast.LENGTH_SHORT).show();
        LogWatcher.getInstance().init(getApplicationContext(), "logcat").startWatch();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogWatcher.getInstance().stopWatch();
    }
}
