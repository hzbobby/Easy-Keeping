package com.vividbobo.easy.googleml;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.nl.entityextraction.DateTimeEntity;
import com.google.mlkit.nl.entityextraction.Entity;
import com.google.mlkit.nl.entityextraction.EntityAnnotation;
import com.google.mlkit.nl.entityextraction.EntityExtraction;
import com.google.mlkit.nl.entityextraction.EntityExtractionParams;
import com.google.mlkit.nl.entityextraction.EntityExtractor;
import com.google.mlkit.nl.entityextraction.EntityExtractorOptions;
import com.google.mlkit.nl.entityextraction.MoneyEntity;
import com.vividbobo.easy.database.model.Bill;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class GoogleBillExtractor implements BillExtractor {
    public static final String KEY_DATE = "date";
    public static final String KEY_AMOUNT = "amount";
    private static final String TAG = "TextEntityExtractor";
    private EntityExtractor entityExtractor;

    private View.OnClickListener onAnalyzeDoneListener;

    public void setOnAnalyzeDoneListener(View.OnClickListener onAnalyzeDoneListener) {
        this.onAnalyzeDoneListener = onAnalyzeDoneListener;
    }


    public GoogleBillExtractor() {
        entityExtractor = EntityExtraction.getClient(new EntityExtractorOptions.Builder(EntityExtractorOptions.CHINESE).build());
    }


    public String preExtract(String text) {
        if (text.lastIndexOf("元") != -1) {
            text = replaceLastOccurrence(text, "元", "yuan");
        } else if (text.lastIndexOf("块") != -1) {
            text = replaceLastOccurrence(text, "块", "yuan");
        }
        if (text.contains("收入")) {
            billType = Bill.INCOME;
        }

        return text;
    }

    public static String replaceLastOccurrence(String str, String toReplace, String replacement) {
        int pos = str.lastIndexOf(toReplace);
        if (pos > -1) {
            return str.substring(0, pos) + replacement + str.substring(pos + toReplace.length(), str.length());
        } else {
            return str;
        }
    }

    public void extract(String text) {
        remark = text;
        String preExtractText = preExtract(text);
        entityExtractor.downloadModelIfNeeded().addOnSuccessListener(ignored -> {
            EntityExtractionParams params = new EntityExtractionParams.Builder(preExtractText).setPreferredLocale(Locale.CHINESE).build();
            entityExtractor.annotate(params).addOnSuccessListener(new OnSuccessListener<List<EntityAnnotation>>() {
                @Override
                public void onSuccess(List<EntityAnnotation> entityAnnotations) {

                    Log.d(TAG, "onSuccess: entityAnnotations size: " + entityAnnotations.size());

                    String date = null, amount = null;

                    for (EntityAnnotation entityAnnotation : entityAnnotations) {

                        List<Entity> entities = entityAnnotation.getEntities();

                        for (Entity entity : entities) {
                            Log.d(TAG, "onSuccess: entityType: " + entity.getType());
                            try {
                                switch (entity.getType()) {
                                    case Entity.TYPE_DATE_TIME:
                                        DateTimeEntity dateTimeEntity = entity.asDateTimeEntity();
                                        if (Objects.nonNull(dateTimeEntity)) {
//                                            Log.d(TAG, "Granularity: " + dateTimeEntity.getDateTimeGranularity());
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                                            date = sdf.format(dateTimeEntity.getTimestampMillis());
                                        }
                                        break;
                                    case Entity.TYPE_MONEY:
                                        MoneyEntity moneyEntity = entity.asMoneyEntity();
                                        if (Objects.nonNull(moneyEntity)) {
                                            amount = moneyEntity.getIntegerPart() + "." + moneyEntity.getFractionalPart();
                                        }
                                        break;
                                    default:
                                }

                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                    if (date != null && amount != null) {
                        success = true;
                        dateStr = date;
                        amountStr = amount;
                        Bundle bundle = new Bundle();
                        bundle.putString(KEY_DATE, dateStr);
                        bundle.putString(KEY_AMOUNT, amountStr);
                    } else {
                        success = false;
                        dateStr = null;
                        amountStr = null;
                    }
                    onAnalyzeDoneListener.onClick(null);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: ");
//                    Toast.makeText(getContext(), "提取失败，请检查网络环境", Toast.LENGTH_SHORT).show();
                    onAnalyzeDoneListener.onClick(null);
                }
            });

        }).addOnFailureListener(ignored -> {
            Log.d(TAG, "onClick: down failure");
//            Toast.makeText(getContext(), "提取失败，请检查网络环境", Toast.LENGTH_SHORT).show();
            onAnalyzeDoneListener.onClick(null);
        });


    }

    private Integer billType = Bill.EXPENDITURE;
    private String amountStr = null;
    private String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    private Boolean success = false;
    private String remark = null;

    @Override
    public Integer getBillType() {
        return billType;
    }

    @Override
    public String getAmount() {
        return amountStr;
    }

    @Override
    public String getDate() {
        return dateStr;
    }

    @Override
    public Boolean isSuccess() {
        return success;
    }

    @Override
    public String getRemark() {
        return remark;
    }
}
