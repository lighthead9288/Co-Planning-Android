package com.example.lighthead.androidcustomcalendar.helpers.communication;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    public RetrofitClient() {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClientBuilder.addInterceptor(logging);

        Log.d("MyLog", "Connecting with localhost:");
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClientBuilder.build());
        Log.d("MyLog", "Connected");

        retrofit = builder.build();

    }


    private Retrofit retrofit;


    public Retrofit GetRetrofitEntity() {
        return retrofit;
    }
}
