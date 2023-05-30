package com.vividbobo.easy.hmsml.text;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;

import androidx.navigation.fragment.DialogFragmentNavigatorDestinationBuilder;

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
                Log.d(TAG, "onSuccess: " + text.getPlates());
                List<MLText.Block> blocks = text.getBlocks();
                parseBillList(blocks);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                // 识别失败处理。
                e.printStackTrace();
            }
        });
    }

    private void parseBillList(List<MLText.Block> blocks) {
        AsyncProcessor.getInstance().submit(new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                        EasyDatabase db = EasyDatabase.getDatabase(mContext);
                        ConfigDao configDao = db.configDao();

                        Bill bill = null;
                        Integer year = null;

                        int DEVIATION = 50;
                        int baseX = 240, baseY = 300;
//                        for (int i = 0; i < blocks.size(); i++) {
//                            MLText.Block infoBlock = blocks.get(i);
//                            // get base Year
//                            if (i == 0) {
//                                try {
//                                    year = Integer.parseInt(infoBlock.getStringValue().substring(0, 4).strip());
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                    year = LocalDate.now().getYear();
//                                }
//                                continue;
//                            }
//                            // text line
//                            for (int j = 0; j < infoBlock.getContents().size(); j++) {
//                                MLText.TextLine line = infoBlock.getContents().get(j);
//                                Rect lineBorder = line.getBorder();
//                                //最左边坐标偏差小于50，则认为是信息开头处
//                                if (Math.abs(lineBorder.left - baseX) <= DEVIATION){
//
//                                }
//                            }
//                        }


                        for (int i = 0; i < blocks.size(); i++) {
                            MLText.Block infoBlock = blocks.get(i);
                            Log.d("TEXT_ANALYZER", " \n" + infoBlock.getBorder());
                            Log.d("TEXT_ANALYZER", "block#" + i);
                            StringBuilder sb = new StringBuilder();
                            for (int j = 0; j < infoBlock.getContents().size(); j++) {
                                MLText.TextLine content = infoBlock.getContents().get(j);
                                StringBuilder sb2 = new StringBuilder();
                                for (int k = 0; k < content.getContents().size(); k++) {
                                    sb2.append("    " + content.getContents().get(k).getStringValue() + "\n");
                                }
                                Log.d("TEXT_ANALYZER", sb2.toString());
                                sb.append("line " + j + " :" + content.getStringValue() + "\n");
                                Log.d("TEXT_ANALYZER", content.getBorder().toString());
                                Log.d("TEXT_ANALYZER", content.getStringValue());
                            }
                            Log.d("TEXT_ANALYZER", " \n" + sb.toString());

                            if (Objects.equals(infoBlock.getStringValue(), "w")) {
                                continue;
                            }
                            if (infoBlock.getStringValue().isEmpty()) {
                                continue;
                            }

                            Log.d(TAG, "onSuccess: block " + i);
                            Log.d(TAG, "onSuccess: block: " + infoBlock.getStringValue());
                            if (i == 0) {
                                //year
                                try {
                                    year = Integer.parseInt(infoBlock.getStringValue().substring(0, 4).strip());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    year = LocalDate.now().getYear();
                                }
                            }

                            String[] infos = infoBlock.getStringValue().split("\n");
                            if (infos.length > 2) {
                                //new bill
                                bill = Bill.create();
                                int infosLength = infos.length;
                                // payee
                                if (infos[infosLength - 3].contains(PAYEE_TRIP_HEAD)) {
                                    bill.setPayeeTitle(infos[infosLength - 3]
                                            .replace(PAYEE_TRIP_HEAD, "").strip());
                                } else {
                                    bill.setPayeeTitle(infos[infosLength - 3].strip());
                                }
                                // remark
                                bill.setRemark(infos[infosLength - 2]);
                                // parse date and time
                                // ...


                                String time = infos[infosLength - 1].substring(infos[infosLength - 1].length() - 5).strip();
                                String[] timeParts = time.split(":");
                                Integer hours = Integer.parseInt(timeParts[0]);
                                Integer minutes = Integer.parseInt(timeParts[1]);
                                bill.setTime(LocalTime.of(hours, minutes)); //set time

                                //set date
                                Calendar calendar = Calendar.getInstance();
                                if (infos[infosLength - 1].contains("今天")) {
                                    bill.setDate(new Date(calendar.getTime().getTime()));
                                } else if (infos[infosLength - 1].contains("昨天")) {
                                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                                    bill.setDate(new Date(calendar.getTime().getTime()));
                                } else {
                                    String date = String.valueOf(year) + "-" + infos[infosLength - 1].substring(0, infos[infosLength - 1].length() - 5).strip();

                                    try {
                                        Long dateLong = CalendarUtils.getDateFrom(date, "yyyy-MM-dd").getTime();
                                        bill.setDate(new Date(dateLong));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        bill.setDate(new Date(calendar.getTime().getTime()));
                                    }
                                }
                                // account
                                Account account = configDao.getRawAccountByType(Config.TYPE_IMPORT_ACCOUNT_ALIPAY);
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
                                // else , amount text block
                                if (Objects.isNull(bill)) {
                                    // create the bill in infos block (1st block)
                                    continue;
                                }
                                try {
                                    String[] rightParts = infoBlock.getStringValue().split("\n");
                                    //amount
                                    Double amountD = Double.parseDouble(rightParts[0].strip());
                                    // set billType
                                    if (amountD < 0) {
                                        bill.setBillType(Bill.EXPENDITURE);

                                    } else {
                                        bill.setBillType(Bill.INCOME);
                                    }
                                    // set amount
                                    bill.setAmount((long) (Math.abs(amountD) * 100));

                                    // set other attrs and save bill.
                                    //...


                                    //predict category by remark
                                    //...
                                    if (rightParts.length > 1) {
                                        bill.setRemark(bill.getRemark() + " " + rightParts[1]);
                                    }

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
                }).

                addListener(new Runnable() {
                    @Override
                    public void run() {
                        onTextAnalyzerSuccess.onSuccess(getBillList());
//                Log.d(TAG, "run: success: " + getBillList().toString());
                    }
                }, AsyncProcessor.getInstance().

                        getExecutorService());

    }


}
