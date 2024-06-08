package com.example.pet;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.loader.content.AsyncTaskLoader;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;

public class BoardRegister extends AppCompatActivity implements OnTaskCompleted{

    // 로그에 사용할 TAG 변수 선언
    final private String TAG = getClass().getSimpleName();

    // 사용할 컴포넌트 선언
    private ImageButton imageAddButton;
    //private ImageView addedImage;

    private EditText titleInput;
    private EditText contentInput;
    private Button registBtn;
    private Uri imageUri;

    private static final int REQUEST_CODE = 1;

    @Override
    public void onTaskCompleted(String result) {
        // AsyncTask의 작업이 완료된 후 호출될 메서드
        Log.i("Result Post board", result);
        // board list로 이동
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_register);

        //컴포넌트 초기화
        imageAddButton = findViewById(R.id.board_add_image_button);
        //addedImage = findViewById(R.id.board_added_image);

        titleInput = findViewById(R.id.board_title_input);
        contentInput = findViewById(R.id.board_content_input);
        registBtn = findViewById(R.id.registBtn);

        imageAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        //버튼 이벤트 추가
        registBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //String userID = "userID"; // 사용자 ID
                //String date = "2024-00-00"; // 날짜 대체
                String title = titleInput.getText().toString();
                String content = contentInput.getText().toString();

                if (imageUri != null){
                    try {
                        File imageFile = new File(getCacheDir(), "board_image.jpg");
                        try (FileOutputStream out = new FileOutputStream(imageFile)) {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        }
                        // Retrofit을 사용하여 서버로 데이터 전송
                        RetrofitClient.postBoard(getApplicationContext(), title, content, imageFile);
                        finish();

                    } catch (IOException e){
                        e.printStackTrace();;
                        Toast.makeText(BoardRegister.this, "이미지 처리 오류", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Retrofit을 사용하여 이미지 없이 서버로 데이터 전송
                    RetrofitClient.postBoard(getApplicationContext(), title, content,null);
                    finish();
                }
            }
        });
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                imageUri = data.getData();
                if (imageUri != null) {
                    Bitmap bitmap;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                        imageAddButton.setImageBitmap(bitmap);
                        imageAddButton.setVisibility(View.VISIBLE);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}