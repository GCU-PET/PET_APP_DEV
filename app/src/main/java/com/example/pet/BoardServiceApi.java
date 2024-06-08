package com.example.pet;

import android.media.tv.TimelineResponse;

import androidx.annotation.Nullable;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface BoardServiceApi {
    @Multipart
    @POST("api/board/post/image") // 경로
    Call<ResponseBody> postBoardDataImage(
            @Part("title") RequestBody title,
            @Part("content") RequestBody content,
            @Part MultipartBody.Part profile
    );

    @POST("api/board/post") // 경로
    Call<ResponseBody> postBoardData(
            @Body RequestBody body
//            @Part("title") RequestBody title,
//            @Part("content") RequestBody content
    );

    @GET("api/board/list") //경로
    Call<BoardResponse> getBoardList();

    @GET("api/mypage/log-list/{date}")
    Call<LogListResponse> getLogList(@Path("date") String date);
}