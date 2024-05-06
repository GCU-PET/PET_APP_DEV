package com.example.pet;

import static android.app.Activity.RESULT_OK;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BoardListFragment extends Fragment implements OnTaskCompleted{

    private RecyclerView recyclerView;
    private BoardListAdapter adapter;
    private List<BoardItem> boardItemList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Button writeBtn;


    @Override
    public void onTaskCompleted(String result) {
        // AsyncTask의 작업이 완료된 후 호출될 메서드
        Log.i("Result - BoardList", result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                String resultStatus = jsonObject.getString("result");

                // result 값이 success 일 때
                if ("success".equals(resultStatus)) {
                    JSONArray boardArray = jsonObject.getJSONArray("board");

                    for (int i = 0; i < boardArray.length(); i++) {
                        JSONObject boardObject = boardArray.getJSONObject(i);

                        String title = boardObject.getString("title");
                        String date = boardObject.getString("date");
                        String UserID = boardObject.getString("writer");
                        String content = boardObject.getString("content");

                        //date 형식 변환
                        String formattedDate = convertDateFormat(date);

                        // BoardItem 객체 생성
                        BoardItem boardItem = new BoardItem(title, formattedDate, UserID, content);

                        // RecyclerView에 추가
                        boardItemList.add(0, boardItem);
                        adapter.notifyItemInserted(0);
                        recyclerView.scrollToPosition(0);
                    }

                    Toast.makeText(getContext(), "게시글이 등록되었습니다!", Toast.LENGTH_SHORT).show();
                } else {
                    // 서버에서 실패 응답이 온 경우에 대한 처리
                    Log.e("server","서버 실패 응답");

                    Toast.makeText(getContext(), result.toString(), Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
                // JSON 파싱 오류 처리
            }
    }

    private String convertDateFormat(String originalDate) {
        // 원본 날짜 형식 지정
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss:SSS", Locale.getDefault());

        // 대중적인 날짜 형식으로 변환할 형식 지정
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

        try {
            // 원본 날짜를 Date 객체로 파싱
            Date date = originalFormat.parse(originalDate);

            // 대중적인 날짜 형식으로 포맷팅
            return targetFormat.format(date);
        } catch (ParseException | java.text.ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_board_list, container, false);

        recyclerView = view.findViewById(R.id.boardList_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        // 게시판 아이템 데이터를 초기화하고 어댑터에 연결
        boardItemList = new ArrayList<>();
        adapter = new BoardListAdapter(boardItemList);
        recyclerView.setAdapter(adapter);

        //게시글 등록 버튼
//        writeBtn = view.findViewById(R.id.writeBtn);
//        writeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), BoardRegister.class);
//                startActivity(intent);
//                //startActivityForResult(intent, REQUEST_IMAGE_UPLOAD);
//            }
//        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e("SWIPE","SWIPE!");
                // 새로고침 작업 수행
                initializeBoardItems();
            }
        });

        // 기존 데이터 로드
        initializeBoardItems();
        Log.i("test","BoardTest");

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 프래그먼트가 활성화될 때마다 데이터 초기화
        initializeBoardItems();
        Log.i("test", "onResume BoardTest");
    }

    private void initializeBoardItems() {
        // 게시판 아이템 데이터를 초기화
        // boardItemList에 필요한 데이터를 추가하고 adapter.notifyDataSetChanged()를 호출
        // 서버 연동
        String i = "asd";
        String p = "1234";

        JSONObject jsonParam = new JSONObject();

        try {
            jsonParam.put("ID", i);
            jsonParam.put("PW", p);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        // 토큰 파일이 이미 존재하는지 확인
        File file = new File(requireActivity().getFilesDir(), "token.txt");
        String token = "";
        // 파일 존재 여부 확인
        if (file.exists()) {
            // 파일이 존재할 경우의 처리
            try {
                // 파일 읽기
                BufferedReader reader = new BufferedReader(new FileReader(file));
                StringBuilder content = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }

                reader.close();
                // 읽은 내용 출력 또는 다른 처리 수행
                String fileContent = content.toString();
                token = fileContent;
                Log.i("File Content - Token: ", fileContent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Gson gson = new Gson();
        List<String> listA = new ArrayList<String>();
        listA.add(String.valueOf(jsonParam));
        listA.add("GET");
        listA.add("api/board/list");
        listA.add(token);

        String jsonWifiData = gson.toJson(listA); // converting wifiData to JSON format

        new SendDataTask(this).execute(jsonWifiData);

        adapter.notifyDataSetChanged();
    }

}


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(requestCode == REQUEST_IMAGE_UPLOAD && resultCode == RESULT_OK) {
//            if(data!=null){
//                String title = data.getStringExtra("title");
//                String content = data.getStringExtra("content");
//                Uri imageUri = null;
//                String uriString = data.getStringExtra("imageUri");
//                if (uriString != null) {
//                    imageUri = Uri.parse(uriString);
//                }
//
//                BoardItem newBoardItem;
//                if (imageUri != null) {
//                    newBoardItem = new BoardItem(title, content, imageUri);
//                } else {
//                    // 이미지가 없는 경우
//                    newBoardItem = BoardItem.createWithoutImage(title, content);
//                }
//
//                boardItemList.add(newBoardItem );
//                adapter.notifyItemInserted(boardItemList.size() - 1);
//                recyclerView.scrollToPosition(boardItemList.size() - 1);
//            }
//        }
//    }