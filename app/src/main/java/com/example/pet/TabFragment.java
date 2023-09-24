package com.example.pet;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TabFragment extends Fragment {

    private static final String ARG_TAB_INDEX = "tab_index";

    public TabFragment() {
        // Required empty public constructor
    }

    public static TabFragment newInstance(int tabIndex) {
        TabFragment fragment = new TabFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TAB_INDEX, tabIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 탭 인덱스
        int tabIndex = getArguments().getInt(ARG_TAB_INDEX);

//        // fragment manager 선언 및 transaction 시작
//        FragmentManager fragmentManager = getChildFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//        // 기존 프래그먼트를 제거 (있으면)
//        Fragment currentFragment = fragmentManager.findFragmentById(R.id.board_container);
//        if (currentFragment != null) {
//            fragmentTransaction.remove(currentFragment);
//        }
//
//        // tabIndex가 2일 때 (user 탭일 때) 프래그먼트 추가
//        if (tabIndex == 2) {
//            Fragment newFragment = new BoardFragment(); // BoardFragment는 예시입니다.
//            fragmentTransaction.add(R.id.board_container, newFragment);
//        }
//
//        fragmentTransaction.commit();

        View view;
        // 탭에 따른 레이아웃 처리 추가
        switch (tabIndex) {
            case 0:
                view = inflater.inflate(R.layout.fragment_camera, container, false);
                break;
            case 2:
                view = inflater.inflate(R.layout.fragment_user, container, false);
                break;
            default:
                view = inflater.inflate(R.layout.fragment_home, container, false);
                break;
        }
        return view;
    }
}
