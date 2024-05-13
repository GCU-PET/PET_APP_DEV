package com.example.pet;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPager2TimeLineAdapter extends FragmentStateAdapter {

    public ViewPager2TimeLineAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // 각 탭에 보여질 Fragment 생성
        switch (position) {
            case 0:
                return new FragmentTimeLine();
            case 1:
                return new FragmentTimeLine();
            case 2:
                return new FragmentTimeLine();
            case 3:
                return new FragmentTimeLine();
            case 4:
                return new FragmentTimeLine();
            case 5:
                return new FragmentTimeLine();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
