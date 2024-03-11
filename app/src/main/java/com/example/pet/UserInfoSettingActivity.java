package com.example.pet;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserInfoSettingActivity extends AppCompatActivity implements OnTaskCompleted {

    private ImageButton imageAddButton;
    private static final int REQUEST_CODE = 1;

    private Button submitBtn;
    EditText userName;
    EditText userID;
    EditText userPassword;

    EditText petName;
    EditText petAge;

    @Override
    public void onTaskCompleted(String result) {
        // AsyncTask의 작업이 완료된 후 호출될 메서드
        Log.i("Result Post board", result);
        // user fragment로 이동
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_setting);

        submitBtn = findViewById(R.id.userInfo_submit);

        userName = findViewById(R.id.user_name);
        userID = findViewById(R.id.user_id);
        userPassword = findViewById(R.id.user_password);

        petName = findViewById(R.id.pet_name);
        petAge = findViewById(R.id.pet_age);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_name = userName.getText().toString();
                String user_ID = userID.getText().toString();
                String user_password = userPassword.getText().toString();

                String pet_name = petName.getText().toString();
                String pet_age = petAge.getText().toString();

                JSONObject jsonParam = new JSONObject();
                try {
                    jsonParam.put("userName", user_name);
                    jsonParam.put("PW", user_password);
                    jsonParam.put("petName", pet_name);
                    jsonParam.put("petAge", pet_age);

                    // token 불러오기
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
                    listA.add("api/user/update"); //
                    listA.add(token);

                    String jsonWifiData = gson.toJson(listA); // converting wifiData to JSON format

                    new SendDataTask(UserInfoSettingActivity.this).execute(jsonWifiData);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });


        imageAddButton = findViewById(R.id.info_setting_add_image_button);

        imageAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), REQUEST_CODE);
            }
        });

        imageAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // pickImage 메소드
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri imageUri = data.getData();
                if (imageUri != null) {
                    Bitmap bitmap = null;
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