package com.vividbobo.easy.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RateApiClient {
    public static final String RATE_KEY = "46e679302b2733c67ce732b6";
    public static final String RATE_BASE_URL = "https://v6.exchangerate-api.com/v6/";
    private final Retrofit retrofit;

    public RateApiClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(RATE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public RateService getRateService() {
        return retrofit.create(RateService.class);
    }
}
