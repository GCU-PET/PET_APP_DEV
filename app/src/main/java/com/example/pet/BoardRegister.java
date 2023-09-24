package com.example.pet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class BoardRegister extends AppCompatActivity {

    // 로그에 사용할 TAG 변수 선언
    final private String TAG = getClass().getSimpleName();

    // 사용할 컴포넌트 선언
    EditText title_et, content_et;
    Button registBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_register);

        //컴포넌트 초기화
        title_et = findViewById(R.id.title_et);
        content_et = findViewById(R.id.content_et);
        registBtn = findViewById(R.id.registBtn);

        //버튼 이벤트 추가
        registBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //게시물 등록 함수
                RegistBoard registBoard = new RegistBoard();
            }
        });
    }

    class RegistBoard extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "onPreExecute");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "onPostExecute, " + s);

            if(s.equals("success")){
                // 결과값이 success 이면
                // 토스트 메시지를 뿌리고
                // 이전 액티비티(ListActivity)로 이동,
                // 이때 ListActivity 의 onResume() 함수 가 호출되며, 데이터를 새로 고침
                Toast.makeText(BoardRegister.this, "등록되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }else {
                Toast.makeText(BoardRegister.this, s, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            return null;
        }

    }
}