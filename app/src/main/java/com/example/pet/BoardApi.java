package com.example.pet;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface BoardApi {
    //@Header({Request Header})
    @FormUrlEncoded
    @POST("api/board/post") // 경로
    Call<Void> postBoardData(
            @Field("title") String title,
            @Field("date") String date,
            @Field("userID") String userID,
            @Field("content") String content
    );

    @Multipart
    @POST("api/board/post")
    Call<Void> postBoardDataWithImage(
            @Part("title") RequestBody title,
            @Part("date") RequestBody date,
            @Part("userID") RequestBody userID,
            @Part("content") RequestBody content,
            @Part MultipartBody.Part image
    );

    @GET("api/board/items")
    Call<List<BoardData>> getBoardItems();
}
