package com.example.pet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
                startActivity(intent);
            }
        });

        return view;
    }

    private void initializeBoardItems() {
        // 게시판 아이템 데이터를 초기화
        // boardItemList에 필요한 데이터를 추가하고 adapter.notifyDataSetChanged()를 호출
    }
}