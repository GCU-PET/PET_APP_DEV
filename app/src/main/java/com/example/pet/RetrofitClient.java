package com.example.pet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
    private static final String BASE_URL = "https://wicked-paws-make.loca.lt//";

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

    // 회원가입 정보 입력
    public static void signUpUser(String userId, String password, String nickname, File profileImage) {
        // 로깅 인터셉터 추가
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        // Retrofit 설정
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // 서비스 생성
        UserServiceApi service = retrofit.create(UserServiceApi.class);

        // 텍스트 데이터 준비
        RequestBody userIdPart = RequestBody.create(MediaType.parse("text/plain"), userId);
        RequestBody passwordPart = RequestBody.create(MediaType.parse("text/plain"), password);
        RequestBody nicknamePart = RequestBody.create(MediaType.parse("text/plain"), nickname);

        // 이미지 파일 준비
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), profileImage);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("profile_image", profileImage.getName(), requestFile);

        // 요청 전송
        Call<ResponseBody> call = service.signupUser(userIdPart, passwordPart, nicknamePart, imagePart);
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

    // 회원정보 업데이트
    public static void updateUser(String userId, String nickname, File profileImage) {
        // Retrofit 인스턴스 및 서비스 생성은 registerUser 메서드와 동일
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        // Retrofit 설정
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // 서비스 생성
        UserServiceApi service = retrofit.create(UserServiceApi.class);

        // 텍스트 데이터와 이미지 파일 준비
        RequestBody nicknamePart = RequestBody.create(MediaType.parse("text/plain"), nickname);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), profileImage);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("profile_image", profileImage.getName(), requestFile);

        // 업데이트 요청 전송
        Call<ResponseBody> call = service.updateUser(userId, nicknamePart, imagePart);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        System.out.println("Update Success: " + response.body().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    try {
                        System.out.println("Update Error: " + response.errorBody().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("Update Failure");
                t.printStackTrace();
            }
        });
    }

    // 게시판 글 추가
    public static void postBoard(String userID, String title, String content,String date,@Nullable File boardImage) {
        // Retrofit 인스턴스 및 서비스 생성은 registerUser 메서드와 동일
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        // Retrofit 설정
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // 서비스 생성
        BoardServiceApi service = retrofit.create(BoardServiceApi.class);

        // 텍스트 데이터 준비
        RequestBody userIdPart = RequestBody.create(MediaType.parse("text/plain"), userID);
        RequestBody titlePart = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody contentPart = RequestBody.create(MediaType.parse("text/plain"), content);
        RequestBody datePart = RequestBody.create(MediaType.parse("text/plain"),date);

        //이미지 준비
        MultipartBody.Part imagePart = null;
        if(boardImage != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), boardImage);
            imagePart = MultipartBody.Part.createFormData("board_image", boardImage.getName(), requestFile);
        }

        // 요청 전송
        Call<ResponseBody> call = service.postBoardData(userIdPart, titlePart, contentPart, datePart, imagePart);

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

    public static BoardServiceApi getBoardServiceApi() {
        return getRetrofitInstance().create(BoardServiceApi.class);
    }

}
