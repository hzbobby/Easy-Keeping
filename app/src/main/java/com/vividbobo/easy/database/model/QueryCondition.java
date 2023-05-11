package com.vividbobo.easy.database.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class QueryCondition {
    private Map<String, Set<Integer>> intSetMap;
    private Map<String, Long> longMap;
    private Map<String, Boolean> boolMap;
    private Map<String, String> stringMap;
    private Map<String, Object> objectMap;
    private Map<String, String[]> stringArrayMap;

    public Map<String, Object> getObjectMap() {
        return objectMap;
    }

    public Map<String, Set<Integer>> getIntSetMap() {
        return intSetMap;
    }

    public Map<String, Long> getLongMap() {
        return longMap;
    }

    public Map<String, Boolean> getBoolMap() {
        return boolMap;
    }

    public Map<String, String> getStringMap() {
        return stringMap;
    }

    public QueryCondition() {
        intSetMap = new HashMap<>();
        longMap = new HashMap<>();
        boolMap = new HashMap<>();
        stringMap = new HashMap<>();
        objectMap = new HashMap<>();
        stringArrayMap = new HashMap<>();
    }

    public Map<String, String[]> getStringArrayMap() {
        return stringArrayMap;
    }


    public static final String BILL_LEGER = "leger";
    public static final String BILL_ACCOUNT = "account";
    public static final String BILL_ACCOUNT_TYPE = "accountType";

    public static final String BILL_ROLE = "role";
    public static final String BILL_PAYEE = "payee";
    public static final String BILL_CATEGORY = "category";
    public static final String BILL_TAG = "tag";
    public static final String BILL_BILLTYPE = "billType";
    public static final String BILL_IMAGE = "image";
    public static final String BILL_REFUND = "refund";
    public static final String BILL_REIMBURSE = "reimburse";
    public static final String BILL_INCOME_EXPENDITURE = "inExp";
    public static final String BILL_BUDGET = "budget";
    public static final String BILL_MIN_AMOUNT = "minAmount";
    public static final String BILL_MAX_AMOUNT = "maxAmount";
    public static final String BILL_DATE_START = "dateStart";
    public static final String BILL_DATE_END = "dateEnd";
    public static final String BILL_REMARK = "remark";


    public static QueryCondition createBillCondition() {
        QueryCondition qc = new QueryCondition();
        qc.intSetMap.put(BILL_LEGER, new HashSet<>());
        qc.intSetMap.put(BILL_ACCOUNT, new HashSet<>());
        qc.intSetMap.put(BILL_ROLE, new HashSet<>());
        qc.intSetMap.put(BILL_PAYEE, new HashSet<>());
        qc.intSetMap.put(BILL_CATEGORY, new HashSet<>());
        qc.intSetMap.put(BILL_TAG, new HashSet<>());
        qc.intSetMap.put(BILL_BILLTYPE, new HashSet<>());

        return qc;
    }

    @Override
    public String toString() {
        return "QueryCondition{" +
                "intSetMap=" + intSetMap +
                ", longMap=" + longMap +
                ", boolMap=" + boolMap +
                ", stringMap=" + stringMap +
                ", objectMap=" + objectMap +
                '}';
    }
}
