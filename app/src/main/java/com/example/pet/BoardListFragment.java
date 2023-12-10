package com.example.pet;

import static android.app.Activity.RESULT_OK;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BoardListFragment extends Fragment {

    private RecyclerView recyclerView;
    private BoardListAdapter adapter;
    private List<BoardItem> boardItemList;
    private Button writeBtn;

    private static final int REQUEST_IMAGE_UPLOAD = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_board_list, container, false);

        recyclerView = view.findViewById(R.id.boardList_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // 게시판 아이템 데이터를 초기화하고 어댑터에 연결
        boardItemList = new ArrayList<>();
        adapter = new BoardListAdapter(boardItemList);
        recyclerView.setAdapter(adapter);

        //게시글 등록 버튼
        writeBtn = view.findViewById(R.id.writeBtn);
        writeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("test","test");

                Intent intent = new Intent(getActivity(), BoardRegister.class);
                startActivityForResult(intent, REQUEST_IMAGE_UPLOAD);
            }
        });

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

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_IMAGE_UPLOAD && resultCode == RESULT_OK) {
            if(data!=null){
                String title = data.getStringExtra("title");
                String content = data.getStringExtra("content");
                String userID = data.getStringExtra("")

                Uri imageUri = null;
                String uriString = data.getStringExtra("imageUri");
                if (uriString != null) {
                    imageUri = Uri.parse(uriString);
                }

                BoardItem newBoardItem;
                if (imageUri != null) {
                    newBoardItem = new BoardItem(title,userID, date, content, imageUri);
                } else {
                    // 이미지가 없는 경우
                    newBoardItem = BoardItem.createWithoutImage(title, content);
                }

                boardItemList.add(newBoardItem );
                adapter.notifyItemInserted(boardItemList.size() - 1);
                recyclerView.scrollToPosition(boardItemList.size() - 1);
            }
        }
    }

    private void initializeBoardItems() {

        // 게시판 아이템 데이터를 초기화
        // boardItemList에 필요한 데이터를 추가하고 adapter.notifyDataSetChanged()를 호출
        // 서버 연동
//        String i = "asd";
//        String p = "1234";
//
//        JSONObject jsonParam = new JSONObject();
//
//        try {
//            jsonParam.put("ID", i);
//            jsonParam.put("PW", p);
//        } catch (JSONException e) {
//            throw new RuntimeException(e);
//        }
//
//        // 토큰 파일이 이미 존재하는지 확인
//        File file = new File(requireActivity().getFilesDir(), "token.txt");
//        String token = "";
//        // 파일 존재 여부 확인
//        if (file.exists()) {
//            // 파일이 존재할 경우의 처리
//            try {
//                // 파일 읽기
//                BufferedReader reader = new BufferedReader(new FileReader(file));
//                StringBuilder content = new StringBuilder();
//                String line;
//
//                while ((line = reader.readLine()) != null) {
//                    content.append(line);
//                }
//
//                reader.close();
//                // 읽은 내용 출력 또는 다른 처리 수행
//                String fileContent = content.toString();
//                token = fileContent;
//                Log.i("File Content - Token: ", fileContent);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        Gson gson = new Gson();
//        List<String> listA = new ArrayList<String>();
//        listA.add(String.valueOf(jsonParam));
//        listA.add("GET");
//        listA.add("api/board/list");
//        listA.add(token);
//
//        String jsonWifiData = gson.toJson(listA); // converting wifiData to JSON format
//
//        new SendDataTask().execute(jsonWifiData);
//
//
//        adapter.notifyDataSetChanged();
    }
}