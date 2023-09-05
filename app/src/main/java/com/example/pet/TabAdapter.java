package com.example.pet;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class TabAdapter extends FragmentStateAdapter {

    private final int numTabs; // 탭의 개수

    public TabAdapter(@NonNull FragmentActivity fragmentActivity, int numTabs) {
        super(fragmentActivity);
        this.numTabs = numTabs;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // 각 탭에 보여질 Fragment 생성
        return TabFragment.newInstance(position);
    }

    @Override
    public int getItemCount() {
        return numTabs;
    }
}