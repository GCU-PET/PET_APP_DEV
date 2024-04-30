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

    private File photoFile;

    // 사용할 컴포넌트 선언
    private ImageButton imageAddButton;
    private ImageView addedImage;

    private EditText titleInput;
    private EditText contentInput;
    private Button registBtn;

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
        addedImage = findViewById(R.id.board_added_image);

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
                //uploadPost();

                String title = titleInput.getText().toString();
                String content = contentInput.getText().toString();

                RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), photoFile);
                MultipartBody.Part profile = MultipartBody.Part.createFormData("profile", photoFile.getName(), fileBody);

                JSONObject jsonParam = new JSONObject();
                try {
                    jsonParam.put("title", title); //
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                try {
                    jsonParam.put("content",content); //
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                try {
                    jsonParam.put("profile",profile);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                // 파일이 이미 존재하는지 확인
                File file = new File(getFilesDir(), "token.txt");
                String token = "";
                // 파일 존재 여부 확인
                if (file.exists()) {
                    // 파일이 존재할 경우의 처리
                    try {
                        // 파일 읽기
                        BufferedReader reader = new BufferedReader(new FileReader(file));
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;

                        while ((line = reader.readLine()) != null) {
                            stringBuilder.append(line);
                        }

                        reader.close();
                        // 읽은 내용 출력 또는 다른 처리 수행
                        String fileContent = stringBuilder.toString();
                        token = fileContent;
                        Log.i("File Content - Token: ", fileContent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                Gson gson = new Gson();
                List<String> listA = new ArrayList<String>();
                listA.add(String.valueOf(jsonParam));
                listA.add("POST");
                listA.add("api/board/post/image"); //
                listA.add(token);

                String jsonWifiData = gson.toJson(listA); // converting wifiData to JSON format

                new SendDataTask(BoardRegister.this).execute(jsonWifiData);

            }
        });
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), REQUEST_CODE);

    }

    private void uploadPost() {
        if (addedImage.getDrawable() != null) {
            String title = titleInput.getText().toString();
            String content = contentInput.getText().toString();
            Uri imageUri = getImageUri(addedImage);

            if (imageUri != null) {  // 이미지 URI가 null인 경우 처리
                Intent resultIntent = new Intent();
                resultIntent.putExtra("title", title);
                resultIntent.putExtra("content", content);
                resultIntent.putExtra("imageUri", imageUri.toString());
                setResult(RESULT_OK, resultIntent);

                finish();
            } else {
                Toast.makeText(this, "Failed to upload image.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }else {
            String title = titleInput.getText().toString();
            String content = contentInput.getText().toString();

            Intent resultIntent = new Intent();
            resultIntent.putExtra("title", title);
            resultIntent.putExtra("content", content);
            setResult(RESULT_OK, resultIntent);

            finish();
        }
    }

    private Uri getImageUri(ImageView imageView) {
        Drawable drawable = imageView.getDrawable();
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        }

        // 이미지를 저장할 임시 파일 생성
        File imageFile = new File(getCacheDir(), "temp_image.jpg");
        try (OutputStream outputStream = Files.newOutputStream(imageFile.toPath())) {
            assert bitmap != null;
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // FileProvider를 사용하여 콘텐츠 URI 생성
        return FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", imageFile);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri imageUri = data.getData();
                if (imageUri != null) {
                    Bitmap bitmap;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                        addedImage.setImageBitmap(bitmap);
                        //imageAddButton.setVisibility(View.GONE);
                        addedImage.setVisibility(View.VISIBLE);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}