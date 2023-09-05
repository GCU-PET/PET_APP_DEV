package com.example.pet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 sliderViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sliderViewPager = findViewById(R.id.sliderViewPager);
        tabLayout = findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab().setText("카메라"));
        tabLayout.addTab(tabLayout.newTab().setText("홈"));
        tabLayout.addTab(tabLayout.newTab().setText("게시판"));

        TabAdapter tabAdapter = new TabAdapter(this,3);
        sliderViewPager.setAdapter(tabAdapter);

        new TabLayoutMediator(tabLayout, sliderViewPager,
                (tab, position) -> {
                    // 탭 이름 설정
                    switch (position) {
                        case 0:
                            tab.setText("카메라");
                            break;
                        case 1:
                            tab.setText("홈");
                            break;
                        case 2:
                            tab.setText("게시판");
                            break;
                        // 추가적인 탭 이름 설정
                        default:
                            tab.setText("기탭 " + (position + 1));
                            break;
                    }
                }
        ).attach();

        // 홈 화면을 초기 화면으로 설정
        sliderViewPager.setCurrentItem(1);
    }
}