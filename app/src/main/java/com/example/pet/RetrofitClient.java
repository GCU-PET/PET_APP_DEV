package com.example.pet;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://wicked-paws-make.loca.lt//";

    public static Retrofit getRetrofit() {
        if(retrofit == null) {
            Retrofit.Builder builder = new Retrofit.Builder();
            //builder.baseUrl("{baseURL}/");
            builder.baseUrl(BASE_URL);
            builder.addConverterFactory(GsonConverterFactory.create());

            retrofit = builder.build();
        }
        return retrofit;
    }
}
