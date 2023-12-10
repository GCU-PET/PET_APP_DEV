package com.example.pet;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FragmentUser extends Fragment implements OnTaskCompleted {

    public ImageButton editButton;
    public ImageView imagePet;
    private static final int PICK_IMAGE_REQUEST = 1;
    private RecyclerView recyclerView;
    private BoardListAdapter adapter;
    private List<BoardItem> boardItemList;

    @Override
    public void onTaskCompleted(String result) {
        // AsyncTask의 작업이 완료된 후 호출될 메서드
        Log.i("Result1231231", result);

        try {
            JSONObject jsonObject = new JSONObject(result);
            String resultStatus = jsonObject.getString("result");

            if ("success".equals(resultStatus)) {
                JSONArray boardArray = jsonObject.getJSONArray("board");

                for (int i = 0; i < boardArray.length(); i++) {
                    JSONObject boardObject = boardArray.getJSONObject(i);

                    String title = boardObject.getString("title");
                    String date = boardObject.getString("date");
                    String userID = boardObject.getString("writer");
                    String content = boardObject.getString("content");

                    // BoardItem 객체 생성
                    BoardItem boardItem = new BoardItem(title, userID, date, content);

                    // RecyclerView에 추가
                    boardItemList.add(boardItem);
                    adapter.notifyItemInserted(boardItemList.size() - 1);
                    recyclerView.scrollToPosition(boardItemList.size() - 1);
                }
            } else {
                // 서버에서 실패 응답이 온 경우에 대한 처리
                Log.e("error", "서버 응답 실패");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // JSON 파싱 오류 처리
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        // 이곳에 BoardListFragment를 불러옴.
        // 1. FragmentTransaction 시작
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        // 2. 추가할 BoardListFragment 생성
        BoardListFragment boardListFragment = new BoardListFragment();

        // 3. FrameLayout에 BoardListFragment 추가
        transaction.replace(R.id.board_container, boardListFragment);

        // 4. FragmentTransaction을 커밋하여 변경 사항 적용
        transaction.commit();

        editButton = view.findViewById(R.id.userInfo_edit_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserInfoSettingActivity.class);
                startActivity(intent);
            }
        });

        imagePet = view.findViewById(R.id.image_pet);


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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                ImageButton imageButton = getView().findViewById(R.id.userInfo_edit_button);
                imageButton.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}