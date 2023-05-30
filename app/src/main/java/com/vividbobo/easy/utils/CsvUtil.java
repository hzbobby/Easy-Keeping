package com.vividbobo.easy.utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.OpenableColumns;
import android.util.Log;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvBeanIntrospectionException;
import com.opencsv.exceptions.CsvChainedException;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvFieldAssignmentException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.vividbobo.easy.database.converter.Converters;
import com.vividbobo.easy.database.converter.EnumConverter;
import com.vividbobo.easy.database.converter.LocalTimeConverter;
import com.vividbobo.easy.database.model.Account;
import com.vividbobo.easy.database.model.Bill;
import com.vividbobo.easy.database.model.Category;
import com.vividbobo.easy.database.model.Leger;
import com.vividbobo.easy.database.model.Payee;
import com.vividbobo.easy.database.model.Role;
import com.vividbobo.easy.database.model.Tag;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 导出为CSV工具类
 */
public class CsvUtil {
    private static Map<String, HeaderColumnNameMappingStrategy> strategyMap;


    private static String toIntString(Integer intValue) {
        if (Objects.isNull(intValue)) {
            return "";
        }
        return String.valueOf(intValue);
    }

    private static Integer toInteger(String intStr) {
        if (intStr.isBlank()) return null;
        return Integer.parseInt(intStr);
    }

    private static String toLongString(Long longValue) {
        if (Objects.isNull(longValue)) {
            return "";
        }
        return String.valueOf(longValue);
    }

    private static Long toLong(String longStr) {
        if (longStr.isBlank()) return null;
        return Long.parseLong(longStr);
    }

    private static String toBooleanString(Boolean boolValue) {
        if (Objects.isNull(boolValue)) {
            return "";
        }
        return String.valueOf(boolValue);
    }

    private static Boolean toBoolean(String boolStr) {
        if (boolStr.isBlank()) return null;
        return Boolean.parseBoolean(boolStr);
    }

    private static String toString(String str) {
        if (str.isBlank()) return null;
        return str;
    }

