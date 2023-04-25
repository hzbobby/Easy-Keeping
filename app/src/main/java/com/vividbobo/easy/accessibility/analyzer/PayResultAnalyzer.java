package com.vividbobo.easy.accessibility.analyzer;

import android.view.accessibility.AccessibilityNodeInfo;

import com.vividbobo.easy.utils.FormatUtils;

/**
 * 支付抽象类
 */
public abstract class PayResultAnalyzer {
    private Long amount = 0L;
    private String payee = "";
    private String remark = "";
    private boolean success = false; //pay status

    /**
     * 分析页面上的component
     *
     * @param nodeInfo
     */
    public abstract void analyze(AccessibilityNodeInfo nodeInfo);

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String successInfo() {
        return "保存成功: 金额=" + FormatUtils.getAmount(getAmount()) + ", 收款方=" + getPayee() + ", 备注=" + getRemark();
    }

    @Override
    public String toString() {
        return "PayResultAnalyzer{" +
                "status='" + success + '\'' +
                ", amount='" + amount + '\'' +
                ", payee='" + payee + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
