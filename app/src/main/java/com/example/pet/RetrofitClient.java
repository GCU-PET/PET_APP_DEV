package com.example.pet;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;

public class RetrofitClient {
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://www.feople-eeho.com/";

    // header에 token이 없을 때
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    // header에 token이 있을 때
    public static Retrofit getRetrofitInstance(Context context) {
        if (retrofit == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .addInterceptor(new TokenInterceptor(context))
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static UserServiceApi getUserServiceApi(Context context) {
        return getRetrofitInstance(context).create(UserServiceApi.class);
    }

    public static void postUpdateUserInfo(Context context, String PW, String userName, String petName) {
        UserServiceApi service = getRetrofitInstance(context).create(UserServiceApi.class);

//        RequestBody pwPart = RequestBody.create(MediaType.parse("text/plain"), PW);
//        RequestBody userNamePart = RequestBody.create(MediaType.parse("text/plain"), userName);
//        RequestBody petNamePart = RequestBody.create(MediaType.parse("text/plain"), petName);

        // 텍스트 데이터 준비
        JsonObject json = new JsonObject();
        json.addProperty("PW", PW);
        json.addProperty("userName", userName);
        json.addProperty("petName", petName);

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json.toString());
        Call<ResponseBody> call = service.updateUser(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // 성공적으로 업데이트되었습니다.
                    System.out.println("User updated successfully.");
                } else {
                    // 오류 처리
                    System.out.println("Error: " + response.errorBody().toString());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // 네트워크 오류 등
                t.printStackTrace();
            }
        });

    }

    // 게시판 글 추가
    public static void postBoard(Context context, String title, String content, @Nullable File boardImage) {

        // 서비스 생성
        BoardServiceApi service = getRetrofitInstance(context).create(BoardServiceApi.class);
        Call<ResponseBody> call;

        if(boardImage != null) {
            // 텍스트 데이터 준비
            RequestBody titlePart = RequestBody.create(MediaType.parse("multipart/form-data"), title);
            RequestBody contentPart = RequestBody.create(MediaType.parse("multipart/form-data"), content);

            //이미지 준비
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), boardImage);
            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("profile", boardImage.getName(), requestFile);

            call = service.postBoardDataImage(titlePart, contentPart, imagePart);
        } else {
            // 텍스트 데이터 준비
            JsonObject json = new JsonObject();
            json.addProperty("title", title);
            json.addProperty("content", content);

            RequestBody body = RequestBody.create(MediaType.parse("application/json"), json.toString());
            call = service.postBoardData(body);
        }

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        System.out.println("Server Response: " + response.body().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    try {
                        System.out.println("Request Error :: " + response.errorBody().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    public static BoardServiceApi getBoardServiceApi(Context context) {
        return getRetrofitInstance(context).create(BoardServiceApi.class);
    }

    public static BoardServiceApi getTimelineLogList(Context context) {
        if (retrofit == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .addInterceptor(new TokenInterceptor(context))
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit.create(BoardServiceApi.class);
    }

}