    static {
        HeaderColumnNameMappingStrategy<Bill> billStrategy = new HeaderColumnNameMappingStrategy<Bill>() {
            @Override
            public Bill populateNewBean(String[] line) throws CsvBeanIntrospectionException, CsvFieldAssignmentException, CsvChainedException {
                Bill bill = new Bill();
                bill.setId(toLong(line[0]));
                bill.setAmount(toLong(line[1]));
                bill.setBillType(toInteger(line[2]));
                bill.setCategoryId(toInteger(line[3]));
                bill.setCategoryTitle(CsvUtil.toString(line[4]));
                bill.setCategoryIconResName(CsvUtil.toString(line[5]));

                bill.setLegerId(toInteger(line[6]));
                bill.setLegerTitle(CsvUtil.toString(line[7]));
                bill.setRoleId(toInteger(line[8]));
                bill.setRoleTitle(CsvUtil.toString(line[9]));
                bill.setAccountId(toInteger(line[10]));
                bill.setAccountTitle(CsvUtil.toString(line[11]));
                bill.setAccountIconResName(CsvUtil.toString(line[12]));

                bill.setPayeeId(toInteger(line[13]));
                bill.setPayeeTitle(CsvUtil.toString(line[14]));
                bill.setTarAccountId(toInteger(line[15]));
                bill.setTarAccountTitle(CsvUtil.toString(line[16]));
                bill.setTarAccountIconResName(CsvUtil.toString(line[17]));
                bill.setCurrencyCode(CsvUtil.toString(line[18]));
                bill.setTags(Converters.fromString(line[19]));

                List<String> imagePathList = Converters.fromStringToStringList(line[20]);
                Log.d("TAG", "populateNewBean: " + imagePathList.size());
                if (Objects.nonNull(imagePathList) && !imagePathList.isEmpty())
                    bill.setImagePaths(imagePathList);
                bill.setRemark(CsvUtil.toString(line[21]));
                bill.setRefund(toBoolean(line[22]));
                bill.setReimburse(toBoolean(line[23]));
                bill.setIncomeExpenditureIncluded(toBoolean(line[24]));
                bill.setBudgetIncluded(toBoolean(line[25]));
                bill.setDate(Converters.fromDateStringToDate(line[26]));
                bill.setTime(LocalTimeConverter.fromString(line[27]));
                bill.setUploaded(toBoolean(line[28]));
                bill.setCreateTime(Converters.toTimestamp(line[29]));

                return bill;
            }

            @Override
            public String[] generateHeader(Bill bean) throws CsvRequiredFieldEmptyException {
                return new String[]{"Id", "金额", "账单种类", "类别Id", "类别名称", "类别图标", "账本Id", "账本名称", "角色Id", "角色名称", "账户Id", "账户名称", "账户图标", "收款方Id", "收款方名称", "转入账户Id", "转入账户名称", "转入账户图标", "货币类型", "标签", "图片路径", "备注", "是否退款", "是否报销", "是否计入收支", "是否计入预算", "账单日期", "账单时间", "是否上传", "创建时间"};
            }

            @Override
            public String[] transmuteBean(Bill bean) throws CsvFieldAssignmentException, CsvChainedException {

                String[] values = new String[]{toLongString(bean.getId()), toLongString(bean.getAmount()), toIntString(bean.getBillType()), toIntString(bean.getCategoryId()), bean.getCategoryTitle(), bean.getCategoryIconResName(), toIntString(bean.getLegerId()), bean.getLegerTitle(), toIntString(bean.getRoleId()), bean.getRoleTitle(), toIntString(bean.getAccountId()), bean.getAccountTitle(), bean.getAccountIconResName(), toIntString(bean.getPayeeId()), bean.getPayeeTitle(), toIntString(bean.getTarAccountId()), bean.getTarAccountTitle(), bean.getTarAccountIconResName(), bean.getCurrencyCode(), Converters.fromTagList(bean.getTags()), Converters.fromStringListToString(bean.getImagePaths()), bean.getRemark(), toBooleanString(bean.getRefund()), toBooleanString(bean.getReimburse()), toBooleanString(bean.getIncomeExpenditureIncluded()), toBooleanString(bean.getBudgetIncluded()), Converters.fromDateToDateText(bean.getDate()), LocalTimeConverter.localTimeToString(bean.getTime()), toBooleanString(bean.isUploaded()), Converters.toTimestampString(bean.getCreateTime())};
                return values;
            }


        };
        billStrategy.setType(Bill.class);

        HeaderColumnNameMappingStrategy<Leger> legerStrategy = new HeaderColumnNameMappingStrategy<Leger>() {
            @Override
            public String[] generateHeader(Leger bean) throws CsvRequiredFieldEmptyException {
                return new String[]{"Id", "名称", "描述", "封面", "封面类型", "是否上传", "创建时间"};
            }

            @Override
            public Leger populateNewBean(String[] line) throws CsvBeanIntrospectionException, CsvFieldAssignmentException, CsvChainedException {
                Leger leger = new Leger();
                leger.setId(toInteger(line[0]));
                leger.setTitle(line[1]);
                leger.setDesc(line[2]);
                leger.setCoverPath(line[3]);
                leger.setCoverType(EnumConverter.toResourceType(toInteger(line[4])));
                leger.setUploaded(toBoolean(line[5]));
                leger.setCreateTime(Converters.toTimestamp(line[6]));

                return leger;
            }

            @Override
            public String[] transmuteBean(Leger bean) throws CsvFieldAssignmentException, CsvChainedException {
                return new String[]{toIntString(bean.getId()), bean.getTitle(), bean.getDesc(), bean.getCoverPath(), toIntString(EnumConverter.toOrdinal(bean.getCoverType())), toBooleanString(bean.isUploaded()), Converters.toTimestampString(bean.getCreateTime())};
            }
        };
        legerStrategy.setType(Leger.class);

        HeaderColumnNameMappingStrategy<Account> accountStrategy = new HeaderColumnNameMappingStrategy<Account>() {
            @Override
            public String[] generateHeader(Account bean) throws CsvRequiredFieldEmptyException {
                return new String[]{"Id", "名称", "描述", "图标Id", "图标", "币种", "余额", "账户类型Id", "账户类型名称", "账户类型图标", "卡号", "备注", "是否上传", "创建时间"};
            }

            @Override
            public Account populateNewBean(String[] line) throws CsvBeanIntrospectionException, CsvFieldAssignmentException, CsvChainedException {
                Account account = new Account();
                account.setId(toInteger(line[0]));
                account.setTitle(line[1]);
                account.setDesc(line[2]);
                account.setIconResId(toInteger(line[3]));
                account.setIconResName(line[4]);
                account.setCurrencyCode(line[5]);
                account.setBalance(toLong(line[6]));
                account.setAccountTypeId(toInteger(line[7]));
                account.setAccountTypeTitle(line[8]);
                account.setAccountTypeIconResName(line[9]);
                account.setCardNum(line[10]);
                account.setRemark(line[11]);
                account.setUploaded(toBoolean(line[12]));
                account.setCreateTime(Converters.toTimestamp(line[13]));

                return account;
            }

            @Override
            public String[] transmuteBean(Account bean) throws CsvFieldAssignmentException, CsvChainedException {
                return new String[]{toIntString(bean.getId()), bean.getTitle(), bean.getDesc(), toIntString(bean.getIconResId()), bean.getIconResName(), bean.getCurrencyCode(), toLongString(bean.getBalance()), toIntString(bean.getAccountTypeId()), bean.getAccountTypeTitle(), bean.getAccountTypeIconResName(), bean.getCardNum(), bean.getRemark(), toBooleanString(bean.isUploaded()), Converters.toTimestampString(bean.getCreateTime())};
            }
        };
        accountStrategy.setType(Account.class);

        HeaderColumnNameMappingStrategy<Category> categoryStrategy = new HeaderColumnNameMappingStrategy<Category>() {
            @Override
            public String[] generateHeader(Category bean) throws CsvRequiredFieldEmptyException {
                return new String[]{"Id", "名称", "图标", "父Id", "层级", "类型", "是否上传", "创建时间"};
            }

            @Override
            public String[] transmuteBean(Category bean) throws CsvFieldAssignmentException, CsvChainedException {
                return new String[]{toIntString(bean.getId()), bean.getTitle(), bean.getIconResName(), toIntString(bean.getParentId()), toIntString(bean.getOrderNum()), toIntString(bean.getType()), toBooleanString(bean.isUploaded()), Converters.toTimestampString(bean.getCreateTime())};
            }

            @Override
            public Category populateNewBean(String[] line) throws CsvBeanIntrospectionException, CsvFieldAssignmentException, CsvChainedException {
                Category category = new Category();
                category.setId(toInteger(line[0]));
                category.setTitle(line[1]);
                category.setIconResName(line[2]);
                category.setParentId(toInteger(line[3]));
                category.setOrderNum(toInteger(line[4]));
                category.setType(toInteger(line[5]));
                category.setUploaded(toBoolean(line[6]));
                category.setCreateTime(Converters.toTimestamp(line[7]));

                return category;
            }
        };
        categoryStrategy.setType(Category.class);

        HeaderColumnNameMappingStrategy<Tag> tagStrategy = new HeaderColumnNameMappingStrategy<Tag>() {
            @Override
            public String[] generateHeader(Tag bean) throws CsvRequiredFieldEmptyException {
                return new String[]{"Id", "名称", "颜色", "是否上传", "创建时间"};
            }

            @Override
            public String[] transmuteBean(Tag bean) throws CsvFieldAssignmentException, CsvChainedException {
                return new String[]{toIntString(bean.getId()), bean.getTitle(), bean.getHexCode(), toBooleanString(bean.isUploaded()), Converters.toTimestampString(bean.getCreateTime())};
            }

            @Override
            public Tag populateNewBean(String[] line) throws CsvBeanIntrospectionException, CsvFieldAssignmentException, CsvChainedException {
                Tag tag = new Tag();
                tag.setId(toInteger(line[0]));
                tag.setTitle(line[1]);
                tag.setHexCode(line[2]);
                tag.setUploaded(toBoolean(line[3]));
                tag.setCreateTime(Converters.toTimestamp(line[4]));

                return tag;
            }
        };
        tagStrategy.setType(Tag.class);

        HeaderColumnNameMappingStrategy<Payee> payeeStrategy = new HeaderColumnNameMappingStrategy<Payee>() {
            @Override
            public String[] generateHeader(Payee bean) throws CsvRequiredFieldEmptyException {
                return new String[]{"Id", "名称", "描述", "图标Id", "图标", "是否上传", "创建时间"};
            }

            @Override
            public String[] transmuteBean(Payee bean) throws CsvFieldAssignmentException, CsvChainedException {
                return new String[]{toIntString(bean.getId()), bean.getTitle(), bean.getDesc(), toIntString(bean.getIconResId()), bean.getIconResName(), toBooleanString(bean.isUploaded()), Converters.toTimestampString(bean.getCreateTime())};
            }

            @Override
            public Payee populateNewBean(String[] line) throws CsvBeanIntrospectionException, CsvFieldAssignmentException, CsvChainedException {
                Payee payee = new Payee();
                payee.setId(toInteger(line[0]));
                payee.setTitle(line[1]);
                payee.setDesc(line[2]);
                payee.setIconResId(toInteger(line[3]));
                payee.setIconResName(line[4]);
                payee.setUploaded(toBoolean(line[5]));
                payee.setCreateTime(Converters.toTimestamp(line[6]));

                return payee;
            }
        };
        payeeStrategy.setType(Payee.class);

        HeaderColumnNameMappingStrategy<Role> roleStrategy = new HeaderColumnNameMappingStrategy<Role>() {
            @Override
            public String[] generateHeader(Role bean) throws CsvRequiredFieldEmptyException {
                return new String[]{"Id", "名称", "描述", "图标Id", "图标", "是否上传", "创建时间"};
            }

            @Override
            public String[] transmuteBean(Role bean) throws CsvFieldAssignmentException, CsvChainedException {
                return new String[]{toIntString(bean.getId()), bean.getTitle(), bean.getDesc(), toIntString(bean.getIconResId()), bean.getIconResName(), toBooleanString(bean.isUploaded()), Converters.toTimestampString(bean.getCreateTime())};
            }

            @Override
            public Role populateNewBean(String[] line) throws CsvBeanIntrospectionException, CsvFieldAssignmentException, CsvChainedException {
                Role role = new Role();
                role.setId(toInteger(line[0]));
                role.setTitle(line[1]);
                role.setDesc(line[2]);
                role.setIconResId(toInteger(line[3]));
                role.setIconResName(line[4]);
                role.setUploaded(toBoolean(line[5]));
                role.setCreateTime(Converters.toTimestamp(line[6]));
                return role;
            }
        };
        roleStrategy.setType(Role.class);

        strategyMap = new HashMap<String, HeaderColumnNameMappingStrategy>();
        strategyMap.put(ConstantValue.EXPORT_BILL_CSV_FILENAME, billStrategy);
        strategyMap.put(ConstantValue.EXPORT_LEGER_CSV_FILENAME, legerStrategy);
        strategyMap.put(ConstantValue.EXPORT_ACCOUNT_CSV_FILENAME, accountStrategy);
        strategyMap.put(ConstantValue.EXPORT_CATEGORY_CSV_FILENAME, categoryStrategy);
        strategyMap.put(ConstantValue.EXPORT_TAG_CSV_FILENAME, tagStrategy);
        strategyMap.put(ConstantValue.EXPORT_PAYEE_CSV_FILENAME, payeeStrategy);
        strategyMap.put(ConstantValue.EXPORT_ROLE_CSV_FILENAME, roleStrategy);
    }


