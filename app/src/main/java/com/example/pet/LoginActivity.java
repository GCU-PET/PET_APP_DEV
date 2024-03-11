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

    // 중복체크 결과 확인 변수
    Boolean checkingDup = false;
    // 중복체크 여부 확인 변수
    Boolean dupCheck = false;
    // 중복 여부 확인 변수
    Boolean isDup = true;

    @Override
    public void onTaskCompleted(String result) {
        // AsyncTask의 작업이 완료된 후 호출될 메서드
        Log.i("Result Login", result);
        TextView failMessage = findViewById(R.id.loginFailure);

        // 중복체크 결과일 때
        if(checkingDup){
            try{
                JSONObject jsonResult = new JSONObject(result);
                boolean enable = jsonResult.getBoolean("result");
                Log.e("enable", String.valueOf(enable));

                if(enable){
                    Toast.makeText(this, "중복이 아닙니다!", Toast.LENGTH_SHORT).show();
                    isDup = false;
                }else{
                    Toast.makeText(this, "중복입니다!", Toast.LENGTH_SHORT).show();
                    isDup = true;
                }
                checkingDup = false;
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
                }else{
                    //로그인 실패
                    Toast.makeText(LoginActivity.this, "로그인 실패!", Toast.LENGTH_SHORT).show();
                    failMessage.setVisibility(View.VISIBLE);
                }

            } catch (JSONException e) {
                Log.e("JSON Parsing Error", "Error parsing JSON result", e);
            }
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
        LinearLayout layoutLogin = findViewById(R.id.login_layout);
        LinearLayout layoutSignup = findViewById(R.id.regis_layout);

        TextView signupText = findViewById(R.id.signup_text);
        TextView backtoLoginText = findViewById(R.id.backtologin_text);
        signupText.setPaintFlags(signupText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        backtoLoginText.setPaintFlags(backtoLoginText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        failMessage.setVisibility(View.GONE);
        layoutSignup.setVisibility(View.GONE);
        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutLogin.setVisibility(View.GONE);
                layoutSignup.setVisibility(View.VISIBLE);
            }
        });

        backtoLoginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutLogin.setVisibility(View.VISIBLE);
                layoutSignup.setVisibility(View.GONE);
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


        Button regisBtn = findViewById(R.id.regis_btn);
        regisBtn.setEnabled(false);

        EditText regisID = findViewById(R.id.regis_id);
        EditText regisPW = findViewById(R.id.regis_password);
        Button dupliBtn = findViewById(R.id.dupliBtn);

        regisPW.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean isRegisIDEmpty = TextUtils.isEmpty(regisID.getText().toString().trim());
                boolean isRegisPWEmpty = TextUtils.isEmpty(regisPW.getText().toString().trim());

                // regisID와 regisPW는 공백이 아니고 중복검사 했고, 중복이 아닐 때
                if (isRegisIDEmpty == false && isRegisPWEmpty == false && dupCheck && !isDup) {
                    regisBtn.setEnabled(true);
                } else {
                    regisBtn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        regisID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dupCheck = false;
                regisBtn.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });


        dupliBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isRegisIDEmpty = TextUtils.isEmpty(regisID.getText().toString().trim());
                boolean isRegisPWEmpty = TextUtils.isEmpty(regisPW.getText().toString().trim());
                dupCheck = true;
                checkingDup = true;

                //regisBtn.setEnabled(true);
                JSONObject jsonParam = new JSONObject();
                try {
                    jsonParam.put("ID",regisID.getText().toString());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                Gson gson = new Gson();
                List<String> listA = new ArrayList<String>();
                listA.add(String.valueOf(jsonParam));
                listA.add("POST");
                listA.add("api/user/isExisted");

                String jsonWifiData = gson.toJson(listA); // converting wifiData to JSON format

                new SendDataTask(LoginActivity.this).execute(jsonWifiData);

                // regisID와 regisPW 중 하나라도 공백이면 또는 중복검사를 하지 않았으면 regisBtn 비활성화
                if (isRegisIDEmpty == false && isRegisPWEmpty == false && !isDup) {
                    regisBtn.setEnabled(true);
                } else {
                    regisBtn.setEnabled(false);
                }
            }
        });

        regisBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = regisID.getText().toString();
                String pw = regisPW.getText().toString();

                JSONObject jsonParam = new JSONObject();
                try {
                    jsonParam.put("ID",id);
                    jsonParam.put("PW",pw);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                Gson gson = new Gson();
                List<String> listA = new ArrayList<String>();
                listA.add(String.valueOf(jsonParam));
                listA.add("POST");
                listA.add("api/user/signup");

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

    @Override
    protected void onResume() {
        super.onResume();
    }
}