package com.example.pet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class BoardDetail extends AppCompatActivity {

    private TextView title;
    private TextView content;
    private TextView userID;
    private TextView date;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_detail);

        // 컴포넌트 초기화
        title = findViewById(R.id.board_title);
        content = findViewById(R.id.board_content);
        userID = findViewById(R.id.board_userID);
        date = findViewById(R.id.board_date);
        image = findViewById(R.id.board_detail_image);

        // 인텐트로부터 데이터 수신
        String titleText = getIntent().getStringExtra("title");
        String contentText = getIntent().getStringExtra("content");
        String dateText = getIntent().getStringExtra("date");
        String userIDText = getIntent().getStringExtra("userID");
        String imageUrlText = getIntent().getStringExtra("imageUrl");

        //수신한 데이터를 UI에 설정
        title.setText(titleText);
        content.setText(contentText);
        userID.setText(userIDText);
        date.setText(dateText);

        //이미지 로드
        if (imageUrlText != null && !imageUrlText.isEmpty()) {
            Glide.with(this)
                    .load(imageUrlText)
                    .placeholder(R.drawable.icon_camera)
                    .into(image);
        }


    }
}