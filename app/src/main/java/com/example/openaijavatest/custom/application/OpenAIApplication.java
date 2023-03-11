package com.example.openaijavatest.custom.application;

import android.app.Application;

import com.example.openaijavatest.api.ApiManager;
import com.example.openaijavatest.api.NetworkService;
import com.example.openaijavatest.api.NetworkServiceImpl;
import com.example.openaijavatest.custom.OpenAiProperties;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OpenAIApplication extends Application {

    private Retrofit mOpenAiRetrofit;
    private ApiManager mOpenAiApiManager;

    private Retrofit mHotpotRetrofit;
    private ApiManager mHotpotApiManager;

    private Retrofit mStableDiffusionRetrofit;
    private ApiManager mStableDiffusionApiManager;

    private final NetworkService mNetworkService = new NetworkServiceImpl();
    private OkHttpClient mOkHttpClient;

    private static WeakReference<OpenAIApplication> Instance;

    public static OpenAIApplication getInstance() {

        if (Instance != null && Instance.get() != null) {
            return Instance.get();
        } else {
            return null;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Instance = new WeakReference<>(this);

        mOkHttpClient = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public NetworkService getNetworkService() {
        return mNetworkService;
    }

    private Retrofit getOpenAiRetrofit() {

        if (mOpenAiRetrofit == null) {

            mOpenAiRetrofit = new Retrofit.Builder()
                    .baseUrl(OpenAiProperties.OPEN_AI_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(mOkHttpClient)
                    .build();
        }

        return mOpenAiRetrofit;
    }

    public ApiManager getOpenAiApiManager() {

        if (mOpenAiApiManager == null) {
            mOpenAiApiManager = new ApiManager(getOpenAiRetrofit());
        }

        return mOpenAiApiManager;
    }

    private Retrofit getHotpotRetrofit() {
        if (mHotpotRetrofit == null) {

            mHotpotRetrofit = new Retrofit.Builder()
                    .baseUrl(OpenAiProperties.HOP_POT_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(mOkHttpClient)
                    .build();
        }

        return mHotpotRetrofit;
    }

    public ApiManager getHotpotApiManager() {

        if (mHotpotApiManager == null) {
            mHotpotApiManager = new ApiManager(getHotpotRetrofit());
        }

        return mHotpotApiManager;
    }

    private Retrofit getStableDiffusionRetrofit() {
        if (mStableDiffusionRetrofit == null) {

            mStableDiffusionRetrofit = new Retrofit.Builder()
                    .baseUrl(OpenAiProperties.STABLE_DIFFUSION_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(mOkHttpClient)
                    .build();
        }
        return mStableDiffusionRetrofit;
    }

    public ApiManager getStableDiffusionApiManager() {
        if (mStableDiffusionApiManager == null) {
            mStableDiffusionApiManager = new ApiManager(getStableDiffusionRetrofit());
        }

        return mStableDiffusionApiManager;
    }
}
