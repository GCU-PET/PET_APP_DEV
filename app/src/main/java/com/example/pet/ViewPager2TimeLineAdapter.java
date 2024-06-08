package com.example.pet;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ViewPager2TimeLineAdapter extends FragmentStateAdapter {

    public ViewPager2TimeLineAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        List<String> last5Days = getLast5Days();
        // 각 탭에 보여질 Fragment 생성
        switch (position) {
            case 0:
                FragmentTimeLine f0 = new FragmentTimeLine();
                f0.setLogDate(last5Days.get(0));
                return f0;
            case 1:
                FragmentTimeLine f1 = new FragmentTimeLine();
                f1.setLogDate(last5Days.get(1));
                return f1;
            case 2:
                FragmentTimeLine f2 = new FragmentTimeLine();
                f2.setLogDate(last5Days.get(2));
                return f2;
            case 3:
                FragmentTimeLine f3 = new FragmentTimeLine();
                f3.setLogDate(last5Days.get(3));
                return f3;
            case 4:
                FragmentTimeLine f4 = new FragmentTimeLine();
                f4.setLogDate(last5Days.get(4));
                return f4;
            case 5:
                FragmentTimeLine f5 = new FragmentTimeLine();
                f5.setLogDate(last5Days.get(5));
                return f5;
            default:
                return null;
        }
    }

    private static List<String> getLast5Days() {
        List<String> dateList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();

        // 현재 날짜부터 이전 5일까지의 날짜를 리스트에 추가
        for (int i = 0; i < 5; i++) {
            dateList.add(sdf.format(calendar.getTime()));
            calendar.add(Calendar.DAY_OF_YEAR, -1);
        }

        return dateList;
    }

    @Override
    public int getItemCount() {
        return 6;
    }


}
