package com.example.pet;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPager2Adapter extends FragmentStateAdapter {

    public ViewPager2Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // 각 탭에 보여질 Fragment 생성
        switch (position) {
            case 0:
                return new FragmentCamera();
            case 1:
                return new FragmentHome();
            case 2:
                return new FragmentCommunity();
            case 3:
                return new FragmentUserInfoSetting();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}