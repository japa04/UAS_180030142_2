package com.bi183.japasuharsa.services;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public static Retrofit retrofit;
    public static final String BASE_URL = "http://hewoow.000webhostapp.com/api/";
    public static final String IMAGE_URL = "http://hewoow.000webhostapp.com/";

    public static Retrofit getRetrofitInstance(){
        if (retrofit==null){
            try {
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            } catch (Exception er) {
                er.printStackTrace();
            }
        }
        return retrofit;
    }
}