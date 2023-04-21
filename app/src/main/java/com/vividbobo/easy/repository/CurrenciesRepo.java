package com.vividbobo.easy.repository;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.vividbobo.easy.database.EasyDatabase;
import com.vividbobo.easy.database.dao.CurrencyDao;
import com.vividbobo.easy.database.model.Currency;
import com.vividbobo.easy.network.RateApiClient;
import com.vividbobo.easy.network.response.RateResponse;
import com.vividbobo.easy.utils.AsyncProcessor;
import com.vividbobo.easy.utils.ConstantValue;
import com.vividbobo.easy.utils.SharePreferenceUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CurrenciesRepo {
    private final Context mContext;
    //dao
    private final CurrencyDao currencyDao;

    // network
    private final RateApiClient rateApiClient;

    //livedata
    private final LiveData<List<Currency>> enableCurrencies;
    private final MutableLiveData<String> baseCurrencyCode;
    //    private final MutableLiveData<Map<String, Float>> rateMap;
    private LiveData<Currency> baseCurrency;

    //用于获取更新列表的回调;  使用ListenableFuture进行更新列表，没有使用LiveData是因为，我不需要对数据库所有数据项进行实时观察监听
    // 避免当插入数据时，也会更新整个列表
    private MutableLiveData<ListenableFuture<List<Currency>>> listenableFutureOfCurrencies;


    public CurrenciesRepo(Application application) {
        mContext = application;
        EasyDatabase db = EasyDatabase.getDatabase(application);
        currencyDao = db.currencyDao();
        enableCurrencies = currencyDao.getEnableCurrencies();
        listenableFutureOfCurrencies = new MutableLiveData<>(currencyDao.getAllCurrencies());

        //network
        rateApiClient = new RateApiClient();

        //baseCurrency
//        updateBaseCurrency();
        String baseCode = SharePreferenceUtil.getCode(application);
        baseCurrencyCode = new MutableLiveData<>(baseCode);
        baseCurrency = Transformations.switchMap(baseCurrencyCode, currencyDao::getCurrency);      //lambda

//        Map<String, Float> _rateMap = SharePreferenceUtil.getRateMap(application);
//        rateMap = new MutableLiveData<>(_rateMap);
    }

    public MutableLiveData<String> getBaseCurrencyCode() {
        return baseCurrencyCode;
    }


    public LiveData<List<Currency>> getEnableCurrencies() {
        return enableCurrencies;
    }


    public void update(Currency currency) {
        AsyncProcessor.getInstance().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                currencyDao.update(currency);
                return null;
            }
        });
    }

    public void update(List<Currency> currencies) {
        ListenableFuture<Integer> updateFuture = AsyncProcessor.getInstance().submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return currencyDao.update(currencies);
            }
        });
        Futures.addCallback(updateFuture, new FutureCallback<Integer>() {
            @Override
            public void onSuccess(Integer result) {
                Log.d("TAG", "onSuccess: update return result: " + result);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("TAG", "onFailure: update failure");
            }
        }, AsyncProcessor.getInstance().getExecutorService());
    }

    public void updateRate(Map<String, Float> rates) {
        AsyncProcessor.getInstance().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                Log.d("TAG", "call: updating rates");
                for (Map.Entry<String, Float> entry : rates.entrySet()) {
                    currencyDao.setAutoUpdateRate(entry.getKey(), entry.getValue());
                }
                return null;
            }
        }).addListener(new Runnable() {
            @Override
            public void run() {
                //call 完成后
                Log.d("TAG", "run: update done");
                listenableFutureOfCurrencies.postValue(currencyDao.getAllCurrencies());
            }
        }, AsyncProcessor.getInstance().getExecutorService());

    }

    /**
     * 获取最新的汇率
     *
     * @param baseCode 基货币代码
     */
    public void updateRateOf(String baseCode) {
        Log.d("TAG", "updateRateOf: " + baseCode);

        Call<RateResponse> call = rateApiClient.getRateService().getRateOf(RateApiClient.RATE_KEY, baseCode);
        call.enqueue(new Callback<RateResponse>() {
            @Override
            public void onResponse(Call<RateResponse> call, Response<RateResponse> response) {
                //set livedata
                if (response.code() == 200) {
                    //网络请求成功
                    RateResponse rateResponse = response.body();
                    if (rateResponse != null) {
                        // update the live date and store into sharePreference
//                        rateMap.setValue(rateResponse.getConversion_rates());
//                        Log.d("TAG", "onResponse: the rateMap size: " + rateMap.getValue().size());
                        // store the map
//                        SharePreferenceUtil.setRateMap(mContext, rateResponse.getConversion_rates());
                        // store base code
                        SharePreferenceUtil.setCode(mContext, rateResponse.getBase_code());
                        // write the rate into database
                        updateRate(rateResponse.getConversion_rates());

                    } else {
                        //response.body is null
                        Log.d("TAG", "onResponse: body is null");
                    }
                } else {
                    //code != 200
                    Log.d("TAG", "onResponse: failure");
                }
            }

            @Override
            public void onFailure(Call<RateResponse> call, Throwable t) {
                //don't set livedata
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });

    }


    public LiveData<ListenableFuture<List<Currency>>> getListenableFutureOfCurrencies() {
        return listenableFutureOfCurrencies;
    }

    public LiveData<Currency> getBaseCurrency() {
        return baseCurrency;
    }

    public LiveData<Currency> getCurrency(String code) {
        return currencyDao.getCurrency(code);
    }

    public void updateEnable(String code, boolean isCheck) {
        AsyncProcessor.getInstance().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                currencyDao.setEnable(code, isCheck);

                return null;
            }
        });
    }

//    public LiveData<Map<String, Float>> getRateMap() {
//        return rateMap;
//    }
}
