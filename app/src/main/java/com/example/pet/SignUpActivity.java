package com.example.pet;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity implements OnTaskCompleted{

    //private CircleImageView profile;
    private EditText userName;
    private EditText petName;
    private EditText userID;
    private EditText password;
    private Button signupBtn;
    private Button dupliBtn;

    private boolean isUserIdValid = false;
    private boolean isReadyToCheckDuplicate = false;
    private boolean resultForCheckingDup = true;

    private static final int REQUEST_CODE = 1;

    @Override
    public void onTaskCompleted(String result) {
        // 중복체크 결과일 때
        if(resultForCheckingDup){
            try{
                JSONObject jsonResult = new JSONObject(result);
                boolean enable = jsonResult.getBoolean("result");
                Log.e("enable", String.valueOf(enable));

                if(enable){
                    Toast.makeText(this, "중복이 아닙니다!", Toast.LENGTH_SHORT).show();
                    isUserIdValid = true;
                }else{
                    Toast.makeText(this, "중복입니다!", Toast.LENGTH_SHORT).show();
                    isUserIdValid = false;
                }
                resultForCheckingDup = false;

                checkFieldsForValidValues();
            } catch (JSONException e) {
                Log.e("JSON Parsing Error", "Error parsing JSON result", e);
            }
        }
        else{
            try {
                JSONObject jsonResult = new JSONObject(result);
                boolean loginResult = jsonResult.getBoolean("result");

                // 이제 loginResult 변수에는 "result" 필드의 boolean 값이 들어 있습니다.
                // 이 값을 활용하여 필요한 작업을 수행할 수 있습니다.
                if(loginResult){
                    String token = jsonResult.getString("token");

                    Log.e("token",token);
                    // txt 파일에 token 저장
                    saveTokenToFile(token);
                    // mainActivity 이동
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();

                }else{
                    //로그인 실패
                    Toast.makeText(SignUpActivity.this, "회원가입에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                Log.e("JSON Parsing Error", "Error parsing JSON result", e);
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //profile = findViewById(R.id.signup_profile);
        userName = findViewById(R.id.signup_username);
        petName = findViewById(R.id.signup_petname);
        userID = findViewById(R.id.signup_id);
        password = findViewById(R.id.signup_password);

        signupBtn = findViewById(R.id.signupBtn);
        dupliBtn = findViewById(R.id.dupliBtn);

        signupBtn.setEnabled(false);
        dupliBtn.setEnabled(false);

//        profile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("image/*");
//                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
//                startActivityForResult(Intent.createChooser(intent, "Select Image"), REQUEST_CODE);
//            }
//        });

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                checkFieldsForValidValues();
            }
        };

        userName.addTextChangedListener(textWatcher);
        petName.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);

        userID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if(!userID.getText().toString().trim().isEmpty()){
                    isReadyToCheckDuplicate = true;
                }
                else{
                    isReadyToCheckDuplicate = false;
                }

                dupliBtn.setEnabled(isReadyToCheckDuplicate);

                resultForCheckingDup = true;
                isUserIdValid = false;
                signupBtn.setEnabled(false);

                checkFieldsForValidValues();
            }
        });

        dupliBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject jsonParam = new JSONObject();
                try {
                    jsonParam.put("ID",userID.getText().toString());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                Gson gson = new Gson();
                List<String> listA = new ArrayList<String>();
                listA.add(String.valueOf(jsonParam));
                listA.add("POST");
                listA.add("api/user/isExisted");

                String jsonWifiData = gson.toJson(listA); // converting wifiData to JSON format

                new SendDataTask(SignUpActivity.this).execute(jsonWifiData);
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = userID.getText().toString();
                String pw = password.getText().toString();
                String username = userName.getText().toString();
                String pet_name = petName.getText().toString();

                JSONObject jsonParam = new JSONObject();
                try {
                    jsonParam.put("ID",id);
                    jsonParam.put("PW",pw);
                    jsonParam.put("userName", username);
                    jsonParam.put("petName", pet_name);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                Gson gson = new Gson();
                List<String> listA = new ArrayList<String>();
                listA.add(String.valueOf(jsonParam));
                listA.add("POST");
                listA.add("api/user/signup");

                String jsonWifiData = gson.toJson(listA); // converting wifiData to JSON format

                new SendDataTask(SignUpActivity.this).execute(jsonWifiData);
            }
        });


    }

    private void checkFieldsForValidValues() {
        boolean isUserNameFilled = !userName.getText().toString().trim().isEmpty();
        boolean isPetNameFilled = !petName.getText().toString().trim().isEmpty();
        //boolean isUserIdFilled = !userID.getText().toString().trim().isEmpty();
        boolean isPasswordFilled = !password.getText().toString().trim().isEmpty();

        signupBtn.setEnabled(isUserNameFilled && isPetNameFilled && isUserIdValid && isPasswordFilled);

//        petNameError.setVisibility(isPetNameFilled ? View.GONE : View.VISIBLE);
//        userIdError.setVisibility(isUserIdFilled ? View.GONE : View.VISIBLE);
//        passwordError.setVisibility(isPasswordFilled ? View.GONE : View.VISIBLE);
    }

    private void saveTokenToFile(String token) {
        try {
            // 파일 이름
            String fileName = "token.txt";

            File file = new File(getFilesDir(), fileName);
            // 파일 생성 및 쓰기
            FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(token.getBytes());
            fos.close();

            try {
                FileInputStream fis = new FileInputStream(file);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }

                // 파일 내용을 로그로 출력
                String fileContent = sb.toString();
                Log.d("FileContent", fileContent);

                // 여기에서 fileContent 변수에 파일 내용이 저장되어 있습니다.

                br.close();
                isr.close();
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.i("File Save", "Token saved to " + fileName);

        } catch (IOException e) {
            Log.e("File Save Error", "Error saving token to file", e);
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
//            if (data != null) {
//                Uri imageUri = data.getData();
//                if (imageUri != null) {
//                    Bitmap bitmap;
//                    try {
//                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
//                        profile.setImageBitmap(bitmap);
//                        //imageAddButton.setVisibility(View.GONE);
//                        //profile.setVisibility(View.VISIBLE);
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//    }

}