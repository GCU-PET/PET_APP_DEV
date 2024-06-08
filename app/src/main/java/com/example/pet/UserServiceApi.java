package com.example.pet;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface UserServiceApi {

    @POST("/api/user/login")
    Call<ResponseBody> loginUser(
            @Part("ID") RequestBody userId,
            @Part("PW") RequestBody password
    );

    @Multipart
    @POST("/api/user/signup")
    Call<ResponseBody> signupUser(
            @Part("user_id") RequestBody userId,
            @Part("password") RequestBody password,
            @Part("petName") RequestBody petName,
            @Part MultipartBody.Part profileImage
    );

    // user id 불러오기
//    @Multipart
//    @PUT("/update/{user_id}")
//    Call<ResponseBody> updateUser(
//            @Path("user_id") String userId,
//            @Part("password") RequestBody password,
//            @Part("petName") RequestBody petName,
//            @Part MultipartBody.Part profileImage
//    );

    @POST("/api/user/update")
    Call<ResponseBody> updateUser(
            @Body RequestBody body
//            @Part("PW") RequestBody password,
//            @Part("userName") RequestBody userName,
//            @Part("petName") RequestBody petName
    );

    //getUserInfo
    @GET("/api/mypage/dashboard")
    Call<UserInfoResponse> getUserInfo();


}
