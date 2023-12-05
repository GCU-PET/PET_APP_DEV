package com.example.pet;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.loader.content.AsyncTaskLoader;

import android.content.Intent;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class BoardRegister extends AppCompatActivity {

    // 로그에 사용할 TAG 변수 선언
    final private String TAG = getClass().getSimpleName();

    // 사용할 컴포넌트 선언
    private ImageButton imageAddButton;
    private ImageView addedImage;

    private EditText titleInput;
    private EditText contentInput;
    private Button registBtn;

    private static final int REQUEST_CODE = 1;

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
                uploadPost();
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