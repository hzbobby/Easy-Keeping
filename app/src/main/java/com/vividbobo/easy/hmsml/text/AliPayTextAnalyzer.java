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
import com.vividbobo.easy.database.dao.AccountDao;
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
import java.time.LocalTime;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;

public class AliPayTextAnalyzer extends TextAnalyzer {
    private static final String TAG = "AliPayTextAnalyzer";
    private static final String PAYEE_TRIP_HEAD = "扫收钱码付款绐";

    public AliPayTextAnalyzer(Context mContext) {
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
                List<MLText.Block> blocks = text.getBlocks();
                constructBillList(blocks);

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
                AccountDao accountDao = db.accountDao();
                ConfigDao configDao = db.configDao();

                Bill bill = null;

                for (int i = 0; i < blocks.size(); i++) {
                    MLText.Block infoBlock = blocks.get(i);
                    if (Objects.equals(infoBlock.getStringValue(), "w")) {
                        continue;
                    }
                    if (infoBlock.getStringValue().isEmpty()) {
                        continue;
                    }
//                    Log.d(TAG, "onSuccess: block " + i);
                    Log.d(TAG, "onSuccess: block: " + infoBlock.getStringValue());
                    String[] infos = infoBlock.getStringValue().split("\n");
//                    for (int j = 0; j < infos.length; j++) {
//                        Log.d(TAG, "onSuccess: split: " + infos[j]);
//                    }
                    Log.d(TAG, "call: infos.length: " + infos.length);
                    if (infos.length >= 3) {
                        //new bill
                        bill = Bill.create();

                        if (infos.length > 0) {
                            bill.setPayeeTitle(infos[0].replace(PAYEE_TRIP_HEAD, "").strip());
                        }
                        if (infos.length > 1) {
                            //remark
                            bill.setRemark(infos[1]);
                            // and predict category

                            //...


                        }
                        if (infos.length > 2) {
                            String time = infos[2].substring(infos[2].length() - 5).strip();
                            String[] timeParts = time.split(":");
                            Integer hours = Integer.parseInt(timeParts[0]);
                            Integer minutes = Integer.parseInt(timeParts[1]);
                            bill.setTime(LocalTime.of(hours, minutes)); //set time

                            //set date
                            Calendar calendar = Calendar.getInstance();
                            if (infos[2].contains("今天")) {
                                bill.setDate(new Date(calendar.getTime().getTime()));
                            } else if (infos[2].contains("昨天")) {
                                calendar.add(Calendar.DAY_OF_MONTH, -1);
                                bill.setDate(new Date(calendar.getTime().getTime()));
                            } else {
                                String date = infos[2].substring(0, infos[2].length() - 5).strip();
                                Long dateLong = CalendarUtils.getDateFrom(date, "MM-dd").getTime();
                                bill.setDate(new Date(dateLong));
                            }
                        }
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
                        //amount
                        if (Objects.isNull(bill)) {
                            continue;
                        }
                        try {
                            Double amountD = Double.parseDouble(infoBlock.getStringValue().strip());
                            //billType
                            if (amountD < 0) {
                                bill.setBillType(Bill.EXPENDITURE);

                            } else {
                                bill.setBillType(Bill.INCOME);
                            }
                            //amount
                            bill.setAmount((long) (Math.abs(amountD) * 100));

                            //predict category by remark
                            //...

                            // default category
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
//                Log.d(TAG, "run: success: " + getBillList().toString());
            }
        }, AsyncProcessor.getInstance().getExecutorService());

    }


}