    private static <T> List<T> parseBeanList(InputStream csvInputStream, String typeOfCsv) {
        Class<T> targetClass = getTargetClass(typeOfCsv);
        if (targetClass == null) {
            return new ArrayList<>();
        }
        CsvToBeanBuilder<T> builder = new CsvToBeanBuilder<T>(new InputStreamReader(csvInputStream)).withType(targetClass);
        HeaderColumnNameMappingStrategy<T> strategy = strategyMap.get(typeOfCsv);
        CsvToBean<T> csvToBean = builder.withMappingStrategy(strategy).build();
        return csvToBean.parse();
    }

    private static <T> Class<T> getTargetClass(String typeOfCsv) {
        switch (typeOfCsv) {
            case ConstantValue.EXPORT_BILL_CSV_FILENAME:
                return (Class<T>) Bill.class;
            case ConstantValue.EXPORT_LEGER_CSV_FILENAME:
                return (Class<T>) Leger.class;
            case ConstantValue.EXPORT_ACCOUNT_CSV_FILENAME:
                return (Class<T>) Account.class;
            case ConstantValue.EXPORT_CATEGORY_CSV_FILENAME:
                return (Class<T>) Category.class;
            case ConstantValue.EXPORT_ROLE_CSV_FILENAME:
                return (Class<T>) Role.class;
            case ConstantValue.EXPORT_PAYEE_CSV_FILENAME:
                return (Class<T>) Payee.class;
            default:
                return null;
        }
    }

