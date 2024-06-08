package com.example.pet;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements OnTaskCompleted{

    @Override
    public void onTaskCompleted(String result) {
        // AsyncTask의 작업이 완료된 후 호출될 메서드
        Log.i("Result Login", result);
        TextView failMessage = findViewById(R.id.loginFailure);

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
                Toast.makeText(LoginActivity.this, "로그인 실패!", Toast.LENGTH_SHORT).show();
                failMessage.setVisibility(View.VISIBLE);
            }

        } catch (JSONException e) {
            Log.e("JSON Parsing Error", "Error parsing JSON result", e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText id = findViewById(R.id.id);
        EditText pw = findViewById(R.id.password);
        Button loginBtn = findViewById(R.id.loginBtn);
        TextView failMessage = findViewById(R.id.loginFailure);

        Button signinBtn = findViewById(R.id.signinBtn);

        failMessage.setVisibility(View.INVISIBLE);

        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // signup 페이지로 이동
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //List<String> loginList = new ArrayList<String>();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String i = id.getText().toString();
                String p = pw.getText().toString();

                JSONObject jsonParam = new JSONObject();

                try {
                    jsonParam.put("ID", i);
                    jsonParam.put("PW", p);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                Gson gson = new Gson();
                List<String> listA = new ArrayList<String>();
                listA.add(String.valueOf(jsonParam));
                listA.add("POST");
                listA.add("api/user/login");

                String jsonWifiData = gson.toJson(listA); // converting wifiData to JSON format

                new SendDataTask(LoginActivity.this).execute(jsonWifiData);
            }
        });
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

}