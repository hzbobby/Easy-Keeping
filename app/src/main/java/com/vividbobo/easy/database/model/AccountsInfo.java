package com.vividbobo.easy.database.model;

/**
 * 所有账户相关信息
 */
public class AccountsInfo {
    private Long netAssert; //净资产
    private Long totalAssert;   //总资产
    private Long totalDebt; //总负债

    public AccountsInfo() {
        netAssert = 0L;
        totalAssert = 0L;
        totalDebt = 0L;
    }

    public void addNetAssert(long amount) {
        netAssert += amount;
    }

    public void addTotalAssert(long amount) {
        totalAssert += amount;
    }

    public void addTotalDebt(long amount) {
        totalDebt += amount;
    }

    public Long getNetAssert() {
        return netAssert;
    }

    public void setNetAssert(Long netAssert) {
        this.netAssert = netAssert;
    }

    public Long getTotalAssert() {
        return totalAssert;
    }

    public void setTotalAssert(Long totalAssert) {
        this.totalAssert = totalAssert;
    }

    public Long getTotalDebt() {
        return totalDebt;
    }

    public void setTotalDebt(Long totalDebt) {
        this.totalDebt = totalDebt;
    }
}
