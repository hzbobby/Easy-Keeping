package com.vividbobo.easy.network;

import com.vividbobo.easy.network.response.RateResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * 汇率服务接口
 */
public interface RateService {
    //https://v6.exchangerate-api.com/v6/YOUR-API-KEY/latest/USD
    @GET("{API_KEY}/latest/{ISO_4217_CODE}")
    Call<RateResponse> getRateOf(@Path("API_KEY") String apiKey, @Path("ISO_4217_CODE") String currencyCode);
}
