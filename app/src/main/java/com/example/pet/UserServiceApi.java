package com.example.pet;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface UserServiceApi {
    @Multipart
    @POST("/signup")
    Call<ResponseBody> signupUser(
            @Part("user_id") RequestBody userId,
            @Part("password") RequestBody password,
            @Part("petName") RequestBody petName,
            @Part MultipartBody.Part profileImage
    );

    @Multipart
    @PUT("/update/{user_id}")
    Call<ResponseBody> updateUser(
            @Path("user_id") String userId,
            //@Part("password") RequestBody password,
            @Part("nickname") RequestBody nickname,
            @Part MultipartBody.Part profileImage
    );
}
