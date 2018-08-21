package com.example.rajat.tourism;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface api {

    /*@GET("nearbysearch/json?location=28.247226,76.814590&rankby=distance&type=atm&key=AIzaSyCpW9ycZy2iYulIlPf0BFxikurJYzsWyvo")
    Call<ResponseBody> getJSON();*/



    @GET("{login}")
    Call<ResponseBody> getJSON(@Path("login") String postfix, @QueryMap Map<String, Object> options, @Header("authorization") String auth);


    @GET("{login}")
    Call<ResponseBody> getJSONdetails(@Path("login") String postfix, @QueryMap Map<String, Object> options, @Header("authorization") String auth);

}
