package com.example.pet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class FragmentUser extends Fragment {

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

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
