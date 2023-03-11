package com.example.openaijavatest.api;

import retrofit2.Retrofit;

public class ApiManager {

    private Api mApi;

    public Api getApi() {
        return mApi;
    }

    public ApiManager(Retrofit retrofit) {
        mApi = retrofit.create(Api.class);
    }
}
