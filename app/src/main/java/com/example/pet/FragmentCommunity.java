package com.example.pet;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentCommunity extends Fragment {

    private RecyclerView recyclerView;
    private BoardListAdapter adapter;
    private List<BoardItem> boardItemList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageButton writeBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community, container, false);

        recyclerView = view.findViewById(R.id.boardList_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        writeBtn = view.findViewById(R.id.writeBtn);

        // 게시판 아이템 데이터를 초기화하고 어댑터에 연결
        boardItemList = new ArrayList<>();
        adapter = new BoardListAdapter(boardItemList, getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        writeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BoardRegister.class);
                startActivity(intent);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e("SWIPE","새로고침!");
                // 새로고침 작업 수행
                initializeBoardItems();
            }
        });

        // 기존 데이터 로드
        initializeBoardItems();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    // 아이템 불러오기
    private void initializeBoardItems(){
        BoardServiceApi service = RetrofitClient.getBoardServiceApi();
        Call<List<BoardItem>> call = service.getBoardList();

        call.enqueue(new Callback<List<BoardItem>>() {
            @Override
            public void onResponse(Call<List<BoardItem>> call, Response<List<BoardItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    boardItemList.clear();
                    boardItemList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    Log.e(TAG, "Request Error :: " + response.errorBody());
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<List<BoardItem>> call, Throwable t) {
                t.printStackTrace();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQEUST_BOARD_REGISTER && resultCode == Activity.RESULT_OK){
//            // 게시글 작성 액티비티에서 작성된 내용을 받아옴
//            if (data != null) {
//                String title = data.getStringExtra("title");
//                String content = data.getStringExtra("content");
//
////                // 받아온 내용을 리스트에 추가
////                BoardItem newBoardItem = new BoardItem(title, content);
////                boardItemList.add(newBoardItem);
////                adapter.notifyItemInserted(boardItemList.size() - 1);
//            }
//        }
//    }
}