package com.vividbobo.easy.ui.home;

import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.vividbobo.easy.BaseActivity;
import com.vividbobo.easy.R;
import com.vividbobo.easy.adapter.adapter.HomeBillAdapter;
import com.vividbobo.easy.database.model.Bill;
import com.vividbobo.easy.databinding.ActivityScreenshortImportBinding;
import com.vividbobo.easy.hmsml.text.AliPayTextAnalyzer;
import com.vividbobo.easy.hmsml.text.TextAnalyzer;
import com.vividbobo.easy.hmsml.text.WeChatTextAnalyzer;
import com.vividbobo.easy.utils.ToastUtil;
import com.vividbobo.easy.viewmodel.ScreenshotImportViewModel;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ScreenshotImportActivity extends BaseActivity {
    private static final String TAG = "ScreenshotImportActivit";
    private ActivityScreenshortImportBinding binding;
    public static final String ANALYZER_WECHAT = "ANALYZER_WECHAT";
    public static final String ANALYZER_ALIPAY = "ANALYZER_ALIPAY";
    private TextAnalyzer analyzer;
    private HomeBillAdapter adapter;
    private ScreenshotImportViewModel screenshotImportView;
    private List<Bill> billList = null;

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
        adapter = new HomeBillAdapter(this);
        adapter.setEnableFooter(false);
        adapter.setEnableHeader(false);
        binding.contentRv.setAdapter(adapter);
    }

    private void configScreenshotPicker() {
        ActivityResultLauncher<PickVisualMediaRequest> pickMedia = registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(9), uris -> {
            // Callback is invoked after the user selects media items or closes the
            // photo picker.
            textAnalyze(uris);
        });
        //编译器报错bug，采用 activityx.1.7以上就不会
        ActivityResultContracts.PickVisualMedia.VisualMediaType mediaType = (ActivityResultContracts.PickVisualMedia.VisualMediaType) ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE;
        PickVisualMediaRequest request = new PickVisualMediaRequest.Builder().setMediaType(mediaType).build();
        binding.selectScreenshotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickMedia.launch(request);
            }
        });
    }

    private void textAnalyze(List<Uri> uris) {
        if (Objects.isNull(uris) || uris.isEmpty() || Objects.isNull(analyzer)) {
            return;
        }
        //create bitmap
        for (int i = 0; i < uris.size(); i++) {
            Uri uri = uris.get(i);
            ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), uri);
            Bitmap bitmap = null;
            try {
                bitmap = ImageDecoder.decodeBitmap(source);
            } catch (IOException e) {
                e.printStackTrace();
                ToastUtil.makeToast("create bitmap failed at " + i);
//                throw new RuntimeException(e);
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
            }

        }

    }

}
