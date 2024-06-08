package com.example.pet;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentHome extends Fragment {

    private CircleImageView profile;

    private TabLayout timeline_tab;
    private ViewPager2 timeline_slider;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
    private SimpleDateFormat monthFormat = new SimpleDateFormat("MM");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        profile = view.findViewById(R.id.home_profile);

        String imagePath = getImagePath();
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            profile.setImageBitmap(bitmap);
        }

        //현재 날짜 얻기
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        // 5일 전 날짜 계산
        calendar.add(Calendar.DAY_OF_MONTH, -5);

        timeline_slider = view.findViewById(R.id.timeline_sliderViewPager);
        timeline_tab = view.findViewById(R.id.date_tab);

        ViewPager2TimeLineAdapter viewPager2TimeLineAdapter = new ViewPager2TimeLineAdapter(getActivity());
        timeline_slider.setAdapter(viewPager2TimeLineAdapter);

        //탭에 날짜 추가
        for (int i = 0; i < 6; i++) {
            TabLayout.Tab tab = timeline_tab.newTab();
            tab.setCustomView(R.layout.timelinetab_layout);
            TextView tabMonth = tab.getCustomView().findViewById(R.id.timelinetab_month);
            TextView tabDate = tab.getCustomView().findViewById(R.id.timelinetab_date);

            // 날짜 설정
            tabMonth.setText(monthFormat.format(calendar.getTime()));
            tabDate.setText(dateFormat.format(calendar.getTime()));

            // 다음 날짜 계산
            calendar.add(Calendar.DAY_OF_MONTH, 1);

            timeline_tab.addTab(tab);
        }

        // TabLayout에 OnTabSelectedListener 등록
        timeline_tab.addOnTabSelectedListener(new tabSelectedListener());

        // 초기 탭 선택
        timeline_tab.selectTab(timeline_tab.getTabAt(5), true);

        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        String imagePath = getImagePath();
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            profile.setImageBitmap(bitmap);
        }
    }

    private String getImagePath() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("profileImagePath", null);  // 기본값으로 null 설정
    }

    // 선택한 탭 아이콘의 색을 바꾸는 클래스
    public class tabSelectedListener implements TabLayout.OnTabSelectedListener {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            // 선택된 탭의 색상 변경
            View tabView = tab.getCustomView();
            if (tabView != null){
                // 배경 drawable의 속성을 변경합니다.
                tabView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(tabView.getContext(), R.color.Bittersweet)));
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            // 선택되지 않은 탭의 색상 변경
            View tabView = tab.getCustomView();
            if (tabView != null){
                // 배경 drawable의 속성을 변경합니다.
                tabView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(tabView.getContext(), R.color.Ivory)));
            }
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            // 재선택된 탭에 대한 동작 (선택되었을 때와 동일하게 처리해도 됨)
            View tabView = tab.getCustomView();
            if (tabView != null){
                // 배경 drawable의 속성을 변경합니다.
                tabView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(tabView.getContext(), R.color.Bittersweet)));
            }
        }
    }
}
