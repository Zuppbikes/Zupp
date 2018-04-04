package com.zupp.component.httpclient;


import com.zupp.component.AppHelper;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppClient {
    private static API api;

    static {
        setupRestClient();
    }

    private AppClient() {
    }

    private static void setupRestClient() {
        AuthInterceptor aAuthInterceptor = new AuthInterceptor();
        HttpLoggingInterceptor aLogging = new HttpLoggingInterceptor();
        // set your desired log level
        aLogging.setLevel(AppHelper.getInstance().isDebugEnabled() ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        OkHttpClient aClient = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).retryOnConnectionFailure(true)
                .addInterceptor(aAuthInterceptor)
                .addInterceptor(aLogging)
                .build();

        Retrofit aRetrofit = new Retrofit.Builder()
                .baseUrl(BaseUrlHelper.getBaseUrl())
                .client(aClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = aRetrofit.create(API.class);
    }

    public static API get() {
        return api;
    }
}