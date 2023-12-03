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

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_IMAGE_UPLOAD && resultCode == RESULT_OK) {
            if(data!=null){
                String title = data.getStringExtra("title");
                String content = data.getStringExtra("content");

                Uri imageUri = null;
                String uriString = data.getStringExtra("imageUri");
                if (uriString != null) {
                    imageUri = Uri.parse(uriString);
                }

                BoardItem newBoardItem;
                if (imageUri != null) {
                    newBoardItem = new BoardItem(title, content, imageUri);
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
    }
}