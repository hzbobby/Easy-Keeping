package com.vividbobo.easy.googleml;

public interface BillExtractor {
    Integer getBillType();

    String getAmount();

    String getDate();

    Boolean isSuccess();

    String getRemark();
}
