package com.example.pet;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface BoardServiceApi {
    @Multipart
    @POST("api/board/post") // 경로
    Call<ResponseBody> postBoardData(
            @Part("title") RequestBody boardTitle,
            @Part("content") RequestBody boardContent,
            @Part("date") RequestBody date,
            @Part("userID") RequestBody userID,
            @Part MultipartBody.Part boardImage
    );

    @GET("api/board/list") //경로
    Call<List<BoardItem>> getBoardList();
}
