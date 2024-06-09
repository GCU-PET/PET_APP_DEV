package com.example.pet;

import android.util.Log;

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
    private List<String> last5Days;

    public ViewPager2TimeLineAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        last5Days = getLast5Days();
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // 각 탭에 보여질 Fragment 생성
        FragmentTimeLine fragment = new FragmentTimeLine();
        fragment.setLogDate(last5Days.get(position));
        Log.e("TimeLine", "Timeline Fragment " + position + " generate with date: " + last5Days.get(position));
        return fragment;

//        switch (position) {
//            case 0:
//                FragmentTimeLine f0 = new FragmentTimeLine();
//                f0.setLogDate(last5Days.get(0));
//                Log.e("TimeLine","Timeline f0 Fragment generate");
//                return f0;
//            case 1:
//                FragmentTimeLine f1 = new FragmentTimeLine();
//                f1.setLogDate(last5Days.get(1));
//                Log.e("TimeLine","Timeline f1 Fragment generate");
//                return f1;
//            case 2:
//                FragmentTimeLine f2 = new FragmentTimeLine();
//                f2.setLogDate(last5Days.get(2));
//                Log.e("TimeLine","Timeline f2 Fragment generate");
//                return f2;
//            case 3:
//                FragmentTimeLine f3 = new FragmentTimeLine();
//                f3.setLogDate(last5Days.get(3));
//                Log.e("TimeLine","Timeline f3 Fragment generate");
//                return f3;
//            case 4:
//                FragmentTimeLine f4 = new FragmentTimeLine();
//                f4.setLogDate(last5Days.get(4));
//                Log.e("TimeLine","Timeline f4 Fragment generate");
//                return f4;
//            case 5:
//                FragmentTimeLine f5 = new FragmentTimeLine();
//                f5.setLogDate(last5Days.get(5));
//                Log.e("TimeLine","Timeline f5 Fragment generate");
//                return f5;
//            default:
//                return null;
//        }
    }



    private static List<String> getLast5Days() {
        List<String> dateList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();

        // 현재 날짜부터 이전 5일까지의 날짜를 리스트에 추가
        for (int i = 0; i < 6; i++) {
            dateList.add(0, sdf.format(calendar.getTime())); // 최근 날짜를 마지막에 추가
            calendar.add(Calendar.DAY_OF_YEAR, -1);
        }

        return dateList;
    }

    @Override
    public int getItemCount() {
        return last5Days.size();
    }


    public String getDate(int position) {
        return last5Days.get(position);
    }
}