    public static Map<String, List> readCSVFolder(Uri selectedFolderUri, Context context) {
        Map<String, List> map = new HashMap<>();
        map.put(ConstantValue.EXPORT_BILL_CSV_FILENAME, new ArrayList());
        map.put(ConstantValue.EXPORT_LEGER_CSV_FILENAME, new ArrayList());
        map.put(ConstantValue.EXPORT_ACCOUNT_CSV_FILENAME, new ArrayList());
        map.put(ConstantValue.EXPORT_CATEGORY_CSV_FILENAME, new ArrayList());
        map.put(ConstantValue.EXPORT_ROLE_CSV_FILENAME, new ArrayList());
        map.put(ConstantValue.EXPORT_PAYEE_CSV_FILENAME, new ArrayList());
        ContentResolver contentResolver = context.getContentResolver();

        // 授予选择的文件夹uri以读取其内容的权限
        final int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION;
        contentResolver.takePersistableUriPermission(selectedFolderUri, takeFlags);

        // 获取文件夹uri内的文件uri，并对其中的csv文件进行读取。
        try {
            Uri childrenUri = DocumentsContract.buildChildDocumentsUriUsingTree(selectedFolderUri, DocumentsContract.getTreeDocumentId(selectedFolderUri));
            Cursor childrenCursor = contentResolver.query(childrenUri, new String[]{DocumentsContract.Document.COLUMN_DOCUMENT_ID, DocumentsContract.Document.COLUMN_DISPLAY_NAME}, null, null, null);

            if (childrenCursor != null) {
                while (childrenCursor.moveToNext()) {
                    String documentId = childrenCursor.getString(0);
                    String fileName = childrenCursor.getString(1);
                    // ... 获取该文件uri

                    // 该文件名称是否与预定义的csv文件名称一致
                    if (fileName.equals(ConstantValue.EXPORT_BILL_CSV_FILENAME) || fileName.equals(ConstantValue.EXPORT_LEGER_CSV_FILENAME) || fileName.equals(ConstantValue.EXPORT_ACCOUNT_CSV_FILENAME) || fileName.equals(ConstantValue.EXPORT_CATEGORY_CSV_FILENAME) || fileName.equals(ConstantValue.EXPORT_ROLE_CSV_FILENAME) || fileName.equals(ConstantValue.EXPORT_PAYEE_CSV_FILENAME)) {
                        // 根据 selected Folder uri 与文件Id: documentId 获取文件Uri地址
                        Uri fileUri = DocumentsContract.buildDocumentUriUsingTree(selectedFolderUri, documentId);
                        // 读取csv文件内容
                        InputStream inputStream = contentResolver.openInputStream(fileUri);
                        if (inputStream != null) {
                            List beanList = parseBeanList(inputStream, fileName);
                            // 将parse的java bean list 结果保存到map中
                            map.put(fileName, beanList);
                            inputStream.close();
                        }
                    }
                }
                childrenCursor.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }

    public static Map<String, List> readCSVFiles(List<Uri> uris, Context context) {
        Map<String, List> map = new HashMap<>();
        map.put(ConstantValue.EXPORT_BILL_CSV_FILENAME, new ArrayList());
        map.put(ConstantValue.EXPORT_LEGER_CSV_FILENAME, new ArrayList());
        map.put(ConstantValue.EXPORT_ACCOUNT_CSV_FILENAME, new ArrayList());
        map.put(ConstantValue.EXPORT_CATEGORY_CSV_FILENAME, new ArrayList());
        map.put(ConstantValue.EXPORT_ROLE_CSV_FILENAME, new ArrayList());
        map.put(ConstantValue.EXPORT_PAYEE_CSV_FILENAME, new ArrayList());


        for (Uri fileUri : uris) {
            String fileName = getFileNameFromUri(fileUri, context);
            if (fileName.equals(ConstantValue.EXPORT_BILL_CSV_FILENAME) ||
                    fileName.equals(ConstantValue.EXPORT_LEGER_CSV_FILENAME) ||
                    fileName.equals(ConstantValue.EXPORT_ACCOUNT_CSV_FILENAME) ||
                    fileName.equals(ConstantValue.EXPORT_CATEGORY_CSV_FILENAME) ||
                    fileName.equals(ConstantValue.EXPORT_ROLE_CSV_FILENAME) ||
                    fileName.equals(ConstantValue.EXPORT_PAYEE_CSV_FILENAME)) {
                List beanList = readSingleCSVFile(fileUri, context);
                map.put(fileName, beanList);
            }
        }
        return map;
    }

    public static List readSingleCSVFile(Uri fileUri, Context context) {
        List beanList = null;
        ContentResolver contentResolver = context.getContentResolver();

        // 授予文件 URI 以读取其内容的权限
        final int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION;
        contentResolver.takePersistableUriPermission(fileUri, takeFlags);

        try {
            InputStream inputStream = contentResolver.openInputStream(fileUri);
            if (inputStream != null) {
                String fileName = getFileNameFromUri(fileUri, context);
                beanList = parseBeanList(inputStream, fileName);
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return beanList;
    }

    @SuppressLint("Range")
    private static String getFileNameFromUri(Uri uri, Context context) {
        Cursor cursor = context.getContentResolver().query(uri, new String[]{OpenableColumns.DISPLAY_NAME}, null, null, null);
        String name = null;
        if (cursor != null && cursor.moveToFirst()) {
            name = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            cursor.close();
        }
        return name;
    }


    /**
     * @param dataClass the class of java bean, e.g: User.class
     * @param dataList  java bean list
     * @param parentDir the parent directory of csv file
     * @param fileName  scv filename
     * @param strategy  the header columns of csv file
     * @param <T>       type of java bean
     */
    private static <T> void exportToCSV(Class<T> dataClass, List<T> dataList, String parentDir, String fileName, HeaderColumnNameMappingStrategy<T> strategy) {
        String baseDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();
        String folderPath = baseDir + File.separator + ConstantValue.DOCUMENTS_APP_PATH + File.separator + "csv" + File.separator + parentDir;
        File folder = new File(folderPath);

        if (!folder.exists()) {
            folder.mkdirs();
        }
        File csvFile = new File(folderPath + File.separator + fileName);
        try (FileWriter writer = new FileWriter(csvFile)) {

            StatefulBeanToCsv<T> beanToCsv = new StatefulBeanToCsvBuilder<T>(writer).withMappingStrategy(strategy).build();
            beanToCsv.write(dataList);
        } catch (IOException | CsvException e) {
            Log.e("CsvExportUtil", "Error exporting to CSV", e);
        }
    }

    public static <T> void exportToCSV(Class<T> dataClass, List<T> dataList, String parentDir, String typeOfCsv) {
        exportToCSV(dataClass, dataList, parentDir, typeOfCsv, strategyMap.get(typeOfCsv));
    }

    private static String getString(Object object) {
        if (Objects.isNull(object)) {
            return null;
        } else {
            return object.toString();
        }
    }

}