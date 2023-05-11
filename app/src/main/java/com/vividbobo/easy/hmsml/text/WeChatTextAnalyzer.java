package com.vividbobo.easy.hmsml.text;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.mlsdk.common.MLFrame;
import com.huawei.hms.mlsdk.text.MLText;
import com.vividbobo.easy.database.EasyDatabase;
import com.vividbobo.easy.database.dao.ConfigDao;
import com.vividbobo.easy.database.model.Account;
import com.vividbobo.easy.database.model.Bill;
import com.vividbobo.easy.database.model.Category;
import com.vividbobo.easy.database.model.Config;
import com.vividbobo.easy.database.model.Leger;
import com.vividbobo.easy.database.model.Role;
import com.vividbobo.easy.utils.AsyncProcessor;
import com.vividbobo.easy.utils.CalendarUtils;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                constructBillList(text.getBlocks());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                // 识别失败处理。
                e.printStackTrace();
            }
        });
    }

    private void constructBillList(List<MLText.Block> blocks) {
        AsyncProcessor.getInstance().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                EasyDatabase db = EasyDatabase.getDatabase(mContext);
                ConfigDao configDao = db.configDao();

                Bill bill = null;
                Integer year = null;
                for (int i = 0; i < blocks.size(); i++) {
                    MLText.Block infoBlock = blocks.get(i);
//            Log.d(TAG, "constructBillList: info contents: " + infoBlock.getContents());

                    if (infoBlock.getStringValue().isEmpty()) {
                        continue;
                    }
                    if (i == 0) {
                        //get China Year
                        try {
                            year = Integer.parseInt(infoBlock.getStringValue().substring(0, 4));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            year = LocalDate.now().getYear();
                        }
                    }

                    Log.d(TAG, "constructBillList: block at " + i);
                    Log.d(TAG, "constructBillList: " + infoBlock.getStringValue());
                    String[] infoParts = infoBlock.getStringValue().split("\n");
                    if (infoParts.length > 1) {
                        bill = Bill.create();
                        String payee;
                        if (infoParts[0].contains("转账-来自")) {
                            //自己收款
                            payee = "自己";
                        } else {
                            payee = infoParts[0].strip();
                        }
                        bill.setPayeeTitle(payee);
                        bill.setRemark(infoParts[0]);

                        String lastInfoPart = infoParts[infoParts.length - 1];
                        String[] timeParts = lastInfoPart.substring(lastInfoPart.length() - 5).split(":");
                        Integer hour = Integer.parseInt(timeParts[0]);
                        Integer minute = Integer.parseInt(timeParts[1]);
                        //set time
                        bill.setTime(LocalTime.of(hour, minute));

                        String dateStr = year + "年" + lastInfoPart.substring(0, lastInfoPart.length() - 5).strip();
                        Long dateLong = CalendarUtils.getDateFrom(dateStr, "yyyy年MM月dd日").getTime();
                        //set date
                        bill.setDate(new Date(dateLong));

                        // account
                        Account account = configDao.getRawAccountByType(Config.TYPE_IMPORT_ACCOUNT_WECHAT);
                        bill.setAccountId(account.getId());
                        bill.setAccountTitle(account.getTitle());
                        bill.setAccountIconResName(account.getIconResName());

                        //leger
                        Leger leger = configDao.getRawLegerByType(Config.TYPE_LEGER);
                        bill.setLegerTitle(leger.getTitle());
                        bill.setLegerId(leger.getId());

                        //role
                        Role role = configDao.getRawRoleByType(Config.TYPE_ROLE);
                        bill.setRoleTitle(role.getTitle());
                        bill.setRoleId(role.getId());


                    } else {
                        try {
                            if (Objects.isNull(bill)) {
                                continue;
                            }
                            Double amountD = Double.parseDouble(infoBlock.getStringValue().strip());
                            if (amountD < 0) {
                                bill.setBillType(Bill.EXPENDITURE);
                            } else {
                                bill.setBillType(Bill.INCOME);
                            }
                            bill.setAmount((long) (Math.abs(amountD) * 100));
                            // if you can predict by remark

                            Category category = null;
                            if (bill.getBillType() == Bill.EXPENDITURE) {
                                category = configDao.getRawCategoryByType(Config.TYPE_CATEGORY_EXPENDITURE);
                            } else {
                                category = configDao.getRawCategoryByType(Config.TYPE_CATEGORY_INCOME);
                            }
                            bill.setCategoryId(category.getId());
                            bill.setCategoryTitle(category.getTitle());
                            bill.setCategoryIconResName(category.getIconResName());


                            //add to list
                            addBill(bill);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        } finally {
                            bill = null;
                        }
                    }
                }
                return null;
            }
        }).addListener(new Runnable() {
            @Override
            public void run() {
                onTextAnalyzerSuccess.onSuccess(getBillList());
            }
        }, AsyncProcessor.getInstance().getExecutorService());
    }


}
