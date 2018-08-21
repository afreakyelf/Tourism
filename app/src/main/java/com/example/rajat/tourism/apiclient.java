package com.example.rajat.tourism;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class apiclient {

    private static Retrofit retrofit = null;
    public static Retrofit getClient(){
        OkHttpClient httpClient = new OkHttpClient().newBuilder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();

        if(retrofit==null){
            retrofit = new Retrofit.Builder()
                            .baseUrl("https://maps.googleapis.com/maps/api/place/nearbysearch/")
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;

    }



}
