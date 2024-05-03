package com.example.pet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 sliderViewPager;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sliderViewPager = findViewById(R.id.sliderViewPager);
        tabLayout = findViewById(R.id.tab_layout);

        ViewPager2Adapter viewPager2Adapter = new ViewPager2Adapter(this);
        sliderViewPager.setAdapter(viewPager2Adapter);

        new TabLayoutMediator(tabLayout, sliderViewPager,
                (tab, position) -> {
                    // 탭 이름 설정
                    switch (position) {
                        case 0:
                            tab.setIcon(R.drawable.camera_tab_idle);
                            break;
                        case 1:
                            tab.setIcon(R.drawable.home_tab_idle);
                            break;
                        case 2:
                            tab.setIcon(R.drawable.board_tab_idle);
                            break;
                        case 3:
                            tab.setIcon(R.drawable.user_tab_idle);
                            break;
                        // 추가적인 탭 이름 설정
                        default:
                            tab.setText(" " + (position + 1));
                            break;
                    }
                }
        ).attach();

        // TabLayout에 OnTabSelectedListener 등록
        tabLayout.addOnTabSelectedListener(new tabSelectedListener());

        // 초기 탭 색상 설정
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                if (i == tabLayout.getSelectedTabPosition()) {
                    // 선택된 탭의 색상 설정
                    tab.getIcon().setColorFilter(ContextCompat.getColor(tabLayout.getContext(), R.color.Ivory), PorterDuff.Mode.SRC_IN);
                } else {
                    // 선택되지 않은 탭의 색상 설정
                    tab.getIcon().setColorFilter(ContextCompat.getColor(tabLayout.getContext(), R.color.RobinEggBlue), PorterDuff.Mode.SRC_IN);
                }
            }
        }

        // 홈 화면을 초기 화면으로 설정
        sliderViewPager.setCurrentItem(1);

    }

    // 선택한 탭 아이콘의 색을 바꾸는 클래스
    public class tabSelectedListener implements TabLayout.OnTabSelectedListener {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            // 선택된 탭의 색상 변경
            tab.getIcon().setColorFilter(ContextCompat.getColor(tabLayout.getContext(), R.color.RobinEggBlue), PorterDuff.Mode.SRC_IN);
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            // 선택되지 않은 탭의 색상 변경
            tab.getIcon().setColorFilter(ContextCompat.getColor(tabLayout.getContext(), R.color.Ivory), PorterDuff.Mode.SRC_IN);
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            // 재선택된 탭에 대한 동작 (선택되었을 때와 동일하게 처리해도 됨)
            tab.getIcon().setColorFilter(ContextCompat.getColor(tabLayout.getContext(), R.color.RobinEggBlue), PorterDuff.Mode.SRC_IN);
        }
    }
}