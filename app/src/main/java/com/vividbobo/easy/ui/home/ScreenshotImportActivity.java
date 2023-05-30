package com.vividbobo.easy.ui.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.vividbobo.easy.BaseActivity;
import com.vividbobo.easy.R;
import com.vividbobo.easy.adapter.adapter.BillAdapter;
import com.vividbobo.easy.database.model.Bill;
import com.vividbobo.easy.databinding.ActivityScreenshortImportBinding;
import com.vividbobo.easy.hmsml.text.AliPayTextAnalyzer;
import com.vividbobo.easy.hmsml.text.TextAnalyzer;
import com.vividbobo.easy.hmsml.text.WeChatTextAnalyzer;
import com.vividbobo.easy.utils.ToastUtil;
import com.vividbobo.easy.viewmodel.ScreenshotImportViewModel;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class ScreenshotImportActivity extends BaseActivity {
    private static final String TAG = "ScreenshotImportActivit";
    private static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 0x1000;
    private ActivityScreenshortImportBinding binding;
    public static final String ANALYZER_WECHAT = "ANALYZER_WECHAT";
    public static final String ANALYZER_ALIPAY = "ANALYZER_ALIPAY";
    private TextAnalyzer analyzer;
    private BillAdapter adapter;
    private ScreenshotImportViewModel screenshotImportView;
    private List<Bill> billList = null;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    private PickVisualMediaRequest request;

    // Register the permissions callback, which handles the user's response to the
// system permissions dialog. Save the return value, an instance of
// ActivityResultLauncher, as an instance variable.


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScreenshortImportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        screenshotImportView = new ViewModelProvider(this).get(ScreenshotImportViewModel.class);

        binding.appBarLayout.layoutToolBar.inflateMenu(R.menu.menu_setting);
        binding.appBarLayout.layoutToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.appBarLayout.layoutToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.setting) {
                    ScreenshotImportSettingsDialog.newInstance().show(getSupportFragmentManager(), ScreenshotImportSettingsDialog.TAG);
                    return true;
                }
                return false;
            }
        });

        String analyzerStr = getIntent().getStringExtra("analyzer");
        if (Objects.equals(analyzerStr, ANALYZER_WECHAT)) {
            analyzer = new WeChatTextAnalyzer(this);
            binding.appBarLayout.layoutToolBarTitleTv.setText("微信账单截图导入");
            binding.selectScreenshotBtn.setText("选择微信账单截图");
        } else {
            analyzer = new AliPayTextAnalyzer(this);
            binding.appBarLayout.layoutToolBarTitleTv.setText("支付宝账单截图导入");
            binding.selectScreenshotBtn.setText("选择支付宝账单截图");
        }
        configBillRv();
        configScreenshotPicker();
        configImport();
    }

    private void configImport() {
        binding.importBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.nonNull(billList)) {
                    screenshotImportView.insert(billList);
                    finish();
                } else {
                    ToastUtil.makeToast("导入账单为空");
                }
            }
        });
    }

    private void configBillRv() {
        adapter = new BillAdapter(this);
        adapter.setEnableFooter(false);
        adapter.setEnableHeader(false);
        binding.contentRv.setAdapter(adapter);
    }

    private void configScreenshotPicker() {
        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            // Callback is invoked after the user selects media items or closes the
            // photo picker.
            textAnalyze(uri);
        });
        //编译器报错bug，采用 activityx.1.7以上就不会
        ActivityResultContracts.PickVisualMedia.VisualMediaType mediaType = (ActivityResultContracts.PickVisualMedia.VisualMediaType) ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE;
        request = new PickVisualMediaRequest.Builder().setMediaType(mediaType).build();


        ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                pickMedia.launch(request);
            } else {
                // Explain to the user that the feature is unavailable because the
                // feature requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.
                ToastUtil.makeToast("请授予读取图片权限");
            }
        });

        binding.selectScreenshotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (ContextCompat.checkSelfPermission(binding.getRoot().getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                    // You can use the API that requires the permission.
//                    pickMedia.launch(request);
//
//                } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                    // In an educational UI, explain to the user why your app requires this
//                    // permission for a specific feature to behave as expected, and what
//                    // features are disabled if it's declined. In this UI, include a
//                    // "cancel" or "no thanks" button that lets the user continue
//                    // using your app without granting the permission.
//
//                } else {
//                    // You can directly ask for the permission.
//                    // The registered ActivityResultCallback gets the result of this request.
//                    requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
//                }

//                if (ActivityCompat.checkSelfPermission(ScreenshotImportActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
//                        != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(ScreenshotImportActivity.this,
//                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                            PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
//                } else {
                    pickMedia.launch(request);
//                }


            }
        });
    }

    private void textAnalyze(Uri uri) {
        if (Objects.isNull(uri)) return;
//        if (Objects.isNull(uris) || uris.isEmpty() || Objects.isNull(analyzer)) {
//            return;
//        }
        //create bitmap
//        for (int i = 0; i < uris.size(); i++) {
//            Uri uri = uris.get(i);

        ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), uri);
        Bitmap bitmap = null;
        try {
            bitmap = ImageDecoder.decodeBitmap(source);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Objects.nonNull(bitmap)) {
            analyzer.analyze(bitmap);
            analyzer.setOnTextAnalyzerSuccess(new TextAnalyzer.OnTextAnalyzerSuccess() {
                @Override
                public void onSuccess(List<Bill> results) {
                    Log.d(TAG, "onSuccess: " + results.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            billList = results;
                            adapter.updateItems(results);
                            binding.importBtn.setVisibility(View.VISIBLE);
                        }
                    });
                }
            });
//        }
        }

    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // Permission was granted, do something with the permission.
//                    pickMedia.launch(request);
//                } else {
//                    // Permission was denied, disable the functionality that depends on this permission.
//                    ToastUtil.makeToast("请授予读取图片权限");
//                }
//            }
//        }
//    }

}
